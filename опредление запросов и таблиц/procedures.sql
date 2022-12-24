-- FINAL

-- ДОБАВЛЕНИЕ
    -- 1. Добавление товаров 
        INSERT Goods(VendorCode, GoodsTitle, Description, Price) VALUES (1000, 'Spoon', 'Silver spoon.', 125.50);

    -- 2. Добавление поставщика
        INSERT Seller(Name, TaxPayerId, Phone) VALUES ('Out knives',  193312, '89531437811')

-- 
-- ИЗМЕНЕНИЕ
    -- 1. Изменение личных данных конкретного менеджера
        UPDATE Staff SET IdRole=1, FIO='Staheev Dmitry Ivanovich', DateOfBirth='2002-01-08', Phone='+79214782210'
            WHERE (UserName='admin')

    -- 2. Изменение пароля/логина менеджера
    UPDATE LoginPassword SET UserName='0001ms', Password='notqwerty'
            WHERE (UserName ='0001ms' AND 1 = (SELECT Staff.IdStaff FROM Staff WHERE Staff.UserName = '0001ms'))


    -- 4. Изменение статуса учётной записи конкретного менеджера на "не активен"
    UPDATE LoginPassword SET IsValidAccount=0
        WHERE (LoginPassword.UserName='0001ms')
    -- 2. Изменение статуса учётной записи конкретного менеджера на "не активен"
    -- 3. Изменение личных данных конкретного поставщика
        UPDATE Seller SET Name='Out knives', TaxPayerId=193312 , Phone='89531437811'
            WHERE (Seller.IdSeller=1)
    --4. Изменение информации об одном товаре 
        UPDATE Goods SET VendorCode=1001, 
                     GoodsTitle='Spoon', 
                     Description='Silver spoon.', 
                     Price=12690
            WHERE (Goods.VendorCode=1000)

-- 
-- ВЫВОД 
    -- 1. Вывод всех статусов заказов 
        SELECT * FROM StatusOrder

    -- 2. Вывод всех ролей 
        SELECT * FROM Role

    -- 3. Вывод данных о конкретном менеджере
        SELECT FIO, Role.Name as RoleName, FIO, DateOfBirth, Phone, LoginPassword.UserName, LoginPassword.Password, IsValidAccount FROM Staff
            JOIN Role ON Role.IdRole = Staff.IdRole
            JOIN LoginPassword on Staff.UserName = LoginPassword.UserName
            WHERE (Staff.IdStaff=1)

    -- 4. Вывод данных о конкретном поставщике
        -- по id 
        SELECT * FROM Seller
            WHERE Seller.IdSeller=1

       -- по номеру телефона 
        SELECT * FROM Seller
            WHERE Seller.Phone='89531437811'

        -- по ИНН
        SELECT * FROM Seller
            WHERE Seller.TaxPayerId=193312


    -- 5. Вывод данных о конкретном товаре
        SELECT * FROM Goods
            WHERE VendorCode = 1001
    
    -- 6. Вывод данных о товаре в ценовом диапазоне
        -- меньше
            SELECT * FROM Goods WHERE Price <= 20.0 ORDER BY Price ASC
        
        -- больше
            SELECT * FROM Goods WHERE Price >= 20.0 ORDER BY Price DESC

