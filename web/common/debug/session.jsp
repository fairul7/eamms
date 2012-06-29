<%@page import="java.io.Serializable"%>
<%@page import="java.io.IOException"%>
<%@page import="java.util.*"%>
<%@page import="java.lang.reflect.*"%>
<%@page import="java.security.*"%>
<%@page import="kacang.ui.Widget"%>
<%@page import="kacang.ui.WidgetManager"%>
<%@page import="kacang.stdui.Page"%>

<html>
    <head>
        <style>
            body { font-family: Arial; font-size: 9pt }
        </style>
    </head>
    <body>
        
<%
WidgetManager wm = WidgetManager.getWidgetManager(request);
out.println("Total WidgetManager size: <b>" + calcObjectSize(wm) + "</b>");
out.println("<ul>");
Map pageMap = wm.getPageMap();
for (Iterator i=pageMap.keySet().iterator(); i.hasNext();) {
    String url = (String)i.next();
    String pageName = (String)pageMap.get(url);
    Page pg = (Page)wm.getWidget(pageName);
    clearWidgetManager(pg);
    traverseWidget(pg, out);
}
out.println("</ul>");
%>

    </body>
</html>

<%!
ObjectProfiler objectProfiler = new ObjectProfiler();

public void clearWidgetManager(Widget widget) throws IOException {
    widget.setWidgetManager(null);
    for (Iterator i=widget.getChildren().iterator(); i.hasNext();) {
        Widget child = (Widget)i.next();
        clearWidgetManager(child);
    }
}

public void traverseWidget(Widget widget, JspWriter out) throws IOException {
    WidgetManager wm = WidgetManager.getWidgetManager();
    Widget parent = (Widget)widget.getParent();
    try {
        widget.setWidgetManager(null);
        widget.setParent(null);
        out.println("<ul>");
        out.println("<li>" + widget.getClass() + " " + widget.getAbsoluteName() + ": <b>" + calcObjectSize(widget) + "</b></li>");
        for (Iterator i=widget.getChildren().iterator(); i.hasNext();) {
            Widget child = (Widget)i.next();
            traverseWidget(child, out);
        }
        out.println("</ul>");
    } finally {
        widget.setParent(parent);
        widget.setWidgetManager(wm);
    }
}


public String calcObjectSize(Object object) {
    String result;
    long value = objectProfiler.sizeof(object);
    double dValue = value;
    if (value > 1048576) {
        dValue = value / 1048576;
        result = dValue + "M";
    } else if (value > 1024) {
        dValue = value / 1024;
        result = dValue + "K";
    } else {
        result = value + "bytes";
    }
    return result;
}

