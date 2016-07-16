package bao.autopost.model;

import java.util.Collection;

import org.openqa.selenium.By;

public class ClassicWebsite extends Website {

	@Override
	protected void insertContent(String content) {
		webDriver.findElement(By.xpath(contentXpath)).sendKeys(content);
	}

	@Override
	public String chooseContent(Collection<Content> contents) throws Exception {
		for (Content content : contents) {
			if (content.getType().equals("Classic")) {
				return content.getValue();
			}
		}
		throw new Exception("Cannot find content");
	}

	@Override
	public String getType() {
		return "Classic";
	}		
}
