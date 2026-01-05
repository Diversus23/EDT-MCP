/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.CommonTemplate;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;

/**
 * Formatter for CommonTemplate metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class CommonTemplateFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "CommonTemplate"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof CommonTemplate;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof CommonTemplate))
        {
            return "Error: Expected CommonTemplate object"; //$NON-NLS-1$
        }
        
        CommonTemplate ct = (CommonTemplate) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, ct.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, ct, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, ct, language);
            formatKeyProperties(sb, ct, language);
        }
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, CommonTemplate ct, String language)
    {
        if (ct.getTemplateType() != null)
        {
            addPropertyRow(sb, "Template Type", formatEnum(ct.getTemplateType())); //$NON-NLS-1$
        }
    }
}
