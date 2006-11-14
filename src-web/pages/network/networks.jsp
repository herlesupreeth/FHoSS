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
<jsp:useBean id="networks" type="java.util.List" scope="request"> </jsp:useBean>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="/hss.web.console/style/fokus_ngni.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><bean:message key="network.title" /></title>
<script language="JavaScript" type="text/javascript">
	function editNetworkName(id, name){
		newName = prompt("<bean:message key="network.rename" />" ,name);
		document.networkForm.id.value = id;
		document.networkForm.networkString.value = newName;
		document.networkForm.actionString.value = "<%=NetworkForm.ACTION_RENAME%>";
		document.networkForm.submit();
	} 
	function removeNetwork(id){
		document.networkForm.actionString.value = "<%=NetworkForm.ACTION_DELETE%>";
		document.networkForm.id.value = id;
		document.networkForm.submit();
	} 	
</script>
</head>
<body bgcolor="#FFFFFF">
<table id="main" height="100%">
	<tr id="bound_bottom_black">
		<td valign="top" bgcolor="#FFFFFF">
			<jsp:include page="/pages/tiles/error.jsp"></jsp:include>
			<table class="as" width="500">
				<tr class="header">
					<td><bean:message key="network.name" /></td>
					<td><bean:message key="form.head.action" /></td>
				</tr>
				<logic:iterate name="networks" id="network" indexId="ix">
				<tr class="<%= ix.intValue()%2 == 0 ? "even" : "odd" %>">
					<td>
						<bean:write name="network" property="networkString"/>
					</td>
					<td>
						<% if(request.isUserInRole(SecurityPermissions.SP_NETWORK)) { %>
							<a href="javascript:editNetworkName(<bean:write name="network" property="nwId" />,'<bean:write name="network" property="networkString"/>');">
								<img src="/hss.web.console/images/edit_small.gif" width="16"
												height="16">
							</a>
							<a href="javascript:removeNetwork(<bean:write name="network" property="nwId" />);">
								<img src="/hss.web.console/images/progress_rem.gif" width="16"
												height="16" alt="Delete">	
							</a>
						<% } %>
					</td>
				</tr>
				</logic:iterate>
				<% if(request.isUserInRole(SecurityPermissions.SP_NETWORK)) { %>
				<html:form action="/networksSubmit">
					<tr class="header">
						<td>
							<html:hidden property="id" value=""/>
							<html:text property="networkString" value=""></html:text>
							<html:hidden property="actionString" value="<%=NetworkForm.ACTION_CREATE%>"/> 
						</td>
						<td>
							<input type="image"
								src="/hss.web.console/images/save_edit.gif" width="16" height="16"
								alt="Save">
						</td>
					</tr>
				</html:form>
				<% } %>
			</table>
		</td>
		<td id="bound_right"></td>
	</tr>
</table>
</body>
</html>