package kattis.john.col.integers;

import java.util.ArrayList;
import java.util.List;

public class Factors {

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
    if (a != 1) list.add(a);
    if (b != 1) list.add(b);
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
    if (prime != 1) factors.add(prime);
    return this;
  }

  public List<Integer> getList() {
    return factors;
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
