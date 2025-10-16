package me.duanbn.snailfish.core.orm.rdb;

import java.util.List;

import me.duanbn.snailfish.core.DataNode;

public interface DataStatDao {

    List<DataNode> execute(String sql, List<Object> params);

}
