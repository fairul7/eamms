<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>
<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
    <tr>
        <td>
            <table width="100%" cellpadding="3" cellspacing="1">
                <jsp:include page="../form_header.jsp" flush="true"/>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">User</td>
                    <td class="classRow" align="left"><c:out value="${form.userName.value}"/>
                        <input type="hidden" name="<c:out value="${form.userName.absoluteName}"/>" value="<c:out value="${form.userName.value}"/>">
                    </td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">Gender</td>
                    <td class="classRow" align="left" >
                        <x:display name="${form.male.absoluteName}"/> &nbsp;&nbsp;
                        <x:display name="${form.female.absoluteName}"/>
                    </td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">Date Joined</td>
                    <td class="classRow" align="left"><x:display name="${form.joinDate.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">Department</td>
                    <td class="classRow" align="left"><x:display name="${form.department.absoluteName}"/></td>
                </tr>
<%--
                <tr>
                    <td class="classRowLabel" valign="top" align="right">Email</td>
                    <td class="classRow" align="left"><x:display name="${form.email.absoluteName}"/></td>
                </tr>
--%>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">&nbsp;</td>
                    <td class="classRow" valign="top">
                        <x:display name="${form.add.absoluteName}"/>
<%--                        <x:display name="${form.reset.absoluteName}"/>--%>
                        <x:display name="${form.cancel.absoluteName}"/>
                    </td>
                </tr>
                <jsp:include page="../form_footer.jsp" flush="true"/>
            </table>
        </td>
    </tr>
</table>
