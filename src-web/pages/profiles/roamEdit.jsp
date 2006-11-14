<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-nested"
	prefix="nested"%>
<%@ page import="de.fhg.fokus.hss.util.SecurityPermissions" %>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="/hss.web.console/style/fokus_ngni.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Roaming Network Edit Page</title>
<script language="JavaScript" type="text/javascript">

	function addNetwork(){
		m2len = networks.length ;
        for ( i=0; i<m2len ; i++){
            if (networks.options[i].selected == true ) {
                m1len = roams.length;
                roams.options[m1len]= new Option(networks.options[i].text, networks.options[i].value);
            }
        }
        for ( i=(m2len-1); i>=0; i--) {
            if (networks.options[i].selected == true ) {
                networks.options[i] = null;
            }
        }
	}
	function removeNetwork(){
	    m1len = roams.length ;
	    for ( i=0; i<m1len ; i++){
	        if (roams.options[i].selected == true ) {
	            m2len = networks.length;
	            networks.options[m2len]= new Option(roams.options[i].text, roams.options[i].value);
	        }
	    }
	
	    for ( i = (m1len -1); i>=0; i--){
	        if (roams.options[i].selected == true ) {
	            roams.options[i] = null;
	        }
	    }
	}
	
	function selectNetworks(){
		m1len = roams.length ;
	    for ( i=0; i<m1len ; i++){
	        roams.options[i].selected = true;
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
					<h1><bean:message key="roam.title" /></h1>
					<html:form action="/roamSubmit" onsubmit="javascript:selectNetworks();" enctype="multipart/form-data">
					<html:hidden property="impiId"/>
					<table>
						<tr>
							<td>
								<html:select property="roams" name="roamingForm" multiple="true" size="10" style="width:200px;">
									<html:optionsCollection name="roamingForm" property="roams" label="networkString" value="nwId"/>
								</html:select>
							</td>
							<td align="center" width="80">
								<input type="button" value="&lt;" onclick="javascript:addNetwork();"><br>
								<input type="button" value="&gt;" onclick="javascript:removeNetwork();">
							</td>
							<td>
								<html:select property="networks" name="roamingForm" multiple="true" size="10" style="width:200px;">
									<html:optionsCollection name="roamingForm" property="networks" label="networkString" value="nwId"/>
								</html:select>
							</td>
						</tr>
					</table>
					<% if(request.isUserInRole(SecurityPermissions.SP_IMPI)) { %>
					<input type="image"
						src="/hss.web.console/images/save_edit.gif" width="16" height="16"
						alt="Save">
					<% } %>
					</html:form>
				</td>
			</tr>
		</table>

		</td>
		<td id="bound_right"></td>
	</tr>
</table>
<script language="JavaScript" type="text/javascript">
	var roams = document.roamingForm.roams;
	var networks = document.roamingForm.networks;
</script>
</body>
</html>
