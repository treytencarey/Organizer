package model.editing;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;

import model.Organizer;

public class OrderEditingSupport extends EditingSupport {
	private TableViewer		viewer;
	private TextCellEditor	editor;

	public OrderEditingSupport(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
		this.editor = new TextCellEditor(viewer.getTable());
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return this.editor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		return String.valueOf(((Organizer) element).getOrder());
	}

	@Override
	protected void setValue(Object element, Object value) {
		int order = ((Organizer) element).getOrder();
		try
		{
			int temp = Integer.parseInt(String.valueOf(value));
			order = temp;
		}
		catch (Exception e)
		{
			
		}
		if (((Organizer) element).getOrder() != order)
		{
			((Organizer)element).setOrder(order);
			this.viewer.update(element, null);
		}
	}

	@Override
	protected void saveCellEditorValue(CellEditor cellEditor, ViewerCell cell) {
		super.saveCellEditorValue(cellEditor, cell);
	}
}
