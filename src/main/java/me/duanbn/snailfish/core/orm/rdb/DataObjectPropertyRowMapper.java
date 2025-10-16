package me.duanbn.snailfish.core.orm.rdb;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.RowMapper;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.orm.rdb.AbstractDataObjectDao.DbField;
import me.duanbn.snailfish.core.orm.rdb.dialect.Dialect;

@Slf4j
public class DataObjectPropertyRowMapper<T> implements RowMapper<T> {

    private Class<T> clazz;
    private Dialect dialect;

    public DataObjectPropertyRowMapper(Class<T> clazz, Dialect dialect) {
        this.clazz = clazz;
        this.dialect = dialect;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            ResultSetMetaData rsMetaData = rs.getMetaData();
            Map<String, DbField> fieldMap = DataObjectUtil.getFieldsWithMap(clazz, true);

            int columnCount = rsMetaData.getColumnCount();

            if (log.isDebugEnabled()) {
                log.debug("columnCount: {}", columnCount);
            }

            Object obj = this.clazz.getDeclaredConstructor().newInstance();
            DbField dbField = null;
            String columnName = null;
            for (int i = 1; i <= columnCount; i++) {
                columnName = rsMetaData.getColumnName(i).toUpperCase();
                dbField = fieldMap.get(columnName);
                if (dbField == null) {
                    continue;
                }

                Class<?> fType = dbField.original.getType();
                Field f = dbField.original;
                if (fType == Byte.TYPE) {
                    f.setByte(obj, dialect.getByte(rs, columnName));
                } else if (fType == Byte.class) {
                    Byte value = parseToByte(dialect.getObject(rs, columnName));
                    f.set(obj, value);
                } else if (fType == Long.TYPE) {
                    f.setLong(obj, dialect.getLong(rs, columnName));
                } else if (fType == Long.class) {
                    Long value = parseToLong(dialect.getObject(rs, columnName));
                    f.set(obj, value);
                } else if (fType == Integer.TYPE) {
                    f.setInt(obj, dialect.getInt(rs, columnName));
                } else if (fType == Integer.class) {
                    Integer value = parseToInteger(dialect.getObject(rs, columnName));
                    f.set(obj, value);
                } else if (fType == Short.TYPE) {
                    f.setShort(obj, dialect.getShort(rs, columnName));
                } else if (fType == Short.class) {
                    Short value = parseToShort(dialect.getObject(rs, columnName));
                    f.set(obj, value);
                } else if (fType == Float.TYPE) {
                    f.setFloat(obj, dialect.getFloat(rs, columnName));
                } else if (fType == Float.class) {
                    Float value = parseToFloat(dialect.getObject(rs, columnName));
                    f.set(obj, value);
                } else if (fType == Double.TYPE) {
                    f.setDouble(obj, dialect.getDouble(rs, columnName));
                } else if (fType == Double.class) {
                    Double value = parseToDouble(dialect.getObject(rs, columnName));
                    f.set(obj, value);
                } else if (fType == Boolean.TYPE) {
                    f.setBoolean(obj, dialect.getBool(rs, columnName));
                } else if (fType == Boolean.class) {
                    Boolean value = parseToBoolean(dialect.getObject(rs, columnName));
                    f.set(obj, value);
                } else if (fType == BigDecimal.class) {
                    f.set(obj, dialect.getBigDecimal(rs, columnName));
                } else if (fType == byte[].class
                        || fType == Byte[].class) {
                    Object value = dialect.getBytes(rs, columnName);
                    if (value != null) {
                        f.set(obj, value);
                    }
                } else if (fType.isEnum()) {
                    f.set(obj, Enum.valueOf((Class<Enum>) f.getType(), dialect.getString(rs, columnName)));
                } else if (fType == Date.class) {
                    Object value = dialect.getObject(rs, columnName);
                    if (value instanceof LocalDateTime) {
                        ZoneId zoneId = ZoneId.systemDefault();
                        ZonedDateTime zonedDateTime = ((LocalDateTime) value).atZone(zoneId);
                        Instant instant = zonedDateTime.toInstant();
                        f.set(obj, Date.from(instant));
                    }
                } else if (fType == List.class) {
                    f.set(obj, dialect.getList(rs, columnName));
                } else if (fType == Set.class) {
                    f.set(obj, dialect.getSet(rs, columnName));
                } else if (fType == Map.class) {
                    f.set(obj, dialect.getMap(rs, columnName));
                } else if (dbField.jsonField) {
                    f.set(obj, dialect.getJson(rs, columnName, fType));
                } else {
                    f.set(obj, dialect.getObject(rs, columnName));
                }

            }

            return (T) obj;
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    private Byte parseToByte(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).byteValue();
        }

        return (Byte) value;
    }

    private boolean parseToBoolean(Object value) {
        if (value instanceof Integer && (Integer) value == 0) {
            return false;
        } else if (value instanceof Byte && (Byte) value == 0) {
            return false;
        } else {
            return true;
        }
    }

    private Short parseToShort(Object value) {
        if (value instanceof String) {
            return Short.valueOf((String) value);
        }

        if (value instanceof Integer) {
            return (Short) ((Integer) value).shortValue();
        }

        if (value instanceof Long) {
            return (Short) ((Long) value).shortValue();
        }

        if (value instanceof BigInteger) {
            return (Short) ((BigInteger) value).shortValue();
        }

        return (Short) value;
    }

    private Float parseToFloat(Object value) {
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).floatValue();
        }
        if (value instanceof Double) {
            return ((Double) value).floatValue();
        }

        return (Float) value;
    }

    private Double parseToDouble(Object value) {
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).doubleValue();
        }

        return (Double) value;
    }

    private Integer parseToInteger(Object value) {
        if (value instanceof String) {
            return Integer.valueOf((String) value);
        }

        if (value instanceof Long) {
            return (Integer) ((Long) value).intValue();
        }

        if (value instanceof BigInteger) {
            return (Integer) ((BigInteger) value).intValue();
        }

        return (Integer) value;
    }

    private Long parseToLong(Object value) {
        if (value instanceof String) {
            return Long.valueOf((String) value);
        }

        if (value instanceof Integer) {
            return (Long) ((Integer) value).longValue();
        }

        if (value instanceof BigInteger) {
            return (Long) ((BigInteger) value).longValue();
        }

        return (Long) value;
    }

}
