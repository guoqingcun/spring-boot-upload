/**
 * 
 */
package cn.onlon.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author CTB-BJB-0081
 *
 */
public class FileUtils {
	
	public static List<File> directorToSort(File fD) {
		List<File> fileList = null;
		File[] fileArray = FileUtils.filterOutFolders(fD);

		// 转成集合，便于排序
		fileList = new ArrayList<File>(Arrays.asList(fileArray));
		Collections.sort(fileList, new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				// 从小到大排序
				if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())) {
					return -1;
				}
				return 1;
			}
		});
		
		return fileList;
	}
	/**
	 * 
	 * @Title: deleteDirectory
	 * @Description: TODO(删除目录)
	 * @param @param fD 参数
	 * @return void 返回类型
	 * @throws
	 */
	public static void deleteDirectory(File fD) {
		// 清除文件加
		if (fD.isDirectory() && fD.exists()) {
			fD.delete();
		}

	}

	/**
	    * 过滤掉目录
	 * 
	 * @param file
	 * @return
	 */
	public static File[] filterOutFolders(File file) {
		File[] fileArray = null;
		if (null != file) {
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

	/**
	 * 获取文件名称     
	 * 
	 * @param headerStr     
	 * @return     
	 */
	public static String getfilename(String headerStr) {
		return headerStr.substring(headerStr.lastIndexOf("=") + 2, headerStr.length() - 1);
	}

	/**
	 * 获取文件后缀     
	 */
	public static String getSuffix(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."), fileName.length());
	}
	
	/**
	 * 获取文件后缀     
	 */
	public static File getFileByName(File directory,String fileName) {
		File result = null;
		File[] fileArray = directory.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if(name.contains(fileName)) {
					return true;
				}
				return false;
			}
		});
		if(fileArray.length > 0) {
			result = fileArray[0];
		}
		return result;
	}
}
