-- 修复数据库外键约束的SQL脚本
-- 1. 禁用外键约束检查
SET FOREIGN_KEY_CHECKS = 0;

-- 2. 删除question表的外键约束
ALTER TABLE question DROP FOREIGN KEY question_ibfk_1;

-- 3. 删除question_bank表
DROP TABLE IF EXISTS question_bank;

-- 4. 删除question表
DROP TABLE IF EXISTS question;

-- 5. 启用外键约束检查
SET FOREIGN_KEY_CHECKS = 1;
