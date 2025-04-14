package com.ndebugs.nhttpx.config;

import com.ndebugs.nhttpx.message.MessageSettings;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.helpers.DefaultValidationEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@Configuration
@ComponentScan("com.ndebugs.nhttpx")
public class BeanConfiguration {
    
    @Bean
    public JAXBContext jaxbContext() throws JAXBException {
        return JAXBContext.newInstance(MessageSettings.class);
    }
    
    @Bean
    public Unmarshaller unmarshaller(@Autowired JAXBContext context) throws JAXBException {
        Unmarshaller unmarshaller = context.createUnmarshaller();
        unmarshaller.setEventHandler(new DefaultValidationEventHandler());
        return unmarshaller;
    }
}
