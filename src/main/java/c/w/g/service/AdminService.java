package c.w.g.service;

import c.w.g.bean.Admin;
import c.w.g.bean.Deliverer;
import c.w.g.bean.Order;
import c.w.g.bean.Type;
import c.w.g.repository.AdminRepository;
import c.w.g.repository.DelivererRepository;
import c.w.g.repository.OrderRepository;
import c.w.g.repository.TypeRepository;
import com.google.gson.Gson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
@Transactional
public class AdminService {
    @Resource
    private AdminRepository adminRepository;
    @Resource
    private OrderRepository orderRepository;
    @Resource
    private DelivererRepository delivererRepository;
    @Resource
    private TypeRepository typeRepository;

    public String login(String name, String password, HttpSession httpSession) {
        String emptyOrNullResult = "";
        if (name.equals("")) {
            return emptyOrNullResult + "{" +
                    "\"result\":\"fail\"," +
                    "\"msg\":\"请输入账号\"" +
                    "}";
        }
        if (password.equals("")) {
            return emptyOrNullResult + "{" +
                    "\"result\":\"fail\"," +
                    "\"msg\":\"请输入密码\"" +
                    "}";
        }
        Optional<Admin> optional = adminRepository.findById(name);
        if (optional.isPresent()) {
            Admin admin = optional.get();
            if (admin.getPassword().equals(password)) {
                httpSession.setAttribute("adminName", name);
                return "{" +
                        "\"result\":\"success\"" +
                        "}";
            } else {
                return "{" +
                        "\"result\":\"fail\"," +
                        "\"msg\":\"密码错误\"" +
                        "}";
            }
        } else {
            return "{" +
                    "\"result\":\"fail\"," +
                    "\"msg\":\"无此账号\"" +
                    "}";
        }
    }

    public void logout(HttpSession httpSession) {
        httpSession.invalidate();
    }

    /**
     * 查询某月按区域地址所有邮差（邮递员）的工作量（收派件计件数），最大工作量和最小工作量，要求能够以直方图进行显示；
     *
     * @param month 月份
     * @return JSON数据
     */
    public String queryDelivererWorkLoad(Integer month) {
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String[] strings1 = orderRepository.queryDelivererReceiveWorkLoad(year + "-" + month + "-01 00:00:00",
                year + "-" + month + "-31 23:59:59");
        String[] strings2 = orderRepository.queryDelivererDispatchWorkLoad(year + "-" + month + "-01 00:00:00",
                year + "-" + month + "-31 23:59:59");
        List<Map<String, String>> maps = new ArrayList<>();
        List<Map<String, String>> maps2 = new ArrayList<>();
        for (String string : strings1) {
            String[] values = string.split(",");
            Map<String, String> map = new HashMap<>();
            map.put("max", values[0]);
            map.put("min", values[1]);
            map.put("receiveArea", values[2]);
            map.put("sum", values[3]);
            maps.add(map);
        }
        for (String string : strings2) {
            String[] values = string.split(",");
            Map<String, String> map = new HashMap<>();
            map.put("max", values[0]);
            map.put("min", values[1]);
            map.put("dispatchArea", values[2]);
            map.put("sum", values[3]);
            maps2.add(map);
        }
        Gson gson = new Gson();
        String JSON = "[" +
                gson.toJson(maps) + "," +
                gson.toJson(maps2) +
                "]";
        return JSON;
    }

    /**
     * 分页查询所有邮差
     *
     * @param pageNum 页码
     * @param size    数量
     * @return JSON数据供BootstrapTable使用
     */
    public String queryDelivererPaged(int pageNum, int size) {
        Pageable pageable = PageRequest.of(pageNum, size);
        Page<Deliverer> delivererPage = delivererRepository.findAll(pageable);
        List<Deliverer> delivererList = delivererPage.getContent();
        Gson gson = new Gson();
        long total = delivererRepository.count();
        List<Map<String, String>> mapList = new ArrayList<>();
        for (Deliverer deliverer : delivererList) {
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(deliverer.getId()));
            map.put("name", String.valueOf(deliverer.getName()));
            map.put("phone", String.valueOf(deliverer.getPhone()));
            map.put("receiveNum", String.valueOf(deliverer.getReceiveNum()));
            map.put("dispatchNum", String.valueOf(deliverer.getDispatchNum()));
            map.put("area", String.valueOf(deliverer.getArea()));
            map.put("state", String.valueOf(deliverer.getState()));
            mapList.add(map);
        }
        String rows = gson.toJson(mapList);
        return "{" +
                "\"rows\":" + rows + ","
                + "\"total\":" + total
                + "}";
    }

    /**
     * 删除订单
     *
     * @param orderId 订单id
     */
    public void deleteOrder(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            orderRepository.delete(order);
        }
    }

    /**
     * 编辑订单
     *
     * @param order 更新后订单对象
     * @return JSON
     */
    public String editOrder(Order order) {
        Optional<Order> orderOptional = orderRepository.findById(order.getId());
        if (orderOptional.isPresent()) {
            Order newOrder = orderOptional.get();
            newOrder.setState(order.getState());
            newOrder.setSenderAddress(order.getSenderAddress());
            newOrder.setReceiverAddress(order.getReceiverAddress());
            orderRepository.save(newOrder);
        }
        return "{" +
                "\"result\":\"success\"," +
                "}";
    }


    /**
     * 删除类型
     *
     * @param name 类型名
     */
    public void deleteType(String name) {
        Optional<Type> typeOptional = typeRepository.findById(name);
        if (typeOptional.isPresent()) {
            Type type = typeOptional.get();
            typeRepository.delete(type);
        }
    }

    /**
     * 编辑类型
     *
     * @param oldValue 旧类型
     * @param newValue 新类型
     * @return JSON
     */
    public String editType(String oldValue, String newValue) {
        typeRepository.updateType(newValue, oldValue);
        return "{" +
                "\"result\":\"success\"," +
                "}";
    }

    /**
     * 添加类型
     *
     * @param name 类型名称
     * @return JSON
     */
    public String addType(String name) {
        Type type = new Type();
        type.setName(name);
        typeRepository.save(type);
        return "{" +
                "\"result\":\"success\"," +
                "}";
    }
}
