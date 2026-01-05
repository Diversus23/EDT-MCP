/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.metadata.mdclass.Sequence;
import com._1c.g5.v8.dt.metadata.mdclass.SequenceDimension;

/**
 * Formatter for Sequence metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class SequenceFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "Sequence"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof Sequence;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof Sequence))
        {
            return "Error: Expected Sequence object"; //$NON-NLS-1$
        }
        
        Sequence sequence = (Sequence) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, sequence.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, sequence, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, sequence, language);
            formatKeyProperties(sb, sequence, language);
        }
        
        // Special sections
        formatDimensions(sb, sequence, language);
        formatDocuments(sb, sequence);
        formatRegisters(sb, sequence);
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, Sequence sequence, String language)
    {
        if (sequence.getMoveBoundaryOnPosting() != null)
        {
            addPropertyRow(sb, "Move Boundary On Posting", formatEnum(sequence.getMoveBoundaryOnPosting())); //$NON-NLS-1$
        }
    }
    
    private void formatDimensions(StringBuilder sb, Sequence sequence, String language)
    {
        if (sequence.getDimensions() == null || sequence.getDimensions().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Dimensions"); //$NON-NLS-1$
        startTable(sb, "Name", "Synonym", "Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        for (SequenceDimension dim : sequence.getDimensions())
        {
            addTableRow(sb,
                dim.getName(),
                getSynonym(dim.getSynonym(), language),
                formatType(dim.getType()));
        }
    }
    
    private void formatDocuments(StringBuilder sb, Sequence sequence)
    {
        if (sequence.getDocuments() == null || sequence.getDocuments().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Documents"); //$NON-NLS-1$
        startTable(sb, "Document"); //$NON-NLS-1$
        
        for (var doc : sequence.getDocuments())
        {
            if (doc != null)
            {
                sb.append("| ").append(doc.getName()).append(" |\n"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
    }
    
    private void formatRegisters(StringBuilder sb, Sequence sequence)
    {
        if (sequence.getRegisterRecords() == null || sequence.getRegisterRecords().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Register Records"); //$NON-NLS-1$
        startTable(sb, "Register"); //$NON-NLS-1$
        
        for (var reg : sequence.getRegisterRecords())
        {
            if (reg != null)
            {
                sb.append("| ").append(reg.getName()).append(" |\n"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
    }
}
