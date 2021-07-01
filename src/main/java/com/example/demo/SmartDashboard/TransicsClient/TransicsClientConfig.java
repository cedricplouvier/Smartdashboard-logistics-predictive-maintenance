package com.example.demo.SmartDashboard.TransicsClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

@Configuration
public class TransicsClientConfig {
    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.example.demo.SmartDashboard.TransicsClient.gen");
        return marshaller;
    }

    @Bean
    public TransicsClient transicsClient(Jaxb2Marshaller marshaller) {
        TransicsClient client = new TransicsClient();
        //client.setDefaultUri("https://tx-tango.tx-connect.com/IWS_ASMX/Service.asmx");
        //client.setMarshaller(marshaller);
        //client.setUnmarshaller(marshaller);
        client.setWebServiceTemplate(webServiceTemplate());
        return client;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate() {
        WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

        webServiceTemplate.setMarshaller(marshaller());
        webServiceTemplate.setUnmarshaller(marshaller());
        webServiceTemplate.setDefaultUri("https://tx-tango.tx-connect.com/IWS_ASMX/Service.asmx");

        webServiceTemplate.setMessageSender(webServiceMessageSender());
        return webServiceTemplate;
    }

    @Bean
    public WebServiceMessageSender webServiceMessageSender() {
        HttpComponentsMessageSender httpComponentsMessageSender = new HttpComponentsMessageSender();
        // timeout for creating a connection
        int timeout = 600000;
        httpComponentsMessageSender.setConnectionTimeout(timeout);
        // when you have a connection, timeout the read blocks for
        httpComponentsMessageSender.setReadTimeout(timeout);
        return httpComponentsMessageSender;
    }
}
