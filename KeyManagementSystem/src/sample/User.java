/**
 * Created by Administrator on 2017/5/9.
 */
package sample;
public class User {
    private String username;
    private String password;
    private String mobile;
    private String email;
    private String mobileid;
    private String id;
    private int money;
    private static User instance;
    private User ()
    {
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileid()
    {
        return mobileid;
    }

    public void setMobileid(String mobileid) {
        this.mobileid = mobileid;
    }

    public void setId(String id) {this.id = id;}

    public String getId()
    {
        return id;
    }

    public void setMoney(int money) {this.money = money;}

    public int getMoney() {return  money;}


    public static User getInstance() {
        if (instance == null)
            instance = new User();
        return instance;
    }


}
