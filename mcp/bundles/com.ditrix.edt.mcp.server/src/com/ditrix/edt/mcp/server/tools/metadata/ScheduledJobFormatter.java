/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.metadata.mdclass.ScheduledJob;

/**
 * Formatter for ScheduledJob metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class ScheduledJobFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "ScheduledJob"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof ScheduledJob;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof ScheduledJob))
        {
            return "Error: Expected ScheduledJob object"; //$NON-NLS-1$
        }
        
        ScheduledJob sj = (ScheduledJob) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, sj.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, sj, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, sj, language);
            formatKeyProperties(sb, sj, language);
        }
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, ScheduledJob sj, String language)
    {
        if (sj.getMethodName() != null)
        {
            addPropertyRow(sb, "Method Name", sj.getMethodName()); //$NON-NLS-1$
        }
        addPropertyRow(sb, "Use", sj.isUse()); //$NON-NLS-1$
        addPropertyRow(sb, "Predefined", sj.isPredefined()); //$NON-NLS-1$
    }
}
