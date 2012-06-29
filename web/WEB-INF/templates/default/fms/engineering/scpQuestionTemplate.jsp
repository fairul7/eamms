<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="widget" value="${widget}"/>
<!-- jsp:include page="../../form_header.jsp" flush="true"/-->
<x:display name="${widget.panel.absoluteName}" body="custom">
<br>
<c:set var="colSpanValue" value="9"/>
<table align="center" class="borderTable" cellpadding="3" cellspacing="1" width="95%">
	<tr>
		<th>Please rate the following</th>
		<th colspan="4">&lt;&lt;Not Satisfactory</th>
		<th colspan="4">Excellent&gt;&gt;</th>
	</tr>
    	<!-- :forEach items="${widget.services}" var="obj" varStatus="sNo"-->
    	
    		<c:forEach items="${widget.lbQuestions}" var="check" varStatus="checkStatus">
				<tr>
					<td class="profileRow" align="left">&nbsp;
					<!-- c:if test="${checkStatus.index eq sNo.index}"-->
						<x:display name="${check.absoluteName}"/>
					<!-- /c:if-->
					</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>

				</tr>
			</c:forEach>
		
        <!-- /c:forEach-->
    
    <tr><td class="profileFooter" colspan='<c:out value="${colSpanValue}"/>'><x:display name="${widget.add.absoluteName}"/></td></tr>
</table><br>
</x:display>
<!-- >jsp:include page="../../form_footer.jsp" flush="true"/-->

