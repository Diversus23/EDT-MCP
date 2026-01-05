/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.Constant;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;

/**
 * Formatter for Constant metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class ConstantFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "Constant"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof Constant;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof Constant))
        {
            return "Error: Expected Constant object"; //$NON-NLS-1$
        }
        
        Constant constant = (Constant) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, constant.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, constant, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, constant, language);
            formatKeyProperties(sb, constant, language);
        }
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, Constant constant, String language)
    {
        addPropertyRow(sb, "Type", formatType(constant.getType())); //$NON-NLS-1$
    }
}
