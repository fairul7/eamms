<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

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

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
<td>

<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
	<tr>
		<td>
                        <jsp:include page="../form_header.jsp" flush="true"></jsp:include>
			<table width="100%" cellpadding="3" cellspacing="1">


<%-- 
<table cellpadding="5" cellspacing="2" border="0" style="text-align: left; width: 100%;">
  <tbody>
--%>

    <tr>
      <td class="classRowLabel" align="right" style="vertical-align: top;"><fmt:message key='siteadmin.label.siteTemplate'/><br>
      </td>
      <td class="classRow" style="vertical-align: top;">
        <x:display name="${form.childMap.siteTemplateBox.absoluteName}"/>
        <x:display name="${form.childMap.siteTemplateButton.absoluteName}"/>
      </td>
    </tr>

    <tr>
      <td class="contentTitleFont" style="vertical-align: top;" colspan="2">
        <%-- span style="font-family:Arial; font-size:12px; font-weight:bold" --%>
        <fmt:message key='siteadmin.label.contentTemplates'/>
        <%-- /span --%>
      </td>
    </tr>

    <tr>
      <td class="classRowLabel" align="right" style="vertical-align: top;"><fmt:message key='siteadmin.label.defaultTemplate'/><br>
      </td>
      <td class="classRow" style="vertical-align: top;">
            /content.jsp
      </td>
    </tr>

    <tr>
      <td class="classRowLabel" align="right" style="vertical-align: top;"><fmt:message key='siteadmin.label.customTemplate'/><br>
      </td>
      <td class="classRow" style="vertical-align: top;">
        <x:display name="${form.childMap.pageTemplateBox.absoluteName}"/>
        <x:display name="${form.childMap.pageTemplateButton.absoluteName}"/>
      </td>
    </tr>

    <tr>
      <td class="classRowLabel" align="right" style="vertical-align: top;"><fmt:message key='siteadmin.label.selectedContent'/><br>
      </td>
      <td class="classRow" style="vertical-align: top;">
        <x:display name="${form.childMap.selectedContentBox.absoluteName}"/>
        <x:display name="${form.childMap.removeButton.absoluteName}"/>
        <br>
        <x:display name="${form.childMap.contentListTable.absoluteName}"/>
      </td>
    </tr>

<%-- 
  </tbody>
</table>
--%>

</table>
		</td>
	</tr>
</table>

</td>
</tr>	
</table>


</form>
