package model.filter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import model.Organizer;

public class OrganizerFilter extends ViewerFilter {
	private String searchString;

	public void setSearchText(String s) {
		this.searchString = ".*" + s + ".*";
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (searchString == null || searchString.length() == 0) {
			return true;
		}
		Organizer p = (Organizer) element;
		if (p.getClassOrPackageName().matches(searchString)) {
			return true;
		}
		if (p.getMethodOrVariableName().matches(searchString)) {
			return true;
		}
		if (p.getMatch().matches(searchString)) {
			return true;
		}

		return false;
	}
}
