package pl.radoslaw.kopec.BudgetSupportBackend.model;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.*;
import java.security.Key;
import org.apache.commons.codec.binary.Base64;

@Entity
@Table(name = "PASSWORD")
public class Password {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int id;
    private String password;

    public Password() {
    }

    public Password(String password) throws Exception {
        Key key = generateKey();
        this.password = encrypt(password,key);
    }

    public Password(int id, String password) throws Exception {
        this.id = id;
        Key key = generateKey();
        this.password = encrypt(password,key);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() throws Exception {
        Key key = generateKey();
        return decrypt(this.password,key);
    }

    public void setPassword(String password) throws Exception {
        Key key = generateKey();
        this.password = encrypt(password,key);
    }

    private  static Key generateKey() throws  Exception{
        Key key = new SecretKeySpec(value, "AES");
        return key;

    }
    public static String decrypt(String encryptedValue, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decodedBytes = new Base64().decode(encryptedValue.getBytes());

        byte[] val = cipher.doFinal(decodedBytes);
        return new String(val);
    }

    public static String encrypt(String valueToEnc, Key key) throws Exception {


        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptValue = cipher.doFinal(valueToEnc.getBytes());
        byte[] encryptedValue = new Base64().encode(encryptValue);

        return new String(encryptedValue);
    }
   // private static final String ALGORITHM = "AES";
    private static final byte[] value = "1234567891234567".getBytes();
}
