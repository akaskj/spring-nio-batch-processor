package sample.service;

import org.springframework.web.bind.annotation.RestController;
import sample.model.UserFeed;

@RestController
public class TestService {

    public void test() {
        UserFeed userFeed = new UserFeed();
        userFeed.getAddress();
    }



}
