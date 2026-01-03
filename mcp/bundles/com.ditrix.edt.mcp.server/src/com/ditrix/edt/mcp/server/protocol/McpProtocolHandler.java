/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.protocol;

import java.util.HashMap;
import java.util.Map;

import com.ditrix.edt.mcp.server.Activator;
import com.ditrix.edt.mcp.server.tools.IMcpTool;
import com.ditrix.edt.mcp.server.tools.McpToolRegistry;

/**
 * Handles MCP JSON-RPC protocol messages.
 * Supports Streamable HTTP transport as per MCP 2025-03-26 specification.
 */
public class McpProtocolHandler
{
    private final McpToolRegistry toolRegistry;
    
    /**
     * Creates a new protocol handler.
     */
    public McpProtocolHandler()
    {
        this.toolRegistry = McpToolRegistry.getInstance();
    }
    
    /**
     * Processes an MCP JSON-RPC request.
     * 
     * @param requestBody the JSON request body
     * @return JSON response with correct id from request
     */
    public String processRequest(String requestBody)
    {
        // Extract the request id for use in response
        String requestId = JsonUtils.extractRequestId(requestBody);
        
        try
        {
            // Check for initialize method
            if (JsonUtils.hasMethod(requestBody, McpConstants.METHOD_INITIALIZE))
            {
                return buildInitializeResponse(requestId);
            }
            
            // Check for initialized notification (no response needed, but return 202)
            if (JsonUtils.hasMethod(requestBody, McpConstants.METHOD_INITIALIZED))
            {
                return null; // Signal for 202 Accepted with no body
            }
            
            // Check for tools/list method
            if (JsonUtils.hasMethod(requestBody, McpConstants.METHOD_TOOLS_LIST))
            {
                return buildToolsListResponse(requestId);
            }
            
            // Check for tools/call method
            if (JsonUtils.hasMethod(requestBody, McpConstants.METHOD_TOOLS_CALL))
            {
                return handleToolCall(requestBody, requestId);
            }
            
            // Method not found
            return buildErrorResponse(McpConstants.ERROR_METHOD_NOT_FOUND, "Method not found", requestId); //$NON-NLS-1$
        }
        catch (Exception e)
        {
            Activator.logError("Error processing MCP request", e); //$NON-NLS-1$
            return buildErrorResponse(McpConstants.ERROR_INTERNAL, e.getMessage(), requestId);
        }
    }
    
    /**
     * Handles a tools/call request.
     */
    private String handleToolCall(String requestBody, String requestId)
    {
        // Find which tool is being called
        for (IMcpTool tool : toolRegistry.getAllTools())
        {
            if (JsonUtils.hasToolCall(requestBody, tool.getName()))
            {
                Activator.logInfo("Processing tools/call: " + tool.getName()); //$NON-NLS-1$
                
                // Extract parameters
                Map<String, String> params = extractToolParams(requestBody, tool);
                
                // Execute tool
                String result = tool.execute(params);
                
                // Return response based on tool's declared response type
                switch (tool.getResponseType())
                {
                    case JSON:
                        return buildToolCallJsonResponse(result, requestId);
                    case MARKDOWN:
                        String fileName = tool.getResultFileName(params);
                        return buildToolCallResourceResponse(result, "text/markdown", fileName, requestId); //$NON-NLS-1$
                    case TEXT:
                    default:
                        return buildToolCallTextResponse(result, requestId);
                }
            }
        }
        
        // Tool not found
        return buildErrorResponse(McpConstants.ERROR_METHOD_NOT_FOUND, "Tool not found", requestId); //$NON-NLS-1$
    }
    
    /**
     * Extracts tool parameters from request.
     */
    private Map<String, String> extractToolParams(String requestBody, IMcpTool tool)
    {
        Map<String, String> params = new HashMap<>();
        
        // Extract all common parameters
        String[] paramNames = {
            "projectName", "typeName", "errorType", "severity", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            "checkId", "filePath", "priority", "limit", "clean" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        };
        
        for (String paramName : paramNames)
        {
            String value = JsonUtils.extractStringArgument(requestBody, paramName);
            if (value != null)
            {
                params.put(paramName, value);
            }
        }
        
        return params;
    }
    
