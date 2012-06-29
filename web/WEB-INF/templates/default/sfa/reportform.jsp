<%@include file="/common/header.jsp"%>

<c:set var="form" value="${widget}"></c:set>

    <jsp:include page="../form_header.jsp" flush="true"/>


<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>">
 <tr>
    <td colspan ="2">
        <b><fmt:message key='sfa.message.selectdaterangetoviewreport'/></b>
    </td>

 </tr>

 <tr>
     <td>
        <b><fmt:message key='sfa.message.from'/><b/>
     </td>
     <td>
        <x:display name="${form.from.absoluteName}" />
     </td>
  </tr>
  <tr>
     <td>
        <b><fmt:message key='sfa.message.to'/></b>
     </td>
     <td>
        <x:display name="${form.to.absoluteName}" />
     </td>
  </tr>

  <tr>
    <td>&nbsp;</td>

	<td>
        <x:display name="${form.view.absoluteName}" />
    </td>
  </tr>

</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