--
-- СЛОЖНЫЕ ЗАПРОСЫ
    -- 3. Вывод информации о конкретной накладной и ее закупках 
        SELECT Invoice.InvoiceNumber, [Date], TotalSum, Seller.IdSeller, Staff.UserName FROM Invoice
            JOIN Staff ON Invoice.IdManger = Staff.IdStaff
            JOIN Seller ON Invoice.IdSeller = Seller.IdSeller
                WHERE InvoiceNumber = 1
                    ORDER BY Invoice.[Date] ASC

    -- 4. Вывод информации о конкретном заказе и его под заказах
        SELECT OrderContent.VendorCode, Quantity, OrderContent.Price, StatusOrder.Title, OrderContentNumber, GoodsTitle FROM OrderContent
            JOIN StatusOrder ON OrderContent.Status = StatusOrder.IdStatus 
            JOIN Goods G on OrderContent.VendorCode = G.VendorCode
                WHERE OrderNumber = 1

    -- 5. Вывод кол-ва продаж товара за период 
        SELECT SUM(Quantity) AS SumQuantity FROM OrderContent 
                JOIN Orders ON Orders.OrderNumber = OrderContent.OrderNumber
                    WHERE OrderContent.VendorCode = 1001 AND
                    Orders.[Date] >= '2020-12-01' AND
                    Orders.[Date] <= '2020-12-09'

    -- 6. Вывод кол-ва закупок товара за период
        SELECT SUM(Quantity) AS SumQuantity FROM InvoiceContent
                JOIN Invoice ON Invoice.InvoiceNumber = InvoiceContent.InvoiceNumber
                    WHERE InvoiceContent.VendorCode = 1001 AND
                    Invoice.[Date] >= '2020-12-01' AND
                    Invoice.[Date] <= '2020-12-09'

    -- 7. Вывод суммы закупок товара за период
        SELECT SUM(SumPurchaseOneItem) FROM ( 
            SELECT (Purchase)*(Quantity) AS SumPurchaseOneItem FROM InvoiceContent 
                JOIN Invoice ON Invoice.InvoiceNumber = InvoiceContent.InvoiceNumber 
                    WHERE InvoiceContent.VendorCode = 1001 AND
                    Invoice.[Date] >= '2020-12-01' AND
                    Invoice.[Date] <= '2020-12-09' ) as PurchaseQuantity

    -- 8. Вывод суммы продаж товара за период
        SELECT SUM(SumPriceOneItem) FROM (
            SELECT (Price)*(Quantity) AS SumPriceOneItem FROM OrderContent
                JOIN Orders ON Orders.OrderNumber = OrderContent.OrderNumber
                    WHERE OrderContent.VendorCode = 1001 AND
                    Orders.[Date] >= '2020-12-01' AND +
                    Orders.[Date] <= '2020-12-09' ) as PriceQuantity

    -- 9. Вывод накладных по номеру телефона менеджера 
        SELECT Invoice.InvoiceNumber, [Date], TotalSum, Seller.IdSeller, Staff.UserName FROM Invoice
            JOIN Staff ON Invoice.IdManger = Staff.IdStaff 
            JOIN Seller ON Invoice.IdSeller = Seller.IdSeller
                WHERE Staff.Phone = 'phone'
                    ORDER BY Invoice.[Date] ASC

    -- 10. Вывод заказов по номеру телефона менеджера 
        SELECT OrderNumber, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.UserName FROM Orders
            JOIN Staff ON Orders.IdManager = Staff.IdStaff
                WHERE Staff.Phone = 'phone'
                    ORDER BY Orders.[Date] ASC 


    -- 11. Вывод всех накладных конкретного поставщика
        -- по телефону
         SELECT Invoice.InvoiceNumber, [Date], TotalSum,
            Seller.IdSeller, Staff.UserName FROM Invoice 
            JOIN Staff ON Invoice.IdManger = Staff.IdStaff 
            JOIN Seller ON Invoice.IdSeller = Seller.IdSeller
                WHERE Seller.Phone = '89531437811' ORDER BY Invoice.[Date] ASC
        
        -- по ИНН
        SELECT Invoice.InvoiceNumber, [Date], TotalSum, 
            Seller.IdSeller, Staff.UserName FROM Invoice 
            JOIN Staff ON Invoice.IdManger = Staff.IdStaff 
            JOIN Seller ON Invoice.IdSeller = Seller.IdSeller
                WHERE Seller.TaxPayerId = 193312 ORDER BY Invoice.[Date] ASC


    -- 12. Вывод всех заказов конкретного покупателя по номеру телефона
        SELECT OrderNumber, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.UserName FROM Orders
            JOIN Staff ON Orders.IdManager = Staff.IdStaff
                WHERE Orders.PnoneCustomer = '89522795509'
                    ORDER BY Orders.[Date] ASC

    -- 13. Вывод всех заказов, оформленных конкретным менеджером 
        SELECT OrderNumber, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.UserName FROM Orders
            JOIN Staff ON Orders.IdManager = Staff.IdStaff
                WHERE Staff.IdStaff =  1
                    ORDER BY Orders.[Date] ASC

    -- 14. Вывод кол-ва оформленных заказов и сумму заказов для каждого менеджера продаж за период времени
        SELECT Staff.Phone as PhoneManager, Staff.FIO as FIOManager, SUM(TotalSum) as SumPrice, SUM(Quantity) as SumQuantity FROM Orders
            JOIN OrderContent ON Orders.OrderNumber = OrderContent.OrderNumber
            JOIN Staff ON Orders.IdManager = Staff.IdStaff
                WHERE Orders.[Date] >= '2020-12-01' AND
                Orders.[Date] <= '2020-12-09'
                    GROUP BY Staff.Phone, Staff.FIO

    -- 15. Вывод кол-ва оформленных накладных и суммы закупок для каждого менеджера закупок за период времени 
        SELECT Staff.Phone as PhoneManager, Staff.FIO as FIOManager, SUM(TotalSum) as SumPrice, SUM(Quantity) as SumQuantity FROM Invoice
                JOIN InvoiceContent ON Invoice.InvoiceNumber = InvoiceContent.InvoiceNumber 
                JOIN Staff ON Invoice.IdManger = Staff.IdStaff
                    WHERE Invoice.[Date] >= '2020-12-01' AND
                    Invoice.[Date] <= '2020-12-09'
                        GROUP BY Staff.Phone, Staff.FIO

    -- 16. Вывод под-заказов, имеющие конкретный статус заказа 
        SELECT OrderContent.VendorCode, Quantity, OrderContent.Price, StatusOrder.Title, OrderContentNumber, GoodsTitle FROM OrderContent
            JOIN StatusOrder ON OrderContent.Status = StatusOrder.IdStatus 
            JOIN Goods G on OrderContent.VendorCode = G.VendorCode
                WHERE OrderNumber = orderNumber AND StatusOrder.Title = 'PAID'


