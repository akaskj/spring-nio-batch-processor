package sample.batch;

import java.io.IOException;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFeedJobStarter {

	private static final Logger log = LoggerFactory.getLogger(UserFeedJobStarter.class);

	private JobLauncher jobLauncher;

	private Job importFileJob;

	@Autowired
	public UserFeedJobStarter(JobLauncher  jobLauncher, Job importFileJob) {
		this.jobLauncher = jobLauncher;
		this.importFileJob = importFileJob;
	}

  public void runJob(String directory, String fileName) throws JobExecutionException, IOException
	{
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString("INPUT_FILE_DIRECTORY", directory);
		jobParametersBuilder.addString("INPUT_FILE_NAME", fileName);
		jobParametersBuilder.addLong("TIMESTAMP",new Date().getTime());

		log.info("Starting job on file " + fileName);

		jobLauncher.run(importFileJob, jobParametersBuilder.toJobParameters());
	}

}