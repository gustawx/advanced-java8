package com.kustosz.spliterator;

import com.kustosz.spliterator.model.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.kustosz.FileUtil.getFilePath;

/**
 * Simple example of custom spliterator usage
 */

public class CreatingSpliterator {
    public static void main(String... args){
        Path path = getFilePath("/creating_spliterator/people.txt");

        try ( Stream<String> lines = Files.lines(path) ) {
            Spliterator<String> lineSpliterator = lines.spliterator();
            PersonSpliterator peopleSpliterator = new PersonSpliterator(lineSpliterator);

            //false means that we will not use trySplit method
            Stream<Person> people = StreamSupport.stream(peopleSpliterator, false);
            people.forEach(System.out::println);

        } catch (IOException ioe){
            ioe.printStackTrace();
        }

    }
}
