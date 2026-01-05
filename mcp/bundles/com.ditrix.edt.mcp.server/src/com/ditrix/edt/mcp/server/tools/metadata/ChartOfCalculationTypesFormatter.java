/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.BasicCommand;
import com._1c.g5.v8.dt.metadata.mdclass.BasicForm;
import com._1c.g5.v8.dt.metadata.mdclass.ChartOfCalculationTypes;
import com._1c.g5.v8.dt.metadata.mdclass.ChartOfCalculationTypesAttribute;
import com._1c.g5.v8.dt.metadata.mdclass.DbObjectTabularSection;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.metadata.mdclass.TabularSectionAttribute;

/**
 * Formatter for ChartOfCalculationTypes metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class ChartOfCalculationTypesFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "ChartOfCalculationTypes"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof ChartOfCalculationTypes;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof ChartOfCalculationTypes))
        {
            return "Error: Expected ChartOfCalculationTypes object"; //$NON-NLS-1$
        }
        
        ChartOfCalculationTypes chart = (ChartOfCalculationTypes) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, chart.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, chart, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, chart, language);
            formatKeyProperties(sb, chart, language);
        }
        
        // Special sections
        formatAttributes(sb, chart, full, language);
        formatTabularSections(sb, chart, full, language);
        formatForms(sb, chart, language);
        formatCommands(sb, chart, language);
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, ChartOfCalculationTypes chart, String language)
    {
        addPropertyRow(sb, "Code Length", chart.getCodeLength()); //$NON-NLS-1$
        addPropertyRow(sb, "Description Length", chart.getDescriptionLength()); //$NON-NLS-1$
        addPropertyRow(sb, "Action Period Use", chart.isActionPeriodUse()); //$NON-NLS-1$
        addPropertyRow(sb, "Depends On Calculation Types", formatEnum(chart.getDependenceOnCalculationTypes())); //$NON-NLS-1$
    }
    
    private void formatAttributes(StringBuilder sb, ChartOfCalculationTypes chart, boolean full, String language)
    {
        if (chart.getAttributes() == null || chart.getAttributes().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Attributes"); //$NON-NLS-1$
        
        if (full)
        {
            startTable(sb, "Name", "Synonym", "Type", "Fill Checking", "Indexed"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            
            for (ChartOfCalculationTypesAttribute attr : chart.getAttributes())
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
            
            for (ChartOfCalculationTypesAttribute attr : chart.getAttributes())
            {
                addTableRow(sb,
                    attr.getName(),
                    getSynonym(attr.getSynonym(), language),
                    formatType(attr.getType()));
            }
        }
    }
    
    private void formatTabularSections(StringBuilder sb, ChartOfCalculationTypes chart, boolean full, String language)
    {
        if (chart.getTabularSections() == null || chart.getTabularSections().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Tabular Sections"); //$NON-NLS-1$
        
        for (DbObjectTabularSection tabSection : chart.getTabularSections())
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
    
    private void formatForms(StringBuilder sb, ChartOfCalculationTypes chart, String language)
    {
        if (chart.getForms() == null || chart.getForms().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Forms"); //$NON-NLS-1$
        startTable(sb, "Name", "Synonym", "Form Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        for (BasicForm form : chart.getForms())
        {
            addTableRow(sb,
                form.getName(),
                getSynonym(form.getSynonym(), language),
                formatEnum(form.getFormType()));
        }
    }
    
    private void formatCommands(StringBuilder sb, ChartOfCalculationTypes chart, String language)
    {
        if (chart.getCommands() == null || chart.getCommands().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Commands"); //$NON-NLS-1$
        startTable(sb, "Name", "Synonym", "Group"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        for (BasicCommand cmd : chart.getCommands())
        {
            String group = cmd.getGroup() != null ? cmd.getGroup().toString() : DASH;
            addTableRow(sb,
                cmd.getName(),
                getSynonym(cmd.getSynonym(), language),
                group);
        }
    }
}
