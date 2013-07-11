package net.bioclipse.hpc.views;

import net.bioclipse.hpc.domains.hpc.Person;
import net.bioclipse.hpc.domains.hpc.Project;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * This class provides the content for the TableTreeViewer in ProjInfoPersonTableTree
 */
public class ProjInfoContentProvider implements ITreeContentProvider {
  private static final Object[] EMPTY_OBJ = new Object[] {};

  /**
   * Gets the children for a ProjInfoGroup or ProjInfoPerson
   * 
   * @param element
   *            the ProjInfoGroup or ProjInfoPerson
   * @return Object[]
   */
  public Object[] getChildren(Object element) {
    if (element instanceof Project) {
        return ((Project) element).getProjInfoPersons().toArray();    	
    }
    // ProjInfoPersons have no children, so don't return anything for them.
    return EMPTY_OBJ;
  }

  /**
   * Gets the parent ProjInfoGroup for a ProjInfoPerson
   * 
   * @param element
   *            the ProjInfoPerson
   * @return Object
   */
  public Object getParent(Object element) {
    return ((Person) element).getProjInfoGroup();
  }

  /**
   * Gets whether this ProjInfoGroup or ProjInfoPerson has children
   * 
   * @param element
   *            the ProjInfoGroup or ProjInfoPerson
   * @return boolean
   */
  public boolean hasChildren(Object element) {
    return getChildren(element).length > 0;
  }

  /**
   * Gets the elements for the table
   * 
   * @param element
   *            the model
   * @return Object[]
   */
  public Object[] getElements(Object element) {
    // Returns all the ProjInfoGroups in the model
    return ((ProjInfoContentModel) element).getProjInfoGroups().toArray();
  }

  /**
   * Disposes any resources
   */
  public void dispose() {
    // We don't create any resources, so we don't dispose any
  }

  /**
   * Called when the input changes
   * 
   * @param arg0
   *            the parent viewer
   * @param arg1
   *            the old input
   * @param arg2
   *            the new input
   */
  public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
    // Nothing to do
  }
}