<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<table width="100%" class="cotentBgColor" cellpadding="4" cellspacing="1">
    <tr><td colspan="2" class="contentTitleFont"><fmt:message key='forum.label.newForum'/></td></tr>
    <tr>
        <td valign="top" class="classRowLabel" align="right" width="150px"><fmt:message key='forum.label.forumName'/>&nbsp;&nbsp;<FONT class="forumRowLabel">*</FONT></td>
        <td class="classRow"><x:display name="${form.forumName.absoluteName}"/></td>
    </tr>
    <tr>
        <td valign="top" class="classRowLabel" align="right"><fmt:message key='general.label.status'/></td>
        <td class="classRow"><x:display name="${form.isPublic.absoluteName}"/><br>
            <x:display name="${form.active.absoluteName}"/></td>
    </tr>
    <tr>
        <td valign="top" class="classRowLabel" align="right"><fmt:message key='general.label.description'/></td>
        <td class="classRow"><x:display name="${form.description.absoluteName}"/></td>
    </tr>
    <tr>
        <td valign="top" class="classRowLabel" align="right"><fmt:message key="forum.label.category"/>&nbsp;&nbsp;<FONT class="forumRowLabel">*</FONT></td>
        <td class="classRow"><x:display name="${form.categories.absoluteName}"/></td>
    </tr>
    <tr>
        <td valign="top" class="classRowLabel" align="right">&nbsp;</td>
        <td class="classRow"><fmt:message key="forum.label.newCategory"/>&nbsp;&nbsp;<FONT class="forumRowLabel">*</FONT> : <x:display name="${form.newCategory.absoluteName}"/></td>
    </tr>
    <c:if test="${! empty form.message}">
        <tr>
            <td valign="top" class="classRowLabel" align="right">&nbsp;</td>
            <td class="classRow"><font color="red"><c:out value="${form.message}"/></font></td>
        </tr>
    </c:if>
    <tr><td colspan="2" class="classRowLabel" >&nbsp;</td></tr>
        <tr><td colspan="2" class="classRow"><span style="font-family:Arial; font-size:16px; font-weight:bold"><fmt:message key='forum.label.accessPermission'/></span></td></tr>
    <tr>
    
      <td class="classRowLabel">
        <b><fmt:message key='forum.label.byGroup'/></b>
        <hr size="1">
      </td>     
      <td></td>
      <tr>
        <td valign="top" class="classRowLabel" align="right"><fmt:message key='forum.label.moderators'/>&nbsp;&nbsp;<FONT class="forumRowLabel">*</FONT></td>
        <td class="classRow"><x:display name="${form.moderatorGroup.absoluteName}"/></td>
    </tr>
    <tr>
        <td valign="top" class="classRowLabel" align="right"><fmt:message key='forum.label.users'/></td>
        <td class="classRow"><x:display name="${form.userGroup.absoluteName}"/></td>
    </tr>
    <tr>
      <td class="classRowLabel">
        <b><fmt:message key='forum.label.byUsers'/></b>
        <hr size="1">
      </td>
      <td></td>
    </tr>
	<tr>
        <td valign="top" class="classRowLabel" align="right"><fmt:message key='forum.label.moderators'/> </td>
        <td class="classRow"><x:display name="${form.childMap.userModeratorSelectBox.absoluteName}"/></td>
    </tr>
    <tr>
        <td valign="top" class="classRowLabel" align="right"><fmt:message key='forum.label.users'/></td>
        <td class="classRow"><x:display name="${form.childMap.userSelectBox.absoluteName}"/></td>
    </tr>
    <tr><td colspan="2" class="classRowLabel" align="right">&nbsp;</td></tr>
    <tr>
        <td class="classRowLabel" align="right">&nbsp;</td>
        <td class="classRow">
            <x:display name="${form.createForum.absoluteName}"/>
            <x:display name="${form.cancel.absoluteName}"/>
        </td>
    </tr>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>