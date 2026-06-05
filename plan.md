이건 단순 CRUD 프로젝트가 아니라 **"ZeuX가 고객사 시스템을 어떻게 관측하고 RCA하는지 증명하는 데모 환경"**이야.

그래서 목표를 명확히 해야 함.

```text
고객사 서비스 환경 구축
↓
Observability 데이터 생성
↓
장애 유발
↓
Alert 발생
↓
Incident 생성
↓
RCA 분석
↓
Action Recommendation
```

---

# 프로젝트 목표

## 목적

ZeuX 플랫폼의 E2E 검증 환경 구축

검증 범위

```text
Metrics
Logs
Traces

↓

Alert

↓

Incident

↓

RCA

↓

Recommendation
```

---

# 전체 아키텍처

```text
order-service
│
├── payment-service
│      │
│      └── inventory-service
│              │
│              └── PostgreSQL
│
└── recommendation-agent
```

---

# 서비스 구성

## 1. order-service

역할

```text
주문 생성

전체 비즈니스 진입점
```

API

```http
POST /orders

GET /orders/{id}
```

책임

```text
주문 요청 수신

payment-service 호출

recommendation-agent 호출
```

---

## 2. payment-service

역할

```text
결제 처리
```

API

```http
POST /payments
```

책임

```text
결제 검증

inventory-service 호출
```

---

## 3. inventory-service

역할

```text
재고 관리
```

API

```http
POST /inventory/reserve

GET /inventory
```

책임

```text
DB 접근

재고 예약
```

---

## 4. recommendation-agent

역할

```text
추천 엔진
```

API

```http
GET /recommendations/{userId}
```

책임

```text
주문 패턴 분석

상품 추천
```

---

## 5. PostgreSQL

테이블

```sql
products

orders

payments

inventory

recommendations
```

---

# 기술 스택

## 서비스

```text
Spring Boot 3

Java 21

Gradle
```

---

## 통신

```text
OpenFeign
```

또는

```text
RestClient
```

---

## DB

```text
PostgreSQL
```

---

## 컨테이너

```text
Docker

Docker Compose
```

---

# Observability 설계

## Metrics

수집 대상

```text
HTTP Request Count

HTTP Latency

Error Rate

JVM Memory

JVM GC

Thread Count

Database Latency
```

---

## Logs

형식

```json
{
  "service":"inventory-service",
  "level":"ERROR",
  "traceId":"..."
}
```

---

## Traces

수집

```text
OTel Java Agent
```

자동 계측

```text
Spring MVC

Feign

JDBC

RestTemplate

WebClient
```

---

# 장애 시나리오 설계

이게 제일 중요함.

---

## Scenario 1

DB Latency

inventory-service

```java
Thread.sleep(5000)
```

---

예상 결과

```text
Latency 증가

Trace 증가

Alert 발생

Incident 생성
```

---

RCA

```text
Root Cause

Database Latency
```

---

## Scenario 2

DB Connection Failure

```java
throw SQLException
```

---

예상 결과

```text
HTTP 500 증가
```

---

RCA

```text
Root Cause

Database Connection Error
```

---

## Scenario 3

recommendation-agent 장애

```bash
docker stop recommendation-agent
```

---

예상 결과

```text
Downstream Timeout
```

---

RCA

```text
Affected Component

recommendation-agent
```

---

## Scenario 4

CPU 폭주

```java
while(true){}
```

---

예상 결과

```text
CPU 100%

Response Time 증가
```

---

# Trace 검증 목표

정상

```text
order-service

├── payment-service
│     └── inventory-service
│           └── PostgreSQL
│
└── recommendation-agent
```

---

장애

```text
inventory-service
     ↓
PostgreSQL
     ↓
Latency 증가
```

---

Tempo

```text
Span Duration

5000ms
```

확인

---

# RCA 검증 목표

입력

```text
Alert

Error Rate

Latency

Trace

Logs
```

---

출력

```json
{
  "rootCauseType":"DATABASE_LATENCY",
  "affectedComponent":"inventory-service",
  "confidence":0.92
}
```

---

# 개발 순서

## Phase 1

서비스 생성

```text
order-service

payment-service

inventory-service

recommendation-agent
```

---

## Phase 2

PostgreSQL 연동

```text
inventory

orders
```

---

## Phase 3

서비스 간 호출

```text
Feign
```

구성

---

## Phase 4

Docker Compose

```text
4 Services

1 PostgreSQL
```

구성

---

## Phase 5

OTel Java Agent 적용

```text
Metrics

Logs

Traces
```

검증

---

## Phase 6

장애 유발 API 추가

```http
POST /chaos/db-delay

POST /chaos/error

POST /chaos/cpu
```

---

## Phase 7

Prometheus Alert Rule 작성

```text
Latency

Error Rate

Availability
```

---

## Phase 8

Incident 생성 검증

---

## Phase 9

RCA 엔진 검증

---

# 완료 기준 (Definition of Done)

다음이 실제로 동작해야 함.

```text
1. 주문 생성

2. 서비스 간 Trace 생성

3. PostgreSQL Span 생성

4. Grafana 확인 가능

5. 장애 유발

6. Alert 발생

7. Incident 생성

8. RCA 수행

9. Root Cause 출력
```

여기까지 되면 ZeuX MVP의 **관측 → 탐지 → 분석(RCA)** 전체 흐름을 시연할 수 있는 완전한 고객사 샘플 환경이 된다.
