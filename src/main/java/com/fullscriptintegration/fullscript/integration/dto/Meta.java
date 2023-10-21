package com.fullscriptintegration.fullscript.integration.dto;

import lombok.Data;

@Data
public class Meta {
    private Integer current_page;
    private Integer next_page;
    private Integer prev_page;
    private Integer total_pages;
    private Integer total_count;
}