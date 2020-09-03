package c.w.g.service;

import c.w.g.bean.Deliverer;
import c.w.g.bean.Order;
import c.w.g.repository.DelivererRepository;
import c.w.g.repository.OrderRepository;
import com.google.gson.Gson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderService {

    @Resource
    private OrderRepository orderRepository;
    @Resource
    private DelivererRepository delivererRepository;

    /**
     * 订单生成
     *
     * @param order       订单实体
     * @param httpSession 用户会话
     * @return 处理结果
     */
    public String pushOrder(Order order, HttpSession httpSession) {
        if (order.getReceiverAddress() == null || order.getReceiverAddress().isEmpty()) {
            return "{" +
                    "\"result\":\"fail\"," +
                    "\"msg\":\"邮寄地址为空或不全\"" +
                    "}";
        }
        String date = LocalDateTime.now().toString();
        date = date.replace("T", " ");
        date = date.substring(0, date.indexOf("."));
        order.setDate(date);
        order.setState("未接单");
        order.setId(System.currentTimeMillis());
        order.setSenderId((Integer) httpSession.getAttribute("memberId"));
        ArrayList<Deliverer> deliverers =
                delivererRepository.findGivenDelivererByArea(order.getReceiveArea());
        Deliverer deliverer = deliverers.get(0);
        order.setState("已接单");
        order.setReceiveDelivererId(deliverer.getId());
        order.setDispatchErrorNum(0);
        orderRepository.save(order);
        return "{" +
                "\"result\":\"success\"" +
                "}";
    }

    /**
     * 返回分页的订单列表
     *
     * @param pageNum 页数
     * @param size    每页大小
     * @return JSON数据
     */
    public String getOrderPaged(int pageNum, int size) {
        Pageable pageable = PageRequest.of(pageNum, size);
        Page<Order> orderPage = orderRepository.findAll(pageable);
        List<Order> orderList = orderPage.getContent();
        Gson gson = new Gson();
        long total = orderRepository.count();
        String rows = gson.toJson(orderList);
        return "{" +
                "\"rows\":" + rows + ","
                + "\"total\":" + total
                + "}";
    }

    public String getOrderByMemberId(Integer senderId) {
        List<Order> orderList = orderRepository.findBySenderId(senderId);
        Gson gson = new Gson();
        long total = orderList.size();
        String rows = gson.toJson(orderList);
        return "{" +
                "\"rows\":" + rows + ","
                + "\"total\":" + total
                + "}";
    }

}
