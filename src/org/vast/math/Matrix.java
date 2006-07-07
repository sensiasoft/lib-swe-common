/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.math;

import java.text.DecimalFormat;


/**
 * <p><b>Title:</b><br/>
 * Matrix
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Abstract Matrix object. 
 * Contians some common matrix algebra routines
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Mike Botts, Alexandre Robin
 * @date Nov 29, 2005
 * @version 1.0
 */
public abstract class Matrix
{
    int rows, columns; // matrix size
    double[][] a;

    public int[] getSize()
    {
        int[] size = new int[2];
        size[0] = rows;
        size[1] = columns;
        return size;
    }


    public void dump()
    {
        System.out.println("r,c : " + rows + ", " + columns);
        int i, j;
        for (i = 0; i < rows; i++)
        {
            for (j = 0; j < columns; j++)
            {
                System.out.println("a" + i + j + " = " + a[i][j]);
            }
        }
    }


    /* TODO should throw an exception on error */
    public double getElement(int i, int j)
    {
        if (i > rows || j > columns)
        {
            System.out.println("sttMatrix: " + i + "x" + j
                               + " exceeds " + rows + "x" + columns + " matrix size");
            return 0.0;
        }
        return a[i][j];
    }


    public void setElement(int i, int j, double value)
    {
        if (i > rows || j > columns)
        {
            System.out.println("sttMatrix: " + i + "x" + j
                               + " exceeds " + rows + "x" + columns + " matrix size");
            return;
        }
        a[i][j] = value;
    }


    public double trace()
    {
        double sum = 0;

        for (int i = 0; i < columns; i++)
            sum += a[i][i];

        return sum;
    }


    public double[][] getArray2D()
    {
        return a;
    }

    
    public String toString()
    {
        StringBuffer text = new StringBuffer();
        for (int i=0; i<4; i++)
        {
            for (int j=0; j<4; j++)
            {
                DecimalFormat formatter = new DecimalFormat("#0.0000");
                text.append(formatter.format(a[i][j]));
                text.append("\t\t");
            }
            
            text.append('\n');
        }
        
        return text.toString();
    }
}
