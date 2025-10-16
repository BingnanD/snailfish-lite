package me.duanbn.snailfish.core.orm.rdb.dialect;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * database dialect for adapter different database sql.
 * 
 * @author bingnan.dbn
 */
public interface Dialect {

    boolean support(DBCategoryEnum dbCategoryEnum);

    String quotes(String value);

    byte getByte(ResultSet rs, String colName) throws SQLException;

    byte[] getBytes(ResultSet rs, String colName) throws SQLException;

    long getLong(ResultSet rs, String colName) throws SQLException;

    int getInt(ResultSet rs, String colName) throws SQLException;

    short getShort(ResultSet rs, String colName) throws SQLException;

    float getFloat(ResultSet rs, String colName) throws SQLException;

    double getDouble(ResultSet rs, String colName) throws SQLException;

    boolean getBool(ResultSet rs, String colName) throws SQLException;

    BigDecimal getBigDecimal(ResultSet rs, String colName) throws SQLException;

    Object getObject(ResultSet rs, String colName) throws SQLException;

    String getString(ResultSet rs, String colName) throws SQLException;

    @SuppressWarnings("rawtypes")
    List getList(ResultSet rs, String colName) throws SQLException;

    @SuppressWarnings("rawtypes")
    Set getSet(ResultSet rs, String colName) throws SQLException;

    @SuppressWarnings("rawtypes")
    Map getMap(ResultSet rs, String colName) throws SQLException;

    <T> T getJson(ResultSet rs, String colName, Class<T> clazz) throws SQLException;

    public static enum DBCategoryEnum {

        H2,
        TDDL,
        MYSQL,
        POSTGRESQL,
        DM,
        OTHER;

    }

}
