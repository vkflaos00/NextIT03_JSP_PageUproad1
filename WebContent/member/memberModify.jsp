<%@page import="kr.or.nextit.exception.BizNotEffectedException"%>
<%@page import="kr.or.nextit.exception.BizPasswordNotMatchedException"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<% request.setCharacterEncoding("utf-8"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>NextIT</title>
<link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath }/images/nextit_log.jpg" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/header.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/member.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/footer.css">
<script src="http://code.jquery.com/jquery-latest.min.js"></script>

</head>
<body>
<jsp:useBean id="member" class="kr.or.nextit.member.vo.MemberVO"></jsp:useBean>
<jsp:setProperty property="*" name="member"/>

<%
	System.out.println("member.toString():" + member.toString());

	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	try{
		conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:study");
		StringBuffer sb1  = new StringBuffer();
		sb1.append("SELECT count(mem_id) FROM member WHERE mem_id = ? AND mem_pass = ?");
		
		pstmt = conn.prepareStatement(sb1.toString());
		pstmt.setString(1, member.getMemId());
		pstmt.setString(2, member.getMemPass());
		
		rs = pstmt.executeQuery();
		
		rs.next();
		int rowCount = rs.getInt(1);
		System.out.println("rowCount : "+ rowCount);
		
		if(rowCount != 1) {
			throw new BizPasswordNotMatchedException();
		}else {
			StringBuffer sb2 = new StringBuffer();
			sb2.append(" UPDATE member SET 			");
			sb2.append("   mem_pass = ?				");
			sb2.append(" , mem_name = ?				");
			sb2.append(" , mem_bir = ?				");
			sb2.append(" , mem_zip = ?				");
			sb2.append(" , mem_add1 = ?				");
			sb2.append(" , mem_add2 = ?				");
			sb2.append(" , mem_hp = ?				");
			sb2.append(" , mem_job = ?				");
			sb2.append(" , mem_hobby = ?			");
			sb2.append(" , mem_edit_date = sysdate 	");
			sb2.append(" WHERE mem_id = ?			");
			
			pstmt = conn.prepareStatement(sb2.toString());
			
			int cnt = 1;
			pstmt.setString(cnt++, member.getMemPassNew());
			pstmt.setString(cnt++, member.getMemName());
			pstmt.setString(cnt++, member.getMemBir());
			pstmt.setString(cnt++, member.getMemZip());
			pstmt.setString(cnt++, member.getMemAdd1());
			pstmt.setString(cnt++, member.getMemAdd2());
			pstmt.setString(cnt++, member.getMemHp());
			pstmt.setString(cnt++, member.getMemJob());
			pstmt.setString(cnt++, member.getMemHobby());
			pstmt.setString(cnt++, member.getMemId());
			
			int resultCnt = pstmt.executeUpdate();
			if(resultCnt== 0){
				throw new BizNotEffectedException();
			}
		}
		
	}catch(BizPasswordNotMatchedException bpe){
		bpe.printStackTrace();
		request.setAttribute("bpe", bpe);
	}catch(BizNotEffectedException bne){
		bne.printStackTrace();
		request.setAttribute("bne", bne);
	}catch(Exception e){
		e.printStackTrace();
		request.setAttribute("se", e);
	}finally {
		if(rs != null){try{rs.close();}catch(Exception e){e.printStackTrace();}}
		if(pstmt != null){try{pstmt.close();}catch(Exception e){e.printStackTrace();}}
		if(conn != null){try{conn.close();}catch(Exception e){e.printStackTrace();}}
	}
%>

<div class="container">
	<c:if test="${bpe eq null and bne eq null and se eq null }">
		<h3>회원정보 수정 성공</h3>
		<div>
			<p>정상적으로 회원정보가 수정 되었습니다. 확인을 클릭하시면 매인페이지로 이동.</p>
			<div class="btn-area">
				<button type="button" onclick="location.href='${pageContext.request.contextPath}/home/home.jsp'">확인</button>
			</div>
		</div>
	</c:if>
	<c:if test="${bpe ne null }">
		<div>
			<h3>회원정보 수정  실패</h3>
			<p>입력하신 패스워드가 올바르지 않습니다. 다시 입력 부탁드립니다.</p>
			<div class="btn-area">
				<button type="button" onclick="history.back()">뒤로가기</button>
				<button type="button" onclick="location.href='${pageContext.request.contextPath}/home/home.jsp'">홈</button>
			</div>
		</div>
	</c:if>
	<c:if test="${bne ne null or se ne null }">
		<h3>회원정보 수정  실패</h3>
		<div class="alert alert-warning">
			<p> 회원 등록에 실패하였습니다. 전산실에 문의 부탁드립니다. 042-719-8850</p>
			<div class="btn-area">
				<button type="button" onclick="history.back()">뒤로가기</button>
				<button type="button" onclick="location.href='${pageContext.request.contextPath}/home/home.jsp'">홈</button>
			</div>
		</div>
	
	</c:if>
	
</div>



  
</body>
</html>