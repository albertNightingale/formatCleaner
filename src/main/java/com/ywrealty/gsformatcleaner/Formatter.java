/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ywrealty.gsformatcleaner;

import com.ywrealty.gsformatcleaner.bean.ClientInformation;
import com.ywrealty.gsformatcleaner.util.ListUtil;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author albertliu
 */
public class Formatter {
    
    /****
     * Check if a row of ClientInformation String data is in standard format, 
     * which means:
     *      no & or no / for all not in null values
     * @param arowofdata
     * @return 
     */
    public static boolean isinstandardformat(List<String> arowofdata)
    {
        for (int i = 0; i < arowofdata.size() - 1; i++)
        {
            if (arowofdata.get(i) != null && !arowofdata.get(i).equalsIgnoreCase("null")) // not null value
                if (arowofdata.get(i).contains("&") || arowofdata.get(i).contains("/"))
                    return false; 
        }
        
        return true; 
    }
     
    /*****
     * reformat the content of each Client Information in arowofdata 
     * and return a list of formatted client informations    
     * split one data that is connected by '&' or "/" into two pieces of data. 
     * place
     * @param arowofdata
     * @return 
     */
    public static List<ClientInformation> setformat(List<ClientInformation> arowofdata)
    {
        List<ClientInformation> returndatas = new ArrayList<ClientInformation>(); 
        
        for (ClientInformation ci : arowofdata)
        {
            List<String> cidata = ci.buildcontentlist();

            String data; 
            ArrayList<String> names = new ArrayList<String>(); 
            ArrayList<String> teles = new ArrayList<String>(); 
            ArrayList<String> emails = new ArrayList<String>(); 

            // first part: Names
            for (int index = 0; index < 3; index ++) 
            {
                data = cidata.get(index);
                if (data != null && !data.equalsIgnoreCase("null") && !data.equals("") && !data.equals(" "))
                {
                    if (data.contains("&"))
                    {
                        String[] spname = data.split("&");
                        for (String name : spname)
                        {
                            names.add(name.trim()); 
                        }
                    }
                    else if (data.contains("/"))
                    {
                        String[] spname = data.split("/");
                        for (String name : spname)
                        {
                            names.add(name.trim()); 
                        }
                    }
                    else
                    {
                        names.add(data.trim()); 
                    }
                }
            }

                while (names.size() < 3) // some null values are not added
                    names.add("null");  // add empty values
            //   System.out.println(names.get(0) + " " + names.get(1) + " " + names.get(2));
                ci.setNames(names);
                
                
            // second part: Phone Numbers
            for (int index = 0; index < 3; index ++) 
            {
                data = cidata.get(index + 3);
                if (data != null && !data.equalsIgnoreCase("null") && !data.equals("") && !data.equals(" "))
                {
                    if (data.contains("&"))
                    {
                        String[] spteles = data.split("&");
                        for (String tele : spteles)
                        {
                            teles.add(leaveOnlyDigits(tele)); 
                        }
                    }
                    else if (data.contains("/"))
                    {
                        String[] spteles = data.split("/");
                        for (String tele : spteles)
                        {
                            teles.add(leaveOnlyDigits(tele)); 
                        }                            
                    }
                    else
                    {
                        teles.add(leaveOnlyDigits(data)); 
                    }
                }
            }

            while (teles.size() < 6) // some null values are not added
                teles.add("null");  // add empty values
                
            // System.out.println(teles.get(0) + " " + teles.get(1) + " " + teles.get(2));

            ci.setTeles(teles);

            // third part: email addresses    
            for (int index = 0; index < 3; index ++) 
            {
                data = cidata.get(index + 6);
                if (data != null && !data.equalsIgnoreCase("null") && !data.equals("") && !data.equals(" "))
                {
                    if (data.contains("&"))
                    {
                        String[] spemail = data.split("&");
                        for (String email : spemail)
                        {
                            emails.add(email.trim()); 
                        }
                    }
                    else if (data.contains("/"))
                    {
                        String[] spemail = data.split("/");
                        for (String email : spemail)
                        {
                            emails.add(email.trim()); 
                        }                            
                    }
                    else
                    {
                        emails.add(data.trim()); 
                    }
                }
            }

            while (emails.size() < 9) // some null values are not added
                emails.add("null");  // add empty values

            // System.out.println(emails.get(0) + " " + emails.get(1) + " " + emails.get(2));

            ci.setEmails(emails);
            
            returndatas.add(ci); 
            // System.out.println("Added: " + ci.toString());
            // System.out.println("\n\n\n"); 

        }        
        return returndatas; 
    }
    
