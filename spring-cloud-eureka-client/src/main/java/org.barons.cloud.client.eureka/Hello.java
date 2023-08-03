package org.barons.cloud.client.eureka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class Hello {
    @Autowired
    RestTemplate restTemplate;
    @Value("${spring.application.name}")
    private String appName;
    @Value("${server.port}")
    private int port;

    @RequestMapping("/hello")
    public String hello(HttpServletRequest request) {
        return String.format(
                "Hello from '%s' to clientIP:'%s', server:'%s':'%s', ", appName, request.getRemoteAddr(),request.getLocalAddr(),port);
        //serverPodIP:'%s' System.getenv("MY_POD_IP")
    }
}
