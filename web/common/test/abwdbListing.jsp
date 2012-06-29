<%@ include file="/common/header.jsp" %>

<!-- Declare Widgets -->

<x:config>
    <page name="abwdb">
          	<com.tms.fms.abw.ui.AbwProjectTable name="abw_project"/>         
          	<com.tms.fms.abw.ui.AbwTableContent name="abwTable"/>       
                 
    </page>
</x:config>
 <c:if test="${not empty (param.namax) }">	
<x:set name="abwdb.abwTable" property="tableName" value="${ param.namax}" />
<x:set name="abwdb.abwTable" property="sort" value="" />
<x:set name="abwdb.abwTable" property="desc" value="${ false }" />

</c:if>
 <!-- Display Page -->
 
 <div id="content" >
 
  <table>
  <tr>
	  <td valign="top"><x:display name="abwdb.abw_project"/></td>
	  <td valign="top">
	  <b><c:out value="${ param.namax}" /></b>
	  <x:display name="abwdb.abwTable"/> </td>
  </tr>
  </table>
    
 </div>
