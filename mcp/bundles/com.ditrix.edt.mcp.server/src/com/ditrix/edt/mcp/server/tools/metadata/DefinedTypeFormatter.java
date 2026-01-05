/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.DefinedType;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;

/**
 * Formatter for DefinedType metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class DefinedTypeFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "DefinedType"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof DefinedType;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof DefinedType))
        {
            return "Error: Expected DefinedType object"; //$NON-NLS-1$
        }
        
        DefinedType dt = (DefinedType) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, dt.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, dt, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, dt, language);
            formatKeyProperties(sb, dt, language);
        }
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, DefinedType dt, String language)
    {
        addPropertyRow(sb, "Type", formatType(dt.getType())); //$NON-NLS-1$
    }
}
