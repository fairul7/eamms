<script type="text/javascript">
<!--
var win= null;

function NewWindow(mypage,myname,w,h,scroll){
	var winl = (screen.width-w)/2;
	var wint = (screen.height-h)/2;
	var settings ='height='+h+',';
	settings +='width='+w+',';
	settings +='top='+wint+',';
	settings +='left='+winl+',';
	settings +='scrollbars='+scroll+',';
	settings +='resizable=yes';
	win=window.open(mypage,myname,settings);
	if(parseInt(navigator.appVersion) >= 4){win.window.focus();}
}

	function MM_openBrWindow(theURL,winName,features) {
		window.open(theURL,winName,features);
	}


function show_row(noOfRow){
    // Make sure the element exists before calling it's properties
    //document.forms['evaluationPg.EvaluationForm'].elements['evaluationPg.EvaluationForm.qualitySystem'].value =num;
   if(noOfRow==1){
    if ((document.getElementById("response1") != null))
      // Toggle visibility between none and inline
      if ((document.getElementById("response1").style.display == 'inline')){
          document.getElementById("response1").style.display = 'none';
          document.getElementById("response1a").style.display = 'none';
      } else {
          document.getElementById("response1").style.display = 'inline';
          document.getElementById("response1a").style.display = 'inline';
      }
  	}else{
  		 if ((document.getElementById("response1") != null)&&(document.getElementById("response2") != null))
      // Toggle visibility between none and inline
      if ((document.getElementById("response1").style.display == 'inline')&&(document.getElementById("response2").style.display == 'inline'))
      {
          document.getElementById("response1").style.display = 'none';
          document.getElementById("response2").style.display = 'none';
          document.getElementById("response1a").style.display = 'none';
          document.getElementById("response2a").style.display = 'none';
          //document.getElementById("Called").checked = false;
          //document.getElementsByName("Called").checked=false;
      } else {
          document.getElementById("response1").style.display = 'inline';
          document.getElementById("response2").style.display = 'inline';
           document.getElementById("response1a").style.display = 'inline';
          document.getElementById("response2a").style.display = 'inline';
          //document.getElementById("Called").checked = true;
          //document.getElementsByName("Called").checked=false;
      }
  	}
  	
  }  
  
 //-->	 
</script>