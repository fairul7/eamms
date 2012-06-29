<%@ include file="/common/header.jsp" %>
<c:set var="form" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<table width="100%">
    <tr>
        <td width="25%" valign="top" align="right" class="forumRowLabel">
        <fmt:message key='digest.label.digestName'/> *
        </td><td align="left">
        <x:display name="${form.digestList.absoluteName}"/>
        <span id="divid" class="forumRowLabel">
        <fmt:message key='digest.label.otherName'/>
        <x:display name="${form.digestName.absoluteName}"/>
        </span>
        </td>
    </tr>
    <c:if test="${! empty form.message}">
        <tr>
			<td width="25%" align="right">&nbsp;</td>
            <td align="left"><font color="red"><c:out value="${form.message}"/></font></td>
        </tr>
    </c:if>
    <tr>
        <td width="25%" align="right">&nbsp;</td>
        <td align="left">
            <x:display name="${form.submit.absoluteName}"/>
            <x:display name="${form.cancel.absoluteName}"/>
        </td>
    </tr>
</table>
<script language="javascript" type="text/JavaScript">
function sbInit(){
var myForm = document.forms['<c:out value="${form.absoluteName}" />'];
var field1 = myForm['<c:out value="${form.childMap.digestList.absoluteName}" />'];
if(field1.value=="none")
{
document.getElementById('divid').style.display = '';
}
else
{

document.getElementById('divid').style.display = 'none';
}
}
window.onload=sbInit;
</script>
<jsp:include page="../form_footer.jsp" flush="true"/>