package com.ooad.bookinghotel.HotelDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HotelSpec {

    //    private EntityManager entityManager;
//    @PersistenceUnit
//    @Inject
//    private EntityManagerFactory emf;
//

//    private EntityManager entityManager;

    public List<Hotel> QueryTest(Integer star, String name){

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jcg-JPA");
        EntityManager entityManager = emf.createEntityManager();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Hotel> query = cb.createQuery(Hotel.class);
        Root<Hotel> root = query.from(Hotel.class);

        List<Predicate> predicates = new ArrayList<>();

        if (star != null) {
            predicates.add(cb.equal(root.get("star"), star));
        }

        if (name != null) {
            predicates.add(cb.equal(root.get("name"), name));
        }

        query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        List<Hotel> result = entityManager.createQuery(query).getResultList();

        return result;
    }

}
