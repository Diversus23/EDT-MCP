/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.CommonAttribute;
import com._1c.g5.v8.dt.metadata.mdclass.CommonAttributeContentItem;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;

/**
 * Formatter for CommonAttribute metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class CommonAttributeFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "CommonAttribute"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof CommonAttribute;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof CommonAttribute))
        {
            return "Error: Expected CommonAttribute object"; //$NON-NLS-1$
        }
        
        CommonAttribute ca = (CommonAttribute) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, ca.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, ca, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, ca, language);
            formatKeyProperties(sb, ca, language);
        }
        
        // Special sections
        formatContent(sb, ca, language);
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, CommonAttribute ca, String language)
    {
        addPropertyRow(sb, "Type", formatType(ca.getType())); //$NON-NLS-1$
        if (ca.getAutoUse() != null)
        {
            addPropertyRow(sb, "Auto Use", formatEnum(ca.getAutoUse())); //$NON-NLS-1$
        }
        if (ca.getDataSeparation() != null)
        {
            addPropertyRow(sb, "Data Separation", formatEnum(ca.getDataSeparation())); //$NON-NLS-1$
        }
    }
    
    private void formatContent(StringBuilder sb, CommonAttribute ca, String language)
    {
        if (ca.getContent() == null || ca.getContent().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Content"); //$NON-NLS-1$
        startTable(sb, "Metadata Object", "Use"); //$NON-NLS-1$ //$NON-NLS-2$
        
        for (CommonAttributeContentItem content : ca.getContent())
        {
            String mdObjectName = DASH;
            if (content.getMetadata() != null)
            {
                MdObject contentObj = content.getMetadata();
                mdObjectName = contentObj.eClass().getName() + "." + contentObj.getName(); //$NON-NLS-1$
            }
            
            addTableRow(sb,
                mdObjectName,
                formatEnum(content.getUse()));
        }
    }
}
