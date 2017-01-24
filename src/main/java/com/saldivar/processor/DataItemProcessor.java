package com.saldivar.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.saldivar.bean.Data;

public class DataItemProcessor implements ItemProcessor<Data, Data> {

	private static final Logger logger = LoggerFactory.getLogger(DataItemProcessor.class);

	@Override
	public Data process(final Data data) throws Exception {

		final String name = data.getName().toUpperCase();
		final Data transformedData = new Data(name, data.getId());
		logger.info("Converting (" + data + ") into (" + transformedData + ")");

		return data;
	}

}
