package org.vast.data;

import org.ogc.cdm.common.DataBlock;


/**
 * <p><b>Title:</b><br/>
 * Var Size Mapper
 * </p>
 *
 * <p><b>Description:</b><br/>
 * This transfers uses a value from a DataBlock as an array size.
 * This is used automatically when DataArray of variable sizes are
 * referenced in an Indexer Tree.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Apr 3, 2006
 * @version 1.0
 */
public class VarSizeMapper implements DataVisitor
{
    DataArrayIndexer arrayIndexer;
    
    
    public VarSizeMapper(DataArrayIndexer arrayIndexer)
    {
        this.arrayIndexer = arrayIndexer;
    }


    public void mapData(DataBlock data)
    {
        arrayIndexer.arraySize = data.getIntValue();
        arrayIndexer.updateScalarCount();
    }    
}
