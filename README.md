# docs-hub
Reactive ddd microservices in Spring Boot WebFlux for loading PDFs, generating embeddings with Ollama, and answering questions via RAG, using Kafka for event-driven communication

## Tech

- Spring Boot
- Gradle
- Docker
- Docker Compose

## 🚀 Run Locally

### Prerequisites
Make sure the following tools are installed on your system:

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Java JDK 17+](https://adoptium.net/)

### 🧱 Setup & Run

#### 1. Clone the repository

```bash
 git clone https://github.com/dave-testaverde/docs-hub
 cd docs-hub
```

#### 2. Clone the repository
- Open IntelliJ IDEA
- Click on "Open" and select the cloned folder
- Allow the IDE to resolve dependencies (wait for indexing to finish)

#### 3. Build the project using bootJar

```bash
 ./gradlew clean build
```

#### 4. Start environment using Docker Compose

```bash
 docker-compose -p hub_services up --build 
```

#### 5. Enter the Ollama container and install models

Run `docker ps` and locate your ollama container. In my case it is `hub_services-ollama-1`

```bash
 docker exec -it hub_services-ollama-1 bash
 ollama pull llama3
 ollama pull nomic-embed-text 
 ollama pull mxbai-embed-large
```

Warning: `The project is sized for nomic-embed-text, if you want to use mxbai-embed-large remember you have to increase the size of the chunks and adapt the system for larger emebedding: nomic-embed-text 768 dim, mxbai-embed-large 1024 dim`

#### 6. Generate Embeddings from a Document

You can generate embeddings by sending a POST request with a document file:
```bash
 curl -X POST http://localhost:8082/documents/upload \
   -F "file=@data/sample.pdf"
```

If all went well you should get a response like `Uploaded and processed`

#### 7. Generate Q&A from the Document

Once embeddings are generated, you can send a POST request to ask a question:

```bash
 curl -X POST http://localhost:8085/api/query \
  -H "Content-Type: application/json" \
  -d '{"text": "Quali argomenti sono trattati nel documento?"}'
```
