package test.org.springdoc.api.v31.app1337;

import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
    class HelloController {

        @GetMapping("/hello")
        String hello(@RequestParam(required = false) @Min(1) Integer limit) {
            return "hello";
        }
    }
