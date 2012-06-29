<iframe name="assessmentAddFrame" src="../assessmentAddFrame.jsp" width="0" height="0" frameborder="0" marginheight="0"
        marginwidth="0"></iframe>
<script language="Javascript">
    <!--


        try {
            hubBox = document.forms['assessment.assessmentAddForm'].elements['assessment.assessmentAddForm.course'];
            hubBox.onchange=selectHub;

            hubBox2 = document.forms['assessment.assessmentAddForm'].elements['assessment.assessmentAddForm.module'];
            hubBox2.onchange=selectHub;

        }
        catch(e) {
        }

        function selectHub() {

            // get selected hub
            var hubId = "";
            hubBox = document.forms['assessment.assessmentAddForm'].elements['assessment.assessmentAddForm.course'];
            for(c=0; c<hubBox.length; c++) {
                if (hubBox.options[c].selected) {
                    hubId = hubBox.options[c].value;
                    break;
                }
            }


            var hubId2 = "";
            hubBox2 = document.forms['assessment.assessmentAddForm'].elements['assessment.assessmentAddForm.module'];


            for(c=0; c<hubBox2.length; c++) {

                if (hubBox2.options[c].selected) {

                        hubId2= hubBox2.options[c].value;



                    break;
                }

            }




            frame = window.frames['assessmentAddFrame'];
            frame.location.href='assessmentAddFrame.jsp?uid=' + hubId+"&uid2="+hubId2;















        }

        function loadObjects() {

            targetBox = document.forms['assessment.assessmentAddForm'].elements['assessment.assessmentAddForm.module'];
            sourceBox = window.frames['assessmentAddFrame'].document.getElementById('assessmentAddDestination');

            // get selected value
            var selected = new Array();
            var num = targetBox.length;
            idx = 0;
            for(c=0; c<num; c++) {
                if (targetBox.options[c].selected) {
                    selected[idx] = targetBox.options[c].value;
                    idx++;
                }
            }
            // clear target box
            for(c=0; c<num; c++) {
                targetBox.options[c] = null;
            }
            targetBox.length = 0;
            // copy source to target
            idx = 0;
            for(c=0; c<sourceBox.length; c++) {
                if (sourceBox.options[c].value != null) {
                    targetBox.options[idx] = new Option(sourceBox.options[c].text, sourceBox.options[c].value, false, false);

                    for(k=0; k<selected.length; k++) {
                        if (selected[k] == sourceBox.options[c].value) {
                            targetBox.options[idx].selected = true;
                            break;
                        }
                    }
                    idx++;
                }
            }







            targetBox = document.forms['assessment.assessmentAddForm'].elements['assessment.assessmentAddForm.lesson'];
            sourceBox = window.frames['assessmentAddFrame'].document.getElementById('assessmentAddDestination2');

            // get selected value
            var selected = new Array();
            var num = targetBox.length;
            idx = 0;
            for(c=0; c<num; c++) {
                if (targetBox.options[c].selected) {
                    selected[idx] = targetBox.options[c].value;
                    idx++;
                }
            }
            // clear target box
            for(c=0; c<num; c++) {
                targetBox.options[c] = null;
            }
            targetBox.length = 0;
            // copy source to target
            idx = 0;
            for(c=0; c<sourceBox.length; c++) {
                if (sourceBox.options[c].value != null) {
                    targetBox.options[idx] = new Option(sourceBox.options[c].text, sourceBox.options[c].value, false, false);

                    for(k=0; k<selected.length; k++) {
                        if (selected[k] == sourceBox.options[c].value) {
                            targetBox.options[idx].selected = true;
                            break;
                        }
                    }
                    idx++;
                }
            }








        }
    //-->
</script>
