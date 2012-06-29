<%@ include file="/common/header.jsp"  %>

	<br>
	</td>
	<td valign=top align=left width=14><IMG SRC="images/clear.gif" height=5 width=14 border=0></td>

<%
	boolean big=false;
	String id = request.getParameter("id");
      if(id != null && (id.startsWith("com.tms.cms.article") || id.startsWith("com.tms.cms.page"))) {
		big=true;
	}
	pageContext.setAttribute("big", new Boolean(big));
%>
	<td valign=top align=left 
		<c:if test="${!big}">width="50%"</c:if>
		<c:if test="${big}">width="100%"</c:if>
      >

        <!-- Display content-specific spot -->
        <c:if test="${!empty param.id}">
            <x:template name="spot" type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_${param.id}" body="custom" />

            <c:if test="${!empty spot.contentObject.id}">
                <table cellpadding=1 cellspacing=0 border=0 width=100% bgcolor="000000">
                <tr>
                <td valign=top align=center width=100%>

                    <table cellSpacing=0 cellPadding=4 width="100%" bgColor=#e5e5e5 border=0>
                    <tr>
                    <td valign=top align=left>
                            <c:set var="co" value="${spot.contentObject}"/>
                            <x:template type="TemplateDisplayFrontEndEdit" properties="id=${co.id}" />
                            <c:out value="${co.summary}" escapeXml="false" />
                    </td>
                    </tr>
                    </table>

                </td>
                </tr>
                </table>
                <br>
            </c:if>
        </c:if>

		<table cellpadding=1 cellspacing=0 border=0 width=100% bgcolor="000000">
		<tr>
		<td valign=top align=center width=100%>

            <table cellSpacing=0 cellPadding=4 width="100%" bgColor=#e5e5e5 border=0>
            <tr>
            <td valign=top align=left>
                <x:cache scope="application" duration="10" key="featuredContentSpot">
                    <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_FeaturedContentSpot" />
                </x:cache>
            </td>
			</tr>
			</table>

            <table cellSpacing=0 cellPadding=4 width="100%" bgColor=#e5e5e5 border=0>
            <tr>
            <td valign=top align=left>
                <x:cache scope="application" duration="10" key="SpotA">
                    <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_SpotA" />
                </x:cache>
            </td>
			</tr>
			</table>

            <table cellSpacing=0 cellPadding=4 width="100%" bgColor=#e5e5e5 border=0>
            <tr>
            <td valign=top align=left>
                <x:cache scope="application" duration="10" key="SpotB">
                    <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_SpotB" />
                </x:cache>
            </td>
			</tr>
			</table>

		</td>
		</tr>
		</table>

        <br>

        <!-- Vote -->
		<table cellpadding=1 cellspacing=0 border=0 width=100%>
		<tr>
		<td valign=top>
            <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_VoteSpot" />
        </td>
        </tr>
        <tr>
			<td valign=top align=center width=164 bgcolor="ffffff"><IMG SRC="images/dotline_164.gif" height=7 width=164 border=0></td>
        </tr>
        <tr>
		<td valign=top>
            <x:template name="contentVote" type="TemplateDisplayVote" properties="sectionId=${param.id}" />
            <c:if test="${empty contentVote.question}">
                <x:template type="TemplateDisplayVote" properties="sectionId=com.tms.cms.section.Section_Sections" />
            </c:if>
		</td>
		</tr>
		</table>

		<br>

        <!-- Forums -->
		<table cellpadding=1 cellspacing=0 border=0 width=100%>
		<tr>
		<td valign=top>
            <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_ForumSpot" />
        </td>
        </tr>
        <tr>
			<td valign=top align=center width=164 bgcolor="ffffff"><IMG SRC="images/dotline_164.gif" height=7 width=164 border=0></td>
        </tr>
        <tr>
		<td valign=top>
            <x:template name="list" type="TemplateDisplayForumList" body="custom">
                <c:forEach items="${list.forums}" var="forum">
                    <c:if test="${forum.active}">
                        <a href="forumTopicList.jsp?forumId=<c:out value="${forum.forumId}" />">
                            <FONT SIZE="1" FACE="Verdana, Arial, Helvetica" COLOR="000000" CLASS="tableContentFont"><c:out value="${forum.name}" /></FONT></a>
                        <br>
                    </c:if>
                </c:forEach>
            </x:template>
		</td>
		</tr>
		</table>

        <p>
    </td>
    </tr>
    <tr>
    <td colspan="3">

		<table cellpadding=0 cellspacing=0 border=0 width=100%>
            <tr>
                <td valign="top" align="bottom" width="100%" height="3" background="images/dotline_770.gif"><img src="tmsasia.com_files/clear.gif" height="3" width="1" border="0"></td>
            </tr>
			<tr>
			<td valign=top align=left width=100%>
                <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_SubfooterSpot" />
            </td>
			</tr>
		</table>




	</td>
	</tr>
</table>

<table cellpadding=0 cellspacing=0 border=0 width=96%>
    <tr>
    <td valign=top align=left width=100% height=3 background="images/dotline_770.gif"><IMG SRC="images/clear.gif" height=1 width=770 border=0></td>
    </tr>
    <tr>
    <td valign=bottom align=right width=100%>

    <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_FooterSpot" />

    </td>
    </tr>
</table>

<pre>

</pre>
</body>
</HTML>



