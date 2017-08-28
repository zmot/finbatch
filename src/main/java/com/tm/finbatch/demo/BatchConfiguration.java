package com.tm.finbatch.demo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Bean
    public FlatFileItemReader<Instrument> dataReader() {
        FlatFileItemReader<Instrument> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("sample_data.csv"));
        reader.setLineMapper(new DefaultLineMapper<Instrument>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[]{"type", "createdDate", "amount"});
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Instrument>() {{
                    setTargetType(Instrument.class);
                }});
            }
        });
        return reader;
    }

    @Bean
    public JdbcBatchItemWriter<Instrument> dbWriter() {
        JdbcBatchItemWriter<Instrument> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO instrument (type,created_date,amount) VALUES :type, :createdDate, :amount");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public Step importInstruments() {
        return stepBuilderFactory.get("importInstruments").<Instrument, Instrument>chunk(10)
                .processor(filterNonBusinessDays()).processor(multiplier()).reader(dataReader())
                .writer(dbWriter()).build();
    }

    @Bean
    public ItemProcessor<Instrument, Instrument> multiplier() {
        return new ValueMultiplier();
    }

    @Bean
    public ItemProcessor<Instrument, Instrument> filterNonBusinessDays() {
        return new BusinessDayRecordFilter();
    }

    @Bean
    public Job importInstrumentsJob(Step step) {
        return jobBuilderFactory.get("importInstrumentsJob").incrementer(new RunIdIncrementer())
                .start(importInstruments()).build();
    }

}
