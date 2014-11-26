/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snoparator;

/**
 *
 * @author Christian
 */
public class Result {
    private int resultQualifier;
    
    public Result(int resultQualifier)
    {
        this.resultQualifier = resultQualifier;
    }

    /**
     * @return the resultQualifier
     */
    public int getResultQualifier() {
        return resultQualifier;
    }

    /**
     * @param resultQualifier the resultQualifier to set
     */
    public void setResultQualifier(int resultQualifier) {
        this.resultQualifier = resultQualifier;
    }
    
    
}
