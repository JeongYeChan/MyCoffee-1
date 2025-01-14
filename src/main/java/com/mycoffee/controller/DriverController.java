package com.mycoffee.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mycoffee.domain.DriverVO;
import com.mycoffee.domain.DriverInfo;
import com.mycoffee.service.BoardService;
import com.mycoffee.service.DriverService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping(value = "/driver/member")
@Log4j
@AllArgsConstructor
public class DriverController {
	
	@Autowired
	private DriverService driverservice;
	
	private BoardService boardservice;
	
	/* 회원가입 페이지 이동 */
	@RequestMapping(value = "/join" , method = RequestMethod.GET)
	public void joinGET() {
		
		log.info("회원가입 페이지 진입");
	}
	
	/* 회원가입 */
	@RequestMapping(value = "/join" , method = RequestMethod.POST)
	public String joinPOST(DriverVO driver) throws Exception{
		
		log.info("join 진입");
		
		/* 회원가입 서비스 실행 */
		driverservice.memberJoin(driver);
		
		log.info("join Service 성공");
		
		return "redirect:./login";
	}
	
	/* 회원 정보 수정 */
//	@RequestMapping(value = "/edit" , method = RequestMethod.POST)
	@PostMapping(value = "/edit")
	public String driverEdit(DriverVO driver,
							 HttpServletRequest request) {
		
		log.info(driver);
		if (driverservice.modify(driver)) {
			log.info(driver);
			HttpSession session = request.getSession(false);
			log.info(session);
			session.setAttribute("did",driver);
			return "redirect:./login";
		}
		return "redirect:./login";
		
	}
	
	/* 로그인 페이지 이동 */
	@RequestMapping(value = "/login" , method = RequestMethod.GET)
	public void loginGET() {
		log.info("로그인 페이지 진입");
	}
	
	/* 로그인 */
	@RequestMapping(value="login", method=RequestMethod.POST)
    public String loginPOST(HttpServletRequest request,
    						DriverVO driver,
    						RedirectAttributes rttr,
    						@RequestParam("did") String did
    						) throws Exception{
        
		/* System.out.println("login 메서드 진입"); */
		/* System.out.println("전달된 데이터 : " + driver); */
		HttpSession session = request.getSession();
		DriverVO lvo = driverservice.driverLogin(driver);
		if(lvo == null) {
			int result = 0;
			rttr.addFlashAttribute("result", result);
			return "redirect:./login";
		}
		
		if(lvo.getAuth() == 1){
			return "redirect:./list";
		}
		session.setAttribute("driver", lvo);
        return "redirect:./driverOrder?did="+did;
    }
	
	/* 로그인 세션 */
	@RequestMapping(value="/GetSession", method=RequestMethod.GET)
	public String LoginMainGET(@RequestParam("did") String did, 
							 @RequestParam("password") String password,
							 HttpServletRequest request) {
		HttpSession session = request.getSession();
		DriverVO driver = driverservice.driverLogin(did, password);
		session.setAttribute("Driver",driver);
		return "redirect:./driverOrder?did="+did;
		
	}
	
	/* 로그아웃 세션 */
	@RequestMapping(value="logout.do", method=RequestMethod.GET)
	public String logoutMainGET(HttpServletRequest request) throws Exception{
		
		log.info("logout Get method 진입");
		HttpSession session = request.getSession();
		session.invalidate();
		
		return "redirect:./login";
	}
	
	/* 아이디 중복 검사 */
	@RequestMapping(value = "/driverIdChk" , method = RequestMethod.POST)
	@ResponseBody
	public String driverIdChkPOST(String did) throws Exception{
		
		log.info("driverIdCHk() 진입");
		
		int result = driverservice.idCheck(did);
		
		log.info("결과값 = " + result);
		
		if(result != 0) {
			return "fail";	//중복 아이디가 존재
		} else {
			return "success";	//중복 아이디 없음
		}
		
	}// driverIdChkPOST() 종료
	
//	http://localhost:8080/driver/member/driverOrder?did=driver001@example.com
	/* 배달원 주문확인 페이지 */
	@GetMapping(value = "/driverOrder")
	public void driverOrder(@RequestParam("did") String did, Model model) {
		log.info("배달원 주문확인 페이지 진입");
		List<DriverInfo> list = driverservice.getOrder(did);
		model.addAttribute("order", list);
	}
	
	
	/* 배달원 현황 조회 */
	@GetMapping("/list")
	public void list(Model model){
		log.info("배달원현황 관리자 페이지 진입");
		model.addAttribute("list", boardservice.getList());
		System.out.println(boardservice.getList());
	}
	
