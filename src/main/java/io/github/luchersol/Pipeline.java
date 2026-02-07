package io.github.luchersol;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Pipeline<T, R> {
    private final List<Step<?, ?>> steps = new ArrayList<>();

    private Pipeline() { }

    // Step genérico: T -> R
    private static class Step<I, O> {
        final Function<? super I, ? extends O> mapper;
        final Supplier<? extends O> fallback;
        final Predicate<? super O> condition;

        Step(Function<? super I, ? extends O> mapper, Supplier<? extends O> fallback, Predicate<? super O> condition) {
            this.mapper = mapper;
            this.fallback = fallback;
            this.condition = condition;
        }

        @SuppressWarnings("unchecked")
        O apply(Object input) {
            O result;
            try {
                result = ((Function<Object, O>) mapper).apply(input);
                if (condition != null && !condition.test(result)) {
                    result = fallback.get();
                }
            } catch (Exception e) {
                if (fallback != null) {
                    result = fallback.get();
                } else {
                    throw new RuntimeException(e);
                }
            }
            return result;
        }
    }

    // Crear pipeline vacío
    public static <T> Pipeline<T, T> empty() {
        return new Pipeline<>();
    }

    // Añadir un paso normal: T -> R
    public <V> Pipeline<T, V> add(Function<? super R, ? extends V> mapper) {
        Pipeline<T, V> next = new Pipeline<>();
        next.steps.addAll(this.steps);
        next.steps.add(new Step<>(mapper, null, null));
        return next;
    }

    // Añadir un paso con alternativa externa
    public <V> Pipeline<T, V> addOrElse(Function<? super R, ? extends V> mapper, Supplier<? extends V> fallback) {
        Pipeline<T, V> next = new Pipeline<>();
        next.steps.addAll(this.steps);
        next.steps.add(new Step<R,V>(mapper, fallback, null));
        return next;
    }

    // Añadir un paso con condición y fallback
    public <V> Pipeline<T, V> addSwitch(Function<? super R, ? extends V> mapper, Predicate<? super V> condition, Supplier<? extends V> fallback) {
        Pipeline<T, V> next = new Pipeline<>();
        next.steps.addAll(this.steps);
        next.steps.add(new Step<R,V>(mapper, fallback, condition));
        return next;
    }

    // Aplicar pipeline a un objeto
    @SuppressWarnings("unchecked")
    public R apply(T input) {
        Object current = input;
        for (Step<?, ?> step : steps) {
            current = step.apply(current);
        }
        return (R) current;
    }
}
