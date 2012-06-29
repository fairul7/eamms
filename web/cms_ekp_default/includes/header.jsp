<%@ include file="/common/header.jsp" %>

<x:template type="TemplateDisplaySetup" name="setup" scope="request"/>

<HTML>
<HEAD>
<TITLE><c:out value='${setup.propertyMap.siteName}'/></TITLE>
<LINK rel="stylesheet" href="<c:out value='${setup.propertyMap.siteStyleSheet}'/>">
<META name="Keywords" content="<c:out value='${setup.propertyMap.siteMetaKeywords}'/>">
<META name="Description" content="<c:out value='${setup.propertyMap.siteMetaDescription}'/>">
</head>
<BODY BGCOLOR="#ffffff" TEXT="#000000" LINK="#000000" LEFTMARGIN=0 RIGHTMARGIN=0 TOPMARGIN=0 MARGINHEIGHT=0 MARGINWIDTH=0>
<!--- Header --->
<table cellpadding=0 cellspacing=0 border=0 width="780">
<tr>
<td valign=top align=left width=10 bgcolor="000000"><IMG SRC="images/clear.gif" height=75 width=10 border=0></td>
<td valign=middle align=left width=195><IMG SRC="<c:out value='${setup.propertyMap.siteLogo}'/>" height=67 width=190 border=0></td>
<td valign=top align=left height=75>

	<!---- top menu --->
    <c:choose>
    <c:when test="${empty sessionScope.currentUser || sessionScope.currentUser.id == 'anonymous'}">

	<table cellpadding=0 cellspacing=0 border=0 width="100%">
	<tr>
	<td valign=middle align=center height=2 width=70 bgcolor="CC0000"><IMG SRC="images/clear.gif" height=2 width=1 border=0></td>
	<td valign=middle align=center height=2 width=1><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
	<td valign=middle align=center height=2 width=98 bgcolor="CC0000"><IMG SRC="images/clear.gif" height=2 width=1 border=0></td>
	<td valign=middle align=center height=2 width=1><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
	<td valign=middle align=center height=2 bgcolor="000000"><IMG SRC="images/clear.gif" height=2 width=1 border=0></td>
	</tr>
	<tr>
	<td valign=middle align=center height=1 colspan=5><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
	</tr>
	<tr>
	<td valign=bottom align=left height=35 width=70 bgcolor="DADADA">&nbsp;<a href="index.jsp" class="topmenu">Home</a></td>
	<td valign=middle align=center height=2 width=1><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
	<td valign=bottom align=left height=2 width=88 bgcolor="DADADA">&nbsp;<a href="login.jsp" class="topmenu01">Login</a></td>
	<td valign=middle align=center height=2 width=1><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
	<td valign=bottom align=left height=2 bgcolor="515151"><IMG SRC="images/clear.gif" height=2 width=1 border=0></td>
	</tr>
	<tr>
	<td valign=bottom align=right height=35 colspan=5>

        <IMG SRC="images/ic_user.gif" height=18 width=45 border=0>
        <font size="-1" face="Arial, Verdana" color="#333333">
            <c:out value='${sessionScope.currentUser.username}'/>
        </font>
        &nbsp;
    </td>
	</tr>
    </table>

    </c:when>
    <c:otherwise>

	<table cellpadding=0 cellspacing=0 border=0 width="100%">
	<tr>
	<td valign=middle align=center height=2 width=70 bgcolor="CC0000"><IMG SRC="images/clear.gif" height=2 width=1 border=0></td>
	<td valign=middle align=center height=2 width=1><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
	<td valign=middle align=center height=2 width=90 bgcolor="CC0000"><IMG SRC="images/clear.gif" height=2 width=1 border=0></td>
	<td valign=middle align=center height=2 width=1><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
	<td valign=middle align=center height=2 width=88 bgcolor="CC0000"><IMG SRC="images/clear.gif" height=2 width=1 border=0></td>
	<td valign=middle align=center height=2 width=1><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
	<td valign=middle align=center height=2 width=88 bgcolor="CC0000"><IMG SRC="images/clear.gif" height=2 width=1 border=0></td>
	<td valign=middle align=center height=2 width=1><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
	<td valign=middle align=center height=2 width=88 bgcolor="CC0000"><IMG SRC="images/clear.gif" height=2 width=1 border=0></td>
	<td valign=middle align=center height=2 width=1><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
	<td valign=middle align=center height=2 bgcolor="000000"><IMG SRC="images/clear.gif" height=2 width=1 border=0></td>
	</tr>
	<tr>
	<td valign=middle align=center height=1 colspan=13><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
	</tr>
	<tr>
	<td valign=bottom align=left height=35 width=70 bgcolor="#DADADA">&nbsp;<a href="index.jsp" class="topmenu">Home</a></td>
	<td valign=middle align=center height=2 width=1><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
	<td valign=bottom align=left height=2 width=90 bgcolor="#DADADA">&nbsp;<a href="profile.jsp" class="topmenu01">Edit Profile</a></td>
	<td valign=middle align=center height=2 width=1><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
	<td valign=bottom align=left height=2 width=90 bgcolor="#DADADA">&nbsp;<a href="subscription.jsp" class="topmenu01">Subscriptions</a></td>
	<td valign=middle align=center height=2 width=1><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
	<td valign=bottom align=left height=2 width=88 bgcolor="#DADADA">&nbsp;<a href="eventList.jsp" class="topmenu01">Events</a></td>
	<td valign=middle align=center height=2 width=1><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
	<td valign=bottom align=left height=2 width=88 bgcolor="#DADADA">&nbsp;<a href="forumList.jsp" class="topmenu01">Forums</a></td>
	<td valign=middle align=center height=2 width=1><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
	<td valign=bottom align=left height=2 width=88 bgcolor="#DADADA">&nbsp;<a href="logout.jsp" class="topmenu01">Logout</a></td>
	<td valign=middle align=center height=2 width=1><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
	<td valign=bottom align=left height=2 bgcolor="#515151"><IMG SRC="images/clear.gif" height=2 width=1 border=0></td>
	</tr>
	<tr>
	<td valign=bottom align=right height=35 colspan=13>

        <x:template type="TemplateDisplayFrontEndEditSelector" />

        <IMG SRC="images/ic_user.gif" height=18 width=45 border=0>
        <font size="-1" face="Arial, Verdana" color="#333333">
            <c:out value='${sessionScope.currentUser.username}'/>
            (<c:out value='${sessionScope.currentUser.propertyMap.firstName}'/>
            <c:out value='${sessionScope.currentUser.propertyMap.lastName}'/>)
        </font>
        &nbsp;
    </td>
	</tr>
	</table>

    </c:otherwise>
    </c:choose>
	<!---- close top menu --->

