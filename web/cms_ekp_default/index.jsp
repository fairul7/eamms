<%@ include file="/common/header.jsp" %>

<jsp:include page="includes/header.jsp" flush="true" />


	<!--- 2Columns --->
	<table cellpadding=0 cellspacing=0 border=0 width="100%">
	<tr>
	<td valign=top align=left colspan=3><IMG SRC="images/clear.gif" height=6 width=1 border=0></td>
	</tr>
	<tr>
	<td valign=top align=left width=219>

		<!--- Blue Panel 01 --->
		<table cellpadding=0 cellspacing=0 border=0>
		<tr bgcolor="#358dcb">
		<td valign=top align=left width=219 colspan=3><IMG SRC="images/tab_blue01.gif" height=8 width=219 border=0></td>
		</tr>
		<tr bgcolor="#358dcb">
		<td valign=top align=right width=27><IMG SRC="images/arrow_dwn01.gif" height=16 width=25 border=0></td>
		<td valign=top align=left>

            <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_HomeSpot" />

		</td>
		<td valign=top align=right width=10><IMG SRC="images/clear.gif" height=1 width=10 border=0></td>
		</tr>
		<tr bgcolor="358DCB">
		<td valign=top align=left width=219 colspan=3><IMG SRC="images/clear.gif" height=10 width=1 border=0></td>
		</tr>
		<tr>
		<td valign=top align=left width=219 colspan=3><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
		</tr>
		</table>
		<!--- Close Blue Panel 01 --->


        <c:if test="${!empty currentUser && currentUser.id != 'anonymous'}">
		<!--- Blue Panel 02 --->
		<table cellpadding=0 cellspacing=0 border=0>
		<tr bgcolor="ABD6FF">
		<td valign=top align=left width=219 colspan=3><IMG SRC="images/tab_blue02.gif" height=8 width=219 border=0></td>
		</tr>
		<tr bgcolor="ABD6FF">
		<td valign=top align=right width=27><IMG SRC="images/arrow_dwn02.gif" height=17 width=26 border=0></td>
		<td valign=top align=left width="182"><IMG SRC="images/hd_genevent.gif" height=17 width=99 border=0><br>

            <x:template name="widget" type="TemplateDisplayEventList" body="custom">
                <table cellpadding=2 cellspacing=1 border=0 width="90%">
                <c:forEach items="${widget.eventList}" var="event">
                    <tr>
                    <td valign=top align=left>
                        <img src="images/arrow_02.gif" height=10 width=6 border=0></td>
                    <td valign=top align=left>
                        <a href="event.jsp?id=<c:out value="${event.eventId}"/>" class="featurelink2"><c:out value="${event.title}"/></a><br>
                    <span class="tsBody3"><fmt:formatDate pattern="${globalDateLong}" value="${event.startDate}" /> - <fmt:formatDate pattern="${globalDateLong}" value="${event.endDate}" /></span></td>
                    </tr>
                </c:forEach>
                <tr>
                <td valign=top align=right colspan=2><IMG SRC="images/square01.gif" height=9 width=8 border=0><a href="eventList.jsp" class="smalllink3">More Events</a></td>
                </tr>
                </table>
            </x:template>

		</td>
		<td valign=top align=right width=10><IMG SRC="images/clear.gif" height=1 width=10 border=0></td>
		</tr>
		<tr bgcolor="ABD6FF">
		<td valign=top align=left width=219 colspan=3><IMG SRC="images/clear.gif" height=10 width=1 border=0></td>
		</tr>
		<tr>
		<td valign=top align=left width=219 colspan=3><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
		</tr>
		</table>
		<!--- Close Blue Panel 02 --->
        </c:if>

		<!--- Blue Panel 03 --->
		<table cellpadding=0 cellspacing=0 border=0>
		<tr bgcolor="DBEEFF">
		<td valign=top align=left width=219 colspan=3><IMG SRC="images/tab_blue03.gif" height=8 width=219 border=0></td>
		</tr>
		<tr bgcolor="DBEEFF">
		<td valign=top align=right width=27><IMG SRC="images/arrow_dwn03.gif" height=18 width=27 border=0></td>
		<td valign=top align=left>

            <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_HighlightsSpot" />

		</td>
		<td valign=top align=right width=10><IMG SRC="images/clear.gif" height=1 width=10 border=0></td>
		</tr>
		<tr bgcolor="DBEEFF">
		<td valign=top align=left width=219 colspan=3><IMG SRC="images/clear.gif" height=10 width=1 border=0></td>
		</tr>
		<tr>
		<td valign=top align=left width=219 colspan=3><IMG SRC="images/clear.gif" height=1 width=1 border=0></td>
		</tr>
		</table>
		<!--- Close Blue Panel 03 --->


	</td>
	<td valign=top align=left width=16><IMG SRC="images/clear.gif" height=1 width=16 border=0></td>
	<td valign=top align=left>

		<!--- In The News --->
		<table cellpadding=1 cellspacing=2 border=0 width=100%>
		<tr>
		<td valign=top align=left colspan=2>
            <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_FeaturedContentSpot" />
        </td>
		</tr>

        <x:template name="featuredContent" type="TemplateDisplayVSection" properties="id=com.tms.cms.vsection.VSection_FeaturedContent" body="custom">
            <tr>
            <td valign=top align=left colspan=2><strong><c:out value="${featuredContent.contentObject.name}"/></strong></td>
            </tr>
            <tr>
            <td valign=top align=left colspan="2"><x:template type="TemplateDisplayFrontEndEdit" properties="id=${featuredContent.id}" /></td>
            </tr>
            <c:forEach var="content" items="${featuredContent.contentObjectList}">
                <tr>
                <td valign=top align=left><img src="images/arrow_text.gif" height=13 width=20 border=0></td>
                <td valign=top align=left><a href="content.jsp?id=<c:out value='${content.id}'/>" class="newslink"><c:out value='${content.name}'/></a></td>
                </tr>
            </c:forEach>
        </x:template>

		</table>
		<!--- Close In The News --->

		<br>
		<!--- Latest --->
		<table cellpadding=0 cellspacing=0 border=0 width=100%>
		<tr>
		<td valign=top align=left colspan=2>
        &nbsp;
        <IMG SRC="images/hor_dotline.gif" height=5 width="90%" border=0>
        <br>
        <strong>Latest Articles and Documents</strong>
        </td>
		</tr>
		<tr>
		<td valign=top align=left>
            &nbsp;
        </td>
		</tr>
		<tr bgcolor="DF9A40">
		<td valign=top align=left><IMG SRC="images/clear.gif" height=6 width=1 border=0></td>
		</tr>
		<tr bgcolor="DF9A40">
		<td valign=top align=center>

        <x:template name="latestContent" type="TemplateDisplayLatestContent" properties="types=com.tms.cms.article.Article,com.tms.cms.document.Document&pageSize=10&hideSummary=true" body="custom">
			<table cellpadding=2 cellspacing=1 border=0 width="95%">
			<tr bgcolor="926428">
			<td valign=top align=left><font size=-2 face=arial color=ffffff>Title</font></td>
			<td valign=top align=left><font size=-2 face=arial color=ffffff>Date</font></td>
			<td valign=top align=left width="20%"><font size=-2 face=arial color=ffffff>Author</font></td>
			</tr>

            <c:forEach items="${latestContent.children}" var="co">
                <tr bgcolor="F4CC98">
                <td valign=top align=left><a href="content.jsp?id=<c:out value="${co.id}"/>" class="tablefontLink"><c:out value="${co.name}"/></a></td>
                <td valign=top align=left><span class="tsBody2"><fmt:formatDate pattern="${globalDateLong}" value="${co.date}" /></span></td>
                <td valign=top align=left width="20%"><span class="tsBody2"><c:out value="${co.author}"/></span></td>
                </tr>
            </c:forEach>
			</table>
        </x:template>

		</td>
		</tr>
		<tr bgcolor="DF9A40">
		<td valign=top align=left><IMG SRC="images/clear.gif" height=6 width=1 border=0></td>
		</tr>
		</table>
		<!--- Close Documents --->

	</td>
	</tr>
	</table>
	<!--- Close 2Columns --->


	<!--- dotline --->
	<table cellpadding=0 cellspacing=0 border=0 width=100%>
	<tr>
	<td valign=top align=left colspan=3><img src="images/clear.gif" height=10 width=1 border=0></td>
	</tr>
	<tr>
	<td valign=top align=left colspan=3 background="images/bg_dotline.gif"><img src="images/clear.gif" height=7 width=1 border=0></td>
	</tr>
	<tr>
	<td valign=top align=left colspan=3><img src="images/clear.gif" height=5 width=1 border=0></td>
	</tr>
	</table>
	<!--- Close dotlines --->


        <%--
	<!--- Forum --->
	<table cellpadding=1 cellspacing=0 border=0 width="100%">
	<tr>
	<td valign=top align=left colspan=2>
        <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_ForumSpot" />

        <x:template type="TemplateDisplayForumList" />
        </td>
	</tr>
	</table>
	<!--- Close forum --->
	--%>

<jsp:include page="includes/footer.jsp" flush="true" />
