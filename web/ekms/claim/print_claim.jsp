<%@ page import="java.math.BigDecimal,
                 com.tms.hr.claim.ui.ClaimFormPrint,
                 kacang.util.Log,
                 kacang.Application,
                 com.tms.hr.claim.model.*,
                 java.text.DecimalFormat" %>
<%@ page import="java.util.*" %>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="claimForm">
        <com.tms.hr.claim.ui.ClaimFormPrint name="print" id="${param.id}"/>
    </page>
</x:config>


<x:permission permission="com.tms.hr.claim.model.Access" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">
<x:display name="claimForm.print" body="custom">

<%
    ClaimFormItemCategoryModule catModule = (ClaimFormItemCategoryModule) Application.getInstance().getModule(ClaimFormItemCategoryModule.class);
    ClaimFormIndexModule remarkModule = (ClaimFormIndexModule)Application.getInstance().getModule(ClaimFormIndexModule.class);
    Vector colCat = new Vector(catModule.selectObjectsIgnoreDefault("status", "act", 0, -1));
    ClaimConfigModule configModule = (ClaimConfigModule) Application.getInstance().getModule(ClaimConfigModule.class);
    Collection col = configModule.findObjects(new String[]{" namespace='com.tms.hr.claim.ui.ClaimConfigMileage' "},
            (String) "id", false, 0, -1);
    String mileage = "";
    if (col != null && col.size() > 0) {
        ClaimConfig configObj = (ClaimConfig) col.iterator().next();
        mileage = configObj.getProperty1();
    }
%>
<html>
<head>
    <title><c:out value="${widget.index.remarks}" default="-n/a-"/></title>
    <style>
        .report {
            font-size: 10pt;
            font-family: Arial, Helvetica, sans-serif;
        }

        .reportSub {
            font-size: 8pt;
            font-family: Arial, Helvetica, sans-serif;
            background-color = #FFFFFF;
        }

        .reportBody {
            font-size: 7pt;
            font-family: Arial, Helvetica, sans-serif;
        }

        .reportFoot {
            font-size: 8pt;
            font-family: Arial, Helvetica, sans-serif;
            background-color = #FFFFFF;
        }

        th {
            font-size: 7pt;
            font-family: Arial, Helvetica, sans-serif;
            background-color = #CCCCCC;
        }
    </style>
</head>

<body>

<table border="0" width="95%" cellpadding="0" cellspacing="0">
<tr>
    <td class="report" height="20">
        <b><c:out value="${widget.companyName}"/></b>
    </td>
    <td rowspan="3" align="right" valign="top"><img src="/ekms/<c:out value="${widget.companyLogo}"/>"></td>
</tr>
<tr>
    <td class="report" height="20"><b>GENERAL CLAIMS FORM</b></td>
</tr>
<tr>
    <td class="report" height="20"><b>CLAIMS FOR THE MONTH OF : </b><c:out value="${widget.index.remarks}"
                                                                           default="-n/a-"/></td>
</tr>
<tr>
    <td colspan="2">&nbsp;</td>
</tr>
<tr>
<td colspan="2" bgcolor="#000000">


<!-- table for details -->
<table width="100%" cellpadding="0" cellspacing="1">
<tr>
    <td>
        <table width="100%" cellpadding="1" cellspacing="0">
            <tr>

                <td class="reportSub"><b>NAME : </b><c:out value="${widget.owner.name}"/></td>
                <!--<%--<td class="reportSub"><b>DESIGNATION : </b><c:out value="${widget.designation}" default="-n/a-"/></td>--%>-->

            <td class="reportSub"><b>DEPARTMENT : </b><c:out value="${widget.department}" default="-n/a-"/></td>
            </tr>
            <tr>

                <td class="reportSub">&nbsp;</td>
                <td class="reportSub">&nbsp;</td>
            </tr>
        </table>
    </td>
</tr>
<tr>
<td bgcolor="#000000">


<!-- item list table -->
<table width="100%" cellpadding="0" cellspacing="1">
<!-- set header -->
<tr bgColor="#FFFFFF">
    <th rowspan="2" width="60">DATE</th>
    <th rowspan="2">DESCRIPTION - CLIENT / SUPPLIERS / PLACE

            <%
                    String[] categoryList = new String[colCat.size()];
                    String[] categoryListFull = new String[colCat.size()+5];
                    double[] categoryTotal = new double[colCat.size()+4];
                    String[] categoryTotalStr = new String[colCat.size()+4];
                    double grandTotal=0.0;
                    DecimalFormat dFormat = new DecimalFormat("0.00");
                    for(int cnt1 = 0; cnt1<colCat.size(); cnt1++) {
                        ClaimFormItemCategory valObj = (ClaimFormItemCategory) colCat.get(cnt1);


                        categoryList[cnt1]=valObj.getId();   //getName is to print the title (header)

				%>
    <th rowspan="2"><%=valObj.getName()%></th>
    <%

        }
        pageContext.setAttribute("categoryList", categoryList);
        pageContext.setAttribute("toll", "travel-toll");
        pageContext.setAttribute("parking", "travel-parking");
        pageContext.setAttribute("mileage", "travel-mileage");
        pageContext.setAttribute("allowance", "travel-allowance");

    %>
    <th rowspan="2">Allowance</th>
    <th rowspan="2">Toll</th>
    <th rowspan="2">Parking</th>
    <th colspan="4">Travelling - Mileage Claim @ <%=mileage%></th>
    <th rowspan="2">Total (RM)</th>
