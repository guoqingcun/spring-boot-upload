/**
 * 
 */
package cn.onlon.utils;

import java.io.File;
import java.io.FileFilter;

/**
 * @author CTB-BJB-0081
 *
 */
public class FileUtils {

	/**
	 *      过滤掉目录
	 * @param file
	 * @return
	 */
	public static File[] filterOutFolders(File file) {
		File[] fileArray = null;
		if(null !=  file) {
			fileArray = file.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File pathname) {
					if (pathname.isDirectory()) {
						return false;
					}
					return true;
					
				}
			});
		}
		return fileArray;
	}
}
