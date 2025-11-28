// package me.duanbn.snailfish.core.orm.rdb.dialect;

// import java.sql.ResultSet;
// import java.sql.SQLException;

// import dm.jdbc.driver.DmdbBlob;
// import dm.jdbc.util.FileUtil;

// /**
// * dameng database dialect impl.
// *
// * @author bingnan.dbn
// */
// public class DaMengDialect extends GenericDialect {

// public static final String quotes = "\"";

// @Override
// public boolean support(DBCategoryEnum dbCategoryEnum) {
// return DBCategoryEnum.DM == dbCategoryEnum;
// }

// @Override
// public String quotes(String value) {
// return quotes + value + quotes;
// }

// @Override
// public byte[] getBytes(ResultSet rs, String colName) throws SQLException {
// DmdbBlob blob = (DmdbBlob) rs.getObject(colName);
// if (blob != null) {
// byte[] data = FileUtil.readByteArray(blob.getBinaryStream());
// return data;
// }

// return null;
// }

// }
