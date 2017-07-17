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


@WebServlet("/checkServlet")
public class checkServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	public void wrong1(){
    	String msg = "时间同步错误！";
    	int type = JOptionPane.YES_NO_CANCEL_OPTION;
    	String title = "信息提示";
    	JOptionPane.showMessageDialog(null, msg, title, type);
    }
	
	public void wrong2(){
    	String msg = "时间同步失败！";
    	int type = JOptionPane.YES_NO_CANCEL_OPTION;
    	String title = "信息提示";
    	JOptionPane.showMessageDialog(null, msg, title, type);
    }
    public checkServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String checkotp = new String(request.getParameter("myotp").getBytes("ISO-8859-1"), "UTF-8");
        System.out.println(checkotp);
		String otp = new String(request.getParameter("otp").getBytes("ISO-8859-1"), "UTF-8");
		String userName = new String(request.getParameter("userName").getBytes("ISO-8859-1"), "UTF-8");		
        String[] checkotps = checkotp.split(" ");
        int otpOffset = 0;
        if(checkotp.equals(""))
		{
			wrong2();
			response.sendRedirect("http://localhost:8080/IDtest2/mainpage.jsp");
		}
        else{
        boolean flag = false;
        	for(int i = 0; i<5; i++){
        		if(checkotps[i].equals(otp)){
        			otpOffset = 2-i;
        			flag = true;
        			break;
        		}
        		
        	}
        if (!flag) {
        	wrong1();
        }
    
   		Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
		try{
			
	        Class.forName("com.mysql.jdbc.Driver");
	        con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/users?useUnicode=true&chatacterEncoding=gbk",
					"root","1234");  
	        
	        stmt = con.createStatement();
	        String sql = "select * from user where username='"+userName+"'";
	        rs = stmt.executeQuery(sql);

	        rs.next();
			
		
				String sql2 = "update user set totpOffset = '" + otpOffset + "' where userName = '" + userName+"'";
				stmt.executeUpdate(sql2);
				 tip();
	        rs.close();
			stmt.close();
			con.close();        	
		}catch(Exception e){
			e.printStackTrace();
		}finally{

			
		}
	
	
			
		
		
		//response.sendRedirect("http://localhost:8080/IDtest/");
		 String appContext = request.getContextPath();
		 String basePath = request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort() + appContext; 
		 
		response.sendRedirect(basePath+"/index.jsp");
        }
	}
	public void tip(){
    	String msg = "同步成功！";
    	int type = JOptionPane.YES_NO_CANCEL_OPTION;
    	String title = "信息提示";
    	JOptionPane.showMessageDialog(null, msg, title, type);}


	

}
