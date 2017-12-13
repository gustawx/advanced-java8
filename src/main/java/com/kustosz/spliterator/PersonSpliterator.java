package com.kustosz.spliterator;

import com.kustosz.spliterator.model.Person;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Spliterator used to create own stream
 * need to override 4 methods, tryAdvance, trySplit, estimateSize and characteristics
 */

public class PersonSpliterator implements Spliterator{
    private final Spliterator<String> lineSpliterator;
    private String name;
    private Integer age;
    private String city;

    PersonSpliterator(Spliterator lineSpliterator){
        this.lineSpliterator = lineSpliterator;
    }

    /**
     * Parses lines, returns Person object and passes it to accept method
     * @return boolean whether operation succeeded
     */
    @Override
    public boolean tryAdvance(Consumer action) {
        if (this.lineSpliterator.tryAdvance(line -> this.name = line) &&
           this.lineSpliterator.tryAdvance(line -> this.age = Integer.parseInt(line)) &&
           this.lineSpliterator.tryAdvance(line -> this.city = line)) {

            Person p = new Person(name, age, city);
            action.accept(p);
            return true;
        } else return false;
    }

    @Override
    public Spliterator trySplit() {
        return null;
    }

    /**
     * one Person is combined of 3 lines, hence divided by 3
     */
    @Override
    public long estimateSize() {
        return lineSpliterator.estimateSize() / 3;
    }

    @Override
    public int characteristics() {
        return lineSpliterator.characteristics();
    }
}
