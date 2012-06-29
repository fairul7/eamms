<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${widget}"/>


<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
<td>
<jsp:include page="../form_header.jsp" flush="true"></jsp:include>
<table width="100%" cellpadding="0" cellspacing="0" class="classBackground" >
	<tr>
		<td>
                        
			<table width="100%" cellpadding="3" cellspacing="1">





<c:if test="${!(empty widget.id)}">
    <c:if test="${!(empty widget.message)}">
        <script>alert("<c:out value="${widget.message}"/>");</script>
    </c:if>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="security.label.name"/>*</td>
            <td class="classRow"><x:display name="${widget.groupName.absoluteName}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="security.label.description"/></td>
            <td class="classRow"><x:display name="${widget.description.absoluteName}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" align="right">&nbsp;</td>
            <td class="classRow"><x:display name="${widget.active.absoluteName}"/><fmt:message key="security.label.active"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="security.label.users"/></td>
            <td class="classRow"><x:display name="${widget.users.absoluteName}"/></td>
        </tr>
        <tr><td colspan="2" class="contentTitleFont">
        	<%-- span style="font-family:Arial; font-size:16px; font-weight:bold" --%>
        	<fmt:message key="security.label.permissions"/>
        	<%-- span --%>
        </td></tr>
        <tr><td colspan="2" class="classRow"><x:display name="${widget.permissions.absoluteName}"/></td></tr>
        <tr><td colspan="2" class="classRow">&nbsp;</td></tr>
        <tr>
            <td class="classRowLabel">&nbsp;</td>
            <td class="classRow">
            	<x:display name="${widget.groupButton.absoluteName}"/> <x:display name="${widget.cancelButton.absoluteName}"/>
            </td>
        </tr>
</c:if>
			<jsp:include flush="true" page="../form_footer.jsp"></jsp:include>

			</table>
		</td>
	</tr>
</table>
</td>
</tr>	
</table>


