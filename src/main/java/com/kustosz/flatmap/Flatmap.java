package com.kustosz.flatmap;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.kustosz.FileUtil.getFilePath;

public class Flatmap {
    @SneakyThrows
    public static void main(String... args) {
        Stream<String> stream1 = Files.lines(getFilePath("/flatmap_files/TomSawyer_01.txt"));
        Stream<String> stream2 = Files.lines(getFilePath("/flatmap_files/TomSawyer_02.txt"));
        Stream<String> stream3 = Files.lines(getFilePath("/flatmap_files/TomSawyer_03.txt"));
        Stream<String> stream4 = Files.lines(getFilePath("/flatmap_files/TomSawyer_04.txt"));

        // merging streams
        Stream<String> streamOfLines = Stream.of(stream1, stream2, stream3, stream4)
                                             .flatMap(Function.identity());
        // creating stream of all words
        Stream<String> streamOfWords = streamOfLines.flatMap(line ->
                                                    Pattern.compile(" ").splitAsStream(line));

        System.out.println(streamOfWords.count());

        // number of distinct words four characters long
        long numberOfWords = streamOfWords.map(String::toLowerCase)
                                                     .filter(word -> word.length() == 4)
                                                     .distinct()
                                                     .count();
        System.out.println(numberOfWords);
    }
}
