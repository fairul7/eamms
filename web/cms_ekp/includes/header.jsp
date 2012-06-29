<%@ include file="/common/header.jsp" %>

<x:template type="TemplateDisplaySetup" name="setup" scope="request"/>


<HTML>
<HEAD>
<TITLE><c:out value='${setup.propertyMap.siteName}'/></TITLE>
<LINK rel="stylesheet" href="<c:out value='${setup.propertyMap.siteStyleSheet}'/>">
<META name="Keywords" content="<c:out value='${setup.propertyMap.siteMetaKeywords}'/>">
<META name="Description" content="<c:out value='${setup.propertyMap.siteMetaDescription}'/>">
</HEAD>

<BODY BGCOLOR="#ffffff" TEXT="#000000" LINK="#FF6600"" ALINK="#000000"
VLINK="#FF6600" LEFTMARGIN=0 RIGHTMARGIN=0 TOPMARGIN=0 MARGINHEIGHT=0 MARGINWIDTH=0>

<x:template type="TemplateDisplayFrontEndEditSelector" />

<table cellpadding=0 cellspacing=0 border=0 width=96%>
	<tr>
	<td valign=middle align=left height=5 width=100% bgcolor="FF6600"><IMG SRC="images/tp_orgsm.gif" height=5 width=98 border=0></td>
	</tr>
	<tr>
	<td valign=middle align=left height=1 width=100%><IMG SRC="images/clear.gif" height=1 width=770 border=0></td>
	</tr>
</table>

<table cellpadding=0 cellspacing=0 border=0 width=96%>
	<tr>
	<td valign=top align=left width=240 height=56 rowspan=2><a href="<c:url value="/cms/" />"><IMG SRC="<c:out value='${setup.propertyMap.siteLogo}'/>" border=0></a></td>
	<td valign=top align=right>


		<table cellpadding=0 cellspacing=0 border=0>
			<tr>
			<td valign=middle align=left width=20 height=18 bgcolor="000000"><IMG SRC="images/tpmenu_t.gif" height=18 width=16 border=0></td>
			<td valign=middle align=left height=18 bgcolor="000000"><a href="index.jsp" class="topmenu">home</a></td>

            <x:template name="pages" type="TemplateDisplayChildren" properties="id=com.tms.cms.section.Section_Pages" body="custom">
                <c:forEach items="${pages.children}" var="page" varStatus="stat" >
			<td valign=middle align=center width=16 height=18 bgcolor="000000"><IMG SRC="images/tpmenu_e.gif" height=18 width=9 border=0></td>
			<td valign=middle align=left height=18 bgcolor="000000" nowrap><a href="content.jsp?id=<c:out value='${page.id}'/>"><c:out value='${page.name}'/></a></td>
                </c:forEach>
            </x:template>
            <c:choose>
            <c:when test="${empty sessionScope.currentUser || sessionScope.currentUser.id == 'anonymous'}">
    			<td valign=middle align=center width=16 height=18 bgcolor="000000"><IMG SRC="images/tpmenu_e.gif" height=18 width=9 border=0></td>
				<td valign=middle align=left height=18 bgcolor="000000"><a href="login.jsp" class="topmenu">login</a></td>
				<td valign=middle align=center width=16 height=18 bgcolor="000000"><IMG SRC="images/tpmenu_e.gif" height=18 width=9 border=0></td>
				<td valign=middle align=left height=18 bgcolor="000000" nowrap><a href="signup.jsp" class="topmenu">sign up</a></td>
            </c:when>
            <c:otherwise>
                <x:permission module="com.tms.cms.core.model.ContentManager" permission="com.tms.cms.SubscribeContent">
                    <td valign=middle align=center width=16 height=18 bgcolor="000000"><IMG SRC="images/tpmenu_e.gif" height=18 width=9 border=0></td>
                    <td valign=middle align=left height=18 bgcolor="000000"><a href="subscription.jsp" class="topmenu">subscriptions</a></td>
                </x:permission>
				<td valign=middle align=center width=16 height=18 bgcolor="000000"><IMG SRC="images/tpmenu_e.gif" height=18 width=9 border=0></td>
				<td valign=middle align=left height=18 bgcolor="000000"><a href="profile.jsp" class="topmenu">profile</a></td>
				<td valign=middle align=center width=16 height=18 bgcolor="000000"><IMG SRC="images/tpmenu_e.gif" height=18 width=9 border=0></td>
				<td valign=middle align=left height=18 bgcolor="000000"><a href="logout.jsp" class="topmenu">logout</a></td>
            </c:otherwise>
            </c:choose>

			<td valign=middle align=left width=10 height=18 bgcolor="000000"><IMG SRC="images/clear.gif" height=18 width=10 border=0></td>
			</tr>
		</table>


	</td>
	</tr>
	</tr>
	<td valign=bottom align=right><span class="textsmall">Logged in as : [ <span class="textsmall2">
	    <c:out value='${sessionScope.currentUser.username}'/>
    </span> ]
	</span></td>
	</tr>
	<tr>
	<td valign=top align=left width=100% background="images/dotline_770.gif" height=3 colspan=2><IMG SRC="images/clear.gif" height=1 width=770 border=0></td>
	</tr>
