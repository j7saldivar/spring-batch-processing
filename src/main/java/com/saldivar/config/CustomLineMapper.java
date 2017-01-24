package com.saldivar.config;

import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

import com.saldivar.bean.Data;

public class CustomLineMapper {

	private DelimitedLineTokenizer getDelimitedLineTokenizer() {
		DelimitedLineTokenizer delimitedLineTokeniker = new DelimitedLineTokenizer();
		delimitedLineTokeniker.setNames(new String[] { "name", "id" });
		return delimitedLineTokeniker;
	}

	private BeanWrapperFieldSetMapper<Data> getBeanWrapperFieldSetMapper() {
		BeanWrapperFieldSetMapper<Data> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
		beanWrapperFieldSetMapper.setTargetType(Data.class);
		return beanWrapperFieldSetMapper;
	}

	public DefaultLineMapper<Data> getNewLineMapper() {
		DefaultLineMapper<Data> dataLineMapper = new DefaultLineMapper<>();
		dataLineMapper.setLineTokenizer(getDelimitedLineTokenizer());
		dataLineMapper.setFieldSetMapper(getBeanWrapperFieldSetMapper());
		return dataLineMapper;

	}

}