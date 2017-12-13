package com.kustosz.Intstreams;

import com.kustosz.FileUtil;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class IntegerSteams {
    @SneakyThrows
    public static void main(String... args){
        Set<String> shakespearWords = Files.lines(FileUtil.getFilePath("/int_streams/words.shakespeare.txt"))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        Set<String> scrabbleWords = Files.lines(FileUtil.getFilePath("/int_streams/ospd.txt"))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        // score of each letter in scrabble
        final int[] scrabbleENScore = {
             // a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p,  q, r, s, t, u, v, w, x, y,  z
                1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10} ;

        Function<String, Integer> score =
                word -> word.
                        chars().
                        map(letter -> scrabbleENScore[letter - 'a']).
                        sum();

        // another way of getting score
        ToIntFunction<String> intScore =
                word -> word.
                        chars().
                        map(letter -> scrabbleENScore[letter - 'a']).
                        sum();

        // get summaryStatistics which contains count, sum, min, max and average
        IntSummaryStatistics intSummaryStatistics = shakespearWords.
                                                    stream().
                                                    filter(scrabbleWords::contains).
                                                    mapToInt(intScore).
                                                    summaryStatistics();
        System.out.println("Shakespear statistics: " + intSummaryStatistics);

        // find the word with biggest score
        String bestWord = shakespearWords.
                        stream().
                        filter(scrabbleWords::contains).
                        max(Comparator.comparing(score)).
                        orElse("apparently something went wrong ;), is Shakespear book empty?");
        System.out.println("Best word: " + bestWord);
    }
}
