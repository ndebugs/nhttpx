package com.ndebugs.nhttpx.message;

import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@XmlRootElement(name = "message-settings")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class MessageSettings {

    @XmlElementWrapper
    @XmlElement(name = "message")
    private List<Message> messages;
}
