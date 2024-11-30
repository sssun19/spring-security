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
*sec:authorize와 sec:authentiacion() 의 차이*

![image](https://github.com/user-attachments/assets/05ada567-8b97-4f6a-91a0-e9ec5c1e4c38)

1️⃣ sec:authorize 태그는 해당 요소를 표시할 수 있는 권한을 설정하는 데 사용

(authenticated 속성 값은 현재 사용자가 인증되었을 때만 해당 콘텐츠를 표시하겠다는 의미)

2️⃣ sec:authentication 태그는 현재 사용자의 인증 상태 또는 속성을 HTML 내에서 직접 출력할 때 사용

(isAuthenticated()는 현재 사용자가 인증되었는지 여부를 Boolean 값으로 반환하는 메서드)

---

🌟 UsernamePasswordAuthenticationFilter 커스터마이징

![image](https://github.com/user-attachments/assets/d0774c67-ae6c-4b48-aaa5-d420cda73d24)

사실상 폼 로그인 필터와 커스텀 필터를 같이 사용한 모습이지만 로그인 필터보다 커스텀 필터를 먼저 사용하기 때문에

로그인 필터 내의 defaultSuccessUrl 이나 failureUrl 을 사용하고 싶다면 두 필터를 같이 써도 괜찮다!


🧑‍🏫 #login-multi-chain 학습 도중 로그인이 되지 않는 오류 발생

```
<script>
    async function handleLogin() {
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        const rememberMe = document.getElementById('rememberMe').checked;
        const type = document.querySelector('input[name="type"]:checked').value;

        try {
            const response = await fetch('/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({
                    username,
                    password,
                    rememberMe,
                    type
                })
            });

            if (response.ok) {
                console.log("로그인 성공");
                console.log("아이디: ", username);
                console.log("비번 : ", password);
                window.location.href = '/';
            } else {
                const errorMessage = await response.text();
                console.error("로그인 오류: ", errorMessage);
                document.getElementById('errorMessage').textContent = "로그인 실패: 아이디나 비밀번호가 올바르지 않습니다.";
            }
        } catch (error) {
            console.error("네트워크 오류:", error);
            document.getElementById('errorMessage').textContent = "네트워크 오류가 발생했습니다.";
        }

    }

</script>
```

script 로 콘솔 출력하여 오류를 잡아보려고 했지만 로그인 성공 여부와 상관 없이 response 가 ok 로 넘어왔다.

![image](https://github.com/user-attachments/assets/c10868c2-af8a-4269-9f96-e2d4632b1194)

해당 코드로 아무 비밀번호나 입력해도 로그인이 되게끔 설정해 놨는데도 계속해서 로그인이 되지 않았다.

계속해서 시도해본 결과, 커스텀 필터와 spring security 에서 기본 작동하는 필터가 충돌한 듯 싶어 이것저것 수정해보았다.

```
@Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomLoginFilter filter = new CustomLoginFilter(authenticationManager());
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeRequests(request->
                        request.antMatchers("/", "/login").permitAll()
                                .anyRequest().authenticated()
                )

                .addFilterAt(filter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout->logout.logoutSuccessUrl("/"))
                .exceptionHandling(e->e.accessDeniedPage("/access-denied"))
                .formLogin()
                .loginProcessingUrl("/")
                .failureUrl("/login-error")
                .permitAll()
        ;
    }
```

위와 같이 formLogin() 메서드를 깔끔하게 비우고 꼭 필요한 메서드는 따로 구현해주었다. formLogin 메서드 안에서 로그인 페이지, 실패한 경우 등 여러 설정을 붙였더니 커스텀 필터와 충돌이 난 것 같다.

---
👨‍🏫 RememberMeAuthenticationToken

![image](https://github.com/user-attachments/assets/d3d2211a-8c75-4493-8513-ab1b0d764f06)

security config 클래스에서 rememberMe() 활성화</br>
rememberMeAuthenticationFilter 에서 rememberMe 를 감지하여 사용자의 session 을 저장해둔다.</br>
*단! 사용자의 쿠키값이 탈취 되었다고 가정할 경우 로그아웃을 해도</br>
![image](https://github.com/user-attachments/assets/c637a2e3-db65-4082-b61f-2b1db710ade6)

수동으로 입력해주면 로그인을 할 수가 있다.
