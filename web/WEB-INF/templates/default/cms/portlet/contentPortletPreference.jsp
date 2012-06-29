<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="crt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${widget}"/>
<link ref="stylesheet" href="images/style.css">
<table cellpadding="3" cellspacing="1" width="100%" class="articleBackground">
    <jsp:include page="../../form_header.jsp" flush="true"/>
    <tr><td class="articleHeader" colspan="2">Article Personalization</td></tr>
    <tr><td class="articleRow" colspan="2">&nbsp;</td></tr>
    <c:choose>
        <c:when test="${!(empty widget.entity)}">
            <tr>
                <td class="articleRowLabel" valign="top" align="right" nowrap>Sections&nbsp;</td>
                <td class="articleRow"><x:display name="${widget.sections.absoluteName}"/></td>
            </tr>
            <tr>
                <td class="articleRowLabel" valign="top" align="right" nowrap>Listing&nbsp;</td>
                <td class="articleRow"><x:display name="${widget.listing.absoluteName}"/></td>
            </tr>
            <tr>
                <td class="articleRowLabel" valign="top" align="right" nowrap>Content Types&nbsp;</td>
                <td class="articleRow">
                    <x:display name="${widget.articles.absoluteName}"/><br>
                    <x:display name="${widget.documents.absoluteName}"/>
                </td>
            </tr>
            <tr>
                <td class="articleRow">&nbsp;</td>
                <td class="articleRow">
                    <x:display name="${widget.update.absoluteName}"/>
                    <x:display name="${widget.cancel.absoluteName}"/>
                </td>
            </tr>
            <tr><td class="articleLabel" colspan="2">&nbsp;</td></tr>
        </c:when>
        <c:otherwise>
            <tr><td class="articleLabel" colspan="2">Entity ID Not Specified</td></tr>
            <tr><td class="articleRow" colspan="2">&nbsp;</td></tr>
        </c:otherwise>
    </c:choose>
    <jsp:include page="../../form_footer.jsp" flush="true"/>
</table>
