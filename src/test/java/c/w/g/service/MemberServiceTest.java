package c.w.g.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback(value = false)
public class MemberServiceTest {

    @Resource
    private MemberService memberService;

    @Test
    public void queryProvinceTest() {
        System.out.println(memberService.queryProvince());
    }
}
