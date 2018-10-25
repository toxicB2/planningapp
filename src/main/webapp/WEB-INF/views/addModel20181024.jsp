<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">-->
<meta http-equiv="Content-Type" content="application/json; charset=UTF-8">

<title>${title}</title>
</head>
<body>
	<h2>${title}:</h2>
	<c:if test="${not empty error}">
		<span style="color:red;">
			${error}<br>
		</span>
	</c:if>
	
	<c:choose>
	    <c:when test="${not empty model.DataType}">
	        <c:set var="actionPath" value="${pageContext.request.contextPath}/doAdd${model.DataType}"/>
	    </c:when>
	    <c:otherwise>
	        <c:set var="actionPath" value="${pageContext.request.contextPath}/doAddModel"/>
	    </c:otherwise>
	</c:choose>
	
	<form method="post" action="${actionPath}">	
        <table style="padding-top: 30px; border: 0.5px;">	
	        <c:forEach items="${model.data}" var="oneModel" varStatus="status">
                <tr>
                    <td>
	                    ${oneModel.key}
	                </td>
	                <td>
	                    <c:set var = "check" scope = "session" value = "${0}"/>
	                    <c:forEach items="${model}" var="oneItem" varStatus="status">
	                        <c:if test="${oneItem.key == oneModel.key}">
                                <select name="${oneItem.key}" id = "${oneItem.key}">
                                    <c:forEach items="${oneItem.value}" var="role">
                                        <option value="${role.key}" <c:if test="${role.key eq oneModel.value}">selected="selected"</c:if> >
                                            ${role.value}
                                        </option>
                                    </c:forEach>
                                </select>  
                                <c:set var = "check" scope = "session" value = "${1}"/>
                            </c:if>
                        </c:forEach>
                        <c:if test="${check != 1}">
                            <input type="text" name="${oneModel.key}" value="${oneModel.value}">
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${model.actionPermitted == true}">
                <tr>
				    <td colspan="2">
					    <button type="submit">Submit</button>
				    </td>
			    </tr>
			</c:if>      
        </table>
	</form>
</body>
</html>