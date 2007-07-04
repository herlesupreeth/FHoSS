<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ page import="java.util.*, de.fhg.fokus.hss.db.model.*, de.fhg.fokus.hss.web.util.WebConstants " %>
<jsp:useBean id="associated_IMPIs" type="java.util.List" scope="request"></jsp:useBean>

<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>IMSU </title>
<link rel="stylesheet" type="text/css" href="/hss.web.console/style/fokus_ngni.css">

<%
	int id = Integer.parseInt(request.getParameter("id"));
%>

<script type="text/javascript" language="JavaScript">

function add_action_for_form(action, associated_ID) {
	switch(action){
		case 1:
			document.IMSU_Form.nextAction.value="save";
			document.IMSU_Form.submit();
			break;
		case 2:
			document.IMSU_Form.nextAction.value="reset";
			document.IMSU_Form.reset();
			break;
		case 3:
			document.IMSU_Form.nextAction.value="refresh";
			document.IMSU_Form.submit();			
			break;
		case 4:
			document.IMSU_Form.nextAction.value="delete";
			document.IMSU_Form.submit();			
			break;
		case 5:
			document.IMSU_Form.nextAction.value="delete_impi";
			document.IMSU_Form.associated_ID.value = associated_ID;
			document.IMSU_Form.submit();			
			break;
			
		case 12:
			document.IMSU_Form.nextAction.value="add_impi";
			document.IMSU_Form.submit();			
			break;
	}
}
</script>
</head>

