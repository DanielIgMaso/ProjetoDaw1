CREATE TABLE public.cliente
(
    codigo serial NOT NULL,
    nome text,
    cpf text,
    data_nascimento date,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (codigo)
);