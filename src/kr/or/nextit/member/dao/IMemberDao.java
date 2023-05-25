package kr.or.nextit.member.dao;

import kr.or.nextit.member.vo.MemberVO;

public interface IMemberDao {
	
	public MemberVO getMember(String memId);

	public int insertMember(MemberVO member);

	public int insertUserRole(MemberVO member);

	public MemberVO loginCheck(MemberVO member);

	public int deleteMember(MemberVO member);
	
	
	
}
