<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Resultados</title>
</head>
<body>
	<c:out value="${msg}"></c:out>
	<form action="search" method="post">
		<table align="center" border="1">
			<thead>
				<tr>
					<th>Nome Arquivo</th>
					<th>Caminho</th>
					<th>Download</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${resultados}" var="res">
					<tr>
						<td><c:out value="${res.nomeArquivo}"/></td>
						<td><c:out value="${res.link}"/></td>
						<td><a href="download?path=<c:out value='${res.link}'/>">Download</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</form>
</body>
</html>