</td>
</tr>
<tr>
<td valign=top align=left width=1 height=1 colspan=3><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
</tr>
</table>

<table cellpadding=0 cellspacing=0 border=0 width="780">
<tr>
<td valign=top align=left width=10 bgcolor="000000"><IMG SRC="images/site_p_orange.gif" height=85 width=10 border=0></td>
<td valign=top align=left width=1><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
<td valign=middle align=left width=184 bgcolor="#EEF1EB">

	<!---- search --->
	<table cellpadding=0 cellspacing=0 border=0>
    <form action="search.jsp" method="get">
	<tr>
	<td valign=middle align=left width=180><IMG SRC="images/hd_search.gif" height=16 width=63 border=0></td>
	</tr>
	<tr>
	<td valign=middle align=center width=180>
        <input type="text" name="query" value="" size="16"><input type="image" src="images/ic_go.gif" height=16 width=28 border=0 style="cursor:hand">
    </td>
	</tr>
    </form>
	</table>
	<!---- close search --->


</td>
<td valign=top align=left width=10 bgcolor="000000"><IMG SRC="images/clear.gif" height=85 width=10 border=0></td>
<td valign=top align=left width=1><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
<td valign=top align=right bgcolor="2B2B2C"> <IMG SRC="images/top_panel.gif" height=85 width=540 border=0></td>
</tr>
<tr>
<td valign=top align=left width=1 height=1 colspan=6><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
</tr>
</table>

<table cellpadding=0 cellspacing=0 border=0 width="780">
<tr>
<td valign=top align=left width=10><IMG SRC="images/clear.gif" height=1 width=10 border=0></td>
<td valign=top align=left width=1><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
<td valign=middle align=left width=194 height=24 bgcolor="D7D7D7"><IMG SRC="images/arrow_down.gif" height=15 width=18 border=0><font size=-2 face=Arial, Verdana color="333333">MAIN MENU</font></td>
<td valign=top align=left width=1><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
<td valign=top align=left height=24 bgcolor="#D7D7D7">

	<!---- date --->
	<table cellpadding=0 cellspacing=0 border=0 width="100%">
	<tr bgcolor="CC0000">
	<td valign=middle align=left height=2><IMG SRC="images/clear.gif" height=2 width=1 border=0></td>
	</tr>
	<tr>
	<td valign=middle align=right height=22>
        <c-rt:set var="currentDate" value="<%= new java.util.Date() %>"/>
        <span class="datefont"><fmt:formatDate pattern="${globalDateLong}" value="${currentDate}"/></span> &nbsp;
    </td>
	</tr>
	</table>
	<!---- close date --->

