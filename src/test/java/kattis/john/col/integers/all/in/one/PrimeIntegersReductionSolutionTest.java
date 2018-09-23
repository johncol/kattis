package kattis.john.col.integers.all.in.one;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class PrimeIntegersReductionSolutionTest {

  private final PrimeIntegersReductionSolution primeReducer = new PrimeIntegersReductionSolution();

  @Test
  public void shouldSolveASinglePrimeNumber_2() {
    int number = 2;

    String result = primeReducer.reduce(number);

    assertThat(result, is("2 1"));
  }

  @Test
  public void shouldSolveASinglePrimeNumber_3() {
    int number = 3;

    String result = primeReducer.reduce(number);

    assertThat(result, is("3 1"));
  }

  @Test
  public void shouldSolveASinglePrimeNumber_5() {
    int number = 5;

    String result = primeReducer.reduce(number);

    assertThat(result, is("5 1"));
  }

  @Test
  public void shouldSolve_76() {
    int number = 76;

    String result = primeReducer.reduce(number);

    assertThat(result, is("23 2"));
  }

  @Test
  public void shouldSolve_100() {
    int number = 100;

    String result = primeReducer.reduce(number);

    assertThat(result, is("5 5"));
  }

  @Test
  public void shouldSolve_2001() {
    int number = 2001;

    String result = primeReducer.reduce(number);

    assertThat(result, is("5 6"));
  }

}
