<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>

<head>
	<title>Error Page</title>
</head>

<body bgcolor="#FFFFFF">
<bean:message key="error.exceptionTitle" />
<BR>
<table border="1">
	<tr>
		<td><pre>
<bean:write name="exceptionMessage" />
</pre></td>
	</tr>
</table>
</body>
</html>
