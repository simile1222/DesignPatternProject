package org.example.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchCondition {
    private String model;       // 모델명
    private Boolean isRented;   // 대여 중 여부 필터
    private Double minPrice;    // 최소 가격
    private Double maxPrice;    // 최대 가격
    private Integer parkingId;  // 특정 주차장 필터
}
