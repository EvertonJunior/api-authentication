# Authentication-Service


Authentication-Service é uma aplicação backend para Serviço de gerenciamento de usuários que se conecta através de mensageria via RabbitMQ com microserviço de envio de emails.


## Sobre o projeto:

Em resumo, o usuário pode criar conta, se autenticar e recuperar senha. Ao criar a conta e recuperar senha o usuário recebe um email de boas vindas ou de recuperação, através do microserviço de envio de email. Foi configurado docker-compose para iniciar a aplicação em container docker.

## Funcionalidades:

- Conexão com banco de dados
- Inserção, remoção e consulta de dados.
- Envio de emails
- Criação de Conta: Os usuários podem se registrar e criar uma nova conta, recebendo um e-mail de boas-vindas.
- Autenticação: Os usuários podem se autenticar na aplicação.
- Recuperação de Senha: Se os usuários esquecerem sua senha, podem solicitar a recuperação.
  
Quando um usuário faz a solicitação através do endpoint de recuperação de senha,  é criado um token com validade de  30 minutos, esse token vai na URI , o link completo é enviado por e-mail  para criar uma nova senha. O envio de e-mails é gerenciado por um microserviço, que se comunica com a aplicação principal através do RabbitMQ para garantir uma comunicação eficiente e segura entre os serviços.

## Resiliência do Sistema:

As notificações geradas pelo RabbitMQ são armazenadas com um status de "enviada" ou "pendente". Para garantir que todas as notificações sejam entregues, implementei um método que é executado automaticamente a cada 1 minuto usando a anotação @Scheduled do spring. Esse método verifica o status de cada notificação e, se o status estiver pendente, reenvia a notificação.
Dessa forma, assegura-se a resiliência e a confiabilidade do sistema de notificações, garantindo que nenhuma mensagem seja perdida e que todas as comunicações com os usuários sejam realizadas de forma eficiente.
Essa abordagem modular com microserviços permite escalabilidade e manutenção simplificada, além de utilizar mensageria para garantir a entrega confiável das mensagens.

## Testes

Para garantir o funcionamento correto e a integridade da aplicação, foram implementados testes end-to-end (E2E). Esses testes cobrem todo o fluxo da aplicação, foram implementados para validar que todas as funcionalidades estão operando conforme o esperado.

## Deploy AWS

Inicialmente, criei um banco de dados utilizando o Amazon RDS. Em seguida, criei imagens Docker do authentication-service, email-service e também do RabbitMQ, configuradas para conectar-se a esse banco de dados e as disponibilizei no meu repositório no Docker Hub. Posteriormente, configurei uma instância EC2 na AWS, onde implantei a aplicação iniciando os containers a partir das imagens armazenadas no Docker Hub.

Imagem do EC2:

![EC2](https://github.com/EvertonJunior/api-authentication/blob/main/authentication-service/Captura%20de%20tela%20de%202024-11-29%2019-25-17.png)


Imagem do RDS:

![RDS](https://github.com/EvertonJunior/api-authentication/blob/main/authentication-service/Captura%20de%20tela%20de%202024-11-29%2019-25-46.png)


## Tecnologias utilizadas:

- Java
- Spring boot
- JPA / Hibernate
- MySql
- Spring Security e JWT token
- RabbitMQ
- Spring doc e Swagger
- Docker
- AWS

# Autor

Jose Everton Carlos da Silva Junior

[![LinkedIn](https://img.shields.io/badge/LinkedIn-blue?style=flat-square&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/devjoseeverton/)
