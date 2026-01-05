/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.BasicCommand;
import com._1c.g5.v8.dt.metadata.mdclass.BasicForm;
import com._1c.g5.v8.dt.metadata.mdclass.DbObjectAttribute;
import com._1c.g5.v8.dt.metadata.mdclass.DbObjectTabularSection;
import com._1c.g5.v8.dt.metadata.mdclass.Document;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.metadata.mdclass.TabularSectionAttribute;

/**
 * Formatter for Document metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class DocumentFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "Document"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof Document;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof Document))
        {
            return "Error: Expected Document object"; //$NON-NLS-1$
        }
        
        Document document = (Document) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, document.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, document, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, document, language);
            formatKeyProperties(sb, document, language);
        }
        
        // Always show attributes, tabular sections, forms, commands
        formatAttributes(sb, document, full, language);
        formatTabularSections(sb, document, full, language);
        formatForms(sb, document, language);
        formatCommands(sb, document, language);
        
        return sb.toString();
    }
    
    /**
     * Format key properties for basic mode (backward compatible).
     */
    private void formatKeyProperties(StringBuilder sb, Document document, String language)
    {
        addPropertyRow(sb, "Number Type", formatEnum(document.getNumberType())); //$NON-NLS-1$
        addPropertyRow(sb, "Number Length", document.getNumberLength()); //$NON-NLS-1$
        addPropertyRow(sb, "Number Allowed Length", formatEnum(document.getNumberAllowedLength())); //$NON-NLS-1$
        addPropertyRow(sb, "Posting", formatEnum(document.getPosting())); //$NON-NLS-1$
        addPropertyRow(sb, "Real Time Posting", formatEnum(document.getRealTimePosting())); //$NON-NLS-1$
    }
    
    private void formatAttributes(StringBuilder sb, Document document, boolean full, String language)
    {
        if (document.getAttributes() == null || document.getAttributes().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Attributes"); //$NON-NLS-1$
        
        if (full)
        {
            startTable(sb, "Name", "Synonym", "Type", "Fill Checking", "Fill From Filling Value", "Indexing"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
            
            for (DbObjectAttribute attr : document.getAttributes())
            {
                addTableRow(sb,
                    attr.getName(),
                    getSynonym(attr.getSynonym(), language),
                    formatType(attr.getType()),
                    formatEnum(attr.getFillChecking()),
                    formatBoolean(attr.isFillFromFillingValue()),
                    formatEnum(attr.getIndexing()));
            }
        }
        else
        {
            startTable(sb, "Name", "Synonym", "Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            
            for (DbObjectAttribute attr : document.getAttributes())
            {
                addTableRow(sb,
                    attr.getName(),
                    getSynonym(attr.getSynonym(), language),
                    formatType(attr.getType()));
            }
        }
    }
    
    private void formatTabularSections(StringBuilder sb, Document document, boolean full, String language)
    {
        if (document.getTabularSections() == null || document.getTabularSections().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Tabular Sections"); //$NON-NLS-1$
        
        for (DbObjectTabularSection tabSection : document.getTabularSections())
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
                if (full)
                {
                    startTable(sb, "Name", "Synonym", "Type", "Fill Checking", "Indexing"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                    
                    for (TabularSectionAttribute attr : tabSection.getAttributes())
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
    }
    
    private void formatForms(StringBuilder sb, Document document, String language)
    {
        if (document.getForms() == null || document.getForms().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Forms"); //$NON-NLS-1$
        startTable(sb, "Name", "Synonym", "Form Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        for (BasicForm form : document.getForms())
        {
            addTableRow(sb,
                form.getName(),
                getSynonym(form.getSynonym(), language),
                formatEnum(form.getFormType()));
        }
        
        // Show default forms
        if (document.getDefaultObjectForm() != null)
        {
            sb.append("\n**Default Object Form:** ").append(document.getDefaultObjectForm().getName()).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (document.getDefaultListForm() != null)
        {
            sb.append("**Default List Form:** ").append(document.getDefaultListForm().getName()).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (document.getDefaultChoiceForm() != null)
        {
            sb.append("**Default Choice Form:** ").append(document.getDefaultChoiceForm().getName()).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
    
    private void formatCommands(StringBuilder sb, Document document, String language)
    {
        if (document.getCommands() == null || document.getCommands().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Commands"); //$NON-NLS-1$
        startTable(sb, "Name", "Synonym", "Group"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        for (BasicCommand cmd : document.getCommands())
        {
            String group = cmd.getGroup() != null ? cmd.getGroup().toString() : DASH;
            addTableRow(sb,
                cmd.getName(),
                getSynonym(cmd.getSynonym(), language),
                group);
        }
    }
}
