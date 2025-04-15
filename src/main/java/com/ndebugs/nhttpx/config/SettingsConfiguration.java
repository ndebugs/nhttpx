package com.ndebugs.nhttpx.config;

import com.ndebugs.nhttpx.message.MessageSettings;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@Configuration
public class SettingsConfiguration {

    @Autowired
    private Unmarshaller unmarshaller;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    public MessageSettings messageSettings() throws IOException, JAXBException {
        File file = new File(applicationProperties.getSettingsFile());
        MessageSettings messageSettings;
        try (InputStream is = new FileInputStream(file)) {
            messageSettings = (MessageSettings) unmarshaller.unmarshal(is);
        }

        return messageSettings;
    }
}
