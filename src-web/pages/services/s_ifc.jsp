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
<jsp:useBean id="attached_ifc" type="java.util.List" scope="request"></jsp:useBean>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title> Shared iFC </title>
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
			document.S_IFC_Form.nextAction.value="save";
			document.S_IFC_Form.submit();
			break;
		case 2:
			document.S_IFC_Form.nextAction.value="refresh";
			document.S_IFC_Form.submit();			
			break;
		case 3:
			document.S_IFC_Form.nextAction.value="reset";
			document.S_IFC_Form.reset();
			break;
		case 4:
			document.S_IFC_Form.nextAction.value="delete";
			document.S_IFC_Form.submit();			
			break;
		case 5:
			document.S_IFC_Form.nextAction.value="detach_ifc";
			document.S_IFC_Form.associated_ID.value = associated_ID;
			document.S_IFC_Form.submit();
			break;
				
		case 12:
			document.S_IFC_Form.nextAction.value="attach_ifc";
			document.S_IFC_Form.submit();
			break;
	}
}
</script>
</head>

<body>
	<table id="title-table" align="center" weight="100%" >
	<tr>
		<td align="center">
			<h1> Shared iFC Sets -Sh-iFC-</h1> 			
			<br/><br/>			
		</td>
	<tr>	
		<td align="left">
			<!-- Print errors, if any -->
			<jsp:include page="/pages/tiles/error.jsp"></jsp:include>
		</td>
	</tr>
	</table> <!-- title-table -->

	<html:form action="/S_IFC_Submit">
		<html:hidden property="nextAction" value=""/>
		<html:hidden property="associated_ID" value=""/>
		
		<table id="main-table" align="center" valign="middle">
		<tr>
			<td>
	 			<table id="top-side-table" border="0" align="center">						
	 			<tr>
 					<td>
						<table id="s_ifc_table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">						
						<tr bgcolor="#FFCC66">
							<td> ID-Set </td>
							<td><html:text property="id_set" readonly="true" styleClass="inputtext_readonly" style="width:100px;"/> </td>
						</tr>
						<tr bgcolor="#FFCC66">
							<td>Name* </td>
							<td><html:text property="name" styleClass="inputtext" style="width:200px;"/> </td>
						</tr>
						
			<%			if (id_set == -1){
			%>			
						<tr bgcolor="#FFCC66">
							<td> IFC* </td>							
							<td>
								<html:select property="id_ifc" name="S_IFC_Form" styleClass="inputtext" size="1" style="width:200px;">
									<html:option value="-1">Select iFC...</html:option>
									<html:optionsCollection name="S_IFC_Form" property="select_ifc" label="name" value="id"/>
								</html:select>	
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
						</table>
					</td>
				</tr>
	 			<tr>
 					<td>
						<table align="center">			
						<tr>
							<td align="center"> 
							<b> Mandatory fields were marked with "*" </b>
						</td>
						</tr>	
						
						<tr>
							<td align=center> <br/>
								<%
									 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
								%>													
								<html:button property="save_button" value="Save" onclick="add_action_for_form(1);"/>				
								<%
									}
								%>
								<html:button property="refresh_button" value="Refresh" onclick="add_action_for_form(2);"/> 
								<% 
									if (request.isUserInRole(WebConstants.Security_Permission_ADMIN) && id_set == -1){ 
								%>
									<html:button property="reset_button" value="Reset" onclick="add_action_for_form(3);"/> 
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
						</table>
					</td>
				</tr>							
				</table>		
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
					<br /><br />
					<b>Attach iFC </b>
					<table id="attach-ifc-table" width="400" class="as" border="0" cellspacing="1" align="center" style="border:2px solid #FF6600;">
					<tr class="even">
						<td>
							<html:select property="id_ifc" name="S_IFC_Form" styleClass="inputtext" size="1"  value="">
								<html:option value="-1">Select iFC...</html:option>
								<html:optionsCollection name="S_IFC_Form" property="select_ifc" label="name" value="id"/>
							</html:select>	
						</td>

						<td>
							Priority	
						</td>
						<td>	
							<html:text property="priority" styleClass="inputtext" style="width:100px;"/>
						</td>
						
						<td>
							<html:button property="cap_attach_button" value="Attach" onclick="add_action_for_form(12, -1);"/>
							<br />
						</td>
					</tr>	
					</table> <!-- attach-ifc-->
				</td>
			</tr>
			<tr>
				<td>
					<table id="warning-table" class="as" width="400" border="0" cellspacing="1" align="center" style="border:2px solid #FF6600;">
					<tr>
						<td>
							<font color="#FF0000">
								Warning: Priority values defined here can overwrite priority values defined in SP-iFC setup! 
							</font>						
						</td>
					</tr>
					</table>
				</td>
			</tr>
			<%
				} // endif ADMIN
			%>
			<tr>
				<td>		
					<br />
					<b>List of attached IFCs </b>
					<table id="list-attached-ifcs" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">
					<tr class="header">
						<td class="header"> ID </td>			
						<td class="header"> Name </td>
						<td class="header"> Priority </td>
						<%
							 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
						%>														
								<td class="header"> Detach </td>
						<%
							}
						%>
					</tr>
					<%
						if (attached_ifc != null){
							Iterator it = attached_ifc.iterator();
							Shared_IFC_Set s_ifc_set = null;
							int idx = 0;
							IFC ifc = null;
							while (it.hasNext()){
								s_ifc_set = (Shared_IFC_Set) it.next();
								ifc = IFC_DAO.get_by_ID(hibSession, s_ifc_set.getId_ifc());												
					%>
							<tr class="<%= idx % 2 == 0 ? "even" : "odd" %>">																			
								<td>  <%= ifc.getId() %></td>												
										
								<td>  
									<a href="/hss.web.console/IFC_Load.do?id=<%= ifc.getId() %>" > 
										<%= ifc.getName() %>
									</a>	
								</td>

								<td>  
									<%=s_ifc_set.getPriority()%>
								</td>
								
								<%
									 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
								%>																
								<td> 									
									<%
										if (((String)request.getAttribute("detachDeactivation")).equals("true")){
									%>
											<input type="button" name="detach_ifc" "value="Detach" onclick="add_action_for_form(5, <%= ifc.getId() %>);" disabled/>	
									<%
										}
										else{
									%>
											<input type="button" name="detach_ifc" "value="Detach" onclick="add_action_for_form(5, <%= ifc.getId() %>);" />										
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
					</table> <!-- list-attached-ifcs -->
				</td>	
			<%
				}
			%>	
		</tr>
		</table> <!-- main-table -->		
	</html:form>
</body>
</html>
