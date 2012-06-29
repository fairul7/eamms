package com.tms.fms.engineering.model;

import java.text.DecimalFormat;
import java.util.HashMap;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultDataObject;
import kacang.util.Log;

import com.tms.crm.sales.misc.DateUtil;

public class Sequence extends DefaultDataObject {
	public static String TYPE_ENGINEERING="E";
	public static String TYPE_TRANSPORT="M";
	public static String TYPE_TRANSPORT_ASSIGNMENT="S";
	public static String TYPE_SCP="A";
	public static String TYPE_POSTPRODUCTION="P";
	public static String TYPE_VTR="V";
	public static String TYPE_MANPOWER="D";
	public static String TYPE_STUDIO="B";
	public static String TYPE_OTHER="O";
	public static String TYPE_TVRO="T";
	public static HashMap SERVICE_MAP=new HashMap();
	
	{
		SERVICE_MAP.put(TYPE_SCP, "SMOSV");
		SERVICE_MAP.put(TYPE_POSTPRODUCTION, "POSTNLE");
		SERVICE_MAP.put(TYPE_VTR, "VTRMCR");
		SERVICE_MAP.put(TYPE_MANPOWER, "MAN");
		SERVICE_MAP.put(TYPE_STUDIO, "STU");
		SERVICE_MAP.put(TYPE_OTHER, "OTH");
		SERVICE_MAP.put(TYPE_TVRO, "TVRO");
	}
	
	private String type;
	private int year=0;
	private Number sequence;
	
	private Sequence() {
	}
	
	public Sequence(String type) {
		this.type=type;
	}
	
	public Sequence(String type,int year) {
		this.type=type;
		this.year=year;
	}
	public Number getSequence() {
		return sequence;
	}
	

	public void setSequence(Number sequence) {
		this.sequence = sequence;
	}
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String genarteCode(){
		String code="";
		try {
				if(year==0){
					year=DateUtil.getDate().getYear();
				}
				int newSequence=getNewSequence(type, year);
				if(type!=null ){
					code=type+DateUtil.formatDate("yyyy", DateUtil.getDate())+DateUtil.formatDate("MM", DateUtil.getDate())+formatNumberWithPrefixZero(new Integer(newSequence));
				}
			return code;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error genarteCode", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public String genarteAssignmentCode(){
		String code="";
		try {
				if(year==0){
					year=DateUtil.getDate().getYear();
				}
				int newSequence=getNewSequence(type, year);
				if(type!=null ){
					code=SERVICE_MAP.get(type)+"/"+year+"/"+formatNumberWithPrefixZero(new Integer(newSequence),"0000",4);
				}
			return code;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error genarteCode", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	private synchronized static int getNewSequence(String type, int year) throws DaoException {
		// note: synchronized code to avoid generating duplicate code
		EngineeringDao dao = (EngineeringDao) Application.getInstance().getModule(EngineeringModule.class).getDao();
		
		int existingSequence = dao.getSequence(type, year+"");
		Sequence s = new Sequence(type);
		s.setYear(year);
		
		int newSeq = existingSequence + 1;
		s.setSequence(newSeq);
		if (existingSequence == 0) {
			dao.insertSequence(s);
		} else {
			dao.updateSequence(s);
		}
		
		//Log.getLog(Sequence.class).info("getNewSequence type=" + type + " year=" + year + " newSeq=" + newSeq);
		return newSeq;
	}
	
	public String generateGroupAssignmentCode(){
		String code="";
		try {
				if(year==0){
					year=DateUtil.getDate().getYear();
				}
				int existingSequence=getNewGroupSequence(type, year);
				if(type!=null ){
					code="G/"+SERVICE_MAP.get(type)+"/"+year+"/"+formatNumberWithPrefixZero(new Integer(existingSequence+1),"0000",4);
				}
			return code;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error genarteCode", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	private synchronized static int getNewGroupSequence(String type, int year) throws DaoException {
		// note: synchronized code to avoid generating duplicate code
		EngineeringDao dao = (EngineeringDao) Application.getInstance().getModule(EngineeringModule.class).getDao();
		
		int existingSequence = dao.getSequenceGroupAssignment("G"+type,year+"");
		Sequence s = new Sequence("G"+type);
		s.setYear(year);
		
		int newSeq = existingSequence + 1;
		s.setSequence(newSeq);
		if (existingSequence == 0) {
			dao.insertSequenceGroupAssignment(s);
		} else {
			dao.updateSequenceGroupAssignment(s);
		}
		
		//Log.getLog(Sequence.class).info("getNewGroupSequence type=" + type + " year=" + year + " newSeq=" + newSeq);
		return newSeq;
	}
	
	private String formatNumberWithPrefixZero(Number n){
		 return formatNumberWithPrefixZero(n,"0000",5);
	}

	private String formatNumberWithPrefixZero(Number n, String format,int noOfDigits) {
		DecimalFormat formatter = new DecimalFormat(format);
		 formatter.setMinimumIntegerDigits(noOfDigits);
	        String s = formatter.format(n);
	    
	        return s;
	}
	
}
