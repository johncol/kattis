package kattis.john.col.integers;

import static kattis.john.col.integers.Helpers.isPrime;

public class PrimeReducer {

  private final FactorsFinder factorsFinder = new FactorsFinder();

  public static void main(String[] args) {
  }

  public ReductionResult reduce(int x) {
    if (isPrime(x)) {
      return new ReductionResult(x, 1);
    }

    return reduce(factorsFinder.factorsOf(x).getSum())
        .plusOneIteration();
  }

}
