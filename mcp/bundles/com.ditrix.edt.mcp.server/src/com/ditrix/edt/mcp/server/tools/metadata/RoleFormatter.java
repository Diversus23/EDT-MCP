/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.AbstractRoleDescription;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.metadata.mdclass.Role;
import com._1c.g5.v8.dt.rights.model.ObjectRight;
import com._1c.g5.v8.dt.rights.model.ObjectRights;
import com._1c.g5.v8.dt.rights.model.RoleDescription;

/**
 * Formatter for Role metadata objects.
 * Uses dynamic EMF reflection for comprehensive property output.
 */
public class RoleFormatter extends AbstractMetadataFormatter
{
    private static final String TYPE = "Role"; //$NON-NLS-1$
    
    @Override
    public String getMetadataType()
    {
        return TYPE;
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        return mdObject instanceof Role;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (!(mdObject instanceof Role))
        {
            return "Error: Expected Role object"; //$NON-NLS-1$
        }
        
        Role role = (Role) mdObject;
        StringBuilder sb = new StringBuilder();
        
        addMainHeader(sb, TYPE, role.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, role, language, "All Properties"); //$NON-NLS-1$
            formatRoleRights(sb, role);
        }
        else
        {
            // Basic mode: show only key properties
            formatBasicProperties(sb, role, language);
            formatKeyProperties(sb, role, language);
        }
        
        return sb.toString();
    }
    
    private void formatKeyProperties(StringBuilder sb, Role role, String language)
    {
        AbstractRoleDescription abstractRights = role.getRights();
        if (abstractRights instanceof RoleDescription)
        {
            RoleDescription rights = (RoleDescription) abstractRights;
            addPropertyRow(sb, "Set For New Objects", rights.isSetForNewObjects()); //$NON-NLS-1$
            addPropertyRow(sb, "Set For Attributes By Default", rights.isSetForAttributesByDefault()); //$NON-NLS-1$
        }
    }
    
    private void formatRoleRights(StringBuilder sb, Role role)
    {
        AbstractRoleDescription abstractRights = role.getRights();
        if (!(abstractRights instanceof RoleDescription))
        {
            return;
        }
        RoleDescription rights = (RoleDescription) abstractRights;
        if (rights.getRights() == null || rights.getRights().isEmpty())
        {
            return;
        }
        
        addSectionHeader(sb, "Object Rights (first 50)"); //$NON-NLS-1$
        startTable(sb, "Object", "Rights"); //$NON-NLS-1$ //$NON-NLS-2$
        
        int count = 0;
        for (ObjectRights objRights : rights.getRights())
        {
            if (count >= 50)
            {
                break;
            }
            
            if (objRights.getObject() != null)
            {
                StringBuilder rightsStr = new StringBuilder();
                if (objRights.getRights() != null)
                {
                    for (ObjectRight right : objRights.getRights())
                    {
                        if (right.getRight() != null && right.getValue() != null)
                        {
                            // RightValue is an enum - SET means the right is granted
                            if (right.getValue() == com._1c.g5.v8.dt.rights.model.RightValue.SET)
                            {
                                if (rightsStr.length() > 0)
                                {
                                    rightsStr.append(", "); //$NON-NLS-1$
                                }
                                rightsStr.append(right.getRight().getName());
                            }
                        }
                    }
                }
                
                if (rightsStr.length() > 0)
                {
                    addTableRow(sb, objRights.getObject().toString(), rightsStr.toString());
                    count++;
                }
            }
        }
    }
}
