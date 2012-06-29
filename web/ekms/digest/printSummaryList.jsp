<%@ include file="/common/header.jsp" %>
<%@ page import="kacang.Application,
				com.tms.portlet.taglibs.PortalServerUtil,
				com.tms.cms.digest.model.MailingListDataObject,
				kacang.runtime.*,
				kacang.ui.*,
				java.util.*,
				com.tms.cms.digest.model.DigestModule,
				com.tms.cms.digest.model.DigestDaoOracle,
				com.tms.cms.digest.model.DigestDataObject,
				com.tms.cms.digest.model.DigestContentDataObject,
				kacang.model.operator.DaoOperator,
				kacang.model.operator.OperatorEquals,
				kacang.model.operator.OperatorIn,
				java.text.SimpleDateFormat,
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
   String mailingListId = (String)WidgetManager.getWidgetManager(request).getAttribute("summarymailingListId");
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
    	
    	String[] selectedKeys = (String [])WidgetManager.getWidgetManager(request).getAttribute("selectedsummary");
    	if(selectedKeys.length>0){
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
				query3.addProperty(new OperatorEquals("cms_digest.digestIssueId", mldo.getDigestIssue(), DaoOperator.OPERATOR_AND));
				query3.addProperty(new OperatorIn("cms_digest.digestId", selectedKeys, DaoOperator.OPERATOR_AND));
				mldo.setDigest(digest.getDigestMain(query3));
				mldo.setEmailFormatType("Summaries List");
	        }
			} catch (DigestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	ContentManager cm = (ContentManager) Application.getInstance().getModule(ContentManager.class);    	
	    	SimpleDateFormat sdf= new SimpleDateFormat("dd MMM yyyy");
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

	        mailbuffer.append("<tr><td >");
	        mailbuffer.append("<table border=0 cellpadding=5 cellspacing=1 width=\"100%\">");
	        mailbuffer.append("<tr><td bgcolor=\"BBD5F2\" width=\"1%\" align=\"right\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">No.</td>");
	        mailbuffer.append("<td bgcolor=\"BBD5F2\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">"+Application.getInstance().getMessage("digest.label.news")+"</font></td>");
	        mailbuffer.append("<td bgcolor=\"BBD5F2\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">"+Application.getInstance().getMessage("digest.label.sectors")+"</font></td>");
	        mailbuffer.append("<td bgcolor=\"BBD5F2\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">"+Application.getInstance().getMessage("digest.label.country")+"</font></td>");
	        mailbuffer.append("<td bgcolor=\"BBD5F2\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">"+Application.getInstance().getMessage("digest.label.company")+"</font></td>");
	        mailbuffer.append("<td bgcolor=\"BBD5F2\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">"+Application.getInstance().getMessage("digest.label.sourceByLine")+"</font></td>");
	        //mailbuffer.append("<td bgcolor=\"D5EFAB\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\">"+Application.getInstance().getMessage("digest.label.sourceDate")+"</font></td>");        
	        mailbuffer.append("</tr>");
	        
	        int no=1;
	        for (Iterator i = mldo.getDigest().iterator(); i.hasNext();)
	        {
	        	DigestDataObject ddo = (DigestDataObject) i.next();
	        	ddo.setContents(digest.selectDigestContents(ddo.getDigestId(),null,null,true, 0, -1, true));        	
	        	for (Iterator j = ddo.getContents().iterator(); j.hasNext();)
	            {        		
	        		DigestContentDataObject dcdo = (DigestContentDataObject) j.next();
	        		String color="#FFFFFF";
	        		if(no%2==0)
	        			color="F3F3F3";
	        		else{
	        			color="#FFFFFF";
	        		}
	        		mailbuffer.append("<tr><td bgcolor=\""+color+"\" valign=\"top\" width=\"1%\" align=\"right\"><font size=\"1\" face=\"Arial, Helvetica, sans-serif\">"+no+"</font></td>");
	        		String summary=dcdo.getSummary();
	        		if(summary==null||"".equals(summary)){
	        			summary="Abstract N/A.";	
	        		}
	        		if(dcdo.getContentId().startsWith("com.tms.cms.document.Document_")){
		        		mailbuffer.append("<td bgcolor=\""+color+"\" valign=\"top\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\"><b><a  target=\"login\" href=\""+serverUrl+"/cms/documentstorage/"+dcdo.getContentId()+"/"+dcdo.getFilename()+"\">"+dcdo.getName()+"</a></b><br/><font size=\"1\">"+summary+"</font></font></td>");
		        		//mailbuffer.append("<td bgcolor=\""+color+"\" valign=\"top\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\"><b><a  target=\"login\" href=\""+serverUrl+"/ekms/digest/displayDigestContent.jsp?url=/cms/documentstorage/"+dcdo.getContentId()+"/"+dcdo.getFilename()+"\">"+dcdo.getName()+"</a></b><br/><font size=\"1\">"+summary+"</font></font></td>");
	        		}else{
	        			mailbuffer.append("<td bgcolor=\""+color+"\" valign=\"top\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\"><b><a  target=\"login\" href=\""+serverUrl+"/ekms/content/content.jsp?id="+dcdo.getContentId()+"\">"+dcdo.getName()+"</a></b><br/><font size=\"1\">"+summary+"</font></font></td>");
	        			//mailbuffer.append("<td bgcolor=\""+color+"\" valign=\"top\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\"><b><a  target=\"login\" href=\""+serverUrl+"/ekms/digest/displayDigestContent.jsp?url=/ekms/content/content.jsp?id="+dcdo.getContentId()+"\">"+dcdo.getName()+"</a></b>\n<font size=\"1\">"+summary+"</font></font></td>");
	        		}
	        		mailbuffer.append("<td bgcolor=\""+color+"\" valign=\"top\"><font size=\"1\" face=\"Arial, Helvetica, sans-serif\">"+dcdo.getSector()+"</font></td>");
	        		mailbuffer.append("<td bgcolor=\""+color+"\" valign=\"top\"><font size=\"1\" face=\"Arial, Helvetica, sans-serif\">"+dcdo.getCountry()+"</font></td>");
	        		mailbuffer.append("<td bgcolor=\""+color+"\" valign=\"top\"><font size=\"1\" face=\"Arial, Helvetica, sans-serif\">"+dcdo.getCompany()+"</font></td>");
	        		String source=dcdo.getAllsource();
	        		if(source==null||"".equals(source)){
	        			source="N/A";	
	        		}
	        		mailbuffer.append("<td bgcolor=\""+color+"\" valign=\"top\"><font size=\"1\" face=\"Arial, Helvetica, sans-serif\">"+source+"</font></td>");
	        		//mailbuffer.append("<td bgcolor=\""+color+"\" valign=\"top\"><font size=\"1\" face=\"Arial, Helvetica, sans-serif\">"+sdf.format(dcdo.getSourceDate())+"</font></td></tr>");
	        		       		
	        		no+=1;
	            }
	        }
	        mailbuffer.append("</table>");
	        mailbuffer.append("</td></tr>");
	        mailbuffer.append("</table>");
	        String formattedMail = mailbuffer.toString();
	        //String formattedMail = StringUtils.replace(mailbuffer.toString(), "\n", "<br>");
%>
<%=formattedMail%>
<%

    	}}
%>