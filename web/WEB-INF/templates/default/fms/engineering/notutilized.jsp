<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="widget" value="${widget}"/>
<table class="profileTable" cellpadding="3" cellspacing="1" width="100%">
    <jsp:include page="../../form_header.jsp" flush="true"/>
			
			<tr>
		        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.facility.notUtilizedReason"/></b>&nbsp;</td>
		        <td width="70%" class="profileRow"> <x:display name="${widget.reason.absoluteName}" /></td>
		    </tr>
		    <tr>
		        <td width="30%" nowrap class="profileRow" align="right">&nbsp;</td>
		        <td width="70%" class="profileRow">
		         <x:display name="${widget.submit.absoluteName}" />
			     <x:display name="${widget.cancelButton.absoluteName}" />
		        </td>
		    </tr>
		    
			
		</table>

<jsp:include page="../../form_footer.jsp" flush="true"/>