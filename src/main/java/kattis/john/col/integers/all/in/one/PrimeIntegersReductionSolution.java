package kattis.john.col.integers.all.in.one;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.OutputStream;

public class PrimeIntegersReductionSolution {

  public static final int END_OF_INPUT = 4;

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

  public ReductionResult reduce(int x) {
    if (Numbers.isPrime(x)) {
      return new ReductionResult(x, 1);
    }

    return reduce(factorsFinder.factorsOf(x).getSum())
        .plusOneIteration();
  }

}

class FactorsFinder {

  public Factors factorsOf(int x) {
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

    boolean isPrimeFactorA = Numbers.isPrime(factorA);
    boolean isPrimeFactorB = Numbers.isPrime(factorB);

    if (isPrimeFactorA && isPrimeFactorB) {
      return Factors.of(factorA, factorB);
    } else if (isPrimeFactorA) {
      return findFactorsOfOddNumberByFermatTheorem(factorB).includingPrime(factorA);
    } else if (isPrimeFactorB) {
      return findFactorsOfOddNumberByFermatTheorem(factorA).includingPrime(factorB);
    } else {
      Factors factorsOfA = findFactorsOfOddNumberByFermatTheorem(factorA);
      Factors factorsOfB = findFactorsOfOddNumberByFermatTheorem(factorB);
      return Factors.of(factorsOfA, factorsOfB);
    }
  }

  private boolean isSquare(double x) {
    double sqrt = Math.sqrt(x);
    double floor = Math.floor(sqrt);
    return sqrt - floor == 0;
  }

}

class Factors {

  private List<Integer> factors;

  private Factors(List<Integer> factors) {
    this.factors = factors;
  }

  public static Factors of(int x) {
    List<Integer> list = new ArrayList<>(2);
    list.add(x);
    return new Factors(list);
  }

  public static Factors of(int a, int b) {
    List<Integer> list = new ArrayList<>(2);
    if (a != 1) {
      list.add(a);
    }
    if (b != 1) {
      list.add(b);
    }
    return new Factors(list);
  }

  public static Factors powerOf2(int power) {
    List<Integer> list = new ArrayList<>(power);
    addNTimes2ToList(power, list);
    return new Factors(list);
  }

  public static Factors of(Factors factorsOfA, Factors factorsOfB) {
    List<Integer> list = new ArrayList<>(factorsOfA.factors.size() + factorsOfB.factors.size());
    list.addAll(factorsOfA.factors);
    list.addAll(factorsOfB.factors);
    return new Factors(list);
  }

  public Factors combinedWithPowerOf2(int counterOf2) {
    if (counterOf2 > 0) {
      addNTimes2ToList(counterOf2, factors);
    }
    return this;
  }

  public Factors includingPrime(int prime) {
    if (prime != 1) {
      factors.add(prime);
    }
    return this;
  }

  public int getSum() {
    int sum = 0;
    for (int factor : factors) {
      sum += factor;
    }
    return sum;
  }

  private static void addNTimes2ToList(int power, List<Integer> list) {
    for (int i = 0; i < power; i++) {
      list.add(2);
    }
  }
}

class FactorsWithArray {

  private int[] factors;

  private FactorsWithArray(int ...factors) {
    this.factors = factors;
  }

  public static FactorsWithArray of(int x) {
    return new FactorsWithArray(x);
  }

  public static FactorsWithArray of(int a, int b) {
    return new FactorsWithArray(a, b);
  }

  public static FactorsWithArray powerOf2(int power) {
    int[] factors = new int[power];
    Arrays.fill(factors, 2);
    return new FactorsWithArray(factors);
  }

  public static FactorsWithArray of(FactorsWithArray factorsOfA, FactorsWithArray factorsOfB) {
    int[] factors = Arrays.copyOf(factorsOfA.factors, factorsOfA.factors.length + factorsOfB.factors.length);
    return new FactorsWithArray(factors);
  }

  public FactorsWithArray combinedWithPowerOf2(int counterOf2) {
    if (counterOf2 > 0) {
      factors = addNTimes2ToList(counterOf2, factors);
    }
    return this;
  }

  public FactorsWithArray includingPrime(int prime) {
    if (prime != 1) {
      factors = Arrays.copyOf(factors, factors.length + 1);
      factors[factors.length - 1] = prime;
    }
    return this;
  }

  public int getSum() {
    int sum = 0;
    for (int factor : factors) {
      sum += factor;
    }
    return sum;
  }

  private static int[] addNTimes2ToList(int power, int[] factors) {
    int originalLength = factors.length;
    int[] newFactors = Arrays.copyOf(factors, originalLength + power);
    for (int i = originalLength; i < originalLength + power; i++) {
      newFactors[i] = 2;
    }
    return newFactors;
  }
}

class ReductionResult {

  private int lastPrime;
  private int iterations;

  public ReductionResult(int lastPrime, int iterations) {
    this.lastPrime = lastPrime;
    this.iterations = iterations;
  }

  @Override
  public String toString() {
    return lastPrime + " " + iterations;
  }

  public ReductionResult plusOneIteration() {
    iterations++;
    return this;
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
