-- ============================================================
-- 大学成绩管理系统 - MySQL 数据库建表SQL脚本
-- 数据库：MySQL 5.7+ / MySQL 8.0+
-- 编码：UTF-8
-- 说明：包含5张数据表的创建语句和测试数据
-- ============================================================

-- ------------------------------------------------------------
-- 创建数据库（如果不存在）
-- ------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS gradebook DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE gradebook;

-- ------------------------------------------------------------
-- 1. 用户表 users
-- 说明：存储系统用户，包括管理员、教师、学生
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    username      VARCHAR(50)  NOT NULL PRIMARY KEY COMMENT '用户名（登录账号）',
    password      VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密存储）',
    role          VARCHAR(20)  NOT NULL COMMENT '角色：ADMIN/TEACHER/STUDENT',
    real_name     VARCHAR(50)  DEFAULT NULL COMMENT '真实姓名',
    email         VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    INDEX idx_users_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ------------------------------------------------------------
-- 2. 学生表 students
-- 说明：存储学生详细信息
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS students (
    student_id    VARCHAR(20)  NOT NULL PRIMARY KEY COMMENT '学号',
    name          VARCHAR(50)  NOT NULL COMMENT '姓名',
    gender        VARCHAR(10)  DEFAULT NULL COMMENT '性别',
    major         VARCHAR(50)  NOT NULL COMMENT '专业',
    class_name    VARCHAR(50)  NOT NULL COMMENT '班级',
    email         VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    phone         VARCHAR(20)  DEFAULT NULL COMMENT '电话',
    INDEX idx_students_class (class_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生表';

-- ------------------------------------------------------------
-- 3. 课程表 courses
-- 说明：存储课程信息
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS courses (
    course_id     VARCHAR(20)  NOT NULL PRIMARY KEY COMMENT '课程编号',
    course_name   VARCHAR(100) NOT NULL COMMENT '课程名称',
    credit        DECIMAL(3,1) NOT NULL COMMENT '学分',
    teacher_name  VARCHAR(50)  DEFAULT NULL COMMENT '授课教师姓名',
    department    VARCHAR(50)  DEFAULT NULL COMMENT '开课学院',
    semester      VARCHAR(20)  DEFAULT NULL COMMENT '学期',
    INDEX idx_courses_semester (semester)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程表';

-- ------------------------------------------------------------
-- 4. 成绩表 grades
-- 说明：存储学生成绩记录
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS grades (
    id            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID（自增）',
    student_id    VARCHAR(20)  NOT NULL COMMENT '学号',
    course_id     VARCHAR(20)  NOT NULL COMMENT '课程编号',
    score         DECIMAL(5,1) DEFAULT NULL COMMENT '分数',
    grade_level   VARCHAR(10)  DEFAULT NULL COMMENT '等级：A/B/C/D/E',
    semester      VARCHAR(20)  DEFAULT NULL COMMENT '学期',
    exam_type     VARCHAR(20)  DEFAULT NULL COMMENT '考试类型',
    INDEX idx_grades_student (student_id),
    INDEX idx_grades_course (course_id),
    CONSTRAINT fk_grades_student FOREIGN KEY (student_id) REFERENCES students(student_id),
    CONSTRAINT fk_grades_course FOREIGN KEY (course_id) REFERENCES courses(course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='成绩表';

-- ------------------------------------------------------------
-- 5. 教师-课程关联表 teacher_courses
-- 说明：记录教师负责授课的课程（用于权限过滤）
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS teacher_courses (
    id                BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID（自增）',
    teacher_username  VARCHAR(50) NOT NULL COMMENT '教师用户名',
    course_id         VARCHAR(20) NOT NULL COMMENT '课程编号',
    INDEX idx_tc_teacher (teacher_username),
    INDEX idx_tc_course (course_id),
    CONSTRAINT fk_tc_teacher FOREIGN KEY (teacher_username) REFERENCES users(username),
    CONSTRAINT fk_tc_course FOREIGN KEY (course_id) REFERENCES courses(course_id),
    UNIQUE KEY uk_tc_teacher_course (teacher_username, course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教师课程关联表';


-- ============================================================
-- 测试数据插入
-- ============================================================

-- ------------------------------------------------------------
-- 插入用户数据（密码为BCrypt加密后的值）
-- 原始密码：admin123 / teacher123 / student123
-- ------------------------------------------------------------
INSERT IGNORE INTO users (username, password, role, real_name, email) VALUES
('admin',     '$2a$10$O4r8.4dtwQpCdVSGIld.YOukyNdtoLqfvlfHqEY.0Z7w0M0MFexnG', 'ADMIN',   '管理员', 'admin@university.edu'),
('teacher001','$2a$10$YsRn0PiIcDkE/Z.lVxnkauzR.4WpXsYa3M8aUHrIrKv0GgyDwG9.K', 'TEACHER', '张教授', 'zhang@university.edu'),
('teacher002','$2a$10$YsRn0PiIcDkE/Z.lVxnkauzR.4WpXsYa3M8aUHrIrKv0GgyDwG9.K', 'TEACHER', '李老师', 'li@university.edu'),
('2022001',   '$2a$10$O/rM3P.7kZHfG9gpN4o9CuJyXnNWaMQYHK6Wz7SZ4pV0lS5tU8u.G', 'STUDENT', '王明',   'wangming@university.edu'),
('2022002',   '$2a$10$O/rM3P.7kZHfG9gpN4o9CuJyXnNWaMQYHK6Wz7SZ4pV0lS5tU8u.G', 'STUDENT', '李华',   'lihua@university.edu'),
('2022003',   '$2a$10$O/rM3P.7kZHfG9gpN4o9CuJyXnNWaMQYHK6Wz7SZ4pV0lS5tU8u.G', 'STUDENT', '张伟',   'zhangwei@university.edu');

-- ------------------------------------------------------------
-- 插入学生数据
-- ------------------------------------------------------------
INSERT IGNORE INTO students (student_id, name, gender, major, class_name, email, phone) VALUES
('2022001', '王明', '男', '计算机科学与技术', '计科2201班', 'wangming@university.edu', '13800138001'),
('2022002', '李华', '女', '计算机科学与技术', '计科2201班', 'lihua@university.edu',    '13800138002'),
('2022003', '张伟', '男', '计算机科学与技术', '计科2202班', 'zhangwei@university.edu', '13800138003'),
('2022004', '刘洋', '男', '软件工程',         '软工2201班', 'liuyang@university.edu',  '13800138004'),
('2022005', '陈静', '女', '软件工程',         '软工2201班', 'chenjing@university.edu', '13800138005'),
('2022006', '杨帆', '男', '人工智能',         'AI2201班',   'yangfan@university.edu',  '13800138006'),
('2022007', '赵雪', '女', '人工智能',         'AI2201班',   'zhaoxue@university.edu',  '13800138007'),
('2022008', '孙磊', '男', '网络工程',         '网工2201班', 'sunlei@university.edu',   '13800138008');

-- ------------------------------------------------------------
-- 插入课程数据
-- ------------------------------------------------------------
INSERT IGNORE INTO courses (course_id, course_name, credit, teacher_name, department, semester) VALUES
('CS101', '高等数学',       4.0, '张教授', '数学学院',       '2024-2025-1'),
('CS102', '大学英语',       3.0, '李老师', '外国语学院',     '2024-2025-1'),
('CS103', '数据结构',       4.0, '王教授', '计算机学院',     '2024-2025-1'),
('CS104', '操作系统',       4.0, '赵老师', '计算机学院',     '2024-2025-2'),
('CS105', '计算机网络',     3.0, '刘教授', '计算机学院',     '2024-2025-2'),
('CS106', '数据库原理',     3.0, '张教授', '计算机学院',     '2024-2025-2'),
('CS107', '人工智能导论',   3.0, '陈教授', '人工智能学院',   '2024-2025-1'),
('CS108', '软件工程',       4.0, '周老师', '计算机学院',     '2024-2025-2'),
('CS109', '离散数学',       3.0, '张教授', '数学学院',       '2024-2025-1'),
('CS110', '编译原理',       4.0, '王教授', '计算机学院',     '2024-2025-2'),
('CS111', '算法设计',       4.0, '王教授', '计算机学院',     '2024-2025-1'),
('CS112', 'Web开发技术',    3.0, '刘教授', '计算机学院',     '2024-2025-2'),
('CS113', '云计算基础',     3.0, '周老师', '计算机学院',     '2024-2025-1'),
('CS114', '大数据导论',     3.0, '陈教授', '人工智能学院',   '2024-2025-2');

-- ------------------------------------------------------------
-- 插入成绩数据
-- ------------------------------------------------------------
-- 王明(2022001) 8门课程
INSERT INTO grades (student_id, course_id, score, grade_level, semester, exam_type) VALUES
('2022001', 'CS101', 85.0, 'B', '2024-2025-1', '期末考试'),
('2022001', 'CS102', 92.0, 'A', '2024-2025-1', '期末考试'),
('2022001', 'CS103', 78.0, 'C', '2024-2025-1', '期末考试'),
('2022001', 'CS104', 88.0, 'B', '2024-2025-2', '期末考试'),
('2022001', 'CS105', 95.0, 'A', '2024-2025-2', '期末考试'),
('2022001', 'CS106', 90.0, 'A', '2024-2025-2', '期末考试'),
('2022001', 'CS109', 82.0, 'B', '2024-2025-1', '期末考试'),
('2022001', 'CS111', 87.0, 'B', '2024-2025-1', '期末考试'),

-- 李华(2022002) 8门课程
('2022002', 'CS101', 90.0, 'A', '2024-2025-1', '期末考试'),
('2022002', 'CS102', 88.0, 'B', '2024-2025-1', '期末考试'),
('2022002', 'CS103', 92.0, 'A', '2024-2025-1', '期末考试'),
('2022002', 'CS104', 85.0, 'B', '2024-2025-2', '期末考试'),
('2022002', 'CS105', 80.0, 'B', '2024-2025-2', '期末考试'),
('2022002', 'CS109', 86.0, 'B', '2024-2025-1', '期末考试'),
('2022002', 'CS110', 91.0, 'A', '2024-2025-2', '期末考试'),
('2022002', 'CS112', 84.0, 'B', '2024-2025-2', '期末考试'),

-- 张伟(2022003) 8门课程
('2022003', 'CS101', 75.0, 'C', '2024-2025-1', '期末考试'),
('2022003', 'CS102', 82.0, 'B', '2024-2025-1', '期末考试'),
('2022003', 'CS103', 88.0, 'B', '2024-2025-1', '期末考试'),
('2022003', 'CS106', 70.0, 'C', '2024-2025-2', '期末考试'),
('2022003', 'CS108', 85.0, 'B', '2024-2025-2', '期末考试'),
('2022003', 'CS109', 78.0, 'C', '2024-2025-1', '期末考试'),
('2022003', 'CS111', 80.0, 'B', '2024-2025-1', '期末考试'),
('2022003', 'CS113', 83.0, 'B', '2024-2025-1', '期末考试'),

-- 刘洋(2022004) 8门课程
('2022004', 'CS101', 80.0, 'B', '2024-2025-1', '期末考试'),
('2022004', 'CS102', 75.0, 'C', '2024-2025-1', '期末考试'),
('2022004', 'CS107', 92.0, 'A', '2024-2025-1', '期末考试'),
('2022004', 'CS106', 88.0, 'B', '2024-2025-2', '期末考试'),
('2022004', 'CS108', 90.0, 'A', '2024-2025-2', '期末考试'),
('2022004', 'CS110', 85.0, 'B', '2024-2025-2', '期末考试'),
('2022004', 'CS112', 87.0, 'B', '2024-2025-2', '期末考试'),
('2022004', 'CS114', 91.0, 'A', '2024-2025-2', '期末考试'),

-- 陈静(2022005) 8门课程
('2022005', 'CS101', 88.0, 'B', '2024-2025-1', '期末考试'),
('2022005', 'CS102', 95.0, 'A', '2024-2025-1', '期末考试'),
('2022005', 'CS107', 85.0, 'B', '2024-2025-1', '期末考试'),
('2022005', 'CS106', 92.0, 'A', '2024-2025-2', '期末考试'),
('2022005', 'CS108', 88.0, 'B', '2024-2025-2', '期末考试'),
('2022005', 'CS109', 90.0, 'A', '2024-2025-1', '期末考试'),
('2022005', 'CS111', 86.0, 'B', '2024-2025-1', '期末考试'),
('2022005', 'CS113', 89.0, 'B', '2024-2025-1', '期末考试'),

-- 杨帆(2022006) 8门课程
('2022006', 'CS101', 92.0, 'A', '2024-2025-1', '期末考试'),
('2022006', 'CS102', 85.0, 'B', '2024-2025-1', '期末考试'),
('2022006', 'CS107', 95.0, 'A', '2024-2025-1', '期末考试'),
('2022006', 'CS105', 90.0, 'A', '2024-2025-2', '期末考试'),
('2022006', 'CS108', 82.0, 'B', '2024-2025-2', '期末考试'),
('2022006', 'CS110', 88.0, 'B', '2024-2025-2', '期末考试'),
('2022006', 'CS112', 93.0, 'A', '2024-2025-2', '期末考试'),
('2022006', 'CS114', 87.0, 'B', '2024-2025-2', '期末考试'),

-- 赵雪(2022007) 8门课程
('2022007', 'CS101', 85.0, 'B', '2024-2025-1', '期末考试'),
('2022007', 'CS102', 90.0, 'A', '2024-2025-1', '期末考试'),
('2022007', 'CS107', 88.0, 'B', '2024-2025-1', '期末考试'),
('2022007', 'CS105', 85.0, 'B', '2024-2025-2', '期末考试'),
('2022007', 'CS108', 92.0, 'A', '2024-2025-2', '期末考试'),
('2022007', 'CS109', 84.0, 'B', '2024-2025-1', '期末考试'),
('2022007', 'CS111', 91.0, 'A', '2024-2025-1', '期末考试'),
('2022007', 'CS113', 86.0, 'B', '2024-2025-1', '期末考试'),

-- 孙磊(2022008) 8门课程
('2022008', 'CS101', 78.0, 'C', '2024-2025-1', '期末考试'),
('2022008', 'CS102', 80.0, 'B', '2024-2025-1', '期末考试'),
('2022008', 'CS103', 85.0, 'B', '2024-2025-1', '期末考试'),
('2022008', 'CS105', 90.0, 'A', '2024-2025-2', '期末考试'),
('2022008', 'CS106', 75.0, 'C', '2024-2025-2', '期末考试'),
('2022008', 'CS108', 82.0, 'B', '2024-2025-2', '期末考试'),
('2022008', 'CS110', 79.0, 'C', '2024-2025-2', '期末考试'),
('2022008', 'CS112', 88.0, 'B', '2024-2025-2', '期末考试');

-- ------------------------------------------------------------
-- 插入教师-课程关联数据（用于实现教师权限过滤）
-- ------------------------------------------------------------
INSERT IGNORE INTO teacher_courses (teacher_username, course_id) VALUES
-- 张教授(teacher001) 负责：高等数学、数据库原理、离散数学
('teacher001', 'CS101'),
('teacher001', 'CS106'),
('teacher001', 'CS109'),

-- 李老师(teacher002) 负责：大学英语、Web开发技术
('teacher002', 'CS102'),
('teacher002', 'CS112');


-- ============================================================
-- 表关系说明
-- ============================================================
-- users.username         <---> students.student_id   （学生登录账号=学号）
-- grades.student_id      ---> students.student_id
-- grades.course_id       ---> courses.course_id
-- teacher_courses.course_id ---> courses.course_id
-- teacher_courses.teacher_username ---> users.username
-- ============================================================