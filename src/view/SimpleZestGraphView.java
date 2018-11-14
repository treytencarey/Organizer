/*
 * @(#) View.java
 *
 */
package view;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import graph.builder.GModelBuilder;
import graph.provider.GLabelProvider;
import graph.provider.GNodeContentProvider;
import model.Organizer;
import model.OrganizerModelProvider;

public class SimpleZestGraphView {
   public static final String SIMPLEZESTVIEW = "project-demo.partdescriptor.organizerzestview";
   public static final String POPUPMENU_ID = "project-demo.popupmenu.organizerzestview";
   private GraphViewer gViewer;
   private int layout = 0;

   @PostConstruct
   public void createControls(Composite parent, EMenuService menuService) {
      gViewer = new GraphViewer(parent, SWT.BORDER);
      gViewer.setContentProvider(new GNodeContentProvider());
      gViewer.setLabelProvider(new GLabelProvider());
      gViewer.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
      gViewer.applyLayout();
      menuService.registerContextMenu(gViewer.getControl(), POPUPMENU_ID);
      
      update();
   }

   public void update() {
	  boolean containsPackage = false;
	  boolean containsClass = false;
	  
	  for (Organizer organizer : OrganizerModelProvider.INSTANCE.getOrganizers())
	  {
		  if (organizer.getClassOrPackage() == 0)
			  containsClass = true;
		  if (organizer.getClassOrPackage() == 1)
			  containsPackage = true;
	  }
	  
	  GraphNode classNode = null;
	  if (containsClass)
		  classNode = new GraphNode(gViewer.getGraphControl(), SWT.NONE, "Classes");
	  GraphNode packageNode = null;
	  if (containsPackage)
		  packageNode = new GraphNode(gViewer.getGraphControl(), SWT.NONE, "Packages");
	  
	  /*new GraphNode(gViewer.getGraphControl(), SWT.NONE, "Node1");
	  new GraphNode(gViewer.getGraphControl(), SWT.NONE, "Node2");
	  new GraphNode(gViewer.getGraphControl(), SWT.NONE, "Node3");
	  new GraphNode(gViewer.getGraphControl(), SWT.NONE, "Node4");*/
	   
      gViewer.setInput(GModelBuilder.instance().getNodes());
      if (layout % 2 == 0)
         gViewer.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
      else
         gViewer.setLayoutAlgorithm(new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
      layout++;
   }

   @Focus
   public void setFocus() {
      this.gViewer.getGraphControl().setFocus();
   }
}
