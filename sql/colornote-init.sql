DROP TABLE IF EXISTS `cn_user`;
CREATE TABLE `cn_user` (
  `user_id` varchar(255) NOT NULL COMMENT '用户Id',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名称',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `create_date` varchar(8) NOT NULL COMMENT '创建日期yyyyMMdd',
  `create_time` varchar(8) NOT NULL COMMENT '创建时间HHmmss',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(255) DEFAULT NULL COMMENT '电话',
  `icon_url` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `account_status` varchar(2) NOT NULL DEFAULT '0' COMMENT '账户状态',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;