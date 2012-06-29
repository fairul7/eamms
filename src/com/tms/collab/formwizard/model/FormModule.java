package com.tms.collab.formwizard.model;

import com.tms.collab.formwizard.engine.*;
import com.tms.collab.formwizard.widget.FileLinkCheckbox;
import com.tms.collab.formwizard.xmlwidget.*;
import com.tms.collab.messaging.model.*;
import com.tms.ekms.setup.model.SetupException;
import com.tms.ekms.setup.model.SetupModule;
import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DefaultDataObject;
import kacang.model.DefaultModule;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.storage.StorageException;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.stdui.FileUpload;
import kacang.stdui.Form;
import kacang.stdui.Panel;
import kacang.ui.Event;
import kacang.ui.Widget;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import org.apache.commons.collections.SequencedHashMap;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


public class FormModule extends DefaultModule {
    public static final String WORKFLOW_PENDING = "PENDING";
    public static final String WORKFLOW_APPROVE = "APPROVE";
    public static final String WORKFLOW_REJECT = "REJECT";
    public static final String FORM_MODULE_ID = FormModule.class.getName();
    public static final String CONTEXT_PATH_PROPERTY = "contextPath";
    public static final String WORKFLOW_APPROVAL_MEMO = "memo";
    public static final String WORKFLOW_APPROVAL_EMAIL = "email";
    public static final String PROPERTY_MEMO = "com.tms.collab.formwizard.memo";
    public static final String FORM_ACTIVE = "1";
    public static final String FORM_INACTIVE = "0";
    public static final String FORM_PENDING = "1";
    public static final String FORM_APPROVE = "0";
    public static final String FORM_PREFIX = "frw_form_name_";

    public void init() {
    }

    public void approveFormData(String formUID, String action, String userID, Event evt)
            throws FormException, FormDaoException, FormDocumentException {
        approveFormData(formUID, action, userID, evt, true);
    }

    public void approveFormData(String formUID, String action, String userID, Event evt, boolean sendNotification)
            throws FormException, FormDaoException, FormDocumentException {
        approveFormData("", formUID, action, userID, evt, sendNotification);
    }

    public void approveFormData(String rejectedReason, String formUid, String action, String userId, Event evt)
            throws FormDaoException {
        approveFormData(rejectedReason, formUid, action, userId, evt, true);
    }

    public void approveFormData(String rejectedReason, String formUid, String action, String userId, Event evt, boolean sendNotification)
            throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        List storageFileList;


        FormWorkFlowDataObject wf = getFormWFDetail(formUid);
        wf.setFormUid(formUid);
        wf.setFormApprovalDate(new Date());

        int highestPriority = dao.getHighPriority(wf.getFormId());

        if (highestPriority == wf.getPriority() && !"reject".equals(action))
            wf.setStatus(WORKFLOW_APPROVE);
        else if ("approve".equals(action))
            wf.setStatus(WORKFLOW_PENDING);
        else if ("reject".equals(action))
            wf.setStatus(WORKFLOW_REJECT);

        FormWorkFlowDataObject fwfdo = getNextPriorityApprover(wf.getFormId(), wf.getPriority() + 1);

        if (highestPriority > wf.getPriority() && !action.equals("reject")) {
            wf.setFormApprovalId(fwfdo.getFormApprovalId());
        }

        if (WORKFLOW_APPROVE.equals(wf.getStatus()))
            deleteFormsWorkFlow(wf.getFormUid());
        else
            updateFormsWorkFlow(wf);

        FormDataObject fdo = getForm(wf.getFormId());
        storageFileList = getFile(wf.getFormId(), formUid);

