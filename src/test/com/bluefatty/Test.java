package com.bluefatty;

import com.bluefatty.utils.RsaUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Base64Utils;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-10-15
 */
public class Test {
    @Value("${rsa.private-key}")
    String PRIVATE_KEY;
    @Value("${rsa.public-key}")
    String PUBLIC_KEY;
    @org.junit.Test
    public void fun1(){
        String s = rsaPublicEncryptAndPrivateKeyDecrypt("hello world");
        System.out.println(s);
    }

    public String rsaPublicEncryptAndPrivateKeyDecrypt(String rawText) {

        byte[] privateKey = Base64Utils.decodeFromString(PRIVATE_KEY);
        byte[] publicKey = Base64Utils.decodeFromString(PUBLIC_KEY);
        try {
            System.out.println("原数据为:"+rawText);
            byte[] encryptByPublicKey = RsaUtils.encryptByPublicKey(rawText.getBytes(),
                    publicKey);
            String encryptByPublicKeyString = Base64Utils.encodeToString(encryptByPublicKey);
            System.out.println("公钥加密后的数据为:"+encryptByPublicKeyString);
            byte[] decryptByPrivateKey = RsaUtils.decryptByPrivateKey(
                    Base64Utils.decodeFromString(encryptByPublicKeyString),
                    privateKey);
            String result=new String(decryptByPrivateKey);
            System.out.println("私钥解密后的数据为:"+result);
            return result;
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

}
