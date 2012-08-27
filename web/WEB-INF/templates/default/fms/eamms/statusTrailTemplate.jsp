<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="widget" value="${widget}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont">
            <b><font color="#FFCF63" class="contentTitleFont">
                <fmt:message key="eamms.feed.list.msg.statusTrail"/>
            </font></b>
        </td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
            <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10">
        </td>
    </tr>
    <tr><td colspan="2" valign="TOP" align="CENTER" bgcolor="#EFEFEF" class="contentBgColor">

        <table>
            <c:forEach items="${widget.statusTrailCol}" var="obj">
            <tr>
                <td>
                    <b><c:out value="${obj.status}"/></b>,&nbsp; 
                    <fmt:message key="eamms.feed.list.msg.on"/>&nbsp; 
                    <fmt:formatDate value="${obj.createdDate}" pattern="dd MMM yyyy hh:mm aaa"/>&nbsp; 
                    <fmt:message key="eamms.feed.list.msg.by"/>&nbsp; 
                    <c:out value="${obj.createdBy}"/>
                </td>
            </tr>
            </c:forEach>
        </table>
    
    </td></tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
            <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15">
        </td>
    </tr>
</table>

<jsp:include page="../../form_footer.jsp" flush="true"/>
