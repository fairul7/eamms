package com.tms.collab.weblog.ui;

import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.UuidGenerator;
import kacang.util.Log;
import kacang.model.DaoException;
import com.tms.collab.weblog.model.WeblogModule;
import com.tms.collab.weblog.model.Link;
import org.apache.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Mar 10, 2004
 * Time: 1:39:58 PM
 * To change this template use Options | File Templates.
 */
public class addBlogLinkForm extends BlogLinkForm
{

    private void refresh(){
        nameTF.setValue("");
        url.setValue("");
    }
    public Forward onValidate(Event event)
    {
        if(saveButton.getAbsoluteName().equals(findButtonClicked(event))){
            link = new Link();
            link.setId(UuidGenerator.getInstance().getUuid());
            link.setName(nameTF.getValue().toString());
            //link.setUrl(url.getValue().toString().replaceFirst("http://",""));
            link.setUrl(StringUtils.replaceOnce(url.getValue().toString(), "http://", ""));
            link.setBlogId(blogId);
            WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
            try
            {
                wm.addLink(link);
                refresh();
                return new Forward(FORWARD_LINK_ADDED);
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                return new Forward(FORWARD_LINK_FAILED);
            }
        }
        return super.onValidate(event);
    }

}
