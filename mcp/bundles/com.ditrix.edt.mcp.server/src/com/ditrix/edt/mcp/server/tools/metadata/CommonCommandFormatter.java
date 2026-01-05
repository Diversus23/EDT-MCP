/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.CommonCommand;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;

/**
 * Formatter for CommonCommand metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class CommonCommandFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "CommonCommand"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof CommonCommand;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof CommonCommand))
        {
            return "Error: Expected CommonCommand object"; //$NON-NLS-1$
        }
        
        CommonCommand cc = (CommonCommand) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, cc.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, cc, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, cc, language);
            formatKeyProperties(sb, cc);
        }
        
        return sb.toString();
    }
    
    /**
     * Format key properties for basic mode (backward compatible).
     */
    private void formatKeyProperties(StringBuilder sb, CommonCommand cc)
    {
        if (cc.getGroup() != null)
        {
            addPropertyRow(sb, "Group", cc.getGroup().toString()); //$NON-NLS-1$
        }
        if (cc.getRepresentation() != null)
        {
            addPropertyRow(sb, "Representation", formatEnum(cc.getRepresentation())); //$NON-NLS-1$
        }
        if (cc.getCommandParameterType() != null)
        {
            addPropertyRow(sb, "Command Parameter Type", formatType(cc.getCommandParameterType())); //$NON-NLS-1$
        }
        addPropertyRow(sb, "Modifies Data", cc.isModifiesData()); //$NON-NLS-1$
    }
}
