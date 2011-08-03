package net.bioclipse.hpc.business;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.bioclipse.hpc.Activator;
import net.bioclipse.hpc.domains.application.HPCApplication;
import net.bioclipse.hpc.xmldisplay.XmlUtils;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;

public class HPCUtils {

	public static HPCApplication getApplication() {
		Activator plugin = Activator.getDefault();
		HPCApplication application = plugin.application;
		return application;
	}

	public static String[] readFileToStringArray(String filePath) {
		File file = new File(filePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		List<String> stringBuffer = new ArrayList<String>();

		try {
			fis = new FileInputStream(file);

			// Here BufferedInputStream is added for fast reading.
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);

			// dis.available() returns 0 if the file does not have more lines.
			int i = 0;
			while (dis.available() != 0) {
				stringBuffer.add(dis.readLine());
			}

			// dispose all the resources after using them.
			fis.close();
			bis.close();
			dis.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] result = stringBuffer.toArray(new String[]{});
		return result;
	}
	
	public static String getFileAbsolutePathFromSelection(IStructuredSelection sel) {
		Object selObj = sel.getFirstElement();
		String remFileAbsPath = null;
		if (selObj instanceof IRemoteFile){
			IRemoteFile remFile = (IRemoteFile) selObj;
			remFileAbsPath = remFile.getAbsolutePath();
		}
		return remFileAbsPath;
	}	

	public static String currentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
		return sdf.format(System.currentTimeMillis());
	}

	public static String dateTimeStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.HHmm");
		return sdf.format(System.currentTimeMillis());
	}

	public static String dateStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(System.currentTimeMillis());
	}

	public static List<String> getMatches(String regexPattern, String text) {
		List<String> result = new ArrayList<String>();
		Pattern p = Pattern.compile(regexPattern);
		Matcher m = p.matcher(text);
		while (m.find()) {
			String currentMatchString = m.group();
			result.add(currentMatchString); 
		}
		return result;
	}

	public static String getMatch(String regexPattern, String text, int group) {
		String result = null;
		Pattern p = Pattern.compile(regexPattern);
		Matcher m = p.matcher(text);
		if (m.find()) {
			result = m.group(group);
		}
		return result;
	}

	public static String arrayToString(String[] stringArray){
		String str = " ";
		for (int i = 0; i < stringArray.length; i++) {
			str = str + stringArray[i];
		}
		return str;
	}

	public static String[] removeXmlDefinitionLine(String[] fileContentLines) {
		// Remove the initial XML header, if existing, since it messes up the SAX parser
		fileContentLines[0] = fileContentLines[0].replaceFirst("<\\?xml.*\\?>", ""); 
		return fileContentLines;
	}

	public static String[] stringListToArray(List<String> stringList) {
		String[] stringArray = stringList.toArray(new String[stringList.size()]);
		return stringArray;
	}

	public static String ensureEndsWithColon(String labelName) {
		if (!labelName.endsWith(":")) {
			labelName = labelName + ":";
		}
		return labelName;
	}

	public static void createGridLayout(Composite composite, int columnsCount) {
		// create the desired layout for this wizard page
		GridLayout gl = new GridLayout();
		gl.numColumns = columnsCount;
		composite.setLayout(gl);
	}

	public static Combo createCombo(Composite composite) {
		GridData gridData;
		Combo combo = new Combo(composite, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = GridData.BEGINNING;
		combo.setLayoutData(gridData);
		return combo;
	}

	public static int[] range(int start, int end, int increment) {
		int[] values = new int[Math.abs((end - start) / increment) + 1];
		boolean reverse = start > end;

		for (int i = start, index = 0; reverse ? (i >= end) : (i <= end); i+=increment, ++index)
		{
			values[index] = i;
		}
		return values;
	}

}
