import com.google.gson.*;
import java.io.*;
import java.util.*;
import java.math.BigInteger;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java Main <json-file>");
            return;
        }

        String fileName = args[0];
        JsonObject json = JsonParser.parseReader(new FileReader(fileName)).getAsJsonObject();

        int k = json.getAsJsonObject("keys").get("k").getAsInt();

        List<Integer> xs = new ArrayList<>();
        List<BigInteger> ys = new ArrayList<>();

        for (String key : json.keySet()) {
            if (key.equals("keys")) continue;

            int x = Integer.parseInt(key);
            xs.add(x);

            JsonObject obj = json.getAsJsonObject(key);
            int base = obj.get("base").getAsInt();
            String val = obj.get("value").getAsString();

            BigInteger y = new BigInteger(val, base);
            ys.add(y);
        }

        // Build matrix using BigInteger
        int m = k - 1;
        BigInteger[][] A = new BigInteger[k][k + 1];
        for (int i = 0; i < k; i++) {
            int x = xs.get(i);
            BigInteger y = ys.get(i);

            for (int j = 0; j <= m; j++) {
                A[i][j] = BigInteger.valueOf(x).pow(m - j);
            }
            A[i][k] = y; // last column = y
        }

        // Solve using Gaussian elimination with BigInteger
        BigInteger[] sol = gaussianBigInteger(A);

        // Print only the constant term C
        System.out.println("C = " + sol[sol.length - 1]);
    }

    // Gaussian elimination using BigInteger with exact division
    public static BigInteger[] gaussianBigInteger(BigInteger[][] a) {
        int n = a.length;

        // Convert matrix to fractions represented by numerator/denominator
        Fraction[][] mat = new Fraction[n][n + 1];
        for (int i = 0; i < n; i++)
            for (int j = 0; j <= n; j++)
                mat[i][j] = new Fraction(a[i][j], BigInteger.ONE);

        // Forward elimination
        for (int i = 0; i < n; i++) {
            // Pivot
            int max = i;
            for (int j = i + 1; j < n; j++)
                if (mat[j][i].abs().compareTo(mat[max][i].abs()) > 0)
                    max = j;
            Fraction[] temp = mat[i];
            mat[i] = mat[max];
            mat[max] = temp;

            // Eliminate
            for (int j = i + 1; j < n; j++) {
                Fraction factor = mat[j][i].divide(mat[i][i]);
                for (int k = i; k <= n; k++)
                    mat[j][k] = mat[j][k].subtract(factor.multiply(mat[i][k]));
            }
        }

        // Back substitution
        Fraction[] ans = new Fraction[n];
        for (int i = n - 1; i >= 0; i--) {
            Fraction sum = mat[i][n];
            for (int j = i + 1; j < n; j++)
                sum = sum.subtract(mat[i][j].multiply(ans[j]));
            ans[i] = sum.divide(mat[i][i]);
        }

        // Return only numerator (constant term as BigInteger)
        BigInteger[] result = new BigInteger[n];
        for (int i = 0; i < n; i++)
            result[i] = ans[i].toBigInteger();
        return result;
    }

    // Fraction class for exact arithmetic
    static class Fraction {
        BigInteger num, den;

        Fraction(BigInteger n, BigInteger d) {
            if (d.equals(BigInteger.ZERO))
                throw new ArithmeticException("Divide by zero");
            if (d.signum() < 0) {
                n = n.negate();
                d = d.negate();
            }
            BigInteger g = n.gcd(d);
            num = n.divide(g);
            den = d.divide(g);
        }

        Fraction add(Fraction f) {
            BigInteger n = num.multiply(f.den).add(f.num.multiply(den));
            BigInteger d = den.multiply(f.den);
            return new Fraction(n, d);
        }

        Fraction subtract(Fraction f) {
            BigInteger n = num.multiply(f.den).subtract(f.num.multiply(den));
            BigInteger d = den.multiply(f.den);
            return new Fraction(n, d);
        }

        Fraction multiply(Fraction f) {
            return new Fraction(num.multiply(f.num), den.multiply(f.den));
        }

        Fraction divide(Fraction f) {
            return new Fraction(num.multiply(f.den), den.multiply(f.num));
        }

        int compareTo(Fraction f) {
            return num.multiply(f.den).compareTo(f.num.multiply(den));
        }

        Fraction abs() {
            return new Fraction(num.abs(), den);
        }

        BigInteger toBigInteger() {
            return num.divide(den);
        }

        public String toString() {
            if (den.equals(BigInteger.ONE)) return num.toString();
            return num + "/" + den;
        }
    }
}
