package com.hworld.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hworld.base.dao.OrderDAO;
import com.hworld.base.vo.DirectVO;
import com.hworld.base.vo.MemberVO;
import com.hworld.base.vo.OrderDirectVO;
import com.hworld.base.vo.OrderVO;

@Service
public class OrderService {

	@Autowired 
	private OrderDAO orderDAO;
	
    // 주문 리스트 조회
	public List<OrderVO> getList() throws Exception{
		return orderDAO.getList();
	}
	
	//주문 하나 조회 
	public OrderVO getDetail(OrderDirectVO orderDirectVO) throws Exception{
		return orderDAO.getDetail(orderDirectVO);
	}
	
	
	//주문테이블 인서트  
	public int setInsert(OrderVO orderVO) throws Exception {
		return orderDAO.setInsert(orderVO);
	}

}