--
-- ХРАНИМЫЕ ПРОЦЕДУРЫ
    -- 1. регистрация менеджера
        GO
        create PROCEDURE registerManager(@UserName Varchar(20), @Password VARCHAR(64),@IdRole INT, @FIO VARCHAR(100), @DateOfBirth DATE, @Phone VARCHAR(13)) 
            AS
            BEGIN
                INSERT LoginPassword(UserName, Password) VALUES (@UserName, @Password)
                IF (@@ERROR = 0)
                    BEGIN
                        INSERT Staff(IdRole, FIO, DateOfBirth, Phone, UserName) VALUES (@IdRole, @FIO, @DateOfBirth, @Phone, @UserName)
                        IF (@@ERROR != 0)
                        BEGIN
                            DELETE LoginPassword WHERE UserName=@UserName
                            RAISERROR (15600, -1, -1, 'registerManager: not created record');
                        END
                    END
                ELSE
                    RAISERROR (15600, -1, -1, 'registerManager: not created account');
            END;

    -- 2. Добавление заказа 
        -- 1. Сам заказ
        GO
        CREATE PROCEDURE AddOrder(@OrderNumber INT, @IdManager INT, @PhoneCustomer Varchar(13), @NameCustomer Varchar(100), @Date DATETIME)
            AS 
            INSERT Orders(OrderNumber, IdManager, PnoneCustomer, NameCustomer, [Date]) VALUES(@OrderNumber, @IdManager, @PhoneCustomer, @NameCustomer, @Date)
        -- 2. Добавление под-заказа
            GO
            CREATE PROCEDURE AddSubOrder(@OrderContentNumber INT, @OrderNumber INT, @Status INT, @VendorCode INT,  @Quantity INT)
                AS 
                DECLARE @countAmountOrder INT
                    SET @countAmountOrder = (SELECT Amount FROM Goods WHERE Goods.VendorCode=@VendorCode)
                DECLARE @Price Money

                IF (@countAmountOrder - @Quantity >=0)
                    BEGIN
                        SET @Price = (SELECT Price FROM Goods WHERE Goods.VendorCode=@VendorCode)
                        INSERT OrderContent(OrderContentNumber, OrderNumber, [Status], VendorCode, Quantity, Price) VALUES (@OrderContentNumber, @OrderNumber, @Status, @VendorCode, @Quantity, @Price)
                        IF (@@ERROR = 0)
                            BEGIN
                                SET @countAmountOrder = @countAmountOrder - @Quantity
                                EXEC UpdateGoodsAfterOrder @VendorCode, @countAmountOrder
                            END
                        ELSE 
                            PRINT('U can not buy. Incorrect data')
                    END
                ELSE
                    PRINT('U can not buy more then have')

                DECLARE @TotalSum MONEY = 0.00
                    EXEC SumOfSubOrders @OrderNumber, @TotalSum OUTPUT
                    UPDATE Orders SET TotalSum=@TotalSum
                        WHERE Orders.OrderNumber = @OrderNumber
        -- 3. Изменение кол-ва товара после покупки  
            GO
            CREATE PROCEDURE UpdateGoodsAfterOrder( @VendorCode INT,  @NewAmount INT)
                AS UPDATE Goods SET Amount=@NewAmount
                   WHERE (Goods.VendorCode=@VendorCode)
        -- 4. Подсичать сумму всего заказа
            GO
            CREATE PROCEDURE SumOfSubOrders( @OrderNumber INT, @Sum Money OUT) 
                AS SELECT @Sum = SUM(SumPriceOneOrder) FROM ( SELECT (Price)*(Quantity) AS SumPriceOneOrder FROM OrderContent 
                    WHERE OrderNumber=@OrderNumber) as SumPrice
                RETURN;

    -- 3. Добавление накладной
        -- 1.  Cама накладная  
            GO
            CREATE PROCEDURE AddInvoice(@InvoiceNumber INT, @IdManager INT, @IdSeller INT, @Date DATETIME)
                AS INSERT Invoice(InvoiceNumber, IdManger, IdSeller, [Date]) VALUES(@InvoiceNumber, @IdManager, @IdSeller, @Date)

        -- 2. Под-закупка 
            GO
            CREATE PROCEDURE AddSubInvoice(@InvoiceContentNumber INT,
                                   @InvoiceNumber INT,
                                   @VendorCode INT, 
                                   @Quantity INT,
                                   @Purchase Money)
                AS
                    DECLARE @countAmountInvoice INT
                    SET @countAmountInvoice = (SELECT Amount FROM Goods WHERE Goods.VendorCode=@VendorCode)

                    DECLARE @countPurchaseInvoice Money
                    SET @countPurchaseInvoice = (SELECT Purchase FROM Goods WHERE Goods.VendorCode=@VendorCode)

                    SET @countPurchaseInvoice = (@countPurchaseInvoice * @countAmountInvoice + @Quantity * @Purchase) / (@countAmountInvoice+@Quantity)
                    SET @countAmountInvoice = (@countAmountInvoice+@Quantity)

                    PRINT(@countPurchaseInvoice)
                    PRINT(@countAmountInvoice)

                    INSERT InvoiceContent(InvoiceContentNumber, InvoiceNumber, VendorCode, Quantity, Purchase) VALUES (@InvoiceContentNumber, @InvoiceNumber, @VendorCode, @Quantity, @Purchase)

                    if (@@ERROR = 0)
                        EXEC UpdateGoodsAfterInvoice @VendorCode, @countAmountInvoice, @countPurchaseInvoice

                    DECLARE @TotalSum MONEY = 0.00
                    EXEC SumOfSubInvoices @InvoiceNumber, @TotalSum OUTPUT
                    UPDATE Invoice SET Invoice.TotalSum=@TotalSum
                        WHERE Invoice.InvoiceNumber = @InvoiceNumber

        -- 3. Изменение цены закупки и кол-ва товара после закупки 
            GO
            CREATE PROCEDURE UpdateGoodsAfterInvoice(@VendorCode INT, @NewAmount INT, @NewPurchase Money)
                AS UPDATE Goods SET Amount=@NewAmount, Purchase=@NewPurchase
                    WHERE (Goods.VendorCode=@VendorCode)
        -- 4. Подсичать сумму всего прихода 
            GO 
            CREATE PROCEDURE SumOfSubInvoices( @InvoiceNumber INT, @Sum Money OUT) 
                AS SELECT @Sum = SUM(SumPriceOneInvoice) FROM ( SELECT (Purchase)*(Quantity) AS SumPriceOneInvoice FROM InvoiceContent 
                    WHERE InvoiceNumber=@InvoiceNumber) as SumPurchase
                RETURN;

    -- 4. Вывод доступных для покупки товаров
        GO
        CREATE PROCEDURE GetAvailiableGoods AS
            SELECT * FROM Cataloge
                WHERE (Amount > 0)
    -- 5. Изменение статуса конкретного под-заказа
        GO
        CREATE PROCEDURE ChangeStatus(@OrderNumber INT, @OrderContentNumber INT, @IdNewStatus INT) AS
            UPDATE OrderContent SET [Status]=@IdNewStatus
            WHERE (OrderContent.OrderNumber = @OrderNumber) AND (OrderContent.OrderContentNumber=@OrderContentNumber)

--

--
-- Представления
-- 
-- 1. Вывод всех поставщиков
    GO
    CREATE VIEW Seller_VIEW AS 
        SELECT Name, Phone, TaxPayerId FROM Seller;       

-- 2. Вывод всех менеджеров
    GO
    CREATE VIEW Manager_VIEW AS
        SELECT IdStaff, Staff.UserName, Role.Name as NameRole, FIO, DateOfBirth, Phone FROM Staff
            JOIN Role ON Role.IdRole = Staff.IdRole 

-- 3. Вывод всех товаров
    GO 
    CREATE VIEW Cataloge AS 
        SELECT VendorCode, GoodsTitle, Description, Price, Amount FROM GOODS
 
-- 4. Вывод всех заказов и их под заказов
    GO
    CREATE VIEW OrderAndContent AS 
        SELECT Orders.OrderNumber, Status, OrderContentNumber, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.FIO as FIOManager, Staff.Phone as PhoneManager, VendorCode, Quantity, Price  FROM Orders
            JOIN Staff ON Orders.IdManager = Staff.IdStaff 
            JOIN OrderContent ON Orders.OrderNumber = OrderContent.OrderNumber

