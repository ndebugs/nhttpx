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

    @Value("${runtime.delay:1000}")
    private int runtimeDelay;

    @Value("${output.allowDuplicate:false}")
    private boolean outputAllowDuplicate;

    @Value("${output.trimmed:true}")
    private boolean outputTrimmed;

    @Value("${output.dir:out}")
    private String outputDir;

    @Value("${connection.repeatOnError.max:2}")
    private int connectionMaxErrorRepeat;

    @Value("${connection.responseCode.pattern:200}")
    private String connectionResponseCodePattern;
}
