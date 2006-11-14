<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ page import="de.fhg.fokus.hss.util.SecurityPermissions" %>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="/hss.web.console/style/fokus_ngni.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Application Server Page</title>
</head>
<body bgcolor="#FFFFFF">
<table id="main" height="100%">
	<tr id="bound_bottom_black">
		<td valign="top" bgcolor="#FFFFFF">
		<table width="500">
			<tr>
				<td><jsp:include page="/pages/tiles/error.jsp"></jsp:include>
				<h1><bean:message key="psiTempl.title" /></h1>
				
				<html:form action="/psiTemplSubmit">
				<html:hidden property="psiTemplId" />
					<table border="0">
						<tr>
							<td nowrap="nowrap"><bean:message key="psiTempl.head.name" />*</td>
							<td><html:text styleClass="inputtext" property="psiTemplName"  style="width:325px;"/></td>
						</tr>
						<tr>
							<td nowrap="nowrap"><bean:message key="psiTempl.head.username" />*</td>
							<td><html:text styleClass="inputtext" property="username"  style="width:325px;"/></td>
						</tr>	
						<tr>
							<td nowrap="nowrap"><bean:message key="psiTempl.head.hostname" />*</td>
							<td><html:text styleClass="inputtext" property="hostname"  style="width:325px;"/></td>
						</tr>
						<tr>
							<td nowrap="nowrap"><bean:message key="psiTempl.head.apsvr" />*</td>
							<td>
								<html:select property="apsvrId"
									styleClass="inputtext" size="1" style="width:325px;">
									<html:option value="">Select ...</html:option>
									<html:optionsCollection property="apsvrs" label="name" value="apsvrId"/>
								</html:select>
							</td>
						</tr>						
					</table>
					<% if(request.isUserInRole(SecurityPermissions.SP_PSI)) { %>
					<p style="text-align:right;"><input type="image"
						src="/hss.web.console/images/save_edit.gif" width="16" height="16"
						alt="Save"></p>
					<% } %>
				</html:form> 
				</td>
			</tr>
		</table>

		</td>
		<td id="bound_right"></td>
	</tr>
</table>

</body>
</html>
