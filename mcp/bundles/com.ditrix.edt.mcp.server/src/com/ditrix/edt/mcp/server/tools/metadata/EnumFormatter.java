/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.EnumValue;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;

/**
 * Formatter for Enum metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class EnumFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "Enum"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Enum;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Enum))
        {
            return "Error: Expected Enum object"; //$NON-NLS-1$
        }
        
        com._1c.g5.v8.dt.metadata.mdclass.Enum enumObject = (com._1c.g5.v8.dt.metadata.mdclass.Enum) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, enumObject.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, enumObject, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, enumObject, language);
            formatKeyProperties(sb, enumObject, language);
        }
        
        // Special sections
        formatEnumValues(sb, enumObject, language);
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, com._1c.g5.v8.dt.metadata.mdclass.Enum enumObject, String language)
    {
        addPropertyRow(sb, "Quick Choice", enumObject.isQuickChoice()); //$NON-NLS-1$
        addPropertyRow(sb, "Choice Mode", formatEnum(enumObject.getChoiceMode())); //$NON-NLS-1$
    }
    
    private void formatEnumValues(StringBuilder sb, com._1c.g5.v8.dt.metadata.mdclass.Enum enumObject, String language)
    {
        if (enumObject.getEnumValues() == null || enumObject.getEnumValues().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Values"); //$NON-NLS-1$
        startTable(sb, "Name", "Synonym", "Comment"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        for (EnumValue value : enumObject.getEnumValues())
        {
            addTableRow(sb,
                value.getName(),
                getSynonym(value.getSynonym(), language),
                value.getComment() != null ? value.getComment() : DASH);
        }
    }
}
