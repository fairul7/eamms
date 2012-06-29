package com.tms.tmsPIMSync.ui;

import com.tms.collab.directory.model.AddressBookException;
import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.directory.model.Contact;
import com.tms.collab.messaging.model.Util;
import com.tms.tmsPIMSync.VCardUtils;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * For downloading VCard
 */
public class DownloadVCardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contactId;

        request.setCharacterEncoding("UTF-8");
        contactId = request.getParameter("contactId");

        if(contactId==null) {
            PrintWriter out = response.getWriter();
            out.print("Invalid parameters");
            return;
        }

        AddressBookModule am = (AddressBookModule) Application.getInstance().getModule(AddressBookModule.class);
        Contact contact = null;
        try {
            contact = am.getContact(contactId);
            String userId = Util.getUser(request).getId();
            if(!contact.getOwnerId().equals(userId)){
                PrintWriter out = response.getWriter();
                out.print("Possible hacking attempt: You do not have access to this contact.");
                return;
            }
            VCardUtils util = new VCardUtils();
            String vcard = util.ekpContactToVCard(contact);

            String fileName;
            if(contact.getFirstName()!=null && !contact.getFirstName().equals("")){
                fileName = contact.getFirstName();
            }else if(contact.getNickName()!=null && !contact.getNickName().equals("")){
                fileName = contact.getNickName();
            }else fileName = "contact";

            fileName += ".vcf";

            response.setContentType("text/x-vCard");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            InputStream in = new ByteArrayInputStream(vcard.getBytes("UTF-8"));
            ServletOutputStream out = response.getOutputStream();
 
            int bytesRead;
            while(-1 != (bytesRead = in.read())){
                out.write(bytesRead);
            }

        } catch (AddressBookException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DataObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}