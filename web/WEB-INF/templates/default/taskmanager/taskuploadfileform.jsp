<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>

<%--<jsp:include page="../form_header.jsp" flush="true"/>--%>

<form style="margin: 0px; border: none" name="<c:out value="${form.absoluteName}"/>"
      action="?"
      method="<c:out value="${form.method}"/>"
      target="<c:out value="${form.target}"/>"
      <c:if test="${!empty form.enctype}">
          enctype="<c:out value="${form.enctype}"/>"
      </c:if>
      onSubmit=""
      onReset="<c:out value="${form.attributeMap['onReset']}"/>"
>
   <input type="hidden" name="cn" value="<c:out value="${form.absoluteName}"/>">
<table   cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
    <tr>
        <td class="calendarRow">
            <x:display name="${form.uploadForm.absoluteName}" />
        </td>
    </tr>

    <tr>
        <td align="left" class="calendarRow">
        <c:set var="doneUrl">
        <%=response.encodeURL(request.getRequestURI())%>?cn=<c:out value="${form.absoluteName}" />&et=done
        </c:set>
        <INPUT class="button" type="button" name="done" value="Done" onClick="document.location = '<c:out value="${doneUrl}"/>'" />
        <%--<a href="<%=response.encodeURL(request.getRequestURI())%>?cn=<c:out value="${form.absoluteName}" />&et=done"><fmt:message key='taskmanager.label.Done'/></a>--%>
        </td>
    </tr>

</table>
<%--
<jsp:include page="../form_footer.jsp" flush="true"/>--%>
