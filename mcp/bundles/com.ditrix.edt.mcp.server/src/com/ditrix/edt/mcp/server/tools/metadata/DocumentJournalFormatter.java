/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.BasicForm;
import com._1c.g5.v8.dt.metadata.mdclass.Column;
import com._1c.g5.v8.dt.metadata.mdclass.DocumentJournal;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;

/**
 * Formatter for DocumentJournal metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class DocumentJournalFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "DocumentJournal"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof DocumentJournal;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof DocumentJournal))
        {
            return "Error: Expected DocumentJournal object"; //$NON-NLS-1$
        }
        
        DocumentJournal journal = (DocumentJournal) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, journal.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, journal, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, journal, language);
            formatKeyProperties(sb, journal, language);
        }
        
        // Special sections
        formatColumns(sb, journal, full, language);
        formatRegisteredDocuments(sb, journal);
        formatForms(sb, journal, language);
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, DocumentJournal journal, String language)
    {
        if (journal.getDefaultForm() != null)
        {
            addPropertyRow(sb, "Default Form", journal.getDefaultForm().getName()); //$NON-NLS-1$
        }
        addPropertyRow(sb, "Use Standard Commands", journal.isUseStandardCommands()); //$NON-NLS-1$
    }
    
    private void formatColumns(StringBuilder sb, DocumentJournal journal, boolean full, String language)
    {
        if (journal.getColumns() == null || journal.getColumns().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Columns"); //$NON-NLS-1$
        
        if (full)
        {
            startTable(sb, "Name", "Synonym", "Indexed"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            
            for (Column column : journal.getColumns())
            {
                addTableRow(sb,
                    column.getName(),
                    getSynonym(column.getSynonym(), language),
                    formatEnum(column.getIndexing()));
            }
        }
        else
        {
            startTable(sb, "Name", "Synonym"); //$NON-NLS-1$ //$NON-NLS-2$
            
            for (Column column : journal.getColumns())
            {
                addTableRow(sb,
                    column.getName(),
                    getSynonym(column.getSynonym(), language));
            }
        }
    }
    
    private void formatRegisteredDocuments(StringBuilder sb, DocumentJournal journal)
    {
        if (journal.getRegisteredDocuments() == null || journal.getRegisteredDocuments().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Registered Documents"); //$NON-NLS-1$
        startTable(sb, "Document"); //$NON-NLS-1$
        
        for (var doc : journal.getRegisteredDocuments())
        {
            if (doc != null)
            {
                sb.append("| ").append(doc.getName()).append(" |\n"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
    }
    
    private void formatForms(StringBuilder sb, DocumentJournal journal, String language)
    {
        if (journal.getForms() == null || journal.getForms().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Forms"); //$NON-NLS-1$
        startTable(sb, "Name", "Synonym", "Form Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        for (BasicForm form : journal.getForms())
        {
            addTableRow(sb,
                form.getName(),
                getSynonym(form.getSynonym(), language),
                formatEnum(form.getFormType()));
        }
    }
}
