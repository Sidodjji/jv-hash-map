package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int size;
    private int capacity = 16;
    private final float LOAD_FACTOR = 0.75f;
    private int threshold = (int)(capacity * LOAD_FACTOR);
    private Node<K,V>[] table = new Node[capacity];

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }

        int hash = hash(key);
        int index = hash & (capacity - 1);

        Node<K,V> node = table[index];

        if (node == null) {
            table[index] = new Node<>(hash, key, value, null);
            size++;
            return;
        }

        Node<K,V> current = node;
        while (current != null) {
            if (current.hash == hash && Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            if (current.next == null) break;
            current = current.next;
        }

        current.next = new Node<>(hash, key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = hash & (capacity - 1);

        Node<K,V> node = table[index];
        while (node != null) {
            if (node.hash == hash && Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int oldCapacity = capacity;
        Node<K,V>[] oldTable = table;

        capacity *= 2;
        threshold = (int)(capacity * LOAD_FACTOR);
        table = new Node[capacity];

        size = 0;

        for (int i = 0; i < oldCapacity; i++) {
            Node<K,V> node = oldTable[i];
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hash(Object key) {
        int h = (key == null) ? 0 : key.hashCode();
        return h ^ (h >>> 16);
    }

    static class Node<K,V> {
        int hash;
        K key;
        V value;
        Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
