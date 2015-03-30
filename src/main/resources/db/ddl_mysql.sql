
    create table ACCOUNT (
        id bigint not null auto_increment,
        account_email varchar(255) not null,
        account_firstname varchar(255) not null,
        account_inscriptionDate datetime,
        account_activated bit not null,
        account_premium bit not null,
        account_lastConnectionDate datetime,
        account_lastname varchar(255) not null,
        account_password varchar(255) not null,
        account_expirationDate datetime,
        account_role varchar(255),
        account_screenname varchar(255) not null,
        primary key (id)
    );

    create table CODE (
        id bigint not null auto_increment,
        primary key (id)
    );

    create table PARTY (
        id bigint not null auto_increment,
        party_creationDate datetime,
        party_elo integer,
        party_language integer,
        party_name varchar(255),
        creator_id bigint,
        primary key (id)
    );

    create table PARTY_MEMBERS (
        account_id bigint not null,
        party_id bigint not null,
        primary key (account_id, party_id)
    );

    create table WEB_AGENT (
        id bigint not null auto_increment,
        primary key (id)
    );

    alter table ACCOUNT 
        add constraint UK_jkd9cix86ciqqcm6do9odr3h7  unique (account_email);

    alter table ACCOUNT 
        add constraint UK_f4lc6mbyr2oahgta5npnx74l  unique (account_screenname);

    alter table PARTY 
        add constraint UK_6f9crrkedyvu40ib31ca4s3w3  unique (party_name);

    alter table PARTY 
        add constraint FK_k1774sand3ceowkyfblvuhp2v 
        foreign key (creator_id) 
        references ACCOUNT (id);

    alter table PARTY_MEMBERS 
        add constraint FK_l63ligsxb1a58basb48ar416v 
        foreign key (party_id) 
        references PARTY (id);

    alter table PARTY_MEMBERS 
        add constraint FK_tr5mu73up68rceqibb98kuvjb 
        foreign key (account_id) 
        references ACCOUNT (id);
