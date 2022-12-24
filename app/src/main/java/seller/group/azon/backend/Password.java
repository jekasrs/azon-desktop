package seller.group.azon.backend;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Password {
    public static String doHash(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            messageDigest.update(password.getBytes(StandardCharsets.UTF_8));

            byte[] res = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b: res)
                sb.append(String.format("%02x", b));

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
}
