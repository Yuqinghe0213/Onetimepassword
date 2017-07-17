<%@ page language="java"  pageEncoding="utf-8"%>
<html>
<head>
   	<meta charset="utf-8" />
	<title>Insert title here</title>
</head>
<body>	
	<script>
		//弹出窗口
		function openwin(url,width,height){   
		  	var l=window.screen.width ;   
		  	var w= window.screen.height;    
		  	var al=(l-width)/2;   
		  	var aw=(w-height)/2;   
		  	var OpenWindow=window.open(url,"弹出窗口",
		  			"toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=yes,width="
		  			+width+",height="+height+",top="+aw+",left="+al+"");       
			OpenWindow.focus();
			if(OpenWindow!=null){ //弹出窗口关闭事件
				if(window.attachEvent) OpenWindow.attachEvent("onunload", quickOut); 
			}
		}
		//关闭触发方法
		function quickOut(){
			alert("窗口已关闭");
		}
	</script>
	
	<form action="totpServlet" method="post" al>
		<p>用加密的TOTP验证：
			<input type="text" name="myotp" id="myotp"></input>
			<input type="button" value="获取" onclick="openwin('qrcode-reader-master/reader.html','480','400');"></input>
		</p>
		<p>提交消息：
			<input type="submit" value="确定" size="12" />
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        	<input type="reset" value="清除" size="12" />
        </p>
	</form>
	
</body>
</html>