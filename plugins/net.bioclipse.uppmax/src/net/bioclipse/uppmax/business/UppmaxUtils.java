package net.bioclipse.uppmax.business;

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

public class UppmaxUtils {
	
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

    public static String currentTime() {
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        return sdf.format(System.currentTimeMillis());
    }

	public static String arrayToString(String[] stringArray){
		String str = " ";
		for (int i = 0; i < stringArray.length; i++) {
			str = str + stringArray[i];
		}
		return str;
	}
}
