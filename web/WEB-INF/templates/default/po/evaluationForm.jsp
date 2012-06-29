<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />

<script language="JavaScript" type="text/javascript">

function starSelected(id, num){
	for(var i=1; i<5; i++){
		var starNum;
		if(i == 1)
			starNum = 'one-star';
		else if(i == 2)
			starNum = 'two-stars';
		else if(i == 3)
			starNum = 'three-stars';
		else
			starNum = 'four-stars';
			
		var ele2 = document.getElementById(id+i);
		ele2.className = starNum;
	}
	
	var ele = document.getElementById(id+num);
	ele.className = 'select'+num;
	if(id == 'qualitySystem')
		document.forms['evaluationPg.EvaluationForm'].elements['evaluationPg.EvaluationForm.qualitySystem'].value =num;
    else if(id == 'concern')
		document.forms['evaluationPg.EvaluationForm'].elements['evaluationPg.EvaluationForm.concern'].value =num;
    else if(id == 'actual')
		document.forms['evaluationPg.EvaluationForm'].elements['evaluationPg.EvaluationForm.actual'].value =num;
	else if(id == 'negotiation')
		document.forms['evaluationPg.EvaluationForm'].elements['evaluationPg.EvaluationForm.negotiation'].value =num;
    else if(id == 'technical')
		document.forms['evaluationPg.EvaluationForm'].elements['evaluationPg.EvaluationForm.technical'].value =num;
	else if(id == 'history')
		document.forms['evaluationPg.EvaluationForm'].elements['evaluationPg.EvaluationForm.history'].value =num;
    else if(id == 'delivery')
		document.forms['evaluationPg.EvaluationForm'].elements['evaluationPg.EvaluationForm.delivery'].value =num;
	else if(id == 'assistance')
		document.forms['evaluationPg.EvaluationForm'].elements['evaluationPg.EvaluationForm.assistance'].value =num;
    
}

