<%@ page import="com.tms.fms.facility.ui.AssignmentInformationForm"%>
<%@include file="/common/header.jsp" %>

<x:config>
	<page name="assignmentInformation">
    	<com.tms.fms.facility.ui.AssignmentInformationForm name="table" width="100%"/>
	</page>
</x:config>

<%@include file="/ekms/includes/linkCSS.jsp"%>

<script language="javascript" src="/ekms/fms/ajaxJSLibrary/mootools.svn.js" type="text/javascript"></script>

<style>
<!--
body { background-color: #000000; font-family:Arial, Helvetica, sans-serif; font-size: 12px; color:#333333; }
-->
</style>


<div id="NewsTicker">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="11"><img src="/ekms/images/fms2008/table_titlebar_L.gif" width="11" height="29" /></td>
	    <td class="titlebar"><fmt:message key='fms.label.engineering.manpowerAssignmentBoard'/></td>
	    <td width="11"><img src="/ekms/images/fms2008/table_titlebar_R.gif" width="11" height="29" /></td>
	  </tr>
	</table>
	<table width="100%" border="0" cellspacing="1" cellpadding="7">
	  <tr>
	    <td width="30%" class="subtitlebar">&nbsp;<fmt:message key='fms.request.label.requestDetails'/></td>
	    <td class="subtitlebar">&nbsp;<fmt:message key='fms.request.label.assignmentDetails'/></td>
	  </tr>
	</table>
	<div id="NewsVertical">
		<x:display name="assignmentInformation.table"/>
	</div>
<div id="myElement"></div>
</div>


<script language="javascript" type="text/javascript" src="/ekms/fms/ajaxJSLibrary/newsticker.js"></script>

<script type="text/javascript">
	
	var periodic = 1000 * <%=AssignmentInformationForm.getRefreshRate()%>;

	if (periodic <=0) {
		periodic = 600000; // default : run every 10 minutes
	}

	var page = "globalInfo.jsp";
	function ajax(url,target) {
	  // native XMLHttpRequest object
	  //document.getElementById(target).innerHTML = 'Loading...';
	  if (window.XMLHttpRequest) {
	    req = new XMLHttpRequest();
	    req.onreadystatechange = function() {ajaxDone(target);};
	    req.open("GET", url, true);
	    req.send(null);
	    // IE/Windows ActiveX version
	  } else if (window.ActiveXObject) {
	    req = new ActiveXObject("Microsoft.XMLDOM");
	    if (req) {
	      req.onreadystatechange = function() {ajaxDone(target);};
	      req.open("GET", url, true);
	      req.send(null);
	    }
	  }
	}

	var intervalGlobalInfo = 255000;  // default : run every (about) 5 minutes, 255000 is 255 seconds
	if (((periodic / 2) - 45000) >= intervalGlobalInfo) {
		intervalGlobalInfo = (periodic / 2) - 45000;
	}

	ajax(page, 'myElement');
	setInterval("ajax(page,'myElement')", intervalGlobalInfo);
	
	function ajaxDone(target) {
	  // only if req is "loaded"
	  if (req.readyState == 4) {
	    // only if "OK"
	    if (req.status == 200 || req.status == 304) {
	    	globalInfo = req.responseText;
	    	if (globalInfo != "" && globalInfo != null) {
		       document.getElementById(target).innerHTML = globalInfo;
			}
	    } else {
	      //document.getElementById(target).innerHTML="ajax error:\n" +
	      //req.statusText;
	    }
	  }
	}
	
	window.addEvent('domready', function() {
	   var hor = new Ticker('TickerVertical', {
	      speed : 4000, delay : 7000, direction : 'vertical'});

	   var myXHR = null;  // myXHR is XmlHTTPRequest object
	   var getInformation = function(){ 
			   	   
		   // set options of XmlHTTPRequest
		   // XHR is basic XMLHttpRequest Wrapper class, see file '/ekms/fms/ajaxJSLibrary/mootools.svn.js' line 5117
		   myXHR = new XHR({
			   method: 'get', 
			   onSuccess: function() {
			   
			   	   // get the value from myXHR response
			       var myString = this.response.text;
			       
			       // inject the 'myString' value to 'NewsVertical' element
			       $('NewsVertical').setHTML(myString);	  
			       
			       // restart the news-ticker
			       var hor2 = new Ticker('TickerVertical', {
			    	      speed : 4000, delay : 7000, direction : 'vertical'});
		   	   }
		   }).send('assignmentInformationForm.jsp', '');
		   
		   
	   };

	   var myAjax = null;
	   var getGlobalInfo = function() {
		   myAjax = new Ajax('globalInfo.jsp', {
			   method: 'get',
			   onSuccess: function() {
			      var globalInfo = this.response.text;

				  if (globalInfo != "" && globalInfo != null) {
					  $('myElement').setHTML(globalInfo);
				  }
		   	   }
		   }).request();
	   };	

	   getInformation.periodical(periodic);

	   //getGlobalInfo();
	   //getGlobalInfo.periodical(600000);
	});
</script>

<%@include file="/ekms/includes/footer.jsp" %>