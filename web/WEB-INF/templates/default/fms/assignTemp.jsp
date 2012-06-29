<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<table cellpadding="1" cellspacing="1" width="100%">

<tr>
  	<td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.table.label.manpower" /></b></td> 
	<td width="70%" class="profileRow" valign="top">
		<div style="padding-left:5px">
			<x:display name="${form.manpower.absoluteName}" />
		</div> 
  	</td> 
</tr>
<tr>
  	<td width="30%" nowrap class="profileRow" align="right" height="25" valign="top"><b><fmt:message key="fms.label.selectAssignmentType" /></b></td> 
	<td width="70%" class="profileRow" valign="top">
		<div style="padding-left:5px">
			<x:display name="${form.chargeBack.absoluteName}" /> 
			<x:display name="${form.callBack.absoluteName}" />
		</div> 
  	</td> 
</tr>
<c:if test="${form.blockBooking eq 'true'}">
	<tr>
	  	<td width="30%" class="profileRow" align="left" height="35" valign="top" colspan="2"><b><fmt:message key="fms.label.thisIsBlockBooking" /></b></td>
	</tr>
	<tr>
		<td height="25"></td> 
		<td width="70%" class="profileRow" valign="top">
			<div style="padding-left:5px">
				<x:display name="${form.rdYes.absoluteName}" /> 
				<x:display name="${form.rdNo.absoluteName}" />
			</div> 
	  	</td> 
	</tr>
</c:if>
<tr>
  	<td class="profileRow" height="10" valign="top">&nbsp;</td>
	<td class="profileRow" valign="top">
		<div style="padding-left:5px">
   			<x:display name="${form.submit.absoluteName}" />
		</div>     
  	</td>
</tr>

</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
