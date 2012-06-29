<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="adUi" value="${widget}"/>
<c:if test="${adUi.showMenu}">
    <table width="100%" border="1" cellspacing="0" cellpadding="5">
        <tr>
            <td><c:out value="${adUi.title}" /></td>
        </tr>
        <tr>
            <td>
    [ <x:event name="${adUi.absoluteName}" type="listAdLocations"><fmt:message key='ad.label.listAdLocations'/></x:event> |
    <x:event name="${adUi.absoluteName}" type="listAds"><fmt:message key='ad.label.listAds'/></x:event> |
    <x:event name="${adUi.absoluteName}" type="refreshModule"><fmt:message key='ad.label.refreshModule'/></x:event> ]
            </td>
        </tr>
    </table><br>
</c:if>
<x:display name="${adUi.adLocationTable.absoluteName}" />
<x:display name="${adUi.adTable.absoluteName}" />
<x:display name="${adUi.adLocationForm.absoluteName}" />
<x:display name="${adUi.adForm.absoluteName}" />