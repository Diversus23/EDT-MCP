/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.CommonModule;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;

/**
 * Formatter for CommonModule metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class CommonModuleFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "CommonModule"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof CommonModule;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof CommonModule))
        {
            return "Error: Expected CommonModule object"; //$NON-NLS-1$
        }
        
        CommonModule module = (CommonModule) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, module.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, module, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, module, language);
            formatKeyProperties(sb, module);
        }
        
        sb.append("\n---\n\n"); //$NON-NLS-1$
        
        return sb.toString();
    }
    
    /**
     * Format key properties for basic mode (backward compatible).
     */
    private void formatKeyProperties(StringBuilder sb, CommonModule module)
    {
        // Context flags
        addPropertyRow(sb, "Server", module.isServer()); //$NON-NLS-1$
        addPropertyRow(sb, "Server Call", module.isServerCall()); //$NON-NLS-1$
        addPropertyRow(sb, "External Connection", module.isExternalConnection()); //$NON-NLS-1$
        addPropertyRow(sb, "Client Managed Application", module.isClientManagedApplication()); //$NON-NLS-1$
        addPropertyRow(sb, "Client Ordinary Application", module.isClientOrdinaryApplication()); //$NON-NLS-1$
        
        // Special flags
        addPropertyRow(sb, "Global", module.isGlobal()); //$NON-NLS-1$
        addPropertyRow(sb, "Privileged", module.isPrivileged()); //$NON-NLS-1$
        addPropertyRow(sb, "Return Values Reuse", formatEnum(module.getReturnValuesReuse())); //$NON-NLS-1$
    }
}
