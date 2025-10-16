/**
 * 
 */
package me.duanbn.snailfish.core.orm;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.duanbn.snailfish.core.orm.rdb.AbstractDataObjectDao.DbField;
import me.duanbn.snailfish.core.orm.rdb.DataObjectUtil;
import me.duanbn.snailfish.core.orm.rdb.annotations.Table;

/**
 * @author bingnan.dbn
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Persistence {

    public static String getSqlStatement(List<Class> domainEntityClasses, List<Class> domainValueObjectClasses) {
        StringBuilder sql = new StringBuilder();

        if (domainEntityClasses != null && !domainEntityClasses.isEmpty()) {
            for (Class domainEntityClazz : domainEntityClasses) {

                Table tableAnno = (Table) domainEntityClazz.getAnnotation(Table.class);
                if (tableAnno == null) {
                    continue;
                }

                sql.append(getSql(domainEntityClazz, tableAnno));
            }
        }

        if (domainValueObjectClasses != null && !domainValueObjectClasses.isEmpty()) {
            for (Class domainValueObjectClazz : domainValueObjectClasses) {

                Table tableAnno = (Table) domainValueObjectClazz.getAnnotation(Table.class);
                if (tableAnno == null) {
                    continue;
                }

                sql.append(getSql(domainValueObjectClazz, tableAnno));
            }
        }

        return sql.toString();
    }

    private static String getSql(Class clazz, Table tableAnno) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS `" + tableAnno.value() + "`");
        sql.append("\n");
        sql.append("(\n");

        // field
        Collection<DbField> dbFields = DataObjectUtil.getFields(clazz, true);
        for (DbField dbField : dbFields) {
            sql.append("  `" + dbField.name + "` " + getFieldDbType(dbField));
            if (!dbField.primaryKey && dbField.nullable) {
                sql.append(" NULL");
            } else {
                sql.append(" NOT NULL");
            }
            if (dbField.primaryKey) {
                sql.append(" AUTO_INCREMENT");
            }
            sql.append(" COMMENT '" + dbField.comment + "'");
            sql.append(",\n");
        }

        // index
        for (DbField dbField : dbFields) {
            if (dbField.primaryKey) {
                sql.append("  PRIMARY KEY (`" + dbField.name + "`),\n");
            }
            if (dbField.index) {
                sql.append(
                        "  KEY " + "`idx_" + tableAnno.value() + "_" + dbField.name + "` (`" + dbField.name + "`),\n");
            }
        }

        sql.deleteCharAt(sql.length() - 2);
        sql.append(") COMMENT='" + tableAnno.comment() + "';\n\n");

        return sql.toString();

    }

    private static String getFieldDbType(DbField f) {
        if (f.original.getType() == Byte.class || f.original.getType() == Byte.TYPE) {
            return "tinyint";
        } else if (f.original.getType() == Short.class || f.original.getType() == Short.TYPE) {
            return "smallint";
        } else if (f.original.getType() == Integer.class || f.original.getType() == Integer.TYPE) {
            return "int";
        } else if (f.original.getType() == Long.class || f.original.getType() == Long.TYPE) {
            return "bigint";
        } else if (f.original.getType() == Float.class || f.original.getType() == Float.TYPE) {
            return "float";
        } else if (f.original.getType() == Double.class || f.original.getType() == Double.TYPE) {
            return "double";
        } else if (f.original.getType() == Boolean.class || f.original.getType() == Boolean.TYPE) {
            return "tinyint";
        } else if (f.original.getType().isEnum()) {
            return "varchar (" + f.length + ")";
        } else if (f.original.getType() == Date.class) {
            return "datetime";
        } else if (f.original.getType() == byte[].class || f.original.getType() == Byte[].class) {
            return "blob";
        } else if (f.jsonField) {
            return "varchar(1024)";
        } else if (f.original.getType() == List.class || f.original.getType() == Map.class
                || f.original.getType() == Set.class) {
            return "varchar(1024)";
        } else {
            return "varchar(" + f.length + ")";
        }
    }

}