    /*********
     * Clear out all non-digits in a string. 
     * @param s
     * @return 
     */
    private static String leaveOnlyDigits(String s)
    {
        String tmpstring = s.trim(); 
        if (tmpstring != null)
        {
            int stringlength = tmpstring.length();
            for (int i = 0; i < stringlength; i++)
            {
                if (!Character.isDigit(tmpstring.charAt(i))) // not a digit
                {
                    tmpstring = tmpstring.replace(Character.toString(tmpstring.charAt(i)), ""); 
                    stringlength = tmpstring.length(); 
                }
            }
            return tmpstring; 
        }
        else 
        {
            return null; 
        }
    }
            
    
    /**********
     * remove duplicate ClientInformations, 
     * selecting the one with the most detailed client information and delete the other, 
     * merge them if they both are the same yet containing different client information  
     * @param clientinformations
     * @return 
     */
    public static List<ClientInformation> removeDuplicates(List<ClientInformation> clientinformations)
    {
        List<ClientInformation> clientinfowoduplicates = new ArrayList<ClientInformation>();
        Hashtable<String, Integer> names = new Hashtable<String, Integer>();
        
        ClientInformation ci; 
        int i = 0; 
        for (int index = 0; index < clientinformations.size(); index++)
        {
            ci = clientinformations.get(index);
            boolean containsname1 = names.containsKey(ci.getName1());
            boolean containsname2 = names.containsKey(ci.getName2());
            boolean containsname3 = names.containsKey(ci.getName3());
            if(!containsname1 && !containsname2 && !containsname3)  
            // meaning that the contact is not related to others since it does not have same names as others
            {
                clientinfowoduplicates.add(ci); // add to the returnlist
                if(!ci.getName1().equals("null"))
                    names.put(ci.getName1(), index);
                if(!ci.getName2().equals("null"))
                    names.put(ci.getName2(), index);                
                if(!ci.getName3().equals("null"))
                    names.put(ci.getName3(), index);                            
            }
            else  // potential duplicates found, perform step 2 verification trying to find the most detailed data
            {
                i++;
                String name;
                if (containsname1)
                    name = ci.getName1();
                else if (containsname2)
                    name = ci.getName2();
                else 
                    name = ci.getName3();            
                
                ClientInformation oldci = clientinformations.get(names.get(name)); 

                if (performSecondLevelVerification(ci, oldci))  // related
                {  
                    if (removeClientInformation(clientinfowoduplicates, ci, oldci)) {
                        names.remove(name); // remove name off the hashtable  
                        names.put(ci.getName1(), index);
                    }
                } else { 
                        // not related
                    clientinfowoduplicates.add(ci);
                    System.out.println("Not Related: " + i);
                    System.out.println(ci.toString());
                    System.out.println(oldci.toString() + "\n");
                }
            }
        }        
        
        return clientinfowoduplicates;
    }
    
//    /******
//     * Verify if two ClientInformations are same based on telephone numbers
//     * @return 
//     * true if verified to be related, false if verified to be unrelated
//     */
//    private static boolean performSecondLevelVerification(ClientInformation newci, ClientInformation oldci)
//    {
//        if (newci.getTele1().equals("null") && oldci.getTele1().equals("null")
//            && newci.getTele2().equals("null") && oldci.getTele2().equals("null")
//            && newci.getTele3().equals("null") && oldci.getTele3().equals("null"))
//        {
//            return true; 
//        }
//        
//        for (int i = 0; i < 3; i ++)
//        {
//            if (!newci.getTeles().get(i).equals("null") && oldci.getTeles().contains(newci.getTeles().get(i)))
//            {
//                return true; 
//            }
//        }
//        
//        return false; 
//    }

