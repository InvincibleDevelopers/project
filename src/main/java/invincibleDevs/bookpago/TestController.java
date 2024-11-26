package invincibleDevs.bookpago;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/testtest")
    public String test(){
        return "Hello world";
    }
}
