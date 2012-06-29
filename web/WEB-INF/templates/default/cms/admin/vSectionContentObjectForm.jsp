<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" scope="request" value="${widget}"/>
<c:set var="co" scope="request" value="${form.contentObject}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>
<table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
    <tbody>
        <tr>
            <td style="vertical-align: top; width: 150px;" class="classRowLabel" align="right"><fmt:message key='general.label.nameTitle'/>&nbsp;</td>
            <td style="vertical-align: top;"><x:display name="${form.childMap.nameField.absoluteName}"/></td>
        </tr>
        <tr><td style="vertical-align: center;" colspan="2" class="contentTitleFont" height="22">&nbsp;<fmt:message key='general.label.content'/></td></tr>
        <tr>
            <td style="vertical-align: top;" class="classRowLabel" align="right"><fmt:message key='general.label.selected'/>&nbsp;</td>
            <td style="vertical-align: top;">
                <x:display name="${form.childMap.selectBox.absoluteName}"/>
                <input type="hidden" id="selectedValue" onchange="populate()" value="<c:out value="${form.selectBoxIdsAsString}"/>">
                <input type="hidden" id="selectedName">
                <input type="button" class="button" onclick="window.open('vsection/vSectionContentObjectPopupForm.jsp?form=<c:out value="${form.rootForm.absoluteName}"/>&box=<c:out value="${form.childMap.selectBox.absoluteName}"/>','vSectionContentObjectPopupForm','status=yes,scrollbars=yes,resizable=yes,width=640,height=460');" value="<fmt:message key='general.label.selectContent'/>" />
                <input id="resetSortOrder" type="button" class="button" onclick="populate()" onfocus="populate()" value="<fmt:message key='general.label.resetSortOrder'/>"/>
                <br>
                <br>
                <br>
                <br>
                <script language="Javascript">
                    <!--
                    function populate() {
                        targetBox = document.forms['<c:out value="${form.rootForm.absoluteName}"/>'].elements['<c:out value="${form.childMap.selectBox.absoluteName}"/>'];
                        v = document.getElementById('selectedValue').value;
                        n = document.getElementById('selectedName').value;
                        values = v.split("|");
                        names = n.split("|");
                        for (i = 0; i < names.length; i++) {
                            if (values[i] != '' && names[i] != '') {
                                targetBox.options[i] = new Option(names[i], values[i], false, true);
                            }
                        }
                    }
                    //-->
                </script>
            </td>
        </tr>
        <tr>
            <td style="vertical-align: top;" class="classRowLabel" align="right"><fmt:message key='general.label.abstract'/>&nbsp;</td>
            <td style="vertical-align: top;"><x:display name="${form.childMap.summaryBox.absoluteName}"/></td>
        </tr>
        <tr><td style="vertical-align: center;" colspan="2" class="contentTitleFont" height="22">&nbsp;<fmt:message key='general.label.metaInformation'/></td></tr>
        <c:choose>
            <c:when test="${!empty co && !empty co.id}">
                <tr>
                    <td style="vertical-align: top; width: 150px;" class="classRowLabel" align="right"><fmt:message key='general.label.id'/>&nbsp;</td>
                    <td style="vertical-align: top;"><c:out value="${co.id}"/></td>
                </tr>
            </c:when>
            <c:otherwise>
                <tr>
                    <td style="vertical-align: top; width: 150px;" class="classRowLabel" align="right"><fmt:message key='general.label.id'/>&nbsp;</td>
                    <td style="vertical-align: top;"><x:display name="${form.childMap.idField.absoluteName}"/></td>
                </tr>
            </c:otherwise>
        </c:choose>
        <tr>
            <td style="vertical-align: top;" class="classRowLabel" align="right"><fmt:message key='general.label.description'/>&nbsp;</td>
            <td style="vertical-align: top;"><x:display name="${form.childMap.descriptionBox.absoluteName}"/></td>
        </tr>
        <tr><td style="vertical-align: center;" colspan="2" class="contentTitleFont" height="22">&nbsp;<fmt:message key='general.label.publication'/></td></tr>
        <tr>
            <td style="vertical-align: top;" class="classRowLabel" align="right"><fmt:message key='general.label.publishingSchedule'/>&nbsp;</td>
            <td style="vertical-align: top;">
                <table cellpadding="2">
                    <tr><td colspan="2"><x:display name="${form.childMap.scheduleCheckBox.absoluteName}"/> <fmt:message key='general.label.schedulePublishing'/></td></tr>
                    <tr>
                        <td><fmt:message key='general.label.startDate'/></td>
                        <td><x:display name="${form.childMap.startDate.absoluteName}"/></td>
                    </tr>
                    <tr>
                        <td><fmt:message key='general.label.startTime'/></td>
                        <td><x:display name="${form.childMap.startTime.absoluteName}"/></td>
                    </tr>
                    <tr><td colspan="2"><x:display name="${form.childMap.scheduleEndCheckBox.absoluteName}"/> <fmt:message key='general.label.schedulePublishingEnd'/></td></tr>
                    <tr>
                        <td><fmt:message key='general.label.endDate'/></td>
                        <td><x:display name="${form.childMap.endDate.absoluteName}"/></td>
                    </tr>
                    <tr>
                        <td><fmt:message key='general.label.endTime'/></td>
                        <td><x:display name="${form.childMap.endTime.absoluteName}"/></td>
                    </tr>
                </table>
                <p>
            </td>
        </tr>
    </tbody>
</table>
<jsp:include page="../../form_footer.jsp" flush="true"/>
