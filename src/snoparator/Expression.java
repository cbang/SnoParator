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
public class Expression {
    private long fcsId;
    private ArrayList<Relationship> relationships;
    
    public Expression(long fcsId, ArrayList<Relationship> relationships)
    {
        this.fcsId = fcsId;
        this.relationships = relationships;
    }

    /**
     * @return the fcsId
     */
    public long getFcsId() {
        return fcsId;
    }

    /**
     * @param fcsId the fcsId to set
     */
    public void setFcsId(long fcsId) {
        this.fcsId = fcsId;
    }

    /**
     * @return the relationships
     */
    public ArrayList<Relationship> getRelationships() {
        return relationships;
    }

    /**
     * @param relationships the relationships to set
     */
    public void setRelationships(ArrayList<Relationship> relationships) {
        this.relationships = relationships;
    }
    
    
}
