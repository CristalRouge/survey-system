-- 问卷调查系统数据库初始化脚本
-- 数据库: survey_db

CREATE DATABASE IF NOT EXISTS survey_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE survey_db;

-- 用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
  `role` VARCHAR(20) NOT NULL DEFAULT 'user' COMMENT '角色: admin, user',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0禁用, 1正常',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 0未删除, 1已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 问卷表
DROP TABLE IF EXISTS `survey`;
CREATE TABLE `survey` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '问卷ID',
  `title` VARCHAR(200) NOT NULL COMMENT '问卷标题',
  `description` TEXT COMMENT '问卷描述',
  `cover_image` VARCHAR(500) DEFAULT NULL COMMENT '封面图片URL',
  `status` VARCHAR(20) NOT NULL DEFAULT 'draft' COMMENT '状态: draft草稿, published已发布, closed已关闭',
  `user_id` BIGINT NOT NULL COMMENT '创建用户ID',
  `response_count` INT DEFAULT 0 COMMENT '回答数量',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 0未删除, 1已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问卷表';

-- 问题表
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '问题ID',
  `survey_id` BIGINT NOT NULL COMMENT '所属问卷ID',
  `type` VARCHAR(20) NOT NULL DEFAULT 'single' COMMENT '类型: single单选题, multiple多选题, dropdown下拉框, text文本框',
  `title` VARCHAR(500) NOT NULL COMMENT '问题标题',
  `description` TEXT COMMENT '问题描述',
  `image` VARCHAR(500) DEFAULT NULL COMMENT '问题图片URL',
  `required` TINYINT DEFAULT 0 COMMENT '是否必填: 0否, 1是',
  `placeholder` VARCHAR(200) DEFAULT NULL COMMENT '占位提示文字',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 0未删除, 1已删除',
  PRIMARY KEY (`id`),
  KEY `idx_survey_id` (`survey_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问题表';

-- 选项表
DROP TABLE IF EXISTS `question_option`;
CREATE TABLE `question_option` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '选项ID',
  `question_id` BIGINT NOT NULL COMMENT '所属问题ID',
  `text` VARCHAR(200) NOT NULL COMMENT '选项文本',
  `image` VARCHAR(500) DEFAULT NULL COMMENT '选项图片URL',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 0未删除, 1已删除',
  PRIMARY KEY (`id`),
  KEY `idx_question_id` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问题选项表';

-- 回答表
DROP TABLE IF EXISTS `survey_response`;
CREATE TABLE `survey_response` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '回答ID',
  `survey_id` BIGINT NOT NULL COMMENT '所属问卷ID',
  `user_id` BIGINT DEFAULT NULL COMMENT '回答用户ID(可选)',
  `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
  `user_agent` VARCHAR(500) DEFAULT NULL COMMENT 'User-Agent',
  `submit_time` DATETIME DEFAULT NULL COMMENT '提交时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 0未删除, 1已删除',
  PRIMARY KEY (`id`),
  KEY `idx_survey_id` (`survey_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_ip_submit_time` (`ip_address`, `submit_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问卷回答表';

-- 答案表
DROP TABLE IF EXISTS `answer`;
CREATE TABLE `answer` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '答案ID',
  `response_id` BIGINT NOT NULL COMMENT '所属回答ID',
  `question_id` BIGINT NOT NULL COMMENT '所属问题ID',
  `value` TEXT COMMENT '答案值(JSON格式)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 0未删除, 1已删除',
  PRIMARY KEY (`id`),
  KEY `idx_response_id` (`response_id`),
  KEY `idx_question_id` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='答案表';

-- 插入默认管理员账号 (密码: admin123)
-- 密码加密: salt:hash 格式
INSERT INTO `sys_user` (`username`, `password`, `email`, `nickname`, `role`, `status`) VALUES
('admin', 'a1b2c3d4:e8c2b05a8f8e6d4c3b2a1f8e9d7c6b5a', 'admin@example.com', '管理员', 'admin', 1);

-- 插入测试用户 (密码: user123)
INSERT INTO `sys_user` (`username`, `password`, `email`, `nickname`, `role`, `status`) VALUES
('test', 'test1234:5e