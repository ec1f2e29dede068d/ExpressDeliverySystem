package c.w.g.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestServiceTest {
    @Autowired
    TestService testService;

    @Test
    public void testGetTestBetween() {
        List<c.w.g.bean.Test> tests = testService.getTestBetween(10, 16);
        for (c.w.g.bean.Test test : tests) {
            System.out.println(test.getId() + "  " + test.getName());
        }
    }
}
