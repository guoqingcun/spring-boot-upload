package cn.onlon.web;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * 
 * @ClassName: FileUploadServlet
 * @Description: TODO(文件上传)
 * @author GuoQingcun
 * @date 2020-01-02 04:00:41
 */
@WebServlet(name = "FileUploadServlet", urlPatterns = "/FileUploadServlet")
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${file.upload.directory}")
	public String FILE_DIRECTORY = "e:/tmp/";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());


		// 1.创建DiskFileItemFactory对象，配置缓存用
		DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();

		// 2. 创建 ServletFileUpload对象
		ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);

		// 3. 设置文件名称编码
		servletFileUpload.setHeaderEncoding("utf-8");

		// 4. 开始解析文件
		// 文件md5获取的字符串
		String fileMd5 = null;
		// 文件的索引
		String chunk = null;
		try {
			List<FileItem> items = servletFileUpload.parseRequest(request);
			for (FileItem fileItem : items) {

				if (fileItem.isFormField()) { // >> 普通数据
					String fieldName = fileItem.getFieldName();
					if ("info".equals(fieldName)) {
						String info = fileItem.getString("utf-8");
						logger.info("info:{}",info);
					}
					if ("fileMd5".equals(fieldName)) {
						fileMd5 = fileItem.getString("utf-8");
						logger.info("fileMd5:{}",fileMd5);
					}
					if ("chunk".equals(fieldName)) {
						chunk = fileItem.getString("utf-8");
						logger.info("chunk:{}",chunk);
					}
				} else { // >> 文件
					// 如果文件夹没有创建文件夹
					File file = new File(FILE_DIRECTORY + "/" + fileMd5);
					if (!file.exists()) {
						file.mkdirs();
					}
					// 保存文件
					File chunkFile = new File(FILE_DIRECTORY + "/" + fileMd5 + "/" + chunk);
					FileUtils.copyInputStreamToFile(fileItem.getInputStream(), chunkFile);

				}

			}

		} catch (Exception e) {
			logger.error("上传文件过程异常",e);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
