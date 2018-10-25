<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>List of models</title>
<script type="text/javascript">
	function canDelete (modelName) {
		return confirm("Are you sure you want to delete " + modelName + "?");
	}
</script>
</head>
<body>
<h2>Models:</h2>
<table style="padding-top: 30px; border: 0.5px;">

    <c:forEach items="${models[0]}" var="submodel" varStatus="status">
        <th bgcolor="#6FD75B">${submodel.key}</th>
    </c:forEach>
	
	<c:forEach items="${models}" var="model" varStatus="status">
    <tr>
        <c:forEach items="${model}" var="submodel" varStatus="status">
            <td>${submodel.value}</td>
        </c:forEach>
    </tr>
    </c:forEach>
	<!--<c:forEach items="${models}" var="model">
		<tr>
		    
			<td>${model.ID}</td>
			<td>${model.name}</td>
			<td>${model.category}</td>
			<td>${model.origination_area}</td>
			<td>${model.risk}</td>
			<td>${model.val_so}</td>
			<td>${model.amr_so}</td>
			<td>${model.is_validated}</td>
			<td>${model.manager_id}</td>
			<td>
				<a href="model/update/${model.ID}">Edit</a>
			</td>
			<td>
				<a href="model/delete/${model.ID}" onclick="return canDelete('${model.name}');">Delete</a>
			</td>
		</tr>
	</c:forEach>-->
</table>
<p/>
<button type="submit" onclick="location.href='addModel'">Add Model</button>
</body>
</html>