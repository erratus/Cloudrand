-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema cloud_storage
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema cloud_storage
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `cloud_storage` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `cloud_storage` ;

-- -----------------------------------------------------
-- Table `cloud_storage`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cloud_storage`.`users` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `role` ENUM('admin', 'user', 'developer') NOT NULL DEFAULT 'user',
  `phone` VARCHAR(15) NULL DEFAULT NULL,
  `profile_picture` VARCHAR(255) NULL DEFAULT NULL,
  `status` ENUM('active', 'inactive', 'banned') NOT NULL DEFAULT 'active',
  `last_login` TIMESTAMP NULL DEFAULT NULL,
  `created_by` INT NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `username` (`username` ASC) VISIBLE,
  UNIQUE INDEX `email` (`email` ASC) VISIBLE,
  UNIQUE INDEX `phone` (`phone` ASC) VISIBLE,
  INDEX `created_by` (`created_by` ASC) VISIBLE,
  CONSTRAINT `users_ibfk_1`
    FOREIGN KEY (`created_by`)
    REFERENCES `cloud_storage`.`users` (`user_id`)
    ON DELETE SET NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `cloud_storage`.`files`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cloud_storage`.`files` (
  `file_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `file_name` VARCHAR(255) NOT NULL,
  `file_path` VARCHAR(500) NOT NULL,
  `file_size` BIGINT NOT NULL,
  `uploaded_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`file_id`),
  INDEX `user_id` (`user_id` ASC) VISIBLE,
  CONSTRAINT `files_ibfk_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `cloud_storage`.`users` (`user_id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `cloud_storage`.`file_versions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cloud_storage`.`file_versions` (
  `version_id` INT NOT NULL AUTO_INCREMENT,
  `file_id` INT NOT NULL,
  `version_number` INT NOT NULL,
  `file_path` VARCHAR(500) NOT NULL,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`version_id`),
  INDEX `file_id` (`file_id` ASC) VISIBLE,
  CONSTRAINT `file_versions_ibfk_1`
    FOREIGN KEY (`file_id`)
    REFERENCES `cloud_storage`.`files` (`file_id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `cloud_storage`.`shared_links`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cloud_storage`.`shared_links` (
  `link_id` INT NOT NULL AUTO_INCREMENT,
  `file_id` INT NOT NULL,
  `share_token` VARCHAR(255) NOT NULL,
  `expires_at` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`link_id`),
  UNIQUE INDEX `share_token` (`share_token` ASC) VISIBLE,
  INDEX `file_id` (`file_id` ASC) VISIBLE,
  CONSTRAINT `shared_links_ibfk_1`
    FOREIGN KEY (`file_id`)
    REFERENCES `cloud_storage`.`files` (`file_id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `cloud_storage`.`storage`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cloud_storage`.`storage` (
  `storage_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `total_storage` BIGINT NOT NULL,
  `used_storage` BIGINT NOT NULL DEFAULT '0',
  PRIMARY KEY (`storage_id`),
  UNIQUE INDEX `user_id` (`user_id` ASC) VISIBLE,
  CONSTRAINT `storage_ibfk_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `cloud_storage`.`users` (`user_id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

CREATE TABLE IF NOT EXISTS `cloud_storage`.`folders` (
  `folder_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `folder_name` VARCHAR(255) NOT NULL,
  `parent_folder_id` INT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`folder_id`),
  INDEX `user_id` (`user_id` ASC),
  CONSTRAINT `folders_ibfk_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `cloud_storage`.`users` (`user_id`)
    ON DELETE CASCADE,
  CONSTRAINT `folders_ibfk_2`
    FOREIGN KEY (`parent_folder_id`)
    REFERENCES `cloud_storage`.`folders` (`folder_id`)
    ON DELETE CASCADE
);


ALTER TABLE `cloud_storage`.`files` 
ADD COLUMN `folder_id` INT NULL,
ADD CONSTRAINT `files_ibfk_2`
  FOREIGN KEY (`folder_id`)
  REFERENCES `cloud_storage`.`folders` (`folder_id`)
  ON DELETE SET NULL;


CREATE TABLE IF NOT EXISTS `cloud_storage`.`permissions` (
  `permission_id` INT NOT NULL AUTO_INCREMENT,
  `file_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `access_level` ENUM('read', 'write', 'owner') NOT NULL DEFAULT 'read',
  PRIMARY KEY (`permission_id`),
  INDEX `file_id` (`file_id` ASC),
  INDEX `user_id` (`user_id` ASC),
  CONSTRAINT `permissions_ibfk_1`
    FOREIGN KEY (`file_id`)
    REFERENCES `cloud_storage`.`files` (`file_id`)
    ON DELETE CASCADE,
  CONSTRAINT `permissions_ibfk_2`
    FOREIGN KEY (`user_id`)
    REFERENCES `cloud_storage`.`users` (`user_id`)
    ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS `cloud_storage`.`file_tags` (
  `tag_id` INT NOT NULL AUTO_INCREMENT,
  `file_id` INT NOT NULL,
  `tag_name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`tag_id`),
  INDEX `file_id` (`file_id` ASC),
  CONSTRAINT `file_tags_ibfk_1`
    FOREIGN KEY (`file_id`)
    REFERENCES `cloud_storage`.`files` (`file_id`)
    ON DELETE CASCADE
);
