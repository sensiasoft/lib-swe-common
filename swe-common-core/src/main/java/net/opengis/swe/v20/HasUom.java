package net.opengis.swe.v20;


/**
 * <p>
 * Tagging interface for data components that have a unit of measure
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Nov 7, 2014
 */
@SuppressWarnings("javadoc")
public interface HasUom
{
    /**
     * Gets the uom property
     */
    public UnitReference getUom();
    
    
    /**
     * Sets the uom property
     */
    public void setUom(UnitReference uom);
}
