package com.tms.fms.eamms.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.services.security.User;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tms.fms.eamms.model.EammsModule;
import com.tms.util.MailUtil;

public class TXMReport  extends HttpServlet{
	private static Font headerFont = new Font(Font.TIMES_ROMAN, 14,Font.BOLD);
	private static Font textFont = new Font(Font.TIMES_ROMAN, 8);
	private static Font textBoldFont = new Font(Font.TIMES_ROMAN, 8,Font.BOLD);
	
	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException{
		String mode =  req.getParameter("mode");
		String fileName =  req.getParameter("txmReportId")+"TM.pdf";
		String reportId = req.getParameter("id");
		String preparedBy = req.getParameter("preparedBy");
		
		
		try {			
			ByteArrayOutputStream baosPDF = createPdf(reportId, preparedBy);
			
			if("print".equals(mode)){							
				
				resp.setContentType("application/pdf");		
				resp.setHeader("Content-Disposition","inline;filename="+fileName);
				resp.setContentLength(baosPDF.size());
				ServletOutputStream stream = resp.getOutputStream();
				baosPDF.writeTo(stream);
				
				stream.flush();
				stream.close();	
			}	
			
			if("mail".equals(mode)){
				HashMap info = new HashMap();
				info.put("toEmail", req.getParameter("toEmail"));
				info.put("ccEmail",req.getParameter("ccEmail"));
				info.put("bccEmail", req.getParameter("bccEmail"));
				info.put("subject", req.getParameter("subject"));
				info.put("msgBody", req.getParameter("msgBody"));
				
				
				ArrayList<DefaultDataObject> attachment = new ArrayList();
				
				DefaultDataObject attch = new DefaultDataObject();
				
				attch.setProperty("fileName", "TXMReport.pdf");
				attch.setProperty("attch", baosPDF);
				attch.setProperty("type", "application/pdf");
				attachment.add(attch);
				
				info.put("attachment", attachment); 
				sendMail( info);
			}
			
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		
			  
	}
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException{
		String mode =  req.getParameter("mode");
		String fileName =  req.getParameter("txmReportId")+"TM.pdf";
		String reportId = req.getParameter("id");
		String preparedBy = req.getParameter("preparedBy");
		
		try {			
			ByteArrayOutputStream baosPDF = createPdf(reportId, preparedBy);
			
			if("print".equals(mode)){							
				
				resp.setContentType("application/pdf");		
				resp.setHeader("Content-Disposition","inline;filename="+fileName);
				resp.setContentLength(baosPDF.size());
				ServletOutputStream stream = resp.getOutputStream();
				baosPDF.writeTo(stream);
				
				stream.flush();
				stream.close();	
			}	
			
			if("mail".equals(mode)){
				HashMap info = new HashMap();
				info.put("fromEmail", req.getParameter("fromEmail"));
				info.put("toEmail", req.getParameter("toEmail"));
				info.put("ccEmail",req.getParameter("ccEmail"));
				info.put("bccEmail", req.getParameter("bccEmail"));
				info.put("subject", req.getParameter("subject"));
				info.put("msgBody", req.getParameter("msgBody"));
				
				
				ArrayList<DefaultDataObject> attachment = new ArrayList();
				
				DefaultDataObject attch = new DefaultDataObject();
				
				byte[] bytes = baosPDF.toByteArray();
				
				attch.setProperty("fileName", fileName);
				attch.setProperty("attch", bytes);
				attch.setProperty("type", "application/pdf");
				attachment.add(attch);
				
				info.put("attachment", attachment); 
				sendMail( info);
				
			}
			
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}	
			  
	}
	
	private static ByteArrayOutputStream createPdf(String reportId, String preparedBy) throws DocumentException{
		Document doc = new Document();
		ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
		PdfWriter docWriter = null;
	
		docWriter = PdfWriter.getInstance(doc, baosPDF);	
		doc.open();
		addContent(doc,reportId, preparedBy);
		doc.close();
		docWriter.close();
		
		return baosPDF;
	}
	
			  
	  private static void addContent(Document document, String reportId, String preparedBy) throws DocumentException {
		
		      EammsModule mod = (EammsModule)Application.getInstance().getModule(EammsModule.class);
			  DefaultDataObject obj = mod.getTXMReportInfo(reportId); 
		  
			  Paragraph paragraph = new Paragraph("NETWORK OPERATION", headerFont);
		      paragraph.setAlignment(Element.ALIGN_CENTER);
		      document.add(paragraph);
		      
		      paragraph = new Paragraph(obj.getProperty("channel") + " DAILY TXM REPORT", headerFont);
		      paragraph.setAlignment(Element.ALIGN_CENTER);
		      document.add(paragraph);
		      
		      addEmptyLine(document,2);
		      
		      document.add(new Paragraph("Date             : " + obj.getProperty("c_date"),textFont));
		      document.add(new Paragraph("Start of TX  : " + obj.getProperty("c_txStart"),textFont));
		      document.add(new Paragraph("End of TX   : " + obj.getProperty("c_txEnd"),textFont));
		      document.add(new Paragraph("Shift            : " + obj.getProperty("c_shift"),textFont));	
		      
		      document.add(createTable(obj));//add table		      
		      
		      addEmptyLine(document,1);	
		      paragraph = new Paragraph("REMARKS",textBoldFont);
		      document.add(paragraph);	
		      Collection col = mod.getTXMReportRemarksInfo(reportId);
		      
		      for(Iterator iter= col.iterator();iter.hasNext();){
		    	  HashMap map = (HashMap) iter.next();
		    	  document.add(new Paragraph("  • " + map.get("c_remarks"),textFont));
		      }	     
		      
			  
			  addEmptyLine(document,2);
			  
			  
			  paragraph = new Paragraph("Prepared By : " + preparedBy,textFont);
			  document.add(paragraph);
		
		  
	  }
	  
		  private static PdfPTable createTable(DefaultDataObject obj)  throws BadElementException {
			  
			  PdfPTable tableBorder = new PdfPTable(1);
			  PdfPCell c1 = new PdfPCell(new Phrase("PROGRAMME : "+obj.getProperty("c_programme"),textBoldFont));
			  c1.setHorizontalAlignment(Element.ALIGN_LEFT);			
			  tableBorder.addCell(c1);
			  
			    PdfPTable table = new PdfPTable(4);//inner table
			   
			    PdfPCell c21 = new PdfPCell(new Phrase("PROG. START",textFont));	  
			    c21.setBorder(0);
			    table.addCell(c21);
			    
			    c21 = new PdfPCell(new Phrase(": "+obj.getProperty("c_startProg") ,textFont));	  
			    c21.setBorder(0);
			    table.addCell(c21);
			    
			    c21 = new PdfPCell(new Phrase("DURATION" ,textFont));	  
			    c21.setBorder(0);
			    table.addCell(c21);
			    
			    c21 = new PdfPCell(new Phrase(": " + obj.getProperty("c_progDuration"),textFont));	  
			    c21.setBorder(0);		    
			    table.addCell(c21);
			    
			    c21 = new PdfPCell(new Phrase("TIME OCCURED",textFont));	  
			    c21.setBorder(0);
			    table.addCell(c21);
			   
			    
			    c21 = new PdfPCell(new Phrase(": "+obj.getProperty("c_timeOccur"),textFont));	  
			    c21.setBorder(0);
			    table.addCell(c21);
			    
			    c21 = new PdfPCell(new Phrase("DURATION",textFont));	  
			    c21.setBorder(0);	    
			    
			    table.addCell(c21);
			    
			    c21 = new PdfPCell(new Phrase(": "+obj.getProperty("c_faultDuration"),textFont));	  
			    c21.setBorder(0);
			    table.addCell(c21);
			    
			    c21 = new PdfPCell(new Phrase("NATURE OF FAULT",textFont));	  
			    c21.setBorder(0);
			    table.addCell(c21);
			    		    
			    c21 = new PdfPCell(new Phrase(": "+obj.getProperty("c_natureOfFault"),textFont));	  
			    c21.setBorder(0);
			    c21.setColspan(3);
			    table.addCell(c21);
			    
			    c21 = new PdfPCell(new Phrase("ACTION TAKEN",textFont));	  
			    c21.setBorder(0);
			    table.addCell(c21);
			    		    
			    c21 = new PdfPCell(new Phrase(": "+obj.getProperty("c_action"),textFont));	  
			    c21.setBorder(0);
			    c21.setColspan(3);
			    table.addCell(c21);
			    
			    c21 = new PdfPCell(new Phrase("FOLLOW UP ACTION",textFont));	  
			    c21.setBorder(0);
			    table.addCell(c21);
			    
			    EammsModule mod = (EammsModule)Application.getInstance().getModule(EammsModule.class);
			    c21 = new PdfPCell(new Phrase(": " + mod.getTXMReportFollowupInfo(obj.getId()),textFont));	  
			    c21.setBorder(0);
			    c21.setColspan(3);
			    table.addCell(c21);
			    
			    PdfPCell c2 = new PdfPCell(table);
			    tableBorder.addCell(c2);
			
			    return tableBorder;

		  }
		  
		  private static void addEmptyLine(Document document, int line)
	      throws DocumentException {
			  for(int i=0; i<line;i++){
				  document.add(new Paragraph(" "));
			  }
			  
		  }
		  
		  private static void sendMail(HashMap info)
			{
			 				
				
				String smtpServer = (String) Application.getInstance().getProperty("smtp.server");
				String fromEmail = (String) info.get("fromEmail");
				String subject = (String) info.get("subject");
				String msgBody = (String) info.get("msgBody");
				
				
				String toEmail = (String) info.get("toEmail");
				String ccEmail = (String) info.get("ccEmail");
				String bccEmail = (String) info.get("bccEmail");
				
				ArrayList<DefaultDataObject> attachment = (ArrayList) info.get("attachment");
				MailUtil.sendEmailNow(smtpServer, fromEmail, toEmail, ccEmail, bccEmail, subject, msgBody, attachment);
			}
	

}
