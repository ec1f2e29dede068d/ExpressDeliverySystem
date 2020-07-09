package c.w.g.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminServiceTest {

    @Autowired
    AdminService adminService;

    @Test
    public void queryDelivererWorkLoad() {
        System.out.println(adminService.queryDelivererWorkLoad(11));
    }

}
