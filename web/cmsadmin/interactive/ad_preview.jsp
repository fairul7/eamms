<%@ taglib uri="kacang.tld" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<x:permission permission="com.tms.cms.ad.ManageAds" module="com.tms.cms.ad.model.AdModule" url="noPermission.jsp" />

<html>
<head>
<title><fmt:message key='ad.label.previewBannerAdLocation'/></title>
</head>
<body onload="focus()">

<fmt:message key='ad.label.adLocationName'/>: <b><c:out value="${param.adLocationName}" /></b>

<hr>
<form>
<fmt:message key='ad.message.copyScriptAdLocationShowUp'/>:
<textarea cols="60" rows="5">&lt;%@ taglib uri="kacang.tld" prefix="x" %&gt;
&lt;x:template type="com.tms.cms.ad.ui.DisplayAd"
    properties="adLocationName=<c:out value="${param.adLocationName}" />"/&gt;</textarea>
<br><fmt:message key='ad.message.noteTheFirstLine'/> <code>&lt;%@ taglib uri="kacang.tld" prefix="x" %&gt;<code>
<fmt:message key='ad.message.isOnlyRequiredOnce'/>
</form>

<hr>
<x:template type="com.tms.cms.ad.ui.DisplayAd" properties="adLocationName=${param.adLocationName}&preview=true"/>

<hr>
[ <a href="javascript:window.close()"><fmt:message key='ad.label.close'/></a> ]
</body>
</html>