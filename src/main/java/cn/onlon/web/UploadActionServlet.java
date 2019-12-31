package cn.onlon.web;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.onlon.utils.FileUtils;

/**
 * 合并上传文件
 */
@WebServlet(name = "UploadActionServlet", urlPatterns = "/UploadActionServlet")
public class UploadActionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	private String FILE_DIRECTORY = "e:/tmp/";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("进入合并后台...");
		
		String action = request.getParameter("action");
		if ("mergeChunks".equals(action)) {
			// 获得需要合并的目录
			String fileMd5 = request.getParameter("fileMd5");

			// 读取目录所有文件
			File f = new File(FILE_DIRECTORY + "/" + fileMd5);
			File[] fileArray = FileUtils.filterOutFolders(f);

			// 转成集合，便于排序
			List<File> fileList = new ArrayList<File>(Arrays.asList(fileArray));
			// 从小到大排序
			Collections.sort(fileList, new Comparator<File>() {

				@Override
				public int compare(File o1, File o2) {
					if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())) {
						return -1;
					}
					return 1;
				}

			});

			// 新建保存文件
			File outputFile = new File(FILE_DIRECTORY + "/" + UUID.randomUUID().toString() + ".zip");

			// 创建文件
			outputFile.createNewFile();

			// 输出流
			FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
			FileChannel outChannel = fileOutputStream.getChannel();

			// 合并
			FileChannel inChannel;
			for (File file : fileList) {
				inChannel = new FileInputStream(file).getChannel();
				inChannel.transferTo(0, inChannel.size(), outChannel);
				inChannel.close();

				// 删除分片
				file.delete();
			}

			// 关闭流
			fileOutputStream.close();
			outChannel.close();

			// 清除文件加
			File tempFile = new File(FILE_DIRECTORY + "/" + fileMd5);
			if (tempFile.isDirectory() && tempFile.exists()) {
				tempFile.delete();
			}

			logger.info("合并文件成功");

		} else if ("checkChunk".equals(action)) {
			// 校验文件是否已经上传并返回结果给前端

			// 文件唯一表示								
			String fileMd5 = request.getParameter("fileMd5");
			// 当前分块下标
			String chunk = request.getParameter("chunk");
			// 当前分块大小
			String chunkSize = request.getParameter("chunkSize");

			// 找到分块文件
			File checkFile = new File(FILE_DIRECTORY + "/" + fileMd5 + "/" + chunk);

			// 检查文件是否存在，且大小一致
			response.setContentType("text/html;charset=utf-8");
			if (checkFile.exists() && checkFile.length() == Integer.parseInt((chunkSize))) {
				response.getWriter().write("{\"ifExist\":1}");
			} else {
				response.getWriter().write("{\"ifExist\":0}");
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
