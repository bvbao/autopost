package bao.autopost.model;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import bao.autopost.listeners.PostTaskListener;

public class PostTask implements Runnable {

	private static final Logger logger = Logger.getLogger(PostTask.class);

	private Website website;

	private String title;

	private String content;

	private TaskStatus status = TaskStatus.Idle;

	private boolean isCanceling = false;

	private Collection<PostTaskListener> listeners = new ArrayList<PostTaskListener>();

	public PostTask(Website website, String title, String content) {
		this.website = website;
		this.title = title;
		this.content = content;
	}

	@Override
	public void run() {
		try {
			setStatus(TaskStatus.Running);
			website.post(title, content);
			setStatus(TaskStatus.Successful);
		} catch (Exception e) {
			if (!isCanceling) {
				setStatus(TaskStatus.Failed);
				logger.error("Error while posting: url = " + website.getUrl(), e);
			}
		} finally {
			website.quit();
			notifyFinished();
		}
	}

	public void stop() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (website) {
					isCanceling = true;
					setStatus(TaskStatus.Canceling);
					while (true) {
						try {
							website.quit();
							break;
						} catch (Exception e) {
							// try to quit again
						}
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
						}
					}
					isCanceling = false;
					setStatus(TaskStatus.Canceled);
				}
			}
		}).start();
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
		website.setLastStatus(status);
		notifyStatusChanged(status, website);
	}

	private void notifyStatusChanged(TaskStatus status, Website site) {
		for (PostTaskListener listener : listeners) {
			listener.taskStatusChanged(status, site);
		}
	}

	private void notifyFinished() {
		for (PostTaskListener listener : listeners) {
			listener.taskFinished();
		}
	}

	public void addListener(PostTaskListener listener) {
		listeners.add(listener);
	}

	@Override
	public int hashCode() {
		return website.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return website.equals(obj);
	}
}
