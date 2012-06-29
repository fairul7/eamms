package com.tms.collab.messaging.ui;

import com.tms.collab.messaging.model.*;
import kacang.Application;
import kacang.services.storage.StorageException;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.util.Log;

import javax.mail.internet.MimeUtility;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;


/**
 * To enable downloading of attachments, this servlet must be enabled. This
 * servlet also serves email source.
 * <p>
 *
 * <pre>
    <!-- servlet definition & mapping for messaging module -->
    <servlet>
        <servlet-name>DownloadAttachmentServlet</servlet-name>
        <servlet-class>com.tms.collab.messaging.ui.DownloadAttachmentServlet</servlet-class>
    </servlet>

    <servlet-mapping>
        <servlet-name>DownloadAttachmentServlet</servlet-name>
        <url-pattern>/messaging/downloadAttachment</url-pattern>
    </servlet-mapping>
 * </pre>
 *
 *
 */
public class DownloadAttachmentServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String messageId, name;
        String action;
        String attachIndexStr;
        int attachmentIndex = 0;
        request.setCharacterEncoding("UTF-8");
        action = request.getParameter("action");
        messageId = request.getParameter("messageId");
        name = request.getParameter("name");
        attachIndexStr = request.getParameter("index");
        try {
            if (attachIndexStr != null) {
                attachmentIndex = Integer.parseInt(attachIndexStr);
            }
        }
        catch (NumberFormatException e) {
            // ignore
        }

        if("source".equals(action)) {
            // download email source
            doDownloadEmailSource(request, response, messageId);
            return;
        }


        if(messageId==null || name==null) {
            PrintWriter out = response.getWriter();
            out.print("Invalid parameters");
            return;
        }
        
        name = Util.decodeHex(name);
        //name = URLDecoder.decode(name, "UTF-8");
        
        MessagingModule mm;
        Message message;
        InputStream in = null;

        mm = Util.getMessagingModule();
        try {
            message = mm.getMessageByMessageId(messageId);
            if(!isValidUser(request, mm, message.getFolderId())) {
                throw new MessagingException("Invalid access to messaging module!");
            }

            in = message.getAttachmentInputStream(name, attachmentIndex);
            StorageFile sf = message.getAttachmentStorageFile(name, attachmentIndex);
            String fileName = sf.getName();

            String userAgent = request.getHeader("User-Agent");
            if (userAgent != null && userAgent.indexOf("MSIE") > 0) {
                // handle for IE, truncate because long filenames will not show properly
            	 fileName = URLEncoder.encode(fileName, "UTF-8");
                 fileName = StringUtils.replace(fileName, "+", " ");
                int maxLength = 225;
                if (fileName.length() > maxLength) {
                    int ext = fileName.lastIndexOf(".");
                    fileName = fileName.substring(0, maxLength) + fileName.substring(ext);
                }
            }
            else {
                fileName = MimeUtility.encodeText(fileName, "UTF-8", "B");
            }

            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            StorageFileDataSource.copy(in, response.getOutputStream());
            in.close();

        } catch (MessagingException e) {
            PrintWriter out = response.getWriter();
            Log.getLog(getClass()).error(e.getMessage(), e);
            out.print(e.getMessage());
        } catch (StorageException e) {
            PrintWriter out = response.getWriter();
            Log.getLog(getClass()).error(e.getMessage(), e);
            out.print(e.getMessage());
        } finally {
            if(in!=null) {
                in.close();
            }
        }

    }

    private void doDownloadEmailSource(HttpServletRequest request, HttpServletResponse response, String messageId) throws IOException {
        MessagingModule mm;
        Message message;
        StorageService ss;
        StorageFile sf;

        mm = Util.getMessagingModule();
        try {
            message = mm.getMessageByMessageId(messageId);
            if(!isValidUser(request, mm, message.getFolderId())) {
                throw new MessagingException("Invalid access to messaging module!");
            }

            ss = (StorageService) Application.getInstance().getService(StorageService.class);
            sf = new StorageFile(message.getStorageFilename());
            sf = ss.get(sf);

            response.setHeader("Content-Disposition", "attachment; filename=" + sf.getName());
            StorageFileDataSource.copy(sf.getInputStream(), response.getOutputStream());

        } catch (MessagingException e) {
            PrintWriter out = response.getWriter();
            Log.getLog(getClass()).error(e.getMessage(), e);
            out.print(e.getMessage());
        } catch (StorageException e) {
            PrintWriter out = response.getWriter();
            Log.getLog(getClass()).error(e.getMessage(), e);
            out.print(e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }

    }

    /**
     * Returns true if folderId belongs to current user, false otherwise
     * @param request
     * @param mm
     * @param folderId
     * @return
     * @throws MessagingException
     */
    private boolean isValidUser(HttpServletRequest request, MessagingModule mm, String folderId) throws MessagingException {
        String userId = Util.getUser(request).getId();

        if(mm.getFolder(folderId).getUserId().equals(userId)) {
            return true;
        } else {
            return false;
        }
    }
}
