package io.github.luchersol.collections;

import java.util.ArrayList;
import java.util.Arrays;
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

public class List2<E> extends AbstractList2<E> {
    
    // ----------------- Constructors -----------------

    public List2() {
        this.inner = new ArrayList<>();
    }

    public List2(Collection<? extends E> c) {
        this.inner = new ArrayList<>(c);
    }

    public List2(Collection<? extends E> c, Supplier<List<E>> factory) {
        this.inner = factory.get();
        this.inner.addAll(c);
    }

    public static <E> List2<E> empty() {
        return new List2<E>();
    }

    public static <E> List2<E> of(Collection<E> c) {
        return new List2<>(c);
    } 

    @SafeVarargs
    public static <E> List2<E> of(E... arg0) {
        return new List2<>(Arrays.asList(arg0));
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

    public List2<E> toFilter(Predicate<? super E> predicate) {
        List2<E> result = empty();
        for (E e : this.inner) 
            if(predicate.test(e)) result.add(e);
        return result;
    }

    public void filter(Predicate<? super E> predicate) {
        removeIf(predicate.negate());
    }

    public <R> List2<R> toMapped(Function<? super E, ? extends R> mapper) {
        List2<R> result = empty();
        for (E e : this.inner) 
            result.add(mapper.apply(e));
        return result;
    }

    public void map(Function<? super E, ? extends E> mapper) {
        for (int i = 0, n = size(); i < n; i++)
            set(i, mapper.apply(get(i)));
    }

    public List2<E> toDistinct() {
        Set<E> seen = new HashSet<>();
        List2<E> result = new List2<>();
        for (E e : this.inner) 
            if (seen.add(e)) result.add(e);
        return result;
    }

    public void distinct() {
        Set<E> seen = new HashSet<>();
        for (int i = 0; i < size(); ) {
            E e = get(i);
            if (!seen.add(e)) remove(i);
            else i++;
        }
    }

    public List2<E> toSorted(Comparator<? super E> comparator) {
        List2<E> result = copy();
        Collections.sort(result, comparator);
        return result;
    }

    public void reverse() {
        Collections.reverse(inner);
    }

    public void shuffle() {
        Collections.shuffle(inner);
    }

    public void rotateRight(int distance) {
        Collections.rotate(inner, distance);
    }

    public void rotateLeft(int distance) {
        Collections.rotate(inner, -distance);
    }

    public void swap(int i, int j) {
        E temp = get(i);
        inner.set(i, get(j));
        inner.set(j, temp);
    }

    // ----------------- Combination and extraction -----------------

    public final List2<E> concat(Collection<Collection<? extends E>> collections) {
        List2<E> result = copy();
        collections.forEach(result::addAll);
        return result;
    }

    @SafeVarargs
    public final void concat(Collection<? extends E>... collections) {
        for(Collection<? extends E> collection: collections) 
            inner.addAll(collection);
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
