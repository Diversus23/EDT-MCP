/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.HTTPService;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.metadata.mdclass.Method;
import com._1c.g5.v8.dt.metadata.mdclass.URLTemplate;

/**
 * Formatter for HTTPService metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class HttpServiceFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "HTTPService"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof HTTPService;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof HTTPService))
        {
            return "Error: Expected HTTPService object"; //$NON-NLS-1$
        }
        
        HTTPService hs = (HTTPService) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, hs.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, hs, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, hs, language);
            formatKeyProperties(sb, hs, language);
        }
        
        // Special sections
        formatUrlTemplates(sb, hs, full, language);
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, HTTPService hs, String language)
    {
        if (hs.getRootURL() != null && !hs.getRootURL().isEmpty())
        {
            addPropertyRow(sb, "Root URL", hs.getRootURL()); //$NON-NLS-1$
        }
        if (hs.getReuseSessions() != null)
        {
            addPropertyRow(sb, "Reuse Sessions", formatEnum(hs.getReuseSessions())); //$NON-NLS-1$
        }
    }
    
    private void formatUrlTemplates(StringBuilder sb, HTTPService hs, boolean full, String language)
    {
        if (hs.getUrlTemplates() == null || hs.getUrlTemplates().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "URL Templates"); //$NON-NLS-1$
        
        for (URLTemplate template : hs.getUrlTemplates())
        {
            sb.append("\n**").append(template.getName()).append("**"); //$NON-NLS-1$ //$NON-NLS-2$
            String synonym = getSynonym(template.getSynonym(), language);
            if (synonym != null && !synonym.isEmpty())
            {
                sb.append(" (").append(synonym).append(")"); //$NON-NLS-1$ //$NON-NLS-2$
            }
            sb.append("\n\n"); //$NON-NLS-1$
            
            // Template pattern
            if (template.getTemplate() != null && !template.getTemplate().isEmpty())
            {
                sb.append("- Template: `").append(template.getTemplate()).append("`\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
            }
            
            // Methods
            if (template.getMethods() != null && !template.getMethods().isEmpty())
            {
                startTable(sb, "Method", "Synonym", "Handler"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                
                for (Method method : template.getMethods())
                {
                    String handlerName = method.getHandler() != null ? method.getHandler() : DASH;
                    addTableRow(sb,
                        method.getName(),
                        getSynonym(method.getSynonym(), language),
                        handlerName);
                }
            }
        }
    }
}
