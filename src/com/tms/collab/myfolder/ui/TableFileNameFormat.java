package com.tms.collab.myfolder.ui;

import kacang.Application;
import kacang.stdui.TableFormat;
import kacang.util.Log;

import com.tms.collab.myfolder.model.FileFolder;
import com.tms.collab.myfolder.model.MyFolderException;
import com.tms.collab.myfolder.model.MyFolderModule;

public class TableFileNameFormat implements TableFormat{
	
	private String folderForward="";
	private String nonFolderForward="";
	
	public TableFileNameFormat(String folderForward, String nonFolderForward){
		this.folderForward = folderForward;
		this.nonFolderForward = nonFolderForward;
	}
	
	public String format(Object obj){
		MyFolderModule mf = (MyFolderModule)Application.getInstance().getModule(MyFolderModule.class);
		FileFolder ff = new FileFolder();
		StringBuffer buf = new StringBuffer();
		
		try{
			ff = mf.loadFileFolder((String)obj);
		
			buf.append("<a href='");
			if("folder".equalsIgnoreCase(ff.getFileType())){
				buf.append(folderForward);
			}
			else{
				buf.append(nonFolderForward);
			}
			buf.append((String)obj);
			buf.append("'> ");
			
			buf.append(ff.getFileName());
			if("folder".equalsIgnoreCase(ff.getFileType())){
				buf.append(" (" + mf.getNumOfItemsInFolder((String)obj) + ")");
			}
			
			buf.append("</a>");
			
		}catch(MyFolderException e){
			Log.getLog(getClass()).error("error in TableFileNameFormat: " + e.getMessage());
		}
		
		return buf.toString();
		
		
	}
	
}
