package me.duanbn.snailfish.core.orm.rdb.dialect;

/**
 * mysql database dialect impl.
 * 
 * @author bingnan.dbn
 */
public class MysqlDialect extends GenericDialect {

    private static final String quotes = "`";

    @Override
    public boolean support(DBCategoryEnum dbCategoryEnum) {
        return DBCategoryEnum.MYSQL == dbCategoryEnum;
    }

    @Override
    public String quotes(String value) {
        if (value.contains(".")) {
            String[] split = value.split("\\.");
            return split[0] + "." + quotes + split[1] + quotes;
        }
        return quotes + value + quotes;
    }

}
