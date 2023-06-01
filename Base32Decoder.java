/**
 * <p>Provides Base32 encoding and decoding as defined by <a href="http://www.ietf.org/rfc/rfc4648.txt">RFC 4648</a>.
 * However it uses a custom alphabet first coined by Douglas Crockford. Only addition to the alphabet is that 'u' and
 * 'U' characters decode as if they were 'V' to improve mistakes by human input.<p/>
 * <p>
 * This class operates directly on byte streams, and not character streams.
 * </p>
 *
 * @version $Id: Base32.java 1382498 2012-09-09 13:41:55Z sebb $
 * @see <a href="http://www.ietf.org/rfc/rfc4648.txt">RFC 4648</a>
 * @see <a href="http://www.crockford.com/wrmg/base32.html">Douglas Crockford's Base32 Encoding</a>
 * @since 1.5
 */
import java.util.HashMap;
import java.util.Map;


public class Base32Decoder {
    
    private static final String BASE32_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    
    private static Map<Character, Integer> createDecodeTable() {
        Map<Character, Integer> decodeTable = new HashMap<>();
        for (int i = 0; i < BASE32_CHARS.length(); i++) {
            char c = BASE32_CHARS.charAt(i);
            decodeTable.put(c, i);
        }
        return decodeTable;
    }
    
    public static byte[] bbase32Decode(String data) {
        // Remove padding characters from the input
        data = data.replaceAll("=", "");
        
        // Create the decode table
        Map<Character, Integer> decodeTable = createDecodeTable();
        
        int bitLength = 0;
        int currentByte = 0;
        byte[] decodedData = new byte[data.length() * 5 / 8];
        int decodedIndex = 0;
        
        // Process each character in the input
        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            
            // Skip characters that are not in the Base32 character set
            if (!decodeTable.containsKey(c)) {
                continue;
            }
            
            // Get the 5-bit value for the character
            int value = decodeTable.get(c);
            
            // Shift the current byte to the left by 5 bits and add the new value
            currentByte = (currentByte << 5) | value;
            
            // Update the bit length
            bitLength += 5;
            
            // If we have a complete byte, append it to the decoded data
            if (bitLength >= 8) {
                bitLength -= 8;
                decodedData[decodedIndex++] = (byte) ((currentByte >> bitLength) & 0xFF);
            }
        }
        
        // Return the decoded data
        return decodedData;
    }
    
    public static String base32Decode(String data) {
        byte[] decodedData = bbase32Decode(data);
        String decodedString = new String(decodedData);
        return decodedString;

    }
}
