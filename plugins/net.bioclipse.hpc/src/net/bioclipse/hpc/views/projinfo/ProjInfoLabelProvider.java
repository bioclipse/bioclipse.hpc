package net.bioclipse.hpc.views.projinfo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.bioclipse.hpc.domains.hpc.Person;
import net.bioclipse.hpc.domains.hpc.Project;
import net.bioclipse.hpc.images.ImageRetriever;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * This class provides the labels for PlayerTable
 */

class ProjInfoLabelProvider implements ITableLabelProvider {
	private Image groupImg;
	private Image userImg;

  // Constructs a PlayerLabelProvider
  public ProjInfoLabelProvider() {
		groupImg = new Image(null, ImageRetriever.class.getResourceAsStream("group.png"));
		userImg = new Image(null, ImageRetriever.class.getResourceAsStream("user.png"));
  }
  
  /**
   * Gets the text for the specified column
   * 
   * @param element
   *            the player
   * @param columnIndex
   *            the column
   * @return String
   */
  public String getColumnText(Object element, int columnIndex) {
	    if (element instanceof Person) {
			switch (columnIndex) {
				case 0:
					return ((Person) element).getName();
				case 1:
					return ((Person) element).getUsedHours();
				case 2:
					return ((Person) element).getCurrentAllocation();
				default:
					return "";				  
			}	    	
  		} else if (element instanceof Project) {
  			switch (columnIndex) {
			  	case 0:
			  		return ((Project) element).getGroupName();
			  	case 1:
			  		return ((Project) element).getGroupUsedHours();
			  	case 2:
			  		return ((Project) element).getGroupCurrentAllocation();
		  		default:
		  			return "";
	  			}
  		}
	    return "";
  }
  
  public Image getColumnImage(Object element, int columnIndex) {
	  if (columnIndex == 0) {
		  if (element instanceof Project) {
			  return groupImg;
		  } else if (element instanceof Person) {
			  return userImg;
		  }
	  }
	  return null;
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

           