%>
<%!
public final class IdentityHashMap extends AbstractMap implements Map,
        java.io.Serializable, Cloneable {
    /**
* The initial capacity used by the no-args constructor.
* MUST be a power of two.  The value 32 corresponds to the
* (specified) expected maximum size of 21, given a load factor
* of 2/3.
    */
    private static final int DEFAULT_CAPACITY = 32;
    
    /**
* The minimum capacity, used if a lower value is implicitly specified
* by either of the constructors with arguments.  The value 4 corresponds
* to an expected maximum size of 2, given a load factor of 2/3.
* MUST be a power of two.
    */
    private static final int MINIMUM_CAPACITY = 4;
    
    /**
* The maximum capacity, used if a higher value is implicitly specified
* by either of the constructors with arguments.
* MUST be a power of two <= 1<<29.
    */
    private static final int MAXIMUM_CAPACITY = 1 << 29;
    
    /**
* The table, resized as necessary. Length MUST always be a power of two.
    */
    protected transient Object[] table;
    
    /**
* The number of key-value mappings contained in this identity hash map.
*
* @serial
    */
    protected int size;
    
    /**
* The number of modifications, to support fast-fail iterators
    */
    protected transient volatile int modCount;
    
    /**
* The next size value at which to resize (capacity * load factor).
    */
    private transient int threshold;
    
    /**
* Value representing null keys inside tables.
    */
    private final Object NULL_KEY = new Object();
    
    /**
* Use NULL_KEY for key if it is null.
    */
    
    private Object maskNull(Object key) {
        return (key == null ? NULL_KEY : key);
    }
    
    /**
* Return internal representation of null key back to caller as null
    */
    protected Object unmaskNull(Object key) {
        return (key == NULL_KEY ? null : key);
    }
    
    /**
* Constructs a new, empty identity hash map with a default expected
* maximum size (21).
    */
    public IdentityHashMap() {
        init(DEFAULT_CAPACITY);
    }
    
    /**
* Constructs a new, empty map with the specified expected maximum size.
* Putting more than the expected number of key-value mappings into
* the map may cause the internal data structure to grow, which may be
* somewhat time-consuming.
*
* @param expectedMaxSize the expected maximum size of the map.
* @throws IllegalArgumentException if <tt>expectedMaxSize</tt> is negative
    */
    public IdentityHashMap(int expectedMaxSize) {
        if (expectedMaxSize < 0)
            throw new IllegalArgumentException("expectedMaxSize is negative: "
                    + expectedMaxSize);
        init(capacity(expectedMaxSize));
    }
    
    /**
* Returns the appropriate capacity for the specified expected maximum
* size.  Returns the smallest power of two between MINIMUM_CAPACITY
* and MAXIMUM_CAPACITY, inclusive, that is greater than
* (3 * expectedMaxSize)/2, if such a number exists.  Otherwise
* returns MAXIMUM_CAPACITY.  If (3 * expectedMaxSize)/2 is negative, it
* is assumed that overflow has occurred, and MAXIMUM_CAPACITY is returned.
    */
    private int capacity(int expectedMaxSize) {
// Compute min capacity for expectedMaxSize given a load factor of 2/3
        int minCapacity = (3 * expectedMaxSize)/2;
        
// Compute the appropriate capacity
        int result;
        if (minCapacity > MAXIMUM_CAPACITY || minCapacity < 0) {
            result = MAXIMUM_CAPACITY;
        } else {
            result = MINIMUM_CAPACITY;
            while (result < minCapacity)
                result <<= 1;
        }
        return result;
    }
    
    /**
* Initialize object to be an empty map with the specified initial
* capacity, which is assumed to be a power of two between
* MINIMUM_CAPACITY and MAXIMUM_CAPACITY inclusive.
    */
    private void init(int initCapacity) {
// assert (initCapacity & -initCapacity) == initCapacity; // power of 2
// assert initCapacity >= MINIMUM_CAPACITY;
// assert initCapacity <= MAXIMUM_CAPACITY;
        
        threshold = (initCapacity * 2)/3;
        table = new Object[2 * initCapacity];
    }
    
    /**
* Constructs a new identity hash map containing the keys-value mappings
* in the specified map.
*
* @param m the map whose mappings are to be placed into this map.
* @throws NullPointerException if the specified map is null.
    */
    public IdentityHashMap(Map m) {
// Allow for a bit of growth
        this((int) ((1 + m.size()) * 1.1));
        putAll(m);
    }
    
    /**
* Returns the number of key-value mappings in this identity hash map.
*
* @return the number of key-value mappings in this map.
    */
    public int size() {
        return size;
    }
    
    /**
* Returns <tt>true</tt> if this identity hash map contains no key-value
* mappings.
*
* @return <tt>true</tt> if this identity hash map contains no key-value
*         mappings.
    */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
* Return index for Object x.
    */
    protected int hash(Object x, int length) {
        int h = System.identityHashCode(x);
// Multiply by -127, and left-shift to use least bit as part of hash
        return ((h << 1) - (h << 8)) & (length - 1);
    }
    
    /**
* Circularly traverse table of size len.
    **/
    protected int nextKeyIndex(int i, int len) {
        return (i + 2 < len ? i + 2 : 0);
    }
    
    /**
* Returns the value to which the specified key is mapped in this identity
* hash map, or <tt>null</tt> if the map contains no mapping for
* this key.  A return value of <tt>null</tt> does not <i>necessarily</i>
* indicate that the map contains no mapping for the key; it is also
* possible that the map explicitly maps the key to <tt>null</tt>. The
* <tt>containsKey</tt> method may be used to distinguish these two
* cases.
*
* @param   key the key whose associated value is to be returned.
* @return  the value to which this map maps the specified key, or
*          <tt>null</tt> if the map contains no mapping for this key.
* @see #put(Object, Object)
    */
    public Object get(Object key) {
        Object k = maskNull(key);
        Object[] tab = table;
        int len = tab.length;
        int i = hash(k, len);
        while (true) {
            Object item = tab[i];
            if (item == k)
                return tab[i + 1];
            if (item == null)
                return item;
            i = nextKeyIndex(i, len);
        }
    }
    
    /**
* Tests whether the specified object reference is a key in this identity
* hash map.
*
* @param   key   possible key.
* @return  <code>true</code> if the specified object reference is a key
*          in this map.
* @see     #containsValue(Object)
    */
    public boolean containsKey(Object key) {
        Object k = maskNull(key);
        Object[] tab = table;
        int len = tab.length;
        int i = hash(k, len);
        while (true) {
            Object item = tab[i];
            if (item == k)
                return true;
            if (item == null)
                return false;
            i = nextKeyIndex(i, len);
        }
    }
    
    /**
* Tests whether the specified object reference is a value in this identity
* hash map.
*
* @param value value whose presence in this map is to be tested.
* @return <tt>true</tt> if this map maps one or more keys to the
*         specified object reference.
* @see     #containsKey(Object)
    */
    public boolean containsValue(Object value) {
        Object[] tab = table;
        for (int i = 1; i < tab.length; i+= 2)
            if (tab[i] == value)
                return true;
        
        return false;
    }
    
    /**
* Tests if the specified key-value mapping is in the map.
*
* @param   key   possible key.
* @param   value possible value.
* @return  <code>true</code> if and only if the specified key-value
*          mapping is in map.
    */
    protected boolean containsMapping(Object key, Object value) {
        Object k = maskNull(key);
        Object[] tab = table;
        int len = tab.length;
        int i = hash(k, len);
        while (true) {
            Object item = tab[i];
            if (item == k)
                return tab[i + 1] == value;
            if (item == null)
                return false;
            i = nextKeyIndex(i, len);
        }
    }
    
    /**
* Associates the specified value with the specified key in this identity
* hash map.  If the map previously contained a mapping for this key, the
* old value is replaced.
*
* @param key the key with which the specified value is to be associated.
* @param value the value to be associated with the specified key.
* @return the previous value associated with <tt>key</tt>, or
*	       <tt>null</tt> if there was no mapping for <tt>key</tt>.  (A
*         <tt>null</tt> return can also indicate that the map previously
*         associated <tt>null</tt> with the specified key.)
* @see     Object#equals(Object)
* @see     #get(Object)
* @see     #containsKey(Object)
    */
    public Object put(Object key, Object value) {
        Object k = maskNull(key);
        Object[] tab = table;
        int len = tab.length;
        int i = hash(k, len);
        
        Object item;
        while ( (item = tab[i]) != null) {
            if (item == k) {
                Object oldValue = tab[i + 1];
                tab[i + 1] = value;
                return oldValue;
            }
            i = nextKeyIndex(i, len);
        }
        
        ++modCount;
        tab[i] = k;
        tab[i + 1] = value;
        if (++size >= threshold)
            resize(len); // len == 2 * current capacity.
        return null;
    }
    
    /**
* Resize the table to hold given capacity.
*
* @param newCapacity the new capacity, must be a power of two.
    */
    private void resize(int newCapacity) {
// assert (newCapacity & -newCapacity) == newCapacity; // power of 2
        int newLength = newCapacity * 2;
        
        Object[] oldTable = table;
        int oldLength = oldTable.length;
        if (oldLength == 2*MAXIMUM_CAPACITY) { // can't expand any further
            if (threshold == MAXIMUM_CAPACITY-1)
                throw new IllegalStateException("Capacity exhausted.");
            threshold = MAXIMUM_CAPACITY-1;  // Gigantic map!
            return;
        }
        if (oldLength >= newLength)
            return;
        
        Object[] newTable = new Object[newLength];
        threshold = newLength / 3;
        
        for (int j = 0; j < oldLength; j += 2) {
            Object key = oldTable[j];
            if (key != null) {
                Object value = oldTable[j+1];
                oldTable[j] = null;
                oldTable[j+1] = null;
                int i = hash(key, newLength);
                while (newTable[i] != null)
                    i = nextKeyIndex(i, newLength);
                newTable[i] = key;
                newTable[i + 1] = value;
            }
        }
        table = newTable;
    }
    
    /**
* Copies all of the mappings from the specified map to this map
* These mappings will replace any mappings that
* this map had for any of the keys currently in the specified map.<p>
*
* @param t mappings to be stored in this map.
* @throws NullPointerException if the specified map is null.
    */
    public void putAll(Map t) {
        int n = t.size();
        if (n == 0)
            return;
        if (n > threshold) // conservatively pre-expand
            resize(capacity(n));
        
        for (Iterator it = t.entrySet().iterator(); it.hasNext(); ) {
            Entry e = (Entry) it.next();
            put(e.getKey(), e.getValue());
        }
    }
    
    /**
* Removes the mapping for this key from this map if present.
*
* @param key key whose mapping is to be removed from the map.
* @return previous value associated with specified key, or <tt>null</tt>
*	       if there was no entry for key.  (A <tt>null</tt> return can
*	       also indicate that the map previously associated <tt>null</tt>
*	       with the specified key.)
    */
    public Object remove(Object key) {
        Object k = maskNull(key);
        Object[] tab = table;
        int len = tab.length;
        int i = hash(k, len);
        
        while (true) {
            Object item = tab[i];
            if (item == k) {
                ++modCount;
                --size;
                Object oldValue = tab[i + 1];
                tab[i + 1] = null;
                tab[i] = null;
                closeDeletion(i);
                return oldValue;
            }
            if (item == null)
                return null;
            i = nextKeyIndex(i, len);
        }
        
    }
    
    /**
* Removes the specified key-value mapping from the map if it is present.
*
* @param   key   possible key.
* @param   value possible value.
* @return  <code>true</code> if and only if the specified key-value
*          mapping was in map.
    */
    protected boolean removeMapping(Object key, Object value) {
        Object k = maskNull(key);
        Object[] tab = table;
        int len = tab.length;
        int i = hash(k, len);
        
        while (true) {
            Object item = tab[i];
            if (item == k) {
                if (tab[i + 1] != value)
                    return false;
                ++modCount;
                --size;
                tab[i] = null;
                tab[i + 1] = null;
                closeDeletion(i);
                return true;
            }
            if (item == null)
                return false;
            i = nextKeyIndex(i, len);
        }
    }
    
    /**
* Rehash all possibly-colliding entries following a
* deletion. This preserves the linear-probe
* collision properties required by get, put, etc.
*
* @param d the index of a newly empty deleted slot
    */
    private void closeDeletion(int d) {
// Adapted from Knuth Section 6.4 Algorithm R
        Object[] tab = table;
        int len = tab.length;
        
// Look for items to swap into newly vacated slot
// starting at index immediately following deletion,
// and continuing until a null slot is seen, indicating
// the end of a run of possibly-colliding keys.
        Object item;
        for (int i = nextKeyIndex(d, len); (item = tab[i]) != null;
        i = nextKeyIndex(i, len) ) {
// The following test triggers if the item at slot i (which
// hashes to be at slot r) should take the spot vacated by d.
// If so, we swap it in, and then continue with d now at the
// newly vacated i.  This process will terminate when we hit
// the null slot at the end of this run.
// The test is messy because we are using a circular table.
            int r = hash(item, len);
            if ((i < r && (r <= d || d <= i)) || (r <= d && d <= i)) {
                tab[d] = item;
                tab[d + 1] = tab[i + 1];
                tab[i] = null;
                tab[i + 1] = null;
                d = i;
            }
        }
    }
    
    /**
* Removes all mappings from this map.
    */
    public void clear() {
        ++modCount;
        Object[] tab = table;
        for (int i = 0; i < tab.length; ++i)
            tab[i] = null;
        size = 0;
    }
    
    /**
* Compares the specified object with this map for equality.  Returns
* <tt>true</tt> if the given object is also a map and the two maps
* represent identical object-reference mappings.  More formally, this
* map is equal to another map <tt>m</tt> if and only if
* map <tt>this.entrySet().equals(m.entrySet())</tt>.
*
* <p><b>Owing to the reference-equality-based semantics of this map it is
* possible that the symmetry and transitivity requirements of the
* <tt>Object.equals</tt> contract may be violated if this map is compared
* to a normal map.  However, the <tt>Object.equals</tt> contract is
* guaranteed to hold among <tt>IdentityHashMap</tt> instances.</b>
*
* @param  o object to be compared for equality with this map.
* @return <tt>true</tt> if the specified object is equal to this map.
* @see Object#equals(Object)
    */
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof IdentityHashMap) {
            IdentityHashMap m = (IdentityHashMap) o;
            if (m.size() != size)
                return false;
            
            Object[] tab = m.table;
            for (int i = 0; i < tab.length; i+=2) {
                Object k = tab[i];
                if (k != null && !containsMapping(k, tab[i + 1]))
                    return false;
            }
            return true;
        } else if (o instanceof Map) {
            Map m = (Map)o;
            return entrySet().equals(m.entrySet());
        } else {
            return false;  // o is not a Map
        }
    }
    
    /**
* Returns the hash code value for this map.  The hash code of a map
* is defined to be the sum of the hashcode of each entry in the map's
* entrySet view.  This ensures that <tt>t1.equals(t2)</tt> implies
* that <tt>t1.hashCode()==t2.hashCode()</tt> for any two
* <tt>IdentityHashMap</tt> instances <tt>t1</tt> and <tt>t2</tt>, as
* required by the general contract of {@link Object#hashCode()}.
*
* <p><b>Owing to the reference-equality-based semantics of the
* <tt>Map.Entry</tt> instances in the set returned by this map's
* <tt>entrySet</tt> method, it is possible that the contractual
* requirement of <tt>Object.hashCode</tt> mentioned in the previous
* paragraph will be violated if one of the two objects being compared is
* an <tt>IdentityHashMap</tt> instance and the other is a normal map.</b>
*
* @return the hash code value for this map.
* @see Object#hashCode()
* @see Object#equals(Object)
* @see #equals(Object)
    */
    public int hashCode() {
        int result = 0;
        Object[] tab = table;
        for (int i = 0; i < tab.length; i +=2) {
            Object key = tab[i];
            if (key != null) {
                Object k = unmaskNull(key);
                result += System.identityHashCode(k) ^
                        System.identityHashCode(tab[i + 1]);
            }
        }
        return result;
    }
    
    /**
* Returns a shallow copy of this identity hash map: the keys and values
* themselves are not cloned.
*
* @return a shallow copy of this map.
    */
    public Object clone() {
        try {
            IdentityHashMap t = (IdentityHashMap)super.clone();
            t.entrySet = null;
            t.table = (Object[])(table.clone());
            return t;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
    
    private abstract class IdentityHashMapIterator implements Iterator {
        int index = (size != 0 ? 0 : table.length); // current slot.
        int expectedModCount = modCount; // to support fast-fail
        int lastReturnedIndex = -1;      // to allow remove()
        boolean indexValid; // To avoid unecessary next computation
        Object[] traversalTable = table; // reference to main table or copy
        
        public boolean hasNext() {
            Object[] tab = traversalTable;
            for (int i = index; i < tab.length; i+=2) {
                Object key = tab[i];
                if (key != null) {
                    index = i;
                    return indexValid = true;
                }
            }
            index = tab.length;
            return false;
        }
        
        protected int nextIndex() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            if (!indexValid && !hasNext())
                throw new NoSuchElementException();
            
            indexValid = false;
            lastReturnedIndex = index;
            index += 2;
            return lastReturnedIndex;
        }
        
        public void remove() {
            if (lastReturnedIndex == -1)
                throw new IllegalStateException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            
            expectedModCount = ++modCount;
            int deletedSlot = lastReturnedIndex;
            lastReturnedIndex = -1;
            --size;
// back up index to revisit new contents after deletion
            index = deletedSlot;
            indexValid = false;
            
// Removal code proceeds as in closeDeletion except that
// it must catch the rare case where an element already
// seen is swapped into a vacant slot that will be later
// traversed by this iterator. We cannot allow future
// next() calls to return it again.  The likelihood of
// this occurring under 2/3 load factor is very slim, but
// when it does happen, we must make a copy of the rest of
// the table to use for the rest of the traversal. Since
// this can only happen when we are near the end of the table,
// even in these rare cases, this is not very expensive in
// time or space.
            
            Object[] tab = traversalTable;
            int len = tab.length;
            
            int d = deletedSlot;
            Object key = tab[d];
            tab[d] = null;        // vacate the slot
            tab[d + 1] = null;
            
// If traversing a copy, remove in real table.
// We can skip gap-closure on copy.
            if (tab != IdentityHashMap.this.table) {
                IdentityHashMap.this.remove(key);
                expectedModCount = modCount;
                return;
            }
            
            Object item;
            for (int i = nextKeyIndex(d, len); (item = tab[i]) != null;
            i = nextKeyIndex(i, len)) {
                int r = hash(item, len);
// See closeDeletion for explanation of this conditional
                if ((i < r && (r <= d || d <= i)) ||
                        (r <= d && d <= i)) {
                    
// If we are about to swap an already-seen element
// into a slot that may later be returned by next(),
// then clone the rest of table for use in future
// next() calls. It is OK that our copy will have
// a gap in the "wrong" place, since it will never
// be used for searching anyway.
                    
                    if (i < deletedSlot && d >= deletedSlot &&
                            traversalTable == IdentityHashMap.this.table) {
                        int remaining = len - deletedSlot;
                        Object[] newTable = new Object[remaining];
                        System.arraycopy(tab, deletedSlot,
                                newTable, 0, remaining);
                        traversalTable = newTable;
                        index = 0;
                    }
                    
                    tab[d] = item;
                    tab[d + 1] = tab[i + 1];
                    tab[i] = null;
                    tab[i + 1] = null;
                    d = i;
                }
            }
        }
    }
    
    private class KeyIterator extends IdentityHashMapIterator {
        public Object next() {
            return unmaskNull(traversalTable[nextIndex()]);
        }
    }
    
    private class ValueIterator extends IdentityHashMapIterator {
        public Object next() {
            return traversalTable[nextIndex() + 1];
        }
    }
    
    /**
* Since we don't use Entry objects, we use the Iterator
* itself as an entry.
    */
    private class EntryIterator extends IdentityHashMapIterator
            implements Map.Entry {
        public Object next() {
            nextIndex();
            return this;
        }
        
        public Object getKey() {
// Provide a better exception than out of bounds index
            if (lastReturnedIndex < 0)
                throw new IllegalStateException("Entry was removed");
            
            return unmaskNull(traversalTable[lastReturnedIndex]);
        }
        
        public Object getValue() {
// Provide a better exception than out of bounds index
            if (lastReturnedIndex < 0)
                throw new IllegalStateException("Entry was removed");
            
            return traversalTable[lastReturnedIndex+1];
        }
        
        public Object setValue(Object value) {
// It would be mean-spirited to proceed here if remove() called
            if (lastReturnedIndex < 0)
                throw new IllegalStateException("Entry was removed");
            Object oldValue = traversalTable[lastReturnedIndex+1];
            traversalTable[lastReturnedIndex+1] = value;
// if shadowing, force into main table
            if (traversalTable != IdentityHashMap.this.table)
                put(traversalTable[lastReturnedIndex], value);
            return oldValue;
        }
        
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry e = (Map.Entry)o;
            return e.getKey()   == getKey() &&
                    e.getValue() == getValue();
        }
        
        public int hashCode() {
            return System.identityHashCode(getKey()) ^
                    System.identityHashCode(getValue());
        }
        
        public String toString() {
            return getKey() + "=" + getValue();
        }
    }
    
