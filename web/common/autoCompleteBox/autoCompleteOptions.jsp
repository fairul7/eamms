<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ include file="/common/header.jsp" %>
<c:set var="cn" value="${param.widget}"/>
<c:set var="textbox" value="${widgets[cn]}"/>
<c:choose>
<c:when test="${!empty textbox}">
<c:set var="optionList" value=""/>
<c:forEach var="option" items="${textbox.options}" varStatus="stat" >
    <%
        String option = (String)pageContext.getAttribute("option");
        option = StringUtils.replace(option, "'", "\\'");
        option = StringUtils.replace(option, "\n", "");
        option = StringUtils.replace(option, "\r", "");
        pageContext.setAttribute("option", option);
        pageContext.setAttribute("newline", System.getProperty("line.separator"));
    %>
    <c:if test="${stat.index>0}"><c:set var="optionList" value="${optionList},${newline}"/></c:if>
    <c:set var="optionList"><c:out value="${optionList}" escapeXml="false" />'<c:out value="${option}" escapeXml="false"/>'</c:set>
</c:forEach>
var <c:out value="${textbox.optionsVarName}"/>=new Array(<c:out value="${optionList}" escapeXml="false" />);
<c:out value=""/>
</c:when>
<c:otherwise>
var <c:out value="${textbox.optionsVarName}"/>=new Array();
</c:otherwise>
</c:choose>

