package c.w.g.configuration;

import c.w.g.interceptor.AdminInterceptor;
import c.w.g.interceptor.DelivererInterceptor;
import c.w.g.interceptor.MemberInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //为管理员，客户和快递员注册拦截器
        InterceptorRegistration adminInterceptorRegistration = registry.addInterceptor(new AdminInterceptor());
        InterceptorRegistration memberInterceptorRegistration = registry.addInterceptor(new MemberInterceptor());
        InterceptorRegistration delivererInterceptorRegistration = registry.addInterceptor(new DelivererInterceptor());

        //为管理员，客户和快递员添加限制的url正则表达式
        adminInterceptorRegistration.addPathPatterns("/admin/**");
        memberInterceptorRegistration.addPathPatterns("/member/**");
        delivererInterceptorRegistration.addPathPatterns("/deliverer/**");

        //为管理员，客户和快递员添加排除的url，以便可以进行登录和注册
        adminInterceptorRegistration.excludePathPatterns("/admin/login.html", "/admin/login");
        memberInterceptorRegistration.excludePathPatterns("/member/login.html", "/member/login"
                , "/member/register.html", "/member/register");
        delivererInterceptorRegistration.excludePathPatterns("/deliverer/login.html", "/deliverer/login"
                , "/deliverer/register.html", "/deliverer/register");
    }

}
