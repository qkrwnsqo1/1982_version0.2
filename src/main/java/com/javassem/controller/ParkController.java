package com.javassem.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javassem.domain.GraphVO;
import com.javassem.domain.PagingVO;
import com.javassem.domain.ParkBlackVO;
import com.javassem.domain.ParkVO;
import com.javassem.service.GraphService;
import com.javassem.service.ParkBlackService;
import com.javassem.service.ParkService;


@Controller
@RequestMapping("admin")
public class ParkController {
	
	
	@Autowired
	public ParkService parkService;
	@Autowired
	public ParkBlackService parkBlackService;
	@Autowired
	private GraphService graphService;
	

	
	
	
//	@RequestMapping("/{step}.do") // 단순 페이지 이동용
//	public String parkJoin(@PathVariable String step){
//		System.out.println("호출");
//		return "/admin/" +step;
//	}
	
	@RequestMapping("admin_login.do")
	public String move(){
		return "admin/admin_login";	

	}
	
	@RequestMapping("adminPage.do") //로그인과 동시에 블랙리스트 데이터 넘기기
	public String userLogin(ParkVO vo, Model m, PagingVO vo1, GraphVO vo2
			,@RequestParam(value="nowPage", required=false)String nowPage
			, @RequestParam(value="cntPerPage", required=false)String cntPerPage){
		
		ParkVO result =  parkService.idCheck_Login(vo);
		
		if(result != null){
			HashMap<Object, Object> map = new HashMap<>();
			int total_black = parkBlackService.countBlacklist();
			
			if (nowPage == null && cntPerPage == null) {
				nowPage = "1";
				cntPerPage = "5";
			} else if (nowPage == null) {
				nowPage = "1";
			} else if (cntPerPage == null) { 
				cntPerPage = "5";
			}
			
			vo1 = new PagingVO(total_black, Integer.parseInt(nowPage), 5);
			
			map.put("start", vo1.getStart());
			map.put("end", vo1.getEnd());
			
			List<ParkBlackVO> list = parkBlackService.getBlackList(map);
			m.addAttribute("blacklist",list);
			m.addAttribute("paging", vo1);
			
			//그래프관련
			
			//매칭률
			List<GraphVO> matching= graphService.getMatchList();
			m.addAttribute("matching", matching.get(0).getMatching());
			m.addAttribute("wholeApply", matching.get(0).getWhole_apply());
			m.addAttribute("matching_success", matching.get(0).getMatching_success());
			
			//재이용률
			List<GraphVO> reusing= graphService.getReusingList();
			m.addAttribute("reusing", reusing.get(0).getReusing());
			m.addAttribute("whole_use", reusing.get(0).getWhole_use());
			m.addAttribute("reusing_ratio", reusing.get(0).getReusing_ratio());
			
			//회원가입자 수
			
			int total = graphService.getJoinTotal();
			int joinToday = graphService.getJoinToday();
			int joinYesterday1 = graphService.getJoinYesterday1();;
			int joinYesterday2 = graphService.getJoinYesterday2();
			int joinYesterday3 = graphService.getJoinYesterday3();
			int joinYesterday4 = graphService.getJoinYesterday4();

			m.addAttribute("joinToday", joinToday);
			m.addAttribute("joinYesterday1", joinYesterday1);
			m.addAttribute("joinYesterday2", joinYesterday2);
			m.addAttribute("joinYesterday3", joinYesterday3);
			m.addAttribute("joinYesterday4", joinYesterday4);
			
			
			// 누적 가입자 수
			
			m.addAttribute("cumulToday", total);
			m.addAttribute("cumulYesterday1", total-joinToday);
			m.addAttribute("cumulYesterday2", total-joinToday-joinYesterday1);
			m.addAttribute("cumulYesterday3", total-joinToday-joinYesterday1-joinYesterday2);
			m.addAttribute("cumulYesterday4", total-joinToday-joinYesterday1-joinYesterday2-joinYesterday3);
			
			return "/admin/" + "adminPage";
			
		}else{
			System.out.println("실패");
			return "./main";
		}
	}
	}
