package com.crypt.ClassicCipher;

import java.util.Scanner;

public class CaesarCipher {

    /**
     * 加密
     * @param plaintext 明文
     * @param key 密钥
     * @return 密文
     */
    public static String encrypt(String plaintext, int key) {
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i++) {
            char ch = plaintext.charAt(i);
            if (Character.isLowerCase(ch)) {
                encrypted.append((char) (((ch - 'a' + key) % 26) + 'a'));
            } else if (Character.isUpperCase(ch)) {
                encrypted.append((char) (((ch - 'A' + key) % 26) + 'A'));
            } else {
                encrypted.append(ch);
            }
        }
        return encrypted.toString();
    }

    /**
     * 解密
     * @param ciphertext 密文
     * @param key 密钥
     * @return 明文
     */
    public static String decrypt(String ciphertext, int key) {
        StringBuilder decrypted = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i++) {
            char ch = ciphertext.charAt(i);
            if (Character.isLowerCase(ch)) {
                decrypted.append((char) (((ch - 'a' - key + 26) % 26) + 'a'));
            } else if (Character.isUpperCase(ch)) {
                decrypted.append((char) (((ch - 'A' - key + 26) % 26) + 'A'));
            } else {
                decrypted.append(ch);
            }
        }
        return decrypted.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("凯撒加密");
        System.out.println("=====================================");
        while (true) {
            try {
                System.out.print("请输入要加密或解密的内容： ");
                String message = scanner.nextLine();
                System.out.print("请输入位移数：");
                int key = scanner.nextInt();
                scanner.nextLine(); // 消耗掉 nextInt() 后的换行符
                System.out.print("选择加密还是解密?(E/D/Q)");
                String option = scanner.nextLine().toUpperCase();
                if ("E".equals(option)) {
                    System.out.println("密文如下：" + encrypt(message, key));
                } else if ("D".equals(option)) {
                    System.out.println("明文如下：" + decrypt(message, key));
                } else if ("Q".equals(option)) {
                    break;
                } else {
                    System.out.println("请输入 E、D 或者 Q");
                }
            } catch (Exception e) {
                System.out.println("输入的位移数必须是一个有效的整数，请重新输入。");
                scanner.nextLine(); // 清除无效输入
            }
        }
        scanner.close();
    }
}    