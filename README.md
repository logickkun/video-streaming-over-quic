QUIC 기반 WebRTC Video Streaming Platform (On-Premise Deployment)



## 로드맵

- WebRTC 기반의 1:N 비디오 스트리밍 플랫폼 구축
- 백엔드 / 프론트엔드 분리 구조
- Spring Boot 기반 백엔드에 Kafka, Redis, Security 등 마이크로서비스 요소 적용
- 온프레미스 환경에 Docker Swarm으로 초기 배포
- 구현 완료 후 Kubernetes로 이전 예정

---

## 최종 목표

- HTTP/3 기반 <kbd>QUIC</kbd> 프로토콜을 실시간 스트리밍에 적용
- 향후 <kbd>MoQ</kbd> (Media over QUIC) 아키텍처 도입 검토

---

## Frontend 구성

<ul>
  <li><kbd>React (Vite or CRA)</kbd></li>
  <li><kbd>WebRTC API (MediaStream, RTCPeerConnection, ICE)</kbd></li>
  <li><kbd>WebSocket / STOMP</kbd></li>
  <li><kbd>Redux Toolkit</kbd> 또는 <kbd>React Context</kbd></li>
</ul>

---

## Backend

<ul>
  <li><kbd>Spring Boot 3.5.3</kbd></li>
  <li><kbd>Spring Security 6.5.1</kbd></li>
  <li><kbd>Spring Authorization Server 1.5.1</kbd></li>
  <li><kbd>OAuth2 Resource Server 6.5.4</kbd></li>
  <li><kbd>Spring Data JPA 3.2.3</kbd></li>
  <li><kbd>Spring WebSocket 6.1.3</kbd></li>
  <li><kbd>Spring for Apache Kafka 3.2.1</kbd></li>
  <li><kbd>Spring Data Redis 3.2.3</kbd></li>
</ul>


---

## DevOps & Infra

<ul>
  <li><kbd>Docker + Docker Compose + Docker Swarm</kbd> (초기 배포)</li>
  <li><kbd>Traefik</kbd> 또는 <kbd>Nginx</kbd> (Reverse Proxy)</li>
  <li><kbd>Prometheus + Grafana</kbd> (모니터링 예정)</li>
  <li><kbd>Kubernetes</kbd> (최종 마이그레이션 목표)</li>
</ul>

---

## 향후 계획 – QUIC 적용

<ul>
  <li><kbd>WebRTC Signaling → WebTransport</kbd> 대체 실험</li>
  <li><kbd>HTTP/3</kbd> 기반 <kbd>VOD 스트리밍</kbd></li>
  <li><kbd>QUIC Relay</kbd> 기반 실시간 중계 서버 실험</li>
</ul>


---


### Video Codec

<table>
<thead>
<tr><th>코덱</th><th>설명</th><th>장점</th><th>단점</th></tr>
</thead>
<tbody>
<tr><td><kbd>VP8</kbd></td><td>WebRTC 기본 지원</td><td>브라우저 호환성 우수, 무료</td><td>압축률 낮음</td></tr>
<tr><td><kbd>H.264 (AVC)</kbd></td><td>표준 코덱, HLS 및 WebRTC 지원</td><td>HW 가속, 고화질</td><td>특허 라이센스 이슈</td></tr>
<tr><td><kbd>VP9</kbd></td><td>Google 개발 고압축 코덱</td><td>고압축률</td><td>인코딩 부담 큼</td></tr>
<tr><td><kbd>AV1</kbd></td><td>차세대 고효율 코덵</td><td>초고화질, 저대역폭</td><td>지원 미비, 실시간 인코딩 부담</td></tr>
</tbody>
</table>

<ul>
  <li>초기: <kbd>VP8</kbd> 사용</li>
  <li>이후: <kbd>H.264 / VP9</kbd> 트랜스코딩 (FFmpeg)</li>
  <li>실험: <kbd>AV1 + HTTP/3</kbd> 조합 테스트</li>
</ul>

---

### Audio Codec

<table>
<thead>
<tr><th>코덱</th><th>설명</th><th>장점</th><th>단점</th></tr>
</thead>
<tbody>
<tr><td><kbd>Opus</kbd></td><td>WebRTC 기본 오디오 코덱</td><td>저지연, 고음질</td><td>-</td></tr>
<tr><td><kbd>AAC</kbd></td><td>스트리밍/방송 표준</td><td>HLS 호환성</td><td>특허 이슈</td></tr>
</tbody>
</table>



