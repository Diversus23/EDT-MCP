/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.FunctionalOptionsParameter;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;

/**
 * Formatter for FunctionalOptionsParameter metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class FunctionalOptionsParameterFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "FunctionalOptionsParameter"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof FunctionalOptionsParameter;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof FunctionalOptionsParameter))
        {
            return "Error: Expected FunctionalOptionsParameter object"; //$NON-NLS-1$
        }
        
        FunctionalOptionsParameter fop = (FunctionalOptionsParameter) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, fop.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, fop, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, fop, language);
            formatKeyProperties(sb, fop, language);
        }
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, FunctionalOptionsParameter fop, String language)
    {
        if (fop.getUse() != null && !fop.getUse().isEmpty())
        {
            StringBuilder useStr = new StringBuilder();
            for (MdObject obj : fop.getUse())
            {
                if (useStr.length() > 0)
                {
                    useStr.append(", "); //$NON-NLS-1$
                }
                useStr.append(obj.getName());
            }
            addPropertyRow(sb, "Use", useStr.toString()); //$NON-NLS-1$
        }
    }
}