// Views
    
    /**
* This field is initialized to contain an instance of the entry set
* view the first time this view is requested.  The view is stateless,
* so there's no reason to create more than one.
    */
    
    private transient Set entrySet = null;
    
    /**
* Returns an identity-based set view of the keys contained in this map.
* The set is backed by the map, so changes to the map are reflected in
* the set, and vice-versa.  If the map is modified while an iteration
* over the set is in progress, the results of the iteration are
* undefined.  The set supports element removal, which removes the
* corresponding mapping from the map, via the <tt>Iterator.remove</tt>,
* <tt>Set.remove</tt>, <tt>removeAll</tt> <tt>retainAll</tt>, and
* <tt>clear</tt> methods.  It does not support the <tt>add</tt> or
* <tt>addAll</tt> methods.
*
* <p><b>While the object returned by this method implements the
* <tt>Set</tt> interface, it does <i>not</i> obey <tt>Set's</tt> general
* contract.  Like its backing map, the set returned by this method
* defines element equality as reference-equality rather than
* object-equality.  This affects the behavior of its <tt>contains</tt>,
* <tt>remove</tt>, <tt>containsAll</tt>, <tt>equals</tt>, and
* <tt>hashCode</tt> methods.</b>
*
* <p>The <tt>equals</tt> method of the returned set returns <tt>true</tt>
* only if the specified object is a set containing exactly the same
* object references as the returned set.  The symmetry and transitivity
* requirements of the <tt>Object.equals</tt> contract may be violated if
* the set returned by this method is compared to a normal set.  However,
* the <tt>Object.equals</tt> contract is guaranteed to hold among sets
* returned by this method.</b>
*
* <p>The <tt>hashCode</tt> method of the returned set returns the sum of
* the <i>identity hashcodes</i> of the elements in the set, rather than
* the sum of their hashcodes.  This is mandated by the change in the
* semantics of the <tt>equals</tt> method, in order to enforce the
* general contract of the <tt>Object.hashCode</tt> method among sets
* returned by this method.
*
* @return an identity-based set view of the keys contained in this map.
* @see Object#equals(Object)
* @see System#identityHashCode(Object)
    */
    public Set keySet() {
        return new KeySet();//XXX can't do otherwise than de-optimize
/*
Set ks = keySet;
if (ks != null)
return ks;
else
return keySet = new KeySet();
        */
    }
    
    private class KeySet extends AbstractSet {
        public Iterator iterator() {
            return new KeyIterator();
        }
        public int size() {
            return size;
        }
        public boolean contains(Object o) {
            return containsKey(o);
        }
        public boolean remove(Object o) {
            int oldSize = size;
            IdentityHashMap.this.remove(o);
            return size != oldSize;
        }
/*
* Must revert from AbstractSet's impl to AbstractCollection's, as
* the former contains an optimization that results in incorrect
* behavior when c is a smaller "normal" (non-identity-based) Set.
        */
        public boolean removeAll(Collection c) {
            boolean modified = false;
            for (Iterator i = iterator(); i.hasNext(); ) {
                if(c.contains(i.next())) {
                    i.remove();
                    modified = true;
                }
            }
            return modified;
        }
        public void clear() {
            IdentityHashMap.this.clear();
        }
        public int hashCode() {
            int result = 0;
            for (Iterator i = iterator(); i.hasNext(); )
                result += System.identityHashCode(i.next());
            return result;
        }
    }
    
    /**
* <p>Returns a collection view of the values contained in this map.  The
* collection is backed by the map, so changes to the map are reflected in
* the collection, and vice-versa.  If the map is modified while an
* iteration over the collection is in progress, the results of the
* iteration are undefined.  The collection supports element removal,
* which removes the corresponding mapping from the map, via the
* <tt>Iterator.remove</tt>, <tt>Collection.remove</tt>,
* <tt>removeAll</tt>, <tt>retainAll</tt> and <tt>clear</tt> methods.
* It does not support the <tt>add</tt> or <tt>addAll</tt> methods.
*
* <p><b>While the object returned by this method implements the
* <tt>Collection</tt> interface, it does <i>not</i> obey
* <tt>Collection's</tt> general contract.  Like its backing map,
* the collection returned by this method defines element equality as
* reference-equality rather than object-equality.  This affects the
* behavior of its <tt>contains</tt>, <tt>remove</tt> and
* <tt>containsAll</tt> methods.</b>
*
* @return a collection view of the values contained in this map.
    */
    public Collection values() {
        return new Values();//XXX can't do otherwise than de-optimize
/*
Collection vs = values;
if (vs != null)
return vs;
else
return values = new Values();
        */
    }
    
    private class Values extends AbstractCollection {
        public Iterator iterator() {
            return new ValueIterator();
        }
        public int size() {
            return size;
        }
        public boolean contains(Object o) {
            return containsValue(o);
        }
        public boolean remove(Object o) {
            for (Iterator i = iterator(); i.hasNext(); ) {
                if (i.next() == o) {
                    i.remove();
                    return true;
                }
            }
            return false;
        }
        public void clear() {
            IdentityHashMap.this.clear();
        }
    }
    
    /**
* Returns a set view of the mappings contained in this map.  Each element
* in the returned set is a reference-equality-based <tt>Map.Entry</tt>.
* The set is backed by the map, so changes to the map are reflected in
* the set, and vice-versa.  If the map is modified while an iteration
* over the set is in progress, the results of the iteration are
* undefined.  The set supports element removal, which removes the
* corresponding mapping from the map, via the <tt>Iterator.remove</tt>,
* <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
* <tt>clear</tt> methods.  It does not support the <tt>add</tt> or
* <tt>addAll</tt> methods.
*
* <p>Like the backing map, the <tt>Map.Entry</tt> objects in the set
* returned by this method define key and value equality as
* reference-equality rather than object-equality.  This affects the
* behavior of the <tt>equals</tt> and <tt>hashCode</tt> methods of these
* <tt>Map.Entry</tt> objects.  A reference-equality based <tt>Map.Entry
* e</tt> is equal to an object <tt>o</tt> if and only if <tt>o</tt> is a
* <tt>Map.Entry</tt> and <tt>e.getKey()==o.getKey() &&
* e.getValue()==o.getValue()</tt>.  To accommodate these equals
* semantics, the <tt>hashCode</tt> method returns
* <tt>System.identityHashCode(e.getKey()) ^
* System.identityHashCode(e.getValue())</tt>.
*
* <p><b>Owing to the reference-equality-based semantics of the
* <tt>Map.Entry</tt> instances in the set returned by this method,
* it is possible that the symmetry and transitivity requirements of
* the {@link Object#equals(Object)} contract may be violated if any of
* the entries in the set is compared to a normal map entry, or if
* the set returned by this method is compared to a set of normal map
* entries (such as would be returned by a call to this method on a normal
* map).  However, the <tt>Object.equals</tt> contract is guaranteed to
* hold among identity-based map entries, and among sets of such entries.
* </b>
*
* @return a set view of the identity-mappings contained in this map.
    */
    public Set entrySet() {
        Set es = entrySet;
        if (es != null)
            return es;
        else
            return entrySet = new EntrySet();
    }
    
    private class EntrySet extends AbstractSet {
        public Iterator iterator() {
            return new EntryIterator();
        }
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry entry = (Map.Entry)o;
            return containsMapping(entry.getKey(), entry.getValue());
        }
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry entry = (Map.Entry)o;
            return removeMapping(entry.getKey(), entry.getValue());
        }
        public int size() {
            return size;
        }
        public void clear() {
            IdentityHashMap.this.clear();
        }
