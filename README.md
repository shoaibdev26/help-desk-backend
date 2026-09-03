# 🤖 AI Help Desk — Spring AI Tool Calling

An **AI-powered Help Desk backend** built with **Java, Spring Boot, and Spring AI**. The system allows users to interact with an AI assistant using natural language to perform help-desk operations such as creating tickets, checking ticket status, searching tickets, updating ticket information, and sending email notifications.

The project demonstrates how **Spring AI Tool Calling** can connect an LLM with application services and business operations.

> **Note:** This project focuses on **Spring AI Tool Calling** and does **not use RAG or pgvector**. The AI interacts with the application through explicitly defined tools.

---

## 🚀 Key Features

* 🤖 AI-powered Help Desk assistant
* 🔧 Spring AI Tool Calling
* 🎫 Create and manage support tickets
* 🔍 Search and retrieve tickets
* 📊 Ticket status and priority management
* 📧 Email notification tool
* 🧠 Natural-language interaction with the Help Desk
* 🗄️ Persistent ticket storage using Spring Data JPA
* 🔐 Centralized AI configuration
* 🧩 Clean Controller → Service → Repository architecture
* 📦 Extensible tool-based architecture

---

# 🏗️ Architecture

```text
                         ┌───────────────────────┐
                         │       User / Client   │
                         └───────────┬───────────┘
                                     │
                                     │ Natural Language
                                     ▼
                         ┌───────────────────────┐
                         │     AiController      │
                         └───────────┬───────────┘
                                     │
                                     ▼
                         ┌───────────────────────┐
                         │      AIService        │
                         │                       │
                         │   Spring AI + LLM     │
                         └───────────┬───────────┘
                                     │
                              Tool Calling
                                     │
                    ┌────────────────┴────────────────┐
                    │                                 │
                    ▼                                 ▼
        ┌────────────────────────┐       ┌──────────────────────┐
        │ TicketDatabaseTool     │       │      EmailTool       │
        │                        │       │                      │
        │ Ticket Operations      │       │ Email Notifications  │
        └───────────┬────────────┘       └──────────────────────┘
                    │
                    ▼
        ┌────────────────────────┐
        │     TicketService      │
        │                        │
        │ Business Logic         │
        └───────────┬────────────┘
                    │
                    ▼
        ┌────────────────────────┐
        │    TicketRepository    │
        │                        │
        │    Spring Data JPA     │
        └───────────┬────────────┘
                    │
                    ▼
        ┌────────────────────────┐
        │       Database         │
        └────────────────────────┘
```

---

# 🔄 AI Tool Calling Flow

The application follows an action-oriented AI workflow.

```text
User
  │
  │ "Create a high priority ticket
  │  for login failure"
  ▼
AiController
  │
  ▼
AIService
  │
  ▼
Spring AI
  │
  │ LLM determines required action
  ▼
Tool Selection
  │
  ▼
TicketDatabaseTool
  │
  ▼
TicketService
  │
  ▼
TicketRepository
  │
  ▼
Database
  │
  ▼
Tool Result
  │
  ▼
Spring AI / LLM
  │
  ▼
Natural Language Response
  │
  ▼
User
```

The important concept is that the LLM does **not directly access the database**.

Instead:

```text
LLM
 ↓
Tool
 ↓
Service
 ↓
Repository
 ↓
Database
```

This keeps business logic inside the application rather than allowing the model to directly execute database operations.

---

# 🧠 Why Spring AI Tool Calling?

Traditional chatbot systems generally generate text responses.

This application goes one step further.

The AI can determine when an application operation is required and invoke an appropriate tool.

For example:

### User

```text
Create a ticket because I cannot login to my account.
```

### AI

The model determines that a ticket needs to be created and invokes:

```text
TicketDatabaseTool
        ↓
TicketService
        ↓
TicketRepository
```

The application performs the actual operation and returns the result to the AI.

The AI can then generate a user-friendly response:

```text
Your support ticket has been created successfully.
Ticket ID: 1024
Priority: HIGH
Status: OPEN
```

---

# 🛠️ Available AI Tools

## 🎫 TicketDatabaseTool

The `TicketDatabaseTool` provides the AI with controlled access to Help Desk ticket operations.

Typical operations include:

```text
Create Ticket
Get Ticket
Search Tickets
Update Ticket
Update Ticket Status
Assign Ticket
Get Open Tickets
```

Example user requests:

```text
Create a ticket for payment failure.
```

```text
Show me the status of ticket 1005.
```

```text
Find all open high-priority tickets.
```

```text
Update ticket 1005 to RESOLVED.
```

The AI determines which tool operation is appropriate.

---

# 📧 EmailTool

`EmailTool` provides email-related functionality to the AI assistant.

Example:

```text
Send an email notification to the customer
that ticket #1005 has been resolved.
```

The AI can invoke the email tool when an email notification is required.

```text
AI
 │
 ▼
EmailTool
 │
 ▼
Email Service
 │
 ▼
Customer Email
```

---

# 📦 Project Structure

