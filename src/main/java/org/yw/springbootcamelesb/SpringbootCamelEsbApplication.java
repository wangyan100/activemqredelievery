package org.yw.springbootcamelesb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.yw.springbootcamelesb.file.CreateFileProcessor;
import org.yw.springbootcamelesb.file.RequestCreateFileProcessor;
import org.apache.activemq.camel.component.ActiveMQComponent;
import javax.jms.ConnectionFactory;

@SpringBootApplication
public class SpringbootCamelEsbApplication {

	private static final Logger LOG = LoggerFactory.getLogger(SpringbootCamelEsbApplication.class);

	@Bean
	public CreateFileProcessor createFileProcessor() {
		return new CreateFileProcessor();
	}
	
	@Bean
	public RequestCreateFileProcessor requestCreateFileProcessor(@Value("${soapEndpointPort}")String port) {
		RequestCreateFileProcessor requestCreateFileProcessor=new RequestCreateFileProcessor();
		requestCreateFileProcessor.setPort(port);
		return requestCreateFileProcessor;
	}

	@Bean
	public ActiveMQComponent activeMQComponent(ConnectionFactory factory) {
		ActiveMQComponent activeMQComponent = new ActiveMQComponent();
		activeMQComponent.setConnectionFactory(factory);
		return activeMQComponent;
	}

	public static void main(String[] args) {
		LOG.info("SpringbootCamelEsbApplication is starting ......");
		SpringApplication.run(SpringbootCamelEsbApplication.class, args);
	}
}
