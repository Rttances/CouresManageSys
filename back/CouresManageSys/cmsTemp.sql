-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: course_manager_system
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `administrative_class`
--

DROP TABLE IF EXISTS `administrative_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `administrative_class` (
  `class_id` bigint NOT NULL AUTO_INCREMENT COMMENT '班级编号',
  `class_name` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '班级名称',
  `grade` int NOT NULL DEFAULT '2023' COMMENT '年级',
  `major_name` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '专业名称',
  `direction_name` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '方向名称',
  `student_number` int NOT NULL DEFAULT '0' COMMENT '学生人数',
  PRIMARY KEY (`class_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='行政班级';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrative_class`
--

LOCK TABLES `administrative_class` WRITE;
/*!40000 ALTER TABLE `administrative_class` DISABLE KEYS */;
INSERT INTO `administrative_class` VALUES (1,'1 班',2023,'软件工程','软件工程',30),(2,'2 班',2023,'计算机科学与技术','机器人与物联网技术',35),(3,'3 班',2023,'计算机科学与技术','数据科学与大数据技术',35),(4,'4 班',2023,'计算机科学与技术','计算机软件技术',43),(5,'5 班',2023,'计算机科学与技术','人工智能',43),(6,'6 班',2023,'网络工程','网络空间安全',28),(7,'7 班',2023,'网络工程','网络技术应用',28),(8,'演示测试一班',2024,'演示测试','演示测试1',30),(9,'演示测试二班',2024,'演示测试','演示测试2',20);
/*!40000 ALTER TABLE `administrative_class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `course_id` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '课程编号',
  `score` int NOT NULL DEFAULT '0' COMMENT '学分',
  `total_class_hour` int NOT NULL DEFAULT '0' COMMENT '总课时',
  `theoretical_class_hour` int NOT NULL DEFAULT '0' COMMENT '理论课时',
  `experimental_class_hour` int NOT NULL DEFAULT '0' COMMENT '实验课时',
  `week_theoretical_class_hour` int NOT NULL DEFAULT '0' COMMENT '周理论课时',
  `week_experimental_class_hour` int NOT NULL DEFAULT '0' COMMENT '周实验课时',
  `start_week` int NOT NULL DEFAULT '1' COMMENT '起始周',
  `end_week` int NOT NULL DEFAULT '16' COMMENT '结束周',
  `grade` int NOT NULL DEFAULT '2023' COMMENT '年级',
  `semester` int NOT NULL DEFAULT '1' COMMENT '学期',
  `major` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '专业',
  `belong_system` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '归属系',
  `course_name` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '课程名称',
  PRIMARY KEY (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='课程表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES ('1',3,48,48,0,3,0,1,16,2023,1,'测试专业','科学','编译原理'),('10',2,48,16,32,1,2,1,16,2023,1,'测试专业','科学','Linux操作系统与应用'),('11',3,64,48,16,3,1,1,16,2023,1,'测试专业','应用','汇编语言与接口技术'),('12',2,48,32,16,2,1,1,16,2023,1,'测试专业','软件','大数据处理技术与应用'),('13',3,64,48,16,3,1,1,16,2023,1,'测试专业','软件','数据挖掘'),('14',3,48,48,0,3,0,1,16,2023,1,'测试专业','软件','自然语言处理'),('15',3,48,48,0,3,0,1,16,2023,1,'测试专业','科学','计算机图形学及应用'),('16',3,48,48,0,3,0,1,16,2023,1,'测试专业','应用','单片机原理及应用'),('17',1,16,0,16,1,0,1,16,2023,1,'测试专业','应用','单片机原理及应用实验项目'),('18',2,32,32,0,2,0,1,16,2023,1,'测试专业','科学','计算理论导引'),('19',2,48,32,16,2,1,1,16,2023,1,'测试专业','应用','嵌入式软件设计'),('2',3,64,48,16,3,1,1,16,2023,1,'测试专业','软件','软件工程'),('20',2,48,32,16,2,1,1,16,2023,1,'测试专业','应用','数字图像处理基础'),('21',2,48,32,16,2,1,1,16,2023,1,'测试专业','应用','平面动画2D'),('22',2,48,32,16,2,1,1,16,2023,1,'测试专业','软件','数据库系统实现'),('23',2,48,32,16,2,1,1,16,2023,1,'测试专业','软件','程序设计方法与艺术'),('24',2,48,32,16,2,1,1,16,2023,1,'测试专业',NULL,'分布式系统'),('25',2,32,32,0,2,0,1,16,2023,1,'测试专业',NULL,'RFID原理及应用'),('26',3,64,32,32,2,2,1,16,2023,1,'测试专业','软件','机器人基础'),('27',1,2,0,0,2,0,1,16,2023,1,'测试专业','应用','嵌入式系统课程项目'),('28',3,64,48,16,3,1,1,16,2023,1,'测试专业','软件','大型数据库技术及应用'),('29',1,2,0,0,2,0,1,16,2023,1,'测试专业','科学','操作系统课程项目'),('3',1,32,16,16,1,1,1,16,2023,1,'测试专业','应用','机器人与教育'),('31',2,48,32,16,2,1,1,16,2023,1,'测试专业','应用','软件测试'),('32',2,48,16,32,1,2,1,16,2023,1,'测试专业','软件','软件开发框架高级实践'),('33',1,2,0,0,2,0,1,16,2023,1,'测试专业','软件','中级软件设计实作'),('34',3,48,48,0,3,0,1,16,2023,1,'测试专业',NULL,'计算智能方法'),('35',1,2,0,0,2,0,1,16,2023,1,'测试专业','软件','数据库系统课程项目'),('36',3,48,48,0,3,0,1,16,2023,1,'测试专业','科学','信息技术教学论'),('4',3,48,48,0,3,0,1,16,2023,1,'测试专业','网络','互联网原理与应用'),('40',3,64,48,16,2,1,1,16,2023,1,'测试专业','人工智能','机器学习高级技术及应用'),('5',3,48,48,0,3,0,1,16,2023,1,'测试专业','科学','数值计算方法');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_teacher_teach_class_group_teach_type`
--

DROP TABLE IF EXISTS `course_teacher_teach_class_group_teach_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_teacher_teach_class_group_teach_type` (
  `course_id` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '课程编码',
  `teacher_id` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '教师编号',
  `teach_class_group_id` bigint NOT NULL COMMENT '教学班组编号',
  `course_type` int NOT NULL COMMENT '课程类型：0-理论，1-实验',
  PRIMARY KEY (`course_id`,`teacher_id`,`teach_class_group_id`,`course_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='课程教师教学班映射表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_teacher_teach_class_group_teach_type`
--

LOCK TABLES `course_teacher_teach_class_group_teach_type` WRITE;
/*!40000 ALTER TABLE `course_teacher_teach_class_group_teach_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `course_teacher_teach_class_group_teach_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_volunteer`
--

DROP TABLE IF EXISTS `course_volunteer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_volunteer` (
  `course_volunteer_id` bigint NOT NULL AUTO_INCREMENT COMMENT '课程志愿编号',
  `status` tinyint NOT NULL DEFAULT '2' COMMENT '状态：0-失败，1-成功，2-等待',
  `volunteer_time` json DEFAULT NULL COMMENT '志愿时间',
  `school_timetable_id` bigint NOT NULL COMMENT '课程表编号',
  PRIMARY KEY (`course_volunteer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='课程志愿表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_volunteer`
--

LOCK TABLES `course_volunteer` WRITE;
/*!40000 ALTER TABLE `course_volunteer` DISABLE KEYS */;
INSERT INTO `course_volunteer` VALUES (3,1,'[{\"order\": 0, \"weekDay\": 1, \"daySectionEnd\": 7, \"daySectionStart\": 5}, {\"order\": 1, \"weekDay\": 2, \"daySectionEnd\": 7, \"daySectionStart\": 5}, {\"order\": 2, \"weekDay\": 3, \"daySectionEnd\": 7, \"daySectionStart\": 5}]',3),(4,1,'[{\"order\": 0, \"weekDay\": 1, \"daySectionEnd\": 7, \"daySectionStart\": 5}, {\"order\": 1, \"weekDay\": 2, \"daySectionEnd\": 7, \"daySectionStart\": 5}, {\"order\": 2, \"weekDay\": 3, \"daySectionEnd\": 7, \"daySectionStart\": 5}]',4),(5,1,'[{\"order\": 0, \"weekDay\": 1, \"daySectionEnd\": 7, \"daySectionStart\": 5}, {\"order\": 1, \"weekDay\": 2, \"daySectionEnd\": 7, \"daySectionStart\": 5}, {\"order\": 2, \"weekDay\": 3, \"daySectionEnd\": 7, \"daySectionStart\": 5}]',5),(6,1,'[{\"order\": 0, \"weekDay\": 1, \"daySectionEnd\": 7, \"daySectionStart\": 5}, {\"order\": 1, \"weekDay\": 2, \"daySectionEnd\": 7, \"daySectionStart\": 5}, {\"order\": 2, \"weekDay\": 3, \"daySectionEnd\": 7, \"daySectionStart\": 5}]',6);
/*!40000 ALTER TABLE `course_volunteer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permission` (
  `permission_id` bigint NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `permission_name` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '权限名称',
  PRIMARY KEY (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '角色名称',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'admin'),(2,'teacher');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permission`
--

DROP TABLE IF EXISTS `role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_permission` (
  `role_id` bigint NOT NULL COMMENT '角色id',
  `permission_id` bigint NOT NULL COMMENT '权限id',
  PRIMARY KEY (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='角色-权限映射表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permission`
--

LOCK TABLES `role_permission` WRITE;
/*!40000 ALTER TABLE `role_permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `school_timetable`
--

DROP TABLE IF EXISTS `school_timetable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `school_timetable` (
  `school_timetable_id` bigint NOT NULL AUTO_INCREMENT COMMENT '课程表编号',
  `teach_class_group_id` bigint NOT NULL COMMENT '教学班级组编号',
  `teacher_id` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '教师编号',
  `week_day` int DEFAULT NULL COMMENT '周几',
  `day_section_start` int DEFAULT NULL COMMENT '一天第几节',
  `course_id` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '课程编号',
  `day_section_end` int DEFAULT NULL,
  `course_type` int NOT NULL DEFAULT '0' COMMENT '课程类型：0-理论，1-实验',
  PRIMARY KEY (`school_timetable_id`),
  UNIQUE KEY `teach_class_group_teacher_course_course_type` (`teach_class_group_id`,`teacher_id`,`course_id`,`course_type`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='课程表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `school_timetable`
--

LOCK TABLES `school_timetable` WRITE;
/*!40000 ALTER TABLE `school_timetable` DISABLE KEYS */;
INSERT INTO `school_timetable` VALUES (3,2,'CXK12135',1,5,'13',7,0),(4,2,'CXK12135',2,5,'14',7,0),(5,2,'CXK12135',3,5,'15',7,0),(6,2,'CXK12135',5,5,'16',7,0);
/*!40000 ALTER TABLE `school_timetable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teach_class_group`
--

DROP TABLE IF EXISTS `teach_class_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teach_class_group` (
  `teach_class_group_id` bigint NOT NULL AUTO_INCREMENT COMMENT '教学课堂组编号',
  `class_id_set` json NOT NULL COMMENT '行政班级编号集合',
  `group_name` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '教学班级组名称',
  PRIMARY KEY (`teach_class_group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='教学班级组';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teach_class_group`
--

LOCK TABLES `teach_class_group` WRITE;
/*!40000 ALTER TABLE `teach_class_group` DISABLE KEYS */;
INSERT INTO `teach_class_group` VALUES (1,'[1, 2]','软工计科教学班'),(2,'[8, 9]','演示测试合并班级');
/*!40000 ALTER TABLE `teach_class_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teacher` (
  `teacher_id` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '教师编号',
  `teacher_name` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '教师姓名',
  `teacher_level` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '教师职称',
  PRIMARY KEY (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='教师信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher`
--

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES ('CXK0914','菠萝吹雪','讲师'),('CXK12134','查头吹','讲师'),('CXK12135','长孙蓉初','讲师'),('CXK12139','暨矗贞','副教授'),('CXK12140','计绢','副教授'),('CXK12141','郑肮','教授'),('CXK12142','路螟','教授'),('CXK123456','test111','讲师'),('CXK123457','林依晨','教授'),('CXK123458','周一和','讲师'),('CXK123459','徐婷','教授'),('CXK123460','林品如','讲师'),('CXK123461','德云社','教授'),('CXK2120','梁华','讲师'),('CXK2159','马哈','讲师'),('test123456','演示测试教师','教授'),('test2201','111','讲师');
/*!40000 ALTER TABLE `teacher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '用户id',
  `username` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '昵称',
  `password` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '密码',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('CXK0914',NULL,'李默利','95a7256c17e21f4cf57ed91c066c8dd59e21021e80dd9dab7a98496dcac87f8c'),('CXK12134',NULL,'查头吹','f8e02dd41aec0e417323387cf03501f3edef8912c4defbd2e0e30a82567bdea2'),('CXK12135',NULL,'长孙蓉初','7d9919e3227e8aa88c927227a5cb35ac0309e40cc720b97a68793fae8b9e7c39'),('CXK12139',NULL,'暨矗贞','cfe3f47e78cda5d89e995233e541ded328dffdf55eebdf8325a8dd36e79cdb78'),('CXK12140',NULL,'计绢','0454166a186b0a62c1a570d0fd2f2bea4873add6d47f809bd33f7d216475437b'),('CXK12141',NULL,'郑肮','b719b2907375e2c8e09ac8f31536e9072fcc713b3e13f5fd2977b9ac0bd71a28'),('CXK12142',NULL,'路螟','1f48470856494418c3b7d570a423e717fd128c4ce0e6278b7b31ddf5190302f3'),('CXK123456',NULL,'test','207cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018fb'),('CXK123457',NULL,'林依晨','1c7ee6c50bf0cb3634c733685f0a20911bddb144c0ad13f8e053d1373b6d05a8'),('CXK123458',NULL,'周一和','9b4a75b632aef309f9210df597f8a2664b037a207e0a51724335e671f41ed6ec'),('CXK123459',NULL,'徐婷','83421d33b11875b7ea8aeefa1d268cebe1e7554529cb2fc94d0dd1e784998fda'),('CXK123460',NULL,'林品如','0a2b97c86c49681a1ef26bee0142c003c7d7e95fd0003fbb0b85fb762e8d2389'),('CXK123461',NULL,'德云社','7dd25ddd703962f357dc1e4ee146b98b4f4634666fb2da8172c29d5c71f49efd'),('CXK2120',NULL,'梁华','7620083119b5983fc8330bd453582c8074fa135b6fe8ba603f2b9bd50b810e63'),('CXK2159',NULL,'马哈','e1e12449d469766fbd2def25de44c77ad16bb8e9e48020a07b9b5b61fbcdef74'),('admin',NULL,NULL,'dc1fd00e3eeeb940ff46f457bf97d66ba7fcc36e0b20802383de142860e76ae6'),('test123456',NULL,'演示测试教师','207cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018fb'),('test2201',NULL,'111','6226d6ae072c90443d842e5333f9e4888b81a2267d54961de3cb03caf607598e');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `user_id` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '用户id',
  `role_id` bigint NOT NULL COMMENT '角色id',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户-角色映射表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES ('CXK0914',2),('CXK12134',2),('CXK12135',2),('CXK12139',2),('CXK12140',2),('CXK12141',2),('CXK12142',2),('CXK123456',2),('CXK123457',2),('CXK123458',2),('CXK123459',2),('CXK123460',2),('CXK123461',2),('CXK2120',2),('CXK2159',2),('admin',1),('test123456',2),('test2201',2);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-21 14:18:47
