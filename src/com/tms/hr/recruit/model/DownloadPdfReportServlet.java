package com.tms.hr.recruit.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.util.Log;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tms.collab.messaging.model.StorageFileDataSource;

public class DownloadPdfReportServlet extends HttpServlet{
	public static final String VACANCY_TITLE="Vacancy Report-";
	int fontSize = 24;
	int fontSize1 = 12;
	int fontSize2 = 8;
	int fontSize3 = 5;
	int listingNo = 1;
	
	//main method
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String vacancyCode = request.getParameter("vacancyCode");
		//step 1
		Document document = new Document();
		try{
			//step 2: we set the ContentType and create an instance of the corresponding Writer
            PdfWriter.getInstance(document, response.getOutputStream());
            PdfWriter.getInstance(document, new FileOutputStream("web/WEB-INF/data/storage/recruit/"+VACANCY_TITLE+vacancyCode+".pdf"));
            
			//step 3
            document.open();
           
            //add table
            document.add(drawTable(vacancyCode));
            document.add(drawTableTotal(vacancyCode));
            //pdfDrawing(vacancyCode);
            
            // step 4
            //document.add(new Paragraph("Hello World to ingting"));
            //document.add(new Paragraph("the Code is " + vacancyCode));
           // document.add(new Paragraph(new Date().toString()));
             
            //pop up and download file
            //downloadFile(vacancyCode, response);
    		
            //step 5: we close the document (the outputstream is also closed internally)
    		document.close();
    		
		}catch(DocumentException de) {
            de.printStackTrace();
            System.err.println("document: " + de.getMessage());
        }
	}
	
	//method to draw pdf
	public void pdfDrawing(String vacancyCode){
		
	}
	
	//	generate pdf table
	public PdfPTable drawTable(String vacancyCode){
		Application app = Application.getInstance();
		VacancyObj vacancyObj = getData(vacancyCode);
		
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		
		Paragraph para = new Paragraph("Vacancy Code : "+vacancyCode ,FontFactory.getFont(FontFactory.HELVETICA, fontSize2, Font.BOLD));	
		PdfPCell cell = new PdfPCell(para);
		cell.setBorder(Rectangle.TOP | Rectangle.LEFT | Rectangle.RIGHT);
		//cell.setBorder(Rectangle.NO_BORDER);
		/*cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);*/
		cell.setColspan(2);
		table.addCell(cell);
		
		/*para = new Paragraph("");
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		
		para = new Paragraph("");
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);*/
		
		para = new Paragraph(app.getMessage("recruit.general.label.position") + " : " + vacancyObj.getPositionId() 
				,FontFactory.getFont(FontFactory.HELVETICA, fontSize2, Font.NORMAL));
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.TOP | Rectangle.LEFT | Rectangle.RIGHT);

		table.addCell(cell);
		
		para = new Paragraph(app.getMessage("recruit.general.label.createdBy") + " : " + vacancyObj.getCreatedBy() 
				,FontFactory.getFont(FontFactory.HELVETICA, fontSize2, Font.NORMAL));
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.TOP | Rectangle.LEFT | Rectangle.RIGHT);
		table.addCell(cell);
	
		para = new Paragraph(app.getMessage("recruit.general.label.country") + " : " + vacancyObj.getCountryId() 
				,FontFactory.getFont(FontFactory.HELVETICA, fontSize2, Font.NORMAL));
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
		table.addCell(cell);
		
		para = new Paragraph(app.getMessage("recruit.general.label.dateCreated") + " : " + vacancyObj.getCreatedDate() 
				,FontFactory.getFont(FontFactory.HELVETICA, fontSize2, Font.NORMAL));
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
		table.addCell(cell);
		
		para = new Paragraph(app.getMessage("recruit.general.label.department") + " : " + vacancyObj.getDepartmentId() 
				,FontFactory.getFont(FontFactory.HELVETICA, fontSize2, Font.NORMAL));
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
		table.addCell(cell);
		
		para = new Paragraph(app.getMessage("recruit.general.label.lastUpdatedBy") + " : " + vacancyObj.getLastUpdatedBy() 
				,FontFactory.getFont(FontFactory.HELVETICA, fontSize2, Font.NORMAL));
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
		table.addCell(cell);
		
		para = new Paragraph(app.getMessage("recruit.general.label.noOfPosition") + " : " + vacancyObj.getNoOfPositionDetail() 
				,FontFactory.getFont(FontFactory.HELVETICA, fontSize2, Font.NORMAL));
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
		table.addCell(cell);
		
		para = new Paragraph(app.getMessage("recruit.general.label.lastDateModified") + " : " + vacancyObj.getLastUpdatedDate()
				,FontFactory.getFont(FontFactory.HELVETICA, fontSize2, Font.NORMAL));
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
		table.addCell(cell);
		
		para = new Paragraph(app.getMessage("recruit.general.label.priorty") + " : " + vacancyObj.getPriorityName() 
				,FontFactory.getFont(FontFactory.HELVETICA, fontSize2, Font.NORMAL));
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT );
		table.addCell(cell);
		
		para = new Paragraph("");
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
		table.addCell(cell);
		
		para = new Paragraph("");
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
		table.addCell(cell);
		
		para = new Paragraph("");
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
		table.addCell(cell);
		
		para = new Paragraph(app.getMessage("recruit.general.label.vacancyStartDate") + " : " + vacancyObj.getStartDate()
				,FontFactory.getFont(FontFactory.HELVETICA, fontSize2, Font.NORMAL));
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
		table.addCell(cell);
		
		para = new Paragraph("");
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
		table.addCell(cell);
		
		para = new Paragraph(app.getMessage("recruit.general.label.vacancyEndDate") + " : " + vacancyObj.getEndDate()
				,FontFactory.getFont(FontFactory.HELVETICA, fontSize2, Font.NORMAL));
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
		table.addCell(cell);
		
		para = new Paragraph("");
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT  | Rectangle.BOTTOM);
		table.addCell(cell);
		
		para = new Paragraph("");
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		table.addCell(cell);
		
		para = new Paragraph(app.getMessage("recruit.general.label.jobFunctionality") ,FontFactory.getFont(FontFactory.HELVETICA, fontSize2, Font.NORMAL));
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.RIGHT | Rectangle.BOTTOM);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(2);
		table.addCell(cell);
		
		para = new Paragraph(app.getMessage("recruit.general.label.tJobRespon") ,FontFactory.getFont(FontFactory.HELVETICA, fontSize2, Font.NORMAL));
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.LEFT  | Rectangle.RIGHT | Rectangle.BOTTOM);
		table.addCell(cell);
		
		para = new Paragraph(app.getMessage("recruit.general.label.tJobRequire") ,FontFactory.getFont(FontFactory.HELVETICA, fontSize2, Font.NORMAL));
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.LEFT  | Rectangle.RIGHT  | Rectangle.BOTTOM);
		table.addCell(cell);

		/*para = new Paragraph(vacancyObj.getResponsibilities() ,FontFactory.getFont(FontFactory.HELVETICA, fontSize2, Font.NORMAL));
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.LEFT  | Rectangle.RIGHT | Rectangle.BOTTOM);
		table.addCell(cell);*/
		
		//StringReader strReader = new StringReader(vacancyObj.getResponsibilities());
		StyleSheet styles = new StyleSheet();
		styles.loadTagStyle("ol", "leading", "16,0");
		try{
			ArrayList objects = HTMLWorker.parseToList(new StringReader(vacancyObj.getResponsibilities()), styles);
			//ArrayList list = HTMLWorker.parseToList(strReader, styles);
			for (int k = 0; k < objects.size(); ++k)
				para.add((Element) objects.get(k));
			//para.setLeading(0);
		}catch(Exception e){
			
		}
		//para = new Paragraph(vacancyObj.getResponsibilities() ,FontFactory.getFont(FontFactory.HELVETICA, fontSize2, Font.NORMAL));
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.LEFT  | Rectangle.RIGHT | Rectangle.BOTTOM);
		table.addCell(cell);
		
		/*para = new Paragraph(HtmlEncoder.encode());
		StringReader sr = new String Reader(vacancyObj.getResponsibilities());
		HTMLWorker.parseToList(sr, null);*/
		
		para = new Paragraph(vacancyObj.getRequirements() ,FontFactory.getFont(FontFactory.HELVETICA, fontSize2, Font.NORMAL));
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.LEFT  | Rectangle.RIGHT | Rectangle.BOTTOM);
		table.addCell(cell);
		
		para = new Paragraph("");
		cell = new PdfPCell(para);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		table.addCell(cell);
		
		return table;
	}
	
	
	public PdfPTable drawTableTotal(String vacancyCode){
		Application app = Application.getInstance();
		VacancyObj vacancyObj = getData(vacancyCode);
		
		PdfPTable table = new PdfPTable(11);
		table.setWidthPercentage(100);
		
		Paragraph para = new Paragraph(app.getMessage("recruit.menu.label.total") ,FontFactory.getFont(FontFactory.HELVETICA, fontSize2, Font.NORMAL));
		PdfPCell cell = new PdfPCell(para);
		cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.RIGHT | Rectangle.BOTTOM);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(11);
		table.addCell(cell);
		
		Collection totalTitle = new ArrayList();
		totalTitle.add("tApplied");
		totalTitle.add("tShortlisted");
		totalTitle.add("tScheduled");
		totalTitle.add("tReScheduled");
		totalTitle.add("tReScheduledRejected");
		totalTitle.add("tJobOffered");
		totalTitle.add("tInterviewUnsuccessful");
		totalTitle.add("tJobAccepted");
		totalTitle.add("tJobRejected");
		totalTitle.add("tBlackListed");
		totalTitle.add("tViewed");
		
		for(Iterator ite = totalTitle.iterator(); ite.hasNext(); ){
			para = new Paragraph(app.getMessage("recruit.general.label."+ite.next().toString()) ,FontFactory.getFont(FontFactory.HELVETICA, fontSize2, Font.NORMAL));
			cell = new PdfPCell(para);
			cell.setBorder(Rectangle.LEFT  | Rectangle.RIGHT | Rectangle.BOTTOM);
			table.addCell(cell);
		}
		
		Collection totalTitleData = new ArrayList();
		totalTitleData.add(vacancyObj.getTotalApplied());
		totalTitleData.add(vacancyObj.getTotalShortlisted());
		totalTitleData.add(vacancyObj.getTotalScheduled());
		totalTitleData.add(vacancyObj.getTotalReScheduled());
		totalTitleData.add(vacancyObj.getTotalReScheduledRejected());
		totalTitleData.add(vacancyObj.getTotalJobOffered());
		totalTitleData.add(vacancyObj.getTotalInterviewUnsuccessful());
		totalTitleData.add(vacancyObj.getTotalJobAccepted());
		totalTitleData.add(vacancyObj.getTotalJobRejected());
		totalTitleData.add(vacancyObj.getTotalBlackListed());
		totalTitleData.add(vacancyObj.getTotalViewed());
		
		for(Iterator iter = totalTitleData.iterator(); iter.hasNext(); ){
			para = new Paragraph(iter.next().toString() ,FontFactory.getFont(FontFactory.HELVETICA, fontSize2, Font.NORMAL));
			cell = new PdfPCell(para);
			cell.setBorder(Rectangle.LEFT  | Rectangle.RIGHT | Rectangle.BOTTOM);
			table.addCell(cell);
		}
		
		return table;
	}
	
	//get all data
	public VacancyObj getData(String vacancyCode){
		Application app = Application.getInstance();
		RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
		Collection vacancyTotalCol = rm.findAllVacancyTotal(null, false, 0, 1, "", "", "", "", "", "", vacancyCode);
		HashMap map= (HashMap)vacancyTotalCol.iterator().next();
		
		VacancyObj vacancyObj = new VacancyObj();
		DateFormat dmyDateFmt = new SimpleDateFormat(app.getProperty("globalDatetimeLong"));
		//vacancy detail
		vacancyObj.setVacancyCode(vacancyCode);
		vacancyObj.setResponsibilities(map.get("responsibilities").toString());
		vacancyObj.setRequirements(map.get("requirements").toString());
		vacancyObj.setPositionId(map.get("positionDesc").toString());
		vacancyObj.setCountryId(map.get("countryDesc").toString());
		vacancyObj.setDepartmentId(map.get("departmentDesc").toString());
		vacancyObj.setCreatedBy(map.get("createdBy").toString());
		vacancyObj.setLastUpdatedBy(map.get("lastUpdatedBy").toString());
		vacancyObj.setPriorityName(map.get("priorityName").toString());
		
		/*vacancyObj.setCreatedDate(dmyDateFmt.format(map.get("createdDate")));
		vacancyObj.setLastUpdatedDate(dmyDateFmt.format(map.get("lastUpdatedDate")));
		
		vacancyObj.setStartDate(map.get("startDate"));
		vacancyObj.setEndDate(map.get("endDate"));*/
		
		if(map.get("noOfPositionOffered")!=null && !map.get("noOfPositionOffered").equals(""))
			vacancyObj.setNoOfPositionDetail(map.get("noOfPositionOffered").toString() +"/"+ map.get("noOfPosition").toString());
		else
			vacancyObj.setNoOfPositionDetail("0/"+ map.get("noOfPosition").toString());
		
		//total
		vacancyObj.setTotalApplied(Integer.parseInt(map.get("totalApplied").toString()));
		vacancyObj.setTotalShortlisted(Integer.parseInt(map.get("totalShortlisted").toString()));
		vacancyObj.setTotalScheduled(Integer.parseInt(map.get("totalScheduled").toString()));
		vacancyObj.setTotalReScheduled(Integer.parseInt(map.get("totalReScheduled").toString()));
		vacancyObj.setTotalReScheduledRejected(Integer.parseInt(map.get("totalReScheduledRejected").toString()));
		vacancyObj.setTotalJobOffered(Integer.parseInt(map.get("totalJobOffered").toString()));
		vacancyObj.setTotalInterviewUnsuccessful(Integer.parseInt(map.get("totalInterviewUnsuccessful").toString()));
		vacancyObj.setTotalJobAccepted(Integer.parseInt(map.get("totalJobAccepted").toString()));
		vacancyObj.setTotalJobRejected(Integer.parseInt(map.get("totalJobRejected").toString()));
		vacancyObj.setTotalBlackListed(Integer.parseInt(map.get("totalBlackListed").toString()));
		vacancyObj.setTotalViewed(Integer.parseInt(map.get("totalViewed").toString()));
		
		return vacancyObj;
	}
	
	//method to download file
	public void downloadFile(String vacancyCode, HttpServletResponse response) throws IOException, ServletException {
		Application app = Application.getInstance();
		SecurityService service = (SecurityService) app.getService(SecurityService.class);
		StorageService ss;
        StorageFile sf;
		try{
				ss = (StorageService) app.getService(StorageService.class);
			    sf = new StorageFile("/recruit/"+ VACANCY_TITLE+vacancyCode+".pdf");
			    sf = ss.get(sf);
			             
				String newName = sf.getName();
			    response.setHeader("Content-Disposition", "attachment; filename=\"" + newName + "\"");
			    StorageFileDataSource.copy(sf.getInputStream(), response.getOutputStream());
		}
		catch(Exception e){
			Log.getLog(getClass()).error("error in download file servlet: " + e.getMessage(), e);
			PrintWriter oute = response.getWriter();
	        oute.print(e.getMessage());
		}	
	}
	
}
