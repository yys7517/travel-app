package com.example.travel.service;

import com.example.travel.entity.Destination;
import com.example.travel.repository.DestinationRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DestinationService {

  private final DestinationRepository destinationRepository;

  // 전체 조회 (Redis 캐싱, key: destinations)
  @Cacheable(value = "destinations")
  public List<Destination> getAllDestinations() {
    log.info("전체 여행지 조회 - DB 조회");    // 캐싱된 데이터 가져올 때 로그 출력 x
    return destinationRepository.findAll();
  }

  // 단건 조회
  @Cacheable(value = "destinations", key = "#id")  //  캐시 저장 : destinations::{id}
  public Destination getDestination(Long id) {
    log.info("여행지 조회 - ID: {}", id);
    return destinationRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("여행지를 찾을 수 없습니다."));
  }

  // 생성
  @Transactional
  public Destination createDestination(Destination destination) {
    log.info("여행지 생성 - {}", destination.getName());
    log.info("여행지 정보 : {}", destination);
    return destinationRepository.save(destination);
  }

  // 수정
  @Transactional
  @CacheEvict(value = "destinations", allEntries = true)   // 수정할 때 전체 캐시 삭제하는게 좋음
  @CachePut(value = "destinations", key = "#id")   // 캐시 수정
  public Destination updateDestination(Long id, Destination updateData) {
    log.info("여행지 수정 - ID: {}", id);
    Destination destination = getDestination(id);
    destination.setName(updateData.getName());
    destination.setCountry(updateData.getCountry());
    destination.setDescription(updateData.getDescription());
    destination.setRating(updateData.getRating());
    return destinationRepository.save(destination);
  }

  // 삭제
  @Transactional
  @CacheEvict(value = "destinations", allEntries = true)   // Redis 캐시 삭제
  public void deleteDestination(Long id) {
    log.info("여행지 삭제 - ID: {}", id);
    destinationRepository.deleteById(id);
  }

  // 국가로 검색
  public List<Destination> getDestinationsByCountry(String country) {
    log.info("국가별 여행지 조회 - {}", country);
    return destinationRepository.findByCountry(country);
  }

  // 키워드 검색
  public List<Destination> searchDestinations(String keyword) {
    log.info("여행지 검색 - 키워드: {}", keyword);
    return destinationRepository.findByNameContaining(keyword);
  }

  // 평점 높은 순
  public List<Destination> getTopRatedDestinations() {
    log.info("평점 높은 여행지 조회");
    return destinationRepository.findAllOrderByRatingDesc();
  }
}