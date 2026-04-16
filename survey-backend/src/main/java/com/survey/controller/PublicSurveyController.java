package com.survey.controller;

import com.survey.dto.request.ResponseSubmitRequest;
import com.survey.dto.response.Result;
import com.survey.dto.response.SurveyVO;
import com.survey.service.SurveyResponseService;
import com.survey.service.SurveyService;
import com.survey.util.IpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/surveys")
@Tag(name = "公开问卷接口", description = "无需认证的问卷访问接口")
public class PublicSurveyController {

    private final SurveyService surveyService;
    private final SurveyResponseService responseService;

    public PublicSurveyController(SurveyService surveyService, SurveyResponseService responseService) {
        this.surveyService = surveyService;
        this.responseService = responseService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取公开问卷")
    public Result<SurveyVO> getPublicSurvey(@PathVariable Long id) {
        SurveyVO survey = surveyService.getPublishedSurvey(id);
        return Result.success(survey);
    }

    @PostMapping("/{id}/responses")
    @Operation(summary = "提交问卷回答")
    public Result<Void> submitResponse(
            @PathVariable Long id,
            @Valid @RequestBody ResponseSubmitRequest request,
            HttpServletRequest httpRequest) {
        String ipAddress = IpUtil.getIpAddress(httpRequest);
        String userAgent = IpUtil.getUserAgent(httpRequest);

        if (responseService.checkDuplicateSubmission(id, ipAddress)) {
            return Result.error(400, "您已提交过此问卷，请勿重复提交");
        }

        responseService.submitResponse(id, request, null, ipAddress, userAgent);
        return Result.success();
    }
}
