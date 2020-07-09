package c.w.g.service;

import c.w.g.repository.OrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        OrderService.class
})
public class OrderServiceTest {

    @Resource
    private OrderService orderService;

    @Test
    public void testGetOrderByMemberId() {
        System.out.println(orderService.getOrderByMemberId(2));
    }
}
