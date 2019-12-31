/**   
* @Title: FileController.java 
* @Package cn.onlon.web 
* @Description: TODO(用一句话描述该文件做什么) 
* @author guoqingcun   
* @date 2019年12月28日 下午6:11:48 
* @version V1.0   
*/
package cn.onlon.web;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.onlon.error.ReturnJsonException;

/** 
* @ClassName: FileController 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author guoqingcun
* @date 2019年12月28日 下午6:11:48 
*  
*/
@Controller
public class FileController {

	private String serverPath = "e:/";
	
	@RequestMapping("/hello")
	public String hello() throws Exception {
		throw new Exception("发生错误");
	}
	
	@RequestMapping("/json")
	public String json() throws ReturnJsonException {
	throw new ReturnJsonException("发⽣错误2");
	}
	
	@RequestMapping("/upload1")
	public String upload1() {
		System.out.println("dfgd");
		return "upload1";
	}
	
	@RequestMapping("/upload2")
	public String upload2() {
		System.out.println("dfgd");
		return "upload2";
	}
	
	@RequestMapping("/do/upload1")
	public String doUpload1(HttpServletRequest request, HttpServletResponse response,@RequestParam(value = "file") MultipartFile file) throws IOException {

		// 文件的索引
		try {

			// 获取原文件名
		    String fileName = file.getOriginalFilename();
			File tempFile = new File(serverPath + "/" + fileName);
			FileUtils.copyInputStreamToFile(file.getInputStream(), tempFile);

			
			
			
			
			
			
			
			
			
			


		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
