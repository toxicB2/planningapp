<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${title}</title>
<script type="text/javascript">
	function canDelete (modelName) {
		return confirm("Are you sure you want to delete " + modelName + "?");
	}
</script>
</head>
<body>
<h2>${title}</h2>
<table style="padding-top: 30px; border: 0.5px;">

    <c:forEach items="${models.data[0]}" var="submodel" varStatus="status">
        <th bgcolor="#6FD75B">${submodel.key}</th>
    </c:forEach>
	
	<c:forEach items="${models.data}" var="model" varStatus="status">
    <tr>
        <c:forEach items="${model}" var="submodel" varStatus="status">
            <td>${submodel.value}</td>
        </c:forEach>
        <c:if test="${currentUserPermissions.modelWrite == true}">
            <td>
			    <a href="${models.type}/update/${model.ID}">Edit</a>   
		    </td>
		    <td>
			    <a href="${models.type}/delete/${model.ID}" onclick="return canDelete('${model.name}');">Delete</a>
		    </td>
	    </c:if>
    </tr>
    </c:forEach>
</table>
<p/>
<c:if test="${currentUserPermissions.modelWrite == true}">
    <button type="submit" onclick="location.href='addUser'">Add Model</button> <!-- Temp -->
</c:if>
</body>
</html>