package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import analysis.ProjectAnalyzer;
import model.Organizer;
import model.OrganizerModelProvider;
import analysis.ProjectAnalyzer;;

public class AddDialog extends TitleAreaDialog {
   private Text   classOrPackageName;
   private Combo  classOrPackageCombo;
   
   private Text   methodOrVariableName;
   private Combo  methodOrVariableCombo;
   
   private Text   startsWithName;
   
   private Combo  orderCombo;
   
   private Organizer organizer;
   
   public Organizer getOrganizer() {
      return organizer;
   }

   public AddDialog(Shell parentShell) {
      super(parentShell);
   }

   @Override
   protected Control createContents(Composite parent) {
      Control contents = super.createContents(parent);
      setTitle("Add a new Organizer");
      setMessage("Please enter the data of the new organizer", IMessageProvider.INFORMATION);
      return contents;
   }

   @Override
   protected Control createDialogArea(Composite parent) {
      GridLayout layout = new GridLayout();
      layout.numColumns = 2;
      parent.setLayout(layout);
      
      new Label(parent, SWT.NONE);
      classOrPackageCombo = new Combo(parent, SWT.BORDER);
      String choices[] = {"Class", "Package"};
      classOrPackageCombo.setItems(choices);
      classOrPackageCombo.select(0);  // Select "1" by default
      
      Label classOrPackageLabel = new Label(parent, SWT.LEFT);
      classOrPackageName = new Text(parent, SWT.BORDER);
      classOrPackageLabel.setText("Class Name");
      
      classOrPackageCombo.addSelectionListener(new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			String newSelection = classOrPackageCombo.getItem(classOrPackageCombo.getSelectionIndex());
			classOrPackageLabel.setText(newSelection + " Name");
			classOrPackageLabel.setBounds(classOrPackageLabel.getBounds().x, classOrPackageLabel.getBounds().y, 80, classOrPackageLabel.getBounds().height);
		}
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			
		} 
      });
      
      new Label(parent, SWT.NONE);
      methodOrVariableCombo = new Combo(parent, SWT.BORDER);
      String choices2[] = {"Method", "Variable"};
      methodOrVariableCombo.setItems(choices2);
      methodOrVariableCombo.select(0);  // Select "1" by default
      
      Label methodOrVariableLabel = new Label(parent, SWT.LEFT);
      methodOrVariableName = new Text(parent, SWT.BORDER);
      methodOrVariableLabel.setText("Method Name");
      
      methodOrVariableCombo.addSelectionListener(new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			String newSelection = methodOrVariableCombo.getItem(methodOrVariableCombo.getSelectionIndex());
			methodOrVariableLabel.setText(newSelection + " Name");
			methodOrVariableLabel.setBounds(methodOrVariableLabel.getBounds().x, methodOrVariableLabel.getBounds().y, 80, methodOrVariableLabel.getBounds().height);
		}
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			
		} 
      });
      
      Label startsWithLabel = new Label(parent, SWT.NONE);
      startsWithLabel.setText("Starts With");
      startsWithName = new Text(parent, SWT.BORDER);
      
      Label orderLabel = new Label(parent, SWT.NONE);
      orderLabel.setText("Order");
      orderCombo = new Combo(parent, SWT.BORDER);
      
      ArrayList<Integer> orders = new ArrayList<Integer>();
      for (Organizer organizer : OrganizerModelProvider.INSTANCE.getOrganizers())
      {
    	  if (!orders.contains(organizer.getOrder()))
    	  {
    		  orders.add(organizer.getOrder());
    	  }
      }
      Collections.sort(orders);
      orders.add(orders.get(orders.size()-1)+1);
      String[] choices3 = new String[orders.size()];
      for (int i = 0; i < orders.size(); i++)
      {
    	  choices3[i] = String.valueOf(orders.get(i));
      }
      
      orderCombo.setItems(choices3);
      orderCombo.select(0);
      
      return parent;
   }

   @Override
   protected void createButtonsForButtonBar(Composite parent) {
      ((GridLayout) parent.getLayout()).numColumns++;

      Button button = new Button(parent, SWT.PUSH);
      button.setText("OK");
      button.setFont(JFaceResources.getDialogFont());
      button.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent e) {
        	if (classOrPackageName.getText().length() != 0 && methodOrVariableName.getText().length() != 0 && startsWithName.getText().length() != 0) {
               organizer = new Organizer(classOrPackageCombo.getSelectionIndex(), classOrPackageName.getText(), methodOrVariableCombo.getSelectionIndex(), methodOrVariableName.getText(), startsWithName.getText(), Integer.parseInt(orderCombo.getItem(orderCombo.getSelectionIndex())));
               
               try {
				new ProjectAnalyzer().analyze();
			} catch (CoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
               
               close();
            } else {
               setErrorMessage("Please enter all data");
            }
         }
      });
   }
}
