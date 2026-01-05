/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.BasicCommand;
import com._1c.g5.v8.dt.metadata.mdclass.BasicForm;
import com._1c.g5.v8.dt.metadata.mdclass.DataProcessor;
import com._1c.g5.v8.dt.metadata.mdclass.DataProcessorAttribute;
import com._1c.g5.v8.dt.metadata.mdclass.DataProcessorTabularSection;
import com._1c.g5.v8.dt.metadata.mdclass.DataProcessorTabularSectionAttribute;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;

/**
 * Formatter for DataProcessor metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class DataProcessorFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "DataProcessor"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof DataProcessor;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof DataProcessor))
        {
            return "Error: Expected DataProcessor object"; //$NON-NLS-1$
        }
        
        DataProcessor dataProcessor = (DataProcessor) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, dataProcessor.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, dataProcessor, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, dataProcessor, language);
            formatKeyProperties(sb, dataProcessor, language);
        }
        
        // Special sections
        formatAttributes(sb, dataProcessor, full, language);
        formatTabularSections(sb, dataProcessor, full, language);
        formatForms(sb, dataProcessor, language);
        formatCommands(sb, dataProcessor, language);
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, DataProcessor dataProcessor, String language)
    {
        if (dataProcessor.getDefaultForm() != null)
        {
            addPropertyRow(sb, "Default Form", dataProcessor.getDefaultForm().getName()); //$NON-NLS-1$
        }
    }
    
    private void formatAttributes(StringBuilder sb, DataProcessor dataProcessor, boolean full, String language)
    {
        if (dataProcessor.getAttributes() == null || dataProcessor.getAttributes().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Attributes"); //$NON-NLS-1$
        
        if (full)
        {
            startTable(sb, "Name", "Synonym", "Type", "Fill Checking"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            
            for (DataProcessorAttribute attr : dataProcessor.getAttributes())
            {
                addTableRow(sb,
                    attr.getName(),
                    getSynonym(attr.getSynonym(), language),
                    formatType(attr.getType()),
                    formatEnum(attr.getFillChecking()));
            }
        }
        else
        {
            startTable(sb, "Name", "Synonym", "Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            
            for (DataProcessorAttribute attr : dataProcessor.getAttributes())
            {
                addTableRow(sb,
                    attr.getName(),
                    getSynonym(attr.getSynonym(), language),
                    formatType(attr.getType()));
            }
        }
    }
    
    private void formatTabularSections(StringBuilder sb, DataProcessor dataProcessor, boolean full, String language)
    {
        if (dataProcessor.getTabularSections() == null || dataProcessor.getTabularSections().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Tabular Sections"); //$NON-NLS-1$
        
        for (DataProcessorTabularSection tabSection : dataProcessor.getTabularSections())
        {
            sb.append("\n**").append(tabSection.getName()).append("**"); //$NON-NLS-1$ //$NON-NLS-2$
            String synonym = getSynonym(tabSection.getSynonym(), language);
            if (synonym != null && !synonym.isEmpty())
            {
                sb.append(" (").append(synonym).append(")"); //$NON-NLS-1$ //$NON-NLS-2$
            }
            sb.append("\n\n"); //$NON-NLS-1$
            
            if (tabSection.getAttributes() != null && !tabSection.getAttributes().isEmpty())
            {
                startTable(sb, "Name", "Synonym", "Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                
                for (DataProcessorTabularSectionAttribute attr : tabSection.getAttributes())
                {
                    addTableRow(sb,
                        attr.getName(),
                        getSynonym(attr.getSynonym(), language),
                        formatType(attr.getType()));
                }
            }
        }
    }
    
    private void formatForms(StringBuilder sb, DataProcessor dataProcessor, String language)
    {
        if (dataProcessor.getForms() == null || dataProcessor.getForms().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Forms"); //$NON-NLS-1$
        startTable(sb, "Name", "Synonym", "Form Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        for (BasicForm form : dataProcessor.getForms())
        {
            addTableRow(sb,
                form.getName(),
                getSynonym(form.getSynonym(), language),
                formatEnum(form.getFormType()));
        }
    }
    
    private void formatCommands(StringBuilder sb, DataProcessor dataProcessor, String language)
    {
        if (dataProcessor.getCommands() == null || dataProcessor.getCommands().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Commands"); //$NON-NLS-1$
        startTable(sb, "Name", "Synonym", "Group"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        for (BasicCommand cmd : dataProcessor.getCommands())
        {
            String group = cmd.getGroup() != null ? cmd.getGroup().toString() : DASH;
            addTableRow(sb,
                cmd.getName(),
                getSynonym(cmd.getSynonym(), language),
                group);
        }
    }
}