-- 5. Вывод всех закупок и их под закупок
    GO
    CREATE VIEW InvoiceAndContent AS   
        SELECT Invoice.InvoiceNumber, InvoiceContentNumber, [Date], TotalSum, Seller.Name as NameSeller, Seller.Phone as PhoneSeller, TaxPayerId, Staff.FIO as FIOManager, Staff.Phone as PhoneManager, InvoiceContent.VendorCode, InvoiceContent.Quantity, InvoiceContent.Purchase  FROM Invoice
            JOIN Staff ON Invoice.IdManger = Staff.IdStaff 
            JOIN Seller ON Invoice.IdSeller = Seller.IdSeller
            JOIN InvoiceContent ON Invoice.InvoiceNumber = InvoiceContent.InvoiceNumber

-- 6. Вывод всех менеджеров продаж и оформленных ими под заказов
    GO
    CREATE VIEW MangerAndSubOrder AS
        SELECT Staff.FIO as FIOManager, Staff.Phone as PhoneManager, [Date], OrderContent.OrderContentNumber, OrderContent.VendorCode, OrderContent.Quantity, OrderContent.Price FROM Orders
            JOIN Staff ON Orders.IdManager = Staff.IdStaff 
            JOIN OrderContent ON Orders.OrderNumber = OrderContent.OrderNumber

-- 7. Вывод всех менеджеров закупок и оформленных ими закупок
    GO 
    CREATE VIEW ManagerAndSubInvoice AS
        SELECT Staff.FIO as FIOManager, Staff.Phone as PhoneManager, [Date], InvoiceContent.InvoiceContentNumber, InvoiceContent.VendorCode, InvoiceContent.Quantity, InvoiceContent.Purchase FROM Invoice
            JOIN Staff ON Invoice.IdManger = Staff.IdStaff 
            JOIN InvoiceContent ON Invoice.InvoiceNumber = InvoiceContent.InvoiceNumber



















--
-- ":KHUGEYVCMUYFTCRXCTVBNBHVGCFVGBHNJHBGVBHGHNBG"
--

-- 3. Вывод данных о конкретном товаре по артикула товара 
    SELECT * FROM Goods
        WHERE (VendorCode = 3626)

--4. Вывод всех товаров в ценовом диапазоне (цены продажи), остортированных по возрастанию 
    SELECT * FROM Cataloge
        WHERE (Price >= 0.00 AND Price <= 10000) AND (Amount > 0)
        ORDER BY Price ASC

-- 4. Вывод всех товаров в ценовом диапазоне (цены продажи), остортированных по убыванию 
    SELECT * FROM Cataloge
        WHERE (Price >= 0.00 AND Price <= 12000) AND (Amount > 0)
        ORDER BY Price DESC

-- 5. Вывод всех товаров, которых в кол-ве меньше чем N, отсортированных по возрастанию
    SELECT * FROM Goods
        WHERE (Amount < 1)
        ORDER BY Amount ASC

-- 5. Вывод всех товаров, которых в кол-ве больше чем N, отсортированных по возрастанию
    SELECT * FROM Goods
        WHERE (Amount > -1)
        ORDER BY Amount ASC

-- МЕНЕДЖЕРЫ
-- 1. регистрация 
    GO
    create PROCEDURE registerManager(
            @UserName Varchar(20), 
            @Password VARCHAR(64),
            @IdRole INT, 
            @FIO VARCHAR(100), 
            @DateOfBirth DATE, 
            @Phone VARCHAR(13)) AS
        BEGIN
        INSERT LoginPassword(UserName, Password) VALUES (@UserName, @Password)
        IF (@@ERROR = 0)
            BEGIN
                INSERT Staff(IdRole, FIO, DateOfBirth, Phone, UserName) VALUES (@IdRole, @FIO, @DateOfBirth, @Phone, @UserName)
                IF (@@ERROR != 0)
                BEGIN
                    DELETE LoginPassword WHERE UserName=@UserName
                    RAISERROR (15600, -1, -1, 'registerManager: not created record');
                END
            END
        ELSE
            RAISERROR (15600, -1, -1, 'registerManager: not created account');
        END;


    
-- 6. Вывод всех менеджеров
     SELECT * FROM Staff

-- 7. Вывод всех менеджеров 
    SELECT FIO, Role.Name as RoleName, DateOfBirth, Phone FROM Manager_VIEW
        JOIN Role ON Role.IdRole = Manager_VIEW.IdRole

GO
-- 8. Вывод всех менеджеров
CREATE VIEW Manager_VIEW AS
    SELECT IdStaff, Staff.UserName, Role.Name as NameRole, FIO, DateOfBirth, Phone FROM Staff
                JOIN Role ON Role.IdRole = Staff.IdRole 
GO
-- 9. Вывод всех не заблокированных менеджеров
SELECT IdStaff, NameRole, FIO, DateOfBirth, Phone, LoginPassword.UserName, LoginPassword.[Password], IsValidAccount FROM Manager_VIEW 
    JOIN LoginPassword on Manager_VIEW.UserName = LoginPassword.UserName
        WHERE IsValidAccount=1
--
-- ПОСТАВЩИКИ
-- 




-- -- 5. Вывод всех поставщиков
--     SELECT * FROM Seller_VIEW
-- 
-- НАКЛАДНЫЕ ЗАКУПКИ
--
-- 1. Вывод всех накладных 
    SELECT InvoiceNumber, [Date], TotalSum, Seller.Name as NameSeller, Seller.Phone as PhoneSeller, TaxPayerId, Staff.FIO as FIOManager, Staff.Phone as PhoneManager FROM Invoice
        JOIN Staff ON Invoice.IdManger = Staff.IdStaff 
        JOIN Seller ON Invoice.IdSeller = Seller.IdSeller

-- 1. Вывод конкретной накладной
    SELECT [Date], TotalSum, Seller.Name as NameSeller ,Seller.Phone as PhoneSeller, TaxPayerId, Staff.FIO as FIOManager, Staff.Phone as PhoneManager FROM Invoice
        JOIN Staff ON Invoice.IdManger = Staff.IdStaff 
        JOIN Seller ON Invoice.IdSeller = Seller.IdSeller
        WHERE (InvoiceNumber=2)

