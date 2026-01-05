/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.metadata.mdclass.SessionParameter;

/**
 * Formatter for SessionParameter metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class SessionParameterFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "SessionParameter"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof SessionParameter;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof SessionParameter))
        {
            return "Error: Expected SessionParameter object"; //$NON-NLS-1$
        }
        
        SessionParameter sp = (SessionParameter) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, sp.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, sp, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, sp, language);
            formatKeyProperties(sb, sp, language);
        }
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, SessionParameter sp, String language)
    {
        addPropertyRow(sb, "Type", formatType(sp.getType())); //$NON-NLS-1$
    }
}
