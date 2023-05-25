package kr.or.nextit.member.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.or.nextit.exception.BizNotEffectedException;
import kr.or.nextit.exception.DaoException;
import kr.or.nextit.member.vo.MemberVO;

public class MemberDaoImpl implements IMemberDao{

	@Override
	public MemberVO getMember(String memId) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		
		try {
			conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:study");
			StringBuffer sb = new StringBuffer();
			
			sb.append(" SELECT 		            ");
			sb.append("        mem_id           ");
			sb.append("        ,mem_pass        ");
			sb.append("        ,mem_name        ");
			sb.append("        ,mem_bir         ");
			sb.append("        ,mem_zip         ");
			sb.append("        ,mem_add1        ");
			sb.append("        ,mem_add2        ");
			sb.append("        ,mem_hp          ");
			sb.append("        ,mem_mail        ");
			sb.append("        ,mem_job         ");
			sb.append("        ,mem_hobby       ");
			sb.append("        ,mem_mileage     ");
			sb.append("        ,mem_del_yn      ");
			sb.append("        ,mem_join_date   ");
			sb.append("        ,mem_edit_date   ");
			sb.append(" FROM member             ");
			sb.append(" WHERE mem_id = ?        ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, memId);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				MemberVO member = new MemberVO();
				member.setMemId(rs.getString("mem_id")); 
				member.setMemPass(rs.getString("mem_pass")); 
				member.setMemName(rs.getString("mem_name"));  	  	
				member.setMemBir(rs.getString("mem_bir"));    		
				member.setMemZip(rs.getString("mem_zip"));  
				member.setMemAdd1(rs.getString("mem_add1"));
				member.setMemAdd2(rs.getString("mem_add2"));
				member.setMemHp(rs.getString("mem_hp"));    		
				member.setMemMail(rs.getString("mem_mail"));    		 
				member.setMemJob(rs.getString("mem_job"));    		
				member.setMemHobby(rs.getString("mem_hobby"));    		
				member.setMemMileage(rs.getInt("mem_mileage"));  
				member.setMemDelYn(rs.getString("mem_del_yn"));  
				member.setMemDelYn(rs.getString("mem_join_date"));  
				member.setMemDelYn(rs.getString("mem_edit_date"));  
				
				System.out.println("member.toString(): " + member.toString());
				return member;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException();
		}finally {
			if(rs != null){try{rs.close();}catch(Exception e){e.printStackTrace();}}
			if(pstmt != null){try{pstmt.close();}catch(Exception e){e.printStackTrace();}}
			if(conn != null){try{conn.close();}catch(Exception e){e.printStackTrace();}}
		}
		return null;
	}

