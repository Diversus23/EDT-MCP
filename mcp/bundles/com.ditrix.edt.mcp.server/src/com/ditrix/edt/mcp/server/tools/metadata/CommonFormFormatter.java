/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.CommonForm;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;

/**
 * Formatter for CommonForm metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class CommonFormFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "CommonForm"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof CommonForm;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof CommonForm))
        {
            return "Error: Expected CommonForm object"; //$NON-NLS-1$
        }
        
        CommonForm cf = (CommonForm) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, cf.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, cf, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, cf, language);
            formatKeyProperties(sb, cf);
        }
        
        return sb.toString();
    }
    
    /**
     * Format key properties for basic mode (backward compatible).
     */
    private void formatKeyProperties(StringBuilder sb, CommonForm cf)
    {
        if (cf.getFormType() != null)
        {
            addPropertyRow(sb, "Form Type", formatEnum(cf.getFormType())); //$NON-NLS-1$
        }
        addPropertyRow(sb, "Use Standard Commands", cf.isUseStandardCommands()); //$NON-NLS-1$
    }
}
