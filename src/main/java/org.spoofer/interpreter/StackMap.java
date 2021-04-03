package org.spoofer.interpreter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class StackMap implements Stack {
    private final Map<String, Object> values = new HashMap<>();

    public <T> void put(String name, T value) {
        values.put(name, value);
    }

    @Override
    public <T> T get(String name) {
        if (name == null || name.trim().length() == 0) {
            return null;
        }
        List<String> keys = new ArrayList<>(Arrays.asList(name.split("\\.")));
        return readValue(keys, values);
    }

    @Override
    public boolean contains(String name) {
        return values.containsKey(cleanToken(name));
    }

    public static String cleanToken(String token) {
        String s = token.trim();
        if (s.startsWith(TOKEN_START)) {
            s = s.substring(TOKEN_START.length());
        }
        if (s.endsWith(TOKEN_END)) {
            s = s.substring(0, s.length() - TOKEN_END.length());
        }
        return s;
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
        throw new IllegalArgumentException(String.format("'%s' is unknown"));
    }
}
