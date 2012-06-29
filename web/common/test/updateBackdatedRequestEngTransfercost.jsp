<%@ include file="/common/header.jsp" %>

<!-- Declare Widgets -->

<x:config>
    <page name="extraction">
         
    
     <portlet name="TransferCost" text="Run script to update backdated Engineering Transfer Cost" width="100%" permanent="true">
          	<com.tms.fms.abw.ui.BackdatedExtraction name="form" mode="TransferCost"/> 
     </portlet>   
    
                
    </page>
</x:config>
<c:if test="${forward.name == 'dateNotValid'}">
	<script>alert("Invalid date range !");</script>
</c:if>
<c:if test="${forward.name == 'noToday'}">
	<script>alert("`Date To` must before today!");</script>
</c:if>
 
 <!-- Display Page -->
 
 <div id="content" >
 
  <table>
  <tr><td>&nbsp;</td></tr>
 
  <tr><td valign="top"><x:display name="extraction.TransferCost"/></td>
  </tr>
  
  </table>
    
 </div>
