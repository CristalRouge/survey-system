package com.survey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.survey.dto.request.ResponseSubmitRequest;
import com.survey.entity.Answer;
import com.survey.entity.Survey;
import com.survey.entity.SurveyResponse;
import com.survey.mapper.AnswerMapper;
import com.survey.mapper.SurveyResponseMapper;
import com.survey.service.SurveyResponseService;
import com.survey.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class SurveyResponseServiceImpl extends ServiceImpl<SurveyResponseMapper, SurveyResponse> implements SurveyResponseService {

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private AnswerMapper answerMapper;

    @Override
    @Transactional
    public void submitResponse(Long surveyId, ResponseSubmitRequest request, Long userId, String ipAddress, String userAgent) {
        SurveyResponse response = new SurveyResponse();
        response.setSurveyId(surveyId);
        response.setUserId(userId);
        response.setIpAddress(ipAddress);
        response.setUserAgent(userAgent);
        response.setSubmitTime(LocalDateTime.now());
        save(response);

        // 保存答案
        for (Map.Entry<Long, Object> entry : request.getAnswers().entrySet()) {
            Answer answer = new Answer();
            answer.setResponseId(response.getId());
            answer.setQuestionId(entry.getKey());
            answer.setValue(entry.getValue() instanceof String ? (String) entry.getValue() : entry.getValue().toString());
            answerMapper.insert(answer);
        }

        // 更新问卷回答数
        surveyService.incrementResponseCount(surveyId);
    }

    @Override
    public Page<SurveyResponse> listResponses(Long surveyId, Page<SurveyResponse> page) {
        LambdaQueryWrapper<SurveyResponse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SurveyResponse::getSurveyId, surveyId);
        wrapper.orderByDesc(SurveyResponse::getSubmitTime);
        return page(page, wrapper);
    }

    @Override
    public SurveyResponse getResponseById(Long id) {
        return getById(id);
    }

    @Override
    public boolean checkDuplicateSubmission(Long surveyId, String ipAddress) {
        LambdaQueryWrapper<SurveyResponse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SurveyResponse::getSurveyId, surveyId);
        wrapper.eq(SurveyResponse::getIpAddress, ipAddress);
        wrapper.ge(SurveyResponse::getSubmitTime, LocalDateTime.now().minusHours(1));
        return count(wrapper) > 0;
    }
}
