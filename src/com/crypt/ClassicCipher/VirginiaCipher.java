package com.crypt.ClassicCipher;

import java.util.Scanner;

public class VirginiaCipher {

    /**
     * 生成对照表格
     * @return 对照表格
     */
    private static char[][] generateTables() {
        char[][] table = new char[26][26];
        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < 26; j++) {
                table[i][j] = (char) ((i + j) % 26 + 'A');
            }
        }
        return table;
    }

    /**
     * 获取这个字母所对应的行或列(即所对应的数字)
     * @param c 字母
     * @return 这个字母所对应的行或列(即所对应的数字)
     */
    private static int getRowOrCol(char c) {
        return c - 'A';
    }

    /**
     * 加密
     * @param plaintext 明文
     * @param key 密钥
     * @return 密文
     */
    private static String encrypt(String plaintext, String key) {
        // 处理明文与密钥等长
        StringBuilder preparedKey = new StringBuilder();
        if (plaintext.length() <= key.length()) {
            preparedKey.append(key, 0, plaintext.length());
        } else {
            int repeatTimes = plaintext.length() / key.length() + 1;
            for (int i = 0; i < repeatTimes; i++) {
                preparedKey.append(key);
            }
            preparedKey.setLength(plaintext.length());
        }

        // 生成表格进行代换
        char[][] tables = generateTables();
        StringBuilder cipher = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i++) {
            if (!Character.isLetter(plaintext.charAt(i))) {
                continue;
            }
            int row = getRowOrCol(plaintext.charAt(i));
            int col = getRowOrCol(preparedKey.charAt(i));
            cipher.append(tables[row][col]);
        }
        return cipher.toString();
    }

    /**
     * 解密
     * @param ciphertext 密文
     * @param key 密钥
     * @return 明文
     */
    private static String decrypt(String ciphertext, String key) {
        StringBuilder preparedKey = new StringBuilder();
        if (ciphertext.length() <= key.length()) {
            preparedKey.append(key, 0, ciphertext.length());
        } else {
            int repeatTimes = ciphertext.length() / key.length() + 1;
            for (int i = 0; i < repeatTimes; i++) {
                preparedKey.append(key);
            }
            preparedKey.setLength(ciphertext.length());
        }
        char[][] tables = generateTables();
        StringBuilder decrypted = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i++) {
            int col = getRowOrCol(preparedKey.charAt(i));
            char c = ciphertext.charAt(i);
            for (int j = 0; j < 26; j++) {
                if (c == tables[j][col]) {
                    decrypted.append((char) (j + 'A'));
                    break;
                }
            }
        }
        return decrypted.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("维吉尼亚密码加密");
        System.out.println("===================================");
        while (true) {
            try {
                // 获取用户输入的明文
                System.out.print("请输入要加解密的字符串： ");
                String message = scanner.nextLine().toUpperCase().replace(" ", "");
                // 获取用户输入的密钥
                System.out.print("请输入密钥：");
                String key = scanner.nextLine().toUpperCase().replace(" ", "");
                if (!key.matches("[A-Z]+")) {
                    throw new IllegalArgumentException("密钥不能包含特殊符号");
                }
                System.out.print("选择加密,解密还是退出?(E/D/Q)");
                String option = scanner.nextLine().toUpperCase();
                if ("E".equals(option)) {
                    String encryptedMessage = encrypt(message, key);
                    System.out.println("密文如下：" + encryptedMessage);
                } else if ("D".equals(option)) {
                    String decryptedMessage = decrypt(message, key);
                    System.out.println("明文如下：" + decryptedMessage);
                } else if ("Q".equals(option)) {
                    System.out.println("程序已退出。");
                    break;
                } else {
                    System.out.println("请输入 E、D 或者 Q");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        scanner.close();
    }
}    