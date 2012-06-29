<%@ include file="/common/header.jsp" %>

<c:set var="entry" value="${widget.entry}"/>

<jsp:include page="../form_header.jsp" flush="true" />

<table width="100%" border="0" cellpadding="5" cellspacing="1">

  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap width="30%"><strong><fmt:message key="leave.label.leaveType"/></strong></td>
    <td class="contentBgColor">
        <c:out value="${entry.leaveType}"/> (<c:out value="${entry.leaveTypeName}"/>)
        <c:if test="${entry.credit}"> - <strong><fmt:message key="leave.label.CreditLeave"/></strong></c:if>
        <c:if test="${entry.adjustment}"> - <strong><fmt:message key="leave.label.adjustment"/></strong></c:if>
    </td>
  </tr>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap width="30%"><strong><fmt:message key="leave.label.applyDate"/></strong></td>
    <td class="contentBgColor">
        <fmt:formatDate pattern="${globalDateLong}" value="${entry.applicationDate}"/>
    </td>
  </tr>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap width="30%"><strong><fmt:message key="leave.label.startDate"/></strong></td>
    <td class="contentBgColor">
        <fmt:formatDate pattern="${globalDateLong}" value="${entry.startDate}"/>
    </td>
  </tr>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap width="30%"><strong><fmt:message key="leave.label.endDate"/></strong></td>
    <td class="contentBgColor">
        <fmt:formatDate pattern="${globalDateLong}" value="${entry.endDate}"/>
    </td>
  </tr>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap width="30%"><strong><fmt:message key="leave.label.appliedDays"/></strong></td>
    <td class="contentBgColor">
        <fmt:formatNumber pattern="#.#" value="${entry.calculatedDays}"/>
    </td>
  </tr>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap width="30%"><strong><fmt:message key="leave.label.reason"/></strong></td>
    <td class="contentBgColor">
        <c:out value="${entry.reason}"/>
    </td>
  </tr>            
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap width="30%"><strong><fmt:message key="leave.label.status"/></strong></td>
    <td class="contentBgColor">
        <c:out value="${entry.status}"/>
    </td>
  </tr>	 	
  	
            		
  <c:set var="abc" value="${widget.statusRejectTxtfd}"/> 
  
<% 
	String ab = (String)pageContext.getAttribute("abc");	
   if (ab.equals("SHOW")){
%>
   
 <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap width="30%"><strong><fmt:message key="leave.label.rejectreason"/></strong></td>
    <td class="contentBgColor">
        <x:display name="${widget.childMap.reasonRejectText.absoluteName}"/>
    </td>
  </tr> 
<%   
   }
%>   
          	

  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap width="30%">&nbsp;</td>
    <td class="contentBgColor">
            <x:display name="${widget.childMap.approveLeaveButton.absoluteName}"/>
            <x:display name="${widget.childMap.rejectLeaveButton.absoluteName}"/>
            <x:display name="${widget.childMap.cancelLeaveButton.absoluteName}"/>
    </td>
  </tr>
</table>
<%--<jsp:include page="../form_footer.jsp" flush="true" />--%>
<%@include file="/ekms/includes/footer.jsp" %>
