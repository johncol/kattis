package kattis.john.col.integers.all.in.one;

import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.OutputStream;

public class PrimeIntegersReductionSolution {

  private static final int END_OF_INPUT = 4;

  private final PrimeReducer primeReducer = new PrimeReducer();

  public String reduce(int x) {
    return primeReducer.reduce(x).toString();
  }

  public static void main(String[] args) {
    Kattio kattio = new Kattio(System.in);
    PrimeReducer primeReducer = new PrimeReducer();

    int number;
    while (kattio.hasMoreTokens() && (number = kattio.getInt()) != END_OF_INPUT) {
      System.out.println(primeReducer.reduce(number));
    }
  }

}

class PrimeReducer {

  private final FactorsFinder factorsFinder = new FactorsFinder();

  ReductionResult reduce(int x) {
    if (Numbers.isPrime(x)) {
      return new ReductionResult(x, 1);
    }
    return reduce(factorsFinder.factorsOf(x).getSum())
        .plusOneIteration();
  }

}

class FactorsFinder {

  Factors factorsOf(int x) {
    if (Numbers.isPrime(x)) {
      return Factors.of(x);
    }

    int counterOf2 = 0;
    int number = x;
    while (Numbers.isDivisibleBy(number, 2)) {
      number = number / 2;
      counterOf2++;
    }
    if (number == 1) {
      return Factors.powerOf2(counterOf2);
    }

    Factors factorsOfNumber = findFactorsOfOddNumberByFermatTheorem(number);
    return factorsOfNumber.combinedWithPowerOf2(counterOf2);
  }

  private Factors findFactorsOfOddNumberByFermatTheorem(int x) {
    int a = (int) Math.ceil(Math.sqrt(x));
    int b = a * a - x;

    while (!isSquare(b)) {
      a++;
      b = a * a - x;
    }
    b = (int) Math.sqrt(b);

    int factorA = a + b;
    int factorB = a - b;

    boolean factorAEqualsOne = factorA == 1;
    if (factorAEqualsOne || factorB == 1) {
      return Factors.of(factorAEqualsOne ? factorB : factorA);
    }

    boolean isPrimeFactorA = Numbers.isPrime(factorA);
    boolean isPrimeFactorB = Numbers.isPrime(factorB);

    if (isPrimeFactorA && isPrimeFactorB) {
      return Factors.of(factorA, factorB);
    } else if (isPrimeFactorA) {
      return findFactorsOfOddNumberByFermatTheorem(factorB).includingPrime(factorA);
    } else {
      return findFactorsOfOddNumberByFermatTheorem(factorA).includingPrime(factorB);
    }

//    else if (isPrimeFactorB) {
//      return findFactorsOfOddNumberByFermatTheorem(factorA).includingPrime(factorB);
//    } else {
//      Factors factorsOfA = findFactorsOfOddNumberByFermatTheorem(factorA);
//      Factors factorsOfB = findFactorsOfOddNumberByFermatTheorem(factorB);
//      return Factors.of(factorsOfA, factorsOfB);
//    }
  }

  private boolean isSquare(double x) {
    double sqrt = Math.sqrt(x);
    double floor = Math.floor(sqrt);
    return sqrt - floor == 0;
  }

}

class Factors {

  private int[] factors = new int[100];
  private int length = 0;
  private int countOf2 = 0;

  private Factors(int countOf2) {
    this.countOf2 = countOf2;
  }

  static Factors of(int x) {
    return new Factors(0).add(x);
  }

  static Factors of(int a, int b) {
    return new Factors(0).add(a).add(b);
  }

  static Factors powerOf2(int power) {
    return new Factors(power);
  }

//  static Factors of(Factors factorsOfA, Factors factorsOfB) {
//    List<Integer> list = new ArrayList<>(factorsOfA.factors.size() + factorsOfB.factors.size());
//    list.addAll(factorsOfA.factors);
//    list.addAll(factorsOfB.factors);
//    return new Factors(list, 0);
//  }

  Factors combinedWithPowerOf2(int countOf2) {
    this.countOf2 += countOf2;
    return this;
  }

  Factors includingPrime(int prime) {
    return add(prime);
  }

  int getSum() {
    int sum = 0;
    for (int factor : factors) {
      sum += factor;
    }
    return sum + (countOf2 * 2);
  }

  private Factors add(int x) {
    this.factors[length++] = x;
    return this;
  }
}

class ReductionResult {

  private int lastPrime;
  private int iterations;

  ReductionResult(int lastPrime, int iterations) {
    this.lastPrime = lastPrime;
    this.iterations = iterations;
  }

  ReductionResult plusOneIteration() {
    iterations++;
    return this;
  }

  @Override
  public String toString() {
    return lastPrime + " " + iterations;
  }
}

class Numbers {

  static boolean isPrime(int x) {
    for (int i = 2; i <= Math.sqrt(x); i++) {
      if (isDivisibleBy(x, i)) {
        return false;
      }
    }
    return true;
  }

  static boolean isDivisibleBy(int x, int y) {
    return modulus(x, y) == 0;
  }

  static int modulus(int x, int y) {
    return Math.floorMod(x, y);
  }

}

class Kattio extends PrintWriter {

  public Kattio(InputStream i) {
    super(new BufferedOutputStream(System.out));
    r = new BufferedReader(new InputStreamReader(i));
  }

  public Kattio(InputStream i, OutputStream o) {
    super(new BufferedOutputStream(o));
    r = new BufferedReader(new InputStreamReader(i));
  }

  public boolean hasMoreTokens() {
    return peekToken() != null;
  }

  public int getInt() {
    return Integer.parseInt(nextToken());
  }

  public double getDouble() {
    return Double.parseDouble(nextToken());
  }

  public long getLong() {
    return Long.parseLong(nextToken());
  }

  public String getWord() {
    return nextToken();
  }

  private BufferedReader r;
  private String line;
  private StringTokenizer st;
  private String token;

  private String peekToken() {
    if (token == null) {
      try {
        while (st == null || !st.hasMoreTokens()) {
          line = r.readLine();
          if (line == null) {
            return null;
          }
          st = new StringTokenizer(line);
        }
        token = st.nextToken();
      } catch (IOException e) {
      }
    }
    return token;
  }

  private String nextToken() {
    String ans = peekToken();
    token = null;
    return ans;
  }
}
