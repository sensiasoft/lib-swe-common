package net.opengis;


/**
 * <p>
 * Tagging interface for all objects supporting copy.
 * This is used when deep-copying an OgcProperty object
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Nov 8, 2014
 */
public interface HasCopy
{
    public Object copy();
}
