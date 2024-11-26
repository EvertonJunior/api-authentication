insert into tb_usuarios (id, username, password, role) values (100, 'ana@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_ADMIN');
insert into tb_usuarios (id, username, password, role) values (101, 'maria@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_USER');
insert into tb_usuarios (id, username, password, role) values (102, 'joaquina@email.com', '$2a$12$VQSVztr/M8tcwzcy9jJgFebpqybKpF4FVwM3zilhOAI4yL1iW3rJa', 'ROLE_USER');

insert into reset_password_token (id, expire_date, status, token, username, usuario_id) values (20, '2025-11-25 15:17:27.406442', 'VALIDO', '1dc82400-470a-42cd-a1f2-47cb8e9d041d', 'maria@email.com', 101);

