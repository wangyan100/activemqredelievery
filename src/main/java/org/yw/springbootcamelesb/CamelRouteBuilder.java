package org.yw.springbootcamelesb;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yw.springbootcamelesb.file.CreateFileProcessor;
import org.yw.springbootcamelesb.file.RequestCreateFileProcessor;

@Component
public class CamelRouteBuilder extends RouteBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(CamelRouteBuilder.class);

	@Override
	public void configure() throws Exception {

		// General Error handling or error monitoring
		onException(Exception.class).log(LoggingLevel.INFO, "##### Error caught globally  ##### ${body}");


		// File -> ActiveMQ Queue
		from("file:src/data/inputforqueue1?moveFailed=.error")
				.log(LoggingLevel.INFO, "##### Read Message before inputqueue1 ##### ${body}")
				.to("activemq:{{inputqueue1}}");


		// ActiveMQ queue -> Transformation(Fixlength to XML) -> ActiveMQ Topic1
		from("activemq:queue:inputqueue")
					.routeId("")
					.onException(BusinessException.class)
						.log(LoggingLevel.ERROR, "business exception noticed locally /n ${body}")
						.handled(true).to("mock:monitor").
					end()
					.onException(TechnicalException.class)
						.log(LoggingLevel.ERROR, "technical exception noticed locally /n  ${body}")
						//.asyncDelayedRedelivery()
						.redeliveryDelay(3000) // 3 Minutes
						.maximumRedeliveries(2)
						.log(LoggingLevel.ERROR, "maximReadliveries exceed /n  ${body}")
						.to("activemq:queue:mydeadletterqueue")
					.end()
					.process(exchange->{
						String message =exchange.getIn().getBody().toString().toLowerCase();
						if(message.contains("technicalerror"))
							throw new TechnicalException("technicalerror");

						if(message.contains("businesserror"))
							throw new TechnicalException("businesserror");

					}).to("activemq:queue:toqueue");
	}
}
