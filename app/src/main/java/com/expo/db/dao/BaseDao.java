package com.expo.db.dao;

import android.content.ContentValues;

import com.expo.db.QueryParams;

import java.util.List;

public interface BaseDao {

    /**
     * 保存或更新数据
     *
     * @param obj 数据
     * @param <T>
     * @return 成功true
     */
    <T> boolean saveOrUpdate(T obj);

    /**
     * 保存或更新所有数据
     *
     * @param objs 要保存的数据
     * @param <T>
     */
    <T> void saveOrUpdateAll(List<T> objs);

    /**
     * 清除指定类型的所有数据
     *
     * @param clazz 类型
     * @param <T>
     * @return 清除成功true
     */
    <T> boolean clear(Class<T> clazz);

    /**
     * 是否有符合样例条件的数据
     *
     * @param t   样例
     * @param <T>
     * @return 有true
     */
    <T> boolean exists(T t);

    /**
     * 查询匹配样例的数据
     *
     * @param t   样例
     * @param <T>
     * @return 符合条件的数据集合
     */
    <T> List<T> queryForMatching(T t);

    /**
     * 查询匹配样例的数据
     *
     * @param t   样例
     * @param <T>
     * @return 符合条件的数据
     */
    <T> T uniqueForMatching(T t);

    /**
     * 删除指定数据
     *
     * @param t   数据
     * @param <T>
     * @return 成功true
     */
    <T> boolean delete(T t);

    /**
     * 删除所有
     *
     * @param list
     * @return
     */
    <T> void deleteAll(List<T> list);

    /**
     * 删除指定id的数据
     *
     * @param clazz 数据类
     * @param id    数据id
     * @param <T>   数据类型
     * @param <ID>  id的数据类型
     * @return 成功true
     */
    <T, ID> boolean delete(Class<T> clazz, ID id);

    /**
     * 查询指定id的数据
     *
     * @param clazz 类型
     * @param id    ID
     * @param <T>
     * @param <ID>
     * @return
     */
    <T, ID> T queryById(Class<T> clazz, ID id);

    /**
     * 按照指定条件查询
     *
     * @param clazz       类型
     * @param queryParams
     * @return
     */
    <T> List<T> query(Class<T> clazz, QueryParams queryParams);

    /**
     * 按照指定条件查询唯一项，如果有多项，则返回第一项
     *
     * @param clazz       类型
     * @param queryParams
     * @return
     */
    <T> T unique(Class<T> clazz, QueryParams queryParams);

    /**
     * 按照指定条件查询总数
     *
     * @param clazz       类型
     * @param queryParams
     * @return
     */
    <T> long count(Class<T> clazz, QueryParams queryParams);

    /**
     * 按照指定条件更新数据
     *
     * @param clazz       类型
     * @param queryParams
     * @return
     */
    <T> int update(Class<T> clazz, ContentValues cv, QueryParams queryParams);

}