</table>



<table cellpadding=0 cellspacing=0 border=0 width=96%>
<tr>
<td valign=middle align=left width=196 height=18><IMG SRC="images/clear.gif" height=1 width=196 border=0></td>
<td valign=middle align=left width=404 height=18>&nbsp;</td>
<c-rt:set var="currentDate" value="<%= new java.util.Date() %>"/>
<td valign=middle align=right height=18><span class="dateFont"><fmt:formatDate value="${currentDate}" pattern="${globalDateLong}" /></span></td>
</tr>
</table>


<table cellpadding=0 cellspacing=0 border=0 width=96%>
	<tr>
	<td valign=top align=left width=182 background="images/bg_menu.gif" rowspan="2">

    <!-- Search -->
    <table cellpadding=0 cellspacing=0 border=0 width=182>
        <tr>
        <td valign=top align=left width=18><IMG SRC="images/clear.gif" height=1 width=18 border=0></td>
        <td valign=top align=center width=164 bgcolor="ffffff"><IMG SRC="images/clear.gif" height=10 width=2 border=0><br>

            <table cellpadding=0 cellspacing=1 border=0 width=142>
            <form action="search.jsp" method="get">
            <tr>
            <td valign=middle align=left width=42><span style="font-size:6.5pt">Search:</span></td>
            <td valign=top align=right width=100><input type="text" name="query" size="13"></td>
            </tr>
            <tr>
            <td valign=top align=right width=142 colspan=2><input type="image" SRC="images/ic_go.gif" width=23 height=11 border=0></td>
            </tr>
            <tr>
            <td valign=top align=right width=142 colspan=2><IMG  SRC="images/clear.gif" width=1 height=6 border=0></td>
            </tr>
            </form>
            </table>
        </td>
        </tr>
        <tr>
        <td valign=top align=left width=18><IMG SRC="images/clear.gif" height=1 width=18 border=0></td>
        <td valign=top align=center width=164 bgcolor="ffffff"><IMG SRC="images/dotline_164.gif" height=7 width=164 border=0></td>
        </tr>
        <tr>
        <td valign=top align=left width=18><IMG SRC="images/clear.gif" height=1 width=18 border=0></td>
        <td valign=top align=center width=164 bgcolor="ffffff"><IMG SRC="images/clear.gif" height=8 width=1 border=0></td>
        </tr>
    </table>

    <!-- Menu -->
	<table cellpadding=0 cellspacing=0 border=0 width=182>
	<tr>
	<td valign=top align=left width=17><IMG SRC="images/clear.gif" height=1 width=17 border=0></td>
	<td valign=top align=left width=165 colspan=2 bgcolor="ffffff"><IMG SRC="images/clear.gif" height=1 width=2 border=0>
        <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_MenuSpot"/>
    </td>
	</tr>
	<tr>
	<td valign=top align=left width=17><IMG SRC="images/clear.gif" height=1 width=17 border=0></td>
	<td colspan="2" bgcolor="white">
        <x:template
            name="menu"
            type="TemplateDisplayMenu"
            properties="id=com.tms.cms.section.Section_Sections&hideSummary=true&orphans=true" />
    </td>
    </tr>
	</table>

    <table cellpadding=0 cellspacing=0 border=0 width=182>
    <tr>
    <td valign=top align=left width=18><IMG SRC="images/clear.gif" height=1 width=17 border=0></td>
    <td valign=top align=left width=164 bgcolor="ffffff"><IMG SRC="images/clear.gif" height=8 width=2 border=0></td>
    </tr>
    <tr>
    <td valign=top align=left width=18><IMG SRC="images/clear.gif" height=1 width=18 border=0></td>
    <td valign=top align=center width=164 bgcolor="ffffff"><IMG SRC="images/dotline_164.gif" height=7 width=164 border=0></td>
    </tr>
    </table>

	<table cellpadding=2 cellspacing=0 border=0 width=182>
	<tr>
	<td valign=top align=left width=17><IMG SRC="images/clear.gif" height=1 width=17 border=0></td>
	<td valign=top align=left width=165 colspan=2><IMG SRC="images/clear.gif" height=1 width=2 border=0>
        <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_EventSpot" />
    </td>
	</tr>
    </table>

	</td>
	<td valign=top align=left width=14 background="images/ver_line.gif" rowspan="2"><IMG SRC="images/clear.gif" height=1 width=14 border=0></td>

<%
	boolean big=false;
	String id = request.getParameter("id");
      if(id!=null && id.startsWith("com.tms.cms.article")) {
		big=true;
	}
	pageContext.setAttribute("big", new Boolean(big));
%>
	<td valign=top align=left
		<c:if test="${!big}">width="50%"</c:if>
		<c:if test="${big}">width="100%"</c:if>
      >


