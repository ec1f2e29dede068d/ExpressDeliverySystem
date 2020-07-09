package c.w.g.controller;

import c.w.g.bean.Order;
import c.w.g.service.AdminService;
import c.w.g.service.DelivererService;
import c.w.g.service.OrderService;
import c.w.g.service.TypeService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("admin")
public class AdminController {
    @Resource
    private AdminService adminService;
    @Resource
    private DelivererService delivererService;
    @Resource
    private OrderService orderService;
    @Resource
    private TypeService typeService;

    @RequestMapping("login")
    @ResponseBody
    public String login(String name, String password, HttpSession httpSession) {
        return adminService.login(name, password, httpSession);
    }

    @RequestMapping("main")
    public String main() {
        return "/admin/main";
    }

    @RequestMapping("logout")
    public String logout(HttpSession httpSession) {
        adminService.logout(httpSession);
        return "redirect:/admin/login.html";
    }

    @RequestMapping("dutyDetails")
    public String dutyDetails() {
        return "/admin/dutyDetails";
    }

    @RequestMapping("queryDelivererDutyDetails")
    @ResponseBody
    public String queryDelivererDutyDetails(Integer delivererId, Integer month) {
        return delivererService.queryDelivererDutyDetails(delivererId, month);
    }

    @RequestMapping("queryDelivererPaged")
    @ResponseBody
    public String queryDelivererPaged(int pageNum, int size) {
        return adminService.queryDelivererPaged(pageNum, size);
    }

    @RequestMapping("delivererManage")
    public String delivererManage() {
        return "/admin/delivererManage";
    }

    @RequestMapping("queryDelivererWorkLoad")
    @ResponseBody
    public String queryDelivererWorkLoad(Integer month) {
        return adminService.queryDelivererWorkLoad(month);
    }

    @RequestMapping("orderManage")
    public String orderManage() {
        return "/admin/orderManage";
    }

    @RequestMapping("queryOrderPaged")
    @ResponseBody
    public String queryOrderPaged(int pageNum, int size) {
        return orderService.getOrderPaged(pageNum, size);
    }

    @RequestMapping("deleteOrder")
    @ResponseBody
    public String deleteOrder(Long orderId) {
        adminService.deleteOrder(orderId);
        return "{" +
                "\"result\":\"success\"," +
                "\"msg\":\"删除成功\"" +
                "}";
    }

    @RequestMapping("deleteOrders")
    @ResponseBody
    public String deleteOrders(String selections) {
        JsonArray selectedOrders = new JsonParser().parse(selections).getAsJsonArray();
        for (JsonElement selectedOrder : selectedOrders) {
            JsonObject selectedOrderAsJsonObject = selectedOrder.getAsJsonObject();
            adminService.deleteOrder(Long.valueOf(String.valueOf(selectedOrderAsJsonObject.get("id"))));
        }
        return "{" +
                "\"result\":\"success\"," +
                "\"msg\":\"删除成功\"" +
                "}";
    }

    @RequestMapping("editOrder")
    @ResponseBody
    public String editOrder(@RequestBody Order order) {
        return adminService.editOrder(order);
    }

    @RequestMapping("typeManage")
    public String typeManage() {
        return "/admin/typeManage";
    }

    @RequestMapping("queryTypePaged")
    @ResponseBody
    public String queryTypePaged(int pageNum, int size) {
        return typeService.queryTypePaged(pageNum, size);
    }


    @RequestMapping("deleteType")
    @ResponseBody
    public String deleteType(String name) {
        adminService.deleteType(name);
        return "{" +
                "\"result\":\"success\"," +
                "\"msg\":\"删除成功\"" +
                "}";
    }

    @RequestMapping("deleteTypes")
    @ResponseBody
    public String deleteTypes(String selections) {
        JsonArray selectedTypes = new JsonParser().parse(selections).getAsJsonArray();
        for (JsonElement selectedType : selectedTypes) {
            JsonObject selectedTypeAsJsonObject = selectedType.getAsJsonObject();
            adminService.deleteType(String.valueOf(selectedTypeAsJsonObject.get("name"))
                    .replaceAll("\"", ""));
        }
        return "{" +
                "\"result\":\"success\"," +
                "\"msg\":\"删除成功\"" +
                "}";
    }

    @RequestMapping("editType")
    @ResponseBody
    public String editType(String oldValue, String newValue) {
        return adminService.editType(oldValue, newValue);
    }

    @RequestMapping("addType")
    @ResponseBody
    public String addType(String name) {
        return adminService.addType(name);
    }

}
