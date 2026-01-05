/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.BasicForm;
import com._1c.g5.v8.dt.metadata.mdclass.CalculationRegister;
import com._1c.g5.v8.dt.metadata.mdclass.CalculationRegisterAttribute;
import com._1c.g5.v8.dt.metadata.mdclass.CalculationRegisterDimension;
import com._1c.g5.v8.dt.metadata.mdclass.CalculationRegisterResource;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.metadata.mdclass.Recalculation;

/**
 * Formatter for CalculationRegister metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class CalculationRegisterFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "CalculationRegister"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof CalculationRegister;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof CalculationRegister))
        {
            return "Error: Expected CalculationRegister object"; //$NON-NLS-1$
        }
        
        CalculationRegister register = (CalculationRegister) mdObject;
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
        formatRecalculations(sb, register, language);
        formatForms(sb, register, language);
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, CalculationRegister register, String language)
    {
        if (register.getChartOfCalculationTypes() != null)
        {
            addPropertyRow(sb, "Chart Of Calculation Types", register.getChartOfCalculationTypes().getName()); //$NON-NLS-1$
        }
        if (register.getPeriodicity() != null)
        {
            addPropertyRow(sb, "Periodicity", formatEnum(register.getPeriodicity())); //$NON-NLS-1$
        }
        addPropertyRow(sb, "Action Period", register.isActionPeriod()); //$NON-NLS-1$
    }
    
    private void formatDimensions(StringBuilder sb, CalculationRegister register, boolean full, String language)
    {
        if (register.getDimensions() == null || register.getDimensions().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Dimensions"); //$NON-NLS-1$
        
        if (full)
        {
            startTable(sb, "Name", "Synonym", "Type", "Base", "Indexed"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            
            for (CalculationRegisterDimension dim : register.getDimensions())
            {
                addTableRow(sb,
                    dim.getName(),
                    getSynonym(dim.getSynonym(), language),
                    formatType(dim.getType()),
                    formatBoolean(dim.isBaseDimension()),
                    formatEnum(dim.getIndexing()));
            }
        }
        else
        {
            startTable(sb, "Name", "Synonym", "Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            
            for (CalculationRegisterDimension dim : register.getDimensions())
            {
                addTableRow(sb,
                    dim.getName(),
                    getSynonym(dim.getSynonym(), language),
                    formatType(dim.getType()));
            }
        }
    }
    
    private void formatResources(StringBuilder sb, CalculationRegister register, boolean full, String language)
    {
        if (register.getResources() == null || register.getResources().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Resources"); //$NON-NLS-1$
        
        if (full)
        {
            startTable(sb, "Name", "Synonym", "Type", "Fill Checking"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            
            for (CalculationRegisterResource res : register.getResources())
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
            
            for (CalculationRegisterResource res : register.getResources())
            {
                addTableRow(sb,
                    res.getName(),
                    getSynonym(res.getSynonym(), language),
                    formatType(res.getType()));
            }
        }
    }
    
    private void formatAttributes(StringBuilder sb, CalculationRegister register, boolean full, String language)
    {
        if (register.getAttributes() == null || register.getAttributes().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Attributes"); //$NON-NLS-1$
        
        if (full)
        {
            startTable(sb, "Name", "Synonym", "Type", "Fill Checking"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            
            for (CalculationRegisterAttribute attr : register.getAttributes())
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
            
            for (CalculationRegisterAttribute attr : register.getAttributes())
            {
                addTableRow(sb,
                    attr.getName(),
                    getSynonym(attr.getSynonym(), language),
                    formatType(attr.getType()));
            }
        }
    }
    
    private void formatRecalculations(StringBuilder sb, CalculationRegister register, String language)
    {
        if (register.getRecalculations() == null || register.getRecalculations().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Recalculations"); //$NON-NLS-1$
        startTable(sb, "Name", "Synonym"); //$NON-NLS-1$ //$NON-NLS-2$
        
        for (Recalculation recalc : register.getRecalculations())
        {
            addTableRow(sb,
                recalc.getName(),
                getSynonym(recalc.getSynonym(), language));
        }
    }
    
    private void formatForms(StringBuilder sb, CalculationRegister register, String language)
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
