package com.kustosz.parallelprocessing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParallelStreams {
    public static void main(String... args){
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "3");

        /*
         * example of wrongly created stream
         * following code will not work correctly because ArrayList is an ordered, not thread safe list
         * random behavior is:
         * - ArrayIndexOutOfBoundsException
         * - some thread may be interrupted which causes different number of elements inside of a stream
         * also using stateful methods like limit() or skip() on parallel streams decreases performance
         */
        List<String> strings = new ArrayList<>();
        try {
            Stream.iterate("+", s -> s + "+").
                    parallel().
                    limit(1000).
                    forEach(strings::add);

            System.out.println("Size of not thread safe collection: " + strings.size());

        } catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("Ooops ArrayIndexOutOfBoundsException occurred on not thread safe collection");
        }

        //to fix the issue with ArrayList we need to use unordered() method
        List<String> strings2 = new ArrayList<>();
        try {
            Stream.iterate("+", s -> s + "+").
                    parallel().
                    unordered().
                    limit(1000).
                    forEach(strings2::add);

            System.out.println("Size of unordered() collection: " + strings2.size());

        } catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("I will never get to this point !!!");
        }

        // finally we should use Collectors to create a List so that automatically thread safe collection
        // is created for us hence there is no need to use unordered() method
        List<String> safeList =
                    Stream.iterate("+", s -> s + "+").
                            parallel().
                            limit(1000).
                            collect(Collectors.toList());
        System.out.println("Size of thread safe collection: " + safeList.size());
    }
}
