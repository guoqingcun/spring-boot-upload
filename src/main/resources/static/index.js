// 监听分块上传的时间点，断点续传
var fileMd5;
var chunkSize = 1024 * 1024;//1k
/**
 * 配置发送过程
 */
WebUploader.Uploader.register({
	"before-send-file":"beforeSendFile",
	"before-send":"beforeSend",
	"after-send-file":"afterSendFile"
	},{
		beforeSendFile:function(file) {
			// 创建一个deffered,用于通知是否完成操作
			var deferred = WebUploader.Deferred();
			
			// 计算文件的唯一标识，用于断点续传和妙传
			(new WebUploader.Uploader()).md5File(file, 0, chunkSize)
				.progress(function(percentage){
					$("#"+file.id).find("span.state").text("正在获取文件信息...");
				})
				.then(function(val) {
					fileMd5 = val;
					$("#" + file.id).find("span.state").text("成功获取文件信息");
					// 放行
					deferred.resolve();
				});
			// 通知完成操作
			return deferred.promise();
		},
		beforeSend:function(block) {
			var deferred = WebUploader.Deferred();
			
			// 支持断点续传，发送到后台判断是否已经上传过
			$.ajax(
				{
					type:"POST",
					url:"/UploadActionServlet?action=checkChunk",
					data:{
						// 文件唯一表示								
						fileMd5:fileMd5,
						// 当前分块下标
						chunk:block.chunk,
						// 当前分块大小
						chunkSize:block.end-block.start
					},
					dataType:"json",
					success:function(response) {
						if(response.ifExist) {
							// 分块存在，跳过该分块
							deferred.reject();
						} else {
							// 分块不存在或不完整，重新发送
							deferred.resolve();
						}
					}
				}
			);
			
			
			// 发送文件md5字符串到后台
			this.owner.options.formData.fileMd5 = fileMd5;
			return deferred.promise();
		},
		afterSendFile:function(file) {
			console.log(file)
			// 通知合并分块
			$.ajax(
				{
					type:"POST",
					url:"/UploadActionServlet?action=mergeChunks",
					data:{
						fileMd5:fileMd5
					},
					success:function(response){
						
					}
				}
			);
		}
	}
);

/**
 * 上传基本配置
 */
var uploader = WebUploader.create(
	{
		swf:"/js/Uploader.swf",
		server:"/FileUploadServlet",
		pick:"#filePicker",
		auto:true,
		dnd:"#dndArea",
		disableGlobalDnd:true,
		paste:"#uploader",
		chunked:true,// 是否分块
		chunkSize:chunkSize,// 每块文件大小（默认5M）,在此设置为1k
		threads:10,// 开启几个并非线程（默认3个）
		chunkRetry:5, //默认值：2
		prepareNextFile:true// 在上传当前文件时，准备好下一个文件
	}		
);

/**
 * 生成缩略图和上传进度
 * @param file
 * @returns
 */
uploader.on("fileQueued", function(file) {
		// 把文件信息追加到fileList的div中
		$("#fileList").append("<div id='" + file.id + "'><img/><span>" + file.name + "</span><div><span class='state'></span></div><div><span class='percentage'></span></div></div>");
		
		// 制作缩略图
		// error：不是图片，则有error
		// src:代表生成缩略图的地址
		uploader.makeThumb(file, function(error, src) {
			if (error) {
				$("#" + file.id).find("img").replaceWith("<span>无法预览&nbsp;</span>");
			} else {
				$("#" + file.id).find("img").attr("src", src);
			}
		});
	}
);

/**
 * 监控上传进度
 * @param file
 * @param percentage 代表上传文件的百分比
 * @returns
 */
uploader.on("uploadProgress", function(file, percentage) {
	$("#" + file.id).find("span.percentage").text(Math.round(percentage * 100) + "%");
});