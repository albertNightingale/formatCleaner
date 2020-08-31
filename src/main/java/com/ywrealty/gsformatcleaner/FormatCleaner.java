/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ywrealty.gsformatcleaner;

import com.ywrealty.gsformatcleaner.bean.ClientInformation;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author albertliu
 */
public class FormatCleaner {
    
    private static final Logger logger = Logger.getLogger(FormatCleaner.class.getName());
    private static Properties gs_prop; 
    private static int cilength;
    private static List<ClientInformation> clientinfolist;
    
    public FormatCleaner() throws FileNotFoundException, IOException
    {
        gs_prop = new Properties();
        clientinfolist = new ArrayList<ClientInformation>();
        cilength = 0; 
        // gs_prop.load(new FileReader(home + "config/"));
    }

    public static int getCilength() {
        return cilength;
    }

    public static List<ClientInformation> getClientinfolist() {
        return clientinfolist;
    }
    
    public static void main(String[] args)
    {
        try {            
            FormatCleaner f = new FormatCleaner();
            SheetManager manager = new SheetManager(gs_prop);
            final int startinglength; 
            
            List<List<Object>> lists = manager.accessdata(Defines.SPREADSHEET_ID, Defines.TTL_SHEETNAME);
            
            
            List<Object> header = lists.get(0);
            lists.remove(header); // remove the first line
            List<Object> title = lists.get(0);
            lists.remove(title); // remove the title line 
            
            // trasversal operation, print out all the data that is being read through the spreadsheet reading operation          
            /* 
            int i = 1; 
            for (List<Object> list : lists)
            {
                System.out.print(i + ": " + list.size() + "  ");
                for (Object o : list)
                    if (!o.equals(""))
                        System.out.print(o.toString() + ",");
                    else
                        System.out.print("null,"); 
                System.out.println();
                i++; 
            }
            */
            
            System.out.println("\n\n\n\n\n\n"); 
            
            // build ClientInformation Object 
            for (List<Object> list : lists)
            {
                int listsize = list.size();
                while(listsize < 14)
                {
                    list.add("");
                    listsize = list.size();
                }
                
                ClientInformation newrow = new ClientInformation(list.get(0).toString(), list.get(1).toString(), list.get(2).toString(), 
                        list.get(3).toString(), list.get(4).toString(), list.get(5).toString(), 
                        list.get(6).toString(), list.get(7).toString(), list.get(8).toString(), 
                        list.get(9).toString(), list.get(10).toString(), list.get(11).toString(), 
                        list.get(12).toString(), list.get(13).toString()); 
                clientinfolist.add(newrow);
            }
            
            startinglength = clientinfolist.size();
            
            // re-arrange the format 
            cilength = clientinfolist.size(); // client infomations length before setting format
            System.out.println("clients amount: " + cilength);
            clientinfolist = Formatter.setformat(clientinfolist);
            cilength = clientinfolist.size(); // client infomations length after set format and before remove duplicates
            System.out.println("clients amount: " + cilength);  
            clientinfolist = Formatter.removeDuplicates(clientinfolist);
            cilength = clientinfolist.size(); // client informations after removing duplicates
            System.out.println("clients amount: " + cilength);
            
            trasverseThroughDuplicates(clientinfolist); 
//            for (ClientInformation ci : clientinfolist)
//                System.out.println(ci.toString());

            

            // push back into the Googlesheet 
            manager.deleterows(startinglength);
            manager.appendDatas(clientinfolist);
            
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (GeneralSecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(FormatCleaner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static boolean containsDuplicates(List<ClientInformation> list)
    {
        Hashtable<String, Integer> clientDictionary = buildDictionary(list);
        Set<String> keys = clientDictionary.keySet(); 
        for (String key : keys)
        {
            if (!clientDictionary.get(key).equals(1))
            {
                return false; 
            }
        }
        return true; 
    }
    
    private static void trasverseThroughDuplicates(List<ClientInformation> list)
    {
        Hashtable<String, Integer> clientDictionary = buildDictionary(list);
        
        Set<String> keys = clientDictionary.keySet(); 
        for (String key : keys)
        { 
            if (!clientDictionary.get(key).equals(1))
            {
                System.out.println(key + ": " + clientDictionary.get(key));
            }
        }
    }
    
    private static Hashtable<String, Integer> buildDictionary(List<ClientInformation> list)
    {
        Hashtable<String, Integer> dictionary = new Hashtable<String, Integer>(1500, (float)0.45); 
        String ciname; 
        for (ClientInformation ci : list)
        {
            ciname = ci.getName1(); 
            if (dictionary.contains(ciname))
            {
                dictionary.replace(ciname, dictionary.get(ciname), dictionary.get(ciname)+1);
            }
            else
            {
                dictionary.put(ciname, 1); 
            }
        }
        
        return dictionary;
    }
    
}
