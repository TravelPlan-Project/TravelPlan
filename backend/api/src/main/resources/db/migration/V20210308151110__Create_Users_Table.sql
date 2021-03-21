DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME DEFAULT NULL,
  `deleted_at` DATETIME DEFAULT NULL,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE INDEX `email_index` ON `users` (`email`);