<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>注册页面</title>
</head>

<body bgcolor="CCCFFF">
	<table align="center">
    	<tr>
        	<td colspan="3" align="center">
            	<h3><font color="red">请填写以下信息</font></h3>
            </td>
        </tr>
        
        <tr>
        	<td>
            	<form action="http://localhost:8080/IDtest2/RegisterServlet1" method="post">
                	<table border="2" cellpadding="0" cellspacing="0" bgcolor="AAABBB">
                    	<tr>
                        	<td>登录名字</td>
                            <td><input type="text" name="userName" size="20" /></td>
                        </tr>
                    	<tr>
                        	<td>用户密码</td>
                            <td><input type="password" name="password1" size="20" /></td>
                        </tr>
                    	<tr>
                        	<td>重复密码</td>
                            <td><input type="password" name="password2" size="20" /></td>
                        </tr>
                    	<tr>
                        	<td>邮箱地址</td>
                            <td><input type="text" name="email" size="20" /></td>
                        </tr>
                        <tr>
                        	<td> 初始金额</td>
                            <td><input type="text" name="cash" size="20" /></td>
                        </tr>
                        <tr>
                        	<td>
                        	    <center>
                            	<input type="submit" value="确定" size="12" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <input type="reset" value="清除" size="12" />
                                </center>
                            </td>
                        </tr>
                    </table>
                </form>
            </td>
        </tr>
        
    </table>
</body>
</html>