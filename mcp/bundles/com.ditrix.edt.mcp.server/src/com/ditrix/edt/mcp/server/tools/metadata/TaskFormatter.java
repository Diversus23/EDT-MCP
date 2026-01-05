/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.AddressingAttribute;
import com._1c.g5.v8.dt.metadata.mdclass.BasicForm;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.metadata.mdclass.TabularSectionAttribute;
import com._1c.g5.v8.dt.metadata.mdclass.Task;
import com._1c.g5.v8.dt.metadata.mdclass.TaskAttribute;
import com._1c.g5.v8.dt.metadata.mdclass.TaskTabularSection;

/**
 * Formatter for Task metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class TaskFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "Task"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof Task;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof Task))
        {
            return "Error: Expected Task object"; //$NON-NLS-1$
        }
        
        Task task = (Task) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, task.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, task, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, task, language);
            formatKeyProperties(sb, task, language);
        }
        
        // Special sections
        formatAttributes(sb, task, full, language);
        formatAddressingAttributes(sb, task, full, language);
        formatTabularSections(sb, task, full, language);
        formatForms(sb, task, language);
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, Task task, String language)
    {
        addPropertyRow(sb, "Number Length", task.getNumberLength()); //$NON-NLS-1$
        addPropertyRow(sb, "Description Length", task.getDescriptionLength()); //$NON-NLS-1$
        if (task.getAddressing() != null)
        {
            addPropertyRow(sb, "Addressing Type", task.getAddressing().getName()); //$NON-NLS-1$
        }
    }
    
    private void formatAttributes(StringBuilder sb, Task task, boolean full, String language)
    {
        if (task.getAttributes() == null || task.getAttributes().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Attributes"); //$NON-NLS-1$
        
        if (full)
        {
            startTable(sb, "Name", "Synonym", "Type", "Fill Checking", "Indexed"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            
            for (TaskAttribute attr : task.getAttributes())
            {
                addTableRow(sb,
                    attr.getName(),
                    getSynonym(attr.getSynonym(), language),
                    formatType(attr.getType()),
                    formatEnum(attr.getFillChecking()),
                    formatEnum(attr.getIndexing()));
            }
        }
        else
        {
            startTable(sb, "Name", "Synonym", "Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            
            for (TaskAttribute attr : task.getAttributes())
            {
                addTableRow(sb,
                    attr.getName(),
                    getSynonym(attr.getSynonym(), language),
                    formatType(attr.getType()));
            }
        }
    }
    
    private void formatAddressingAttributes(StringBuilder sb, Task task, boolean full, String language)
    {
        if (task.getAddressingAttributes() == null || task.getAddressingAttributes().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Addressing Attributes"); //$NON-NLS-1$
        
        if (full)
        {
            startTable(sb, "Name", "Synonym", "Type", "Addressing Dimension"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            
            for (AddressingAttribute attr : task.getAddressingAttributes())
            {
                String dimension = attr.getAddressingDimension() != null 
                    ? attr.getAddressingDimension().getName() : DASH;
                addTableRow(sb,
                    attr.getName(),
                    getSynonym(attr.getSynonym(), language),
                    formatType(attr.getType()),
                    dimension);
            }
        }
        else
        {
            startTable(sb, "Name", "Synonym", "Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            
            for (AddressingAttribute attr : task.getAddressingAttributes())
            {
                addTableRow(sb,
                    attr.getName(),
                    getSynonym(attr.getSynonym(), language),
                    formatType(attr.getType()));
            }
        }
    }
    
    private void formatTabularSections(StringBuilder sb, Task task, boolean full, String language)
    {
        if (task.getTabularSections() == null || task.getTabularSections().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Tabular Sections"); //$NON-NLS-1$
        
        for (TaskTabularSection tabSection : task.getTabularSections())
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
                
                for (TabularSectionAttribute attr : tabSection.getAttributes())
                {
                    addTableRow(sb,
                        attr.getName(),
                        getSynonym(attr.getSynonym(), language),
                        formatType(attr.getType()));
                }
            }
        }
    }
    
    private void formatForms(StringBuilder sb, Task task, String language)
    {
        if (task.getForms() == null || task.getForms().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Forms"); //$NON-NLS-1$
        startTable(sb, "Name", "Synonym", "Form Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        for (BasicForm form : task.getForms())
        {
            addTableRow(sb,
                form.getName(),
                getSynonym(form.getSynonym(), language),
                formatEnum(form.getFormType()));
        }
    }
}
