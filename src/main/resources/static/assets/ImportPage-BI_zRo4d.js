import{a as T,r as c,h as S,j as e,L as D,t as u,i as E}from"./index-DlloBimL.js";import{p as z,e as P,u as J,a as W,g as k}from"./index-DqRoP1O5.js";import{P as F}from"./index-CFHmIrxB.js";import{P as M}from"./PortalTabs-TUmy03Fd.js";const L=4e3,U={QUEUED:{label:"W kolejce",cls:"run"},RUNNING:{label:"W trakcie",cls:"run"},SUCCESS:{label:"Sukces",cls:"ok"},PARTIAL:{label:"Częściowy sukces",cls:"warn"},FAILED:{label:"Błąd",cls:"err"}};function B({source:a}){const s=T(n=>n.user?.role==="ADMIN"),r=z(a),[i,f]=c.useState(null),[g,v]=c.useState(null),[x,y]=c.useState(!1),d=c.useRef(null),b=c.useCallback(async()=>{try{const n=await S.get(a);return f(n.data),v(null),n.data}catch(n){return v(n),null}},[a]);c.useEffect(()=>{let n=!1;const m=async()=>{const l=await b();!n&&l?.running&&(d.current=setTimeout(m,L))};return m(),()=>{n=!0,clearTimeout(d.current)}},[b]);const h=async()=>{y(!0);try{await S.run(a),u.info(`Scraper ${r.label} uruchomiony w tle`),clearTimeout(d.current);const n=async()=>{const m=await b();if(m?.running)d.current=setTimeout(n,L);else if(m?.lastRun){const l=m.lastRun.status;l==="SUCCESS"?u.success(m.lastRun.message):l==="PARTIAL"?u.warn(m.lastRun.message):u.error(m.lastRun.message??"Błąd przebiegu")}};n()}catch(n){n.status===409?u.warn(`Scraper ${r.label} już pracuje`):u.error(n?.message??"Nie udało się uruchomić"),b()}finally{y(!1)}},o=i?.lastRun,t=o?U[o.status]??U.FAILED:null,w=i?.running||x;return e.jsxs("section",{className:"imp-section",children:[e.jsxs("div",{className:"imp-section-head",children:[e.jsx("span",{className:"imp-section-icon",children:"⚙"}),e.jsxs("div",{children:[e.jsxs("h2",{className:"imp-section-title",children:["Scraper ",r.label]}),e.jsxs("p",{className:"imp-section-sub",children:["Pobiera aktualne oferty Java (praktykant / junior / mid) z ",r.label," i importuje je do bazy. Działa niezależnie od pozostałych portali."]})]})]}),e.jsxs("div",{className:"imp-section-body",children:[g&&!i&&e.jsxs("div",{className:"imp-log imp-log--error",children:[e.jsx("span",{className:"imp-log-icon",children:"✕"}),e.jsxs("div",{className:"imp-log-body",children:[e.jsx("p",{className:"imp-log-title",children:"Nie udało się pobrać statusu"}),e.jsx("p",{className:"imp-log-msg",children:g.message})]})]}),i&&!i.enabled&&e.jsx("p",{className:"sc-note sc-note--warn",children:"⚠ Scraper jest wyłączony w konfiguracji serwera."}),e.jsxs("div",{className:"imp-btn-row",children:[s?e.jsx("button",{className:"imp-action-btn imp-action-btn--accent",onClick:h,disabled:w||!i?.enabled,children:i?.running?e.jsxs(e.Fragment,{children:[e.jsx("span",{className:"imp-spin"}),"Scraper pracuje…"]}):"▶ Uruchom scraper"}):e.jsx("p",{className:"sc-note",children:"Uruchamianie scrapera jest dostępne dla administratora."}),s&&e.jsxs(D,{to:`/admin?portal=${r.slug}#scraper-urls`,className:"imp-action-btn sc-urls-link",children:["⛓ Adresy wyszukiwania ",r.label]})]}),i?.running&&e.jsxs("div",{className:"imp-log imp-log--run",children:[e.jsx("span",{className:"imp-spin"}),e.jsxs("div",{className:"imp-log-body",children:[e.jsx("p",{className:"imp-log-title",children:o?.status==="QUEUED"?"Czeka na zakończenie innego scrapera":"Trwa pobieranie ofert…"}),e.jsxs("p",{className:"imp-log-msg",children:["Start: ",P(o?.startedAt)," · możesz swobodnie korzystać z aplikacji."]})]})]}),o&&!i?.running&&e.jsxs("div",{className:`imp-log imp-log--${t.cls==="ok"?"success":t.cls==="warn"?"warn":"error"}`,children:[e.jsx("span",{className:"imp-log-icon",children:t.cls==="ok"?"✓":t.cls==="warn"?"!":"✕"}),e.jsxs("div",{className:"imp-log-body",children:[e.jsxs("p",{className:"imp-log-title",children:["Ostatni przebieg: ",t.label,e.jsxs("span",{className:"sc-sub",children:[" · ",o.trigger==="schedule"?"harmonogram":"ręcznie",o.mode==="IMPORT_ONLY"?" · tylko import":""," · ",P(o.finishedAt??o.startedAt)]})]}),o.message&&e.jsx("p",{className:"imp-log-msg",children:o.message}),e.jsxs("div",{className:"imp-log-stats",children:[o.scraper&&[["Na listach",o.scraper.found],["Unikalne",o.scraper.unique],["Już w bazie",o.scraper.alreadyInDatabase],["Pobrane",o.scraper.detailsOk],["Błędy",o.scraper.errors],["Czas [s]",o.scraper.durationSec]].filter(([,n])=>n!=null).map(([n,m])=>e.jsx(R,{label:n,value:m},n)),o.import&&[["Nowe",o.import.imported,"var(--green)"],["Zaktual.",o.import.updated],["Pominięte",o.import.skipped,"var(--yellow)"],["Odrzucone",o.import.rejected,"var(--red)"]].filter(([,n])=>n!=null).map(([n,m,l])=>e.jsx(R,{label:n,value:m,color:l},n))]})]})]}),i&&e.jsxs("p",{className:"sc-note",children:[i.todaysRun?e.jsxs(e.Fragment,{children:["⏱ Dzisiejsze automatyczne ",i.enabled?"pobranie":"import pliku",": ",e.jsx("strong",{children:i.todaysRun.runAt.slice(0,5)}),i.todaysRun.started?" — już uruchomione.":"."]}):"⏱ Na dziś nie zaplanowano automatycznego pobrania."," ","Harmonogram losowany codziennie o 00:01: jedna z 20 godzin przesunięta o 3/7/11/15/17 min w przód lub wstecz, inna niż wczoraj, min. 2 h odstępu od pozostałych portali.",!o&&!i.running&&" Brak przebiegów od uruchomienia serwera."]})]}),e.jsx("style",{children:`
        .imp-action-btn--accent { background: var(--accent); color: #000; border-color: var(--accent); }
        .imp-action-btn--accent:hover:not(:disabled) { background: var(--accent-dim); border-color: var(--accent-dim); }
        .imp-log--run  { background: var(--accent-glow); border-color: var(--border-0); align-items: center; }
        .imp-log--warn { background: rgba(234,179,8,.06); border-color: rgba(234,179,8,.3); }
        .imp-log--warn .imp-log-icon { color: var(--yellow); }
        .sc-note { font-family: var(--font-mono); font-size: 0.74rem; color: var(--text-2); line-height: 1.6; }
        .sc-note strong { color: var(--accent); }
        .sc-note--warn { color: var(--yellow); }
        .sc-sub { font-family: var(--font-mono); font-weight: 400; font-size: 0.7rem; color: var(--text-3); }
        .sc-urls-link { text-decoration: none; }
      `})]})}function R({label:a,value:s,color:r}){return e.jsxs("div",{className:"imp-log-stat",children:[e.jsx("span",{className:"imp-log-stat-val",style:{color:r??"var(--text-0)"},children:s}),e.jsx("span",{className:"imp-log-stat-label",children:a})]})}const $=`[
  {
    "title": "Senior Java Developer",
    "description": "Opis stanowiska i wymagania...",
    "location": "Warsaw, Mazovia",
    "city": "Warsaw",
    "salaryRange": "15000-20000 PLN",
    "level": "Senior",
    "offerUrl": "https://theprotocol.it/szczegoly/praca/java-developer-warszawa,oferta,01000000-…",
    "company": "Acme Corp",
    "source": "TheProtocol"
  },
  {
    "title": "Mid Backend Developer",
    "description": "...",
    "location": "Kraków",
    "salaryRange": "10000-14000 PLN",
    "level": "Mid",
    "offerUrl": "https://theprotocol.it/szczegoly/praca/mid-backend-krakow,oferta,01000000-…",
    "company": "SoftHouse Sp. z o.o.",
    "source": "TheProtocol"
  }
]`;function K({onClose:a}){return c.useEffect(()=>{const s=r=>{r.key==="Escape"&&a()};return window.addEventListener("keydown",s),()=>window.removeEventListener("keydown",s)},[a]),e.jsxs("div",{className:"modal-backdrop",onClick:a,children:[e.jsxs("div",{className:"modal-box",onClick:s=>s.stopPropagation(),role:"dialog","aria-modal":"true",children:[e.jsxs("div",{className:"modal-head",children:[e.jsx("span",{className:"modal-title",children:"◈ Format pliku JSON"}),e.jsx("button",{className:"modal-close",onClick:a,"aria-label":"Zamknij",children:"✕"})]}),e.jsxs("div",{className:"modal-body",children:[e.jsxs("p",{className:"modal-intro",children:["Plik musi być ",e.jsx("strong",{children:"tablicą obiektów"})," (JSON array). Każdy obiekt to jedna oferta pracy."]}),e.jsxs("div",{className:"modal-section",children:[e.jsx("p",{className:"modal-section-title",children:"Pola wymagane"}),e.jsxs("div",{className:"modal-fields",children:[e.jsxs("div",{className:"modal-field modal-field--req",children:[e.jsx("code",{children:"offerUrl"}),e.jsx("span",{children:"Unikalny URL oferty z domeny wybranego portalu — używany do wykrywania duplikatów"})]}),e.jsxs("div",{className:"modal-field modal-field--req",children:[e.jsx("code",{children:"company"}),e.jsx("span",{children:"Nazwa firmy (nie może być pusta)"})]})]})]}),e.jsxs("div",{className:"modal-section",children:[e.jsx("p",{className:"modal-section-title",children:"Pola opcjonalne"}),e.jsx("div",{className:"modal-fields",children:[["title","Tytuł stanowiska"],["description","Opis oferty (może być wieloliniowy)"],["location",'Lokalizacja, np. "Warsaw, Mazovia" — miasto wyciągane automatycznie'],["city","Miasto (jeśli podane, nadpisuje ekstrakcję z location)"],["salaryRange",'Widełki, np. "10000-15000 PLN"'],["level","Poziom: Trainee / Junior / Mid / Senior / Expert"],["source","NoFluff, TheProtocol lub JustJoin — musi zgadzać się z wybraną zakładką"],["requirements","Lista wymaganych technologii (tablica)"]].map(([s,r])=>e.jsxs("div",{className:"modal-field",children:[e.jsx("code",{children:s}),e.jsx("span",{children:r})]},s))})]}),e.jsxs("div",{className:"modal-section",children:[e.jsx("p",{className:"modal-section-title",children:"Przykład"}),e.jsx("pre",{className:"modal-code",children:$})]}),e.jsxs("div",{className:"modal-note",children:[e.jsx("span",{children:"◎"}),e.jsxs("span",{children:["Oferty z już istniejącym ",e.jsx("code",{children:"offerUrl"})," w bazie zostaną pominięte — nie nadpiszesz danych. Oferty z innego portalu zostaną odrzucone."]})]})]})]}),e.jsx("style",{children:`
        .modal-backdrop {
          position: fixed; inset: 0; z-index: 500;
          background: rgba(0,0,0,.65); backdrop-filter: blur(3px);
          display: flex; align-items: center; justify-content: center;
          padding: 24px; animation: fadeIn .15s ease;
        }
        @keyframes fadeIn { from { opacity: 0 } to { opacity: 1 } }
        .modal-box {
          background: var(--bg-1); border: 1px solid var(--border-0);
          border-radius: var(--radius-lg); width: 100%; max-width: 660px;
          max-height: 85vh; display: flex; flex-direction: column;
          box-shadow: 0 24px 64px rgba(0,0,0,.5);
          animation: slideUp .18s ease;
        }
        @keyframes slideUp { from { transform: translateY(20px); opacity: 0 } to { transform: none; opacity: 1 } }
        .modal-head {
          display: flex; align-items: center; justify-content: space-between;
          padding: 16px 20px; border-bottom: 1px solid var(--border-1);
          background: var(--bg-2); border-radius: var(--radius-lg) var(--radius-lg) 0 0;
          flex-shrink: 0;
        }
        .modal-title {
          font-family: var(--font-display); font-weight: 700;
          font-size: 0.95rem; color: var(--text-0);
        }
        .modal-close {
          background: none; border: none; color: var(--text-3);
          cursor: pointer; font-size: 0.8rem; padding: 4px 6px;
          border-radius: var(--radius-sm); transition: color .15s, background .15s;
        }
        .modal-close:hover { color: var(--text-0); background: var(--bg-3); }
        .modal-body {
          padding: 20px; overflow-y: auto; display: flex; flex-direction: column; gap: 18px;
        }
        .modal-intro { font-size: 0.82rem; color: var(--text-1); line-height: 1.6; }
        .modal-intro strong { color: var(--accent); }
        .modal-section { display: flex; flex-direction: column; gap: 8px; }
        .modal-section-title {
          font-family: var(--font-mono); font-size: 0.66rem; font-weight: 700;
          color: var(--text-3); text-transform: uppercase; letter-spacing: .08em;
        }
        .modal-fields { display: flex; flex-direction: column; gap: 5px; }
        .modal-field {
          display: flex; align-items: baseline; gap: 10px;
          padding: 7px 10px; border-radius: var(--radius-sm);
          background: var(--bg-2); border: 1px solid var(--border-1);
        }
        .modal-field--req { border-color: rgba(99,102,241,.3); background: rgba(99,102,241,.04); }
        .modal-field code {
          font-family: var(--font-mono); font-size: 0.76rem;
          color: var(--accent); white-space: nowrap; flex-shrink: 0; min-width: 110px;
        }
        .modal-field span { font-size: 0.76rem; color: var(--text-2); }
        .modal-code {
          font-family: var(--font-mono); font-size: 0.72rem; color: var(--text-1);
          background: var(--bg-0); border: 1px solid var(--border-1);
          border-radius: var(--radius-md); padding: 14px 16px;
          overflow-x: auto; white-space: pre; line-height: 1.6;
        }
        .modal-note {
          display: flex; align-items: flex-start; gap: 8px;
          padding: 10px 12px; border-radius: var(--radius-md);
          background: rgba(250,204,21,.05); border: 1px solid rgba(250,204,21,.2);
          font-size: 0.76rem; color: var(--text-2);
        }
        .modal-note span:first-child { color: var(--yellow); flex-shrink: 0; }
        .modal-note code { color: var(--accent); font-size: 0.72rem; }
      `})]})}function O(a){return a?typeof a=="string"?{message:a}:a:null}function A({log:a,error:s}){if(s)return e.jsxs("div",{className:"imp-log imp-log--error",children:[e.jsx("span",{className:"imp-log-icon",children:"✕"}),e.jsxs("div",{className:"imp-log-body",children:[e.jsx("p",{className:"imp-log-title",children:"Błąd operacji"}),e.jsx("p",{className:"imp-log-msg",children:s?.message??"Nieznany błąd"})]})]});if(!a)return null;const r=[a.imported!=null&&{label:"Zaimportowano",value:a.imported,color:"var(--green)"},a.updated!=null&&{label:"Uzupełniono",value:a.updated,color:"var(--cyan)"},a.skipped!=null&&{label:"Pominięto",value:a.skipped,color:"var(--yellow)"},a.rejected!=null&&{label:"Odrzucono",value:a.rejected,color:"var(--red)"},a.duplicates!=null&&{label:"Duplikaty",value:a.duplicates,color:"var(--red)"},a.total!=null&&{label:"Łącznie",value:a.total,color:"var(--text-0)"}].filter(Boolean);return e.jsxs("div",{className:"imp-log imp-log--success",children:[e.jsx("span",{className:"imp-log-icon",children:"✓"}),e.jsxs("div",{className:"imp-log-body",children:[e.jsx("p",{className:"imp-log-title",children:"Operacja zakończona"}),a.message&&e.jsx("p",{className:"imp-log-msg",children:a.message}),r.length>0&&e.jsx("div",{className:"imp-log-stats",children:r.map(({label:i,value:f,color:g})=>e.jsxs("div",{className:"imp-log-stat",children:[e.jsx("span",{className:"imp-log-stat-val",style:{color:g},children:f}),e.jsx("span",{className:"imp-log-stat-label",children:i})]},i))}),r.length===0&&Object.keys(a).some(i=>!["message","imported","skipped","duplicates","total","updated","rejected","source","errors"].includes(i))&&e.jsx("pre",{className:"imp-log-raw",children:JSON.stringify(a,null,2)})]})]})}function q({source:a}){const s=z(a),[r,i]=c.useState(null),[f,g]=c.useState(!1),[v,x]=c.useState(null),[y,d]=c.useState(null),[b,h]=c.useState(!1),o=c.useRef(),t=l=>{const p=l.target.files?.[0];if(p){if(!p.name.endsWith(".json")){u.warn("Wybierz plik z rozszerzeniem .json");return}i(p),x(null),d(null)}},w=l=>{l.preventDefault();const p=l.dataTransfer.files?.[0];if(p){if(!p.name.endsWith(".json")){u.warn("Tylko pliki .json");return}i(p),x(null),d(null)}},n=async()=>{if(r){g(!0),x(null),d(null);try{const l=await r.text(),p=JSON.parse(l);if(!Array.isArray(p)||p.length===0)throw{message:"Plik musi zawierać niepustą tablicę ofert (JSON array)"};const N=p.filter(j=>!j||!k(j.offerUrl,a)||j.source&&j.source!==a);if(N.length>0)throw{message:`${N.length} z ${p.length} ofert nie pochodzi z ${s.label} (offerUrl spoza ${s.domain} lub inne pole source). Wybierz właściwą zakładkę lub popraw plik.`};const C=await E.importFromJson(p,a);x(O(C.data)),u.success("Import z JSON zakończony")}catch(l){if(l instanceof SyntaxError){const p={message:"Nieprawidłowy JSON — sprawdź format pliku"};d(p),u.error(p.message)}else d(l),u.error(l?.message??"Błąd importu")}finally{g(!1)}}},m=()=>{i(null),x(null),d(null),o.current&&(o.current.value="")};return e.jsxs("section",{className:"imp-section",children:[e.jsxs("div",{className:"imp-section-head",children:[e.jsx("span",{className:"imp-section-icon",children:"◈"}),e.jsxs("div",{children:[e.jsxs("h2",{className:"imp-section-title",children:["Import z pliku JSON — ",s.label]}),e.jsxs("p",{className:"imp-section-sub",children:["Wgraj plik .json z ofertami ",s.label," (np. wynik scrapera). Plik jest sprawdzany pod kątem portalu."]})]})]}),e.jsxs("div",{className:"imp-section-body",children:[e.jsxs("div",{className:`imp-drop${r?" imp-drop--has-file":""}`,onClick:()=>o.current?.click(),onDrop:w,onDragOver:l=>l.preventDefault(),role:"button",tabIndex:0,onKeyDown:l=>l.key==="Enter"&&o.current?.click(),"aria-label":"Wybierz plik JSON",children:[e.jsx("input",{ref:o,type:"file",accept:".json",style:{display:"none"},onChange:t}),r?e.jsxs("div",{className:"imp-drop-file",children:[e.jsx("span",{className:"imp-drop-file-icon",children:"◉"}),e.jsxs("div",{children:[e.jsx("p",{className:"imp-drop-file-name",children:r.name}),e.jsxs("p",{className:"imp-drop-file-size",children:[(r.size/1024).toFixed(1)," KB"]})]}),e.jsx("button",{className:"imp-drop-clear",onClick:l=>{l.stopPropagation(),m()},"aria-label":"Usuń plik",children:"✕"})]}):e.jsxs("div",{className:"imp-drop-placeholder",children:[e.jsx("span",{className:"imp-drop-ico",children:"⊕"}),e.jsxs("p",{children:["Kliknij lub przeciągnij plik ",e.jsx("strong",{children:".json"})]})]})]}),e.jsxs("div",{className:"imp-btn-row",children:[e.jsx("button",{className:"imp-action-btn",onClick:n,disabled:!r||f,children:f?e.jsxs(e.Fragment,{children:[e.jsx("span",{className:"imp-spin"}),"Importowanie…"]}):"↑ Importuj plik"}),e.jsx("button",{className:"imp-info-btn",onClick:()=>h(!0),type:"button",children:"? Instrukcja"})]}),b&&e.jsx(K,{onClose:()=>h(!1)}),e.jsx(A,{log:v,error:y})]})]})}const I={NoFluff:"nofluffjobs.com/pl/job/…",TheProtocol:"theprotocol.it/szczegoly/praca/…",JustJoin:"justjoin.it/job-offer/…"};function _({source:a}){const s=z(a),[r,i]=c.useState(""),[f,g]=c.useState(!1),[v,x]=c.useState(null),[y,d]=c.useState(null),b=t=>{try{return["http:","https:"].includes(new URL(t).protocol)}catch{return!1}},h=async()=>{if(!b(r)){u.warn("Wpisz poprawny adres URL (http:// lub https://)");return}if(!k(r.trim(),a)){u.warn(`To nie jest adres ogłoszenia z ${s.label}`);return}g(!0),x(null),d(null);try{const t=await E.importFromUrl(r.trim());x(O(t.data)),u.success("Import z URL zakończony")}catch(t){d(t),u.error(t?.message??"Błąd importu z URL")}finally{g(!1)}},o=t=>{t.key==="Enter"&&!f&&h()};return e.jsxs("section",{className:"imp-section",children:[e.jsxs("div",{className:"imp-section-head",children:[e.jsx("span",{className:"imp-section-icon",children:"◎"}),e.jsxs("div",{children:[e.jsxs("h2",{className:"imp-section-title",children:["Import pojedynczej oferty z URL — ",s.label]}),e.jsxs("p",{className:"imp-section-sub",children:["Wklej adres ogłoszenia z ",s.domain," — backend pobierze je właściwym scraperem i doda do Twojej listy."]})]})]}),e.jsxs("div",{className:"imp-section-body",children:[e.jsxs("div",{className:"imp-url-row",children:[e.jsxs("div",{className:"imp-url-wrap",children:[e.jsx("span",{className:"imp-url-prefix",children:"https://"}),e.jsx("input",{className:"imp-url-input",type:"url",placeholder:I[a]??I.NoFluff,value:r,onChange:t=>{i(t.target.value),x(null),d(null)},onKeyDown:o,disabled:f,spellCheck:!1}),r&&e.jsx("button",{className:"imp-url-clear",onClick:()=>{i(""),x(null),d(null)},"aria-label":"Wyczyść URL",children:"✕"})]}),e.jsx("button",{className:"imp-action-btn imp-action-btn--inline",onClick:h,disabled:!r.trim()||f,children:f?e.jsxs(e.Fragment,{children:[e.jsx("span",{className:"imp-spin"}),"Pobieranie…"]}):"↓ Importuj"})]}),r&&!b(r)&&e.jsx("p",{className:"imp-url-hint",children:"⚠ Wpisz pełny adres zaczynający się od http:// lub https://"}),r&&b(r)&&!k(r.trim(),a)&&e.jsxs("p",{className:"imp-url-hint",children:["⚠ Adres spoza ",s.domain," — przełącz zakładkę portalu"]}),e.jsx(A,{log:v,error:y})]})]})}function V(){J("Import ofert");const[a,s]=W();return e.jsxs("div",{className:"import-page animate-fade-in",children:[e.jsx(F,{title:"Import ofert",subtitle:"Zarządzaj źródłami danych — każdy portal osobno"}),e.jsx(M,{value:a,onChange:s}),e.jsxs("div",{className:"imp-grid",children:[e.jsx(B,{source:a}),e.jsx(q,{source:a}),e.jsx(_,{source:a})]},a),e.jsx(H,{})]})}function H(){return e.jsx("style",{children:`
      .import-page { max-width: 820px; }

      /* Grid sekcji */
      .imp-grid { display: flex; flex-direction: column; gap: 16px; }

      /* Sekcja */
      .imp-section {
        background: var(--bg-1); border: 1px solid var(--border-1);
        border-radius: var(--radius-lg); overflow: hidden;
      }

      .imp-section-head {
        display: flex; align-items: flex-start; gap: 14px;
        padding: 18px 20px; border-bottom: 1px solid var(--border-1);
        background: var(--bg-2);
      }
      .imp-section-icon {
        font-size: 1.2rem; color: var(--accent);
        width: 28px; text-align: center; flex-shrink: 0; margin-top: 2px;
      }
      .imp-section-title {
        font-family: var(--font-display); font-weight: 700;
        font-size: 0.95rem; color: var(--text-0); margin-bottom: 3px;
      }
      .imp-section-sub {
        font-size: 0.77rem; color: var(--text-2); line-height: 1.5;
      }

      .imp-section-body {
        padding: 20px; display: flex; flex-direction: column; gap: 14px;
      }

      /* ── Spinner ── */
      .imp-spin {
        display: inline-block; width: 14px; height: 14px;
        border: 2px solid transparent; border-top-color: currentColor;
        border-radius: 50%; animation: spin .6s linear infinite; flex-shrink: 0;
      }
      .imp-spin--sm { width: 12px; height: 12px; }

      /* ── Drop zone ── */
      .imp-drop {
        border: 1.5px dashed var(--border-1); border-radius: var(--radius-md);
        padding: 28px 20px; cursor: pointer; text-align: center;
        transition: border-color .15s, background .15s;
        background: var(--bg-2);
      }
      .imp-drop:hover,
      .imp-drop:focus-visible { border-color: var(--accent); background: var(--accent-glow); outline: none; }
      .imp-drop--has-file { border-style: solid; border-color: var(--accent); }

      .imp-drop-placeholder { display: flex; flex-direction: column; align-items: center; gap: 8px; }
      .imp-drop-ico { font-size: 1.8rem; color: var(--text-3); }
      .imp-drop-placeholder p { font-size: 0.8rem; color: var(--text-2); }
      .imp-drop-placeholder strong { color: var(--accent); }

      .imp-drop-file {
        display: flex; align-items: center; gap: 12px; text-align: left;
      }
      .imp-drop-file-icon { font-size: 1.4rem; color: var(--accent); flex-shrink: 0; }
      .imp-drop-file-name {
        font-family: var(--font-mono); font-size: 0.82rem; color: var(--text-0);
        word-break: break-all;
      }
      .imp-drop-file-size { font-family: var(--font-mono); font-size: 0.7rem; color: var(--text-3); }
      .imp-drop-clear {
        margin-left: auto; background: none; border: none;
        color: var(--text-3); cursor: pointer; font-size: 0.75rem;
        padding: 4px; transition: color .15s; flex-shrink: 0;
      }
      .imp-drop-clear:hover { color: var(--red); }

      /* ── Wiersz przycisków ── */
      .imp-btn-row { display: flex; align-items: center; gap: 8px; flex-wrap: wrap; }

      /* ── Przycisk Instrukcja ── */
      .imp-info-btn {
        display: inline-flex; align-items: center; gap: 7px;
        padding: 9px 16px;
        background: rgba(56,189,248,.1);
        color: var(--cyan);
        border: 1px solid rgba(56,189,248,.45);
        border-radius: var(--radius-md);
        font-family: var(--font-mono); font-size: 0.78rem; font-weight: 700;
        cursor: pointer; white-space: nowrap;
        transition: background .15s, border-color .15s, box-shadow .15s;
      }
      .imp-info-btn:hover {
        background: rgba(56,189,248,.18);
        border-color: var(--cyan);
        box-shadow: 0 0 0 2px rgba(56,189,248,.15);
      }

      /* ── Przycisk akcji ── */
      .imp-action-btn {
        display: inline-flex; align-items: center; gap: 8px;
        padding: 9px 18px; background: var(--bg-3); color: var(--text-0);
        border: 1px solid var(--border-1); border-radius: var(--radius-md);
        font-family: var(--font-mono); font-size: 0.8rem; font-weight: 600;
        cursor: pointer; transition: background .15s, border-color .15s;
        align-self: flex-start; white-space: nowrap;
      }
      .imp-action-btn:hover:not(:disabled) {
        background: var(--bg-4); border-color: var(--border-0);
      }
      .imp-action-btn:disabled { opacity: .45; cursor: not-allowed; }
      .imp-action-btn--inline { align-self: auto; flex-shrink: 0; }

      /* ── URL input ── */
      .imp-url-row { display: flex; gap: 8px; align-items: stretch; flex-wrap: wrap; }
      .imp-url-wrap {
        position: relative; flex: 1; min-width: 240px;
        display: flex; align-items: center;
        background: var(--bg-2); border: 1px solid var(--border-1);
        border-radius: var(--radius-md);
        transition: border-color .15s, box-shadow .15s;
      }
      .imp-url-wrap:focus-within {
        border-color: var(--accent); box-shadow: 0 0 0 2px var(--accent-glow);
      }
      .imp-url-prefix {
        padding: 0 10px; font-family: var(--font-mono); font-size: 0.75rem;
        color: var(--text-3); border-right: 1px solid var(--border-1);
        white-space: nowrap; flex-shrink: 0;
      }
      .imp-url-input {
        flex: 1; padding: 9px 32px 9px 10px;
        background: none; border: none; outline: none;
        font-family: var(--font-mono); font-size: 0.8rem; color: var(--text-0);
      }
      .imp-url-input::placeholder { color: var(--text-3); }
      .imp-url-clear {
        position: absolute; right: 10px;
        background: none; border: none; color: var(--text-3);
        cursor: pointer; font-size: 0.68rem; padding: 2px;
        transition: color .15s;
      }
      .imp-url-clear:hover { color: var(--text-0); }
      .imp-url-hint {
        font-family: var(--font-mono); font-size: 0.72rem; color: var(--yellow);
      }

      /* ── Log wynikowy ── */
      .imp-log {
        display: flex; gap: 12px; align-items: flex-start;
        padding: 14px 16px; border-radius: var(--radius-md); border: 1px solid;
      }
      .imp-log--success {
        background: rgba(34,197,94,0.06); border-color: rgba(34,197,94,0.25);
      }
      .imp-log--error {
        background: rgba(239,68,68,0.06); border-color: rgba(239,68,68,0.25);
      }
      .imp-log-icon {
        font-size: 1rem; flex-shrink: 0; margin-top: 1px;
      }
      .imp-log--success .imp-log-icon { color: var(--green); }
      .imp-log--error   .imp-log-icon { color: var(--red); }

      .imp-log-body { flex: 1; display: flex; flex-direction: column; gap: 8px; }
      .imp-log-title {
        font-family: var(--font-display); font-weight: 700;
        font-size: 0.82rem; color: var(--text-0);
      }
      .imp-log-msg { font-size: 0.78rem; color: var(--text-2); }

      .imp-log-stats {
        display: flex; gap: 16px; flex-wrap: wrap; margin-top: 4px;
      }
      .imp-log-stat {
        display: flex; flex-direction: column; align-items: center; gap: 2px;
      }
      .imp-log-stat-val {
        font-family: var(--font-display); font-size: 1.4rem;
        font-weight: 800; line-height: 1;
      }
      .imp-log-stat-label {
        font-family: var(--font-mono); font-size: 0.62rem;
        color: var(--text-3); text-transform: uppercase; letter-spacing: .07em;
      }
      .imp-log-raw {
        font-family: var(--font-mono); font-size: 0.72rem; color: var(--text-1);
        background: var(--bg-3); border-radius: var(--radius-sm);
        padding: 10px; overflow-x: auto; white-space: pre-wrap; word-break: break-all;
      }
    `})}export{V as default};