    /*****
     * Verify if ClientInformations are same based on email address
     * @param newci
     * @param oldci
     * @return 
     * true if verified to be related, false if verified to be unrelated
     */
    private static boolean performSecondLevelVerification(ClientInformation newci, ClientInformation oldci)
    {
        if ((newci.getEmail1().equals("null") && newci.getEmail2().equals("null") && newci.getEmail3().equals("null"))
            || (oldci.getEmail1().equals("null") && oldci.getEmail2().equals("null") && oldci.getEmail3().equals("null")))
        {
            return true; 
        }
        
        List<String> newciemails = newci.getEmails(); 
        List<String> oldciemails = oldci.getEmails(); 
        
        for (int i = 0; i < 3; i ++)
            if (!newciemails.get(i).equals("null") && oldciemails.contains(newciemails.get(i)))
                return true; 
        
        for (int i = 0; i < 3; i ++)
            if (!oldciemails.get(i).equals("null") && newciemails.contains(oldciemails.get(i)))
                return true; 
        
        return false; 
    }
    
    /****
     * Verify and determine the appropriate ClientInformation to be removed; 
     * Remove the less detailed one. 
     * @param cis
     * @param newci
     * @param oldci 
     */
    private static boolean removeClientInformation(List<ClientInformation> cis, ClientInformation newci, ClientInformation oldci)
    {   
        boolean isremovesuccessful = false; 
        ClientInformation moredetailedci = compare(newci, oldci);  // compare out and return the more detailed ci
        
        if(moredetailedci != null)
        {
            boolean isequal = moredetailedci.equals(newci);
            if(isequal) // the newer one has more detail, 
                // so remove the older one and replace it with the new one
            {
                String notestobeadded = oldci.getPastnotes(); 
                // if past notes have nothing, then do nothing, else, set notestobeadded be pastnotes
                notestobeadded = notestobeadded.equals("null") ? "" : notestobeadded; 
                // if recent notes have nothing, then do nothing, else, add recent notes to the notestobeadded 
                notestobeadded = oldci.getRecentnotes().equals("null") ? notestobeadded: notestobeadded + oldci.getRecentnotes(); 
                // if newci have no past notes, then do nothing, else, just add in notestobeadded
                notestobeadded = newci.getPastnotes().equals("null") ? notestobeadded: newci.getPastnotes() + notestobeadded; 
                newci.setPastnotes(notestobeadded);  // set the notes
                
                System.out.println("remove: " + oldci.toString());
                System.out.println("because: " + newci.toString() + "\n");
                isremovesuccessful = cis.remove(oldci);
                cis.add(moredetailedci);
            }
            else // just stick to the current one, nothing changes
            {
                //System.out.println("keep : " + oldci.toString());
            }
        }       
        else // just stick to the current one, nothing changes
        {
            //System.out.println("keep : " + oldci.toString());
        }
        
        return isremovesuccessful; 
    }
    
    /***
     * Find the Client Information that is the most complete, 
     * containing the most amount of non-null datas, 
     * or least amount of data that is null. 
     * @param ci1
     * @param ci2
     * @return 
     * ci1 when ci2 has more null datas
     * ci2 when ci1 has more null datas
     * null when both has the same amount of data
     */
    private static ClientInformation compare(ClientInformation ci1, ClientInformation ci2)  
    {
        Object[] ci1datas, ci2datas;
        ci1datas = ci1.buildcontentlist().toArray();
        ci2datas = ci2.buildcontentlist().toArray(); 
        
        
        ListUtil<Object> lu = new ListUtil<Object>();
        
        if (lu.getMostDuplicates(ci1datas, ci2datas, "null") == 1)
            return ci2;
        else if (lu.getMostDuplicates(ci1datas, ci2datas, "null") == 2)
            return ci1; 
        else
            return null;
    }
    
    
    
    
}


