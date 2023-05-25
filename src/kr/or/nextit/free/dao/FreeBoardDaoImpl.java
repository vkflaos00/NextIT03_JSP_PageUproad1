package kr.or.nextit.free.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.or.nextit.common.vo.PagingVO;
import kr.or.nextit.exception.DaoException;
import kr.or.nextit.free.vo.FreeBoardVO;

public class FreeBoardDaoImpl implements IFreeBoardDao{

	@Override
	public int insertBoard(FreeBoardVO freeBoard) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:study");
			StringBuffer sb1 = new StringBuffer();
			sb1.append("  SELECT 'B01-'||TO_CHAR(SYSDATE,'YYYY')||'-'||LPAD((select count(bo_no)+1 from free_board),6,'0')AS bo_no FROM DUAL ");
			
			pstmt = conn.prepareStatement(sb1.toString());
			rs = pstmt.executeQuery();
			
			String bo_no = null;
			if(rs.next()) {
				bo_no = rs.getString("bo_no");
			}
			System.out.println("bo_no: " + bo_no);
			
			StringBuffer sb2 = new StringBuffer();
			sb2.append(" INSERT INTO free_board (                              	");
			sb2.append("      bo_no         , bo_title    , bo_category        	");
			sb2.append("    , bo_writer     , bo_pass     , bo_content         	");
			sb2.append("    , bo_ip         , bo_hit      , bo_reg_date        	");
			sb2.append("    , bo_del_yn	    				                  	");
			sb2.append(" ) VALUES  (                                           	");
			sb2.append("      ?  			, ? 			, ?                	");
			sb2.append("    , ?             , ?           	, ?                	");
			sb2.append("    , ?             , 0           	, sysdate           ");
			sb2.append("    , 'N'				                              	"); 
			sb2.append(" )                                                     	");

			pstmt= conn.prepareStatement(sb2.toString());
			int cnt=1;

			pstmt.setString(cnt++, bo_no);
			pstmt.setString(cnt++, freeBoard.getBoTitle());
			pstmt.setString(cnt++, freeBoard.getBoCategory());
			pstmt.setString(cnt++, freeBoard.getBoWriter());
			pstmt.setString(cnt++, freeBoard.getBoPass());
			pstmt.setString(cnt++, freeBoard.getBoContent());
			pstmt.setString(cnt++, freeBoard.getBoIp());

