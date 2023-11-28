INSERT INTO `employee` (employee_name)
VALUES ('tony'),
       ('jane'),
       ('mark');
commit;

INSERT INTO `product` (product_name, employee_id, sale_status)
VALUES ('nike air max', 1, 'A'),
       ('adidas superstar', 2, 'S'),
       ('chanel classic', 3, 'X');
commit;

INSERT INTO `deal` (deal_name, deal_status)
VALUES ('sports', 'A'),
       ('luxury', 'I');
commit;

INSERT INTO `deal_product` (deal_id, product_id)
VALUES (1, 1),
       (1, 2),
       (2, 3);
commit;