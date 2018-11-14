package model.sorter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

import model.Organizer;

public class OrganizerSorter extends ViewerComparator {
	private int						propertyIndex;
	private static final int	DESCENDING	= 1;
	private int						direction	= DESCENDING;

	public OrganizerSorter() {
		this.propertyIndex = 0;
		direction = DESCENDING;
	}

	public int getDirection() {
		return direction == 1 ? SWT.DOWN : SWT.UP;
	}

	public void setColumn(int column) {
		if (column == this.propertyIndex) {
			direction = 1 - direction;
		} else {
			this.propertyIndex = column;
			direction = DESCENDING;
		}
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		Organizer p1 = (Organizer) e1;
		Organizer p2 = (Organizer) e2;
		int rc = 0;
		switch (propertyIndex) {
		case 0:
			rc = p1.getClassOrPackageName().compareTo(p2.getClassOrPackageName());
			break;
		case 1:
			rc = p1.getMethodOrVariableName().compareTo(p2.getMethodOrVariableName());
			break;
		case 2:
			rc = p1.getMatch().compareTo(p2.getMatch());
		default:
			rc = 0;
		}
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}

}