	/* 배달원 현황 get Did */
//	@GetMapping(value = "list/{did}")
//	public ResponseEntity<DriverVO> getDriver(@PathVariable("did") String did){
//		log.info("did" + did);
//		return ResponseEntity.ok(driverservice.getDriver(did));
//	}

//	/* 배달원 현황 get Did */
//	@GetMapping(value = "list/test")
//	public ResponseEntity<DriverVO> getDriver2(String did){
//		log.info("did" + did);
//		return ResponseEntity.ok(driverservice.getDriver(did));
//	}
	
//	@PostMapping(value = "/test2", produces = {MediaType.APPLICATION_JSON_VALUE})
//	public ResponseEntity<OrderrInfo> test(@RequestBody HashMap<String, Object> param){
//		log.info("json: " + param);
//		OrderrInfo o = new OrderrInfo();
//		o.setOid("test");
//		return ResponseEntity.ok(o);
//	}
	
	@PostMapping(value = "list/data", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<DriverVO> getDriver(@RequestBody HashMap<String, Object> param){
		log.info("param:" + param);
		String did = (String)param.get("did");
		return ResponseEntity.ok(driverservice.getDriver(did));
	}
	
	@ResponseBody
	@GetMapping(value = "/driverOrder/isstarted", produces = {MediaType.APPLICATION_JSON_VALUE})
	//public ResponseEntity<Boolean> workDriver(@RequestParam ("did")String did) {
	public ResponseEntity<Boolean> workDriver(HttpSession session) {
		log.info("sessionid: " + session.getId());
		DriverVO driver =  (DriverVO)session.getAttribute("driver");
		String did = driver.getDid();
		return ResponseEntity.ok(driverservice.workDriver(did));
	}
	@ResponseBody
	@GetMapping(value = "/driverOrder/open", produces = {MediaType.APPLICATION_JSON_VALUE})
	//public ResponseEntity<Boolean> startDriver(@RequestParam ("did")String did) {
	public ResponseEntity<Boolean> startDriver(HttpSession session) {
		DriverVO driver =  (DriverVO)session.getAttribute("driver");
		String did = driver.getDid();
		return ResponseEntity.ok(driverservice.startDriver(did));
	}
	@ResponseBody
	@GetMapping(value = "/driverOrder/close", produces = {MediaType.APPLICATION_JSON_VALUE})
	//public ResponseEntity<Boolean> endDriver(@RequestParam ("did")String did) {
	public ResponseEntity<Boolean> endDriver(HttpSession session) {
		DriverVO driver =  (DriverVO)session.getAttribute("driver");
		String did = driver.getDid();
		return ResponseEntity.ok(driverservice.endDriver(did));
	}
	
	@PostMapping("/list/approve")
	public String approveRegist(DriverVO driver) {
		driverservice.approveRegist(driver);
		return "redirect: /driver/member/list"; 
	}

	@PostMapping("/list/reject")
	public String rejectRegist(DriverVO driver){
		driverservice.rejectRegist(driver);
		return "redirect: /driver/member/list";
	}
	
	@PostMapping("/driverOrder/delivery")
	public String deliveryRegist(DriverInfo driver) {
		driverservice.deliveryRegist(driver);
		return "redirect: /driver/member/driverOrder/delivery";
	}
	
	@PostMapping("/driverOrder/confirm")
	public String confirmRegist(DriverInfo driver) {
		driverservice.confirmRegist(driver);
		return "redirect: /driver/member/driverOrder/confirm";
	}
	
//	@PostMapping("/driverOrder/status")
//	public String driverStatus(DriverInfo driver) {
//		driverservice.driverStatus(driver);
//		return "redirect: /driver/member/driverOrder";
//	}
}
