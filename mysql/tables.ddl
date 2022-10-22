
CREATE TABLE `quote_tweets` (
  `tweet_id` varchar(255) DEFAULT NULL,
  `tweet` text,
  KEY `tweet_id` (`tweet_id`),
  CONSTRAINT `quote_tweets_ibfk_1` FOREIGN KEY (`tweet_id`) REFERENCES `users` (`tweet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `users` (
  `user_id` varchar(255) DEFAULT NULL,
  `tweet_id` varchar(255) NOT NULL,
  `tweet` text,
  `tweet_time` datetime DEFAULT NULL,
  PRIMARY KEY (`tweet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `sentiment_results` (
  `tweet_id` text,
  `tweet_time` datetime DEFAULT NULL,
  `sentiment` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
