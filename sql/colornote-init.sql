DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `user_id` varchar(30) NOT NULL COMMENT '用户Id(手机号码)',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名称',
  `create_date` char(8) NOT NULL COMMENT '创建日期yyyyMMdd',
  `create_time` char(8) NOT NULL COMMENT '创建时间HHmmss',
  `icon_url` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `account_status` varchar(2) NOT NULL DEFAULT '0' COMMENT '账户状态  0--正常 1--注销 2--冻结',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '用户表';

DROP TABLE IF EXISTS `tb_note_kind`;
CREATE TABLE `tb_note_kind` (
  `note_kind_id` varchar(50) NOT NULL COMMENT '笔记分类ID字段',
  `user_id` varchar(30) NOT NULL COMMENT '用户Id(手机号码)',
  `note_kind_name` varchar(255) DEFAULT NULL COMMENT '用户名称',
  `create_date` char(8) NOT NULL COMMENT '创建日期yyyyMMdd',
  `create_time` char(8) NOT NULL COMMENT '创建时间HHmmss',
  `kind_icon_url` varchar(255) DEFAULT NULL COMMENT '头像URL',
  PRIMARY KEY (`note_kind_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '笔记分类表';

DROP TABLE IF EXISTS `tb_system_config`;
CREATE TABLE `tb_system_config` (
  `config_id` varchar(50) NOT NULL,
  `config_key` varchar(255) NOT NULL,
  `config_value` varchar(255) NOT NULL,
  `create_date` char(8) NOT NULL,
  `create_time` char(6) NOT NULL,
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_system_config
-- ----------------------------
INSERT INTO `tb_system_config` VALUES ('1fb9f181ae66453cbdfd0f9e78b8f7a5', 'imageUrl', 'http://localhost:8020/ColorNote/image/getById/', '20191107', '102301');
