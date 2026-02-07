package com.sme.utils;

import com.github.pagehelper.PageInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 分页工具类
 */
public class PageUtils {

    /**
     * 将分页信息封装到Map中
     * @param pageInfo
     * @return
     */
    public static <T> Map<String, Object> toPageResult(PageInfo<T> pageInfo){

        HashMap<String, Object> result = new HashMap<>();
        //当前页的数据列表
        result.put("list", pageInfo.getList());
        //总记录数
        result.put("total", pageInfo.getTotal());
        //当前页码
        result.put("pageNum", pageInfo.getPageNum());
        //每页记录数
        result.put("pageSize", pageInfo.getPageSize());
        //总页数
        result.put("pages", pageInfo.getPages());
        //是否存在下一页
        result.put("hasNextPage", pageInfo.isHasNextPage());
        //是否存在上一页
        result.put("hasPreviousPage", pageInfo.isHasPreviousPage());

        return result;
    }

}
