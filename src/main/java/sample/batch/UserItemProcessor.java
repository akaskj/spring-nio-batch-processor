package sample.batch;

import org.springframework.batch.item.ItemProcessor;
import sample.model.UserFeed;
import java.text.SimpleDateFormat;

public class UserItemProcessor implements ItemProcessor<UserFeed, UserFeed> {

    @Override
    public UserFeed process(final UserFeed userFeed) throws Exception {

        if (userFeed.getBirthDate() != null) {
            userFeed.setBirthDateDb(new SimpleDateFormat("MM/dd/yyyy").parse(userFeed.getBirthDate()));
        }
        return userFeed;
    }

}