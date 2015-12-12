package datastructures;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class HashTable<K,V> {

    private static int defaultCapacity = 5;
    private static double loadTolerance = 0.67;

    private int initialCapacity;
    private int count;
    private LinkedList<TableEntry<K,V>>[] table;

    public HashTable(){
        this(defaultCapacity);
    }

    public HashTable(int initialCapacity){
        this.initialCapacity = initialCapacity;
        this.count = 0;
        table = new LinkedList[initialCapacity];

    }

    private void resize(){
        count = 0;
        LinkedList<TableEntry<K,V>>[] oldTable = table;
        table = new LinkedList[1 + oldTable.length * 2];

        for(LinkedList<TableEntry<K,V>> list : oldTable)
            if(list != null)
                for(TableEntry<K,V> entry : list)
                    put(entry.getKey(),entry.getValue());
    }

    private V put(TableEntry<K,V> entry){
        return put(entry.getKey(),entry.getValue());
    }

    public V put(K key, V value) throws NullPointerException{

        if(key == null || value == null)
            throw new NullPointerException("Cannot accept null keys or values");

        V previousValue = null;

        if(loadFactor() > loadTolerance) resize();

        int k = hashKey(key);
        if(table[k] == null){
            table[k] = new LinkedList<TableEntry<K,V>>();
        }

        LinkedList<TableEntry<K,V>> list = table[k];

        for(TableEntry<K,V> entry : list) {
            if (entry.getKey() == key) {
                previousValue = entry.getValue();
                entry.setValue(value);
                return previousValue;
            }
        }

        list.add(new TableEntry<K, V>(key, value));
        count++;

        return null;
    }

    public boolean containsKey(K key){
        return get(key) != null;
    }

    public boolean updateValue(K key, V value) throws NoSuchElementException{
        TableEntry<K,V> entry = removeEntry(key);

        if(entry == null) throw new NoSuchElementException("Key not found in table");

        entry.setValue(value);
        put(entry);
        return true;
    }

    private TableEntry<K,V> getEntry(K key){
        int k = hashKey(key);
        LinkedList<TableEntry<K,V>> list = table[k];

        if(list == null) return null;

        for(TableEntry<K,V> entry : list) {
            if (entry.getKey().equals(key)) {
                return entry;
            }
        }

        return null;
    }

    private TableEntry<K,V> removeEntry(K key){
        TableEntry<K,V> removeMe = null;
        int k = hashKey(key);
        LinkedList<TableEntry<K,V>> list = table[k];

        if(list == null) return null;

        for(TableEntry<K,V> entry : list) {
            if (entry.getKey().equals(key)) {
                removeMe = entry;
                count--;
                break;
            }
        }

        if(removeMe != null) list.remove(removeMe);

        return removeMe;

    }

    public V get(K key){
        TableEntry<K,V> entry = getEntry(key);
        return entry != null ? entry.getValue() : null;
    }

    public V remove(K key){
        TableEntry<K,V> entry = removeEntry(key);
        return entry != null ? entry.getValue() : null;
    }

    public int size(){
        return count;
    }

    public int capacity(){
        return table.length;
    }

    public double loadFactor(){
        return ((double) count) / ((double) table.length);
    }


    private int hashKey(K key){
        return Math.abs(key.hashCode()) % table.length;
    }

    private class TableEntry<K,V>{
        private K key;
        private V value;

        public TableEntry(K k, V v){
            key = k;
            value = v;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public String toString(){
            return "(" + key + "," + value + ")";
        }
    }


    public static void main(String[] args) {

    }


}
