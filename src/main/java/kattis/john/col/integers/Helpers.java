package kattis.john.col.integers;

public class Helpers {

  public static boolean isPrime(int x) {
    for (int i = 2; i <= Math.sqrt(x); i++) {
      if (isDivisibleBy(x, i)) {
        return false;
      }
    }
    return true;
  }

  public static int nextPrime(int x) {
    // TODO
    return x;
  }

  public static boolean isDivisibleBy(int x, int y) {
    return modulus(x, y) == 0;
  }

  public static int modulus(int x, int y) {
    return Math.floorMod(x, y);
  }

}
