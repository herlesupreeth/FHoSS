<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="/hss.web.console/style/fokus_ngni.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search Page</title>
</head>
<body bgcolor="#CCCCCC">
<html:form action="imsuSearch"
	target="content">
<table id="main" height="100%">
	<tr id="bound_bottom_black">
		<td valign="top" bgcolor="#FFFFFF">
			<table border="0">
				<tr>
					<td><bean:message key="imsu.head.name" /></td>
					<td><html:text styleClass="inputtext" property="name" /></td>
					<td><html:submit value="Search" /></td>
				</tr>
			</table>
		</td>
		<td id="bound_right">&nbsp;</td>
	</tr>
</table>
</html:form>
</body>
</html>
