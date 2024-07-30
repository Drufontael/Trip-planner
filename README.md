

# Trip-Planner

## Objetivo

Criar uma API em Java usando o framework Spring Boot para desafio final de projeto para Bootcamp Desenvolvimento em Java com IA da DIO.

## Descrição

Esta é uma API de planejamento de viagem, atividades durante a viagem e controle de gastos.

## Diagrama de Classes
```mermaid
classDiagram
    class Trip {
        Long id
        String name
        LocalDate startDate
        LocalDate endDate
        TripCategory category
        User user
        List~Task~ tasks
    }

    class Task {
        Long id
        String description
        boolean completed
        LocalDate date
        Trip trip
        User user
        List~Expense~ expenses
    }

    class Expense {
        Long id
        BigDecimal estimatedCost
        BigDecimal actualCost
        ExpenseCategory expenseCategory
        Task task
        User user
    }
    class User {
        Long id
        String username
        String password
        List~Trip~ trips
    }

class TripCategory {
    <<enumeration>>
BUSINESS
TOURISM
VISIT
}

class ExpenseCategory {
    <<enumeration>>
ACCOMMODATION
TRANSPORTATION
FOOD
TICKETS
SOUVENIRS
    }
User "1"*--"0--*"Trip 
Trip "1"*-- "1" TripCategory
Trip "1"*-- "0..*" Task
Task "1"*-- "0..*" Expense
Expense "1"*-- "1" ExpenseCategory
```
## Estrutura do Projeto
```
├───src
│   ├───main
│   │   ├───java
│   │   │   └───br
│   │   │       └───dev
│   │   │           └───drufontael
│   │   │               └───Trip_Planer
│   │   │                   ├───configuration
│   │   │                   ├───controller
│   │   │                   ├───dto
│   │   │                   ├───exception
│   │   │                   ├───model
│   │   │                   │   └───enuns
│   │   │                   ├───repository
│   │   │                   ├───service
│   │   │                   └───utils
```


## Tecnologias Utilizadas
- **Java**
- **Spring Boot**
- **ChatGPT**
- **Mermaid**
- **Springdoc Swagger**
- **Railway**

## Endpoints

### Auth Endpoints
- **Registrar Usuário:** `POST /api/register`
- **Login:** `POST /api/login`
- **Obter Sessão:** `GET /api/session`

### Trip Endpoints
- **Criar Trip:** `POST /trips`
- **Visualizar todas as Trips:** `GET /trips`
- **Visualizar Trip por ID:** `GET /trips/{id}`
- **Atualizar Trip por ID:** `PUT /trips/{id}`
- **Deletar Trip por ID:** `DELETE /trips/{id}`

### Task Endpoints
- **Criar Task:** `POST /trips/{tripId}/tasks`
- **Visualizar todas as Tasks por Trip:** `GET /trips/{tripId}/tasks`
- **Visualizar Task por ID:** `GET /trips/{tripId}/tasks/{taskId}`
- **Atualizar Task por ID:** `PUT /trips/{tripId}/tasks/{taskId}`
- **Deletar Task por ID:** `DELETE /trips/{tripId}/tasks/{taskId}`

### Expense Endpoints
- **Criar Expense:** `POST /trips/{tripId}/tasks/{taskId}/expenses`
- **Visualizar todas as Expenses por Task:** `GET /trips/{tripId}/tasks/{taskId}/expenses`
- **Visualizar Expense por ID:** `GET /trips/{tripId}/tasks/{taskId}/expenses/{expenseId}`
- **Atualizar Expense por ID:** `PUT /trips/{tripId}/tasks/{taskId}/expenses/{expenseId}`
- **Deletar Expense por ID:** `DELETE /trips/{tripId}/tasks/{taskId}/expenses/{expenseId}`

## Instruções para Uso

Para usar a API, é necessário criar um usuário e fazer login para criar uma sessão. Com uma sessão ativa, o usuário poderá acessar as funcionalidades da API. Siga os passos abaixo:
1. **Registrar Ususario:**
- Endpoint: `POST /api/register`
- Corpo da Requisição:
```json
{
  "username": "seu_username",
  "password": "sua_password"
}
```
2. **Login:**
- Endpoint: `POST /api/login`
- Corpo da Requisição:
```json
{
  "username": "seu_username",
  "password": "sua_password"
}
```
- Resposta:
    - Status 200: Login bem-sucedido
    - Status 400: Username ou password invalidos

    


## Link do Deploy
[Trip Planner - Railway](https://trip-planner-production-b484.up.railway.app/swagger-ui.html)

## Alterações Recentes
1. Criada a entidade "user" referente no banco de dados a "tb_user".
2. Criado controle de registro e de login.
3. Restringindo o acesso das trips aos usuários donos.
4. Versão alterada de 0.0.1 para 1.0.0.
