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
<%@ page import="de.fhg.fokus.hss.model.SptBO.SIP_METHOD"%>
<%@ page import="de.fhg.fokus.hss.model.TrigptBO"%>
<jsp:useBean id="triggerPointXML" type="java.lang.String" scope="request"></jsp:useBean>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="/hss.web.console/style/fokus_ngni.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Trigger Point Page</title>
</head>
<body bgcolor="#FFFFFF">
<table id="main" height="100%">
	<tr id="bound_bottom_black">
		<td valign="top" bgcolor="#FFFFFF">
		<table width="500">
			<tr>
				<td>
					<h1><bean:message key="triggerPoint.title" /></h1>
					<P>
					<a href="javascript:history.back();">[back]</a>
					</P>
					<table border="1"><tr><td>
						<textarea cols="80" rows="30"><bean:write name="triggerPointXML"/></textarea>
					</td></tr></table>						
				</td>
			</tr>
		</table>

		</td>
		<td id="bound_right"></td>
	</tr>
</table>

</body>
</html>
