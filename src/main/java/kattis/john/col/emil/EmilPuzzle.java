package kattis.john.col.emil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class EmilPuzzle {

  public static void main(String[] args) throws IOException {
//    List<Puzzle> puzzles = Emil.giveMeThePuzzles();
    List<Puzzle> puzzles = Emil.mapToPuzzles(Files.readAllLines(Paths.get(args[0])));
    Alan.solveThePuzzles(puzzles);
  }

}

class Alan {

  public static void solveThePuzzles(List<Puzzle> puzzles) {
    for (Puzzle puzzle : puzzles) {
      PuzzleSolution puzzleSolution = solvePuzzle(puzzle);
      System.out.println(puzzleSolution);
    }
  }

  private static PuzzleSolution solvePuzzle(Puzzle puzzle) {
    List<PuzzlePair> pairsSelectableToStart = Alan.findPairsSelectableToStart(puzzle.getPairs());

    List<String> solutionsSortedByLength = pairsSelectableToStart.stream()
        .flatMap(startingPair -> {
          PairsInspected pairsInspected = new PairsInspected(puzzle).withNewInspectedIndex(startingPair);
          String missingSuffix = Alan.findMissingSuffix(startingPair.getFirst(), startingPair.getSecond());
          PuzzlePair.Part pairPart = startingPair.firstStartsWithSecond() ? PuzzlePair.Part.SECOND : PuzzlePair.Part.FIRST;
          return Alan.findSolutions(pairsInspected, missingSuffix, pairPart).stream();
        })
        .map(PairsInspected::buildSolution)
        .sorted((solution1, solution2) -> solution1.length() > solution2.length() ? 1 : -1)
        .collect(Collectors.toList());

    if (solutionsSortedByLength.isEmpty()) {
      return PuzzleSolution.unsolvable(puzzle);
    }

    int minLength = solutionsSortedByLength.get(0).length();
    return solutionsSortedByLength.stream()
        .filter(solution -> solution.length() == minLength)
        .findFirst()
        .map(solution -> PuzzleSolution.solved(puzzle, solution))
        .orElseThrow(() -> new RuntimeException("No solution string had the calculated min length"));
  }

  private static List<PairsInspected> findSolutions(PairsInspected pairsInspected, String missingSuffix, PuzzlePair.Part part) {
    if (missingSuffix.isEmpty()) {
      return Collections.singletonList(pairsInspected);
    }

    List<PairsInspected> solutions = new ArrayList<>();

    PuzzlePair[] nonInspectedPairs = pairsInspected.findNonInspectedPairs();

    for (int i = 0; i < nonInspectedPairs.length; i++) {
      PuzzlePair pair = nonInspectedPairs[i];
      if (pair == null) {
        break;
      }

      String pairPart = pair.get(part);
      String otherPairPart = pair.get(part.theOtherOne());
      String missingSuffixPlusOtherPairPart = missingSuffix + otherPairPart;

      boolean partStartsWithSuffix = pairPart.startsWith(missingSuffixPlusOtherPairPart);
      boolean suffixStartsWithPart = missingSuffixPlusOtherPairPart.startsWith(pairPart);

      if (partStartsWithSuffix || suffixStartsWithPart) {
        PairsInspected newPairsInspected = pairsInspected.withNewInspectedIndex(pair);
        String newMissingSuffix = Alan.findMissingSuffix(pairPart, missingSuffixPlusOtherPairPart);
        PuzzlePair.Part partToInspect = suffixStartsWithPart ? part : part.theOtherOne();
        List<PairsInspected> thisWaySolutions = Alan.findSolutions(newPairsInspected, newMissingSuffix, partToInspect);
        solutions.addAll(thisWaySolutions);
      }
    }

    return solutions;
  }

  private static String findMissingSuffix(String firstString, String secondString) {
    return firstString.startsWith(secondString) ?
        firstString.substring(secondString.length()) :
        secondString.substring(firstString.length());
  }

  private static List<PuzzlePair> findPairsSelectableToStart(PuzzlePair[] pairs) {
    List<PuzzlePair> starters = new ArrayList<>();
    for (int i = 0; i < pairs.length; i++) {
      PuzzlePair pair = pairs[i];
      if (pair.firstStartsWithSecond() || pair.secondStartsWithFirst()) {
        starters.add(pair);
      }
    }
    return starters;
  }
}

class PuzzlePair {

  public enum Part {
    FIRST, SECOND;

    public Part theOtherOne() {
      return this == FIRST ? SECOND : FIRST;
    }
  }

  private final String first;
  private final String second;
  private final byte indexInPuzzle;

  private PuzzlePair(String first, String second, byte indexInPuzzle) {
    this.first = first;
    this.second = second;
    this.indexInPuzzle = indexInPuzzle;
  }

