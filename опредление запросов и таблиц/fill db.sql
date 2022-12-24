-- ЗАПОЛНЕНИТЕЛЬ --

-- CТАТУСЫ 
INSERT StatusOrder VALUES('PAID')      -- отправлен
INSERT StatusOrder VALUES('RECEIVED')  -- получен

SELECT * FROM Goods;

-- РОЛИ
INSERT Role(Name) VALUES('Sales Manager')
INSERT Role(Name) VALUES('Purchasing Manager')
INSERT Role(Name) VALUES('Admin')

-- ТОВАРЫ 
INSERT Goods(VendorCode, GoodsTitle, Description, Price) VALUES (804451, 'NortFolk Jacket', 'New brand long men`s jacket. Collection 2022. With deep hood, for everyday wear.', 100.50);
INSERT Goods(VendorCode, GoodsTitle, Description, Price) VALUES (8074351, 'NortFolk Jacket', 'New brand warm men`s jacket. Collection 2022. Without hood, for everyday wear.', 120.00);
INSERT Goods(VendorCode, GoodsTitle, Description, Price) VALUES (80551, 'NortFolk Jacket', 'Brand men`s jacket. Collection 2022. With hood, comfortable for driving. Short model, protective fabric.', 80.00);
INSERT Goods(VendorCode, GoodsTitle, Description, Price) VALUES (8044951, 'Women NortFolk Jacket', 'New brand warm women`s jacket. Collection 2022. With removable hood, for everyday wear.', 99.99);

EXEC registerManager 'admin', 'admin,', 3, 'Smirnov Evgenii Alexandrovich', '2001-12-10', '89522795509'

-- ПОСТАВЩИКИ
INSERT Seller(Name, TaxPayerId, Phone) VALUES ('Citi Classic', 781101, '89253530096')
INSERT Seller(Name, TaxPayerId, Phone) VALUES ('Novikov', 781102, '89516415865')
INSERT Seller(Name, TaxPayerId, Phone) VALUES ('Bobrov', 781103, '89996573121')

-- НАКЛАДНЫЕ ЗАКУПКИ
EXEC AddInvoice 781101, 3, 1, '2022-09-24 12:00:00'
EXEC AddInvoice 781102, 1, 1, '2022-08-24 13:00:00' 
EXEC AddInvoice 781103, 3, 2, '2022-07-24 12:00:00' 
EXEC AddInvoice 781104, 4, 3, '2022-06-24 13:00:00' 

-- SUB INVOICES 
EXEC AddSubInvoice 1, 781101, 112481, 100, 50.01
EXEC AddSubInvoice 2, 781102, 80551, 5, 105.00
EXEC AddSubInvoice 3, 781102, 80551, 5, 205.00
EXEC AddSubInvoice 4, 781103, 901351, 100, 60.5
EXEC AddSubInvoice 6, 781104, 741951, 100, 30.00

DECLARE @sum Money;
EXEC SumOfSubInvoices 781102, @sum OUTPUT
SELECT @sum;

-- ЗАКАЗЫ
EXEC AddOrder 791101, 1, '+79516415865', 'Max',    '2022-08-20 12:00:00'
EXEC AddOrder 791102, 2, '+79052296931', 'Dima',   '2022-08-12 13:00:00'
EXEC AddOrder 791103, 2, '+79522795509', 'Stefani','2022-08-14 13:30:00'

EXEC AddSubOrder 1, 791101, 1, 112481, 10
EXEC AddSubOrder 2, 791101, 1, 901351, 12

EXEC AddSubOrder 3, 1, 2, 804451, 1
EXEC AddSubOrder 4, 791102, 1, 741951, 5

EXEC AddSubOrder 5, 791103, 1, 112481, 2
EXEC AddSubOrder 6, 791103, 1, 901351, 1
EXEC AddSubOrder 7, 791103, 1, 741951, 11

-- сумма автоматически для всего заказа

EXEC AddSubOrder 8, 791103, 1, 741951, 84

SELECT * FROM Role;

SELECT * FROM Staff JOIN LoginPassword ON LoginPassword.UserName = Staff.UserName;

SELECT * FROM StatusOrder;
SELECT * FROM Orders
SELECT * FROM Manager_VIEW
SELECT * FROM LoginPassword;
SELECT * FROM Seller;
SELECT * FROM InvoiceContent;
SELECT IdStaff, Staff.UserName, Password, FIO, Role.Name as RoleName, DateOfBirth, Phone FROM Staff 
    JOIN Role ON Role.IdRole = Staff.IdRole 
    JOIN LoginPassword ON LoginPassword.UserName = Staff.UserName 
    WHERE Staff.UserName = 'admin'

SELECT OrderNumber, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.UserName
    FROM Orders 
        JOIN Staff ON Orders.IdManager = Staff.IdStaff
            ORDER BY Orders.[Date] ASC

SELECT VendorCode, Quantity, Price, StatusOrder.Title, OrderContentNumber, GoodsTitle FROM OrderContent 
        JOIN StatusOrder ON OrderContent.Status = StatusOrder.IdStatus 
        JOIN Goods G on OrderContent.VendorCode = G.VendorCode
        WHERE OrderNumber = 56


UPDATE LoginPassword SET Password='7c4a8d09ca3762af61e59520943dc26494f8941b' WHERE UserName = 'admin'
