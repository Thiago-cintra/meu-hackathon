package com.stefanini.repository;


import javax.enterprise.context.ApplicationScoped;

import com.stefanini.dao.GenericDAO;
import com.stefanini.entity.Stefamon;

@ApplicationScoped
public class StefamonRepository extends GenericDAO<Stefamon, Long> {
}