-- 2. Вывод всех накладных за период по дате оформления закукпи, отсортированных по убыванию
    SELECT InvoiceNumber, [Date], TotalSum, Seller.Name as NameSeller ,Seller.Phone as PhoneSeller, TaxPayerId, Staff.FIO as FIOManager, Staff.Phone as PhoneManager FROM Invoice
        JOIN Staff ON Invoice.IdManger = Staff.IdStaff 
        JOIN Seller ON Invoice.IdSeller = Seller.IdSeller
        WHERE Invoice.[Date] >= '2022-06-23' AND Invoice.[Date] <= '2022-07-25'
        ORDER BY Invoice.[Date] DESC

-- 2. Вывод всех накладных за период по дате оформления закукпи, отсортированных по возрастанию
    SELECT InvoiceNumber, [Date], TotalSum, Seller.Name as NameSeller, Seller.Phone as PhoneSeller, TaxPayerId, Staff.FIO as FIOManager, Staff.Phone as PhoneManager FROM Invoice
        JOIN Staff ON Invoice.IdManger = Staff.IdStaff 
        JOIN Seller ON Invoice.IdSeller = Seller.IdSeller
        WHERE Invoice.[Date] >= '2022-06-23' AND Invoice.[Date] <= '2022-07-25'
        ORDER BY Invoice.[Date] ASC

-- 3. Вывод всех накладных конкретного поставщика (по телефону поставщика)
    SELECT InvoiceNumber, [Date], TotalSum, Seller.Name as NameSeller, TaxPayerId, Staff.FIO as FIOManager, Staff.Phone as PhoneManager FROM Invoice
        JOIN Seller ON Invoice.IdSeller = Seller.IdSeller
        WHERE Seller.Phone = '89516415865'

-- 3. Вывод всех накладных конкретного поставщика (по ИНН поставщика)
    SELECT InvoiceNumber, [Date], TotalSum, Seller.Name as NameSeller, Seller.Phone as PhoneSeller, Staff.FIO as FIOManager, Staff.Phone as PhoneManager FROM Invoice
        JOIN Seller ON Invoice.IdSeller = Seller.IdSeller
        WHERE Seller.TaxPayerId = 781199
 
-- 4. Вывод содержания накладной (всех под-закупок) по номеру накладной
    SELECT * FROM InvoiceContent
        WHERE InvoiceNumber = 781102

-- 5. Вывод всех закупках и их под-закупках
   SELECT Invoice.InvoiceNumber, InvoiceContentNumber, [Date], TotalSum, Seller.Name as NameSeller, Seller.Phone as PhoneSeller, TaxPayerId, InvoiceContent.VendorCode, InvoiceContent.Quantity, InvoiceContent.Purchase  FROM Invoice
        JOIN Seller ON Invoice.IdSeller = Seller.IdSeller
        JOIN InvoiceContent ON Invoice.InvoiceNumber = InvoiceContent.InvoiceNumber
-- 
-- НАКЛАДНЫЕ ПРОДАЖИ
--
-- 1. Вывод всех заказов
    SELECT OrderNumber, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.FIO as FIOManager, Staff.Phone as PhoneManager FROM Orders
        JOIN Staff ON Orders.IdManager = Staff.IdStaff 

