<%@page import="java.util.List"%>
<%@page import="kr.or.nextit.exception.DaoException"%>
<%@page import="kr.or.nextit.free.vo.FreeBoardVO"%>
<%@page import="kr.or.nextit.exception.BizNotEffectedException"%>
<%@page import="kr.or.nextit.free.service.IFreeBoardService"%>
<%@page import="kr.or.nextit.free.service.FreeBoardServiceImpl"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>NextIT</title>
<link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath }/images/nextit_log.jpg" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/header.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/freeBoardList.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/footer.css">
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">
	$(function(){
		// alert("freeList $(function(){})");
		$("#id_rowSizePerPage").change(function(){
			// alert("id_rowSizePerPage: " + this.value);
			location.href = "${pageContext.request.contextPath}/free/freeList.jsp?curPage=1&rowSizePerPage="+this.value;
		});
	});
</script>
</head>
<body>

	<jsp:useBean id="pagingVO" class="kr.or.nextit.common.vo.PagingVO"></jsp:useBean>
	<jsp:setProperty property="*" name="pagingVO"/>
<%
	IFreeBoardService freeBoardService = new FreeBoardServiceImpl();
	try{
		List<FreeBoardVO> freeBoardList = freeBoardService.getBoardList(pagingVO);
		request.setAttribute("freeBoardList", freeBoardList);
	}catch(BizNotEffectedException bne) {
		bne.printStackTrace();
		request.setAttribute("bne", bne);
	}catch(DaoException de) {
		de.printStackTrace();
		request.setAttribute("de",de);
	}
%>
<div id="wrap">
    <div class="header">
        <div class="top_nav">
            <!-- header 영역 -->
            <%@ include file="/header/header.jsp" %>
            
        </div>
    </div>
    <!-- header e -->

    <div class="intro_bg">
        <div class="intro_text">
            <h1>NextIT</h1>
            <h4>넥스트아이티</h4>
        </div>
    </div>
    <!-- intro_bg e -->

    <!-- 전체 영역잡기 -->
    <div class="contents">
        <!-- 사용할 영역잡기 -->
        <div class="content01">
            <div class="content01_h1">
                <h1>자유게시판</h1>
            </div>
            <c:if test="${bne ne null or de ne null }">
            	<div class="alert alert-warning">
            		목록을 불러오지 못하였습니다. 전산실에 문의 부탁드립니다. 042-719-8850
            	</div>
            	<div class="div_button">
            		<input type="button" onclick="histroy.back();" value="뒤로가기">
            	</div>
            </c:if>
            <c:if test="${ bne eq null and de eq null }">
            
            	<div class="rowSizePerPage">
            		<div>
            			전체 ${pagingVO.totalRowCount }건 조회 
            			<select id="id_rowSizePerPage" name="rowSizePerPage">
            				<c:forEach begin="10" end="50" step="10" var="i">
            					<option value="${i }" ${pagingVO.rowSizePerPage eq i ? "selected='selected'" : "" }> ${i }</option>
            				</c:forEach>
            			
            			</select>
            		</div>
            	</div>
            <!-- 리스트 -->
            <div id="div_table">
                <table>
                    <colgroup>
                        <col width="100">
                        <col width="150">
                        <col >
                        <col width="150">
                        <col width="150">
                        <col width="100">
                    </colgroup>
                    <thead>
                        <tr>
                            <th>글번호</th>
							<th>분류</th>
							<th>제목</th>
							<th>작성자</th>
							<th>등록일</th>
							<th>조회수</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${freeBoardList}" var="freeBoard">
                        	<tr>
                        		<td><c:out value="${freeBoard.rnum }"></c:out></td>
                        		<td><c:out value="${freeBoard.boCategoryNm }"></c:out></td>
                        		<td><a href="${pageContext.request.contextPath}/free/freeView.jsp?boNo=${freeBoard.boNo }">
                        				<c:out value="${freeBoard.boTitle }"></c:out>
                        			</a></td>
                        		<td><c:out value="${freeBoard.boWriter }"></c:out></td>
                        		<td><c:out value="${freeBoard.boRegDate }"></c:out></td>
                        		<td><c:out value="${freeBoard.boHit }"></c:out></td>
                        	</tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            </c:if>

            <!-- paging -->
            <div class="div_paging">
               <!--  <ul class="pagination">
                    <li><a href="#">&laquo;</a></li>  	 
                    <li><a href="#">&lt;</a></li>		 
                    <li><a href="#">1</a></li>
                    <li><a href="#">2</a></li>
                    <li><a href="#">3</a></li>
                  	<li><a href="#">4</a></li>
                    <li><a href="#">5</a></li>
                    <li><a href="#">6</a></li>
                    <li><a href="#">7</a></li>
                    <li><a href="#">8</a></li>
                    <li><a href="#">9</a></li>
                    <li><a href="#">10</a></li>
                    <li><a href="#">&gt;</a></li>		 
                    <li><a href="#">&raquo;</a></li>   
                </ul> -->
                
                <ul class="pagination">
                	<!-- "<<" : 클릭할 경우 이전 10번단위 페이지에서 마지막 페이지 요청  
                		23페이지인 경우 "<<"클릭하면 20페이지로 이동
                	-->
                	<c:if test="${pagingVO.firstPage gt 10 }">
                		<li><a href="${pageContext.request.contextPath }/free/freeList.jsp?curPage=${pagingVO.firstPage-1}&rowSizePerPage=${pagingVO.rowSizePerPage}">&laquo;</a></li>
                	</c:if>
                	
                	<!-- " < " : 이전 페이지  -->
                	<c:if test="${pagingVO.curPage ne 1 }">
                		<li><a href="${pageContext.request.contextPath }/free/freeList.jsp?curPage=${pagingVO.curPage-1}&rowSizePerPage=${pagingVO.rowSizePerPage}">&lt;</a></li>
                	</c:if>
                	
                	<!-- 페이지 -->
                	<c:forEach begin="${pagingVO.firstPage }" end="${pagingVO.lastPage }" step="1" var="i">
                		<c:if test="${pagingVO.curPage ne i}">
                			<!-- 클릭 가능한 경우 -->
                			<li><a href="${pageContext.request.contextPath }/free/freeList.jsp?curPage=${i}&rowSizePerPage=${pagingVO.rowSizePerPage}">${i }</a></li>
                		</c:if>
                		<c:if test="${pagingVO.curPage eq i}">
                			<!-- 클릭할 필요가 없는 경우 -->
                			<li><a href="#" class="curPage_a">${i }</a></li>
                		</c:if>
                		
                	</c:forEach>
                	
                	<!-- ">", ">>" -->
                	<c:if test="${pagingVO.lastPage ne pagingVO.totalPageCount }">
                		<li><a href="${pageContext.request.contextPath }/free/freeList.jsp?curPage=${pagingVO.curPage+1}&rowSizePerPage=${pagingVO.rowSizePerPage}">&gt;</a></li>
                		<li><a href="${pageContext.request.contextPath }/free/freeList.jsp?curPage=${pagingVO.lastPage+1}&rowSizePerPage=${pagingVO.rowSizePerPage}">&raquo;</a></li>
                	</c:if>
                </ul>
                <div class="div_board_write">
                    <input type="button" onclick="location.href='${pageContext.request.contextPath}/free/freeForm.jsp'" value="글쓰기">
                </div>
            </div>
        </div>
    </div>

    <!-- footer -->
    <footer id="page_footer">
		<!-- footer영역 -->
		<%@ include file="/footer/footer.jsp" %>
    </footer>

</div>  


</body>
</html>