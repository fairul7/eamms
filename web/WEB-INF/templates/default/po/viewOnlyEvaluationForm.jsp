<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />

<script language="JavaScript" type="text/javascript">


function initialStar(){
    
	 var qualitySystem  =	document.forms['viewEvaluationPg.ViewEvaluationForm'].elements['viewEvaluationPg.ViewEvaluationForm.qualitySystem'].value;
   	 var concern  =	document.forms['viewEvaluationPg.ViewEvaluationForm'].elements['viewEvaluationPg.ViewEvaluationForm.concern'].value;
   	 var actual  =	document.forms['viewEvaluationPg.ViewEvaluationForm'].elements['viewEvaluationPg.ViewEvaluationForm.actual'].value;
	 var negotiation  = document.forms['viewEvaluationPg.ViewEvaluationForm'].elements['viewEvaluationPg.ViewEvaluationForm.negotiation'].value;
   	 var technical  =	document.forms['viewEvaluationPg.ViewEvaluationForm'].elements['viewEvaluationPg.ViewEvaluationForm.technical'].value;
	 var history  =	document.forms['viewEvaluationPg.ViewEvaluationForm'].elements['viewEvaluationPg.ViewEvaluationForm.history'].value;
  	 var delivery  =	document.forms['viewEvaluationPg.ViewEvaluationForm'].elements['viewEvaluationPg.ViewEvaluationForm.delivery'].value;
	 var assistance  =	document.forms['viewEvaluationPg.ViewEvaluationForm'].elements['viewEvaluationPg.ViewEvaluationForm.assistance'].value;
	 
	for(var i=1; i<5; i++){
		var starNum;
		if((qualitySystem == 0 || i != qualitySystem) ||(concern == 0 || i != concern) ||(actual == 0 || i != actual)||(negotiation == 0 || i != negotiation) ||(technical == 0 || i != technical) ||(history == 0 || i != history)||(delivery == 0 || i != delivery)||(assistance == 0 || i != assistance)){
			if(i == 1)
				starNum = 'one-star';
			else if(i == 2)
				starNum = 'two-stars';
			else if(i == 3)
				starNum = 'three-stars';
			else
				starNum = 'four-stars';
		}	
		document.getElementById('qualitySystem'+i).className = starNum;
		document.getElementById('concern'+i).className = starNum;
		document.getElementById('actual'+i).className = starNum;
		document.getElementById('negotiation'+i).className = starNum;
		document.getElementById('technical'+i).className = starNum;
		document.getElementById('history'+i).className = starNum;
		document.getElementById('delivery'+i).className = starNum;
		document.getElementById('assistance'+i).className = starNum;
	}
	
	if(qualitySystem!=0){
	document.getElementById('qualitySystem'+qualitySystem).className = 'select'+qualitySystem;
	}
	
	if( concern!=0 ){
		document.getElementById('concern'+concern).className = 'select'+concern;
	}

	if( actual!=0 ){
		document.getElementById('actual'+actual).className = 'select'+actual;
	} 
	
	if( negotiation!=0 ){
		document.getElementById('negotiation'+negotiation).className = 'select'+negotiation;
	} 
	
	if( technical!=0 ){
		document.getElementById('technical'+technical).className = 'select'+technical;
	}
	
	if( history!=0 ){
	  document.getElementById('history'+history).className = 'select'+history;
	}
	
	if( delivery!=0 ){
		document.getElementById('delivery'+delivery).className = 'select'+delivery;
	}
	
	if(assistance!=0){
		document.getElementById('assistance'+assistance).className = 'select'+assistance;
	}
	
}

</script>