-- 1. Вывод конкретного заказа
    SELECT [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.FIO as FIOManager, Staff.Phone as PhoneManager FROM Orders
        JOIN Staff ON Orders.IdManager = Staff.IdStaff 
        WHERE (OrderNumber=791101)

-- 2. Вывод всех заказов за период по дате оформления заказа, отсортированных по убыванию
    SELECT OrderNumber, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.FIO as FIOManager, Staff.Phone as PhoneManager FROM Orders
        JOIN Staff ON Orders.IdManager = Staff.IdStaff 
        WHERE Orders.[Date] > '2022-08-14' AND Orders.[Date] < '2022-08-21'
        ORDER BY Orders.[Date] ASC

-- 2. Вывод всех заказов за период по дате оформления заказа, отсортированных по возрастанию
    SELECT OrderNumber, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.FIO as FIOManager, Staff.Phone as PhoneManager FROM Orders
        JOIN Staff ON Orders.IdManager = Staff.IdStaff 
        WHERE Orders.[Date] > '2022-08-14' AND Orders.[Date] < '2022-08-21'
        ORDER BY Orders.[Date] DESC

-- 3. Вывод всех заказов конкретного покупателя (по номеру телефона)
    SELECT OrderNumber, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.FIO as FIOManager, Staff.Phone as PhoneManager FROM Orders
        JOIN Staff ON Orders.IdManager = Staff.IdStaff 
        WHERE PnoneCustomer = '+79052296931'
        ORDER BY [Date] DESC

-- 4. Вывод всех заказов, оформленных конкретным менеджером
    SELECT OrderNumber, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.FIO as FIOManager, Staff.Phone as PhoneManager FROM Orders
        JOIN Staff ON Orders.IdManager = Staff.IdStaff
        WHERE Staff.IdStaff = 4
        ORDER BY [Date] DESC

-- 5. Вывод содержания заказа (под-заказов) по номеру заказа
    SELECT * FROM OrderContent
        WHERE OrderNumber = 791101

-- 6. Вывод под-заказов и даты оформления по статусу за период по возрастанию по дате 
    SELECT Orders.[Date], OrderContentNumber, VendorCode, Quantity, Price  FROM OrderContent
        JOIN Orders ON OrderContent.OrderNumber = Orders.OrderNumber
        WHERE ([Status] = 1) AND 
              (Orders.[Date] >= '2020-12-20') AND (Orders.[Date] <= '2023-12-20')
        ORDER BY Orders.[Date] ASC

-- 6. Вывод под-заказов и даты оформления по статусу за период по убыванию по дате 
    SELECT Orders.[Date], OrderContentNumber, VendorCode, Quantity, Price  FROM OrderContent
        JOIN Orders ON OrderContent.OrderNumber = Orders.OrderNumber
        WHERE ([Status] = 1) AND 
              (Orders.[Date] >= '2020-12-20') AND (Orders.[Date] <= '2023-12-20')
        ORDER BY Orders.[Date] DESC
 
-- 7. Вывод всех заказов и их под заказов
    SELECT Orders.OrderNumber, Status, OrderContentNumber, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.FIO as FIOManager, Staff.Phone as PhoneManager, VendorCode, Quantity, Price  FROM Orders
        JOIN Staff ON Orders.IdManager = Staff.IdStaff 
        JOIN OrderContent ON Orders.OrderNumber = OrderContent.OrderNumber
--
-- АНАЛИЗ
-- 
-- 1. Вывод кол-ва продаж товара за период
    SELECT SUM(Quantity) AS SumQuantity FROM OrderContent 
        JOIN Orders ON Orders.OrderNumber = OrderContent.OrderNumber 
            WHERE OrderContent.VendorCode = 112481 AND
                Orders.[Date] >= '2022-07-24' AND 
                Orders.[Date] <= '2022-08-25'

-- 2. Вывод кол-ва закупок товара за период
    SELECT SUM(Quantity) AS SumQuantity FROM InvoiceContent 
        JOIN Invoice ON Invoice.InvoiceNumber = InvoiceContent.InvoiceNumber 
            WHERE InvoiceContent.VendorCode = 112481 AND
                Invoice.[Date] >= '2022-07-24' AND 
                Invoice.[Date] <= '2022-08-25'

-- 3. Вывод суммы закупок товара за период
    SELECT SUM(SumPurchaseOneItem) FROM ( 
        SELECT (Purchase)*(Quantity) AS SumPurchaseOneItem FROM InvoiceContent 
            JOIN Invoice ON Invoice.InvoiceNumber = InvoiceContent.InvoiceNumber 
                WHERE InvoiceContent.VendorCode = 112481 AND
                    Invoice.[Date] >= '2022-07-24' AND 
                    Invoice.[Date] <= '2022-08-25' ) as PurchaseQuantity

-- 4. Вывод суммы продаж товара за период
    SELECT SUM(SumPriceOneItem) FROM ( 
        SELECT (Price)*(Quantity) AS SumPriceOneItem FROM OrderContent 
            JOIN Orders ON Orders.OrderNumber = OrderContent.OrderNumber 
                WHERE OrderContent.VendorCode = 112481 AND
                    Orders.[Date] >= '2022-07-24' AND 
                    Orders.[Date] <= '2022-08-25' ) as PriceQuantity

-- 5. Вывод кол-ва оформленных заказов и сумму заказов для каждого менеджера продаж за период времени, отсортированный по возрастанию по сумме заказов 
    SELECT Staff.Phone as PhoneManager, Staff.FIO as FIOManager, SUM(TotalSum) as SumPrice, SUM(Quantity) as SumQuantity FROM Orders
        JOIN OrderContent ON Orders.OrderNumber = OrderContent.OrderNumber
        JOIN Staff ON Orders.IdManager = Staff.IdStaff
            GROUP BY Staff.Phone, Staff.FIO
            ORDER BY SumPrice ASC

-- 5. Вывод кол-ва оформленных заказов и сумму заказов для каждого менеджера продаж за период времени, отсортированный по убыванию по кол-ву заказов 
    SELECT Staff.Phone as PhoneManager, Staff.FIO as FIOManager, SUM(TotalSum) as SumPrice, SUM(Quantity) as SumQuantity FROM Orders
        JOIN OrderContent ON Orders.OrderNumber = OrderContent.OrderNumber
        JOIN Staff ON Orders.IdManager = Staff.IdStaff
            GROUP BY Staff.Phone, Staff.FIO
            ORDER BY SumPrice DESC

-- 5. Вывод кол-ва оформленных заказов и сумму заказов для каждого менеджера продаж  за период времени, отсортированный по возрастанию по сумме заказов 
    SELECT Staff.Phone as PhoneManager, Staff.FIO as FIOManager, SUM(TotalSum) as SumPrice, SUM(Quantity) as SumQuantity FROM Orders
        JOIN OrderContent ON Orders.OrderNumber = OrderContent.OrderNumber
        JOIN Staff ON Orders.IdManager = Staff.IdStaff
            GROUP BY Staff.Phone, Staff.FIO
            ORDER BY SumQuantity ASC

-- 5. Вывод кол-ва оформленных заказов и сумму заказов для каждого менеджера продаж  за период времени, отсортированный по убыванию по кол-ву заказов 
    SELECT Staff.Phone as PhoneManager, Staff.FIO as FIOManager, SUM(TotalSum) as SumPrice, SUM(Quantity) as SumQuantity FROM Orders
        JOIN OrderContent ON Orders.OrderNumber = OrderContent.OrderNumber
        JOIN Staff ON Orders.IdManager = Staff.IdStaff
            GROUP BY Staff.Phone, Staff.FIO
            ORDER BY SumQuantity DESC

-- 6. Вывод кол-ва оформленных накладных и суммы закупок для каждого менеджера закупок за период времени, отсортированный по возрастанию по сумме закупок
    SELECT Staff.Phone as PhoneManager, Staff.FIO as FIOManager, SUM(TotalSum) as SumPurchase, SUM(Quantity) as SumQuantity FROM Invoice
        JOIN InvoiceContent ON InvoiceContent.InvoiceNumber = Invoice.InvoiceNumber
        JOIN Staff ON Invoice.IdManger=Staff.IdStaff
        GROUP BY Staff.Phone, Staff.FIO
        ORDER BY SumPurchase ASC

-- 6. Вывод кол-ва оформленных накладных и суммы закупок для каждого менеджера закупок за период времени, отсортированный по убыванию по сумме закупок
    SELECT Staff.Phone as PhoneManager, Staff.FIO as FIOManager, SUM(TotalSum) as SumPurchase, SUM(Quantity) as SumQuantity FROM Invoice
        JOIN InvoiceContent ON InvoiceContent.InvoiceNumber = Invoice.InvoiceNumber
        JOIN Staff ON Invoice.IdManger=Staff.IdStaff
        GROUP BY Staff.Phone, Staff.FIO
        ORDER BY SumPurchase DESC

-- 6. Вывод кол-ва оформленных накладных и суммы закупок для каждого менеджера закупок за период времени, отсортированный по возрастанию по кол-ву закупок
    SELECT Staff.Phone as PhoneManager, Staff.FIO as FIOManager, SUM(TotalSum) as SumPurchase, SUM(Quantity) as SumQuantity FROM Invoice
        JOIN InvoiceContent ON InvoiceContent.InvoiceNumber = Invoice.InvoiceNumber
        JOIN Staff ON Invoice.IdManger=Staff.IdStaff
        GROUP BY Staff.Phone, Staff.FIO
        ORDER BY SumQuantity ASC

-- 6. Вывод кол-ва оформленных накладных и суммы закупок для каждого менеджера закупок за период времени, отсортированный по убыванию по кол-ву закупок
    SELECT Staff.Phone as PhoneManager, Staff.FIO as FIOManager, SUM(TotalSum) as SumPurchase, SUM(Quantity) as SumQuantity FROM Invoice
        JOIN InvoiceContent ON InvoiceContent.InvoiceNumber = Invoice.InvoiceNumber
        JOIN Staff ON Invoice.IdManger=Staff.IdStaff
        GROUP BY Staff.Phone, Staff.FIO
        ORDER BY SumQuantity DESC
--
-- COMMON запросы 
--
-- 1. Вывод информации о конкретной накладной и ее закупках
     SELECT Invoice.InvoiceNumber, InvoiceContentNumber, [Date], TotalSum, Seller.Name as NameSeller, Seller.Phone as PhoneSeller, TaxPayerId, Staff.FIO as FIOManager, Staff.Phone as PhoneManager, InvoiceContent.VendorCode, InvoiceContent.Quantity, InvoiceContent.Purchase  FROM Invoice
        JOIN Staff ON Invoice.IdManger = Staff.IdStaff 
        JOIN Seller ON Invoice.IdSeller = Seller.IdSeller
        JOIN InvoiceContent ON Invoice.InvoiceNumber = InvoiceContent.InvoiceNumber
            WHERE (InvoiceContent.InvoiceNumber=781102)

-- 2. Вывод накладных и её закупках, оформленных за период, отсортированных по дате оформление по возрастанию
     SELECT Invoice.InvoiceNumber, InvoiceContentNumber, [Date], TotalSum, Seller.Name as NameSeller, Seller.Phone as PhoneSeller, TaxPayerId, Staff.FIO as FIOManager, Staff.Phone as PhoneManager, InvoiceContent.VendorCode, InvoiceContent.Quantity, InvoiceContent.Purchase  FROM Invoice
        JOIN Staff ON Invoice.IdManger = Staff.IdStaff 
        JOIN Seller ON Invoice.IdSeller = Seller.IdSeller
        JOIN InvoiceContent ON Invoice.InvoiceNumber = InvoiceContent.InvoiceNumber
            WHERE Invoice.[Date] >= '2022-07-24' AND Invoice.[Date] <= '2022-08-25'
            ORDER BY Invoice.[Date] ASC

-- 2. Вывод накладных и её закупках, оформленных за период, отсортированных по дате оформление по убыванию
     SELECT Invoice.InvoiceNumber, InvoiceContentNumber, [Date], TotalSum, Seller.Name as NameSeller, Seller.Phone as PhoneSeller, TaxPayerId, Staff.FIO as FIOManager, Staff.Phone as PhoneManager, InvoiceContent.VendorCode, InvoiceContent.Quantity, InvoiceContent.Purchase  FROM Invoice
        JOIN Staff ON Invoice.IdManger = Staff.IdStaff 
        JOIN Seller ON Invoice.IdSeller = Seller.IdSeller
        JOIN InvoiceContent ON Invoice.InvoiceNumber = InvoiceContent.InvoiceNumber
            WHERE Invoice.[Date] >= '2022-07-24' AND Invoice.[Date] <= '2022-08-25'
            ORDER BY Invoice.[Date] DESC

-- 3. Вывод информации о конкретном заказе и его под заказах
    SELECT OrderContentNumber, Status, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.FIO as FIOManager, Staff.Phone as PhoneManager, VendorCode, Quantity, Price  FROM Orders
        JOIN Staff ON Orders.IdManager = Staff.IdStaff 
        JOIN OrderContent ON Orders.OrderNumber = OrderContent.OrderNumber
            WHERE (OrderContent.OrderNumber=791103)

-- 4. Вывод заказов и его под заказов, оформленных за период, отсортированных по дате оформления по возрастанию
    SELECT Orders.OrderNumber, Status, OrderContentNumber, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.FIO as FIOManager, Staff.Phone as PhoneManager, VendorCode, Quantity, Price  FROM Orders
        JOIN Staff ON Orders.IdManager = Staff.IdStaff 
        JOIN OrderContent ON Orders.OrderNumber = OrderContent.OrderNumber
            WHERE Orders.[Date] >= '2022-07-24' AND Orders.[Date] <= '2022-08-25'
            ORDER BY Orders.[Date] ASC
        
-- 4. Вывод заказов и его под заказов, оформленных за период, отсортированных по дате оформления по убыванию
    SELECT Orders.OrderNumber, Status, OrderContentNumber, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.FIO as FIOManager, Staff.Phone as PhoneManager, VendorCode, Quantity, Price  FROM Orders
        JOIN Staff ON Orders.IdManager = Staff.IdStaff 
        JOIN OrderContent ON Orders.OrderNumber = OrderContent.OrderNumber
            WHERE Orders.[Date] >= '2022-07-24' AND Orders.[Date] <= '2022-08-25'
            ORDER BY Orders.[Date] DESC
--
-- Франкенштейн
--
-- Вывод всех заказов и его под заказов, имеющие конкретный статус заказа, отсортированных по дате оформления по возрастанию 
    SELECT Orders.OrderNumber, OrderContentNumber, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.FIO as FIOManager, Staff.Phone as PhoneManager, VendorCode, Quantity, Price  FROM Orders
        JOIN Staff ON Orders.IdManager = Staff.IdStaff 
        JOIN OrderContent ON Orders.OrderNumber = OrderContent.OrderNumber
            WHERE (OrderContent.Status=1)
            ORDER BY Orders.[Date] ASC
    
-- Вывод всех заказов и его под заказов, имеющие конкретный статус заказа, отсортированных по дате оформления по убыванию 
    SELECT Orders.OrderNumber, OrderContentNumber, [Date], TotalSum, PnoneCustomer, NameCustomer, Staff.FIO as FIOManager, Staff.Phone as PhoneManager, VendorCode, Quantity, Price  FROM Orders
        JOIN Staff ON Orders.IdManager = Staff.IdStaff 
        JOIN OrderContent ON Orders.OrderNumber = OrderContent.OrderNumber
            WHERE (OrderContent.Status=1)
            ORDER BY Orders.[Date] DESC
-- 
-- ДОП.
--  
-- 1. Вывод всех менеджеров закупок и оформленных ими-закупок
    SELECT Staff.FIO as FIOManager, Staff.Phone as PhoneManager, [Date], InvoiceContent.InvoiceContentNumber, InvoiceContent.VendorCode, InvoiceContent.Quantity, InvoiceContent.Purchase FROM Invoice
        JOIN Staff ON Invoice.IdManger = Staff.IdStaff 
        JOIN InvoiceContent ON Invoice.InvoiceNumber = InvoiceContent.InvoiceNumber

-- 2. Вывод всех менеджеров продаж и оформленных ими под-заказов
    SELECT Staff.FIO as FIOManager, Staff.Phone as PhoneManager, [Date], OrderContent.OrderContentNumber, OrderContent.VendorCode, OrderContent.Quantity, OrderContent.Price FROM Orders
        JOIN Staff ON Orders.IdManager = Staff.IdStaff 
        JOIN OrderContent ON Orders.OrderNumber = OrderContent.OrderNumber

-- ФУНКЦИИ 
GO

-- 1. Изменение кол-ва товара после покупки  
CREATE PROCEDURE UpdateGoodsAfterOrder(
                        @VendorCode INT, 
                        @NewAmount INT)
    AS UPDATE Goods SET Amount=@NewAmount
        WHERE (Goods.VendorCode=@VendorCode)
GO

-- 1. Добавление заказа
CREATE PROCEDURE AddOrder(@OrderNumber INT, 
                          @IdManager INT, 
                          @PhoneCustomer Varchar(13), 
                          @NameCustomer Varchar(100), 
                          @Date DATETIME, 
                          @TotalSum Money)
    AS 
        INSERT Orders VALUES(@OrderNumber, @IdManager, @PhoneCustomer, @NameCustomer, @Date, @TotalSum)
GO

-- 1. Добавление под-заказа
CREATE PROCEDURE AddSubOrder(@OrderContentNumber INT,
                             @OrderNumber INT,
                             @Status INT,
                             @VendorCode INT, 
                             @Quantity INT)
    AS 
    DECLARE @countAmountOrder INT
        SET @countAmountOrder = (SELECT Amount FROM Goods WHERE Goods.VendorCode=@VendorCode)
    DECLARE @Price Money

    IF (@countAmountOrder - @Quantity >=0)
        BEGIN
            SET @Price = (SELECT Price FROM Goods WHERE Goods.VendorCode=@VendorCode)
            INSERT OrderContent VALUES (@OrderContentNumber, @OrderNumber, @Status, @VendorCode, @Quantity, @Price)
            IF (@@ERROR = 0)
                BEGIN
                    SET @countAmountOrder = @countAmountOrder - @Quantity
                    EXEC UpdateGoodsAfterOrder @VendorCode, @countAmountOrder
                END
            ELSE 
                PRINT('U can not buy. INcorrect data')
        END
    ELSE
        PRINT('U can not buy more then have')

    DECLARE @TotalSum MONEY = 0.00
        EXEC SumOfSubOrders @OrderNumber, @TotalSum
        UPDATE Orders SET TotalSum=@TotalSum
            WHERE Orders.OrderNumber = @OrderNumber

GO

-- 2. Изменение цены закупки и кол-ва товара после закупки 
CREATE PROCEDURE UpdateGoodsAfterInvoice(
                        @VendorCode INT, 
                        @NewAmount INT, 
                        @NewPurchase Money)
    AS UPDATE Goods SET Amount=@NewAmount, Purchase=@NewPurchase
        WHERE (Goods.VendorCode=@VendorCode)
GO
-- 1. Подсичать сумму всего прихода 
CREATE PROCEDURE SumOfSubInvoices(
                        @InvoiceNumber INT,
                        @Sum Money OUT
                                      ) 
    AS 
    SELECT @Sum = SUM(Purchase) FROM InvoiceContent
        WHERE InvoiceContent.InvoiceNumber = @InvoiceNumber
GO

CREATE PROCEDURE SumOfSubOrders(
                        @OrderNumber INT,
                        @Sum Money OUT
                                      ) 
    AS 
    SELECT @Sum = SUM(Price) FROM OrderContent
        WHERE OrderContent.OrderNumber = @OrderNumber
GO


-- 2. Добавление накладной
CREATE PROCEDURE AddInvoice(@InvoiceNumber INT, 
                            @IdManager INT, 
                            @IdSeller INT, 
                            @Date DATETIME)
    AS 
        INSERT Invoice(InvoiceNumber, IdManger, IdSeller, [Date]) VALUES(@InvoiceNumber, @IdManager, @IdSeller, @Date)
GO

-- 2. Добавление под-закупки
CREATE PROCEDURE AddSubInvoice(@InvoiceContentNumber INT,
                               @InvoiceNumber INT,
                               @VendorCode INT, 
                               @Quantity INT,
                               @Purchase Money)
    AS
        DECLARE @countAmountInvoice INT
        SET @countAmountInvoice = (SELECT Amount FROM Goods WHERE Goods.VendorCode=@VendorCode)

        DECLARE @countPurchaseInvoice Money
        SET @countPurchaseInvoice = (SELECT Purchase FROM Goods WHERE Goods.VendorCode=@VendorCode)
        
        SET @countPurchaseInvoice = (@countPurchaseInvoice * @countAmountInvoice + @Quantity * @Purchase) / (@countAmountInvoice+@Quantity)
        SET @countAmountInvoice = (@countAmountInvoice+@Quantity)

        INSERT InvoiceContent(InvoiceContentNumber, InvoiceNumber, VendorCode, Quantity, Purchase) VALUES (@InvoiceContentNumber, @InvoiceNumber, @VendorCode, @Quantity, @Purchase)
   
        if (@@ERROR = 0)
            EXEC UpdateGoodsAfterInvoice @VendorCode, @countAmountInvoice, @countPurchaseInvoice
        
        DECLARE @TotalSum MONEY = 0.00
        EXEC SumOfSubInvoices @InvoiceNumber, @TotalSum
        UPDATE Invoice SET TotalSum=@TotalSum
            WHERE Invoice.InvoiceNumber = @InvoiceNumber
        
GO


GO


GO
