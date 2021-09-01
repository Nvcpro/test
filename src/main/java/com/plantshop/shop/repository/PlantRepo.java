package com.plantshop.shop.repository;

import com.plantshop.shop.model.Plant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantRepo extends JpaRepository<Plant,Long> {
	@Query("select p,sum(od.quantity) as total from Plant p left join OrderDetails od on od.plant.id=p.id group by p.id order by total desc")
	List<Plant> getTopSellingPlants(Pageable pageable);
}
