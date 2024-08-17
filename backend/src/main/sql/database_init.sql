-- 创建数据库
CREATE DATABASE IF NOT EXISTS `cary_code` CHARSET utf8mb4;
USE `cary_code`;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `uid`   int	NOT NULL    AUTO_INCREMENT,
    `name` varchar(10)  NOT NULL	UNIQUE,
    `nickname`  varchar(10) NOT NULL,
    `password_hash` varchar(64) NOT NULL,
	`authority`	varchar(10)	NOT NULL,
    `avatar`    varchar(64),
    `signature` varchar(200),
    `register_time` datetime,
    `banned_until`  datetime,
	PRIMARY KEY(`uid`)
);

-- 论坛主题帖
CREATE TABLE IF NOT EXISTS `article` (
    `id`    int	NOT NULL    AUTO_INCREMENT,
    `uid`   int	NOT NULL,
    `nickname`  varchar(10) NOT NULL,
    `title` varchar(20) NOT NULL,
    `content`   text   NOT NULL,
	`type`      varchar(4)	NOT NULL,
    `likes` int NOT NULL    DEFAULT 0,
    `hates` int NOT NULL    DEFAULT 0,
    `create_time`   datetime    NOT NULL,
    `update_time`   datetime    NOT NULL,
    `latest_operate_time`   datetime    NOT NULL,
    PRIMARY KEY(`id`)
);

-- 评论
CREATE TABLE IF NOT EXISTS `comment` (
    `id`    int NOT NULL    AUTO_INCREMENT,
    `article_id`   int NOT NULL,
    `comment_id`    int,
    `uid`   int NOT NULL,
    `nickname`  varchar(10) NOT NULL,
    `content`   text   NOT NULL,
    `likes` int NOT NULL    DEFAULT 0,
    `hates` int NOT NULL    DEFAULT 0,
    `create_time`   datetime    NOT NULL,
    PRIMARY KEY(`id`)
);

-- 点赞和点踩记录
CREATE TABLE IF NOT EXISTS `action` (
    `id`    int NOT NULL    AUTO_INCREMENT,
    `text_id`   int NOT NULL,
    `text_type` varchar(8)  NOT NULL,
    `uid`   int NOT NULL,
    `action_type`   varchar(8)  NOT NULL,
    PRIMARY KEY(`id`),
    UNIQUE KEY `pair` (`text_id`, `text_type`, `uid`, `action_type`)
);