/*
* Must revert from AbstractSet's impl to AbstractCollection's, as
* the former contains an optimization that results in incorrect
* behavior when c is a smaller "normal" (non-identity-based) Set.
        */
        public boolean removeAll(Collection c) {
            boolean modified = false;
            for (Iterator i = iterator(); i.hasNext(); ) {
                if(c.contains(i.next())) {
                    i.remove();
                    modified = true;
                }
            }
            return modified;
        }
        
        public Object[] toArray() {
            Collection c = new ArrayList(size());
            for (Iterator i = iterator(); i.hasNext(); )
                c.add(new SimpleEntry((Map.Entry) i.next()));
            return c.toArray();
        }
        public Object[] toArray(Object a[]) {
            Collection c = new ArrayList(size());
            for (Iterator i = iterator(); i.hasNext(); )
                c.add(new SimpleEntry((Map.Entry) i.next()));
            return c.toArray(a);
        }
        
    }
    
    
    /**
* Save the state of the <tt>IdentityHashMap</tt> instance to a stream
* (i.e., serialize it).
*
* @serialData The <i>size</i> of the HashMap (the number of key-value
*	        mappings) (<tt>int</tt>), followed by the key (Object) and
*          value (Object) for each key-value mapping represented by the
*          IdentityHashMap.  The key-value mappings are emitted in no
*          particular order.
    */
    private void writeObject(java.io.ObjectOutputStream s)
    throws java.io.IOException  {
// Write out and any hidden stuff
        s.defaultWriteObject();
        
// Write out size (number of Mappings)
        s.writeInt(size);
        
// Write out keys and values (alternating)
        Object[] tab = table;
        for (int i = 0; i < tab.length; i += 2) {
            Object key = tab[i];
            if (key != null) {
                s.writeObject(unmaskNull(key));
                s.writeObject(tab[i + 1]);
            }
        }
    }
    
    /**
* Reconstitute the <tt>IdentityHashMap</tt> instance from a stream (i.e.,
* deserialize it).
    */
    private void readObject(java.io.ObjectInputStream s)
    throws java.io.IOException, ClassNotFoundException  {
// Read in any hidden stuff
        s.defaultReadObject();
        
// Read in size (number of Mappings)
        int l_size = s.readInt();
        
// Allow for 33% growth (i.e., capacity is >= 2* size()).
        init(capacity((l_size*4)/3));
        
// Read the keys and values, and put the mappings in the table
        for (int i=0; i<l_size; ++i) {
            Object key = s.readObject();
            Object value = s.readObject();
            put(key, value);
        }
    }
}

