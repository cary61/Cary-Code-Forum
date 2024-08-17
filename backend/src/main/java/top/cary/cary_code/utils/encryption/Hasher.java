package top.cary.cary_code.utils.encryption;

import org.springframework.stereotype.Component;
import top.cary.cary_code.entity.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class Hasher {
    MessageDigest messageDigest;

    public Hasher() throws NoSuchAlgorithmException {
        messageDigest = MessageDigest.getInstance("MD5");
    }

    public String hash(String str) {
        messageDigest.update(str.getBytes());
        StringBuilder sb = new StringBuilder();
        byte[] h = messageDigest.digest();
        for (byte b : h) {
            int digit = b;
            if (digit < 0) {
                digit += 128;
            }
            sb.append(Integer.toHexString(digit));
        }
        return sb.toString();
    }

    public String hashFileName() {
        return Long.toHexString(System.currentTimeMillis());
    }

    public String getToken(User user) {
        return user.getPasswordHash();  // TO-DO
    }
}
