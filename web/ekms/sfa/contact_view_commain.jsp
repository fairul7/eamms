<%@include file="/common/header.jsp" %>
<%@page import="com.tms.crm.sales.misc.*, com.tms.crm.sales.ui.*" %>


<x:config>
    <page name="jsp_contactView_commain">
     	<com.tms.crm.sales.ui.ContactForm name="form1" type="View" width="100%" />
    </page>
</x:config>

<c:choose>
	<c:when test="${not empty (param.contactID)}">
		<c:set var="contactID" value="${param.contactID}"/>
	</c:when>
	<c:otherwise>
		<c:set var="contactID" value="${widgets['jsp_contactView_commain.form1'].contactID}"/>
	</c:otherwise>
</c:choose>

<%--
<c:set var="contactID" value="${param.contactID}" />
--%>
<x:set name="jsp_contactView_commain.form1" property="contactID" value="${contactID}" />
<%
	NaviUtil naviUtil = new NaviUtil(pageContext);
	naviUtil.getCompanyID4Contact("contactID", "companyID");
%>

    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
                <fmt:message key='sfa.message.viewContact'/>
                </td>
                 </tr>
    <tr>
    <td class="sfaRow">
	<x:display name="jsp_contactView_commain.form1"></x:display>
    <input type="button" class="button" value="<fmt:message key='sfa.message.edit'/>" onClick="location = 'contact_edit_commain.jsp?contactID=<c:out value="${contactID}" />'"/>
    <input type="button" class="button" value="<fmt:message key='sfa.message.back'/>" onClick="location = 'company_view_commain.jsp'"/>

<%--
	<a href=""></a>
--%>
<%--
	<br>
	
	<c:set var="buttonBack" value="contact_list_commain.jsp?companyID=${companyID}" scope="request"/>
	<jsp:include page="includes/navButtons.jsp"/>
--%>
</td></tr>

    <tr>
        <td class="sfaFooter">
            &nbsp;
        </td>
    </tr>
    </table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
