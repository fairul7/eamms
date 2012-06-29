<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>
<c:choose>
<c:when test="${form.type == 'View'}">
	<table cellpadding="2" cellspacing="0" class="formStyle" width="<c:out value="${form.width}"/>">
		<tr>
			<td valign="top" width="150px" align="right" class="classRowLabel"><fmt:message key='sfa.message.company'/></td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.lbCompanyName"/></td>
		</tr>
		<tr>
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.firstName'/></td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.lbContactFirstName"/></td>
		</tr>
		<tr>
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.lastName'/></td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.lbContactLastName"/></td>
		</tr>
		<tr>
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.designation'/></td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.lbContactDesignation"/>&nbsp;</td>
		</tr>
		<tr>	
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.salutation'/></td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.lbSalutationID"/>&nbsp;</td>
		</tr>
		<tr>
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.address'/></td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.lbContactStreet1"/></td>
		</tr>
		<tr>
			<td valign="top" align="right" class="classRowLabel">&nbsp;</td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.lbContactStreet2"/></td>
		</tr>
		<tr>
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.city'/></td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.lbContactCity"/></td>
		</tr>
		<tr>
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.postcode'/></td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.lbContactPostcode"/></td>
		</tr>	
		<tr>
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.state'/></td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.lbContactState"/></td>
		</tr>
		<tr>
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.country'/></td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.lbContactCountry"/></td>
		</tr>
		<tr>
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.directNo'/></td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.lbContactDirectNum"/>&nbsp;</td>
		</tr>
		<tr>	
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.mobileNo'/></td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.lbContactMobile"/>&nbsp;</td>
		</tr>
		<tr>
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.email'/></td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.lbContactEmail"/>&nbsp;</td>
		</tr>
		<tr>
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.remarks'/></td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.lbContactRemarks"/>&nbsp;</td>
		</tr>
	</table>
</c:when>
<c:when test="${form.type == 'Edit' or form.type == 'Add'}">
	<table cellpadding="2" cellspacing="0" class="formStyle" width="<c:out value="${form.width}"/>">
		<tr>
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.company'/></td>
            <c:choose>
                <c:when test="${form.companyID == null}" >
			        <td valign="top" class="classRow"><x:display name="${form.sel_Companies.absoluteName}"/></td>
                </c:when>
                <c:otherwise>
        			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.lbCompanyName"/></td>
                </c:otherwise>
            </c:choose>
		</tr>
		<tr>
			<td valign="top" width="150px" align="right" class="classRowLabel"><fmt:message key='sfa.message.firstName'/></td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.tf_ContactFirstName"/></td>
		</tr>
		<tr>	
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.lastName'/>&nbsp<c:out value="*"/></td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.tf_ContactLastName"/></td>
		</tr>
		<tr>
			<td valign="top" align="right" align="right" class="classRowLabel"><fmt:message key='sfa.message.designation'/></td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.tf_ContactDesignation"/></td>
		</tr>
		<tr>
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.salutation'/></td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.sel_SalutationID"/></td>
		</tr>
		<tr>
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.address'/></td>
			<td  valign="top" class="classRow"><x:display name="${form.absoluteName}.tf_ContactStreet1"/></td>
		</tr>
		<tr>	
			<td valign="top" align="right" class="classRowLabel">&nbsp;</td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.tf_ContactStreet2"/></td>
		</tr>
		<tr>
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.city'/></td>
			<td  valign="top" class="classRow"><x:display name="${form.absoluteName}.tf_ContactCity"/></td>
		</tr>
		<tr>	
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.postcode'/></td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.tf_ContactPostcode"/></td>
		</tr>
		<tr>
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.state'/></td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.tf_ContactState"/></td>
		 </tr>
		 <tr>
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.country'/></td>
			<td valign="top" class="classRow"><x:display name="${form.absoluteName}.csb_ContactCountry"/></td>
		 </tr>
		 <tr>
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.directNo'/></td>
			<td valign="top"><x:display name="${form.absoluteName}.tf_ContactDirectNum"/></td>
		 <tr>	
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.mobileNo'/></td>
			<td valign="top"><x:display name="${form.absoluteName}.tf_ContactMobile"/></td>
		</tr>
		<tr>
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.email'/></td>
			<td valign="top" class="classRowLabel"><x:display name="${form.absoluteName}.tf_ContactEmail"/></td>
		</tr>
		<tr>
			<td valign="top" align="right" class="classRowLabel"><fmt:message key='sfa.message.remarks'/></td>
			<td valign="top" class="classRowLabel"><x:display name="${form.absoluteName}.tb_ContactRemarks"/>&nbsp;</td>
		</tr>
		<tr>
			<td class="classRow"></td>
			<td class="classRow"><x:display name="${form.absoluteName}.submit"/><x:display name="${form.cancel.absoluteName}" /></td>
		</tr>
	</table>
</c:when>
</c:choose>
<jsp:include page="../form_footer.jsp" flush="true"/>
