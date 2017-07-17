<%@page import="OTP.TOTP" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="myBean.loginBean" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<jsp:useBean id="totp" class="OTP.TOTP" scope="session"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    
<title>主页</title>

<script type="" src="jquery-1.11.2.min.js"></script>
</head>
<body bgcolor="CCCFFF">
<%
	String userName = null;
	ArrayList login = (ArrayList)session.getAttribute("login");
	loginBean A = (loginBean)request.getSession().getAttribute("loginBeanA");
	if(login==null || login.size()==0){
		response.sendRedirect("http://localhost:8080/IDtest2/login/login.jsp");		
	}else{
		for(int i=login.size()-1;i>=0;i--){
			loginBean nn= (loginBean)login.get(i);
			userName=nn.getUserName();
		}
	}
%>
	<table width="100%" align="right" bgcolor="#66CCFF">
		<tr>
	    	<td>欢迎，<%=userName%>登录系统</td>
	    </tr>
	    <tr align = "right" >
	    	<td align = "right" width = "20"><a href = "login/logout.jsp">退出登录</a></td>
	    </tr>
	</table>


<br></br>
			<%
				String otp = TOTP.getTOTP(A);
			%>	
	
	<div>
		<p>
			当前OTP：<%=otp %>
		</p>  	
		
		</div>
		


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
	<form name="myform">
	 		此动态码有效时间还有：<label id="myclock"></label>
	 		<%
	 			//response.setHeader("refresh","10");  

	 		%>
		</form>
	 <table width="100%" align="center" bgcolor="#66CCFF">

		<tr>
	    	<td align="center">
	    	操作模块
	    	</td>
	    </tr>
	    
	    </table>

	<form action="totpServlet" method="post">
        <center>
		<input type="hidden" name="userName" value="<%=userName%>"/>
		<p>&nbsp;转账金额：
			<input type="text" name="cash" />
		</p>
		<p></p><input type="hidden" name="otp" value="<%=otp %>" id="otp1"></input></p>
		<p>&nbsp;&nbsp;&nbsp;二维码：
			<input type="text" name="myotp" id="myotp"></input>
			<input type="button" value="获取" onclick="openwin('qrcode-reader-master/reader.html','480','400');"></input>
		</p>
		
		<p>提交消息：
			<input type="submit" value="确定" size="12" />
			&nbsp;&nbsp;&nbsp;&nbsp;
        	<input type="reset" value="清除" size="12" />
        </p>
       </center>
	</form>
	
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
	  <script language="javascript" > 
    
     $(function(){
    	 $("#myclock").text(30);
    	 setInterval("countDown()",1000);
    	 
    	 var $checkForm = $("#checkOTPForm");
    	 $checkForm.submit(function(e){
    		 
    		 //todo 把myotp的值复制下来
    		 var myotpValue = $("#myotp").val();
    	
    		 $("#myotp2").val(myotpValue);
    	
    		 return true;
    		 
    	 });
     });
     function countDown() {
    	 var $myclock = $("#myclock");
    	 var nowTime = $myclock.text();
    	 if (nowTime > 0) $myclock.text(nowTime-1);   	    	 
    	 if ($myclock.text() <= 0) document.location.reload();
     }
   	
    </script>
<form action="checkServlet" method="post" id="checkOTPForm">
 
			<input type="hidden" name="userName" value="<%=userName%>"/>
		<p><input type="hidden" name="otp" value="<%=otp %>"></input></p>
		<p>
		    <input type="hidden" name="myotp" id="myotp2"></input>
		</p>
		<center>
		<p>时间同步：
			<input type="submit" value="时间同步" size="12" />
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        </p>
        </center>
	</form>
</body>
</html>