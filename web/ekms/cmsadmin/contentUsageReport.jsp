<%@ include file="/common/header.jsp" %>

<script type="text/javascript">

function checkChildren(id, cb){
	var ele = document.getElementById(id);
	
	if(cb.checked){
		
		//alert(ele.childNodes.length);
		
		for(var i=0; i<ele.childNodes.length; i++){
			
			if(ele.childNodes[i].tagName == 'INPUT'){
				
				var ele2 = ele.childNodes[i];
				ele2.checked = cb.checked;
				
			}else{
				
				var ele2 = ele.childNodes[i];
				
				if(ele2.childNodes != null && ele2.childNodes.length > 0){
					checkInputRecur(ele2, cb.checked);
				}
				
			}
			
		}
	}else{
	
		for(var i=0; i<ele.childNodes.length; i++){
			
			if(ele.childNodes[i].tagName == 'INPUT'){
				
				var ele2 = ele.childNodes[i];
				ele2.checked = cb.checked;
				
			}else{
				
				var ele2 = ele.childNodes[i];
				
				if(ele2.childNodes != null && ele2.childNodes.length > 0){
					checkInputRecur(ele2, cb.checked);
				}
				
			}
			
		}
	
	}
}

function checkInputRecur(ele, checked){
	
	for(var i=0; i<ele.childNodes.length; i++){
		
		if(ele.childNodes[i].tagName == 'INPUT'){
			var ele2 = ele.childNodes[i];
			ele2.checked = checked;
		}
		else{
			
			var ele2 = ele.childNodes[i];
			
			if(ele2.childNodes != null && ele2.childNodes.length > 0){
				checkInputRecur(ele2, checked);
			}
				
		}
	}
	
}


function generateCsv() {
	document.location = "genContentUsageReportCSV.jsp";
	return false;
}
</script>

<x:config>
    <page name="contentUsageReportPg">
        <com.tms.report.ui.ContentUsageReport name="contentUsageReport"/>
    </page>
</x:config>

<x:permission permission="com.tms.cms.AccessReports" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="com.tms.report"/> - Content Usage Report</c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

<x:display name="contentUsageReportPg.contentUsageReport"/>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>