# spring-security

**application.yml 파일 설정**

![image](https://github.com/user-attachments/assets/476ecd18-95dc-4a6f-a312-4a3ab29de908)


spring security 하위 user 에 더미 아이디, 패스워드를 만들어준다.

---

*HomeController*

![image](https://github.com/user-attachments/assets/36a557f3-3411-4ea2-85c3-d27598865871)

@PreAutorize annotation 으로 role 부여

user 계정으로 들어가도 /admin 정보를 확인할 수 있다. 이런 경우 spring security 에서는 SecurityConfig 로 권한을 체크함.

WebSecurityConfigurerAdapter 상속 받아 보안 강화

@EnableWebSecurity

@EnableGlobalMethodSecurity(prePostEnabled = true) //prePost 로 권한 체크 선언

![image](https://github.com/user-attachments/assets/71ec5f3c-2f63-4ac5-a292-7509c2b6acac)

위와 같이 user 계정으로 로그인 하면 /admin 페이지에 권한이 통과 되지 않아 막힌다.

*SecurityConfig*

![image](https://github.com/user-attachments/assets/eef4bb41-2eea-408a-941c-2634ce3f1093)

application.yml에 설정할 수 있는 user 는 1개 뿐이다. configure 메서드로 쉽게 유저를 생성할 수 있다.

password 를 인코딩 없이 그대로 문자열을 집어넣으면 보안 문제가 발생하므로 PasswordEncoder 클래스로 인코딩 해주어야 한다.

![image](https://github.com/user-attachments/assets/81a7c0c0-7d1e-40af-803c-c2da8b834ca2)

admin 계정으로 로그인 하면 /admin 페이지에 접근할 수 있다.

**하지만** /user 페이지로 가려고 하면 아까 user 계정으로 /admin 페이지에 접근한 것 처럼 spring security 에 막힌다.

🧑‍🏫
*sec:authorize와 sec:authentiacion 의 차이*

![image](https://github.com/user-attachments/assets/05ada567-8b97-4f6a-91a0-e9ec5c1e4c38)

1️⃣ sec:authorize 태그는 해당 요소를 표시할 수 있는 권한을 설정하는 데 사용

(authenticated 속성 값은 현재 사용자가 인증되었을 때만 해당 콘텐츠를 표시하겠다는 의미)

2️⃣ sec:authentication 태그는 현재 사용자의 인증 상태 또는 속성을 HTML 내에서 직접 출력할 때 사용

(isAuthenticated()는 현재 사용자가 인증되었는지 여부를 Boolean 값으로 반환하는 메서드)
