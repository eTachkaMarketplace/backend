package com.sellbycar.marketplace.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PredicateBuilder {

    private final List<Predicate> predicates = new ArrayList<>();
    private final CriteriaBuilder criteriaBuilder;

    @NotNull
    public <Y extends Comparable<? super Y>> PredicateBuilder equal(Path<Y> path, @Nullable Y value) {
        if (value != null) predicates.add(criteriaBuilder.equal(path, value));
        return this;
    }

    @NotNull
    public <Y extends Comparable<? super Y>> PredicateBuilder between(Path<Y> path, @Nullable Y min, @Nullable Y max) {
        if (min != null && max != null) {
            predicates.add(criteriaBuilder.between(path, min, max));
        } else if (min != null) {
            greaterEq(path, min);
        } else if (max != null) {
            lessEq(path, max);
        }
        return this;
    }

    @NotNull
    public <Y extends Comparable<? super Y>> PredicateBuilder greater(Path<Y> path, @Nullable Y value) {
        if (value != null) predicates.add(criteriaBuilder.greaterThan(path, value));
        return this;
    }

    @NotNull
    public <Y extends Comparable<? super Y>> PredicateBuilder greaterEq(Path<Y> path, @Nullable Y value) {
        if (value != null) predicates.add(criteriaBuilder.greaterThanOrEqualTo(path, value));
        return this;
    }

    @NotNull
    public <Y extends Comparable<? super Y>> PredicateBuilder less(Path<Y> path, @Nullable Y value) {
        if (value != null) predicates.add(criteriaBuilder.lessThan(path, value));
        return this;
    }

    @NotNull
    public <Y extends Comparable<? super Y>> PredicateBuilder lessEq(Path<Y> path, @Nullable Y value) {
        if (value != null) predicates.add(criteriaBuilder.lessThanOrEqualTo(path, value));
        return this;
    }

    @NotNull
    public PredicateBuilder like(Path<String> path, @Nullable String pattern) {
        if (pattern != null) predicates.add(criteriaBuilder.like(path, pattern));
        return this;
    }

    @NotNull
    public Predicate build() {
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
