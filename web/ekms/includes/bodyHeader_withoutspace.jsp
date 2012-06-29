<%@ include file="/common/header.jsp" %>
<c:set var="title" value="${requestScope['bodyTitle']}"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="contentBgColor">

	<%-- 
    <tr>
        <td colspan="2" valign="TOP"><img src="<c:url value="/ekms/images/blank.gif"/>" width="5" height="5"></td>
    </tr>
    --%>
    <tr valign="middle">
        <td colspan="2" height="22" class="contentTitleFont">
        <b><font class="contentTitleFont">&nbsp;
        <c:out value="${title}"/>
        </font></b>
        </td>
        <%-- 
        <td align="right" bgcolor="#003366" class="contentTitleFont">xxxxx&nbsp;</td>
        --%>
    </tr>
    <tr><td colspan="2">&nbsp;</td>
    </tr>
    <tr>
    <td colspan="2">

<%--     
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <table width="100%" cellpadding="5">
        <tr><td>
    --%>
