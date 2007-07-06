<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
	
<%@ page import="java.util.*, de.fhg.fokus.hss.db.model.*, de.fhg.fokus.hss.db.op.*, de.fhg.fokus.hss.web.util.*,
	de.fhg.fokus.hss.db.hibernate.*, org.hibernate.Session, de.fhg.fokus.hss.web.util.WebConstants " %>

<html>

<head>
<jsp:useBean id="scscf_list" type="java.util.List" scope="request"></jsp:useBean>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title> Preferred S-CSCF Set </title>
<link rel="stylesheet" type="text/css" href="/hss.web.console/style/fokus_ngni.css">

<%
	int id_set = Integer.parseInt(request.getParameter("id_set"));
%>

<script type="text/javascript" language="JavaScript">

function add_action_for_form(action, associated_ID) {
	switch(action){
		case 1:
			document.PrefS_Form.nextAction.value="save";
			document.PrefS_Form.submit();
			break;
		case 2:
			document.PrefS_Form.nextAction.value="reset";
			document.PrefS_Form.reset();
			break;
		case 3:
			document.PrefS_Form.nextAction.value="refresh";
			document.PrefS_Form.submit();			
			break;
		case 4:
			document.PrefS_Form.nextAction.value="delete";
			document.PrefS_Form.submit();			
			break;
		case 5:
			document.PrefS_Form.nextAction.value="delete_scscf";
			document.PrefS_Form.associated_ID.value = associated_ID;
			document.PrefS_Form.submit();
			break;
				
		case 12:
			document.PrefS_Form.nextAction.value="add_scscf";
			document.PrefS_Form.submit();
			break;
	}
}
</script>
</head>

<body>

	<table id="title-table" align="center" weight="100%" >
	<tr>
		<td align="center">
			<h1>  Preferred S-CSCF Set </h1> 			
			<br/><br/>			
		</td>
	<tr>	
		<td align="left">
			<!-- Print errors, if any -->
			<jsp:include page="/pages/tiles/error.jsp"></jsp:include>
		</td>
	</tr>
	</table> <!-- title-table -->

	<html:form action="/PrefS_Submit">
		<html:hidden property="nextAction" value=""/>
		<html:hidden property="associated_ID" value=""/>
		
		<table id="main-table" align="center" valign="middle">
		<tr>
			<td>
	 			<table id="pcscf-table" border="0" align="center" width="400" >						
	 			<tr>
 					<td>
						<table id="fields-table" class="as" border="0" cellspacing="1" align="center" width="100%" style="border:2px solid #FF6600;">						
						<tr bgcolor="#FFCC66">
							<td> ID-Set </td>
							<td><html:text property="id_set" readonly="true" styleClass="inputtext_readonly" style="width:100px;"/> </td>
						</tr>
						<tr bgcolor="#FFCC66">
							<td>Name* </td>
							<td><html:text property="name" styleClass="inputtext" style="width:200px;"/> </td>
						</tr>
						
			<%			if (id_set == -1){
						// we can create a preferred_scscf_set only if we have an initial S-CSCF Name
			%>			
						<tr bgcolor="#FFCC66">
							<td> S-CSCF Name*</td>							
							<td>
								<html:text property="scscf_name" styleClass="inputtext" value="" style="width:200px;"/> </td>
							</td>
						</tr>
						
						<tr bgcolor="#FFCC66">
							<td>
								Priority*	
							</td>
							<td>	
								<html:text property="priority" styleClass="inputtext" style="width:100px;"/>
							</td>
						</tr>
			<%
						}
			%>			
						</table> <!-- fields-table -->
					</td>
				</tr>
	 			<tr>
 					<td>
						<table id="buttons-table" align="center">			
						<tr>
							<td align="center"> 
								<b> Mandatory fields were marked with "*" </b>
							</td>
						</tr>	
						<tr>
							<td align=center> 
								<br/>
								<%
									 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
								%>								
										<html:button property="save_button" value="Save" onclick="add_action_for_form(1);"/>				
								<%
									}
								%>
								
								<html:button property="refresh_button" value="Refresh" onclick="add_action_for_form(3);"/> 
								<% 
									if (request.isUserInRole(WebConstants.Security_Permission_ADMIN) && id_set == -1){ 
								%>
									<html:button property="reset_button" value="Reset" onclick="add_action_for_form(2);"/> 
								<%
									}
								%>
								<% 
									if (request.isUserInRole(WebConstants.Security_Permission_ADMIN) && id_set != -1){ 
								%>
									<html:button property="delete_button" value="Delete" onclick="add_action_for_form(4);" 
										disabled="<%=Boolean.parseBoolean((String)request.getAttribute("deleteDeactivation")) %>"/>				
								<%
									}
								%>									
							</td>
						</tr>
						</table> <!-- buttons-table -->
					</td>
				</tr>							
				</table>	<!-- pcscf-table -->	
			</td>
		</tr>
		
		<%
			if (id_set != -1){		
		%>
			<%
				 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
			%>										
			<tr>
				<td>	
					<br />
					<h2> Add S-CSCF </h2>
					<table id="add-scscf-table" width="600" class="as" border="0" cellspacing="1" align="center" style="border:2px solid #FF6600;">
					<tr class="even">
						<td>
							S-CSCF Name
						</td>	
						<td>
							<html:text property="scscf_name" styleClass="inputtext" value="" style="width:200px;"/> </td>
						</td>

						<td>
							Priority	
						</td>
						<td>	
							<html:text property="priority" styleClass="inputtext" style="width:100px;"/>
						</td>
						
						<td>
							<html:button property="scscf_add_button" value="Add" onclick="add_action_for_form(12, -1);"/>
						</td>
					</tr>	
					</table> <!-- add-scscf-table-->
				</td>
			</tr>
			<%
				}//endif ADMIN
			%>
			
			<tr>
				<td>
					<br />						
					<h2> List of attached S-CSCFs </h2>
					<table id="list-attached-scscf" class="as" border="0" cellspacing="1" align="center" width="600" style="border:2px solid #FF6600;">
					<tr class="header">
						<td class="header"> S-CSCF Name </td>
						<td class="header"> Priority </td>
						<%
							 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
						%>														
								<td class="header"> Delete </td>
						<%	
							}
						%>
					</tr>
					<%
						if (scscf_list != null){
							Iterator it = scscf_list.iterator();
							Preferred_SCSCF_Set preferred_scscf_set = null;
							int idx = 0;
							while (it.hasNext()){
								preferred_scscf_set = (Preferred_SCSCF_Set) it.next();
					%>
							<tr class="<%= idx % 2 == 0 ? "even" : "odd" %>">																			
								<td>  
									<%= preferred_scscf_set.getScscf_name() %>
								</td>

								<td>  
									<%=preferred_scscf_set.getPriority()%>
								</td>
								

									<%
									 	if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
									 		out.println("<td>");
											if (((String)request.getAttribute("deleteSCSCFDeactivation")).equals("true")){
									%>
												<input type="button" name="delete_scscf" "value="Delete" onclick="add_action_for_form(5, <%= preferred_scscf_set.getId() %>);" disabled/>	
									<%
											}
											else{
									%>
												<input type="button" name="delete_scscf" "value="Delete" onclick="add_action_for_form(5, <%= preferred_scscf_set.getId() %>);" />										
									<%
											}
									 		out.println("</td>");
										}
									%>												
								
							</tr>											
					<%			
								idx++;												
								}
							}
					%>
					</table> <!-- list-attached-scscf -->
				</td>	
			<%
				}
			%>	
		</tr>
		</table>	<!-- main-table -->	
	</html:form>
</body>
</html>
