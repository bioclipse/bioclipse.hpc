package net.bioclipse.uppmax.business;

import org.eclipse.swt.graphics.Image;

/**
 * This class provides the labels for the PlayerTableTree application
 */

public class ProjInfoLabelProvider extends ProjInfoPersonLabelProvider {
	
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
    if (obj instanceof ProjInfoPerson)
      return super.getColumnText(obj, colNo);
    ProjInfoGroup group = (ProjInfoGroup) obj;
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
