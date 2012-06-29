package com.tms.quotation.model;

import java.util.Date;
import kacang.Application;
import kacang.model.DefaultDataObject;


public class CustomerDataObject extends DefaultDataObject {

  private String customerId;
  private String contactFirstName;
  private String contactLastName;
  private String companyName;
  private String address1;
  private String address2;
  private String address3;
  private String postcode;
  private String state;
  private String country;
  private String gender;
  private String salutation;
  private String active;
  
//  private String tableAction;
  
  public CustomerDataObject() {}
/*  public String getTableAction(){ 
	  return "<table><tbody><tr><td><a href=?id="+ customerId +"><img height='15' width='15' border='0' src='images/edit_trash.gif'></a></td></tr></tbody></table>"; 
  }*/

  public String getCustomerId() {
    return customerId;
  }

  public String getContactFirstName() {
    return contactFirstName;
  }

  public String getContactLastName() {
    return contactLastName;
  }

  public String getCompanyName() {
    return companyName;
  }

  public String getAddress1() {
    return address1;
  }

  public String getAddress2() {
    return address2;
  }

  public String getAddress3() {
    return address3;
  }

  public String getPostcode() {
    return postcode;
  }

  public String getState() {
    return state;
  }

  public String getCountry() {
    return country;
  }

  public String getGender() {
    return gender;
  }

  public String getSalutation() {
    return salutation;
  }
  
  public String getActive() {
    return active;
  }

  /* setter methods */

  public void setCustomerId(String string) {
    customerId = string;
  }

  public void setContactFirstName(String string) {
    contactFirstName = string;
  }

  public void setContactLastName(String string) {
    contactLastName = string;
  }

  public void setCompanyName(String string) {
    companyName = string;
  }

  public void setAddress1(String string) {
    address1 = string;
  }

  public void setAddress2(String string) {
    address2 = string;
  }

  public void setAddress3(String string) {
    address3 = string;
  }

  public void setPostcode(String string) {
    postcode = string;
  }

  public void setState(String string) {
    state = string;
  }

  public void setCountry(String string) {
    country = string;
  }

  public void setGender(String string) {
    gender = string;
  }

  public void setSalutation(String string) {
    salutation = string;
  }

  public void setActive(String active) {
    this.active = active;
  }

}