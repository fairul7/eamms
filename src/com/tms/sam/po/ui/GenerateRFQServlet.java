package com.tms.sam.po.ui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kacang.Application;
import kacang.services.storage.StorageDirectory;
import kacang.services.storage.StorageException;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.util.Log;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tms.collab.messaging.model.StorageFileDataSource;
import com.tms.ekms.setup.model.SetupException;
import com.tms.ekms.setup.model.SetupModule;
import com.tms.sam.po.model.SupplierModule;
import com.tms.sam.po.model.SupplierObject;

public class GenerateRFQServlet extends HttpServlet{
	public static final String STORAGE_ROOT = "purchaseOrdering/RFQ";
	private static final int BUFFER = 2048;
	private String fileName="";
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StorageService storage = (StorageService) Application.getInstance().getService(StorageService.class);
		String zipOutputFolder = storage.getRootPath() + "/" + STORAGE_ROOT + "/";
		Application app = Application.getInstance();
		SupplierModule module = (SupplierModule)app.getModule(SupplierModule.class);
		BufferedInputStream origin = null;
		ZipOutputStream out = null;
		String zipOutputFileName = "RFQ_"+ request.getSession().getId() + ".zip";
		boolean success = true;
		HttpSession session = request.getSession();
		if(session != null) {
			if(session.getAttribute("ppID") != null) {
			//	generate RFQs in pdf form
				
				String ppID = session.getAttribute("ppID").toString();
				Collection numSupplier = module.supplierInfo(ppID);
				Collection requester = module.getRequester(ppID);
				Map propertyMap = new HashMap();
		        Application application = Application.getInstance();
		        SetupModule setup = (SetupModule)application.getModule(SetupModule.class);
		        try {
		            propertyMap = setup.getAll();
		        } catch (SetupException e) {
		            throw new RuntimeException("Error loading setup properties: " + e.toString());
		        }
				String logoPath = propertyMap.get("siteUrl").toString()+"/ekms/"+propertyMap.get("siteLogo").toString();
				String name = module.getInfo("Company Name");
				String addr = module.getInfo("Company Address");
				String tel = module.getInfo("Company Tel");
				String fax = module.getInfo("Company Fax");
				int fontSize = 24;
				int fontSize2 = 9;
				int fontSize3 = 7;
				int listingNo;
				
				for(Iterator i=numSupplier.iterator();i.hasNext();){
					SupplierObject obj= (SupplierObject)i.next();
					Document document = new Document(PageSize.A4, 20, 20, 40, 10);
					try {
						// step 2:
						// we create a writer that listens to the document
						// and directs a PDF-stream to a file
						StorageDirectory sd = new StorageDirectory("/"+STORAGE_ROOT+"/");
						storage.store(sd);
						fileName = obj.getLastKnownCompany()+obj.getCounting()+"_"+obj.getProperty("purchaseCode");
						
						PdfWriter.getInstance(document,	new FileOutputStream(zipOutputFolder+fileName+".pdf"));
						// step 3: we open the document
						document.open();
						//StorageService ss = (StorageService)Application.getInstance().getService(StorageService.class);
						//ss.getRootPath();
						Image logo = Image.getInstance(logoPath);
						logo.scalePercent(100, 100);
						
						// step 4: we add a paragraph to the document
						PdfPTable headerTable = new PdfPTable(2);
						headerTable.setWidthPercentage(100);
						
						PdfPCell cell = cell = new PdfPCell(logo, false);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						headerTable.addCell(cell);
						Paragraph para = new Paragraph(app.getMessage("po.label.requestForQuotation"),FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize, Font.BOLDITALIC));
						cell = new PdfPCell(para);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						headerTable.addCell(cell);
						
						PdfPTable infoTable = new PdfPTable(1);
						infoTable.setWidthPercentage(100);
						para =  new Paragraph("\n" + name + ", "+  addr,
								FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize3, Font.BOLDITALIC));
						cell = new PdfPCell(para);
						cell.setBorder(Rectangle.NO_BORDER);
						infoTable.addCell(cell);
						
						para = new Paragraph(app.getMessage("po.label.tel")+": "+ tel +", "+app.getMessage("po.label.fax")+": "+ fax + "\n",
								FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize3, Font.BOLDITALIC));
						cell = new PdfPCell(para);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						infoTable.addCell(cell);
						
						para = new Paragraph("",
								FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize3, Font.ITALIC));
						cell = new PdfPCell(para);
						cell.setBorder(Rectangle.BOTTOM);
						cell.setFixedHeight(10);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						infoTable.addCell(cell);
						
