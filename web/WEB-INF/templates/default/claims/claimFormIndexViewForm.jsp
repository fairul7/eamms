<%@ include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>

<table width="100%" cellpadding="4" cellspacing="1" class="forumBackground" >
<tr>
    <td align="right" valign="top" width="15%"><b><fmt:message key="claims.label.title"/></b></td>
    <td width="30%"><x:display name="${w.lbTitle.absoluteName}"/></td>
    <td width="10%">&nbsp;</td>
    <td width="15%" align="right"><b><fmt:message key="claims.label.date"/></b></td>
    <td width="30%"><x:display name="${w.lbDate.absoluteName}"/></td>
</tr>
<tr>
    <td align="right" valign="top" width="15%"><b><fmt:message key="claims.label.submittedBy"/></b></td>
    <td width="30%"><x:display name="${w.lbUserOriginator.absoluteName}"/></td>
    <td width="10%">&nbsp;</td>
    <td width="15%" align="right"><b><fmt:message key="claims.label.claimant"/></b></td>
    <td width="30%"><x:display name="${w.lbUserOwner.absoluteName}"/></td>
</tr>
<c:if test="${w.approver1==true}">
<tr>
    <td align="right" valign="top" width="15%"><b><fmt:message key="claims.label.approver1"/></b></td>
    <td width="30%"><x:display name="${w.lbUserApprover1.absoluteName}"/></td>
    <td width="10%">&nbsp;</td>
    <c:if test="${w.approver2==true}">
    <td width="15%" align="right"><b><fmt:message key="claims.label.approver2"/></b></td>
    <td width="30%"><x:display name="${w.lbUserApprover2.absoluteName}"/></td>
    </c:if>
    <c:if test="${w.approver2==false}">
    <td colspan="2">&nbsp;</td>
    </c:if>
</tr>


     <c:if test="${w.approver1DateBool==true}">
<tr>


    <td align="right" valign="top" width="15%"><b>Approved Date</b></td>
    <td width="30%"><x:display name="${w.approver1Date.absoluteName}"/></td>
    <td width="10%">&nbsp;</td>

    <c:if test="${w.approver2DateBool==true}">
    <td width="15%" align="right"><b>Approved Date</b></td>
    <td width="30%"><x:display name="${w.approver2Date.absoluteName}"/></td>
    </c:if>
    <c:if test="${w.approver2DateBool==false}">
    <td colspan="2">&nbsp;</td>
    </c:if>
</tr>

    </c:if>

</c:if>
<tr>
    <td align="right" valign="top" width="15%"><b><fmt:message key="claims.label.amount"/></b></td>
    <td width="30%"><x:display name="${w.lbAmount.absoluteName}"/></td>
    <td width="10%">&nbsp;</td>
    <td width="15%" align="right">&nbsp;</td>
    <td width="30%">&nbsp;</td>
</tr>
</table>
