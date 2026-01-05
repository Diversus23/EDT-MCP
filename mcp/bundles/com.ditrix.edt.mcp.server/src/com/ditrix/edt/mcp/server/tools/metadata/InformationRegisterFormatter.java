/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.BasicCommand;
import com._1c.g5.v8.dt.metadata.mdclass.BasicForm;
import com._1c.g5.v8.dt.metadata.mdclass.InformationRegister;
import com._1c.g5.v8.dt.metadata.mdclass.InformationRegisterAttribute;
import com._1c.g5.v8.dt.metadata.mdclass.InformationRegisterDimension;
import com._1c.g5.v8.dt.metadata.mdclass.InformationRegisterResource;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;

/**
 * Formatter for InformationRegister metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class InformationRegisterFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "InformationRegister"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof InformationRegister;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof InformationRegister))
        {
            return "Error: Expected InformationRegister object"; //$NON-NLS-1$
        }
        
        InformationRegister register = (InformationRegister) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, register.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, register, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, register, language);
            formatKeyProperties(sb, register, language);
        }
        
        // Always show dimensions, resources, attributes, forms, commands
        formatDimensions(sb, register, full, language);
        formatResources(sb, register, full, language);
        formatAttributes(sb, register, full, language);
        formatForms(sb, register, language);
        formatCommands(sb, register, language);
        
        return sb.toString();
    }
    
    /**
     * Format key properties for basic mode (backward compatible).
     */
    private void formatKeyProperties(StringBuilder sb, InformationRegister register, String language)
    {
        addPropertyRow(sb, "Write Mode", formatEnum(register.getWriteMode())); //$NON-NLS-1$
        addPropertyRow(sb, "Information Register Periodicity", formatEnum(register.getInformationRegisterPeriodicity())); //$NON-NLS-1$
        addPropertyRow(sb, "Main Filter On Period", register.isMainFilterOnPeriod()); //$NON-NLS-1$
    }
    
    private void formatDimensions(StringBuilder sb, InformationRegister register, boolean full, String language)
    {
        if (register.getDimensions() == null || register.getDimensions().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Dimensions"); //$NON-NLS-1$
        
        if (full)
        {
            startTable(sb, "Name", "Synonym", "Type", "Master", "Indexing", "Main Filter"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
            
            for (InformationRegisterDimension dim : register.getDimensions())
            {
                addTableRow(sb,
                    dim.getName(),
                    getSynonym(dim.getSynonym(), language),
                    formatType(dim.getType()),
                    formatBoolean(dim.isMaster()),
                    formatEnum(dim.getIndexing()),
                    formatBoolean(dim.isMainFilter()));
            }
        }
        else
        {
            startTable(sb, "Name", "Synonym", "Type", "Master"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            
            for (InformationRegisterDimension dim : register.getDimensions())
            {
                addTableRow(sb,
                    dim.getName(),
                    getSynonym(dim.getSynonym(), language),
                    formatType(dim.getType()),
                    formatBoolean(dim.isMaster()));
            }
        }
    }
    
    private void formatResources(StringBuilder sb, InformationRegister register, boolean full, String language)
    {
        if (register.getResources() == null || register.getResources().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Resources"); //$NON-NLS-1$
        
        if (full)
        {
            startTable(sb, "Name", "Synonym", "Type", "Fill Checking", "Indexing"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            
            for (InformationRegisterResource res : register.getResources())
            {
                addTableRow(sb,
                    res.getName(),
                    getSynonym(res.getSynonym(), language),
                    formatType(res.getType()),
                    formatEnum(res.getFillChecking()),
                    formatEnum(res.getIndexing()));
            }
        }
        else
        {
            startTable(sb, "Name", "Synonym", "Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            
            for (InformationRegisterResource res : register.getResources())
            {
                addTableRow(sb,
                    res.getName(),
                    getSynonym(res.getSynonym(), language),
                    formatType(res.getType()));
            }
        }
    }
    
    private void formatAttributes(StringBuilder sb, InformationRegister register, boolean full, String language)
    {
        if (register.getAttributes() == null || register.getAttributes().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Attributes"); //$NON-NLS-1$
        
        if (full)
        {
            startTable(sb, "Name", "Synonym", "Type", "Fill Checking", "Indexing"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            
            for (InformationRegisterAttribute attr : register.getAttributes())
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
            
            for (InformationRegisterAttribute attr : register.getAttributes())
            {
                addTableRow(sb,
                    attr.getName(),
                    getSynonym(attr.getSynonym(), language),
                    formatType(attr.getType()));
            }
        }
    }
    
    private void formatForms(StringBuilder sb, InformationRegister register, String language)
    {
        if (register.getForms() == null || register.getForms().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Forms"); //$NON-NLS-1$
        startTable(sb, "Name", "Synonym", "Form Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        for (BasicForm form : register.getForms())
        {
            addTableRow(sb,
                form.getName(),
                getSynonym(form.getSynonym(), language),
                formatEnum(form.getFormType()));
        }
    }
    
    private void formatCommands(StringBuilder sb, InformationRegister register, String language)
    {
        if (register.getCommands() == null || register.getCommands().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Commands"); //$NON-NLS-1$
        startTable(sb, "Name", "Synonym", "Group"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        for (BasicCommand cmd : register.getCommands())
        {
            String group = cmd.getGroup() != null ? cmd.getGroup().toString() : DASH;
            addTableRow(sb,
                cmd.getName(),
                getSynonym(cmd.getSynonym(), language),
                group);
        }
    }
}
