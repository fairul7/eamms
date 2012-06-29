package com.tms.cms.template;

import com.tms.cms.core.model.ContentUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Front Controller Servlet for previewing content.
 */
public class TemplatePreviewFrontServlet extends TemplateFrontServlet {


//    private Log log = Log.getLog(getClass());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // set preview attribute in request
        ContentUtil.setPreviewRequest(request);

        // do work
        super.doPost(request, response);
    }

}
