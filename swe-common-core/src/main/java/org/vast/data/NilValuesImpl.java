package org.vast.data;

import java.util.ArrayList;
import java.util.List;
import net.opengis.HasCopy;
import net.opengis.swe.v20.NilValue;
import net.opengis.swe.v20.NilValues;


/**
 * POJO class for XML type NilValuesType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public class NilValuesImpl extends AbstractSWEImpl implements NilValues, HasCopy
{
    static final long serialVersionUID = 1L;
    protected List<NilValue> nilValueList = new ArrayList<NilValue>();
    
    
    public NilValuesImpl()
    {
    }
    
    
    public NilValuesImpl copy()
    {
        NilValuesImpl newObj = new NilValuesImpl();
        for (NilValue nil: nilValueList)
            newObj.nilValueList.add(new NilValueImpl(nil.getReason(), nil.getValue()));
        return newObj;
    }
    
    
    /**
     * Gets the list of nilValue properties
     */
    @Override
    public List<NilValue> getNilValueList()
    {
        return nilValueList;
    }
    
    
    /**
     * Returns number of nilValue properties
     */
    @Override
    public int getNumNilValues()
    {
        if (nilValueList == null)
            return 0;
        return nilValueList.size();
    }
    
    
    /**
     * Adds a new nilValue property
     */
    @Override
    public void addNilValue(NilValue nilValue)
    {
        this.nilValueList.add(nilValue);
    }
}
