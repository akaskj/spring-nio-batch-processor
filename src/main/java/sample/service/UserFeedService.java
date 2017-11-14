package sample.service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sample.batch.UserFeedJobStarter;

@Component
public class UserFeedService {

  private static final Logger log = LoggerFactory.getLogger(UserFeedService.class);

  private UserFeedJobStarter userFeedJobStarter;

  @Autowired
  public UserFeedService(UserFeedJobStarter userFeedJobStarter) {
    this.userFeedJobStarter = userFeedJobStarter;
  }


  public void watch() {

    WatchService watchService = null;

    try {
      watchService = FileSystems.getDefault().newWatchService();
    } catch (IOException e) {
      e.printStackTrace();
    }

    Path path = Paths.get(System.getProperty("user.home") + System.getProperty("file.separator")  + "user-feed");

    log.info("Looking for changes at " + path);

    try {
      path.register(watchService,StandardWatchEventKinds.ENTRY_CREATE);
    } catch (IOException e) {
      e.printStackTrace();
    }

    /*
    * Using Java nio (new IO) to monitor changes in a particular directory and trigger batch process
    * when a new file is created.
    */
    WatchKey key;
    try {
      while ((key = watchService.take()) != null) {

        for (WatchEvent<?> event : key.pollEvents()) {

          if (event.context().toString().matches("(.*)csv")) {

            log.info("CSV file "+ event.kind() + " " + event.context());

            try
            {
              userFeedJobStarter.runJob(path + System.getProperty("file.separator"),  event.context().toString());
            }
            catch (JobExecutionException e)
            {
              log.info("Error executing batch job");
              e.printStackTrace();
            }
            catch (IOException e) {
              e.printStackTrace();
            }

          }

        }
        key.reset();
      }
    } catch (InterruptedException e) {

      e.printStackTrace();

    }

  }

}