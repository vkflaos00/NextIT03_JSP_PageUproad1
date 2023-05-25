package kr.or.nextit.free.service;

import java.util.List;

import kr.or.nextit.common.vo.PagingVO;
import kr.or.nextit.exception.BizNotEffectedException;
import kr.or.nextit.free.dao.FreeBoardDaoImpl;
import kr.or.nextit.free.dao.IFreeBoardDao;
import kr.or.nextit.free.vo.FreeBoardVO;

public class FreeBoardServiceImpl implements IFreeBoardService{
	
	IFreeBoardDao freeBoardDao = new FreeBoardDaoImpl();
	
	@Override
	public void registerBoard(FreeBoardVO freeBoard) throws BizNotEffectedException {
		
		if(freeBoard.getBoTitle() == null || freeBoard.getBoTitle().equals("")) {
			throw new BizNotEffectedException();
		}
		
		int resultCnt = freeBoardDao.insertBoard(freeBoard);
		
		if(resultCnt != 1) {
			throw new BizNotEffectedException();
		}
	}

	@Override
	public List<FreeBoardVO> getBoardList(PagingVO pagingVO) throws BizNotEffectedException {
		
		int totalRowCount = freeBoardDao.getTotalRowCount();
		pagingVO.setTotalRowCount(totalRowCount);
		pagingVO.pageSetting();
		System.out.println("pagingVO.pageSetting() :" + pagingVO.toString());
		
		List<FreeBoardVO> freeBoardList = freeBoardDao.getBoardList(pagingVO);
		
		if(freeBoardList == null) {
			throw new BizNotEffectedException();
		}
		
		return freeBoardList;
	}

	@Override
	public FreeBoardVO getBoard(String boNo) throws BizNotEffectedException {
		
		if(boNo != null && !boNo.equals("")) {
			FreeBoardVO freeBoard = freeBoardDao.getBoard(boNo);
			
			if(freeBoard == null) {
				throw new BizNotEffectedException();
			}
			return freeBoard;
		}else {
			throw new BizNotEffectedException();
		}
	}

	@Override
	public void increaseHit(String boNo) throws BizNotEffectedException {
		if(boNo != null && !boNo.equals("")) {
			int cnt = freeBoardDao.increaseHit(boNo);
			
			if(cnt != 1) {
				throw new BizNotEffectedException();
			}
		}else {
			throw new BizNotEffectedException();
		}
	}

}
