package me.duanbn.snailfish.core.orm.rdb;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import me.duanbn.snailfish.core.orm.PersistenceException;
import me.duanbn.snailfish.core.orm.rdb.AbstractDataObjectDao.DbField;
import me.duanbn.snailfish.util.collection.Maps;
import me.duanbn.snailfish.util.lang.StringUtil;

public class DataObjectUtil {

    private static final Map<Class<?>, DbField> primaryKeyCache = Maps.newConcurrentMap();

    private static final Map<Class<?>, Map<String, DbField>> fieldCache = Maps.newConcurrentMap();

    public static Long getPrimaryKeyValue(Object obj) {
        try {
            DbField primaryKey = getPrimaryKey(obj.getClass(), true);
            return (Long) primaryKey.original.get(obj);
        } catch (Exception e) {
            throw new PersistenceException("get pk error obj=" + obj.getClass(), e);
        }
    }

    public static DbField getPrimaryKey(Class<?> clazz, boolean includeSuperClass) {
        DbField dbField = primaryKeyCache.get(clazz);

        if (dbField == null) {
            synchronized (DataObjectUtil.class) {
                Collection<DbField> dbFields = getFields(clazz, includeSuperClass);
                for (DbField f : dbFields) {
                    if (f.primaryKey) {
                        dbField = f;
                        break;
                    }
                }
            }
        }

        return dbField;
    }

    public static Map<String, DbField> getFieldsWithMap(Class<?> clazz, boolean includeSuperClass) {
        Map<String, DbField> fieldMap = fieldCache.get(clazz);

        if (fieldMap == null) {
            synchronized (DataObjectUtil.class) {
                fieldMap = Maps.newLinkedHashMap();
                loadFields(fieldMap, clazz, includeSuperClass);
            }
        }

        return fieldMap;
    }

    public static Collection<DbField> getFields(Class<?> clazz, boolean includeSuperClass) {
        return getFieldsWithMap(clazz, includeSuperClass).values();
    }

    private static void loadFields(Map<String, DbField> fields, Class<?> clazz, boolean includeSuperClass) {

        if (includeSuperClass && clazz.getSuperclass() != null) {
            loadFields(fields, clazz.getSuperclass(), includeSuperClass);
        }

        DbField dbField = null;

        me.duanbn.snailfish.core.orm.rdb.annotations.Field annoField = null;
        for (Field f : clazz.getDeclaredFields()) {

            f.setAccessible(true);

            annoField = f.getAnnotation(me.duanbn.snailfish.core.orm.rdb.annotations.Field.class);
            if (annoField != null && fields.get(f.getName()) == null) {
                dbField = new DbField();
                dbField.original = f;
                dbField.nullable = annoField.isNull();
                dbField.index = annoField.index();
                dbField.length = annoField.length();
                dbField.name = StringUtil.isBlank(annoField.value()) ? f.getName().toUpperCase()
                        : annoField.value().toUpperCase();
                dbField.comment = StringUtil.isBlank(annoField.comment()) ? "" : annoField.comment();
                dbField.primaryKey = annoField.primaryKey();
                dbField.jsonField = annoField.jsonField();
                fields.put(dbField.name, dbField);
            }

        }

    }

}
