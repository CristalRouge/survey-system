package com.survey.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.survey.entity.Answer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface AnswerMapper extends BaseMapper<Answer> {

    @Select("SELECT value, COUNT(*) as count FROM answer WHERE question_id = #{questionId} GROUP BY value")
    List<Map<String, Object>> countByQuestionId(@Param("questionId") Long questionId);
}