	public int insertMember(MemberVO member) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:study");
			StringBuffer sb = new StringBuffer();
			sb.append(" insert into member( 			 				 ");
			sb.append("      mem_id   	, mem_pass		, mem_name       ");
			sb.append("    , mem_bir  	, mem_zip		, mem_add1       ");
			sb.append("    , mem_add2 	, mem_hp		, mem_mail       ");
			sb.append("    , mem_job  	, mem_hobby		, mem_mileage  	 ");
			sb.append("    , mem_del_yn , mem_join_date , mem_edit_date  ");
			sb.append(" )values(										 ");
			sb.append("   	?				, ?			, ?				 ");
			sb.append("   	, ?				, ?			, ?	             ");
			sb.append("   	, ?				, ?			, ?	             ");
			sb.append("   	, ?				, ?			, 0  			 ");
			sb.append("   	, 'N'           , sysdate   , sysdate		 ");
			sb.append(" )												 ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			int cnt = 1;
			pstmt.setString(cnt++, member.getMemId());
			pstmt.setString(cnt++, member.getMemPass());
			pstmt.setString(cnt++, member.getMemName());
			pstmt.setString(cnt++, member.getMemBir()); 
			pstmt.setString(cnt++, member.getMemZip());
			pstmt.setString(cnt++, member.getMemAdd1());
			pstmt.setString(cnt++, member.getMemAdd2());
			pstmt.setString(cnt++, member.getMemHp());
			pstmt.setString(cnt++, member.getMemMail());
			pstmt.setString(cnt++, member.getMemJob());
			pstmt.setString(cnt++, member.getMemHobby());
			
			int resultCnt = pstmt.executeUpdate();
			
			System.out.println("insertMember resultCnt : " + resultCnt);
			
			return resultCnt;

		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException();
		}finally {
			if(rs != null){try{rs.close();}catch(Exception e){e.printStackTrace();}}
			if(pstmt != null){try{pstmt.close();}catch(Exception e){e.printStackTrace();}}
			if(conn != null){try{conn.close();}catch(Exception e){e.printStackTrace();}}
		}
	}

	@Override
	public int insertUserRole(MemberVO member) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:study");
			StringBuffer sb = new StringBuffer();
			sb.append("insert into member_role(user_id, user_role, user_role_nm) values( ?, 'me', 'MEMBER')");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, member.getMemId());
			int resultCnt = pstmt.executeUpdate();
			
			System.out.println("insertUserRole resultCnt: " + resultCnt);
			return resultCnt;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException();
		}finally {
			if(pstmt != null){try{pstmt.close();}catch(Exception e){e.printStackTrace();}}
			if(conn != null){try{conn.close();}catch(Exception e){e.printStackTrace();}}
		}
		
	}

	@Override
	public MemberVO loginCheck(MemberVO member) {
		
		String memId = member.getMemId();
		String memPass = member.getMemPass();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:study");
			StringBuffer sb1 = new StringBuffer();
			sb1.append(" SELECT   											 	");
			sb1.append("      mem_id   	, mem_pass			, mem_name       	");
			sb1.append("    , mem_bir  	, mem_zip			, mem_add1       	");
			sb1.append("    , mem_add2 	, mem_hp			, mem_mail       	");
			sb1.append("    , mem_job  	, mem_hobby			, mem_mileage  	 	");
			sb1.append("    , mem_del_yn , mem_join_date 	, mem_edit_date 	");
			sb1.append("    FROM member										 	");
			sb1.append("    WHERE mem_id = ?								 	");
			sb1.append("    AND mem_pass = ?								 	");
			sb1.append("    AND mem_del_yn = 'N'								");
			
			pstmt = conn.prepareStatement(sb1.toString());
			pstmt.setString(1, memId);
			pstmt.setString(2, memPass);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				//member.setMemId(rs.getString("mem_id")); //사실 이미 담겨있으므로 필요없음 
				//member.setMemPass(rs.getString("mem_pass")); //사실 이미 담겨있으므로 필요없음
				member.setMemName(rs.getString("mem_name"));  	  	
				member.setMemBir(rs.getString("mem_bir"));    		
				member.setMemZip(rs.getString("mem_zip"));  
				member.setMemAdd1(rs.getString("mem_add1"));
				member.setMemAdd2(rs.getString("mem_add2"));
				member.setMemHp(rs.getString("mem_hp"));    		
				member.setMemMail(rs.getString("mem_mail"));    		 
				member.setMemJob(rs.getString("mem_job"));    		
				member.setMemHobby(rs.getString("mem_hobby"));    		
				member.setMemMileage(rs.getInt("mem_mileage"));  
				member.setMemDelYn(rs.getString("mem_del_yn"));
				member.setMemJoinDate(rs.getString("mem_join_date"));  
				member.setMemEditDate(rs.getString("mem_edit_date"));
				
				System.out.println("loginCheck member.toString: " + member.toString());
				return member;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException();
		} finally {
			if(rs != null){try{rs.close();}catch(Exception e){e.printStackTrace();}}
			if(pstmt != null){try{pstmt.close();}catch(Exception e){e.printStackTrace();}}
			if(conn != null){try{conn.close();}catch(Exception e){e.printStackTrace();}}
		}
		return null;
	}

	@Override
	public int deleteMember(MemberVO member) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try{
			conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:study");
			StringBuffer sb = new StringBuffer();
			sb.append("   update member set 		 	");
			sb.append("    	mem_del_yn ='Y' 			");
			sb.append("   where mem_id = ? 				");

			pstmt = conn.prepareStatement(sb.toString());
			System.out.println("member.getMemId()::"+member.getMemId());
			
			pstmt.setString(1, member.getMemId());
			int resultCnt = pstmt.executeUpdate();
			return resultCnt;
			
		}catch(Exception e){
	 		System.out.println("deleteMember : "+ e.getMessage());
			throw new DaoException(e.getMessage(), e);
		}finally{
			if(pstmt!=null){try{pstmt.close();} catch(Exception e){e.printStackTrace();} }
			if(conn!=null){try{conn.close();}catch(Exception e){e.printStackTrace();} }
		}
		//return 0;
	}

	
	
	
	
	
	
}
