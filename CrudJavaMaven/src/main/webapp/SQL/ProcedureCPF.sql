USE master 
DROP DATABASE CrudCPF

CREATE DATABASE CrudCPF
GO 
USE CrudCPF

CREATE FUNCTION ValidarCPF(@CPF CHAR(11)) RETURNS CHAR(11) AS
BEGIN
    DECLARE @cpfValido VARCHAR(10) = 'Válido';
    DECLARE @primeiroDigito INT;
    DECLARE @segundoDigito INT;
    DECLARE @i INT;
    DECLARE @soma INT;
    DECLARE @resto INT;

    -- Verificação se o CPF contém apenas dígitos numéricos
    IF @CPF NOT LIKE '%[^0-9]%'
    BEGIN
        -- Cálculo do primeiro dígito verificador
        SET @soma = 0;
        SET @i = 10;
        WHILE @i >= 2
        BEGIN
            SET @soma = @soma + (CAST(SUBSTRING(@CPF, 11 - @i, 1) AS INT) * @i);
            SET @i = @i - 1;
        END;
        SET @resto = @soma % 11;
        SET @primeiroDigito = IIF(@resto < 2, 0, 11 - @resto);

        -- Cálculo do segundo dígito verificador
        SET @soma = 0;
        SET @i = 11;
        SET @CPF = @CPF + CAST(@primeiroDigito AS NVARCHAR(1));
        WHILE @i >= 2
        BEGIN
            SET @soma = @soma + (CAST(SUBSTRING(@cpf, 12 - @i, 1) AS INT) * @i);
            SET @i = @i - 1;
        END;
        SET @resto = @soma % 11;
        SET @segundoDigito = IIF(@resto < 2, 0, 11 - @resto);

        -- Verificação dos dígitos verificadores
        IF LEN(@CPF) <> 11 OR @CPF IN ('00000000000', '11111111111', '22222222222', '33333333333', '44444444444', '55555555555', '66666666666', '77777777777', '88888888888', '99999999999')
            OR SUBSTRING(@CPF, 10, 1) != CAST(@primeiroDigito AS NVARCHAR(1)) OR SUBSTRING(@CPF, 11, 1) != CAST(@segundoDigito AS NVARCHAR(1))
        BEGIN
            SET @cpfValido = 'Inválido';
        END;
    END
    ELSE
    BEGIN
        SET @cpfValido = 'Inválido';
    END;
    RETURN @cpfValido;
END;
GO


-- Tabela Cliente
CREATE TABLE Cliente (
    CPF CHAR(11),
    nome VARCHAR(100),
    email VARCHAR(200),
    limite_de_credito DECIMAL(7,2),
    dt_nascimento VARCHAR(10),
    PRIMARY KEY(CPF)
);
GO

CREATE PROCEDURE GerenciarCliente (
    @op VARCHAR(100),
    @CPF CHAR(11),
    @nome VARCHAR(100),
    @email VARCHAR(200),
    @limite_de_credito DECIMAL(7,2),
    @dt_nascimento Varchar(10),
    @saida VARCHAR(100) OUTPUT
)
AS
BEGIN
    IF @op = 'I'
    BEGIN
        IF dbo.ValidarCPF(@CPF) = 'Válido'
        BEGIN
            INSERT INTO Cliente (CPF, nome, email, limite_de_credito, dt_nascimento) VALUES (@CPF, @nome, @email, @limite_de_credito, @dt_nascimento);
            SET @saida = 'Cliente cadastrado com sucesso.';
        END
        ELSE
        BEGIN
           SET @saida = 'CPF inválido. Cadastro não realizado.';
        END
    END
    ELSE IF @op = 'U'
    BEGIN
        IF EXISTS (SELECT 1 FROM Cliente WHERE CPF = @CPF)
        BEGIN
            IF dbo.ValidarCPF(@CPF) = 'Válido'
            BEGIN
                UPDATE Cliente SET nome = @nome, email = @email, limite_de_credito = @limite_de_credito, dt_nascimento = @dt_nascimento
                WHERE CPF = @CPF;
                SET @saida = 'Cliente atualizado com sucesso.';
            END
            ELSE
            BEGIN
               SET @saida = 'CPF inválido. Atualização não realizada.';
            END
        END
        ELSE
        BEGIN
            SET @saida ='CPF não existe na base de dados.';
        END
    END
    ELSE IF @op = 'D'
    BEGIN
        IF EXISTS (SELECT 1 FROM Cliente WHERE CPF = @CPF)
        BEGIN
            DELETE FROM Cliente WHERE CPF = @CPF;
            SET @saida = 'Cliente deletado com sucesso.';
        END
        ELSE
        BEGIN
            SET @saida = 'CPF não existe na base de dados.';
        END
    END
    ELSE
    BEGIN
        SET @saida = 'Operação inválida.';
    END
END;
GO

SELECT dbo.ValidarCPF('99763168872') AS Resultado;


DECLARE @saida VARCHAR(100);

EXEC GerenciarCliente 'I', '12345678909', 'Nome do Cliente', 'email@cliente.com', 1000.00, '2000-01-01', @saida OUTPUT;
PRINT @saida;


SELECT * FROM Cliente