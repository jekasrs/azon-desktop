Create DATABASE Company;
GO

Use Company;
GO

-- РОЛИ СОТРУДНИКОВ
CREATE TABLE Role(
    IdRole INT PRIMARY KEY IDENTITY(1,1),
    Name VARCHAR(20) NOT NULL UNIQUE
);
GO

-- ЛОГИН и ПАРОЛЬ для учетки
CREATE TABLE LoginPassword(
    UserName Varchar(20) PRIMARY KEY,
    Password VARCHAR(64) NOT NULL, 
    IsValidAccount BIT NOT NULL DEFAULT(1)
);
GO

-- СОТРУДНИКИ 
CREATE TABLE Staff(
    IdStaff INT IDENTITY(1,1) PRIMARY KEY,
    UserName Varchar(20) NOT NULL UNIQUE, 
    IdRole INT NOT NULL,
    FIO VARCHAR(100) NOT NULL,
    DateOfBirth DATE NOT NULL,
    Phone VARCHAR(13) NOT NULL UNIQUE,
    FOREIGN KEY (IdRole) REFERENCES Role(IdRole),
    FOREIGN KEY (UserName) REFERENCES LoginPassword(UserName) ON UPDATE CASCADE
);
GO

-- ПОСТАВЩИКИ  
CREATE TABLE Seller(
    IdSeller INT IDENTITY(1,1) PRIMARY KEY, 
    Name VARCHAR(100) NOT NULL,
    TaxPayerId INT NOT NULL UNIQUE,
    Phone VARCHAR(13) NOT NULL UNIQUE,
);
GO

-- ТОВАРЫ  
CREATE TABLE Goods (
    VendorCode INT PRIMARY KEY,
    GoodsTitle VARCHAR(100) NOT NULL,
    Description VARCHAR(MAX) NOT NULL,
    Purchase Money NOT NULL CHECK(Purchase >= 0) DEFAULT(0), 
    Price Money NOT NULL CHECK(Price >= 0),
    Amount INT NOT NULL CHECK(Amount >=0) DEFAULT(0)
);
GO

-- НАКЛАДНЫЕ ЗАКУПОК 
CREATE TABLE Invoice(
    InvoiceNumber INT PRIMARY KEY,
    IdManger INT NOT NULL,
    IdSeller INT NOT NULL,
    Date DATETIME NOT NULL, 
    TotalSum Money DEFAULT(0.00), 
    FOREIGN KEY (IdManger) REFERENCES Staff(IdStaff),
    FOREIGN KEY (IdSeller) REFERENCES Seller(IdSeller)
);
GO

-- ПОД-ЗАКУПКА
CREATE TABLE InvoiceContent(
    InvoiceContentNumber INT NOT NULL,
    InvoiceNumber INT NOT NULL,
    VendorCode INT NOT NULL,
    Quantity INT NOT NULL CHECK (Quantity > 0),
    Purchase Money NOT NULL CHECK(Purchase >= 0),
    FOREIGN KEY (InvoiceNumber) REFERENCES Invoice (InvoiceNumber),
    FOREIGN KEY (VendorCode) REFERENCES Goods (VendorCode) ON UPDATE CASCADE
);
GO

-- СТАТУСЫ ЗАКАЗОВ 
CREATE TABLE StatusOrder(
    IdStatus INT PRIMARY KEY IDENTITY(1,1),
    Title VARCHAR(13) UNIQUE NOT NULL
);
GO

-- НАКЛАДНЫЕ ПРОДАЖ 
CREATE TABLE Orders (
    OrderNumber INT PRIMARY KEY, 
    IdManager INT NOT NULL,
    PnoneCustomer VARCHAR(13) NOT NULL,
    NameCustomer VARCHAR(100) NOT NULL,
    Date DATETIME NOT NULL,
    TotalSum Money DEFAULT(0.00), 
    FOREIGN KEY (IdManager) REFERENCES Staff (IdStaff),
);
GO

-- ПОД-ЗАКАЗ
CREATE TABLE OrderContent(
    OrderContentNumber INT NOT NULL,
    OrderNumber INT NOT NULL,
    Status INT NOT NULL,
    VendorCode INT NOT NULL,
    Quantity INT NOT NULL CHECK (Quantity > 0),
    Price Money NOT NULL CHECK(Price > 0),
    FOREIGN KEY (Status) REFERENCES StatusOrder (IdStatus),
    FOREIGN KEY (OrderNumber) REFERENCES Orders (OrderNumber),
    FOREIGN KEY (VendorCode) REFERENCES Goods(VendorCode) ON UPDATE CASCADE
);
GO



SELECT * FROM Staff;
SELECT * FROM LoginPassword;










-- КОНЕЦ
DROP DATABASE Company;

DROP TABLE InvoiceContent;
DROP TABLE Invoice;
DROP TABLE Seller;

DROP TABLE OrderContent;
DROP TABLE Orders;

DROP TABLE StatusOrder;
DROP TABLE Goods;

DROP TABLE Staff;
DROP TABLE Role;
DROP TABLE LoginPassword;

DROP PROCEDURE registerManager;
DROP PROCEDURE UpdateGoodsAfterInvoice;
DROP PROCEDURE AddInvoice;
DROP PROCEDURE AddSubInvoice;
DROP PROCEDURE AddSubOrder;
DROP PROCEDURE SumOfSubInvoices;
DROP PROCEDURE SumOfSubOrders;
DROP PROCEDURE GetAvailiableGoods;
DROP PROCEDURE ChangeStatus;
DROP PROCEDURE AddOrder;
DROP PROCEDURE AddSubOrder;
DROP PROCEDURE UpdateGoodsAfterOrder;
DROP VIEW Cataloge;
DROP VIEW OrderAndContent;
DROP VIEW MangerAndSubOrder;
DROP VIEW InvoiceAndContent;
DROP VIEW ManagerAndSubInvoice;
DROP VIEW Seller_VIEW;