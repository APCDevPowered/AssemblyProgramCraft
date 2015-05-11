package yuxuanchiadm.apc.util.collection;

public class SingleEntry<K,V>
{
    private K key;
    private V value;
    
    public SingleEntry()
    {
        
    }
    public SingleEntry(K key, V value)
    {
        this.key = key;
        this.value = value;
    }
    
    public K getKey()
    {
        return key;
    }
    public K setKey(K key)
    {
        return this.key = key;
    }
    public V getValue()
    {
        return value;
    }
    public V setValue(V value)
    {
        return this.value = value;
    }
}
