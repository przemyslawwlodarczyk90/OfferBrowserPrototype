import{j as e,r as l,t as p}from"./index-B4E70c8-.js";import{u as z,i as b}from"./index-BBvXT-jJ.js";import{P as N}from"./index-CsYv5Omc.js";const S=`[
  {
    "title": "Senior Java Developer",
    "description": "Opis stanowiska i wymagania...",
    "location": "Warsaw, Mazovia",
    "city": "Warsaw",
    "salaryRange": "15000-20000 PLN",
    "level": "Senior",
    "offerUrl": "https://example.com/job/123",
    "company": "Acme Corp"
  },
  {
    "title": "Mid Backend Developer",
    "description": "...",
    "location": "Kraków",
    "salaryRange": "10000-14000 PLN",
    "level": "Mid",
    "offerUrl": "https://example.com/job/456",
    "company": "SoftHouse Sp. z o.o."
  }
]`;function L({onClose:r}){return l.useEffect(()=>{const a=i=>{i.key==="Escape"&&r()};return window.addEventListener("keydown",a),()=>window.removeEventListener("keydown",a)},[r]),e.jsxs("div",{className:"modal-backdrop",onClick:r,children:[e.jsxs("div",{className:"modal-box",onClick:a=>a.stopPropagation(),role:"dialog","aria-modal":"true",children:[e.jsxs("div",{className:"modal-head",children:[e.jsx("span",{className:"modal-title",children:"◈ Format pliku JSON"}),e.jsx("button",{className:"modal-close",onClick:r,"aria-label":"Zamknij",children:"✕"})]}),e.jsxs("div",{className:"modal-body",children:[e.jsxs("p",{className:"modal-intro",children:["Plik musi być ",e.jsx("strong",{children:"tablicą obiektów"})," (JSON array). Każdy obiekt to jedna oferta pracy."]}),e.jsxs("div",{className:"modal-section",children:[e.jsx("p",{className:"modal-section-title",children:"Pola wymagane"}),e.jsxs("div",{className:"modal-fields",children:[e.jsxs("div",{className:"modal-field modal-field--req",children:[e.jsx("code",{children:"offerUrl"}),e.jsx("span",{children:"Unikalny URL oferty — używany do wykrywania duplikatów"})]}),e.jsxs("div",{className:"modal-field modal-field--req",children:[e.jsx("code",{children:"company"}),e.jsx("span",{children:"Nazwa firmy (nie może być pusta)"})]})]})]}),e.jsxs("div",{className:"modal-section",children:[e.jsx("p",{className:"modal-section-title",children:"Pola opcjonalne"}),e.jsx("div",{className:"modal-fields",children:[["title","Tytuł stanowiska"],["description","Opis oferty (może być wieloliniowy)"],["location",'Lokalizacja, np. "Warsaw, Mazovia" — miasto wyciągane automatycznie'],["city","Miasto (jeśli podane, nadpisuje ekstrakcję z location)"],["salaryRange",'Widełki, np. "10000-15000 PLN"'],["level","Poziom: Trainee / Junior / Mid / Senior / Expert"]].map(([a,i])=>e.jsxs("div",{className:"modal-field",children:[e.jsx("code",{children:a}),e.jsx("span",{children:i})]},a))})]}),e.jsxs("div",{className:"modal-section",children:[e.jsx("p",{className:"modal-section-title",children:"Przykład"}),e.jsx("pre",{className:"modal-code",children:S})]}),e.jsxs("div",{className:"modal-note",children:[e.jsx("span",{children:"◎"}),e.jsxs("span",{children:["Oferty z już istniejącym ",e.jsx("code",{children:"offerUrl"})," w bazie zostaną pominięte — nie nadpiszesz danych."]})]})]})]}),e.jsx("style",{children:`
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
        .modal-field--req { border-color: rgba(0,212,212,.3); background: rgba(0,212,212,.04); }
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
      `})]})}function h(r){return r?typeof r=="string"?{message:r}:r:null}function v({log:r,error:a}){if(a)return e.jsxs("div",{className:"imp-log imp-log--error",children:[e.jsx("span",{className:"imp-log-icon",children:"✕"}),e.jsxs("div",{className:"imp-log-body",children:[e.jsx("p",{className:"imp-log-title",children:"Błąd operacji"}),e.jsx("p",{className:"imp-log-msg",children:a?.message??"Nieznany błąd"})]})]});if(!r)return null;const i=[r.imported!=null&&{label:"Zaimportowano",value:r.imported,color:"var(--green)"},r.skipped!=null&&{label:"Pominięto",value:r.skipped,color:"var(--yellow)"},r.duplicates!=null&&{label:"Duplikaty",value:r.duplicates,color:"var(--red)"},r.total!=null&&{label:"Łącznie",value:r.total,color:"var(--text-0)"}].filter(Boolean);return e.jsxs("div",{className:"imp-log imp-log--success",children:[e.jsx("span",{className:"imp-log-icon",children:"✓"}),e.jsxs("div",{className:"imp-log-body",children:[e.jsx("p",{className:"imp-log-title",children:"Operacja zakończona"}),r.message&&e.jsx("p",{className:"imp-log-msg",children:r.message}),i.length>0&&e.jsx("div",{className:"imp-log-stats",children:i.map(({label:t,value:x,color:s})=>e.jsxs("div",{className:"imp-log-stat",children:[e.jsx("span",{className:"imp-log-stat-val",style:{color:s},children:x}),e.jsx("span",{className:"imp-log-stat-label",children:t})]},t))}),i.length===0&&Object.keys(r).some(t=>!["message","imported","skipped","duplicates","total"].includes(t))&&e.jsx("pre",{className:"imp-log-raw",children:JSON.stringify(r,null,2)})]})]})}function U(){const[r,a]=l.useState(null),[i,t]=l.useState(null),[x,s]=l.useState(null),f=async()=>{a("pending"),t(null),s(null);try{const m=await b.runScript();t(h(m.data)),a("started"),p.success("Skrypt uruchomiony w tle")}catch(m){s(m),a("error"),p.error(m?.message??"Błąd uruchamiania skryptu")}},n=r==="pending";return e.jsxs("section",{className:"imp-section",children:[e.jsxs("div",{className:"imp-section-head",children:[e.jsx("span",{className:"imp-section-icon",children:"⊕"}),e.jsxs("div",{children:[e.jsx("h2",{className:"imp-section-title",children:"Uruchom skrypt Python"}),e.jsx("p",{className:"imp-section-sub",children:"Ręczne wywołanie skryptu importującego — normalnie uruchamia się automatycznie przez cron."})]})]}),e.jsxs("div",{className:"imp-section-body",children:[e.jsxs("div",{className:"imp-notice",children:[e.jsx("span",{className:"imp-notice-icon",children:"◷"}),e.jsxs("span",{children:["Skrypt może działać ",e.jsx("strong",{children:"kilka minut"})," — operacja odbywa się w tle i nie blokuje aplikacji."]})]}),e.jsxs("div",{className:"imp-btn-wrap",children:[e.jsx("button",{className:"imp-run-btn",onClick:f,disabled:n,children:n?e.jsxs(e.Fragment,{children:[e.jsx("span",{className:"imp-spin"}),"Uruchamianie…"]}):"▶ Uruchom skrypt"}),n&&e.jsx("span",{className:"imp-btn-hint",children:"Trwa pobieranie danych, proszę czekać…"})]}),r==="started"&&e.jsxs("div",{className:"imp-bg-info",children:[e.jsx("span",{className:"imp-spin imp-spin--sm"}),e.jsx("span",{children:"Skrypt działa w tle — import ofert nastąpi automatycznie po jego zakończeniu. Możesz korzystać z aplikacji normalnie."})]}),e.jsx(v,{log:null,error:r==="error"?x:null})]})]})}function P(){const[r,a]=l.useState(null),[i,t]=l.useState(!1),[x,s]=l.useState(null),[f,n]=l.useState(null),[m,g]=l.useState(!1),u=l.useRef(),c=o=>{const d=o.target.files?.[0];if(d){if(!d.name.endsWith(".json")){p.warn("Wybierz plik z rozszerzeniem .json");return}a(d),s(null),n(null)}},y=o=>{o.preventDefault();const d=o.dataTransfer.files?.[0];if(d){if(!d.name.endsWith(".json")){p.warn("Tylko pliki .json");return}a(d),s(null),n(null)}},j=async()=>{if(r){t(!0),s(null),n(null);try{const o=await r.text(),d=JSON.parse(o),w=await b.importFromJson(d);s(h(w.data)),p.success("Import z JSON zakończony")}catch(o){if(o instanceof SyntaxError){const d={message:"Nieprawidłowy JSON — sprawdź format pliku"};n(d),p.error(d.message)}else n(o),p.error(o?.message??"Błąd importu")}finally{t(!1)}}},k=()=>{a(null),s(null),n(null),u.current&&(u.current.value="")};return e.jsxs("section",{className:"imp-section",children:[e.jsxs("div",{className:"imp-section-head",children:[e.jsx("span",{className:"imp-section-icon",children:"◈"}),e.jsxs("div",{children:[e.jsx("h2",{className:"imp-section-title",children:"Import z pliku JSON"}),e.jsx("p",{className:"imp-section-sub",children:"Wgraj plik .json z listą ofert — zostanie przetworzone przez backend."})]})]}),e.jsxs("div",{className:"imp-section-body",children:[e.jsxs("div",{className:`imp-drop${r?" imp-drop--has-file":""}`,onClick:()=>u.current?.click(),onDrop:y,onDragOver:o=>o.preventDefault(),role:"button",tabIndex:0,onKeyDown:o=>o.key==="Enter"&&u.current?.click(),"aria-label":"Wybierz plik JSON",children:[e.jsx("input",{ref:u,type:"file",accept:".json",style:{display:"none"},onChange:c}),r?e.jsxs("div",{className:"imp-drop-file",children:[e.jsx("span",{className:"imp-drop-file-icon",children:"◉"}),e.jsxs("div",{children:[e.jsx("p",{className:"imp-drop-file-name",children:r.name}),e.jsxs("p",{className:"imp-drop-file-size",children:[(r.size/1024).toFixed(1)," KB"]})]}),e.jsx("button",{className:"imp-drop-clear",onClick:o=>{o.stopPropagation(),k()},"aria-label":"Usuń plik",children:"✕"})]}):e.jsxs("div",{className:"imp-drop-placeholder",children:[e.jsx("span",{className:"imp-drop-ico",children:"⊕"}),e.jsxs("p",{children:["Kliknij lub przeciągnij plik ",e.jsx("strong",{children:".json"})]})]})]}),e.jsxs("div",{className:"imp-btn-row",children:[e.jsx("button",{className:"imp-action-btn",onClick:j,disabled:!r||i,children:i?e.jsxs(e.Fragment,{children:[e.jsx("span",{className:"imp-spin"}),"Importowanie…"]}):"↑ Importuj plik"}),e.jsx("button",{className:"imp-info-btn",onClick:()=>g(!0),type:"button",children:"? Instrukcja"})]}),m&&e.jsx(L,{onClose:()=>g(!1)}),e.jsx(v,{log:x,error:f})]})]})}function I(){const[r,a]=l.useState(""),[i,t]=l.useState(!1),[x,s]=l.useState(null),[f,n]=l.useState(null),m=c=>{try{return["http:","https:"].includes(new URL(c).protocol)}catch{return!1}},g=async()=>{if(!m(r)){p.warn("Wpisz poprawny adres URL (http:// lub https://)");return}t(!0),s(null),n(null);try{const c=await b.importFromUrl(r.trim());s(h(c.data)),p.success("Import z URL zakończony")}catch(c){n(c),p.error(c?.message??"Błąd importu z URL")}finally{t(!1)}},u=c=>{c.key==="Enter"&&!i&&g()};return e.jsxs("section",{className:"imp-section",children:[e.jsxs("div",{className:"imp-section-head",children:[e.jsx("span",{className:"imp-section-icon",children:"◎"}),e.jsxs("div",{children:[e.jsx("h2",{className:"imp-section-title",children:"Import z URL"}),e.jsx("p",{className:"imp-section-sub",children:"Podaj adres URL do pliku JSON z ofertami — backend pobierze i przetworzy dane."})]})]}),e.jsxs("div",{className:"imp-section-body",children:[e.jsxs("div",{className:"imp-url-row",children:[e.jsxs("div",{className:"imp-url-wrap",children:[e.jsx("span",{className:"imp-url-prefix",children:"https://"}),e.jsx("input",{className:"imp-url-input",type:"url",placeholder:"example.com/offers.json",value:r,onChange:c=>{a(c.target.value),s(null),n(null)},onKeyDown:u,disabled:i,spellCheck:!1}),r&&e.jsx("button",{className:"imp-url-clear",onClick:()=>{a(""),s(null),n(null)},"aria-label":"Wyczyść URL",children:"✕"})]}),e.jsx("button",{className:"imp-action-btn imp-action-btn--inline",onClick:g,disabled:!r.trim()||i,children:i?e.jsxs(e.Fragment,{children:[e.jsx("span",{className:"imp-spin"}),"Pobieranie…"]}):"↓ Importuj"})]}),r&&!m(r)&&e.jsx("p",{className:"imp-url-hint",children:"⚠ Wpisz pełny adres zaczynający się od http:// lub https://"}),e.jsx(v,{log:x,error:f})]})]})}function C(){return z("Import ofert"),e.jsxs("div",{className:"import-page animate-fade-in",children:[e.jsx(N,{title:"Import ofert",subtitle:"Zarządzaj źródłami danych"}),e.jsxs("div",{className:"imp-grid",children:[e.jsx(U,{}),e.jsx(P,{}),e.jsx(I,{})]}),e.jsx(R,{})]})}function R(){return e.jsx("style",{children:`
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

      /* ── Przycisk skryptu ── */
      .imp-run-btn {
        display: inline-flex; align-items: center; gap: 8px;
        padding: 10px 20px; background: var(--accent); color: #000;
        border: 1px solid var(--accent); border-radius: var(--radius-md);
        font-family: var(--font-mono); font-size: 0.82rem; font-weight: 700;
        cursor: pointer; transition: background .15s, box-shadow .15s;
        align-self: flex-start;
      }
      .imp-run-btn:hover:not(:disabled) {
        background: var(--accent-dim); box-shadow: var(--shadow-accent);
      }
      .imp-run-btn:disabled { opacity: .5; cursor: not-allowed; }

      .imp-notice {
        display: flex; align-items: flex-start; gap: 8px;
        padding: 10px 13px;
        background: rgba(250,204,21,0.06); border: 1px solid rgba(250,204,21,0.25);
        border-radius: var(--radius-md);
        font-size: 0.76rem; color: var(--text-2); line-height: 1.5;
      }
      .imp-notice-icon { color: var(--yellow); flex-shrink: 0; font-size: 0.85rem; margin-top: 1px; }
      .imp-notice strong { color: var(--text-1); }

      .imp-btn-wrap { display: flex; align-items: center; gap: 12px; flex-wrap: wrap; }
      .imp-btn-hint {
        font-family: var(--font-mono); font-size: 0.72rem; color: var(--text-3);
        animation: pulse 1.8s ease-in-out infinite;
      }
      @keyframes pulse { 0%,100% { opacity: 1; } 50% { opacity: .4; } }

      .imp-bg-info {
        display: flex; align-items: flex-start; gap: 10px;
        padding: 12px 14px;
        background: rgba(0,212,212,0.06); border: 1px solid rgba(0,212,212,0.25);
        border-radius: var(--radius-md);
        font-family: var(--font-mono); font-size: 0.76rem; color: var(--text-1);
        line-height: 1.5;
      }
      .imp-bg-info .imp-spin { flex-shrink: 0; margin-top: 2px; color: var(--cyan); }

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
        background: rgba(0,212,212,.1);
        color: var(--cyan);
        border: 1px solid rgba(0,212,212,.45);
        border-radius: var(--radius-md);
        font-family: var(--font-mono); font-size: 0.78rem; font-weight: 700;
        cursor: pointer; white-space: nowrap;
        transition: background .15s, border-color .15s, box-shadow .15s;
      }
      .imp-info-btn:hover {
        background: rgba(0,212,212,.18);
        border-color: var(--cyan);
        box-shadow: 0 0 0 2px rgba(0,212,212,.15);
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
    `})}export{C as default};
