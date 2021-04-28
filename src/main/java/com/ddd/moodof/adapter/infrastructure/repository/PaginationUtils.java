package com.ddd.moodof.adapter.infrastructure.repository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PaginationUtils {
    public Sort getSort(String sortBy, boolean descending) {
        Sort sort = Sort.by(sortBy);
        if (descending) {
            return sort.descending();
        }
        return sort.ascending();
    }

    public long getTotalPageCount(long count, int pageSize) {
        return (long) Math.ceil((double) count / pageSize);
    }
}
