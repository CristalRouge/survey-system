package com.survey.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class SurveyCreateRequest {
    @NotBlank(message = "问卷标题不能为空")
    private String title;

    private String description;
    private String coverImage;
    private List<QuestionRequest> questions;

    @Data
    public static class QuestionRequest {
        @NotBlank(message = "问题标题不能为空")
        private String title;
        private String type = "single";
        private String description;
        private String image;
        private Boolean required = false;
        private String placeholder;
        private Integer sort;
        private List<OptionRequest> options;
    }

    @Data
    public static class OptionRequest {
        private String text;
        private String image;
        private Integer sort;
    }
}
