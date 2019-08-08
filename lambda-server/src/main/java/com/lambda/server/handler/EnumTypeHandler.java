package com.lambda.server.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;  
import org.apache.ibatis.type.JdbcType;  

public class EnumTypeHandler<E extends Enum<?> & CodeBaseEnum> extends BaseTypeHandler<CodeBaseEnum> {
    
	private Class<E> clazz;
 
    public EnumTypeHandler(Class<E> enumType) {
        if (enumType == null) throw new IllegalArgumentException("Type argument cannot be null");
        this.clazz = enumType;
    }
 
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, CodeBaseEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.name());
    }
 
    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return CodeEnumUtil.codeOf(clazz, rs.getString(columnName));
    }
 
    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return CodeEnumUtil.codeOf(clazz, rs.getString(columnIndex));
    }
 
    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return CodeEnumUtil.codeOf(clazz, cs.getString(columnIndex));
    }
}