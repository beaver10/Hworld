package com.hworld.base.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hworld.base.dao.DirectDAO;
import com.hworld.base.dao.MemberDAO;
import com.hworld.base.dao.PlanDAO;
import com.hworld.base.util.Pager;
import com.hworld.base.util.SHA256Util;
import com.hworld.base.vo.BaseVO;
import com.hworld.base.vo.DirectVO;
import com.hworld.base.vo.MemberVO;
import com.hworld.base.vo.PlanVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PlanService {

	@Autowired
	private PlanDAO planDAO;
	
	//요금제 리스트
	public List<PlanVO> getPlanList() throws Exception{
		return planDAO.getPlanList();
	}
	
	//존재하는 plan 타입 가져오기
	public List<PlanVO> getExistPlanList() throws Exception {
		return planDAO.getExistPlanList();
	};
	//요금제 이름 조회
	public PlanVO getNoteName(PlanVO planVO) throws Exception{
		return planDAO.getNoteName(planVO);
	}
	//요금제 상세페이지 
	public PlanVO getDetail(PlanVO planVO)throws Exception{
		return planDAO.getDetail(planVO);
	}
	//대표회선 요금제 조회
	public PlanVO getKingPlanNum(Integer memberNum) throws Exception{
		return planDAO.getKingPlanNum(memberNum);
	}
	//본인인증
	public int getIdentify(MemberVO memberVO, HttpSession session) throws Exception{
		MemberVO sessionMember = (MemberVO)session.getAttribute("memberVO");
		Integer sessionMemberNum = sessionMember.getMemberNum();
		log.error("{}<==========멤버넘 세션 ",sessionMemberNum);
		
		
		//입력받은 rrnl을 암호화 하고 db 정보와 비교
		String RRN = memberVO.getRrnf()+"-"+memberVO.getRrnl();
	    memberVO.setRrnl(SHA256Util.encryptMD5(RRN));
	    
	    String memberName = memberVO.getName();
	    
	    
	    log.error("{}<=======서비스",memberVO.getName());
		log.error(memberVO.getRrnf());
		log.error(memberVO.getRrnl());
	    MemberVO memberCheck = planDAO.getMemberInput(memberVO);
	    
	    if (sessionMemberNum != null && memberCheck != null && sessionMemberNum.equals(memberCheck.getMemberNum())) {
            return 1;
        } else {
            return 0;
        }
    }

	
	
	// 선택된 타입의 공통코드 정보 가져오기 
	public List<BaseVO> getCommonCode(BaseVO baseVO) throws Exception{
		return planDAO.getCommonCode(baseVO);
	}
	// 새로운 공통코드 인서트 
	public int setCommonCode(BaseVO baseVO) throws Exception{
		return planDAO.setCommonCode(baseVO);
	}
	
	//요금제 등록 
	public int setInsert(PlanVO planVO)throws Exception{
		return planDAO.setInsert(planVO);
	}
	//요금제 수정 
	public int setPlanUpdate(PlanVO planVO) throws Exception{
		return planDAO.setPlanUpdate(planVO);
	}
	//요금제 삭제 
	public int setDelete(PlanVO planVO) throws Exception{
		return planDAO.setPlanDelete(planVO);
	}
	
	
}
