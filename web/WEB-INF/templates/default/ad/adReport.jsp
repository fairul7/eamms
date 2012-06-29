<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:if test="${widget.click}"><fmt:message key='general.label.clickThruReport'/></c:if>
<c:if test="${!widget.click}"><fmt:message key='general.label.pageViewReport'/></c:if>
<fmt:parseDate var="monthDate" pattern="M" value="${widget.month}"/>
for <b><fmt:formatDate pattern="MMMM" value="${monthDate}" /></b> <b><c:out value="${widget.year}" /></b>.
<br>

<c:if test="${widget.errorMessage ne null}">
    <font color=red>{ AdReport: <c:out value="${widget.errorMessage}" /> }</font>
</c:if>
<c:if test="${!widget.validReport}">
    <fmt:message key='general.error.adReport'/>
</c:if>
<c:if test="${widget.errorMessage eq null && widget.validReport}">
    <form>
        <input type="hidden" name="adId" value="<c:out value="${widget.adId}" />">
        <input type="hidden" name="click" value="<c:out value="${widget.click}" />">

        <select name="month">
            <option value="1"<c:if test="${widget.month eq '1'}"> selected</c:if>>Jan</option>
            <option value="2"<c:if test="${widget.month eq '2'}"> selected</c:if>>Feb</option>
            <option value="3"<c:if test="${widget.month eq '3'}"> selected</c:if>>Mac</option>
            <option value="4"<c:if test="${widget.month eq '4'}"> selected</c:if>>Apr</option>
            <option value="5"<c:if test="${widget.month eq '5'}"> selected</c:if>>May</option>
            <option value="6"<c:if test="${widget.month eq '6'}"> selected</c:if>>Jun</option>
            <option value="7"<c:if test="${widget.month eq '7'}"> selected</c:if>>Jul</option>
            <option value="8"<c:if test="${widget.month eq '8'}"> selected</c:if>>Aug</option>
            <option value="9"<c:if test="${widget.month eq '9'}"> selected</c:if>>Sep</option>
            <option value="10"<c:if test="${widget.month eq '10'}"> selected</c:if>>Oct</option>
            <option value="11"<c:if test="${widget.month eq '11'}"> selected</c:if>>Nov</option>
            <option value="12"<c:if test="${widget.month eq '12'}"> selected</c:if>>Dec</option>
        </select>
        <select name="year">
            <c:forEach var="year" items="${widget.validYearList}">
                <option><c:out value="${year}" /></option>
            </c:forEach>
        </select>
        <input type="submit" class="button" value="<fmt:message key='general.label.updateReport'/>">
    </form>
    <hr>


    <b><fmt:message key='general.label.dailyReport'/></b>
    <table border="1" cellpadding="2" cellspacing="0" width="100%">
    <tr>
        <td width="20%"><b><fmt:message key='general.label.day'/></b></td>
        <td><b><fmt:message key='general.label.hits'/></b></td>
    </tr>
    <c:forEach var="i" items="${widget.dailyReportMap}" varStatus="k"><tr>
        <td><c:out value="${i.key}" /></td>
        <td><c:out value="${i.value}" /></td>
    </tr></c:forEach>
    </table><br>


    <b><fmt:message key='general.label.dailyUniqueReport'/></b>
    <table border="1" cellpadding="2" cellspacing="0" width="100%">
    <tr>
        <td width="20%"><b><fmt:message key='general.label.day'/></b></td>
        <td><b><fmt:message key='general.label.hits'/></b></td>
    </tr>
    <c:forEach var="i" items="${widget.dailyUniqueReportMap}" varStatus="k"><tr>
        <td><c:out value="${i.key}" /></td>
        <td><c:out value="${i.value}" /></td>
    </tr></c:forEach>
    </table><br>


    <b><fmt:message key='general.label.monthlyReport'/></b>
    <table border="1" cellpadding="2" cellspacing="0" width="100%">
    <tr>
        <td width="20%"><b><fmt:message key='general.label.month'/></b></td>
        <td><b><fmt:message key='general.label.hits'/></b></td>
    </tr>
    <c:forEach var="i" items="${widget.monthlyReportMap}" varStatus="k"><tr>
        <td><c:out value="${i.key}" /></td>
        <td><c:out value="${i.value}" /></td>
    </tr></c:forEach>
    </table><br>


    <b><fmt:message key='general.label.monthlyUniqueReport'/></b>
    <table border="1" cellpadding="2" cellspacing="0" width="100%">
    <tr>
        <td width="20%"><b><fmt:message key='general.label.month'/></b></td>
        <td><b><fmt:message key='general.label.hits'/></b></td>
    </tr>
    <c:forEach var="i" items="${widget.monthlyUniqueReportMap}" varStatus="k"><tr>
        <td><c:out value="${i.key}" /></td>
        <td><c:out value="${i.value}" /></td>
    </tr></c:forEach>
    </table><br>

</c:if>