%>
<%!
public class SimpleEntry implements Map.Entry, Serializable {
    private final Object key;
    private Object value;
    
    /**
* Creates an entry representing a mapping from the specified
* key to the specified value.
*
* @param key the key represented by this entry
* @param value the value represented by this entry
    */
    public SimpleEntry(Object key, Object value) {
        this.key   = key;
        this.value = value;
    }
    
    /**
* Creates an entry representing the same mapping as the
* specified entry.
*
* @param entry the entry to copy
    */
    public SimpleEntry(Map.Entry entry) {
        this.key   = entry.getKey();
        this.value = entry.getValue();
    }
    
    /**
* @return the key corresponding to this entry
    */
    public Object getKey() {
        return key;
    }
    
    /**
* @return the value corresponding to this entry
    */
    public Object getValue() {
        return value;
    }
    
    /**
* Replaces the value corresponding to this entry with the specified
* value.
*
* @param value new value to be stored in this entry
* @return the old value corresponding to the entry
    */
    public Object setValue(Object value) {
        Object oldValue = this.value;
        this.value = value;
        return oldValue;
    }
    
    /**
* Compares the specified object with this entry for equality.
* Returns {@code true} if the given object is also a map entry and
* the two entries represent the same mapping.	More formally, two
* entries {@code e1} and {@code e2} represent the same mapping
* if<pre>
*   (e1.getKey()==null ?
*	e2.getKey()==null :
*	e1.getKey().equals(e2.getKey()))
*   &amp;&amp;
*   (e1.getValue()==null ?
*	e2.getValue()==null :
*	e1.getValue().equals(e2.getValue()))</pre>
* This ensures that the {@code equals} method works properly across
* different implementations of the {@code Map.Entry} interface.
*
* @param o object to be compared for equality with this map entry
* @return {@code true} if the specified object is equal to this map
*	   entry
* @see	#hashCode
    */
    public boolean equals(Object o) {
        if (!(o instanceof Map.Entry))
            return false;
        Map.Entry e = (Map.Entry)o;
        return eq(key, e.getKey()) && eq(value, e.getValue());
    }
    
