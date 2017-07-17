package myServlet;



import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import java.util.Random; 
import java.security.MessageDigest; 



import OTP.RandomString;
import OTP.TOTP;
import myBean.loginBean;

/**
 * Servlet implementation class LoginServlet1
 */
@WebServlet("/LoginServlet1")
public class LoginServlet1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet1() {
        super();
        // TODO Auto-generated constructor stub
    }
    

    public void wrong1(){
    	String msg = "用户名不能为空!";
    	int type = JOptionPane.YES_NO_CANCEL_OPTION;
    	String title = "信息提示";
    	JOptionPane.showMessageDialog(null, msg, title, type);
    }
    public void wrong2(){
    	String msg = "登录密码不能为空，登录失败!";
    	int type = JOptionPane.YES_NO_CANCEL_OPTION;
    	String title = "信息提示";
    	JOptionPane.showMessageDialog(null, msg, title, type);
    }
    public void wrong3(){
    	String msg = "该用户尚未注册，登录失败!";
    	int type = JOptionPane.YES_NO_CANCEL_OPTION;
    	String title = "信息提示";
    	JOptionPane.showMessageDialog(null, msg, title, type);
    }
    public void wrong4(){
    	String msg = "用户密码不正确，登录失败!";
    	int type = JOptionPane.YES_NO_CANCEL_OPTION;
    	String title = "信息提示";
    	JOptionPane.showMessageDialog(null, msg, title, type);
    }
    public static boolean verify(String password, String md5) {  
        char[] cs1 = new char[32];  
        char[] cs2 = new char[16];  
        for (int i = 0; i < 48; i += 3) {  
            cs1[i / 3 * 2] = md5.charAt(i);  
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);  
            cs2[i / 3] = md5.charAt(i + 1);  
        }  
        String salt = new String(cs2);  
        return md5Hex(password + salt).equals(new String(cs1));  
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
		String password = new String(request.getParameter("password").getBytes("ISO-8859-1"), "UTF-8");
	
		if(userName.equals("")){
			//密码为空
			wrong1();
			response.sendRedirect("http://localhost:8080/IDtest2/index.jsp");
		}else if(password.equals("")){
			wrong2();
			response.sendRedirect("http://localhost:8080/IDtest2/index.jsp");
		}else{
			try{
				Connection con = null;
		        Statement stmt = null;
		        ResultSet rs = null;
		        Class.forName("com.mysql.jdbc.Driver");
		        con = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/users?useUnicode=true&chatacterEncoding=gbk",
						"root","1234");  
		        
		        stmt = con.createStatement();
		        String sql = "select * from user where username='"+userName+"'";
		        rs = stmt.executeQuery(sql);

		    	
	        	
	        	
		        int N=0, P=0;
		        while(rs.next()){
		        	if( userName.equals(rs.getString("userName")) ){
		        		N=1001;
		        		boolean flag = false;
		        		String password0 = rs.getString("password");
		        		flag = verify(password, password0);
		        		if(flag){
		        			P=1001;
		        			loginBean nn = new loginBean();
		        			nn.setUserName(userName);
		        			nn.setPassword(password);
		        			//获取session对象
		        			HttpSession session = request.getSession();
		        			ArrayList login = new ArrayList();
		        			login.add(nn);//把个人信息保存到列表中
		        			loginBean A = new loginBean();
		    	        	String code = rs.getString("checkCode");
		    	        	int totpOffset = rs.getInt("totpOffset");
		    	        	A.setcode(code);
		    	        	A.settotpOffset(totpOffset);
		    	        	request.getSession().setAttribute("loginBeanA", A);
		        			session.setAttribute("login", login);
		        			response.sendRedirect("http://localhost:8080/IDtest2/mainpage.jsp");
		        			/* RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		        		        String name = userName;
		        		        request.setAttribute("id",name);//存值
		        		        rd.forward(request,response);*/
		        		}else{
		        			
		        		}
		        	}else{
		        		N++;
		        	}
		        }//while
		        if(N<1001){
		        	wrong3();
					response.sendRedirect("http://localhost:8080/IDtest2/index.jsp");
		        }else if(P<1001){
		        	wrong4();
					response.sendRedirect("http://localhost:8080/IDtest2/index.jsp");
		        }		        

				rs.close();
				stmt.close();
				con.close();
				
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
