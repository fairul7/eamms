<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="crt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}" />
<jsp:include page="../form_header.jsp" flush="true"/>

<table cellpadding="3" cellspacing="2" class="sterlingBackground" border="0" width="100%" align="left" >

	<tr>
		<td class="sterlingRow" width="60%" align="left" valign="top">
			<table cellpadding="1" cellspacing="1" border="0" width="100%">
			<tr>
				<td width="20%" class="classRowLabel" align="right">First Alert</td>
				<td class="classRow"><x:display name="${form.childMap.firstAlert.absoluteName}"/> &nbsp;&nbsp; hour(s)</td>
			</tr>
			</table>
		</td>
    </tr>

	<tr>
		<td class="sterlingRow" width="60%" align="left" valign="top">
		<table cellpadding="1" cellspacing="1" border="0" width="100%">
			<tr>
			<td width="20%" class="classRowLabel" align="right">Subsequent Alert</td>
			<td class="classRow"><x:display name="${form.childMap.subsequentAlert.absoluteName}"/> &nbsp;&nbsp; hour(s)</td>
			</tr>
		</table>
		</td>
    </tr>

	<tr>
		<td class="sterlingRow" width="60%" align="left" valign="top">
		<table cellpadding="1" cellspacing="1" border="0" width="100%">
			<tr>
			<td width="20%" class="classRowLabel" align="right">Alert Occurance</td>
			<td class="classRow"><x:display name="${form.childMap.alertOccurance.absoluteName}"/> &nbsp;&nbsp; time(s)</td>
			</tr>
		</table>
		</td>
    </tr>


	<tr>
		<td class="sterlingRow" width="60%" align="left" valign="top">
		<table cellpadding="1" cellspacing="1" border="0" width="100%">
			<tr>
			<td width="20%" class="classRowLabel" align="right">Alert Method(s)</td>
			<td class="classRow"><x:display name="${form.childMap.memo.absoluteName}"/><x:display name="${form.childMap.email.absoluteName}"/></td>
			</tr>
		</table>
		</td>
    </tr>

	<tr>
			<td width="15%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<x:display name="${form.childMap.save.absoluteName}"/></td>
		</td>
	</tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
