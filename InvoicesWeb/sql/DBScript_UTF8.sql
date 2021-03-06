DROP DATABASE IF EXISTS gera_invoices;

CREATE DATABASE gera_invoices WITH ENCODING = 'UTF8';

\c gera_invoices

CREATE TABLE sellers
(
  sel_id SERIAL NOT NULL PRIMARY KEY,
  sel_name VARCHAR(100) NOT NULL,
  sel_address VARCHAR(200) NOT NULL
);

CREATE TABLE invoices
(
  inv_id SERIAL NOT NULL PRIMARY KEY,
  inv_number INT NOT NULL,
  inv_date DATE NOT NULL,
  inv_sum INT NOT NULL,
  sel_id INT NOT NULL REFERENCES sellers (sel_id) ON DELETE CASCADE
);

CREATE TABLE goods
(
  g_id SERIAL NOT NULL PRIMARY KEY,
  g_description VARCHAR(200) NOT NULL,
  g_amount INT NOT NULL,
  g_price INT NOT NULL,
  g_sum INT NOT NULL,
  inv_id INT NOT NULL REFERENCES invoices (inv_id) ON DELETE CASCADE
);

INSERT INTO sellers (sel_name, sel_address)
VALUES
('ООО "Ромашка"', '100, г. Рязань, ул. Какая-то, д.5'),
('ООО "Компатер"', '1647, г. Москва, ул. Неизвестная, д.18'),
('АО "Фирма Cool"', '7457, г. Воронеж, ул. Известная, д.27'),
('ПАО "Копыта"', '744, г. Мурманск, ул. Широкая, д.64'),
('ООО "Магазино"', '62, г. Смоленск, ул. Знаменитая, д.4');

INSERT INTO invoices (inv_number, inv_date, inv_sum, sel_id)
VALUES
(15, '2017-01-01', 30000, 1),
(1, '2003-06-17', 150, 1),
(64, '2008-04-09', 400, 1),

(4, '2014-12-01', 148000, 2),
(8, '2003-06-17', 4500, 2),
(804, '2012-07-15', 1100, 2),

(65, '2017-01-01', 346000, 3),
(1, '2003-06-17', 45000, 3),
(69, '2008-06-14', 6400, 3),

(74, '2006-03-16', 7000, 4),
(96, '2007-09-27', 457400, 4),
(6078, '2003-06-17', 12340, 4),

(32, '2017-01-01', 6310, 5),
(47, '2009-03-16', 57900, 5),
(81, '2007-05-01', 73200, 5);

INSERT INTO goods (g_description, g_amount, g_price, g_sum, inv_id)
VALUES
('Букет цветов', 1, 15000, 15000, 1),
('Сувенир', 2, 7500, 15000, 1),

('Календарь', 3, 50, 150, 2),

('Салфетки', 2, 50, 100, 3),
('Пакет', 5, 20, 100, 3),
('Конфеты', 10, 20, 200, 3),

('Системный блок', 1, 100000, 100000, 4),
('Монитор', 1, 40000, 40000, 4),
('Клавиатура', 1, 4000, 4000, 4),
('Мышь', 1, 4000, 4000, 4),

('Принтер', 1, 4000, 4000, 5),
('Картридж', 2, 250, 500, 5),

('Провода', 5, 200, 1000, 6),
('Коврик для мышки', 1, 100, 100, 6),

('Аренда ресторана для корпоратива', 1, 300000, 300000, 7),
('Фейерверк', 1, 46000, 46000, 7),

('Разбитая посуда', 2, 2500, 5000, 8),
('Обслуживание официантами', 1, 40000, 40000, 8),

('Музыкальное сопровождение', 1, 6400, 6400, 9),

('Чучело кролика', 1, 3500, 3500, 10),
('Чучело белки', 1, 3500, 3500, 10),

('Сафари', 1, 230000, 230000, 11),
('Охота', 1, 227400, 227400, 11),

('Аренда палатки', 3, 4000, 12000, 12),
('Аренда мангала', 1, 340, 340, 12),

('Кофеварка', 1, 4000, 4000, 13),
('Чашки', 4, 100, 400, 13),
('Набор плюшек', 1, 1910, 1910, 13),

('Пуфики', 4, 4000, 16000, 14),
('Стулья', 4, 4000, 16000, 14),
('Столы', 4, 4000, 16000, 14),
('Самое нужное что-то', 4, 2475, 9900, 14),

('Приставка xBox', 1, 50000, 50000, 15),
('Телевизор', 1, 23200, 23200, 15);