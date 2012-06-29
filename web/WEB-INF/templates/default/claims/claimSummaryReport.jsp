<%@ include file="/common/header.jsp" %>

<c:set var="w" value="${widget}"/>
<c:set var="countColumn" value="${w.countColumn}"/>


<table width="100%" cellpadding="0" cellspacing="0">
<tr>

    <td  align="right" valign="top"><img src="<c:out value="${w.companyLogo}"/>"></td>  
</tr>
<tr>

    <td><b><c:out value="${w.companyName}"/></b>
        <br><b><c:out value="${w.title}"/></b></td>
</tr>
<tr>
  <td class="tableBackground">
  <table width="100%" cellpadding="2" cellspacing="1">
  <tr>
    <td class="tableHeader" rowspan="2">
    <fmt:message key="claims.label.no"/>
    </td>
    <td class="tableHeader" rowspan="2">
    <fmt:message key="claims.category.name"/>
    </td>
    <td class="tableHeader" rowspan="2">
    <fmt:message key="claims.label.total"/>
    </td>

    <c:forEach var="typeName" items="${w.typeName}">


<c:choose>
    <c:when test="${w.countCategoriesMap[typeName.key]=='0'}">

    <td class="tableHeader" align="center" rowspan="2" valign="top" colspan="1" >

    </c:when>

    <c:otherwise>

    <td class="tableHeader" align="center"  rowspan="1" valign="top" colspan="<c:out value="${w.countCategoriesMap[typeName.key]}"/>">

    </c:otherwise>

</c:choose>
       <%-- <c:out value="${category.code}"/><br><c:out value="${category.name}"/>--%>
           <c:out value="${typeName.value}"/>
    </td>

    </c:forEach>

  </tr>
  <tr>

      <c:forEach var="typeCategories" items="${w.typeCategories}">
          <td class="tableHeader" align="center" valign="top" >
       <%-- <c:out value="${category.code}"/><br><c:out value="${category.name}"/>--%>

               <c:out value='${typeCategories.value}'/>
          </td>
          </c:forEach>




  </tr>
  <%
      int i=0;     int tempCount=0;
  %>
  <c:forEach var="employee" items="${w.employees}">
  <tr>
    <% String sI=""+i; %>
    <c-rt:set var="i" value="<%=sI%>"/>
    <% sI=""+(i+1); %>
    <td class="tableRow" align="center"><%=sI%></td>
    <td class="tableRow"><c:out value="${employee.firstName}"/> <c:out value="${employee.lastName}"/></td>
    <td class="tableRow" align="right"><c:out value="${w.expensesTotal[i]}"/></td>
    <% int tempCountEnd= tempCount+((Integer.parseInt((String)pageContext.getAttribute("countColumn")))-1);

        String tempCountStr= ""+tempCount;

        String tempCountEndStr= ""+tempCountEnd;
        pageContext.setAttribute("myAtt1", tempCountStr);
        pageContext.setAttribute("myAtt2", tempCountEndStr);
    %>
    
    <c:forEach var="list" items="${w.expenses}" begin="${myAtt1}" end="${myAtt2}"   >
        <td class="tableRow" align="right" colspan="1">
        <c:if test="${list=='0.0'}">-</c:if>
        <c:if test="${list !='0.0'}"><c:out value="${list}"/></c:if>



        </td>     <% tempCount++; %>
    </c:forEach>
   
    <% i++; %>
  </tr>
  </c:forEach>
  <tr>
    <td class="tableRow">&nbsp;</td>
    <td class="tableRow" align="right"><b><fmt:message key="claims.label.total"/></b></td>
    <td class="tableRow" align="right"><c:out value="${w.grandTotal}"/></td>
    <c:forEach var="total" items="${w.categoryTotal}">
        <td class="tableRow" align="right">
        <c:if test="${total=='0.0'}">-</c:if>
        <c:if test="${total!='0.0'}"><c:out value="${total}"/></c:if>
        </td>
    </c:forEach>
  </tr>
  </table>
  </td>
</tr>
</table>