package io.github.luchersol.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.github.luchersol.Checker;

public abstract class AbstractList2<E> implements List<E> {
    
    protected List<E> inner;

    // ----------------- Inner -----------------

    public List<E> toList() {
        return inner;
    }

    public List<E> toUnmodifiableList() {
        return Collections.unmodifiableList(inner);
    }

    // ----------------- Gets with default or optional values -----------------

    public E getOrElse(int index, E defaultValue) {
        return index >= size() ? defaultValue : get(index);
    }

    public E getFirstOrElse(int index, E defaultValue) {
        return isEmpty() ? defaultValue : getFirst();
    }

    public E getLastOrElse(int index, E defaultValue) {
        return isEmpty() ? defaultValue : getLast();
    }

    public Optional<E> find(int index) {
        return index >= size() ? Optional.empty() : Optional.ofNullable(get(index));
    }

    public Optional<E> findFirst() {
        return isEmpty() ? Optional.empty() : Optional.ofNullable(getFirst());
    }

    public Optional<E> findLast() {
        return isEmpty() ? Optional.empty() : Optional.ofNullable(getLast());
    }

    // ----------------- Checkers -----------------

    public boolean containsAny(Collection<? super E> c) {
        Set<? super E> set = new HashSet<>(c);
        return inner.stream().anyMatch(set::contains);
    }
    
    public boolean isSorted(Comparator<? super E> cmp) {
		return IntStream.range(0, size() - 1).allMatch(i-> cmp.compare(get(i),get(i+1)) <= 0);
	}
    
    // ----------------- Count and statistics -----------------

    public long count(Predicate<E> predicate) {
        long count = 0L;
        for(E element: inner)
            if(predicate.test(element)) count++;
        return count;
    }

    public Optional<E> findMax(Comparator<E> comparator) {
        return this.stream().max(comparator);
    }

    public E max(Comparator<E> comparator) {
        return findMax(comparator).get();
    }

    public E maxOrElse(Comparator<E> comparator, E defaultValue) {
        return findMax(comparator).orElse(defaultValue);
    }

    public Optional<E> findMin(Comparator<E> comparator) {
        return stream().min(comparator);
    }

    public E min(Comparator<E> comparator) {
        return findMin(comparator).get();
    }

    public E minOrElse(Comparator<E> comparator, E defaultValue) {
        return findMin(comparator).orElse(defaultValue);
    }
    
    // ----------------- Combination and extraction -----------------

    public <C1 extends Collection<C2>, C2 extends Collection<E>> C1 split(int chunkSize, Supplier<C1> outerSupplier, Supplier<C2> innerSupplier) {

        Checker.check(chunkSize > 0, "chunkSize must be > 0");

        C1 outer = outerSupplier.get();
        C2 current = null;

        int count = 0;

        for (E element : inner) {
            if (count % chunkSize == 0) {
                current = innerSupplier.get();
                outer.add(current);
            }
            current.add(element);
            count++;
        }

        return outer;
    }

    // ----------------- Advanced transformations -----------------

    public <K> Map<K, List<E>> groupingBy(Function<E, K> classifier) {
        return stream().collect(Collectors.groupingBy(classifier));
    }

    public <K, V> Map<K, V> toMap(Function<? super E, ? extends K> keyMapper, Function<? super E, ? extends V> valueMapper) {
        return stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    public <K, V> Map<K, V> toMap(Function<? super E, ? extends K> keyMapper, Function<? super E, ? extends V> valueMapper, BinaryOperator<V> mergeFunction) {
        return stream().collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction));
    }

    public <K, V, M extends Map<K,V>> M toMap(Function<E, K> keyMapper, Function<? super E, ? extends V> valueMapper, BinaryOperator<V> mergeFunction, Supplier<M> mapFactory) {
        return stream().collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction, mapFactory));
    }

    // ----------------- toString ----------------- 

	public String toString(String sep, String prefix, String suffix) {
		return inner.stream()
				.map(Object::toString)
				.collect(Collectors.joining(sep,prefix,suffix));			
	}

	public String toString(String prefix, String suffix) {
		return toString("\n", prefix, suffix);	
	}
    
    @Override
    public String toString() {
        return toString("", "");		
	}
        
    // ----------------- Default List Methods -----------------
    
    @Override
    public int size() {
        return inner.size();
    }

    @Override
    public boolean isEmpty() {
        return inner.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return inner.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return inner.iterator();
    }

    @Override
    public Object[] toArray() {
        return inner.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return inner.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return inner.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return inner.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return inner.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return inner.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return inner.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return inner.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return inner.retainAll(c);
    }

    @Override
    public void clear() {
        inner.clear();
    }

    @Override
    public E get(int index) {
        return inner.get(index);
    }

    @Override
    public E set(int index, E element) {
        return inner.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        inner.add(index, element);
    }

    @Override
    public E remove(int index) {
        return inner.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return inner.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return inner.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return inner.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return inner.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return inner.subList(fromIndex, toIndex);
    }
}
