package com.crypt.ClassicCipher;

import java.util.Scanner;

public class PlayfairCipher {
    private static final String ALPHABET = "ABCDEFGHIKLMNOPQRSTUVWXYZ";

    /**
     * 寻找字母在矩阵中的位置
     * @param c 字母
     * @param matrix 矩阵
     * @return 行和列
     */
    private static int[] findPosition(char c, char[][] matrix) {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (matrix[row][col] == c) {
                    return new int[]{row, col};
                }
            }
        }
        return new int[]{-1, -1};
    }

    /**
     * 生成矩阵
     * @param key 密钥
     * @return 矩阵
     */
    private static char[][] generateMatrix(String key) {
        // 将 J 换成 I
        key = key.replace('J', 'I');
        StringBuilder keyChars = new StringBuilder();
        for (char c : key.toCharArray()) {
            // 如果密钥字符串中没有此字母就添加
            if (keyChars.indexOf(String.valueOf(c)) == -1) {
                keyChars.append(c);
            }
        }
        for (char c : ALPHABET.toCharArray()) {
            // 如果密钥字符串中没有此字母就添加
            if (keyChars.indexOf(String.valueOf(c)) == -1) {
                keyChars.append(c);
            }
        }
        char[][] matrix = new char[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j] = keyChars.charAt(i * 5 + j);
            }
        }
        return matrix;
    }

    /**
     * 处理明文
     * @param plaintext 明文
     * @return 处理后的明文
     */
    private static String[] preparePlain(String plaintext) {
        // 将明文中的 J 换成 I
        plaintext = plaintext.replace('J', 'I');
        StringBuilder preparedPlain = new StringBuilder();
        int index = 0;
        while (index < plaintext.length()) {
            // 如果是最后一个字符或是两个相同的字符，就加 X
            if (index + 1 == plaintext.length() || plaintext.charAt(index) == plaintext.charAt(index + 1)) {
                preparedPlain.append(plaintext.charAt(index)).append('X');
                index++;
            } else {
                preparedPlain.append(plaintext, index, index + 2);
                index += 2;
            }
        }
        String[] pairs = new String[preparedPlain.length() / 2];
        for (int i = 0; i < pairs.length; i++) {
            pairs[i] = preparedPlain.substring(i * 2, i * 2 + 2);
        }
        return pairs;
    }

    /**
     * 加密
     * @param plaintext 明文
     * @param key 密钥
     * @return 密文
     */
    private static String encrypt(String plaintext, String key) {
        char[][] matrix = generateMatrix(key);
        String[] preparedPlain = preparePlain(plaintext);
        StringBuilder encrypted = new StringBuilder();
        for (String pair : preparedPlain) {
            char char1 = pair.charAt(0);
            char char2 = pair.charAt(1);
            int[] pos1 = findPosition(char1, matrix);
            int[] pos2 = findPosition(char2, matrix);
            int row1 = pos1[0], col1 = pos1[1];
            int row2 = pos2[0], col2 = pos2[1];
            // 同行右移
            if (row1 == row2) {
                encrypted.append(matrix[row1][(col1 + 1) % 5]);
                encrypted.append(matrix[row2][(col2 + 1) % 5]);
            }
            // 同列下移
            else if (col1 == col2) {
                encrypted.append(matrix[(row1 + 1) % 5][col1]);
                encrypted.append(matrix[(row2 + 1) % 5][col2]);
            }
            // 不同行不同列取另外两个顶点
            else {
                encrypted.append(matrix[row1][col2]);
                encrypted.append(matrix[row2][col1]);
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
    private static String decrypt(String ciphertext, String key) {
        char[][] matrix = generateMatrix(key);
        String[] preparedPlain = preparePlain(ciphertext);
        StringBuilder decrypted = new StringBuilder();
        for (String pair : preparedPlain) {
            char char1 = pair.charAt(0);
            char char2 = pair.charAt(1);
            int[] pos1 = findPosition(char1, matrix);
            int[] pos2 = findPosition(char2, matrix);
            int row1 = pos1[0], col1 = pos1[1];
            int row2 = pos2[0], col2 = pos2[1];
            if (row1 == row2) {
                decrypted.append(matrix[row1][(col1 - 1 + 5) % 5]);
                decrypted.append(matrix[row2][(col2 - 1 + 5) % 5]);
            } else if (col1 == col2) {
                decrypted.append(matrix[(row1 - 1 + 5) % 5][col1]);
                decrypted.append(matrix[(row2 - 1 + 5) % 5][col2]);
            } else {
                decrypted.append(matrix[row1][col2]);
                decrypted.append(matrix[row2][col1]);
            }
        }
        return decrypted.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("playfair加解密算法");
        System.out.println("======================================================================");
        while (true) {
            try {
                System.out.print("请输入要加解密的纯字母字符串： ");
                String message = scanner.nextLine().toUpperCase().replace(" ", "");
                System.out.print("请输入密钥：");
                String key = scanner.nextLine().toUpperCase().replace(" ", "");
                if (!message.matches("[A-Z]+") || !key.matches("[A-Z]+")) {
                    throw new IllegalArgumentException("输入的密文不能包含特殊符号，请重新输入。");
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
                    System.out.println("请输入E、D或者Q");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        scanner.close();
    }
}    