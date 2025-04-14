package com.ndebugs.nhttpx.config;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class ApplicationProperties {

    private boolean allowDuplicate;
    private boolean trimmed;
    private int processTimeout;
    private String settingsFile;
    private String outputDir;

    public boolean isAllowDuplicate() {
        return allowDuplicate;
    }

    public void setAllowDuplicate(boolean allowDuplicate) {
        this.allowDuplicate = allowDuplicate;
    }

    public boolean isTrimmed() {
        return trimmed;
    }

    public void setTrimmed(boolean trimmed) {
        this.trimmed = trimmed;
    }

    public int getProcessTimeout() {
        return processTimeout;
    }

    public void setProcessTimeout(int processTimeout) {
        this.processTimeout = processTimeout;
    }

    public String getSettingsFile() {
        return settingsFile;
    }

    public void setSettingsFile(String settingsFile) {
        this.settingsFile = settingsFile;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }
}
