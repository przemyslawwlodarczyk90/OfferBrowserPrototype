import{v as W,r as i,w as C,j as a,L as D,t as u,x as z,e as I,y as M}from"./index-DlloBimL.js";import{a as K,p as F,P,e as E,g as J,i as V,u as Z}from"./index-DqRoP1O5.js";import{E as B,P as _}from"./index-CFHmIrxB.js";import{P as q}from"./PortalTabs-TUmy03Fd.js";const Y={NoFluff:"https://nofluffjobs.com/pl/warszawa/Java?criteria=seniority%3Djunior",TheProtocol:"https://theprotocol.it/filtry/java;t/junior,mid;p/lodz;wp",JustJoin:"https://justjoin.it/job-offers/lodz/java?experience-levels=junior,mid"};function H(e,n){const c=e.trim();if(!c)return"Podaj adres wyszukiwania";if(/\s/.test(c))return"Adres nie może zawierać spacji";let o;try{o=new URL(c)}catch{return"To nie jest poprawny adres URL"}return["https:","http:"].includes(o.protocol)?J(c,n)?null:`Adres musi prowadzić do ${F(n).domain}`:"Adres musi zaczynać się od https://"}function X({url:e}){let n=null;try{n=new URL(e)}catch{}if(!n)return a.jsx("span",{className:"su-url-raw",children:e});const c=[...n.searchParams.entries()],o=n.pathname.split("/").filter(m=>m.includes(";"));return a.jsxs("div",{className:"su-url",children:[a.jsxs("span",{className:"su-url-text",children:[a.jsx("span",{className:"su-url-host",children:n.host}),a.jsx("span",{className:"su-url-path",children:decodeURIComponent(n.pathname)}),n.search&&a.jsx("span",{className:"su-url-query",children:decodeURIComponent(n.search)})]}),(c.length>0||o.length>0)&&a.jsxs("span",{className:"su-chips",children:[o.map(m=>a.jsx("span",{className:"su-chip su-chip--path",children:decodeURIComponent(m)},m)),c.map(([m,p])=>a.jsxs("span",{className:"su-chip",children:[a.jsx("b",{children:m}),"=",p]},m+p))]})]})}function O({portalId:e,initial:n,submitLabel:c,onSubmit:o,onCancel:m,busy:p,autoFocus:d}){const[l,f]=i.useState(n?.url??""),[g,x]=i.useState(n?.label??""),[s,y]=i.useState(!1),v=H(l,e),h=s&&l.length>0&&v,k=async w=>{if(w.preventDefault(),y(!0),v)return;await o({url:l.trim(),label:g.trim()||null})&&!n&&(f(""),x(""),y(!1))};return a.jsxs("form",{className:"su-form",onSubmit:k,noValidate:!0,children:[a.jsxs("div",{className:"su-form-row",children:[a.jsxs("label",{className:"su-field su-field--url",children:[a.jsx("span",{className:"su-field-label",children:"Adres wyszukiwania (z parametrami)"}),a.jsx("input",{className:`su-input su-input--mono${h?" su-input--err":""}`,value:l,autoFocus:d,onChange:w=>f(w.target.value),onBlur:()=>y(!0),placeholder:Y[e],spellCheck:!1})]}),a.jsxs("label",{className:"su-field su-field--label",children:[a.jsxs("span",{className:"su-field-label",children:["Opis ",a.jsx("span",{className:"su-opt",children:"(opcjonalnie)"})]}),a.jsx("input",{className:"su-input",value:g,maxLength:120,onChange:w=>x(w.target.value),placeholder:"np. Java – Łódź, junior/mid"})]})]}),h&&a.jsx("p",{className:"su-err",children:v}),a.jsxs("div",{className:"su-form-actions",children:[m&&a.jsx("button",{type:"button",className:"su-btn su-btn--ghost",onClick:m,children:"Anuluj"}),!v&&a.jsx("a",{className:"su-btn su-btn--ghost",href:l.trim(),target:"_blank",rel:"noopener noreferrer",children:"↗ Sprawdź w serwisie"}),a.jsx("button",{type:"submit",className:"su-btn su-btn--primary",disabled:p||s&&!!v,children:p?a.jsxs(a.Fragment,{children:[a.jsx("span",{className:"su-spin"}),"Zapisywanie…"]}):c})]})]})}function G({item:e,index:n,portalId:c,isLast:o,onSave:m,onDelete:p}){const[d,l]=i.useState(!1),[f,g]=i.useState(!1),[x,s]=i.useState(!1),y=async h=>{s(!0);const k=await m(e.id,h);return s(!1),k&&l(!1),k},v=async()=>{s(!0),await p(e.id),s(!1),g(!1)};return a.jsxs("li",{className:`su-row${d?" su-row--editing":""} animate-fade-in`,children:[a.jsx("span",{className:"su-idx",children:n+1}),a.jsx("div",{className:"su-row-main",children:d?a.jsx(O,{portalId:c,initial:e,submitLabel:"Zapisz zmiany",busy:x,onSubmit:y,onCancel:()=>l(!1),autoFocus:!0}):a.jsxs(a.Fragment,{children:[a.jsxs("div",{className:"su-row-head",children:[a.jsx("span",{className:`su-label${e.label?"":" su-label--empty"}`,children:e.label||"Bez opisu"}),e.isDefault&&a.jsx("span",{className:"su-badge",children:"domyślny"})]}),a.jsx(X,{url:e.url}),a.jsxs("p",{className:"su-meta",children:[e.createdBy?`dodał ${e.createdBy}`:"lista startowa"," · ",E(e.createdAt),e.updatedAt&&a.jsxs(a.Fragment,{children:[" · zmieniono ",E(e.updatedAt)]})]})]})}),!d&&a.jsx("div",{className:"su-row-actions",children:f?a.jsxs("div",{className:"su-confirm",children:[a.jsx("span",{children:"Usunąć?"}),a.jsx("button",{className:"su-btn su-btn--danger su-btn--sm",onClick:v,disabled:x,children:x?a.jsx("span",{className:"su-spin"}):"Tak, usuń"}),a.jsx("button",{className:"su-btn su-btn--ghost su-btn--sm",onClick:()=>g(!1),children:"Nie"})]}):a.jsxs(a.Fragment,{children:[a.jsx("a",{className:"su-icon-btn",href:e.url,target:"_blank",rel:"noopener noreferrer",title:"Otwórz wyszukiwanie w serwisie",children:"↗"}),a.jsx("button",{className:"su-icon-btn",title:"Edytuj adres lub opis",onClick:()=>l(!0),children:"✎"}),a.jsx("button",{className:"su-icon-btn su-icon-btn--danger",title:o?"To ostatni adres portalu — najpierw dodaj nowy":"Usuń adres",disabled:o,onClick:()=>g(!0),children:"✕"})]})})]})}function Q(){const[e,n]=K(),c=W(),o=i.useRef(null),[m,p]=i.useState({}),[d,l]=i.useState(!0),[f,g]=i.useState(null),[x,s]=i.useState(!1),[y,v]=i.useState(!1),h=F(e),k=i.useCallback(async()=>{l(!0);try{const t=await Promise.all(P.map(b=>C.list(b.id)));p(Object.fromEntries(P.map((b,N)=>[b.id,t[N].data]))),g(null)}catch(t){g(t)}finally{l(!1)}},[]);i.useEffect(()=>{k()},[k]),i.useEffect(()=>{!d&&c.hash==="#scraper-urls"&&o.current?.scrollIntoView({behavior:"smooth",block:"start"})},[d,c.hash]);const w=(t,b)=>p(N=>({...N,[t]:b})),j=i.useMemo(()=>m[e]??[],[m,e]),U=Object.fromEntries(P.map(t=>[t.id,m[t.id]?.length])),R=async t=>{s(!0);try{const b=await C.add(e,t);return w(e,[...j,b.data]),u.success(`Dodano adres — ${h.label} użyje go przy następnym pobraniu`),!0}catch(b){return u.error(b.message??"Nie udało się dodać adresu"),!1}finally{s(!1)}},A=async(t,b)=>{try{const N=await C.update(t,b);return w(e,j.map(T=>T.id===t?N.data:T)),u.success("Zapisano zmiany adresu"),!0}catch(N){return u.error(N.message??"Nie udało się zapisać"),!1}},S=async t=>{try{await C.remove(t),w(e,j.filter(b=>b.id!==t)),u.success("Usunięto adres")}catch(b){u.error(b.message??"Nie udało się usunąć")}},r=async()=>{v(!0);try{const t=await C.restoreDefaults(e);w(e,t.data.urls),t.data.added>0?u.success(`Przywrócono ${t.data.added} domyślnych adresów`):u.info("Wszystkie domyślne adresy są już na liście")}catch(t){u.error(t.message??"Nie udało się przywrócić")}finally{v(!1)}};return a.jsxs("section",{className:"su-page",id:"scraper-urls",ref:o,children:[a.jsxs("div",{className:"su-head",children:[a.jsx("h2",{className:"adm-section-title--standalone su-title",children:"Adresy wyszukiwania scraperów"}),a.jsx("p",{className:"su-sub",children:"Listy wyszukiwania, po których chodzą scrapery — osobno dla każdego portalu"})]}),a.jsx(q,{value:e,onChange:n,counts:U}),a.jsxs("div",{className:"su-info",children:[a.jsx("span",{className:"su-info-icon",children:"ⓘ"}),a.jsxs("p",{children:["Scraper ",a.jsx("strong",{children:h.label})," przechodzi po kolei przez poniższe adresy (strona po stronie) i pobiera nowe oferty. Zmiany zapisują się w bazie i działają ",a.jsx("strong",{children:"od następnego pobrania"})," — bez restartu. Żeby zmienić parametry wyszukiwania, ustaw filtry w serwisie, skopiuj adres z paska przeglądarki i wklej go tutaj (albo edytuj istniejący ✎)."," ",a.jsx(D,{to:`/import?portal=${h.slug}`,className:"su-link",children:"Uruchom scraper →"})]})]}),a.jsxs("section",{className:"su-card",children:[a.jsx("header",{className:"su-card-head",children:a.jsxs("h2",{className:"su-card-title",children:["Dodaj adres — ",h.label]})}),a.jsx("div",{className:"su-card-body",children:a.jsx(O,{portalId:e,submitLabel:"+ Dodaj adres",busy:x,onSubmit:R},e)})]}),a.jsxs("section",{className:"su-card",children:[a.jsxs("header",{className:"su-card-head",children:[a.jsxs("h2",{className:"su-card-title",children:["Lista ",h.label,!d&&a.jsx("span",{className:"su-count",children:j.length})]}),a.jsx("button",{className:"su-btn su-btn--ghost su-btn--sm",onClick:r,disabled:y||d,title:"Dodaje brakujące adresy z listy startowej; Twoje adresy zostają",children:y?a.jsxs(a.Fragment,{children:[a.jsx("span",{className:"su-spin"}),"Przywracanie…"]}):"↺ Przywróć domyślne"})]}),d&&a.jsx("div",{className:"su-skel-list",children:[0,1,2].map(t=>a.jsx("div",{className:"su-skel"},t))}),!d&&f&&a.jsx(B,{icon:"✕",title:"Nie udało się wczytać adresów",description:f.message,action:a.jsx("button",{className:"su-btn su-btn--primary",onClick:k,children:"Spróbuj ponownie"})}),!d&&!f&&j.length===0&&a.jsx(B,{icon:"⛓",title:"Brak adresów",description:`Scraper ${h.label} nie ma czego przeszukiwać — dodaj adres albo przywróć domyślne.`}),!d&&!f&&j.length>0&&a.jsx("ol",{className:"su-list",children:j.map((t,b)=>a.jsx(G,{item:t,index:b,portalId:e,isLast:j.length===1,onSave:A,onDelete:S},t.id))})]}),a.jsx(aa,{})]})}function aa(){return a.jsx("style",{children:`
      .su-page { display: flex; flex-direction: column; gap: 16px; margin-bottom: 24px; scroll-margin-top: 16px; }
      .su-head { display: flex; flex-direction: column; gap: 2px; }
      .su-title { margin-bottom: 0 !important; }
      .su-sub { font-size: 0.78rem; color: var(--text-2); }
      .su-page .portal-tabs { margin-bottom: 0; align-self: flex-start; }

      .su-info {
        display: flex; gap: 12px; align-items: flex-start;
        padding: 14px 16px; border-radius: var(--radius-lg);
        background: var(--accent-glow); border: 1px solid var(--border-0);
        font-size: 0.8rem; color: var(--text-1); line-height: 1.65;
      }
      .su-info strong { color: var(--text-0); }
      .su-info-icon { color: var(--accent); font-size: 1.05rem; line-height: 1.4; }
      .su-link { color: var(--accent); font-weight: 600; white-space: nowrap; }
      .su-link:hover { text-decoration: underline; }

      .su-card { background: var(--bg-1); border: 1px solid var(--border-1); border-radius: var(--radius-lg); overflow: hidden; }
      .su-card-head {
        display: flex; align-items: center; justify-content: space-between; gap: 12px;
        padding: 14px 18px; border-bottom: 1px solid var(--border-1);
      }
      .su-card-title {
        display: flex; align-items: center; gap: 8px;
        font-size: 0.86rem; font-weight: 700; color: var(--text-1);
        text-transform: uppercase; letter-spacing: .06em;
      }
      .su-count {
        font-family: var(--font-mono); font-size: 0.66rem; padding: 1px 7px; border-radius: 100px;
        background: var(--bg-3); color: var(--text-1); border: 1px solid var(--border-1);
      }
      .su-card-body { padding: 16px 18px; }

      /* ── Formularz ── */
      .su-form { display: flex; flex-direction: column; gap: 10px; }
      .su-form-row { display: grid; grid-template-columns: minmax(0, 2.2fr) minmax(0, 1fr); gap: 10px; }
      @media (max-width: 760px) { .su-form-row { grid-template-columns: 1fr; } }
      .su-field { display: flex; flex-direction: column; gap: 5px; min-width: 0; }
      .su-field-label {
        font-family: var(--font-mono); font-size: 0.66rem; font-weight: 700;
        color: var(--text-2); text-transform: uppercase; letter-spacing: .07em;
      }
      .su-opt { text-transform: none; letter-spacing: 0; font-weight: 400; color: var(--text-3); }
      .su-input {
        width: 100%; padding: 9px 12px; border-radius: var(--radius-md);
        background: var(--bg-2); border: 1px solid var(--border-1); color: var(--text-0);
        font-size: 0.84rem; outline: none; transition: border-color .15s, box-shadow .15s;
      }
      .su-input--mono { font-family: var(--font-mono); font-size: 0.78rem; }
      .su-input::placeholder { color: var(--text-3); }
      .su-input:focus { border-color: var(--accent); box-shadow: 0 0 0 3px var(--accent-glow); }
      .su-input--err { border-color: var(--red); }
      .su-err { font-family: var(--font-mono); font-size: 0.72rem; color: var(--red); }
      .su-form-actions { display: flex; justify-content: flex-end; gap: 8px; flex-wrap: wrap; }

      /* ── Przyciski ── */
      .su-btn {
        display: inline-flex; align-items: center; gap: 7px; white-space: nowrap;
        padding: 8px 16px; border-radius: var(--radius-md); border: 1px solid transparent;
        font-family: var(--font-mono); font-size: 0.76rem; font-weight: 700; cursor: pointer;
        transition: background .15s, border-color .15s, box-shadow .15s, opacity .15s;
      }
      .su-btn:disabled { opacity: .5; cursor: not-allowed; }
      .su-btn--sm { padding: 5px 11px; font-size: 0.7rem; }
      .su-btn--primary { background: var(--accent); color: #fff; border-color: var(--accent); }
      .su-btn--primary:hover:not(:disabled) { background: var(--accent-dim); box-shadow: var(--shadow-accent); }
      .su-btn--ghost { background: var(--bg-2); color: var(--text-1); border-color: var(--border-1); }
      .su-btn--ghost:hover:not(:disabled) { color: var(--text-0); border-color: var(--border-0); }
      .su-btn--danger { background: rgba(244,63,94,.12); color: var(--red); border-color: rgba(244,63,94,.4); }
      .su-btn--danger:hover:not(:disabled) { background: rgba(244,63,94,.2); }
      .su-spin {
        width: 11px; height: 11px; border: 2px solid transparent; border-top-color: currentColor;
        border-radius: 50%; animation: spin .6s linear infinite;
      }

      /* ── Lista ── */
      .su-list { list-style: none; }
      .su-row {
        display: flex; gap: 14px; align-items: flex-start;
        padding: 14px 18px; border-bottom: 1px solid var(--border-2);
        transition: background .15s;
      }
      .su-row:last-child { border-bottom: none; }
      .su-row:hover { background: var(--bg-2); }
      .su-row--editing { background: var(--bg-2); }
      .su-idx {
        flex-shrink: 0; width: 26px; height: 26px; border-radius: 50%;
        display: flex; align-items: center; justify-content: center;
        font-family: var(--font-mono); font-size: 0.7rem; font-weight: 700;
        background: var(--accent-glow); color: var(--accent); border: 1px solid var(--border-0);
      }
      .su-row-main { flex: 1; min-width: 0; display: flex; flex-direction: column; gap: 6px; }
      .su-row-head { display: flex; align-items: center; gap: 8px; flex-wrap: wrap; }
      .su-label { font-size: 0.88rem; font-weight: 700; color: var(--text-0); }
      .su-label--empty { color: var(--text-3); font-weight: 400; font-style: italic; }
      .su-badge {
        font-family: var(--font-mono); font-size: 0.6rem; font-weight: 700; text-transform: uppercase;
        letter-spacing: .07em; padding: 2px 7px; border-radius: 100px;
        background: rgba(16,185,129,.1); color: var(--green); border: 1px solid rgba(16,185,129,.3);
      }
      .su-url { display: flex; flex-direction: column; gap: 6px; min-width: 0; }
      .su-url-text, .su-url-raw {
        font-family: var(--font-mono); font-size: 0.74rem; line-height: 1.5;
        overflow-wrap: anywhere; word-break: break-all;
      }
      .su-url-host { color: var(--text-2); }
      .su-url-path { color: var(--text-0); }
      .su-url-query { color: var(--cyan); }
      .su-chips { display: flex; flex-wrap: wrap; gap: 5px; }
      .su-chip {
        font-family: var(--font-mono); font-size: 0.66rem; color: var(--text-1);
        padding: 2px 8px; border-radius: var(--radius-sm);
        background: var(--cyan-glow); border: 1px solid rgba(56,189,248,.25);
      }
      .su-chip b { color: var(--cyan); font-weight: 700; }
      .su-chip--path { background: var(--accent-glow); border-color: var(--border-0); }
      .su-meta { font-family: var(--font-mono); font-size: 0.66rem; color: var(--text-3); }

      .su-row-actions { display: flex; gap: 6px; flex-shrink: 0; align-items: center; }
      .su-icon-btn {
        width: 30px; height: 30px; border-radius: var(--radius-md);
        display: inline-flex; align-items: center; justify-content: center;
        background: var(--bg-2); border: 1px solid var(--border-1); color: var(--text-1);
        font-size: 0.85rem; cursor: pointer; transition: color .15s, border-color .15s, background .15s;
      }
      .su-icon-btn:hover:not(:disabled) { color: var(--text-0); border-color: var(--border-0); background: var(--bg-3); }
      .su-icon-btn--danger:hover:not(:disabled) { color: var(--red); border-color: rgba(244,63,94,.45); }
      .su-icon-btn:disabled { opacity: .35; cursor: not-allowed; }
      .su-confirm {
        display: flex; align-items: center; gap: 6px;
        font-family: var(--font-mono); font-size: 0.72rem; color: var(--red);
      }
      @media (max-width: 640px) {
        .su-row { flex-wrap: wrap; }
        .su-row-actions { width: 100%; justify-content: flex-end; }
      }

      .su-skel-list { padding: 14px 18px; display: flex; flex-direction: column; gap: 10px; }
      .su-skel {
        height: 64px; border-radius: var(--radius-md);
        background: linear-gradient(90deg, var(--bg-2) 25%, var(--bg-3) 50%, var(--bg-2) 75%);
        background-size: 200% 100%; animation: shimmer 1.5s ease infinite;
      }
    `})}const ea={manual:"ręczna","pre-restore":"przed przywróceniem"},ra=e=>e.fileName.startsWith("uploaded_")?"wgrana":ea[e.kind]??e.kind??"—",L="PRZYWRÓĆ";function sa(e,n){const c=URL.createObjectURL(e),o=document.createElement("a");o.href=c,o.download=n,document.body.appendChild(o),o.click(),o.remove(),setTimeout(()=>URL.revokeObjectURL(c),1e3)}function ta({target:e,onConfirm:n,onClose:c,busy:o}){const[m,p]=i.useState(""),d=m.trim().toUpperCase()===L;return a.jsx("div",{className:"bk-backdrop",onClick:()=>!o&&c(),children:a.jsxs("div",{className:"bk-modal animate-fade-in",role:"dialog","aria-modal":"true",onClick:l=>l.stopPropagation(),children:[a.jsx("div",{className:"bk-modal-icon",children:"⚠"}),a.jsx("h3",{className:"bk-modal-title",children:"Przywrócić bazę danych?"}),a.jsxs("p",{className:"bk-modal-desc",children:["Dane wszystkich tabel z kopii ",a.jsx("code",{children:e.label})," zastąpią obecne (użytkownicy, oferty, statusy, notatki, flagi, adresy scraperów). Przed przywróceniem automatycznie zapisze się kopia obecnego stanu — w razie pomyłki możesz do niej wrócić."]}),a.jsxs("p",{className:"bk-modal-hint",children:["Wpisz ",a.jsx("code",{children:L}),", aby kontynuować:"]}),a.jsx("input",{className:"bk-input",autoFocus:!0,value:m,placeholder:L,onChange:l=>p(l.target.value),onKeyDown:l=>l.key==="Enter"&&d&&!o&&n()}),a.jsxs("div",{className:"bk-modal-foot",children:[a.jsx("button",{className:"bk-btn bk-btn--ghost",onClick:c,disabled:o,children:"Anuluj"}),a.jsx("button",{className:"bk-btn bk-btn--danger",onClick:n,disabled:!d||o,children:o?a.jsxs(a.Fragment,{children:[a.jsx("span",{className:"bk-spin"}),"Przywracanie…"]}):"↺ Przywróć bazę"})]})]})})}function na(){const[e,n]=i.useState(null),[c,o]=i.useState(!0),[m,p]=i.useState(!1),[d,l]=i.useState(null),[f,g]=i.useState(!1),[x,s]=i.useState(null),[y,v]=i.useState(null),h=i.useRef(null),k=i.useCallback(async()=>{try{const r=await z.list();n(r.data)}catch(r){u.error(r.message??"Nie udało się wczytać listy kopii")}finally{o(!1)}},[]);i.useEffect(()=>{k()},[k]);const w=async()=>{p(!0);try{const r=await z.create();u.success(`Kopia zapisana: ${r.data.fileName} (${r.data.totalRows} wierszy)`),k()}catch(r){u.error(r.message??"Nie udało się utworzyć kopii")}finally{p(!1)}},j=async r=>{try{const t=await z.download(r);sa(t.data,r)}catch{u.error("Nie udało się pobrać kopii")}},U=async r=>{try{await z.remove(r),v(null),u.success("Usunięto kopię"),k()}catch(t){u.error(t.message??"Nie udało się usunąć kopii")}},R=r=>{if(h.current&&(h.current.value=""),!!r){if(!r.name.toLowerCase().endsWith(".json")){u.error("Wybierz plik .json z kopią bazy");return}l({file:r,label:r.name})}},A=async()=>{g(!0);try{const r=d.file?await z.restoreUpload(d.file):await z.restore(d.fileName);s(r.data),l(null),u.success(`Przywrócono bazę — ${r.data.totalRows} wierszy`),k()}catch(r){u.error(r.message??"Przywracanie nie powiodło się — baza bez zmian")}finally{g(!1)}},S=e?.backups??[];return a.jsxs("section",{className:"bk-wrap",children:[a.jsxs("div",{className:"bk-head",children:[a.jsxs("div",{children:[a.jsx("h2",{className:"adm-section-title--standalone bk-title",children:"Kopia zapasowa bazy danych"}),a.jsxs("p",{className:"bk-sub",children:["Cała baza (wszystkie tabele) w jednym pliku JSON. Katalog: ",a.jsx("code",{children:e?.directory??"…"})," ","(",a.jsx("code",{children:"backup.dir"})," w properties)"]})]}),a.jsxs("div",{className:"bk-actions",children:[a.jsx("button",{className:"bk-btn bk-btn--ghost",onClick:()=>h.current?.click(),disabled:f,children:"⇪ Przywróć z pliku JSON…"}),a.jsx("button",{className:"bk-btn bk-btn--primary",onClick:w,disabled:m,children:m?a.jsxs(a.Fragment,{children:[a.jsx("span",{className:"bk-spin"}),"Tworzenie…"]}):"⤓ Utwórz kopię teraz"}),a.jsx("input",{ref:h,type:"file",accept:".json,application/json",hidden:!0,onChange:r=>R(r.target.files?.[0])})]})]}),x&&a.jsxs("div",{className:"bk-result animate-fade-in",children:[a.jsx("span",{className:"bk-result-icon",children:"✓"}),a.jsxs("div",{className:"bk-result-body",children:[a.jsxs("p",{className:"bk-result-title",children:["Baza przywrócona z ",a.jsx("code",{children:x.fileName})," — ",x.totalRows," wierszy"]}),a.jsx("p",{className:"bk-result-meta",children:Object.entries(x.restoredRows).map(([r,t])=>`${r}: ${t}`).join(" · ")}),a.jsxs("p",{className:"bk-result-meta",children:["Kopia stanu sprzed przywrócenia: ",a.jsx("code",{children:x.safetyBackup}),x.untouchedTables?.length>0&&a.jsxs(a.Fragment,{children:[" · tabele spoza kopii (bez zmian): ",x.untouchedTables.join(", ")]})]})]}),a.jsx("button",{className:"bk-icon-btn",title:"Zamknij",onClick:()=>s(null),children:"✕"})]}),a.jsx("div",{className:"bk-card",children:c?a.jsx("div",{className:"bk-skel"}):S.length===0?a.jsx("p",{className:"bk-empty",children:"Brak kopii — kliknij „Utwórz kopię teraz”."}):a.jsx("div",{className:"bk-table-wrap",children:a.jsxs("table",{className:"bk-table",children:[a.jsx("thead",{children:a.jsxs("tr",{children:[a.jsx("th",{children:"Plik"}),a.jsx("th",{children:"Utworzono"}),a.jsx("th",{children:"Rodzaj"}),a.jsx("th",{className:"bk-num",children:"Wiersze"}),a.jsx("th",{className:"bk-num",children:"Rozmiar"}),a.jsx("th",{className:"bk-right",children:"Akcje"})]})}),a.jsx("tbody",{children:S.map(r=>a.jsxs("tr",{children:[a.jsx("td",{className:"bk-file",title:Object.entries(r.rowCounts).map(([t,b])=>`${t}: ${b}`).join(`
`),children:r.fileName}),a.jsxs("td",{children:[E(r.createdAt),r.createdBy&&a.jsxs("span",{className:"bk-by",children:[" · ",r.createdBy]})]}),a.jsx("td",{children:a.jsx("span",{className:`bk-kind bk-kind--${r.kind==="pre-restore"?"safety":"normal"}`,children:ra(r)})}),a.jsx("td",{className:"bk-num",children:r.totalRows.toLocaleString("pl-PL")}),a.jsx("td",{className:"bk-num",children:V(r.size)}),a.jsx("td",{className:"bk-right",children:y===r.fileName?a.jsxs("span",{className:"bk-confirm",children:["Usunąć?",a.jsx("button",{className:"bk-btn bk-btn--danger bk-btn--sm",onClick:()=>U(r.fileName),children:"Tak"}),a.jsx("button",{className:"bk-btn bk-btn--ghost bk-btn--sm",onClick:()=>v(null),children:"Nie"})]}):a.jsxs("span",{className:"bk-row-actions",children:[a.jsx("button",{className:"bk-btn bk-btn--ghost bk-btn--sm",onClick:()=>l({fileName:r.fileName,label:r.fileName}),children:"↺ Przywróć"}),a.jsx("button",{className:"bk-icon-btn",title:"Pobierz plik",onClick:()=>j(r.fileName),children:"↓"}),a.jsx("button",{className:"bk-icon-btn bk-icon-btn--danger",title:"Usuń kopię",onClick:()=>v(r.fileName),children:"✕"})]})})]},r.fileName))})]})})}),d&&a.jsx(ta,{target:d,busy:f,onConfirm:A,onClose:()=>l(null)}),a.jsx("style",{children:`
        .bk-wrap { display: flex; flex-direction: column; gap: 12px; margin-bottom: 24px; }
        .bk-head { display: flex; justify-content: space-between; align-items: flex-end; gap: 12px; flex-wrap: wrap; }
        .bk-title { margin-bottom: 2px !important; }
        .bk-sub { font-size: 0.78rem; color: var(--text-2); }
        .bk-sub code, .bk-result code, .bk-modal code {
          font-family: var(--font-mono); font-size: 0.72rem; color: var(--text-1);
          background: var(--bg-3); padding: 1px 5px; border-radius: var(--radius-sm); overflow-wrap: anywhere;
        }
        .bk-actions { display: flex; gap: 8px; flex-wrap: wrap; }

        .bk-btn {
          display: inline-flex; align-items: center; gap: 7px; white-space: nowrap;
          padding: 9px 16px; border-radius: var(--radius-md); border: 1px solid transparent;
          font-family: var(--font-mono); font-size: 0.76rem; font-weight: 700; cursor: pointer;
          transition: background .15s, border-color .15s, box-shadow .15s, opacity .15s;
        }
        .bk-btn:disabled { opacity: .5; cursor: not-allowed; }
        .bk-btn--sm { padding: 5px 10px; font-size: 0.7rem; }
        .bk-btn--primary { background: var(--accent); color: #fff; border-color: var(--accent); }
        .bk-btn--primary:hover:not(:disabled) { background: var(--accent-dim); box-shadow: var(--shadow-accent); }
        .bk-btn--ghost { background: var(--bg-2); color: var(--text-1); border-color: var(--border-1); }
        .bk-btn--ghost:hover:not(:disabled) { color: var(--text-0); border-color: var(--border-0); }
        .bk-btn--danger { background: rgba(244,63,94,.12); color: var(--red); border-color: rgba(244,63,94,.4); }
        .bk-btn--danger:hover:not(:disabled) { background: rgba(244,63,94,.2); }
        .bk-spin { width: 11px; height: 11px; border: 2px solid transparent; border-top-color: currentColor; border-radius: 50%; animation: spin .6s linear infinite; }
        .bk-icon-btn {
          width: 28px; height: 28px; border-radius: var(--radius-md); cursor: pointer;
          display: inline-flex; align-items: center; justify-content: center;
          background: var(--bg-2); border: 1px solid var(--border-1); color: var(--text-1); font-size: 0.8rem;
        }
        .bk-icon-btn:hover { color: var(--text-0); border-color: var(--border-0); }
        .bk-icon-btn--danger:hover { color: var(--red); border-color: rgba(244,63,94,.45); }

        .bk-card { background: var(--bg-1); border: 1px solid var(--border-1); border-radius: var(--radius-lg); overflow: hidden; }
        .bk-empty { padding: 28px; text-align: center; color: var(--text-2); font-size: 0.82rem; }
        .bk-skel {
          height: 90px; margin: 14px; border-radius: var(--radius-md);
          background: linear-gradient(90deg, var(--bg-2) 25%, var(--bg-3) 50%, var(--bg-2) 75%);
          background-size: 200% 100%; animation: shimmer 1.5s ease infinite;
        }
        .bk-table-wrap { overflow-x: auto; }
        .bk-table { width: 100%; border-collapse: collapse; font-size: 0.78rem; color: var(--text-1); }
        .bk-table th {
          text-align: left; padding: 10px 14px; background: var(--bg-2); color: var(--text-2);
          font-family: var(--font-mono); font-size: 0.64rem; text-transform: uppercase; letter-spacing: .07em;
          border-bottom: 1px solid var(--border-1); white-space: nowrap;
        }
        .bk-table td { padding: 10px 14px; border-bottom: 1px solid var(--border-2); white-space: nowrap; vertical-align: middle; }
        .bk-table tr:last-child td { border-bottom: none; }
        .bk-table tbody tr:hover { background: var(--bg-2); }
        .bk-file { font-family: var(--font-mono); font-size: 0.72rem; color: var(--text-0); }
        .bk-by { color: var(--text-3); }
        .bk-num { text-align: right; font-family: var(--font-mono); }
        .bk-right { text-align: right; }
        .bk-row-actions, .bk-confirm { display: inline-flex; gap: 6px; align-items: center; }
        .bk-confirm { font-family: var(--font-mono); font-size: 0.72rem; color: var(--red); }
        .bk-kind {
          font-family: var(--font-mono); font-size: 0.62rem; font-weight: 700; text-transform: uppercase;
          letter-spacing: .06em; padding: 2px 8px; border-radius: 100px;
          background: var(--accent-glow); color: var(--accent); border: 1px solid var(--border-0);
        }
        .bk-kind--safety { background: rgba(245,158,11,.1); color: var(--yellow); border-color: rgba(245,158,11,.35); }

        .bk-result {
          display: flex; gap: 12px; align-items: flex-start; padding: 12px 14px; border-radius: var(--radius-lg);
          background: rgba(16,185,129,.07); border: 1px solid rgba(16,185,129,.3);
        }
        .bk-result-icon { color: var(--green); font-weight: 800; }
        .bk-result-body { flex: 1; min-width: 0; display: flex; flex-direction: column; gap: 4px; }
        .bk-result-title { font-size: 0.84rem; font-weight: 700; color: var(--text-0); }
        .bk-result-meta { font-family: var(--font-mono); font-size: 0.68rem; color: var(--text-2); overflow-wrap: anywhere; }

        .bk-backdrop {
          position: fixed; inset: 0; z-index: 300; background: rgba(0,0,0,.65);
          display: flex; align-items: center; justify-content: center; padding: 16px;
        }
        .bk-modal {
          width: min(480px, 100%); background: var(--bg-1); border: 1px solid rgba(244,63,94,.35);
          border-radius: var(--radius-lg); padding: 24px; box-shadow: var(--shadow-lg);
          display: flex; flex-direction: column; gap: 12px;
        }
        .bk-modal-icon { font-size: 1.6rem; color: var(--red); }
        .bk-modal-title { font-size: 1.05rem; font-weight: 800; color: var(--text-0); }
        .bk-modal-desc, .bk-modal-hint { font-size: 0.8rem; color: var(--text-1); line-height: 1.6; }
        .bk-input {
          padding: 9px 12px; border-radius: var(--radius-md); background: var(--bg-2);
          border: 1px solid var(--border-1); color: var(--text-0); font-family: var(--font-mono); outline: none;
        }
        .bk-input:focus { border-color: var(--red); box-shadow: 0 0 0 3px rgba(244,63,94,.15); }
        .bk-modal-foot { display: flex; justify-content: flex-end; gap: 8px; margin-top: 4px; }
      `})]})}const $={ADMIN:{bg:"rgba(245,158,11,0.13)",border:"rgba(245,158,11,0.38)",color:"#f59e0b"},USER:{bg:"rgba(56,189,248,0.10)",border:"rgba(56,189,248,0.30)",color:"#38bdf8"}};function oa({role:e}){const n=$[e]||$.USER;return a.jsx("span",{style:{background:n.bg,border:`1px solid ${n.border}`,color:n.color,fontFamily:"var(--font-mono)",fontSize:"0.65rem",fontWeight:700,letterSpacing:"0.07em",textTransform:"uppercase",padding:"3px 8px",borderRadius:"100px",whiteSpace:"nowrap"},children:e})}function ia({icon:e,title:n,description:c,buttonLabel:o,buttonVariant:m="primary",loading:p,onClick:d,notice:l}){return a.jsxs("div",{className:"adm-action-card",children:[a.jsxs("div",{className:"adm-action-top",children:[a.jsx("span",{className:"adm-action-icon",children:e}),a.jsxs("div",{children:[a.jsx("h3",{className:"adm-action-title",children:n}),a.jsx("p",{className:"adm-action-desc",children:c})]})]}),a.jsx("button",{className:`adm-action-btn adm-action-btn--${m}`,onClick:d,disabled:p,children:p?a.jsxs(a.Fragment,{children:[a.jsx("span",{className:"adm-action-spin"})," Trwa..."]}):o}),l&&a.jsxs("div",{className:"adm-action-notice",children:[a.jsx("span",{className:"adm-action-notice-spin"}),a.jsx("span",{children:l})]})]})}function pa(){Z("Admin");const[e,n]=i.useState([]),[c,o]=i.useState(!0),[m,p]=i.useState(!1);i.useEffect(()=>{I.getUsers().then(s=>n(s.data)).catch(()=>u.error("Błąd pobierania użytkowników")).finally(()=>o(!1))},[]);const d=async()=>{p(!0);try{await M.sendDailyEmails(),u.success("Maile z ofertami zostały wysłane do wszystkich użytkowników")}catch{u.error("Błąd podczas wysyłki maili")}finally{p(!1)}},l=e.length,f=e.filter(s=>s.active).length,g=e.filter(s=>s.role==="ADMIN").length,x=e.reduce((s,y)=>s+y.appliedCount,0);return a.jsxs(a.Fragment,{children:[a.jsx(_,{title:"Panel Administratora",subtitle:"Użytkownicy, adresy wyszukiwania scraperów, kopia zapasowa bazy i akcje administracyjne"}),a.jsxs("div",{className:"adm-overview",children:[a.jsxs("div",{className:"adm-card adm-card--stat",children:[a.jsx("span",{className:"adm-card-val",children:l}),a.jsx("span",{className:"adm-card-lbl",children:"Użytkownicy"})]}),a.jsxs("div",{className:"adm-card adm-card--stat",children:[a.jsx("span",{className:"adm-card-val",style:{color:"var(--green)"},children:f}),a.jsx("span",{className:"adm-card-lbl",children:"Aktywni"})]}),a.jsxs("div",{className:"adm-card adm-card--stat",children:[a.jsx("span",{className:"adm-card-val",style:{color:"var(--yellow)"},children:g}),a.jsx("span",{className:"adm-card-lbl",children:"Administratorzy"})]}),a.jsxs("div",{className:"adm-card adm-card--stat",children:[a.jsx("span",{className:"adm-card-val",style:{color:"var(--accent)"},children:x}),a.jsx("span",{className:"adm-card-lbl",children:"Łączne aplikacje"})]})]}),a.jsxs("div",{className:"adm-actions-section",children:[a.jsx("h2",{className:"adm-section-title adm-section-title--standalone",children:"Akcje administracyjne"}),a.jsx("div",{className:"adm-actions-grid",children:a.jsx(ia,{icon:"◷",title:"Wyślij maile z ofertami do użytkowników",description:"Ręcznie wyzwala wysyłkę powiadomień e-mail do wszystkich aktywnych użytkowników. Każdy użytkownik otrzyma listę ofert ze swojej listy obserwowanych, do których jeszcze nie aplikował. Normalnie działa automatycznie o 17:00.",buttonLabel:"Wyślij maile",buttonVariant:"cyan",loading:m,onClick:d})}),a.jsxs("p",{className:"adm-actions-hint",children:["Scrapery uruchamiasz w zakładce ",a.jsx(D,{to:"/import",children:"Import"})," — osobno dla każdego portalu."]})]}),a.jsx(Q,{}),a.jsx(na,{}),a.jsxs("div",{className:"adm-section",children:[a.jsx("h2",{className:"adm-section-title",children:"Lista użytkowników"}),c?a.jsxs("div",{className:"adm-loading",children:[a.jsx("span",{className:"adm-spinner"}),a.jsx("span",{children:"Ładowanie..."})]}):e.length===0?a.jsx("p",{className:"adm-empty",children:"Brak użytkowników"}):a.jsx("div",{className:"adm-table-wrap",children:a.jsxs("table",{className:"adm-table",children:[a.jsx("thead",{children:a.jsxs("tr",{children:[a.jsx("th",{children:"Użytkownik"}),a.jsx("th",{children:"Email"}),a.jsx("th",{children:"Rola"}),a.jsx("th",{children:"Status"}),a.jsx("th",{className:"adm-th-center",children:"Aplikacje"}),a.jsx("th",{className:"adm-th-center",children:"Odrzucone"}),a.jsx("th",{className:"adm-th-center",children:"Notatki"}),a.jsx("th",{className:"adm-th-center",children:"CV"})]})}),a.jsx("tbody",{children:e.map(s=>a.jsxs("tr",{className:"adm-row",children:[a.jsx("td",{children:a.jsxs("div",{className:"adm-user-cell",children:[a.jsx("span",{className:"adm-avatar",children:s.username[0]?.toUpperCase()}),a.jsx("span",{className:"adm-username",children:s.username})]})}),a.jsx("td",{className:"adm-email",children:s.email}),a.jsx("td",{children:a.jsx(oa,{role:s.role})}),a.jsx("td",{children:a.jsx("span",{className:`adm-status ${s.active?"adm-status--active":"adm-status--inactive"}`,children:s.active?"Aktywny":"Nieaktywny"})}),a.jsx("td",{className:"adm-td-center",children:a.jsx("span",{className:"adm-num adm-num--applied",children:s.appliedCount})}),a.jsx("td",{className:"adm-td-center",children:a.jsx("span",{className:"adm-num adm-num--useless",children:s.uselessCount})}),a.jsx("td",{className:"adm-td-center",children:a.jsx("span",{className:"adm-num adm-num--notes",children:s.notesCount})}),a.jsx("td",{className:"adm-td-center",children:a.jsx("span",{className:`adm-cv ${s.hasCv?"adm-cv--yes":""}`,title:s.hasCv?"Wgrał CV":"Brak CV",children:s.hasCv?"✓":"—"})})]},s.id))})]})})]}),a.jsx("style",{children:`
        /* ── Overview ── */
        .adm-overview {
          display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px;
          margin-bottom: 24px;
        }
        @media (max-width: 900px) { .adm-overview { grid-template-columns: repeat(2, 1fr); } }

        .adm-card {
          background: var(--bg-1); border: 1px solid var(--border-1);
          border-radius: var(--radius-lg); padding: 20px 24px;
        }
        .adm-card--stat { display: flex; flex-direction: column; gap: 4px; }
        .adm-card-val {
          font-family: var(--font-mono); font-size: 2rem; font-weight: 800;
          color: var(--text-0); line-height: 1;
        }
        .adm-card-lbl {
          font-size: 0.74rem; color: var(--text-2); text-transform: uppercase;
          letter-spacing: 0.06em; font-weight: 600;
        }

        /* ── Admin actions ── */
        .adm-actions-section { margin-bottom: 24px; }
        .adm-section-title--standalone {
          font-family: var(--font-display); font-size: 0.88rem; font-weight: 700;
          color: var(--text-1); text-transform: uppercase; letter-spacing: 0.07em;
          margin: 0 0 12px; padding: 0;
        }
        .adm-actions-grid {
          display: grid; grid-template-columns: 1fr; gap: 16px;
        }
        .adm-actions-hint {
          margin-top: 10px; font-family: var(--font-mono); font-size: 0.72rem; color: var(--text-2);
        }
        .adm-actions-hint a { color: var(--accent); font-weight: 600; }
        .adm-actions-hint a:hover { text-decoration: underline; }

        .adm-action-card {
          background: var(--bg-1); border: 1px solid var(--border-1);
          border-radius: var(--radius-lg); padding: 20px 24px;
          display: flex; flex-direction: column; gap: 16px;
        }
        .adm-action-top { display: flex; gap: 14px; align-items: flex-start; }
        .adm-action-icon {
          font-size: 1.4rem; color: var(--accent); flex-shrink: 0;
          margin-top: 2px; width: 24px; text-align: center;
        }
        .adm-action-title {
          font-family: var(--font-display); font-size: 0.95rem; font-weight: 700;
          color: var(--text-0); margin: 0 0 6px;
        }
        .adm-action-desc {
          font-size: 0.78rem; color: var(--text-2); line-height: 1.6; margin: 0;
        }

        .adm-action-btn {
          align-self: flex-start;
          display: inline-flex; align-items: center; gap: 8px;
          font-family: var(--font-mono); font-size: 0.78rem; font-weight: 700;
          padding: 9px 18px; border-radius: var(--radius-md); border: 1px solid transparent;
          cursor: pointer; white-space: nowrap;
          transition: background var(--t-fast), box-shadow var(--t-fast), opacity var(--t-fast);
        }
        .adm-action-btn:disabled { opacity: .5; cursor: not-allowed; }

        .adm-action-btn--accent {
          background: var(--accent); color: #fff; border-color: var(--accent);
        }
        .adm-action-btn--accent:hover:not(:disabled) {
          background: var(--accent-dim); box-shadow: var(--shadow-accent);
        }
        .adm-action-btn--cyan {
          background: rgba(56,189,248,0.12); color: var(--cyan);
          border-color: rgba(56,189,248,0.35);
        }
        .adm-action-btn--cyan:hover:not(:disabled) {
          background: rgba(56,189,248,0.20); box-shadow: var(--shadow-cyan);
        }
        .adm-action-spin {
          width: 12px; height: 12px; border: 2px solid transparent;
          border-top-color: currentColor; border-radius: 50%;
          animation: spin .6s linear infinite; flex-shrink: 0;
        }
        .adm-action-notice {
          display: flex; align-items: flex-start; gap: 10px;
          padding: 10px 13px; border-radius: var(--radius-md);
          background: rgba(56,189,248,0.06); border: 1px solid rgba(56,189,248,0.25);
          font-size: 0.76rem; color: var(--text-2); line-height: 1.6;
        }
        .adm-action-notice-spin {
          width: 12px; height: 12px; flex-shrink: 0; margin-top: 2px;
          border: 2px solid transparent; border-top-color: var(--cyan);
          border-radius: 50%; animation: spin .8s linear infinite;
        }

        /* ── Section ── */
        .adm-section { background: var(--bg-1); border: 1px solid var(--border-1); border-radius: var(--radius-lg); overflow: hidden; }
        .adm-section-title {
          font-family: var(--font-display); font-size: 0.88rem; font-weight: 700;
          color: var(--text-1); text-transform: uppercase; letter-spacing: 0.07em;
          padding: 16px 20px; border-bottom: 1px solid var(--border-1); margin: 0;
        }

        .adm-loading {
          display: flex; align-items: center; gap: 10px;
          padding: 48px; color: var(--text-2); font-size: 0.84rem;
        }
        .adm-spinner {
          width: 18px; height: 18px; border: 2px solid var(--border-1);
          border-top-color: var(--accent); border-radius: 50%;
          animation: spin .7s linear infinite; flex-shrink: 0;
        }
        .adm-empty { padding: 48px; color: var(--text-2); text-align: center; font-size: 0.84rem; }

        /* ── Table ── */
        .adm-table-wrap { overflow-x: auto; }
        .adm-table {
          width: 100%; border-collapse: collapse;
          font-size: 0.82rem; color: var(--text-1);
        }
        .adm-table thead { background: var(--bg-2); }
        .adm-table th {
          padding: 10px 16px; text-align: left;
          font-family: var(--font-mono); font-size: 0.67rem; font-weight: 700;
          text-transform: uppercase; letter-spacing: 0.07em;
          color: var(--text-2); border-bottom: 1px solid var(--border-1);
          white-space: nowrap;
        }
        .adm-th-center { text-align: center; }
        .adm-td-center { text-align: center; }

        .adm-row { border-bottom: 1px solid var(--border-2); transition: background var(--t-fast); }
        .adm-row:last-child { border-bottom: none; }
        .adm-row:hover { background: var(--bg-2); }
        .adm-table td { padding: 12px 16px; vertical-align: middle; }

        .adm-user-cell { display: flex; align-items: center; gap: 10px; }
        .adm-avatar {
          width: 30px; height: 30px; border-radius: 50%;
          background: var(--accent-glow); border: 1px solid var(--border-0);
          color: var(--accent); font-family: var(--font-mono); font-size: 0.75rem; font-weight: 700;
          display: flex; align-items: center; justify-content: center; flex-shrink: 0;
        }
        .adm-username { font-weight: 600; color: var(--text-0); }
        .adm-email { color: var(--text-2); font-size: 0.78rem; }

        .adm-status {
          font-family: var(--font-mono); font-size: 0.65rem; font-weight: 700;
          letter-spacing: 0.06em; text-transform: uppercase;
          padding: 3px 8px; border-radius: 100px;
        }
        .adm-status--active   { background: rgba(16,185,129,0.12); color: var(--green); border: 1px solid rgba(16,185,129,0.3); }
        .adm-status--inactive { background: rgba(148,163,184,0.08); color: var(--text-3); border: 1px solid var(--border-1); }

        .adm-num { font-family: var(--font-mono); font-size: 0.82rem; font-weight: 600; color: var(--text-1); }
        .adm-num--applied { color: var(--accent); }
        .adm-num--useless { color: var(--red); }
        .adm-num--notes   { color: var(--cyan); }
        .adm-cv { font-family: var(--font-mono); font-size: 0.82rem; color: var(--text-3); }
        .adm-cv--yes { color: var(--green); font-weight: 700; }


      `})]})}export{pa as default};
