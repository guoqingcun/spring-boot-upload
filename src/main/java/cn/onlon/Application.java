package cn.onlon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
@ServletComponentScan
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@RequestMapping("/")
	public String index(ModelMap map) {
	// 加⼊⼀个属性，⽤来在模板中读取
	map.addAttribute("host", "http://blog.didispace.com2");
	// return模板⽂件的名称，对应src/main/resources/templates/index.html
	return "index";
	}
}