package deque;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class LinkedListDeque<T> implements Iterable<T> {


    public static class Node<T> {
        T item;
        Node<T> prev;
        Node<T> next;
        Node(T item, Node<T> prev, Node<T> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    private final Node<T> sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node<>(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public void addFirst(T item) {
        Node<T> first = new Node<>(item, sentinel, sentinel.next);
        sentinel.next.prev = first;
        sentinel.next = first;
        size++;
    }

    public void addLast(T item) {
        Node<T> last = new Node<>(item, sentinel.prev, sentinel);
        sentinel.prev.next = last;
        sentinel.prev = last;
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node<T> p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item);
            if (p.next != sentinel) System.out.print(" ");
            p = p.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        if (isEmpty()) return null;
        Node<T> first = sentinel.next;
        sentinel.next = first.next;
        sentinel.next.prev = sentinel;
        size--;
        return first.item;
    }

    public T removeLast() {
        if (isEmpty()) return null;
        Node<T> last = sentinel.prev;
        sentinel.prev = last.prev;
        sentinel.prev.next = sentinel;
        size--;
        return last.item;
    }

    public T get(int index) {
        if (index < 0 || index >= size) return null;
        Node<T> p = sentinel.next;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return p.item;
    }

    public T getRecursive(int index) {
        if (index < 0 || index >= size) return null;
        return getRecursiveHelper(sentinel.next, index);
    }

    private T getRecursiveHelper(Node<T> node, int index) {
        if (index == 0) return node.item;
        return getRecursiveHelper(node.next, index - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinkedListDeque<?>)) return false;
        LinkedListDeque<?> other = (LinkedListDeque<?>) o;
        if (this.size != other.size) return false;

        Iterator<T> it1 = this.iterator();
        Iterator<?> it2 = other.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            T item1 = it1.next();
            Object item2 = it2.next();

            if (!(item1 == null ? item2 == null : item1.equals(item2))) return false;
        }
        return !(it1.hasNext() || it2.hasNext());
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private Node<T> current = sentinel.next;
        @Override
        public boolean hasNext() {
            return current != sentinel;
        }
        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            T val = current.item;
            current = current.next;
            return val;
        }

    }
}