        if (sendNotification) {
            try {
                if (WORKFLOW_PENDING.equals(wf.getStatus())) {
                    sendNotification(wf.getFormId(), userId, fwfdo.getUserId(), getApproverMessageBody(wf, fdo),
                                     Application.getInstance().getMessage("formWizard.notification.approval.pending.title" + fdo.getFormDisplayName(), "Form submitted for approval"), null);
                }
                else if (WORKFLOW_REJECT.equals(wf.getStatus())) {
                    sendNotification(wf.getFormId(), wf.getApproverId(), wf.getUserId(), getRejectedMessageBody(wf, fdo, rejectedReason),
                                     Application.getInstance().getMessage("formWizard.notification.approval.rejected.title", "Form Rejected"), storageFileList);
                }
                else if (WORKFLOW_APPROVE.equals(wf.getStatus())) {
                    sendNotification(wf.getFormId(), wf.getApproverId(), wf.getUserId(), getApprovedMessageBody(wf, fdo),
                                     Application.getInstance().getMessage("formWizard.notification.approval.approved.title", "Form Approved"), storageFileList);
                }
            }
            catch (SecurityException e) {
                Log.getLog(getClass()).info(e.getMessage(), e);
            }

        }
    }

    public void deleteFormDraft(String formId, String formUid) throws FormDaoException {
        FormDao dao = (FormDao)  getDao();
        dao.deleteFormDraft(formUid,formId);
    }

    public Collection getAllForms(String sort, boolean desc) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        return dao.selectAllForms(sort, desc);
    }

    private String getApprovedMessageBody(FormWorkFlowDataObject wf, FormDataObject fdo) {
        StringBuffer contentBuffer = new StringBuffer();

        contentBuffer.append(Application.getInstance().getMessage("formWizard.notification.content.user.approved",
                                                                  "Your submitted form named " + fdo.getFormDisplayName() + " has been approved.",
                                                                  new Object[]{fdo.getFormDisplayName()}));
        contentBuffer.append(getFormDetail(wf.getFormUid(), wf.getFormId()));


        return contentBuffer.toString();
    }

    private String getApproverMessageBody(FormWorkFlowDataObject wfDO, FormDataObject formsDO)
            throws SecurityException {

        SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
        SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
        User user = security.getUser(wfDO.getUserId());

        String siteURL;
        try {
            siteURL = setup.get("siteUrl");
            String context = Application.getInstance().getProperty(CONTEXT_PATH_PROPERTY);

            siteURL += context + "/frwFormDataDetail.jsp?formUid=" + wfDO.getFormUid();

            StringBuffer contentBuffer = new StringBuffer();

            contentBuffer.append(Application.getInstance().getMessage("formWizard.notification.content.user.submission",
                                                                      user.getUsername() + " has submitted a form that requires your approval.",
                                                                      new Object[]{user.getUsername()}));

            contentBuffer.append("<br><br><b>");
            contentBuffer.append(Application.getInstance().getMessage("formWizard.notification.content.form", "Form"));
            contentBuffer.append(":</b> ").append(formsDO.getFormDisplayName()).append("<br><br><b>");
            contentBuffer.append(Application.getInstance().getMessage("formWizard.notification.content.url", "URL"));
            contentBuffer.append(":</b> <a href = \"").append(siteURL).append("\"> ").append(siteURL).append("</a><br><br>");


            return contentBuffer.toString();
        }
        catch (SetupException e) {
            Log.getLog(getClass()).error("Error selecting siteUrl property", e);
        }
        return "";
    }

    public List getFile(String formId, String formUid) {
        List storageFileList = null;
        Object object;
        FormLayout layout;
        PanelField panelField;


        try {
            FormDataObject fdo = getForm(formId);
            layout = getFormLayout(formId);
            storageFileList = getFileList(formUid, fdo.getFormName(), layout);

            for (Iterator iterator = layout.getFieldList().iterator(); iterator.hasNext();) {
                object =  iterator.next();
                if (object instanceof PanelField) {
                    panelField = (PanelField) object;
                    if (panelField.getFormLayout() != null) {
                        storageFileList.addAll(getFileList(formUid, fdo.getFormName(), panelField.getName(),
                                                           getFormTemplateLayout(panelField.getFormLayout().getTemplateId())));
                    }
                }

            }
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }



        return storageFileList;

    }

    public List getFileList(String formUid, String tableName, FormLayout layout) throws FormDaoException {
        return getFileList(formUid,tableName,"",layout);
    }

    public List getFileList(String formUid, String tableName, String templateId,FormLayout layout) throws FormDaoException {
        Object object;
        List storegeFileList = new ArrayList();
        Collection data;
        DefaultDataObject ddo;
        String name;
        StorageFile sf;
        String file = "";

        StorageService service = (StorageService) Application.getInstance().getService(StorageService.class);

        try {
            if (layout != null) {
                List fieldList = layout.getFieldList();
                for (Iterator iterator = fieldList.iterator(); iterator.hasNext();) {
                    object = iterator.next();
                    if (object instanceof FileField) {
                        name = templateId + ((FileField) object).getName();
                        data = getFormData(formUid, tableName, name);
                        if (data.iterator().hasNext()) {
                            ddo = (DefaultDataObject) data.iterator().next();


                            if (ddo.getProperty(name) != null) {
                                file = String.valueOf(ddo.getProperty(name));
                                sf = service.get(new StorageFile(file));
                                storegeFileList.add(sf);
                            }
                        }
                    }

                }
            }
        }
        catch (StorageException e) {
            Log.getLog(getClass()).error("Error getting file " + file, e);
        }
        catch (FileNotFoundException e) {
            Log.getLog(getClass()).error("Error locating file " + file, e);
        }

        return storegeFileList;
    }

    public String getFormDetail(String formUid, String formId) {
        FormLayout layout;

        StringBuffer buffer = new StringBuffer();

        try {
            layout = getFormLayout(formId, getFormDetailMap(formId, formUid));

            if (layout.getFieldList() != null) {
                parseFormDetail(layout.getFieldList(), buffer);
            }
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return buffer.toString();
    }

    public Map getFormDetailMap(String formId, String formUid) throws FormDaoException {
        Map map = new HashMap();
        DefaultDataObject ddo = null;

        Collection data = getFormDataDetail(formId, formUid, getForm(formId).getFormName());

        if (data.iterator().hasNext())
            ddo = (DefaultDataObject) data.iterator().next();

        if (ddo != null)
            map = ddo.getPropertyMap();

        return map;

    }

    public FormLayout getFormLayout(String formId) {
        return getFormLayout(formId, null);
    }

    public FormLayout getFormLayout(String formId, Map map) {
        InputStream stream = null;
        FormLayout layout = null;
        try {
            stream = getFormXML(formId);
            StructureEngine engine = new StructureEngine();
            engine.setXml(stream);
            engine.setData(map);
            layout = engine.retriveStructure();
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        finally {
            try {
                if (stream != null)
                    stream.close();
            }
            catch (IOException e) {

            }
        }
        return layout;
    }

    public Object getFormProperty(String formId, String propertyName) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        return dao.getFormProperty(formId, "formLink");
    }

    public FormLayout getFormTemplateLayout(String templateId) {
        InputStream stream = null;
        FormLayout layout = null;
        try {
            stream = getTemplatePreviewXml(templateId) ;
            StructureEngine engine = new StructureEngine();
            engine.setXml(stream);
            layout = engine.retriveStructure();

        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        finally {
            try {
                if (stream != null)
                    stream.close();
            }
            catch (IOException e) {

            }
        }

        return layout;
    }

    private String getRejectedMessageBody(FormWorkFlowDataObject wf, FormDataObject fdo, String reason) {

        SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
        String siteURL;
        try {
            siteURL = setup.get("siteUrl");
            String context = Application.getInstance().getProperty(CONTEXT_PATH_PROPERTY);


            siteURL += context + "/frwResubmitData.jsp?id=" + wf.getFormUid() + "&reset=true";

            StringBuffer contentBuffer = new StringBuffer();

            contentBuffer.append(Application.getInstance().getMessage("formWizard.notification.content.user.rejected",
                                                                      "Your submitted form named " + fdo.getFormDisplayName() + "has been <b>rejected</b>.",
                                                                      new Object[]{fdo.getFormDisplayName()}));
            contentBuffer.append("<br><br>");

            contentBuffer.append(getFormDetail(wf.getFormUid(), wf.getFormId()));
            contentBuffer.append("<br><br>").append(Application.getInstance().getMessage("formWizard.notification.content.reason", "Reason")).append(":<br>");
            contentBuffer.append(reason);
            contentBuffer.append("<br><br>");
            contentBuffer.append("<b>").append(Application.getInstance().getMessage("formWizard.notification.content.url", "URL"));
            contentBuffer.append(":</b>&nbsp;<a href=\"").append(siteURL).append("\">").append(siteURL).append("</a>");


            return contentBuffer.toString();
        }
        catch (SetupException e) {
            Log.getLog(getClass()).error("Error selecting siteUrl property", e);
        }

        return "";

    }

    public boolean isValidForm(String formId, String userId) throws FormDaoException, FormException {
        boolean valid;
        FormDataObject fdo;
        Collection data;

        fdo = getForm(formId);

        if (fdo.getIsActive().equals(FORM_ACTIVE))
            valid = true;
        else
            valid = false;


        //check for permission
        if (valid) {
            valid = false;
            data = getViewForms(new DaoQuery(), userId, null, false, 0, -1);
            for (Iterator iterator = data.iterator(); iterator.hasNext();) {
                fdo = (FormDataObject) iterator.next();
                if (fdo.getFormId().equals(formId))
                    valid = true;
            }
        }


        return valid;
    }

    public void parseFormDataDetail(List list, String templateId, StringBuffer buffer) {
        Object object;
        PanelField panelField;

        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            object = iterator.next();


            if (object instanceof TextFieldField ||
                object instanceof TextBoxField ||
                object instanceof ButtonGroupField ||
                object instanceof SelectBoxField ||
                object instanceof DateFieldField ||
                object instanceof FileField ||
                object instanceof TableGridField) {
                buffer.append(templateId).append(((Field) object).getName()).append(",");
            }
            else if (object instanceof PanelField) {
                panelField = (PanelField) object;
                if (panelField.getFormLayout() != null) {
                    parseFormDataDetail(panelField.getFormLayout().getFieldList(), panelField.getName(), buffer);
                }
            }

        }
    }

    public void parseFormDetail(List list, StringBuffer buffer) {
        Object object;
        PanelField panelField;

        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            object = iterator.next();

            if (object instanceof LabelField) {
                if (((LabelField) object).getText() != null)
                    buffer.append("<b>").append(((LabelField) object).getText()).append("</b> : ");
            }
            else if (object instanceof TextFieldField ||
                     object instanceof TextBoxField ||
                     object instanceof ButtonGroupField ||
                     object instanceof SelectBoxField) {
                buffer.append(((Field) object).getValue()).append("<br>");
            }
            else if (object instanceof DateFieldField) {
                buffer.append(Util.getDate((Date) ((Field) object).getValue())).append("<br>");
            }
            else if (object instanceof FileField) {
                buffer.append(Util.processFile((String) ((Field) object).getValue())).append("<br>");
            }
            else if (object instanceof TableGridField) {
                TableGridField tableGridField = (TableGridField) object;
                buffer.append("<b>").append(tableGridField.getTitle()).append("</b> :<br>");
                buffer.append(Util.formatG2FieldData(tableGridField.getTitle(),
                                                     tableGridField.getColumnListXml(),
                                                     (String) tableGridField.getValue())).append("<br>");
            }
            else if (object instanceof PanelField) {
                panelField = (PanelField) object;
                if (panelField.getFormLayout() != null) {
                    parseFormDetail(panelField.getFormLayout().getFieldList(), buffer);
                }
            }

        }
    }

    public void saveEditFormData(FormWorkFlowDataObject wfDO, Event event, List list) throws FormDaoException, FormDocumentException, FormException {
        FormDao dao = (FormDao) getDao();
        List fileList = wfDO.getFileList();
        String filePath;


       try {

            wfDO.setFormName(FORM_PREFIX + getForm(wfDO.getFormId()).getFormName());

            dao.saveEditFormData(wfDO);


            //delete file
            for (Iterator iterator = fileList.iterator(); iterator.hasNext();) {
                filePath = String.valueOf(iterator.next());
                deleteFile(filePath);
            }

            //upload file
            uploadFile(list, event, wfDO,"");

            //verify the workflow if the form is drafted

            if (getFormDraft(wfDO.getFormUid()) != null) {
                notification(wfDO);
                approvalCycle(wfDO, event);
            }

            //delete from draft
            deleteFormDraft(wfDO.getFormId(), wfDO.getFormUid());            
            //save db options is no
            noSaveDb(wfDO);


        }
        catch (StorageException e) {
            throw new FormException("Error uploading file" + wfDO.getFormUid(), e);
        }
        catch (IOException e) {
            throw new FormException("Error uploading file" + wfDO.getFormUid(), e);
        }
        catch (MessagingException e) {
            Log.getLog(getClass()).info(e.getMessage());
        }


    }

    protected void sendNotification(String formId, String senderId, String receiverId,
                                    String body, String subject, List storageFileList)
            throws SecurityException {

        String notifyMethod;
        String token;
        try {


            notifyMethod = getFormNotificationMethod(formId);
            boolean notifyByEmail = false;
            boolean notifyByMemo = false;

            StringTokenizer stk = new StringTokenizer(notifyMethod, ",");
            while (stk.hasMoreTokens()) {
                token = stk.nextToken();
                if (FormModule.WORKFLOW_APPROVAL_EMAIL.equals(token))
                    notifyByEmail = true;
                if (FormModule.WORKFLOW_APPROVAL_MEMO.equals(token))
                    notifyByMemo = true;
            }


            if (notifyByEmail) {
                try {
                    sendNotificationByEmail(receiverId, senderId, subject, body, storageFileList);
                }
                catch (MessagingException e) {
                    Log.getLog(getClass()).fatal(e.getMessage());
                }
            }


            if (notifyByMemo) {
                Collection toUsers = new ArrayList();
                SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
                User user = security.getUser(receiverId);
                toUsers.add(user);
                try {
                    sendNotificationByMemo(toUsers, senderId, subject, body, storageFileList);
                }
                catch (MessagingException e) {
                    Log.getLog(getClass()).fatal("Error sending memo notification", e);
                }
            }


        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
    }

    public void storeFile(Event event, FileUpload fileUpload, FormWorkFlowDataObject wfdo, String panelName)
            throws IOException, StorageException {
        storeFile(event, fileUpload, wfdo, panelName,fileUpload.getName());
    }

    public void storeFile(Event event, FileUpload fileUpload, FormWorkFlowDataObject wfdo, String panelName, String name) throws IOException, StorageException {
        StorageService storage = (StorageService) Application.getInstance().getService(StorageService.class);
        StorageFile sf = fileUpload.getStorageFile(event.getRequest());
        if (sf != null) {
            sf.setParentDirectoryPath(FormConstants.ROOT_PATH + "/" + wfdo.getFormId() + "/" + wfdo.getFormUid() + "/" + panelName + name);
            storage.store(sf);
        }
    }


    public void uploadFile(List fieldList, Event event, FormWorkFlowDataObject wfdo, String panelName) throws IOException, StorageException {
        Object object;
        FileField fileField;
        FileLinkCheckboxField linkCheckboxField;

        for (Iterator iterator = fieldList.iterator(); iterator.hasNext();) {
            object =  iterator.next();

            if (object instanceof FileField) {
                fileField = (FileField) object;
                storeFile(event, (FileUpload) fileField.getWidget(), wfdo, panelName);
            }
            else if (object instanceof PanelField) {
                if ( ((PanelField) object).getFormLayout() != null) {
                    uploadFile(((PanelField) object).getFormLayout().getFieldList(),event, wfdo, ((Field)object).getName());
                }
            }
            else if (object instanceof FileLinkCheckboxField) {
                linkCheckboxField = (FileLinkCheckboxField) object;
                storeFile(event,  ((FileLinkCheckbox) linkCheckboxField.getWidget()).getFile(), wfdo, panelName, linkCheckboxField.getName());
            }

        }

    }

    public void addForm(String userID, FormDataObject formsDO, FormConfigDataObject fcdo) throws FormNameExistsException, FormException, FormDaoException {
        FormDao dao = (FormDao) getDao();
        FormElement form;
        try {
            if (dao.isFormNameExist(formsDO.getFormName()))
                throw new FormNameExistsException("FormName Already Exits!");
            //insert data into frw_form table
            dao.insertForm(formsDO);

            //duplicate field
            if (fcdo != null) {
                form = Util.getFormElementInstanceByXml(fcdo.getPreviewXml());
                addFormConfig(fcdo, form);
            }
            else {
                //table column
                fcdo = new FormConfigDataObject();
                fcdo.setFormConfigId(UuidGenerator.getInstance().getUuid());
                fcdo.setFormId(formsDO.getFormId());
                form = new FormElement(formsDO.getFormName(), formsDO.getFormHeader(),
                                       Integer.parseInt(formsDO.getTableColumn()));
                try {
                    fcdo.setPreviewXml(form.display());
                    fcdo.setFormXml(fcdo.getPreviewXml());
                }
                catch (IOException e) {
                    throw new FormDaoException("Error displaying form - formId:" + formsDO.getFormId());
                }
                dao.insertFormsConfig(fcdo, null, dao.createTableSQL(form));


            }


        }
        catch (DaoException e) {
            throw new FormException(e.toString());
        }
    }

    public void notification(FormWorkFlowDataObject fwfdo) throws MessagingException {

        try {
            FormDataObject fdo = getForm(fwfdo.getFormId());
            String body = getBody(fwfdo, fdo.getFormDisplayName());
            String frmHeader = fdo.getFormDisplayName();

            List storageFileList = getFile(fwfdo.getFormId(), fwfdo.getFormUid());
            //email submitted form
            if (fdo.getFormEmails() != null && !fdo.getFormEmails().equals(""))

                emailTo(fwfdo.getUserId(), body, fdo, storageFileList, frmHeader);

            String memoTo = getMemoTo(fwfdo.getFormId());
            if (memoTo != null && !memoTo.equals(""))
                memoTo(memoTo, fwfdo.getUserId(), body, storageFileList, frmHeader);


        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        catch (SecurityException e) {
            Log.getLog(getClass()).info("User not found", e);
        }


    }

    public Collection getFormMemoSubmission(String formId) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        return dao.selectFormMemoSubmission(formId);
    }

    private String getMemoTo(String formId) throws FormDaoException {
        StringBuffer buffer = new StringBuffer();
        Collection memoSubmission = getFormMemoSubmission(formId);
        for (Iterator iterator = memoSubmission.iterator(); iterator.hasNext();) {
            buffer.append(((FormMemoSubmission) iterator.next()).getUserId()).append(",");
        }

        return buffer.toString();

    }


    private void notificationToApprover(String userID, FormDataObject formsDO)
            throws SecurityException, MessagingException {
        String subject, body, link, context;
        String siteURL, memo;
        Collection users;

        try {
            SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
            SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);

            siteURL = setup.get("siteUrl");
            users = security.getUsersByPermission(FormConstants.FORM_APPROVE_PERMISSION_ID, new Boolean(true), "firstName", false, 0, -1);
            context = Application.getInstance().getProperty(CONTEXT_PATH_PROPERTY);

            link = siteURL + context + "/fw_approveForm.jsp?formID=" + formsDO.getFormId();
            subject = "Pending Form";
            body = "Please approve the form - " + formsDO.getFormDisplayName() + "<br><br><br>"
                   + "<a href=\"" + link + "\">Click here to approve</a>";

            memo = Application.getInstance().getProperty(FormModule.PROPERTY_MEMO);
            if (memo == null || memo.equals("false")) {
                List toEmail = new ArrayList();
                for (Iterator it = users.iterator(); it.hasNext();) {
                    User user = (User) it.next();
                    if (user.getProperty("email1") != null)
                        toEmail.add(user.getProperty("email1"));
                }
                sendNotificationByEmail(toEmail, userID, subject, body);
            }
            else {
                sendNotificationByMemo(users, userID, subject, body);
            }

        }
        catch (SetupException e) {
            Log.getLog(getClass()).error("Site URL is invalid", e);
        }

    }

    private void sendNotificationByMemo(Collection toUsers, String senderId, String subject, String body,
                                        List storageFileList)
            throws MessagingException {
        String accountName;
        List toList = new ArrayList();

        IntranetAccount intranetAccount;
        SmtpAccount smtpAccount;


        MessagingModule messaging = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);


        smtpAccount = new SmtpAccount();
        SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
        try {

            smtpAccount.setServerName(setup.get("siteSmtpServer"));
            smtpAccount.setServerPort(25);
        }
        catch (SetupException e) {
            Log.getLog(getClass()).error("Smtp Server property is not found", e);
        }


        Message message = new Message();
        message.setMessageId(UuidGenerator.getInstance().getUuid());


        for (Iterator i = toUsers.iterator(); i.hasNext();) {
            User user = (User) i.next();
            intranetAccount = messaging.getIntranetAccountByUserId(user.getId());
            if (intranetAccount != null) {
                accountName = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
                toList.add(accountName);
            }
        }

        if (toList.size() > 0)
            message.setToIntranetList(toList);


        message.setSubject(subject);
        message.setDate(new Date());

        if (body != null) {
            body = StringUtils.replace(body, "\r", "<br>");
        }

        message.setBody(body);
        message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
        if (storageFileList != null)
            message.setStorageFileList(storageFileList);


        messaging.sendMessage(smtpAccount, message, senderId);
    }

    private void sendNotificationByMemo(Collection toUsers, String senderId, String subject, String body)
            throws MessagingException {
        sendNotificationByMemo(toUsers, senderId, subject, body, null);

    }

    public void editForm(String userID, FormDataObject formsDO)
            throws FormException, FormDaoException, FormDocumentException {
        FormDao dao = (FormDao) getDao();
        SequencedHashMap map = null;
        String receiverId = null;
        boolean approvalCyCleChanged = false;
        boolean sendNotification = false;
        Element rootElem = null;
        String previewXml = null, formXml = null;

        try {

            Collection approvers = dao.getFormApproval(formsDO.getFormId());
            if (approvers != null && approvers.size() > 0) {
                map = new SequencedHashMap();
                for (Iterator iterator = approvers.iterator(); iterator.hasNext();) {
                    FormWorkFlowDataObject approver = (FormWorkFlowDataObject) iterator.next();
                    map.put(approver.getUserId(), String.valueOf(approver.getPriority()));

                }
            }

            int count = 1;

            if (map != null && map.size() > 0) {
                String value;
                if (formsDO.getApproverIDs() != null) {
                    for (Iterator i = formsDO.getApproverIDs().keySet().iterator(); i.hasNext(); count++) {
                        String id = (String) i.next();

                        if (count == 1)
                            receiverId = id;
                        value = String.valueOf(map.get(id));

                        if (map.get(id) == null) {
                            approvalCyCleChanged = true;
                            sendNotification = true;
                            break;
                        }

                        if (!value.equals(String.valueOf(count))) {
                            approvalCyCleChanged = true;
                            sendNotification = true;
                            break;
                        }
                    }

                }
                else {
                    approvalCyCleChanged = true;
                    sendNotification = false;
                }

                if (!approvalCyCleChanged && formsDO.getApproverIDs() != null && map.size() != formsDO.getApproverIDs().size()) {
                    approvalCyCleChanged = true;
                    sendNotification = true;
                }


            }
            else {
                approvalCyCleChanged = true;
            }


            dao.updateForm(formsDO, approvalCyCleChanged);
            //update table column
            Document jDomDoc = Util.buildJDomDocument(getFormPreviewXML(formsDO.getFormId()));
            if (jDomDoc != null) {
                rootElem = jDomDoc.getRootElement();
                rootElem.setAttribute("columns", formsDO.getTableColumn());
                previewXml = Util.JDomDocumentToString(jDomDoc);

                jDomDoc = Util.buildJDomDocument(getFormXML(formsDO.getFormId()));
                rootElem = jDomDoc.getRootElement();
                rootElem.setAttribute("columns", formsDO.getTableColumn());
                formXml = Util.JDomDocumentToString(jDomDoc);


                dao.updateFormConfig(formsDO.getFormId(), previewXml, formXml);
            }


            //send notification to 1st approver
            if (approvalCyCleChanged && sendNotification && approvers != null && approvers.size() > 0)
                sendNotificationToFirstApprover(formsDO.getFormId(), formsDO.getFormDisplayName(), receiverId, formsDO.getFormUpdatedBy());

        }
        catch (DaoException e) {
            throw new FormDaoException("Error updating form " + formsDO.getFormId(), e);
        }
        catch (SecurityException e) {
            throw new FormException("Error sending form notification  from " + formsDO.getFormUpdatedBy() + " to " + receiverId, e);
        }
    }

    public Collection getForms(String userID, String action, String sort, boolean desc, int start, int rows) throws FormException {
        FormDao dao = (FormDao) getDao();
        try {
            if (sort == null)
                sort = "formsName";
            if (action.equals("public"))
                return dao.selectPublicForms(userID, action, sort, desc, start, rows);
            else
                return dao.selectApproveForms(sort, desc, start, rows);
        }
        catch (DaoException e) {
            throw new FormException(e.toString());
        }

    }

    public int getForms(String userID, String action) throws FormException {
        FormDao dao = (FormDao) getDao();
        try {
            return dao.selectApproveForms();
        }
        catch (DaoException e) {
            throw new FormException(e.toString());
        }
    }

    public String replaceString(String str, String c, String r) {
        int location = 0;
        int startingLoc = 0;
        int strLength = c.length();
        while (location != -1) {
            StringBuffer strBuffer = new StringBuffer(str);
            location = str.indexOf(c, startingLoc);
            if (location != -1)
                strBuffer.replace(location, location + strLength, r);
            str = strBuffer.toString();
            startingLoc = location + strLength;
        }
        return str;
    }

    public Collection getFormData(String formUid, String tableName, String columnName) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        Collection data;

        data = dao.getSubmittedFormData(formUid, tableName, columnName);


        return data;
    }

    public Collection getFormDataDetail(String formId, String formUid, String tableName) throws FormDaoException {
        InputStream xml = getFormPreviewXML(formId);
        try {

            StringBuffer buffer = new StringBuffer();

            parseFormDataDetail(getFormLayout(formId).getFieldList(), "", buffer);

            if (buffer.length() > 0)
                buffer.setLength(buffer.length() - 1);

            return getFormData(formUid, tableName, buffer.toString());
        }
        finally {
            try {
                if (xml != null)
                    xml.close();
            }
            catch (IOException e) {
            }
        }
    }


    public InputStream getFormXML(String formId) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        return dao.selectFormXML(formId);

    }

    public String generateTemplateFieldSql(FormElement form, String fieldId, String fieldName, boolean repeat) {
        FormDao dao = (FormDao) getDao();
        return dao.generateTemplateFieldSql(form, fieldId, fieldName, repeat);

    }

    public String generateFormXML(FormElement form, List formChildren) {
        FormDao dao = (FormDao) getDao();
        return dao.generateFormXML(form, formChildren);
    }

    public String generateEditFormFieldSql(FormElement form, String fieldId, String formName, String fieldName) {
        String sql = generateTemplateFieldSql(form, fieldId, fieldName, true);
        FormDao dao = (FormDao) getDao();
        return dao.changeSql(sql, FormModule.FORM_PREFIX + formName);
    }

    public String generateAddFormFieldSql(FormElement form, String fieldId, String formName, String fieldName) {
        String sql = generateTemplateFieldSql(form, fieldId, fieldName, false);
        FormDao dao = (FormDao) getDao();
        return dao.addSql(sql, FormModule.FORM_PREFIX + formName);
    }

    public String generateDeleteColumnsSql(String tableName, List columnList) {
        FormDao dao = (FormDao) getDao();
        return dao.generateDeleteColumnsSql(tableName, columnList);
    }

    public String generateEditFormFieldXML(FormElement form, String fieldId) {
        FormDao dao = (FormDao) getDao();
        return dao.generateEditFormFieldXML(form, fieldId);
    }


    public String generateEditFormXML(FormElement form) throws IOException {
        String formXML = "";
        List formChildren = form.getChildren();
        boolean noFormField = true;


        for (Iterator fromChildrenIter = formChildren.iterator(); fromChildrenIter.hasNext();) {
            Element child = (Element) fromChildrenIter.next();

            if (!"description".equals(child.getName()))
                noFormField = false;
            child.getQualifiedName();
            child.getContent();
            if (!(child instanceof XmlWidgetAttributes)) {
                continue;
            }
            XmlWidgetAttributes xmlWidget = (XmlWidgetAttributes) child;
            xmlWidget.removeMetaData();
        }

        if (!noFormField) {
            Element submitBtn = new Element("button");
            submitBtn.setAttribute("name", "submit");
            submitBtn.setAttribute("text", "Submit");

            Element resetBtn = new Element("resetbutton");
            resetBtn.setAttribute("name", "reset");
            resetBtn.setAttribute("text", "Reset");


            Element labelElem = new Element("label");
            labelElem.setAttribute("name", "submitLbl");

            Element panelElem = new Element("panel");
            panelElem.setAttribute("name", "submitPnl");


            panelElem.addContent(submitBtn);
            panelElem.addContent(resetBtn);

            Element listenerElem = new Element("listener_form");

            form.addContent(labelElem);
            form.addContent(panelElem);
            form.addContent(listenerElem);

            formXML = form.display();

            form.removeContent(labelElem);
            form.removeContent(panelElem);
            form.removeContent(listenerElem);


        }
        else
            formXML = form.display();


        return formXML;
    }

    public void addFormConfig(FormConfigDataObject fcDO, FormElement form) throws FormException, FormDaoException {
        FormDao dao = (FormDao) getDao();
        FormTemplateField formTemplateField = null;
        try {

            removeFormMetaData(form);
            formTemplateField = setFormTemplateField(fcDO, form);

            getFormXmlFormElement(form);

            fcDO.setFormXml(form.display());

            String createTableDDL = dao.createTableSQL(form);
            List elementList = getElementList(form, fcDO.getFormId());
            String sqlDDL = generateFormXML(form, elementList);
			if(!("".equals(sqlDDL)))
            	dao.insertFormsConfig(fcDO, sqlDDL, createTableDDL);
            if (formTemplateField != null)
                dao.insertFormTemplateField(formTemplateField);
        }
        catch (IOException e) {
            throw new FormException(e.toString());
        }
        catch (DaoException e) {
            throw new FormException(e.toString());
        }
    }

    public void getFormXmlFormElement(FormElement form) {
        Element submitBtn = new Element("button");
        submitBtn.setAttribute("name", "submit");
        submitBtn.setAttribute("text", "Submit");
        Element resetBtn = new Element("resetbutton");
        resetBtn.setAttribute("name", "reset");
        resetBtn.setAttribute("text", "Reset");
        Element labelElem = new Element("label");
        labelElem.setAttribute("name", "submitLbl");
        Element panelElem = new Element("panel");
        panelElem.setAttribute("name", "submitPnl");
        panelElem.addContent(submitBtn);
        panelElem.addContent(resetBtn);

        Element listenerElem = new Element("listener_form");
        form.addContent(labelElem);
        form.addContent(panelElem);
        form.addContent(listenerElem);        
    }

    public List getElementList(FormElement form, String formId) throws FormDaoException, FormException {
        List elementList = new ArrayList(), templateElementList = new ArrayList();
        FormElement savedForm = Util.getFormElement(formId);
        List children = savedForm.getChildren();
        List formchildren = form.getChildren();
        int size = children.size();
        int formSize = formchildren.size();
        Element element = null;


        if (size > 1 && formSize > size) {
            elementList = formchildren.subList(size, formSize);
        }
        else if (formSize > size) {
            elementList = formchildren;
        }

        for (Iterator iterator = elementList.iterator(); iterator.hasNext();) {
            element = (Element) iterator.next();
            if (TemplateElement.ELEMENT_NAME.equals(element.getName())) {
                templateElementList.add(getTemplateElement(element, form));
            }

        }

        for (Iterator iterator = templateElementList.iterator(); iterator.hasNext();) {
            List list = (List) iterator.next();
            elementList.addAll(list);
        }

        return elementList;

    }

    public FormTemplateField setFormTemplateField(FormConfigDataObject fcDO, FormElement form) {
        FormTemplateField formTemplateField = null;

        List formChildren = form.getChildren();
        int size = formChildren.size();
        Element lastElement = null;

        if (size > 0) {
            lastElement = (Element) formChildren.get(size - 1);
            if (TemplateElement.ELEMENT_NAME.equals(lastElement.getName())) {
                formTemplateField = new FormTemplateField();
                formTemplateField.setFormTemplateId(lastElement.getAttributeValue("templateId"));
                formTemplateField.setTemplateNodeName(lastElement.getAttributeValue("name"));
                formTemplateField.setFormId(fcDO.getFormId());
                formTemplateField.setFormName(form.getAttributeValue("name"));
            }
        }
        return formTemplateField;
    }

    public org.w3c.dom.Document buildDOMDocument(InputStream stream)
            throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        org.w3c.dom.Document w3cDocument = factory.newDocumentBuilder().parse(stream);
        return w3cDocument;
    }


    public List getTemplateElement(Element element, FormElement form) throws FormException, FormDaoException {
        List elementList = new ArrayList();
        String templateId = null;
        InputStream stream = null;
        Node templateNode;
        org.w3c.dom.Document w3cDocument = null;
        NodeList nl;
        String nodeName;
        NamedNodeMap attributes;
        FormFieldDataObject ffDO = new FormFieldDataObject();
        String name = null;

        if (TemplateElement.ELEMENT_NAME.equals(element.getName())) {
            templateId = element.getAttributeValue("templateId");
            name = element.getAttributeValue("name");

            stream = getTemplatePreviewXml(templateId);

            try {
                w3cDocument = buildDOMDocument(stream);
                templateNode = w3cDocument.getDocumentElement();
                nl = templateNode.getChildNodes();

                for (int i = 0; i < nl.getLength(); i++) {
                    nodeName = nl.item(i).getNodeName();
                    attributes = nl.item(i).getAttributes();

                    if (!nodeName.equals(FormElement.ELEMENT_NAME)) {
                        //text field
                        if (TextFieldElement.ELEMENT_NAME.equals(nodeName)) {
                            ffDO.setFormFieldId(name + attributes.getNamedItem("name").getNodeValue());
                            ffDO.setMaxLength(attributes.getNamedItem("maxLength").getNodeValue());
                            ffDO.setFieldSize(attributes.getNamedItem("size").getNodeValue());
                            ffDO.setDataType(attributes.getNamedItem("type").getNodeValue());
                            ffDO.setHiddenFlag(attributes.getNamedItem("hidden").getNodeValue());
                            ffDO.setRequiredFlag(attributes.getNamedItem("require").getNodeValue());
                            elementList.add(new TextFieldElement(ffDO));
                        }
                        //text box
                        else if (TextBoxElement.ELEMENT_NAME.equals(nodeName)) {
                            ffDO.setFormFieldId(name + attributes.getNamedItem("name").getNodeValue());
                            ffDO.setMaxRows(attributes.getNamedItem("rows").getNodeValue());
                            ffDO.setMaxCols(attributes.getNamedItem("cols").getNodeValue());
                            ffDO.setHiddenFlag(attributes.getNamedItem("hidden").getNodeValue());
                            ffDO.setRequiredFlag(attributes.getNamedItem("require").getNodeValue());
                            elementList.add(new TextBoxElement(ffDO));
                        }
                        //check box or radio button
                        else if (ButtonGroupElement.ELEMENT_NAME.equals(nodeName)) {
                            ffDO.setFormFieldId(name + attributes.getNamedItem("name").getNodeValue());
                            ffDO.setHiddenFlag(attributes.getNamedItem("hidden").getNodeValue());
                            ffDO.setRequiredFlag(attributes.getNamedItem("require").getNodeValue());
                            elementList.add(new ButtonGroupElement(ffDO));
                        }
                        //pull down menu
                        else if (SelectElement.ELEMENT_NAME.equals(nodeName)) {
                            ffDO.setFormFieldId(name + attributes.getNamedItem("name").getNodeValue());
                            ffDO.setHiddenFlag(attributes.getNamedItem("hidden").getNodeValue());
                            ffDO.setRequiredFlag(attributes.getNamedItem("require").getNodeValue());
                            elementList.add(new SelectElement(ffDO));
                        }
                        else if (DateFieldElement.ELEMENT_NAME.equals(nodeName)) {
                            ffDO.setFormFieldId(name + attributes.getNamedItem("name").getNodeValue());
                            ffDO.setHiddenFlag(attributes.getNamedItem("hidden").getNodeValue());
                            ffDO.setRequiredFlag(attributes.getNamedItem("require").getNodeValue());
                            elementList.add(new DateFieldElement(ffDO));
                        }
                        //file upload
                        else if (FileUploadElement.ELEMENT_NAME.equals(nodeName)) {
                            ffDO.setFormFieldId(name + attributes.getNamedItem("name").getNodeValue());
                            ffDO.setHiddenFlag(attributes.getNamedItem("hidden").getNodeValue());
                            ffDO.setRequiredFlag(attributes.getNamedItem("require").getNodeValue());
                            elementList.add(new FileUploadElement(ffDO));
                        }
                        else if (TableGridElement.ELEMENT_NAME.equals(nodeName)) {
                            ffDO.setFormFieldId(name + attributes.getNamedItem("name").getNodeValue());
                            ffDO.setName(attributes.getNamedItem("title").getNodeValue());
                            elementList.add(new TableGridElement(ffDO, attributes.getNamedItem("columnListXml").getNodeValue()));


                        }

                    }
                }

            }
            catch (ParserConfigurationException e) {
                throw new FormException("Error parsing the config", e);
            }
            catch (IOException e) {
                throw new FormException("Error parsing the config", e);
            }
            catch (SAXException e) {
                throw new FormException("Error parsing the config", e);
            }
            finally {
                try {
                    if (stream != null)
                        stream.close();
                }
                catch (IOException e) {
                }
            }
        }

        return elementList;
    }


    public void editFormConfig(FormConfigDataObject fcDO, FormElement form, String fieldId, Map deletedG2Field)
            throws FormException {
        FormDao dao = (FormDao) getDao();
        try {

            String createTableDDL = dao.createTableSQL(form);
            String sqlDDL = generateEditFormFieldXML(form, fieldId);
            getFormXmlFormElement(form);
            fcDO.setFormXml(form.display());
            dao.insertFormsConfig(fcDO, sqlDDL, createTableDDL);

            if (deletedG2Field != null) {
                deleteG2Field(deletedG2Field);
            }

        }
        catch (IOException e) {
            throw new FormException(e.toString());

        }
        catch (DaoException e) {
            throw new FormException("Error updating frw_form_config table", e);
        }
    }

    public void deleteG2Field(Map deletedG2Field) {

    }

    public void approveForms(String formsID) throws FormException {
        FormDao dao = (FormDao) getDao();
        try {
            dao.approveForms(formsID);
        }
        catch (DaoException e) {
            throw new FormException(e.toString());
        }
    }

    public void deleteForms(String formId) throws FormException {
        FormDao dao = (FormDao) getDao();
        try {
            dao.deleteForms(formId);

            //delete files
            deleteFile(FormConstants.ROOT_PATH + "/" + formId);


        }
        catch (DaoException e) {
            throw new FormException(e.toString());
        }
    }


    public void deleteFormData(String formName, String id, String formId) throws FormDocumentException, FormDaoException {
        FormDao dao = (FormDao) getDao();


        deleteFile(formId, id);
        //delete data from database
        dao.deleteFormData(formName, id);

    }

    public void deleteFormData(String formName, String id, String formId, List deletedFileList) throws FormException, FormDocumentException, FormDaoException {
        FormDao dao = (FormDao) getDao();


        deleteFile(deletedFileList);
        //delete data from database
        dao.deleteFormData(FormModule.FORM_PREFIX + formName, id);

    }


    public void deleteFile(String formId, String formUid) throws FormDocumentException {

        List formTemplateList = null;
        String tableName = null;
        Element formTemplateElement = null;
        FormElement form = null, formTemplate = null;


        form = Util.getFormElement(formId);
        if (form != null) {
            tableName = form.getAttributeValue("name");
            deleteFile(form, formUid, tableName, "");

            //get the form template
            try {
                formTemplateList = (List) XPath.selectNodes(form, "/form/" + TemplateElement.ELEMENT_NAME);
                for (Iterator iterator = formTemplateList.iterator(); iterator.hasNext();) {
                    formTemplateElement = (Element) iterator.next();
                    formTemplate = Util.getTemplateElement(formTemplateElement.getAttributeValue("templateId"));
                    deleteFile(formTemplate, formUid, tableName, formTemplateElement.getAttributeValue("name"));
                }
            }
            catch (JDOMException e) {
                throw new FormDocumentException("Error selecting /form/" + TemplateElement.ELEMENT_NAME + " nodes", e);
            }
        }

    }

    public void deleteFile(FormElement form, String formUid, String tableName, String templateNodeName) throws FormDocumentException {
        String file = null, columnName = "";
        Element fileElem = null;
        List fileUploadList = null;
        DefaultDataObject ddo = null;
        FormDao dao = null;
        try {
            dao = (FormDao) getDao();
            fileUploadList = (List) XPath.selectNodes(form, "/form/fileupload");
            if (fileUploadList != null) {
                for (Iterator iterator = fileUploadList.iterator(); iterator.hasNext();) {
                    fileElem = (Element) iterator.next();

                    Collection data = null;
                    try {
                        columnName = templateNodeName + fileElem.getAttributeValue("name");
                        data = dao.getSubmittedFormData(formUid, tableName, columnName);
                        if (data.iterator().hasNext()) {
                            ddo = (DefaultDataObject) data.iterator().next();

                            file = String.valueOf(ddo.getProperty(columnName));
                            //delete file from storage
                            if (ddo.getProperty(columnName) != null)
                                deleteFile(file);
                        }
                    }
                    catch (FormDaoException e) {
                        Log.getLog(getClass()).error(e.getMessage(), e);
                    }
                    catch (FormException e) {
                        Log.getLog(getClass()).error(e.getMessage(), e);
                    }
                }
            }
        }
        catch (JDOMException e) {
            throw new FormDocumentException("Error selecting /form/fileupload nodes", e);
        }


    }

    public void deleteFile(String filePath) throws FormException {
        StorageService storageService = (StorageService) Application.getInstance().getService(StorageService.class);

        try {
            //delete file from storage
            if (filePath != null && !filePath.trim().equals(""))
                storageService.delete(new StorageFile(filePath));
        }
        catch (StorageException e) {
            throw new FormException("Error deleting file:" + filePath, e);
        }

    }


    public void deleteFile(List fileList) {

        String filePath = "";
        for (Iterator iterator = fileList.iterator(); iterator.hasNext();) {
            filePath = String.valueOf(iterator.next());

            try {
                //delete file from storage
                deleteFile(filePath);
            }
            catch (FormException e) {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }

        }
    }

    /**
     * Delete form template files
     */
    public void deleteFormTemplateFile(String tableName, String columnName) throws FormDaoException, FormException {
        Collection submittedData = null;
        String value = null;
        DefaultDataObject ddo = null;
        FormDao dao = null;

        dao = (FormDao) getDao();
        submittedData = dao.getSubmittedFormData(tableName, columnName);

        for (Iterator iterator = submittedData.iterator(); iterator.hasNext();) {
            ddo = (DefaultDataObject) iterator.next();
            value = String.valueOf(ddo.getProperty(columnName));
            deleteFile(value);
        }


    }

    public void saveForm(FormWorkFlowDataObject wfDO, Event evt, List list) throws FormException, FormDaoException, FormDocumentException {
        FormDao dao = (FormDao) getDao();

        try {

            wfDO.setFormName(FORM_PREFIX + getForm(wfDO.getFormId()).getFormName());


            dao.saveForm(wfDO);
            //store file
            uploadFile(list, evt, wfDO, "");

            //notification
            notification(wfDO);


            //approval cycle
            approvalCycle(wfDO, evt);
            //save db options is no
            noSaveDb(wfDO);
        }
        catch (IOException e) {
            throw new FormException("Error Uploading File", e);
        }
        catch (StorageException e) {
            throw new FormException("Error Uploading File", e);
        }
        catch (MessagingException e) {
            Log.getLog(getClass()).info(e.getMessage());
        }

    }

    public void noSaveDb(FormWorkFlowDataObject wfDO) throws FormDaoException, FormDocumentException {
        FormDataObject formsDO = getForm(wfDO.getFormId());

        if (FormConstants.FORM_NO_SAVE_DB.equals(formsDO.getSaveDb())) {
            deleteFormData(wfDO.getFormName(), wfDO.getFormUid(), wfDO.getFormId());
        }
    }


    public void storeFile(Event event, FormWorkFlowDataObject wfDO, Collection children, String panelName)
            throws StorageException, IOException {
        Map childMap;
        String name;
        StorageService storage;


        storage = (StorageService) Application.getInstance().getService(StorageService.class);
        for (Iterator widgetsIterator = children.iterator(); widgetsIterator.hasNext();) {
            Widget w = (Widget) widgetsIterator.next();

            if (w instanceof FileUpload) {
                FileUpload fileUpload = (FileUpload) w;

                StorageFile sf = fileUpload.getStorageFile(event.getRequest());

                if (sf != null) {
                    name = fileUpload.getName();
                    if (panelName != null)
                        name = panelName + name;
                    sf.setParentDirectoryPath(FormConstants.ROOT_PATH + "/" + wfDO.getFormId() + "/" + wfDO.getFormUid() + "/" + name);
                    storage.store(sf);
                }
            }

            if (w instanceof Panel) {
                childMap = w.getChildMap();
                if (w instanceof Form)
                    storeFile(event, wfDO, childMap.values(), panelName);
                else
                    storeFile(event, wfDO, childMap.values(), w.getName());

            }
        }
    }

    public void storeFile(Collection children, Event event, FormWorkFlowDataObject wfDO, String panelName) throws StorageException, IOException {
        StorageService storage;


        storage = (StorageService) Application.getInstance().getService(StorageService.class);
        for (Iterator widgetsIterator = children.iterator(); widgetsIterator.hasNext();) {
            Widget w = (Widget) widgetsIterator.next();

            if (w instanceof FileUpload) {
                FileUpload fileUpload = (FileUpload) w;

                StorageFile sf = fileUpload.getStorageFile(event.getRequest());
                if (sf != null) {
                    sf.setParentDirectoryPath(FormConstants.ROOT_PATH + "/" + wfDO.getFormId() + "/" + wfDO.getFormUid() + "/" + panelName + fileUpload.getName());
                    storage.store(sf);
                }
            }
            else if (w instanceof Panel) {
                if (w instanceof Form)
                    storeFile(w.getChildren(), event, wfDO, panelName);
                else
                    storeFile(w.getChildren(), event, wfDO, w.getName());
            }
        }
    }

    public void saveResubmitFormData(FormWorkFlowDataObject wfDO, Event evt, List list)
            throws FormDaoException, FormDocumentException, FormException  {
        deleteFormData(wfDO.getFormName(), wfDO.getFormUid(), wfDO.getFormId(), wfDO.getFileList());
        deleteFormsWorkFlow(wfDO.getFormUid());
        saveForm(wfDO, evt, list);
    }



    private String getBody(FormWorkFlowDataObject wfDO, String formName)
            throws SecurityException {
        StringBuffer content = new StringBuffer();
        SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);


        User user = security.getUser(wfDO.getUserId());

        content.append(user.getUsername()).append(" has submitted a form.<br><br>").append("<b>").
                append(Application.getInstance().getMessage("formWizard.label.formsView.formName", "Form Name")).
                append(" : </b> ").
                append(formName).append("<br><br>");


        Map values = wfDO.getValues();
        Set keyset = values.keySet();
        FormData formData;


        for (Iterator iterator = keyset.iterator(); iterator.hasNext();) {
            formData = (FormData) values.get(iterator.next());

            content.append("<b>").append(formData.getLabel()).append("</b> : ").
                    append(Util.nullToEmpty(formData.getDisplayValue())).append("<br>");
        }


        return content.toString();
    }


    private void emailTo(String userID, String body, FormDataObject formsDO, List storageFileList, String frmHeader)
            throws MessagingException, SecurityException {

        String subject;
        List toEmail = new ArrayList();
        StringTokenizer stk = null;

        subject = "Online Form Submitted: " + frmHeader ;
        stk = new StringTokenizer(formsDO.getFormEmails(), ",");
        while (stk.hasMoreTokens()) {
            toEmail.add(stk.nextToken());
        }


        sendNotificationByEmail(toEmail, userID, subject, body, storageFileList);


    }

    private void memoTo(String memoTo, String userID, String body, List storageFileList, String frmHeader)
            throws MessagingException {

        String subject;
        Collection users;
        StringTokenizer stk;
        User user;

        users = new ArrayList();
        stk = new StringTokenizer(memoTo, ",");
        while (stk.hasMoreTokens()) {
            user = new User();
            user.setId(stk.nextToken());
            users.add(user);
        }
        subject = "Form submitted: "+ frmHeader;

        sendNotificationByMemo(users, userID, subject, body, storageFileList);
    }

    private void sendNotificationByEmail(String receiverID, String userID, String subject, String body, List storageFileList)
            throws SecurityException, MessagingException {
        List toEmail = new ArrayList();
        SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);

        User user = security.getUser(receiverID);
        toEmail.add(user.getProperty("email1"));


        sendNotificationByEmail(toEmail, userID, subject, body, storageFileList);
    }

    private void sendNotificationByEmail(String receiverID, String userID, String subject, String body)
            throws SecurityException, MessagingException {
        sendNotificationByEmail(receiverID, userID, subject, body, null);

    }

    private void sendNotificationByEmail(List toEmail, String userID, String subject, String body, List storageFileList)
            throws MessagingException, SecurityException {

        String fromEmail = null;
        SmtpAccount account = null;

        MessagingModule messaging = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);


        account = new SmtpAccount();
        SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
        try {

            account.setServerName(setup.get("siteSmtpServer"));
            account.setServerPort(25);
        }
        catch (SetupException e) {
            Log.getLog(getClass()).error("Smtp Server property is not found", e);
        }


        SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
        User user = security.getUser(userID);
        fromEmail = String.valueOf(user.getProperty("email1"));

        Message message = new Message();
        message.setDate(new Date());
        message.setSubject(subject);

        if (body != null) {
            body = StringUtils.replace(body, "\r", "<br>");
        }

        message.setBody(body);
        message.setToList(toEmail);
        message.setFrom(fromEmail);
        message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
        if (storageFileList != null)
            message.setStorageFileList(storageFileList);
        messaging.sendMessage(account, message, null, false);


    }

    private void sendNotificationByEmail(List toEmail, String userID, String subject, String body)
            throws MessagingException, SecurityException {
        sendNotificationByEmail(toEmail, userID, subject, body, null);

    }


    public Collection getSubmittedFormData(String id, String tableName, String columnsStr) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        return dao.getSubmittedFormData(id, tableName, columnsStr);

    }

    public FormWorkFlowDataObject getFormsWorkFlow(String formUid) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        FormWorkFlowDataObject fwfdo = null;
        Collection data = dao.selectFormsWorkFlow(formUid);
        if (data.iterator().hasNext()) {
            fwfdo = (FormWorkFlowDataObject) data.iterator().next();
        }
        return fwfdo;
    }

    public boolean hasPermission(String userID, String formUID) throws FormDaoException, DaoException {
        if (userID != null && formUID != null) {
            FormDao dao = (FormDao) getDao();
            return dao.hasPermission(userID, formUID);
        }
        return false;

    }

    public void submitForApproval(String formId, String userId, FormDataObject formsDO) throws FormException {
        FormDao dao = (FormDao) getDao();
        try {
            formsDO.setIsActive(FormModule.FORM_ACTIVE);
            formsDO.setIsPending(FormModule.FORM_PENDING);
            dao.updateFormDetail(formsDO);

            //send notification to approver
            notificationToApprover(userId, formsDO);
        }
        catch (DaoException e) {
            throw new FormException(e.getMessage(), e);
        }
        catch (MessagingException e) {
            throw new FormException("Fail to send notification to approver", e);
        }
        catch (SecurityException e) {
            throw new FormException("UserId is invalid", e);
        }
    }

    public FormDataObject getForm(String formId) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        FormDataObject fdo = null;

        Collection form = dao.selectForm(formId);
        if (form.iterator().hasNext()) {
            fdo = (FormDataObject) form.iterator().next();
        }


        return fdo;
    }

    public boolean isFormApproved(String formId) throws FormDaoException {
        boolean approved = true;
        FormDao dao = (FormDao) getDao();
        Collection forms;

        FormDataObject fdo = getForm(formId);

        if (fdo != null) {
            if (fdo.getIsPending().equals(FormModule.FORM_PENDING))
                approved = false;
            else if (fdo.getIsPending().equals(FormModule.FORM_APPROVE))
                approved = true;
        }


        return approved;
    }

    public void activateForm(String formId) throws FormException {
        FormDao dao = (FormDao) getDao();

        try {
            FormDataObject fdo = new FormDataObject();
            fdo.setFormId(formId);
            fdo.setIsActive(FormModule.FORM_ACTIVE);
            fdo.setIsPending(FormModule.FORM_APPROVE);
            dao.updateFormDetail(fdo);
        }
        catch (DaoException e) {
            throw new FormException("Error activate form " + formId, e);
        }

    }

    public void sendNotificationToFirstApprover(String formId, String formDisplayName,
                                                String receiverId, String senderId) throws FormException, SecurityException {
        String subject;
        StringBuffer bodyBuffer = new StringBuffer();

        String notifyMethod = "";

        try {
            notifyMethod = getFormNotificationMethod(formId);
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }


        boolean notifyByEmail = false;
        boolean notifyByMemo = false;
        String token;
        StringTokenizer stk = new StringTokenizer(notifyMethod, ",");
        while (stk.hasMoreTokens()) {
            token = stk.nextToken();
            if (FormModule.WORKFLOW_APPROVAL_EMAIL.equals(token))
                notifyByEmail = true;
            if (FormModule.WORKFLOW_APPROVAL_MEMO.equals(token))
                notifyByMemo = true;
        }

        SecurityService security;
        SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
        String siteURL = "", context = "";

        try {
            siteURL = setup.get("siteUrl");
        }
        catch (SetupException e) {
            Log.getLog(getClass()).error("Site URL is invalid", e);
        }

        context = Application.getInstance().getProperty(CONTEXT_PATH_PROPERTY);


        subject = "Submitted Form Approval Required";

        bodyBuffer.append("Form : ").append(formDisplayName).append("<br><br>");
        bodyBuffer.append("This form has been edited and requires your approval/re-approval.<br>");
        bodyBuffer.append("URL : <a href=\"").append(siteURL).append(context).append("/frwApproveFormData.jsp").append("\">");
        bodyBuffer.append(siteURL).append(context).append("/frwApproveFormData.jsp</a><br>");

        try {
            if (notifyByEmail)
                sendNotificationByEmail(receiverId, senderId, subject, bodyBuffer.toString());
        }
        catch (MessagingException e) {
            Log.getLog(getClass()).error("Error delivering email", e);
        }

        try {
            if (notifyByMemo) {
                Collection toUsers = new ArrayList();
                security = (SecurityService) Application.getInstance().getService(SecurityService.class);
                User user = security.getUser(receiverId);
                toUsers.add(user);
                sendNotificationByMemo(toUsers, senderId, subject, bodyBuffer.toString());
            }
        }
        catch (MessagingException e) {
            Log.getLog(getClass()).error("Error sending message via intranet", e);
        }


    }

    public boolean isFormNameExist(String formName) throws FormException {
        FormDao dao = (FormDao) getDao();

        try {
            return dao.isFormNameExist(formName);

        }
        catch (DaoException e) {
            throw new FormException("Error in checking existance of form named" + formName, e);
        }


    }

    public void addFormTemplate(FormTemplate template) throws FormDaoException, FormException {
        FormDao dao = (FormDao) getDao();
        template.setFormTemplateId(UuidGenerator.getInstance().getUuid());

        dao.addFormTemplate(template);
        FormElement formTemplate = Util.getTemplateElement(template);
        try {
            template.setPreviewXml(formTemplate.display());
            dao.updateFormTemplate(template);
        }
        catch (IOException e) {
            throw new FormException("Error streaming the Xml", e);
        }


    }

    public void updateFormTemplateTable(FormTemplate template) throws FormDaoException {
        FormDao dao = (FormDao) getDao();


        dao.updateFormTemplate(template);
    }

    public void updateFormTemplate(FormTemplate template) throws FormDaoException {
        updateFormTemplateTable(template);

        addTablesColumn(template.getFormTemplateId(), getFieldId(template));
    }

    public void updateFormTemplateColumn(FormTemplate template) throws FormDaoException, FormDocumentException {
        Element rootElem = null;

        //update table column
        Document jDomDoc = Util.buildJDomDocument(template.getPreviewXml());
        rootElem = jDomDoc.getRootElement();
        rootElem.setAttribute("columns", template.getTableColumn());
        template.setPreviewXml(Util.JDomDocumentToString(jDomDoc));

        updateFormTemplateTable(template);
    }


    // get the last field
    public String getFieldId(FormTemplate formTemplate) {
        FormElement form = Util.getTemplateElement(formTemplate);
        List childrenList = form.getChildren();
        Element element = null;
        String fieldId = null;
        if (childrenList.size() > 0) {
            element = (Element) childrenList.get(childrenList.size() - 1);
        }

        if (element != null) {
            fieldId = element.getAttributeValue("name");
        }

        return fieldId;
    }


    public void deleteFormTemplate(FormTemplate template) throws FormDaoException, FormException, FormDocumentException {
        InputStream stream = null;
        List labelList = null, nameList = null;
        String value = null;
        Document jDomDocument = null;
        Element element = null;
        FormDao dao = null;


        dao = (FormDao) getDao();
        nameList = new ArrayList();
        stream = getTemplatePreviewXml(template.getFormTemplateId());

        try {

            if (stream != null) {
                //get the templateId
                jDomDocument = Util.buildJDomDocument(stream);
                labelList = XPath.selectNodes(jDomDocument, "/form/label");
                for (Iterator iterator = labelList.iterator(); iterator.hasNext();) {
                    element = (Element) iterator.next();
                    value = element.getAttributeValue("name");

                    if (value != null && !value.trim().equals("") && value.endsWith("lb")) {
                        value = value.substring(0, value.length() - 2);
                        nameList.add(value);
                    }
                }
                //drop the field on respective table
                dropTablesColumn(template.getFormTemplateId(), nameList);

                //update frw_form_config previewXML
                removeTemplateElement(template.getFormTemplateId());
            }

            //delete data from frw_form_template_field
            dao.deleteFormTemplateField(template.getFormTemplateId());

            //delete data from frw_form_template
            dao.deleteFormTemplate(template);

        }
        catch (JDOMException e) {
            throw new FormDocumentException("Error selecting label nodes", e);
        }


        finally {
            try {
                if (stream != null)
                    stream.close();
            }
            catch (IOException e) {

            }
        }


    }

    public void removeTemplateElement(String formTemplateId) throws FormDaoException, FormException, FormDocumentException {
        Collection formTemplateFields = null;
        InputStream stream = null;
        List nodeList = null;
        String previewXml = null;
        Document jDomDocument = null;
        Element element = null, childrenElement = null;
        FormDao dao = null;
        FormTemplateField formTemplateField = null;

        dao = (FormDao) getDao();
        formTemplateFields = dao.getFormTemplateFieldDistinct(formTemplateId);

        for (Iterator iterator = formTemplateFields.iterator(); iterator.hasNext();) {
            formTemplateField = (FormTemplateField) iterator.next();
            try {
                stream = getFormPreviewXML(formTemplateField.getFormId());
                jDomDocument = Util.buildJDomDocument(stream);
                childrenElement = (Element) jDomDocument.getContent(0);
                nodeList = XPath.selectNodes(jDomDocument, "/form/" + TemplateElement.ELEMENT_NAME
                                                           + "[@templateId= '" + formTemplateId + "']");
                for (Iterator iterator1 = nodeList.iterator(); iterator1.hasNext();) {
                    element = (Element) iterator1.next();
                    childrenElement.removeContent(element);
                }

                //update the frw_form_config table
                previewXml = Util.JDomDocumentToString(jDomDocument);
                dao.updateFormConfig(formTemplateField.getFormId(), previewXml, "");

            }
            catch (JDOMException e) {
                throw new FormDocumentException("Error select formwizardtemplate node", e);
            }
            finally {
                try {
                    if (stream != null)
                        stream.close();
                }
                catch (IOException e) {
                }

            }
        }

    }


    public boolean isFormTemplateNameExist(String templateName) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        return dao.isFormTemplateNameExist(templateName);
    }

    public boolean isFormTemplateNameExist(String templateName, String templateId) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        return dao.isFormTemplateNameExist(templateName, templateId);
    }

    public Collection getFormTemplate(String templateId) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        return dao.getFormTemplate(templateId);
    }

    public Collection getFormTemplate(String sort, boolean desc, int start, int rows) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        return dao.getFormTemplate(sort, desc, start, rows);
    }

    public Collection getFormTemplate(DaoQuery query, String sort, boolean desc, int start, int rows) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        return dao.getFormTemplate(query, sort, desc, start, rows);
    }

    public int getFormTemplateCount(DaoQuery query) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        return dao.getFormTemplateCount(query);
    }

    public InputStream getTemplatePreviewXml(String templateId) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        return dao.getTemplatePreviewXml(templateId);
    }

    public void removeFormMetaData(FormElement form) {

        List formChildren = form.getChildren();
        for (Iterator fromChildrenIter = formChildren.iterator(); fromChildrenIter.hasNext();) {
            Element child = (Element) fromChildrenIter.next();

            if (child instanceof TextBlockElement)
                ((TextBlockElement) child).removeMetaData();

            if (!(child instanceof XmlWidgetAttributes)) {
                continue;
            }
            XmlWidgetAttributes xmlWidget = (XmlWidgetAttributes) child;
            xmlWidget.removeMetaData();
        }
    }


    public Collection getFormTemplateField(String templateId) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        return dao.getFormTemplateField(templateId);
    }

    public void updateFormTablesColumn(String formTemplateId, String fieldId) throws FormDaoException {
        String sql;
        String fieldname;
        FormDao dao = (FormDao) getDao();
        Collection data = dao.getFormTemplateField(formTemplateId);
        FormElement form = null;
        FormTemplate formTemplate = new FormTemplate();
        formTemplate.setFormTemplateId(formTemplateId);
        form = Util.getTemplateElement(formTemplate);


        for (Iterator iterator = data.iterator(); iterator.hasNext();) {
            FormTemplateField formTemplateField = (FormTemplateField) iterator.next();
            fieldname = formTemplateField.getTemplateNodeName() + fieldId;
            sql = generateEditFormFieldSql(form, fieldId, formTemplateField.getFormName(), fieldname);
            try {
                dao.runDDL(sql);
            }
            catch (DaoException e) {
                Log.getLog(getClass()).error("Error updating column in " + formTemplateField.getFormName() + " table", e);
            }

        }

    }

    /**
     * e.g. <fileupload name="_1087366648296" hidden="0" require="1" template="formwizard/moveButtons"> (form template field)
     * Drop the columns from various table prefix with "frw_form_name"
     *
     * @param name a list of removed template field name, the parameter value is "_1087366648296"
     */
    public void dropTablesColumn(String formTemplateId, List name) throws FormDaoException, FormDocumentException, FormException {
        String sql, value, nodeName = null;
        Collection formTemplateFields = getFormTemplateField(formTemplateId);
        FormDao dao = (FormDao) getDao();
        StringBuffer buffer = new StringBuffer();
        FormElement element = null;
        FormTemplate formTemplate = null;


        formTemplate = new FormTemplate();
        formTemplate.setFormTemplateId(formTemplateId);
        element = Util.getTemplateElement(formTemplate);


        for (Iterator iterator = formTemplateFields.iterator(); iterator.hasNext();) {
            buffer = new StringBuffer();
            FormTemplateField data = (FormTemplateField) iterator.next();
            if (data.getTemplateNodeName() != null && data.getFormName() != null) {

                for (Iterator nameIterator = name.iterator(); nameIterator.hasNext();) {
                    value = String.valueOf(nameIterator.next());
                    buffer.append(dao.dropColumnSql(data, value));
                    nodeName = getNodeName(element, value);

                    //remove file
                    if (FileUploadElement.ELEMENT_NAME.equals(nodeName)) {
                        deleteFormTemplateFile(data.getFormName(), data.getTemplateNodeName() + value);
                    }

                }
                sql = buffer.toString();
                if (sql != null && !sql.trim().equals("") && sql.endsWith(","))
                    sql = sql.substring(0, sql.length() - 1);

                sql = dao.alterTableSql(sql, FormModule.FORM_PREFIX + data.getFormName());
                try {
                    dao.runDDL(sql);
                }
                catch (DaoException e) {
                    Log.getLog(getClass()).error("Error dropping the column", e);
                }
            }
        }

    }

    public void dropTablesColumn(String formTemplateId, String name)
            throws FormDaoException, FormDocumentException, FormException {
        List nameList = new ArrayList();
        nameList.add(name);
        dropTablesColumn(formTemplateId, nameList);
    }


    public void addTablesColumn(String formTemplateId, String fieldId) throws FormDaoException {
        String sql, fieldName;
        Collection formTemplateFields = getFormTemplateField(formTemplateId);
        FormDao dao = (FormDao) getDao();
        FormTemplate formTemplate = new FormTemplate();
        formTemplate.setFormTemplateId(formTemplateId);
        FormElement form = Util.getTemplateElement(formTemplate);

        for (Iterator iterator = formTemplateFields.iterator(); iterator.hasNext();) {
            FormTemplateField formTemplateField = (FormTemplateField) iterator.next();
            fieldName = formTemplateField.getTemplateNodeName() + fieldId;
            sql = generateAddFormFieldSql(form, fieldId, formTemplateField.getFormName(), fieldName);

            try {
                dao.runDDL(sql);
            }
            catch (DaoException e) {
                Log.getLog(getClass()).error("Error dropping the column", e);
            }

        }
    }

    /**
     * Delete a field from a form
     * e.g. <fileupload name="_1087366648296" hidden="0" require="1" template="formwizard/moveButtons"> (form field)
     *
     * @param form Form Element
     * @param name name attribute, as provided example, the parameter value is "_1087366648296"
     */
    public void removeField(FormElement form, String formId, String name)
            throws FormDaoException, FormException, FormDocumentException {
        List columnList = null;
        String formName = null, sql = null, formTemplateId = null, templateNodeName = null, value = null;
        Element child, childLb = null;
        FormDao dao = null;
        FormConfigDataObject fcdo = null;
        try {

            dao = (FormDao) getDao();

            //update the XML
            fcdo = new FormConfigDataObject();
            child = (Element) XPath.selectSingleNode(form, "//*[@name='" + name + "']");

            childLb = (Element) XPath.selectSingleNode(form, "//*[@name='" + name + "lb']");

            if (child != null) {
                child.getParent().removeContent(child);
            }

            if (childLb != null) {
                childLb.getParent().removeContent(childLb);
            }

            fcdo.setPreviewXml(Util.escapeSingleQuote(form.display()));
            fcdo.setFormXml(Util.escapeSingleQuote(generateEditFormXML(form)));
            fcdo.setFormId(formId);
            dao.updateFormConfig(fcdo);


            formName = form.getAttributeValue("name");
            formTemplateId = child.getAttributeValue("templateId");
            templateNodeName = child.getAttributeValue("name");
            if (formTemplateId != null) {
                columnList = getColumnName(formTemplateId, templateNodeName);
            }
            else if (!child.getAttributeValue("name").endsWith(FormConstants.FIELD_TEXT_BLOCK_SUFFIX)) {
                columnList = new ArrayList();
                columnList.add(child.getAttributeValue("name"));
            }

            //remove file
            if (columnList != null)
                for (Iterator iterator = columnList.iterator(); iterator.hasNext();) {
                    value = String.valueOf(iterator.next());
                    deleteFormTemplateFile(formName, value);
                }


            //drop field from the table
            sql = generateDeleteColumnsSql(FormModule.FORM_PREFIX + formName, columnList);
            dao.runDDL(sql);

            //drop the templateNodeName from frw_form_template_field
            dao.deleteFormTemplateFieldTemplateNodeName(templateNodeName);


        }
        catch (JDOMException e) {
            throw new FormDocumentException("Error selecting the node", e);
        }
        catch (IOException e) {
            throw new FormDocumentException("Error outputing the form element", e);
        }
        catch (DaoException e) {
            throw new FormDocumentException("Error dropping column from table " + FormModule.FORM_PREFIX + formName, e);
        }

    }

    public List getColumnName(String templateId, String templateNodeName)
            throws FormDaoException, FormException, FormDocumentException {
        List columnList = new ArrayList();
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        InputStream stream = module.getTemplatePreviewXml(templateId);

        Document jDomDocument = Util.buildJDomDocument(stream);
        Element element = null;
        String labelName = null, fieldName = null;
        try {


            List labelList = XPath.selectNodes(jDomDocument, "/form/label");
            for (Iterator iterator = labelList.iterator(); iterator.hasNext();) {
                element = (Element) iterator.next();
                labelName = element.getAttributeValue("name");
                if (labelName.endsWith("lb")) {
                    fieldName = labelName.substring(0, labelName.length() - 2);
                    columnList.add(templateNodeName + fieldName);
                }

            }
        }
        catch (JDOMException e) {
            throw new FormDocumentException("Error selecting the label nodes", e);
        }

        return columnList;
    }

    public void removeTemplateField(FormElement form, String name, String formTemplateId)
            throws FormDaoException, FormException, FormDocumentException {

        String templateNodeName = null;
        Element child = null, childLb = null;
        FormDao dao = null;
        FormTemplate formTemplate = null;


        dao = (FormDao) getDao();

        try {
            child = (Element) XPath.selectSingleNode(form, "//*[@name='" + name + "']");

            childLb = (Element) XPath.selectSingleNode(form, "//*[@name='" + name + "lb']");






            //drop the field from respective table
            templateNodeName = child.getAttributeValue("name");
            dropTablesColumn(formTemplateId, templateNodeName);




            //update the frw_form_template table
            if (child != null) {
                child.getParent().removeContent(child);
            }

            if (childLb != null) {
                childLb.getParent().removeContent(childLb);
            }

            formTemplate = new FormTemplate();
            formTemplate.setTemplateName(form.getAttributeValue("name"));
            formTemplate.setPreviewXml(form.display());
            formTemplate.setFormXml("");
            formTemplate.setFormTemplateId(formTemplateId);
            dao.updateFormTemplate(formTemplate);


        }
        catch (JDOMException e) {
            throw new FormDocumentException("Error selecting the node", e);
        }
        catch (IOException e) {
            throw new FormDocumentException("Error outputing the form element", e);
        }


    }

    /**
     * Get the node name
     * e.g. <fileupload name="_1087366648296" hidden="0" require="1" template="formwizard/moveButtons">
     *
     * @param form FormElement or FormTemplateElement
     * @param name name attribute, as the provided example, the parameter value is "_1087366648296"
     * @return element name, as the provided example, the return value is "fileupload"
     */
    public String getNodeName(FormElement form, String name) throws FormDocumentException {
        Element element = null;
        try {
            element = (Element) XPath.selectSingleNode(form, "/form/*[@name='" + name + "']");
            return element.getName();
        }
        catch (JDOMException e) {
            throw new FormDocumentException("Error selecting nodes - name attribute:" + name, e);
        }
    }

    public InputStream getFormPreviewXML(String formId) throws FormDaoException {
        FormDao dao;

        dao = (FormDao) getDao();
        return dao.selectFormPreviewXML(formId);
    }

    public void draftForm(FormWorkFlowDataObject wfDO, Event event, List list) throws FormException, FormDaoException {
        List fileList = wfDO.getFileList();
        String filePath;
        FormDao dao;

        dao = (FormDao) getDao();


        try {


            wfDO.setFormName(FORM_PREFIX + getForm(wfDO.getFormId()).getFormName());



            //save data to respective table - "frw_form_name_<formname>"
            dao.saveEditFormData(wfDO);


            //delete file
            if (fileList != null) {
                for (Iterator iterator = fileList.iterator(); iterator.hasNext();) {
                    filePath = String.valueOf(iterator.next());

                    deleteFile(filePath);
                }
            }

            //upload file
            uploadFile(list,event,wfDO,"");


            //delete the data from frw_form_draft
            deleteFormDraft(wfDO.getFormId(),wfDO.getFormUid());

            //save the data to frw_form_draft
            dao.insertFormDraft(wfDO);
        }
        catch (StorageException e) {
            throw new FormException("Error Uploading File", e);
        }
        catch (IOException e) {
            throw new FormException("Error Uploading File", e);
        }


    }

    public Collection getFormDraft(DaoQuery query, String userId, String sort,
                                   boolean desc, int start, int rows) throws FormDaoException {
        FormDao dao = null;
        dao = (FormDao) getDao();
        return dao.getFormDraft(query, userId, sort, desc, start, rows);
    }

    public FormWorkFlowDataObject getFormDraft(String formUid) throws FormDaoException {
        FormWorkFlowDataObject fwfdo = null;

        FormDao dao = (FormDao) getDao();
        Collection draft = dao.getFormDraft(formUid);
        if (draft.iterator().hasNext())
            fwfdo = (FormWorkFlowDataObject) draft.iterator().next();
        return fwfdo;
    }

    public int getFormDraftCount(DaoQuery query, String userId) throws FormDaoException {
        FormDao dao = null;
        dao = (FormDao) getDao();
        return dao.getFormDraftCount(query, userId);
    }

    public void approvalCycle(FormWorkFlowDataObject wfDO, Event evt) throws FormDaoException, FormDocumentException, FormException {
        FormDao dao;
        FormWorkFlowDataObject wf = null;
        String receiverID = null;
        String body;
        String subject;

        dao = (FormDao) getDao();


        FormDataObject formsDO = getForm(wfDO.getFormId());

        Collection approver = dao.getApproverByPriority(wfDO.getFormId(), 1);

        if (approver.iterator().hasNext())
            wf = (FormWorkFlowDataObject) approver.iterator().next();

        if (wf != null) {
            wfDO.setWorkflowFlag(1);
            wfDO.setFormApprovalId(wf.getFormApprovalId());
            wfDO.setApproverId(wf.getApproverId());
            wfDO.setFormApprovalDate(new Date());
            wfDO.setStatus(WORKFLOW_PENDING);
            wfDO.setFormSubmissionDate(new Date());
            receiverID = wf.getApproverId();
        }


        try {
            //auto approve data if the form submitter and 1st level approver is the same user
            if (wf != null) {
                dao.insertFormWorkFlow(wfDO);
                if (wfDO.getUserId().equals(receiverID)) {
                    approveFormData(wfDO.getFormUid(), "approve", receiverID, evt, false);
                }
                else {
                    body = getApproverMessageBody(wfDO, formsDO);
                    subject = Application.getInstance().getMessage("formWizard.notification.approval.pending.title","Form Submission Approval Required: ")+formsDO.getFormDisplayName();//"Approval Required""Form submitted for approval";
                    sendNotification(wfDO.getFormId(), wfDO.getUserId(), receiverID, body, subject, null);

                }

            }
        }
        catch (SecurityException e) {
            Log.getLog(getClass()).error("User not found", e);
        }
    }

    public void deleteDraft(String formUid) throws FormDaoException, FormDocumentException  {
        FormWorkFlowDataObject obj, wfDO;


        wfDO = new FormWorkFlowDataObject();


        obj = getFormDraft(formUid);
        wfDO.setFormId(obj.getFormId());
        wfDO.setFormName(obj.getFormName());


        wfDO.setFormUid(formUid);

        //delete from frw_draft
        deleteFormDraft(wfDO.getFormId(), wfDO.getFormUid());

        //delete data and file
        deleteFormData(wfDO.getFormName(), formUid, wfDO.getFormId());

    }

    public Collection getViewForms(DaoQuery query, String userId, String sort,
                                   boolean desc, int start, int rows) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        return dao.selectViewForms(query, userId, sort, desc, start, rows);
    }

    public int getViewFormsCount(DaoQuery query, String userId) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        return dao.selectViewFormsCount(query, userId);
    }

    public Collection getEditForms(DaoQuery query, String sort,
                                   boolean desc, int start, int rows) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        return dao.selectEditForms(query, sort, desc, start, rows);
    }

    public int getEditFormsCount(DaoQuery query) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        return dao.selectEditFormsCount(query);
    }

    public Collection getQueryForms(DaoQuery query, String userId, String sort,
                                    boolean desc, int start, int rows) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        return dao.selectQueryForms(query, userId, sort, desc, start, rows);
    }

    public int getQueryFormsCount(DaoQuery query, String userId) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        return dao.selectQueryFormsCount(query, userId);
    }

    public String getFormNotificationMethod(String formId) throws FormDaoException {
        FormDataObject fdo = getForm(formId);
        return fdo.getFormApprMethod();
    }

    public FormWorkFlowDataObject getFormWFDetail(String formUid) throws FormDaoException {
        FormWorkFlowDataObject fwfdo = null;
        FormDao dao = (FormDao) getDao();
        Collection collection = dao.selectFormWFDetail(formUid);
        if (collection.iterator().hasNext())
            fwfdo = (FormWorkFlowDataObject) collection.iterator().next();
        return fwfdo;
    }

    public FormWorkFlowDataObject getNextPriorityApprover(String formId, int priority) throws FormDaoException {
        FormWorkFlowDataObject fwfdo = null;
        FormDao dao = (FormDao) getDao();
        Collection collection = dao.getNextPriorityApprover(formId, priority);
        if (collection.iterator().hasNext())
            fwfdo = (FormWorkFlowDataObject) collection.iterator().next();
        return fwfdo;

    }

    public void deleteFormsWorkFlow(String formUid) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        dao.deleteFormsWorkFlow(formUid);
    }

    public void updateFormsWorkFlow(FormWorkFlowDataObject fwfdo) throws FormDaoException {
        FormDao dao = (FormDao) getDao();
        dao.updateFormWorkFlow(fwfdo);
    }


}




