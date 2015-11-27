
CREATE DATABASE IF NOT EXISTS gamemachine CHARACTER SET utf8 COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON gamemachine.* TO 'gamemachine'@'%' IDENTIFIED BY 'gamemachine' WITH GRANT OPTION;
FLUSH PRIVILEGES;

DROP TABLE IF EXISTS `entities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entities` (
  `id` varchar(128) NOT NULL,
  `value` varbinary(2048) DEFAULT NULL,
  `datatype` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
