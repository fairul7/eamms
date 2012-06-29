<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="statusReportTablePage">
     	<portlet name="statusReportTablePortlet" text="Submitted Form Status" width="100%" permanent="true">
    		<com.tms.collab.formwizard.ui.StatusReportTable name="statusReportTable"/>
    	</portlet>
    </page>
</x:config>


<c:if test="${!empty param.formUid}">
	<c:redirect url = "frwResubmitData.jsp?id=${param.formUid}" />
</c:if>

<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>

<jsp:include page="/cmsadmin/includes/headerInteractive.jsp" flush="true" />


<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td style="vertical-align: top; width: 250px;">

        <jsp:include page="/cmsadmin/includes/sideInteractiveFW.jsp" flush="true"/>

      </td>
      <td style="vertical-align: top;">
        <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
          <tbody>
            <tr>
              <td style="vertical-align: top;"><br>


     <x:display name="statusReportTablePage.statusReportTablePortlet" ></x:display>

  <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
              </td>
            </tr>
          </tbody>
        </table>
      </td>
    </tr>
  </tbody>
</table>


