import org.json.simple.JSONObject;

/**
 * Created by Administrator on 2017/5/20.
 * This class define all type of messages that reply to desktop application
 */
public class JsonFormat {

    private static JsonFormat instance;

    public JsonFormat()
    {

    }

    public JSONObject userpassreturn(String result)
    {
        JSONObject lockid = new JSONObject();
        lockid.put("type", "userpass");
        lockid.put("result", result);
        return lockid;
    }

    public JSONObject userpassreturn(String result, String mobile, String email)
    {
        JSONObject lockid = new JSONObject();
        lockid.put("type", "userpass");
        lockid.put("result", result);
        lockid.put("mobile", mobile);
        lockid.put("email", email);
        return lockid;
    }

    public JSONObject chinforeturn(Boolean result)
    {
        JSONObject lockid = new JSONObject();
        lockid.put("type", "changeinfo");
        lockid.put("result", result);
        return lockid;
    }

    public JSONObject lockreturn(Boolean result)
    {
        JSONObject lockid = new JSONObject();
        lockid.put("type", "lockaccount");
        lockid.put("result", result);
        return lockid;
    }

    public JSONObject pwdchangereturn(Boolean result)
    {
        JSONObject lockid = new JSONObject();
        lockid.put("type", "changepwd");
        lockid.put("result", result);
        return lockid;
    }

    public JSONObject mbidchangereturn(Boolean result, String id)
    {
        JSONObject lockid = new JSONObject();
        lockid.put("type", "mbidchange");
        lockid.put("result", result);
        lockid.put("id", id);
        return lockid;
    }

    public JSONObject checkpassreturn(Boolean result)
    {
        JSONObject lockid = new JSONObject();
        lockid.put("type", "checkpass");
        lockid.put("result", result);
        return lockid;
    }

    public JSONObject registereturn(Boolean result)
    {
        JSONObject lockid = new JSONObject();
        lockid.put("type", "registeresult");
        lockid.put("result", result);
        return lockid;
    }

    public JSONObject registereturn(Boolean result, String id)
    {
        JSONObject lockid = new JSONObject();
        lockid.put("type", "registeresult");
        lockid.put("result", result);
        lockid.put("id", id);
        return lockid;
    }



    public static JsonFormat getInstance()
    {
        if(instance ==null)
            instance = new JsonFormat();
        return instance;
    }


}
