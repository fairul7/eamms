<%@include file="/common/header.jsp" %>
<%@page import="com.tms.crm.sales.misc.*, com.tms.crm.sales.model.*" %>

<x:config>
    <page name="jsp_projection2">
     	<com.tms.crm.sales.ui.ProjectionForm name="form1" />
    </page>
</x:config>

<c:if test="${forward.name == 'cancel_form_action'}" >
    <c:redirect url="/ekms/sfa/setup_projection.jsp" />
</c:if>


<c:choose>
	<c:when test="${not empty(param.userID)}">
		<c:set var="userID" value="${param.userID}"/>
		<c:set var="year"   value="${param.year}"/>
        <x:set name="jsp_projection2.form1" property="userID" value="${userID}" />
        <x:set name="jsp_projection2.form1" property="year"   value="${year}" />
	</c:when>
	<c:otherwise>
		<c:set var="userID" value="${widgets['jsp_projection2.form1'].userID}"/>
		<c:set var="year"   value="${widgets['jsp_projection2.form1'].year}"/>
	</c:otherwise>
</c:choose>
<c:if test="${forward.name == 'updateProjection'}">
	<c:redirect url="setup_projection.jsp"/>
</c:if>


    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
            <fmt:message key='sfa.message.projection'/>
             </tr>
    <tr>
    <td class="sfaRow">

<%
	NaviUtil naviUtil = new NaviUtil(pageContext);
	String userID = (String) pageContext.getAttribute("userID");
	AccountManager acMan = naviUtil.getAccountManager(userID);
%>

<span class="niceStyle"><fmt:message key='sfa.message.user'/>: <%=acMan.getFirstName()%> <%=acMan.getLastName()%> (<fmt:message key='sfa.message.year'/>: <c:out value="${year}"/>)</span><br><br>

<x:display name="jsp_projection2.form1"></x:display>
</td></tr>

    <tr>
        <td class="sfaFooter">
            &nbsp;
        </td>
    </tr>
    </table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
