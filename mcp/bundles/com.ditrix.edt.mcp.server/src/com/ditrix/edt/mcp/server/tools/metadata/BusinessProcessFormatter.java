/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.BasicForm;
import com._1c.g5.v8.dt.metadata.mdclass.BusinessProcess;
import com._1c.g5.v8.dt.metadata.mdclass.BusinessProcessAttribute;
import com._1c.g5.v8.dt.metadata.mdclass.DbObjectTabularSection;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.metadata.mdclass.TabularSectionAttribute;

/**
 * Formatter for BusinessProcess metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class BusinessProcessFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "BusinessProcess"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof BusinessProcess;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof BusinessProcess))
        {
            return "Error: Expected BusinessProcess object"; //$NON-NLS-1$
        }
        
        BusinessProcess bp = (BusinessProcess) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, bp.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, bp, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, bp, language);
            formatKeyProperties(sb, bp, language);
        }
        
        // Special sections
        formatAttributes(sb, bp, full, language);
        formatTabularSections(sb, bp, full, language);
        formatForms(sb, bp, language);
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, BusinessProcess bp, String language)
    {
        addPropertyRow(sb, "Number Length", bp.getNumberLength()); //$NON-NLS-1$
        if (bp.getTask() != null)
        {
            addPropertyRow(sb, "Task", bp.getTask().getName()); //$NON-NLS-1$
        }
        addPropertyRow(sb, "Create Task In Privileged Mode", bp.isCreateTaskInPrivilegedMode()); //$NON-NLS-1$
    }
    
    private void formatAttributes(StringBuilder sb, BusinessProcess bp, boolean full, String language)
    {
        if (bp.getAttributes() == null || bp.getAttributes().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Attributes"); //$NON-NLS-1$
        
        if (full)
        {
            startTable(sb, "Name", "Synonym", "Type", "Fill Checking", "Indexed"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            
            for (BusinessProcessAttribute attr : bp.getAttributes())
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
            
            for (BusinessProcessAttribute attr : bp.getAttributes())
            {
                addTableRow(sb,
                    attr.getName(),
                    getSynonym(attr.getSynonym(), language),
                    formatType(attr.getType()));
            }
        }
    }
    
    private void formatTabularSections(StringBuilder sb, BusinessProcess bp, boolean full, String language)
    {
        if (bp.getTabularSections() == null || bp.getTabularSections().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Tabular Sections"); //$NON-NLS-1$
        
        for (DbObjectTabularSection tabSection : bp.getTabularSections())
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
    
    private void formatForms(StringBuilder sb, BusinessProcess bp, String language)
    {
        if (bp.getForms() == null || bp.getForms().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Forms"); //$NON-NLS-1$
        startTable(sb, "Name", "Synonym", "Form Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        for (BasicForm form : bp.getForms())
        {
            addTableRow(sb,
                form.getName(),
                getSynonym(form.getSynonym(), language),
                formatEnum(form.getFormType()));
        }
    }
}
