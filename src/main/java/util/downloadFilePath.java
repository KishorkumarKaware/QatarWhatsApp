package util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import com.vyoms.whatsapp.util.*;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class downloadFilePath {

	public static void copyFile_Directory(String origin, String destDir, String destination) throws IOException {

		Path FROM = Paths.get(origin);
		Path TO = Paths.get(destination);
		File directory = new File(String.valueOf(destDir));

		if (!directory.exists()) {
			directory.mkdir();
		}
		//overwrite the destination file if it exists, and copy
		// the file attributes, including the rwx permissions
		CopyOption[] options = new CopyOption[]{
				StandardCopyOption.REPLACE_EXISTING,
				StandardCopyOption.COPY_ATTRIBUTES

		};
		Files.copy(FROM, TO, options);



	}
	public static File getTheNewestFile(String filePath, String ext) {
		File theNewestFile = null;
		File dir = new File(filePath);
		FileFilter fileFilter = new WildcardFileFilter("*." + ext);
		File[] files = dir.listFiles(fileFilter);
		System.out.println("File present +"+files.length);
		if (files.length > 0) {
			/** The newest file comes first **/
			Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
			theNewestFile = files[0];
		}

		return theNewestFile;
	}
	public static void main(String[] args) throws IOException
	{
		System.out.println("Using Directory:- "+Constants.downloadFilepath);
		System.out.println(getTheNewestFile(Constants.downloadFilepath, "jpeg"));
		copyFile_Directory(getTheNewestFile(Constants.downloadFilepath, "jpeg").toString(), "Nobita", "C:\\Users\\HP LAPTOP\\Desktop\\AutoomationEdgeData\\QatarData\\ImagePath\\Images");
	}
}
