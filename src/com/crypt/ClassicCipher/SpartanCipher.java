package com.crypt.ClassicCipher;

import java.util.Scanner;

public class SpartanCipher {

    /**
     * 加密
     * @param plaintext 明文
     * @param key 密钥
     * @return 密文
     */
    private static String encrypt(String plaintext, int key) {
        // 去除明文中的空格
        plaintext = plaintext.replace(" ", "");
        int length = plaintext.length();
        int rowLength = (length + key - 1) / key;  // 计算行长度（向上取整）

        // 创建栅栏矩阵
        char[][] fence = new char[rowLength][key];
        for (int i = 0, index = 0; i < rowLength; i++) {
            for (int j = 0; j < key && index < length; j++) {
                fence[i][j] = plaintext.charAt(index++);
            }
        }

        // 按列读取生成密文
        StringBuilder encrypted = new StringBuilder();
        for (int j = 0; j < key; j++) {
            for (int i = 0; i < rowLength; i++) {
                encrypted.append(fence[i][j]);
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
    private static String decrypt(String ciphertext, int key) {
        int length = ciphertext.length();
        int rowLength = (length + key - 1) / key;  // 计算行长度（向上取整）

        // 创建栅栏矩阵并按列填充密文
        char[][] fence = new char[rowLength][key];
        for (int j = 0, index = 0; j < key; j++) {
            for (int i = 0; i < rowLength && index < length; i++) {
                fence[i][j] = ciphertext.charAt(index++);
            }
        }

        // 按行读取还原明文（逐行按顺序读取）
        StringBuilder decrypted = new StringBuilder();
        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < key; j++) {
                decrypted.append(fence[i][j]);
            }
        }
        return decrypted.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("斯巴达木卷加密（栅栏加密）");
        System.out.println("===================================");

        while (true) {
            try {
                System.out.print("请输入要加解密的字符串： ");
                String message = scanner.nextLine();

                System.out.print("请输入列数（密钥）：");
                int key = scanner.nextInt();
                scanner.nextLine();  // 消耗多余的换行符

                System.out.print("选择加密,解密还是退出?(E/D/Q)：");
                String option = scanner.nextLine().toUpperCase();

                if ("E".equals(option)) {
                    String encrypted = encrypt(message, key);
                    System.out.println("密文如下：" + encrypted);
                } else if ("D".equals(option)) {
                    String decrypted = decrypt(message, key);
                    System.out.println("明文如下：" + decrypted);
                } else if ("Q".equals(option)) {
                    System.out.println("程序已退出。");
                    break;
                } else {
                    System.out.println("请输入有效选项（E/D/Q）");
                }

            } catch (NumberFormatException e) {
                System.out.println("错误：请输入有效的整数密钥");
                scanner.nextLine();  // 清除无效输入
            }
        }
        scanner.close();
    }
}