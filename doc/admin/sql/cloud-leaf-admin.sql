/*
SQLyog Ultimate
MySQL - 5.7.16 : Database - cloud-leaf-admin
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`cloud-leaf-admin` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

/*Table structure for table `t_sys_config` */

DROP TABLE IF EXISTS `t_sys_config`;

CREATE TABLE `t_sys_config` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `name` varchar(100) DEFAULT '' COMMENT '参数名称',
  `key` varchar(100) DEFAULT '' COMMENT '参数键名',
  `value` varchar(500) DEFAULT '' COMMENT '参数键值',
  `type` char(1) DEFAULT '0' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='参数配置表';

/*Data for the table `t_sys_config` */

insert  into `t_sys_config`(`id`,`name`,`key`,`value`,`type`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,'主框架页-默认皮肤样式名称','sys.index.skinName','skin-blue','1','admin','2021-08-01 17:12:51','',NULL,'蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow'),
(2,'用户管理-账号初始密码','sys.user.initPassword','123456','1','admin','2021-08-01 17:12:51','',NULL,'初始化密码 123456'),
(3,'主框架页-侧边栏主题','sys.index.sideTheme','theme-dark','1','admin','2021-08-01 17:12:51','',NULL,'深色主题theme-dark，浅色主题theme-light'),
(4,'账号自助-是否开启用户注册功能','sys.account.registerUser','false','1','admin','2021-08-01 17:12:51','',NULL,'是否开启注册用户功能（true开启，false关闭）');

/*Table structure for table `t_sys_dict_data` */

DROP TABLE IF EXISTS `t_sys_dict_data`;

CREATE TABLE `t_sys_dict_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `sort` int(4) DEFAULT '0' COMMENT '字典排序',
  `label` varchar(100) DEFAULT '' COMMENT '字典标签',
  `value` varchar(100) DEFAULT '' COMMENT '字典键值',
  `type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
  `is_default` tinyint(1) DEFAULT '0' COMMENT '是否默认（1是 0否）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COMMENT='字典数据表';

/*Data for the table `t_sys_dict_data` */

