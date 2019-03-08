package org.yw.springbootcamelesb.file;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.ws.BindingProvider;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.yw.springbootcamelesb.CamelRouteBuilder;
import org.yw.springbootcamelesb.soap.CreateFileService;
import org.yw.springbootcamelesb.soap.CreateFileServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestCreateFileProcessor implements Processor {
	private static final Logger LOG = LoggerFactory.getLogger(RequestCreateFileProcessor.class);
	private static SimpleDateFormat suffixFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss") ;

	private String port;

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		URL wsdl = CamelRouteBuilder.class.getClassLoader().getResource("wsdl/CreateFileServiceService.wsdl");
		CreateFileServiceService createFileServiceStub = new CreateFileServiceService(wsdl);
		CreateFileService createFileService = createFileServiceStub.getCreateFileServicePort();
		BindingProvider provider = (BindingProvider) createFileService;
		String soapURL="http://localhost:" + port + "/soap";
		provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,soapURL);
		createFileService.createFile("createdBySoapService_"+suffixFormatter.format(new Date())+".xml", exchange.getIn().getBody().toString(), "camel");
	}
}
