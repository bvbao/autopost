package bao.autopost.model;

import java.util.Collection;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

public class ModernWebsite extends Website {

	@Override
	protected void insertContent(String content) {
		String iframeXpath = getIframeXpath();
		if (iframeXpath != null && !iframeXpath.isEmpty()) {
			webDriver.switchTo().frame(webDriver.findElement(By.xpath(iframeXpath)));
		}
		String javascript = "document.evaluate(\"%s\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.innerHTML='%s';";
		javascript = String.format(javascript, contentXpath, content);
		((JavascriptExecutor) webDriver).executeScript(javascript);
	}

	@Override
	public String chooseContent(Collection<Content> contents) throws Exception {
		for (Content content : contents) {
			if (content.getType().equals("Modern")) {
				return content.getValue();
			}
		}
		throw new Exception("Could not find content type \"modern\"");
	}

	@Override
	public String getType() {
		return "Modern";
	}
}
