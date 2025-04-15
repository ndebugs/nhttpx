package com.ndebugs.nhttpx.message;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Message {

    @XmlElement
    private String name;

    @XmlElement
    private Request request;

    @XmlElement
    private Response response;
}
