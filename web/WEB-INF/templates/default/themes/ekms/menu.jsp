<%@ include file="/common/header.jsp" %>

    <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr bgcolor="#B5CFE7"><td colspan="4" class="titleMenuTop"><img src="<c:url value="/ekms/images/blank.gif"/>" width="5" height="2"></td></tr>
        <tr bgcolor="#639ACE">
            <td Valign="middle" class="titleMenu">
                <table border="0" cellspacing="0" cellpadding="0">
                    <tr align="CENTER">
                        <td nowrap><b>&nbsp;&nbsp;<a href="<c:url value="/ekms/"/>"><font color="#FFFFFF" class="titleMenuFont"><fmt:message key="general.label.dashboard"/></font></A></b></td>
                        <td><font color="#FFFFFF" class="titleMenuFont">&nbsp;&nbsp;</font></td>
                    </tr>
                </table>
            </td>
            <td colspan="3" align="right" Valign="middle" class="titleMenu">
                <table border="0" cellspacing="0" cellpadding="0">
                    <tr align="CENTER">
						<x:display name="portal.appMenu" body="custom">
							<c:forEach items="${widget.menuItems}" var="menuItem" varStatus="status">
								<td nowrap><b><a href="<c:out value="${menuItem.link}"/>"><font color="#FFFFFF" class="titleMenuFont"><c:out value="${menuItem.label}"/></font></A></b></td>
								<c:if test="${! status.last}">
									<td><font color="#FFFFFF" class="titleMenuFont">&nbsp;&nbsp;|&nbsp;&nbsp;</font></td>
								</c:if>
							</c:forEach>
						</x:display>
                        <td height="22">&nbsp;&nbsp;</td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr bgcolor="#00557B"><td colspan="4" class="titleMenuBottom"><img src="<c:url value="/ekms/images/blank.gif"/>" width="5" height="3"></td></tr>
    </table>
