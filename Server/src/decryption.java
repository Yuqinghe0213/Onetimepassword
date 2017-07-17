import java.io.*;
import java.security.MessageDigest;
import java.security.interfaces.RSAPrivateKey;
import java.util.Random;

/**
 * Created by Administrator on 2017/5/15.
 * This class implement some functions related to cryptography.
 * inuding salted MD5, RSA and AES
 */
public class decryption {

    public static String[] decrypt(String data){

        String context[] = data.split(" ");
        System.out.println(data);
        String[] result = new String[2];
        String keyMI = context[0];
        String dataMI = context[1];

        try {
            RSAPrivateKey pk = (RSAPrivateKey) loadKey();
            String aes_key = RSAUtil.decryptByPrivateKey(keyMI,pk);
            String realData = AESUtil.decrypt(dataMI,aes_key);
            result[0] = aes_key;
            result[1] = realData;
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

    public static String saltedMD5(String password) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
        String salt = sb.toString();
        password = md5Hex(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }

    public static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            String result = byte2HexStr(bs);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean verifyPass(String password, String md5) {
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
    public static String byte2HexStr(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
            // if (n<b.length-1) hs=hs+":";
        }
        return hs.toUpperCase();
    }


}
