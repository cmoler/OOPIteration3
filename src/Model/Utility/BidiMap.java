package Model.Utility;

import java.util.HashMap;
import java.util.Set;

public class BidiMap<K, V> extends HashMap {
    private HashMap<K, V> standardMap = new HashMap<K,V>();
    private HashMap<V, K> reverseMap = new HashMap<V,K>();

    public void place(K key,V value) {
        standardMap.put(key, value);
        reverseMap.put(value,key);
    }

    public void removeByValue(V value){
        standardMap.remove(reverseMap.remove(value));
    }

    public void removeByKey(K key){
        reverseMap.remove(standardMap.remove(key));
    }

    public V getValueFromKey(K key){
        return standardMap.get(key);
    }

    public K getKeyFromValue(V value){
        return reverseMap.get(value);
    }

    public boolean hasKey(K key){
        return standardMap.containsKey(key);
    }

    public boolean hasValue(V value){
        return standardMap.containsValue(value);
    }

    public void replacePair(K key, V newValue){
        reverseMap.remove(standardMap.get(key));
        standardMap.replace(key,newValue);
        reverseMap.put(newValue,key);
    }

    public Set<K> getKeyList(){
        return standardMap.keySet();
    }

    public Set<V> getValueList(){
        return reverseMap.keySet();
    }

    public void clear(){
        standardMap.clear();
        reverseMap.clear();
    }
}
