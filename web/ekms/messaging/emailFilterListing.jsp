<%@include file="includes/taglib.jsp" %>
<!--DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"-->

<x:config>
	<page name="emailFilterListingPage">
		<com.tms.collab.messaging.ui.FilterListingTable name="tblExistingFilters" />
	</page>
</x:config>

<%@include file="includes/header.jsp" %>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr valign="middle" >
		<td colspan="3" height="22" bgcolor="#003366" class="contentTitleFont">
			<b><font color="#FFCF63" class="contentTitleFont">
				&nbsp;<fmt:message key='messaging.label.options'/> > <fmt:message key='messaging.label.manageFilters'/>
			</font></b>
		</td>
	<tr>
	<tr valign="middle">
		<td align="center" valign="top" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td>
		<td align="center" valign="top" bgcolor="#EFEFEF" class="contentBgColor">
			
			<table valign="top" width="97%" border="0" cellspacing="0" cellpadding="0">
				<tr><td>&nbsp;</td></tr>
				<tr valign="middle">
					<td>
						<b><fmt:message key="messaging.filtering.msg.filteringRule" /></b>
					</td>
				</tr>
			</table>
		
			<div align="center">
			<!-- here comes the content -->
			<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="0">
  				<tr valign="middle">
    				<td height="22" bgcolor="#EFEFEF" class="contentBgColor">
						<x:display name="emailFilterListingPage.tblExistingFilters" />
					</td>
  				</tr>
			</table>
			<!-- end of content -->
			</div>
		</td>
		<td align="center" valign="top" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td>
	</tr>
</table>

<%@include file="includes/footer.jsp" %>


