package me.duanbn.snailfish.core.orm.rdb.dialect;

/**
 * postgresql database dialect impl.
 * 
 * @author bingnan.dbn
 */
public class PostgresqlDialect extends GenericDialect {

    public static final String quotes = "\"";

    @Override
    public boolean support(DBCategoryEnum dbCategoryEnum) {
        return DBCategoryEnum.POSTGRESQL == dbCategoryEnum;
    }

    @Override
    public String quotes(String value) {
        return quotes + value + quotes;
    }

}
