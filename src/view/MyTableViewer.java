package view;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import model.OrganizerModelProvider;
import model.editing.ClassOrPackageNameEditingSupport;
import model.editing.MatchEditingSupport;
import model.editing.MethodOrVariableNameEditingSupport;
import model.editing.OrderEditingSupport;
import model.editing.MethodOrVariableEditingSupport;
import model.editing.ClassOrPackageEditingSupport;
import model.filter.OrganizerFilter;
import model.labelprovider.MethodOrVariableLabelProvider;
import model.labelprovider.MethodOrVariableNameLabelProvider;
import model.labelprovider.OrderLabelProvider;
import model.labelprovider.ClassOrPackageLabelProvider;
import model.labelprovider.ClassOrPackageNameLabelProvider;
import model.labelprovider.MatchLabelProvider;
import model.sorter.OrganizerSorter;

public class MyTableViewer {
   public final static String ID        = "project-demo.partdescriptor.organizer";
   public final static String POPUPMENU = "project-demo.popupmenu.organizer";
   public final static String filePath  = "C:\\Users\\Treyten\\workspaceCSCI4980\\workspaceCSCI4980carey\\project-demo\\saveorganizers.txt";

   private TableViewer        viewer;
   private OrganizerFilter       filter;
   private Text               searchText;
   private OrganizerSorter       organizerSorter;

   @Inject
   private ESelectionService  selectionService;

   /**
    * Create contents of the view part.
    */
   @PostConstruct
   public void createControls(Composite parent, EMenuService menuService) {
      GridLayout layout = new GridLayout(2, false);
      parent.setLayout(layout);

      createSearchText(parent);
      createViewer(parent);
      // register context menu on the table
      menuService.registerContextMenu(viewer.getControl(), POPUPMENU);
      addKeyEventToSearchText();
   }

   private void createSearchText(Composite parent) {
      Label searchLabel = new Label(parent, SWT.NONE);
      searchLabel.setText("Search: ");
      searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
      searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
   }

   private void addKeyEventToSearchText() {
      searchText.addKeyListener(new KeyAdapter() {
         public void keyReleased(KeyEvent ke) {
            filter.setSearchText(searchText.getText());
            viewer.refresh();
         }
      });
   }

   private void createViewer(Composite parent) {
      viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
      createColumns(parent, viewer);
      final Table table = viewer.getTable();
      table.setHeaderVisible(true);
      table.setLinesVisible(true);

      viewer.setContentProvider(ArrayContentProvider.getInstance());
      viewer.setInput(OrganizerModelProvider.INSTANCE.getOrganizers());
      viewer.addSelectionChangedListener(new ISelectionChangedListener() {
         @Override
         public void selectionChanged(SelectionChangedEvent event) {
            IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
            Object firstElement = selection.getFirstElement();
            System.out.println("Do something with it: " + firstElement);

            // set the selection to the service
            selectionService.setSelection(selection.size() == 1 ? //
            firstElement : selection.toArray());
         }
      });

      GridData gridData = new GridData();
      gridData.verticalAlignment = GridData.FILL;
      gridData.horizontalSpan = 2;
      gridData.grabExcessHorizontalSpace = true;
      gridData.grabExcessVerticalSpace = true;
      gridData.horizontalAlignment = GridData.FILL;
      viewer.getControl().setLayoutData(gridData);
      filter = new OrganizerFilter();
      viewer.addFilter(filter);
      organizerSorter = new OrganizerSorter();
      viewer.setComparator(organizerSorter);
   }

   private void createColumns(final Composite parent, final TableViewer viewer) {
      String[] titles = { "Is Class or Package", "Class/Package Name", "Is Method or Variable", "Method/Variable Name", "Matches", "Order" };
      int[] bounds = { 100, 100, 100, 100, 100, 100 };

      TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
      col.setLabelProvider(new ClassOrPackageLabelProvider(searchText));
      col.setEditingSupport(new ClassOrPackageEditingSupport(viewer));

      col = createTableViewerColumn(titles[1], bounds[1], 1);
      col.setLabelProvider(new ClassOrPackageNameLabelProvider(searchText));
      col.setEditingSupport(new ClassOrPackageNameEditingSupport(viewer));
      
      col = createTableViewerColumn(titles[2], bounds[2], 2);
      col.setLabelProvider(new MethodOrVariableLabelProvider(searchText));
      col.setEditingSupport(new MethodOrVariableEditingSupport(viewer));
      
      col = createTableViewerColumn(titles[3], bounds[3], 3);
      col.setLabelProvider(new MethodOrVariableNameLabelProvider(searchText));
      col.setEditingSupport(new MethodOrVariableNameEditingSupport(viewer));
      
      col = createTableViewerColumn(titles[4], bounds[4], 4);
      col.setLabelProvider(new MatchLabelProvider(searchText));
      col.setEditingSupport(new MatchEditingSupport(viewer));
      
      col = createTableViewerColumn(titles[5], bounds[5], 5);
      col.setLabelProvider(new OrderLabelProvider(searchText));
      col.setEditingSupport(new OrderEditingSupport(viewer));
   }

   private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
      final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
      final TableColumn column = viewerColumn.getColumn();
      column.setText(title);
      column.setWidth(bound);
      column.setResizable(true);
      column.setMoveable(true);
      column.addSelectionListener(getSelectionAdapter(column, colNumber));
      return viewerColumn;
   }

   private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index) {
      SelectionAdapter selectionAdapter = new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
        	 organizerSorter.setColumn(index);
            int dir = organizerSorter.getDirection();
            viewer.getTable().setSortDirection(dir);
            viewer.getTable().setSortColumn(column);
            viewer.refresh();
         }
      };
      return selectionAdapter;
   }

   public TableViewer getViewer() {
      return viewer;
   }

   @PreDestroy
   public void dispose() {
   }

   @Focus
   public void setFocus() {
   }

   public void refresh() {
      viewer.refresh();
   }
}
