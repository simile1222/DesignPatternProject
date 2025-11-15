🧭 1️⃣ 기본 원리
main  ←  안정적인 버전 (배포용)
├── feature/login
├── feature/rental
└── feature/payment


main : 항상 “정상 동작하는 버전” 유지

feature/ : 각 기능별로 독립된 개발 공간

이렇게 나누면 기능별로 작업이 완전히 분리되기 때문에
충돌 위험이 줄고, 코드 관리가 훨씬 깔끔해집니다.

⚙️ 2️⃣ 새로운 기능 개발 시 흐름
(1) 최신 main 가져오기
git checkout main
git pull origin main


GitHub의 최신 main을 로컬 main에 반영
(항상 최신 main에서 새 브랜치를 따야 합니다!)

(2) 새 기능용 브랜치 생성
git checkout -b feature/login


main에서 feature/login 브랜치를 새로 생성 후 이동

(3) 코드 수정 & 커밋
git add .
git commit -m "Implement login feature"

(4) GitHub에 브랜치 업로드 (연동)
git push -u origin feature/login


GitHub에도 동일한 브랜치가 생기고, 이후 push / pull만 해도 자동 동기화됨

🔄 3️⃣ 작업 중 main 업데이트 반영하기 (중요)

기능 작업 중에도 main이 변경될 수 있죠 (다른 개발자가 merge 했을 때).
그럼 내 feature 브랜치에도 최신 main 내용을 반영해야 합니다.

(1) 원격 최신 상태 가져오기
git fetch origin

(2) 내 브랜치로 main 병합
git merge origin/main


충돌(conflict) 발생 시 직접 수정 후 git add . + git commit

(히스토리를 깔끔하게 유지하고 싶다면 merge 대신
git rebase origin/main 도 가능합니다 — 단, 혼자 작업할 때만 추천)

🧩 4️⃣ 기능 개발 완료 후 main으로 병합
(1) main으로 전환
git checkout main

(2) 최신 main 반영
git pull origin main

(3) 병합하기
git merge feature/login


병합 완료 후 main 브랜치에 로그인 기능이 추가됩니다.

(4) 병합 내용 푸시
git push origin main

(5) 필요하면 서브 브랜치 삭제 (정리용)
git branch -d feature/login        # 로컬 브랜치 삭제
git push origin --delete feature/login   # 원격 브랜치 삭제

🧠 5️⃣ 깃허브에서 Pull Request(PR) 방식으로도 가능

협업 시에는 직접 merge 하지 않고,
GitHub 웹에서 Pull Request(PR) 를 만드는 게 일반적입니다 👇

feature/login 푸시 후

GitHub에서 “Compare & Pull Request” 클릭

코드 리뷰 → 승인 → “Merge into main” 클릭

이렇게 하면 기록도 남고, 협업 중 충돌 관리도 쉬워집니다.

✅ 정리 요약
단계	명령어	설명
1	git checkout main && git pull origin main	최신 main 준비
2	git checkout -b feature/기능명	새 기능 브랜치 생성
3	git add . && git commit -m "..."	기능 개발 후 커밋
4	git push -u origin feature/기능명	GitHub에 업로드
5	git merge origin/main	최신 main 반영 (필요 시)
6	git checkout main && git merge feature/기능명	기능 완료 후 병합
7	git push origin main	main 푸시
8	git branch -d feature/...	브랜치 정리 (선택)
🚀 추천 브랜치 네이밍 패턴
용도	브랜치명 예시
기능 개발	feature/login, feature/payment
버그 수정	bugfix/login-error, bugfix/api-null
실험용 코드	experiment/ai-logic-test
긴급 수정	hotfix/security-patch

💬 요약 한 줄로 정리

각 기능마다 feature/기능명 브랜치를 따고,
완성 후 main 으로 merge → push 하면 깔끔한 Git 관리가 됩니다. ✅