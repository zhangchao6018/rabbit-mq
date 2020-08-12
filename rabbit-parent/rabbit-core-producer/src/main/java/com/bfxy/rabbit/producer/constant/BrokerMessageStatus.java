package com.bfxy.rabbit.producer.constant;

/**
 * 	$BrokerMessageStatus
 * 	消息的发送状态
 * @author Alienware
 *
 */
public enum BrokerMessageStatus {
	//发送中
	SENDING("0"),
	//发送成功
	SEND_OK("1"),
	//发送失败
	SEND_FAIL("2"),
	//...
	SEND_FAIL_A_MOMENT("3");
	
	private String code;
	
	private BrokerMessageStatus(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
	
}
