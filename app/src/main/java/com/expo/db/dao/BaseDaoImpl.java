package com.expo.db.dao;

import android.content.ContentValues;

import com.expo.base.BaseApplication;
import com.expo.base.utils.LogUtils;
import com.expo.db.DBUtil;
import com.expo.db.QueryParams;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.StatementBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class BaseDaoImpl implements BaseDao {

    private static final String TAG = "BaseDaoImpl";

    @Override
    public <T> boolean clear(Class<T> clazz) {
        try {
            Dao<T, ?> dao = DBUtil.getDao(BaseApplication.getApplication(), clazz);
            int count = dao.deleteBuilder().delete();
            if (count > 0) {
                return true;
            }
        } catch (SQLException e) {
            LogUtils.e(TAG, e);
        }
        return false;
    }

    @Override
    public <T> boolean exists(T t) {
        try {
            Dao dao = DBUtil.getDao(BaseApplication.getApplication(), t.getClass());
            List<T> result = dao.queryForMatching(t);
            if (result != null && !result.isEmpty()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public <T> List<T> queryForMatching(T t) {
        try {
            Dao dao = DBUtil.getDao(BaseApplication.getApplication(), t.getClass());
            List<T> ts = dao.queryForMatching(t);
            return ts;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T uniqueForMatching(T t) {
        List<T> ts = queryForMatching(t);
        if (ts != null && !ts.isEmpty()) {
            return ts.get(0);
        }
        return null;
    }

    @Override
    public boolean delete(Object t) {
        try {
            Dao dao = DBUtil.getDao(BaseApplication.getApplication(), t.getClass());
            int count = dao.delete(t);
            if (count > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public <T, ID> boolean delete(Class<T> clazz, ID id) {
        try {
            Dao<T, ID> dao = DBUtil.getDao(BaseApplication.getApplication(), clazz);
            int count = dao.deleteById(id);
            if (count > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public <T, ID> T queryById(Class<T> clazz, ID id) {
        try {
            Dao<T, ID> dao = DBUtil.getDao(BaseApplication.getApplication(), clazz);
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> boolean saveOrUpdate(T obj) {
        try {
            Dao dao = DBUtil.getDao(BaseApplication.getApplication(), obj.getClass());
            Dao.CreateOrUpdateStatus status = dao.createOrUpdate(obj);
            if (status.getNumLinesChanged() > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public <T> void saveOrUpdateAll(List<T> objs) {
        if (objs == null || objs.isEmpty()) return;
        try {
            Dao dao = DBUtil.getDao(BaseApplication.getApplication(), objs.get(0).getClass());
            for (Object obj : objs) {
                dao.createOrUpdate(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> List<T> query(Class<T> clazz, QueryParams queryParams) {
        try {
            Dao<T, ?> dao = DBUtil.getDao(BaseApplication.getApplication(), clazz);
            if (queryParams == null) {
                return dao.queryForAll();
            }
            QueryBuilder<T, ?> queryBuilder = dao.queryBuilder();
            setQueryParams(queryBuilder, queryParams);
            return queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T unique(Class<T> clazz, QueryParams queryParams) {
        try {
            Dao<T, ?> dao = DBUtil.getDao(BaseApplication.getApplication(), clazz);
            if (queryParams == null) {
                return dao.queryBuilder().queryForFirst();
            }
            QueryBuilder<T, ?> builder = dao.queryBuilder();
            setQueryParams(builder, queryParams);
            return builder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> long count(Class<T> clazz, QueryParams queryParams) {
        try {
            Dao<T, ?> dao = DBUtil.getDao(BaseApplication.getApplication(), clazz);
            if (queryParams == null) {
                return dao.countOf();
            }
            QueryBuilder<T, ?> builder = dao.queryBuilder();
            setQueryParams(builder, queryParams);
            return builder.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public <T> int update(Class<T> clazz, ContentValues cv, QueryParams queryParams) {
        if (cv == null || cv.size() == 0) return 0;
        try {
            Dao<T, ?> dao = DBUtil.getDao(BaseApplication.getApplication(), clazz);
            UpdateBuilder<T, ?> updateBuilder = dao.updateBuilder();
            for (String column : cv.keySet()) {
                updateBuilder.updateColumnValue(column, cv.get(column));
            }
            setQueryParams(updateBuilder, queryParams);
            return updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public <T> void deleteAll(List<T> list) {
        for (T t : list) {
            delete(t);
        }
    }

    private <T, ID> void setQueryParams(StatementBuilder<T, ID> builder, QueryParams queryParams) throws SQLException {
        if (builder instanceof QueryBuilder && !queryParams.getRestrictions().isEmpty()) {
            setProjections((QueryBuilder<T, ID>) builder, queryParams.getRestrictions());
        }
        if (!queryParams.getProjections().isEmpty()) {
            setSelections(builder.where(), queryParams.getProjections());
        }
    }

    private <T, ID> void setSelections(Where<T, ID> where, List<QueryParams.Selection> selections) throws SQLException {
        for (QueryParams.Selection selection : selections) {
            if ("and".equals(selection.type)) {
                if (selection.value1 != null) {
                    QueryParams[] queryParams = (QueryParams[]) selection.value1;
                    if (queryParams.length == 0) {
                        where.and();
                    } else if (queryParams.length < 2) {
                        throw new IllegalArgumentException("and条件的子where语句数量至少为两个，当前数量：" + queryParams.length);
                    } else {
                        Where<T, ID>[] wheres = new Where[queryParams.length];
                        for (int i = 0; i < queryParams.length; i++) {
                            wheres[i] = where;
                            setSelections(wheres[i], queryParams[i].getProjections());
                        }
                        if (wheres.length > 2) {
                            where.and(wheres[0], wheres[1], Arrays.copyOfRange(wheres, 2, wheres.length));
                        } else {
                            where.and(wheres[0], wheres[1]);
                        }
                    }
                } else {
                    where.and();
                }
            } else if ("or".equals(selection.type)) {
                if (selection.value1 != null) {
                    QueryParams[] queryParams = (QueryParams[]) selection.value1;
                    if (queryParams.length == 0) {
                        where.or();
                    } else if (queryParams.length < 2) {
                        throw new IllegalArgumentException("or条件的子where语句数量至少为两个，当前数量：" + queryParams.length);
                    } else {
                        Where<T, ID>[] wheres = new Where[queryParams.length];
                        for (int i = 0; i < queryParams.length; i++) {
                            wheres[i] = where;
                            setSelections(wheres[i], queryParams[i].getProjections());
                        }
                        if (wheres.length > 2) {
                            where.or(wheres[0], wheres[1], Arrays.copyOfRange(wheres, 2, wheres.length));
                        } else {
                            where.or(wheres[0], wheres[1]);
                        }
                    }
                } else {
                    where.or();
                }
            } else if ("eq".equals(selection.type)) {
                where.eq(selection.columnName, selection.value1);
            } else if ("ne".equals(selection.type)) {
                where.ne(selection.columnName, selection.value1);
            } else if ("ge".equals(selection.type)) {
                where.ge(selection.columnName, selection.value1);
            } else if ("gt".equals(selection.type)) {
                where.gt(selection.columnName, selection.value1);
            } else if ("le".equals(selection.type)) {
                where.le(selection.columnName, selection.value1);
            } else if ("lt".equals(selection.type)) {
                where.lt(selection.columnName, selection.value1);
            } else if ("like".equals(selection.type)) {
                where.like(selection.columnName, selection.value1);
            } else if ("between".equals(selection.type)) {
                where.between(selection.columnName, selection.value1, selection.value2);
            } else if ("in".equals(selection.type)) {
                if (selection.value1 instanceof Iterable) {
                    where.in(selection.columnName, ((Iterable) selection.value1));
                } else {
                    where.in(selection.columnName, selection.value1);
                }
            } else if ("notIn".equals(selection.type)) {
                where.notIn(selection.columnName, selection.value1);
            } else if ("null".equals(selection.type)) {
                where.isNull(selection.columnName);
            } else if ("notNull".equals(selection.type)) {
                where.isNotNull(selection.columnName);
            }
        }
    }

    private <T, ID> void setProjections(QueryBuilder<T, ID> queryBuilder, List<QueryParams.Selection> projections) throws SQLException {
        for (QueryParams.Selection selection : projections) {
            if ("orderBy".equals(selection.type)) {
                boolean asc = true;
                if (selection.value1 instanceof Boolean) {
                    asc = (boolean) selection.value1;
                } else if ("asc".equals(selection.value1) || "true".equals(selection.value1)) {
                    asc = true;
                } else if ("desc".equals(selection.value1) || "false".equals(selection.value1)) {
                    asc = false;
                } else {
                    throw new UnsupportedOperationException("orderBy support true/false or string value 'asc'/'desc'/'true'/'false'. current value:" + selection.value1);
                }
                queryBuilder.orderBy(selection.columnName, asc);
            } else if ("groupBy".equals(selection.type)) {
                queryBuilder.groupBy(selection.columnName);
            } else if ("limit".equals(selection.type)) {
                Long offset = null;
                if (selection.value1 instanceof String) {
                    offset = Long.parseLong((String) selection.value1);
                } else if (selection.value1 instanceof Integer) {
                    offset = ((Integer) selection.value1).longValue();
                } else if (selection.value1 instanceof Long) {
                    offset = (Long) selection.value1;
                }
                if (offset != null && offset >= 0) {
                    queryBuilder.offset(offset);
                }
                Long limit = null;
                if (selection.value2 instanceof String) {
                    limit = Long.parseLong((String) selection.value2);
                } else if (selection.value2 instanceof Integer) {
                    limit = ((Integer) selection.value2).longValue();
                } else if (selection.value2 instanceof Long) {
                    limit = (Long) selection.value2;
                }
                if (limit != null && limit >= 0) {
                    queryBuilder.limit(limit);
                }
            } else if ("distinct".equals(selection.type)) {
                queryBuilder.distinct();
            } else if ("having".equals(selection.type)) {
                queryBuilder.having(selection.value1.toString());
            }
        }
    }
}
