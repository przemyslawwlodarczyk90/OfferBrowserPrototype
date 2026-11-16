import{r as o,q as P,j as e,t as b,u as B,a as z,b as F}from"./index-DlloBimL.js";import{i as C,e as R,u as V}from"./index-DqRoP1O5.js";import{P as Z}from"./index-CFHmIrxB.js";const $=["pdf","doc","docx","odt","rtf"],E=10*1024*1024,S=a=>(a.split(".").pop()||"").toLowerCase();function W(a,s){return a?$.includes(S(a.name))?a.size===0?"Plik jest pusty":a.size>s?`Plik jest za duży — maksymalnie ${C(s)}`:null:"Dozwolone formaty: PDF, DOC, DOCX, ODT, RTF":"Nie wybrano pliku"}function M(a,s){const r=URL.createObjectURL(a),c=document.createElement("a");c.href=r,c.download=s,document.body.appendChild(c),c.click(),c.remove(),setTimeout(()=>URL.revokeObjectURL(r),1e3)}function X(){const[a,s]=o.useState(null),[r,c]=o.useState(E),[d,f]=o.useState(!0),[n,x]=o.useState(null),[g,m]=o.useState(!1),[v,w]=o.useState(null),[j,u]=o.useState(!1),[i,l]=o.useState(!1),p=o.useRef(null),y=o.useCallback(async()=>{f(!0);try{const t=await P.info();s(t.data),t.data.maxBytes&&c(t.data.maxBytes)}catch(t){t.status===404?(s(null),t.response?.data?.maxBytes&&c(t.response.data.maxBytes)):w(t.message??"Nie udało się pobrać informacji o CV")}finally{f(!1)}},[]);o.useEffect(()=>{y()},[y]);const D=async t=>{const h=W(t,r);if(w(h),!h){x(0);try{const N=await P.upload(t,x);s(N.data),u(!1),b.success(a?"CV zostało zastąpione nowym plikiem":"CV zostało zapisane")}catch(N){w(N.message??"Nie udało się wysłać pliku")}finally{x(null),p.current&&(p.current.value="")}}},O=t=>{t.preventDefault(),m(!1),n==null&&D(t.dataTransfer.files?.[0])},U=async t=>{l(!0);try{const h=await P.download(),N=new Blob([h.data],{type:a?.contentType||h.data.type});if(t&&a?.contentType==="application/pdf"){const L=URL.createObjectURL(N);window.open(L,"_blank","noopener"),setTimeout(()=>URL.revokeObjectURL(L),6e4)}else M(N,a?.fileName??"CV")}catch(h){b.error(h.status===404?"Plik CV nie istnieje na serwerze":"Nie udało się pobrać CV"),h.status===404&&y()}finally{l(!1)}},A=async()=>{l(!0);try{await P.remove(),s(null),u(!1),b.success("CV zostało usunięte")}catch(t){b.error(t.message??"Nie udało się usunąć CV")}finally{l(!1)}},k=n!=null;return e.jsxs("section",{className:"pf-section",children:[e.jsxs("div",{className:"pf-section-head",children:[e.jsx("span",{className:"pf-section-icon",children:"⎙"}),e.jsxs("div",{children:[e.jsx("h2",{className:"pf-section-title",children:"Moje CV"}),e.jsxs("p",{className:"pf-section-sub",children:["Jeden plik PDF, DOC, DOCX, ODT lub RTF (do ",C(r),") — nowy plik zastępuje poprzedni"]})]})]}),e.jsxs("div",{className:"pf-section-body",children:[d?e.jsx("div",{className:"cv-skel"}):a&&!k?e.jsxs("div",{className:"cv-file animate-fade-in",children:[e.jsx("span",{className:`cv-file-icon cv-file-icon--${S(a.fileName)}`,children:S(a.fileName).toUpperCase()}),e.jsxs("div",{className:"cv-file-info",children:[e.jsx("p",{className:"cv-file-name",title:a.fileName,children:a.fileName}),e.jsxs("p",{className:"cv-file-meta",children:[C(a.size)," · wgrano ",R(a.uploadedAt)]})]}),j?e.jsxs("div",{className:"cv-confirm",children:[e.jsx("span",{children:"Usunąć CV?"}),e.jsx("button",{className:"pf-btn pf-btn--danger",onClick:A,disabled:i,children:"Tak, usuń"}),e.jsx("button",{className:"pf-btn pf-btn--ghost",onClick:()=>u(!1),children:"Nie"})]}):e.jsxs("div",{className:"cv-file-actions",children:[a.contentType==="application/pdf"&&e.jsx("button",{className:"pf-btn pf-btn--ghost",onClick:()=>U(!0),disabled:i,children:"◉ Podgląd"}),e.jsx("button",{className:"pf-btn pf-btn--ghost",onClick:()=>U(!1),disabled:i,children:"↓ Pobierz"}),e.jsx("button",{className:"pf-btn pf-btn--ghost",onClick:()=>p.current?.click(),disabled:i,children:"⇄ Zamień"}),e.jsx("button",{className:"pf-btn pf-btn--danger",onClick:()=>u(!0),disabled:i,children:"✕ Usuń"})]})]}):e.jsx("div",{className:`cv-drop${g?" cv-drop--over":""}${k?" cv-drop--busy":""}`,role:"button",tabIndex:0,onClick:()=>!k&&p.current?.click(),onKeyDown:t=>(t.key==="Enter"||t.key===" ")&&!k&&p.current?.click(),onDragOver:t=>{t.preventDefault(),m(!0)},onDragLeave:()=>m(!1),onDrop:O,children:k?e.jsxs(e.Fragment,{children:[e.jsx("span",{className:"cv-drop-icon",children:"⇡"}),e.jsxs("p",{className:"cv-drop-title",children:["Wysyłanie… ",n!=null?`${n}%`:""]}),e.jsx("div",{className:"cv-progress",children:e.jsx("div",{className:"cv-progress-fill",style:{width:`${n??0}%`}})})]}):e.jsxs(e.Fragment,{children:[e.jsx("span",{className:"cv-drop-icon",children:"⎙"}),e.jsx("p",{className:"cv-drop-title",children:"Przeciągnij tutaj plik CV"}),e.jsxs("p",{className:"cv-drop-sub",children:["albo ",e.jsx("span",{className:"cv-drop-link",children:"wybierz z dysku"})," · PDF, DOC, DOCX, ODT, RTF"]})]})}),e.jsx("input",{ref:p,type:"file",hidden:!0,accept:".pdf,.doc,.docx,.odt,.rtf,application/pdf",onChange:t=>D(t.target.files?.[0])}),v&&e.jsxs("p",{className:"pf-err cv-err",children:["✕ ",v]})]}),e.jsx("style",{children:`
        .cv-skel {
          height: 76px; border-radius: var(--radius-md);
          background: linear-gradient(90deg, var(--bg-2) 25%, var(--bg-3) 50%, var(--bg-2) 75%);
          background-size: 200% 100%; animation: shimmer 1.5s ease infinite;
        }
        .cv-drop {
          display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 6px;
          padding: 28px 16px; border-radius: var(--radius-lg); cursor: pointer; text-align: center;
          border: 2px dashed var(--border-0); background: var(--bg-2);
          transition: border-color .15s, background .15s, transform .15s;
        }
        .cv-drop:hover, .cv-drop:focus-visible { border-color: var(--accent); background: var(--accent-glow); outline: none; }
        .cv-drop--over { border-color: var(--accent); background: var(--accent-glow-strong); transform: scale(1.01); }
        .cv-drop--busy { cursor: progress; }
        .cv-drop-icon { font-size: 1.8rem; color: var(--accent); line-height: 1; }
        .cv-drop-title { font-weight: 700; color: var(--text-0); font-size: 0.9rem; }
        .cv-drop-sub { font-family: var(--font-mono); font-size: 0.72rem; color: var(--text-2); }
        .cv-drop-link { color: var(--accent); text-decoration: underline; }
        .cv-progress { width: min(320px, 100%); height: 6px; border-radius: 3px; background: var(--bg-4); overflow: hidden; }
        .cv-progress-fill { height: 100%; background: var(--accent); transition: width .2s ease; }

        .cv-file {
          display: flex; align-items: center; gap: 14px; flex-wrap: wrap;
          padding: 14px 16px; border-radius: var(--radius-lg);
          background: var(--bg-2); border: 1px solid var(--border-1);
        }
        .cv-file-icon {
          width: 46px; height: 54px; flex-shrink: 0; border-radius: 6px;
          display: flex; align-items: flex-end; justify-content: center; padding-bottom: 7px;
          font-family: var(--font-mono); font-size: 0.6rem; font-weight: 800; letter-spacing: .04em;
          color: #fff; background: var(--accent);
          clip-path: polygon(0 0, 72% 0, 100% 22%, 100% 100%, 0 100%);
        }
        .cv-file-icon--pdf { background: #e5484d; }
        .cv-file-icon--doc, .cv-file-icon--docx { background: #3b82f6; }
        .cv-file-icon--odt { background: #0ea5e9; }
        .cv-file-info { flex: 1; min-width: 160px; }
        .cv-file-name {
          font-weight: 700; color: var(--text-0); font-size: 0.9rem;
          overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
        }
        .cv-file-meta { font-family: var(--font-mono); font-size: 0.7rem; color: var(--text-2); margin-top: 3px; }
        .cv-file-actions, .cv-confirm { display: flex; gap: 8px; flex-wrap: wrap; align-items: center; }
        .cv-confirm span { font-family: var(--font-mono); font-size: 0.74rem; color: var(--red); }
        .cv-err { margin-top: 10px; display: block; }
      `})]})}function q(a){const s={};return a.username.trim()?a.username.length<3&&(s.username="Min. 3 znaki"):s.username="Pole wymagane",a.email&&!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(a.email)&&(s.email="Nieprawidłowy adres e-mail"),s}function H(a){const s={};return a.currentPassword||(s.currentPassword="Pole wymagane"),a.newPassword?a.newPassword.length<6&&(s.newPassword="Min. 6 znaków"):s.newPassword="Pole wymagane",a.confirmPassword?a.newPassword!==a.confirmPassword&&(s.confirmPassword="Hasła nie są zgodne"):s.confirmPassword="Pole wymagane",s}function T(a){if(!a)return null;let s=0;return a.length>=8&&s++,/[A-Z]/.test(a)&&s++,/[0-9]/.test(a)&&s++,/[^A-Za-z0-9]/.test(a)&&s++,["Bardzo słabe","Słabe","Średnie","Silne","Bardzo silne"][s]}function I(a){const s=T(a);return{"Bardzo słabe":"var(--red)",Słabe:"var(--red)",Średnie:"var(--yellow)",Silne:"var(--green)","Bardzo silne":"var(--green)"}[s]??"var(--text-3)"}function J(a){if(!a)return"0%";let s=0;return a.length>=8&&s++,/[A-Z]/.test(a)&&s++,/[0-9]/.test(a)&&s++,/[^A-Za-z0-9]/.test(a)&&s++,`${s/4*100}%`}function _({user:a}){const s=a?.username?a.username.slice(0,2).toUpperCase():"??";return e.jsxs("div",{className:"pf-avatar-section",children:[e.jsx("div",{className:"pf-avatar",children:s}),e.jsxs("div",{className:"pf-avatar-info",children:[e.jsx("p",{className:"pf-avatar-name",children:a?.username??"—"}),e.jsx("p",{className:"pf-avatar-email",children:a?.email??"brak adresu e-mail"}),a?.id&&e.jsxs("p",{className:"pf-avatar-id",children:["ID: ",a.id]})]})]})}function G({user:a,onUpdated:s}){const[r,c]=o.useState({username:a?.username??"",email:a?.email??""}),[d,f]=o.useState({}),[n,x]=o.useState(!1),[g,m]=o.useState(!1),v=u=>{const{name:i,value:l}=u.target;c(p=>({...p,[i]:l})),m(!1),d[i]&&f(p=>({...p,[i]:void 0}))},w=async u=>{u.preventDefault();const i=q(r);if(Object.keys(i).length){f(i);return}x(!0);try{await F.updateProfile({username:r.username.trim(),email:r.email.trim()||void 0}),s({username:r.username.trim(),email:r.email.trim()||null}),b.success("Profil zaktualizowany"),m(!0)}catch(l){b.error(l?.message??"Błąd aktualizacji profilu")}finally{x(!1)}},j=r.username!==(a?.username??"")||r.email!==(a?.email??"");return e.jsxs("section",{className:"pf-section",children:[e.jsxs("div",{className:"pf-section-head",children:[e.jsx("span",{className:"pf-section-icon",children:"⊙"}),e.jsxs("div",{children:[e.jsx("h2",{className:"pf-section-title",children:"Dane profilu"}),e.jsx("p",{className:"pf-section-sub",children:"Zmień nazwę użytkownika lub adres e-mail"})]})]}),e.jsxs("form",{onSubmit:w,className:"pf-section-body",noValidate:!0,children:[e.jsxs("div",{className:"pf-field",children:[e.jsxs("label",{className:"pf-label",htmlFor:"pf-username",children:["Nazwa użytkownika ",e.jsx("span",{className:"pf-required",children:"*"})]}),e.jsx("input",{id:"pf-username",name:"username",className:`pf-input${d.username?" pf-input--err":""}`,value:r.username,onChange:v,disabled:n,autoComplete:"username"}),d.username&&e.jsx("span",{className:"pf-err",children:d.username})]}),e.jsxs("div",{className:"pf-field",children:[e.jsxs("label",{className:"pf-label",htmlFor:"pf-email",children:["Adres e-mail ",e.jsx("span",{className:"pf-optional",children:"(opcjonalnie)"})]}),e.jsx("input",{id:"pf-email",name:"email",type:"email",className:`pf-input${d.email?" pf-input--err":""}`,value:r.email,onChange:v,disabled:n,autoComplete:"email",placeholder:"np. jan@example.com"}),d.email&&e.jsx("span",{className:"pf-err",children:d.email})]}),e.jsxs("div",{className:"pf-foot",children:[g&&!j&&e.jsx("span",{className:"pf-saved",children:"✓ Zapisano"}),e.jsx("button",{type:"submit",className:"pf-btn pf-btn--primary",disabled:n||!j,children:n?e.jsxs(e.Fragment,{children:[e.jsx("span",{className:"pf-spin"}),"Zapisywanie…"]}):"Zapisz zmiany"})]})]})]})}function K(){const[a,s]=o.useState({currentPassword:"",newPassword:"",confirmPassword:""}),[r,c]=o.useState({}),[d,f]=o.useState(!1),[n,x]=o.useState({current:!1,newPw:!1,confirm:!1}),g=i=>{const{name:l,value:p}=i.target;s(y=>({...y,[l]:p})),r[l]&&c(y=>({...y,[l]:void 0}))},m=i=>x(l=>({...l,[i]:!l[i]})),v=async i=>{i.preventDefault();const l=H(a);if(Object.keys(l).length){c(l);return}f(!0);try{await F.changePassword({currentPassword:a.currentPassword,newPassword:a.newPassword}),b.success("Hasło zostało zmienione"),s({currentPassword:"",newPassword:"",confirmPassword:""}),c({})}catch(p){b.error(p?.message??"Błąd zmiany hasła"),(p?.status===400||p?.status===401)&&c({currentPassword:"Nieprawidłowe aktualne hasło"})}finally{f(!1)}},w=T(a.newPassword),j=I(a.newPassword),u=J(a.newPassword);return e.jsxs("section",{className:"pf-section",children:[e.jsxs("div",{className:"pf-section-head",children:[e.jsx("span",{className:"pf-section-icon",children:"◈"}),e.jsxs("div",{children:[e.jsx("h2",{className:"pf-section-title",children:"Zmiana hasła"}),e.jsx("p",{className:"pf-section-sub",children:"Ustaw nowe hasło do konta"})]})]}),e.jsxs("form",{onSubmit:v,className:"pf-section-body",noValidate:!0,children:[e.jsxs("div",{className:"pf-field",children:[e.jsx("label",{className:"pf-label",htmlFor:"pf-cur-pw",children:"Aktualne hasło"}),e.jsxs("div",{className:"pf-input-wrap",children:[e.jsx("input",{id:"pf-cur-pw",name:"currentPassword",type:n.current?"text":"password",className:`pf-input pf-input--pw${r.currentPassword?" pf-input--err":""}`,value:a.currentPassword,onChange:g,disabled:d,autoComplete:"current-password",placeholder:"••••••••"}),e.jsx("button",{type:"button",className:"pf-eye",onClick:()=>m("current"),"aria-label":n.current?"Ukryj":"Pokaż",children:n.current?"◎":"●"})]}),r.currentPassword&&e.jsx("span",{className:"pf-err",children:r.currentPassword})]}),e.jsxs("div",{className:"pf-field",children:[e.jsx("label",{className:"pf-label",htmlFor:"pf-new-pw",children:"Nowe hasło"}),e.jsxs("div",{className:"pf-input-wrap",children:[e.jsx("input",{id:"pf-new-pw",name:"newPassword",type:n.newPw?"text":"password",className:`pf-input pf-input--pw${r.newPassword?" pf-input--err":""}`,value:a.newPassword,onChange:g,disabled:d,autoComplete:"new-password",placeholder:"min. 6 znaków"}),e.jsx("button",{type:"button",className:"pf-eye",onClick:()=>m("newPw"),"aria-label":n.newPw?"Ukryj":"Pokaż",children:n.newPw?"◎":"●"})]}),r.newPassword&&e.jsx("span",{className:"pf-err",children:r.newPassword}),a.newPassword&&e.jsxs("div",{className:"pf-strength",children:[e.jsx("div",{className:"pf-strength-track",children:e.jsx("div",{className:"pf-strength-fill",style:{width:u,background:j}})}),e.jsx("span",{className:"pf-strength-label",style:{color:j},children:w})]})]}),e.jsxs("div",{className:"pf-field",children:[e.jsx("label",{className:"pf-label",htmlFor:"pf-conf-pw",children:"Powtórz nowe hasło"}),e.jsxs("div",{className:"pf-input-wrap",children:[e.jsx("input",{id:"pf-conf-pw",name:"confirmPassword",type:n.confirm?"text":"password",className:`pf-input pf-input--pw${r.confirmPassword?" pf-input--err":""}`,value:a.confirmPassword,onChange:g,disabled:d,autoComplete:"new-password",placeholder:"••••••••"}),e.jsx("button",{type:"button",className:"pf-eye",onClick:()=>m("confirm"),"aria-label":n.confirm?"Ukryj":"Pokaż",children:n.confirm?"◎":"●"})]}),r.confirmPassword&&e.jsx("span",{className:"pf-err",children:r.confirmPassword})]}),e.jsx("div",{className:"pf-foot",children:e.jsx("button",{type:"submit",className:"pf-btn pf-btn--primary",disabled:d||!a.currentPassword||!a.newPassword||!a.confirmPassword,children:d?e.jsxs(e.Fragment,{children:[e.jsx("span",{className:"pf-spin"}),"Zapisywanie…"]}):"Zmień hasło"})})]})]})}function Y({onLogout:a}){const[s,r]=o.useState(!1);return e.jsxs("section",{className:"pf-section pf-section--danger",children:[e.jsxs("div",{className:"pf-section-head",children:[e.jsx("span",{className:"pf-section-icon pf-section-icon--danger",children:"⊘"}),e.jsxs("div",{children:[e.jsx("h2",{className:"pf-section-title",children:"Sesja i konto"}),e.jsx("p",{className:"pf-section-sub",children:"Wylogowanie z aplikacji"})]})]}),e.jsx("div",{className:"pf-section-body",children:s?e.jsxs("div",{className:"pf-confirm animate-fade-in",children:[e.jsx("p",{className:"pf-confirm-text",children:"Czy na pewno chcesz się wylogować?"}),e.jsxs("div",{className:"pf-confirm-actions",children:[e.jsx("button",{className:"pf-btn pf-btn--ghost",onClick:()=>r(!1),children:"Anuluj"}),e.jsx("button",{className:"pf-btn pf-btn--danger",onClick:a,children:"⊘ Tak, wyloguj"})]})]}):e.jsxs("div",{className:"pf-danger-row",children:[e.jsxs("div",{children:[e.jsx("p",{className:"pf-danger-title",children:"Wyloguj się"}),e.jsx("p",{className:"pf-danger-desc",children:"Zakończy bieżącą sesję i usunie token JWT z przeglądarki."})]}),e.jsx("button",{className:"pf-btn pf-btn--danger",onClick:()=>r(!0),children:"⊘ Wyloguj"})]})})]})}function re(){V("Profil");const a=B(),s=z(n=>n.user),r=z(n=>n.logout),c=z(n=>n.updateUser),d=n=>{c(n)},f=()=>{r(),a("/login",{replace:!0})};return e.jsxs("div",{className:"profile-page animate-fade-in",children:[e.jsx(Z,{title:"Profil",subtitle:"Ustawienia konta"}),e.jsxs("div",{className:"pf-layout",children:[e.jsx("aside",{className:"pf-sidebar",children:e.jsx(_,{user:s})}),e.jsxs("div",{className:"pf-main",children:[e.jsx(G,{user:s,onUpdated:d}),e.jsx(X,{}),e.jsx(K,{}),e.jsx(Y,{onLogout:f})]})]}),e.jsx(Q,{})]})}function Q(){return e.jsx("style",{children:`
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
    `})}export{re as default};
