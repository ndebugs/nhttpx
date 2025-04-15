package com.ndebugs.nhttpx.message;

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
public class Response {

    @XmlElement(name = "data-field")
    private String dataField;

    @XmlElementWrapper
    @XmlElement(name = "field")
    private String[] fields;
}
