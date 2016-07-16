package bao.autopost.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import bao.autopost.model.Content;
import bao.autopost.model.Website;
import bao.autopost.model.Websites;
import bao.autopost.util.SettingsUtil;

public class DataService {

	private static boolean isDataChanged = false;

	private static final String FILE_PATH = "websites.xml";

	private List<Website> websites;

	private Websites websitesWrapper;

	private List<Content> contents;

	public List<Website> getAllWebsites() throws JAXBException {
		if (websites == null) {
			JAXBContext jaxbContext = JAXBContext.newInstance(Websites.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			websitesWrapper = (Websites) jaxbUnmarshaller.unmarshal(new File(FILE_PATH));
			this.websites = websitesWrapper.getWebsites();
		}
		return websites;
	}

	public void save() throws JAXBException {
		if (!isDataChanged) {
			return;
		}
		JAXBContext jaxbContext = JAXBContext.newInstance(Websites.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(websitesWrapper, new File(FILE_PATH));
	}

	public List<Content> getAllContents() {
		if (contents == null) {
			String[] types = SettingsUtil.getWebsitetypes();
			contents = new ArrayList<Content>(types.length);
			for (String type : types) {
				contents.add(new Content(type, ""));
			}
		}
		return contents;
	}

	public static boolean isDataChanged() {
		return isDataChanged;
	}

	public static void setDataChanged(boolean isDataChanged) {
		DataService.isDataChanged = isDataChanged;
	}
}
