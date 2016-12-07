create table account (id binary(255) not null, credentials_expired bit not null, disabled bit not null, email varchar(255), expired bit not null, first varchar(255), last varchar(255), locked bit not null, password varchar(254) not null, password_reset_token varchar(255), payment_info varchar(255), plan varchar(255), trial_expiration_date datetime, primary key (id))
create table role (name varchar(255) not null, account binary(255) not null, primary key (name, account))
alter table role add constraint FKdeocu91gwlgxxjdiq8fu9kdtw foreign key (account) references account (id)
