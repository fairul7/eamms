            <%@ include file="/common/header.jsp"%>

                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr><td bgcolor="#000066"><img src="<c:url value="/ekms/images/" />blank.gif" width="5" height="1"></td></tr>
        <tr>
            <td>
                <table width="100%" border="0" cellpadding="7" cellspacing="0">
                    <tr>
                        <td bgcolor="666666" class="topMenuBg">
                            <table border="0" cellspacing="0" cellpadding="0">
                                <tr align="CENTER">
                                    <td nowrap><b>&nbsp;&nbsp;<a href="<c:url value="/ekms/"/>"><font color="#FFFFFF" class="titleMenuSmall"><fmt:message key="general.label.dashboard"/></font></A></b></td>
                                </tr>
                            </table>
                        </td>
                        <td align="right" bgcolor="666666" class="topMenuBg">
                            <table border="0" cellspacing="0" cellpadding="0">
                                <tr align="CENTER" >
                                    <td><font color="#FFFFFF" class="titleMenuFont"><c:out value="${sessionScope.currentUser.propertyMap.firstName}"/> <c:out value="${sessionScope.currentUser.propertyMap.lastName}"/></font></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</body>
</html>