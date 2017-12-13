package com.kustosz.optionals;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UsingOptionals {
    public static void main(String... args){
        Function<Double, Stream<Double>> flatMapper =
                d -> NewMath.inv(d)
                        .flatMap(NewMath::sqrt)
                        .map(Stream::of)
                        .orElseGet(Stream::empty);

        List<Double> resultList =
            ThreadLocalRandom.current()
                    .doubles(10_000)
                    .parallel()
                    .map(d -> d*20 - 10)
                    .boxed()
                    .flatMap(flatMapper)
                    .collect(Collectors.toList());

        System.out.println(resultList.size());
    }
}
