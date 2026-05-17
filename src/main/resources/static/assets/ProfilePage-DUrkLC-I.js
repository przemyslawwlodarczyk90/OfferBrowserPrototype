import{u as P,a as j,j as e,r as c,b as y,t as g}from"./index-BXWfP1i_.js";import{u as k}from"./index-BwTqQwrb.js";import{P as z}from"./index-huvFFqiG.js";function S(a){const s={};return a.username.trim()?a.username.length<3&&(s.username="Min. 3 znaki"):s.username="Pole wymagane",a.email&&!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(a.email)&&(s.email="Nieprawidłowy adres e-mail"),s}function C(a){const s={};return a.currentPassword||(s.currentPassword="Pole wymagane"),a.newPassword?a.newPassword.length<6&&(s.newPassword="Min. 6 znaków"):s.newPassword="Pole wymagane",a.confirmPassword?a.newPassword!==a.confirmPassword&&(s.confirmPassword="Hasła nie są zgodne"):s.confirmPassword="Pole wymagane",s}function N(a){if(!a)return null;let s=0;return a.length>=8&&s++,/[A-Z]/.test(a)&&s++,/[0-9]/.test(a)&&s++,/[^A-Za-z0-9]/.test(a)&&s++,["Bardzo słabe","Słabe","Średnie","Silne","Bardzo silne"][s]}function Z(a){const s=N(a);return{"Bardzo słabe":"var(--red)",Słabe:"var(--red)",Średnie:"var(--yellow)",Silne:"var(--green)","Bardzo silne":"var(--green)"}[s]??"var(--text-3)"}function A(a){if(!a)return"0%";let s=0;return a.length>=8&&s++,/[A-Z]/.test(a)&&s++,/[0-9]/.test(a)&&s++,/[^A-Za-z0-9]/.test(a)&&s++,`${s/4*100}%`}function U({user:a}){const s=a?.username?a.username.slice(0,2).toUpperCase():"??";return e.jsxs("div",{className:"pf-avatar-section",children:[e.jsx("div",{className:"pf-avatar",children:s}),e.jsxs("div",{className:"pf-avatar-info",children:[e.jsx("p",{className:"pf-avatar-name",children:a?.username??"—"}),e.jsx("p",{className:"pf-avatar-email",children:a?.email??"brak adresu e-mail"}),a?.id&&e.jsxs("p",{className:"pf-avatar-id",children:["ID: ",a.id]})]})]})}function F({user:a,onUpdated:s}){const[r,d]=c.useState({username:a?.username??"",email:a?.email??""}),[o,p]=c.useState({}),[n,h]=c.useState(!1),[m,f]=c.useState(!1),b=x=>{const{name:t,value:i}=x.target;d(l=>({...l,[t]:i})),f(!1),o[t]&&p(l=>({...l,[t]:void 0}))},w=async x=>{x.preventDefault();const t=S(r);if(Object.keys(t).length){p(t);return}h(!0);try{await y.updateProfile({username:r.username.trim(),email:r.email.trim()||void 0}),s({username:r.username.trim(),email:r.email.trim()||null}),g.success("Profil zaktualizowany"),f(!0)}catch(i){g.error(i?.message??"Błąd aktualizacji profilu")}finally{h(!1)}},u=r.username!==(a?.username??"")||r.email!==(a?.email??"");return e.jsxs("section",{className:"pf-section",children:[e.jsxs("div",{className:"pf-section-head",children:[e.jsx("span",{className:"pf-section-icon",children:"⊙"}),e.jsxs("div",{children:[e.jsx("h2",{className:"pf-section-title",children:"Dane profilu"}),e.jsx("p",{className:"pf-section-sub",children:"Zmień nazwę użytkownika lub adres e-mail"})]})]}),e.jsxs("form",{onSubmit:w,className:"pf-section-body",noValidate:!0,children:[e.jsxs("div",{className:"pf-field",children:[e.jsxs("label",{className:"pf-label",htmlFor:"pf-username",children:["Nazwa użytkownika ",e.jsx("span",{className:"pf-required",children:"*"})]}),e.jsx("input",{id:"pf-username",name:"username",className:`pf-input${o.username?" pf-input--err":""}`,value:r.username,onChange:b,disabled:n,autoComplete:"username"}),o.username&&e.jsx("span",{className:"pf-err",children:o.username})]}),e.jsxs("div",{className:"pf-field",children:[e.jsxs("label",{className:"pf-label",htmlFor:"pf-email",children:["Adres e-mail ",e.jsx("span",{className:"pf-optional",children:"(opcjonalnie)"})]}),e.jsx("input",{id:"pf-email",name:"email",type:"email",className:`pf-input${o.email?" pf-input--err":""}`,value:r.email,onChange:b,disabled:n,autoComplete:"email",placeholder:"np. jan@example.com"}),o.email&&e.jsx("span",{className:"pf-err",children:o.email})]}),e.jsxs("div",{className:"pf-foot",children:[m&&!u&&e.jsx("span",{className:"pf-saved",children:"✓ Zapisano"}),e.jsx("button",{type:"submit",className:"pf-btn pf-btn--primary",disabled:n||!u,children:n?e.jsxs(e.Fragment,{children:[e.jsx("span",{className:"pf-spin"}),"Zapisywanie…"]}):"Zapisz zmiany"})]})]})]})}function $(){const[a,s]=c.useState({currentPassword:"",newPassword:"",confirmPassword:""}),[r,d]=c.useState({}),[o,p]=c.useState(!1),[n,h]=c.useState({current:!1,newPw:!1,confirm:!1}),m=t=>{const{name:i,value:l}=t.target;s(v=>({...v,[i]:l})),r[i]&&d(v=>({...v,[i]:void 0}))},f=t=>h(i=>({...i,[t]:!i[t]})),b=async t=>{t.preventDefault();const i=C(a);if(Object.keys(i).length){d(i);return}p(!0);try{await y.changePassword({currentPassword:a.currentPassword,newPassword:a.newPassword}),g.success("Hasło zostało zmienione"),s({currentPassword:"",newPassword:"",confirmPassword:""}),d({})}catch(l){g.error(l?.message??"Błąd zmiany hasła"),(l?.status===400||l?.status===401)&&d({currentPassword:"Nieprawidłowe aktualne hasło"})}finally{p(!1)}},w=N(a.newPassword),u=Z(a.newPassword),x=A(a.newPassword);return e.jsxs("section",{className:"pf-section",children:[e.jsxs("div",{className:"pf-section-head",children:[e.jsx("span",{className:"pf-section-icon",children:"◈"}),e.jsxs("div",{children:[e.jsx("h2",{className:"pf-section-title",children:"Zmiana hasła"}),e.jsx("p",{className:"pf-section-sub",children:"Ustaw nowe hasło do konta"})]})]}),e.jsxs("form",{onSubmit:b,className:"pf-section-body",noValidate:!0,children:[e.jsxs("div",{className:"pf-field",children:[e.jsx("label",{className:"pf-label",htmlFor:"pf-cur-pw",children:"Aktualne hasło"}),e.jsxs("div",{className:"pf-input-wrap",children:[e.jsx("input",{id:"pf-cur-pw",name:"currentPassword",type:n.current?"text":"password",className:`pf-input pf-input--pw${r.currentPassword?" pf-input--err":""}`,value:a.currentPassword,onChange:m,disabled:o,autoComplete:"current-password",placeholder:"••••••••"}),e.jsx("button",{type:"button",className:"pf-eye",onClick:()=>f("current"),"aria-label":n.current?"Ukryj":"Pokaż",children:n.current?"◎":"●"})]}),r.currentPassword&&e.jsx("span",{className:"pf-err",children:r.currentPassword})]}),e.jsxs("div",{className:"pf-field",children:[e.jsx("label",{className:"pf-label",htmlFor:"pf-new-pw",children:"Nowe hasło"}),e.jsxs("div",{className:"pf-input-wrap",children:[e.jsx("input",{id:"pf-new-pw",name:"newPassword",type:n.newPw?"text":"password",className:`pf-input pf-input--pw${r.newPassword?" pf-input--err":""}`,value:a.newPassword,onChange:m,disabled:o,autoComplete:"new-password",placeholder:"min. 6 znaków"}),e.jsx("button",{type:"button",className:"pf-eye",onClick:()=>f("newPw"),"aria-label":n.newPw?"Ukryj":"Pokaż",children:n.newPw?"◎":"●"})]}),r.newPassword&&e.jsx("span",{className:"pf-err",children:r.newPassword}),a.newPassword&&e.jsxs("div",{className:"pf-strength",children:[e.jsx("div",{className:"pf-strength-track",children:e.jsx("div",{className:"pf-strength-fill",style:{width:x,background:u}})}),e.jsx("span",{className:"pf-strength-label",style:{color:u},children:w})]})]}),e.jsxs("div",{className:"pf-field",children:[e.jsx("label",{className:"pf-label",htmlFor:"pf-conf-pw",children:"Powtórz nowe hasło"}),e.jsxs("div",{className:"pf-input-wrap",children:[e.jsx("input",{id:"pf-conf-pw",name:"confirmPassword",type:n.confirm?"text":"password",className:`pf-input pf-input--pw${r.confirmPassword?" pf-input--err":""}`,value:a.confirmPassword,onChange:m,disabled:o,autoComplete:"new-password",placeholder:"••••••••"}),e.jsx("button",{type:"button",className:"pf-eye",onClick:()=>f("confirm"),"aria-label":n.confirm?"Ukryj":"Pokaż",children:n.confirm?"◎":"●"})]}),r.confirmPassword&&e.jsx("span",{className:"pf-err",children:r.confirmPassword})]}),e.jsx("div",{className:"pf-foot",children:e.jsx("button",{type:"submit",className:"pf-btn pf-btn--primary",disabled:o||!a.currentPassword||!a.newPassword||!a.confirmPassword,children:o?e.jsxs(e.Fragment,{children:[e.jsx("span",{className:"pf-spin"}),"Zapisywanie…"]}):"Zmień hasło"})})]})]})}function B({onLogout:a}){const[s,r]=c.useState(!1);return e.jsxs("section",{className:"pf-section pf-section--danger",children:[e.jsxs("div",{className:"pf-section-head",children:[e.jsx("span",{className:"pf-section-icon pf-section-icon--danger",children:"⊘"}),e.jsxs("div",{children:[e.jsx("h2",{className:"pf-section-title",children:"Sesja i konto"}),e.jsx("p",{className:"pf-section-sub",children:"Wylogowanie z aplikacji"})]})]}),e.jsx("div",{className:"pf-section-body",children:s?e.jsxs("div",{className:"pf-confirm animate-fade-in",children:[e.jsx("p",{className:"pf-confirm-text",children:"Czy na pewno chcesz się wylogować?"}),e.jsxs("div",{className:"pf-confirm-actions",children:[e.jsx("button",{className:"pf-btn pf-btn--ghost",onClick:()=>r(!1),children:"Anuluj"}),e.jsx("button",{className:"pf-btn pf-btn--danger",onClick:a,children:"⊘ Tak, wyloguj"})]})]}):e.jsxs("div",{className:"pf-danger-row",children:[e.jsxs("div",{children:[e.jsx("p",{className:"pf-danger-title",children:"Wyloguj się"}),e.jsx("p",{className:"pf-danger-desc",children:"Zakończy bieżącą sesję i usunie token JWT z przeglądarki."})]}),e.jsx("button",{className:"pf-btn pf-btn--danger",onClick:()=>r(!0),children:"⊘ Wyloguj"})]})})]})}function H(){k("Profil");const a=P(),s=j(n=>n.user),r=j(n=>n.logout),d=j(n=>n.updateUser),o=n=>{d(n)},p=()=>{r(),a("/login",{replace:!0})};return e.jsxs("div",{className:"profile-page animate-fade-in",children:[e.jsx(z,{title:"Profil",subtitle:"Ustawienia konta"}),e.jsxs("div",{className:"pf-layout",children:[e.jsx("aside",{className:"pf-sidebar",children:e.jsx(U,{user:s})}),e.jsxs("div",{className:"pf-main",children:[e.jsx(F,{user:s,onUpdated:o}),e.jsx($,{}),e.jsx(B,{onLogout:p})]})]}),e.jsx(D,{})]})}function D(){return e.jsx("style",{children:`
      .profile-page { max-width: 900px; }

      /* ── Layout ── */
      .pf-layout {
        display: grid;
        grid-template-columns: 220px 1fr;
        gap: 20px; align-items: flex-start;
      }
      @media (max-width: 640px) {
        .pf-layout { grid-template-columns: 1fr; }
      }

      /* ── Avatar sidebar ── */
      .pf-sidebar {
        position: sticky; top: 24px;
      }
      .pf-avatar-section {
        display: flex; flex-direction: column; align-items: center; gap: 14px;
        background: var(--bg-1); border: 1px solid var(--border-1);
        border-radius: var(--radius-lg); padding: 24px 16px; text-align: center;
      }
      .pf-avatar {
        width: 72px; height: 72px; border-radius: 50%;
        background: var(--accent-glow); border: 2px solid var(--border-0);
        display: flex; align-items: center; justify-content: center;
        font-family: var(--font-display); font-weight: 800;
        font-size: 1.5rem; color: var(--accent);
      }
      .pf-avatar-name {
        font-family: var(--font-display); font-weight: 700;
        font-size: 0.95rem; color: var(--text-0);
      }
      .pf-avatar-email {
        font-family: var(--font-mono); font-size: 0.7rem;
        color: var(--text-2); word-break: break-all;
      }
      .pf-avatar-id {
        font-family: var(--font-mono); font-size: 0.65rem; color: var(--text-3);
      }

      /* ── Główna kolumna ── */
      .pf-main { display: flex; flex-direction: column; gap: 14px; }

      /* ── Sekcja ── */
      .pf-section {
        background: var(--bg-1); border: 1px solid var(--border-1);
        border-radius: var(--radius-lg); overflow: hidden;
      }
      .pf-section--danger { border-color: rgba(239,68,68,.2); }

      .pf-section-head {
        display: flex; align-items: flex-start; gap: 12px;
        padding: 16px 20px; border-bottom: 1px solid var(--border-1);
        background: var(--bg-2);
      }
      .pf-section--danger .pf-section-head {
        background: rgba(239,68,68,.04);
        border-bottom-color: rgba(239,68,68,.15);
      }
      .pf-section-icon {
        font-size: 1.1rem; color: var(--accent);
        flex-shrink: 0; margin-top: 2px;
      }
      .pf-section-icon--danger { color: var(--red); }
      .pf-section-title {
        font-family: var(--font-display); font-weight: 700;
        font-size: 0.92rem; color: var(--text-0); margin-bottom: 2px;
      }
      .pf-section-sub { font-size: 0.75rem; color: var(--text-2); }

      .pf-section-body {
        padding: 20px; display: flex; flex-direction: column; gap: 14px;
      }

      /* ── Pola formularza ── */
      .pf-field { display: flex; flex-direction: column; gap: 6px; }
      .pf-label {
        font-family: var(--font-mono); font-size: 0.73rem; color: var(--text-2);
      }
      .pf-required { color: var(--accent); }
      .pf-optional { color: var(--text-3); font-size: 0.67rem; }
      .pf-input {
        padding: 9px 12px;
        background: var(--bg-2); border: 1px solid var(--border-1);
        border-radius: var(--radius-md);
        font-family: var(--font-mono); font-size: 0.82rem; color: var(--text-0);
        outline: none; transition: border-color .15s, box-shadow .15s;
      }
      .pf-input::placeholder { color: var(--text-3); }
      .pf-input:focus { border-color: var(--accent); box-shadow: 0 0 0 2px var(--accent-glow); }
      .pf-input--err { border-color: var(--red) !important; }
      .pf-input--pw  { padding-right: 40px; }
      .pf-err {
        font-family: var(--font-mono); font-size: 0.7rem; color: var(--red);
      }

      /* Input z okiem */
      .pf-input-wrap { position: relative; }
      .pf-eye {
        position: absolute; right: 10px; top: 50%; transform: translateY(-50%);
        background: none; border: none; color: var(--text-3);
        cursor: pointer; font-size: 0.8rem; padding: 2px;
        transition: color .15s;
      }
      .pf-eye:hover { color: var(--text-0); }

      /* Siła hasła */
      .pf-strength {
        display: flex; align-items: center; gap: 10px; margin-top: 2px;
      }
      .pf-strength-track {
        flex: 1; height: 3px; background: var(--bg-3); border-radius: 2px; overflow: hidden;
      }
      .pf-strength-fill {
        height: 100%; border-radius: 2px; transition: width .3s ease, background .3s ease;
      }
      .pf-strength-label {
        font-family: var(--font-mono); font-size: 0.67rem;
        white-space: nowrap; transition: color .3s ease;
      }

      /* Stopka formularza */
      .pf-foot {
        display: flex; justify-content: flex-end; align-items: center; gap: 10px;
        padding-top: 4px; border-top: 1px solid var(--border-1);
      }
      .pf-saved {
        font-family: var(--font-mono); font-size: 0.72rem; color: var(--green);
      }

      /* ── Przyciski ── */
      .pf-btn {
        display: inline-flex; align-items: center; gap: 6px;
        font-family: var(--font-mono); font-weight: 600;
        border-radius: var(--radius-md); border: 1px solid;
        cursor: pointer; white-space: nowrap;
        transition: background .15s, border-color .15s, box-shadow .15s, transform .1s;
        padding: 8px 16px; font-size: 0.8rem;
      }
      .pf-btn:active:not(:disabled) { transform: scale(.98); }
      .pf-btn:disabled { opacity: .45; cursor: not-allowed; }

      .pf-btn--primary {
        background: var(--accent); color: #000; border-color: var(--accent);
      }
      .pf-btn--primary:hover:not(:disabled) {
        background: var(--accent-dim); box-shadow: var(--shadow-accent);
      }
      .pf-btn--ghost {
        background: transparent; color: var(--text-1); border-color: var(--border-1);
      }
      .pf-btn--ghost:hover:not(:disabled) { background: var(--bg-2); color: var(--text-0); }
      .pf-btn--danger {
        background: transparent; color: var(--red); border-color: rgba(239,68,68,.3);
      }
      .pf-btn--danger:hover:not(:disabled) { background: rgba(239,68,68,.1); }

      /* Spinner */
      .pf-spin {
        display: inline-block; width: 12px; height: 12px;
        border: 2px solid transparent; border-top-color: currentColor;
        border-radius: 50%; animation: spin .6s linear infinite;
      }

      /* ── Strefa niebezpieczna ── */
      .pf-danger-row {
        display: flex; align-items: center;
        justify-content: space-between; gap: 16px; flex-wrap: wrap;
      }
      .pf-danger-title {
        font-family: var(--font-display); font-weight: 700;
        font-size: 0.85rem; color: var(--text-0); margin-bottom: 3px;
      }
      .pf-danger-desc { font-size: 0.77rem; color: var(--text-2); }

      .pf-confirm { display: flex; flex-direction: column; gap: 12px; }
      .pf-confirm-text {
        font-family: var(--font-mono); font-size: 0.82rem; color: var(--text-1);
      }
      .pf-confirm-actions { display: flex; gap: 8px; }
    `})}export{H as default};
