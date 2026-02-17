package com.sme.utils;


import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * List<Long> 与 逗号分隔字符串 互转的MyBatis类型处理器
 * 注意：public类名必须与文件名一致 → ListToStringTypeHandler.java
 */
@MappedTypes(List.class) // 处理Java类型：List
@MappedJdbcTypes(JdbcType.VARCHAR) // 处理JDBC类型：VARCHAR
public class ListToStringTypeHandler extends BaseTypeHandler<List<Long>> {

    /**
     * 写入数据库：List<Long> → 逗号分隔字符串（如 [1,2,3] → "1,2,3"）
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Long> parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null || parameter.isEmpty()) {
            ps.setString(i, "");
            return;
        }
        // 将Long列表转成逗号分隔的字符串
        String str = parameter.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        ps.setString(i, str);
    }

    /**
     * 从数据库读取：字符串 → List<Long>（如 "1,2,3" → [1,2,3]）
     */
    @Override
    public List<Long> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return stringToList(rs.getString(columnName));
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return stringToList(rs.getString(columnIndex));
    }

    @Override
    public List<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return stringToList(cs.getString(columnIndex));
    }

    /**
     * 工具方法：字符串转Long列表
     */
    private List<Long> stringToList(String str) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        // 按逗号分割并转成Long列表
        return Arrays.stream(str.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
