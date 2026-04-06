# 📌 DebateSeason 크롤러 전용 Extractor

## 🧾 Overview

DebateSeason 크롤러 전용 Extractor는 크롤러가 수집한 원시 데이터로부터 **핵심 키워드를 추출하기 위한 데이터 처리 모듈**입니다.
데이터 수집부터 가공, 키워드 추출까지 이어지는 전체 파이프라인을 안정적으로 수행하며, **데이터 정합성과 처리 효율을 동시에 고려한 설계**를 목표로 합니다.

---

## 🚀 Features

* 크롤링된 원시 데이터 수집 및 정제
* 텍스트 기반 핵심 키워드 추출
* LLM 기반 키워드 분석 (EXAONE 3.5 활용)
* 데이터 처리 상태를 0/1로 관리하여 명확한 처리 흐름 유지

---

## ⚙️ Tech Stack

* **Backend**: Java, Spring Boot
* **Database**: MariaDB
* **Crawling**: Selenium
* **AI Model**: EXAONE 3.5
* **기타**: Prompt Engineering

---

## 🏗️ Architecture & Key Design

### 🔄 안정적인 데이터 수집

* **커서 기반 증분 로딩 (Cursor-based Incremental Loading)** 적용

  * 마지막 처리 지점을 기준으로 데이터를 순차적으로 수집
  * 데이터 누락 없이 안정적인 수집 보장

---

### ⚡ 고성능 데이터 처리

* **멀티 스레드 기반 병렬 비동기 처리 구조**

  * 데이터 가공 및 LLM 요청을 병렬로 처리
  * 처리량(Throughput) 향상 및 확장성 확보

---

### 🛡️ 데이터 정합성 보장

* **Retry 전략 기반 복구 메커니즘**

  * 실패한 요청에 대해 주기적인 재시도 수행
  * 일시적인 장애 상황에서도 데이터 유실 방지

* **상태값 기반 처리 관리 (0 / 1)**

  * 처리 여부를 명확히 구분하여 데이터 흐름 추적 가능
  * 중복 처리 및 누락 방지

---

## ▶️ Usage

### 📊 데이터 처리 흐름

1. 크롤러 서버에서 원시 데이터 수집
2. DB 저장 후 처리 대상 데이터 식별
3. 멀티 스레드 기반 병렬 처리로 데이터 가공
4. LLM(EXAONE 3.5)을 활용한 키워드 추출 수행
5. 처리 완료 상태 업데이트 (0 → 1)

---

## 📂 Project Structure

```
src/
 ├── datasource/     # 백엔드 DB 및 가공 서버 DB 연동
 ├── llm/            # EXAONE 3.5 요청을 위한 DTO
 ├── media/          # 크롤링 대상 도메인 (raw / processed 분리)
 ├── prompt/         # 프롬프트 관련 DTO
 └── scheduler/      # collector & extractor 스케줄링 로직
```

---

## 🎯 Highlights

* 커서 기반 증분 로딩으로 **데이터 유실 없는 안정적인 수집 구조 구현**
* 멀티 스레드 비동기 처리로 **대량 데이터 처리 성능 최적화**
* Retry 전략을 통한 **데이터 정합성 보장 및 장애 복원력 확보**
* 상태값 기반 처리로 **명확하고 추적 가능한 데이터 파이프라인 설계**

---

## 👤 Author

* DebateSeason Team

