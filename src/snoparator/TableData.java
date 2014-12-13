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
public class TableData {
    
    private String expAFcsId;
    private String expBFcsId;
    private int RQ;
    private String match;
    private String description;
    private ArrayList<String> qualifierTypes;
    
    public TableData(String expAFcsId, String expBFcsId, int RQ)
    {
        this.expAFcsId = expAFcsId;
        this.expBFcsId = expBFcsId;
        this.RQ = RQ;
        qualifierTypes = new ArrayList<>();
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
        
        int rqVal = RQ;
        description = qualifierTypes.get(rqVal-1);
        
        if (rqVal == 1)
        {
            this.match = "Full Match";
        }
        if (rqVal > 1 && rqVal < 18)
        {
            this.match = "Partial Match";
        }
        if (rqVal == 18)
        {
            this.match = "No Match";
        }
        
    }

    /**
     * @return the expAFcsId
     */
    public String getExpAFcsId() {
        return expAFcsId;
    }

    /**
     * @param expAFcsId the expAFcsId to set
     */
    public void setExpAFcsId(String expAFcsId) {
        this.expAFcsId = expAFcsId;
    }

    /**
     * @return the expBFcsId
     */
    public String getExpBFcsId() {
        return expBFcsId;
    }

    /**
     * @param expBFcsId the expBFcsId to set
     */
    public void setExpBFcsId(String expBFcsId) {
        this.expBFcsId = expBFcsId;
    }

    /**
     * @return the RQ
     */
    public int getRQ() {
        return RQ;
    }

    /**
     * @param RQ the RQ to set
     */
    public void setRQ(int RQ) {
        this.RQ = RQ;
    }

    /**
     * @return the match
     */
    public String getMatch() {
        return match;
    }

    /**
     * @param match the match to set
     */
    public void setMatch(String match) {
        this.match = match;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
}
