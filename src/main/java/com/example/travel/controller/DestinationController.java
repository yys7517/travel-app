package com.example.travel.controller;

import com.example.travel.entity.Destination;
import com.example.travel.service.DestinationService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/destinations")
@RequiredArgsConstructor
public class DestinationController {

  private final DestinationService destinationService;

  // 전체 조회
  @GetMapping
  public Map<String, Object> getAllDestinations() {
    long startTime = System.currentTimeMillis();

    List<Destination> destinations = destinationService.getAllDestinations();

    long endTime = System.currentTimeMillis();
    long duration = endTime - startTime;

    Map<String, Object> response = new HashMap<>();
    response.put("destinations", destinations);
    response.put("responseTime", duration);

    return response;
  }

  // 단건 조회
  @GetMapping("/{id}")
  public Map<String, Object> getDestination(@PathVariable Long id) {
    long startTime = System.currentTimeMillis();

    Destination destination = destinationService.getDestination(id);

    long endTime = System.currentTimeMillis();
    long duration = endTime - startTime;

    Map<String, Object> response = new HashMap<>();
    response.put("destination", destination);
    response.put("responseTime", duration);
    return response;
  }

  // 생성
  @PostMapping
  public Destination createDestination(@RequestBody Destination destination) {
    return destinationService.createDestination(destination);
  }

  // 수정
  @PutMapping("/{id}")
  public Destination updateDestination(
      @PathVariable Long id,
      @RequestBody Destination destination) {
    return destinationService.updateDestination(id, destination);
  }

  // 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDestination(@PathVariable Long id) {
    destinationService.deleteDestination(id);
    return ResponseEntity.ok().build();
  }

  // 국가로 검색
  @GetMapping("/country/{country}")
  public List<Destination> getDestinationsByCountry(@PathVariable String country) {
    return destinationService.getDestinationsByCountry(country);
  }

  // 키워드 검색
  @GetMapping("/search")
  public List<Destination> searchDestinations(@RequestParam String keyword) {
    return destinationService.searchDestinations(keyword);
  }

  // 평점 높은 순
  @GetMapping("/top-rated")
  public List<Destination> getTopRatedDestinations() {
    return destinationService.getTopRatedDestinations();
  }
}