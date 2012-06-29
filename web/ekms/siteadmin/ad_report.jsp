<%@ taglib uri="kacang.tld" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<x:permission permission="com.tms.cms.ad.ManageAds" module="com.tms.cms.ad.model.AdModule" url="noPermission.jsp" />

<html>
<head>
<title><fmt:message key='ad.label.bannerAdReport'/></title>
</head>
<body onload="focus()">

<hr>
<x:template type="com.tms.cms.ad.ui.AdReport" properties="adId=${param.adId}&click=${param.click}&month=${param.month}&year=${param.year}"/>

<hr>
[ <a href="javascript:window.close()"><fmt:message key='ad.label.close'/></a> ]
</body>
</html>