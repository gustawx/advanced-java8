package com.kustosz.parallelprocessing;

import com.kustosz.spliterator.model.Person;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.ForkJoinPool;

public class CustomForkJoinThreadPool {
    @SneakyThrows
    public static void main(String... args){
        List<Person> persons = Arrays.asList(new Person("Alice", 52, "New York"),
                new Person("John", 13, "Zurich"),
                new Person("Ian", 45, "Munich"),
                new Person("Seb", 33, "Solothurn"),
                new Person("Maria", 72, "Cracow"),
                new Person("Sam", 18, "Moscow"));

        final int limitAge = 20;

        // create ForkJoinPool with three threads
        ForkJoinPool fjp = new ForkJoinPool(3);
        // submit computation - calculate average age of all people older than 20 years
        OptionalDouble averageAbove20 =
        fjp.submit( () ->
                     persons.parallelStream()
                            .unordered()
                            .mapToInt(Person::getAge)
                            .filter(age -> age > limitAge)
                            .peek(s -> System.out.println("Age: " + s + " is in thread: " + Thread.currentThread().getName()))
                            .average()
        ).get(); //get from Future

        System.out.println("Average age is: " + averageAbove20.orElse(0));

    }
}
