package functional;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class FUtils {

	public static <T> Optional<T> first(Iterable<T> lst) {
		for(T t: lst) {
			return Optional.of(t);
		}
		return Optional.empty();
	}

	public static <T> Optional<T> firstThat(Iterable<T> lst, Predicate<T> predicate) {
		for(T t: lst) {
			if (predicate.test(t)) {
				return Optional.of(t);
			}
		}
		return Optional.empty();
	}

	public static <S, T> Optional<T> castOptional(Optional<S> opt) {
		return (Optional<T>)opt.map((t) -> (T)t);
	}

	public static RangeStream range(long len) {
		return new RangeStream(len);
	}

	public static abstract class Stream<T> {
		public abstract Optional<T> next();

		public Stream<T> filter(Predicate<T> predicate) {
			return new FilterStream(this, predicate);
		}

		public <S> Stream<S> map(Function<T, S> fn) {
			return new MapStream(this, fn);
		}

		public void forEach(Consumer<T> fn) {
			Optional<T> nextVal = next();
			while(nextVal.isPresent()) {
				fn.accept(nextVal.get());
				nextVal = next();
			}
		}

		public List<T> toList() {
			return Lists.newArrayList(toIterator());
		}

		public Iterator<T> toIterator() {
			return new StreamIterator(this);
		}

		public <S> S reduce(BiFunction<S, T, S> reduceFun, S zero) {
			S currAgg = zero;
			Optional<T> currVal;

			while((currVal = next()).isPresent()) {
				currAgg = reduceFun.apply(currAgg, currVal.get());
			}

			return currAgg;
		}
	}

	public static class RangeStream extends Stream<Long> {
		long current;
		long limit;

		public RangeStream(long limit) {
			this.current = 0;
			this.limit = limit;
		}


		@Override
		public Optional<Long> next() {
			if (current < limit) {
				current += 1;
				return Optional.of(current);
			}
			else {
				return Optional.empty();
			}
		}
	}

	public static class StreamIterator<T> implements Iterator<T> {

		private Stream<T> st;
		private Optional<T> nextVal;

		public StreamIterator(Stream<T> st) {
			this.st = st;
			this.nextVal = st.next();
		}

		@Override
		public boolean hasNext() {
			return nextVal.isPresent();
		}

		@Override
		public T next() {
			T ret = nextVal.get();
			nextVal = st.next();
			return ret;
		}
	}

	public static class MapStream<S, T> extends Stream<T> {
		private Stream<S> st;
		private Function<S, T> fn;

		MapStream(Stream st, Function<S, T> fn) {
			this.st = st;
			this.fn = fn;
		}

		@Override
		public Optional<T> next() {
			return st.next().map(fn);
		}
	}

	public static class FilterStream<T> extends Stream<T> {

		private Stream<T> st;
		private Predicate<T> predicate;

		FilterStream(Stream st, Predicate<T> predicate) {
			this.st = st;
			this.predicate = predicate;
		}

		@Override
		public Optional<T> next() {
			Optional<T> nextVal = st.next();
			while(nextVal.isPresent() && ! predicate.test(nextVal.get())) {
				nextVal = st.next();
			}
			return nextVal;
		}
	}

	public static class IteratorStream<T> extends Stream<T> {
		Iterator<T> it;

		public IteratorStream(Iterator<T> it) {
			this.it = it;
		}

		public Optional<T> next() {
			if (it.hasNext()) {
				return Optional.of(it.next());
			}
			else {
				return Optional.empty();
			}
		}
	}

	public static <T> Stream<T> asStream(Iterator<T> it) {
		return new IteratorStream(it);
	}

	public static <T> Stream<T> asStream(Iterable<T> it) {
		return new IteratorStream(it.iterator());
	}

	public static <T> Stream<T> filter(Iterator<T> it, Predicate<T> predicate) {
		return asStream(it).filter(predicate);
	}

	public static <T> Stream<T> filter(Iterable<T> it, Predicate<T> predicate) {
		return asStream(it).filter(predicate);
	}

	public static boolean instanceOf(Object obj, Class<?> cls) {
		return obj != null && cls.isAssignableFrom(obj.getClass());
	}

}
