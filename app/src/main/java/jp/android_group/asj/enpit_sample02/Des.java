package jp.android_group.asj.enpit_sample02;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Des {
    private SecretKey sk;
    byte[]kagi = "12345678".getBytes();

    void init() {
        try {
            DESKeySpec dks = new DESKeySpec(kagi);
            SecretKeyFactory sfk = SecretKeyFactory.getInstance("DES");
            sk = sfk.generateSecret(dks);


        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    byte[] encrypt(byte[]in) {
        try {
            Cipher c = Cipher.getInstance("DES");

            c.init(Cipher.ENCRYPT_MODE, sk);
            byte[] out = c.doFinal(in);

            System.out.println(out);
            return out;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    byte[] decrypt(byte[] in){

        try {
            Cipher c = Cipher.getInstance("DES");
            c.init(Cipher.DECRYPT_MODE, sk);

            byte[] out = c.doFinal(in);
            System.out.println(out);
            return out;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
