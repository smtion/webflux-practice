CREATE TABLE `product`
(
    `product_id` int NOT NULL AUTO_INCREMENT,
    `product_name` varchar(255) NOT NULL,
    `employee_id` int NOT NULL,
    `sale_status` varchar(50) NOT NULL COMMENT '{A = Active, S = Sold out, X = Inactive}',
    PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `employee`
(
    `employee_id` int NOT NULL AUTO_INCREMENT,
    `employee_name` varchar(50) NOT NULL,
    PRIMARY KEY (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `deal`
(
    `deal_id` int NOT NULL AUTO_INCREMENT,
    `deal_name` varchar(255) NOT NULL,
    `deal_status` varchar(50) NOT NULL COMMENT '{A = Active, I = Inactive}',
    PRIMARY KEY (`deal_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='deal은 여러개의 product로 구성된 집합';

CREATE TABLE `deal_product`
(
    `deal_id` int NOT NULL,
    `product_id` int NOT NULL,
    UNIQUE KEY deal_product_default_unique_key (deal_id, product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;