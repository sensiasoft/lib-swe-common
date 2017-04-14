/***************************************************************
 (c) Copyright 2007, University of Alabama in Huntsville (UAH)
 ALL RIGHTS RESERVED

 This software is the property of UAH.
 It cannot be duplicated, used, or distributed without the
 express written consent of UAH.

 This software developed by the Vis Analysis Systems Technology
 (VAST) within the Earth System Science Lab under the direction
 of Mike Botts (mike.botts@atmos.uah.edu)
 ***************************************************************/

package org.vast.util;

import java.util.Objects;

/**
 * <p>
 * Class for storing the definition of a spatial domain.
 * This mainly includes enveloppe coordinates and a crs.
 * </p>
 *
 * @author Alex Robin
 * @since Nov 15, 2005
 * */
public class SpatialExtent
{
	protected String crs;
	protected double minX = Double.NaN;
	protected double maxX = Double.NaN;
	protected double minY = Double.NaN;
	protected double maxY = Double.NaN;
	protected double minZ = Double.NaN;
	protected double maxZ = Double.NaN;    


    public SpatialExtent()
    {  
    }
    
    
    /**
     * @return an exact copy of this SpatialExtent
     */
    public SpatialExtent copy()
    {
        SpatialExtent bbox = new SpatialExtent();
        
        bbox.crs = this.crs;
        bbox.minX = this.minX;
        bbox.minY = this.minY;
        bbox.minZ = this.minZ;
        bbox.maxX = this.maxX;
        bbox.maxY = this.maxY;
        bbox.maxZ = this.maxZ;        
        
        return bbox;
    }
    
    
    public double[] getCenter()
    {
        double[] center = new double[3];
        center[0] = (minX + maxX) / 2;
        center[0] = (minY + maxY) / 2;
        center[0] = (minZ + maxZ) / 2;
        return center;
    }
    
    
    public double getDiagonalDistance()
    {
    	double dx = (maxX - minX);
        double dy = (maxY - minY);
        double dz = (maxZ - minZ);
    	double dx2 = dx * dx;
        double dy2 = dy * dy;
                
        if (Double.isNaN(dz))
        	return Math.sqrt(dx2 + dy2);
        
        double dz2 = dz * dz;
        return Math.sqrt(dx2 + dy2 + dz2);
    }
    
    
    public double getMaxDistance()
    {
        double dx = (maxX - minX);
        double dy = (maxY - minY);
        double dz = (maxZ - minZ);
        
        if (Double.isNaN(dz))
        	return Math.max(dx, dy);
        else
        	return Math.max(Math.max(dx, dy), dz);
    }
    
    
    public double getSizeX()
    {
        return maxX - minX;
    }
    
    
    public double getSizeY()
    {
        return maxY - minY;
    }
    
    
    public double getSizeZ()
    {
        return maxZ - minZ;
    }
    
    
    public boolean isNull()
    {
        if (Double.isNaN(minX)) return true;
        if (Double.isNaN(minY)) return true;
        //if (Double.isNaN(minZ)) return true;
        if (Double.isNaN(maxX)) return true;
        if (Double.isNaN(maxY)) return true;
        //if (Double.isNaN(maxZ)) return true;
        return false;
    }
    
    
    public void nullify()
    {
        minX = Double.NaN;
        maxX = Double.NaN;
        minY = Double.NaN;
        maxY = Double.NaN;
        minZ = Double.NaN;
        maxZ = Double.NaN;
    }
    
    
    /**
     * Resize spatial extent so that it contains the given 3D point
     * Point x,y,z coordinates must be in same Crs as SpatialExtent
     * @param x
     * @param y
     * @param z
     */
    public void resizeToContain(double x, double y, double z)
    {
        if (isNull())
        {
            minX = maxX = x;
            minY = maxY = y;
            minZ = maxZ = z;
            return;
        }        
        
        if (x < minX)
            minX = x;
        else if (x > maxX)
            maxX = x;
        
        if (y < minY)
            minY = y;
        else if (y > maxY)
            maxY = y;
        
        if (z < minZ)
            minZ = z;
        else if (z > maxZ)
            maxZ = z;
    }
    
    
    /**
     * Combines given extent with this extent
     * by computing the smallest rectangular
     * extent that contains both of them. 
     * @param bbox
     */
    public void add(SpatialExtent bbox)
    {
        checkCrs(bbox);
        
        if (isNull())
        {
            minX = bbox.minX;
            minY = bbox.minY;
            minZ = bbox.minZ;
            maxX = bbox.maxX;
            maxY = bbox.maxY;
            maxZ = bbox.maxZ;
            return;
        }
        
        if (minX > bbox.minX)
            minX = bbox.minX;        
        if (minY > bbox.minY)
            minY = bbox.minY;        
        if (minZ > bbox.minZ)
            minZ = bbox.minZ;
        if (maxX < bbox.maxX)
            maxX = bbox.maxX;
        if (maxY < bbox.maxY)
            maxY = bbox.maxY;
        if (maxZ < bbox.maxZ)
            maxZ = bbox.maxZ;
    }
    
    
    /**
     * Finds out if this bbox intersects the given bbox.
     * @param bbox
     * @return true if both bbox intersect
     */
    public boolean intersects(SpatialExtent bbox)
    {
        checkCrs(bbox);
        
        double bboxX1 = bbox.getMinX();
        double bboxX2 = bbox.getMaxX();
        double bboxY1 = bbox.getMinY();
        double bboxY2 = bbox.getMaxY();
        
        if (bboxX1 < minX && bboxX2 < minX)
            return false;
        
        if (bboxX1 > maxX && bboxX2 > maxX)
            return false;
        
        if (bboxY1 < minY && bboxY2 < minY)
            return false;
        
        if (bboxY1 > maxY && bboxY2 > maxY)
            return false;
        
        return true;
    }
    
    
    /**
     * Finds out if given extent is included in this one.
     * Returns true if extent is completely contained
     * within this extent
     * @param bbox
     * @return true if given bbox is contained in this bbox
     */
    public boolean contains(SpatialExtent bbox)
    {
        checkCrs(bbox);
        
        double bboxX1 = bbox.getMinX();
        double bboxX2 = bbox.getMaxX();
        double bboxY1 = bbox.getMinY();
        double bboxY2 = bbox.getMaxY();
        
        if (bboxX1 < minX || bboxX1 > maxX)
            return false;
        
        if (bboxX2 < minX || bboxX2 > maxX)
            return false;
        
        if (bboxY1 < minY || bboxY1 > maxY)
            return false;
        
        if (bboxY2 < minY || bboxY2 > maxY)
            return false;
        
        return true;
    }
    
    
    /**
     * Checks if extents crs are compatible
     * @param bbox
     */
    protected void checkCrs(SpatialExtent bbox)
    {
        if (crs != null && bbox.crs != null)
            if (!crs.equals(bbox.crs))
                throw new IllegalStateException("CRS must match");
    }
    
    
    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof SpatialExtent &&
               Objects.equals(crs, ((SpatialExtent)obj).crs) &&
               NumberUtils.ulpEquals(minX, ((SpatialExtent)obj).minX) &&
               NumberUtils.ulpEquals(maxX, ((SpatialExtent)obj).maxX) &&
               NumberUtils.ulpEquals(minY, ((SpatialExtent)obj).minY) &&
               NumberUtils.ulpEquals(maxY, ((SpatialExtent)obj).maxY) &&
               NumberUtils.ulpEquals(minZ, ((SpatialExtent)obj).minZ) &&
               NumberUtils.ulpEquals(maxZ, ((SpatialExtent)obj).maxZ);
    }
    
    
    @Override
    public int hashCode()
    {
        return Objects.hash(crs, minX, maxX, minY, maxY, minZ, maxZ);
    }
    
    
	public String getCrs()
	{
		return crs;
	}


	public void setCrs(String crs)
	{
		this.crs = crs;
	}


	public double getMaxX()
	{
		return maxX;
	}


	public void setMaxX(double maxX)
	{
		this.maxX = maxX;
	}


	public double getMaxY()
	{
		return maxY;
	}


	public void setMaxY(double maxY)
	{
		this.maxY = maxY;
	}


	public double getMaxZ()
	{
		return maxZ;
	}


	public void setMaxZ(double maxZ)
	{
		this.maxZ = maxZ;
	}


	public double getMinX()
	{
		return minX;
	}


	public void setMinX(double minX)
	{
		this.minX = minX;
	}


	public double getMinY()
	{
		return minY;
	}


	public void setMinY(double minY)
	{
		this.minY = minY;
	}


	public double getMinZ()
	{
		return minZ;
	}


	public void setMinZ(double minZ)
	{
		this.minZ = minZ;
	}
	
	
	@Override
    public String toString()
	{
		StringBuilder buf = new StringBuilder();
		buf.append(minX).append(',');
		buf.append(minY).append(',');
        buf.append(minZ).append(" - ");
        buf.append(maxX).append(',');
        buf.append(maxY).append(",");
        buf.append(maxZ);
        
        if (crs != null)
        {
            buf.append(" (");
            buf.append(crs);
            buf.append(')');
        }
        
        return buf.toString();
	}
}
