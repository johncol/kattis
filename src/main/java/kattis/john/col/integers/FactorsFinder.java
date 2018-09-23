package kattis.john.col.integers;

import static kattis.john.col.integers.Helpers.isDivisibleBy;
import static kattis.john.col.integers.Helpers.isPrime;

public class FactorsFinder {

  public Factors factorsOf(int x) {
    if (isPrime(x)) {
      return Factors.of(x);
    }

    int counterOf2 = 0;
    int number = x;
    while (isDivisibleBy(number, 2)) {
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

    boolean isPrimeFactorA = isPrime(factorA);
    boolean isPrimeFactorB = isPrime(factorB);

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
