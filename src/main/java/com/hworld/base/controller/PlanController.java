package com.hworld.base.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hworld.base.dao.PlanDAO;
import com.hworld.base.service.PlanService;
import com.hworld.base.util.Pager;
import com.hworld.base.vo.BaseVO;
import com.hworld.base.vo.BillVO;
import com.hworld.base.vo.MemberVO;
import com.hworld.base.vo.PlanVO;
import com.hworld.base.vo.TelephoneVO;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/plan/*")
public class PlanController {
	
	@Autowired
	private PlanService planService;
	
	
	// 요금제 리스트
	@GetMapping("planList")
	public ModelAndView getList() throws Exception{
		ModelAndView mv= new ModelAndView();
		List<PlanVO> existPlanList = planService.getExistPlanList();
		List<PlanVO> planList = planService.getPlanList();
		List<PlanVO> gList = new ArrayList<>();
		List<PlanVO> sList = new ArrayList<>();
		List<PlanVO> tList = new ArrayList<>();
		List<PlanVO> zList = new ArrayList<>();
		List<PlanVO> wList = new ArrayList<>();
		List<PlanVO> hList = new ArrayList<>();

	    for (PlanVO plan : planList) {
		    String planNum = plan.getPlanNum();
		    if (planNum.startsWith("G")) {
		        gList.add(plan);
		    } else if (planNum.startsWith("S")) {
		        sList.add(plan);
		    } else if (planNum.startsWith("T")) {
		        tList.add(plan);
		    } else if (planNum.startsWith("Z")) {
		        zList.add(plan);
		    } else if (planNum.startsWith("W")) {
		        wList.add(plan);
		    } else if (planNum.startsWith("H")) {
		        hList.add(plan);
		    }
		}
//        
//		
		mv.addObject("gList", gList);
		mv.addObject("sList", sList);
		mv.addObject("tList", tList);
		mv.addObject("zList", zList);
		mv.addObject("wList", wList);
		mv.addObject("hList", hList);
		mv.addObject("planList",existPlanList);
		mv.setViewName("hworld/planList");
		return mv;
	}
	//요금제 상세페이지
	@GetMapping("planDetail")
	public ModelAndView getDetail(PlanVO planVO, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		PlanVO plan = planService.getDetail(planVO);
		PlanVO note = planService.getNoteName(planVO);
		List<PlanVO> recommend = planService.recommendPlan(planVO);
		MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

		Integer memberNum = memberVO.getMemberNum();
		
		
		PlanVO kingNum = planService.getKingPlanNum(memberNum);
		if(kingNum==null) {
			mv.addObject("planNote", note);
	        mv.addObject("planVO", plan);
	        mv.addObject("recommend", recommend);
	        mv.setViewName("hworld/planDetail");
		} else {
			mv.addObject("kingNum", kingNum);
	        mv.addObject("planNote", note);
	        mv.addObject("planVO", plan);
	        mv.addObject("recommend", recommend);
	        mv.setViewName("hworld/planDetail");
	    }
		
		return mv;
	}
	//요금제 추가 및 수정 시 필요한 공통코드 테이블 데이터 
	@ResponseBody
	@PostMapping("getCommonCode")
	public List<BaseVO> getCommonCode(String type) throws Exception {
	    BaseVO baseVO = new BaseVO();
	    baseVO.setType(type);
	    return planService.getCommonCode(baseVO);
	  }
	// 요금제 추가 
	@GetMapping("planAdd")
	public ModelAndView setPlanAdd(PlanVO planVO, BaseVO baseVO, ModelAndView mv) throws Exception{
	
		mv.setViewName("hworld/planAdd");
		return mv;
	}
	
	@PostMapping("planAdd")
	public ModelAndView setPlanAdd(PlanVO planVO, BaseVO baseVO) throws Exception{
		ModelAndView mv= new ModelAndView();
		int common = planService.setCommonCode(baseVO);
		int result = planService.setInsert(planVO);
		
		
		mv.setViewName("redirect:./planList");
		return mv;
	}

	// 요금제 변경 본인인증 
	@PostMapping("identify")
	public ModelAndView getIdentify(@RequestParam("planNum") String planNum, MemberVO memberVO, HttpSession session)throws Exception{
		ModelAndView mv = new ModelAndView();
		
		int result = planService.getIdentify(memberVO, session);
		
		PlanVO planVO = new PlanVO();
		planVO.setPlanNum(planNum);		
		planVO=planService.getDetail(planVO);
		
		if(result==1) {
			String message="확인이 완료되었습니다.";
			mv.addObject("url","./planChange");
			mv.addObject("result", message);
			
			session.setAttribute("planVO", planVO);
		}else {
			String message="일치하는 정보가 없습니다. 다시 확인하고 입력해주세요.";
			mv.addObject("url", "./planDetail?planNum="+planNum);
			mv.addObject("result", message);
		}

			mv.setViewName("common/result");
		return mv;
	}
	
	// 요금제 디테일 > 요금제 변경 확인
	@GetMapping("planChange")
	public ModelAndView getBeforPlan(Integer memberNum, HttpSession session) throws Exception{
		ModelAndView mv = new ModelAndView();
		MemberVO sessionMember = (MemberVO)session.getAttribute("memberVO");
		memberNum = sessionMember.getMemberNum();
		

		PlanVO planVO = planService.getBeforePlan(memberNum);
		mv.addObject("bfPlan", planVO);
		mv.setViewName("hworld/planChange");

		return mv;
	}
	// 요금제 변경 완료 
	@PostMapping("planChange")
	public ModelAndView setPlanChange(BillVO billVO,HttpSession session, Integer memberNum) throws Exception{
		ModelAndView mv = new ModelAndView();
		
		int result = planService.setPlanChange(billVO);

		MemberVO sessionMember = (MemberVO)session.getAttribute("memberVO");
		memberNum = sessionMember.getMemberNum();
		
		int changePossible=billVO.getResult();
		
		PlanVO phoneNum = planService.getBeforePlan(memberNum);
		mv.addObject("days", changePossible);
		mv.addObject("phoneNum", phoneNum);
		mv.setViewName("hworld/planResult");
		
		
		
		 // 세션에서 선택한 요금제 데이터 삭제
	    session.removeAttribute("planVO");

		return mv;
	}
		
	
	// 요금제 수정
	@GetMapping("planUpdate")
	public ModelAndView e2() throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("hworld/planUpdate");
		return modelAndView;
	}
	
	// 부가서비스 디테일 > 신청하기
	@GetMapping("ePlanApply")
	public ModelAndView e3() throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("hworld/ePlanApply");
		return modelAndView;
	}
	
	// 부가서비스 추가
	@GetMapping("ePlanAdd")
	public ModelAndView e4() throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("hworld/ePlanAdd");
		return modelAndView;
	}
	
	// 부가서비스 디테일
	@GetMapping("ePlanDetail")
	public ModelAndView e5() throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("hworld/ePlanDetail");
		return modelAndView;
	}
	
	// 부가서비스 수정
	@GetMapping("ePlanUpdate")
	public ModelAndView e6() throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("hworld/ePlanUpdate");
		return modelAndView;
	}
	
	// 부가서비스 디테일 >> 신청 성공
	@GetMapping("planResult")
	public ModelAndView e7() throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("hworld/planResult");
		return modelAndView;
	}
	
	
}
