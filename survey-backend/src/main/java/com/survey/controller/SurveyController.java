package com.survey.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.survey.dto.request.SurveyCreateRequest;
import com.survey.dto.request.SurveyUpdateRequest;
import com.survey.dto.response.Result;
import com.survey.dto.response.SurveyStatsVO;
import com.survey.dto.response.SurveyVO;
import com.survey.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/surveys")
@Tag(name = "问卷管理", description = "问卷CRUD操作")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping
    @Operation(summary = "分页查询问卷列表")
    public Result<Page<SurveyVO>> listSurveys(
            @Parameter(description = "当前页") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "状态筛选") @RequestParam(required = false) String status,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = Long.parseLong(userDetails.getUsername());
        Page<SurveyVO> page = surveyService.listSurveys(new Page<>(current, size), keyword, status, userId);
        return Result.success(page);
    }

    @GetMapping("/published")
    @Operation(summary = "获取已发布问卷列表")
    public Result<List<SurveyVO>> listPublishedSurveys() {
        List<SurveyVO> surveys = surveyService.listPublishedSurveys();
        return Result.success(surveys);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取问卷详情")
    public Result<SurveyVO> getSurvey(@PathVariable Long id) {
        SurveyVO survey = surveyService.getSurveyById(id);
        return Result.success(survey);
    }

    @PostMapping
    @Operation(summary = "创建问卷")
    public Result<SurveyVO> createSurvey(
            @Valid @RequestBody SurveyCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        SurveyVO survey = surveyService.createSurvey(request, userId);
        return Result.success(survey);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新问卷")
    public Result<SurveyVO> updateSurvey(
            @PathVariable Long id,
            @RequestBody SurveyUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        SurveyVO survey = surveyService.updateSurvey(id, request, userId);
        return Result.success(survey);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除问卷")
    public Result<Void> deleteSurvey(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        surveyService.deleteSurvey(id, userId);
        return Result.success();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "更新问卷状态")
    public Result<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        surveyService.updateStatus(id, status, userId);
        return Result.success();
    }

    @GetMapping("/{id}/stats")
    @Operation(summary = "获取问卷统计")
    public Result<SurveyStatsVO> getSurveyStats(@PathVariable Long id) {
        SurveyStatsVO stats = surveyService.getSurveyStats(id);
        return Result.success(stats);
    }
}
