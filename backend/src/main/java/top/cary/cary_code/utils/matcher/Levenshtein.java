package top.cary.cary_code.utils.matcher;

import java.lang.Math;

public class Levenshtein {

    public static void main(String[] args) {
        System.out.println(compare("AC","XXXXXXXXACXXXXXX"));
    }

    public static double compare(final String s1, final String s2) {
        double retval = 0.0;
        final int m = s1.length();
        final int n = s2.length();
        if (m == 0 || n == 0)
            return retval;
        return retval = 1.0 - (compare(s1, m, s2, n) / (Math.max(m, n)));
    }

    private static double compare(final String s1, final int m, final String s2, final int n) {
        int matrix[][] = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++)
            matrix[i][0] = i;
        for (int i = 0; i <= n; i++)
            matrix[0][i] = i;

        for (int i = 1; i <= m; i++) {
            int s1i = s1.codePointAt(i - 1);
            for (int j = 1; j <= n; j++) {
                int s2j = s2.codePointAt(j - 1);
                final int cost = s1i == s2j ? 0 : 1;
                matrix[i][j] = min3(matrix[i - 1][j] + 1, matrix[i][j - 1] + 1, matrix[i - 1][j - 1] + cost);
            }
        }
        return matrix[m][n];
    }

    private static int min3(final int a, final int b, final int c) {
        return Math.min(Math.min(a, b), c);
    }
}