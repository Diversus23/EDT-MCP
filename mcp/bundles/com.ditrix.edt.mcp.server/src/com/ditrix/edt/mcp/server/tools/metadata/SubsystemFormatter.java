/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.metadata.mdclass.Subsystem;

/**
 * Formatter for Subsystem metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class SubsystemFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "Subsystem"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof Subsystem;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof Subsystem))
        {
            return "Error: Expected Subsystem object"; //$NON-NLS-1$
        }
        
        Subsystem subsystem = (Subsystem) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, subsystem.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, subsystem, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, subsystem, language);
            formatKeyProperties(sb, subsystem, language);
        }
        
        // Special sections
        formatContent(sb, subsystem);
        formatChildSubsystems(sb, subsystem, language);
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, Subsystem subsystem, String language)
    {
        addPropertyRow(sb, "Include In Command Interface", subsystem.isIncludeInCommandInterface()); //$NON-NLS-1$
        if (subsystem.getParentSubsystem() != null)
        {
            addPropertyRow(sb, "Parent Subsystem", subsystem.getParentSubsystem().getName()); //$NON-NLS-1$
        }
    }
    
    private void formatContent(StringBuilder sb, Subsystem subsystem)
    {
        if (subsystem.getContent() == null || subsystem.getContent().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Content"); //$NON-NLS-1$
        startTable(sb, "Object", "Type"); //$NON-NLS-1$ //$NON-NLS-2$
        
        for (MdObject content : subsystem.getContent())
        {
            if (content != null)
            {
                addTableRow(sb,
                    content.getName(),
                    content.eClass().getName());
            }
        }
    }
    
    private void formatChildSubsystems(StringBuilder sb, Subsystem subsystem, String language)
    {
        if (subsystem.getSubsystems() == null || subsystem.getSubsystems().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Child Subsystems"); //$NON-NLS-1$
        startTable(sb, "Name", "Synonym"); //$NON-NLS-1$ //$NON-NLS-2$
        
        for (Subsystem child : subsystem.getSubsystems())
        {
            addTableRow(sb,
                child.getName(),
                getSynonym(child.getSynonym(), language));
        }
    }
}
