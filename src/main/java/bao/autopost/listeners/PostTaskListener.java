package bao.autopost.listeners;

import bao.autopost.model.TaskStatus;
import bao.autopost.model.Website;

public interface PostTaskListener {
	void taskStatusChanged(TaskStatus status, Website site);
	void taskFinished();
}
