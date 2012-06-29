<html>
<head>
<title>EKP</title>
<script>
<!--
    var xmlHttp;
    var requestURL = '<%= request.getScheme() %>://<%= request.getServerName() %>:<%= request.getServerPort() %>/toolbar/ekpstatus.jsp';
    var is_ie = (navigator.userAgent.indexOf('MSIE') >= 0) ? 1 : 0;
    var is_ie5 = (navigator.appVersion.indexOf("MSIE 5.5")!=-1) ? 1 : 0;
    var is_opera = ((navigator.userAgent.indexOf("Opera 6")!=-1)||(navigator.userAgent.indexOf("Opera/6")!=-1)) ? 1 : 0;
    //netscape, safari, mozilla behave the same???
    var is_netscape = (navigator.userAgent.indexOf('Netscape') >= 0) ? 1 : 0;

    function loadData() {
        document.getElementById("indicator").style.color = "white";

        //Create the xmlHttp object to use in the request
        //stateChangeHandler will fire when the state has changed, i.e. data is received back
        // This is non-blocking (asynchronous)
        xmlHttp = GetXmlHttpObject(stateChangeHandler);

        //Send the xmlHttp get to the specified url
        var url = requestURL + "?tmp=<%= System.currentTimeMillis() %>";
        xmlHttp_Get(xmlHttp, requestURL);
    }

    //stateChangeHandler will fire when the state has changed, i.e. data is received back
    // This is non-blocking (asynchronous)
    function stateChangeHandler() {
        //readyState of 4 or 'complete' represents that data has been returned
        if (xmlHttp.readyState == 4 || xmlHttp.readyState == 'complete'){
            //Gather the results from the callback
            var str = xmlHttp.responseText;

            //Populate the innerHTML of the div with the results
            document.getElementById('outputDiv').innerHTML = str;
        }
        setTimeout("resetIndicator()", 100);
    }

    function resetIndicator() {
        document.getElementById("indicator").style.color = "lightblue";
    }

    // XMLHttp send GET request
    function xmlHttp_Get(xmlhttp, url) {
        xmlhttp.open('GET', url, true);
        xmlhttp.send(null);
    }

    function GetXmlHttpObject(handler) {
        var objXmlHttp = null;    //Holds the local xmlHTTP object instance

        //Depending on the browser, try to create the xmlHttp object
        if (is_ie){
            //The object to create depends on version of IE
            //If it isn't ie5, then default to the Msxml2.XMLHTTP object
            var strObjName = (is_ie5) ? 'Microsoft.XMLHTTP' : 'Msxml2.XMLHTTP';

            //Attempt to create the object
            try{
                objXmlHttp = new ActiveXObject(strObjName);
                objXmlHttp.onreadystatechange = handler;
            }
            catch(e){
            //Object creation errored
                alert('IE detected, but object could not be created. Verify that active scripting and activeX controls are enabled');
                return;
            }
        }
        else if (is_opera){
            //Opera has some issues with xmlHttp object functionality
            alert('Opera detected. The page may not behave as expected.');
            return;
        }
        else{
            // Mozilla | Netscape | Safari
            objXmlHttp = new XMLHttpRequest();
            objXmlHttp.onload = handler;
            objXmlHttp.onerror = handler;
        }

        //Return the instantiated object
        return objXmlHttp;
    }
    function windowOpen(url) {
        window.open(url, "ekp", "");
        return false;
    }
//-->
</script>
<style>
<!--
body, div, a {font-family:Arial; font-size:7pt; font-weight:bold; text-align:center; text-decoration:none; color:#dd0000}
.button {font-family:Arial; font-size:6pt; font-weight:bold; text-align:center; text-decoration:none; padding-left:0;margin-left:0; line-height:10px; height:12px; width:12px}
.label {color:#222222}
//-->
</style>
</head>
<body leftmargin="0" topmargin="0" marginheight="0" marginwidth="0" bgcolor="lightblue">
    <div id="outputDiv" style="float:left; display:block; width:98%; height:20"></div>
    <div id="indicator" style="float:right; text-align:left; font-weight: bold; color:lightblue">*</div>
    <script language="javascript">
    <!--
    function reload() {
        try {
            loadData();
            window.setTimeout("reload()", 10000);
        }
        catch(e) {
        }
    }
    reload();
    //-->
    </script>
</body>
</html>
