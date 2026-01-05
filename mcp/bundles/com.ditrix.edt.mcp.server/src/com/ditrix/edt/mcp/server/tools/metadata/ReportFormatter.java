/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.BasicCommand;
import com._1c.g5.v8.dt.metadata.mdclass.BasicForm;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.metadata.mdclass.Report;
import com._1c.g5.v8.dt.metadata.mdclass.ReportAttribute;
import com._1c.g5.v8.dt.metadata.mdclass.ReportTabularSection;
import com._1c.g5.v8.dt.metadata.mdclass.ReportTabularSectionAttribute;

/**
 * Formatter for Report metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class ReportFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "Report"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof Report;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof Report))
        {
            return "Error: Expected Report object"; //$NON-NLS-1$
        }
        
        Report report = (Report) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, report.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, report, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, report, language);
            formatKeyProperties(sb, report, language);
        }
        
        // Special sections
        formatAttributes(sb, report, full, language);
        formatTabularSections(sb, report, full, language);
        formatForms(sb, report, language);
        formatCommands(sb, report, language);
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, Report report, String language)
    {
        if (report.getDefaultForm() != null)
        {
            addPropertyRow(sb, "Default Form", report.getDefaultForm().getName()); //$NON-NLS-1$
        }
    }
    
    private void formatAttributes(StringBuilder sb, Report report, boolean full, String language)
    {
        if (report.getAttributes() == null || report.getAttributes().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Attributes"); //$NON-NLS-1$
        
        if (full)
        {
            startTable(sb, "Name", "Synonym", "Type", "Fill Checking"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            
            for (ReportAttribute attr : report.getAttributes())
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
            
            for (ReportAttribute attr : report.getAttributes())
            {
                addTableRow(sb,
                    attr.getName(),
                    getSynonym(attr.getSynonym(), language),
                    formatType(attr.getType()));
            }
        }
    }
    
    private void formatTabularSections(StringBuilder sb, Report report, boolean full, String language)
    {
        if (report.getTabularSections() == null || report.getTabularSections().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Tabular Sections"); //$NON-NLS-1$
        
        for (ReportTabularSection tabSection : report.getTabularSections())
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
                
                for (ReportTabularSectionAttribute attr : tabSection.getAttributes())
                {
                    addTableRow(sb,
                        attr.getName(),
                        getSynonym(attr.getSynonym(), language),
                        formatType(attr.getType()));
                }
            }
        }
    }
    
    private void formatForms(StringBuilder sb, Report report, String language)
    {
        if (report.getForms() == null || report.getForms().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Forms"); //$NON-NLS-1$
        startTable(sb, "Name", "Synonym", "Form Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        for (BasicForm form : report.getForms())
        {
            addTableRow(sb,
                form.getName(),
                getSynonym(form.getSynonym(), language),
                formatEnum(form.getFormType()));
        }
    }
    
    private void formatCommands(StringBuilder sb, Report report, String language)
    {
        if (report.getCommands() == null || report.getCommands().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Commands"); //$NON-NLS-1$
        startTable(sb, "Name", "Synonym", "Group"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        for (BasicCommand cmd : report.getCommands())
        {
            String group = cmd.getGroup() != null ? cmd.getGroup().toString() : DASH;
            addTableRow(sb,
                cmd.getName(),
                getSynonym(cmd.getSynonym(), language),
                group);
        }
    }
}
