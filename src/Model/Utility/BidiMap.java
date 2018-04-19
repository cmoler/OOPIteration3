package Model.Utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class BidiMap<K, V> {
    private HashMap<K, V> keyToElementMap = new HashMap<K,V>();
    private HashMap<V, K> elementToKeyMap = new HashMap<V,K>();

    public void place(K key,V value) {
        keyToElementMap.put(key, value);
        elementToKeyMap.put(value, key);
    }

    public void removeByValue(V value){
        keyToElementMap.remove(elementToKeyMap.remove(value));
    }

    public void removeByKey(K key){
        elementToKeyMap.remove(keyToElementMap.remove(key));
    }

    public V getValueFromKey(K key){
        return keyToElementMap.get(key);
    }

    public K getKeyFromValue(V value){
        return elementToKeyMap.get(value);
    }

    public boolean hasKey(K key){
        return keyToElementMap.containsKey(key);
    }

    public boolean hasValue(V value){
        return keyToElementMap.containsValue(value);
    }

    public void replacePair(K key, V newValue){
        elementToKeyMap.remove(keyToElementMap.get(key));
        keyToElementMap.replace(key,newValue);
        elementToKeyMap.put(newValue,key);
    }

    public Set<K> getKeyList(){
        return keyToElementMap.keySet();
    }

    public Set<V> getValueList(){
        return elementToKeyMap.keySet();
    }

    public Set<Map.Entry<K,V>> entrySet () {
        return keyToElementMap.entrySet();
    }

    public void clear(){
        keyToElementMap.clear();
        elementToKeyMap.clear();
    }

    public boolean isEmpty() {
        return keyToElementMap.isEmpty() && elementToKeyMap.isEmpty();
    }
}
