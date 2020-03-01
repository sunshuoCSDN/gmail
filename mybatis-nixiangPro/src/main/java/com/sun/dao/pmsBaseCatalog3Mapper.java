package com.sun.dao;

import com.sun.entity.pmsBaseCatalog3;

public interface pmsBaseCatalog3Mapper {
    int deleteByPrimaryKey(Long id);

    int insert(pmsBaseCatalog3 record);

    int insertSelective(pmsBaseCatalog3 record);

    pmsBaseCatalog3 selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(pmsBaseCatalog3 record);

    int updateByPrimaryKey(pmsBaseCatalog3 record);
}