function initialStar(){
    
	 var qualitySystem  =	document.forms['evaluationPg.EvaluationForm'].elements['evaluationPg.EvaluationForm.qualitySystem'].value;
   	 var concern  =	document.forms['evaluationPg.EvaluationForm'].elements['evaluationPg.EvaluationForm.concern'].value;
   	 var actual  =	document.forms['evaluationPg.EvaluationForm'].elements['evaluationPg.EvaluationForm.actual'].value;
	 var negotiation  = document.forms['evaluationPg.EvaluationForm'].elements['evaluationPg.EvaluationForm.negotiation'].value;
   	 var technical  =	document.forms['evaluationPg.EvaluationForm'].elements['evaluationPg.EvaluationForm.technical'].value;
	 var history  =	document.forms['evaluationPg.EvaluationForm'].elements['evaluationPg.EvaluationForm.history'].value;
  	 var delivery  =	document.forms['evaluationPg.EvaluationForm'].elements['evaluationPg.EvaluationForm.delivery'].value;
	 var assistance  =	document.forms['evaluationPg.EvaluationForm'].elements['evaluationPg.EvaluationForm.assistance'].value;
	 
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
		                   <td width="20%" height="22" class="contentTitleFont">
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
					  				<li><a href='#' id="qualitySystem1" onClick="starSelected('qualitySystem', 1);" title='Rate this 1 star out of 4' class='one-star'>1</a></li>
									<li><a href='#' id="qualitySystem2" onClick="starSelected('qualitySystem', 2);" title='Rate this 2 stars out of 4' class='two-stars'>2</a></li>
									<li><a href='#' id="qualitySystem3" onClick="starSelected('qualitySystem', 3);" title='Rate this 3 stars out of 4' class='three-stars'>3</a></li>
									<li><a href='#' id="qualitySystem4" onClick="starSelected('qualitySystem', 4);" title='Rate this 4 stars out of 4' class='four-stars'>4</a></li>
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
					  				<li><a href='#' id="concern1" onClick="starSelected('concern', 1);" title='Rate this 1 star out of 4' class='one-star'>1</a></li>
									<li><a href='#' id="concern2" onClick="starSelected('concern', 2);" title='Rate this 2 stars out of 4' class='two-stars'>2</a></li>
									<li><a href='#' id="concern3" onClick="starSelected('concern', 3);" title='Rate this 3 stars out of 4' class='three-stars'>3</a></li>
									<li><a href='#' id="concern4" onClick="starSelected('concern', 4);" title='Rate this 4 stars out of 4' class='four-stars'>4</a></li>
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
					  				<li><a href='#' id="history1" onClick="starSelected('history', 1);" title='Rate this 1 star out of 4' class='one-star'>1</a></li>
									<li><a href='#' id="history2" onClick="starSelected('history', 2);" title='Rate this 2 stars out of 4' class='two-stars'>2</a></li>
									<li><a href='#' id="history3" onClick="starSelected('history', 3);" title='Rate this 3 stars out of 4' class='three-stars'>3</a></li>
									<li><a href='#' id="history4" onClick="starSelected('history', 4);" title='Rate this 4 stars out of 4' class='four-stars'>4</a></li>
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
									<li><a href='#' id="actual1" onClick="starSelected('actual', 1);" title='Rate this 1 star out of 4' class='one-star'>1</a></li>
									<li><a href='#' id="actual2" onClick="starSelected('actual', 2);" title='Rate this 2 stars out of 4' class='two-stars'>2</a></li>
										<li><a href='#' id="actual3" onClick="starSelected('actual', 3);" title='Rate this 3 stars out of 4' class='three-stars'>3</a></li>
									<li><a href='#' id="actual4" onClick="starSelected('actual', 4);" title='Rate this 4 stars out of 4' class='four-stars'>4</a></li>
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
					  				<li><a href='#' id="negotiation1" onClick="starSelected('negotiation', 1);" title='Rate this 1 star out of 4' class='one-star'>1</a></li>
									<li><a href='#' id="negotiation2" onClick="starSelected('negotiation', 2);" title='Rate this 2 stars out of 4' class='two-stars'>2</a></li>
									<li><a href='#' id="negotiation3" onClick="starSelected('negotiation', 3);" title='Rate this 3 stars out of 4' class='three-stars'>3</a></li>
									<li><a href='#' id="negotiation4" onClick="starSelected('negotiation', 4);" title='Rate this 4 stars out of 4' class='four-stars'>4</a></li>
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
					  				<li><a href='#' id="technical1" onClick="starSelected('technical', 1);" title='Rate this 1 star out of 4' class='one-star'>1</a></li>
									<li><a href='#' id="technical2" onClick="starSelected('technical', 2);" title='Rate this 2 stars out of 4' class='two-stars'>2</a></li>
									<li><a href='#' id="technical3" onClick="starSelected('technical', 3);" title='Rate this 3 stars out of 4' class='three-stars'>3</a></li>
									<li><a href='#' id="technical4" onClick="starSelected('technical', 4);" title='Rate this 4 stars out of 4' class='four-stars'>4</a></li>
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
									<li><a href='#' id="delivery1" onClick="starSelected('delivery', 1);" title='Rate this 1 star out of 4' class='one-star'>1</a></li>
									<li><a href='#' id="delivery2" onClick="starSelected('delivery', 2);" title='Rate this 2 stars out of 4' class='two-stars'>2</a></li>
									<li><a href='#' id="delivery3" onClick="starSelected('delivery', 3);" title='Rate this 3 stars out of 4' class='three-stars'>3</a></li>
									<li><a href='#' id="delivery4" onClick="starSelected('delivery', 4);" title='Rate this 4 stars out of 4' class='four-stars'>4</a></li>
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
					  				<li><a href='#' id="assistance1" onClick="starSelected('assistance', 1);" title='Rate this 1 star out of 4' class='one-star'>1</a></li>
									<li><a href='#' id="assistance2" onClick="starSelected('assistance', 2);" title='Rate this 2 stars out of 4' class='two-stars'>2</a></li>
									<li><a href='#' id="assistance3" onClick="starSelected('assistance', 3);" title='Rate this 3 stars out of 4' class='three-stars'>3</a></li>
									<li><a href='#' id="assistance4" onClick="starSelected('assistance', 4);" title='Rate this 4 stars out of 4' class='four-stars'>4</a></li>
							    	<x:display name="${w.assistance.absoluteName}" />
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
		                	
					        <td class="contentBgColor" width="20%" valign="top">
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