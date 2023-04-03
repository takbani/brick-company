package com.caci.technical.test.bricks.repository;

import com.caci.technical.test.bricks.model.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    @Transactional
    @Modifying
    @Query("update Order o set o.numberOfBricks = ?1 where o.orderRef = ?2 ")
    int updateNumberOfBricksOnOrder(int numberOfBricks, Long orderRef);
}
