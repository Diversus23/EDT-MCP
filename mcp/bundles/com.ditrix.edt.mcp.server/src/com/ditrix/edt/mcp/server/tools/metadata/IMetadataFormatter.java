/**
 * Copyright (c) 2025 DitriX
 */
package com.ditrix.edt.mcp.server.tools.metadata;

import com._1c.g5.v8.dt.metadata.mdclass.MdObject;

/**
 * Interface for formatting metadata objects to markdown.
 * Each metadata type (Catalog, Document, etc.) should have its own implementation.
 */
public interface IMetadataFormatter
{
    /**
     * Returns the metadata type this formatter handles (e.g., "Catalog", "Document").
     */
    String getMetadataType();
    
    /**
     * Formats the metadata object to markdown.
     * 
     * @param mdObject The metadata object to format
     * @param full If true, includes all properties; if false, only basic properties
     * @param language Language code for synonyms (e.g., "en", "ru")
     * @return Markdown formatted string with metadata details
     */
    String format(MdObject mdObject, boolean full, String language);
    
    /**
     * Checks if this formatter can handle the given metadata object.
     */
    boolean canFormat(MdObject mdObject);
}
