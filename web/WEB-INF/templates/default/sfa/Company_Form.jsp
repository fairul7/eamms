<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>
<c:if test="${not empty(headerText)}">
	<c:out value="${headerText}" escapeXml="false"/>
</c:if>
<c:out value="${form.subType}" />
<c:choose>
<c:when test="${form.type == 'View'}">
	<table border="0" cellpadding="2" cellspacing="0" class="formStyle" width="<c:out value="${form.width}"/>">
		<tr>
			<td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='sfa.message.company'/>
			</td>
			<td class="classRow" valign="top" width="50%">
				<x:display name="${form.absoluteName}.lbCompanyName"/>
			</td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right">
				<fmt:message key='sfa.message.type'/>
			</td>
			<td class="classRow" valign="top">
				<x:display name="${form.absoluteName}.lbCompanyType"/>
			</td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right">
				<fmt:message key='sfa.message.address'/></td>
			<td class="classRow" valign="top">
				<x:display name="${form.absoluteName}.lbCompanyStreet1"/>
			</td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right">&nbsp;</td>
			<td class="classRow" valign="top">
				<x:display name="${form.absoluteName}.lbCompanyStreet2"/>
			</td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right"> 
				<fmt:message key='sfa.message.city'/>:
			</td>
			<td class="classRow" valign="top">
				<x:display name="${form.absoluteName}.lbCompanyCity"/>
			</td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right">
				<fmt:message key='sfa.message.postcode'/>
			</td>
			<td class="classRow" valign="top">
				<x:display name="${form.absoluteName}.lbCompanyPostcode"/>
			</td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right">
				<fmt:message key='sfa.message.state'/></td>
			<td class="classRow" valign="top">
				<x:display name="${form.absoluteName}.lbCompanyState"/></td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right">
				<fmt:message key='sfa.message.country'/></td>
			<td class="classRow" valign="top">
				<x:display name="${form.absoluteName}.lbCompanyCountry"/></td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right">
				<fmt:message key='sfa.message.telephone'/></td>
			<td class="classRow" valign="top"><x:display name="${form.absoluteName}.lbCompanyTel"/></td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right">
				<fmt:message key='sfa.message.fax'/></td>
			<td class="classRow" valign="top"><x:display name="${form.absoluteName}.lbCompanyFax"/>&nbsp;</td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right">
				<fmt:message key='sfa.message.website'/></td>
			<td class="classRow" valign="top"><x:display name="${form.absoluteName}.lbCompanyWebsite"/></td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right">
				<fmt:message key='sfa.message.partner'/></td>
			<td class="classRow" valign="top"><x:display name="${form.absoluteName}.lbIsPartner"/></td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right">
				<fmt:message key='sfa.message.partnerStatus'/></td>
			<td class="classRow" valign="top"><x:display name="${form.absoluteName}.lbPartnerStatus"/></td>
		</tr>		
	</table>
</c:when>
<c:when test="${form.type == 'Edit' or form.type == 'Add'}">
	<table border="0" cellpadding="2" cellspacing="0" class="formStyle" width="<c:out value="${form.width}"/>">
		<tr>
			<td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='sfa.message.company'/> *</td>
			<td class="classRow" valign="top" width="40%"><x:display name="${form.absoluteName}.tf_CompanyName"/></td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right">
			<fmt:message key='sfa.message.type'/></td>
			<td class="classRow" valign="top"><x:display name="${form.absoluteName}.sel_CompanyType"/></td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right">
			<fmt:message key='sfa.message.address'/></td>
			<td class="classRow" valign="top"><x:display name="${form.absoluteName}.tf_CompanyStreet1"/></td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right"> &nbsp;</td>
			<td class="classRow" valign="top"><x:display name="${form.absoluteName}.tf_CompanyStreet2"/></td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right">
			<fmt:message key='sfa.message.city'/></td>
			<td class="classRow" valign="top"><x:display name="${form.absoluteName}.tf_CompanyCity"/></td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right">
			<fmt:message key='sfa.message.postcode'/></td>
			<td class="classRow" valign="top"><x:display name="${form.absoluteName}.tf_CompanyPostcode"/></td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right">
			<fmt:message key='sfa.message.state'/></td>
			<td class="classRow" valign="top"><x:display name="${form.absoluteName}.tf_CompanyState"/></td>
		</tr>
		<tr>	
			<td class="classRowLabel" valign="top" align="right">
			<fmt:message key='sfa.message.country'/></td>
			<td class="classRow" valign="top"><x:display name="${form.absoluteName}.csb_CompanyCountry"/></td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right"><fmt:message key='sfa.message.telephone'/></td>
			<td class="classRow" valign="top"><x:display name="${form.absoluteName}.tf_CompanyTel"/></td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right"><fmt:message key='sfa.message.fax'/></td>
			<td class="classRow" valign="top"><x:display name="${form.absoluteName}.tf_CompanyFax"/></td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right"><fmt:message key='sfa.message.website'/></td>
			<td class="classRow" valign="top"><x:display name="${form.absoluteName}.tf_CompanyWebsite"/></td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right"><fmt:message key='sfa.message.partner'/></td>
			<td class="classRow" valign="top">
				<c:choose>
					<c:when test="${form.subType == 'Partner'}">
						<fmt:message key='sfa.message.yes'/>
					</c:when>
					<c:otherwise>
						<x:display name="${form.absoluteName}.rd_IsPartner_Yes"/>
						<x:display name="${form.absoluteName}.rd_IsPartner_No"/>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>	
			<td class="classRowLabel" valign="top" align="right">
			<fmt:message key='sfa.message.partnerStatus'/></td>
			<td class="classRow" valign="top">
				<x:display name="${form.absoluteName}.btngrp_PartnerType"/>
			</td>
		</tr>
		<tr>
			<td class="classRowLabel" valign="top" align="right">&nbsp;</td>
			<td class="classRow" valign="top"><x:display name="${form.absoluteName}.submit"/>
                <x:display name="${form.absoluteName}.cancel"/></td>
		</tr>
	</table>
</c:when>
</c:choose>
<jsp:include page="../form_footer.jsp" flush="true"/>
