package kr.or.nextit.free.service;

import java.util.List;

import kr.or.nextit.common.vo.PagingVO;
import kr.or.nextit.exception.BizNotEffectedException;
import kr.or.nextit.free.vo.FreeBoardVO;

public interface IFreeBoardService {
	
	/**
	 * To register free_board
	 * @param freeBoard
	 * @throws BizNotEffectedException
	 */
	public void registerBoard(FreeBoardVO freeBoard) throws BizNotEffectedException;
	
	
//	public List<FreeBoardVO> getBoardList() throws BizNotEffectedException;
	public List<FreeBoardVO> getBoardList(PagingVO pagingVO) throws BizNotEffectedException;
	
	public FreeBoardVO getBoard(String boNo) throws BizNotEffectedException;
	
	public void increaseHit(String boNo) throws BizNotEffectedException;
	
}
