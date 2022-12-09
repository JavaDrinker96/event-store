package com.modsen.eventstore.dto.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PaginationCriteria {

    private Integer page;

    private Integer size;

}