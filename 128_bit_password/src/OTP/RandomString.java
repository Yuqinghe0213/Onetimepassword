package OTP;

import java.security.SecureRandom;

import java.util.Random;
import java.util.UUID;

public class RandomString {
	@SuppressWarnings("unused")
	private static SecureRandom random1 = new SecureRandom();

	public static String getRandomString(int length) { //length表示生成字符串的长度

		String base = "abcdef0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			if (i != 0 && i % 4 == 0) {
				sb.append("");
			}
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static String uuid2() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static void main(String[] args) {
		System.out.println(RandomString.uuid2());
	}
}
