package com.survey.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ResponseSubmitRequest {
    @NotEmpty(message = "答案不能为空")
    private Map<Long, Object> answers;  // questionId -> answer value(s)
}
