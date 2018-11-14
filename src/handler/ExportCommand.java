 
package handler;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.swt.widgets.Shell;

import model.Organizer;
import model.OrganizerModelProvider;
import view.AddDialog;
import view.MyTableViewer;

public class ExportCommand {
	@Inject
   private ESelectionService selectionService;

   @Execute
   public void execute(EPartService epartService) {
      MPart findPart = epartService.findPart(MyTableViewer.ID);
      Object findPartObj = findPart.getObject();

      if (findPartObj instanceof MyTableViewer) {
         MyTableViewer v = (MyTableViewer) findPartObj;
         
         String items = "";
         for (int i = 0; i < v.getViewer().getTable().getItemCount(); i++)
         {
        	 items += v.getViewer().getTable().getItem(i);
         }
      }
   }
		
}