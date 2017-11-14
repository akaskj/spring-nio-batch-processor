package sample.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class UserFeedStepListener implements StepExecutionListener {

	private static final Logger log = LoggerFactory.getLogger(UserFeedStepListener.class);
	@Override
	public void beforeStep(StepExecution stepExecution) {
	log.info("Starting user feed step");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {

		log.info("User feed step finished, number of records processed " + stepExecution.getReadCount());

		return null;
	}
}