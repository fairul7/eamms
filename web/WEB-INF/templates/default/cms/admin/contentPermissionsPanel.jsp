<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="co" scope="request" value="${widget}"/>

<table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td style="vertical-align: top; font-weight:bold"><fmt:message key='general.label.managers'/><br>
      </td>
      <td style="vertical-align: top;">
        <c:forEach items="${co.managerList}" var="principal" varStatus="status">
            <c:if test="${!status.first}">, </c:if>
            <c:out value="${principal.name}"/>
            <c:if test="${principal.class.name == 'kacang.services.security.Group'}">[G]</c:if>
        </c:forEach>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top; font-weight:bold"><fmt:message key='general.label.editors'/><br>
      </td>
      <td style="vertical-align: top;">
        <c:forEach items="${co.editorList}" var="principal" varStatus="status">
            <c:if test="${!status.first}">, </c:if>
            <c:out value="${principal.name}"/>
            <c:if test="${principal.class.name == 'kacang.services.security.Group'}">[G]</c:if>
        </c:forEach>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top; font-weight:bold"><fmt:message key='general.label.authors'/><br>
      </td>
      <td style="vertical-align: top;">
        <c:forEach items="${co.authorList}" var="principal" varStatus="status">
            <c:if test="${!status.first}">, </c:if>
            <c:out value="${principal.name}"/>
            <c:if test="${principal.class.name == 'kacang.services.security.Group'}">[G]</c:if>
        </c:forEach>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top; font-weight:bold"><fmt:message key='general.label.readers'/><br>
      </td>
      <td style="vertical-align: top;">
        <c:forEach items="${co.readerList}" var="principal" varStatus="status">
            <c:if test="${!status.first}">, </c:if>
            <c:out value="${principal.name}"/>
            <c:if test="${principal.class.name == 'kacang.services.security.Group'}">[G]</c:if>
        </c:forEach>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top;" colspan="2">
            <jsp:include page="../../panel.jsp"/>
      </td>
    </tr>

  </tbody>
</table>

