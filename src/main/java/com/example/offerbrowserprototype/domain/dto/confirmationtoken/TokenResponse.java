package com.example.offerbrowserprototype.domain.dto.confirmationtoken;
public class TokenResponse {

    private String token;
    private Long   userId;
    private String username;
    private String email;

    // ── Konstruktor awaryjny (tylko wiadomość błędu) ──────────────────────
    public TokenResponse(String token) {
        this.token = token;
    }

    // ── Konstruktor pełny — po udanym logowaniu ───────────────────────────
    public TokenResponse(String token, Long userId, String username, String email) {
        this.token    = token;
        this.userId   = userId;
        this.username = username;
        this.email    = email;
    }

    public String getToken()             { return token;    }
    public void   setToken(String t)     { this.token = t;  }

    public Long   getUserId()            { return userId;   }
    public void   setUserId(Long id)     { this.userId = id; }

    public String getUsername()          { return username; }
    public void   setUsername(String u)  { this.username = u; }

    public String getEmail()             { return email;    }
    public void   setEmail(String e)     { this.email = e;  }
}