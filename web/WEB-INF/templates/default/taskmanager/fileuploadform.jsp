<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>
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


<table cellpadding="0" border="0" cellspacing = "0">
    <%--<tr>
        <td colspan="2" align="center">
            <b><fmt:message key='taskmanager.label.FilesUpload'/></b>
        </td>
    </tr>--%>

    <tr>
        <td> <table><Tr>
        <Td class="calendarRowLabel" align="right" valign="top"><fmt:message key='taskmanager.label.AttachedFiles'/></td><td class="calendarRow" align="left" valign="top">
            <x:display name="${form.filesListing.absoluteName}" /></td></tr></table>
        </td>
    </tr>

    <tr>
        <td colspan="2">
            <x:display name="${form.fileUpload.absoluteName}" ></x:display>
        </td>

    </tr>
    <tr>
        <td colspan="2">
            <x:display name="${form.uploadButton.absoluteName}" ></x:display>
        </td>

    </tr>


</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
