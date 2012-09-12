<%@ page import="kacang.stdui.validator.Validator"%>
<%@ include file="/common/header.jsp" %>

<c:set var="selectBox" value="${widget}"/>

<input type="hidden" id="<c:out value="${selectBox.absoluteNameForJavaScript}_selectedValue"/>">
<input type="hidden" id="<c:out value="${selectBox.absoluteNameForJavaScript}_selectedName"/>">

<script>
<!--
	function <c:out value="${selectBox.absoluteNameForJavaScript}"/>submit() {
		members = document.forms['<c:out value="${selectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${selectBox.absoluteName}"/>'];
		for(c=0; c < members.length; c++) members.options[c].selected = true;
		return true;
	}
//-->
</script>

<c:if test="${selectBox.invalid}">
  !<span style="border-width:1; border-style:solid; border-color:#de123e;">
</c:if>

<select
	name="<c:out value="${selectBox.absoluteName}"/>"
	style="border-width:1pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:8pt; font-weight:normal"
	size="1"
	onBlur="<c:out value="${selectBox.onBlur}"/>"
	onChange="<c:out value="${selectBox.onChange}"/>"
	onFocus="<c:out value="${selectBox.onFocus}"/>"
	multiple
>
<c:set var="gotItem" value="false"/>
<c:forEach items="${selectBox.optionMap}" var="option">
	<c:if test="${not empty(option.key)}">
		<option value="<c:out value="${option.key}"/>"<c:if test="${selectBox.selectedOptions[option.key]}"> selected</c:if>><c:out value="${option.value}"/></option>
		<c:set var="gotItem" value="true"/>
	</c:if>
</c:forEach>
<c:if test="${not gotItem}">
	<option value="">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; </option>
</c:if>
</select>

<c:if test="${selectBox.invalid}">
  </span>
</c:if>

<input type="button" class="button" name="<c:out value="${selectBox.absoluteNameForJavaScript}_selectButton"/>" onclick="return popupSelectBoxOpen('<c:out value="${selectBox.absoluteName}"/>')" value="<fmt:message key="popupSelectBox.select"/>">
<input type="button" class="button" name="<c:out value="${selectBox.absoluteNameForJavaScript}_removeButton"/>" onclick="return <c:out value="${selectBox.absoluteNameForJavaScript}"/>_popupSelectBoxRemove()" value="<fmt:message key="popupSelectBox.unselect"/>">

<c:forEach var="child" items="${selectBox.children}">
	<% Object child = pageContext.getAttribute("child"); %>
	<% if (child instanceof Validator) { %>
		<x:display name="${child.absoluteName}"/>
	<% } %>
</c:forEach>

<script>
<!--
	function popupSelectBoxOpen(absoluteName) {
		var formCn = absoluteName + ".popupPanel.popupForm";
		window.open('<c:url value="/ekms/popupSingleSelect/popupFormWindowSingle.jsp"/>?popupSelectBoxCn=' + absoluteName + '&popupFormWindowCn=' + formCn, "popupSelectBoxWindow", "height=400,width=600,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes");
		return false;
	}
	
	function <c:out value="${selectBox.absoluteNameForJavaScript}"/>_popupSelectBoxPopulate() {
		targetBox = document.forms['<c:out value="${selectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${selectBox.absoluteName}"/>'];
				
		// get values and names
		v = document.getElementById('<c:out value="${selectBox.absoluteNameForJavaScript}_selectedValue"/>').value;
		n = document.getElementById('<c:out value="${selectBox.absoluteNameForJavaScript}_selectedName"/>').value;
		values = v.split("|||");
		names = n.split("|||");
	
		for (i = 0; i < names.length; i++) {
			if (values[i] != '' && names[i] != '') {
				// remove duplicate options
				for(c=0; c<targetBox.length; c++) {
						targetBox.options[c] = null;
				}
				// add option
				targetBox.options[targetBox.options.length] = new Option(names[i], values[i], true, true);
			}
		}
		
		//customize: so that the window is reload to go into onRequest()
		//window.location = "/ekms/orgChart/staffHierachy.jsp";
	}
	
	function <c:out value="${selectBox.absoluteNameForJavaScript}"/>_popupSelectBoxRemove() {
		var targetBox = document.forms['<c:out value="${selectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${selectBox.absoluteName}"/>'];
		
		// remove target options
		for(c=targetBox.length-1; c>=0; c--) {
			targetBox.options[c] = null;
		}
		
		// add blank option
		targetBox.options[targetBox.options.length] = new Option("", "", true, true);
	
		return false;
	}
//-->
</script>