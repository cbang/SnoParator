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
public class TableData {
    
    private String expAFcsId;
    private String expBFcsId;
    private String RQ;
    
    public TableData(String expAFcsId, String expBFcsId, String RQ)
    {
        this.expAFcsId = expAFcsId;
        this.expBFcsId = expBFcsId;
        this.RQ = RQ;
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
    public String getRQ() {
        return RQ;
    }

    /**
     * @param RQ the RQ to set
     */
    public void setRQ(String RQ) {
        this.RQ = RQ;
    }
    
}
