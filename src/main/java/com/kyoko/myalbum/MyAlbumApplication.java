package com.kyoko.myalbum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class MyAlbumApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyAlbumApplication.class, args);
    }
    //    顺序：访问级别修饰符 返回类型 方法名 参数 具体代码 然后是注解
//    先添加方法需要的注解，再添加注解需要支持的注解
    @GetMapping("/")
    public GreetResponse greet() {
        GreetResponse greetResponse = new GreetResponse(
                "Hello",
                List.of("Java","Golang","Python")
        );
        return greetResponse;//浏览器返回{"res":"Hello"}
    }
    record GreetResponse(
            String res,
            List<String> FavProgrammingLanguages
    ) {}
}
