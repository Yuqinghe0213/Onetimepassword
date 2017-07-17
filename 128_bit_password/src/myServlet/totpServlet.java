package myServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import myBean.loginBean;
import OTP.TOTP;

;

/**
 * Servlet implementation class totpServlet
 */
@WebServlet("/totpServlet")
public class totpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// public String code;
	static String code;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public totpServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String myotp = new String(request.getParameter("myotp").getBytes(
				"ISO-8859-1"), "UTF-8");
	    String otp = new String(request.getParameter("otp").getBytes("ISO-8859-1"), "UTF-8");
        System.out.println(myotp);
		String cash = new String(request.getParameter("cash").getBytes(
				"ISO-8859-1"), "UTF-8");
		String userName = new String(request.getParameter("userName").getBytes(
				"ISO-8859-1"), "UTF-8");
		
		loginBean A = (loginBean)request.getSession().getAttribute("loginBeanA");
        System.out.println(otp);
        if(cash.equals("")){
			wrong1();
			response.sendRedirect("http://localhost:8080/IDtest2/mainpage.jsp");
		}else if(myotp.equals(""))
		{
			wrong2();
			response.sendRedirect("http://localhost:8080/IDtest2/mainpage.jsp");
		}
        else{
			Integer cashInt = Integer.valueOf(cash);
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {

			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager
					.getConnection(
							"jdbc:mysql://localhost:3306/users?useUnicode=true&chatacterEncoding=gbk",
							"root", "1234");

			stmt = con.createStatement();
			//System.out.println(totpOffset);
			
			if (!(myotp.equals(otp))) {
				wrong("动态密码错误，请重新输入!");
			} else {
				String sql = "select * from user where username='" + userName
						+ "'";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					Integer oldCash = rs.getInt("cash");

					// System.out.println(code);
					if (oldCash > cashInt) {
						oldCash = oldCash - cashInt;
						String sql2 = "update user set cash = " + oldCash
								+ " where username='" + userName + "'";
						stmt.executeUpdate(sql2);
						tip();
					} else {
						wrong("金额不足");
					}
					break;
				}// while
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		// response.sendRedirect("http://localhost:8080/IDtest/");
		String appContext = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + appContext;

		response.sendRedirect(basePath + "/mainpage.jsp");
	}
	}
	public void wrong(String errMsg) {
		String msg = "动态密码错误，请重新扫描或进行同步!";
		int type = JOptionPane.YES_NO_CANCEL_OPTION;
		String title = "信息提示";
		JOptionPane.showMessageDialog(null, errMsg, title, type);
	}
	public void wrong1(){
    	String msg = "金额不能为空";
    	int type = JOptionPane.YES_NO_CANCEL_OPTION;
    	String title = "信息提示";
    	JOptionPane.showMessageDialog(null, msg, title, type);
    }
	public void wrong2(){
    	String msg = "动态码为空，请输入";
    	int type = JOptionPane.YES_NO_CANCEL_OPTION;
    	String title = "信息提示";
    	JOptionPane.showMessageDialog(null, msg, title, type);
    }

	public void tip() {
		String msg = "交易成功!";
		int type = JOptionPane.YES_NO_CANCEL_OPTION;
		String title = "信息提示";
		JOptionPane.showMessageDialog(null, msg, title, type);
	}
}
