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
    private Normform expA;
    private Normform expB;
    
    public Comparison(Normform expA, Normform expB)
    {
        this.expA = expA;
        this.expB = expB;
    }

    /**
     * @return the expA
     */
    public Normform getExpA() {
        return expA;
    }

    /**
     * @param expA the expA to set
     */
    public void setExpA(Normform expA) {
        this.expA = expA;
    }

    /**
     * @return the expB
     */
    public Normform getExpB() {
        return expB;
    }

    /**
     * @param expB the expB to set
     */
    public void setExpB(Normform expB) {
        this.expB = expB;
    }
}
