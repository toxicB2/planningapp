<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${title}</title>
</head>
<body>
	<h2>${title}:</h2>
	<c:if test="${not empty error}">
		<span style="color:red;">
			${error}<br>
		</span>
	</c:if>
	
	<c:set var="actionPath" value="${pageContext.request.contextPath}/doAddModel"/>
	<form method="post" action="${actionPath}">
		<table>
			<tr>
				<td>Name:</td>
				<td><input type="text" name="name" value="${model.name}"> </td>
			</tr>
			<tr>
				<td>Category:</td>
				<td><input type="text" name="category" value="${model.category}"> </td>
			</tr>
			<tr>
				<td>Origination Area:</td>
				<td><input type="text" name="origination_area" value="${model.origination_area}"> </td>
			</tr>
			<tr>
				<td>Risk:</td>
				<td><input type="text" name="risk" value="${model.risk}"> </td>
			</tr>
			<tr>
				<td>Last Validation Sign Off Date:</td>
				<td><input type="text" name="val_so" value="${model.val_so}"> </td>
			</tr>
		    <tr>
				<td>Last Annual Model Review Sign Off Date:</td>
				<td><input type="text" name="amr_so" value="${model.amr_so}"> </td>
			</tr>
		    <tr>
				<td>Has the model been validated:</td>
				<td><input type="text" name="is_validated" value="${model.is_validated}"> </td>
			</tr>
		    <!--  <tr>
				<td>Responsible Manager:</td>
				<td><input type="text" name="manager_id" value="${model.manager_id}"> </td>
			</tr> -->
			<tr>
				<td colspan="2">
					<button type="submit">Submit</button>
				</td>
			</tr>
		</table>
		<input type="hidden" name="id" value="${model.ID}">
	</form>
</body>
</html>