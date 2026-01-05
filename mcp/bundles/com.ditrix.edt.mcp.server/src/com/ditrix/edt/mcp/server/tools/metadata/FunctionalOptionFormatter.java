/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.FunctionalOption;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;

/**
 * Formatter for FunctionalOption metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class FunctionalOptionFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "FunctionalOption"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof FunctionalOption;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof FunctionalOption))
        {
            return "Error: Expected FunctionalOption object"; //$NON-NLS-1$
        }
        
        FunctionalOption fo = (FunctionalOption) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, fo.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, fo, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, fo, language);
            formatKeyProperties(sb, fo, language);
        }
        
        // Special sections
        formatContent(sb, fo, language);
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, FunctionalOption fo, String language)
    {
        if (fo.getLocation() != null)
        {
            addPropertyRow(sb, "Location", fo.getLocation().getName()); //$NON-NLS-1$
        }
        addPropertyRow(sb, "Privileged Get Mode", fo.isPrivilegedGetMode()); //$NON-NLS-1$
    }
    
    private void formatContent(StringBuilder sb, FunctionalOption fo, String language)
    {
        if (fo.getContent() == null || fo.getContent().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Content"); //$NON-NLS-1$
        startTable(sb, "Metadata Object", "Type"); //$NON-NLS-1$ //$NON-NLS-2$
        
        for (MdObject content : fo.getContent())
        {
            String mdObjectName = DASH;
            String mdType = DASH;
            
            if (content != null)
            {
                mdObjectName = content.getName();
                mdType = content.eClass().getName();
            }
            
            addTableRow(sb, mdObjectName, mdType);
        }
    }
}