insert  into `t_sys_dict_data`(`id`,`sort`,`label`,`value`,`type`,`css_class`,`list_class`,`is_default`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,1,'男','0','sys_user_sex','','',1,'0','admin','2021-08-01 17:12:49','',NULL,'性别男'),
(2,2,'女','1','sys_user_sex','','',0,'0','admin','2021-08-01 17:12:49','',NULL,'性别女'),
(3,3,'未知','2','sys_user_sex','','',0,'0','admin','2021-08-01 17:12:49','',NULL,'性别未知'),
(4,1,'显示','0','sys_show_hide','','primary',1,'0','admin','2021-08-01 17:12:49','',NULL,'显示菜单'),
(5,2,'隐藏','1','sys_show_hide','','danger',0,'0','admin','2021-08-01 17:12:49','',NULL,'隐藏菜单'),
(6,1,'正常','0','sys_normal_disable','','primary',1,'0','admin','2021-08-01 17:12:49','',NULL,'正常状态'),
(7,2,'停用','1','sys_normal_disable','','danger',0,'0','admin','2021-08-01 17:12:49','',NULL,'停用状态'),
(8,1,'正常','0','sys_job_status','','primary',1,'0','admin','2021-08-01 17:12:49','',NULL,'正常状态'),
(9,2,'暂停','1','sys_job_status','','danger',0,'0','admin','2021-08-01 17:12:49','',NULL,'停用状态'),
(10,1,'默认','DEFAULT','sys_job_group','','',1,'0','admin','2021-08-01 17:12:49','',NULL,'默认分组'),
(11,2,'系统','SYSTEM','sys_job_group','','',0,'0','admin','2021-08-01 17:12:49','',NULL,'系统分组'),
(12,1,'是','Y','sys_yes_no','','primary',1,'0','admin','2021-08-01 17:12:49','',NULL,'系统默认是'),
(13,2,'否','N','sys_yes_no','','danger',0,'0','admin','2021-08-01 17:12:49','',NULL,'系统默认否'),
(14,1,'通知','1','sys_notice_type','','warning',1,'0','admin','2021-08-01 17:12:49','',NULL,'通知'),
(15,2,'公告','2','sys_notice_type','','success',0,'0','admin','2021-08-01 17:12:49','',NULL,'公告'),
(16,1,'正常','0','sys_notice_status','','primary',1,'0','admin','2021-08-01 17:12:49','',NULL,'正常状态'),
(17,2,'关闭','1','sys_notice_status','','danger',0,'0','admin','2021-08-01 17:12:49','',NULL,'关闭状态'),
(18,1,'新增','1','sys_oper_type','','info',0,'0','admin','2021-08-01 17:12:49','',NULL,'新增操作'),
(19,2,'修改','2','sys_oper_type','','info',0,'0','admin','2021-08-01 17:12:49','',NULL,'修改操作'),
(20,3,'删除','3','sys_oper_type','','danger',0,'0','admin','2021-08-01 17:12:49','',NULL,'删除操作'),
(21,4,'授权','4','sys_oper_type','','primary',0,'0','admin','2021-08-01 17:12:49','',NULL,'授权操作'),
(22,5,'导出','5','sys_oper_type','','warning',0,'0','admin','2021-08-01 17:12:49','',NULL,'导出操作'),
(23,6,'导入','6','sys_oper_type','','warning',0,'0','admin','2021-08-01 17:12:49','',NULL,'导入操作'),
(24,7,'强退','7','sys_oper_type','','danger',0,'0','admin','2021-08-01 17:12:49','',NULL,'强退操作'),
(25,8,'生成代码','8','sys_oper_type','','warning',0,'0','admin','2021-08-01 17:12:49','',NULL,'生成操作'),
(26,9,'清空数据','9','sys_oper_type','','danger',0,'0','admin','2021-08-01 17:12:49','',NULL,'清空操作'),
(27,1,'成功','0','sys_common_status','','primary',0,'0','admin','2021-08-01 17:12:49','',NULL,'正常状态'),
(28,2,'失败','1','sys_common_status','','danger',0,'0','admin','2021-08-01 17:12:49','',NULL,'停用状态');

/*Table structure for table `t_sys_dict_type` */

DROP TABLE IF EXISTS `t_sys_dict_type`;

CREATE TABLE `t_sys_dict_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `name` varchar(100) DEFAULT '' COMMENT '字典名称',
  `type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `dict_type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';

/*Data for the table `t_sys_dict_type` */

insert  into `t_sys_dict_type`(`id`,`name`,`type`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,'用户性别','sys_user_sex','0','admin','2021-08-01 17:12:47','',NULL,'用户性别列表'),
(2,'菜单状态','sys_show_hide','0','admin','2021-08-01 17:12:47','',NULL,'菜单状态列表'),
(3,'系统开关','sys_normal_disable','0','admin','2021-08-01 17:12:47','',NULL,'系统开关列表'),
(4,'任务状态','sys_job_status','0','admin','2021-08-01 17:12:47','',NULL,'任务状态列表'),
(5,'任务分组','sys_job_group','0','admin','2021-08-01 17:12:47','',NULL,'任务分组列表'),
(6,'系统是否','sys_yes_no','0','admin','2021-08-01 17:12:47','',NULL,'系统是否列表'),
(7,'通知类型','sys_notice_type','0','admin','2021-08-01 17:12:47','',NULL,'通知类型列表'),
(8,'通知状态','sys_notice_status','0','admin','2021-08-01 17:12:47','',NULL,'通知状态列表'),
(9,'操作类型','sys_oper_type','0','admin','2021-08-01 17:12:47','',NULL,'操作类型列表'),
(10,'系统状态','sys_common_status','0','admin','2021-08-01 17:12:47','',NULL,'登录状态列表');



DROP TABLE IF EXISTS `t_sys_login_log`;

CREATE TABLE `t_sys_login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) DEFAULT '' COMMENT '登录IP地址',
  `status` char(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) DEFAULT '' COMMENT '提示信息',
  `access_time` datetime DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统访问记录';



