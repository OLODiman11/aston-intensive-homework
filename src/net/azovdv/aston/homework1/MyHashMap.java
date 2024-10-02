package net.azovdv.aston.homework1;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class MyHashMap<K, V> {
    private final static int DEFAULT_INIT_SIZE = 10;
    private final static double EXPANSION_COEFFICIENT = 1.5;
    private final static double LOAD_FACTOR = 0.75;

    private LinkedList<Pair<K, V>>[] array;
    private int size;
    private int capacity = DEFAULT_INIT_SIZE;
    private final Set<K> keySet;

    public MyHashMap() {
        this.array = createNewArray(this.capacity);
        this.keySet = new HashSet<>();
    }

    public void clear() {
        size = 0;
        array = createNewArray(this.capacity);
        keySet.clear();
    }

    public V remove(K key) {
        if(!keySet.contains(key)) return null;
        LinkedList<Pair<K, V>> cell = array[key.hashCode() % array.length];
        int index = 0;
        for(Pair<K, V> pair: cell) {
            if(pair.key().equals(key))
                break;
            index++;
        }
        Pair<K, V> pair = cell.remove(index);
        if(cell.isEmpty())
            array[key.hashCode() % array.length] = null;
        return pair.value();
    }

    public V get(K key) {
        if(!keySet.contains(key)) return null;

        int index = key.hashCode() % array.length;
        LinkedList<Pair<K, V>> cell = array[index];
        if(cell.size() == 1) return cell.getFirst().value();

        for(Pair<K, V> pair: cell) {
            if(pair.key().equals(key))
                return pair.value();
        }

        throw new RuntimeException("Key is in the keySet but not in the map");
    }

    public V put(K key, V value) {
        V oldValue = get(key);

        if(array[key.hashCode() % array.length] != null) expandIfNecessary();
        putInto(key, value, array);

        keySet.add(key);

        return oldValue;
    }

    public boolean containsKey(K key) {
        return keySet.contains(key);
    }

    public boolean containsValue(V value) {
        for(Pair<K, V> pair: getIterable()) {
            if(pair.value().equals(value))
                return true;
        }
        return false;
    }

    public Set<Pair<K, V>> entrySet() {
        Set<Pair<K, V>> set = new HashSet<>();
        for(Pair<K, V> pair: getIterable()) {
            set.add(pair);
        }
        return set;
    }

    public Set<K> keySet() {
        return keySet;
    }

    private void expandIfNecessary(){
        long newSize = (long) size + 1;
        if(newSize > Integer.MAX_VALUE)
            throw new RuntimeException("Map capacity exceeded Integer.MAX_VALUE");
        float current_load = (float) newSize / capacity;
        if(current_load < LOAD_FACTOR) return;

        capacity = getNewCapacity((int) newSize);

        LinkedList<Pair<K, V>>[] newArray = createNewArray(capacity);
        for(Pair<K, V> pair: getIterable()) {
            putInto(pair.key(), pair.value(), newArray);
        }

        array = newArray;
    }

    private int getNewCapacity(int newSize) {
        long newCapacity = capacity;
        while(newCapacity < newSize){
            newCapacity = (long) (EXPANSION_COEFFICIENT * newCapacity + 1);
            if(newCapacity <= capacity)
                throw new RuntimeException("Capacity has not increased.");
            if(newCapacity > Integer.MAX_VALUE){
                newCapacity = Integer.MAX_VALUE;
                break;
            }
        }
        return (int) newCapacity;
    }

    private void putInto(K key, V value, LinkedList<Pair<K, V>>[] array) {
        int index = key.hashCode() % array.length;
        if(array[index] == null) {
            array[index] = new LinkedList<>();
            size++;
        }
        array[index].add(new Pair<>(key, value));
    }

    @SuppressWarnings("unchecked")
    private LinkedList<Pair<K, V>>[] createNewArray(int capacity) {
        return (LinkedList<Pair<K, V>>[]) new LinkedList[capacity];
    }

    private Iterable<Pair<K, V>> getIterable() {
        return () -> new Iterator<>() {
            private int curIndex = 0;
            private Iterator<Pair<K, V>> cellIterator = array[curIndex].iterator();

            @Override
            public boolean hasNext() {
                if (cellIterator != null && cellIterator.hasNext()) return true;
                if (getNextIndex() < array.length) return true;
                return false;
            }

            @Override
            public Pair<K, V> next() {
                if (!hasNext()) return null;
                if (!cellIterator.hasNext()) {
                    curIndex = getNextIndex();
                    cellIterator = array[curIndex].iterator();
                }
                return cellIterator.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            private int getNextIndex() {
                for(int i = curIndex + 1; i < array.length; i++) {
                    if(array[i] != null) return i;
                }
                return array.length;
            }
        };
    }
}
