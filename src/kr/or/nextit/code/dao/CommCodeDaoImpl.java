package kr.or.nextit.code.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.or.nextit.code.vo.CodeVO;

public class CommCodeDaoImpl implements IcommCodeDao{

	@Override
	public List<CodeVO> getCodeListByParent(String commParent) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:study");
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT 					 ");
			sb.append(" 	  comm_cd			 ");
			sb.append(" 	, comm_nm			 ");
			sb.append(" 	, comm_parent		 ");
			sb.append(" 	, comm_ord			 ");
			sb.append(" FROM comm_code			 ");
			sb.append(" WHERE comm_parent = ?	 ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, commParent);
			rs = pstmt.executeQuery();
			
			List<CodeVO> codeList = new ArrayList<CodeVO>();
			
			while(rs.next()) {
				CodeVO code = new CodeVO();
				code.setCommCd(rs.getString("comm_cd"));
				code.setCommNm(rs.getString("comm_nm"));
				code.setCommOrd(rs.getInt("comm_ord"));
				code.setCommParent(rs.getString("comm_parent"));
				
				codeList.add(code);
			}
			System.out.println("codeList: " + codeList);
			return codeList;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null){try{rs.close();}catch(Exception e){e.printStackTrace();}}
			if(pstmt != null){try{pstmt.close();}catch(Exception e){e.printStackTrace();}}
			if(conn != null){try{conn.close();}catch(Exception e){e.printStackTrace();}}
		}
		
		
		
		return null;
	}

}
