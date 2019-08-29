package org.yw.springbootcamelesb;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CamelRouteBuilder extends RouteBuilder {
    /*
    https://people.apache.org/~dkulp/camel/exception-clause.html
    https://gist.github.com/bibryam/085f6ef765b5cebecab7
              :    exchange technicalerror send to process  REDELIVERY_COUNTER null
2019-08-26 20:21:27.562  INFO 3129 --- [mer[inputqueue]] o.y.s.CamelRouteBuilder                  : onRedelivery 1
2019-08-26 20:21:27.563  INFO 3129 --- [mer[inputqueue]] o.y.s.CamelRouteBuilder                  :    exchange technicalerror send to process  REDELIVERY_COUNTER 1
2019-08-26 20:21:30.564  INFO 3129 --- [mer[inputqueue]] o.y.s.CamelRouteBuilder                  : onRedelivery 2
2019-08-26 20:21:30.565  INFO 3129 --- [mer[inputqueue]] o.y.s.CamelRouteBuilder                  :    exchange technicalerror send to process  REDELIVERY_COUNTER 2
2019-08-26 20:21:33.566  INFO 3129 --- [mer[inputqueue]] o.y.s.CamelRouteBuilder                  : onRedelivery 3
2019-08-26 20:21:33.567  INFO 3129 --- [mer[inputqueue]] o.y.s.CamelRouteBuilder                  :    exchange technicalerror send to process  REDELIVERY_COUNTER 3
2019-08-26 20:21:33.567 ERROR 3129 --- [mer[inputqueue]]                                          : TechnicalException caught globally
2019-08-26 20:21:33.569 ERROR 3129 --- [mer[inputqueue]]                                          : technical exception send to mydeadletterqueue

     */

	private static final Logger LOG = LoggerFactory.getLogger(CamelRouteBuilder.class);

	@Override
	public void configure() throws Exception {



		onException(TechnicalException.class)

                .log(LoggingLevel.ERROR, "TechnicalException before re-try globally  ")
                //.asyncDelayedRedelivery()
                .redeliveryDelay(3000) // 3 Minutes
                .maximumRedeliveries(3)
				//Exchange.FAILURE_ROUTE_ID
                .onRedelivery(exchange -> {
                	LOG.info("onRedelivery {} , failed at  {} exception type {}", exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER)
					,exchange.getFromEndpoint().getEndpointKey() + ": " +exchange.getFromRouteId(), exchange.getProperty(Exchange.EXCEPTION_CAUGHT).getClass());
                })
				//here the line below will be executed only maximumRedelivery exceeded ,(problem still exists)
				//if exception does not happen at 2, or 3 retry, you will not see below code will be displayed
				// here you must decide either send message to deadletterqueue or just commit it (delete it from original queue)
				.log(LoggingLevel.ERROR, "TechnicalException after re-try ")
				.handled(true)
                .to("activemq:queue:mydeadletterqueue")
                .end();


		// ActiveMQ queue -> Transformation(Fixlength to XML) -> ActiveMQ Topic1
        //from("activemq:queue:inputqueue")
		from("activemq:queue:inputqueue?transacted=true")
					.routeId("activemqroute")

					.onException(BusinessException.class)
						.log(LoggingLevel.ERROR, "business exception noticed locally /n ${body}")
                        .redeliveryDelay(3000) // 3 Minutes
                        .maximumRedeliveries(3)
                        .onRedelivery(exchange -> {LOG.info("onRedelivery {}", exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER));})
						//here is maxRetry exceeded, still not suceeded
						.handled(true).to("activemq:queue:bizerror_deadletterqueue")
                        .end()
                    .process(exchange->{

                        LOG.info("   In process   REDELIVERY_COUNTER {}",exchange.getIn().getBody().toString(),  exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER));
                        String message =exchange.getIn().getBody().toString().toLowerCase();
                        //if(message.contains("technicalerror") && (exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER)==null || Integer.valueOf(exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER).toString())<3 ))
                       if(message.contains("technicalerror"))
                            throw new TechnicalException("technicalerror");

                        if(message.contains("businesserror"))
                            throw new BusinessException("businesserror");

                    }).id("process-in-route")

                .to("activemq:queue:toqueue");
	}
}
