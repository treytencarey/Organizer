package model.editing;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;

import model.Organizer;

public class ClassOrPackageEditingSupport extends EditingSupport {
	private TableViewer		viewer;
	private TextCellEditor	editor;

	public ClassOrPackageEditingSupport(TableViewer viewer) {
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
		int classOrPackage = ((Organizer) element).getClassOrPackage();
		return (classOrPackage == 0) ? "Class" : "Package";
	}

	@Override
	protected void setValue(Object element, Object value) {
		int classOrPackage = ((Organizer) element).getClassOrPackage();
		try
		{
			int temp = Integer.parseInt(String.valueOf(value));
			if (temp == 0 || temp == 1)
				classOrPackage = temp;
		}
		catch (Exception e)
		{
			String val = String.valueOf(value).toLowerCase();
			if (val.equals("class"))
				classOrPackage = 0;
			else if (val.equals("package"))
				classOrPackage = 1;
		}
		if (((Organizer) element).getClassOrPackage() != classOrPackage)
		{
			((Organizer)element).setClassOrPackage(classOrPackage);
			this.viewer.update(element, null);
		}
	}

	@Override
	protected void saveCellEditorValue(CellEditor cellEditor, ViewerCell cell) {
		super.saveCellEditorValue(cellEditor, cell);
	}
}
