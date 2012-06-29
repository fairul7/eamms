<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<%@ page import="kacang.Application,com.tms.fms.engineering.model.EngineeringModule"%>

<c:set var="widget" value="${widget}"/>
<script type="text/javascript">

	onload = function() {
		populateClientName('<c:out value="${widget.lbRequestType}" />');
	}	
</script>
 <jsp:include page="../../form_header.jsp" flush="true"/>
<table class="profileTable" cellpadding="3" cellspacing="1" width="100%">
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.request.label.requestTitle"/>*</b>&nbsp;</td>
        <td width="70%" class="profileRow"><x:display name="${widget.title.absoluteName}"/></td>
    </tr>
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.request.label.requestType"/>*</b>&nbsp;</td>
        <td width="70%" class="profileRow">
        	<c:if test="${! empty widget.requestType}">
		        <c:forEach items="${widget.requestType}" var="profiler">
		            <x:display name="${profiler.absoluteName}"/>
		        </c:forEach>
	    	</c:if>
        </td>
    </tr>
    <tr>
    	<td colspan="2">
    	<div id="client" <c:if test="${widget.lbRequestType != 'E'}">style="display:none"</c:if>>
	    	<table width="100%"><tr>
		        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.request.label.clientName"/></b>&nbsp;</td>
		        <td width="70%" class="profileRow"><x:display name="${widget.clientName.absoluteName}"/></td>
	        </tr></table>
	    </div>
    	</td>
    </tr>
    <tr id="programType" <c:if test="${widget.lbRequestType != 'I'}">style="display:none"</c:if>>
        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.request.label.programType"/>*</b>&nbsp;</td>
        <td width="70%" class="profileRow">
        <c:if test="${! empty widget.programType}">
		        <c:forEach items="${widget.programType}" var="program">
		            <x:display name="${program.absoluteName}"/>
		        </c:forEach>
	    	</c:if>
        </td>
    </tr>
    <tr id="program" <c:if test="${widget.lbRequestType != 'I'}">style="display:none"</c:if>>
        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.request.label.program"/>*</b>&nbsp;</td>
        <td width="70%" class="profileRow"><x:display name="${widget.program.absoluteName}"/></td>
    </tr>
    <tr>
        <td width="30%" nowrap class="profileRow"  valign="top" align="right"><b><fmt:message key="fms.request.label.remarks"/></b>&nbsp;</td>
        <td width="70%" class="profileRow"><x:display name="${widget.remarks.absoluteName}"/></td>
    </tr>
    
    <tr>
        <td width="30%" nowrap class="profileRow">&nbsp;</td>
        <td width="70%" class="profileRow">
            <x:display name="${widget.submit.absoluteName}"/>
            <x:display name="${widget.cancel.absoluteName}"/>
        </td>
    </tr>
    <tr><td class="profileFooter" colspan="2">&nbsp;</td></tr>
    
</table>
<jsp:include page="../../form_footer.jsp" flush="true"/>
<script>
	function populateClientName(selValue){
		var tab=document.getElementById("client");
		var programType=document.getElementById("programType");
		var program=document.getElementById("program");
		if(selValue=='<%=EngineeringModule.REQUEST_TYPE_EXTERNAL%>'){
			tab.style.display='block';
			programType.style.display='none';
			program.style.display='none';
		}else if(selValue=='<%=EngineeringModule.REQUEST_TYPE_NONPROGRAM%>'){
			tab.style.display='none';
			programType.style.display='none';
			program.style.display='none';
		}else{
			tab.style.display='none';
			programType.style.display='';
			program.style.display='';
		}	
	}
</script>