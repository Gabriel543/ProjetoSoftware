use banco;

DROP TABLE IF EXISTS ingrediente;

CREATE TABLE ingrediente(
	id int not null auto_increment,
    nome varchar(155) not null,
    descricao varchar (155),
    versao int default 0,
    primary key(id)
);

insert INTO ingrediente(nome) value('carne');