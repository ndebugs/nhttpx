package com.ndebugs.nhttpx.message;

import com.ndebugs.nhttpx.connection.HTTPMethod;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import lombok.Data;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Request {

    @XmlElement
    private String url;

    @XmlElement
    private HTTPMethod method = HTTPMethod.GET;

    @XmlElementWrapper(name = "params")
    @XmlElement(name = "param")
    private List<Parameter> parameters;
}
