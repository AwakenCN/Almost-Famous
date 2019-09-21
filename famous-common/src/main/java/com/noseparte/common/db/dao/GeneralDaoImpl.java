package com.noseparte.common.db.dao;

import com.noseparte.common.db.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("generalDao")
public abstract class GeneralDaoImpl<T> implements GeneralDao<T> {

    @Autowired
    private MongoTemplate mongoTemplate;

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<T> find(Query query) {
        return mongoTemplate.find(query, this.getEntityClass());
    }

    public Pagination<T> findPaginationByQuery(Query query, int pageNo,
                                               int pageSize) {
        if (query == null) {
            query = new Query();
        }
        long totalCount = this.mongoTemplate
                .count(query, this.getEntityClass());
        Pagination<T> page = new Pagination<T>(pageNo, pageSize, totalCount);
        query.skip(page.getFirstResult());// skip相当于从那条记录.
        query.limit(pageSize);// 从skip,取多少条记录
        List<T> datas = this.find(query);
        page.setDatas(datas);
        return page;
    }

    public void insert(T t) {
        this.mongoTemplate.insert(t);
    }

    public void save(T t) {
        this.mongoTemplate.save(t);
    }

    public void remove(T t) {
        this.mongoTemplate.remove(t);

    }

    public void updateFirst(Query query, Update update) {
        this.mongoTemplate.updateFirst(query, update, this.getEntityClass());
    }

    public T findOneById(String id) {

        return this.mongoTemplate.findById(id, this.getEntityClass());
    }

    public T findAndModify(Query query, Update update) {
        return this.mongoTemplate.findAndModify(query, update,
                this.getEntityClass());
    }

    public T findAndRemove(Query query) {
        return this.mongoTemplate.findAndRemove(query, this.getEntityClass());
    }

    public T findByIdAndCollectionName(String id, String collectionName) {
        return this.mongoTemplate.findById(id, this.getEntityClass(),
                collectionName);
    }

    public T findOneByQuery(Query query) {
        // TODO Auto-generated method stub
        return this.mongoTemplate.findOne(query, this.getEntityClass());
    }

    public void updateAllByQuery(Query query, Update update) {
        this.mongoTemplate.updateMulti(query, update, this.getEntityClass());

    }

    public Integer findCountByQuery(Query query) {
        Long totalCount = this.mongoTemplate
                .count(query, this.getEntityClass());
        return Integer.parseInt(String.valueOf(totalCount));
    }

    protected abstract Class<T> getEntityClass();


}
