<%@ include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>
<table width="100%" cellpadding="4" cellspacing="1" class="forumBackground" >
<tr>
    <td align="right" valign="top" width="20%"><x:display name="${w.lbRemark.absoluteName}"/></td>
    <td ><x:display name="${w.tf_Remarks.absoluteName}"/></td>
</tr>
<tr>
    <td align="right" valign="top"><x:display name="${w.lbUserOriginator.absoluteName}"/></td>
    <td ><x:display name="${w.lbUserOriginatorName.absoluteName}"/></td>
</tr>
<c:if test="${w.owner==true}">
<tr>
    <td align="right" valign="top"><x:display name="${w.lbUserOwner.absoluteName}"/></td>
    <td ><x:display name="${w.sb_UserOwner.absoluteName}"/></td>
</tr>
</c:if>
<c:if test="${w.userApprover1==true}">
<tr>
    <td align="right" valign="top"><x:display name="${w.lbUserApprover1.absoluteName}"/></td>
    <td ><x:display name="${w.lbUserApprover1Name.absoluteName}"/></td>
</tr>
</c:if>
<c:if test="${w.userApprover2==true}">
<tr>
    <td align="right" valign="top"><x:display name="${w.lbUserApprover2.absoluteName}"/></td>
    <td ><x:display name="${w.lbUserApprover2Name.absoluteName}"/></td>
</tr>
</c:if>
<tr>
    <td align="right" valign="top"><x:display name="${w.lbAmount.absoluteName}"/></td>
    <td align="left"><x:display name="${w.lb_Amount.absoluteName}"/></td>
</tr>
<c:if test="${w.submit==true}">
<tr>
    <td  ></td>
    <td  ><x:display name="${w.bn_Submit.absoluteName}"/></td>
</tr>
</c:if>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>