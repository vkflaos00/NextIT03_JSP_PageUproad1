<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>NextIT</title>
</head>
<body>
 <!-- 12시 35분에 함 로그아웃시 세션에 들어가있는 로그인 정보 삭제 -->
 <% 
 	session.removeAttribute("memberVO");
 	response.sendRedirect(request.getContextPath()+"/login/login.jsp");
 %>
 
</body>
</html>