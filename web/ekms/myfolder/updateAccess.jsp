<%@include file="/common/header.jsp"%>

<x:config>
	<page name="pg">
		<com.tms.collab.myfolder.ui.UpdateFileAccessWidget name="update"/>
	</page>
</x:config>


<c:if test="${! empty param.id}">
	<x:set name="pg.update" property="mfId" value="${param.id}"/>
</c:if>

<x:set name="pg.update" property="hidden" value="${true}"/>

<x:display name="pg.update"/>

<c:if test="${path != null}">
		<script>
			window.location =  "<c:out value='${path}'/>";
		</script>
</c:if>


