package com.tms.collab.vote.image;

import kacang.Application;
import kacang.services.storage.StorageDirectory;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;

import java.util.Collection;
import java.util.Iterator;

public class ImageList extends Form
{
    private String id;
    private String subFolder;
    private StorageFile[] storageFiles;
    private Button deleteButton;

    public Button getDeleteButton()
    {
        return deleteButton;
    }

    public String getSubFolder()
    {
        return subFolder;
    }

    public void setSubFolder(String subFolder)
    {
        this.subFolder = subFolder;
    }

    public void setDeleteButton(Button deleteButton)
    {
        this.deleteButton = deleteButton;
    }

    public StorageFile[] getStorageFiles()
    {
        return storageFiles;
    }

    public void setStorageFiles(StorageFile[] storageFiles)
    {
        this.storageFiles = storageFiles;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void onRequest(Event evt)
    {
        storageFiles = null;
        if(id==null)
            id= (String)evt.getRequest().getSession().getAttribute("id");
        try
        {
            StorageService ss =(StorageService) Application.getInstance().getService(StorageService.class);
            StorageDirectory sd = (StorageDirectory)ss.get(new StorageFile("/"+subFolder+"/"+id));
            Collection c = sd.getFileListing();
            if(c!=null&&!c.isEmpty())
            {
                storageFiles = new StorageFile[c.size()];
                int n=0;
                for(Iterator i=c.iterator();i.hasNext();n++)
                {
                    storageFiles[n]= (StorageFile)i.next();
                  //  storageFiles[n].
                }
            }
        }
        catch(Exception e)
        {        }
    }

    public void init()
    {
        deleteButton = new Button("deleteButton");
        deleteButton.setText("Delete");
        addChild(deleteButton);

    }

    public String getTemplate()
    {
        return "vote/image/ImageList";
    }

    public Forward onValidate(Event evt)
    {

        String buttonName = findButtonClicked(evt);
        if(buttonName!=null&&buttonName.equals(deleteButton.getAbsoluteName()))
        {

            String path = evt.getRequest().getParameter("choice");
            if(path != null)
            {
                int p = path.lastIndexOf("storage");
                path = path.substring(p+"storage".length(),path.length());
                StorageService ss =(StorageService) Application.getInstance().getService(StorageService.class);
                try
                {
                    ss.delete(new StorageFile(path));
                }
                catch(Exception e)
                {                }
                onRequest(evt);
            }
        }

        return null;
    }


}
