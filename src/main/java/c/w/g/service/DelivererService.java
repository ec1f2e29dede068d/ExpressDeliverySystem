package c.w.g.service;

import c.w.g.bean.Count;
import c.w.g.bean.Deliverer;
import c.w.g.bean.Order;
import c.w.g.repository.CountRepository;
import c.w.g.repository.DelivererRepository;
import c.w.g.repository.OrderRepository;
import c.w.g.util.MyUtil;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class DelivererService {
    @Resource
    private DelivererRepository delivererRepository;
    @Resource
    private OrderRepository orderRepository;
    @Resource
    private CountRepository countRepository;

    public String register(String name, String password, String phone, String area) {
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
        if (phone.equals("")) {
            return emptyOrNullResult + "{" +
                    "\"result\":\"fail\"," +
                    "\"msg\":\"请输入电话号码\"" +
                    "}";
        }
        if (area.equals("")) {
            return emptyOrNullResult + "{" +
                    "\"result\":\"fail\"," +
                    "\"msg\":\"请选择地区\"" +
                    "}";
        }
        Deliverer deliverer = delivererRepository.findByName(name);
        if (deliverer != null) {
            return "{" +
                    "\"result\":\"fail\"," +
                    "\"msg\":\"账号已存在\"" +
                    "}";
        } else {
            Deliverer newDeliverer = new Deliverer();
            newDeliverer.setName(name);
            newDeliverer.setPassword(new MyUtil().toMd5(password));
            newDeliverer.setPhone(phone);
            newDeliverer.setArea(area);
            newDeliverer.setReceiveNum(0);
            newDeliverer.setDispatchNum(0);
            newDeliverer.setErrorNum(0);
            delivererRepository.save(newDeliverer);
            return "{" +
                    "\"result\":\"success\"," +
                    "\"msg\":\"注册成功\"" +
                    "}";
        }
    }

    public String login(String name, String password, HttpSession httpSession, Model model) {
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
        Deliverer deliverer = delivererRepository.findByName(name);
        if (deliverer != null) {
            if (deliverer.getPassword().equals(new MyUtil().toMd5(password))) {
                httpSession.setAttribute("delivererName", name);
                httpSession.setAttribute("delivererId", deliverer.getId());
                model.addAttribute("delivererState", deliverer.getState());
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

    public void main(HttpSession httpSession, Model model) {
        Optional<Deliverer> delivererOptional
                = delivererRepository.findById((Integer) httpSession.getAttribute("delivererId"));
        if (delivererOptional.isPresent()) {
            Deliverer deliverer = delivererOptional.get();
            model.addAttribute("delivererState", deliverer.getState());
        }
    }

    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("delivererName");
        httpSession.removeAttribute("delivererId");
        return "redirect:/deliverer/login.html";
    }


    public String getOrderNotHandled() {
        List<Order> orderList = orderRepository.findByState("未接单");
        Gson gson = new Gson();
        long total = orderList.size();
        String rows = gson.toJson(orderList);
        return "{" +
                "\"rows\":" + rows + ","
                + "\"total\":" + total
                + "}";
    }

    /**
     * 快递员接单业务，使用synchronized关键字同步DelivererService类实现非公平锁
     *
     * @param orderId     订单id
     * @param httpSession 快递员会话
     * @return JSON数据
     */
    public String handleOrder(long orderId, HttpSession httpSession) {
        synchronized (DelivererService.class) {
            Integer delivererId = (Integer) httpSession.getAttribute("delivererId");
            Optional<Order> orderOptional = orderRepository.findById(orderId);
            //接单操作
            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();
                order.setReceiveDelivererId(delivererId);
                order.setState("已接单");
                orderRepository.save(order);
            }
            return "{" +
                    "\"result\":\"success\"," +
                    "\"msg\":\"接单成功\"" +
                    "}";
        }
    }

    /**
     * 返回快递员已接单的所有收件单，数据供Bootstrap Table使用
     *
     * @param httpSession 会话
     * @return JSON格式 rows：数据列表项 total：总行数
     */
    public String queryOrderHandled(HttpSession httpSession) {
        Integer delivererId = (Integer) httpSession.getAttribute("delivererId");
        List<Order> orderList
                = orderRepository.findByReceiveDelivererIdAndState(delivererId, "已接单");
        Gson gson = new Gson();
        long total = orderList.size();
        String rows = gson.toJson(orderList);
        return "{" +
                "\"rows\":" + rows + ","
                + "\"total\":" + total
                + "}";
    }

    /**
     * 返回快递员已接单的所有派件单，数据供Bootstrap Table使用
     *
     * @param httpSession 会话httpSession
     * @return JSON格式 rows：数据列表项 total：总行数
     */
    public String queryDispatchOrder(HttpSession httpSession) {
        Integer delivererId = (Integer) httpSession.getAttribute("delivererId");
        List<Order> orderList
                = orderRepository.findByDispatchDelivererIdAndState(delivererId);
        Gson gson = new Gson();
        long total = orderList.size();
        String rows = gson.toJson(orderList);
        return "{" +
                "\"rows\":" + rows + ","
                + "\"total\":" + total
                + "}";
    }

    /**
     * 快递员将物件送至物流中心服务
     *
     * @param orderId     订单id
     * @param httpSession 会话
     * @return JSON
     */
    public String finishHandle(long orderId, HttpSession httpSession) {
        Integer delivererId = (Integer) httpSession.getAttribute("delivererId");
        Optional<Deliverer> delivererOptional
                = delivererRepository.findById(delivererId);
        String date = LocalDateTime.now().toString();
        date = date.replace("T", " ");
        date = date.substring(0, date.indexOf("."));
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setState("已递送至物流中心");
            order.setEnterDate(date);
            ArrayList<Deliverer> deliverers
                    = delivererRepository.findGivenDelivererByArea(order.getDispatchArea());
            Deliverer deliverer = deliverers.get(0);
            order.setDispatchDelivererId(deliverer.getId());
            orderRepository.save(order);
        }
        //计数系统操作
        if (delivererOptional.isPresent()) {
            Deliverer deliverer = delivererOptional.get();
            deliverer.setReceiveNum(deliverer.getReceiveNum() + 1);
            delivererRepository.save(deliverer);
        }
        return "{" +
                "\"result\":\"success\"," +
                "\"msg\":\"成功\"" +
                "}";
    }

    /**
     * 快递员完成派送
     *
     * @param orderId     订单id
     * @param httpSession 会话
     * @return JSON
     */
    public String finishDispatch(long orderId, HttpSession httpSession) {
        Integer delivererId = (Integer) httpSession.getAttribute("delivererId");
        Optional<Deliverer> delivererOptional
                = delivererRepository.findById(delivererId);
        String date = LocalDateTime.now().toString();
        date = date.replace("T", " ");
        date = date.substring(0, date.indexOf("."));
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setState("已签收");
            order.setOutDate(date);
            orderRepository.save(order);
        }
        //计数系统操作
        if (delivererOptional.isPresent()) {
            Deliverer deliverer = delivererOptional.get();
            deliverer.setDispatchNum(deliverer.getReceiveNum() + 1);
            delivererRepository.save(deliverer);
        }
        return "{" +
                "\"result\":\"success\"," +
                "\"msg\":\"成功\"" +
                "}";
    }

    /**
     * 返回快递员已送至物流中心的所有订单，数据供Bootstrap Table使用
     *
     * @param httpSession 会话
     * @return JSON格式 rows：数据列表项 total：总行数
     */
    public String queryOrderHandledFinish(HttpSession httpSession) {
        Integer delivererId = (Integer) httpSession.getAttribute("delivererId");
        List<Order> orderList
                = orderRepository.findByReceiveDelivererIdAndStateOrState(delivererId,
                "已递送至物流中心", "已签收");
        Gson gson = new Gson();
        long total = orderList.size();
        String rows = gson.toJson(orderList);
        return "{" +
                "\"rows\":" + rows + ","
                + "\"total\":" + total
                + "}";
    }

    /**
     * 返回快递员已派送完成的所有订单，数据供Bootstrap Table使用
     *
     * @param httpSession 会话
     * @return JSON格式 rows：数据列表项 total：总行数
     */
    public String queryOrderDispatchFinish(HttpSession httpSession) {
        Integer delivererId = (Integer) httpSession.getAttribute("delivererId");
        List<Order> orderList
                = orderRepository.findByDispatchDelivererIdAndState(delivererId, "已签收");
        Gson gson = new Gson();
        long total = orderList.size();
        String rows = gson.toJson(orderList);
        return "{" +
                "\"rows\":" + rows + ","
                + "\"total\":" + total
                + "}";
    }

    /**
     * 快递员每月收派件直方图数据
     *
     * @param httpSession 会话
     * @return 返回ECharts图标所需数据, 格式为JSON
     */
    public String queryMonthsEChartsData(HttpSession httpSession) {
        Integer delivererId = (Integer) httpSession.getAttribute("delivererId");
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        Integer endMonth = calendar.get(Calendar.MONTH) + 1;
        List<Integer> receiveList = new ArrayList<>();
        List<Integer> delivererList = new ArrayList<>();
        for (Integer month = 1; month <= endMonth; month++) {
            receiveList.add(orderRepository
                    .findByReceiveDelivererIdAndStateOrStateAndEnterDateBetween(delivererId,
                            "已递送至物流中心", "已签收", year + "-" + month + "-01 00:00:00",
                            year + "-" + month + "-31 23:59:59","派件失败").size());
        }
        for (Integer month = 1; month <= endMonth; month++) {
            delivererList.add(orderRepository
                    .findByDispatchDelivererIdAndStateAndOutDateBetween(delivererId,
                            "已签收", year + "-" + month + "-01 00:00:00",
                            year + "-" + month + "-31 23:59:59").size());
        }
        Gson gson = new Gson();
        String receiveListToJSON = gson.toJson(receiveList);
        String delivererListToJSON = gson.toJson(delivererList);
        return "{"
                + "\"receive\":" + receiveListToJSON + ","
                + "\"dispatch\":" + delivererListToJSON
                + "}";
    }

    /**
     * 查询某个邮差某个月的工作情况
     *
     * @param delivererId 邮差id
     * @param month       月份
     * @return JSON数据, 供ECharts饼图使用
     */
    public String queryDelivererDutyDetails(Integer delivererId, Integer month) {
        Optional<Deliverer> delivererOptional = delivererRepository.findById(delivererId);
        String JSON = null;
        if (delivererOptional.isPresent()) {
            List<Count> countList = delivererOptional.get().getCounts();
            Gson gson = new Gson();
            Map<String, String> map = null;
            for (Count count : countList) {
                if (count.getMonth().equals(month)) {
                    map = new HashMap<>();
                    map.put("month", String.valueOf(count.getMonth()));
                    map.put("work", String.valueOf(count.getWork()));
                    map.put("overTime", String.valueOf(count.getWorkOverTime()));
                    map.put("rest", String.valueOf(count.getRest()));
                }
            }
            JSON = gson.toJson(map);
        }
        return JSON;
    }

    /**
     * 邮差签到
     *
     * @param state       签到状态
     * @param httpSession 会话
     * @return JSON结果
     */
    public String signIn(Integer state, HttpSession httpSession) {
        Integer delivererId = (Integer) httpSession.getAttribute("delivererId");
        Optional<Deliverer> delivererOptional = delivererRepository.findById(delivererId);
        if (delivererOptional.isPresent()) {
            Calendar calendar = Calendar.getInstance();
            Integer month = calendar.get(Calendar.MONTH) + 1;
            boolean ifFirstSignIn = true;
            Deliverer deliverer = delivererOptional.get();
            deliverer.setState(state);
            List<Count> counts = deliverer.getCounts();
            for (Count count : counts) {
                if (count.getMonth().equals(month)) {
                    ifFirstSignIn = false;
                    if (state.equals(Deliverer.WORK)) {
                        if (count.getWork() >= 22) {
                            count.setWorkOverTime(count.getWorkOverTime() + 1);
                        } else {
                            count.setWork(count.getWork() + 1);
                        }
                    } else if (state.equals(Deliverer.REST)) {
                        count.setRest(count.getRest() + 1);
                    }
                    break;
                }
            }
            delivererRepository.save(deliverer);
            if (ifFirstSignIn) {
                Count count = new Count();
                if (state.equals(Deliverer.WORK)) {
                    count.setWork(1);
                    count.setRest(0);
                } else if (state.equals(Deliverer.REST)) {
                    count.setRest(1);
                    count.setWork(0);
                }
                count.setMonth(month);
                count.setWorkOverTime(0);
                count.setDeliverer(deliverer);
                countRepository.save(count);
            }
        }
        return "{" +
                "\"result\":\"success\"," +
                "\"msg\":\"成功\"" +
                "}";
    }

    /**
     * 收件失败处理
     *
     * @param orderId 订单id
     * @param ps      失败信息
     * @return 处理结果
     */
    public String receiveError(Long orderId, String ps, HttpSession httpSession) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Optional<Deliverer> delivererOptional
                = delivererRepository.findById((Integer) httpSession.getAttribute("delivererId"));
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setState("收件失败");
            order.setPs(ps);
            orderRepository.save(order);
        }
        if (delivererOptional.isPresent()) {
            Deliverer deliverer = delivererOptional.get();
            deliverer.setErrorNum(deliverer.getErrorNum() + 1);
            delivererRepository.save(deliverer);
        }
        return "{" +
                "\"result\":\"success\"," +
                "\"msg\":\"成功\"" +
                "}";
    }

    /**
     * 派件失败处理
     *
     * @param orderId 订单id
     * @param ps      失败信息
     * @return 处理结果
     */
    public String dispatchError(Long orderId, String ps, HttpSession httpSession) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Optional<Deliverer> delivererOptional
                = delivererRepository.findById((Integer) httpSession.getAttribute("delivererId"));
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setState("派件失败");
            order.setPs(ps);
            order.setDispatchErrorNum(order.getDispatchErrorNum() + 1);
            if (order.getDispatchErrorNum() >= 3) {
                order.setState("派件故障");
                if (delivererOptional.isPresent()) {
                    Deliverer deliverer = delivererOptional.get();
                    deliverer.setErrorNum(deliverer.getErrorNum() + 1);
                    delivererRepository.save(deliverer);
                }
            }
            orderRepository.save(order);
        }
        return "{" +
                "\"result\":\"success\"," +
                "\"msg\":\"成功\"" +
                "}";
    }
}
