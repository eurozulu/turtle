package org.spoofer.interpreter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * {@link StateMap} is an implementation of {@link State} using a backing {@link HashMap} of root properties
 * and providing the string name lookup on the contents.
 */
public class StateMap implements State {
    private final Map<String, Object> values = new HashMap<>();

    public StateMap() {
        put("env", System.getenv());
        put("patterns", new HashMap<String, Macro>());
    }

    /**
     * Adds a new root propeerty to the statemape
     *
     * @param name  the key name of the property
     * @param value the value it maps to
     */
    public <T> void put(String name, T value) {
        values.put(name, value);
    }

    @Override
    public <T> T get(String name) {
        if (name == null || name.length() == 0) {
            return null;
        }
        List<String> keys = new ArrayList<>(Arrays.asList(name.split("\\.")));
        return readValue(keys, values);
    }

    private <T extends Object> T readValue(List<String> keys, Object parent) throws IllegalArgumentException {
        if (keys.isEmpty())
            return null;

        T result;
        if (parent instanceof Map) {
            result = (T) ((Map) parent).get(keys.get(0));
        } else {
            try {
                Method m = findNamedMethod(keys.get(0), parent);
                result = (T) m.invoke(parent);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("failed to read value", e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException("failed to read value", e);
            }
        }
        keys.remove(0);

        // IF more left on the key, then recursive call using the newly found parent
        if (!keys.isEmpty()) {
            return readValue(keys, result);
        }
        return result;
    }

    private Method findNamedMethod(String key, Object o) throws IllegalArgumentException {
        String k = "get" + key;
        for (Method m : o.getClass().getMethods()) {
            if (k.equalsIgnoreCase(m.getName())) {
                return m;
            }
        }
        throw new IllegalArgumentException(String.format("'%s' is unknown", key));
    }
}
