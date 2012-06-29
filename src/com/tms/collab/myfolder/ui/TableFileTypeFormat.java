package com.tms.collab.myfolder.ui;

import kacang.Application;
import kacang.stdui.TableFormat;

public class TableFileTypeFormat implements TableFormat{
	
	public TableFileTypeFormat(){
		
	}
	
	public String format(Object obj){
		String folderBasedOnLanguageFile = Application.getInstance().getMessage("mf.label.folder", "folder");
		if(folderBasedOnLanguageFile.equalsIgnoreCase((String)obj))
			return "<img src=\"/ekms/myfolder/images/folder.gif\">";
		
//		else if("text/html".equalsIgnoreCase((String)obj))
//			return "<img src=\"/ekms/myfolder/images/icf_html.gif\">";
//		
//		else if("text/plain".equalsIgnoreCase((String)obj))
//			return "<img src=\"/ekms/myfolder/images/icf_text.gif\">";
//		
//		else if("video/x-msvideo".equalsIgnoreCase((String)obj) || "video/x-sgi-movie".equalsIgnoreCase((String)obj) || "video/mpeg".equalsIgnoreCase((String)obj)
//				|| "video/mpeg2".equalsIgnoreCase((String)obj) || "video/quicktime".equalsIgnoreCase((String)obj) || "application/x-pn-realaudio-plugin".equalsIgnoreCase((String)obj))
//			return "<img src=\"/ekms/myfolder/images/icf_mpeg.gif\">";
//		
//		else if("application/msword".equalsIgnoreCase((String)obj))
//			return "<img src=\"/ekms/myfolder/images/icf_word.gif\">";
//		
//		else if("application/mspowerpoint".equalsIgnoreCase((String)obj))
//			return "<img src=\"/ekms/myfolder/images/icf_pps.gif\">";
//		
//		else if("application/msexcel".equalsIgnoreCase((String)obj) || "application/ms-excel".equalsIgnoreCase((String)obj) || "application/x-excel".equalsIgnoreCase((String)obj)
//	 			|| "application/vnd.ms-excel".equalsIgnoreCase((String)obj))
//			return "<img src=\"/ekms/myfolder/images/icf_excel.gif\">";
//		
//		else if("application/pdf".equalsIgnoreCase((String)obj) || "application/x-pdf".equalsIgnoreCase((String)obj))
//			return "<img src=\"/ekms/myfolder/images/icf_acrobat.gif\">";
//		
//		else if("image/gif".equalsIgnoreCase((String)obj) || "image/x-png".equalsIgnoreCase((String)obj) || "image/jpeg".equalsIgnoreCase((String)obj)
//				|| "image/tiff".equalsIgnoreCase((String)obj) || "image/pjpeg".equalsIgnoreCase((String)obj) || "image/x-ms-bmp".equalsIgnoreCase((String)obj))
//			return "<img src=\"/ekms/myfolder/images/icf_image.gif\">";
//		
//		else if("x-music/x-midi".equalsIgnoreCase((String)obj) || "audio/x-wav".equalsIgnoreCase((String)obj) || "audio/mpeg".equalsIgnoreCase((String)obj)
//				|| "audio/x-ms-wma".equalsIgnoreCase((String)obj) )
//			return "<img src=\"/ekms/myfolder/images/icf_audio.gif\">";
//		
//		else if("application/x-shockwave-flash".equalsIgnoreCase((String)obj) || "audio/x-wav".equalsIgnoreCase((String)obj))
//			return "<img src=\"/ekms/myfolder/images/icf_flash.gif\">";
//		
		else
			return "<img src=\"/ekms/myfolder/images/file.gif\">";
		
	}
	
}