    /**
* Utility method for SimpleEntry and SimpleImmutableEntry.
* Test for equality, checking for nulls.
    */
    private boolean eq(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }
    
    /**
* Returns the hash code value for this map entry.  The hash code
* of a map entry {@code e} is defined to be: <pre>
*   (e.getKey()==null   ? 0 : e.getKey().hashCode()) ^
*   (e.getValue()==null ? 0 : e.getValue().hashCode())</pre>
* This ensures that {@code e1.equals(e2)} implies that
* {@code e1.hashCode()==e2.hashCode()} for any two Entries
* {@code e1} and {@code e2}, as required by the general
* contract of {@link Object#hashCode}.
*
* @return the hash code value for this map entry
* @see	#equals
    */
    public int hashCode() {
        return (key   == null ? 0 :   key.hashCode()) ^
                (value == null ? 0 : value.hashCode());
    }
    
    /**
* Returns a String representation of this map entry.  This
* implementation returns the string representation of this
* entry's key followed by the equals character ("<tt>=</tt>")
* followed by the string representation of this entry's value.
*
* @return a String representation of this map entry
    */
    public String toString() {
        return key + "=" + value;
    }
    
}
%>
<%!
public class ObjectProfiler {
// public: ................................................................
    
// the following constants are physical sizes (in bytes) and are JVM-dependent:
// [the current values are Ok for most 32-bit JVMs]
    
