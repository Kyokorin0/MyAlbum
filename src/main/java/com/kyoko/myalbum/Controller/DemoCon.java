package com.kyoko.myalbum.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author young
 * @create 2023/3/15 1:51
 * @Description
 */
@RestController
@RequestMapping("/api/v1/demo")
public class DemoCon {
    @GetMapping
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello from a secured endpoint");
    }
}
