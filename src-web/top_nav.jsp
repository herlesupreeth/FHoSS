<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="style/fokus_ngni.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Navigation</title>
</head>
<body bgcolor="#FDDE96">
<table id="navigation" height="100%">
	<tr id="bound_bottom">
		<td id="bound_left">&nbsp;</td>
		<td><a href="http://www.fokus.fraunhofer.de/home/" target="_blank"><img
			src="images/hauptlogo_01_en.gif" /></a> <br>
		</td>
		<td><img src="images/blind.gif" height="73" width="575" /></td>
		<td><a
			href="http://www.fokus.fraunhofer.de/fokus/branchen-loesungen/testlabs/hauptseite_testbeds.php?lang=en" target="_blank"><img
			src="images/hauptlogo_02_en.gif" /></a>
		</td>
		<td id="bound_right">&nbsp;</td>
	</tr>
	<tr height="30" bgcolor="#FFFFFF" >
		<td id="bound_left">&nbsp;</td>
		<td colspan="3" id="header">
			<bean:message key="hss.title" />
		</td>
		<td id="bound_right">&nbsp;</td>
	</tr>
	<tr id="bound_bottom">
		<td id="bound_left">&nbsp;</td>
		<td colspan="3">&nbsp;</td>
		<td id="bound_right">&nbsp;</td>
	</tr>
	<tr id="bound_bottom">
		<td id="bound_left">&nbsp;</td>
		<td id="level01" colspan="2">
			<a href="index.jsp" target="_top">Home</a>
			<a href="pages/user" target="main">User Identities</a>
			<a href="pages/services" target="main">Services</a>			
			<a href="pages/network" target="main">Network Configuration</a>
			<a href="pages/info" target="main">Statistics</a>
		</td>
		<td id="language"><a href="javascript:alert('Try again later!');">help</a></td>
		<td id="bound_right">&nbsp;</td>
	</tr>
	<tr id="bound_bottom_black" bgcolor="#EB9015" >
		<td id="bound_left">&nbsp;</td>
		<td id="level02" colspan="3">&nbsp;</td>
		<td id="bound_right">&nbsp;</td>
	</tr>
</table>
</body>
</html>
