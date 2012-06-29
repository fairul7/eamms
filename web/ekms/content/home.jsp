<%@ include file="/common/header.jsp" %>
<x:template type="TemplateProcessVote" />
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true" />

<x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_HomeSpot" />

<table width="100%" border="0" cellspacing="3" cellpadding="0">
    <tr valign="top">
        <td>
        <x:template name="featuredContent" type="TemplateDisplayVSection" properties="id=com.tms.cms.vsection.VSection_FeaturedContent" body="custom">
        <c:if test="${!empty featuredContent.contentObject}">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr valign="middle">
                <td height="22" bgcolor="003366" class="contentTitleFont" nowrap>
                    <b><font color="#FFCF63" class="contentTitleFont">&nbsp;<c:out value="${featuredContent.contentObject.name}"/></font></b>
                </td>
                <td align="right" bgcolor="003366" class="contentTitleFont"></td>
            </tr>
            <tr>
                <td colspan="2" valign="TOP" bgcolor="EFEFEF" class="contentBgColor">
                    <table cellpadding="10" cellspacing="0" width="100%">
                        <tr>
                            <td bgcolor="EFEFEF" class="contentBgColor">
                                    <x:template type="TemplateDisplayFrontEndEdit" properties="id=${featuredContent.id}" />
                                    <c:forEach var="content" items="${featuredContent.contentObjectList}">
                                        <p>
                                        <a href="content.jsp?id=<c:out value='${content.id}'/>" class="featureheadline"><c:out value='${content.name}'/></a><br>
                                        <span class="abstract">
                                        <c:out value='${content.summary}' escapeXml="false" />
                                        </span>
                                    </c:forEach>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        </c:if>
        </x:template>
        </td>
    </tr>
</table>

<jsp:include page="includes/footer.jsp" flush="true" />
<%@ include file="/ekms/includes/footer.jsp" %>
