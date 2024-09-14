高校教学信息管理系统

# 运行环境

1. [JDK 21](https://www.injdk.cn/)
2. [apache maven 3.9.6](https://maven.apache.org/)
3. [MySQL 8.0](https://www.mysql.com/)

# 运行步骤

1. 安装好JDK、Maven并配置好环境变量，其中Maven最好换源
2. 打开项目所在文件夹，pom.xml目录下
3. 运行命令 mvn package -DskipTests
4. 打开target目录，可以看到jar包，运行命令 java -jar jar名称
5. 运行成功之后打开：http://localhost:10010/doc.html 就可以看到接口文档


> 课程表设计（school timetable）
>
> 1. 需要包含教师信息
> 2. 需要包含教学班组信息
> 3. 需要包含上课时间信息（周几-第几节）
>
> 其中需要通过课程 + 教学组来确定一个课程

> 教师选课志愿表（course_volunteer）
>
> 1. 课程表编号
> 2. 志愿一二三的时间（周几-第几节）
> 3. 状态：success-成功；failed-失败；waiting-等待

> 冲突条件
> 1. 同一个班级同一时间上两堂课
> 2. 同一个老师同一时间上两堂课

> 判断过程
> 1. 拉取所有没有安排好的课程志愿
> 2. 按照志愿一-志愿二-志愿三的顺序依次判断是否存在冲突
> 3. 志愿之间的判断冲突：先按照周几来分组，然后按照开始节来分组，看二次分组之后组的内部是否存在冲突的情况
> 4. 如果存在冲突则流转到下一个志愿来安排
