<%@ page import="kacang.Application,
                 java.util.Collection,
                 java.util.HashMap,
                 com.tms.cms.taxonomy.model.TaxonomyNode"%>

<%@ include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>

<script>
function Toggle(item) {
   obj=document.getElementById(item);
   visible=(obj.style.display!="none");
   key=document.getElementById("x"+item);
   if (visible) {
     var items = document.getElementsByTagName("div");
     for (var i=0;i<items.length; i++) {
       var s = ''+items[i].id;
       if (s.length>item.length && s.substring(0,item.length)==item) {
         items[i].style.display="none";
         var k = 'x'+items[i].id;
         itemsKey = document.getElementById(k);
         if (itemsKey!=null)
            itemsKey.innerHTML="<img src='/ekms/images/treePlus.GIF' width='13' height='13' hspace='0' vspace='0' border='0'>";
       }
     }
     obj.style.display="none";
     key.innerHTML="<img src='/ekms/images/treePlus.GIF' width='13' height='13' hspace='0' vspace='0' border='0'>";
   } else {
      obj.style.display="block";
      key.innerHTML="<img src='/ekms/images/treeMinus.GIF' width='13' height='13' hspace='0' vspace='0' border='0'>";
   }
}

function ToggleTax(id){
	var obj = document.getElementById(id);
	
	if(obj != null){
		if(obj.style.display == "none"){
			obj.style.display = "block";
			var objTrigger = document.getElementById("x"+id);
			objTrigger.innerHTML = "<img src='/ekms/images/treeMinus.GIF' width='13' height='13' hspace='0' vspace='0' border='0'>";
		}else{
			obj.style.display = "none"
			var objTrigger = document.getElementById("x"+id);
			objTrigger.innerHTML = "<img src='/ekms/images/treePlus.GIF' width='13' height='13' hspace='0' vspace='0' border='0'>";
		}
	}
}

</script>

<table width="100%" cellpadding="0" cellspacing="0" >
<tr>
    <td style="vertical-align: top; font-size:10pt; font-weight: bold"><b>Please select node(s): </b></td>
</tr>
<tr>
    <td>

<%
    int i=0;
%>

<x:cache key="taxonomyTree" scope="application" time="300">

