package ud.group9.moviemanager.utils;

import org.apache.commons.codec.binary.Base32;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @brief Hash class
 * 
 * Class that helps with the Hasing process
 *
 */
public class Hash {
    private static String method = "SHA-256";

    /**
     * @brief Texto to Hash
     * 
     * Tranasform a plain text to Hash
     * @param hash Plain text
     * @return String Hashed text
     */
    public static String encodeHash(byte []hash) {
        Base32 encoder = new Base32();
        String encoded = encoder.encodeAsString(hash);
        // the string now looks like ABAB...ABAB====
        // remove "====" from the end of the string to avoid errors when sending text
        // as REST query parameter
        return encoded.substring(0, encoded.length() - 4);
    }

    public static byte[] sha256Hash(String raw) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(method);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return digest.digest(raw.getBytes(StandardCharsets.UTF_8));
    }
}
