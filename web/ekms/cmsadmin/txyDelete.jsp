<%@ page import="com.tms.cms.taxonomy.model.TaxonomyModule,
				com.tms.cms.taxonomy.model.TaxonomyNode,
				kacang.Application,
				java.util.Iterator" %>

<%

	String taxonomyId = request.getParameter("taxonomyId")==null?"":request.getParameter("taxonomyId");
	String move = request.getParameter("mv")==null?"":request.getParameter("mv");
	
	if (taxonomyId!=null && !taxonomyId.equals("")) {
		TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
		TaxonomyNode node = mod.getNode(taxonomyId);
		node.setChildNodes(mod.getNodesByParent(taxonomyId,-1));
		if (move!=null && !move.equals("")) {
			if (node.getChildNodes().size()>0) {
				for (Iterator i=node.getChildNodes().iterator();i.hasNext();) {
					TaxonomyNode child = (TaxonomyNode)i.next();
					child.setParentId(node.getParentId());
					mod.updateNode(child);
				}
			}
			mod.deleteNode(node);
		}
		else {
			mod.deleteNodeRecursive(node);
		}
		if (!node.getParentId().equals("0")) {
			TaxonomyNode parent = mod.getNode(node.getParentId());
			parent.setChildNodes(mod.getNodesByParent(parent.getTaxonomyId(),-1));
			if (parent.getChildNodes().size()<=0) {
				parent.setParent(false);
				mod.updateNode(parent);
			}
		}
	}
	
	response.sendRedirect("txyForm.jsp");
%>

