package com.survey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.survey.dto.request.SurveyCreateRequest;
import com.survey.dto.request.SurveyUpdateRequest;
import com.survey.dto.response.SurveyStatsVO;
import com.survey.dto.response.SurveyVO;
import com.survey.entity.Question;
import com.survey.entity.QuestionOption;
import com.survey.entity.Survey;
import com.survey.entity.User;
import com.survey.mapper.QuestionMapper;
import com.survey.mapper.QuestionOptionMapper;
import com.survey.mapper.SurveyMapper;
import com.survey.mapper.UserMapper;
import com.survey.service.SurveyService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveyServiceImpl extends ServiceImpl<SurveyMapper, Survey> implements SurveyService {

    private final QuestionMapper questionMapper;
    private final QuestionOptionMapper optionMapper;
    private final UserMapper userMapper;

    public SurveyServiceImpl(QuestionMapper questionMapper, QuestionOptionMapper optionMapper, UserMapper userMapper) {
        this.questionMapper = questionMapper;
        this.optionMapper = optionMapper;
        this.userMapper = userMapper;
    }

    @Override
    public Page<SurveyVO> listSurveys(Page<SurveyVO> page, String keyword, String status, Long userId) {
        LambdaQueryWrapper<Survey> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Survey::getTitle, keyword).or().like(Survey::getDescription, keyword);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Survey::getStatus, status);
        }
        if (userId != null) {
            wrapper.eq(Survey::getUserId, userId);
        }
        wrapper.orderByDesc(Survey::getCreateTime);

        Page<Survey> result = page(page, wrapper);
        Page<SurveyVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());

        List<SurveyVO> voList = result.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public List<SurveyVO> listPublishedSurveys() {
        LambdaQueryWrapper<Survey> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Survey::getStatus, "published");
        wrapper.orderByDesc(Survey::getCreateTime);
        return list(wrapper).stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public SurveyVO getSurveyById(Long id) {
        Survey survey = getById(id);
        if (survey == null) {
            throw new RuntimeException("问卷不存在");
        }
        SurveyVO vo = convertToVO(survey);
        vo.setQuestions(getQuestionVOs(id));
        return vo;
    }

    @Override
    public SurveyVO getPublishedSurvey(Long id) {
        Survey survey = getById(id);
        if (survey == null || !"published".equals(survey.getStatus())) {
            throw new RuntimeException("问卷不存在或未发布");
        }
        SurveyVO vo = convertToVO(survey);
        vo.setQuestions(getQuestionVOs(id));
        return vo;
    }

    @Override
    @Transactional
    public SurveyVO createSurvey(SurveyCreateRequest request, Long userId) {
        Survey survey = new Survey();
        survey.setTitle(request.getTitle());
        survey.setDescription(request.getDescription());
        survey.setCoverImage(request.getCoverImage());
        survey.setStatus("draft");
        survey.setUserId(userId);
        survey.setResponseCount(0);
        save(survey);

        if (request.getQuestions() != null) {
            saveQuestions(survey.getId(), request.getQuestions());
        }

        return getSurveyById(survey.getId());
    }

    @Override
    @Transactional
    public SurveyVO updateSurvey(Long id, SurveyUpdateRequest request, Long userId) {
        Survey survey = getById(id);
        if (survey == null) {
            throw new RuntimeException("问卷不存在");
        }
        if (!survey.getUserId().equals(userId)) {
            throw new RuntimeException("无权限修改");
        }

        if (request.getTitle() != null) survey.setTitle(request.getTitle());
        if (request.getDescription() != null) survey.setDescription(request.getDescription());
        if (request.getCoverImage() != null) survey.setCoverImage(request.getCoverImage());
        updateById(survey);

        if (request.getQuestions() != null) {
            // 删除旧问题重新创建
            questionMapper.delete(new LambdaQueryWrapper<Question>().eq(Question::getSurveyId, id));
            saveQuestions(id, request.getQuestions());
        }

        return getSurveyById(id);
    }

    @Override
    @Transactional
    public void deleteSurvey(Long id, Long userId) {
        Survey survey = getById(id);
        if (survey == null) {
            throw new RuntimeException("问卷不存在");
        }
        if (!survey.getUserId().equals(userId)) {
            throw new RuntimeException("无权限删除");
        }
        removeById(id);
        questionMapper.delete(new LambdaQueryWrapper<Question>().eq(Question::getSurveyId, id));
    }

    @Override
    public void updateStatus(Long id, String status, Long userId) {
        Survey survey = getById(id);
        if (survey == null) {
            throw new RuntimeException("问卷不存在");
        }
        if (!survey.getUserId().equals(userId)) {
            throw new RuntimeException("无权限修改");
        }
        survey.setStatus(status);
        updateById(survey);
    }

    @Override
    public SurveyStatsVO getSurveyStats(Long id) {
        Survey survey = getById(id);
        if (survey == null) {
            throw new RuntimeException("问卷不存在");
        }

        SurveyStatsVO stats = new SurveyStatsVO();
        stats.setSurveyId(id);
        stats.setTitle(survey.getTitle());
        stats.setTotalResponses(survey.getResponseCount());
        stats.setQuestionStats(new ArrayList<>());

        return stats;
    }

    @Override
    public void incrementResponseCount(Long surveyId) {
        Survey survey = getById(surveyId);
        if (survey != null) {
            survey.setResponseCount(survey.getResponseCount() + 1);
            updateById(survey);
        }
    }

    private List<SurveyVO.QuestionVO> getQuestionVOs(Long surveyId) {
        LambdaQueryWrapper<Question> qWrapper = new LambdaQueryWrapper<>();
        qWrapper.eq(Question::getSurveyId, surveyId);
        qWrapper.orderByAsc(Question::getSort);
        List<Question> questions = questionMapper.selectList(qWrapper);

        return questions.stream().map(q -> {
            SurveyVO.QuestionVO qvo = new SurveyVO.QuestionVO();
            BeanUtils.copyProperties(q, qvo);

            LambdaQueryWrapper<QuestionOption> oWrapper = new LambdaQueryWrapper<>();
            oWrapper.eq(QuestionOption::getQuestionId, q.getId());
            oWrapper.orderByAsc(QuestionOption::getSort);
            List<QuestionOption> options = optionMapper.selectList(oWrapper);

            qvo.setOptions(options.stream().map(o -> {
                SurveyVO.OptionVO ovo = new SurveyVO.OptionVO();
                BeanUtils.copyProperties(o, ovo);
                return ovo;
            }).collect(Collectors.toList()));

            return qvo;
        }).collect(Collectors.toList());
    }

    private void saveQuestions(Long surveyId, List<SurveyCreateRequest.QuestionRequest> questions) {
        for (int i = 0; i < questions.size(); i++) {
            SurveyCreateRequest.QuestionRequest qr = questions.get(i);
            Question question = new Question();
            question.setSurveyId(surveyId);
            question.setTitle(qr.getTitle());
            question.setType(qr.getType());
            question.setDescription(qr.getDescription());
            question.setImage(qr.getImage());
            question.setRequired(qr.getRequired());
            question.setPlaceholder(qr.getPlaceholder());
            question.setSort(qr.getSort() != null ? qr.getSort() : i);
            questionMapper.insert(question);

            if (qr.getOptions() != null) {
                for (int j = 0; j < qr.getOptions().size(); j++) {
                    SurveyCreateRequest.OptionRequest or = qr.getOptions().get(j);
                    QuestionOption option = new QuestionOption();
                    option.setQuestionId(question.getId());
                    option.setText(or.getText());
                    option.setImage(or.getImage());
                    option.setSort(or.getSort() != null ? or.getSort() : j);
                    optionMapper.insert(option);
                }
            }
        }
    }

    private SurveyVO convertToVO(Survey survey) {
        SurveyVO vo = new SurveyVO();
        BeanUtils.copyProperties(survey, vo);
        User user = userMapper.selectById(survey.getUserId());
        if (user != null) {
            vo.setUsername(user.getUsername());
        }
        return vo;
    }
}
