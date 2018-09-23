package kattis.john.col.integers;

public class ReductionResult {
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
