<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" 
    import="de.fhg.fokus.hss.form.NetworkForm"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ page import="de.fhg.fokus.hss.util.SecurityPermissions" %>
<jsp:useBean id="chrginfos" type="java.util.List" scope="request"> </jsp:useBean>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="/hss.web.console/style/fokus_ngni.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><bean:message key="network.title" /></title>
</head>
<body bgcolor="#FFFFFF">
<table id="main" height="100%">
	<tr id="bound_bottom_black">
		<td valign="top" bgcolor="#FFFFFF">
			<table class="as" width="500">
				<tr class="header">
					<td><bean:message key="chrginfo.set.name" /></td>
					<td><bean:message key="form.head.action" /></td>
				</tr>
				<logic:iterate name="chrginfos" id="chrginfo" indexId="ix">
				<tr class="<%= ix.intValue()%2 == 0 ? "even" : "odd" %>">
					<td>
						<bean:write name="chrginfo" property="name"/>
					</td>
					<td>
						<a href="/hss.web.console/chargingShow.do?action=edit&chrgId=<bean:write name="chrginfo" property="chrgId"/>">
							<img src="/hss.web.console/images/edit_small.gif" width="16"
											height="16">
						</a>
						<% if(request.isUserInRole(SecurityPermissions.SP_NETWORK)) { %>
							<a href="/hss.web.console/chargingShow.do?action=del&chrgId=<bean:write name="chrginfo" property="chrgId"/>">
								<img src="/hss.web.console/images/progress_rem.gif" width="16"
												height="16" alt="Delete">	
							</a>
						<% } %>
					</td>
				</tr>
				</logic:iterate>
			</table>
			<br>
			<jsp:include page="/pages/tiles/error.jsp"></jsp:include>
			<html:form action="/chargingSubmit">
			<html:hidden property="action" value="submit"/>
			<html:hidden property="chrgId"/>
			<table class="as" width="500">
				<tr class="header">
					<td><bean:message  key="chrginfo.set.name"/>*
					</td>
					<td class="tgpFormular"><html:text property="name" style="width:250px;"></html:text>
					</td>
				</tr>			
				<tr class="header">
					<td><bean:message  key="chrginfo.set.priEvent"/>*
					</td>
					<td class="tgpFormular"><html:text property="priEventChrgFnName" style="width:250px;"></html:text>
					</td>
				</tr>
				<tr class="header">
					<td><bean:message  key="chrginfo.set.secEvent"/>
					</td>
					<td class="tgpFormular"><html:text property="secEventChrgFnName" style="width:250px;"></html:text>
					</td>
				</tr>
				<tr class="header">
					<td><bean:message  key="chrginfo.set.priColl"/>
					</td>
					<td class="tgpFormular"><html:text property="priChrgCollFnName" style="width:250px;"></html:text>
					</td>
				</tr>				
				<tr class="header">
					<td><bean:message  key="chrginfo.set.secColl"/>
					</td>
					<td class="tgpFormular"><html:text property="secChrgCollFnName" style="width:250px;"></html:text>
					</td>
				</tr>
			</table>
			<% if(request.isUserInRole(SecurityPermissions.SP_NETWORK)) { %>
				<logic:equal property="chrgId" value="-1" name="chrginfoForm">
					<input type="image"
							src="/hss.web.console/images/add_obj.gif" width="16" height="16"
							alt="Save">
				</logic:equal>
				<logic:notEqual property="chrgId" value="-1" name="chrginfoForm">
					<input type="image"
							src="/hss.web.console/images/save_edit.gif" width="16" height="16"
							alt="Save">
				</logic:notEqual>
			<% } %>
			</html:form>
		</td>
		<td id="bound_right"></td>
	</tr>
</table>
</body>
</html>