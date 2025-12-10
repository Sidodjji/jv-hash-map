package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int size;
    private int capacity = DEFAULT_CAPACITY;
    private int threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }

        int hash = hash(key);
        int index = hash & (capacity - 1);

        Node<K, V> node = table[index];

        if (node == null) {
            table[index] = new Node<>(hash, key, value, null);
            size++;
            return;
        }

        // ищем по цепочке
        Node<K, V> current = node;
        while (current != null) {
            if (current.getHash() == hash && Objects.equals(current.getKey(), key)) {
                current.setValue(value);
                return;
            }
            if (current.getNext() == null) {
                break;
            }
            current = current.getNext();
        }

        current.setNext(new Node<>(hash, key, value, null));
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = hash & (capacity - 1);

        Node<K, V> node = table[index];

        while (node != null) {
            if (node.getHash() == hash && Objects.equals(node.getKey(), key)) {
                return node.getValue();
            }
            node = node.getNext();
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        final int oldCapacity = capacity;
        Node<K, V>[] oldTable = table;

        capacity *= 2;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        table = new Node[capacity];
        size = 0;

        for (int i = 0; i < oldCapacity; i++) {
            Node<K, V> node = oldTable[i];
            while (node != null) {
                put(node.getKey(), node.getValue());
                node = node.getNext();
            }
        }
    }

    private int hash(Object key) {
        int h = (key == null) ? 0 : key.hashCode();
        return h ^ (h >>> 16);
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public int getHash() {
            return hash;
        }

        public void setHash(int hash) {
            this.hash = hash;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }
}