</tr>
<tr bgColor="#FFFFFF">
    <th>From</th>
    <th>To</th>
    <th>KM</th>
    <th>RM</th>
</tr>

<%
    for (int i = 0; i < categoryList.length; i++) {
        categoryListFull[i] = categoryList[i];

    }

    categoryListFull[categoryList.length] = "travel-allowance";
    categoryListFull[categoryList.length + 1] = "travel-toll";
    categoryListFull[categoryList.length + 2] = "travel-parking";
    categoryListFull[categoryList.length + 3] = "travel-mileage";
    categoryListFull[categoryList.length + 4] = "total";


    pageContext.setAttribute("categoryListFull", categoryListFull);
     double finalGrandValue =0.0;
     Map rightTotal = new HashMap();
%>

<c:forEach var="item" items="${widget.categoryNames}">
<tr bgColor="#FFFFFF">
<td class="reportBody" align="center"><c:out value="${widget.categoryDates[item.key]}"/></td>
<c:set var="remarkid" value="${item.key}"/>
<%

    //remarkModule.remarkName((String)pageContext.getAttribute("remarkid"));
     String tempName= (String)pageContext.getAttribute("remarkid");
      int indexStop = tempName.indexOf("*>>");
      tempName = tempName.substring(0,indexStop);


%>
<td class="reportBody" align="center"><%=tempName%></td>


<c:forEach var="category" items="${categoryList}">


<c:set var="repeatagain" value="1"/>

<c:forEach var="categoryFiltered" items="${item.value}">


<c:if test="${repeatagain == 1}">

<c:if test="${categoryFiltered.key == category}">
<td class="reportBody" align="center">
    <c:set var="eachValue" value="${categoryFiltered.value}" scope="page"/>
    <%

        String tempValue= new DecimalFormat("0.00").format(Double.parseDouble((String)pageContext.getAttribute("eachValue")));
         if(tempValue.charAt(0)=='-')
         tempValue = "("+tempValue.substring(1,tempValue.length())+")";


        pageContext.setAttribute("eachValue", tempValue);

    %>
    <c:out value="${eachValue}"/>


</td>
    <c:set var="tmpValue" value="${categoryFiltered.value}" scope="page"/>
    <c:set var="tmpName" value="${item.key}" scope="page"/>


    <%
      String temp ="";
      String name ="";
      temp= (String)  pageContext.getAttribute("tmpValue");
      name= (String)  pageContext.getAttribute("tmpName");
      //on first half , name is the title, temp is the value
      if(rightTotal.get(name)  ==null)
      rightTotal.put(name,temp);
      else{
    	rightTotal.put( name, new Double(new Double((String)rightTotal.get(name)).doubleValue() + new Double(temp).doubleValue()).toString());
    	  
      }

    %>
<c:set var="repeatagain" value="0"/>   
</c:if>

</c:if>





</c:forEach>

<c:if test="${repeatagain == 1}">
<td>&nbsp;</td>
</c:if>

</c:forEach>
         <!--loop second portion-->
       <%
       double partTotal =0.0;

       %>



    <c:forEach var="item2" items="${widget.categoryNames2}">


        <c:if test="${item2.key == item.key}">
          <% double generalTotal= 0.0; %>
     <c:choose>
     <c:when test="${empty item2.value}">
        <td></td> <td></td>   <td></td>  <td></td>    <td></td><td></td><td></td>
         <% rightTotal.put( (String)pageContext.getAttribute("lastItemName"), new DecimalFormat("0.00").format(0.0));

         %>
     </c:when>
     <c:otherwise>


     <c:forEach var="categoryfiltered2"  items="${item2.value}">


       <c:if test="${categoryfiltered2.key=='travel-mileage'}">

              <c:forEach var="mileageList" items="${categoryfiltered2.value}" varStatus="status">
               <td class="reportBody" align="center"><c:choose><c:when test="${mileageList=='0'}"></c:when><c:otherwise><c:out value="${mileageList}"/></c:otherwise></c:choose></td>
                   <c:if test="${status.last}" >
                      <c:set var="lastItem" value="${mileageList}" scope="page"/>
                      <c:set var="lastItemName" value="${item.key}" scope="page"/>

                       <%
                          // rightTotal.put( (String)pageContext.getAttribute("lastItemName"), (String)pageContext.getAttribute("lastItem"));
                            generalTotal += Double.parseDouble((String) pageContext.getAttribute("lastItem"));

                       %>
                   </c:if>
                </c:forEach>


       </c:if>

        <c:if test="${categoryfiltered2.key!='travel-mileage'}">
          <td class="reportBody" align="center">




              <c:set var="otherItem" value="${categoryfiltered2.value}" scope="page"/>
          <%
                if((String)pageContext.getAttribute("otherItem") !=null)
                {
                 generalTotal += Double.parseDouble((String) pageContext.getAttribute("otherItem"));

                    pageContext.setAttribute("otherItem",new DecimalFormat("0.00").format(Double.parseDouble((String) pageContext.getAttribute("otherItem") )));

                }
                else{

                }

           //   pageContext.setAttribute("otherItem",new DecimalFormat("0.00").format(Double.parseDouble((String) pageContext.getAttribute("otherItem") )));

          %>

              <c:out value="${otherItem}"/>
          </td>
       </c:if>

     </c:forEach>
         <%

            // rightTotal.put( (String)pageContext.getAttribute("lastItemName"), new DecimalFormat("0.00").format(generalTotal));


             if(rightTotal.get((String)pageContext.getAttribute("lastItemName"))  != null)
             {
                double tempValue = 0.0;
                 tempValue = Double.parseDouble((String)rightTotal.get((String)pageContext.getAttribute("lastItemName")));
                
               	//tempValue is first half, generalTotal is 2nd half 
                tempValue += generalTotal;
                        
                 rightTotal.put( (String)pageContext.getAttribute("lastItemName"), new DecimalFormat("0.00").format(tempValue));
               

             }else{

                 rightTotal.put( (String)pageContext.getAttribute("lastItemName"), new DecimalFormat("0.00").format(generalTotal));



             }



         %>
      </c:otherwise>
        </c:choose>
   </c:if>
    </c:forEach>

        <!--do total on right-->