						para = new Paragraph("",
								FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize3, Font.ITALIC));
						cell = new PdfPCell(para);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setFixedHeight(15);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						infoTable.addCell(cell);
						
						//to
						PdfPTable dataTable = new PdfPTable(3);
						float width[] = {0.07f, 0.5f, 0.42f};
						dataTable.setHorizontalAlignment(Element.ALIGN_CENTER);
						dataTable.setWidths(width);
						dataTable.setWidthPercentage(100);
											
						Paragraph dataPara = new Paragraph(app.getMessage("po.label.to")+": \t", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(dataPara);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						cell.setFixedHeight(22);
						dataTable.addCell(cell);
						
						
						dataPara =new Paragraph(obj.getLastKnownCompany(), FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2));
						cell = new PdfPCell(dataPara);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						dataTable.addCell(cell);
					
						//date
						PdfPTable innerTable = new PdfPTable(2);
						innerTable.setWidthPercentage(100);
						
						dataPara =new Paragraph(app.getMessage("po.label.date")+": \t", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(dataPara);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						innerTable.addCell(cell);
						DateFormat dmyDateFmt = new SimpleDateFormat(app.getProperty("globalDateLong"));
						dataPara =new Paragraph(dmyDateFmt.format(new Date()), FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2));
						cell = new PdfPCell(dataPara);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						innerTable.addCell(cell);
						
						dataPara =new Paragraph(app.getMessage("po.label.refNo")+": \t", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(dataPara);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						innerTable.addCell(cell);
						
						dataPara =new Paragraph(obj.getProperty("purchaseCode").toString(), FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2));
						cell = new PdfPCell(dataPara);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						innerTable.addCell(cell);
						
						dataPara =new Paragraph(app.getMessage("po.label.deliveryDateRequested")+": \t", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(dataPara);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						innerTable.addCell(cell);
						
						dataPara =new Paragraph(dmyDateFmt.format(obj.getDateDelivery()), FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2));
						cell = new PdfPCell(dataPara);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						innerTable.addCell(cell);
						
						cell = new PdfPCell(innerTable);
						cell.setBorder(Rectangle.NO_BORDER);
						dataTable.addCell(cell);
						
						//attn
						PdfPTable attnTable = new PdfPTable(2);
						float width2[] = {0.04f, 0.5f};
						attnTable.setHorizontalAlignment(Element.ALIGN_CENTER);
						attnTable.setWidths(width2);
						attnTable.setWidthPercentage(100);
						
						Paragraph attnPara = new Paragraph(app.getMessage("po.label.attn")+": \t", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(attnPara);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						cell.setFixedHeight(22);
						attnTable.addCell(cell);
						
						attnPara =new Paragraph(obj.getLastKnownSuppName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2));
						cell = new PdfPCell(attnPara);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						attnTable.addCell(cell);
						
						attnPara =new Paragraph("", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2));
						cell = new PdfPCell(attnPara);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setFixedHeight(15);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						attnTable.addCell(cell);
						
						attnPara =new Paragraph("", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2));
						cell = new PdfPCell(attnPara);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setFixedHeight(15);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						attnTable.addCell(cell);
						
						//supplier
						PdfPTable supplierTable = new PdfPTable(1);
						supplierTable.setWidthPercentage(45);
						supplierTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
						para =  new Paragraph(app.getMessage("supplier.label.supp"),
								FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLDITALIC));
						cell = new PdfPCell(para);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						supplierTable.addCell(cell);
						
						PdfPTable itemTable = new PdfPTable(8);
						float width3[] = {0.04f, 0.08f, 0.13f, 0.15f, 0.15f, 0.15f, 0.15f, 0.15f};
						itemTable.setHorizontalAlignment(Element.ALIGN_CENTER);
						itemTable.setWidths(width3);
						itemTable.setWidthPercentage(100);
						
						//item table header
						Paragraph itemPara = new Paragraph(app.getMessage("po.label.no"), FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(itemPara);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
						itemTable.addCell(cell);
						
						itemPara =new Paragraph(app.getMessage("po.label.itemCode"), FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(itemPara);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
						itemTable.addCell(cell);
						
						itemPara = new Paragraph(app.getMessage("po.label.itemDesc"), FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(itemPara);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
						itemTable.addCell(cell);
						
						itemPara =new Paragraph(app.getMessage("po.label.qtyOrdered"), FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(itemPara);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
						itemTable.addCell(cell);
						
						itemPara = new Paragraph(app.getMessage("po.label.unitOfMeasure"), FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(itemPara);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
						itemTable.addCell(cell);
						
						itemPara =new Paragraph(app.getMessage("po.label.qtyInStock"), FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(itemPara);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
						itemTable.addCell(cell);
						
						itemPara = new Paragraph(app.getMessage("po.label.unitPrice"), FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(itemPara);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
						itemTable.addCell(cell);
						
						itemPara =new Paragraph(app.getMessage("po.label.amount"), FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(itemPara);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
						itemTable.addCell(cell);
						
						//item listing
						Collection items = obj.getItemID();
						listingNo = 1;
						for(Iterator j=items.iterator();j.hasNext();){
							HashMap map = (HashMap)j.next();
							itemPara = new Paragraph(String.valueOf(listingNo), FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
							cell = new PdfPCell(itemPara);
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							itemTable.addCell(cell);
							
							itemPara =new Paragraph(map.get("itemCode").toString(), FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
							cell = new PdfPCell(itemPara);
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							itemTable.addCell(cell);
							
							itemPara = new Paragraph(map.get("itemDesc").toString(), FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
							cell = new PdfPCell(itemPara);
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							itemTable.addCell(cell);
							
							itemPara =new Paragraph(map.get("qty").toString(), FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
							cell = new PdfPCell(itemPara);
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							itemTable.addCell(cell);
							
							itemPara = new Paragraph(map.get("unitOfMeasure").toString(), FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
							cell = new PdfPCell(itemPara);
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							itemTable.addCell(cell);
							
							itemPara =new Paragraph("", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
							cell = new PdfPCell(itemPara);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							itemTable.addCell(cell);
							
							itemPara = new Paragraph("", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
							cell = new PdfPCell(itemPara);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							itemTable.addCell(cell);
							
							itemPara =new Paragraph("", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
							cell = new PdfPCell(itemPara);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							itemTable.addCell(cell);
							
							listingNo++;
						}
						
						
						//item total and discount
						PdfPTable totalTable = new PdfPTable(2);
						totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
						totalTable.setWidthPercentage(30);
						
						Paragraph totalPara = new Paragraph(app.getMessage("po.label.discount"), FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(totalPara);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						totalTable.addCell(cell);
						
						totalPara =new Paragraph("", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2));
						cell = new PdfPCell(totalPara);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						totalTable.addCell(cell);
						
						totalPara = new Paragraph(app.getMessage("po.label.total"), FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(totalPara);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						totalTable.addCell(cell);
						
						totalPara =new Paragraph("", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2));
						cell = new PdfPCell(totalPara);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						totalTable.addCell(cell);
						
						totalPara = new Paragraph("\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(totalPara);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setFixedHeight(15);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						totalTable.addCell(cell);
						
						totalPara =new Paragraph("", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2));
						cell = new PdfPCell(totalPara);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						totalTable.addCell(cell);
						
						//quotation details
						
						PdfPTable quotationTable = new PdfPTable(1);
						quotationTable.setWidthPercentage(100);
						Paragraph quotationPara =  new Paragraph(app.getMessage("po.label.quationDetailsSpecialInstruction")+":", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(quotationPara);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						
						quotationTable.addCell(cell);
						
						quotationPara = new Paragraph("-\t "+ obj.getQuotationDetails()+"\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.ITALIC));
						cell = new PdfPCell(quotationPara);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell.setExtraParagraphSpace(50f);
						quotationTable.addCell(cell);
						
						quotationPara =new Paragraph("\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2));
						cell = new PdfPCell(quotationPara);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setFixedHeight(20);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						quotationTable.addCell(cell);
						
						//remarks
						
						PdfPTable remarkTable = new PdfPTable(1);
						remarkTable.setWidthPercentage(100);
						Paragraph remarkPara =  new Paragraph(app.getMessage("po.label.supplierRemarks")+":", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(remarkPara);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						remarkTable.addCell(cell);
						
						remarkPara = new Paragraph("-\t "+app.getMessage("po.label.quotationValid ")+" ______ "+app.getMessage("po.label.day"), FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.ITALIC));
						cell = new PdfPCell(remarkPara);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell.setExtraParagraphSpace(50f);
						remarkTable.addCell(cell);
						
						remarkPara = new Paragraph("\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.ITALIC));
						cell = new PdfPCell(remarkPara);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setFixedHeight(15);
						remarkTable.addCell(cell);
						
						// requester
						PdfPTable bigTable = new PdfPTable(2);
						float width4[] = {0.6f, 0.4f};
						bigTable.setWidths(width4);
						bigTable.setHorizontalAlignment(Element.ALIGN_CENTER);
						bigTable.setWidthPercentage(100);
						
						PdfPTable requestTable = new PdfPTable(2);
						float width5[] = {0.2f,0.8f};
						requestTable.setWidths(width5);
						requestTable.setHorizontalAlignment(Element.ALIGN_LEFT);
						requestTable.setWidthPercentage(100);
						
						Paragraph requestPara = new Paragraph(app.getMessage("po.label.requestedBy")+": \t", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(requestPara);

						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						requestTable.addCell(cell);
						String rName="";
						String rTel="";
						if (requester.size() == 1) {
							Iterator iterator = requester.iterator();
							HashMap map = (HashMap) iterator.next();
							rName = map.get("firstname").toString()+" "+map.get("lastname").toString();
							rTel = map.get("telMobile").toString();
						}
						requestPara =new Paragraph(rName, FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.UNDERLINE));
						cell = new PdfPCell(requestPara);

						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						requestTable.addCell(cell);
						
						requestPara = new Paragraph("Contact: ", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(requestPara);

						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						requestTable.addCell(cell);
						
						requestPara =new Paragraph(rTel, FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.UNDERLINE));
						cell = new PdfPCell(requestPara);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						requestTable.addCell(cell);
						
						PdfPTable quotedTable = new PdfPTable(2);
						requestTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
						requestTable.setWidthPercentage(100);
						
						Paragraph quotedPara = new Paragraph("Quoted by:", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(quotedPara);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						quotedTable.addCell(cell);
						
						quotedPara =new Paragraph("_______________________", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2));
						cell = new PdfPCell(quotedPara);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						quotedTable.addCell(cell);
						
						quotedPara = new Paragraph("Date:", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(quotedPara);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						quotedTable.addCell(cell);
						
						quotedPara =new Paragraph("_______________________", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2));
						cell = new PdfPCell(quotedPara);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						quotedTable.addCell(cell);
						
						quotedPara = new Paragraph(app.getMessage("po.label.paymentDay")+":", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2, Font.BOLD));
						cell = new PdfPCell(quotedPara);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						quotedTable.addCell(cell);
						
						quotedPara =new Paragraph("_______________________", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize2));
						cell = new PdfPCell(quotedPara);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						quotedTable.addCell(cell);
						
						cell = new PdfPCell(requestTable);
						cell.setBorder(Rectangle.NO_BORDER);
						bigTable.addCell(cell);
						
						cell = new PdfPCell(quotedTable);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						bigTable.addCell(cell);
						
						//comments
						PdfPTable commentTable = new PdfPTable(1);
						commentTable.setWidthPercentage(100);
						Paragraph commentPara =  new Paragraph("\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize3));
						cell = new PdfPCell(commentPara);
						cell.setExtraParagraphSpace(20f);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						commentTable.addCell(cell);
		
						commentPara = new Paragraph(app.getMessage("po.label.fillInFields"), FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize3));
						cell = new PdfPCell(commentPara);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell.setExtraParagraphSpace(20f);
						commentTable.addCell(cell);
						
						commentPara = new Paragraph(app.getMessage("po.label.info")+" "+fax + ".", FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize3));
						cell = new PdfPCell(commentPara);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setFixedHeight(15);
						commentTable.addCell(cell);
						
						document.add(headerTable);
						document.add(infoTable);
						document.add(dataTable);
						document.add(attnTable);
						document.add(supplierTable);
						document.add(itemTable);
						document.add(totalTable);
						document.add(quotationTable);
						document.add(remarkTable);
						document.add(bigTable);
						document.add(commentTable);
						
					} catch (DocumentException de) {
						success = false;
						Log.getLog(getClass()).error("Error in PDF: " + de, de);
						
					} catch (IOException ioe) {
						success = false;
						Log.getLog(getClass()).error("Error in PDF: " + ioe, ioe);
					
					} catch (StorageException e) {
						success = false;
						e.printStackTrace();
						Log.getLog(getClass()).error("Error in PDF: " + e, e);
					}
	
					
					// step 5: we close the document
					document.close();
				}
				
				//generate zip file
				
				try {
					
			        FileOutputStream dest = new FileOutputStream(zipOutputFolder + "/" + zipOutputFileName);
			        out = new ZipOutputStream(new BufferedOutputStream(dest));
			         //out.setMethod(ZipOutputStream.DEFLATED);
			         byte data[] = new byte[BUFFER];
			         // get a list of files from current directory
			         File f =null;
	
			         for(Iterator i=numSupplier.iterator();i.hasNext();){
							SupplierObject obj= (SupplierObject)i.next();
			        	fileName = obj.getLastKnownCompany()+obj.getCounting()+"_"+obj.getProperty("purchaseCode");
			        	f = new File(zipOutputFolder+fileName+".pdf");
			        	String files = fileName+".pdf";
			        	if(f.exists()) {
			        		 FileInputStream fi = new FileInputStream(f);
					         origin = new BufferedInputStream(fi, BUFFER);
					         ZipEntry entry = new ZipEntry(files);
					         out.putNextEntry(entry);
					         int count;
					         while((count = origin.read(data, 0,BUFFER)) != -1) {
					        	 out.write(data, 0, count);
					         }
					         origin.close();
			        	}
			           
					}
					
					out.close();
					
					
				}catch(Exception e) {
					success = false;
					Log.getLog(getClass()).error("Error in PDF: " + e, e);
			    }
				
				//download zipped files
				if(success){
					try {
		    			storage = (StorageService) Application.getInstance().getService(StorageService.class);
		    			StorageFile sf = new StorageFile("/" + STORAGE_ROOT + "/" + zipOutputFileName);				
		    			sf = storage.get(sf);
						
						response.setHeader("Content-Disposition", "attachment; filename=\"" + zipOutputFileName + "\"");
			            StorageFileDataSource.copy(sf.getInputStream(), response.getOutputStream());
						
					}
					catch(Exception error) {
						Log.getLog(getClass()).error(error, error);
					}
				}
			}
		}
	}
}
