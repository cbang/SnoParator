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
public class Relationship {
    private long attributeId;
    private long destinationId;
    
    public Relationship(long attributeId, long destinationId)
    {
        this.attributeId = attributeId;
        this.destinationId = destinationId;
    }

    /**
     * @return the attributeId
     */
    public long getAttributeId() {
        return attributeId;
    }

    /**
     * @param attributeId the attributeId to set
     */
    public void setAttributeId(long attributeId) {
        this.attributeId = attributeId;
    }

    /**
     * @return the destinationId
     */
    public long getDestinationId() {
        return destinationId;
    }

    /**
     * @param destinationId the destinationId to set
     */
    public void setDestinationId(long destinationId) {
        this.destinationId = destinationId;
    }
    
    
}