<body>

	<table id="title-table" align="center" weight="100%" >
	<tr>
		<td align="center">
			<h1> IMS Subscription -IMSU-</h1> 			
			<br/><br/>			
		</td>
	<tr>	
		<td align="left">
			<!-- Print errors, if any -->
			<jsp:include page="/pages/tiles/error.jsp"></jsp:include>
		</td>
	</tr>
	</table> <!-- title-table -->

	<!-- IMSU_Form -->
	<html:form action="/IMSU_Submit">
	<html:hidden property="nextAction" value=""/>
	<html:hidden property="associated_ID" value=""/>
	
	<table id="main-table" align="center" valign="middle" >
	<tr>
		<td>
			<table id="left-side-table" >
			<tr>
				<td>			
			 		<table id="imsu-table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">						
				    <tr bgcolor="#FFCC66">
						<td>ID </td>
						<td><html:text property="id" readonly="true" styleClass="inputtext_readonly" style="width:100px;"/> </td>
					</tr>
			
				    <tr bgcolor="#FFCC66">
						<td>Name*</td>
						<td><html:text property="name" styleClass="inputtext" style="width:200px;"/> </td>
					</tr>
						
				    <tr bgcolor="#FFCC66">
						<td>Capabilities Set</td>
						<td>
							<html:select property="id_capabilities_set" name="IMSU_Form" styleClass="inputtext" size="1" style="width:200px;">
								<html:option value="-1">none</html:option>
								<html:optionsCollection name="IMSU_Form" property="select_capabilities_set" label="name" value="id_set"/>
							</html:select>
						</td>	
					</tr>

				    <tr bgcolor="#FFCC66">
						<td>Preferred S-CSCF</td>
						<td>
							<html:select property="id_preferred_scscf" name="IMSU_Form" styleClass="inputtext" size="1" style="width:200px;">
								<html:option value="-1">none</html:option>
								<html:optionsCollection name="IMSU_Form" property="select_preferred_scscf" label="name" value="id_set"/>
							</html:select>
						</td>	
					</tr>
			
				    <tr bgcolor="#FFCC66">
						<td>S-CSCF Name </td>
						<td>
							<html:text property="scscf_name" readonly="true" styleClass="inputtext_readonly" style="width:200px;"/> 
						</td>
					</tr>
				
				    <tr bgcolor="#FFCC66">
						<td>Diameter Name </td>
						<td>
							<html:text property="diameter_name" readonly="true" styleClass="inputtext_readonly" style="width:200px;"/>
						</td>
					</tr>
					</table>		
				</td>
			</tr>
			<tr>
				<td align="center"> 
				<b> Mandatory fields were marked with "*" </b>
				</td>
			</tr>	
			<tr>	
				<td align="center"> 
					<br/>
					<%
						 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
					%>
							<html:button property="save_button" value="Save" onclick="add_action_for_form(1, -1);"/>				
					<%
						}
					%>		
					
					<html:button property="refresh_button" value="Refresh" onclick="add_action_for_form(3, -1);"/> 
				
					<% 
						if (request.isUserInRole(WebConstants.Security_Permission_ADMIN) && id == -1){
					%>
							<html:button property="reset_button" value="Reset" onclick="add_action_for_form(2, -1);"/> 
					<%
						}
					%>

					<% 
						if (request.isUserInRole(WebConstants.Security_Permission_ADMIN) && id != -1){ %>
							<html:button property="delete_button" value="Delete" onclick="add_action_for_form(4, -1);" 
								disabled="<%=Boolean.parseBoolean((String)request.getAttribute("deleteDeactivation")) %>" />				
					<%
						}
					%>		
				</td>
			</tr>
			</table> <!-- left-side-table -->
		</td>
		<td>
		
		<%
			if (id != -1){
		%>	
			<table id="right-side-table" align="center" >
		<%
			 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
		%>
			<tr>
				<td>
					<h2> Create & Bind new IMPI
					<%
						out.println("<a href=\"/hss.web.console/IMPI_Load.do?id=-1&already_assigned_imsu_id=" + id + "\" > ");
					%>
							<img src="/hss.web.console/images/add_obj.gif" /> 
						</a>
					</h2>	
				</td>
			</tr>	
			
			<tr>
				<td>
					<br />
					<h2> Associate IMPI(s) </h2>				
					<table id="associate-impi-table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">										
					<tr class="even">
						<td>
							<b> IMPI Identity </b>
						</td>

						<td>
								<html:text property="impi_identity" value="" styleClass="inputtext" size="32" />
						</td>
						<td>
								<html:button property="add_impi" value="Add" onclick="add_action_for_form(12, -1);"/>
						</td>
					</tr>	
					</table> <!-- associate-impi-table -->
				</td>
			</tr>	
			<%
			} // endif (Security_Permission_ADMIN)
			%>
			
			<tr>
				<td>
					<br />
					<h2> List of associated IMPIs </h2>
					<table id="list-associated-impi-table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">
					<tr class="header">
						<td class="header"> ID </td>			
						<td class="header"> IMPI Identity </td>
						<%
							 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
						%>
						<td class="header"> Delete </td>
						<%
							}
						%>
					</tr>
				<%
								if (associated_IMPIs != null && associated_IMPIs.size() > 0){
									Iterator it = associated_IMPIs.iterator();
									IMPI crt_IMPI = null;
										
									while (it.hasNext()){									
										crt_IMPI = (IMPI) it.next();
				%>
										<tr class="even">																			
											<td>  <%= crt_IMPI.getId() %></td>
											<td> 
												<a href="/hss.web.console/IMPI_Load.do?id=<%= crt_IMPI.getId() %>" > 
													<%= crt_IMPI.getIdentity() %>
												</a>	
											</td>
											
											<%
												 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
											%>
											<td> 
												<input type="button" name="delete_impi" 
													"value="Delete" onclick="add_action_for_form(5, <%= crt_IMPI.getId() %>);"/>													
											</td>
											<%
												}
											%>
										</tr>											
				<%			
									}
								}
				%>
					</table> <!-- list-associated-impi-table -->
				</td>
			</tr>
		</table> <!-- right-side-table -->
	<%	
		}
	%>		
		</td>
	</tr>
	</table>	<!-- main-table -->
	</html:form>
</body>
</html>
