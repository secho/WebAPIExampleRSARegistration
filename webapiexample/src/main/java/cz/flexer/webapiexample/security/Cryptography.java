package cz.flexer.webapiexample.security;

import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import cz.flexer.webapiexample.model.EncryptedMessage;

/**
 * Created by secho on 14.12.14.
 */
public class Cryptography {

    public static String tempSek = "null";
    private static final String RSA_ALGO = "RSA/NONE/OAEPWithSHA1AndMGF1Padding";

    public static String getAESKeyBase64() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // for example
        SecretKey secretKey = keyGen.generateKey();
        return Base64.encodeToString(secretKey.getEncoded(), Base64.NO_WRAP);
    }

    public static String aesEncrypt(String data, String aesKey) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        byte[] keyValue = Base64.decode(aesKey, Base64.NO_WRAP);

        SecretKey key = new SecretKeySpec(keyValue, "AES");
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes(StandardCharsets.UTF_8));

        return Base64.encodeToString(encVal, Base64.NO_WRAP);
    }


    public static String aesDecrypt(String data, String aesKey) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        byte[] keyValue = Base64.decode(aesKey, Base64.NO_WRAP);

        SecretKey key = new SecretKeySpec(keyValue, "AES");
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] dataB = Base64.decode(data, Base64.NO_WRAP);
        byte[] decVal = c.doFinal(dataB);

        return new String(decVal);
    }


//    RSA Encryption

    public static String rsaEncrypt(String data, String publicKey) {

        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGO, "BC");
            cipher.init(Cipher.ENCRYPT_MODE, getPublic(publicKey));
            byte[] encVal = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeToString(encVal, Base64.NO_WRAP);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    public static PublicKey getPublic(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {

        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pub = null;

        if (publicKey != null) {
            EncodedKeySpec publicSpec = new X509EncodedKeySpec(Base64.decode(publicKey, Base64.NO_WRAP));
            pub = keyFactory.generatePublic(publicSpec);
        }

        return pub;
    }


    public static EncryptedMessage encryptMessage(String publicKey, String data) throws IllegalBlockSizeException,
            InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {

        tempSek = getAESKeyBase64();
        Log.v("(1) GENERATED AES KEY USED FOR ENC",tempSek);

        EncryptedMessage encryptedMessage = new EncryptedMessage();
        encryptedMessage.setSession(rsaEncrypt(tempSek, publicKey));
        encryptedMessage.setData(aesEncrypt(data, tempSek));

        return encryptedMessage;
    }
}
