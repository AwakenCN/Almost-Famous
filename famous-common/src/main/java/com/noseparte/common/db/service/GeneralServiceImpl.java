package com.noseparte.common.db.service;

import com.noseparte.common.db.dao.GeneralDao;
import com.noseparte.common.db.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("generalService")
public abstract class GeneralServiceImpl<T> implements GeneralService<T> {


    @Autowired
    protected GeneralDao<T> generalDao;

    public List<T> find(Query query) throws Exception {
        return generalDao.find(query);
    }

    public Pagination<T> findPaginationByQuery(Query query, int pageNo,
                                               int pageSize) throws Exception {

        return generalDao.findPaginationByQuery(query, pageNo, pageSize);
    }

    public void insert(T t) throws Exception {
        this.generalDao.insert(t);
    }

    public void save(T t) throws Exception {
        this.generalDao.save(t);
    }

    public void remove(T t) throws Exception {
        this.generalDao.remove(t);

    }

    public void updateFirst(Query query, Update update) throws Exception {
        this.generalDao.updateFirst(query, update);
    }

    public T findOneById(String id) throws Exception {

        return this.generalDao.findOneById(id);/*findOneById(id);*/
    }

    public T findAndModify(Query query, Update update) {
        return this.findAndModify(query, update);
    }

    public T findAndRemove(Query query) throws Exception {
        return (T) this.generalDao.findAndRemove(query);
    }

    public T findByIdAndCollectionName(String id, String collectionName)
            throws Exception {
        return (T) this.generalDao
                .findByIdAndCollectionName(id, collectionName);
    }

    public T findOneByQuery(Query query) throws Exception {
        // TODO Auto-generated method stub
        return (T) this.generalDao.findOneByQuery(query);
    }

    public void updateAllByQuery(Query query, Update update) {
        this.updateAllByQuery(query, update);

    }

    public Integer findCountByQuery(Query query) throws Exception {
        return generalDao.findCountByQuery(query);
    }

    public GeneralDao<T> getGeneralDao() {
        return generalDao;
    }

    public void setGeneralDao(GeneralDao<T> generalDao) {
        this.generalDao = generalDao;
    }


}
