package com.noseparte.common.db.service;

import com.noseparte.common.db.util.Pagination;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;


public interface GeneralService<T> {

    /**
     * 通过ID查询当前实体
     *
     * @param id
     * @return T
     */
    public T findOneById(String id) throws Exception;

    /**
     * 通过查询查询并分页
     *
     * @param query    条件
     * @param pageNo   起始条数
     * @param pageSize 查询多少条
     * @return 返回分页后Pagination实体
     */
    public Pagination<T> findPaginationByQuery(Query query, int pageNo,
                                               int pageSize) throws Exception;

    /**
     * 插入
     *
     * @param t
     */
    public void insert(T t) throws Exception;

    /**
     * 保存
     *
     * @param t
     */
    public void save(T t) throws Exception;

    /**
     * 查询并且修改记录
     *
     * @param query
     * @param update
     * @return 返回修改后的实体
     */
    public T findAndModify(Query query, Update update) throws Exception;

    /**
     * 查询并删除当前记录
     *
     * @param query
     * @return 返回删除的实体
     */
    public T findAndRemove(Query query) throws Exception;

    /**
     * 查询删除
     *
     * @param T当前删除的对象
     */
    public void remove(T t) throws Exception;

    /**
     * 修改查询后的第一条记录
     *
     * @param query
     * @param update
     */
    public void updateFirst(Query query, Update update) throws Exception;

    /**
     * 通过条件查询所有的记录
     *
     * @param query
     * @return
     */
    public List<T> find(Query query) throws Exception;

    /**
     * 通过ID获取记录,并且指定了集合名
     *
     * @param id
     * @param collectionName
     * @return
     */
    public T findByIdAndCollectionName(String id, String collectionName)
            throws Exception;

    /**
     * 通过条件find当前T
     */
    public T findOneByQuery(Query query) throws Exception;

    /**
     * 通过条件修改所有的记录
     *
     * @param query
     * @param update
     */
    public void updateAllByQuery(Query query, Update update) throws Exception;

    public Integer findCountByQuery(Query query) throws Exception;


}
