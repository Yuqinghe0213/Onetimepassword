package myServlet;



import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;
import java.util.Random; 
import java.security.MessageDigest; 


import OTP.RandomString;
import OTP.TOTP;

/**
 * Servlet implementation class RegisterServlet1
 */
@WebServlet("/RegisterServlet1")
public class RegisterServlet1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet1() {
        super();
        // TODO Auto-generated constructor stub
    }
    

    public void wrong1(){
    	String msg = "不允许有空，注册失败!";
    	int type = JOptionPane.YES_NO_CANCEL_OPTION;
    	String title = "信息提示";
    	JOptionPane.showMessageDialog(null, msg, title, type);
    }
    public void wrong2(){
    	String msg = "两次密码不同，注册失败!";
    	int type = JOptionPane.YES_NO_CANCEL_OPTION;
    	String title = "信息提示";
    	JOptionPane.showMessageDialog(null, msg, title, type);
    }
    public void wrong3(){
    	String msg = "用户名已存在，注册失败!";
    	int type = JOptionPane.YES_NO_CANCEL_OPTION;
    	String title = "信息提示";
    	JOptionPane.showMessageDialog(null, msg, title, type);
    }
    public void right(){
    	String msg = "注册信息合格，注册成功!";
    	int type = JOptionPane.YES_NO_CANCEL_OPTION;
    	String title = "信息提示";
    	JOptionPane.showMessageDialog(null, msg, title, type);
    }
    public static String generate(String password) {  
        Random r = new Random();  
        StringBuilder sb = new StringBuilder(16);  
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));  
        int len = sb.length();  
        if (len < 16) {  
            for (int i = 0; i < 16 - len; i++) {  
                sb.append("0");  
            }  
        }  
        String salt = sb.toString();  
        password = md5Hex(password + salt);  
        char[] cs = new char[48];  
        for (int i = 0; i < 48; i += 3) {  
            cs[i] = password.charAt(i / 3 * 2);  
            char c = salt.charAt(i / 3);  
            cs[i + 1] = c;  
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);  
        }  
        return new String(cs);  
    } 
    public static String md5Hex(String src) {  
        try {  
            MessageDigest md5 = MessageDigest.getInstance("MD5");  
            byte[] bs = md5.digest(src.getBytes());  
            String result = TOTP.bytesToHexString(bs);
            return result;  
        } catch (Exception e) {  
            return null;  
        }  
    } 
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userName = new String(request.getParameter("userName").getBytes("ISO-8859-1"), "UTF-8");
		String password1 = new String(request.getParameter("password1").getBytes("ISO-8859-1"), "UTF-8");
		String password2 = new String(request.getParameter("password2").getBytes("ISO-8859-1"), "UTF-8");
		String email = new String(request.getParameter("email").getBytes("ISO-8859-1"), "UTF-8");
		String cash = new String(request.getParameter("cash").getBytes("ISO-8859-1"), "UTF-8");
		Integer cashLog = Integer.valueOf(cash);
		if(userName.length() == 0 || password1.length() == 0 || password2.length() == 0){
			wrong1();
			response.sendRedirect("http://localhost:8080/IDtest2/register/register.jsp");
		}else if( !(password1.equals(password2)) ){
			wrong2();
			response.sendRedirect("http://localhost:8080/IDtest2/register/register.jsp");
		}else{
			try{
				Connection con = null;
		        Statement stmt = null;
		        ResultSet rs = null;
		        
		        Class.forName("com.mysql.jdbc.Driver");
		        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users","root","1234");  
		        
		        stmt = con.createStatement();
		        String sql = "select * from user where username='"+userName+"'";
		        rs = stmt.executeQuery(sql);
				rs.last();
				
				int k;
				k = rs.getRow();
				if(k>0){
					wrong3();
					response.sendRedirect("http://localhost:8080/IDtest2/register/register.jsp");
				}else{
					String random = RandomString.uuid2();
					String password = generate(password1);
					System.out.println(password);
					String checkcode = random;
					String sql2 = "insert into user" + "(userName, password, email, checkCode, cash)" + "values(" + "'" + userName + "'" + "," + "'" + password + "'" + "," + "'" + email + "'"+","+"'"+ checkcode +"'"+","+"'"+cashLog+"'" + ")";
					stmt.executeUpdate(sql2);
				}
				rs.close();
				stmt.close();
				con.close();
			response.sendRedirect("http://localhost:8080/IDtest2/index.jsp");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
