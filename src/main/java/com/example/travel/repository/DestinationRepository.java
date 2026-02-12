package com.example.travel.repository;

import com.example.travel.entity.Destination;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {

    // 국가로 검색
    List<Destination> findByCountry(String country);

    // 이름에 키워드 포함된 여행지 검색
    List<Destination> findByNameContaining(String keyword);

    // 평점 이상인 여행지
    List<Destination> findByRatingGreaterThanEqual(BigDecimal rating);

    // 평점 높은 순으로 정렬
    @Query("SELECT d FROM Destination d ORDER BY d.rating DESC")
    List<Destination> findAllOrderByRatingDesc();
}