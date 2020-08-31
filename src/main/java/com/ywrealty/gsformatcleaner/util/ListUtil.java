/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ywrealty.gsformatcleaner.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author albertliu
 */
public class ListUtil<T> {
    
    public List<String> tostringlist(List<T> objlist)
    {
        List<String> strlist = new ArrayList<>();
        
        for (T t : objlist)
            strlist.add(t.toString()); 
        return strlist;
    }
    
    public List<Object> toobjectlist(List<T> strlist) throws ClassCastException
    {
        List<Object> objlist = new ArrayList<Object>();
        
        for (T t : strlist)
            objlist.add(t); 
        return objlist;
    }

    public void trasversalprint(List<T> list)
    {
        for (T t : list)
            System.out.print(t + "," );
        System.out.println();
    }
    
    /******
     * find the list that contains the most of passed in data in its list 
     * return 1 for t1
     * return 2 for t2
     * @param t1
     * @param t2
     * @param data
     * @return 
     */
    public int getMostDuplicates(T[] t1, T[] t2, T data)
    {
        int t1count = 0, t2count = 0;
        for (T t: t1)
            if(t.equals(data))
                t1count++; 
        for (T t: t2)
            if(t.equals(data))
                t2count++; 
        
        if (t1count > t2count)
            return 1;
        else if (t1count < t2count)
            return 2;
        else
            return 0; 
    }
    
     /*****
     * Check if a rowofdata is full
     * @param List<T> to be checked 
     * @return 
     */
    public boolean isfull(List<T> arowofdata) 
    {
        for (T data : arowofdata)
        {
            if (data == null)
                return false; 
            
            if (data instanceof String)
            {
                if(((String) data).equalsIgnoreCase("null"))
                    return false; 
                if (((String) data).equals(""))
                    return false; 
                if (((String) data).equals(" ") || ((String) data).equals("  "))
                    return false; 
            }
        }
        
        return true; 
    }
    
    public boolean isAllNull(List<T> list)
    {
        for (T t : list)
        {
            if (t != null)
            {
                if (t instanceof String)
                {
                    if (!((String) t).equalsIgnoreCase("null")) 
                    {
                        return false;
                    }
                    else
                    {
                        continue; 
                    }
                }
                else
                {
                    return false; 
                }
            }
        }
        return true; 
    }
    
    
    /*****
     * check if there is at least one element in list1 that matches list2 except elements in the exception List
     * null is auto handled by the exceptionlist so if a data is null, it will not be verified 
     * @param list1
     * @param list2
     * @param exceptionlist 
     * @return 
     */  
    // still contains bug, but works fine here
    public boolean containAtLeastOneSame(List<T> list1, List<T> list2, List<T> exceptionlist)
    {
        if (isAllSame(list1) || isAllSame(list2))
            return true; 
        
        for (T t : list1)
        {
            if ((t != null) && !exceptionlist.contains(t))
            {
                if (list2.contains(t))
                {
                    return true; 
                }
            }
        }
        
        return false; 
    }
    
    
    // still contains bugs, but works fine for here
    public boolean isAllSame(List<T> list1)
    {

        T t; 
        if (list1.size() <= 1)
            return true; 
        else
            t = list1.get(0); 
        
        for (int i = 1; i < list1.size(); i++)
        {
            if (!list1.get(i).equals(t))
                return false; 
        }
        return true; 
    }
    
    
    /****
     * verify if they are containing the same thing 
     * despite they have different order
     * @param list1
     * @param list2
     * @return 
     */
    public boolean isContainingSameData(List<T> list1, List<T> list2)    
    {
        if (list1.equals(list2))
        {
            return true; 
        }
        else
        {
            int list2size = list2.size(); 
            int list1size = list1.size(); 
            if (list1size == list2size)
            {
                for (int i = 0; i < list1size; i ++)  
                {
                    if (!list2.contains(list1.get(i)) || !list1.contains(list2.get(i))) 
                    // if list 1 does not contain something in list 2  or list 2 does not contain something in list 1. 
                    {
                        return false; 
                    }
                }
                return true; // list1 contains everything in list2 and list2 contains everything in list1. 
            }
            else
            {
                return false; 
            }
        }
    }
    
}
