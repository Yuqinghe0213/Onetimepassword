package sample;
import java.sql.*;
import java.util.UUID;

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
                        if (password.equals(rs.getString("password"))) {
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
                           String mobile, String email, String mobileid)
    {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        String sql = "insert into users (username, password , id, mobile,email,mobileid,state,amount)" +
                " values (?,?,?,?,?,?,1,1000)";
        try {

            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, id);
            ps.setString(4, mobile);
            ps.setString(5, email);
            ps.setString(6, mobileid);
            ps.executeUpdate();
            ps.close();
            conn.close();
            close_con();
            User.getInstance().setUsername(username);
            User.getInstance().setPassword(password);
            User.getInstance().setMobile(mobile);
            User.getInstance().setEmail(email);
            User.getInstance().setMobileid(mobileid);
            User.getInstance().setId(id.toString());
            User.getInstance().setMoney(1000);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id.toString();

    }

    public void getData(Connection conn,String username)
    {

        try {
            st = conn.createStatement();
            String sql = "select * from users where username='" + username + "'";
            rs = st.executeQuery(sql);
            rs.next();
            User.getInstance().setUsername(rs.getString("username"));
            User.getInstance().setPassword(rs.getString("password"));
            User.getInstance().setMobile(rs.getString("mobile"));
            User.getInstance().setEmail(rs.getString("email"));
            User.getInstance().setMobileid(rs.getString("mobileid"));
            User.getInstance().setId(rs.getString("id"));
            User.getInstance().setMoney(rs.getInt("amount"));
            rs.close();
            st.close();
            conn.close();
            close_con();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDate(Connection conn)
    {
        String sql = "update users set password = ?, mobile = ?, email = ?, id = ?," +
                "mobileid = ?, amount = ? where username = ?";
        try {

            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1, User.getInstance().getPassword());
            ps.setString(2, User.getInstance().getMobile());
            ps.setString(3, User.getInstance().getEmail());
            ps.setString(4, User.getInstance().getId());
            ps.setString(5, User.getInstance().getMobileid());
            ps.setInt(6, User.getInstance().getMoney());
            ps.setString(7, User.getInstance().getUsername());
            ps.executeUpdate();
            ps.close();
            conn.close();
            close_con();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
