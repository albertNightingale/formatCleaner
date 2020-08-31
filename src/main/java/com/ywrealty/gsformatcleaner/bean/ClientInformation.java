/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ywrealty.gsformatcleaner.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * @author albertliu
 */
public class ClientInformation {
    private String name1, name2, name3;
    private String tele1, tele2, tele3;
    private String email1, email2, email3;
    private String mailinginfo;
    private String pastnotes, recentnotes;
    private String date; 
    private String status; 

    public ClientInformation() {
    }

    public ClientInformation(String name1, String name2, String name3, String tele1, String tele2, 
        String tele3, String email1, String email2, String email3, String mailinginfo, 
        String pastnotes, String recentnotes, String date, String status) 
    {
        
        if (name1.equals("")) this.name1 = "null"; 
        else this.name1 = name1;
        
        if (name2.equals("")) this.name2 = "null";
        else this.name2 = name2;
        
        if (name3.equals("")) this.name3 = "null";
        else this.name3 = name3;
        
        if (tele1.equals("")) this.tele1 = "null";
        else this.tele1 = tele1;    
        
        if (tele2.equals("")) this.tele2 = "null";
        else this.tele2 = tele2;          
        
        if (tele3.equals("")) this.tele3 = "null";
        else this.tele3 = tele3;          

        if (email1.equals("")) this.email1 = "null";
        else this.email1 = email1;       

        if (email2.equals("")) this.email2 = "null";
        else this.email2 = email2;       

        if (email3.equals("")) this.email3 = "null";
        else this.email3 = email3;       

        if (mailinginfo.equals("")) this.mailinginfo = "null";
        else this.mailinginfo = mailinginfo;       
        
        if (pastnotes.equals("")) this.pastnotes = "null"; 
        else this.pastnotes = pastnotes;  

        if (recentnotes.equals("")) this.recentnotes = "null"; 
        else this.recentnotes = recentnotes;  
        
        if (date.equals("")) this.date = "null";
        else this.date = date; 
        
        if (status.equals("")) this.status = "null"; 
        else this.status = status; 

    }

    public String getPastnotes() {
        return pastnotes;
    }

    public String getRecentnotes() {
        return recentnotes;
    }

    public String getStatus() {
        return status;
    }

    public String getEmail1() {
        return email1;
    }

    public String getEmail2() {
        return email2;
    }

    public String getEmail3() {
        return email3;
    }

    public String getMailinginfo() {
        return mailinginfo;
    }

    public String getName1() {
        return name1;
    }

    public String getName2() {
        return name2;
    }

    public String getName3() {
        return name3;
    }

    public String getTele1() {
        return tele1;
    }

    public String getTele2() {
        return tele2;
    }

    public String getTele3() {
        return tele3;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    public void setMailinginfo(String mailinginfo) {
        this.mailinginfo = mailinginfo;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public void setTele1(String tele1) {
        this.tele1 = tele1;
    }

    public void setTele2(String tele2) {
        this.tele2 = tele2;
    }

    public void setTele3(String tele3) {
        this.tele3 = tele3;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    public void setPastnotes(String pastnotes) {
        this.pastnotes = pastnotes;
    }

    public void setRecentnotes(String recentnotes) {
        this.recentnotes = recentnotes;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public ArrayList<String> buildcontentlist()
    {
        String[] objectinalist = {name1, name2, name3,
            tele1, tele2, tele3,
            email1, email2, email3,
            mailinginfo};
        ArrayList<String> list = new ArrayList<String>();
        for (String var : objectinalist)
            list.add(var);
        return list; 
    }
    
    public void setNames(List<String> names)
    {
        this.setName1(names.get(0));
        this.setName2(names.get(1));
        this.setName3(names.get(2));
    }
    
    public List<String> getNames()
    {
        List<String> names = new ArrayList<String>();
        names.add(name1);
        names.add(name2);
        names.add(name3);
        return names; 
    }
    
    public void setTeles(List<String> teles)
    {
        this.setTele1(teles.get(0));
        this.setTele2(teles.get(1));
        this.setTele3(teles.get(2));
    }
    
    public List<String> getTeles()
    {
        List<String> teles = new ArrayList<String>();
        teles.add(tele1);
        teles.add(tele2);
        teles.add(tele3);
        return teles; 
    }
    
    public void setEmails(List<String> emails)
    {
        this.setEmail1(emails.get(0));
        this.setEmail2(emails.get(1));
        this.setEmail3(emails.get(2));        
    }
    
    public List<String> getEmails()
    {
        List<String> emails = new ArrayList<String>();
        emails.add(email1);
        emails.add(email2);
        emails.add(email3);
        return emails; 
    }
    
    @Override
    public String toString()       
    {
        return name1 + ", " + name2 + ", " + name3 + ", " + 
            tele1 + ", " + tele2 + ", " + tele3 + ", " + 
            email1 + ", " + email2 + ", " + email3 + ", " + 
            mailinginfo + ", " + date + ", " + status; 
    }
}
