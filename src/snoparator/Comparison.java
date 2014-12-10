/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snoparator;

import java.util.ArrayList;

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
    private ArrayList<Relationship> granulatedDestConceptRels;
    private boolean granulatedFcsConcept;
    
    public Comparison(Normform expA, Normform expB, int resultQualifier, int totalRelationships, int matchingRelationships, int granulatedDestConcepts, ArrayList<Relationship> granulatedDestConceptRels, boolean granulatedFcsConcept)
    {
        this.expA = expA;
        this.expB = expB;
        this.resultQualifier = resultQualifier;
        this.totalRelationships = totalRelationships;
        this.matchingRelationships = matchingRelationships;
        this.granulatedDestConcepts = granulatedDestConcepts;
        this.granulatedDestConceptRels = granulatedDestConceptRels;
        this.granulatedFcsConcept = granulatedFcsConcept;
    }
    
    public String myToString()
    {
        ArrayList<String> qualifierTypes = new ArrayList<>();
        String one = "1: Full Match = All attributes + destination IDs are pairwise identical";
        String two = "2: Attributes = All identical. Destination IDs = Granulated";
        String three = "3: Attributes = All identical. Destination IDs = Not Granulated";
        String four = "4: Attributes = Not identical. Destination IDs = Identical";
        String five = "5: Attributes = Not identical. Destination IDs = Granulated";
        String six = "6: Attributes = Not identical. Destination IDs = Not Granulated";
        qualifierTypes.add(one);qualifierTypes.add(two);qualifierTypes.add(three);qualifierTypes.add(four);qualifierTypes.add(five);qualifierTypes.add(six);
        
        String result = "Normform A fcsId: "+String.valueOf(expA.getFcsId()+" | Normform B fcsId: "+String.valueOf(expB.getFcsId())+"\n");
        result = result + "Result Qualifier: "+String.valueOf(qualifierTypes.get(resultQualifier-1))+"\n";
        result = result + "Total unique relationships: "+String.valueOf(totalRelationships)+"\n";
        result = result + "Matching Pairwise Relationships: "+String.valueOf(matchingRelationships)+"\n";
        result = result + "Granulated Destination Concepts: "+String.valueOf(granulatedDestConcepts)+"\n";
        result = result + "Pairwise relationships, where granularity is detected:\n";
        if (granulatedDestConceptRels.size()==0)
        {
            result = result + "Noone\n";
        }
        for (int i = 0; i<granulatedDestConceptRels.size(); i += 2) //Increment by two because they are pairwise! 
        {
            result = result +String.valueOf(i+1)+":\n" +"Attribute: "+String.valueOf(granulatedDestConceptRels.get(i).getAttributeId())+" and Destination Id: "+String.valueOf(granulatedDestConceptRels.get(i).getDestinationId())+"\n"
            +"With respect to:\n"+"Attribute: "+String.valueOf(granulatedDestConceptRels.get(i+1).getAttributeId())+" and Destination Id: "+String.valueOf(granulatedDestConceptRels.get(i+1).getDestinationId())+"\n";
                                     
        }
        result = result + "Granulated Focus Concept?: "+String.valueOf(granulatedFcsConcept)+"\n";
        result = result + "_____________________________________________________\n";
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
