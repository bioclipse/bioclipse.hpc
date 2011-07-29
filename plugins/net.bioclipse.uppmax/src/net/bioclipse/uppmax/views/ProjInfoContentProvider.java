package net.bioclipse.uppmax.views;

import net.bioclipse.uppmax.domains.hpc.Person;
import net.bioclipse.uppmax.domains.hpc.Project;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * This class provides the content for the TableTreeViewer in ProjInfoPersonTableTree
 */
public class ProjInfoContentProvider implements ITreeContentProvider {
  private static final Object[] EMPTY = new Object[] {};

  /**
   * Gets the children for a ProjInfoGroup or ProjInfoPerson
   * 
   * @param arg0
   *            the ProjInfoGroup or ProjInfoPerson
   * @return Object[]
   */
  public Object[] getChildren(Object arg0) {
    if (arg0 instanceof Project)
      return ((Project) arg0).getProjInfoPersons().toArray();
    // ProjInfoPersons have no children . . . except Shawn Kemp
    return EMPTY;
  }

  /**
   * Gets the parent ProjInfoGroup for a ProjInfoPerson
   * 
   * @param arg0
   *            the ProjInfoPerson
   * @return Object
   */
  public Object getParent(Object arg0) {
    return ((Person) arg0).getProjInfoGroup();
  }

  /**
   * Gets whether this ProjInfoGroup or ProjInfoPerson has children
   * 
   * @param arg0
   *            the ProjInfoGroup or ProjInfoPerson
   * @return boolean
   */
  public boolean hasChildren(Object arg0) {
    return getChildren(arg0).length > 0;
  }

  /**
   * Gets the elements for the table
   * 
   * @param arg0
   *            the model
   * @return Object[]
   */
  public Object[] getElements(Object arg0) {
    // Returns all the ProjInfoGroups in the model
    return ((ProjInfoContentModel) arg0).getProjInfoGroups().toArray();
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