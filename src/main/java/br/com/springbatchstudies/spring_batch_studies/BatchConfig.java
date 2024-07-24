package br.com.springbatchstudies.spring_batch_studies;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {
    @Autowired
    @Qualifier("transactionManagerApp")
    private PlatformTransactionManager transactionManager;

    @Bean
    Job job(Step step, JobRepository jobRepository) {
        return new JobBuilder("job", jobRepository)
                .start(step)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    Step step(ItemReader<User> reader, ItemWriter<User> writer, JobRepository jobRepository) {
        return new StepBuilder("step", jobRepository)
                .<User, User>chunk(50, transactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    ItemReader<User> reader() {
        return new FlatFileItemReaderBuilder<User>()
                .name("reader")
                .resource(new FileSystemResource("files/users.csv"))
                .comments("--")
                .delimited()
                .names("name", "age")
                .targetType(User.class)
                .build();
    }

    @Bean
    ItemWriter<User> writer(@Qualifier("springDataSource") DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<User>()
                .dataSource(dataSource)
                .sql(
                        "INSERT INTO users (name, age) VALUES (:name, :age)")
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .build();
    }
}
