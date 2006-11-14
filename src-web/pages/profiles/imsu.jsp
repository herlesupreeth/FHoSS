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
<title><bean:message key="imsu.title" /> Page</title>
</head>
<body bgcolor="#FFFFFF">

<table id="main" height="100%">
	<tr id="bound_bottom_black">
		<td valign="top" bgcolor="#FFFFFF">
		<table width="500">
			<tr>
				<td><jsp:include page="/pages/tiles/error.jsp"></jsp:include>
				<h1><bean:message key="imsu.title" /></h1>
				<html:form action="/imsuSubmit">
					<table border="0">
						<tr>
							<td>Id</td>
							<td><html:text styleClass="inputtext" property="imsuId"
								readonly="true" disabled="true" /> <html:hidden
								property="imsuId" /></td>
						</tr>
						<tr>
							<td><bean:message key="imsu.head.name" /></td>
							<td><html:text styleClass="inputtext" property="name" /></td>
						</tr>
						<tr>
						</tr>
					</table>
					<% if(request.isUserInRole(SecurityPermissions.SP_IMSU)) { %>
					<p style="text-align:right;"><input type="image"
						src="/hss.web.console/images/save_edit.gif" width="16" height="16"
						alt="Save"></p>
					<% } %>
				</html:form>
				<logic:greaterThan name="imsuForm" property="imsuId" value="0">
				<h1><bean:message key="impi.title" /></h1>
				<table class="as" width="100%" border=0>
					<tr class="header">
						<td><bean:message key="impi.head.id" /></td>
						<td><bean:message key="form.head.action" /></td>
					</tr>
					<logic:iterate name="imsuForm" property="impis" id="impi"
						type="de.fhg.fokus.hss.model.Impi" indexId="ix">
						<tr class="<%= ix.intValue()%2 == 0 ? "even" : "odd" %>">
							<td><bean:write name="impi" property="impiString" /></td>
							<td><a
								href="/hss.web.console/impiShow.do?impiId=<%=impi.getImpiId()%>">
							<img src="/hss.web.console/images/edit_small.gif" width="16"
								height="16"> </a> 
								<% if(request.isUserInRole(SecurityPermissions.SP_IMPI)) { %>
							<a href="/hss.web.console/impiDelete.do?impiId=<%=impi.getImpiId()%>&imsuId=<%=impi.getImsu().getImsuId()%>">
							<img src="/hss.web.console/images/progress_rem.gif" width="16"
								height="16" alt="Delete"> </a>
								<% } %>
								</td>
						</tr>
					</logic:iterate>
					<% if(request.isUserInRole(SecurityPermissions.SP_IMPI)) { %>
					<tr class="header">
						<td></td>
						<td><html:form action="/impiCreate">
							<input type="hidden"
								value="<bean:write name="imsuForm" property="imsuId" />"
								name="imsuId">
							<input type="hidden" value="-1" name="impiId">
							<input type="image" src="/hss.web.console/images/add_obj.gif"
								width="16" height="16">
						</html:form></td>
					</tr>
					<% } %>
				</table>
				</logic:greaterThan>
				</td>
			</tr>
		</table>
		</td>
		<td id="bound_right"></td>
	</tr>
</table>


</body>
</html>
