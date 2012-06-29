<%@ include file="/common/header.jsp" %>
                    </td>
                    <td valign=top align=left width=14><IMG SRC="<c:url value='/ekms/images/blank.gif'/>" height=5 width=14 border=0></td>
                    <td valign=top align=left width="250" valign="top">
                        <table>
                        <form action="search.jsp" method="POST">
                        <tr><td align="center" nowrap>
                        <input type="text" name="query" value="" size="25">
                        <input type="submit" class="button" value="<fmt:message key="cms.label.search"/>">
                        </td></tr>
                        </form>
                        </table>

                        <!-- Display content-specific spot -->
                        <c:if test="${!empty param.id}">
                        <x:template name="spot" type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_${param.id}" body="custom" />
                            <c:if test="${!empty spot.contentObject.id}">
                                <table cellpadding=1 cellspacing=0 border=0 width=100% bgcolor="000000">
                                    <tr>
                                        <td valign=top align=center width=100%>
                                            <table cellspacing=0 cellpadding=4 width="100%" border=0 class="contentBody">
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
<%--
// 2004-04-20: removed by KC, demanded by Jack
                        <table cellpadding=1 cellspacing=0 border=0 width=100% class="contentHeader">
                            <tr>
                                <td valign=top align=center width=100%>
                                    <table cellSpacing=0 cellPadding=4 width="100%" border=0 class="contentBody">
                                        <tr>
                                            <td valign=top align=left class="contentBody">
                                                <x:cache scope="application" duration="10" key="featuredContentSpot">
                                                    <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_FeaturedContentSpot" />
                                                </x:cache>
                                            </td>
                                        </tr>
                                    </table>
                                    <table cellSpacing=0 cellPadding=4 width="100%" border=0 class="contentBody">
                                        <tr>
                                            <td valign=top align=left class="contentBody">
                                                <x:cache scope="application" duration="10" key="SpotA">
                                                    <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_SpotA" />
                                                </x:cache>
                                            </td>
                                        </tr>
                                    </table>
                                    <table cellSpacing=0 cellPadding=4 width="100%" border=0 class="contentBody">
                                        <tr>
                                            <td valign=top align=left class="contentBody">
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
--%>
                        <!-- Latest Articles -->
<table width="100%" border="0" cellspacing="3" cellpadding="0">
    <tr valign="top">
        <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr valign="middle">
                <td height="22" bgcolor="003366" class="contentTitleFont" nowrap>
                    <b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key='cms.label.latestArticles'/></font></b>
                </td>
                <td align="right" bgcolor="003366" class="contentTitleFont"></td>
            </tr>
            <tr>
                <td colspan="2" valign="TOP" bgcolor="EFEFEF" class="contentBgColor">
                    <table cellpadding="3" cellspacing="0" width="100%">
                        <tr><td bgcolor="EFEFEF" class="contentBgColor"><img src="images/blank.gif" width="5" height="7"></td></tr>
                        <tr>
                            <td bgcolor="EFEFEF" class="contentBgColor">
                                <x:template name="articles" type="TemplateDisplayLatestContent" properties="types=com.tms.cms.article.Article&pageSize=10&hideSummary=true&page=1&pageSize=10" body="custom">
                                    <table width="100%">
                                    <c:forEach var="co" items="${articles.children}">
                                        <tr>
                                            <td valign="top" width="10"><fmt:message key="cms.label.icon_${co.className}"/></td>
                                            <td valign="top">
                                                <a href="content.jsp?id=<c:out value="${co.id}"/>" class="tablefontLink"><c:out value="${co.name}"/></a>
                                                <br>
                                                <c:out value="${co.author}"/> <fmt:formatDate pattern="${globalDateLong}" value="${co.date}" />
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </table>
                                </x:template>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        </td>
    </tr>
</table>
<br>
                        <!-- Latest Documents -->
<table width="100%" border="0" cellspacing="3" cellpadding="0">
    <tr valign="top">
        <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr valign="middle">
                <td height="22" bgcolor="003366" class="contentTitleFont" nowrap>
                    <b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key='cms.label.latestDocuments'/></font></b>
                </td>
                <td align="right" bgcolor="003366" class="contentTitleFont"></td>
            </tr>
            <tr>
                <td colspan="2" valign="TOP" bgcolor="EFEFEF" class="contentBgColor">
                    <table cellpadding="3" cellspacing="0" width="100%">
                        <tr><td bgcolor="EFEFEF" class="contentBgColor"><img src="images/blank.gif" width="5" height="7"></td></tr>
                        <tr>
                            <td bgcolor="EFEFEF" class="contentBgColor">
                                <x:template name="documents" type="TemplateDisplayLatestContent" properties="types=com.tms.cms.document.Document&pageSize=10&hideSummary=true&page=1&pageSize=10" body="custom">
                                    <table width="100%">
                                    <c:forEach var="co" items="${documents.children}">
                                        <tr>
                                            <td valign="top" width="10"><fmt:message key="cms.label.icon_${co.className}"/></td>
                                            <td valign="top">
                                                <a href="content.jsp?id=<c:out value="${co.id}"/>" class="tablefontLink"><c:out value="${co.name}"/></a>
                                                <br>
                                                <c:out value="${co.author}"/> <fmt:formatDate pattern="${globalDateLong}" value="${co.date}" />
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </table>
                                </x:template>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        </td>
    </tr>
</table>
<br>

                        <!-- Vote -->
<table width="100%" border="0" cellspacing="3" cellpadding="0">
    <tr valign="top">
        <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr valign="middle">
                <td height="22" bgcolor="003366" class="contentTitleFont" nowrap>
                    <b><font color="#FFCF63" class="contentTitleFont">&nbsp;<x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_VoteSpot" /></font></b>
                </td>
                <td align="right" bgcolor="003366" class="contentTitleFont"></td>
            </tr>
            <tr>
                <td colspan="2" valign="TOP" bgcolor="EFEFEF" class="contentBgColor">
                    <table cellpadding="3" cellspacing="0" width="100%">
                        <tr><td bgcolor="EFEFEF" class="contentBgColor"><img src="images/blank.gif" width="5" height="7"></td></tr>
                        <tr>
                            <td bgcolor="EFEFEF" class="contentBgColor">
                                    <x:template name="contentVote" type="TemplateDisplayVote" properties="sectionId=${param.id}" />
                                    <c:if test="${empty contentVote.question}">
                                        <x:template type="TemplateDisplayVote" properties="sectionId=com.tms.cms.section.Section_Sections" />
                                    </c:if>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        </td>
    </tr>
</table>
<br>


        </td>
    </tr>
</table>


        </td>
    </tr>
</table>