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
<jsp:useBean id="attached_cap" type="java.util.List" scope="request"></jsp:useBean>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title> Capability Sets </title>
<link rel="stylesheet" type="text/css" href="/hss.web.console/style/fokus_ngni.css">
<%
	int id_set = Integer.parseInt(request.getParameter("id_set"));
	Session hibSession = HibernateUtil.getCurrentSession();
	HibernateUtil.beginTransaction();
%>

<script type="text/javascript" language="JavaScript">
function add_action_for_form(action, associated_ID) {
	switch(action){
		case 1:
			document.CapS_Form.nextAction.value="save";
			document.CapS_Form.submit();
			break;
		case 2:
			document.CapS_Form.nextAction.value="reset";
			document.CapS_Form.reset();
			break;
		
		case 3:
			document.CapS_Form.nextAction.value="refresh";
			document.CapS_Form.submit();			
			break;

		case 4:
			document.CapS_Form.nextAction.value="delete";
			document.CapS_Form.submit();
			break;
		
		case 5:
			document.CapS_Form.nextAction.value="detach_cap";
			document.CapS_Form.associated_ID.value = associated_ID;
			document.CapS_Form.submit();
			break;
				
		case 12:
			document.CapS_Form.nextAction.value="attach_cap";
			document.CapS_Form.submit();
			break;
			
	}
}
</script>
</head>

<body>

	<table id="title-table" align="center" weight="100%" >
	<tr>
		<td align="center">
			<h1> Capability Sets </h1> 			
			<br/><br/>			
		</td>
	<tr>	
		<td align="left">
			<!-- Print errors, if any -->
			<jsp:include page="/pages/tiles/error.jsp"></jsp:include>
		</td>
	</tr>
	</table> <!-- title-table -->

	<html:form action="/CapS_Submit">
		<html:hidden property="nextAction" value=""/>
		<html:hidden property="associated_ID" value=""/>		

		<table id="main-table" align="center" valign="middle">
			<tr>
				<td>
			 		<table id="cap-set-table" border="0" align="center" >						
			 		<tr>
		 				<td>
						 		<table id="fields-table" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">						
								<tr bgcolor="#FFCC66">
									<td> ID-Set </td>
									<td>
										<html:text property="id_set" readonly="true" styleClass="inputtext_readonly" style="width:100px;"/> 
									</td>
								</tr>
								
								<tr bgcolor="#FFCC66">
									<td> Name* </td>
									<td>
										<html:text property="name" styleClass="inputtext" style="width:200px;"/> 
									</td>
								</tr>

			<%					if (id_set == -1){
			%>			
								<tr bgcolor="#FFCC66">
									<td> Capability* </td>							
									<td>
										<html:select property="id_cap" name="CapS_Form" styleClass="inputtext" size="1" style="width:200px;">
											<html:option value="-1">Select Capability...</html:option>
											<html:optionsCollection name="CapS_Form" property="select_cap" label="name" value="id"/>
										</html:select>	
									</td>
								</tr>
								
								<tr bgcolor="#FFCC66">
									<td>	
										Type*
									</td>
									<td>	
										<html:select property="cap_type" styleClass="inputtext" size="1" style="width:200px;">
											<html:optionsCollection name="CapS_Form" property="select_cap_type" label="name" value="code"/>
										</html:select>
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
										<br />
										<%
											 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
										%>										
												<html:button property="save_button" value="Save" onclick="add_action_for_form(1, -1);"/>				
										<%
											}
										%>
										<html:button property="refresh_button" value="Refresh" onclick="add_action_for_form(3, -1);"/> 
										<% 
											if (request.isUserInRole(WebConstants.Security_Permission_ADMIN) && id_set == -1){ 
										%>
											<html:button property="reset_button" value="Reset" onclick="add_action_for_form(2, -1);"/> 
										<%
											}
										%>
						
										<% 
											if (request.isUserInRole(WebConstants.Security_Permission_ADMIN) && id_set != -1){ 
										%>
										<html:button property="delete_button" value="Delete" onclick="add_action_for_form(4, -1);" 
											disabled="<%=Boolean.parseBoolean((String)request.getAttribute("deleteDeactivation")) %>"/>				
										<%
											}
										%>												
									</td>
								</tr>
							</table>	<!-- buttons-table -->
						</td>
					</tr>
					</table> <!-- cap-set-table -->
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
					<h2>Attach Capability </h2>
					<table id="attach-cap-table" width="400" class="as" border="0" cellspacing="1" align="center" style="border:2px solid #FF6600;">
					<tr class="even">
						<td>
							<html:select property="id_cap" value="-1" name="CapS_Form" styleClass="inputtext" size="1" style="width:150px;">
								<html:option value="-1">Select Capability...</html:option>
								<html:optionsCollection name="CapS_Form" property="select_cap" label="name" value="id"/>
							</html:select>	
						</td>

						<td>	
							<html:select property="cap_type" value="-1" styleClass="inputtext" size="1" style="width:100px;">
								<html:optionsCollection name="CapS_Form" property="select_cap_type" label="name" value="code"/>
							</html:select>
						</td>
						
						<td>
							<html:button property="cap_attach_button" value="Attach" onclick="add_action_for_form(12, -1);"/>
							<br />
						</td>
					</tr>	
					</table> <!-- attach-cap-table -->
				</td>
			</tr>
			<%
				}//endif ADMIN
			%>
			<tr>
				<td>
					<br />		
					<h2> List of attached capabilities </h2>
					<table id="list-attached-cap-table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">
					<tr class="header">
						<td class="header"> ID </td>			
						<td class="header"> Name </td>
						<td class="header"> Mandatory </td>
						<%
							 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
						%>					
								<td class="header"> Detach </td>
						<%
							}
						%>
					</tr>
					<%
						if (attached_cap != null){
							Iterator it = attached_cap.iterator();
							CapabilitiesSet cap_set = null;
							int idx = 0;
							Capability cap = null;
							while (it.hasNext()){
								cap_set = (CapabilitiesSet) it.next();
								cap = Capability_DAO.get_by_ID(hibSession, cap_set.getId_capability());												
					%>
							<tr class="<%= idx % 2 == 0 ? "even" : "odd" %>">																			
								<td>  <%= cap.getId() %></td>												
										
								<td>  
									<a href="/hss.web.console/Cap_Load.do?id=<%= cap.getId() %>" > 
										<%= cap.getName() %>
									</a>	
								</td>

								<td>  
									<%
										Tuple tuple = (Tuple) WebConstants.select_cap_type.get(cap_set.getIs_mandatory()); 
										out.println(tuple.getName()); 
									%>
								</td>
									<%
										 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
									%>
								<td>
								
									<%
											if (((String)request.getAttribute("detachDeactivation")).equals("true")){
									%>
												<input type="button" name="detach_cap" "value="Detach" onclick="add_action_for_form(5, <%= cap.getId() %>);" disabled/>	
									<%
											}
											else{
									%>
												<input type="button" name="detach_cap" "value="Detach" onclick="add_action_for_form(5, <%= cap.getId() %>);" />										
									<%
											}
									%>
								</td>	
									<%											
										}
									%>												
									
							</tr>											
					<%			
								idx++;												
								}
							}
					%>
					</table> <!-- list-attached-cap-table -->					
				</td>	
			<%
				}
			%>	
		</tr>
		</table> <!-- main-table -->			
	</html:form>
</body>
</html>
