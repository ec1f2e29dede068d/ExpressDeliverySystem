package c.w.g.service;

import c.w.g.bean.Member;
import c.w.g.bean.Type;
import c.w.g.repository.MemberRepository;
import c.w.g.repository.TypeRepository;
import c.w.g.util.MyUtil;
import com.google.gson.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class MemberService {

    @Resource
    private MemberRepository memberRepository;
    @Resource
    private TypeRepository typeRepository;

    public String register(String name, String password) {
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
        Member member = memberRepository.findByName(name);
        if (member != null) {
            return "{" +
                    "\"result\":\"fail\"," +
                    "\"msg\":\"账号已存在\"" +
                    "}";
        } else {
            Member newMember = new Member();
            newMember.setName(name);
            newMember.setPassword(new MyUtil().toMd5(password));
            memberRepository.save(newMember);
            return "{" +
                    "\"result\":\"success\"," +
                    "\"msg\":\"注册成功\"" +
                    "}";
        }
    }

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
        Member member = memberRepository.findByName(name);
        if (member != null) {
            if (member.getPassword().equals(new MyUtil().toMd5(password))) {
                httpSession.setAttribute("memberName", name);
                httpSession.setAttribute("memberId", member.getId());
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

    public void main(Model model) {
        Iterable<Type> typeIterable = typeRepository.findAll();
        Iterator<Type> typeIterator = typeIterable.iterator();
        List<Type> typeList = new ArrayList<>();
        while (typeIterator.hasNext()) {
            typeList.add(typeIterator.next());
        }
        model.addAttribute("typeList", typeList);
    }

    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("memberName");
        httpSession.removeAttribute("memberId");
        return "redirect:/member/login.html";
    }

    /**
     * 从static/json/list.json文件中读取省市信息
     *
     * @return JSON 数据
     */
    public String queryProvince() {
        Gson gson = new Gson();
        JsonObject jsonObject = null;
        ClassPathResource classPathResource = new ClassPathResource("static/json/list.json");
        if (classPathResource.exists()) {
            try {
                JsonParser jsonParser = new JsonParser();
                jsonObject = (JsonObject) jsonParser.parse(new InputStreamReader(classPathResource.getInputStream()));
                Set<String> set = jsonObject.keySet();
                set.removeIf(key -> !key.contains("0000"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return gson.toJson(jsonObject);
    }
}
