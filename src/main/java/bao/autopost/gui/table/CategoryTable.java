package bao.autopost.gui.table;

import bao.autopost.model.Category;

public class CategoryTable extends GenericTable<Category> {

	public CategoryTable() {
		super(new String[] { "Name", "URL" });
	}

	@Override
	protected Object[] createRow(Category obj) {
		return new Object[] { obj.getName(), obj.getUrl() };
	}
}
