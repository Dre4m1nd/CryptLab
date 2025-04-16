package com.crypt.ClassicCipher;

import java.math.BigInteger;
import java.util.Scanner;

/*
    加密：
    ciphertext mod 26 = plaintext * key1 + key2 mod 26
    其中 ciphertext 和 plaintext 的值应为每个字母对应的数字
    解密：
    plaintext mod 26 = (ciphertext - key2) * (key1对模26的逆元) mod 26

 */

public class AffineCipher {

    /**
     * 加密
     * @param plaintext 明文
     * @param key1 密钥 1
     * @param key2 密钥 2
     * @return 密文
     */
    private static String encrypt(String plaintext, int key1, int key2) {
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i++) {
            char c = plaintext.charAt(i);
            if (Character.isLowerCase(c)) {
                int m = c - 'a';
                int encryptedValue = (key1 * m + key2) % 26;
                encrypted.append((char) (encryptedValue + 'a'));
            } else if (Character.isUpperCase(c)) {
                int m = c - 'A';
                int encryptedValue = (key1 * m + key2) % 26;
                encrypted.append((char) (encryptedValue + 'A'));
            } else {
                encrypted.append(c); // 非字母字符直接保留
            }
        }
        return encrypted.toString();
    }

    /**
     * 解密
     * @param ciphertext 密文
     * @param key1 密钥 1
     * @param key2 密钥 2
     * @return 明文
     */
    private static String decrypt(String ciphertext, int key1, int key2) {
        try {
            // 计算key1的模逆元（要求key1和26互质）
            // 使用大数库计算模逆元
            BigInteger a = BigInteger.valueOf(key1);
            BigInteger mod = BigInteger.valueOf(26);
            BigInteger inverse = a.modInverse(mod);

            StringBuilder decrypted = new StringBuilder();
            for (int i = 0; i < ciphertext.length(); i++) {
                char c = ciphertext.charAt(i);
                if (Character.isLowerCase(c)) {
                    int m = c - 'a';
                    int decryptedValue = (inverse.intValue() * (m - key2)) % 26; // 处理负数情况
                    decrypted.append((char) (decryptedValue + 'a'));
                } else if (Character.isUpperCase(c)) {
                    int m = c - 'A';
                    int decryptedValue = (inverse.intValue() * (m - key2)) % 26; // 处理负数情况
                    decrypted.append((char) (decryptedValue + 'A'));
                } else {
                    decrypted.append(c); // 非字母字符直接保留
                }
            }
            return decrypted.toString();
        } catch (Exception e) {
            throw new IllegalArgumentException("密钥1必须与26互质（即gcd(key1,26)=1）", e);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("仿射变换加解密算法");
        System.out.println("==================================================================================");

        while (true) {
            try {
                System.out.print("请输入要加密或解密的内容：");
                String message = scanner.nextLine();

                System.out.print("请输入密钥1（整数，需与26互质）：");
                int key1 = scanner.nextInt();
                scanner.nextLine(); // 消耗换行符

                System.out.print("请输入密钥2（整数）：");
                int key2 = scanner.nextInt();
                scanner.nextLine(); // 消耗换行符

                System.out.print("选择加密还是解密?(E/D/Q)：");
                String option = scanner.nextLine().toUpperCase();

                if ("E".equals(option)) {
                    String encrypted = encrypt(message, key1, key2);
                    System.out.println("密文如下：" + encrypted);
                } else if ("D".equals(option)) {
                    String decrypted = decrypt(message, key1, key2);
                    System.out.println("明文如下：" + decrypted);
                } else if ("Q".equals(option)) {
                    System.out.println("程序退出。");
                    break;
                } else {
                    System.out.println("请输入有效选项（E/D/Q）");
                }

            } catch (NumberFormatException e) {
                System.out.println("错误：请输入有效的整数密钥");
                scanner.nextLine();
            } catch (IllegalArgumentException e) {
                System.out.println("错误：" + e.getMessage());
            }
        }
        scanner.close();
    }
}