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
    
}