<body onload="initialStar()"></body>
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
	<td>
		<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
		    <tr>
		        <td>
					<table width="100%" cellpadding="3" cellspacing="1">
		                <jsp:include page="../form_header.jsp" flush="true"/>
		                <tr>
		                    <td height="22" class="contentTitleFont" colspan="2">
								<fmt:message key="po.label.prePurchase"/> > <fmt:message key="evaluation.label.title"/>
							</td>
		                </tr>
		   				<tr>
		                    <td  width="20%" class="classRowLabel" valign="top" align="right">
		                    	<fmt:message key='supplier.label.supp'/>
		                    </td>
		                    <td class="classRow">
		                    	<x:display name="${w.txtSupplier.absoluteName}" />  
	                    	</td>
	               		</tr>
	               		<tr>
		                    <td class="classRowLabel" valign="top" align="right">
		                    	<fmt:message key='supplier.label.company'/> 
		                    </td>
		                    <td class="classRow">
		                    	<x:display name="${w.txtSupplierCompany.absoluteName}" />
	                    	</td>
	               		</tr>
	               		<tr>
		                    <td class="classRowLabel" valign="top" align="right">
		                    	<fmt:message key='evaluation.label.lastEvaluated'/>
		                    </td>
		                    <td class="classRow">
		                    	<x:display name="${w.txtLastEvaluatedBy.absoluteName}" />
	                    	</td>
	               		</tr>
	               		<tr>
		                    <td class="classRowLabel" valign="top" align="right">
		                    	<fmt:message key='evalutation.label.lastEvaluatedDate'/>
		                    </td>
		                    <td class="classRow">
		                    	<x:display name="${w.txtLastEvaluateDate.absoluteName}" /> 
	                    	</td>
	               		</tr>
                		
					</table>
       	 		</td>
			</tr>
		</table>
	</td>
</tr>
</table>

<p></p>

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
	<td>
		<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
		    <tr>
		        <td>
				<table width="100%" cellpadding="3" cellspacing="1">
	             
	                <tr>
	                    <td height="22" class="contentTitleFont">
							<fmt:message key="evaluation.label.quality"/>
						</td>
						<td class="contentTitleFont" width="80%">
							<fmt:message key="evaluation.label.rating"/>
						</td>
	                </tr>
		  			 <tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='evaluation.label.qualitySystem'/> 
	                    </td>
	                    <td class="classRow">
	                    	<ul class='star-rating'>
				  				<li><a id="qualitySystem1" title='Rate this 1 star out of 4'  class='one-star'>1</a></li>
								<li><a id="qualitySystem2" title='Rate this 2 stars out of 4' class='two-stars'>2</a></li>
								<li><a id="qualitySystem3" title='Rate this 3 stars out of 4' class='three-stars'>3</a></li>
								<li><a id="qualitySystem4" title='Rate this 4 stars out of 4' class='four-stars'>4</a></li>
								<x:display name="${w.qualitySystem.absoluteName}" />
			 				</ul>
	                    </td>
	                </tr>
	                 <tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='evaluation.label.concern'/> 
	                    </td>
	                    <td class="classRow">
	                    	 <ul class='star-rating'>
				  				<li><a id="concern1" title='Rate this 1 star out of 4'  class='one-star'>1</a></li>
								<li><a id="concern2" title='Rate this 2 stars out of 4' class='two-stars'>2</a></li>
								<li><a id="concern3" title='Rate this 3 stars out of 4' class='three-stars'>3</a></li>
								<li><a id="concern4" title='Rate this 4 stars out of 4' class='four-stars'>4</a></li>
							   	<x:display name="${w.concern.absoluteName}" />
							 </ul>   
	                    </td>
	                </tr>
	                 <tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='evaluation.label.history'/> 
	                    </td>
	                    <td class="classRow">
	                    	 <ul class='star-rating'>
				  				<li><a id="history1" title='Rate this 1 star out of 4' class='one-star'>1</a></li>
								<li><a id="history2" title='Rate this 2 stars out of 4' class='two-stars'>2</a></li>
								<li><a id="history3" title='Rate this 3 stars out of 4' class='three-stars'>3</a></li>
								<li><a id="history4" title='Rate this 4 stars out of 4' class='four-stars'>4</a></li>
							   	<x:display name="${w.history.absoluteName}" />
							</ul> 
	                    </td>
	                </tr>
	               
				</table>
       	 	</td>
		</tr>
		</table>
	</td>
</tr>
</table>

