<%@ page import="com.tms.cms.taxonomy.model.TaxonomyModule,
				com.tms.cms.taxonomy.model.TaxonomyNode,
				kacang.Application,
				java.util.Iterator" %>

<%
	String parent = request.getParameter("parentId");
	String nodeId = request.getParameter("selectedNode");
	
	TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
	TaxonomyNode node = mod.getNode(nodeId);
	node.setParentId(parent);
	
	mod.updateNode(node);
	
	response.sendRedirect("txyForm.jsp");
	
%>