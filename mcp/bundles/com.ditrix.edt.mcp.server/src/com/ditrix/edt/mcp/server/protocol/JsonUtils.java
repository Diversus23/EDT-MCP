/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * JSON utility methods.
 */
public final class JsonUtils
{
    private JsonUtils()
    {
        // Utility class
    }
    
    /**
     * Escapes special characters for JSON string.
     * Note: Prefer using Gson for JSON serialization.
     * This method is kept for legacy compatibility.
     * 
     * @param s input string
     * @return escaped string
     */
    public static String escapeJson(String s)
    {
        if (s == null)
        {
            return ""; //$NON-NLS-1$
        }
        return s.replace("\\", "\\\\") //$NON-NLS-1$ //$NON-NLS-2$
                .replace("\"", "\\\"") //$NON-NLS-1$ //$NON-NLS-2$
                .replace("\n", "\\n") //$NON-NLS-1$ //$NON-NLS-2$
                .replace("\r", "\\r") //$NON-NLS-1$ //$NON-NLS-2$
                .replace("\t", "\\t"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Extracts a string argument from params map.
     * 
     * @param params the params map
     * @param argumentName the argument name to extract
     * @return value or null if not found
     */
    public static String extractStringArgument(Map<String, String> params, String argumentName)
    {
        if (params == null || argumentName == null)
        {
            return null;
        }
        return params.get(argumentName);
    }
    
    /**
     * Extracts an array argument from params map.
     * The value can be a JSON array string like ["a", "b"] or a comma-separated string.
     * 
     * @param params the params map
     * @param argumentName the argument name to extract
     * @return list of strings or null if not found
     */
    public static List<String> extractArrayArgument(Map<String, String> params, String argumentName)
    {
        if (params == null || argumentName == null)
        {
            return null;
        }
        
        String value = params.get(argumentName);
        if (value == null || value.isEmpty())
        {
            return null;
        }
        
        value = value.trim();
        
        // Check if it's a JSON array
        if (value.startsWith("[")) //$NON-NLS-1$
        {
            try
            {
                JsonElement element = JsonParser.parseString(value);
                if (element.isJsonArray())
                {
                    JsonArray array = element.getAsJsonArray();
                    List<String> result = new ArrayList<>(array.size());
                    for (JsonElement el : array)
                    {
                        if (el.isJsonPrimitive())
                        {
                            result.add(el.getAsString());
                        }
                    }
                    return result;
                }
            }
            catch (Exception e)
            {
                // Fall through to comma-separated parsing
            }
        }
        
        // Parse as comma-separated
        List<String> result = new ArrayList<>();
        for (String part : value.split(",")) //$NON-NLS-1$
        {
            String trimmed = part.trim();
            if (!trimmed.isEmpty())
            {
                result.add(trimmed);
            }
        }
        
        return result.isEmpty() ? null : result;
    }
}
