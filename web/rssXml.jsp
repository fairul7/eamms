<%@ page pageEncoding="UTF-8" %><%--
--%><%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%--
--%><%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %><%--
--%><%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %><%--
--%><%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt-rt" %><%--
--%><%@ taglib uri="http://devsphere.com/xml/taglib/output" prefix="o"%><%--
--%><o:document><%--
--%><%@ taglib uri="kacang.tld" prefix="x" %><%--
--%><%@ page import="com.tms.collab.rss.model.*,kacang.Application,com.tms.collab.rss.*,java.util.*,kacang.model.Module,com.tms.collab.rss.model.Item,com.tms.collab.rss.model.Channel,com.tms.collab.rss.model.RssHandler,com.tms.collab.rss.model.RssAble,com.tms.ekms.setup.model.SetupModule"%>
<%
    // declare variable
    String id = (String)request.getParameter("channelId");
    String strChannel_title;
    String strChannel_description;
    String strChannel_link;
    String strItem_ChannelId;
    String strItem_ItemId;
	String strChannel_updateDate;
    //get application
    
    RssHandler handler = (RssHandler)Application.getInstance().getModule(RssHandler.class);
    Item item = new Item();
    Channel channel = handler.getOneChannel(id);
    Collection collection = handler.getAllItemsByChannelId(id);

	SetupModule setupModel = (SetupModule) Application.getInstance().getModule(SetupModule.class);
	String siteURL = "";
	try {
		siteURL = setupModel.get("siteUrl");
	} catch (Exception e) {
		System.out.println(e.toString());
	}

	if (channel != null){
    	strChannel_title = channel.getTitle();
    	strChannel_description = channel.getDescription();
    	strChannel_link = channel.getLink();
    	strChannel_updateDate = channel.getUpdateDate().toString();
    	pageContext.setAttribute("channelTitle", strChannel_title);
        pageContext.setAttribute("channelLink", strChannel_link);
        pageContext.setAttribute("channelDescription", strChannel_description);
        pageContext.setAttribute("strChannel_updateDate", strChannel_updateDate);
    }

    pageContext.setAttribute("siteURL", siteURL);
%>
<c:set var="version" value="0.91"/>
<c:set var="channelTitle" value="${channelTitle}"/>
<c:set var="channelLink" value="${channelLink}"/>
<c:set var="channelDescription" value="${channelDescription}"/>
<c:set var="strChannel_updateDate" value="${strChannel_updateDate}"/>
<c:set var="siteURL" value="${siteURL}"/>
    <o:element name="rss" attr="version">
    <o:element name="channel">
        <o:element name="title">
            <o:data><c:out value="${channelTitle}"/></o:data>
        </o:element>
        <o:element name="link">
            <o:data><c:out value="${siteURL}"/>/<c:out value="${channelLink}"/></o:data>
        </o:element>
        <o:element name="description">
            <o:data><c:out value="${channelDescription}"/></o:data>
        </o:element>
        <o:element name="image">
			<o:element name="url">
				<o:data>/ekms/images/lg_logo.gif</o:data>
	        </o:element>
	        <o:element name="title">
				<o:data>The Media Shoppe</o:data>
	        </o:element>
        </o:element>
        <o:element name="language">
            <o:data>en-us</o:data>
        </o:element>
        <o:element name="pubDate">
            <o:data><c:out value="${strChannel_updateDate}"/></o:data>
        </o:element>
<%	
    try {
        String strChannel_ModuleId;
        strChannel_ModuleId = channel.getModuleId();
        RssAble rssable = (RssAble)Application.getInstance().getModule(Class.forName(strChannel_ModuleId));
		rssable.setChannelId(id);
        
		for (Iterator i=collection.iterator() ; i.hasNext(); )
        {
			item = (Item)i.next();
			strItem_ChannelId = item.getChannelId();
			strItem_ItemId = item.getItemId();
	        String rssItemDesc = rssable.getItemDesc(strItem_ItemId);
			String rssItemTitle = rssable.getItemTitle(strItem_ItemId);
			String rssItemLink = rssable.getItemLink(strItem_ItemId);
			String rssItemAuthor = rssable.getItemAuthor(strItem_ItemId);
			String rssItemPubDate = rssable.getItemPubDate(strItem_ItemId);
        	
			//System.out.println("rssItemTitle="+rssItemTitle);
			
        	pageContext.setAttribute("itemItemId", strItem_ItemId);
			pageContext.setAttribute("rssItemDesc", rssItemDesc);
			pageContext.setAttribute("rssItemTitle", rssItemTitle);
			pageContext.setAttribute("rssItemLink", rssItemLink);
			pageContext.setAttribute("rssItemAuthor", rssItemAuthor);
			pageContext.setAttribute("rssItemPubDate", rssItemPubDate);
%>
			<c:forEach var="rssItemDesc" items="${rssItemDesc}">
			<c:forEach var="rssItemTitle" items="${rssItemTitle}">
			<c:forEach var="rssItemLink" items="${rssItemLink}">
			<c:forEach var="rssItemAuthor" items="${rssItemAuthor}">
			<c:forEach var="rssItemPubDate" items="${rssItemPubDate}">
		    <o:element name="item">        
			    <o:element name="title">
		            <o:data>
		            	<c:out value="${rssItemTitle}" escapeXml="false"/>
		            </o:data>
		        </o:element>
		        <o:element name="link">
		            <o:data><c:out value="${siteURL}"/>/<c:out value="${rssItemLink}"/></o:data>
		        </o:element>
		        <o:element name="description">
		            <o:data><c:out value="${rssItemDesc}" escapeXml="false"/></o:data>
		        </o:element>
		        <o:element name="author">
		            <o:data><c:out value="${rssItemAuthor}" escapeXml="false"/></o:data>
		        </o:element>
		        <o:element name="pubDate">
		            <o:data><c:out value="${rssItemPubDate}"/></o:data>
		        </o:element>
	        </o:element>  
			</c:forEach>
            </c:forEach>
            </c:forEach>
            </c:forEach>      
            </c:forEach>      
<%		
        }
	} catch (Exception e){
		System.out.println("Jsp Error="+e.toString());
	}
%>
	</o:element>
</o:element>
</o:document>

