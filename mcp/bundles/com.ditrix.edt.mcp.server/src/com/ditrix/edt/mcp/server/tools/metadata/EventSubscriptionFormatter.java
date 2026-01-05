/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.EventSubscription;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;

/**
 * Formatter for EventSubscription metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class EventSubscriptionFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "EventSubscription"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof EventSubscription;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof EventSubscription))
        {
            return "Error: Expected EventSubscription object"; //$NON-NLS-1$
        }
        
        EventSubscription es = (EventSubscription) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, es.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, es, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, es, language);
            formatKeyProperties(sb, es, language);
        }
        
        // Special sections
        formatSource(sb, es, language);
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, EventSubscription es, String language)
    {
        if (es.getEvent() != null)
        {
            addPropertyRow(sb, "Event", formatEnum(es.getEvent())); //$NON-NLS-1$
        }
        if (es.getHandler() != null)
        {
            addPropertyRow(sb, "Handler", es.getHandler()); //$NON-NLS-1$
        }
    }
    
    private void formatSource(StringBuilder sb, EventSubscription es, String language)
    {
        if (es.getSource() == null || es.getSource().getTypes() == null || es.getSource().getTypes().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Source"); //$NON-NLS-1$
        startTable(sb, "Metadata Object Type"); //$NON-NLS-1$
        
        for (var type : es.getSource().getTypes())
        {
            String typeName = formatType(type);
            sb.append("| ").append(typeName).append(" |\n"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
    
    private String formatType(Object type)
    {
        if (type == null)
        {
            return DASH;
        }
        return type.toString();
    }
}
