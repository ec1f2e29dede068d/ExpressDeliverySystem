package c.w.g.controller;

import c.w.g.bean.Test;
import c.w.g.service.TestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("test")
public class TestController {

    @Resource
    private TestService testService;

    @RequestMapping("save")
    public String save() {
        Test test = new Test();
        test.setName("Tom");
        testService.save(test);
        return "save success";
    }

    @RequestMapping("get")
    @ResponseBody
    public String get(Integer pageNum, Integer size) {
        /*Map<String, String[]> map = httpServletRequest.getParameterMap();
        Set<String> set = map.keySet();
        for (String aSet : set) {
            System.out.println(aSet + ":" + Arrays.toString(map.get(aSet)));
        }*/
        return testService.getPageAll(pageNum, size);
    }
}
