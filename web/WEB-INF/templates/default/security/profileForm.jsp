<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${widget}"/>
<table class="profileTable" cellpadding="3" cellspacing="1" width="100%">
    <jsp:include page="../form_header.jsp" flush="true"/>
    <tr><td colspan="2" class="profileHeader"><b><fmt:message key="security.label.personalInformation"/></b></td></tr>
    <tr><td class="profileRow" colspan="2">&nbsp;</td></tr>
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><fmt:message key="security.label.firstName"/>&nbsp;</td>
        <td width="70%" class="profileRow"><font class="profileError"><x:display name="${widget.firstName.absoluteName}"/> <c:out value="${widget.messages.firstName}"/></td>
    </tr>
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><fmt:message key="security.label.lastName"/>&nbsp;</td>
        <td width="70%" class="profileRow"><font class="profileError"><x:display name="${widget.lastName.absoluteName}"/> <c:out value="${widget.messages.lastName}"/></td>
    </tr>
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><fmt:message key="security.label.email"/>&nbsp;</td>
        <td width="70%" class="profileRow"><font class="profileError"><x:display name="${widget.email.absoluteName}"/> <c:out value="${widget.messages.email}"/></td>
    </tr>

    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><fmt:message key="fms.label.staffID"/></td>
        <td width="70%" class="profileRow"><x:display name="${widget.staffID.absoluteName}"/> <%--<c:out value="${widget.messages.telMobile}"/>--%></td>
    </tr>    
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><fmt:message key="security.label.mobilePhone"/></td>
        <td width="70%" class="profileRow"><x:display name="${widget.telMobile.absoluteName}"/> <c:out value="${widget.messages.telMobile}"/></td>
    </tr>    
	<tr>
        <td width="30%" nowrap class="profileRow" align="right"><fmt:message key="fms.label.officeNoExt"/></td>
        <td width="70%" class="profileRow"><x:display name="${widget.telOffice.absoluteName}"/> <c:out value="${widget.messages.telOffice}"/></td>
    </tr>
<%-- 
	<tr>
        <td width="30%" nowrap class="profileRow" align="right"><fmt:message key='fms.label.department'/></td>
        <td width="70%" class="profileRow"><x:display name="${widget.sbDepartment.absoluteName}"/> <c:out value="${widget.messages.telOffice}"/></td>
    </tr>
	<tr>
        <td width="30%" nowrap class="profileRow" align="right"><fmt:message key='fms.label.unit'/></td>
        <td width="70%" class="profileRow"><x:display name="${widget.sbUnit.absoluteName}"/> <c:out value="${widget.messages.telOffice}"/></td>
    </tr>
--%>

    <tr><td colspan="2" class="profileHeader"><b><fmt:message key="security.label.contactInformation"/></b></td></tr>
    <tr>
        <td width="30%" nowrap class="profileRow" align="right" valign="top"><fmt:message key="security.label.address"/></td>
        <td width="70%" class="profileRow"><x:display name="${widget.address.absoluteName}"/> <c:out value="${widget.messages.address}"/></td>
    </tr>
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><fmt:message key="security.label.postcode"/></td>
        <td width="70%" class="profileRow"><x:display name="${widget.postcode.absoluteName}"/> <c:out value="${widget.messages.postcode}"/></td>
    </tr>
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><fmt:message key="security.label.city"/></td>
        <td width="70%" class="profileRow"><x:display name="${widget.city.absoluteName}"/> <c:out value="${widget.messages.city}"/></td>
    </tr>
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><fmt:message key="security.label.state"/></td>
        <td width="70%" class="profileRow"><x:display name="${widget.state.absoluteName}"/> <c:out value="${widget.messages.absoluteName}"/></td>
    </tr>
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><fmt:message key="security.label.country"/></td>
        <td width="70%" class="profileRow"><x:display name="${widget.country.absoluteName}"/> <c:out value="${widget.messages.country}"/></td>
    </tr>
<%-- 
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><fmt:message key="security.label.officePhone"/></td>
        <td width="70%" class="profileRow"><x:display name="${widget.telOffice.absoluteName}"/> <c:out value="${widget.messages.telOffice}"/></td>
    </tr>
--%>    
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><fmt:message key="security.label.homePhone"/></td>
        <td width="70%" class="profileRow"><x:display name="${widget.telHome.absoluteName}"/> <c:out value="${widget.messages.telHome}"/></td>
    </tr>
<%-- 
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><fmt:message key="security.label.mobilePhone"/></td>
        <td width="70%" class="profileRow"><x:display name="${widget.telMobile.absoluteName}"/> <c:out value="${widget.messages.telMobile}"/></td>
    </tr>
--%>    
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><fmt:message key="security.label.fax"/></td>
        <td width="70%" class="profileRow"><x:display name="${widget.telFax.absoluteName}"/> <c:out value="${widget.messages.telFax}"/></td>
    </tr>
    <tr><td colspan="2" class="profileHeader"><b><fmt:message key="security.label.authenticationInformation"/></b></td></tr>
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><fmt:message key="security.label.username"/>&nbsp;</td>
        <td width="70%" class="profileRow"><c:out value="${widget.username.value}"/><input type="hidden" name="<c:out value="${widget.username.absoluteName}"/>" value="<c:out value="${widget.username.value}"/>"></td>
    </tr>
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><fmt:message key="security.label.currentPassword"/>&nbsp;</td>
        <td width="70%" class="profileRow"><x:display name="${widget.currentPassword.absoluteName}"/> <c:out value="${widget.messages.currentPassword}"/></td>
    </tr>
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><fmt:message key="security.label.newPassword"/>&nbsp;</td>
        <td width="70%" class="profileRow"><x:display name="${widget.newPassword.absoluteName}"/> <c:out value="${widget.messages.newPassword}"/></td>
    </tr>
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><fmt:message key="security.label.confirmPassword"/>&nbsp;</td>
        <td width="70%" class="profileRow"><x:display name="${widget.confirmPassword.absoluteName}"/> <c:out value="${widget.messages.confirmPassword}"/></td>
    </tr>
    <c:if test="${! empty widget.profilers}">
        <c:forEach items="${widget.profilers}" var="profiler">
            <tr><td colspan="2" class="profileHeader"><b><c:out value="${profiler.profileableLabel}"/></b></td></tr>
            <x:display name="${profiler.widget.absoluteName}"/>
        </c:forEach>
    </c:if>
    <tr>
        <td width="30%" nowrap class="profileRow">&nbsp;</td>
        <td width="70%" class="profileRow">
            <x:display name="${widget.update.absoluteName}"/>
            <x:display name="${widget.cancel.absoluteName}"/>
        </td>
    </tr>
    <tr><td class="profileFooter" colspan="2">&nbsp;</td></tr>
    <jsp:include page="../form_footer.jsp" flush="true"/>
</table>
