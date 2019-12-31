/**
 * 
 */
package cn.onlon.error;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *      全局异常处理
 * @author CTB-BJB-0081
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	public static final String DEFAULT_ERROR_VIEW = "error";
	
	/**
	 *      全局WEB异常处理方法
	 * @param req
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = Exception.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest req,Exception e) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("exception",e);
		mav.addObject("url",req.getRequestURL());
		mav.setViewName(DEFAULT_ERROR_VIEW);
		
		return mav;
	}
	
	/**
	 *      全局JSON异常处理方法
	 * @param req
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = ReturnJsonException.class)
	@ResponseBody
	public ErrorInfo<String> defaultJsonErrorHandler(HttpServletRequest req,ReturnJsonException e) {
		ErrorInfo<String> r = new ErrorInfo<>();
		r.setMessage(e.getMessage());
		r.setCode(ErrorInfo.ERROR);
		r.setData("Some Data");
		r.setUrl(req.getRequestURL().toString());
		
		return r;
	}
}
