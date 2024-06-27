-- Active: 1719429800355@@mysql-boss-tarefaboss.a.aivencloud.com@11138
CREATE DATABASE
    atribuiBadge;

use atribuiBadge;


create table badge
(
  email_com varchar(70) not null,
  email_empr varchar(70) not null,
  id_badge int not null,
  dt_emissao date not null,
  dt_vencimento date null,
  imagem_b varchar(70) not null,
  constraint PK_badge primary key (email_com, email_empr, id_badge),
  constraint CH_badge check (dt_vencimento > dt_emissao)
);
