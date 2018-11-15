package util;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.PlatformUI;

public class UtilPlatform {

   public static void indentAndSave(ICompilationUnit cu) {
      CodeFormatter formatter = ToolFactory.createCodeFormatter(null);
      ISourceRange range;
      try {
    	 // System.out.println(cu.getElementName() + "," + cu.getSource());
         range = cu.getSourceRange();
         TextEdit indent_edit = formatter.format(CodeFormatter.K_COMPILATION_UNIT, //
               cu.getSource(), range.getOffset(), range.getLength(), 0, null);
         cu.applyTextEdit(indent_edit, null);
         cu.reconcile(ICompilationUnit.NO_AST, ICompilationUnit.FORCE_PROBLEM_DETECTION, null, null);
      } catch (JavaModelException e) {
         e.printStackTrace();
      }
      PlatformUI.getWorkbench().saveAllEditors(false);
   }
   
   /**
    * Reads a ICompilationUnit and creates the AST DOM for manipulating the
    * Java source file.
    * Constant for indicating the AST API that handles JLS8.
    * This API is capable of handling all constructs in the Java language
    * as described in the Java Language Specification, Java SE 8 Edition
    * (JLS8) as specified by JSR337. JLS8 is a superset of all earlier
    * versions of the Java language, and the JLS8 API can be used to
    * manipulate programs written in all versions of the Java language
    * up to and including Java SE 8 (aka JDK 1.8).
    */
   public static CompilationUnit parse(ICompilationUnit unit) {
      ASTParser parser = ASTParser.newParser(AST.JLS8);
      parser.setKind(ASTParser.K_COMPILATION_UNIT);
      parser.setSource(unit);
      parser.setResolveBindings(true);
      return (CompilationUnit) parser.createAST(null); // parse
   }
}