```text
src
└── main
    ├── java
    │   └── com.irusol.helpdesk
    │
    │       ├── config
    │       │   └── AiConfig
    │       │
    │       ├── controller
    │       │   └── AiController
    │       │
    │       ├── entity
    │       │   ├── Priority
    │       │   ├── Status
    │       │   └── Ticket
    │       │
    │       ├── repository
    │       │   └── TicketRepository
    │       │
    │       ├── service
    │       │   ├── AIService
    │       │   └── TicketService
    │       │
    │       ├── tools
    │       │   ├── EmailTool
    │       │   └── TicketDatabaseTool
    │       │
    │       └── HelpDeskBackendApplication
    │
    └── resources
        ├── application.yml
        └── helpdesk-main.st
```

---

# 📁 Package Responsibilities

| Package      | Responsibility                               |
| ------------ | -------------------------------------------- |
| `config`     | Spring AI and application configuration      |
| `controller` | REST API endpoints                           |
| `entity`     | JPA entities and enums                       |
| `repository` | Database access                              |
| `service`    | Business logic and AI orchestration          |
| `tools`      | Spring AI tools exposed to the LLM           |
| `resources`  | Application configuration and system prompts |

---

# 🎫 Ticket Domain Model

The Help Desk contains a `Ticket` entity.

A ticket can contain information such as:

```text
Ticket
 ├── id
 ├── title
 ├── description
 ├── priority
 ├── status
 ├── customer information
 └── timestamps
```

### Priority

```text
LOW
MEDIUM
HIGH
CRITICAL
```

### Status

```text
OPEN
IN_PROGRESS
RESOLVED
CLOSED
```

> Adjust the enum values above if your implementation uses different values.

---

# 🌐 API

## AI Chat Endpoint

The main interaction point is exposed through `AiController`.

Example:

```http
POST /ai/chat
```

Request:

```json
{
  "message": "Create a high priority ticket for login failure"
}
```

Response:

```json
{
  "response": "Your high priority ticket has been created successfully."
}
```

> Update the endpoint and JSON structure above if your `AiController` uses a different mapping.

---

# 💬 Example AI Conversations

### Create Ticket

**User**

```text
I cannot login to my account. Create a high priority ticket.
```

**AI**

```text
Your ticket has been created successfully.

Priority: HIGH
Status: OPEN
```

---

### Get Ticket

**User**

```text
What is the status of ticket 1001?
```

**AI**

```text
Ticket 1001 is currently IN_PROGRESS.
```

---

### Search Tickets

**User**

```text
Show me all open high-priority tickets.
```

**AI**

```text
I found 3 open high-priority tickets.
```

---

### Update Ticket

**User**

```text
Mark ticket 1001 as resolved.
```

**AI**

```text
Ticket 1001 has been marked as RESOLVED.
```

---

### Email Notification

**User**

```text
Send an email to the customer informing them
that ticket 1001 has been resolved.
```

The AI can invoke:

```text
EmailTool
```

to perform the email operation.

---

# 🧩 Technology Stack

| Technology                 | Purpose                       |
| -------------------------- | ----------------------------- |
| Java                       | Programming Language          |
| Spring Boot                | Backend Framework             |
| Spring AI                  | AI/LLM Integration            |
| Spring AI Tool Calling     | AI → Application Actions      |
| Spring Web                 | REST APIs                     |
| Spring Data JPA            | Persistence                   |
| Hibernate                  | ORM                           |
| PostgreSQL / Relational DB | Ticket Storage                |
| Maven                      | Build & Dependency Management |
| Lombok                     | Boilerplate Reduction         |
| YAML                       | Application Configuration     |

---

# 📚 Maven Dependencies

The project uses Spring Boot and Spring AI dependencies.

Typical dependencies include:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-openai</artifactId>
</dependency>
```

Additional dependencies can be added depending on the database and email implementation.

---

# ⚙️ Configuration

Configure your LLM provider and database in:

```text
src/main/resources/application.yml
```

Example:

```yaml
spring:
  application:
    name: helpdesk-backend

  datasource:
    url: jdbc:postgresql://localhost:5432/helpdesk
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

  jpa:
    hibernate:
      ddl-auto: update

  ai:
    openai:
      api-key: ${AI_API_KEY}
```

For security, credentials should be provided through environment variables rather than committed to Git.

Example:

```powershell
$env:AI_API_KEY="your-api-key"
$env:DB_USERNAME="your-username"
$env:DB_PASSWORD="your-password"
```

---

# ▶️ Running the Application

## 1. Clone Repository

```bash
git clone https://github.com/<your-username>/<your-repository>.git
cd <your-repository>
```

## 2. Configure Environment Variables

Set:

```text
AI_API_KEY
DB_USERNAME
DB_PASSWORD
```

## 3. Build Application

```bash
./mvnw clean package
```

Windows:

```powershell
.\mvnw.cmd clean package
```

## 4. Run Application

```bash
./mvnw spring-boot:run
```

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The application will start on the configured Spring Boot port.

---

# 🧪 Testing

Run the test suite using:

```bash
./mvnw test
```

Windows:

```powershell
.\mvnw.cmd test
```

Testing should cover:

* Ticket creation
* Ticket retrieval
* Ticket searching
* Ticket status updates
* Ticket priority handling
* AI service behavior
* Tool execution
* Email tool functionality
* Repository operations

---

# 🔐 Security Considerations

The application follows a controlled tool-based architecture.

The LLM is not given direct access to:

```text
Database
File System
Application Internals
```

Instead, the AI can interact with the application only through explicitly registered tools.

```text
                ┌───────────────┐
                │      LLM      │
                └───────┬───────┘
                        │
                  Tool Calling
                        │
                ┌───────▼───────┐
                │ Allowed Tools │
                └───────┬───────┘
                        │
                ┌───────▼───────┐
                │ Application   │
                │ Business Logic│
                └───────────────┘
