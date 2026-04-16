package com.survey.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.survey.dto.request.SurveyCreateRequest;
import com.survey.dto.request.SurveyUpdateRequest;
import com.survey.dto.response.SurveyStatsVO;
import com.survey.dto.response.SurveyVO;
import com.survey.entity.Survey;

import java.util.List;

public interface SurveyService extends IService<Survey> {
    Page<SurveyVO> listSurveys(Page<SurveyVO> page, String keyword, String status, Long userId);
    List<SurveyVO> listPublishedSurveys();
    SurveyVO getSurveyById(Long id);
    SurveyVO getPublishedSurvey(Long id);
    SurveyVO createSurvey(SurveyCreateRequest request, Long userId);
    SurveyVO updateSurvey(Long id, SurveyUpdateRequest request, Long userId);
    void deleteSurvey(Long id, Long