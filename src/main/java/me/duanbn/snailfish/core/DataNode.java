package me.duanbn.snailfish.core;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

import me.duanbn.snailfish.util.ObjectUtil;

@SuppressWarnings("rawtypes")
public class DataNode extends LinkedHashMap {

    @SuppressWarnings("unchecked")
    public DataNode append(DataNode value) {
        this.putAll(value);
        return this;
    }

    /**
     * @param value
     */
    @SuppressWarnings("unchecked")
    public DataNode append(Object data) {
        Field[] fields = ObjectUtil.getFields(data.getClass(), true);

        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            try {
                Object value = field.get(data);
                this.put(name, value);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new DataNodeException("append " + name + " error", e);
            }
        }
        return this;
    }

    public static class DataNodeException extends RuntimeException {
        public DataNodeException(String msg, Throwable t) {
            super(msg, t);
        }
    }

}
