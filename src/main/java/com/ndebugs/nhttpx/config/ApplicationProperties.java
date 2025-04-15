package com.ndebugs.nhttpx.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@Configuration
@PropertySource(value = "file:application.properties", ignoreResourceNotFound = true)
public class ApplicationProperties {

    @Value("${settings.file:message-settings.xml}")
    private String settingsFile;

    @Value("${process.timeout:10000}")
    private int processTimeout;

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

    public String getSettingsFile() {
        return settingsFile;
    }

    public void setSettingsFile(String settingsFile) {
        this.settingsFile = settingsFile;
    }

    public int getProcessTimeout() {
        return processTimeout;
    }

    public void setProcessTimeout(int processTimeout) {
        this.processTimeout = processTimeout;
    }

    public boolean isOutputAllowDuplicate() {
        return outputAllowDuplicate;
    }

    public void setOutputAllowDuplicate(boolean outputAllowDuplicate) {
        this.outputAllowDuplicate = outputAllowDuplicate;
    }

    public boolean isOutputTrimmed() {
        return outputTrimmed;
    }

    public void setOutputTrimmed(boolean outputTrimmed) {
        this.outputTrimmed = outputTrimmed;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public int getConnectionMaxErrorRepeat() {
        return connectionMaxErrorRepeat;
    }

    public void setConnectionMaxErrorRepeat(int connectionMaxErrorRepeat) {
        this.connectionMaxErrorRepeat = connectionMaxErrorRepeat;
    }

    public String getConnectionResponseCodePattern() {
        return connectionResponseCodePattern;
    }

    public void setConnectionResponseCodePattern(String connectionResponseCodePattern) {
        this.connectionResponseCodePattern = connectionResponseCodePattern;
    }
}
