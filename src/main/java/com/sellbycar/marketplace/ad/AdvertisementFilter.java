package com.sellbycar.marketplace.ad;

import com.sellbycar.marketplace.util.PredicateBuilder;
import lombok.Data;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

@Data
@Nullable
public class AdvertisementFilter {

    private String category;
    private String brand;
    private String model;
    private String region;
    private String city;
    private Integer yearMin;
    private Integer yearMax;
    private BigDecimal priceMin;
    private BigDecimal priceMax;
    private String bodyType;
    private String engineType;
    private Float engineVolumeMin;
    private Float engineVolumeMax;
    private Long mileageMax;
    private String driveType;
    private String transmissionType;
    private String technicalState;
    private String color;

    public Specification<AdvertisementDAO> toSpecification() {
        return (root, query, criteriaBuilder) -> new PredicateBuilder(criteriaBuilder)
                .equal(root.get("category"), category)
                .equal(root.get("car").get("brand"), brand)
                .equal(root.get("car").get("model"), model)
                .equal(root.get("region"), region)
                .equal(root.get("city"), city)
                .between(root.get("car").get("year"), yearMin, yearMax)
                .between(root.get("car").get("price"), priceMin, priceMax)
                .equal(root.get("car").get("bodyType"), bodyType)
                .equal(root.get("car").get("engineType"), engineType)
                .greaterEq(root.get("car").get("engineVolume"), engineVolumeMin)
                .lessEq(root.get("car").get("engineVolume"), engineVolumeMax)
                .lessEq(root.get("car").get("mileage"), mileageMax)
                .equal(root.get("car").get("driveType"), driveType)
                .equal(root.get("car").get("transmissionType"), transmissionType)
                .equal(root.get("car").get("technicalState"), technicalState)
                .equal(root.get("car").get("color"), color)
                .build();
    }
}
