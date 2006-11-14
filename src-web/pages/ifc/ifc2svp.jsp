<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="/hss.web.console/style/fokus_ngni.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Roaming Network Edit Page</title>
<script language="JavaScript" type="text/javascript">

	function addIfc(){
		m2len = ifcs.length;
        for ( i=0; i<m2len ; i++){
            if (ifcs.options[i].selected == true ) {
                m1len = assignedIfcs.length;
                assignedIfcs.options[m1len]= new Option(ifcs.options[i].text, ifcs.options[i].value);
            }
        }
        for ( i=(m2len-1); i>=0; i--) {
            if (ifcs.options[i].selected == true ) {
                ifcs.options[i] = null;
            }
        }
	}
	function removeIfc(){
	    m1len = assignedIfcs.length ;
	    for ( i=0; i<m1len ; i++){
	        if (assignedIfcs.options[i].selected == true ) {
	            m2len = ifcs.length;
	            ifcs.options[m2len]= new Option(assignedIfcs.options[i].text, assignedIfcs.options[i].value);
	        }
	    }
	
	    for ( i = (m1len -1); i>=0; i--){
	        if (assignedIfcs.options[i].selected == true ) {
	            assignedIfcs.options[i] = null;
	        }
	    }
	}
	function higher(){
		m2len = assignedIfcs.length;
        for ( i=0; i<m2len ; i++){
            if ((assignedIfcs.options[i].selected == true )&&(i > 0)) {
                m1len = assignedIfcs.length;
                temp = new Option(assignedIfcs.options[i].text, assignedIfcs.options[i].value);
                assignedIfcs.options[i] = new Option(assignedIfcs.options[i - 1].text, assignedIfcs.options[i - 1].value);
                assignedIfcs.options[i - 1] = temp;
                assignedIfcs.options[i - 1].selected = true;
            }
        }
	}
	
	function lower(){
		m2len = assignedIfcs.length - 1;
        for ( i=0; i<m2len ; i++){
            if (assignedIfcs.options[i].selected == true ) {
                m1len = assignedIfcs.length;
                temp = new Option(assignedIfcs.options[i].text, assignedIfcs.options[i].value);
                assignedIfcs.options[i] = new Option(assignedIfcs.options[i + 1].text, assignedIfcs.options[i + 1].value);
                assignedIfcs.options[i + 1] = temp;
                assignedIfcs.options[i + 1].selected = true;
            }
        }
	}
		
	function selectIfc(){
		m1len = assignedIfcs.length ;
	    for ( i=0; i<m1len ; i++){
	        assignedIfcs.options[i].selected = true;
	    }
	}
</script>
</head>
<body bgcolor="#FFFFFF">

<table id="main" height="100%">
	<tr id="bound_bottom_black">
		<td valign="top" bgcolor="#FFFFFF">
		<table width="500">
			<tr>
				<td>
					<jsp:include page="/pages/tiles/error.jsp"></jsp:include>
					<h1><bean:message key="ifc.title" /></h1>
					<html:form action="/ifc2svpSubmit" onsubmit="javascript:selectIfc();" enctype="multipart/form-data">
					<html:hidden property="svpId"/>
					<table border="0">
						<tr>
							<td>
								<html:select property="assignedIfcs" name="ifc2svpForm" multiple="true" size="10" style="width:200px;">
									<html:optionsCollection name="ifc2svpForm" property="assignedIfcs" label="name" value="ifcId"/>
								</html:select>
							</td>
							<td align="center" width="80">
								<input type="button" value="higher" onclick="javascript:higher();" style="width: 60px"><br>
								<input type="button" value="lower" onclick="javascript:lower();" style="width: 60px"><br><br>							
								<input type="button" value="&lt;" onclick="javascript:addIfc();" style="width: 60px"><br>
								<input type="button" value="&gt;" onclick="javascript:removeIfc();" style="width: 60px">
							</td>
							<td>
								<html:select property="ifcs" name="ifc2svpForm" multiple="true" size="10" style="width:200px;">
									<html:optionsCollection name="ifc2svpForm" property="ifcs" label="name" value="ifcId"/>
								</html:select>
							</td>
						</tr>
					</table>
					<input type="image"
						src="/hss.web.console/images/save_edit.gif" width="16" height="16"
						alt="Save">
					</html:form>
				</td>
			</tr>
		</table>

		</td>
		<td id="bound_right"></td>
	</tr>
</table>
<script language="JavaScript" type="text/javascript">
	var ifcs = document.ifc2svpForm.ifcs;
	var assignedIfcs = document.ifc2svpForm.assignedIfcs;
</script>
</body>
</html>
