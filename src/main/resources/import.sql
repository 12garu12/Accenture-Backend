/* table customers */
Insert into customers(id_card, address, first_name, last_name, email, create_at) values ('12345', '11# 14-08', 'Andrés', 'Rojas', 'rojasAn@hotmail.com','2021-03-10');
Insert into customers(id_card, address, first_name, last_name, email, create_at) values ('987655', '15# 20-08', 'Mr. John', 'Doe', 'john.doe@gmail.com','2021-03-10');
Insert into customers(id_card, address, first_name, last_name, email, create_at) values ('0987667', '05# 14-78','Linus', 'Torvalds', 'linus.tolvards@gmail.com','2021-03-10');
Insert into customers(id_card, address, first_name, last_name, email, create_at) values ('976598', '68# 20-20', 'Rasmus', 'lerdorf', 'rasmus.lerdorf@gmail.com','2021-03-10');
Insert into customers(id_card, address, first_name, last_name, email, create_at) values ('9876', '54# 23-08', 'Erich', 'Gamma', 'erich.gamma@gmail.com','2021-03-10');
Insert into customers(id_card, address, first_name, last_name, email, create_at) values ('0987456', '11# 56-90', 'Richard', 'Helm', 'richard.helm@gmail.com','2021-03-10');
Insert into customers(id_card, address, first_name, last_name, email, create_at) values ('987654', '110# 140-08', 'Ralph', 'Johnson', 'ralph.johnson@gmail.com','2021-03-10');

/*table bills*/
insert into bills(create_at, description, home_delivery, customer_id ) values ('2021-03-10 19:17', 'Ropa deportiva', 0, 1);
insert into bills(create_at, description, home_delivery, customer_id) values ('2021-03-10 19:17', 'Ropa', 0, 2);
insert into bills(create_at, description, home_delivery, customer_id) values ('2021-03-11 19:17', 'Varios', 0, 2);

/*table products*/
insert into products(vat, create_at, price, product_name) values ('8%', '2021-03-10', '22500', 'Pantalón deportivo adidas');
insert into products(vat, create_at, price, product_name) values ('12%', '2021-03-10', '18000', 'Camiseta deportivo Nike');
insert into products(vat, create_at, price, product_name) values ('6%', '2021-03-10', '31200', 'Chaqueta niño juvenil');
insert into products(vat, create_at, price, product_name) values ('11%', '2021-03-10', '40350', 'Saco deportivo Nike');
insert into products(vat, create_at, price, product_name) values ('12%', '2021-03-10', '15600', 'Saco niña deportivo adidas');
insert into products(vat, create_at, price, product_name) values ('6%', '2021-03-10', 'Traje de  baño Swim');
insert into products(vat, create_at, price, product_name) values ('8%', '2021-03-10', 'Conjunto ropa interior dama');

/* table bills_items*/
INSERT INTO bills_items (quantity, product_id, bill_id) VALUES(2, 1, 1);
INSERT INTO bills_items (quantity, product_id, bill_id) VALUES(3, 3, 1);
INSERT INTO bills_items (quantity, product_id, bill_id) VALUES(2, 5, 1);
INSERT INTO bills_items (quantity, product_id, bill_id) VALUES(5, 7, 1);
INSERT INTO bills_items (quantity, product_id, bill_id) VALUES(1, 4, 1);
INSERT INTO bills_items (quantity, product_id, bill_id) VALUES(2, 3, 2);
INSERT INTO bills_items (quantity, product_id, bill_id) VALUES(1, 4, 2);
INSERT INTO bills_items (quantity, product_id, bill_id) VALUES(1, 3, 3);
INSERT INTO bills_items (quantity, product_id, bill_id) VALUES(3, 7, 3);






