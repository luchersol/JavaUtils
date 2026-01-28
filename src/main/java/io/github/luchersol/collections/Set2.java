package io.github.luchersol.collections;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Set2<E> extends AbstractSet2<E> {
    
    // ----------------- Constructors -----------------

    public Set2() {
        this.inner = new HashSet<>();
    }

    public Set2(Collection<? extends E> c) {
        this.inner = new HashSet<>(c);
    }

    public Set2(Collection<? extends E> c, Supplier<Set<E>> factory) {
        this.inner = factory.get();
        this.inner.addAll(c);
    }

    public static <E> Set2<E> empty() {
        return new Set2<E>();
    }

    public static <E> Set2<E> of(Collection<E> c) {
        return new Set2<>(c);
    } 

    @SafeVarargs
    public static <E> Set2<E> of(E... arg0) {
        return new Set2<>(List.of(arg0));
    }

    public Set2<E> copy() {
        return of(this);
    }

    // ----------------- Filters and mappers -----------------

    public Set2<E> filter(Predicate<? super E> predicate) {
        Set2<E> result = empty();
        for (E e : this.inner) 
            if(predicate.test(e)) result.add(e);
        return result;
    }

    public <R> Set2<R> map(Function<? super E, ? extends R> mapper) {
        Set2<R> result = empty();
        for (E e : this.inner) 
            result.add(mapper.apply(e));
        return result;
    }

    public Set2<E> distinct() {
        Set<E> seen = new HashSet<>();
        Set2<E> result = new Set2<>();
        for (E e : this.inner) 
            if (seen.add(e)) result.add(e);
        return result;
    }

    // ----------------- Combination and extraction -----------------

    public final Set2<E> concat(Collection<Collection<? extends E>> collections) {
        Set2<E> result = copy();
        collections.forEach(result::addAll);
        return result;
    }

    @SafeVarargs
    public final void concat(Collection<? extends E>... collections) {
        for(Collection<? extends E> collection: collections) 
            inner.addAll(collection);
    }

    public Set2<E> difference(Collection<? extends E> other){
		Set2<E> result = copy();
        result.removeAll(other);
		return result;
	}
	
	public Set2<E> union(Collection<? extends E> other){
        Set<E> seen = new HashSet<>(copy());
        Set2<E> result = copy();
        for (E e : other) 
            if (seen.add(e)) result.add(e);
        return result;
	}
	
	public Set2<E> intersection(Collection<? extends E> other){
		Set2<E> result = copy();
		result.retainAll(other);
		return result;
	}
    
    public <C extends Collection<E>> Set2<C> split(int chunkSize, Supplier<C> innerSupplier) {
        return split(chunkSize, Set2::new, innerSupplier);
    }

    public Set2<Set2<E>> split(int chunkSize) {
        return split(chunkSize, Set2::new, Set2::new);
    }

    // ----------------- Advanced transformations -----------------
    
    public static <E> Set2<E> flatten(Collection<Collection<E>> collections) {
        List<E> result = collections.stream().flatMap(Collection::stream).collect(Collectors.toList());
        return of(result);
    }

}
