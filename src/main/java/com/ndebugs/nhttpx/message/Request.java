package com.ndebugs.nhttpx.message;

import com.ndebugs.nhttpx.connection.HTTPMethod;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Request {

    @XmlElement
    private String url;

    @XmlElement
    private HTTPMethod method;

    @XmlElementWrapper(name = "params")
    @XmlElement(name = "param")
    private List<Parameter> parameters;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HTTPMethod getMethod() {
        return method != null ? method : HTTPMethod.GET;
    }

    public void setMethod(HTTPMethod method) {
        this.method = method;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
