<%@include file="/common/header.jsp"%>



<c:set value="${widget}" var="form" />


<jsp:include page="../form_header.jsp" flush="true"/>
<table border="1" cellpadding="2" cellspacing="0" class="formStyle" width="<c:out value="${form.width}"/>">
    <tr>
        <td  class="sfaRowLabel">
            <fmt:message key='sfa.message.name'/> &nbsp; *
        </td>
        <td>
            <x:display name="${form.nameTF.absoluteName}" />
        </td>
    </tr>

    <tr>
        <td valign="top" class="sfaRowLabel">
            <fmt:message key='sfa.message.description'/>:
        </td>
        <td>
            <x:display name="${form.descriptionTB.absoluteName}" />
        </td>

    </tr>
    <tr>
        <td valign="top" class="sfaRowLabel">
            <fmt:message key='sfa.message.members'/>:
        </td>
        <td>
            <x:display name="${form.userSelectBox.absoluteName}"/>
        </td>

    </tr>
    <tr>
        <td>
         &nbsp;
        </td>
        <td>
            <x:display name="${form.submit.absoluteName}"/>
			<c:if test="${form.editMode}">
				<x:display name="${form.cancel.absoluteName}" />
			</c:if>
        </td>
    </tr>



</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
