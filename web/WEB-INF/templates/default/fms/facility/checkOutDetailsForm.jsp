<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${widget}"/>
<table class="profileTable" cellpadding="3" cellspacing="1" width="100%">
    <jsp:include page="../../form_header.jsp" flush="true"/>
    <tr>
        <td height="22" width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.facility.form.date"/></b></td>
        <td width="70%" class="profileRow"><c:out value="${widget.date}"/></td>
    </tr>
    <tr>
        <td height="22" width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.facility.form.requestor"/></b></td>
        <td width="70%" class="profileRow"><c:out value="${widget.requestor}"/></td>
    </tr>
    <tr>
        <td height="22" width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.facility.form.assignmentLocation"/></b></td>
        <td width="70%" class="profileRow"><c:out value="${widget.location}"/></td>
    </tr>
    <tr>
        <td height="22" width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.facility.form.purpose"/></b></td>
        <td width="70%" class="profileRow"><c:out value="${widget.purpose}"/></td>
    </tr>
    <tr>
    	<td colspan="2" height="22" >&nbsp;</td>
    </tr>
    <tr>
    	<td colspan="2" height="22" bgcolor="#003366" class="contentTitleFont">
    	<b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.facility.title.itemListing"/></font></b>
    	</td>
    </tr>
    
    <tr>
        <td colspan="2" nowrap>
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<td colspan="7" height="30" style="vertical-align:bottom;text-align:right">
						<x:display name="${widget.search.absoluteName}"/>
						<x:display name="${widget.btnSearch.absoluteName}"/>
					</td>
				</tr>
			</table>
	        <table border="0" cellpadding="2" cellspacing="1" width="100%">
	        <thead>
            	<tr class="tableHeader">
                	<td>&nbsp;</td>
                	<td><b><fmt:message key="fms.facility.table.itemName"/></b></td>
                	<td><b><fmt:message key="fms.facility.table.itemCode"/></b></td>
					<td><b><fmt:message key="fms.facility.table.storeLocation"/></b></td>
                	<td><b><fmt:message key="fms.facility.table.checkOutDate"/></b></td>
					<td><b><fmt:message key="fms.facility.table.checkOutBy"/></b></td>
                	<td><b><fmt:message key="fms.facility.table.checkInDate"/></b></td>
					<td><b><fmt:message key="fms.facility.table.checkInBy"/></b></td>
					
	        	</tr>
	        </thead>
	        <tbody>
		        <c:if test="${! empty widget.itemList}">
	        		<c:forEach items="${widget.itemList}" var="item" varStatus="no">
		        		<tr class="tableRow"><td valign="top" height="25"><c:out value="${no.index+1}"/></td>
	            		<td valign="top"><c:out value="${item.name}"/></td>
	            		<td valign="top"><c:out value="${item.barcode}"/></td>
						<td valign="top"><c:out value="${item.location_name}"/></td>
	            		<td valign="top"><fmt:formatDate pattern="${globalDatetimeLong}" value="${item.checkout_date}"/></td>
						<td valign="top"><c:out value="${item.checkout_by}"/></td>
	            		<td valign="top"><fmt:formatDate pattern="${globalDatetimeLong}" value="${item.checkin_date}"/></td>
						<td valign="top"><c:out value="${item.checkin_by_name}"/></td>
						
	        		</c:forEach>
	    		</c:if>
            </tbody>    
	        </table>
        </td>
    </tr>    
    <tr>
    	<td colspan="2" height="10" >&nbsp;</td>
    </tr>
    <tr>
        <td colspan="2" style="text-align:center">
        	<x:display name="${widget.buttonPanel.absoluteName}"/>
		</td>
    </tr>

    <tr><td class="profileFooter" colspan="2">&nbsp;</td></tr>
    <jsp:include page="../../form_footer.jsp" flush="true"/>
</table>