    public static final int OBJECT_SHELL_SIZE = 8; // java.lang.Object shell
// size in bytes
    public static final int OBJREF_SIZE = 4;
    public static final int LONG_FIELD_SIZE = 8;
    public static final int INT_FIELD_SIZE = 4;
    public static final int SHORT_FIELD_SIZE = 2;
    public static final int CHAR_FIELD_SIZE = 2;
    public static final int BYTE_FIELD_SIZE = 1;
    public static final int BOOLEAN_FIELD_SIZE = 1;
    public static final int DOUBLE_FIELD_SIZE = 8;
    public static final int FLOAT_FIELD_SIZE = 4;
    
    /**
* Estimates the full size of the object graph rooted at 'obj'. Duplicate
* data instances are correctly accounted for. The implementation is not
* recursive.
*
* @param obj
*			input object instance to be measured
* @return 'obj' size [0 if 'obj' is null']
    */
    public long sizeof(final Object obj) {
        if (null == obj)
            return 0;
        
        final IdentityHashMap visited = new IdentityHashMap();
        
        try {
            return computeSizeof(obj, visited, CLASS_METADATA_CACHE);
        } catch (RuntimeException re) {
//re.printStackTrace();//DEBUG
            return -1;
        } catch (NoClassDefFoundError ncdfe) {
// BUG: throws "java.lang.NoClassDefFoundError: org.eclipse.core.resources.IWorkspaceRoot" when run in WSAD 5
// see http://www.javaworld.com/javaforums/showflat.php?Cat=&Board=958763&Number=15235&page=0&view=collapsed&sb=5&o=
//System.err.println(ncdfe);//DEBUG
            return -1;
        }
    }
    
    /**
* Estimates the full size of the object graph rooted at 'obj' by
* pre-populating the "visited" set with the object graph rooted at 'base'.
* The net effect is to compute the size of 'obj' by summing over all
* instance data contained in 'obj' but not in 'base'.
*
* @param base
*			graph boundary [may not be null]
* @param obj
*			input object instance to be measured
* @return 'obj' size [0 if 'obj' is null']
    */
    public long sizedelta(final Object base, final Object obj) {
        if (null == obj)
            return 0;
        if (null == base)
            throw new IllegalArgumentException("null input: base");
        
        final IdentityHashMap visited = new IdentityHashMap();
        
        try {
            computeSizeof(base, visited, CLASS_METADATA_CACHE);
            return visited.containsKey(obj) ? 0 : computeSizeof(obj, visited, CLASS_METADATA_CACHE);
        } catch (RuntimeException re) {
//re.printStackTrace();//DEBUG
            return -1;
        } catch (NoClassDefFoundError ncdfe) {
// BUG: throws "java.lang.NoClassDefFoundError: org.eclipse.core.resources.IWorkspaceRoot" when run in WSAD 5
// see http://www.javaworld.com/javaforums/showflat.php?Cat=&Board=958763&Number=15235&page=0&view=collapsed&sb=5&o=
//System.err.println(ncdfe);//DEBUG
            return -1;
        }
    }
    
// protected: .............................................................
    
// package: ...............................................................
    
// private: ...............................................................
    
/*
* Internal class used to cache class metadata information.
    */
    private final class ClassMetadata {
        ClassMetadata(final int primitiveFieldCount, final int shellSize,
                final Field[] refFields) {
            m_primitiveFieldCount = primitiveFieldCount;
            m_shellSize = shellSize;
            m_refFields = refFields;
        }
        
// all fields are inclusive of superclasses:
        
        final int m_primitiveFieldCount;
        
        final int m_shellSize; // class shell size
        
        final Field[] m_refFields; // cached non-static fields (made accessible)
        
    } // end of nested class
    
    private final class ClassAccessPrivilegedAction implements PrivilegedExceptionAction {
        /** {@inheritDoc} */
        public Object run() throws Exception {
            return m_cls.getDeclaredFields();
        }
        
        void setContext(final Class cls) {
            m_cls = cls;
        }
        
        private Class m_cls;
        
    } // end of nested class
    
    private final class FieldAccessPrivilegedAction implements PrivilegedExceptionAction {
        /** {@inheritDoc} */
        public Object run() throws Exception {
            m_field.setAccessible(true);
            return null;
        }
        
        void setContext(final Field field) {
            m_field = field;
        }
        
        private Field m_field;
        
    } // end of nested class
    
