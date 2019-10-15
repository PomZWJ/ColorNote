package com.bluefatty.utils;

import com.bluefatty.runner.RsaConfigRunner;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.util.Base64Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
/**
 * RSA工具类
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-10-15
 */
public class RsaUtils {
    /**
     * 非对称加密密钥算法
     */
    public static final String KEY_ALGORITHM_RSA = "RSA";

    private static String RSA_JAVA = "RSA/None/PKCS1Padding";

    /**
     * 公钥
     */
    private static final String RSA_PUBLIC_KEY = "RSAPublicKey";

    /**
     * 私钥
     */
    private static final String RSA_PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA密钥长度
     * 默认1024位，
     * 密钥长度必须是64的倍数，
     * 范围在512至65536位之间。
     */
    private static final int KEY_SIZE = 1024;

    static{
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

    /**
     * 加密方法
     * @param rawText
     * @return
     */
    public static String rsaPublicEncrypt(String rawText) {

        byte[] publicKey = Base64Utils.decodeFromString(RsaConfigRunner.PUBLIC_KEY);
        try {
            byte[] encryptByPublicKey = RsaUtils.encryptByPublicKey(rawText.getBytes(),
                    publicKey);
            String result = Base64Utils.encodeToString(encryptByPublicKey);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String rsaPrivateKeyDecrypt(String rawText) {

        byte[] privateKey = Base64Utils.decodeFromString(RsaConfigRunner.PRIVATE_KEY);
        try {
            byte[] decryptByPrivateKey = RsaUtils.decryptByPrivateKey(
                    Base64Utils.decodeFromString(rawText),
                    privateKey);
            String result=new String(decryptByPrivateKey);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }







    /**
     * 私钥解密
     *
     * @param data
     *            待解密数据
     * @param key
     *            私钥
     * @return byte[] 解密数据
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, byte[] key)
            throws Exception {

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_RSA);

        // 生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(RSA_JAVA);

        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        int blockSize = cipher.getBlockSize();
        if(blockSize>0){
            ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
            int j = 0;
            while (data.length - j * blockSize > 0) {
                bout.write(cipher.doFinal(data, j * blockSize, blockSize));
                j++;
            }
            return bout.toByteArray();
        }
        return cipher.doFinal(data);
    }

    /**
     * 公钥解密
     *
     * @param data
     *            待解密数据
     * @param key
     *            公钥
     * @return byte[] 解密数据
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] key)
            throws Exception {

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_RSA);

        // 生成公钥
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(RSA_JAVA);

        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    /**
     * 公钥加密
     *
     * @param data
     *            待加密数据
     * @param key
     *            公钥
     * @return byte[] 加密数据
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] key)
            throws Exception {

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_RSA);

        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(RSA_JAVA);

        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        int blockSize = cipher.getBlockSize();
        byte[] raw = getBytes(data, cipher, blockSize);
        if (raw != null) {
            return raw;
        }
        return cipher.doFinal(data);
    }

    private static byte[] getBytes(byte[] data, Cipher cipher, int blockSize) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        if(blockSize>0){
            int outputSize = cipher.getOutputSize(data.length);
            int leavedSize = data.length % blockSize;
            int blocksSize = leavedSize != 0 ? data.length / blockSize + 1
                    : data.length / blockSize;
            byte[] raw = new byte[outputSize * blocksSize];
            int i = 0,remainSize=0;
            while ((remainSize = data.length - i * blockSize) > 0) {
                int inputLen = remainSize > blockSize?blockSize:remainSize;
                cipher.doFinal(data, i * blockSize, inputLen, raw, i * outputSize);
                i++;
            }
            return raw;
        }
        return null;
    }

    /**
     * 私钥加密
     *
     * @param data
     *            待加密数据
     * @param key
     *            私钥
     * @return byte[] 加密数据
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] key)
            throws Exception {

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_RSA);

        // 生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(RSA_JAVA);

        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        int blockSize = cipher.getBlockSize();
        byte[] raw = getBytes(data, cipher, blockSize);
        return cipher.doFinal(data);
    }

    /**
     * 取得私钥
     *
     * @param keyMap
     *            密钥Map
     * @return key 私钥
     * @throws Exception
     */
    public static Key getPrivateKey(Map<String, Key> keyMap)
            throws Exception {
        return keyMap.get(RSA_PRIVATE_KEY);
    }

    /**
     * 取得私钥
     *
     * @param keyMap
     *            密钥Map
     * @return byte[] 私钥
     * @throws Exception
     */
    public static byte[] getPrivateKeyByte(Map<String, Key> keyMap)
            throws Exception {
        return keyMap.get(RSA_PRIVATE_KEY).getEncoded();
    }

    /**
     * 取得公钥
     *
     * @param keyMap
     *            密钥Map
     * @return key 公钥
     * @throws Exception
     */
    public static Key getPublicKey(Map<String, Key> keyMap)
            throws Exception {
        return keyMap.get(RSA_PUBLIC_KEY);
    }

    /**
     * 取得公钥
     *
     * @param keyMap
     *            密钥Map
     * @return byte[] 公钥
     * @throws Exception
     */
    public static byte[] getPublicKeyByte(Map<String, Key> keyMap)
            throws Exception {
        return keyMap.get(RSA_PUBLIC_KEY).getEncoded();
    }

    /**
     * 初始化密钥
     * @param seed 种子
     * @return Map 密钥Map
     * @throws Exception
     */
    public static Map<String,Key> initKey(byte[] seed)throws Exception{
        // 实例化密钥对生成器
        KeyPairGenerator keyPairGen = KeyPairGenerator
                .getInstance(KEY_ALGORITHM_RSA);

        // 初始化密钥对生成器
        keyPairGen.initialize(KEY_SIZE,    new SecureRandom(seed) );

        // 生成密钥对
        KeyPair keyPair = keyPairGen.generateKeyPair();

        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        // 封装密钥
        Map<String, Key> keyMap = new HashMap<String, Key>(2);

        keyMap.put(RSA_PUBLIC_KEY, publicKey);
        keyMap.put(RSA_PRIVATE_KEY, privateKey);

        return keyMap;
    }

    /**
     * 初始化密钥
     * @param seed 种子
     * @return Map 密钥Map
     * @throws Exception
     */
    public static Map<String,Key> initKey(String seed)throws Exception{
        return initKey(seed.getBytes());
    }

    /**
     * 初始化密钥
     *
     * @return Map 密钥Map
     * @throws Exception
     */
    public static Map<String, Key> initKey() throws Exception {
        return initKey(UUID.randomUUID().toString().getBytes());
    }

    public static PublicKey getPublicRSAKey(String key) throws Exception {
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(Base64.decode(key));
        KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM_RSA);
        return kf.generatePublic(x509);
    }

    public static PrivateKey getPrivateRSAKey(String key) throws Exception {
        PKCS8EncodedKeySpec pkgs8 = new PKCS8EncodedKeySpec(Base64.decode(key));
        KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM_RSA);
        return kf.generatePrivate(pkgs8);
    }
}
