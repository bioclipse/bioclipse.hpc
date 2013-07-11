package net.bioclipse.hpc.views.projinfo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.bioclipse.hpc.domains.hpc.Person;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * This class provides the labels for PlayerTable
 */

class ProjInfoPersonLabelProvider implements ITableLabelProvider {

  // Constructs a PlayerLabelProvider
  public ProjInfoPersonLabelProvider() {
    // Create the image
  }
  
  public Image getColumnImage(Object obj, int arg1) {
	  Image image = null;
	  // TODO: Return an image!
	  return image;
  }

  /**
   * Gets the text for the specified column
   * 
   * @param personObj
   *            the player
   * @param colIdx
   *            the column
   * @return String
   */
  public String getColumnText(Object personObj, int colIdx) {
	  switch (colIdx) {
	  case 0:
		  return ((Person) personObj).getName();
	  case 1:
		  return ((Person) personObj).getUsedHours();
	  case 2:
		  return ((Person) personObj).getCurrentAllocation();
	  }
	  return "";
  }

  /**
   * Adds a listener
   * 
   * @param arg0
   *            the listener
   */
  public void addListener(ILabelProviderListener arg0) {
    // Throw it away
  }

  /**
   * Dispose any created resources
   */
  public void dispose() {
  }

  /**
   * Returns whether the specified property, if changed, would affect the
   * label
   * 
   * @param arg0
   *            the player
   * @param arg1
   *            the property
   * @return boolean
   */
  public boolean isLabelProperty(Object arg0, String arg1) {
    return false;
  }

  /**
   * Removes the specified listener
   * 
   * @param arg0
   *            the listener
   */
  public void removeListener(ILabelProviderListener arg0) {
    // Do nothing
  }
}

           
