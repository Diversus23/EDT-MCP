/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.AccountingRegister;
import com._1c.g5.v8.dt.metadata.mdclass.AccountingRegisterAttribute;
import com._1c.g5.v8.dt.metadata.mdclass.AccountingRegisterDimension;
import com._1c.g5.v8.dt.metadata.mdclass.AccountingRegisterResource;
import com._1c.g5.v8.dt.metadata.mdclass.BasicForm;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;

/**
 * Formatter for AccountingRegister metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class AccountingRegisterFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "AccountingRegister"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof AccountingRegister;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof AccountingRegister))
        {
            return "Error: Expected AccountingRegister object"; //$NON-NLS-1$
        }
        
        AccountingRegister register = (AccountingRegister) mdObject;
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
        
        // Special sections
        formatDimensions(sb, register, full, language);
        formatResources(sb, register, full, language);
        formatAttributes(sb, register, full, language);
        formatForms(sb, register, language);
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, AccountingRegister register, String language)
    {
        if (register.getChartOfAccounts() != null)
        {
            addPropertyRow(sb, "Chart Of Accounts", register.getChartOfAccounts().getName()); //$NON-NLS-1$
        }
        addPropertyRow(sb, "Correspondence", register.isCorrespondence()); //$NON-NLS-1$
    }
    
    private void formatDimensions(StringBuilder sb, AccountingRegister register, boolean full, String language)
    {
        if (register.getDimensions() == null || register.getDimensions().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Dimensions"); //$NON-NLS-1$
        
        if (full)
        {
            startTable(sb, "Name", "Synonym", "Type", "Balance", "Accounting Flag"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            
            for (AccountingRegisterDimension dim : register.getDimensions())
            {
                String accountingFlag = dim.getAccountingFlag() != null 
                    ? dim.getAccountingFlag().getName() : DASH;
                addTableRow(sb,
                    dim.getName(),
                    getSynonym(dim.getSynonym(), language),
                    formatType(dim.getType()),
                    formatBoolean(dim.isBalance()),
                    accountingFlag);
            }
        }
        else
        {
            startTable(sb, "Name", "Synonym", "Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            
            for (AccountingRegisterDimension dim : register.getDimensions())
            {
                addTableRow(sb,
                    dim.getName(),
                    getSynonym(dim.getSynonym(), language),
                    formatType(dim.getType()));
            }
        }
    }
    
    private void formatResources(StringBuilder sb, AccountingRegister register, boolean full, String language)
    {
        if (register.getResources() == null || register.getResources().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Resources"); //$NON-NLS-1$
        
        if (full)
        {
            startTable(sb, "Name", "Synonym", "Type", "Balance", "Accounting Flag"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            
            for (AccountingRegisterResource res : register.getResources())
            {
                String accountingFlag = res.getAccountingFlag() != null 
                    ? res.getAccountingFlag().getName() : DASH;
                addTableRow(sb,
                    res.getName(),
                    getSynonym(res.getSynonym(), language),
                    formatType(res.getType()),
                    formatBoolean(res.isBalance()),
                    accountingFlag);
            }
        }
        else
        {
            startTable(sb, "Name", "Synonym", "Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            
            for (AccountingRegisterResource res : register.getResources())
            {
                addTableRow(sb,
                    res.getName(),
                    getSynonym(res.getSynonym(), language),
                    formatType(res.getType()));
            }
        }
    }
    
    private void formatAttributes(StringBuilder sb, AccountingRegister register, boolean full, String language)
    {
        if (register.getAttributes() == null || register.getAttributes().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Attributes"); //$NON-NLS-1$
        
        if (full)
        {
            startTable(sb, "Name", "Synonym", "Type", "Fill Checking"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            
            for (AccountingRegisterAttribute attr : register.getAttributes())
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
            
            for (AccountingRegisterAttribute attr : register.getAttributes())
            {
                addTableRow(sb,
                    attr.getName(),
                    getSynonym(attr.getSynonym(), language),
                    formatType(attr.getType()));
            }
        }
    }
    
    private void formatForms(StringBuilder sb, AccountingRegister register, String language)
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
}
