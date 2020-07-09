package c.w.g.controller;

import c.w.g.bean.Order;
import c.w.g.service.MemberService;
import c.w.g.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("member")
public class MemberController {

    @Resource
    private MemberService memberService;
    @Resource
    private OrderService orderService;

    @RequestMapping("register")
    @ResponseBody
    public String register(String name, String password) {
        try {
            return memberService.register(name, password);
        } catch (Exception e) {
            e.printStackTrace();
            return "{" +
                    "\"result\":\"fail\"," +
                    "\"msg\":\"服务器出错\"" +
                    "}";
        }

    }

    @RequestMapping("login")
    @ResponseBody
    public String login(String name, String password, HttpSession httpSession) {
        return memberService.login(name, password, httpSession);
    }

    @RequestMapping("main")
    public String main(Model model) {
        memberService.main(model);
        return "/member/main";
    }

    @RequestMapping("logout")
    public String logout(HttpSession httpSession) {
        return memberService.logout(httpSession);
    }

    @RequestMapping("pushOrder")
    @ResponseBody
    public String pushOrder(@RequestBody Order order, HttpSession httpSession) {
        return orderService.pushOrder(order, httpSession);
    }

    @RequestMapping("order")
    public String toOrder() {
        return "/member/order";
    }

    @RequestMapping("querySentOrder")
    @ResponseBody
    public String querySentOrder(HttpSession httpSession) {
        return orderService.getOrderByMemberId((Integer) httpSession.getAttribute("memberId"));
    }

    @RequestMapping("getProvince")
    @ResponseBody
    public String queryProvince(){
        return null;
    }
}
