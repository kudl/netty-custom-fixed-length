package com.kudlwork.netty.sample.controller;

import com.kudlwork.netty.sample.service.DemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    private final DemoService demoService;

    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping("/send/fixed")
    public String sendFixedLength() throws InterruptedException {
        return demoService.sendFixedLength();
    }

    @GetMapping("/send/variable")
    public String sendVariableFixedLength() throws InterruptedException {
        return demoService.sendVariableFixedLength();
    }
}
