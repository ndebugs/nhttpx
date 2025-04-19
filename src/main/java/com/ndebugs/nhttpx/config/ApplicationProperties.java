package com.ndebugs.nhttpx.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@Configuration
@PropertySource(value = "file:application.properties", ignoreResourceNotFound = true)
@Data
public class ApplicationProperties {

    @Value("${settings.file:message-settings.xml}")
    private String settingsFile;

    @Value("${process.timeout:1000}")
    private int processTimeout;

    @Value("${output.dir:out}")
    private String outputDir;

    @Value("${output.allowDuplicate:false}")
    private boolean outputAllowDuplicate;

    @Value("${output.trimmed:true}")
    private boolean outputTrimmed;

    @Value("${request.poolSize:5}")
    private int requestPoolSize;

    @Value("${request.errorAttempts:2}")
    private int requestErrorAttempts;

    @Value("${response.codePattern:200}")
    private String responseCodePattern;
}
