/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.CommonPicture;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;

/**
 * Formatter for CommonPicture metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class CommonPictureFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "CommonPicture"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof CommonPicture;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof CommonPicture))
        {
            return "Error: Expected CommonPicture object"; //$NON-NLS-1$
        }
        
        CommonPicture cp = (CommonPicture) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, cp.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, cp, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, cp, language);
        }
        
        return sb.toString();
    }
}
