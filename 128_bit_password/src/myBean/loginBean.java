package myBean;

public class loginBean {

	private String UserName;
	private String password;
	private String code;
	private int totpOffset;
	
	public String getUserName(){
		return this.UserName;
	}
	public String getPassword(){
		return this.password;
	}
	public String getcode(){
		return this.code;
	}
	public int gettotpOffset(){
		return this.totpOffset;
	}
	public void setUserName(String u){
		this.UserName = u;
	}
	public void setPassword(String p){
		this.password = p;
	}
	public void setcode(String q){
		this.code = q;
	}
	public void settotpOffset(int r){
		this.totpOffset = r;
	}
	
}
