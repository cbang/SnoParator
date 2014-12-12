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
    private Expression expA;
    private Expression expB;
    private int resultQualifier;
    private int totalRelationships;
    private int matchingRelationships;
    private int granulatedDestConcepts;
    private ArrayList<Relationship> granulatedDestConceptRels;
    private boolean granulatedFcsConcept;
    
    public Comparison(Expression expA, Expression expB, int resultQualifier, int totalRelationships, int matchingRelationships, int granulatedDestConcepts, ArrayList<Relationship> granulatedDestConceptRels, boolean granulatedFcsConcept)
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
        String two = "2: Attributes = All are identical. Granularity Detected in Destination IDs";
        String three = "3: Attributes = All are identical. No granularity Detected in Destination IDs";
        String four = "4: Attributes = Not all are identical. Destination IDs are identical";
        String five = "5: Attributes = Not all are identical. Granularity Detected in Destination IDs";
        String six = "6: Attributes = Not all are identical. No granularity Detected in Destination IDs";
        String seven = "7: FcsConcept Granulated. Otherwise Full match";
        String eight = "8: FcsConcept Granulated. Attributes = All are identical. Granularity Detected in Destination IDs";
        String nine = "9: FcsConcept Granulated. Attributes = All are identical. No granularity Detected in Destination IDs";
        String ten = "10: FcsConcept Granulated. Attributes = Not all are identical. Destination IDs are identical";
        String eleven = "11: FcsConcept Granulated. Attributes = Not all are identical. Granularity Detected in Destination IDs";
        String twelve = "12: FcsConcept Granulated. Attributes = Not all are identical. No granularity Detected in Destination IDs";
        String thirteen = "13: FcsConcept not granulated. Otherwise Full match";
        String fourteen = "14: FcsConcept not granulated. Attributes = All are identical. Granularity Detected in Destination IDs";
        String fifteen = "15: FcsConcept not granulated. Attributes = All are identical. No granularity Detected in Destination IDs";
        String sixteen = "16: FcsConcept not granulated. Attributes = Not all are identical. Destination IDs are identical";
        String seventeen = "17: FcsConcept not granulated. Attributes = Not all are identical. Granularity Detected in Destination IDs";
        String eighteen = "18: FcsConcept not granulated. Attributes = Not all are identical. No granularity Detected in Destination IDs";
                
        qualifierTypes.add(one);qualifierTypes.add(two);qualifierTypes.add(three);qualifierTypes.add(four);
        qualifierTypes.add(five);qualifierTypes.add(six);qualifierTypes.add(seven);qualifierTypes.add(eight);
        qualifierTypes.add(nine);qualifierTypes.add(ten);qualifierTypes.add(eleven);qualifierTypes.add(twelve);
        qualifierTypes.add(thirteen);qualifierTypes.add(fourteen);qualifierTypes.add(fifteen);qualifierTypes.add(sixteen);
        qualifierTypes.add(seventeen);qualifierTypes.add(eighteen);
        
        String result = "Comparison: \n";
        result = result + "Normform A fcsId: "+String.valueOf(expA.getFcsId()+" | Normform B fcsId: "+String.valueOf(expB.getFcsId())+"\n");
        result = result + "Result Qualifier = "+String.valueOf(qualifierTypes.get(getResultQualifier()-1))+"\n";
        result = result + "Total unique relationships: "+String.valueOf(getTotalRelationships())+"\n";
        result = result + "Matching Pairwise Relationships: "+String.valueOf(getMatchingRelationships())+"\n";
        result = result + "Granulated Destination Concepts: "+String.valueOf(getGranulatedDestConcepts())+"\n";
        result = result + "Pairwise relationships, where granularity is detected:\n";
        if (getGranulatedDestConceptRels().size()==0)
        {
            result = result + "None\n";
        }
        for (int i = 0; i<getGranulatedDestConceptRels().size(); i += 2) //Increment by two because they are pairwise! 
        {
            result = result +String.valueOf(i+1)+":\n" +"Attribute: "+String.valueOf(getGranulatedDestConceptRels().get(i).getAttributeId())+" and Destination Id: "+String.valueOf(getGranulatedDestConceptRels().get(i).getDestinationId())+"\n"
            +"With respect to:\n"+"Attribute: "+String.valueOf(getGranulatedDestConceptRels().get(i+1).getAttributeId())+" and Destination Id: "+String.valueOf(getGranulatedDestConceptRels().get(i+1).getDestinationId())+"\n";
                                     
        }
        result = result + "Granulated Focus Concept?: "+String.valueOf(isGranulatedFcsConcept())+"\n";
        result = result + "Test validation | Relation: "+String.valueOf(expB.getRelationships().get(0).getAttributeId())+" | Destinations Id: "+String.valueOf(expB.getRelationships().get(0).getDestinationId())+"\n";
        result = result + "_____________________________________________________\n";
        return result;
        
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

    /**
     * @return the totalRelationships
     */
    public int getTotalRelationships() {
        return totalRelationships;
    }

    /**
     * @param totalRelationships the totalRelationships to set
     */
    public void setTotalRelationships(int totalRelationships) {
        this.totalRelationships = totalRelationships;
    }

    /**
     * @return the matchingRelationships
     */
    public int getMatchingRelationships() {
        return matchingRelationships;
    }

    /**
     * @param matchingRelationships the matchingRelationships to set
     */
    public void setMatchingRelationships(int matchingRelationships) {
        this.matchingRelationships = matchingRelationships;
    }

    /**
     * @return the granulatedDestConcepts
     */
    public int getGranulatedDestConcepts() {
        return granulatedDestConcepts;
    }

    /**
     * @param granulatedDestConcepts the granulatedDestConcepts to set
     */
    public void setGranulatedDestConcepts(int granulatedDestConcepts) {
        this.granulatedDestConcepts = granulatedDestConcepts;
    }

    /**
     * @return the granulatedDestConceptRels
     */
    public ArrayList<Relationship> getGranulatedDestConceptRels() {
        return granulatedDestConceptRels;
    }

    /**
     * @param granulatedDestConceptRels the granulatedDestConceptRels to set
     */
    public void setGranulatedDestConceptRels(ArrayList<Relationship> granulatedDestConceptRels) {
        this.granulatedDestConceptRels = granulatedDestConceptRels;
    }

    /**
     * @return the granulatedFcsConcept
     */
    public boolean isGranulatedFcsConcept() {
        return granulatedFcsConcept;
    }

    /**
     * @param granulatedFcsConcept the granulatedFcsConcept to set
     */
    public void setGranulatedFcsConcept(boolean granulatedFcsConcept) {
        this.granulatedFcsConcept = granulatedFcsConcept;
    }
}