</td>
</tr>
</table>
<!--- close Header --->

<!--- Middle : Left section & Content --->
<table cellpadding=0 cellspacing=0 border=0 width="780">
<tr>
<td valign=top align=left width=200>

	<!--- Section/Menu --->
	<table cellpadding=0 cellspacing=0 border=0>
	<tr>
	<td valign=top align=left width=10 bgcolor="F2D85B"><IMG SRC="images/clear.gif" height=10 width=10 border=0></td>
	<td valign=top align=left width=1><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
	<td valign=middle align=left width=185>

        <jsp:include page="displayPopupMenu.jsp" flush="true" />

	</td>
	</tr>
	</table>
	<!--- Close Section/Menu --->

	<!--- Spot A --->
	<table cellpadding=0 cellspacing=0 border=0>
	<tr>
	<td valign=bottom align=left width=9><IMG SRC="images/clear.gif" height=1 width=9 border=0></td>
	<td valign=bottom align=left width=191 height=10 colspan=2><IMG SRC="images/hor_dotline.gif" height=5 width=191 border=0></td>
	</tr>
	<tr>
	<td valign=top align=left width=200 height=10 colspan=3><IMG SRC="images/clear.gif" height=10 width=1 border=0></td>
	</tr>
	<tr>
	<td valign=top align=left width=9><IMG SRC="images/clear.gif" height=1 width=9 border=0></td>
	<td valign=top align=left width=16><IMG SRC="images/arrow_site.gif" height=15 width=16 border=0></td>
	<td valign=top align=left width=175>
	<x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_SpotA" />
	</td>
	</tr>
	<tr>
	<td valign=bottom align=left width=9><IMG SRC="images/clear.gif" height=1 width=9 border=0></td>
	<td valign=bottom align=left width=191 height=10 colspan=2><IMG SRC="images/hor_dotline.gif" height=5 width=191 border=0></td>
	</tr>
	</table>
	<!--- Close Spot A --->

	<!--- Quick Vote --->
	<table cellpadding=0 cellspacing=0 border=0>
	<tr>
	<td valign=top align=left width=200 height=10 colspan=3><IMG SRC="images/clear.gif" height=10 width=1 border=0></td>
	</tr>
	<tr>
	<td valign=top align=left width=9><IMG SRC="images/clear.gif" height=1 width=9 border=0></td>
	<td valign=top align=left width=16><IMG SRC="images/arrow_site.gif" height=15 width=16 border=0></td>
	<td valign=top align=left width=175>
	<strong>Quick Vote<strong>
    <br>

    <x:template name="contentVote" type="TemplateDisplayVote" properties="sectionId=${param.id}" />
    <c:if test="${empty contentVote.question}">
        <x:template type="TemplateDisplayVote" properties="sectionId=com.tms.cms.section.Section_Sections" />
    </c:if>

	</td>
	</tr>
	<tr>
	<td valign=bottom align=left width=9><IMG SRC="images/clear.gif" height=1 width=9 border=0></td>
	<td valign=bottom align=left width=191 height=10 colspan=2><IMG SRC="images/hor_dotline.gif" height=5 width=191 border=0></td>
	</tr>
	</table>
	<!--- Close Quick Vote --->

	<!--- Spot B --->
	<table cellpadding=0 cellspacing=0 border=0>
	<tr>
	<td valign=top align=left width=200 height=10 colspan=3><IMG SRC="images/clear.gif" height=10 width=1 border=0></td>
	</tr>
	<tr>
	<td valign=top align=left width=9><IMG SRC="images/clear.gif" height=1 width=9 border=0></td>
	<td valign=top align=left width=16><IMG SRC="images/arrow_site.gif" height=15 width=16 border=0></td>
	<td valign=top align=left width=175>
	<x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_SpotB" />
	</td>
	</tr>
	<tr>
	<td valign=bottom align=left width=9><IMG SRC="images/clear.gif" height=1 width=9 border=0></td>
	<td valign=bottom align=left width=191 height=10 colspan=2><IMG SRC="images/hor_dotline.gif" height=5 width=191 border=0></td>
	</tr>
	</table>
	<!--- Close Spot B --->

</td>
<td valign=top align=left width=14><IMG SRC="images/clear.gif" height=1 width=14 border=0></td>
<td valign=top align=left>

<br>
