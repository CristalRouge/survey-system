package com.survey.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.survey.dto.request.ResponseSubmitRequest;
import com.survey.entity.SurveyResponse;

public interface SurveyResponseService extends IService<SurveyResponse> {
    void submitResponse(Long surveyId, ResponseSubmitRequest request, Long userId, String ipAddress, String userAgent);
    Page<SurveyResponse> list