			int resultCnt= pstmt.executeUpdate();
			return resultCnt;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException();
		} finally {
			if(rs!=null){try{rs.close();} catch(Exception e){} }
			if(pstmt!=null){try{pstmt.close();} catch(Exception e){} }
			if(conn!=null){try{conn.close();}catch(Exception e){} }
		}
	}

	@Override
	public List<FreeBoardVO> getBoardList(PagingVO pagingVO) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:study");
			StringBuffer sb = new StringBuffer();                                                          
			sb.append("			SELECT                                                                     ");
			sb.append("			      d.*                                                                  ");
			sb.append("			FROM (                                                                     ");
			sb.append("			    SELECT                                                                 ");
			sb.append("			          rownum as rnum                                                   ");
			sb.append("			        , c.*                                                              ");
			sb.append("			    FROM (                                                                 ");
			sb.append("			             select  				                                       ");
			sb.append("			              bo_no   				                                       ");
			sb.append("			             ,bo_title 			                                           ");
			sb.append("			             ,bo_category   		                                       ");
			sb.append("			             ,bo_writer 			                                       ");
			sb.append("			             ,bo_pass    			                                       ");
			sb.append("			             ,bo_content 			                                       ");
			sb.append("			             ,bo_ip    			                                           ");
			sb.append("			             ,bo_hit 				                                       ");
			sb.append("			             ,to_char(bo_reg_date, 'YYYY-MM-DD') as bo_reg_date   	       ");
			sb.append("			             ,to_char(bo_mod_date, 'YYYY-MM-DD') as bo_mod_date 	       ");
			sb.append("			             ,bo_del_yn 			                                       ");
			sb.append("			             ,bo_del_id 			                                       ");
			sb.append("			             ,bo_del_date 			                                	"	);						
			sb.append("			             ,b.comm_nm AS bo_category_nm							       ");
			sb.append("			             FROM free_board a, comm_code b						           ");
			sb.append("			             WHERE a.bo_category = b.comm_cd						       ");
			sb.append("			             AND a.bo_del_yn = 'N'									       ");
			sb.append("			             ORDER BY bo_no                                                ");
			sb.append("			     ) c                                                                   ");
			sb.append("			     ORDER BY rnum desc )d                                                 ");
			sb.append("			     WHERE rnum BETWEEN ? AND ?	                                           ");
			
			
			
			pstmt = conn.prepareStatement(sb.toString());
			
			int cnt = 1;
			pstmt.setInt(cnt++, pagingVO.getLastRow());
			pstmt.setInt(cnt++, pagingVO.getFirstRow());
			rs = pstmt.executeQuery();
			
			List<FreeBoardVO> freeBoardList = new ArrayList<FreeBoardVO>();
			
			while(rs.next()) {
				FreeBoardVO freeBoard = new FreeBoardVO();
				freeBoard.setRnum(rs.getString("rnum"));
				freeBoard.setBoNo(rs.getString("bo_no"));
				freeBoard.setBoTitle(rs.getString("bo_title"));
				freeBoard.setBoCategory(rs.getString("bo_category"));
				freeBoard.setBoWriter(rs.getString("bo_writer"));
				//패스워드는 가져가만 안됨
				//freeBoard.setBoPass(rs.getString("bo_pass"));  
				freeBoard.setBoContent(rs.getString("bo_content"));
				freeBoard.setBoIp(rs.getString("bo_ip"));
				freeBoard.setBoHit(rs.getInt("bo_hit"));
				freeBoard.setBoRegDate(rs.getString("bo_reg_date"));
				freeBoard.setBoModDate(rs.getString("bo_mod_date"));
				freeBoard.setBoDelYn(rs.getString("bo_del_yn"));
				freeBoard.setBoDelId(rs.getString("bo_del_id"));
				freeBoard.setBoDelDate(rs.getString("bo_del_date"));
				freeBoard.setBoCategoryNm(rs.getString("bo_category_nm"));
				
				freeBoardList.add(freeBoard);
			}
			
			return freeBoardList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException();
		} finally {
			if(rs!=null){try{rs.close();} catch(Exception e){} }
			if(pstmt!=null){try{pstmt.close();} catch(Exception e){} }
			if(conn!=null){try{conn.close();}catch(Exception e){} }
		}
	}

	@Override
	public int getTotalRowCount() {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:study");
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT count(*) 			");
			sb.append(" FROM free_board 			");
			sb.append(" WHERE bo_del_yn = 'N'		");
			
			pstmt = conn.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				int resultCnt = rs.getInt(1);
				return resultCnt;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException();
		} finally {
			if(rs!=null){try{rs.close();} catch(Exception e){} }
			if(pstmt!=null){try{pstmt.close();} catch(Exception e){} }
			if(conn!=null){try{conn.close();}catch(Exception e){} }
		}
		
		
		return 0;
	}

	@Override
	public FreeBoardVO getBoard(String boNo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:study");
			StringBuffer sb = new StringBuffer();
			
			sb.append("	SELECT 													 ");
			sb.append(" 	  bo_no     	        							 ");
			sb.append("		, bo_title  	       								 ");
			sb.append("		, bo_category        								 ");
			sb.append("		, bo_writer 	       								 ");
			sb.append("		, bo_pass   	       								 ");
			sb.append("		, bo_content 	    								 ");
			sb.append("		, bo_ip     	        							 ");
			sb.append("		, bo_hit     	    								 ");
			sb.append("		,to_char(bo_reg_date, 'YYYY-MM-DD') as bo_reg_date   ");
			sb.append("	 	,to_char(bo_mod_date, 'YYYY-MM-DD') as bo_mod_date 	 ");
			sb.append("	 	,bo_del_yn 			                                 ");
			sb.append("	 	,bo_del_id 			                                 ");
			sb.append("	 	,bo_del_date 			                             ");						
			sb.append("	 	,b.comm_nm AS bo_category_nm						 ");
			sb.append("	 	FROM free_board a, comm_code b						 ");
			sb.append("	 	WHERE a.bo_category = b.comm_cd						 ");
			sb.append("	 	AND a.bo_no = ?										 ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, boNo);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				FreeBoardVO freeBoard=new FreeBoardVO();
				freeBoard.setBoNo(rs.getString("bo_no"));
				freeBoard.setBoTitle(rs.getString("bo_title"));
				freeBoard.setBoCategory(rs.getString("bo_category"));
				freeBoard.setBoWriter(rs.getString("bo_writer"));
				freeBoard.setBoPass(rs.getString("bo_pass"));
				freeBoard.setBoContent(rs.getString("bo_content"));
				freeBoard.setBoIp(rs.getString("bo_ip"));
				freeBoard.setBoHit(rs.getInt("bo_hit"));
				freeBoard.setBoRegDate(rs.getString("bo_reg_date"));
				freeBoard.setBoModDate(rs.getString("bo_mod_date"));
				freeBoard.setBoDelYn(rs.getString("bo_del_yn"));
				freeBoard.setBoDelId(rs.getString("bo_del_id"));
				freeBoard.setBoDelDate(rs.getString("bo_del_date"));
				freeBoard.setBoCategoryNm(rs.getString("bo_category_nm"));
				
				return freeBoard;
			}
		} catch (Exception e) {
			throw new DaoException("getBoard" + e.getMessage(), e);
		} finally {
			if(rs!=null){try{rs.close();} catch(Exception e){} }
			if(pstmt!=null){try{pstmt.close();} catch(Exception e){} }
			if(conn!=null){try{conn.close();}catch(Exception e){} }
		}
		return null;
	}

	@Override
	public int increaseHit(String boNo) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn=DriverManager.getConnection("jdbc:apache:commons:dbcp:study");
			StringBuffer sb = new StringBuffer();
			sb.append("	 UPDATE free_board		");
			sb.append("	 SET					");
			sb.append("	 bo_hit = bo_hit + 1	");
			sb.append("	 WHERE 1=1				");
			sb.append("	 AND bo_no = ?			");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, boNo);
			
			int resultCnt = pstmt.executeUpdate();
			return resultCnt;
		} catch (Exception e) {
			throw new DaoException("getBoard" + e.getMessage(), e);
		} finally {
			if(pstmt!=null){try{pstmt.close();} catch(Exception e){} }
			if(conn!=null){try{conn.close();}catch(Exception e){} }
		}
	}
	
	
	
	
	
	
	
}
