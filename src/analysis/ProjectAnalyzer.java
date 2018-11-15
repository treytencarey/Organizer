/*
 * @(#) ASTAnalyzer.java
 *
 * Copyright 2015-2018 The Software Analysis Laboratory
 * Computer Science, The University of Nebraska at Omaha
 * 6001 Dodge Street, Omaha, NE 68182.
 */
package analysis;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.resources.IProject; 
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.PlatformUI;

import graph.builder.GModelBuilder;
import graph.model.GNode;
import graph.model.node.GPackageNode;
import model.Organizer;
import model.OrganizerModelProvider;
import util.UtilPlatform;
import visitor.DeclarationVisitor;

public class ProjectAnalyzer {
   private static final String JAVANATURE = "org.eclipse.jdt.core.javanature";
   protected String prjName, pkgName;
   public static Map<String, Map<String, ArrayList<Integer>>> methodsToMove; // Map a class to a method to positions

   public void analyze() throws CoreException {
      GModelBuilder.instance().reset();
      
      // =============================================================
      // 1st step: Project
      // =============================================================
      IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
      for (IProject project : projects) {
         if (!project.isOpen() || !project.isNatureEnabled(JAVANATURE)) { // Check if we have a Java project.
            continue;
         }
         prjName = project.getName();
         analyzePackages(JavaCore.create(project).getPackageFragments());
      }
   }

   protected void analyzePackages(IPackageFragment[] packages) throws CoreException, JavaModelException {
      // =============================================================
      // 2nd step: Packages
      // =============================================================
      for (IPackageFragment iPackage : packages) {
         if (iPackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
            if (iPackage.getCompilationUnits().length < 1) {
               continue;
            }
            pkgName = iPackage.getElementName();

            GNode pkgNode = new GPackageNode(iPackage.getPath().toString(), pkgName);
            // GModelBuilder.instance().getNodes().add(pkgNode);

            analyzeCompilationUnit(iPackage.getCompilationUnits(), pkgNode);
         }
      }
   }

   private void analyzeCompilationUnit(ICompilationUnit[] iCompilationUnits, GNode pkgNode) throws JavaModelException {
      // =============================================================
      // 3rd step: ICompilationUnits
      // =============================================================
      for (ICompilationUnit iUnit : iCompilationUnits) {
         CompilationUnit compilationUnit = parse(iUnit);
         DeclarationVisitor declVisitor = new DeclarationVisitor(pkgNode);
         
         compilationUnit.accept(declVisitor);
         
         String source = iUnit.getSource();
         System.out.println("Source: " + source);
         
         for (String classString : methodsToMove.keySet())
         {
        	 Map<Integer, Integer> orderPositions = new TreeMap<Integer, Integer>();
        	 for (String methodString : methodsToMove.get(classString).keySet())
        	 {
        		 ArrayList<Integer> pos = methodsToMove.get(classString).get(methodString);
        		 
        		 System.out.println("In class (" + classString + "), in method (" + methodString + "), pos is at: (" + pos.get(0) + ", " + pos.get(1) + ")");
        		 
        		 if (orderPositions.get(0) == null || pos.get(0) < orderPositions.get(0))
        			 orderPositions.put(0, pos.get(0));
        	 
        		 for (Organizer organizer : OrganizerModelProvider.INSTANCE.getOrganizers())
        		 {
        			 if (organizer.getClassOrPackage() == 0 && methodString.startsWith(organizer.getMatch()))
        			 {
        				 System.out.println("\tMethod should be in order: " + organizer.getOrder());
        				 
        				 int begLine = pos.get(0);
        				 while (source.charAt(begLine) != '\n') { begLine -= 1; }
        				 
        				 String methodSource = source.substring(begLine, pos.get(0)+pos.get(1)) + "\n";
        				 System.out.println("Method Source: " + methodSource);
        				 
        				 source = source.substring(0, orderPositions.get(0)) + methodSource + source.substring(orderPositions.get(0), begLine) + source.substring(pos.get(0)+pos.get(1));
        				 
        				 
        				 System.out.println("New Source: " + source);
        			 }
        		 }
        	 }
        	 // TODO
        	 // Need to save positions to a TreeMap if method should be in order.
        	 // Then, loop through the first TreeMap. Order 1 should go to the place that has the lowest pos[0]. Order 2 should go to the next lowest. etc.
        	 // Finally, save file. Same as in indentAndSave.
         }
         methodsToMove.clear();
         
         
         // Modify buffer and reconcile
         IBuffer buffer = ((IOpenable)iUnit).getBuffer();
         buffer.setContents(source);
         iUnit.reconcile(ICompilationUnit.NO_AST, false, null, null);
         iUnit.commitWorkingCopy(false, null);

         UtilPlatform.indentAndSave(iUnit);
      }
   }

   private static CompilationUnit parse(ICompilationUnit unit) {
      ASTParser parser = ASTParser.newParser(AST.JLS10);
      parser.setKind(ASTParser.K_COMPILATION_UNIT);
      parser.setSource(unit);
      parser.setResolveBindings(true);
      return (CompilationUnit) parser.createAST(null); // parse
   }
}