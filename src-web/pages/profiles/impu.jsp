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
<title>Profile Page</title>
</head>
<body bgcolor="#FFFFFF">
<html:form action="/impuSubmit">
	<table id="main" height="100%">
		<tr id="bound_bottom_black">
			<td valign="top" bgcolor="#FFFFFF">
			<table width="500">
				<tr>
					<td><jsp:include page="/pages/tiles/error.jsp"></jsp:include>
					<h1><bean:message key="impu.title" /></h1>
					<table border="0">
						<tr>
							<td></td>
							<td><html:hidden
								property="impuId" /> <html:hidden property="impiId" /></td>
						</tr>
						<tr>
							<td nowrap="nowrap"><bean:message key="impu.head.sipUrl" />*</td>
							<td><html:text styleClass="inputtext" property="sipUrl"  style="width:325px;"/></td>
						</tr>
						<tr>
							<td nowrap="nowrap"><bean:message key="impu.head.telUrl" /></td>
							<td><html:text styleClass="inputtext" property="telUrl"  style="width:325px;"/></td>
						</tr>						
						<tr>
							<td nowrap="nowrap"><bean:message key="impu.head.barred" /></td>
							<td><html:checkbox styleClass="inputtext" property="barred" /></td>
						</tr>
						<tr>
						<td nowrap="nowrap"><bean:message key="impu.head.regStatus" /></td>
							<td valign="top">
								<html:select property="userStatusId"
									styleClass="inputtext" size="1" style="width:225px;">
									<html:optionsCollection property="userStatusList" label="label" value="value"/>
								</html:select>							
							</td>
						</tr>
						<tr>						
							<td nowrap="nowrap">
								<bean:message key="svp.title" />
							</td>
							<td>
								<html:select property="svpId" name="impuForm"
										styleClass="inputtext" size="1" style="width:225px;">
										<html:option value="-1">...</html:option>
										<html:optionsCollection name="impuForm" property="svps" label="name" value="svpId"/>
								</html:select>	
							</td>
						</tr>
					</table>
					<p style="text-align:right;">
						<logic:notEqual value="-1" property="impuId" name="impuForm">
						<a href="impuShow.do?impuId=<bean:write name="impuForm" property="impuId" />">
							Refresh
						</a>
						</logic:notEqual>
						<% if(request.isUserInRole(SecurityPermissions.SP_IMPU)) { %>
							<input type="image"
							src="/hss.web.console/images/save_edit.gif" width="16" height="16"
							alt="Save">
						<% } %>
					</p>			
					</td>
				</tr>
			</table>
			</td>
			<td id="bound_right"></td>
		</tr>
	</table>

</html:form>
</body>
</html>
