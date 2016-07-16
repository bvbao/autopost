package bao.autopost.gui.table;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import bao.autopost.listeners.TableListener;

public abstract class GenericTable<T> extends JPanel {

	protected List<T> objects;

	protected JTable table = new JTable();

	private DefaultTableModel model;

	protected List<TableListener> listeners = new ArrayList<TableListener>();

	public GenericTable(String[] columns) {
		setLayout(new BorderLayout(0, 0));
		JScrollPane scrollPane = new JScrollPane();
		model = createModel(columns);
		table.setModel(model);
		RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
		table.setRowSorter(sorter);
		scrollPane.setViewportView(table);
		table.setRowSelectionAllowed(true);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				int index = table.getSelectedRow();
				if (index >= 0) {
					T obj = getFirstSelectedRow();
					notifyRowSelected(obj);
				} else {
					notifyAllRowsDeselected();
				}
			}
		});
		this.add(scrollPane);
	}

	protected DefaultTableModel createModel(String[] columns) {
		return new DefaultTableModel(null, columns) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
	}

	protected abstract Object[] createRow(T obj);

	public void setData(final List<T> objects) {
		this.objects = objects;
		for (T obj : objects) {
			Object[] row = createRow(obj);
			model.addRow(row);
		}
	}

	public List<T> getData() {
		return objects;
	}

	private void notifyRowSelected(T obj) {
		for (TableListener listener : listeners) {
			listener.onRowSelected(obj);
		}
	}

	private void notifyAllRowsDeselected() {
		for (TableListener listener : listeners) {
			listener.onAllRowsDeselected();
		}
	}

	public void addListener(TableListener listener) {
		listeners.add(listener);
	}

	public T getFirstSelectedRow() {
		int viewIndex = table.getSelectedRow();
		if (viewIndex >= 0) {
			int modelRowIndex = table.convertRowIndexToModel(viewIndex);
			return objects.get(modelRowIndex);
		}
		return null;
	}

	public List<T> getSelectedRows() {
		int[] viewIndices = table.getSelectedRows();
		if (viewIndices.length > 0) {
			List<T> objs = new ArrayList<T>();
			for (int viewIndex : viewIndices) {
				int modelIndex = table.convertRowIndexToModel(viewIndex);
				objs.add(objects.get(modelIndex));
			}
			return objs;
		}
		return Collections.EMPTY_LIST;
	}

	public List<T> deleteSelectedRows() {
		int[] viewIndices = table.getSelectedRows();
		if (viewIndices.length > 0) {
			List<T> deletedObjects = getSelectedRows();
			List<Integer> viewIndiceList = getSortedSelectedModelRowIndices();
			for (int viewIndex : viewIndiceList) {
				model.removeRow(viewIndex);
			}
			objects.removeAll(deletedObjects);
			return deletedObjects;
		}
		return Collections.EMPTY_LIST;
	}

	List<Integer> getSortedSelectedModelRowIndices() {
		int[] viewIndices = table.getSelectedRows();
		List<Integer> modelIndexList = new ArrayList<Integer>(viewIndices.length);
		for (int index : viewIndices) {
			modelIndexList.add(table.convertRowIndexToModel(index));
		}
		Collections.sort(modelIndexList);
		Collections.reverse(modelIndexList);
		return modelIndexList;
	}

	protected void setModelValue(int modelRowIndex, int modelColumnIndex, Object value) {
		int viewRowIndex = table.convertRowIndexToView(modelRowIndex);
		int viewColumnIndex = table.convertColumnIndexToView(modelColumnIndex);
		table.setValueAt(value, viewRowIndex, viewColumnIndex);
	}

	public void addRow(T obj) throws Exception {
		if (objects == null) {
			objects = new ArrayList<T>();
		}
		if(objects.contains(obj)){
			throw new Exception("Object existed");			
		}
		objects.add(obj);
		model.addRow(createRow(obj));
	}
}
