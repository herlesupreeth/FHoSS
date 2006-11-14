<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ page import="org.hibernate.Session"%>
<%@ page import="de.fhg.fokus.hss.util.HibernateUtil" %>
<%@ page import="de.fhg.fokus.hss.model.Imsu" %>
<%@ page import="de.fhg.fokus.hss.model.Impu" %>
<%@ page import="de.fhg.fokus.hss.model.Impi" %>
<%@ page import="java.util.Iterator" %>
<%
	// This jsp access hibernate directly to collect
	// information about the hss data content.
	Session hibernateSession = HibernateUtil.currentSession();
	String imsuString = request.getParameter("imsuString");
	String impiString = request.getParameter("impiString");
	
%>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="/hss.web.console/style/fokus_ngni.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Info Page</title>
</head>
<body bgcolor="#FFFFFF">
<table id="main" height="100%">
	<tr id="inner_nav">
		<td id="bound_left">&nbsp;</td>
		<td id="level02" valign="top" bgcolor="#FFFFFF">
		<table cellpadding="0" cellspacing="0" border="0" bgcolor="#FFFFFF" align="left">
		<% 
			Iterator it = hibernateSession.createCriteria(de.fhg.fokus.hss.model.Imsu.class).list().iterator();
			Iterator itImpi = null;
			Iterator itImpu = null;
			while(it.hasNext()){
				Imsu imsu = (Imsu)it.next();
				if((imsuString != null)&&(imsu.getImsuId().intValue() == Integer.parseInt(imsuString))){
					out.println("<tr><td nowrap=\"nowrap\" width=\"18\"><img src=\"/hss.web.console/images/menu-images/menu_folder_open.gif\" width=\"18\" heigth=\"18\"></td><td colspan=\"3\"><a href=\"/hss.web.console/imsuShow.do?imsuId=" + imsu.getImsuId() + "\" target=\"content\">" + imsu.getName() + "</a></td></tr>");
					itImpi = imsu.getImpis().iterator();
					while(itImpi.hasNext()){
						Impi impi = (Impi)itImpi.next();
						if((impiString != null)&&(impi.getImpiId().intValue() == Integer.parseInt(impiString))){
							out.println("<tr><td nowrap=\"nowrap\" width=\"18\">");
							if(it.hasNext()){
								out.println("<img src=\"/hss.web.console/images/menu-images/menu_tee.gif\" width=\"18\" heigth=\"18\">");
							} else {
								out.println("<img src=\"/hss.web.console/images/menu-images/menu_corner.gif\" width=\"18\" heigth=\"18\">");
							}
							out.println("</td><td width=\"18\">");
							out.println("<img src=\"/hss.web.console/images/menu-images/menu_folder_open.gif\">");
							out.println("</td><td colspan=\"2\">");
							out.println("<a href=\"/hss.web.console/impiShow.do?impiId=" + impi.getImpiId() + "\" target=\"content\">" + impi.getImpiString() + "</a></td></tr>");
							itImpu = impi.getImpus().iterator();
							while(itImpu.hasNext()){
								Impu impu = (Impu) itImpu.next();
								out.println("<tr><td nowrap=\"nowrap\" width=\"18\">");
								if(it.hasNext()){
									out.println("<img src=\"/hss.web.console/images/menu-images/menu_bar.gif\" width=\"18\" heigth=\"18\">");
								} else {
									out.println("<img src=\"/hss.web.console/images/menu-images/menu_pixel.gif\" width=\"18\" heigth=\"18\">");
								}
								out.println("</td><td width=\"18\">");
								if (itImpi.hasNext()) {
									out.println("<img src=\"/hss.web.console/images/menu-images/menu_tee.gif\" width=\"18\" heigth=\"18\">");
								} else {
									out.println("<img src=\"/hss.web.console/images/menu-images/menu_corner.gif\" width=\"18\" heigth=\"18\">");
								}
								out.println("</td><td width=\"18\">");
								out.println("<img src=\"/hss.web.console/images/menu-images/m_world.gif\" width=\"18\" heigth=\"18\">");
								out.println("</td><td>");
								out.println("<a href=\"/hss.web.console/impuShow.do?impuId=" + impu.getImpuId() + "\" target=\"content\"><font color=\"" + (impu.getUserStatus().equals(Impu.USER_STATUS_REGISTERED) ? "#00FF00":"#FF0000")+ "\">" + (impu.getSipUrl().length() > 25 ? impu.getSipUrl().substring(0,22) + "..." : impu.getSipUrl()) + "</a></td></tr>");
								
							}
						} else {
							out.println("<tr><td nowrap=\"nowrap\" width=\"18\">");
							if(it.hasNext()){
								out.println("<img src=\"/hss.web.console/images/menu-images/menu_tee.gif\" width=\"18\" heigth=\"18\">");
							} else {
								out.println("<img src=\"/hss.web.console/images/menu-images/menu_corner.gif\" width=\"18\" heigth=\"18\">");
							}
							out.print("</td><td width=\"18\"><a href=\"?impiString="+ impi.getImpiId()+ "&imsuString=" + imsuString +"\">");
							out.println("<img src=\"/hss.web.console/images/menu-images/menu_folder_closed.gif\" width=\"18\" heigth=\"18\"></a>");
							out.println("</td><td colspan=\"2\">");
							out.println("<a href=\"/hss.web.console/impiShow.do?impiId=" + impi.getImpiId() + "\" target=\"content\">" + impi.getImpiString() + "</a></td></tr>");
						}
					}
				}else {
						out.println("<tr><td nowrap=\"nowrap\" align=\"left\" width=\"18\"><a href=\"?imsuString=" + imsu.getImsuId() + "\"><img src=\"/hss.web.console/images/menu-images/menu_folder_closed.gif\" width=\"18\" heigth=\"18\"></a></td><td colspan=\"3\"><a href=\"/hss.web.console/imsuShow.do?imsuId=" + imsu.getImsuId() + "\" target=\"content\">" + imsu.getName() + "</a></td></tr>");
				}
			}
		%>
		</table>
		</td>
		</tr>
		</table>
</body>
</html>
