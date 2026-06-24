CREATE TABLE public.pedido
(
    codigo serial NOT NULL,
    data date,
    codigo_cliente integer,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (codigo)
);

ALTER TABLE public.pedido
    ADD FOREIGN KEY (codigo_cliente)
        REFERENCES public.cliente (codigo)
    NOT VALID;

CREATE TABLE public.item_pedido
(
    codigo serial NOT NULL,
    quantidade integer,
    codigo_pedido integer,
    codigo_produto integer,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (codigo)
);

ALTER TABLE public.item_pedido
    ADD FOREIGN KEY (codigo_pedido)
        REFERENCES public.pedido (codigo)
    NOT VALID;

ALTER TABLE public.item_pedido
    ADD FOREIGN KEY (codigo_produto)
        REFERENCES public.produto (codigo)
    NOT VALID;