/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.metadata.mdclass.BasicCommand;
import com._1c.g5.v8.dt.metadata.mdclass.BasicForm;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;

/**
 * Universal metadata formatter that can format any MdObject type
 * using dynamic EMF reflection.
 * 
 * This replaces all individual type-specific formatters (CatalogFormatter,
 * DocumentFormatter, etc.) with a single universal implementation.
 */
public class UniversalMetadataFormatter extends AbstractMetadataFormatter
{
    private static final UniversalMetadataFormatter INSTANCE = new UniversalMetadataFormatter();
    
    /**
     * Gets the singleton instance.
     */
    public static UniversalMetadataFormatter getInstance()
    {
        return INSTANCE;
    }
    
    @Override
    public String getMetadataType()
    {
        // Universal formatter can handle any type
        return "*"; //$NON-NLS-1$
    }
    
    @Override
    public boolean canFormat(MdObject mdObject)
    {
        // Can format any MdObject
        return mdObject != null;
    }
    
    @Override
    public String format(MdObject mdObject, boolean full, String language)
    {
        if (mdObject == null)
        {
            return "Error: MdObject is null"; //$NON-NLS-1$
        }
        
        StringBuilder sb = new StringBuilder();
        
        // Get type name dynamically from EMF class
        String typeName = mdObject.eClass().getName();
        
        addMainHeader(sb, typeName, mdObject.getName());
        
        if (full)
        {
            // Full mode: use dynamic EMF reflection to show ALL properties
            formatAllDynamicProperties(sb, mdObject, language, "All Properties"); //$NON-NLS-1$
        }
        else
        {
            // Basic mode: show basic properties
            formatBasicProperties(sb, mdObject, language);
        }
        
        // Format containment collections (attributes, tabular sections, forms, commands, etc.)
        formatContainmentCollections(sb, mdObject, full, language);
        
        return sb.toString();
    }
    
    /**
     * Format all containment collections (attributes, tabular sections, forms, commands, etc.)
     */
    private void formatContainmentCollections(StringBuilder sb, MdObject mdObject, boolean full, String language)
    {
        for (EStructuralFeature feature : mdObject.eClass().getEAllStructuralFeatures())
        {
            if (!(feature instanceof EReference))
            {
                continue;
            }
            
            EReference ref = (EReference) feature;
            
            // Only process containment many-valued references
            if (!ref.isContainment() || !ref.isMany())
            {
                continue;
            }
            
            // Skip derived, transient, volatile
            if (ref.isDerived() || ref.isTransient() || ref.isVolatile())
            {
                continue;
            }
            
            if (!mdObject.eIsSet(ref))
            {
                continue;
            }
            
            Object value = mdObject.eGet(ref);
            if (!(value instanceof Collection))
            {
                continue;
            }
            
            Collection<?> collection = (Collection<?>) value;
            if (collection.isEmpty())
            {
                continue;
            }
            
            String collectionName = formatFeatureName(ref.getName());
            
            // Special handling for known collection types
            Object firstItem = collection.iterator().next();
            
            if (firstItem instanceof BasicForm)
            {
                formatFormsCollection(sb, collectionName, collection, language);
            }
            else if (firstItem instanceof BasicCommand)
            {
                formatCommandsCollection(sb, collectionName, collection, language);
            }
            else if (firstItem instanceof MdObject)
            {
                formatMdObjectCollection(sb, collectionName, collection, full, language);
            }
            else if (firstItem instanceof EObject)
            {
                formatEObjectCollection(sb, collectionName, collection, full, language);
            }
        }
    }
    
    /**
     * Format a collection of forms.
     */
    private void formatFormsCollection(StringBuilder sb, String name, Collection<?> forms, String language)
    {
        addSectionHeader(sb, name);
        startTable(sb, "Name", "Synonym", "Form Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        for (Object item : forms)
        {
            if (item instanceof BasicForm)
            {
                BasicForm form = (BasicForm) item;
                addTableRow(sb,
                    form.getName(),
                    getSynonym(form.getSynonym(), language),
                    formatEnum(form.getFormType()));
            }
        }
    }
    
    /**
     * Format a collection of commands.
     */
    private void formatCommandsCollection(StringBuilder sb, String name, Collection<?> commands, String language)
    {
        addSectionHeader(sb, name);
        startTable(sb, "Name", "Synonym", "Group"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        for (Object item : commands)
        {
            if (item instanceof BasicCommand)
            {
                BasicCommand cmd = (BasicCommand) item;
                String group = formatCommandGroup(cmd.getGroup());
                addTableRow(sb,
                    cmd.getName(),
                    getSynonym(cmd.getSynonym(), language),
                    group);
            }
        }
    }
    
    /**
     * Format command group to a readable string.
     * Uses EObjectInspector to extract the category enum from StandardCommandGroup.
     */
    private String formatCommandGroup(Object groupObj)
    {
        if (groupObj == null)
        {
            return DASH;
        }
        
        // If it's an EObject (StandardCommandGroup), use EObjectInspector
        if (groupObj instanceof EObject)
        {
            // EObjectInspector will properly extract the 'category' enum value
            return EObjectInspector.getPrimaryValueAsString((EObject) groupObj);
        }
        
        return groupObj.toString();
    }
    
    /**
     * Format a collection of MdObjects (attributes, tabular sections, dimensions, etc.)
     */
    private void formatMdObjectCollection(StringBuilder sb, String name, Collection<?> items, 
            boolean full, String language)
    {
        addSectionHeader(sb, name);
        
        boolean first = true;
        for (Object item : items)
        {
            if (item instanceof MdObject)
            {
                MdObject mdObj = (MdObject) item;
                
                if (first)
                {
                    // Build table headers based on first item
                    if (full)
                    {
                        startTable(sb, "Name", "Synonym", "Type"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    }
                    else
                    {
                        startTable(sb, "Name", "Synonym"); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                    first = false;
                }
                
                String typeName = getTypeFromMdObject(mdObj);
                if (full)
                {
                    addTableRow(sb, mdObj.getName(), getSynonym(mdObj.getSynonym(), language), typeName);
                }
                else
                {
                    addTableRow(sb, mdObj.getName(), getSynonym(mdObj.getSynonym(), language));
                }
            }
        }
    }
    
    /**
     * Format a collection of EObjects that are not MdObjects.
     */
    private void formatEObjectCollection(StringBuilder sb, String name, Collection<?> items, 
            boolean full, String language)
    {
        addSectionHeader(sb, name);
        
        boolean first = true;
        for (Object item : items)
        {
            if (item instanceof EObject)
            {
                EObject eObj = (EObject) item;
                
                if (first)
                {
                    // Get available feature names for headers
                    startTable(sb, "Name", "Value"); //$NON-NLS-1$ //$NON-NLS-2$
                    first = false;
                }
                
                String itemName = formatEObjectReference(eObj);
                addTableRow(sb, itemName, eObj.eClass().getName());
            }
        }
    }
    
    /**
     * Try to get type information from an MdObject (e.g., for attributes).
     */
    private String getTypeFromMdObject(MdObject mdObj)
    {
        // Try to find a "type" feature
        EStructuralFeature typeFeature = mdObj.eClass().getEStructuralFeature("type"); //$NON-NLS-1$
        if (typeFeature != null)
        {
            Object typeValue = mdObj.eGet(typeFeature);
            if (typeValue instanceof com._1c.g5.v8.dt.mcore.TypeDescription)
            {
                return formatType((com._1c.g5.v8.dt.mcore.TypeDescription) typeValue);
            }
        }
        return DASH;
    }
}
