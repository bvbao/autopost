package bao.autopost.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Websites {

	@XmlElements({ @XmlElement(name = "classicWeb", type = ClassicWebsite.class),
			@XmlElement(name = "modernWeb", type = ModernWebsite.class) })
	private List<Website> websites;

	public List<Website> getWebsites() {
		return websites;
	}
}
