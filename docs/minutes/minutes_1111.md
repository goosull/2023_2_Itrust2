# 난쏘공 팀 회의록

## 회의 개요

- **작성자**: 이협
- **일시**: 2023.11.11(토) 오후 10:00 - 10:40
- **장소**: 비대면(디스코드)
- **참석자**: 김승원 김이주 노우준 박태주 손예원 유광수 이협 최병준 최우현

## 회의 목적

1. 팀별 진행 상황 공유
2. 팀별 회의 진행

## 회의 내용

### 1. 팀별 진행 상황 공유

**1. UC 15, 16 team**
- UC 15를 진행
- back-end 관련 커밋은 아직 없음
- front-end는 HCP로 로그인시 a patients Emergency Health Records에서 patients search 기능을 진행함

**2. UC 20, 22 team**
- UC 22를 진행
- front-end를 우선적으로 처리
- HCP로 로그인시 Edit Office Visit에서 UI를 추가해둠
- patient로 로그인시 Office Visit에서 UI 구성 완료
- ophthalmology HCP로 로그인시 Document Office Visit에서 Ophthalmology Surgery를 선택했을 때 Information form의 UI를 추가해둠
- Edit Office Visit에서 surgery 기록을 어떻게 보이게 할지 추후 회의예정
- back-end는 추후 논의 예정

**3. UC 21, New UC team**
- NEW UC를 진행
- HCP로 로그인시 Office Visit Document에서 Invoice UI를 추가해둠
- HCP의 Office Visit Document에서 추가한 내용을 View Invoice에서 목록을 볼 수 있다.
- patient로 로그인시 View Invoice에서 목록을 볼 수 있다.
- patient의 View Invoice에서 결제 버튼 추가를 추후 논의예정
- back-end는 Controller부분을 제외한 나머지 기능들을 추가해둠

**추가 논의**
- 팀원이 현재 로컬 서버가 제대로 구동되지 않아 오류를 같이 확인
- Database의 버전 문제로 최신 버전을 다시 깔고 해결함

### 2. 팀별 회의 진행

**1. UC 15, 16 team**
- 11월 15일까지 1차 개발 완료하고 팀별 회의 진행

**2. UC 20, 22 team**
- 11월 17일 까지 1차 API, Controller 기능 개발 완료하고 19일 까지 test 코드 개발 완료하기
- Edit Office Page 구성 논의

**3. UC 21, New UC team**
- APIController, UI 코드 개발 완료하기
- test 코드 개발 완료하기