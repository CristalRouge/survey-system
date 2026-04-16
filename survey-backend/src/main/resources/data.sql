-- 示例问卷数据
USE survey_db;

-- 插入示例问卷
INSERT INTO `survey` (`id`, `title`, `description`, `cover_image`, `status`, `user_id`, `response_count`, `create_time`, `update_time`) VALUES
(1, '客户满意度调查', '感谢您抽出宝贵时间完成此调查，您的反馈对我们非常重要。', 'https://picsum.photos/800/400', 'published', 1, 156, NOW(), NOW()),
(2, '产品功能偏好调查', '了解用户对产品功能的偏好和使用习惯', 'https://picsum.photos/800/401', 'published', 1, 89, NOW(), NOW()),
(3, '用户反馈问卷', '收集用户对产品改进的建议和意见', 'https://picsum.photos/800/402', 'published', 1, 234, NOW(), NOW());

-- 问卷1的问题
INSERT INTO `question` (`id`, `survey_id`, `type`, `title`, `description`, `image`, `required`, `placeholder`, `sort`) VALUES
(1, 1, 'single', '您对我们服务的整体满意度如何？', '请选择最符合您感受的选项', 'https://picsum.photos/600/300', 1, NULL, 1),
(2, 1, 'multiple', '您使用过以下哪些功能？', '可选择多项', NULL, 0, NULL, 2),
(3, 1, 'dropdown', '您是通过什么渠道了解我们的？', NULL, 'https://picsum.photos/600/301', 1, '请选择', 3),
(4, 1, 'text', '您对我们有什么建议或意见？', '您的反馈将帮助我们改进', NULL, 0, '请输入您的建议...', 4);

-- 问卷1的选项
INSERT INTO `question_option` (`id`, `question_id`, `text`, `image`, `sort`) VALUES
(1, 1, '非常满意', NULL, 1),
(2, 1, '满意', NULL, 2),
(3, 1, '一般', NULL, 3),
(4, 1, '不满意', NULL, 4),
(5, 2, '在线客服', NULL, 1),
(6, 2, '自助查询', NULL, 2),
(7, 2, '电话咨询', NULL, 3),
(8, 2, '邮件支持', NULL, 4),
(9, 3, '搜索引擎', NULL, 1),
(10, 3, '社交媒体', NULL, 2),
(11, 3, '朋友推荐', NULL, 3),
(12, 3, '广告投放', NULL, 4);

-- 问卷2的问题
INSERT INTO `question` (`id`, `survey_id`, `type`, `title`, `description`, `image`, `required`, `placeholder`, `sort`) VALUES
(5, 2, 'single', '您最常用的功能模块是？', NULL, NULL, 1, NULL, 1),
(6, 2, 'multiple', '您希望我们增加哪些新功能？', '可选择多项', 'https://picsum.photos/600/302', 0, NULL, 2),
(7, 2, 'single', '您每月使用我们产品的频率？', NULL, NULL, 1, NULL, 3);

INSERT INTO `question_option` (`id`, `question_id`, `text`, `image`, `sort`) VALUES
(13, 5, '数据分析', NULL, 1),
(14, 5, '报表导出', NULL, 2),
(15, 5, '团队协作', NULL, 3),
(16, 5, '移动端', NULL, 4),
(17, 6, 'AI智能推荐', NULL, 1),
(18, 6, '多语言支持', NULL, 2),
(19, 6, 'API开放接口', NULL, 3),
(20, 6, '数据可视化增强', NULL, 4),
(21, 7, '每天多次', NULL, 1),
(22, 7, '每天一次', NULL, 2),
(23, 7, '每周几次', NULL, 3),
(24, 7, '偶尔使用', NULL, 4);
