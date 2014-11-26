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
public class Comparison {
    private Expression expA;
    private Expression expB;
    
    public Comparison(Expression expA, Expression expB)
    {
        this.expA = expA;
        this.expB = expB;
    }

    /**
     * @return the expA
     */
    public Expression getExpA() {
        return expA;
    }

    /**
     * @param expA the expA to set
     */
    public void setExpA(Expression expA) {
        this.expA = expA;
    }

    /**
     * @return the expB
     */
    public Expression getExpB() {
        return expB;
    }

    /**
     * @param expB the expB to set
     */
    public void setExpB(Expression expB) {
        this.expB = expB;
    }
}
