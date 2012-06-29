package com.tms.collab.weblog.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DaoException;
import com.tms.collab.weblog.model.WeblogModule;
import org.apache.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Mar 10, 2004
 * Time: 2:04:51 PM
 * To change this template use Options | File Templates.
 */
public class editBlogLinkForm extends BlogLinkForm
{
    public void onRequest(Event event)
    {
        super.onRequest(event);
        if(linkId!=null&&!linkId.equals("")){
            if(link==null||!link.getId().equals(linkId)){
                WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
                try
                {
                    link = wm.getLink(linkId);
                    populateForm();
                } catch (DaoException e)
                {
                    Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                }
            }
        }
    }

    private void populateForm(){
        nameTF.setValue(link.getName());
        url.setValue(link.getUrl());
    }

    public Forward onValidate(Event event)
    {
        if(saveButton.getAbsoluteName().equals(findButtonClicked(event))){
            if(link==null&&(linkId==null||linkId.equals("")))
                return new Forward(FORWARD_LINK_FAILED);
            WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
            try
            {
                if(link==null)
                    wm.getLink(linkId);
                link.setName(nameTF.getValue().toString());
                //link.setUrl(url.getValue().toString().replaceFirst("http://",""));
                link.setUrl(StringUtils.replaceOnce(url.getValue().toString(), "http://", ""));
                wm.updateLink(link);
                return new Forward(FORWARD_LINK_UPDATED);
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                return new Forward(FORWARD_LINK_FAILED);
            }

        }
        return super.onValidate(event);
    }

}
