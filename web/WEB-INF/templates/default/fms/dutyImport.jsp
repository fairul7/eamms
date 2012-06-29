<%@ page import="java.io.*,jxl.*,jxl.write.*,kacang.Application,java.util.*"%>
<%@ include file="/common/header.jsp" %>

<c:set var="form" value="${widget}"/>


<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
  <tr>
    <td>
	  <jsp:include page="../form_header.jsp"/>
	  <table width="100%" border="0"  cellpadding="2" cellspacing="1" class="classBackground" >
        <tr>
          <td  class="classRowLabel" width="27%" height="20" valign="top" align="right"  valign="top"><fmt:message key ='fms.label.File'/> </td>
          <td class="classRow">
            <x:display name="${form.fileUpload.absoluteName}" ></x:display>
          </td>
        </tr>
                
	   	<tr>
          <td  class="classRowLabel" width="27%" height="20" valign="top" align="right"  valign="top"><fmt:message key ='fms.label.transport.sampleFile'/> </td>
          <td class="classRow">

            <a href = "/ekms/fms/Sample.xls" onClick="window.open('/ekms/fms/Sample.xls', 'check', 'scrollbars=yes,resizable=yes,width=400,height=200'); return false"><fmt:message key ='fms.label.transport.sample'/></a>
          </td>
    	</tr>
    	
    	<tr>
          <td class="classRow"></td>
          <td class="classRow">
            &nbsp;
          </td>
    	</tr>
    	
    	<tr>
          <td class="classRow"></td>
          <td class="classRow">
            <x:display name="${form.importButton.absoluteName}" /><x:display name="${form.cancelButton.absoluteName}" />
          </td>
    	</tr>
    	
        <jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
	  </table>
	</td>
  </tr>	
</table>