    /**
     * Builds initialize response.
     */
    private String buildInitializeResponse(String requestId)
    {
        return "{\"jsonrpc\": \"2.0\", \"result\": {" + //$NON-NLS-1$
            "\"protocolVersion\": \"" + McpConstants.PROTOCOL_VERSION + "\"," + //$NON-NLS-1$ //$NON-NLS-2$
            "\"capabilities\": {\"tools\": {}}," + //$NON-NLS-1$
            "\"serverInfo\": {\"name\": \"" + McpConstants.SERVER_NAME + "\", " + //$NON-NLS-1$ //$NON-NLS-2$
            "\"version\": \"" + McpConstants.PLUGIN_VERSION + "\", " + //$NON-NLS-1$ //$NON-NLS-2$
            "\"author\": \"" + McpConstants.AUTHOR + "\"}" + //$NON-NLS-1$ //$NON-NLS-2$
            "}, \"id\": " + requestId + "}"; //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Builds tools/list response dynamically from registry.
     */
    private String buildToolsListResponse(String requestId)
    {
        StringBuilder json = new StringBuilder();
        json.append("{\"jsonrpc\": \"2.0\", \"result\": {\"tools\": ["); //$NON-NLS-1$
        
        boolean first = true;
        for (IMcpTool tool : toolRegistry.getAllTools())
        {
            if (!first)
            {
                json.append(","); //$NON-NLS-1$
            }
            first = false;
            
            json.append("{"); //$NON-NLS-1$
            json.append("\"name\": \"").append(JsonUtils.escapeJson(tool.getName())).append("\", "); //$NON-NLS-1$ //$NON-NLS-2$
            json.append("\"description\": \"").append(JsonUtils.escapeJson(tool.getDescription())).append("\", "); //$NON-NLS-1$ //$NON-NLS-2$
            json.append("\"inputSchema\": ").append(tool.getInputSchema()); //$NON-NLS-1$
            json.append("}"); //$NON-NLS-1$
        }
        
        json.append("]}, \"id\": ").append(requestId).append("}"); //$NON-NLS-1$ //$NON-NLS-2$
        return json.toString();
    }
    
    /**
     * Builds tool call response for text result.
     */
    private String buildToolCallTextResponse(String result, String requestId)
    {
        return "{\"jsonrpc\": \"2.0\", \"result\": {" + //$NON-NLS-1$
            "\"content\": [{\"type\": \"text\", \"text\": \"" + JsonUtils.escapeJson(result) + "\"}]" + //$NON-NLS-1$ //$NON-NLS-2$
            "}, \"id\": " + requestId + "}"; //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Builds tool call response for JSON result.
     * Includes both text (escaped JSON) and structuredContent (raw JSON) per MCP 2025-11-25.
     */
    private String buildToolCallJsonResponse(String jsonResult, String requestId)
    {
        // MCP 2025-11-25 spec: return both text (escaped JSON) and structuredContent (raw JSON)
        return "{\"jsonrpc\": \"2.0\", \"result\": {" + //$NON-NLS-1$
            "\"content\": [{\"type\": \"text\", \"text\": \"" + JsonUtils.escapeJson(jsonResult) + "\"}]," + //$NON-NLS-1$ //$NON-NLS-2$
            "\"structuredContent\": " + jsonResult + //$NON-NLS-1$
            "}, \"id\": " + requestId + "}"; //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Builds tool call response for resource with MIME type (e.g., Markdown).
     * Uses EmbeddedResource with mimeType for proper content type handling.
     * 
     * @param content the content text
     * @param mimeType the MIME type (e.g., "text/markdown")
     * @param fileName the file name with extension (e.g., "get_project_errors.md")
     * @param requestId the JSON-RPC request ID
     * @return the JSON-RPC response
     */
    private String buildToolCallResourceResponse(String content, String mimeType, String fileName, String requestId)
    {
        // EmbeddedResource per MCP spec - allows specifying mimeType for content
        return "{\"jsonrpc\": \"2.0\", \"result\": {" + //$NON-NLS-1$
            "\"content\": [{" + //$NON-NLS-1$
            "\"type\": \"resource\"," + //$NON-NLS-1$
            "\"resource\": {" + //$NON-NLS-1$
            "\"uri\": \"embedded://" + fileName + "\"," + //$NON-NLS-1$ //$NON-NLS-2$
            "\"mimeType\": \"" + mimeType + "\"," + //$NON-NLS-1$ //$NON-NLS-2$
            "\"text\": \"" + JsonUtils.escapeJson(content) + "\"" + //$NON-NLS-1$ //$NON-NLS-2$
            "}" + //$NON-NLS-1$
            "}]" + //$NON-NLS-1$
            "}, \"id\": " + requestId + "}"; //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Builds error response.
     */
    private String buildErrorResponse(int code, String message, String requestId)
    {
        return "{\"jsonrpc\": \"2.0\", \"error\": {\"code\": " + code + //$NON-NLS-1$
            ", \"message\": \"" + JsonUtils.escapeJson(message) + "\"}, \"id\": " + requestId + "}"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
