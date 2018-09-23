package kattis.john.col.integers;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class PrimeReducerTest {

  private final PrimeReducer primeReducer = new PrimeReducer();

  @Test
  public void shouldSolveASinglePrimeNumber_2() {
    int number = 2;

    ReductionResult result = primeReducer.reduce(number);

    assertThat(result.toString(), is("2 1"));
  }

  @Test
  public void shouldSolveASinglePrimeNumber_3() {
    int number = 3;

    ReductionResult result = primeReducer.reduce(number);

    assertThat(result.toString(), is("3 1"));
  }

  @Test
  public void shouldSolveASinglePrimeNumber_5() {
    int number = 5;

    ReductionResult result = primeReducer.reduce(number);

    assertThat(result.toString(), is("5 1"));
  }

  @Test
  public void shouldSolve_76() {
    int number = 76;

    ReductionResult result = primeReducer.reduce(number);

    assertThat(result.toString(), is("23 2"));
  }

  @Test
  public void shouldSolve_100() {
    int number = 100;

    ReductionResult result = primeReducer.reduce(number);

    assertThat(result.toString(), is("5 5"));
  }

  @Test
  public void shouldSolve_2001() {
    int number = 2001;

    ReductionResult result = primeReducer.reduce(number);

    assertThat(result.toString(), is("5 6"));
  }

}
