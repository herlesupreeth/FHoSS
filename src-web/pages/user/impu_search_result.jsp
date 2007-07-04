<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ page import=" java.util.*, de.fhg.fokus.hss.db.model.*, de.fhg.fokus.hss.cx.CxConstants, 
	de.fhg.fokus.hss.web.util.* " %>
	
<jsp:useBean id="resultList" type="java.util.List" scope="request"></jsp:useBean>
<jsp:useBean id="maxPages" type="java.lang.String" scope="request"></jsp:useBean>
<jsp:useBean id="currentPage" type="java.lang.String" scope="request"></jsp:useBean>
<jsp:useBean id="rowPerPage" type="java.lang.String" scope="request"></jsp:useBean>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>

<head>
<link rel="stylesheet" type="text/css" href="/hss.web.console/style/fokus_ngni.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><bean:message key="result.title" /></title>

<script type="text/javascript" language="JavaScript">
function submitForm(pageId){
	document.IMPU_SearchForm.crtPage.value = pageId;
	document.IMPU_SearchForm.submit();
}

function rowsPerPageChanged(){
	document.IMPU_SearchForm.crtPage.value = 1;
	document.IMPU_SearchForm.submit();
}
</script> 
</head>

<body>
	<table id="title-table" align="center" weight="100%" >
	<tr>
		<td align="center">
			<h1> Public User Identity - Search Results </h1> 
			<br/><br/>		
		</td>
	</tr>
	</table>

	<table id="main-table" align="center" valign="middle">
	<tr>
		<td>
	 		<table id="result-table" class="as" width="600" border="0" cellspacing="1" align="center" style="border:2px solid #FF6600;">	
			<tr class="header">
				<td class="header"> ID </td>
				<td class="header"> Identity </td>
				<td class="header"> Implicit-Set ID </td>
				<td class="header"> Type </td>
				<td class="header"> Reg. Status </td>
				<td class="header"> Barring </td>
			</tr>

			<%
			if (resultList != null && resultList.size() > 0){
				IMPU impu;
				int idx = 0;
				Iterator it = resultList.iterator();
					
				while (it.hasNext()){
					impu = (IMPU) it.next();
			%>		
				<tr class="<%= idx % 2 == 0 ? "even" : "odd" %>">
					<td>
						<%= impu.getId() %>
					</td>
					<td> 
						<a href="/hss.web.console/IMPU_Load.do?id=<%= impu.getId() %>"> 
							<%= impu.getIdentity() %>
						</a>	
					</td>
					<td>
						<%= impu.getId_implicit_set() %>
					</td>
					
					<td>
						<%= ((Tuple)WebConstants.select_identity_type.get(impu.getType())).getName() %>
					</td>

					<td>
						<%
							if (impu.getUser_state() == CxConstants.IMPU_user_state_Registered){
								out.println("Registered");
							}
							else if (impu.getUser_state() == CxConstants.IMPU_user_state_Not_Registered){
								out.println("Not-Registered");
							}
							else if (impu.getUser_state() == CxConstants.IMPU_user_state_Unregistered){
								out.println("Unregistered");
							}
							if (impu.getUser_state() == CxConstants.IMPU_user_state_Auth_Pending){
								out.println("Auth-Pending");
							}
						%>
					</td>

					<td>
						<% 
							if (impu.getBarring() == 1){
								out.println("true");
							}
							else{
								out.println("false");
							}
						 %>
					</td>
				</tr>
			<%		
			
						idx++;		
					} //while
				} // if
				else{	
			%>
				<tr>
					<td>
						<bean:message key="result.emptyResultSet" />
					</td>
				</tr>						
			<%
				}
			%>	
			</table> <!-- result-table -->
		</td>
	</tr>		
	<tr>
		<td colspan="3" class="header">
			<html:form action="/IMPU_Search">
				<table align="center">
					<tr>
						<td>
						<%
							int length = Integer.parseInt(maxPages) + 1;
							int cPage = Integer.parseInt(currentPage) + 1;
							for (int iy = 1; iy < length; iy++) {
								if (cPage != iy) {
						%>
									<a href="javascript:submitForm(<%=String.valueOf(iy)%>);"><%=iy%></a>
						<%
								} else {
						%> 
									<font style="color:#FF0000;font-weight: 600;"> 
										<%=String.valueOf(iy)%>
									</font> 
								<% }
							}
						%>
						</td>
						<td>
							<bean:message key="result.rowsPerPage" /><br>
							<html:hidden property="crtPage"></html:hidden> 
							<html:select property="rowsPerPage" onchange="javascript:rowsPerPageChanged();">
								<option value="20"
									<%= rowPerPage.equals("20") ? "selected" : "" %> >20 </option>
								<option value="30"
									<%= rowPerPage.equals("30") ? "selected" : "" %> >30 </option>
								<option value="50"
									<%= rowPerPage.equals("50") ? "selected" : "" %> >50</option>
								<option value="100"
									<%= rowPerPage.equals("100") ? "selected" : "" %> >100</option>
							</html:select>
						</td>
					</tr>
				</table>
			</html:form>
		</td>
	</tr>
	</table> <!-- main-table -->
</body>
</html>
