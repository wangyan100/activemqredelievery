package org.yw.springbootcamelesb;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CamelRouteBuilder extends RouteBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(CamelRouteBuilder.class);

	@Override
	public void configure() throws Exception {

		// General Error handling or error monitoring
		onException(Exception.class).log(LoggingLevel.INFO, "##### Exception caught globally  ##### ${body}");


		onException(TechnicalException.class)
                .log(LoggingLevel.ERROR, "TechnicalException caught globally  ")
                //.asyncDelayedRedelivery()
                .redeliveryDelay(3000) // 3 Minutes
                .maximumRedeliveries(3)
                .onRedelivery(exchange -> {LOG.info("onRedelivery {}", exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER));})
                .handled(true)
                .to("activemq:queue:mydeadletterqueue")
                .log(LoggingLevel.ERROR, "technical exception send to mydeadletterqueue");


		// ActiveMQ queue -> Transformation(Fixlength to XML) -> ActiveMQ Topic1
		from("activemq:queue:inputqueue?transacted=true")
					.routeId("")

					.onException(BusinessException.class)
						.log(LoggingLevel.ERROR, "business exception noticed locally /n ${body}")
                        .redeliveryDelay(3000) // 3 Minutes
                        .maximumRedeliveries(3)
                        .onRedelivery(exchange -> {LOG.info("onRedelivery {}", exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER));})
						.handled(true).to("mock:monitor").
					end()
                    .process(exchange->{
                        LOG.info("   exchange {} send to process  REDELIVERY_COUNTER {}",exchange.getIn().getBody().toString(),  exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER));
                        String message =exchange.getIn().getBody().toString().toLowerCase();
                        if(message.contains("technicalerror"))
                            throw new TechnicalException("technicalerror");

                        if(message.contains("businesserror"))
                            throw new BusinessException("businesserror");

                    })

                .to("activemq:queue:toqueue");
	}
}
