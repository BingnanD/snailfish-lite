package me.duanbn.snailfish.core.orm.rdb.dialect;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * generic dialect impl.
 * 
 * @author bingnan.dbn
 */
public class GenericDialect implements Dialect {

    @Override
    public boolean support(DBCategoryEnum dbCategoryEnum) {
        return true;
    }

    @Override
    public String quotes(String value) {
        return value;
    }

    @Override
    public byte getByte(ResultSet rs, String colName) throws SQLException {
        return rs.getByte(colName);
    }

    @Override
    public byte[] getBytes(ResultSet rs, String colName) throws SQLException {
        return (byte[]) rs.getObject(colName);
    }

    @Override
    public long getLong(ResultSet rs, String colName) throws SQLException {
        return rs.getLong(colName);
    }

    @Override
    public int getInt(ResultSet rs, String colName) throws SQLException {
        return rs.getInt(colName);
    }

    @Override
    public short getShort(ResultSet rs, String colName) throws SQLException {
        return rs.getShort(colName);
    }

    @Override
    public float getFloat(ResultSet rs, String colName) throws SQLException {
        return rs.getFloat(colName);
    }

    @Override
    public double getDouble(ResultSet rs, String colName) throws SQLException {
        return rs.getDouble(colName);
    }

    @Override
    public boolean getBool(ResultSet rs, String colName) throws SQLException {
        Object value = rs.getObject(colName);

        if (value instanceof Integer && (Integer) value == 0) {
            return false;
        } else if (value instanceof Byte && (Byte) value == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public BigDecimal getBigDecimal(ResultSet rs, String colName) throws SQLException {
        return rs.getBigDecimal(colName);
    }

    @Override
    public Object getObject(ResultSet rs, String colName) throws SQLException {
        return rs.getObject(colName);
    }

    @Override
    public String getString(ResultSet rs, String colName) throws SQLException {
        return rs.getString(colName);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List getList(ResultSet rs, String colName) throws SQLException {
        String value = rs.getString(colName);
        return JSON.parseObject(value, List.class);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Set getSet(ResultSet rs, String colName) throws SQLException {
        String value = rs.getString(colName);
        return JSON.parseObject(value, Set.class);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map getMap(ResultSet rs, String colName) throws SQLException {
        String value = rs.getString(colName);
        return JSON.parseObject(value, new TypeReference<Map<String, Object>>() {
        });
    }

    @Override
    public <T> T getJson(ResultSet rs, String colName, Class<T> clazz) throws SQLException {
        String value = rs.getString(colName);

        if (value == null) {
            return null;
        }

        return JSON.parseObject(value, clazz);
    }

}
