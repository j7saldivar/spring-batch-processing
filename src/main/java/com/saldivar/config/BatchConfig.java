package com.saldivar.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.saldivar.bean.Data;
import com.saldivar.processor.DataItemProcessor;
import com.saldivar.processor.JobCompletionNotificationListener;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	@Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    @Autowired(required=false)
    public DataSource dataSource;
	
	@Bean
	public FlatFileItemReader<Data> reader() {
		FlatFileItemReader<Data> reader = new FlatFileItemReader<>();
		reader.setResource(new ClassPathResource("test.csv"));
		reader.setLineMapper(new CustomLineMapper().getNewLineMapper());
		return reader;
	}

	    @Bean
	    public DataItemProcessor processor() {
	        return new DataItemProcessor();
	    } 
	    
	    //JDBC writer not used in this sample
	    //@Bean
	    public JdbcBatchItemWriter<Data> writer() {
	        JdbcBatchItemWriter<Data> writer = new JdbcBatchItemWriter<>();
	        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Data>());
	        writer.setSql("INSERT INTO data (id, name) VALUES (:id, :name)");
	        writer.setDataSource(dataSource);
	        return writer;
	    }
	    
	    @Bean
	    public Job importUserJob(JobCompletionNotificationListener  listener) {
	        return jobBuilderFactory.get("importUserJob")
	                .incrementer(new RunIdIncrementer())
	                .listener(listener)
	                .flow(step1())
	                .end()
	                .build();
	    }
	    
	    @Bean
	    public Step step1() {
	        return stepBuilderFactory.get("step1")
	                .<Data, Data> chunk(10)
	                .reader(reader())
	                .processor(processor())
	                .build();
	    }
	   
}
