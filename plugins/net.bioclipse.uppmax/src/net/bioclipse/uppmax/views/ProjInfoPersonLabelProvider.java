package net.bioclipse.uppmax.views;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.bioclipse.uppmax.domains.hpc.Person;

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
  
  public Image getColumnImage(Object arg0, int arg1) {
	  Image image = null;
	  // do nothing ...
	  return image;
  }

  /**
   * Gets the text for the specified column
   * 
   * @param arg0
   *            the player
   * @param arg1
   *            the column
   * @return String
   */
  public String getColumnText(Object arg0, int arg1) {
	  Person person = (Person) arg0;
	  switch (arg1) {
	  case 0:
		  return person.getName();
	  case 1:
		  return person.getUsedHours();
	  case 2:
		  return person.getCurrentAllocation();
	  }
	  return "Empty text";
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

           