/*Table structure for table `t_sys_menu` */

DROP TABLE IF EXISTS `t_sys_menu`;

CREATE TABLE `t_sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) DEFAULT NULL COMMENT '菜单编码名',
  `title` varchar(100) DEFAULT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级菜单id',
  `component` varchar(100) DEFAULT NULL COMMENT '组件名',
  `path` varchar(100) DEFAULT NULL COMMENT '请求路由',
  `hidden` tinyint(1) DEFAULT NULL COMMENT '控制路由和子路由是否显示在 sidebar',
  `redirect` varchar(100) DEFAULT NULL COMMENT '重定向地址, 访问这个路由时,自定进行重定向',
  `hide_children_in_menu` tinyint(1) DEFAULT NULL COMMENT '强制菜单显示为Item而不是SubItem(配合 meta.hidden)',
  `type` char(1) DEFAULT NULL COMMENT '菜单类型 F目录 M菜单 B按钮',
  `order_no` int(4) DEFAULT NULL COMMENT '排序',
  `icon` varchar(100) DEFAULT NULL COMMENT '菜单图标',
  `hidden_header_content` tinyint(1) DEFAULT NULL COMMENT '*特殊 隐藏 PageHeader 组件中的页面带的 面包屑和页面标题栏',
  `keep_alive` tinyint(1) DEFAULT NULL COMMENT '缓存该路由 (开启 multi-tab 是默认值为 true)',
  `permission` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `new_window` tinyint(1) DEFAULT NULL COMMENT '新窗口打开',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态',
  `create_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb4 COMMENT='系统菜单';

/*Data for the table `t_sys_menu` */

