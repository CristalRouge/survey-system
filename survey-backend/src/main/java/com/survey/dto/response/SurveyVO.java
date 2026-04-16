package com.survey.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SurveyVO {
    private Long id;
    private String title;
    private String description;
    private String coverImage;
    private String status;
    private Long userId;
    private String username;
    private Integer responseCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<QuestionVO> questions;

    @Data
    public static class QuestionVO {
        private Long id;
        private String type;
        private String title;
        private String description;
        private String image;
        private Boolean required;
        private String placeholder;
        private Integer sort;
        private List<OptionVO> options;
    }

    @Data
    public static class OptionVO {
        private Long id;
        private String text;
        private String image;
        private Integer sort;
    }
}