<c:forEach var="parent" items="${w.parentNode}">
    <c-rt:set var="i" value="<%=""+i%>"/>
    <table border=0 cellpadding='1' cellspacing=1>
    <tr>
        <td width='16' style="vertical-align: top; font-size:8pt">
        <c:if test="${parent.parent==true }">
        <a id="x<c:out value="${parent.taxonomyId}"/>" href="javascript:ToggleTax('<c:out value="${parent.taxonomyId}"/>');">
        <img src='/ekms/images/treePlus.GIF' width='13' height='13' hspace='0' vspace='0' border='0'>
        </a>  
        </c:if>
        <c:if test="${parent.parent==false }">
        <a id="x<c:out value="${parent.taxonomyId}"/>" href="javascript:ToggleTax('<c:out value="${parent.taxonomyId}"/>');">
        <img src='/ekms/images/treeMinus.GIF' width='13' height='13' hspace='0' vspace='0' border='0'>
        </a> 
        </c:if>
        </td>
        <td style="vertical-align: top; font-size:8pt">
            <x:display name="${w.cbNodes[i].absoluteName}"/><% i++; %>
            <b><c:out value="${parent.taxonomyName}"/></b>
        </td>
    </tr>
    </table>
    <%
        int iChild=0;
    %>
    <c:forEach var="child" items="${parent.childNodes}">
        <%  iChild++;
            if (iChild==1) {
        %>
        <div id="<c:out value="${parent.taxonomyId}"/>" style="display: none; margin-left: 2em;">
        <%
            }
        %>
        <c-rt:set var="i" value="<%=""+i%>"/>
        <table border=0 cellpadding='1' cellspacing=1>
        <tr>
            <td width='16'  style="vertical-align: top; font-size:8pt">
            <c:if test="${child.parent==true}">
                <a id="x<c:out value="${parent.taxonomyId}"/><c:out value="${child.taxonomyId}"/>" href="javascript:ToggleTax('<c:out value="${parent.taxonomyId}"/><c:out value="${child.taxonomyId}"/>');">
                <img src='/ekms/images/treePlus.GIF' width='13' height='13' hspace='0' vspace='0' border='0'>
                </a>
            </c:if>
            <c:if test="${child.parent==false}">
                <img src='/ekms/images/treeNode.GIF' width='13' height='13' hspace='0' vspace='0' border='0'>
            </c:if>
            </td>
            <td  style="vertical-align: top; font-size:8pt">
                <x:display name="${w.cbNodes[i].absoluteName}"/><% i++; %>
                <c:out value="${child.taxonomyName}"/>
            </td>
        </tr>
        </table>
    <%-- </c:forEach> --%>
    <%--
        if (iChild>0) {
    %>
    </div>
    <%
        }
    --%>
    <%-- <c:forEach var="child" items="${parent.childNodes}">--%>
        <%
            int iGrandChild=0;
        %>
        <c:forEach var="grandChild" items="${child.childNodes}">
            <%
                iGrandChild++;
                if (iGrandChild==1) {
            %>
            <div id="<c:out value="${parent.taxonomyId}"/><c:out value="${child.taxonomyId}"/>" style="display: none; margin-left: 2em;">
            <%
                }
            %>
            <c-rt:set var="i" value="<%=""+i%>"/>
            <table border=0 cellpadding='1' cellspacing=1>
            <tr>
                <td width='16'  style="vertical-align: top; font-size:8pt">
                <c:if test="${grandChild.parent==true}">
                    <a id="x<c:out value="${parent.taxonomyId}"/><c:out value="${child.taxonomyId}"/><c:out value="${grandChild.taxonomyId}"/>" href="javascript:ToggleTax('<c:out value="${parent.taxonomyId}"/><c:out value="${child.taxonomyId}"/><c:out value="${grandChild.taxonomyId}"/>');">
                    <img src='/ekms/images/treePlus.GIF' width='13' height='13' hspace='0' vspace='0' border='0'>
                    </a>
                </c:if>
                <c:if test="${grandChild.parent==false}">
                    <img src='/ekms/images/treeNode.GIF' width='13' height='13' hspace='0' vspace='0' border='0'>
                </c:if>
                </td>
                <td style="vertical-align: top; font-size:8pt">
                    <x:display name="${w.cbNodes[i].absoluteName}"/><% i++; %>
                    <c:out value="${grandChild.taxonomyName}"/>
                </td>
            </tr>
            </table>
        <%-- </c:forEach> --%>
        <%--
            if (iGrandChild>0) {
        %>
        </div>
        <%
            }
        --%>

        <%-- <c:forEach var="grandChild" items="${child.childNodes}"> --%>
            <%
                int iGrandChild1=0;
            %>
            <c:forEach var="grandChild1" items="${grandChild.childNodes}">
                <%
                    iGrandChild1++;
                    if (iGrandChild1==1) {
                %>
                <div id="<c:out value="${parent.taxonomyId}"/><c:out value="${child.taxonomyId}"/><c:out value="${grandChild.taxonomyId}"/>" style="display: none; margin-left: 2em;">
                <%
                    }
                %>
                <c-rt:set var="i" value="<%=""+i%>"/>
                <table border=0 cellpadding='1' cellspacing=1>
                <tr>
                    <td width='16'  style="vertical-align: top; font-size:8pt">
                    <c:if test="${grandChild1.parent==true}">
                        <a id="x<c:out value="${parent.taxonomyId}"/><c:out value="${child.taxonomyId}"/><c:out value="${grandChild.taxonomyId}"/><c:out value="${grandChild1.taxonomyId}"/>" href="javascript:ToggleTax('<c:out value="${parent.taxonomyId}"/><c:out value="${child.taxonomyId}"/><c:out value="${grandChild.taxonomyId}"/><c:out value="${grandChild1.taxonomyId}"/>');">
                        <img src='/ekms/images/treePlus.GIF' width='13' height='13' hspace='0' vspace='0' border='0'>
                        </a>
                    </c:if>
                    <c:if test="${grandChild1.parent==false}">
                        <img src='/ekms/images/treeNode.GIF' width='13' height='13' hspace='0' vspace='0' border='0'>
                    </c:if>
                    </td>
                    <td style="vertical-align: top; font-size:8pt">
                        <x:display name="${w.cbNodes[i].absoluteName}"/><% i++; %>
                        <c:out value="${grandChild1.taxonomyName}"/>
                    </td>
                </tr>
                </table>
            <%-- </c:forEach> --%>
            <%--
                if (iGrandChild1>0) {
            %>
            </div>
            <%
                }
            --%>

            <%-- <c:forEach var="grandChild1" items="${grandChild.childNodes}"> --%>
                <%
                    int iGrandChild2=0;
                %>
                <c:forEach var="grandChild2" items="${grandChild1.childNodes}">
                    <%
                        iGrandChild2++;
                        if (iGrandChild2==1) {
                    %>
                    <div id="<c:out value="${parent.taxonomyId}"/><c:out value="${child.taxonomyId}"/><c:out value="${grandChild.taxonomyId}"/><c:out value="${grandChild1.taxonomyId}"/>" style="display: none; margin-left: 2em;">
                    <%
                        }
                    %>
                    <c-rt:set var="i" value="<%=""+i%>"/>
                    <table border=0 cellpadding='1' cellspacing=1>
                    <tr>
                        <td width='16'  style="vertical-align: top; font-size:8pt">
                        <c:if test="${grandChild2.parent==true}">
                            <a id="x<c:out value="${parent.taxonomyId}"/><c:out value="${child.taxonomyId}"/><c:out value="${grandChild.taxonomyId}"/><c:out value="${grandChild1.taxonomyId}"/><c:out value="${grandChild2.taxonomyId}"/>" href="javascript:ToggleTax('<c:out value="${parent.taxonomyId}"/><c:out value="${child.taxonomyId}"/><c:out value="${grandChild.taxonomyId}"/><c:out value="${grandChild1.taxonomyId}"/><c:out value="${grandChild2.taxonomyId}"/>');">
                            <img src='/ekms/images/treePlus.GIF' width='13' height='13' hspace='0' vspace='0' border='0'>
                            </a>
                        </c:if>
                        <c:if test="${grandChild2.parent==false}">
                            <img src='/ekms/images/treeNode.GIF' width='13' height='13' hspace='0' vspace='0' border='0'>
                        </c:if>
                        </td>
                        <td style="vertical-align: top; font-size:8pt">
                            <x:display name="${w.cbNodes[i].absoluteName}"/><% i++; %>
                            <c:out value="${grandChild2.taxonomyName}"/>
                        </td>
                    </tr>
                    </table>
                </c:forEach>
                <%
                    if (iGrandChild2>0) {
                %>
                </div>
                <%
                    }
                %>

            </c:forEach>
            <%
                if (iGrandChild1>0) {
            %>
            </div>
            <%
                }
            %>

        </c:forEach>
        <%
            if (iGrandChild>0) {
        %>
        </div>
        <%
            }
        %>

    </c:forEach>
    <%
        if (iChild>0) {
    %>
    </div>
    <%
        }
    %>


</c:forEach>

</x:cache>

    </td>
</tr>
<tr>
    <td>
    <x:display name="${w.btnSubmit.absoluteName}"/>
    <x:display name="${w.btnCancel.absoluteName}"/>
    </td>
</tr>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>