    private ObjectProfiler() {
        preinit();
    } // this class is not extendible
    
/*
* The main worker method for sizeof() and sizedelta().
    */
    private long computeSizeof(Object obj, final IdentityHashMap visited,
            final Map /* <Class,ClassMetadata> */metadataMap) {
// this uses depth-first traversal; the exact graph traversal algorithm
// does not matter for computing the total size and this method could be
// easily adjusted to do breadth-first instead (addLast() instead of
// addFirst()),
// however, dfs/bfs require max queue length to be the length of the
// longest
// graph path/width of traversal front correspondingly, so I expect
// dfs to use fewer resources than bfs for most Java objects;
        
        if (null == obj)
            return 0;
        
        final LinkedList queue = new LinkedList();
        
        visited.put(obj, obj);
        queue.add(obj);
        
        long result = 0;
        
        final ClassAccessPrivilegedAction caAction = new ClassAccessPrivilegedAction();
        final FieldAccessPrivilegedAction faAction = new FieldAccessPrivilegedAction();
        
        while (!queue.isEmpty()) {
            obj = queue.removeFirst();
            final Class objClass = obj.getClass();
            
            int skippedBytes = skipClassDueToSunSolarisBug(objClass);
            if (skippedBytes > 0) {
                result += skippedBytes; // can't do better than that
                continue;
            }
            
            if (objClass.isArray()) {
                final int arrayLength = Array.getLength(obj);
                final Class componentType = objClass.getComponentType();
                
                result += sizeofArrayShell(arrayLength, componentType);
                
                if (!componentType.isPrimitive()) {
// traverse each array slot:
                    for (int i = 0; i < arrayLength; ++i) {
                        final Object ref = Array.get(obj, i);
                        
                        if ((ref != null) && !visited.containsKey(ref)) {
                            visited.put(ref, ref);
                            queue.addFirst(ref);
                        }
                    }
                }
            } else { // the object is of a non-array type
                final ClassMetadata metadata = getClassMetadata(objClass,
                        metadataMap, caAction, faAction);
                final Field[] fields = metadata.m_refFields;
                
                result += metadata.m_shellSize;
                
// traverse all non-null ref fields:
                for (int f = 0, fLimit = fields.length; f < fLimit; ++f) {
                    final Field field = fields[f];
                    
                    final Object ref;
                    try { // to get the field value:
                        ref = field.get(obj);
                    } catch (Exception e) {
                        throw new RuntimeException("cannot get field ["
                                + field.getName() + "] of class ["
                                + field.getDeclaringClass().getName() + "]: "
                                + e.toString());
                    }
                    
                    if ((ref != null) && !visited.containsKey(ref)) {
                        visited.put(ref, ref);
                        queue.addFirst(ref);
                    }
                }
            }
        }
        
        return result;
    }
    
/*
* A helper method for manipulating a class metadata cache.
    */
    private ClassMetadata getClassMetadata(final Class cls,
            final Map /* <Class,ClassMetadata> */metadataMap,
            final ClassAccessPrivilegedAction caAction,
            final FieldAccessPrivilegedAction faAction) {
        if (null == cls)
            return null;
        
        ClassMetadata result;
        synchronized (metadataMap) {
            result = (ClassMetadata) metadataMap.get(cls);
        }
        if (result != null)
            return result;
        
        int primitiveFieldCount = 0;
        int shellSize = OBJECT_SHELL_SIZE; // java.lang.Object shell
        final List /* Field */refFields = new LinkedList();
        
        final Field[] declaredFields;
        try {
            caAction.setContext(cls);
            declaredFields = (Field[]) AccessController.doPrivileged(caAction);
        } catch (PrivilegedActionException pae) {
            throw new RuntimeException(
                    "could not access declared fields of class "
                    + cls.getName() + ": " + pae.getException());
        }
        
        for (int f = 0; f < declaredFields.length; ++f) {
            final Field field = declaredFields[f];
            if ((Modifier.STATIC & field.getModifiers()) != 0)
                continue;
            
            final Class fieldType = field.getType();
            if (fieldType.isPrimitive()) {
// memory alignment ignored:
                shellSize += sizeofPrimitiveType(fieldType);
                ++primitiveFieldCount;
            } else {
// prepare for graph traversal later:
                if (!field.isAccessible()) {
                    try {
                        faAction.setContext(field);
                        AccessController.doPrivileged(faAction);
                    } catch (PrivilegedActionException pae) {
                        throw new RuntimeException("could not make field "
                                + field + " accessible: " + pae.getException());
                    }
                }
                
// memory alignment ignored:
                shellSize += OBJREF_SIZE;
                refFields.add(field);
            }
        }
        
// recurse into superclass:
        final ClassMetadata superMetadata = getClassMetadata(cls
                .getSuperclass(), metadataMap, caAction, faAction);
        if (superMetadata != null) {
            primitiveFieldCount += superMetadata.m_primitiveFieldCount;
            shellSize += superMetadata.m_shellSize - OBJECT_SHELL_SIZE;
            refFields.addAll(Arrays.asList(superMetadata.m_refFields));
        }
        
        final Field[] _refFields = new Field[refFields.size()];
        refFields.toArray(_refFields);
        
        result = new ClassMetadata(primitiveFieldCount, shellSize, _refFields);
        synchronized (metadataMap) {
            metadataMap.put(cls, result);
        }
        
        return result;
    }
    
/*
* Computes the "shallow" size of an array instance.
    */
    private int sizeofArrayShell(final int length, final Class componentType) {
// this ignores memory alignment issues by design:
        
        final int slotSize = componentType.isPrimitive() ? sizeofPrimitiveType(componentType)
        : OBJREF_SIZE;
        
        return OBJECT_SHELL_SIZE + INT_FIELD_SIZE + OBJREF_SIZE + length * slotSize;
    }
    
/*
* Returns the JVM-specific size of a primitive type.
    */
    private int sizeofPrimitiveType(final Class type) {
        if (type == int.class)
            return INT_FIELD_SIZE;
        else if (type == long.class)
            return LONG_FIELD_SIZE;
        else if (type == short.class)
            return SHORT_FIELD_SIZE;
        else if (type == byte.class)
            return BYTE_FIELD_SIZE;
        else if (type == boolean.class)
            return BOOLEAN_FIELD_SIZE;
        else if (type == char.class)
            return CHAR_FIELD_SIZE;
        else if (type == double.class)
            return DOUBLE_FIELD_SIZE;
        else if (type == float.class)
            return FLOAT_FIELD_SIZE;
        else
            throw new IllegalArgumentException("not primitive: " + type);
    }
    
// class metadata cache:
    private Map CLASS_METADATA_CACHE = new WeakHashMap(101);
    
    protected Class[] sunProblematicClasses;
    protected Map/*<Class, Integer>*/ sunProblematicClassesSizes;
    
    public void preinit() {
        Map classesSizes = new HashMap(5 * 3/4 + 1);
// 1.3+
        classesSizes.put("java.lang.Throwable", new Integer(20));
// 1.4+
        classesSizes.put("sun.reflect.UnsafeStaticFieldAccessorImpl", new Integer(OBJECT_SHELL_SIZE));//unknown
        classesSizes.put("sun.reflect.UnsafeStaticObjectFieldAccessorImpl", new Integer(OBJECT_SHELL_SIZE));//unknown
        classesSizes.put("sun.reflect.UnsafeStaticObjectFieldAccessorImpl", new Integer(OBJECT_SHELL_SIZE));//unknown
        classesSizes.put("sun.reflect.UnsafeStaticLongFieldAccessorImpl", new Integer(OBJECT_SHELL_SIZE));//unknown
// 1.5+
        classesSizes.put("sun.reflect.ConstantPool", new Integer(8));
        sunProblematicClassesSizes = Collections.unmodifiableMap(classesSizes);
        
        List classes = new ArrayList(sunProblematicClassesSizes.size());
        Iterator iter = sunProblematicClassesSizes.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String className = (String) entry.getKey();
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException cnfe) {
//System.out.println(cnfe);
            }
        }
        sunProblematicClasses = (Class[]) classes.toArray(new Class[0]);
    }
    
    /**
* http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5012949
* Implementation note:
* 	we can compare classes with == since they will always be loaded from the same ClassLoader
* 	(they are "low" in the hierarchy)
    */
    private int skipClassDueToSunSolarisBug(Class clazz) {
        for (int i = 0; i < sunProblematicClasses.length; ++i) {
            Class sunPbClass = sunProblematicClasses[i];
            if (clazz == sunPbClass) {
                return ((Integer)sunProblematicClassesSizes.get(clazz.getName())).intValue();
            }
        }
        return 0;
    }
} // end of class
%>