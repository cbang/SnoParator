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
    private int resultQualifier;
    private int totalRelationships;
    private int matchingRelationships;
    private int granulatedDestConcepts;
    private boolean granulatedFcsConcept;
    
    public Comparison(Normform expA, Normform expB, int resultQualifier, int totalRelationships, int matchingRelationships, int granulatedDestConcepts, boolean granulatedFcsConcept)
    {
        this.expA = expA;
        this.expB = expB;
        this.resultQualifier = resultQualifier;
        this.totalRelationships = totalRelationships;
        this.matchingRelationships = matchingRelationships;
        this.granulatedDestConcepts = granulatedDestConcepts;
        this.granulatedFcsConcept = granulatedFcsConcept;
    }
    
    public String myToString()
    {
        String result = "Normform A fcsId: "+String.valueOf(expA.getFcsId()+" | Normform B fcsId: "+String.valueOf(expB.getFcsId())+"\n");
        result = result + "Result Qualifier: "+String.valueOf(resultQualifier)+"\n";
        result = result + "Total unique relationships: "+String.valueOf(totalRelationships)+"\n";
        result = result + "Matching Relationships: "+String.valueOf(matchingRelationships)+"\n";
        result = result + "Granulated Destination Concepts: "+String.valueOf(granulatedDestConcepts)+"\n";
        result = result + "Granulated Focus Concept?: "+String.valueOf(granulatedFcsConcept);
        return result;
        
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
