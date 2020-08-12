package com.zc.rabbit.producer.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.zc.rabbit.producer.broker.RabbitBroker;
import com.zc.rabbit.producer.constant.BrokerMessageStatus;
import com.zc.rabbit.producer.entity.BrokerMessage;
import com.zc.rabbit.producer.service.MessageStoreService;
import com.zc.rabbit.task.annotation.ElasticJobConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ElasticJobConfig(
		name= "com.zc.rabbit.producer.task.RetryMessageDataflowJob",
		cron= "0/10 * * * * ?",
		description = "可靠性投递消息补偿任务",
		overwrite = true,
		shardingTotalCount = 1
		)
@Slf4j
public class RetryMessageDataflowJob implements DataflowJob<BrokerMessage>{

	@Autowired
	private MessageStoreService messageStoreService;
	
	@Autowired
	private RabbitBroker rabbitBroker;
	
	private static final int MAX_RETRY_COUNT = 3;
	
	@Override
	public List<BrokerMessage> fetchData(ShardingContext shardingContext) {
		List<BrokerMessage> list = messageStoreService.fetchTimeOutMessage4Retry(BrokerMessageStatus.SENDING);
		log.info("--------@@@@@ 抓取数据集合, 数量：	{} 	@@@@@@-----------" , list.size());
		return list;
	}

	@Override
	public void processData(ShardingContext shardingContext, List<BrokerMessage> dataList) {
		
		dataList.forEach( brokerMessage -> {
			
			String messageId = brokerMessage.getMessageId();
			if(brokerMessage.getTryCount() >= MAX_RETRY_COUNT) {
				this.messageStoreService.failure(messageId);
				log.warn(" -----消息设置为最终失败，消息ID: {} -------", messageId);
			} else {
				//	每次重发的时候要更新一下try count字段
				this.messageStoreService.updateTryCount(messageId);
				// 	重发消息
				this.rabbitBroker.reliantSend(brokerMessage.getMessage());
			}
			
		});
	}
	
	

}
