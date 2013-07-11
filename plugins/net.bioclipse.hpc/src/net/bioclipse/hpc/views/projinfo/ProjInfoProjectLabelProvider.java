package net.bioclipse.hpc.views.projinfo;

import net.bioclipse.hpc.domains.hpc.Person;
import net.bioclipse.hpc.domains.hpc.Project;

import org.eclipse.swt.graphics.Image;

/**
 * This class provides the labels for the PlayerTableTree application
 */

public class ProjInfoProjectLabelProvider extends ProjInfoPersonLabelProvider {
	
	public void ProjInfoPersonLabelProvider() {
		// Nothing
	}
  /**
   * Gets the text for the specified column
   * 
   * @param obj
   *            the player or team
   * @param colNo
   *            the column
   * @return String
   */
  public String getColumnText(Object obj, int colNo) {
    if (obj instanceof Person)
      return super.getColumnText(obj, colNo);
    Project group = (Project) obj;
    switch (colNo) {
	  case 0:
		  return group.getGroupName();
	  case 1:
		  return group.getGroupUsedHours();
	  case 2:
		  return group.getGroupCurrentAllocation();
    }
    return null;
  }
}
