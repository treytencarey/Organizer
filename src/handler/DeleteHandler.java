
package handler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;

import model.Organizer;
import model.OrganizerModelProvider;
import view.MyTableViewer;

public class DeleteHandler {
   @Inject
   private ESelectionService selectionService;

   @Execute
   public void execute(EPartService epartService) {
      MPart findPart = epartService.findPart(MyTableViewer.ID);
      Object findPartObj = findPart.getObject();

      if (findPartObj instanceof MyTableViewer) {
         MyTableViewer v = (MyTableViewer) findPartObj;
         Object selection = selectionService.getSelection();
         remove(selection);
         v.refresh();
         
         saveFile();
      }
   }
   
   private void saveFile() {
	   try (BufferedWriter writer = new BufferedWriter(new FileWriter(MyTableViewer.filePath))) {
		    for (Organizer organizer : OrganizerModelProvider.INSTANCE.getOrganizers())
		    {
		    	writer.write(organizer.toString() + "\n");
		    }
	   } catch (IOException ioe) {
	   	    System.err.format("IOException: %s%n", ioe);
   	   }
   }

   private void remove(Object selection) {
      if (selection instanceof Organizer) {
         Organizer p = (Organizer) selection;
         OrganizerModelProvider.INSTANCE.getOrganizers().remove(p);
         return;
      }
      if (selection instanceof Object[]) {
         Object[] objs = (Object[]) selection;
         for (int i = 0; i < objs.length; i++) {
            remove(objs[i]);
         }
      }
   }
}