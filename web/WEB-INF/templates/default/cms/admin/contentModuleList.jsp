<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="list" scope="request" value="${widget}"/>

<table>
  <tr>
    <td>
        <c:choose>
          <c:when test="${empty list.selectedClass}">
            <b><x:event name="${list.absoluteName}" html="class=\"option\""><fmt:message key='cms.label.allContentTypes'/></x:event></b>
          </c:when>
          <c:otherwise>
            <x:event name="${list.absoluteName}" html="class=\"option\""><fmt:message key='cms.label.allContentTypes'/></x:event>
          </c:otherwise>
        </c:choose>
    </td>
  </tr>
<c:forEach items="${list.contentModuleClasses}" var="clazz" varStatus="stat">
  <tr>
    <td>

      <x:event name="${list.absoluteName}" type="${clazz.name}" html="class=\"option\"">
        <c:choose>
          <c:when test="${clazz.name == list.selectedClass}">
<%--            <b><c:out value="${clazz.name}"/></b>--%>
            <b><fmt:message key="cms.label.iconLabel_${clazz.name}" /></b>
          </c:when>
          <c:otherwise>
<%--            <c:out value="${clazz.name}"/>--%>
            <fmt:message key="cms.label.iconLabel_${clazz.name}" />
          </c:otherwise>
        </c:choose>
      </x:event>
    </td>
  </tr>
</c:forEach>
</table>
