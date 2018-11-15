/*
 * @(#) View.java
 *
 */
package view;

import java.util.ArrayList;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import graph.builder.GModelBuilder;
import graph.model.GConnection;
import graph.provider.GLabelProvider;
import graph.provider.GNodeContentProvider;
import model.Organizer;
import model.OrganizerModelProvider;

public class SimpleZestGraphView {
   public static final String SIMPLEZESTVIEW = "project-demo.partdescriptor.organizerzestview";
   public static final String POPUPMENU_ID = "project-demo.popupmenu.organizerzestview";
   public static GraphViewer gViewer;
   public static ArrayList<GraphNode> nodes;

   @PostConstruct
   public void createControls(Composite parent, EMenuService menuService) {
	  nodes = new ArrayList<GraphNode>();
      gViewer = new GraphViewer(parent, SWT.BORDER);
      gViewer.setContentProvider(new GNodeContentProvider());
      gViewer.setLabelProvider(new GLabelProvider());
      gViewer.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
      gViewer.applyLayout();
      menuService.registerContextMenu(gViewer.getControl(), POPUPMENU_ID);
      
      SimpleZestGraphView.update();
   }

   public static void update() {
	  if (gViewer == null)
		  return;
	  
	  for (GraphNode node : nodes)
	  {
		  node.dispose();
	  }
	  nodes.clear();
	  
	  boolean containsPackage = false;
	  boolean packageHasVariable = false;
	  boolean packageHasMethod = false;
	  boolean containsClass = false;
	  boolean classHasVariable = false;
	  boolean classHasMethod = false;
	  
	  ArrayList<GraphNode> classToMethodNodes = new ArrayList<GraphNode>();
	  ArrayList<GraphNode> classToVariableNodes = new ArrayList<GraphNode>();
	  ArrayList<GraphNode> packageToMethodNodes = new ArrayList<GraphNode>();
	  ArrayList<GraphNode> packageToVariableNodes = new ArrayList<GraphNode>();
	  
	  for (Organizer organizer : OrganizerModelProvider.INSTANCE.getOrganizers())
	  {
		  if (organizer.getClassOrPackage() == 0)
		  {
			  if (organizer.getMethodOrVariable() == 0)
			  {
				  nodes.add(new GraphNode(gViewer.getGraphControl(), SWT.NONE, organizer.getMethodOrVariableName()));
				  classToMethodNodes.add(nodes.get(nodes.size()-1));
				  classHasMethod = true;
			  } else {
				  nodes.add(new GraphNode(gViewer.getGraphControl(), SWT.NONE, organizer.getMethodOrVariableName()));
				  classToVariableNodes.add(nodes.get(nodes.size()-1));
				  classHasVariable = true;
			  }
			  containsClass = true;
		  }
		  if (organizer.getClassOrPackage() == 1)
		  {
			  if (organizer.getMethodOrVariable() == 0)
			  {
				  nodes.add(new GraphNode(gViewer.getGraphControl(), SWT.NONE, organizer.getMethodOrVariableName()));
				  packageToMethodNodes.add(nodes.get(nodes.size()-1));
				  packageHasMethod = true;
			  } else {
				  nodes.add(new GraphNode(gViewer.getGraphControl(), SWT.NONE, organizer.getMethodOrVariableName()));
				  packageToVariableNodes.add(nodes.get(nodes.size()-1));
				  packageHasVariable = true;
			  }
			  containsPackage = true;
		  }
	  }
	  
	  GraphNode classNode = null;
	  GraphNode classToMethodNode = null;
	  GraphNode classToVariableNode = null;
	  if (containsClass)
	  {
		  classNode = new GraphNode(gViewer.getGraphControl(), SWT.NONE, "Classes");
		  nodes.add(classNode);
		  if (classHasMethod)
		  {
			  classToMethodNode = new GraphNode(gViewer.getGraphControl(), SWT.NONE, "Methods");
			  nodes.add(classToMethodNode);
			  new GraphConnection(gViewer.getGraphControl(), ZestStyles.CONNECTIONS_DIRECTED, classNode, classToMethodNode);
			  for (GraphNode node : classToMethodNodes)
			  {
				  new GraphConnection(gViewer.getGraphControl(), ZestStyles.CONNECTIONS_DIRECTED, classToMethodNode, node);
			  }
		  }
		  if (classHasVariable)
		  {
			  classToVariableNode = new GraphNode(gViewer.getGraphControl(), SWT.NONE, "Variables");
			  nodes.add(classToVariableNode);
			  new GraphConnection(gViewer.getGraphControl(), ZestStyles.CONNECTIONS_DIRECTED, classNode, classToVariableNode);
			  for (GraphNode node : classToVariableNodes)
			  {
				  new GraphConnection(gViewer.getGraphControl(), ZestStyles.CONNECTIONS_DIRECTED, classToVariableNode, node);
			  }
		  }
	  }
	  GraphNode packageNode = null;
	  GraphNode packageToMethodNode = null;
	  GraphNode packageToVariableNode = null;
	  if (containsPackage)
	  {
		  packageNode = new GraphNode(gViewer.getGraphControl(), SWT.NONE, "Packages");
		  nodes.add(packageNode);
		  if (packageHasMethod)
		  {
			  packageToMethodNode = new GraphNode(gViewer.getGraphControl(), SWT.NONE, "Methods");
			  nodes.add(packageToMethodNode);
			  new GraphConnection(gViewer.getGraphControl(), ZestStyles.CONNECTIONS_DIRECTED, packageNode, packageToMethodNode);
			  for (GraphNode node : packageToMethodNodes)
			  {
				  new GraphConnection(gViewer.getGraphControl(), ZestStyles.CONNECTIONS_DIRECTED, packageToMethodNode, node);
			  }
		  }
		  if (packageHasVariable)
		  {
			  packageToVariableNode = new GraphNode(gViewer.getGraphControl(), SWT.NONE, "Variables");
			  nodes.add(packageToVariableNode);
			  new GraphConnection(gViewer.getGraphControl(), ZestStyles.CONNECTIONS_DIRECTED, packageNode, packageToVariableNode);
			  for (GraphNode node : packageToVariableNodes)
			  {
				  new GraphConnection(gViewer.getGraphControl(), ZestStyles.CONNECTIONS_DIRECTED, packageToVariableNode, node);
			  }
		  }
	  }
	  
	  /*new GraphNode(gViewer.getGraphControl(), SWT.NONE, "Node1");
	  new GraphNode(gViewer.getGraphControl(), SWT.NONE, "Node2");
	  new GraphNode(gViewer.getGraphControl(), SWT.NONE, "Node3");
	  new GraphNode(gViewer.getGraphControl(), SWT.NONE, "Node4");*/
	   
      gViewer.setInput(GModelBuilder.instance().getNodes());
      gViewer.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
   }

   @Focus
   public void setFocus() {
      this.gViewer.getGraphControl().setFocus();
   }
}
