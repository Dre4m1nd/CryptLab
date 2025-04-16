package com.crypt.DESCipher;

import java.util.Scanner;

public class DESCipher {
    // 初始置换表
    private static final int[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };
    // 逆初始置换表
    private static final int[] FP = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
    };

    // 置换选择1表（PC - 1）
    private static final int[] PC1 = {
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
    };

    // 置换选择2表（PC - 2）
    private static final int[] PC2 = {
            14, 17, 11, 24, 1, 5, 3, 28,
            15, 6, 21, 10, 23, 19, 12, 4,
            26, 8, 16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55, 30, 40,
            51, 45, 33, 48, 44, 49, 39, 56,
            34, 53, 46, 42, 50, 36, 29, 32
    };

    // 扩展置换表（E）
    private static final int[] E = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };

    // 左移表
    private static final int[] shiftTable = {
            1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
    };

    // S盒
    private static final int[][][] S = {
            // S0
            {
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
            },
            // S1
            {
                    {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
            },
            // S2
            {
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
            },
            // S3
            {
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
            },
            // S4
            {
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
            },
            // S5
            {
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
            },
            // S6
            {
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
            },
            // S7
            {
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
            }
    };

    // P盒
    private static final int[] P = {
            16, 7, 20, 21, 29, 12, 28, 17,
            1, 15, 23, 26, 5, 18, 31, 10,
            2, 8, 24, 14, 32, 27, 3, 9,
            19, 13, 30, 6, 22, 11, 4, 25
    };

    /**
     * IP置换函数
     * @param msg_64bit 64位消息
     * @return 置换后的64位消息 permuted_msg
     */
    private static String IP(String msg_64bit) {
        StringBuilder permuted_msg = new StringBuilder();
        for (int i : IP) {
            permuted_msg.append(msg_64bit.charAt(i - 1));
        }
        return permuted_msg.toString();
    }
    /**
     * IP逆置换函数
     * @param msg_64bit 64位消息
     * @return 置换后的64位消息 permuted_msg
     */
    private static String inv_IP(String msg_64bit) {
        StringBuilder permuted_msg = new StringBuilder();
        for (int i : FP) {
            permuted_msg.append(msg_64bit.charAt(i - 1));
        }
        return permuted_msg.toString();
    }

    /**
     * f函数
     * @param R_32bit 32位输入消息
     * @param RK      48位轮密钥rki
     * @return 经过 f函数 的32位消息 R
     */
    private static String f(String R_32bit, String[] RK, int i) {
        String R_48bit = E(R_32bit);
        StringBuilder R = new StringBuilder();
        for (int j = 0; j < R_48bit.length(); j++) {
            R.append(Character.getNumericValue(R_48bit.charAt(j)) ^ Character.getNumericValue(RK[i].charAt(j)));
        }
        String[] units = S(R.toString());
        StringBuilder R_S = new StringBuilder();
        for (int j = 0; j < units.length; j++) {
            R_S.append(units[j]);
        }
        return P(R_S.toString());
    }

    /**
     * 扩展置换
     * @param msg_32bit 32位消息
     * @return 拓展置换后的48位消息
     */
    private static String E(String msg_32bit) {
        StringBuilder permuted_msg = new StringBuilder();
        for (int i : E) {
            permuted_msg.append(msg_32bit.charAt(i - 1));
        }
        return permuted_msg.toString();
    }

    /**
     * 非线性代换
     * @param msg_48bit 48位消息
     * @return 非线性代换后的32位消息
     */
    private static String[] S(String msg_48bit) {
        String[] unit = new String[8];
        for (int i = 0; i < unit.length; i++) {
            String current_str = msg_48bit.substring(i * 6, i * 6 + 6);
            int row = Integer.parseInt("" + current_str.charAt(0) + current_str.charAt(5), 2);
            int col = Integer.parseInt(current_str.substring(1, 5), 2);
            unit[i] = String.format("%4s", Integer.toBinaryString(S[i][row][col])).replace(" ", "0");
        }
        return unit;
    }

    /**
     * P置换，即线性置换
     * @param msg_32bit 32位2进制消息
     * @return 置换后的32位2进制消息
     */
    private static String P(String msg_32bit) {
        StringBuilder permuted_msg = new StringBuilder();
        for (int i : P) {
            permuted_msg.append(msg_32bit.charAt(i - 1));
        }
        return permuted_msg.toString();
    }


    /**
     * 密钥拓展算法
     * @param key_64bit 初始密钥
     * @return 16组48bit的密钥数组 rk
     */
    private static String[] des_key_schedule(String key_64bit) {
        StringBuilder C = new StringBuilder();
        StringBuilder D = new StringBuilder();
        for (int i = 0; i < 28; i++) {
            C.append(key_64bit.charAt(PC1[i] - 1));
        }
        for (int i = 28; i < 56; i++) {
            D.append(key_64bit.charAt(PC1[i] - 1));
        }
        String C0 = C.toString();
        String D0 = D.toString();

        String[][] CD = new String[17][2];

        CD[0][0] = C0;
        CD[0][1] = D0;

        for (int i = 1; i <= 16; i++) {
            CD[i][0] = shiftString(CD[i - 1][0], shiftTable[i - 1]);
            CD[i][1] = shiftString(CD[i - 1][1], shiftTable[i - 1]);
        }

        String[] RK = new String[16];
        for (int i = 0; i < 16; i++) {
            StringBuilder CDi = new StringBuilder(CD[i + 1][0] + CD[i + 1][1]);
            StringBuilder RKi = new StringBuilder();
            for (int j : PC2) {
                RKi.append(CDi.charAt(j - 1));
            }
            RK[i] = RKi.toString();
        }
        return RK;
    }

    /**
     * 循环左移
     * @param s 字符串
     * @param i 左移次数
     * @return 左移后的字符串
     */
    private static String shiftString(String s, int i) {
        String s1 = s.substring(0, i);
        String s2 = s.substring(i);
        return s2 + s1;
    }

    /**
     * 16位16进制转64位2进制
     * @param msg_hex 16位16进制
     * @return 64位2进制
     */
    private static String HexToBinary(String msg_hex) {
        StringBuilder msg_bin = new StringBuilder();
        for (int i = 0; i < msg_hex.length(); i++) {
            String char_bin = Integer.toBinaryString(Character.digit(msg_hex.charAt(i), 16));
            while (char_bin.length() < 4) {
                char_bin = "0" + char_bin;
            }
            msg_bin.append(char_bin);
        }
        return msg_bin.toString();
    }

    /**
     * 64位2进制转16位16进制
     * @param msg_bin 64位2进制
     * @return 16位16进制
     */
    private static String BinaryToHex(String msg_bin) {
        StringBuilder msg_hex = new StringBuilder();
        for (int i = 0; i < msg_bin.length() / 4; i++) {
            String char_hex = Integer.toHexString(Integer.parseInt(msg_bin.substring(i * 4, i * 4 + 4), 2));
            msg_hex.append(char_hex);
        }
        return msg_hex.toString();
    }

    /**
     * 加密
     * @param plaintext 16位16进制明文
     * @param key       16位16进制密钥
     * @return 密文
     */
    private static String des_encrypt(String plaintext, String key) {
        // 转换为64位2进制
        String plainText_bin = HexToBinary(plaintext);
        String plainText_IP = IP(plainText_bin);
        String left0 = plainText_IP.substring(0, 32);
        String right0 = plainText_IP.substring(32, 64);
        String key_bin = HexToBinary(key);
        String[] RK = des_key_schedule(key_bin);
        String[] Li = new String[17];
        String[] Ri = new String[17];
        Li[0] = left0;
        Ri[0] = right0;
        for (int i = 1; i <= RK.length; i++) {
            Li[i] = Ri[i - 1];
            String Ri_f = f(Ri[i - 1], RK, i - 1);
            StringBuilder Ri_i = new StringBuilder();
            for (int j = 0; j < Li[i].length(); j++) {
                Ri_i.append(Character.getNumericValue(Li[i - 1].charAt(j)) ^ Character.getNumericValue(Ri_f.charAt(j)));
            }
            Ri[i] = Ri_i.toString();
        }
        String LR16 = Ri[16] + Li[16];
        return BinaryToHex(inv_IP(LR16));
    }

    /**
     * 解密
     * @param cipher 16位16进制密文
     * @param key 16位16进制密钥
     * @return 明文
     */
    private static String des_decrypt(String cipher, String key) {

        String key_bin = HexToBinary(key);
        String[] RK = des_key_schedule(key_bin);

        String cipherText = HexToBinary(cipher);
        String cipherText_IP = IP(cipherText);
        String left0 = cipherText_IP.substring(0, 32);
        String right0 = cipherText_IP.substring(32, 64);

        String[] Li = new String[17];
        String[] Ri = new String[17];
        Li[0] = left0;
        Ri[0] = right0;
        for (int i = 1; i <= RK.length; i++) {
            Li[i] = Ri[i - 1];
            String Ri_f = f(Ri[i - 1], RK, 16 - i);
            StringBuilder Ri_i = new StringBuilder();
            for (int j = 0; j < Li[i].length(); j++) {
                Ri_i.append(Character.getNumericValue(Li[i - 1].charAt(j)) ^ Character.getNumericValue(Ri_f.charAt(j)));
            }
            Ri[i] = Ri_i.toString();
        }
        String LR16 = Ri[16] + Li[16];
        return BinaryToHex(inv_IP(LR16));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("DES加密(仅支持16位16进制)");
            System.out.println("==================================================");
            System.out.print("选择加密/解密/退出(E/D/Q)：");
            String mode = sc.nextLine().toUpperCase();
            if (mode.equals("Q")) {
                break;
            }
            try {
                if (mode.equals("E")) {
                    System.out.print("输入明文：");
                    String plaintext = sc.nextLine();
                    if (!(plaintext.matches("^[0-9A-Fa-f]+$") && plaintext.length() == 16)) {
                        throw new IllegalArgumentException("明文不是16进制或不是16位");
                    }
                    System.out.print("输入密钥：");
                    String key = sc.nextLine();
                    if (!(key.matches("^[0-9A-Fa-f]+$") && key.length() == 16)) {
                        throw new IllegalArgumentException("密钥不是16进制或不是16位");
                    }
                    System.out.println("密文为：" + des_encrypt(plaintext, key));
                } else if (mode.equals("D")) {
                    System.out.print("输入密文：");
                    String cipher = sc.nextLine();
                    if (!(cipher.matches("^[0-9A-Fa-f]+$") && cipher.length() == 16)) {
                        throw new IllegalArgumentException("密文不是16进制或不是16位");
                    }
                    System.out.print("输入密钥：");
                    String key = sc.nextLine();
                    if (!(key.matches("^[0-9A-Fa-f]+$") && key.length() == 16)) {
                        throw new IllegalArgumentException("密钥不是16进制或不是16位");
                    }
                    System.out.println("明文为：" + des_decrypt(cipher, key));
                } else {
                    throw new IllegalArgumentException("请输入E/D/Q");
                }
            } catch (Exception e) {
                System.out.println("错误：" + e.getMessage());
            }
        }
        sc.close();
        System.out.println("程序已退出");
    }
}