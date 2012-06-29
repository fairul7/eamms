<%@ page import="java.net.URLEncoder"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="co" scope="request" value="${widget.contentObject}"/>
<table cellpadding="3" cellspacing="0" width="100%">
    <c:if test="${!widget.noHeader}">
        <tr><td class="contentHeader"><span class="contentName"><c:out value="${co.parent.name}"/></span></td></tr>
        <tr><td class="contentBody">&nbsp;</td></tr>
    </c:if>
<%--    <tr><td class="contentBody"><span class="contentName"><c:out value="${co.name}"/></span></td></tr>--%>
    <tr><td class="contentBody"><span class="contentDate"><fmt:formatDate pattern="${globalDatetimeLong}" value="${co.date}"/></span></td></tr>
    <tr><td class="contentBody">
    <c:if test="${!empty co.author}">
    <span class="contentAuthor"><fmt:message key="document.label.author"/> <c:out value="${co.author}"/></span>
    </c:if>
    </td></tr>
    <tr><td class="contentBody">&nbsp;</td></tr>
    <tr><td class="contentBody"><c:out value="${co.summary}" escapeXml="false" /></td></tr>
    <tr><td class="contentBody">&nbsp;</td></tr>
    <c:if test="${!empty co.fileName}">
        <tr>
            <td class="contentBody">
                <hr size="1">
                    
    <c:set var="filename" value="${co.fileName}"/>
      
    <% 
        //encode the filename
        String encodeFilename = URLEncoder.encode((String)pageContext.getAttribute("filename"), "UTF-8");
        //base on the encoding, space will be replace by +, but IE dun like it, so have to replace all + with %20
        encodeFilename = encodeFilename.replaceAll("\\+", "%20");
    %>

                <p>
                    <a class="contentOptionLink" href="<%= request.getContextPath() %>/cms/documentstorage/<c:out value="${co.id}"/>/<%= encodeFilename %>" target="_blank"><c:out value="${co.fileName}"/></a>
                    <span class="contentOption">
                        <br><c:out value="${co.fileSize}"/> <fmt:message key='general.label.bytes'/>
                        <br><c:out value="${co.contentType}"/>
                    </span>
                </p>
            </td>
        </tr>
    </c:if>
    <tr><td class="contentBody">&nbsp;</td></tr>
    <tr><td class="contentBody"><x:template type="TemplateDisplayContentProfile" properties="id=${co.id}&version=${co.version}" /></td></tr>
    <tr><td class="contentBody"><jsp:include page="displayContentCommentary.jsp" flush="true" /></td></tr>
    <tr><td class="contentBody">&nbsp;</td></tr>
</table>
