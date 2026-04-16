package com.survey.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("survey_response")
public class SurveyResponse {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long surveyId;

    private Long userId;

    private String ipAddress;

    private String userAgent;

    private LocalDateTime submitTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private List<Answer> answers;
}
