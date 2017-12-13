package com.kustosz.collectors;

import com.kustosz.FileUtil;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UsingCollectors {
    @SneakyThrows
    public static void main(String... args){
        Set<String> shakespearWords = Files.lines(FileUtil.getFilePath("/int_streams/words.shakespeare.txt"))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        Set<String> scrabbleWords = Files.lines(FileUtil.getFilePath("/int_streams/ospd.txt"))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        // score of each letter in scrabble
        final int[] letterScores = {
                // a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p,  q, r, s, t, u, v, w, x, y,  z
                1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10} ;

        // get word score
        Function<String, Integer> score =
                word -> word.
                        chars().
                        map(letter -> letterScores[letter - 'a']).
                        sum();

        // create a Map with words grouped by score
        Map<Integer, List<String>> wordsByScore =
                shakespearWords.stream()
                    .filter(scrabbleWords::contains)
                    .collect(Collectors.groupingBy(score));

        System.out.println("Map size: " + wordsByScore.size());

        List threeBestScores =
            wordsByScore.entrySet() // Set<Map.Entry<Integer, List<String>>>
                    .stream()
                    .sorted(Comparator.comparingInt(entry -> -entry.getKey()))
                    .limit(3)
                    .collect(Collectors.toList());

        System.out.println(threeBestScores);

        // considering playing with two blank scrabbles
        // below array of numbers of letters
        int[] scrabbleENDistribution = {
             // a, b, c, d,  e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z
                9, 2, 2, 1, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};

        // compute repeating letters in a given word
        Function<String, Map<Integer, Long>> histogramOfWord =
                word -> word.chars()
                            .boxed()
                            .collect(Collectors.groupingBy(
                                    Function.identity(),
                                    Collectors.counting()
                            ));

        Function<String, Long> numberOfBlanks =
                word -> histogramOfWord.apply(word) //Map<Integer, Long> Map<letter, # of letters>
                .entrySet()
                .stream() //Map.Entry<Integer, Long>
                //compute number of blanks needed to write a word
                .mapToLong(
                        entry ->
                            Long.max(entry.getValue() -
                                    (long)scrabbleENDistribution[entry.getKey() - 'a'],
                                    0L
                            )
                ).sum();

        System.out.println("# of blanks for whizzing: " + numberOfBlanks.apply("whizzing"));

        // word score including number of particular scrabbles and blanks
        Function<String, Integer> scoreWithBlanks =
                word -> histogramOfWord.apply(word)
                .entrySet()
                .stream() //Map.Entry<Integer, Long>
                .mapToInt(
                        entry -> letterScores[entry.getKey() - 'a']*
                                Integer.min(entry.getValue().intValue(),
                                        scrabbleENDistribution[entry.getKey() - 'a']
                                )
                ).sum();

        System.out.println("# score for whizzing: " + score.apply("whizzing"));
        System.out.println("# scoreWithBlanks for whizzing: " + scoreWithBlanks.apply("whizzing"));

        // create a Map with words grouped by score
//        Map<Integer, List<String>> wordsByScore2 =
                shakespearWords
                        .stream()
                        .filter(scrabbleWords::contains)
                        .filter(word -> numberOfBlanks.apply(word) <= 2)
                        .collect(Collectors.groupingBy(scoreWithBlanks))
                        .entrySet()
                        .stream()
                        .sorted(Comparator.comparingInt(entry -> -entry.getKey()))
                        .limit(3)
                        .forEach(System.out::println);
//                        .collect(Collectors.toList());

    }
}
