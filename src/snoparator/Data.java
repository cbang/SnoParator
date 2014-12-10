/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snoparator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class implements the connectivity to the terminology service
 * @author Christian
 */
public class Data {
    
    public Data()
    {
        
    }
    
    public ArrayList<Relationship> getTransitiveClosure(Long sctID) 
            //The transitive closure method returns all descendants and ancestors of a given sctID
    {
        
        ArrayList<Relationship> results = new ArrayList<Relationship>();
        
        try //First get ancestors
        {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/test", "test", "");
        Statement stmt = (Statement) con.createStatement();
        String sctIdString = String.valueOf(sctID);
        ResultSet rs = stmt.executeQuery("SELECT * FROM sct2_transitiveclosure WHERE subtypeId = "+sctIdString+" AND active = 1");
        
        while (rs.next()) 
            {
            String destId = rs.getString("supertypeId");
            Long destinationId = Long.valueOf(destId);
            Long attribute = 116680003L;
            Relationship r = new Relationship(attribute,destinationId);
            results.add(r);
            System.out.println("Ancestors: "+"Attribute: "+String.valueOf(attribute)+" | destination id: "+destId);
                        
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        try //Then get descendants
        {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/test", "test", "");
        Statement stmt = (Statement) con.createStatement();
        String sctIdString = String.valueOf(sctID);
        ResultSet rs = stmt.executeQuery("SELECT * FROM sct2_transitiveclosure WHERE supertypeId = "+sctIdString+" AND active = 1");
        
        while (rs.next()) 
            {
            String destId = rs.getString("subtypeId");
            Long destinationId = Long.valueOf(destId);
            Long attribute = 116680003L;
            Relationship r = new Relationship(attribute,destinationId);
            results.add(r);
            System.out.println("Descendants: "+"Attribute: "+String.valueOf(attribute)+" | destination id: "+destId);
                        
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        return results; 
    }
    
    public ArrayList<Long> getSubsumption(Long sctID, int steps)
    {
        ArrayList<Long> results = new ArrayList<Long>();
        String isA = "116680003";
        String sctIdString = String.valueOf(sctID);
        
        try //First get the parents of the concept
        {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/test", "test", "");
        Statement stmt = (Statement) con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT destinationId FROM soa_relationship WHERE sourceId = "+sctIdString+" AND typeId = 116680003"+" AND active = 1");
        
         while (rs.next()) 
            {
            String destId = rs.getString("destinationId");
            Long destinationId = Long.valueOf(destId);
            results.add(destinationId);
            //System.out.println("Parents: "+String.valueOf(destinationId));
            }
        
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        if (steps == 1)
        {
            //We dont do anything more
        }
        else
        {
            //Find more parents based on the current resultset
        }
        
        try //Then get the children of the concept
        {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/test", "test", "");
        Statement stmt = (Statement) con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT sourceId FROM soa_relationship WHERE destinationId = "+sctIdString+" AND typeId = 116680003"+" AND active = 1");
        
         while (rs.next()) 
            {
            String sourceId = rs.getString("sourceId");
            Long sourceID = Long.valueOf(sourceId);
            results.add(sourceID);
            //System.out.println("Children: "+String.valueOf(sourceID));
            }
        
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        if (steps == 1)
        {
            //We dont do anything more
        }
        else
        {
            //Find more parents based on the current resultset
        }
                
        return results;
    }
        
    
}
