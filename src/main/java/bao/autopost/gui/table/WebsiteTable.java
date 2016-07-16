package bao.autopost.gui.table;

import bao.autopost.model.Category;
import bao.autopost.model.TaskStatus;
import bao.autopost.model.Website;

public class WebsiteTable extends GenericTable<Website> {

	public WebsiteTable() {
		super(new String[] { "Site", "Type", "Category", "Status", "Last Status" });
	}

	public void updateCategoryForSelectedWebsite(Category cat) {
		Website website = getFirstSelectedRow();
		website.setSelectedCategory(cat);
		table.setValueAt(cat, table.getSelectedRow(), 2);
	}

	public void updateTaskStatus(Website site, TaskStatus status) {
		int modelRowIndex = objects.indexOf(site);
		setModelValue(modelRowIndex, 3, status);
		if (!status.equals(TaskStatus.Running) && !status.equals(TaskStatus.Canceling)) {
			setModelValue(modelRowIndex, 4, status);
		}
	}

	public TaskStatus getCurrentStatusOfSelectedWebsite() {
		int index = table.getSelectedRow();
		if (index > -1) {
			return (TaskStatus) table.getValueAt(index, 3);
		}
		return null;
	}

	@Override
	protected Object[] createRow(Website site) {
		return new Object[] { site.getUrl(), site.getType(), site.getSelectedCategory(), TaskStatus.Idle,
				site.getLastStatus() };
	}
}
