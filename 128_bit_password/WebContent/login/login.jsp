<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>登录</title>
</head>

<body bgcolor="#CCCCCC">
    <table border="0" width="100%" cellpadding="0" cellspacing="0">
    
    	<tr>
        	<td align="center">
            <h2>个人现金息管理系统</h2>
            </td>
        </tr>
        
        <tr>
        	<td align="center" bgcolor="#99aadd" width="70%">
            	<form action="http://localhost:8080/IDtest/LoginServlet1" method="post">
                	<table border="2" cellspacing="0" cellpadding="0" bgcolor="#95BDFF" width="350">
                    	<tr align="center">
                    		<td align="center" height="130">
                        	输入用户姓名：<input type="text" name="userName" size="16" /><br />
                            <p></p>
                            输入用户名密码：<input type="password" name="password" size="18" /><br />
                        	</td>
                        </tr>
                        
                        <tr>
                        	<td>
                        		<center>
                            	<input type="submit" value="确定" size="12" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <input type="reset" value="清除" size="12" />
                                </center>
                            </td>
                        </tr>
                        
                        <tr>
                        	<td>
                            	<p align="center">
                                	<a href="../register/register.jsp">注册</a>
                                </p>
                            </td>
                        </tr>
                        
                    </table>
                </form>
            </td>
        </tr>
        
    </table>

</body>
</html>