<p></p>

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
	<td>
		<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
	    <tr>
	        <td>
	        	<table width="100%" cellpadding="3" cellspacing="1">
	               
	                <tr>
	                    <td height="22" class="contentTitleFont">
							<fmt:message key="evaluation.label.price"/>
						</td>
						<td class="contentTitleFont" width="80%">
							<fmt:message key="evaluation.label.rating"/>
						</td>
	                </tr>
		   			<tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='evaluation.label.actual'/> 
	                    </td>
	                    <td class="classRow">
	                    	 <ul class='star-rating'>
								<li><a id="actual1" title='Rate this 1 star out of 4' class='one-star'>1</a></li>
								<li><a id="actual2" title='Rate this 2 stars out of 4' class='two-stars'>2</a></li>
								<li><a id="actual3" title='Rate this 3 stars out of 4' class='three-stars'>3</a></li>
								<li><a id="actual4" title='Rate this 4 stars out of 4' class='four-stars'>4</a></li>
							   	<x:display name="${w.actual.absoluteName}" />
							</ul>     
	                    </td>
	                </tr>
	                <tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	 <fmt:message key='evaluation.label.negotiation'/> 
	                    </td>
	                    <td class="classRow">
	                    	<ul class='star-rating'>
				  				<li><a id="negotiation1" title='Rate this 1 star out of 4' class='one-star'>1</a></li>
								<li><a id="negotiation2" title='Rate this 2 stars out of 4' class='two-stars'>2</a></li>
								<li><a id="negotiation3" title='Rate this 3 stars out of 4' class='three-stars'>3</a></li>
								<li><a id="negotiation4" title='Rate this 4 stars out of 4' class='four-stars'>4</a></li>
						    	<x:display name="${w.negotiation.absoluteName}" />
						   </ul>    
	                    </td>
	                </tr>
	              
				</table>
        	</td>
		</tr>
		</table>
	</td>
</tr>
</table>

<p></p>

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
	<td>
		<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
	    <tr>
	        <td>
	        	<table width="100%" cellpadding="3" cellspacing="1">
	             
	                <tr>
	                    <td height="22" class="contentTitleFont">
							<fmt:message key="evaluation.label.performance"/>
						</td>
						<td class="contentTitleFont" width="80%">
							<fmt:message key="evaluation.label.rating"/>
						</td>
	                </tr>
		   			<tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='evaluation.label.technical'/> 
	                    </td>
	                    <td class="classRow">
				            <ul class='star-rating'>
				  				<li><a id="technical1" title='Rate this 1 star out of 4' class='one-star'>1</a></li>
								<li><a id="technical2" title='Rate this 2 stars out of 4' class='two-stars'>2</a></li>
								<li><a id="technical3" title='Rate this 3 stars out of 4' class='three-stars'>3</a></li>
								<li><a id="technical4" title='Rate this 4 stars out of 4' class='four-stars'>4</a></li>
						   		<x:display name="${w.technical.absoluteName}" />
							</ul>     
	                    </td>
	                </tr>
	                <tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	 <fmt:message key='evaluation.label.delivery'/> 
	                    </td>
	                    <td class="classRow">
	                    	<ul class='star-rating'>
								<li><a id="delivery1" title='Rate this 1 star out of 4' class='one-star'>1</a></li>
								<li><a id="delivery2" title='Rate this 2 stars out of 4' class='two-stars'>2</a></li>
								<li><a id="delivery3" title='Rate this 3 stars out of 4' class='three-stars'>3</a></li>
								<li><a id="delivery4" title='Rate this 4 stars out of 4' class='four-stars'>4</a></li>
						    	<x:display name="${w.delivery.absoluteName}" />
						   	</ul>  
				        </td>
	                </tr>
	                <tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	 <fmt:message key='evaluation.label.assistance'/> 
	                    </td>
	                    <td class="classRow">
				            <ul class='star-rating'>
				  				<li><a id="assistance1" title='Rate this 1 star out of 4' class='one-star'>1</a></li>
								<li><a id="assistance2" title='Rate this 2 stars out of 4' class='two-stars'>2</a></li>
								<li><a id="assistance3" title='Rate this 3 stars out of 4' class='three-stars'>3</a></li>
								<li><a id="assistance4" title='Rate this 4 stars out of 4' class='four-stars'>4</a></li>
						    	<x:display name="${w.assistance.absoluteName}" />
						   </ul> 
						</td>
		             </tr>
		             <tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	
	                    </td>
	                    <td class="classRow">
							<x:display name="${w.buttonPanel.absoluteName}" /> 
						</td>
		             </tr>
	                <jsp:include page="../form_footer.jsp" flush="true"/>
				</table>
        	</td>
		</tr>
		</table>
	</td>
</tr>
</table>