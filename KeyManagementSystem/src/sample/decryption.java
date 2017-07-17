package sample;

import java.io.*;
import java.security.interfaces.RSAPrivateKey;

/**
 * Created by Administrator on 2017/5/15.
 */
public class decryption {

    public static String decrypt(String data){

        String context[] = data.split(" ");
        System.out.println(data);
        String result = null;
        String keyMI = context[0];
        String dataMI = context[1];

        try {
            RSAPrivateKey pk = (RSAPrivateKey) loadKey();
            String aes_key = RSAUtil.decryptByPrivateKey(keyMI,pk);
            String realData = AESUtil.decrypt(dataMI,aes_key);
            result = aes_key + "/"+realData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public static RSAPrivateKey loadKey()
    {
        RSAPrivateKey key = null;
        try {
            ObjectInputStream oos = new ObjectInputStream(new FileInputStream("privateKey.key"));
            key = (RSAPrivateKey) oos.readObject();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return  key;

    }

}
