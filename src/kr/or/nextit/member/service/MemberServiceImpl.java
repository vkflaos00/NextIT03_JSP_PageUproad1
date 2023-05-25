package kr.or.nextit.member.service;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.or.nextit.exception.BizDuplicateKeyException;
import kr.or.nextit.exception.BizNotEffectedException;
import kr.or.nextit.exception.BizNotFoundException;
import kr.or.nextit.exception.BizPasswordNotMatchedException;
import kr.or.nextit.member.dao.IMemberDao;
import kr.or.nextit.member.dao.MemberDaoImpl;
import kr.or.nextit.member.vo.MemberVO;

public class MemberServiceImpl implements IMemberService{

	
	IMemberDao memberDao = new MemberDaoImpl();
	
	@Override
	public void registerMember(MemberVO member) throws BizDuplicateKeyException, BizNotEffectedException {
		// TODO Auto-generated method stub
		System.out.println("registerMember");
		
		if(member.getMemId() !=null && ! member.getMemId().equals("")) {
			MemberVO vo = memberDao.getMember(member.getMemId());
			
			if(vo !=null) {
				throw new BizDuplicateKeyException();
			}
			
			int resultCnt = memberDao.insertMember(member);
			if(resultCnt != 1) {
				throw new BizNotEffectedException();
			}
			
			int resultCnt2 = memberDao.insertUserRole(member);
			if(resultCnt2 !=1) {
				throw new BizNotEffectedException();
			}
		}
	}

	@Override
	public void loginCheck(MemberVO member
			, HttpServletResponse response, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
		MemberVO vo = null;
		if(member.getMemId() == null || member.getMemId().equals("")){
			try {
				response.sendRedirect(request.getContextPath()+"/login/login.jsp?msg=null");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			vo = memberDao.loginCheck(member);
			
			if(vo == null) {
				System.out.println("member 정보를 불러오지 못하였습니다.");
				try {
					response.sendRedirect(request.getContextPath()+"/login/login.jsp?msg=fail");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				System.out.println("success login");
				//loginCheck 01
				HttpSession session = request.getSession();
				session.setAttribute("memberVO", vo);

				String rememberMe = member.getRememberMe();
				if (rememberMe != null && rememberMe.equals("Y")) {
					Cookie cookie= new Cookie("rememberMe", member.getMemId());
					response.addCookie(cookie);
				}else{
					Cookie cookie= new Cookie("rememberMe", "");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
				try {
					response.sendRedirect(request.getContextPath()+"/home/home.jsp");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		
	}

	@Override
	public MemberVO getMember(String memId) throws BizNotEffectedException {
		
		MemberVO member = null;
		if(memId != null && ! memId.equals("")) {
			member = memberDao.getMember(memId);
		}
		
		if(member == null) {
			throw new BizNotEffectedException();
		}
		
		return member;
	}

	@Override
	public void removeMember(MemberVO member) throws BizNotFoundException, BizPasswordNotMatchedException, BizNotEffectedException {
		// TODO Auto-generated method stub
		
		MemberVO vo = null;
		if(member.getMemId() != null && ! member.getMemId().equals("")) {
			vo = memberDao.getMember(member.getMemId());
		}
		
		if( vo == null) {
			throw new BizNotFoundException();
		}
		if( !vo.getMemPass().equals(member.getMemPass()) ){
			throw new BizPasswordNotMatchedException();
		}
		
		int resultCnt = memberDao.deleteMember(member);
		if(resultCnt != 1){
			throw new BizNotEffectedException();
		}
	}

}