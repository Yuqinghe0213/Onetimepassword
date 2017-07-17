import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * This class is used to deal with the message send by desktop application.
 * There are 7 types of messgage and server need to response te these requests
 * and reply a result.
 */

public class ClientConnection extends Thread
{

	private Socket clientSocket;
	private BufferedReader reader;
	private BufferedWriter writer;
	private BlockingQueue<Message> messageQueue;
	private boolean flag = true;

	public ClientConnection(Socket clientSocket) {
		try {
			this.clientSocket = clientSocket;
			reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
			writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
			messageQueue = new LinkedBlockingQueue<Message>();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		try
		{
			ClientMessageReader messageReader = new ClientMessageReader(reader, messageQueue);
			messageReader.setName(this.getName() + "Reader");
			messageReader.start();

			while(true)
			{
				Message msg = messageQueue.take();
				System.out.println(Thread.currentThread().getName()+" deal with " + msg.getMessage());
				if(!msg.isFromClient() && msg.getMessage().equals("exit"))
				{
					break;
				}
				if(msg.isFromClient())
				{
					JSONParser parser = new JSONParser();
					JSONObject clientMsg = (JSONObject) parser.parse(msg.getMessage());
					JSONObject replyMsg;
					String type = (String) clientMsg.get("type");
					switch(type)
					{
						// check the username and password
						case "userpass":
						{
							String username = (String)clientMsg.get("username");
							String password = (String)clientMsg.get("password");
							System.out.println(username + "  " + password);
							DatabaseConnection connect = new DatabaseConnection();
							Connection conn = connect.DatabaseConnection();
							String result = connect.password_check(conn,
									username,password);
							if(result.equals("n") || result.equals("w")
									||result.equals("p")){
								replyMsg = JsonFormat.getInstance().userpassreturn(result);
							}
							else{
								DatabaseConnection connect2 = new DatabaseConnection();
								Connection conn2 = connect2.DatabaseConnection();
								String data = connect2.searchmobile(conn2, username);
								String[] connext = data.split("/");
								replyMsg = JsonFormat.getInstance()
										.userpassreturn(result, connext[0],connext[1]);
							}
							write(replyMsg.toJSONString());
							break;
						}

						// modify the basic information in the database
						case "changeinfo":
						{
							String username = (String)clientMsg.get("username");
							String mobile = (String)clientMsg.get("mobile");
							String email = (String)clientMsg.get("email");
							DatabaseConnection connect3 = new DatabaseConnection();
							Connection conn3 = connect3.DatabaseConnection();
							connect3.changeemail(conn3, username,mobile,email);
							replyMsg = JsonFormat.getInstance().chinforeturn(true);
							write(replyMsg.toJSONString());
							break;
						}

						// verify the one time password
						case "checkpassword":
						{
							String username = (String)clientMsg.get("username");
							String number = (String)clientMsg.get("challleng");
							String password = (String)clientMsg.get("password");
							DatabaseConnection connect4 = new DatabaseConnection();
							Connection conn4 = connect4.DatabaseConnection();
							String id = connect4.getId(conn4, username);
							try {
								String crpassword = GenPassword.getTOTP(number, id);

								if (password.equals(crpassword)) {
									replyMsg = JsonFormat.getInstance().checkpassreturn(true);
									write(replyMsg.toJSONString());
								} else {
									replyMsg = JsonFormat.getInstance().checkpassreturn(false);
									write(replyMsg.toJSONString());
								}
							}catch (Exception e) {
								e.printStackTrace();
							}

							break;
						}

						// register a new account
						case "register":
						{
							String username = (String)clientMsg.get("username");
							String pass = (String)clientMsg.get("password");
							String mobile = (String)clientMsg.get("mobile");
							String email = (String)clientMsg.get("email");
							String data = (String)clientMsg.get("mobileid");
							String password = decryption.saltedMD5(pass);
							DatabaseConnection connect5 = new DatabaseConnection();
							Connection conn5 = connect5.DatabaseConnection();
							Boolean result = connect5.usermanecheck(conn5,username);
							 if(result) {
								 replyMsg = JsonFormat.getInstance().registereturn(false);
								 write(replyMsg.toJSONString());
							 }
							 else {
								 String[] keymobilid = decryption.decrypt(data);
								 String symmekey = keymobilid[0];
								 String mobileid = keymobilid[1];
								 DatabaseConnection connect6 = new DatabaseConnection();
								 Connection conn6 = connect6.DatabaseConnection();
								 String id = connect6.add_data(conn6, username, password,
										 mobile, email,mobileid,symmekey);
								 String contect = id +"/"+mobileid;
								 String cryption = AESUtil.encrypt(contect,symmekey);
								 replyMsg = JsonFormat.getInstance().registereturn(true, cryption);
								 write(replyMsg.toJSONString());
							 }
							 break;
						}

						// lock the account. If a account is locked, this account can not login in anyway.
						case "lockaccount":
						{
							String username = (String)clientMsg.get("username");
							DatabaseConnection connect7 = new DatabaseConnection();
							Connection conn7 = connect7.DatabaseConnection();
							connect7.lockaccount(conn7, username);
							replyMsg = JsonFormat.getInstance().lockreturn(true);
							write(replyMsg.toJSONString());
							break;
						}

						// modify the static password in the database
						case "pwdchange":
						{
							String username = (String)clientMsg.get("username");
							String pass = (String)clientMsg.get("password");
							String password = decryption.saltedMD5(pass);
							DatabaseConnection connect8 = new DatabaseConnection();
							Connection conn8 = connect8.DatabaseConnection();
							connect8.updatepwd(conn8, username, password);
							replyMsg = JsonFormat.getInstance().pwdchangereturn(true);
							write(replyMsg.toJSONString());
							break;
						}

						// modify the IMMEI code in the database
						case "mbidchange":
						{
							String username = (String)clientMsg.get("username");
							String data = (String)clientMsg.get("mobileid");
							String[] keymobilid = decryption.decrypt(data);
							String symmekey = keymobilid[0];
							String mobileid = keymobilid[1];
							DatabaseConnection connect9 = new DatabaseConnection();
							Connection conn9 = connect9.DatabaseConnection();
							String id = connect9.updatemobileid(conn9, username, mobileid);
							String cryption = AESUtil.encrypt(id,symmekey);
							replyMsg = JsonFormat.getInstance().mbidchangereturn(true,cryption);
							write(replyMsg.toJSONString());
							break;
						}
					}
				}
				else
				{
					write(msg.getMessage());
				}

			}
			clientSocket.close();
			ServerState.getInstance().clientDisconnected(this);

		}

		catch (Exception e) 
		{
			e.printStackTrace();

		}
	}
	
	public BlockingQueue<Message> getMessageQueue() 
	{
		return messageQueue;
	}

	// write the reply message to desktop application
	public void write(String msg) 
	{
		try 
		{
			System.out.println(msg);
			writer.write(msg + "\n");
			writer.flush();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

}
