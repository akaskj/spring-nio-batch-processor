package sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import sample.service.UserFeedService;

@SpringBootApplication
public class Application {

  public static void main(final String[] args) {

    ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
    ctx.getBean(UserFeedService.class).watch();

  }

}
