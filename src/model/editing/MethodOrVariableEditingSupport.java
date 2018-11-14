package model.editing;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;

import model.Organizer;

public class MethodOrVariableEditingSupport extends EditingSupport {
	private TableViewer		viewer;
	private TextCellEditor	editor;

	public MethodOrVariableEditingSupport(TableViewer viewer) {
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
		int methodOrVariable = ((Organizer) element).getMethodOrVariable();
		return (methodOrVariable == 0) ? "Class" : "Package";
	}

	@Override
	protected void setValue(Object element, Object value) {
		int methodOrVariable = ((Organizer) element).getMethodOrVariable();
		try
		{
			int temp = Integer.parseInt(String.valueOf(value));
			if (temp == 0 || temp == 1)
				methodOrVariable = temp;
		}
		catch (Exception e)
		{
			String val = String.valueOf(value).toLowerCase();
			if (val.equals("method"))
				methodOrVariable = 0;
			else if (val.equals("variable"))
				methodOrVariable = 1;
		}
		if (((Organizer) element).getMethodOrVariable() != methodOrVariable)
		{
			((Organizer)element).setMethodOrVariable(methodOrVariable);
			this.viewer.update(element, null);
		}
	}

	@Override
	protected void saveCellEditorValue(CellEditor cellEditor, ViewerCell cell) {
		super.saveCellEditorValue(cellEditor, cell);
	}
}
