<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="formsDraftPage">
	    <portlet name="formsDraftPortlet" text="Draft" width="100%" permanent="true">
        	<com.tms.collab.formwizard.ui.FormsDraft name="formsDraft"/>
	    </portlet>
    </page>
</x:config>

<c:if test="${!empty param.formUid}">
    <c:redirect url = "frwSubmitDraft.jsp?formUid=${param.formUid}" />
</c:if>

<c:if test="${forward.name == 'dataDrafted'}">
    <c:redirect url="frwFormsView.jsp" />
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

                  <x:display name="formsDraftPage.formsDraftPortlet" ></x:display>



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
