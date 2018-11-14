package model;

import java.util.ArrayList;
import java.util.List;

import util.UtilFile;
import view.MyTableViewer;

public enum OrganizerModelProvider {
	INSTANCE;

	private List<Organizer> organizers;

	private OrganizerModelProvider() {
		organizers = new ArrayList<Organizer>();
		
		List<String> fileCont = UtilFile.readFile(MyTableViewer.filePath);
		List<List<String>> tableCont = UtilFile.convertTableContents(fileCont);
		for (List<String> organizerCont : tableCont)
		{
			organizers.add(new Organizer(Integer.parseInt(organizerCont.get(0)), organizerCont.get(1), Integer.parseInt(organizerCont.get(2)), organizerCont.get(3), organizerCont.get(4), Integer.parseInt(organizerCont.get(5))));
		}
	}

	public List<Organizer> getOrganizers() {
		return organizers;
	}
}
