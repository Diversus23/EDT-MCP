/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.metadata.mdclass.XDTOPackage;

/**
 * Formatter for XDTOPackage metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class XDTOPackageFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "XDTOPackage"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof XDTOPackage;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof XDTOPackage))
        {
            return "Error: Expected XDTOPackage object"; //$NON-NLS-1$
        }
        
        XDTOPackage xp = (XDTOPackage) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, xp.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, xp, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, xp, language);
            formatKeyProperties(sb, xp, language);
        }
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, XDTOPackage xp, String language)
    {
        if (xp.getNamespace() != null && !xp.getNamespace().isEmpty())
        {
            addPropertyRow(sb, "Namespace", xp.getNamespace()); //$NON-NLS-1$
        }
    }
}
