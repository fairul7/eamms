<%@ include file="/common/header.jsp" %>


<x:config>
  <page name="fms_requestEditPage">
    <com.tms.fms.engineering.ui.RequestForm name="modify" width="100%"/>
  </page>
</x:config>

<c:set var="type" value="Modify"/>
<c:choose>
  <c:when test="${not empty(param.requestId)}">
    <c:set var="requestId" value="${param.requestId}"/>
  </c:when>
  <c:otherwise>
    <c:set var="requestId" value="${widgets['fms_requestEditPage.modify'].requestId}"/>
  </c:otherwise>
</c:choose>
<x:set name="fms_requestEditPage.modify" property="requestId" value="${requestId}"/>
<x:set name="fms_requestEditPage.modify" property="type" value="${type}"/>

<c:choose>
  <c:when test="${forward.name == 'NoServices'}">
    <script>alert('<fmt:message key="fms.facility.msg.needToSelectServices"/>');</script>
  </c:when>
  <c:when test="${forward.name == 'ADDED'}">
    <script>alert('<fmt:message key="fms.facility.msg.requestUpdated"/>'); 
    document.location = "<c:url value="requestListing.jsp"/>";</script>
  </c:when>
  <c:when test="${forward.name == 'EXISTS'}">
    <script>alert('<fmt:message key="fms.facility.msg.requestExists"/>');</script>
  </c:when>
  <c:when test="${forward.name == 'FAILED'}">
    <script>alert('<fmt:message key="fms.facility.msg.fms.facility.msg.requestNotUpdate"/>');</script>
  </c:when>
  <c:when test="${forward.name == 'INVALID-DATE-FROM'}">
    <script>alert('<fmt:message key="fms.facility.msg.dateRequiredFromToInvalid"/>');</script>
  </c:when>
  <c:when test="${forward.name == 'INVALID-DATE-TO'}">
    <script>alert('<fmt:message key="fms.facility.msg.dateRequiredFromToInvalid"/>');</script>
  </c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.request.label.submitRequest"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="fms_requestEditPage.modify"/>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>

<script>


function get_check_value(object1)
{
var c_value = new Array();
var d_value = "";
var nn = document.getElementsByName("fms_requestEditPage.modify.services");
//alert(object1);
//alert(fms_requestEditPage.modify.services[0]);
var count1=0;
for (var i=0; i < nn.length; i++)
   {
   if (nn[i].checked)
      {
	  // alert(nn[i]);
	  if(nn[i].value== "fms_requestEditPage.modify.services_1")
		   d_value = "SCP/MCP/OB/SNG/VSAT";
	  if(nn[i].value== "fms_requestEditPage.modify.services_2")
		   d_value = "Post Production/NLE";
	  if(nn[i].value== "fms_requestEditPage.modify.services_3")
		   d_value = "VTR/MCR";
	  if(nn[i].value== "fms_requestEditPage.modify.services_4")
		   d_value = "Manpower";
	  if(nn[i].value== "fms_requestEditPage.modify.services_5")
		   d_value = "Studio";
	  if(nn[i].value== "fms_requestEditPage.modify.services_6")
		   d_value = "Other Facilities Equipment (Camera,audio,lighting,etc)";
	  if(nn[i].value== "fms_requestEditPage.modify.services_7")
		   d_value = "TVRO/Downlink";

		c_value[count1] = d_value;
		count1++;
	  //c_value = c_value + d_value + "\n";
	      //c_value = c_value + nn[i].value + "\n";
      }
   }
	var currentTagTokens = object1.split( "#" );
	var counter=0;
	var exist=false;
	var arrModifiedServ = new Array();
	var modifiedServ ="";
	for (var o=0; o < currentTagTokens.length -1; o++){
		exist=false;
		for (var p=0; p < c_value.length; p++){
			if(currentTagTokens[o]==c_value[p]){
				exist=true;
			}
		}
		if(!exist){
			modifiedServ = modifiedServ +"- "+currentTagTokens[o]+"\n";
			//alert(currentTagTokens[o]);//exist=true;
			//arrModifiedServ[counter]=currentTagTokens[o];
		}
			
	}
	//alert(currentTagTokens[0]);
	
	//c_value = "You selected the following:\n" + c_value;
	//alert(c_value);
	if(modifiedServ!="")
		return confirm("You modified the following service(s):\n" + modifiedServ +"\nModifying previously checked service\nwill cause assignment to be deleted \nDo you wish to proceed?");
	else
		return true;
	//return false;

}


</script>

