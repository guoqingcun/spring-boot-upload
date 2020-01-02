package cn.onlon.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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
 * @ClassName: DownloadActionServlet
 * @Description: TODO(下载文件)
 * @author GuoQingcun
 * @date 2020-01-02 03:46:48
 */
@WebServlet(name = "DownloadActionServlet", urlPatterns = "/DownloadActionServlet")
public class DownloadActionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${file.upload.directory}")
	public String FILE_DIRECTORY;

	/**
	 * 下载主方法
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String fileMd5 = request.getParameter("FN");
		logger.info("准备下载文件:{}",fileMd5);
		

		// 读取目录所有文件
		File directory = new File(FILE_DIRECTORY );
		File downFile = FileUtils.getFileByName(directory, fileMd5);

		if(null == downFile) {
			logger.info("文件不存在:{}",fileMd5);
			return ;
		}
		
		//准备下载
		int len=0;
		InputStream in=new FileInputStream(downFile);
		String fileName = downFile.getName();
		byte[] buffer = new byte[1024];

		//设置context-disposition响应头，控制浏览器以下载形式打开，这里注意文件字符集编码格式，设置utf-8，不然会出现乱码
		response.setHeader("content-disposition","attachment;filename="+URLEncoder.encode(fileName,"UTF-8"));
		ServletOutputStream out = response.getOutputStream();
		
		//将FileInputStream流写入到buffer缓冲区
		while((len=in.read(buffer))!=-1){
			//使用OutputStream将缓冲区的数据输出到客户端浏览器
			out.write(buffer, 0, len);
		}
		in.close();

		logger.info("文件下载成功:{}",fileMd5);

	}

	/**
	 * to doGet() method
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
