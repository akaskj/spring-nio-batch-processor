package sample.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import sample.model.UserFeed;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Primary
    @Bean
    @ConfigurationProperties(prefix="datasource.batch")
    public DataSource batchDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix="datasource.user")
    public DataSource userDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public BatchConfigurer configurer(){
        return new DefaultBatchConfigurer(batchDataSource());
    }

    @Bean
    public UserFeedStepListener userFeedStepListener(){
        return new UserFeedStepListener();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<UserFeed> csvReader(@Value("#{jobParameters['INPUT_FILE_DIRECTORY']}") final String directory, @Value("#{jobParameters['INPUT_FILE_NAME']}") final String filename) {
        FlatFileItemReader<UserFeed> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(directory+filename));

        reader.setLineMapper(
                new DefaultLineMapper<UserFeed>() {{
                    setLineTokenizer(new DelimitedLineTokenizer() {{
                        setNames(new String[] { "name","emailId","address","phoneNo","birthDate"});
                    }});

                    setFieldSetMapper(new BeanWrapperFieldSetMapper<UserFeed>() {{
                        setTargetType(UserFeed.class);
                    }});
                }}
        );
        return reader;
    }

    @Bean
    public JdbcBatchItemWriter<UserFeed> userInfoWriter() {
        JdbcBatchItemWriter<UserFeed> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO user_data (name, email_id, address, phone_no, birth_date)"
                + "VALUES (:name, :emailId, :address , :phoneNo, :birthDateDb)");
        writer.setDataSource(userDataSource());
        return writer;
    }

    @Bean
    public UserItemProcessor userFeedProcessor() {
        return new UserItemProcessor();
    }

    @Bean
    public Job importUserFeedJob(UserFeedJobCompletionListener listener) {
        return jobBuilderFactory.get("importUserFeedJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(importUserFeedStep())
                .build();
    }

    @Bean
    public Step importUserFeedStep() {
        return ((SimpleStepBuilder<UserFeed, UserFeed>) stepBuilderFactory.get("importUserFeedStep")
                .<UserFeed, UserFeed> chunk(10)
                .listener(userFeedStepListener()))
                .reader(csvReader("", ""))
                .processor(userFeedProcessor())
                .writer(userInfoWriter())
                .build();
    }

}
