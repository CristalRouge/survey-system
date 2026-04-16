package com.survey.dto.response;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class SurveyStatsVO {
    private Long surveyId;
    private String title;
    private Integer totalResponses;
    private Integer todayResponses;
    private Double completionRate;
    private List<QuestionStats> questionStats;

    @Data
    public static class QuestionStats {
        private Long questionId;
        private String questionTitle;
        private String questionType;
        private Integer totalAnswers;
        private List<OptionStats> optionStats;
        private List<AnswerDetail> textAnswers;
    }

    @Data
    public static class OptionStats {
        private Long optionId;
        private String optionText;
        private Integer count;
        private Double percentage;
    }

    @Data
    public static class AnswerDetail {
        private Long responseId;
        private String value;
        private String submitTime;
    }
}
