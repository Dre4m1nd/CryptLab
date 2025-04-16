package com.crypt.ClassicCipher;

import java.util.Scanner;

public class HillCipher {


    /**
     * 将大写字母转换为0 - 25的数字
     * @param c 大写字母
     * @return 0 - 25的数字
     */
    private static int getNum(char c) {
        return c - 'A';
    }

    // 将数字转换为大写字母（自动处理模26）

    /**
     * 将数字转换为大写字母（自动处理模26）
     * @param num 数字
     * @return 对应的大写字母
     */
    private static char getChar(int num) {
        return (char) ((num % 26 + 26) % 26 + 'A');
    }


    /**
     * 生成密钥矩阵
     * @param keyStr 密钥
     * @param n 2阶或3阶
     * @return 密钥矩阵
     */
    private static int[][] generateKeyMatrix(String keyStr, int n) {
        String[] parts = keyStr.split(" ");
        if (parts.length != n * n) {
            throw new IllegalArgumentException("需要输入" + n * n + "个数字");
        }
        int[][] key = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                key[i][j] = Integer.parseInt(parts[i * n + j]);
            }
        }
        return key;
    }

    /**
     * 生成明文数字矩阵（自动填充'A'）
     * @param text 明文
     * @param n 2行或3行
     * @return 明文数字矩阵
     */
    private static int[][] generatePlaintextMatrix(String text, int n) {
        String cleaned = text.toUpperCase().replace(" ", "");
        int padLength = (n - cleaned.length() % n) % n;
        StringBuilder padded = new StringBuilder(cleaned);
        for (int i = 0; i < padLength; i++) {
            padded.append('X');
        }
        int[][] matrix = new int[n][padded.length() / n];
        for (int i = 0; i < padded.length() / n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[j][i] = getNum(padded.charAt(i * n + j));
            }
        }
        return matrix;
    }

    /**
     * 矩阵乘法
     * @param mat1 矩阵1
     * @param mat2 矩阵2
     * @return 结果矩阵
     */
    private static int[][] matrixMultiply(int[][] mat1, int[][] mat2) {
        int m = mat1.length;
        int p = mat2[0].length;
        int n = mat2.length;
        if (mat1[0].length != n) {
            throw new IllegalArgumentException("矩阵维度不匹配");
        }
        int[][] result = new int[m][p];
        // 矩阵 1 行循环
        for (int i = 0; i < m; i++) {
            // 矩阵 2 列循环
            for (int j = 0; j < p; j++) {
                int sum = 0;
                // 循环相加矩阵1的行的每个数和矩阵2的列的每个数之积
                for (int k = 0; k < n; k++) {
                    sum += mat1[i][k] * mat2[k][j];
                }
                result[i][j] = sum % 26;
            }
        }
        return result;
    }

    /**
     * 计算密钥矩阵对应的行列式的值
     * @param matrix 密钥矩阵
     * @param n n阶矩阵
     * @return 密钥矩阵对应的行列式的值
     */
    private static int matrixDet(int[][] matrix, int n) {
        if (n == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        } else if (n == 3) {
            return matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1])
                    - matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0])
                    + matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);
        } else {
            throw new IllegalArgumentException("不支持的矩阵阶数");
        }
    }

    /**
     * 计算伴随矩阵
     * @param matrix 密钥矩阵
     * @param n 阶数
     * @return 伴随矩阵
     */
    private static int[][] adjugateMatrix(int[][] matrix, int n) {
        if (n == 2) {
            return new int[][]{{matrix[1][1], -matrix[0][1]},
                    {-matrix[1][0], matrix[0][0]}};
        } else if (n == 3) {
            int[][] adj = new int[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int[][] minor = new int[2][2];
                    int minorRow = 0;
                    for (int r = 0; r < 3; r++) {
                        if (r == i) continue;
                        int minorCol = 0;
                        for (int c = 0; c < 3; c++) {
                            if (c == j) continue;
                            minor[minorRow][minorCol++] = matrix[r][c];
                        }
                        minorRow++;
                    }
                    int det = minor[0][0] * minor[1][1] - minor[0][1] * minor[1][0];
                    adj[i][j] = (int) (det * Math.pow(-1, i + j));
                }
            }
            return transposeMatrix(adj);
        } else {
            throw new IllegalArgumentException("不支持的矩阵阶数");
        }
    }

    /**
     * 求转置矩阵（是为了求伴随矩阵，密钥矩阵的代数余子式矩阵的转置矩阵就是伴随矩阵）
     * @param matrix 密钥矩阵
     * @return 转置矩阵
     */
    private static int[][] transposeMatrix(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] transpose = new int[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transpose[j][i] = matrix[i][j];
            }
        }
        return transpose;
    }


    /**
     * 计算最大公约数
     * @param a 数 a
     * @param b 数 b
     * @return 最大公约数
     */
    private static int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    /**
     * 求模逆元
     * @param a 数 a
     * @param mod 模数
     * @return 模逆元
     */
    private static int modInverse(int a, int mod) {
        for (int i = 1; i < mod; i++) {
            if ((a * i) % mod == 1) {
                return i;
            }
        }
        return -1; // 无解表示不可逆
    }


    /**
     * 计算模26逆矩阵
     * @param matrix 密钥矩阵
     * @param n 阶数
     * @return 逆矩阵
     */
    private static int[][] inverseMatrix(int[][] matrix, int n) {
        int det = matrixDet(matrix, n);
        int detMod = (det % 26 + 26) % 26; // 确保行列式取模结果为非负数
        if (gcd(detMod, 26) != 1) {
            return null; // 矩阵不可逆
        }
        int detInv = modInverse(detMod, 26);
        if (detInv == -1) {
            return null; // 模逆元不存在，矩阵不可逆
        }
        int[][] adj = adjugateMatrix(matrix, n);
        int[][] invMatrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                invMatrix[i][j] = (adj[i][j] * detInv) % 26;
                if (invMatrix[i][j] < 0) {
                    invMatrix[i][j] += 26; // 处理负数结果
                }
            }
        }
        return invMatrix;
    }

    /**
     * 加密
     * @param plaintext 明文
     * @param key 密钥
     * @return 密文
     */
    private static String encrypt(String plaintext, int[][] key) {
        int n = key.length;
        int[][] plainMatrix = generatePlaintextMatrix(plaintext, n);
        int[][] cipherMatrix = matrixMultiply(key, plainMatrix);

        StringBuilder cipherText = new StringBuilder();
        for (int i = 0; i < cipherMatrix[0].length; i++) {
            for (int j = 0; j < cipherMatrix.length; j++) {
                cipherText.append(getChar(cipherMatrix[j][i]));
            }
        }
        return cipherText.toString();
    }

    /**
     * 解密
     * @param ciphertext 密文
     * @param key 密钥
     * @return 明文
     */
    private static String decrypt(String ciphertext, int[][] key) {
        int n = key.length;
        int[][] invKey = inverseMatrix(key, n);

        int[][] cipherMatrix = generatePlaintextMatrix(ciphertext, n);
        int[][] plainMatrix = matrixMultiply(invKey, cipherMatrix);

        StringBuilder plainText = new StringBuilder();
        for (int i = 0; i < cipherMatrix[0].length; i++) {
            for (int j = 0; j < cipherMatrix.length; j++) {
                plainText.append(getChar(plainMatrix[j][i]));
            }
        }
        // 去除填充的'A'
        return plainText.toString().replaceAll("A+$", "");
    }

    private static boolean isKeyMatrixInvertible(int[][] matrix, int n) {
        return gcd(matrixDet(matrix, n), 26) == 1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hill Cipher 加解密系统");
        System.out.println("============================================================");
        try {
            while (true) {
                try {
                    System.out.print("选择加密,解密还是退出?(E/D/Q)");
                    String mode = scanner.nextLine().toUpperCase();
                    if (mode.equals("Q")) {
                        System.out.println("程序已退出");
                        break;
                    } else if (mode.equals("E")) {
                        System.out.print("请输入要加密的字符串： ");
                        String plaintext = scanner.nextLine();
                        int n = getValidOrder(scanner);
                        System.out.print("请输入密钥：");
                        String key = scanner.nextLine();
                        int[][] matrix = generateKeyMatrix(key, n);
                        if (!isKeyMatrixInvertible(matrix, n)) {
                            System.out.println("错误：密钥矩阵在 mod26 下不可逆，请重新输入。");
                            continue;
                        }
                        System.out.println("密文为：" + encrypt(plaintext, matrix));
                    } else if (mode.equals("D")) {
                        System.out.print("请输入要解密的字符串： ");
                        String ciphertext = scanner.nextLine();
                        int n = getValidOrder(scanner);
                        System.out.print("请输入密钥：");
                        String key = scanner.nextLine();
                        int[][] matrix = generateKeyMatrix(key, n);
                        if (!isKeyMatrixInvertible(matrix, n)) {
                            System.out.println("错误：密钥矩阵在 mod26 下不可逆，请重新输入。");
                            continue;
                        }
                        System.out.println("明文为：" + decrypt(ciphertext, matrix));
                    } else {
                        System.out.println("错误：请输入E/D/Q");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("错误：" + e.getMessage() + "，请重新输入。");
                }
            }
        } finally {
            scanner.close();
        }
    }

    private static int getValidOrder(Scanner scanner) {
        while (true) {
            try {
                System.out.print("请选择阶数(2/3)：");
                int n = scanner.nextInt();
                scanner.nextLine(); // 消耗换行符
                if (n == 2 || n == 3) {
                    return n;
                }
                System.out.println("错误：请选择正确的阶数。");
            } catch (Exception e) {
                System.out.println("错误：输入无效，请输入一个数字。");
                scanner.nextLine(); // 消耗无效输入
            }
        }
    }

}