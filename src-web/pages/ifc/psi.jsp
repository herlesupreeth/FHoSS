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
<table id="main" height="100%">
	<tr id="bound_bottom_black">
		<td valign="top" bgcolor="#FFFFFF">
		<table width="500">
			<tr>
				<td><jsp:include page="/pages/tiles/error.jsp"></jsp:include>
				<h1><bean:message key="psi.title" /></h1>
				<html:form action="/psiSubmit">
				<html:hidden property="psiId" />
					<table border="0" class="as">
						<tr class="header">
							<td><bean:message key="psi.head.name" /></td>
							<td class="tgpFormular"><html:text styleClass="inputtext" property="psiName"  style="width:325px;"/></td>
						</tr>
						<tr class="header">
							<td><bean:message key="psi.head.wildcard" /></td>
							<td class="tgpFormular"><html:text styleClass="inputtext" property="wildcard"  style="width:325px;"/></td>
						</tr>
						<tr class="header">
							<td><bean:message key="psi.head.psiTempl" /></td>
							<td class="tgpFormular">
								<html:select property="psiTemplId"
									styleClass="inputtext" size="1" style="width:325px;">
									<html:option value="">Select ...</html:option>
									<html:optionsCollection property="psiTempls" label="templName" value="templId"/>
								</html:select>
							</td>
						</tr>
						<logic:notEqual value="-1" property="psiId" name="psiForm">
						<tr class="header">
							<td><bean:message key="psi.head.impuName" /></td>
							<td class="tgpFormular"><bean:write property="impuName" name="psiForm"/></td>
						</tr>
						</logic:notEqual>
					</table>
					<% if(request.isUserInRole(SecurityPermissions.SP_PSI)) { %>
					<p style="text-align:right;"><input type="image"
						src="/hss.web.console/images/save_edit.gif" width="16" height="16"
						alt="Save"></p>
					<% } %>
				</html:form>
				
			</tr>	
			<tr>
				<td>
				  <h1><bean:message key="psiTempl.title" /></h1>
				</td>
			</tr>
			<tr>
			  <td>	
			    <table class="as" width="100%" border=0>    
				     <tr class="header">
						<td class="header" width="80%">
							<bean:message key="psiTempl.head.name" />
						</td>
						
						<td class="header" width="20%">
							<bean:message key="form.head.action" />
						</td>										            
					 </tr>	      
					<logic:iterate name="psiForm" property="psiTempls" id="psiTempl"
						type="de.fhg.fokus.hss.model.PsiTempl" indexId="ix">
	
						<tr class="<%= ix.intValue()%2 == 0 ? "even" : "odd" %>">
							<td><bean:write name="psiTempl" property="templName" /></td>
							<td align="center">
								<table>
									<tr>
										<td>
										<form method="post" action="/hss.web.console/psiTemplShow.do"
											target="content" style="text-align: center"><input type="hidden"
											name="psiTemplId"
											value="<bean:write name="psiTempl" property="templId" />"> <input
											type="image" src="/hss.web.console/images/edit_small.gif"></form>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</logic:iterate>
				</table>
			  </td>				
			</tr>	
			
			<tr>
			  <td>				
				
				<logic:notEqual value="-1" property="psiId" name="psiForm">
					<jsp:include page="/pages/tiles/impuSelect.jsp">
						<jsp:param name="formName" value="psiForm"/>
						<jsp:param name="thisTarget" value="/psiShow"/>
						<jsp:param name="connectTarget" value="/impu2psi.do"/>
						<jsp:param name="parentId" value="psiId"/>
						<jsp:param name="addAllow" value="<%= request.isUserInRole(SecurityPermissions.SP_PSI) ? "true" : "false" %>"/>
					</jsp:include>							
				</logic:notEqual>
		</table>
		</td>
	</tr>
</table>


</body>
</html>
