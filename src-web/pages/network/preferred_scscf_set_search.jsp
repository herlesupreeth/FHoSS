<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
<link rel="stylesheet" type="text/css" href="/hss.web.console/style/fokus_ngni.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><bean:message key="result.title" /></title>
</head>

<body>
	<table id="title-table" align="center" weight="100%" >
	<tr>
		<td align="center">
			<h1> Preferred S-CSCF Set - Search </h1> 			
			<br/><br/>
		</td>
	</tr>
	</table> <!-- title-table -->

	<html:form action="PrefS_Search">
		<table id="main-table" align="center" valign="middle">
		<tr height="99%">
			<td align="center">
				<table id="search-table" border="0" cellspacing="0" align="center" width="400">
				<tr>
					<td align="right"><b>Enter Search Parameters:</b></td>
				</tr>
	 			<tr>
	 				<td>
					 	<table id="fields-table" class="as" border="0" cellspacing="1" align="center" width="100%" style="border:2px solid #FF6600;">
		    			<tr bgcolor="#FFCC66">
							<td>ID-Set</td>
							<td><html:text property="id_set" value="" styleClass="inputtext" style="width:100px;"/></td>
						</tr>
						
		    			<tr bgcolor="#FFCC66">							
		    				<td>Name</td>
							<td><html:text property="name" value="" styleClass="inputtext" style="width:200px;"/></td>
						</tr>						
						</table>
					</td>		
				</tr>
				<tr>
					<td align="center"><br/><html:submit property="search" value="Search" /></td>
				</tr>				
				</table>
			</td>
		</tr>		
		</table>		
	</html:form>
</body>
</html>
