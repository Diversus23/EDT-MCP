/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.BasicCommand;
import com._1c.g5.v8.dt.metadata.mdclass.BasicForm;
import com._1c.g5.v8.dt.metadata.mdclass.Catalog;
import com._1c.g5.v8.dt.metadata.mdclass.CatalogAttribute;
import com._1c.g5.v8.dt.metadata.mdclass.DbObjectTabularSection;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.metadata.mdclass.TabularSectionAttribute;

/**
 * Formatter for Catalog metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class CatalogFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "Catalog"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof Catalog;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof Catalog))
        {
            return "Error: Expected Catalog object"; //$NON-NLS-1$
        }
        
        Catalog catalog = (Catalog) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, catalog.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, catalog, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, catalog, language);
            formatKeyProperties(sb, catalog, language);
        }
        
        // Always show attributes, tabular sections, forms, commands
        formatAttributes(sb, catalog, full, language);
        formatTabularSections(sb, catalog, full, language);
        formatForms(sb, catalog, language);
        formatCommands(sb, catalog, language);
        
        return sb.toString();
    }
    
    /**
     * Format key properties for basic mode (backward compatible).
     */
    private void formatKeyProperties(StringBuilder sb, Catalog catalog, String language)
    {
        // Just continue the basic properties table with key catalog-specific properties
        addPropertyRow(sb, "Hierarchical", catalog.isHierarchical()); //$NON-NLS-1$
        addPropertyRow(sb, "Code Length", catalog.getCodeLength()); //$NON-NLS-1$
        addPropertyRow(sb, "Code Type", formatEnum(catalog.getCodeType())); //$NON-NLS-1$
        addPropertyRow(sb, "Description Length", catalog.getDescriptionLength()); //$NON-NLS-1$
    }
    
    private void formatAttributes(StringBuilder sb, Catalog catalog, boolean full, String language)
    {
        if (catalog.getAttributes() == null || catalog.getAttributes().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Attributes"); //$NON-NLS-1$
        
        if (full)
        {
            // Full mode - show detailed attribute info in a table
            startTable(sb, "Name", "Synonym", "Type", "Fill Checking", "Indexing", "Full Text Search"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
            
            for (CatalogAttribute attr : catalog.getAttributes())
            {
                addTableRow(sb,
                    attr.getName(),
                    getSynonym(attr.getSynonym(), language),
                    formatType(attr.getType()),
                    formatEnum(attr.getFillChecking()),
                    formatEnum(attr.getIndexing()),
                    formatEnum(attr.getFullTextSearch()));
            }
        }
        else
        {
            // Basic mode - simple table
            startTable(sb, "Name", "Synonym", "Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            
            for (CatalogAttribute attr : catalog.getAttributes())
            {
                addTableRow(sb,
                    attr.getName(),
                    getSynonym(attr.getSynonym(), language),
                    formatType(attr.getType()));
            }
        }
    }
    
    private void formatTabularSections(StringBuilder sb, Catalog catalog, boolean full, String language)
    {
        if (catalog.getTabularSections() == null || catalog.getTabularSections().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Tabular Sections"); //$NON-NLS-1$
        
        for (DbObjectTabularSection tabSection : catalog.getTabularSections())
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
    
    private void formatForms(StringBuilder sb, Catalog catalog, String language)
    {
        if (catalog.getForms() == null || catalog.getForms().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Forms"); //$NON-NLS-1$
        startTable(sb, "Name", "Synonym", "Form Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        for (BasicForm form : catalog.getForms())
        {
            addTableRow(sb,
                form.getName(),
                getSynonym(form.getSynonym(), language),
                formatEnum(form.getFormType()));
        }
        
        // Show default forms
        if (catalog.getDefaultObjectForm() != null)
        {
            sb.append("\n**Default Object Form:** ").append(catalog.getDefaultObjectForm().getName()).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (catalog.getDefaultListForm() != null)
        {
            sb.append("**Default List Form:** ").append(catalog.getDefaultListForm().getName()).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (catalog.getDefaultChoiceForm() != null)
        {
            sb.append("**Default Choice Form:** ").append(catalog.getDefaultChoiceForm().getName()).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
    
    private void formatCommands(StringBuilder sb, Catalog catalog, String language)
    {
        if (catalog.getCommands() == null || catalog.getCommands().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Commands"); //$NON-NLS-1$
        startTable(sb, "Name", "Synonym", "Group"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        for (BasicCommand cmd : catalog.getCommands())
        {
            String group = cmd.getGroup() != null ? cmd.getGroup().toString() : DASH;
            addTableRow(sb,
                cmd.getName(),
                getSynonym(cmd.getSynonym(), language),
                group);
        }
    }
}