  public static PuzzlePair fromLine(String line, byte indexInPuzzle) {
    String[] lineStrings = line.split(" ");
    return new PuzzlePair(lineStrings[0], lineStrings[1], indexInPuzzle);
  }

  public boolean firstStartsWithSecond() {
    return first.startsWith(second);
  }

  public boolean secondStartsWithFirst() {
    return second.startsWith(first);
  }

  public String get(Part part) {
    return part.equals(Part.FIRST) ? getFirst() : getSecond();
  }

  public String getFirst() {
    return first;
  }

  public String getSecond() {
    return second;
  }

  public byte index() {
    return indexInPuzzle;
  }

}

class Puzzle {

  private final int number;
  private final PuzzlePair[] pairs;

  public Puzzle(int number, PuzzlePair[] pairs) {
    this.number = number;
    this.pairs = pairs;
  }

  public int getNumber() {
    return number;
  }

  public PuzzlePair[] getPairs() {
    return pairs;
  }

}

class PairsInspected {

  private final Puzzle puzzle;
  private final byte[] indexes;

  public PairsInspected(Puzzle puzzle) {
    this(puzzle, new byte[]{});
  }

  public PairsInspected(Puzzle puzzle, byte[] indexes) {
    this.puzzle = puzzle;
    this.indexes = indexes;
  }

  public PuzzlePair[] findNonInspectedPairs() {
    PuzzlePair[] pairs = puzzle.getPairs();
    PuzzlePair[] nonInspectedPairs = new PuzzlePair[pairs.length];
    byte count = 0;
    for (byte indexToInspect = 0; indexToInspect < pairs.length; indexToInspect++) {
      boolean indexFound = false;
      for (byte index : indexes) {
        if (index == indexToInspect) {
          indexFound = true;
          break;
        }
      }
      if (!indexFound) {
        nonInspectedPairs[count++] = pairs[indexToInspect];
      }
    }
    return nonInspectedPairs;
  }

  public PairsInspected withNewInspectedIndex(PuzzlePair puzzlePair) {
    byte[] newIndexes = Arrays.copyOf(indexes, indexes.length + 1);
    newIndexes[indexes.length] = puzzlePair.index();
    return new PairsInspected(puzzle, newIndexes);
  }

  public String buildSolution() {
    PuzzlePair[] pairs = puzzle.getPairs();
    StringBuilder stringBuilder = new StringBuilder();
    for (byte i = 0; i < indexes.length; i++) {
      stringBuilder.append(pairs[indexes[i]].getFirst());
    }
    return stringBuilder.toString();
  }
}

class Emil {

  public static List<Puzzle> giveMeThePuzzles() {
    List<String> inputLines = readInputLines();
    return mapToPuzzles(inputLines);
  }

  private static List<String> readInputLines() {
    Scanner scanner = new Scanner(System.in);
    List<String> lines = new ArrayList<>();
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      lines.add(line);
    }
    return lines;
  }

  public static List<Puzzle> mapToPuzzles(List<String> inputLines) {
    List<Puzzle> puzzles = new ArrayList<>();
    byte linesIndex = 0;
    while (linesIndex < inputLines.size()) {
      byte pairsCount = Byte.parseByte(inputLines.get(linesIndex));
      Puzzle puzzle = Emil.buildPuzzle(inputLines, (byte) (puzzles.size() + 1), linesIndex, pairsCount);
      puzzles.add(puzzle);
      linesIndex += (pairsCount + 1);
    }
    return puzzles;
  }

  private static Puzzle buildPuzzle(List<String> inputLines, byte puzzleNumber, byte linesIndex, byte pairsCount) {
    PuzzlePair[] pairs = new PuzzlePair[pairsCount];
    int delta = linesIndex + 1;
    for (byte i = 0; i < pairsCount; i++) {
      pairs[i] = PuzzlePair.fromLine(inputLines.get(i + delta), i);
    }
    return new Puzzle(puzzleNumber, pairs);
  }

}

class PuzzleSolution {

  private final Puzzle puzzle;
  private final Optional<String> solution;

  private PuzzleSolution(Puzzle puzzle, Optional<String> solution) {
    this.puzzle = puzzle;
    this.solution = solution;
  }

  public static PuzzleSolution solved(Puzzle puzzle, String solution) {
    return new PuzzleSolution(puzzle, Optional.of(solution));
  }

  public static PuzzleSolution unsolvable(Puzzle puzzle) {
    return new PuzzleSolution(puzzle, Optional.empty());
  }

  @Override
  public String toString() {
    return "Case " + puzzle.getNumber() + ": " + solution.orElse("IMPOSSIBLE");
  }
}
