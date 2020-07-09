package c.w.g.controller;

import c.w.g.service.DelivererService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("deliverer")
public class DelivererController {

    @Resource
    private DelivererService delivererService;

    @RequestMapping("register")
    @ResponseBody
    public String register(String name, String password, String phone, String area) {
        return delivererService.register(name, password, phone, area);
    }

    @RequestMapping("login")
    @ResponseBody
    public String login(String name, String password, HttpSession httpSession, Model model) {
        return delivererService.login(name, password, httpSession, model);
    }

    @RequestMapping("main")
    public String main(HttpSession httpSession, Model model) {
        delivererService.main(httpSession, model);
        return "/deliverer/main";
    }

    @RequestMapping("logout")
    public String logout(HttpSession httpSession) {
        return delivererService.logout(httpSession);
    }

    @RequestMapping("order")
    public String order() {
        return "/deliverer/order";
    }

    @RequestMapping("queryOrderNotHandle")
    @ResponseBody
    public String queryOrderNotHandle() {
        return delivererService.getOrderNotHandled();
    }

    @RequestMapping("handleOrder")
    @ResponseBody
    public String handleOrder(long orderId, HttpSession httpSession) {
        return delivererService.handleOrder(orderId, httpSession);
    }

    @RequestMapping("orderHandled")
    public String orderHandled() {
        return "/deliverer/orderHandled";
    }

    @RequestMapping("queryOrderHandled")
    @ResponseBody
    public String queryOrderHandled(HttpSession httpSession) {
        return delivererService.queryOrderHandled(httpSession);
    }

    @RequestMapping("finishHandle")
    @ResponseBody
    public String finishHandle(long orderId, HttpSession httpSession) {
        return delivererService.finishHandle(orderId, httpSession);
    }

    @RequestMapping("orderFinished")
    public String orderFinished() {
        return "/deliverer/orderFinished";
    }

    @RequestMapping("queryOrderHandledFinish")
    @ResponseBody
    public String queryOrderHandledFinish(HttpSession httpSession) {
        return delivererService.queryOrderHandledFinish(httpSession);
    }

    @RequestMapping("queryOrderDispatchFinish")
    @ResponseBody
    public String queryOrderDispatchFinish(HttpSession httpSession) {
        return delivererService.queryOrderDispatchFinish(httpSession);
    }

    @RequestMapping("queryMonthsEChartsData")
    @ResponseBody
    public String queryMonthsEChartsData(HttpSession httpSession) {
        return delivererService.queryMonthsEChartsData(httpSession);
    }

    @RequestMapping("workLoad")
    public String workload() {
        return "/deliverer/workLoad";
    }

    @RequestMapping("signIn")
    @ResponseBody
    public String signIn(Integer state, HttpSession httpSession) {
        return delivererService.signIn(state, httpSession);
    }

    @RequestMapping("queryDispatchOrder")
    @ResponseBody
    public String queryDispatchOrder(HttpSession httpSession) {
        return delivererService.queryDispatchOrder(httpSession);
    }

    @RequestMapping("finishDispatch")
    @ResponseBody
    public String finishDispatch(long orderId, HttpSession httpSession) {
        return delivererService.finishDispatch(orderId, httpSession);
    }

    @RequestMapping("receiveError")
    @ResponseBody
    public String receiveError(Long orderId, String ps, HttpSession httpSession) {
        return delivererService.receiveError(orderId, ps, httpSession);
    }

    @RequestMapping("dispatchError")
    @ResponseBody
    public String dispatchError(Long orderId, String ps, HttpSession httpSession) {
        return delivererService.dispatchError(orderId, ps, httpSession);
    }
}
