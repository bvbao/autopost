package bao.autopost.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import bao.autopost.listeners.PostTaskListener;
import bao.autopost.model.Content;
import bao.autopost.model.PostTask;
import bao.autopost.model.TaskStatus;
import bao.autopost.model.Website;
import bao.autopost.util.SettingsUtil;

public class AutoPostService {

	private static final Logger logger = Logger.getLogger(AutoPostService.class);

	private Map<String, PostTask> mapTask = new HashMap<String, PostTask>();

	private ExecutorService executor;

	@Autowired
	private DataService dataService;

	private int maxTask;

	public AutoPostService() {
		maxTask = SettingsUtil.getMaxThread();
	}

	public List<Website> getAllSites() throws JAXBException {
		return dataService.getAllWebsites();
	}

	public Collection<Content> getAllContents() {
		return dataService.getAllContents();
	}

	public void save() {
		try {
			dataService.save();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public void post(Collection<Website> websites, String title, PostTaskListener listener) {
		executor = Executors.newFixedThreadPool(maxTask);
		for (Website website : websites) {
			try {
				String content = website.chooseContent(getAllContents());
				PostTask task = mapTask.get(website.getUrl());
				if (task == null) {
					task = new PostTask(website, title, content);
					task.addListener(listener);
					mapTask.put(website.getUrl(), task);
				}
				if (!task.getStatus().equals(TaskStatus.Running) && !task.getStatus().equals(TaskStatus.Canceling)) {
					executor.execute(task);
				}
			} catch (Exception ex) {
				logger.error("Could not create task: url=" + website.getUrl(), ex);
			}
		}
	}

	public void stopAllTasks() {
		if (executor != null) {
			executor.shutdown();
			for (PostTask task : mapTask.values()) {
				if (task.getStatus().equals(TaskStatus.Running)) {
					task.stop();
				}
			}
			executor.shutdownNow();
		}
	}

	public void stopTask(Collection<Website> sites) {
		for (Website site : sites) {
			PostTask task = mapTask.get(site.getUrl());
			if (task != null && task.getStatus().equals(TaskStatus.Running)) {
				task.stop();
			}
		}
	}
}
