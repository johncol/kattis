package kattis.john.col.integers;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class FactorsFinderTest {

  private final FactorsFinder factorsFinder = new FactorsFinder();

  @Test
  public void shouldFindFactorsFor_2() {
    int number = 2;

    Factors factors = factorsFinder.factorsOf(number);

    assertThat(factors.getList(), containsInAnyOrder(2));
  }

  @Test
  public void shouldFindFactorsFor_3() {
    int number = 3;

    Factors factors = factorsFinder.factorsOf(number);

    assertThat(factors.getList(), containsInAnyOrder(3));
  }

  @Test
  public void shouldFindFactorsFor_5() {
    int number = 5;

    Factors factors = factorsFinder.factorsOf(number);

    assertThat(factors.getList(), containsInAnyOrder(5));
  }

  @Test
  public void shouldFindFactorsFor_100() {
    int number = 100;

    Factors factors = factorsFinder.factorsOf(number);

    assertThat(factors.getList(), containsInAnyOrder(2, 2, 5, 5));
  }

  @Test
  public void shouldFindFactorsFor_2001() {
    int number = 2001;

    Factors factors = factorsFinder.factorsOf(number);

    assertThat(factors.getList(), containsInAnyOrder(3, 23, 29));
  }

}
