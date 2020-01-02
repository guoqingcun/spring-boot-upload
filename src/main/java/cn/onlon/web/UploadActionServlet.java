package cn.onlon.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import cn.onlon.utils.FileUtils;

/**
 * 
 * @ClassName: UploadActionServlet
 * @Description: TODO(上传文件)
 * @author GuoQingcun
 * @date 2020-01-02 03:49:24
 */
@WebServlet(name = "UploadActionServlet", urlPatterns = "/UploadActionServlet")
public class UploadActionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${file.upload.directory}")
	public String FILE_DIRECTORY;

	/**
	 * 上传主方法
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String action = request.getParameter("action");
		logger.info("上传动作:{}",action);
		
		if ("mergeChunks".equals(action)) {
			// 获得需要合并的目录
			String fileMd5 = request.getParameter("fileMd5");
			String fileName = request.getParameter("fileName");
			String fileSuffix = FileUtils.getSuffix(fileName);

			// 创建文件
			File outputFile = new File(FILE_DIRECTORY + "/" + fileMd5 + fileSuffix);
			outputFile.createNewFile();

			// 输出流
			FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
			FileChannel outChannel = fileOutputStream.getChannel();

			// 合并
			File fD = new File(FILE_DIRECTORY + "/" + fileMd5);
			// 目录排序
			List<File> fileList = FileUtils.directorToSort(fD);
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

			//删除子文件目录
			FileUtils.deleteDirectory(fD);
			
			logger.info("合并文件成功:{}",fileMd5 + fileSuffix);

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
