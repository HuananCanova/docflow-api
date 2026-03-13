# DocFlow API 📄

> REST API for document processing using Java, Spring Boot and AWS

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.5-green?style=flat-square&logo=springboot)
![AWS](https://img.shields.io/badge/AWS-S3_|_Textract_|_Lambda-yellow?style=flat-square&logo=amazonaws)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=flat-square&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-ready-blue?style=flat-square&logo=docker)

---

## 📋 About

DocFlow API is a document processing service that receives file uploads (PNG, JPEG, TIFF, PDF), stores them in AWS S3, and automatically extracts text content using AWS Textract AI. The extracted text is persisted in PostgreSQL and made available through a REST API.

**Real-world use case:** Companies that receive large volumes of scanned documents (invoices, contracts, forms) can use DocFlow to automatically extract and index the text content, eliminating manual data entry.

---

## 🏗️ Architecture

```
Client
  │
  ▼
Spring Boot API  ──────────►  AWS S3 (storage)
  │                                │
  │ returns PROCESSING             │ triggers
  │                                ▼
  │                          AWS Lambda
  │                                │
  │                                ▼
  │                         AWS Textract (OCR)
  │                                │
  │                                ▼
  └──────────────────────  PostgreSQL (update to COMPLETED)
```

### Synchronous flow (current)
```
POST /documents/upload
        │
        ▼
  Upload to S3
        │
        ▼
  Textract extracts text
        │
        ▼
  Save to PostgreSQL
        │
        ▼
  Return result (status: COMPLETED)
```

### Asynchronous flow (in progress 🚧)
```
POST /documents/upload
        │
        ▼
  Upload to S3
        │
        ▼
  Return immediately (status: PROCESSING)
        │
  Lambda triggered by S3 event (background)
        │
        ▼
  Textract extracts text
        │
        ▼
  PUT /documents/{id}/complete
        │
        ▼
  PostgreSQL updated (status: COMPLETED)
```

---

## 🚀 Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 3.4.5 |
| Database | PostgreSQL 16 |
| ORM | Spring Data JPA / Hibernate |
| Cloud Storage | AWS S3 |
| Text Extraction | AWS Textract |
| Serverless | AWS Lambda (Node.js 20) |
| Containerization | Docker |
| Build | Maven |

---

## 📡 API Endpoints

| Method | Endpoint | Description | Status |
|--------|----------|-------------|--------|
| `POST` | `/documents/upload` | Upload a file for processing | ✅ |
| `GET` | `/documents` | List all documents | ✅ |
| `GET` | `/documents/{id}` | Get document by ID | ✅ |
| `PUT` | `/documents/{id}/complete` | Update document with extracted text (Lambda callback) | 🚧 |

### Upload example

**Request:**
```bash
curl -X POST http://localhost:8080/documents/upload \
  -F "file=@document.png"
```

**Response:**
```json
{
  "id": 1,
  "fileName": "document.png",
  "status": "PROCESSING",
  "extractedText": null,
  "uploadedAt": "2026-03-03T22:00:00"
}
```

### Supported file formats
- ✅ PNG
- ✅ JPEG
- ✅ TIFF
- ✅ PDF (text-based)

---

## ⚙️ Running locally

### Prerequisites
- Java 17
- Docker Desktop
- Maven
- AWS account with S3 and Textract access

### 1. Clone the repository
```bash
git clone https://github.com/HuananCanova/docflow-api.git
cd docflow-api
```

### 2. Start PostgreSQL with Docker
```bash
docker run --name docflow-postgres \
  -e POSTGRES_USER=docflow \
  -e POSTGRES_PASSWORD=docflow123 \
  -e POSTGRES_DB=docflow \
  -p 5432:5432 \
  -d postgres:16
```

### 3. Configure credentials
Create `src/main/resources/application-local.properties`:
```properties
aws.accessKeyId=YOUR_ACCESS_KEY
aws.secretKey=YOUR_SECRET_KEY
spring.datasource.password=docflow123
```

### 4. Run the application
```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

---

## 🔒 Security

- AWS credentials stored in local properties file (not committed to git)
- `.gitignore` configured to exclude sensitive files
- IAM user with minimal required permissions (S3 + Textract only)
- Global exception handler — no internal details exposed in error responses

---

## 🗺️ Roadmap

- [x] REST API with Spring Boot
- [x] File upload to AWS S3
- [x] Text extraction with AWS Textract
- [x] PostgreSQL persistence with JPA
- [x] Global exception handler
- [x] File type validation
- [ ] Async processing with AWS Lambda
- [ ] JWT Authentication with Spring Security
- [ ] Automated tests with JUnit and Mockito
- [ ] Dockerfile and containerization
- [ ] AWS RDS for production database
- [ ] React frontend for file upload interface

---

## 👨‍💻 Author

**Huanan Augusto Canova**
- LinkedIn: [linkedin.com/in/huanan-canova](https://www.linkedin.com/in/huanan-canova)
- GitHub: [github.com/HuananCanova](https://github.com/HuananCanova)
