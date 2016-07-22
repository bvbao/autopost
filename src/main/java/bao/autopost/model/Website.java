package bao.autopost.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class Website {

	private static final Logger logger = Logger.getLogger(Website.class);

	private String url;

	private String username;

	private String password;

	private String usernameXpath;

	private String passwordXpath;

	private String loginXpath;

	private String showLoginFormXpath;

	private String iframeXpath;

	private String titleXpath;

	protected String contentXpath;

	private String submitXpath;

	protected WebDriver webDriver;

	private List<Category> categories;

	private Category selectedCategory;

	private TaskStatus lastStatus;

	public String getUrl() {
		return url;
	}

	@XmlAttribute
	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	@XmlElement
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	@XmlElement
	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsernameXpath() {
		return usernameXpath;
	}

	@XmlElement
	public void setUsernameXpath(String usernameXpath) {
		this.usernameXpath = usernameXpath;
	}

	public String getPasswordXpath() {
		return passwordXpath;
	}

	@XmlElement
	public void setPasswordXpath(String passwordXpath) {
		this.passwordXpath = passwordXpath;
	}

	public String getLoginXpath() {
		return loginXpath;
	}

	@XmlElement
	public void setLoginXpath(String loginXpath) {
		this.loginXpath = loginXpath;
	}

	public String getShowLoginFormXpath() {
		return showLoginFormXpath;
	}

	@XmlElement
	public void setShowLoginFormXpath(String showLoginFormXpath) {
		this.showLoginFormXpath = showLoginFormXpath;
	}

	public String getIframeXpath() {
		return iframeXpath;
	}

	@XmlElement
	public void setIframeXpath(String iframeXpath) {
		this.iframeXpath = iframeXpath;
	}

	public String getTitleXpath() {
		return titleXpath;
	}

	@XmlElement
	public void setTitleXpath(String titleXpath) {
		this.titleXpath = titleXpath;
	}

	public String getContentXpath() {
		return contentXpath;
	}

	@XmlElement
	public void setContentXpath(String contentXpath) {
		this.contentXpath = contentXpath;
	}

	public String getSubmitXpath() {
		return submitXpath;
	}

	@XmlElement
	public void setSubmitXpath(String submitXpath) {
		this.submitXpath = submitXpath;
	}

	public Category getSelectedCategory() {
		if (selectedCategory == null && categories.size() > 0) {
			selectedCategory = categories.get(0);
		}
		return selectedCategory;
	}

	@XmlElement(name = "category")
	@XmlElementWrapper(name = "categories")
	public void setCategories(List<Category> categories) {
		this.categories = categories;
		selectedCategory = categories.get(0);
	}

	public List<Category> getCategories() {
		if (categories == null) {
			categories = new ArrayList<Category>();
		}
		return categories;
	}

	@XmlElement(name = "selectedCategory")
	public void setSelectedCategory(Category category) {
		this.selectedCategory = category;
	}

	public TaskStatus getLastStatus() {
		if (lastStatus == null) {
			lastStatus = TaskStatus.Idle;
		}
		return lastStatus;
	}

	@XmlElement
	public void setLastStatus(TaskStatus lastStatus) {
		this.lastStatus = lastStatus;
	}

	private void access() {
		webDriver = new ChromeDriver();
		webDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		webDriver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
		webDriver.get(this.url);
	}

	private void login() {
		if (showLoginFormXpath != null && !showLoginFormXpath.trim().isEmpty()) {
			WebElement element = (new WebDriverWait(webDriver, 15))
					.until(ExpectedConditions.elementToBeClickable(By.xpath(showLoginFormXpath)));
			element.click();
		}
		webDriver.findElement(By.xpath(usernameXpath)).sendKeys(username);
		webDriver.findElement(By.xpath(passwordXpath)).sendKeys(password);
		webDriver.findElement(By.xpath(loginXpath)).click();
	}

	private void fillPostInfo(String title, String content) {
		webDriver.findElement(By.xpath(titleXpath)).sendKeys(title);
		insertContent(content);
	}

	private void goToSelectedCategory() {
		webDriver.get(selectedCategory.getUrl());
	}

	private void submitPost() {
		webDriver.findElement(By.xpath(submitXpath)).click();
	}

	public void post(String title, String content) {
		access();
		login();
		goToSelectedCategory();
		fillPostInfo(title, content);
//		submitPost();
	}

	public void quit() {
		webDriver.quit();
	}

	protected abstract void insertContent(String content);

	public abstract String chooseContent(Collection<Content> contents) throws Exception;

	@Override
	public int hashCode() {
		return url.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Website) {
			return ((Website) obj).getUrl().equals(this.url);
		}
		return false;
	}

	@Override
	public String toString() {
		return url;
	}

	public abstract String getType();
}
