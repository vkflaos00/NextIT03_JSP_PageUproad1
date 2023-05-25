package kr.or.nextit.code.dao;

import java.util.List;

import kr.or.nextit.code.vo.CodeVO;

public interface IcommCodeDao {

	List<CodeVO> getCodeListByParent(String commParent);

}
