package model.editing;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;

import model.Organizer;

public class ClassOrPackageNameEditingSupport extends EditingSupport {
	private TableViewer		viewer;
	private TextCellEditor	editor;

	public ClassOrPackageNameEditingSupport(TableViewer viewer) {
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
		return ((Organizer) element).getClassOrPackageName();
	}

	@Override
	protected void setValue(Object element, Object value) {
		((Organizer) element).setClassOrPackageName(String.valueOf(value));
		this.viewer.update(element, null);
	}

	@Override
	protected void saveCellEditorValue(CellEditor cellEditor, ViewerCell cell) {
		super.saveCellEditorValue(cellEditor, cell);
	}
}
