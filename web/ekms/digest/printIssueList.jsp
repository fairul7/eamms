<%@ include file="/common/header.jsp" %>
<%@ page import="kacang.Application,
				com.tms.portlet.taglibs.PortalServerUtil,
				com.tms.cms.digest.model.MailingListDataObject,
				kacang.runtime.*,
				kacang.ui.*,
				java.util.*,
				com.tms.cms.digest.model.DigestModule,
				com.tms.cms.digest.model.DigestDataObject,
				com.tms.cms.digest.model.DigestContentDataObject,
				kacang.model.operator.DaoOperator,
				kacang.model.operator.OperatorEquals,
				kacang.model.DaoQuery,
				com.tms.cms.digest.model.DigestException,
				kacang.util.Log,
				com.tms.ekms.setup.model.SetupModule,
				com.tms.ekms.setup.model.SetupException,
				org.apache.commons.lang.StringUtils,
				com.tms.cms.core.model.ContentManager,
				com.tms.cms.core.model.ContentPublisher,
				com.tms.cms.core.model.ContentObject,
				kacang.model.DataObjectNotFoundException,
				kacang.services.security.User,
				kacang.services.security.SecurityService"%>

<html>
<head>
<jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body onload="window.print();">

<% 
   String mailingListId =  (String) request.getParameter("mailingListId");
   String reportType=null;
   //String mailingListId = (String)WidgetManager.getWidgetManager(request).getAttribute("printmailingListId");
    if(mailingListId!=null)
    {
    	//get the spot from content as the email header
    	ContentObject mailHeaderSpot=null;
    	try {
            String key = Application.getInstance().getProperty("digest.mail.headerSpotId");
    		User user = ((SecurityService)Application.getInstance().getService(SecurityService.class)).getCurrentUser(request);
            ContentPublisher cm = (ContentPublisher)Application.getInstance().getModule(ContentPublisher.class);
            mailHeaderSpot = cm.view(key, user);
        }
        catch(DataObjectNotFoundException e) {
        	//cannot found the spot
        }
    	
    	DigestModule digest = (DigestModule) Application.getInstance().getModule(DigestModule.class);
    	DaoQuery query=new DaoQuery();
		query.addProperty(new OperatorEquals("mailingListId", mailingListId, DaoOperator.OPERATOR_AND));
    	Collection mail;
		MailingListDataObject mldo=new MailingListDataObject();
		try {
			mail = digest.getMailingList(query);
        for (Iterator i = mail.iterator(); i.hasNext();)
        {
        	mldo = (MailingListDataObject) i.next();	
        	DaoQuery query2=new DaoQuery();
			query2.addProperty(new OperatorEquals("mailingListId", mldo.getMailingListId(), DaoOperator.OPERATOR_AND));
			mldo.setRecipients(digest.getMailingRecipients(query2));	
			DaoQuery query3=new DaoQuery();
			query3.addProperty(new OperatorEquals("digestIssueId", mldo.getDigestIssue(), DaoOperator.OPERATOR_AND));
			mldo.setDigest(digest.getDigestMain(query3));
			mldo.setEmailFormatType("Issue List");
			reportType=mldo.getMailFormat();
        }
		} catch (DigestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    	if("digestFormat".equals(reportType)){
    		String serverUrl = "http://";
            SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
            try
            {
                serverUrl = setup.get("siteUrl");
            }
            catch (SetupException e)
            {
                Log.getLog(DigestModule.class).error(e);
            }
        	Collection userId=new ArrayList();
        	Collection users=new ArrayList();
        	//mldo.getDigestIssueName()
        	StringBuffer mailbuffer = new StringBuffer();
        	mailbuffer.append("\n");
        	mailbuffer.append("<table border=1 cellpadding=0 cellspacing=0 width=\"100%\">");
            mailbuffer.append("<tr>"+
            		"<td >"+((mailHeaderSpot!=null)?mailHeaderSpot.getSummary():"[No content found]")+"</td></tr>");

            
            mailbuffer.append("<tr><td ><table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"1\">");
            
            mailbuffer.append("<tr><td width=\"1%\" align=\"right\" bgcolor=\"BBD5F2\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">No.</font></td><td bgcolor=\"BBD5F2\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">"+Application.getInstance().getMessage("digest.label.digest")+"</font></td></tr>");
            
            int no=1;
            for (Iterator i = mldo.getDigest().iterator(); i.hasNext();)
            {
            	DigestDataObject ddo = (DigestDataObject) i.next();
            	String color="#FFFFFF";
        		if(no%2==0)
        			color="#F3F3F3";
        		else{
        			color="#FFFFFF";
        		}
        		mailbuffer.append("<tr><td width=\"1%\" align=\"right\" bgcolor=\""+color+"\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">"+no+"</font></td><td bgcolor=\""+color+"\"><a target=\"login\" href=\""+serverUrl+"/ekms/digest/digestContentList.jsp?digestId="+ddo.getDigestId()+"\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">"+ddo.getDigestName()+"</font></a></td></tr>");
            	no+=1;
            }
            mailbuffer.append("</table>");
            mailbuffer.append("</td></tr>");
            mailbuffer.append("</table>");
            //String formattedMail = StringUtils.replace(mailbuffer.toString(), "\n", "<br>");
            String formattedMail = mailbuffer.toString();
    	
%>
<%=formattedMail%>
<%
    	}else if("newsFormat".equals(reportType)){
        	ContentManager cm = (ContentManager) Application.getInstance().getModule(ContentManager.class);
        	String serverUrl = "http://";
            SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
            try
            {
                serverUrl = setup.get("siteUrl");
            }
            catch (SetupException e)
            {
                Log.getLog(DigestModule.class).error(e);
            }
        	Collection userId=new ArrayList();
        	Collection users=new ArrayList();

        	StringBuffer mailbuffer = new StringBuffer();
        	mailbuffer.append("<br/>");
        	mailbuffer.append("<table border=1 cellpadding=0 cellspacing=0 width=\"100%\">");
            mailbuffer.append("<tr>"+
            		"<td >"+((mailHeaderSpot!=null)?mailHeaderSpot.getSummary():"[No content found]")+"</td></tr>");

            mailbuffer.append("<tr><td colspan=2 bgcolor=\"#F3F3F3\"><table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"0\"><tr><td bgcolor=\"#F3F3F3\">");
                 
            for (Iterator i = mldo.getDigest().iterator(); i.hasNext();)
            {
            	DigestDataObject ddo = (DigestDataObject) i.next();
            	ddo.setContents(digest.selectDigestContents(ddo.getDigestId(),null,null,true, 0, -1, false));
            	int no=1;
            	mailbuffer.append("<b><font color=\"#0000FF\" size=\"+1\">"+ddo.getDigestName()+"</font></b><br/><br/>");
            	for (Iterator j = ddo.getContents().iterator(); j.hasNext();)
                {
            		DigestContentDataObject dcdo = (DigestContentDataObject) j.next();
            		
            		if(dcdo.getContentId().startsWith("com.tms.cms.document.Document_")){
            			//mailbuffer.append(no+". <font size=\"-1\"><b><a target=\"login\" href=\""+serverUrl+"/ekms/digest/displayDigestContent.jsp?url=/cms/documentstorage/"+dcdo.getContentId()+"/"+dcdo.getFilename()+"\"><strong>"+dcdo.getName()+"</strong></a></b>\n");
            			mailbuffer.append(no+". <font size=\"-1\"><b><a target=\"login\" href=\""+serverUrl+"/cms/documentstorage/"+dcdo.getContentId()+"/"+dcdo.getFilename()+"\"><strong>"+dcdo.getName()+"</strong></a></b><br/>");
            		}else{
            			mailbuffer.append(no+". <font size=\"-1\"><b><a target=\"login\" href=\""+serverUrl+"/ekms/content/content.jsp?id="+dcdo.getContentId()+"\"><strong>"+dcdo.getName()+"</strong></a></b><br/>");
            			//mailbuffer.append(no+". <font size=\"-1\"><b><a target=\"login\" href=\""+serverUrl+"/ekms/digest/displayDigestContent.jsp?url=/ekms/content/content.jsp?id="+dcdo.getContentId()+"\"><strong>"+dcdo.getName()+"</strong></a></b>\n");
            		}
            		
            		String summary=dcdo.getSummary();
            		if(summary==null||"".equals(summary)){
            			summary="Abstract N/A.";	
            		}
            		mailbuffer.append(summary+"</font><br/><br/>");
            		no+=1;
                }
            }
            mailbuffer.append("</td></tr>");
            mailbuffer.append("</table></td></tr></table>");
            //String formattedMail = StringUtils.replace(mailbuffer.toString(), "\n", "<br>");
            String formattedMail = mailbuffer.toString();
            %>
            <%=formattedMail%>
            <%
    	}
}%>