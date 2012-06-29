<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>
    <jsp:include page="../form_header.jsp" flush="true"/>
    
    
<script type="text/javascript" src="<c:out value='${pageContext.request.contextPath}/dwr/engine.js'/>"></script>
<script type="text/javascript" src="<c:out value='${pageContext.request.contextPath}/dwr/util.js'/>"></script>
<script type="text/javascript" src="<c:out value='${pageContext.request.contextPath}/dwr/interface/WikiUtil.js'/>"></script>


	<script>
		function populateCategories() {
			var myForm = document.forms[2];
			var moduleId = myForm.elements['createArticle.form.module'].value;
			WikiUtil.getArticleCategories(fillCategories, moduleId);
		}
		function fillCategories (list) {
			var myForm = document.forms[2];
			removeAllOptions(myForm.elements['createArticle.form.category']);
			categoryId = '<c:out value="${form.categorySelected}"/>';
			for ( i=0 ; i < list.length ; i++ ) {
				var optn = document.createElement("OPTION");
				optn.text = list[i].category;
				optn.value = list[i].categoryId;
				myForm.elements['createArticle.form.category'].options.add(optn);
				if(categoryId==optn.value){					
					myForm.elements['createArticle.form.category'].options[i+1].selected = true;
				}				
			}
		}			
		
		function removeAllOptions(selectbox)
		{
			var i;
			for(i=selectbox.options.length-1;i>=1;i--)
			{
				selectbox.remove(i);
			}
		}
		function firstLoad(){		
			populateCategories();			
			
		}		
		window.onload = firstLoad;	
	</script>    
    <input value = "" name="temp" type="hidden"/>
    <table width="100%" cellpadding="2" cellspacing="1">	
        <tr>
            <td valign="center" align="right" class=lightgrey width="10%"><font class="tsText">Title *</td>
            <td valign="center" class=lightgrey><x:display name="${form.childMap.title.absoluteName}"/> </td>
        </tr>

        <tr>
            <td valign="top" align="right" class=lightgrey><font class="tsText"> Story *</td>
            <td valign="top" class=lightgrey><x:display name="${form.childMap.story.absoluteName}"/> </td>
        </tr>
        
        <tr>
            <td valign="center" align="right" width="10%"> <font class="tsText"> Module *</td>            
            <td valign="center"><x:display name="${form.childMap.module.absoluteName}"/> </td>
        </tr>      
          
        
        <tr>
            <td valign="center" align="right" width="10%"> <font class="tsText"> Category*</td>            
            <td valign="center"><x:display name="${form.childMap.category.absoluteName}"/> </td>
        </tr>      

        <tr>
            <td valign="center" align="right" width="10%"> <font class="tsText"> Tags</td>
            <td valign="center"><x:display name="${form.childMap.tags.absoluteName}"/>(note: seperate tags with comma eg: ajax, web2.0) </td>
            
        </tr>

        <tr>
            <td class="classRowLabel" valign="top">&nbsp;</td>
            <td class="classRow"> <x:display name="${form.childMap.panel.absoluteName}"/>
            </td>
        </tr>

    </table>
    
   
    
  <jsp:include page="../form_footer.jsp" flush="true"/>