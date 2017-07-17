package sample;

import org.json.simple.JSONObject;

/**
 * Created by Administrator on 2017/5/20.
 * This class is define the formate of data that will be sent to server
 */
public class JsonFormat {

    private static JsonFormat instance;

    public JsonFormat()
    {

    }

    public JSONObject userpass(String username, String password)
    {
        JSONObject lockid = new JSONObject();
        lockid.put("type", "userpass");
        lockid.put("username", username);
        lockid.put("password", password);
        return lockid;
    }

    public JSONObject changeemail(String username,String mobile, String email)
    {
        JSONObject lockid = new JSONObject();
        lockid.put("type", "changeinfo");
        lockid.put("username", username);
        lockid.put("mobile", mobile);
        lockid.put("email", email);
        return lockid;
    }

    public JSONObject checkpassword(String username,String channumber, String password)
    {
        JSONObject lockid = new JSONObject();
        lockid.put("type", "checkpassword");
        lockid.put("username", username);
        lockid.put("challleng", channumber);
        lockid.put("password", password);
        return lockid;
    }

    public JSONObject register(String username,String password, String mobile,
                               String email, String mobileid)
    {
        JSONObject lockid = new JSONObject();
        lockid.put("type", "register");
        lockid.put("username", username);
        lockid.put("password", password);
        lockid.put("mobile", mobile);
        lockid.put("email", email);
        lockid.put("mobileid", mobileid);
        return lockid;
    }

    public JSONObject clockAccount(String username)
    {
        JSONObject lockid = new JSONObject();
        lockid.put("type", "lockaccount");
        lockid.put("username", username);
        lockid.put("state", 0);
        return lockid;
    }

    public JSONObject pwdchange(String username, String password)
    {
        JSONObject lockid = new JSONObject();
        lockid.put("type", "pwdchange");
        lockid.put("username", username);
        lockid.put("password", password);
        return lockid;
    }

    public JSONObject mobleidchange(String username, String mobileid)
    {
        JSONObject lockid = new JSONObject();
        lockid.put("type", "mbidchange");
        lockid.put("username", username);
        lockid.put("mobileid", mobileid);
        return lockid;
    }


    public static JsonFormat getInstance()
    {
        if(instance ==null)
            instance = new JsonFormat();
        return instance;
    }


}
