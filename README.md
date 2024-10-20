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
