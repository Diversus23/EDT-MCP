/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.AccumulationRegister;
import com._1c.g5.v8.dt.metadata.mdclass.AccumulationRegisterAttribute;
import com._1c.g5.v8.dt.metadata.mdclass.AccumulationRegisterDimension;
import com._1c.g5.v8.dt.metadata.mdclass.AccumulationRegisterResource;
import com._1c.g5.v8.dt.metadata.mdclass.BasicCommand;
import com._1c.g5.v8.dt.metadata.mdclass.BasicForm;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;

/**
 * Formatter for AccumulationRegister metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class AccumulationRegisterFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "AccumulationRegister"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof AccumulationRegister;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof AccumulationRegister))
        {
            return "Error: Expected AccumulationRegister object"; //$NON-NLS-1$
        }
        
        AccumulationRegister register = (AccumulationRegister) mdObject;
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
            formatKeyProperties(sb, register);
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
    private void formatKeyProperties(StringBuilder sb, AccumulationRegister register)
    {
        addPropertyRow(sb, "Register Type", formatEnum(register.getRegisterType())); //$NON-NLS-1$
    }
    
    private void formatDimensions(StringBuilder sb, AccumulationRegister register, boolean full, String language)
    {
        if (register.getDimensions() == null || register.getDimensions().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Dimensions"); //$NON-NLS-1$
        
        if (full)
        {
            startTable(sb, "Name", "Synonym", "Type", "Deny Incomplete Values", "Use In Totals"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            
            for (AccumulationRegisterDimension dim : register.getDimensions())
            {
                addTableRow(sb,
                    dim.getName(),
                    getSynonym(dim.getSynonym(), language),
                    formatType(dim.getType()),
                    formatBoolean(dim.isDenyIncompleteValues()),
                    formatBoolean(dim.isUseInTotals()));
            }
        }
        else
        {
            startTable(sb, "Name", "Synonym", "Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            
            for (AccumulationRegisterDimension dim : register.getDimensions())
            {
                addTableRow(sb,
                    dim.getName(),
                    getSynonym(dim.getSynonym(), language),
                    formatType(dim.getType()));
            }
        }
    }
    
    private void formatResources(StringBuilder sb, AccumulationRegister register, boolean full, String language)
    {
        if (register.getResources() == null || register.getResources().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Resources"); //$NON-NLS-1$
        
        if (full)
        {
            startTable(sb, "Name", "Synonym", "Type", "Fill Checking"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            
            for (AccumulationRegisterResource res : register.getResources())
            {
                addTableRow(sb,
                    res.getName(),
                    getSynonym(res.getSynonym(), language),
                    formatType(res.getType()),
                    formatEnum(res.getFillChecking()));
            }
        }
        else
        {
            startTable(sb, "Name", "Synonym", "Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            
            for (AccumulationRegisterResource res : register.getResources())
            {
                addTableRow(sb,
                    res.getName(),
                    getSynonym(res.getSynonym(), language),
                    formatType(res.getType()));
            }
        }
    }
    
    private void formatAttributes(StringBuilder sb, AccumulationRegister register, boolean full, String language)
    {
        if (register.getAttributes() == null || register.getAttributes().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Attributes"); //$NON-NLS-1$
        
        if (full)
        {
            startTable(sb, "Name", "Synonym", "Type", "Fill Checking"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            
            for (AccumulationRegisterAttribute attr : register.getAttributes())
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
            
            for (AccumulationRegisterAttribute attr : register.getAttributes())
            {
                addTableRow(sb,
                    attr.getName(),
                    getSynonym(attr.getSynonym(), language),
                    formatType(attr.getType()));
            }
        }
    }
    
    private void formatForms(StringBuilder sb, AccumulationRegister register, String language)
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
    
    private void formatCommands(StringBuilder sb, AccumulationRegister register, String language)
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
