<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ page import="de.fhg.fokus.hss.util.SecurityPermissions" %>
<jsp:useBean id="result" type="java.util.List" scope="request"></jsp:useBean>
<jsp:useBean id="maxPages" type="java.lang.String" scope="request"></jsp:useBean>
<jsp:useBean id="currentPage" type="java.lang.String" scope="request"></jsp:useBean>
<jsp:useBean id="rowPerPage" type="java.lang.String" scope="request"></jsp:useBean>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="/hss.web.console/style/fokus_ngni.css">

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><bean:message key="result.title" /></title>
</head>
<body>
<table id="main" height="100%">
	<tr id="bound_bottom_black">
		<td valign="top" bgcolor="#FFFFFF"><logic:notEmpty name="result">
			<table class="as" width="500">
				<tr class="header">
					<td class="header"><bean:message key="triggerPoint.head.name" /></td>
					<td class="header"><bean:message key="form.head.action" /></td>
				</tr>
				<logic:iterate name="result" id="trigpt"
					type="de.fhg.fokus.hss.model.Trigpt" indexId="ix">

					<tr class="<%= ix.intValue()%2 == 0 ? "even" : "odd" %>">
						<td><bean:write name="trigpt" property="name" /></td>
						<td align="center">
							<table>
								<tr>
									<td>
									<form method="post" action="/hss.web.console/triggerPointShow.do"
										target="content" style="text-align: center"><input type="hidden"
										name="trigPtId"
										value="<bean:write name="trigpt" property="trigptId" />"> <input
										type="image" src="/hss.web.console/images/edit_small.gif"></form>
									</td>
									<% if(request.isUserInRole(SecurityPermissions.SP_IFC)) { %>
									<td>
									<form method="post" action="/hss.web.console/triggerPointDelete.do"
										target="content" style="text-align: center"><input type="hidden"
										name="trigPtId"
										value="<bean:write name="trigpt" property="trigptId" />"> <input
										type="image" src="/hss.web.console/images/progress_rem.gif"></form>
									</td>
									<% } %>
								</tr>
							</table>
						</td>
					</tr>
				</logic:iterate>
				<%if (Integer.parseInt(maxPages) > 1) {

				%>
				<tr>
					<td colspan="3" class="header"><script type="text/javascript"
						language="JavaScript">
						function submitForm(pageId){
							document.triggerPointSearchForm.page.value = pageId;
							document.triggerPointSearchForm.submit();
						}
					</script> <html:form action="/triggerPointSearch">
						<table>
							<tr>
								<td><%int length = Integer.parseInt(maxPages) + 1;
				int cPage = Integer.parseInt(currentPage) + 1;
				for (int iy = 1; iy < length; iy++) {
					if (cPage != iy) {

						%> <a href="javascript:submitForm(<%=String.valueOf(iy)%>);"><%=iy%></a>
								<%} else {

						%> <font style="color:#FF0000;font-weight: 600;"> <%=String.valueOf(iy)%>
								</font> <%}
				}

				%></td>
								<td><bean:message key="result.rowsPerPage" /><br>
								<html:hidden property="trigPtName"></html:hidden> <html:hidden
									property="page"></html:hidden> <html:select
									property="rowsPerPage"
									onchange="javascript:document.triggerPointSearchForm.submit();">
									<option value="20"
										<%= rowPerPage.equals("20") ? "selected" : "" %>>20</option>
									<option value="30"
										<%= rowPerPage.equals("30") ? "selected" : "" %>>30</option>
									<option value="50"
										<%= rowPerPage.equals("50") ? "selected" : "" %>>50</option>
									<option value="100"
										<%= rowPerPage.equals("100") ? "selected" : "" %>>100</option>
								</html:select></td>
							</tr>
						</table>
					</html:form></td>
				</tr>
				<%}

		%>
			</table>
		</logic:notEmpty> <logic:empty name="result">
			<bean:message key="result.emptryResultSet" />
		</logic:empty></td>
		<td id="bound_right">&nbsp;</td>
	</tr>
</table>
</body>
</html>
