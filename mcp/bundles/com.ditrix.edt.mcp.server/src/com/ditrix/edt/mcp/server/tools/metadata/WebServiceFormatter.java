/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.metadata.mdclass.Operation;
import com._1c.g5.v8.dt.metadata.mdclass.Parameter;
import com._1c.g5.v8.dt.metadata.mdclass.WebService;

/**
 * Formatter for WebService metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class WebServiceFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "WebService"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof WebService;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof WebService))
        {
            return "Error: Expected WebService object"; //$NON-NLS-1$
        }
        
        WebService ws = (WebService) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, ws.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, ws, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, ws, language);
            formatKeyProperties(sb, ws, language);
        }
        
        // Special sections
        formatOperations(sb, ws, full, language);
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, WebService ws, String language)
    {
        if (ws.getNamespace() != null && !ws.getNamespace().isEmpty())
        {
            addPropertyRow(sb, "Namespace", ws.getNamespace()); //$NON-NLS-1$
        }
        if (ws.getReuseSessions() != null)
        {
            addPropertyRow(sb, "Reuse Sessions", formatEnum(ws.getReuseSessions())); //$NON-NLS-1$
        }
    }
    
    private void formatOperations(StringBuilder sb, WebService ws, boolean full, String language)
    {
        if (ws.getOperations() == null || ws.getOperations().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Operations"); //$NON-NLS-1$
        
        for (Operation op : ws.getOperations())
        {
            sb.append("\n**").append(op.getName()).append("**"); //$NON-NLS-1$ //$NON-NLS-2$
            String synonym = getSynonym(op.getSynonym(), language);
            if (synonym != null && !synonym.isEmpty())
            {
                sb.append(" (").append(synonym).append(")"); //$NON-NLS-1$ //$NON-NLS-2$
            }
            sb.append("\n\n"); //$NON-NLS-1$
            
            // Return value type
            if (op.getXdtoReturningValueType() != null)
            {
                sb.append("- Return Type: ").append(formatXdtoType(op.getXdtoReturningValueType())).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
            }
            sb.append("- Nillable: ").append(formatBoolean(op.isNillable())).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
            sb.append("- Transactioned: ").append(formatBoolean(op.isTransactioned())).append("\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
            
            // Parameters
            if (op.getParameters() != null && !op.getParameters().isEmpty())
            {
                startTable(sb, "Parameter", "Type", "Nillable", "Transfer Direction"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                
                for (Parameter param : op.getParameters())
                {
                    addTableRow(sb,
                        param.getName(),
                        formatXdtoType(param.getXdtoValueType()),
                        formatBoolean(param.isNillable()),
                        formatEnum(param.getTransferDirection()));
                }
            }
        }
    }
    
    private String formatXdtoType(Object type)
    {
        if (type == null)
        {
            return DASH;
        }
        return type.toString();
    }
}
