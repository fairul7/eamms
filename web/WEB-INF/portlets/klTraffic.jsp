<%@ include file="/common/header.jsp" %>
<script>
<!--
function doView0() {
	var t, u;
	try {
	    t = document.forms['klTraffic'].cam.options[document.forms['klTraffic'].cam.selectedIndex].text;
	    u = document.forms['klTraffic'].cam.options[document.forms['klTraffic'].cam.selectedIndex].value;
	} catch(e) {
	    t = document.forms['klTraffic'].cam.options[0].text;
	    u = document.forms['klTraffic'].cam.options[0].value;
	}
    doView(t, u);
    return false;
}

function doView(t, u) {
    u = u + '?ts=' + new Date().getMilliseconds();

    n = document.getElementById('locationName');
    n.innerHTML = t;

    img = document.getElementById('locationImage');
    img.src = u;

    if (document.getElementById) {
        document.getElementById('locationHref').href = u;
    } else if (document.all) {
      document.all[id].href = u;
    }
}

function openMap1() {
	var win = window.open('http://www.itis.com.my/itis4/map_main.html', 'itis2', 'toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=1,width=1000,height=700,left = 10,top = 10');
}
-->
</script>
<form name="klTraffic" onsubmit="return false">
    <div align="center" style="font-size:9pt">
    <a id="locationHref" href="#"><img id="locationImage" src="/ekms/images/blank.gif" width="200" height="150" border=1></a>
    <b><div id="locationName">&nbsp;</div></b>
    <br>
    <select name="cam" style="font-size:9pt" onchange="doView(this.options[this.selectedIndex].text, this.options[this.selectedIndex].value)">
        <optgroup label="Federal Highway">
            <option value="http://www.itis.com.my/itis4/cctv/CCTV_C168_2.JPG">Near Angkasapuri</option>
            <option value="http://www.itis.com.my/itis4/cctv/CCTV_C125_2.JPG">Near PJ Hilton</option>
            <option value="http://www.itis.com.my/itis4/cctv/CCTV_C126_2.JPG">Near Jalan Templer</option>
            <option value="http://www.itis.com.my/itis4/cctv/CCTV_C241_2.JPG">Near Subang Airport Interchange</option>
            <option value="http://www.itis.com.my/itis4/cctv/CCTV_C247_2.JPG">Near Istana Bukit Kayangan</option>
            <option value="http://www.itis.com.my/itis4/cctv/CCTV_C255_2.JPG">Near Berkeley Roundabout</option>
        </optgroup>
        <optgroup label="KL-Seremban Highway">
            <option value="http://www.itis.com.my/itis4/cctv/CCTV_C085_2.JPG">Near Tech.  Park Malaysia (TPM)</option>
            <option value="http://www.itis.com.my/itis4/cctv/CCTV_C015_2.JPG">Near Kuchai Lama</option>
        </optgroup>
        <optgroup label="Salak Expressway">
            <option value="http://www.itis.com.my/itis4/cctv/CCTV_C166_2.JPG">Salak Expressway</option>
        </optgroup>
        <optgroup label="Jalan TAR">
            <option value="http://www.itis.com.my/itis4/cctv/CCTV_C038_2.JPG">Near Jln Raja Alang junction</option>
        </optgroup>
        <optgroup label="Jalan Kuching">
            <option value="http://www.itis.com.my/itis4/cctv/CCTV_C041_2.JPG">Near Hentian Putra</option>
            <option value="http://www.itis.com.my/itis4/cctv/CCTV_C064_2.JPG">Near Segambut roundabout</option>
        </optgroup>
        <optgroup label="MRR2">
            <option value="http://www.itis.com.my/itis4/cctv/CCTV_C050_2.JPG">Near Pandan Indah</option>
        </optgroup>
        <optgroup label="Jalan Raja Laut">
            <option value="http://www.itis.com.my/itis4/cctv/CCTV_C198_2.JPG">Near KWSP</option>
        </optgroup>
        <optgroup label="Jalan Parlimen">
            <option value="http://www.itis.com.my/itis4/cctv/CCTV_C067_2.JPG">Jalan Parlimen</option>
        </optgroup>
        <optgroup label="Jalan Mahameru">
            <option value="http://www.itis.com.my/itis4/cctv/CCTV_C194_2.JPG">Near Istana Negara</option>
        </optgroup>
    </select> <input type="button" value="View" onclick="return doView0()">
    </div>
</form>
<a href="#" onclick="openMap1();return false" class="tsBody"><img src="<c:url value="/ekms/images/todoedit.gif"/>" valign="top" border="0" width="15" height="14"> &nbsp;<b>Real-time Traffic Info</b></a>
<br><br>
<script>
<!--
    setTimeout("doView0()", 0);
-->
</script>
