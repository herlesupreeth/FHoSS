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
				<h1>
					<bean:message key="svp.title" />
				</h1>
				<html:form action="svpSubmit">
				<html:hidden property="svpId"/>
				<table border="0" width="480">
					<tr>
						<td nowrap="nowrap"><bean:message key="svp.head.name" />*</td>
						<td><html:text styleClass="inputtext" property="name" style="width:270px;"/></td>
					</tr>
				</table>
				<% if(request.isUserInRole(SecurityPermissions.SP_SVP)) { %>
				<html:image src="/hss.web.console/images/save_edit.gif"></html:image>
				<% } %>
				</html:form>
				<logic:notEqual name="svpForm" value="-1" property="svpId">
				<h1>
					<bean:message key="ifc.title" />
					<% if(request.isUserInRole(SecurityPermissions.SP_SVP)) { %>
					<a href="ifc2svp.do?svpId=<bean:write name="svpForm" property="svpId" />">
						<img src="/hss.web.console/images/edit_small.gif" width="16"
							height="16">
					</a>
					<% } %>
				</h1>
				<table class="as" width="100%" border=0>
					<tr class="header">
						<td class="header" width="70%">
							<bean:message key="ifc.head.ifcName" />
						</td>
						<td class="header" width="20%">
							<bean:message key="ifc.head.priority" />
						</td>						
						<td class="header" width="10%">
							<bean:message key="form.head.action" />
						</td>						
					</tr>					
					<logic:iterate name="svpForm" property="ifcs"
						id="ifc" type="de.fhg.fokus.hss.form.IfcForm" indexId="ix">
						<tr class="<%= ix.intValue()%2 == 0 ? "even" : "odd" %>">
							<td><bean:write name="ifc" property="ifcName"/></td>
							<td><bean:write name="ifc" property="priority"/></td>
						    <td align="center">
							    <table>
								  <tr>
									 <td>
									 <form method="post" action="/hss.web.console/ifcShow.do"
										target="content" style="text-align: center"><input type="hidden"
										name="ifcId"
										value="<bean:write name="ifc" property="ifcId" />"> <input
										type="image" src="/hss.web.console/images/edit_small.gif"></form>
									 </td>
								  </tr>
							    </table>
						    </td>							

						</tr>
					</logic:iterate>
				</table>
				</logic:notEqual>
				</td>
			</tr>
		</table>

		</td>
		<td id="bound_right"></td>
	</tr>
</table>

</body>
</html>
