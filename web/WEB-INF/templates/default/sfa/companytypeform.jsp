<%@include file="/common/header.jsp"%>

<c:set var="form" value="${widget}"  ></c:set>

    <jsp:include page="../form_header.jsp" flush="true"/>


<table border="0" cellpadding="2" cellspacing="0" class="formStyle" width="<c:out value="${form.width}"/>">
        <tr>
            <td class="classRowLabel" align="right">
                <fmt:message key='sfa.message.companyType'/>&nbsp;<c:out value="*"/> 
            </td>

            <td class="classRow">
                <x:display name="${form.tf_type.absoluteName}" />
            </td>
        </tr>

        <tr>
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.archived'/>:&nbsp;</td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.sel_IsArchived"/></td>
		</tr>
		<tr>
			<td align="center" colspan="2" class="classRow">
				<x:display name="${form.absoluteName}.submit"/>
                <c:if test="${form.editMode}">
					<x:display name="${form.cancel.absoluteName}" />
				</c:if>
            </td>
		</tr>
	</table>
     <jsp:include page="../form_footer.jsp" flush="true"/>
     


