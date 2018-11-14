
package handler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Shell;

import model.OrganizerModelProvider;
import view.AddDialog;
import view.MyTableViewer;

public class AddHandler {
   @Inject
   private EPartService epartService;
   @Inject
   @Named(IServiceConstants.ACTIVE_SHELL)
   Shell                shell;

   @Execute
   public void execute() {
      OrganizerModelProvider organizerInstance = OrganizerModelProvider.INSTANCE;
      AddDialog dialog = new AddDialog(shell);
      dialog.open();
      if (dialog.getOrganizer() != null) {
    	 organizerInstance.getOrganizers().add(dialog.getOrganizer());
         MPart findPart = epartService.findPart(MyTableViewer.ID);
         Object findPartObj = findPart.getObject();

         if (findPartObj instanceof MyTableViewer) {
            MyTableViewer v = (MyTableViewer) findPartObj;
            v.refresh();
         }
         
         try (BufferedWriter writer = new BufferedWriter(new FileWriter(MyTableViewer.filePath, true))) {
    	    writer.write(dialog.getOrganizer().toString() + "\n");
    	 } catch (IOException ioe) {
    	    System.err.format("IOException: %s%n", ioe);
    	 }
      }
   }
}