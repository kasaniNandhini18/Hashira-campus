import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class Polynomial {

    // Decode a number string from given base to decimal (BigInteger)
    static BigInteger decode(String base, String value) {
        int b = Integer.parseInt(base);
        return new BigInteger(value, b);
    }

    // Lagrange interpolation to compute P(0) (constant term)
    static BigInteger lagrangeInterpolation(BigInteger[] x, BigInteger[] y, int k) {
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {
            BigInteger term = y[i]; // start with y[i]

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    BigInteger numerator = x[j].negate();   // (0 - xj)
                    BigInteger denominator = x[i].subtract(x[j]); // (xi - xj)

                    // Multiply with numerator/denominator
                    term = term.multiply(numerator).divide(denominator);
                }
            }
            result = result.add(term);
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            // Read the JSON file into a string
            BufferedReader br = new BufferedReader(new FileReader("input.json"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line.trim());
            }
            br.close();

            String json = sb.toString();

            // Extract n and k
            int n = Integer.parseInt(json.replaceAll(".*\"n\"\\s*:\\s*(\\d+).*", "$1"));
            int k = Integer.parseInt(json.replaceAll(".*\"k\"\\s*:\\s*(\\d+).*", "$1"));

            // Arrays to store first k points
            BigInteger[] x = new BigInteger[k];
            BigInteger[] y = new BigInteger[k];

            // Regex to match entries like "1": {"base": "10","value":"4"}
            String[] parts = json.split("\\},");
            int count = 0;
            for (String part : parts) {
                if (part.contains("\"base\"") && count < k) {
                    String base = part.replaceAll(".*\"base\"\\s*:\\s*\"(\\w+)\".*", "$1");
                    String value = part.replaceAll(".*\"value\"\\s*:\\s*\"([^\"]+)\".*", "$1");

                    BigInteger decoded = decode(base, value);
                    x[count] = BigInteger.valueOf(count + 1); // index = 1,2,3,...
                    y[count] = decoded;

                    count++;
                }
            }

            // Compute constant term
            BigInteger c = lagrangeInterpolation(x, y, k);
            System.out.println("Constant term c = " + c);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
