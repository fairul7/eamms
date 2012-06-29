<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.SystemSettings" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
    <page name="systemSettings">
        <portlet name="systemSettingsPortlet" text="<fmt:message key='siteadmin.label.systemSettings'/>" width="100%" permanent="true">
            <panel name="systemSettingsPanel" />
            <com.tms.ekms.setup.ui.SetupForm name="setupForm" />
        </portlet>
    </page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key='general.label.systemAdministration'/> > <fmt:message key="siteadmin.label.systemSettings"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

                <x:display name="systemSettings.systemSettingsPortlet.systemSettingsPanel" body="custom">



<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
	<tr>
		<td>
                        <jsp:include page="../form_header.jsp" flush="true"></jsp:include>
			<table width="100%" cellpadding="3" cellspacing="1">




                    <tr>
                      <td class="classRowLabel" style="vertical-align: top;" width="200" align="right"><fmt:message key='siteadmin.label.jvmMemoryAvailable'/><br>
                      </td>
                      <td class="classRow" style="vertical-align: top; text-align:left">
                        <%= (Runtime.getRuntime().freeMemory() / 1000) %> / <%= (Runtime.getRuntime().totalMemory() / 1000) %> K
                      </td>
                    </tr>

					<tr>
						<td class="classRow" colspan="2">&nbsp;</td>
					</tr>

                    <tr>
                      <td style="vertical-align: top;" colspan="2" class="contentTitleFont">
                        <%-- span style="font-family:Arial; font-size:16px; font-weight:bold" --%>
                        <fmt:message key='siteadmin.label.systemProperties'/>
                        <%-- /span --%>
                      </td>
                    </tr>

                    <tr>
                      <td class="classRow" style="vertical-align: top;" colspan="2">
                        <x:display name="systemSettings.systemSettingsPortlet.setupForm" />
                      </td>
                    </tr>

                    <tr>
                      <td class="classRow" style="vertical-align: top;" colspan="2">
                      <hr size="1">
                      </td>
                    </tr>

                    <tr>
                      <td class="classRowLabel" style="vertical-align: top;" align="right">
                      	<fmt:message key='siteadmin.label.siteCache'/><br>
                      </td>
                      <td class="classRow" style="vertical-align: top;">
                        <input type="button" class="button" onclick="location.href='systemSettingsClearCache.jsp?clearCache=true'" value="<fmt:message key='siteadmin.label.clearCache'/>">
                      </td>
                    </tr>
					
					<script>
                    	function optimizeIndexConfirm(){
                    		if(confirm("<fmt:message key='siteadmin.message.optimizeIndex'/>"))
                    			location.href='systemSettingsOptimizeIndex.jsp?optimizeIndex=true';
                    		else
                    			return false;
                    	}
                    </script>
					
                    <tr>
                      <td class="classRowLabel" align="right" style="vertical-align: top;"><fmt:message key='siteadmin.label.searchIndex'/><br>
                      </td>
                      <td class="classRow" style="vertical-align: top;">
                        <input type="button" class="button" onclick="optimizeIndexConfirm()" value="<fmt:message key='siteadmin.label.optimizeIndex'/>">
                        <input type="button" class="button" onclick="location.href='reindexContent.jsp'" value="<fmt:message key='siteadmin.label.reindex'/>">
                      </td>
                    </tr>
                    
                    <script>
                    	function clearQuartzConfirm(){
                    		if(confirm("<fmt:message key='siteadmin.message.clearQuartzTables'/>"))
                    			location.href='clearQuartz.jsp?clear=true';
                    		else
                    			return false;
                    	}
                    </script>
                    
                    <tr>
                      <td class="classRowLabel" align="right" style="vertical-align: top;"><fmt:message key='siteadmin.label.clearQuartzTables'/><br>
                      </td>
                      <td class="classRow" style="vertical-align: top;">
                      	<input type="button" class="button" onclick="return clearQuartzConfirm()" value="<fmt:message key='siteadmin.label.clearTables'/>"> &nbsp;&nbsp;(<fmt:message key='siteadmin.label.clearQuartzTablesNote'/>)
                      </td>
                    </tr>



<jsp:include flush="true" page="../form_footer.jsp"></jsp:include>
			</table>
			
		</td>
	</tr>
</table>

</td>
</tr>	
</table>


                </x:display>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>