insert  into `t_sys_menu`(`id`,`name`,`title`,`parent_id`,`component`,`path`,`hidden`,`redirect`,`hide_children_in_menu`,`type`,`order_no`,`icon`,`hidden_header_content`,`keep_alive`,`permission`,`new_window`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,'dashboard','仪表盘',0,'RouteView',NULL,0,'/dashboard/workplace',0,'F',1,'dashboard',1,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(2,'form','表单页',0,'RouteView',NULL,0,'',0,'F',2,'form',0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(3,'list','列表页',0,'RouteView',NULL,0,'/list/table-list',0,'F',3,'table',0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(4,'profile','详情页',0,'RouteView',NULL,0,'/profile/basic',0,'F',4,'profile',0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(5,'result','结果页',0,'PageView',NULL,0,'/result/success',0,'F',5,'check-circle-o',0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(6,'exception','异常页',0,'RouteView',NULL,0,'/exception/403',0,'F',6,'warning',0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(7,'account','个人页',0,'RouteView',NULL,0,'/account/center',0,'F',7,'user',0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(8,'other','其他页',0,'RouteView',NULL,0,'/other/other',0,'F',8,'user',0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(9,'workplace','工作台',1,'Workplace',NULL,0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(10,'monitor','监控页（外部）',1,NULL,'https://www.baidu.com/',0,'',0,'M',NULL,NULL,0,0,NULL,1,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(11,'Analysis','分析页',1,'Analysis','/dashboard/analysis',0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(12,'basic-form','基础表单',2,'BasicForm',NULL,0,NULL,0,'M',NULL,NULL,1,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(13,'step-form','分步表单',2,'StepForm',NULL,0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(14,'advanced-form','高级表单',2,'AdvanceForm',NULL,0,NULL,0,'M',NULL,NULL,1,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(15,'table-list','查询表格',3,'TableList',NULL,0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(16,'basic-list','标准列表',3,'StandardList',NULL,0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(17,'card','卡片列表',3,'CardList',NULL,0,NULL,0,'F',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(18,'search','搜索列表',3,'SearchLayout',NULL,0,'/list/search/article',0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(19,'article','搜索列表（文章）',18,'SearchArticles',NULL,0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(20,'project','搜索列表（项目）',18,'SearchProjects',NULL,0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(21,'application','搜索列表（应用）',18,'SearchApplications',NULL,0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(22,'basic','基础详情页',4,'ProfileBasic',NULL,0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(23,'advanced','高级详情页',4,'ProfileAdvanced',NULL,0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(24,'success','成功',5,'ResultSuccess',NULL,0,NULL,0,'M',NULL,NULL,1,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(25,'fail','失败',5,'ResultFail',NULL,0,NULL,0,'M',NULL,NULL,1,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(26,'403','403',6,'Exception403',NULL,0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(27,'404','404',6,'Exception404',NULL,0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(28,'500','500',6,'Exception500',NULL,0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(29,'center','个人中心',7,'AccountCenter',NULL,0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(30,'settings','个人设置',7,'AccountSettings',NULL,0,'/account/settings/basic',1,'F',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(31,'BasicSettings','基本设置',30,'BasicSetting','/account/settings/basic',0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(32,'SecuritySettings','安全设置',30,'SecuritySettings','/account/settings/security',0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(33,'CustomSettings','个性化设置',30,'CustomSettings','/account/settings/custom',0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(34,'BindingSettings','账户绑定',30,'BindingSettings','/account/settings/binding',0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(35,'NotificationSettings','新消息通知',30,'NotificationSettings','/account/settings/notification',0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(36,'roleList','角色列表',8,'RoleList',NULL,0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(37,'bigForm','大表单',8,'BigForm','/other/BigForm',0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(38,'iconSelector','图标选择',8,'IconSelectorView','/other/IconSelectorView',0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(39,'userList','用户列表',8,'UserList','/other/UserList',0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(40,'permissionList','权限列表',8,'PermissionList','/other/PermissionList',0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(41,'treeList','树形列表',8,'TreeList','/other/TreeList',0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(42,'TableInnerEditList','可编辑表格',8,'TableInnerEditList','/other/TableInnerEditList',0,NULL,0,'M',NULL,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-03-16 23:07:35',NULL),
(43,'system','系统管理',0,'PageView',NULL,0,'/system/user',0,'F',9,'setting',0,0,NULL,0,1,'admin','2022-03-16 23:07:35','admin','2022-04-13 22:52:34',NULL),
(44,'user','用户管理',43,'system/User',NULL,0,NULL,0,'M',1,NULL,0,1,NULL,0,1,'admin','2022-03-16 23:07:35','user','2022-04-20 10:59:51',NULL),
(45,'role','角色管理',43,'system/Role',NULL,0,NULL,1,'M',2,NULL,0,1,'system.role.list',0,1,'admin','2022-03-16 23:07:35','user','2022-04-22 15:23:03',NULL),
(46,'menu','菜单管理',43,'system/Menu',NULL,0,NULL,0,'M',3,NULL,0,1,NULL,0,1,'admin','2022-03-16 23:07:35','user','2022-04-20 11:00:41',NULL),
(47,'organization','组织管理',43,'system/Organization',NULL,0,NULL,0,'M',4,NULL,0,1,NULL,0,1,'admin','2022-03-16 23:07:35','user','2022-04-20 11:00:47',NULL),
(48,'dict','字典管理',43,'Dict',NULL,0,NULL,0,'M',6,NULL,0,0,NULL,0,1,'admin','2022-03-16 23:07:35','user','2022-04-28 17:10:32',NULL),
(52,NULL,'新增',44,NULL,NULL,NULL,NULL,NULL,'B',2,NULL,NULL,NULL,'system.user.add',NULL,1,'admin','2022-04-14 16:02:55',NULL,NULL,NULL),
(53,NULL,'查询',44,NULL,NULL,NULL,NULL,NULL,'B',1,NULL,NULL,NULL,'system.user.list',NULL,1,'admin','2022-04-14 16:03:34',NULL,NULL,NULL),
(54,NULL,'编辑',44,NULL,NULL,NULL,NULL,NULL,'B',3,NULL,NULL,NULL,'system.user.edit',NULL,1,'admin','2022-04-14 16:04:39',NULL,NULL,NULL),
(55,NULL,'删除',44,NULL,NULL,NULL,NULL,NULL,'B',4,NULL,NULL,NULL,'system.user.delete',NULL,1,'admin','2022-04-14 16:05:14',NULL,NULL,NULL),
(56,NULL,'查询',46,NULL,NULL,NULL,NULL,NULL,'B',1,NULL,NULL,NULL,'system.menu.list',NULL,1,'user','2022-04-15 15:53:09',NULL,NULL,NULL),
(57,NULL,'编辑',46,NULL,NULL,NULL,NULL,NULL,'B',2,NULL,NULL,NULL,'system.menu.edit',NULL,1,'user','2022-04-15 15:57:27','user','2022-04-15 15:58:18',NULL),
(58,NULL,'删除',46,NULL,NULL,NULL,NULL,NULL,'B',3,NULL,NULL,NULL,'system.menu.delete',NULL,1,'user','2022-04-15 15:58:03',NULL,NULL,NULL),
(59,NULL,'新增',46,NULL,NULL,NULL,NULL,NULL,'B',0,NULL,NULL,NULL,'system.menu.add',NULL,1,'user','2022-04-15 16:35:58',NULL,NULL,NULL),
(60,NULL,'查询',45,NULL,NULL,NULL,NULL,NULL,'B',1,NULL,NULL,NULL,'system.role.list',NULL,1,'user','2022-04-22 15:23:41',NULL,NULL,NULL),
(61,NULL,'新增',45,NULL,NULL,NULL,NULL,NULL,'B',2,NULL,NULL,NULL,'system.role.add',NULL,1,'user','2022-04-22 15:24:21',NULL,NULL,NULL),
(62,NULL,'编辑',45,NULL,NULL,NULL,NULL,NULL,'B',3,NULL,NULL,NULL,'system.role.edit',NULL,1,'user','2022-04-22 15:24:46',NULL,NULL,NULL),
(63,NULL,'删除',45,NULL,NULL,NULL,NULL,NULL,'B',4,NULL,NULL,NULL,'system.role.delete',NULL,1,'user','2022-04-22 15:25:10',NULL,NULL,NULL),
(64,NULL,'角色权限',45,NULL,NULL,NULL,NULL,NULL,'B',5,NULL,NULL,NULL,'system.role.permission',NULL,1,'user','2022-04-22 15:25:49',NULL,NULL,NULL),
(65,NULL,'查询',47,NULL,NULL,NULL,NULL,NULL,'B',1,NULL,NULL,NULL,'system.org.list',NULL,1,'user','2022-04-23 17:24:13',NULL,NULL,NULL),
(66,NULL,'新增',47,NULL,NULL,NULL,NULL,NULL,'B',2,NULL,NULL,NULL,'system.org.add',NULL,1,'user','2022-04-23 17:24:39',NULL,NULL,NULL),
(67,NULL,'编辑',47,NULL,NULL,NULL,NULL,NULL,'B',3,NULL,NULL,NULL,'system.org.edit',NULL,1,'user','2022-04-23 17:25:02',NULL,NULL,NULL),
(68,NULL,'删除',47,NULL,NULL,NULL,NULL,NULL,'B',4,NULL,NULL,NULL,'system.org.delete',NULL,1,'user','2022-04-23 17:25:22',NULL,NULL,NULL),
(69,'log','日志管理',43,'RouteView',NULL,0,NULL,0,'F',5,NULL,1,NULL,NULL,NULL,1,'user','2022-04-28 17:11:37','user','2022-04-28 17:47:41',NULL),
(70,'loginLog','登录日志',69,'system/log/LoginLog',NULL,0,NULL,NULL,'M',1,NULL,NULL,0,'system.loginLog.list',0,1,'user','2022-04-28 17:14:08',NULL,NULL,NULL),
(71,'operLog','操作日志',69,'system/log/OperLog',NULL,0,NULL,NULL,'M',2,NULL,NULL,1,NULL,0,1,'user','2022-04-28 17:15:10',NULL,NULL,NULL);


/*Table structure for table `t_sys_oper_log` */

DROP TABLE IF EXISTS `t_sys_oper_log`;

CREATE TABLE `t_sys_oper_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `module` varchar(50) DEFAULT '' COMMENT '模块名',
  `business_type` int(2) DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` int(1) DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `execute_time` int(10) DEFAULT NULL COMMENT '接口耗时 ms',
  `oper_name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
  `json_result` text COMMENT '返回参数',
  `status` int(1) DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=207 DEFAULT CHARSET=utf8mb4 COMMENT='操作日志记录';

/*Table structure for table `t_sys_organization` */

DROP TABLE IF EXISTS `t_sys_organization`;

CREATE TABLE `t_sys_organization` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(50) DEFAULT '' COMMENT '祖级列表',
  `name` varchar(30) DEFAULT '' COMMENT '部门名称',
  `order_no` int(4) DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(20) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` char(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

/*Data for the table `t_sys_organization` */

insert  into `t_sys_organization`(`id`,`parent_id`,`ancestors`,`name`,`order_no`,`leader`,`phone`,`email`,`status`,`del_flag`,`create_by`,`create_time`,`update_by`,`update_time`) values 
(100,0,'0','总公司',0,'管理员','18004557098','18004557098@163.com','1','0','admin','2021-08-01 17:12:32','',NULL),
(101,100,'0,100','深圳总公司',1,'张巧蕊','13388165563','13388165563@163.com','1','0','admin','2021-08-01 17:12:32','',NULL),
(102,100,'0,100','长沙分公司',2,'李山芙','15888888888','15888888888@163.com','1','0','admin','2021-08-01 17:12:32','',NULL),
(103,101,'0,100,101','研发部门',1,'孙雅寒','16715313464','16715313464@163.com','1','0','admin','2021-08-01 17:12:32','',NULL),
(104,101,'0,100,101','市场部门',2,'林雁兰','15825801513','15825801513@163.com','1','0','admin','2021-08-01 17:12:32','',NULL),
(105,101,'0,100,101','测试部门',3,'王冰薇','13216611416','13216611416@163.com','1','0','admin','2021-08-01 17:12:32','',NULL),
(106,101,'0,100,101','财务部门',4,'赵宛秋','15022976666','15022976666@163.com','1','0','admin','2021-08-01 17:12:32','',NULL),
(107,101,'0,100,101','运维部门',5,'泽以彤','13833531060','13833531060@163.com','1','0','admin','2021-08-01 17:12:32','',NULL),
(108,102,'0,100,102','市场部门',1,'长沙人','13441879074','13441879074@163.com','1','0','admin','2021-08-01 17:12:32','',NULL),
(109,102,'0,100,102','财务部门',2,'钱之卉','13628399240','13628399240@163.com','1','0','admin','2021-08-01 17:12:32','',NULL),
(110,100,'','上海分公司',3,NULL,NULL,NULL,'0','0','',NULL,'user','2022-04-23 21:25:37'),
(111,110,'','松江中支',1,NULL,NULL,NULL,'1','0','',NULL,'',NULL),
(115,110,'','徐汇中支',2,NULL,NULL,NULL,'1','0',NULL,NULL,'user','2022-04-18 10:06:53'),
(116,110,'','浦东中支',3,NULL,NULL,NULL,'1','0','user','2022-04-18 10:06:04','user','2022-04-18 10:07:07');

/*Table structure for table `t_sys_role` */

DROP TABLE IF EXISTS `t_sys_role`;

CREATE TABLE `t_sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` varchar(30) NOT NULL COMMENT '角色名称',
  `code` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `order_no` int(4) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
  `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';

/*Data for the table `t_sys_role` */

insert  into `t_sys_role`(`id`,`name`,`code`,`order_no`,`data_scope`,`menu_check_strictly`,`dept_check_strictly`,`status`,`del_flag`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,'超级管理员','admin',1,'1',1,1,'0','0','admin','2021-08-01 17:12:35','',NULL,'超级管理员'),
(2,'普通角色','common',2,'2',1,1,'0','0','admin','2021-08-01 17:12:35','',NULL,'普通角色'),
(4,'研发','dev',4,'1',1,1,'1','0','admin','2022-03-31 14:05:56','',NULL,NULL),
(5,'财务','finacial',4,'1',1,1,'1','0','admin','2022-03-31 14:06:32','',NULL,'1'),
(6,'业务员','biz',6,'1',1,1,'1','0','admin','2022-03-31 14:19:07','',NULL,NULL),
(7,'上分业务员','sh',7,'1',1,1,'1','0','admin','2022-03-31 14:23:06','',NULL,NULL),
(8,'北京业务员','beijing',8,'1',1,1,'1','0','admin','2022-03-31 14:23:27','',NULL,NULL),
(9,'数据库管理','dba',10,'1',1,1,'1','0','admin','2022-03-31 14:24:40','',NULL,NULL),
(10,'前台接待','reception',9,'1',1,1,'1','0','admin','2022-03-31 14:26:00','user','2022-04-18 08:39:29',NULL),
(11,'人力资源','hr',11,'1',1,1,'1','0','admin','2022-03-31 14:26:25','user','2022-04-23 11:21:18','1112');

/*Table structure for table `t_sys_role_menu` */

DROP TABLE IF EXISTS `t_sys_role_menu`;

CREATE TABLE `t_sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和菜单关联表';

/*Data for the table `t_sys_role_menu` */

insert  into `t_sys_role_menu`(`role_id`,`menu_id`) values 
(1,1),
(1,2),
(1,3),
(1,4),
(1,5),
(1,6),
(1,7),
(1,8),
(1,9),
(1,10),
(1,11),
(1,12),
(1,13),
(1,14),
(1,15),
(1,16),
(1,17),
(1,18),
(1,19),
(1,20),
(1,21),
(1,22),
(1,23),
(1,24),
(1,25),
(1,26),
(1,27),
(1,28),
(1,29),
(1,30),
(1,31),
(1,32),
(1,33),
(1,34),
(1,35),
(1,36),
(1,37),
(1,38),
(1,39),
(1,40),
(1,41),
(1,42),
(1,43),
(1,44),
(1,45),
(1,46),
(1,47),
(1,48),
(1,52),
(1,53),
(1,54),
(1,55),
(1,56),
(1,57),
(1,58),
(1,59),
(1,60),
(1,61),
(1,62),
(1,63),
(1,64),
(1,65),
(1,66),
(1,67),
(1,68),
(1,69),
(1,70),
(1,71),
(2,1),
(2,9),
(2,43),
(2,44),
(2,45),
(2,46),
(2,47),
(2,48),
(2,52),
(2,53),
(2,54),
(2,55),
(2,56),
(2,57),
(2,58),
(2,59),
(2,60),
(2,61),
(2,62),
(2,63),
(2,64),
(2,65),
(2,66),
(2,67),
(2,68),
(2,69),
(2,70),
(2,71),
(4,7),
(4,29),
(4,30),
(4,31),
(4,32),
(4,33),
(4,34),
(4,35),
(4,36),
(4,37),
(4,43),
(4,44),
(4,45),
(4,46),
(4,47),
(4,48),
(5,1),
(5,2),
(5,9),
(5,10),
(5,11),
(5,12),
(5,13),
(5,14),
(7,43),
(7,46),
(7,58),
(8,1),
(8,9),
(8,10),
(8,11),
(11,1),
(11,43),
(11,45),
(11,52),
(11,53),
(11,55);


DROP TABLE IF EXISTS `t_sys_user`;

CREATE TABLE `t_sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `org_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `username` varchar(30) NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
  `mobile_phone` varchar(11) DEFAULT '' COMMENT '手机号码',
  `gender` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `status` tinyint(1) DEFAULT '0' COMMENT '帐号状态（1正常 0停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
  `login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

/*Data for the table `t_sys_user` */

insert  into `t_sys_user`(`id`,`org_id`,`username`,`nick_name`,`user_type`,`email`,`mobile_phone`,`gender`,`avatar`,`password`,`status`,`del_flag`,`login_ip`,`login_time`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,103,'admin','管理员','00','admin@163.com','15888888888','1','http://192.168.0.114:9000/cloud/avatar/2022-05-02-031021.jpg','$2a$10$24UkW4UwHwCugOW.uZj5PeJuiSWQgjZzaGzs1RFalQT92lKZAt9tC',0,'0','127.0.0.1','2021-08-01 17:12:34','admin','2021-08-01 17:12:34','','2022-05-02 20:59:41','管理员'),
(2,105,'user','千本田','00','user2233@qq.com','15666666666','1','http://192.168.0.114:9000/cloud/avatar/2022-05-02-378809.jpg','$2a$10$24UkW4UwHwCugOW.uZj5PeJuiSWQgjZzaGzs1RFalQT92lKZAt9tC',0,'0','127.0.0.1','2021-08-01 17:12:34','admin','2021-08-01 17:12:34','user','2022-04-24 22:20:54','个人简介'),
(5,100,'testuser','测试用户3','00','13816063821@163.com','13816063821','0','','$2a$10$pbK9THcpRQylLIqDNgsjfeHLjejutJSLD4xRP5tnjM7J2voosy9f.',1,'0','',NULL,'test2','2022-03-27 20:44:33','admin','2022-03-28 14:16:41','11111'),
(11,109,'san.zhang','张三','00','','13816063434','0','','$2a$10$ULF7FGqcJH4IGbZD8acRte2gUxJWlrw3C46bdidM475g3zbO8J2TW',0,'0','',NULL,'admin','2022-03-28 14:34:41','',NULL,'11'),
(12,102,'beifang','北方','00','','13816063821','1','','$2a$10$NOPUeng6WoUWwKYg/LyraeTJL0i2DB2Dkrenl8LLlaKIQLTUkCVG2',0,'0','',NULL,'admin','2022-03-28 14:35:11','user','2022-04-17 22:10:47','1'),
(14,106,'lili-143','莉莉','00','','13814363821','1','','$2a$10$NafFUsRII6eBKNeP4Br2H.W1I5vUiJKs3K30aKqh5AV/9IxWp4sDa',0,'0','',NULL,'admin','2022-03-28 14:36:17','',NULL,'1111'),
(15,100,'jianghuan','蒋欢','00','','13316063821','1','http://192.168.0.114:9000/cloud/2022-05-02/blob','$2a$10$MD2s8VYu5L4iZkwpOjkxrOhFXuFC08qPlco3/UeBoebR0kKd5qUv.',1,'0','',NULL,'admin','2022-03-28 14:36:56','','2022-05-02 21:03:53','自在');

/*Table structure for table `t_sys_user_role` */

DROP TABLE IF EXISTS `t_sys_user_role`;

CREATE TABLE `t_sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户和角色关联表';

/*Data for the table `t_sys_user_role` */

insert  into `t_sys_user_role`(`user_id`,`role_id`) values 
(1,1),
(2,2),
(3,1),
(3,2),
(5,1),
(5,2),
(6,1),
(11,1),
(12,2),
(14,1),
(15,1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
