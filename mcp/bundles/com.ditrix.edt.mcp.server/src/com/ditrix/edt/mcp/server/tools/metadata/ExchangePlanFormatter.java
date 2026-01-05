/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.BasicForm;
import com._1c.g5.v8.dt.metadata.mdclass.ExchangePlan;
import com._1c.g5.v8.dt.metadata.mdclass.ExchangePlanAttribute;
import com._1c.g5.v8.dt.metadata.mdclass.ExchangePlanContentItem;
import com._1c.g5.v8.dt.metadata.mdclass.ExchangePlanTabularSection;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.metadata.mdclass.TabularSectionAttribute;

/**
 * Formatter for ExchangePlan metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class ExchangePlanFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "ExchangePlan"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof ExchangePlan;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof ExchangePlan))
        {
            return "Error: Expected ExchangePlan object"; //$NON-NLS-1$
        }
        
        ExchangePlan ep = (ExchangePlan) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, ep.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, ep, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, ep, language);
            formatKeyProperties(sb, ep, language);
        }
        
        // Special sections
        formatContent(sb, ep, language);
        formatAttributes(sb, ep, full, language);
        formatTabularSections(sb, ep, full, language);
        formatForms(sb, ep, language);
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, ExchangePlan ep, String language)
    {
        addPropertyRow(sb, "Code Length", ep.getCodeLength()); //$NON-NLS-1$
        addPropertyRow(sb, "Description Length", ep.getDescriptionLength()); //$NON-NLS-1$
        addPropertyRow(sb, "Distributed Info Base", ep.isDistributedInfoBase()); //$NON-NLS-1$
    }
    
    private void formatContent(StringBuilder sb, ExchangePlan ep, String language)
    {
        if (ep.getContent() == null || ep.getContent().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Content"); //$NON-NLS-1$
        startTable(sb, "Metadata Object", "Auto Record"); //$NON-NLS-1$ //$NON-NLS-2$
        
        for (ExchangePlanContentItem content : ep.getContent())
        {
            String mdObjectName = DASH;
            if (content.getMdObject() != null)
            {
                // Get FQN of the metadata object
                MdObject contentObj = content.getMdObject();
                mdObjectName = contentObj.eClass().getName() + "." + contentObj.getName(); //$NON-NLS-1$
            }
            
            addTableRow(sb,
                mdObjectName,
                formatEnum(content.getAutoRecord()));
        }
    }
    
    private void formatAttributes(StringBuilder sb, ExchangePlan ep, boolean full, String language)
    {
        if (ep.getAttributes() == null || ep.getAttributes().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Attributes"); //$NON-NLS-1$
        
        if (full)
        {
            startTable(sb, "Name", "Synonym", "Type", "Fill Checking", "Indexed"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            
            for (ExchangePlanAttribute attr : ep.getAttributes())
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
            
            for (ExchangePlanAttribute attr : ep.getAttributes())
            {
                addTableRow(sb,
                    attr.getName(),
                    getSynonym(attr.getSynonym(), language),
                    formatType(attr.getType()));
            }
        }
    }
    
    private void formatTabularSections(StringBuilder sb, ExchangePlan ep, boolean full, String language)
    {
        if (ep.getTabularSections() == null || ep.getTabularSections().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Tabular Sections"); //$NON-NLS-1$
        
        for (ExchangePlanTabularSection tabSection : ep.getTabularSections())
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
    
    private void formatForms(StringBuilder sb, ExchangePlan ep, String language)
    {
        if (ep.getForms() == null || ep.getForms().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Forms"); //$NON-NLS-1$
        startTable(sb, "Name", "Synonym", "Form Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        for (BasicForm form : ep.getForms())
        {
            addTableRow(sb,
                form.getName(),
                getSynonym(form.getSynonym(), language),
                formatEnum(form.getFormType()));
        }
    }
}
