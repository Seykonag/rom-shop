create sequence bucket_seq start with 1 increment by 1;
create sequence category_seq start with 1 increment by 1;
create sequence comment_seq start with 1 increment by 1;
create sequence mark_seq start with 1 increment by 1;
create sequence order_details_seq start with 1 increment by 1;
create sequence order_seq start with 1 increment by 1;
create sequence paid_order_seq start with 1 increment by 1;
create sequence product_seq start with 1 increment by 1;
create sequence sale_seq start with 1 increment by 1;
create sequence user_seq start with 1 increment by 1;
create table bonus_scores (sum numeric(38,2), id bigint not null, user_id bigint unique, primary key (id));
create table buckets (id bigint not null, user_id bigint unique, primary key (id));
create table buckets_products (bucket_id bigint not null, product_id bigint not null);
create table categories (id bigint not null, sale_id bigint unique, title varchar(255), primary key (id));
create table category_details (category_id bigint not null, detail_name varchar(255) not null, detail_value varchar(255), primary key (category_id, detail_name));
create table comments (rating integer, status boolean not null, data_comment timestamp(6), id bigint not null, user_id bigint, answer_admin varchar(255), text varchar(255), primary key (id));
create table marks (id bigint not null, user_id bigint unique, primary key (id));
create table marks_products (mark_id bigint not null, product_id bigint not null);
create table orders (sum numeric(38,2), created timestamp(6), id bigint not null, updated timestamp(6), user_id bigint, address varchar(255), status varchar(255) check (status in ('NEW','APPROVED','CANCELED','PAID','CLOSED')), primary key (id));
create table orders_details (amount numeric(38,2), price numeric(38,2), id bigint not null, order_id bigint, product_id bigint, primary key (id));
create table orders_details_component (details_id bigint not null unique, order_id bigint not null);
create table paid_orders (created timestamp(6), id bigint not null, order_id bigint unique, updated timestamp(6), currency varchar(255), email varchar(255), first_name varchar(255), href varchar(255), last_name varchar(255), payer_id varchar(255), paymentid varchar(255), total varchar(255), transaction_id varchar(255), primary key (id));
create table products (price numeric(38,2) not null, sale_price numeric(38,2), stock boolean not null, id bigint not null, developer varchar(255) not null, model varchar(255) not null, title varchar(255) not null, photo oid, primary key (id));
create table products_categories (category_id bigint, product_id bigint not null, primary key (product_id));
create table products_commentaries (comment_id bigint not null unique, product_id bigint not null);
create table sales (sale integer not null, category_id bigint unique, created timestamp(6), ended timestamp(6), id bigint not null, primary key (id));
create table users (archive boolean not null, newsletter boolean not null, bonus_score_id bigint unique, bucket_id bigint unique, id bigint not null, mark_id bigint unique, address varchar(255) not null, city varchar(255) not null, company varchar(255), country varchar(255) not null check (country in ('BELARUS','GEORGIA','KAZAKHSTAN','KYRGYZSTAN','RUSSIA','UZBEKISTAN','UKRAINE')), email varchar(255) not null, fax varchar(255), first_name varchar(255) not null, index varchar(255), last_name varchar(255) not null, password varchar(255) not null, phone varchar(255) not null, region varchar(255) not null check (region in ('BREST','VITEBSK','GOMEL','GRODNO','MINSK','MINSKIY','MOGILEV','ABKHAZIA','AJARIA','GURIA','IMERETI','KAKHETI','KVEMOKARTLI','MTSKHETAMTIANETI','RACHALECHKUMIANDKVEMOSVANET','SAMEGRELOZEMOSVANETI','SAMSKHEJAVAKHETI','SHIDAKARTLI','TBILISI','AKMOLINSKIY','AKTUBINSKIY','ALMATINSKIY','ALMATY','ASTANA','ATIRAYSKIY','BAIKONYR','ORIENTALKAZAKHSTAN','JAMBYLSKIY','WESTKAZAKHSTAN','KARAGANDINSKIY','KOSTANASKIY','KYZILORDINSKIY','MANGISTAYSKIY','PAVLODARSKIY','NORTHKAZAKHSTAN','SOUTHKAZAKHSTAN','BATKEN','BISHKEK','CHU','JALALABAD','NARYN','OSH','TALAS','YSYKKOL','BELGOROD','BRYANSK','VLADIMIR','VOLGOGRAD','VOLOGDA','VORONEZH','IVANOVO','IRKUTSK','KALININGRAD','KALUGA','KEMEROVO','KURGAN','KURSK','LENINGRAD','LIPETSK','MAGADAN','MOSCOW','MURMANSK','NIZHNYNOVGOROD','NOVGOROD','NOVOSIBIRSK','OMSK','ORENBURG','OREL','PENZA','PSKOV','ROSTOV','RYAZAN','SAMARA','SARATOV','SAKHALIN','SVERDLOVSK','SMOLYANINOVSKY','TAMBOV','TVER','TOMSK','TULA','TYUMEN','ULYANOVSK','CHELYABINSK','CHECHNYA','CHUVASHIA','CHUKOTKA','YAMALNENETS','YAROSLAVL','ANDIJON','BUXORO','FARGONA','JIZZAX','NAMANGAN','NAVOIY','QUASHDARIO','QORAQALPOGISTON','SAMARQAND','SIRDARYO','SURXONDARYO','TOSHKENT','TOSHENTSKIY','XORAZM','VINNYTSIA','VOLYN','DNIPROPETROVSK','DONETSK','ZHYTOMYR','ZAKARPATTIA','ZAPORIZHIA','IVANOFRANKIVSK','KYIVCITY','KYIV','KIROVOHRAD','LUGANSK','LVOV','MYKOLAIV','ODESSA','POLTAVA','RIVNE','SUMY','TERNOPIL','KHERSON','KHMELNYTSKY','CHERKASY','CHERNIVTSI','CHERNIHIV')), role varchar(255) not null check (role in ('ROLE_ADMIN','ROLE_USER','ROLE_MANAGER')), username varchar(255) not null, primary key (id));
alter table if exists bonus_scores add constraint FK3ih0cccf9f9hmjtkjo1yd447p foreign key (user_id) references users;
alter table if exists buckets add constraint FKnl0ltaj67xhydcrfbq8401nvj foreign key (user_id) references users;
alter table if exists buckets_products add constraint FKloyxdc1uy11tayedf3dpu9lci foreign key (product_id) references products;
alter table if exists buckets_products add constraint FKc49ah45o66gy2f2f4c3os3149 foreign key (bucket_id) references buckets;
alter table if exists categories add constraint FK9ud69xklq8ikpn3w36x39o3sp foreign key (sale_id) references sales;
alter table if exists category_details add constraint FKe2n95vc0r4eibipu7oyegj39q foreign key (category_id) references categories;
alter table if exists comments add constraint FK8omq0tc18jd43bu5tjh6jvraq foreign key (user_id) references users;
alter table if exists marks add constraint FKexm2itxq6fj3pll51arpieejd foreign key (user_id) references users;
alter table if exists marks_products add constraint FKt937o9fqj9outht9m3ecia3jd foreign key (product_id) references products;
alter table if exists marks_products add constraint FKp4gphjuhgwyai61t1ch6nbdyh foreign key (mark_id) references marks;
alter table if exists orders add constraint FK32ql8ubntj5uh44ph9659tiih foreign key (user_id) references users;
alter table if exists orders_details add constraint FK5o977kj2vptwo70fu7w7so9fe foreign key (order_id) references orders;
alter table if exists orders_details add constraint FKs0r9x49croribb4j6tah648gt foreign key (product_id) references products;
alter table if exists orders_details_component add constraint FKesvcivnjxra1j5vwi2heuwhuf foreign key (details_id) references orders_details;
alter table if exists orders_details_component add constraint FKbk4ettnahl6xb1fklrkpljwqi foreign key (order_id) references orders;
alter table if exists paid_orders add constraint FKsdc6fvdlarykvt4fcrmgychh4 foreign key (order_id) references orders;
alter table if exists products_categories add constraint FKqt6m2o5dly3luqcm00f5t4h2p foreign key (category_id) references categories;
alter table if exists products_categories add constraint FKtj1vdea8qwerbjqie4xldl1el foreign key (product_id) references products;
alter table if exists products_commentaries add constraint FKsk6mahahjlxy048swt2c0h1m1 foreign key (comment_id) references comments;
alter table if exists products_commentaries add constraint FKfv1a40e4rtmbn2x3vb201sd2h foreign key (product_id) references products;
alter table if exists sales add constraint FKr875v12qniy46upg1l2arm1vj foreign key (category_id) references categories;
alter table if exists users add constraint FKg79v6irhhdygys6j97sbsppol foreign key (bonus_score_id) references bonus_scores;
alter table if exists users add constraint FK8l2qc4c6gihjdyoch727guci foreign key (bucket_id) references buckets;
alter table if exists users add constraint FKk96l41h6amg2pbp1siiwg1ej8 foreign key (mark_id) references marks;