<%  pageContext.setAttribute("rightTotal", rightTotal);    %>
<c:forEach var="total" items="${rightTotal}" >
     <c:if test="${total.key==item.key }">
      <td class="reportBody" align="center">

          <c:set var="tmpTotal" value="${total.value}" scope="page"/>
          <%
               String tempTotal = new DecimalFormat("0.00").format(Double.parseDouble((String)pageContext.getAttribute("tmpTotal")));
               finalGrandValue += Double.parseDouble(tempTotal);
              if(tempTotal.charAt(0)=='-')
              tempTotal = "("+tempTotal.substring(1,tempTotal.length())+")";


              pageContext.setAttribute("tmpTotal",tempTotal);



          %>




<c:out value="${tmpTotal}"></c:out>

      </td>

     </c:if>

 </c:forEach>


             <%
                 %>

</c:forEach>

<tr bgColor="#FFFFFF">

    <td>&nbsp;</td><td class="reportBody" align="right"><fmt:message key="claims.label.total"/></td>




    <c:forEach var="item3" items="${categoryList}" varStatus="status">
     <td>&nbsp;
     </td>




    </c:forEach>



     <td>&nbsp;</td><td>&nbsp;</td>
     <td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td> <td>&nbsp;</td>

         <% double grandFinalTotal =0.0; String grandFinalTotalStr ="";

             grandFinalTotal = finalGrandValue;

         %>

<% grandFinalTotalStr = new DecimalFormat("0.00").format(grandFinalTotal);
    if(grandFinalTotalStr.charAt(0)=='-')
     grandFinalTotalStr = "("+grandFinalTotalStr.substring(1,grandFinalTotalStr.length())+")";%>
 <td class="reportBody" align="center"><%=grandFinalTotalStr%></td>

</tr>

     <!--looping of each item end here-->



    <% int iCounter=0;%>

    <%

                //change categoryTotal array into str with negative into ()
                for(int i=0; i< categoryTotal.length ; i++)
                {
                   String tempValue= dFormat.format(categoryTotal[i]);
                   categoryTotalStr[i] = "";


                  if(categoryTotal[i] < 0)
                  tempValue=  "("+tempValue.substring(1, tempValue.length())+")";


                  categoryTotalStr[i] = tempValue;


                }


            %>



</tr>
</table>
</td>
</tr>
</table>
</td>
</tr>
<tr>
    <td colspan="2">&nbsp;</td>
</tr>
<tr>
    <td colspan="2">
        <table width="100%" cellpadding="1" cellspacing="1">
            <tr>
                <td class="reportSub"><b>SIGNATURE OF APPLICANT:</b></td>
                <td class="reportSub"><b>APPROVAL BY DEPT.MANAGER:</b></td>
                <td class="reportSub"><b>APPROVED BY SM:</b></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td class="reportSub">DATE:</td>
                <td class="reportSub">DATE:</td>
                <td class="reportSub">DATE:</td>
            </tr>
        </table>
    </td>
</tr>
</table>
<%
    pageContext.setAttribute("now", new Date());
%>
<table border="0" width="95%" cellpadding="5" cellspacing="0">
    <tr><td class="report">
        <c:if test="${checkAmt1 ne checkAmt2}">
            <big><b><font color=red>Verification for claim total failed. DO NOT PROCESS THIS CLAIM!</font></b></big>
        </c:if>
    </td></tr>
    <tr><td class="report" align="center"><br><br><small>
        This page was generated at <fmt:formatDate value="${now}" pattern="${globalDatetimeLong}"/>.
    </small></td></tr>
</table>

</body>
</html>
</x:display>

<script>
    window.print();
</script>


</x:permission>