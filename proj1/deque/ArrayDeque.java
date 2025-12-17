package deque;


import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private T[]  items;
    private int size;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
    }

    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int current = (nextFirst + 1) % items.length;
        for (int i = 0; i < size; i++) {
            a[i] = items[current];
            current = (current + 1) % items.length;
        }
        items = a;
        nextFirst = items.length - 1;
        nextLast = size;
    }

    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFirst] = item;
        nextFirst = (nextFirst - 1 + items.length) % items.length;
        size++;
    }

    @Override
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = item;
        nextLast = (nextLast + 1) % items.length;
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        int current = (nextFirst + 1) % items.length;
        for (int i = 0; i < size; i++) {
            System.out.print(items[current]);
            if (i != size - 1) {
                System.out.print(" ");
            }
            current = (current + 1) % items.length;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        // size - 1 is the size *after* removal.
        if (items.length >= 16 && (size - 1) < items.length / 4) {
            resize(items.length / 2);
        }
        int firstIndex = (nextFirst + 1) % items.length;
        T item = items[firstIndex];
        items[firstIndex] = null;
        nextFirst = firstIndex;
        size--;
        return item;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        // size - 1 is the size *after* removal.
        if (items.length >= 16 && (size - 1) < items.length / 4) {
            resize(items.length / 2);
        }
        int lastIndex = (nextLast - 1 + items.length) % items.length;
        T item = items[lastIndex];
        items[lastIndex] = null;
        nextLast = lastIndex;
        size--;
        return item;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        int actualIndex = (nextFirst + 1 + index) % items.length;
        return items[actualIndex];
    }

    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int wizPos;

        public ArrayDequeIterator() {
            wizPos = 0;
        }

        @Override
        public boolean hasNext() {
            return wizPos < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T item = get(wizPos);
            wizPos += 1;
            return item;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof ArrayDeque)) {
            return false;
        }
        ArrayDeque<?> other = (ArrayDeque<?>) o;
        if (this.size != other.size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!this.get(i).equals(other.get(i))) {
                return false;
            }
        }
        return true;
    }
}
