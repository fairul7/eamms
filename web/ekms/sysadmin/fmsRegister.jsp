<%@ include file="/common/header.jsp" %>

<!-- Declare Widgets -->
<x:config>
	<page name="register">
		<tabbedpanel name="ad" width="100%">
		<panel name="pending" text="Pending Users">
			<com.tms.fms.register.ui.FMSTable name="0" width="100%" />			
		</panel>
		<panel name="accepted" text="Accepted Users">
			<com.tms.fms.register.ui.FMSTable name="1" width="100%" />			
		</panel>
		<panel name="rejected" text="Rejected Users">
			<com.tms.fms.register.ui.FMSTable name="2" width="100%" />			
		</panel>
		</tabbedpanel>
	</page>
</x:config>


<c:if test="${! empty param.userId}">
    <c:set var="userId" value="${param.userId}"></c:set>
</c:if>





<c:if test="${!empty param.id}">
	<c:redirect url="editFmsRegister.jsp?id=${param.id}"/> 
</c:if>

<c:if test="${forward.name == 'Reject'}" >
  <script>     
  	window.open("rejectform.jsp?id=<c:out value="${userId}"/>", "roleWindow", "width=400,height=200,scrollbars=yes,resizable=yes");
  </script>
</c:if>


<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key='general.label.systemAdministration'/> > <fmt:message key="fms.label.registrationList"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
   	
   	<table width="100%" border="0" cellspacing="0" cellpadding="5">
    
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <table width="100%" bgcolor="#FFFFFF" cellpadding="0" cellspacing="0"><tr><td>

            <table width="100%" cellspacing="1" cellpadding="5">
                <tr valign="top">
                    <td bgcolor="#EFEFEF" class="contentBgColor">
                       <x:display name="register.ad"/>
                    </td>
                </tr>
                
            </table>

        </td></tr></table>

    </td></tr>

    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>


