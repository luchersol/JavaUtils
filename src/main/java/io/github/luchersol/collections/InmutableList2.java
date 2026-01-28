package io.github.luchersol.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import io.github.luchersol.Checker;

public class InmutableList2<E> extends AbstractList2<E> {
    
    // ----------------- Constructors -----------------

    public InmutableList2() {
        this.inner = new ArrayList<>();
    }

    public InmutableList2(Collection<? extends E> c) {
        this.inner = new ArrayList<>(c);
    }

    public InmutableList2(List<? extends E> c) {
        this.inner = new ArrayList<>(c);
    }

    public static <E> List2<E> empty() {
        return new List2<E>();
    }

    public static <E> List2<E> of(Collection<E> c) {
        return new List2<>(c);
    } 

    @SafeVarargs
    public static <E> List2<E> of(E... arg0) {
        return new List2<>(List.of(arg0));
    }

    public List2<E> copy() {
        return of(this);
    }

    
    // ----------------- Getters -----------------
    
    public List2<E> choose(List<Integer> indexes) {
        List2<E> result = empty();
        boolean validIndexes = indexes.stream().allMatch(i -> i >= 0 && i < size());
        Checker.check(validIndexes, "Todos los indices deben existir entre 0 y la longitud de la lista");
        for(int index: indexes) result.add(get(index));
        return result;
    }
    
    // ----------------- Filters and mappers -----------------

    public List2<E> filter(Predicate<? super E> predicate) {
        List2<E> result = empty();
        for (E e : this.inner) 
            if(predicate.test(e)) result.add(e);
        return result;
    }

    public <R> List2<R> map(Function<? super E, ? extends R> mapper) {
        List2<R> result = empty();
        for (E e : this.inner) 
            result.add(mapper.apply(e));
        return result;
    }

    public List2<E> distinct() {
        Set<E> seen = new HashSet<>();
        List2<E> result = new List2<>();
        for (E e : this.inner) 
            if (seen.add(e)) result.add(e);
        return result;
    }

    public List2<E> sortBy(Comparator<? super E> comparator) {
        List2<E> result = copy();
        Collections.sort(result, comparator);
        return result;
    }

    public List2<E> reverse() {
        List2<E> result = copy();
        Collections.reverse(result);
        return result;
    }

    public List2<E> shuffle() {
        List2<E> result = copy();
        Collections.shuffle(result);
        return result;
    }

    public List2<E> rotateRight(int distance) {
        List2<E> result = copy();
        Collections.rotate(result, distance);
        return result;
    }

    public List2<E> rotateLeft(int distance) {
        List2<E> result = copy();
        Collections.rotate(result, -distance);
        return result;
    }

    public List2<E> swap(int i, int j) {
        List2<E> result = copy();
        result.set(i, get(j));
        result.set(j, get(i));
        return result;
    }

    // ----------------- Combination and extraction -----------------

    public final List2<E> concat(Collection<Collection<? extends E>> collections) {
        List2<E> result = copy();
        collections.forEach(result::addAll);
        return result;
    }

    @SafeVarargs
    public final List2<E> concat(Collection<? extends E>... collections) {
        List2<E> result = copy();
        for(Collection<? extends E> collection: collections) 
            result.addAll(collection);
        return result;
    }

    public List2<E> difference(Collection<? extends E> other){
		List2<E> result = copy();
        result.removeAll(other);
		return result;
	}
	
	public List2<E> union(Collection<? extends E> other){
        Set<E> seen = new HashSet<>(copy());
        List2<E> result = copy();
        for (E e : other) 
            if (seen.add(e)) result.add(e);
        return result;
	}
	
	public List2<E> intersection(Collection<? extends E> other){
		List2<E> result = copy();
		result.retainAll(other);
		return result;
	}

    public List2<E> subListSafe(int from, int to) {
        int n = size();

        if (n == 0) return empty();

        from = Math.max(0, from);
        to = Math.min(n, to);

        if (from >= to) return empty();

        return of(subList(from, to));
    }


    
    public <C extends Collection<E>> List2<C> split(int chunkSize, Supplier<C> innerSupplier) {
        return split(chunkSize, List2::new, innerSupplier);
    }

    public List2<List2<E>> split(int chunkSize) {
        return split(chunkSize, List2::new, List2::new);
    }

    // ----------------- Advanced transformations -----------------
    
    public static <E> List2<E> flatten(Collection<Collection<E>> collections) {
        List<E> result = collections.stream().flatMap(Collection::stream).collect(Collectors.toList());
        return of(result);
    }

}
