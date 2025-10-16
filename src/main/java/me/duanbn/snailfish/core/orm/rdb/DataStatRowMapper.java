package me.duanbn.snailfish.core.orm.rdb;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import me.duanbn.snailfish.core.DataNode;

public class DataStatRowMapper implements RowMapper<DataNode> {

    @SuppressWarnings("unchecked")
    @Override
    public DataNode mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            ResultSetMetaData rsMetaData = rs.getMetaData();

            int columnCount = rsMetaData.getColumnCount();

            DataNode dataNode = new DataNode();
            String columnName = null;
            for (int i = 1; i <= columnCount; i++) {
                columnName = rsMetaData.getColumnName(i);
                dataNode.put(columnName, rs.getObject(i));
            }

            return dataNode;
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

}
