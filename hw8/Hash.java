import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class Hash {
    static String hash(String to_hash) {
        try {
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // the integer should be converted to a string that represents the number in decimal format
            // BigInteger to_hash_int = new BigInteger(to_hash);
            // BigDecimal to_hash_double = new BigDecimal(to_hash_int);
            // String to_hash_string = to_hash_double.toString();
 
            // digest() method is called to calculate message digest of an input digest() return array of byte
            byte[] messageDigest = md.digest(to_hash.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }    
}