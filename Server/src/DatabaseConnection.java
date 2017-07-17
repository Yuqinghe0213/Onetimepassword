import java.sql.*;
import java.util.UUID;
/**
 * This class is used to connect to database and update the data in the databased.
 * the operation includes add user, update user information, check user information
 */
public class DatabaseConnection {
    private static Connection con;
    private Statement st;
    private ResultSet rs;
    private String dbDriver = "com.mysql.jdbc.Driver";
    private String dbUrl = "jdbc:mysql://127.0.0.1:3306/clients?characterEncoding=utf8&useSSL=false";
    private String user = "root";
    private String password = "930213";

    public Connection DatabaseConnection ()
    {
        con = null;
        try{
            Class.forName(dbDriver);

        }catch (Exception e){
            System.out.println("Error: "+ e);
        }
        try{

            con = DriverManager.getConnection(dbUrl,user,password);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return con;
    }
    public void close_con() throws SQLException
    {
        con.close();
    }



    public String password_check(Connection conn,String username, String password)
    {
        String result = null;
        try {
            st = conn.createStatement();
            String sql = "select * from users where username='" + username + "'";
            rs = st.executeQuery(sql);
            int N = 0, P = 0;
            while (rs.next()) {
                if (rs.getInt("state") != 1) {
                    result = "p";
                } else {
                    if (username.equals(rs.getString("username"))) {
                        N = 10001;
                        String pass = rs.getString("password");
                        if (decryption.verifyPass(password,pass)) {
                            P = 10001;
                            result = rs.getString("id");
                        }
                    } else {
                        N++;
                    }
                    if (N < 10001) {
                        result = "n";
                    } else if (P < 10001) {
                        result = "w";

                    }
                }
            }
            if (result == null)
            {
                result = "n";
            }
            rs.close();
            st.close();
            conn.close();
            close_con();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("login result is " +result);
        return result;
    }
    public String getId(Connection conn,String username)
    {
        String result = null;
        try {
            st = conn.createStatement();
            String sql = "select * from users where username='" + username + "'";
            rs = st.executeQuery(sql);
            rs.next();
            result = rs.getString("id");
            rs.close();
            st.close();
            conn.close();
            close_con();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean usermanecheck(Connection conn,String username)
    {
        boolean result = false;
        try {
            st = conn.createStatement();
            String sql = "select * from users where username='" + username + "'";
            rs = st.executeQuery(sql);
            if (rs.next())
                result = true;
            rs.close();
            st.close();
            conn.close();
            close_con();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public String add_data(Connection conn,String username, String password,
                           String mobile, String email, String mobileid,
                           String symmekey)
    {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        String sql = "insert into users (username, password , id, mobile,email,mobileid,kskey,state,amount)" +
                " values (?,?,?,?,?,?,?,1,1000)";
        try {

            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, id);
            ps.setString(4, mobile);
            ps.setString(5, email);
            ps.setString(6, mobileid);
            ps.setString(7,symmekey);
            ps.executeUpdate();
            ps.close();
            conn.close();
            close_con();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id.toString();
    }

    public String searchmobile(Connection conn,String username)
    {
        String data = null;
        try {
            st = conn.createStatement();
            String sql = "select * from users where username='" + username + "'";
            rs = st.executeQuery(sql);
            rs.next();
            String mobile = rs.getString("mobile");
            String email = rs.getString("email");
            data = mobile+"/"+ email;
            System.out.println(data);
            rs.close();
            st.close();
            conn.close();
            close_con();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void changeemail(Connection conn,String username,
                               String mobile, String email)
    {
        String sql = "update users set mobile = ?, email = ? where username = ?";
        try {

            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1, mobile);
            ps.setString(2, email);
            ps.setString(3, username);
            ps.executeUpdate();
            ps.close();
            conn.close();
            close_con();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void lockaccount(Connection conn,String username)
    {
        String sql = "update users set state = ? where username = ?";
        try {

            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setInt(1, 0);
            ps.setString(2, username);
            ps.executeUpdate();
            ps.close();
            conn.close();
            close_con();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updatepwd(Connection conn,String username, String password)
    {
        String sql = "update users set password = ? where username = ?";
        try {

            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1, password);
            ps.setString(2, username);
            ps.executeUpdate();
            ps.close();
            conn.close();
            close_con();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String updatemobileid(Connection conn,String username, String mobileid)
    {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        String sql = "update users set mobileid = ?, id = ? where username = ?";
        try {
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1, mobileid);
            ps.setString(2, id);
            ps.setString(3,username);
            ps.executeUpdate();
            ps.close();
            conn.close();
            close_con();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }




}