```

This provides a clear boundary between AI reasoning and application execution.

---

# 🆚 Tool Calling vs RAG

This project intentionally focuses on **Tool Calling instead of RAG**.

| Capability                    | Tool Calling | RAG |
| ----------------------------- | ------------ | --- |
| Create ticket                 | ✅            | ❌   |
| Update ticket                 | ✅            | ❌   |
| Send email                    | ✅            | ❌   |
| Execute application operation | ✅            | ❌   |
| Retrieve documents            | Possible     | ✅   |
| Semantic document search      | ❌            | ✅   |
| Knowledge-base Q&A            | Possible     | ✅   |

### Why Tool Calling?

A Help Desk system frequently needs the AI to **perform actions**, not only retrieve information.

For example:

```text
"Create a ticket"
"Close ticket 1001"
"Send an email"
"Change priority to HIGH"
```

These are application operations, making Tool Calling a natural fit.

> **RAG and pgvector are not currently used in this project.** They could be introduced later for knowledge-base and document-based question answering.

---

# 🔮 Future Enhancements

Potential future improvements include:

* [ ] RAG-based Knowledge Base
* [ ] pgvector integration
* [ ] FAQ document search
* [ ] Conversation memory
* [ ] Customer authentication
* [ ] Role-based access control
* [ ] Ticket assignment to support agents
* [ ] Ticket SLA monitoring
* [ ] Priority-based escalation
* [ ] Email templates
* [ ] Dashboard and analytics
* [ ] Redis caching
* [ ] Kafka-based event processing
* [ ] Docker containerization
* [ ] Kubernetes deployment
* [ ] Observability with Prometheus/Grafana
* [ ] AI-powered ticket summarization

---

# 📸 Screenshots / Demo

Add your application screenshots here:

```text
docs/
├── architecture.png
├── ai-chat.png
├── create-ticket.png
├── ticket-list.png
└── email-notification.png
```

Then reference them in the README:

```markdown
## 📸 Screenshots

### AI Help Desk Chat

![AI Help Desk](docs/ai-chat.png)

### Ticket Management

![Ticket Management](docs/ticket-list.png)
```

---

# 🏆 Project Highlights

This project demonstrates practical implementation of:

* Java backend development
* Spring Boot
* Spring AI
* LLM integration
* AI Tool Calling
* REST API development
* Spring Data JPA
* Hibernate
* Database persistence
* Service-layer architecture
* AI-driven application operations
* Email integration
* Clean separation of AI and business logic

---

# 💡 What This Project Demonstrates

The key idea behind this project is:

> **An LLM should reason about what action is required, while the application remains responsible for executing that action.**

For example:

```text
                    USER
                      │
                      ▼
              Natural Language
                      │
                      ▼
                 SPRING AI
                      │
               Decide Action
                      │
                      ▼
               TOOL CALLING
                      │
          ┌───────────┴───────────┐
          ▼                       ▼
   TicketDatabaseTool          EmailTool
          │                       │
          ▼                       ▼
   TicketService             Email Service
          │
          ▼
   TicketRepository
          │
          ▼
       Database
```

This architecture makes the system easier to extend because new capabilities can be exposed as additional tools without changing the fundamental AI interaction model.

---

# 📂 Recommended Repository Structure

```text
helpdesk-backend/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/irusol/helpdesk/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── entity/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       └── tools/
│   │   │
│   │   └── resources/
│   │       ├── application.yml
│   │       └── helpdesk-main.st
│   │
├── docs/
│   ├── architecture.png
│   └── screenshots/
│
├── pom.xml
├── README.md
├── .gitignore
└── mvnw
```

---

# 👨‍💻 Author

**Shoaib Hasan**

Java Backend Developer | Spring Boot | Microservices | Spring AI | React

### Areas of Interest

```text
Java
Spring Boot
Microservices
Spring AI
REST APIs
Spring Security
JPA / Hibernate
PostgreSQL
Docker
Kubernetes
Kafka
React
AI Engineering
```

---

# 📄 License

This project is intended for **learning, experimentation, and demonstration of Spring AI Tool Calling concepts**.

Add your preferred license, such as:

```text
MIT License
```

---

## ⭐ If You Find This Project Useful

If this project helps you understand **Spring AI, LLM Tool Calling, and AI-powered backend systems**, consider giving the repository a ⭐ on GitHub.
