<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_config_setup">
        <com.tms.hr.claim.ui.ClaimConfigDetails name="details"/>
        <tabbedpanel name="tab1" width="100%">
            <panel name="panel1" text="<fmt:message key="claims.label.assessor"/>">
          <com.tms.hr.claim.ui.ClaimConfigAssessor name="assessor2"/>
            </panel>
            <panel name="panel2" text="<fmt:message key="claims.label.mileage"/>">
          <com.tms.hr.claim.ui.ClaimConfigMileage name="mileage" />
            </panel>
            <panel name="panel3" text="<fmt:message key="claims.label.flowLogic"/>">
          <com.tms.hr.claim.ui.ClaimConfigApprovingLogic name="appLogic" />
            </panel>
        </tabbedpanel>
    </page>
</x:config>

<x:permission permission="com.tms.hr.claim.model.Admin" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" /> 

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
			<fmt:message key="claims.label.claimAdmin"/> > <fmt:message key='claims.claim.admin.setup'/> 
			</font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>

    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
    <x:display name="jsp_config_setup.details"/></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#ffffff">
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <spacer type="block" height="1"></td></tr>

    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="jsp_config_setup.tab1"></x:display>
        <%--
		<table align="left" width="500">
			
			<tr valign="top">
					<td>	
<x:display name="jsp_config_setup.assessor2"/>
					</td>
			</tr>
            </table>
    </td></tr>
            <tr>
                <td  colspan="2" bgcolor="#ffffff"><spacer type="block" height="1"></td>
            </tr>
			<tr valign="top">
					<td  colspan="2"  valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
                    <table align="left">
                    <tr><td>
<x:display name="jsp_config_setup.mileage"/>
                    </td></tr>
                    </table>
					</td>
			</tr>
            <tr>
                <td colspan="2" bgcolor="#ffffff"><spacer type="block" height="1"></td>
            </tr>
         <tr valign="top">
               <td  colspan="2"  valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
               <table align="left">
                    <tr><td>
<x:display name="jsp_config_setup.appLogic"/>
                </td></tr>
                    </table>

    --%>
	</td></tr>


    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

    </x:permission>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
