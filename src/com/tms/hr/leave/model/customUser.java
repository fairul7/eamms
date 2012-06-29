package com.tms.hr.leave.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;

import kacang.model.DefaultDataObject;
import kacang.services.security.User;

public class customUser extends DefaultDataObject implements Serializable,Comparable {
	
	String id;
	String username;
	Map propertyMap;
	
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Map getPropertyMap() {
		return propertyMap;
	}
	public void setPropertyMap(Map propertyMap) {
		this.propertyMap = propertyMap;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
    public Object getProperty(String property) {
        return propertyMap.get(property);
    }
	
	public int compareTo(Object anotherPerson) throws ClassCastException {
	    if (!(anotherPerson instanceof customUser)) 
	      throw new ClassCastException("A Person object expected.");
	  
	    String anotherPersonUsername = ((customUser) anotherPerson).getUsername();  
	    return this.getUsername().compareTo(anotherPersonUsername);    
	  
	
		  
	  
	
	}
	
	public static Comparator LastNameComparator = new Comparator() {
		  public int compare(Object person, Object anotherPerson) {
		    String lastName1 = ((customUser) person).getProperty("lastName").toString().toUpperCase();
		    String firstName1 = ((customUser) person).getProperty("firstName").toString().toUpperCase();
		    String lastName2 = ((customUser) anotherPerson).getProperty("lastName").toString().toUpperCase();
		    String firstName2 = ((customUser) anotherPerson).getProperty("firstName").toString().toUpperCase();

		    if (!(lastName1.equals(lastName2)))
		      return lastName1.compareTo(lastName2);
		    else
		      return firstName1.compareTo(firstName2);
		  }
		};
		
		
		
		public static Comparator FistNameComparator = new Comparator() {
			  public int compare(Object person, Object anotherPerson) {
			    String lastName1 = ((customUser) person).getProperty("lastName").toString().toUpperCase();
			    String firstName1 = ((customUser) person).getProperty("firstName").toString().toUpperCase();
			    String lastName2 = ((customUser) anotherPerson).getProperty("lastName").toString().toUpperCase();
			    String firstName2 = ((customUser) anotherPerson).getProperty("firstName").toString().toUpperCase();

			    if (!(firstName1.equals(firstName2)))
			        return firstName1.compareTo(firstName2);
			      else
			        return lastName1.compareTo(lastName2);
			  }
			};

}
