package sample.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class UserFeedJobCompletionListener extends JobExecutionListenerSupport {

	private static final Logger log = LoggerFactory.getLogger(UserFeedJobCompletionListener.class);

	@Override
    public void beforeJob(JobExecution jobExecution) {

		log.info("Job Started, jobExecutionId " + jobExecution.getJobId());
		log.info("Processing file " + jobExecution.getJobParameters().getString("INPUT_FILE_NAME"));

	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		log.info("Job finished with status " + jobExecution.getStatus());

		String fileName = jobExecution.getJobParameters().getString("INPUT_FILE_NAME");
		String directory = jobExecution.getJobParameters().getString("INPUT_FILE_DIRECTORY");


		try {
			Files.move(Paths.get(directory + fileName)
          , Paths.get(directory + System.getProperty("file.separator") + "processed" + System.getProperty("file.separator") + jobExecution.getJobId() + "-" + fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
