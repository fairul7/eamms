package com.tms.collab.taskmanager.ui;

import kacang.ui.Widget;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.services.storage.StorageService;
import kacang.services.storage.StorageDirectory;
import kacang.services.storage.StorageException;
import kacang.services.storage.StorageFile;
import kacang.Application;
import kacang.util.Log;

import java.util.Collection;
import java.io.FileNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Oct 22, 2003
 * Time: 6:23:17 PM
 * To change this template use Options | File Templates.
 */
public class FileListing extends Widget
{
    private String folderId;
    private Collection files;
    private boolean deleteable;
    private boolean downloadable;
    public final static String PARAMETER_EVENT_DELETE = "delete";
    public final static String PARAMETER_DELETE_FILENAME = "filename";


    public FileListing()
    {
    }

    public FileListing(String name)
    {
        super(name);
    }

    public void init()
    {
        super.init();
    }

    public void onRequest(Event evt)
    {
        super.onRequest(evt);
        if(folderId!=null&&folderId.trim().length()>0){
            StorageService ss = (StorageService)Application.getInstance().getService(StorageService.class);
          //  StorageDirectory sd = new StorageDirectory(folderId);
            try
            {
                StorageDirectory sd =  (StorageDirectory)ss.get(new StorageFile(folderId));
                files = sd.getFileListing();
            } catch (FileNotFoundException e)
            {
                //Log.getLog(getClass()).error(e);
            } catch (StorageException e)
            {
                Log.getLog(getClass()).error(e);
            }
        }
    }

    public Forward actionPerformed(Event evt)
    {
        String eventType = evt.getRequest().getParameter(Event.PARAMETER_KEY_EVENT_TYPE);
        if(PARAMETER_EVENT_DELETE.equals(eventType)){
            String fileName =   evt.getRequest().getParameter(PARAMETER_DELETE_FILENAME);
            if(folderId!=null&&fileName!=null){
                StorageFile sf = new StorageFile(folderId+"/"+fileName);
                StorageService ss = (StorageService)Application.getInstance().getService(StorageService.class);
                try
                {
                    ss.delete(sf);
                    refresh();
                } catch (StorageException e)
                {
                    Log.getLog(getClass()).error(e);
                }
            }
        }
        return null;
    }

    public void refresh(){
        if(folderId!=null&&folderId.trim().length()>0){
            StorageService ss = (StorageService)Application.getInstance().getService(StorageService.class);
           // StorageDirectory sd = new StorageDirectory(folderId);
            try
            {
                StorageDirectory sd =  (StorageDirectory)ss.get(new StorageFile(folderId));
                files = sd.getFileListing();
            } catch (FileNotFoundException e)
            {
              //  Log.getLog(getClass()).error(e);
            } catch (StorageException e)
            {
                Log.getLog(getClass()).error(e);
            }
        }
    }
    public String getDefaultTemplate()
    {
        return "taskmanager/filelisting";
    }

    public String getFolderId()
    {
        return folderId;
    }

    public void setFolderId(String folderId)
    {
        this.folderId = folderId;
    }

    public Collection getFiles()
    {
        return files;
    }

    public void setFiles(Collection files)
    {
        this.files = files;
    }

    public boolean isDeleteable()
    {
        return deleteable;
    }

    public void setDeleteable(boolean deleteable)
    {
        this.deleteable = deleteable;
    }

    public boolean isDownloadable()
    {
        return downloadable;
    }

    public void setDownloadable(boolean downloadable)
    {
        this.downloadable = downloadable;
    }

    public boolean isMoreFiles(){
        if(files!=null)
            return files.size()>0;
        return false;
    }
}
