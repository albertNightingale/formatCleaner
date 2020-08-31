/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ywrealty.gsformatcleaner;

import com.ywrealty.gsformatcleaner.bean.ClientInformation;
import java.util.List;
import org.junit.*; 
import static org.junit.Assert.assertEquals;

/**
 *
 * @author albertliu
 */
public class FormatterTest {
    
//    /**
//     * Test of isinstandardformat method, of class Formatter.
//     */
//    @org.junit.jupiter.api.Test
//    public void testIsinstandardformat() {
//        List<String> arowofdata = null;
//        boolean expResult = false;
//        boolean result = Formatter.isinstandardformat(arowofdata);
//        assertEquals(expResult, result);
//    }

//    /**
//     * Test of setformat method, of class Formatter.
//     */
//    @org.junit.jupiter.api.Test
//    public void testSetformat() {
//        List<ClientInformation> arowofdata = null;
//        List<ClientInformation> expResult = null;
//        List<ClientInformation> result = Formatter.setformat(arowofdata);
//        assertEquals(expResult, result);
//    }
    


    /**
     * Test of removeDuplicates method, of class Formatter.
     */
    @org.junit.jupiter.api.Test
    public void testRemoveDuplicates() {
        System.out.println("removeDuplicates");
        List<ClientInformation> clientinformations = null;
        List<ClientInformation> expResult = null;
        List<ClientInformation> result = Formatter.removeDuplicates(clientinformations);
        assertEquals(expResult, result);
    }
    
}
