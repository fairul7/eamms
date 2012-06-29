<%@ include file="/common/header.jsp" %>

<c:set var="form" scope="request" value="${widget}"/>

<form name="<c:out value="${form.absoluteName}"/>"
      action="<%= response.encodeURL(request.getRequestURI()) %>"
      method="<c:out value="${form.method}"/>"
      target="<c:out value="${form.target}"/>"
      <c:if test="${!empty form.enctype}">
          enctype="<c:out value="${form.enctype}"/>"
      </c:if>
      onSubmit="<c:out value="${form.attributeMap['onSubmit']}"/>"
      onReset="<c:out value="${form.attributeMap['onReset']}"/>"
>
<input type="hidden" name="cn" value="<c:out value="${form.absoluteName}"/>">

        <div style="display:none">
            <x:display name="${form.childMap.sbOptions.absoluteName}"/>
        </div>
        <x:display name="${form.popupTable.absoluteName}"/>

<%--
<table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
  <tbody>

    <tr>
      <td style="vertical-align: top;"><fmt:message key='general.label.selected'/><br>
      </td>
      <td style="vertical-align: top;">
        <x:display name="${form.childMap.sbOptions.absoluteName}"/>
        <br>
        <x:display name="${form.popupTable.absoluteName}"/>
      </td>
    </tr>

  </tbody>
</table>
--%>
</form>
