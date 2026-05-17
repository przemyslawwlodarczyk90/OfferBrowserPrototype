import{j as e,r as c,i as h,t as m}from"./index-BXWfP1i_.js";import{u as k}from"./index-BwTqQwrb.js";import{P as z}from"./index-huvFFqiG.js";function b(r){return r?typeof r=="string"?{message:r}:r:null}function v({log:r,error:s}){if(s)return e.jsxs("div",{className:"imp-log imp-log--error",children:[e.jsx("span",{className:"imp-log-icon",children:"✕"}),e.jsxs("div",{className:"imp-log-body",children:[e.jsx("p",{className:"imp-log-title",children:"Błąd operacji"}),e.jsx("p",{className:"imp-log-msg",children:s?.message??"Nieznany błąd"})]})]});if(!r)return null;const a=[r.imported!=null&&{label:"Zaimportowano",value:r.imported,color:"var(--green)"},r.skipped!=null&&{label:"Pominięto",value:r.skipped,color:"var(--yellow)"},r.duplicates!=null&&{label:"Duplikaty",value:r.duplicates,color:"var(--red)"},r.total!=null&&{label:"Łącznie",value:r.total,color:"var(--text-0)"}].filter(Boolean);return e.jsxs("div",{className:"imp-log imp-log--success",children:[e.jsx("span",{className:"imp-log-icon",children:"✓"}),e.jsxs("div",{className:"imp-log-body",children:[e.jsx("p",{className:"imp-log-title",children:"Operacja zakończona"}),r.message&&e.jsx("p",{className:"imp-log-msg",children:r.message}),a.length>0&&e.jsx("div",{className:"imp-log-stats",children:a.map(({label:d,value:u,color:o})=>e.jsxs("div",{className:"imp-log-stat",children:[e.jsx("span",{className:"imp-log-stat-val",style:{color:o},children:u}),e.jsx("span",{className:"imp-log-stat-label",children:d})]},d))}),a.length===0&&e.jsx("pre",{className:"imp-log-raw",children:JSON.stringify(r,null,2)})]})]})}function w(){const[r,s]=c.useState(null),[a,d]=c.useState(null),[u,o]=c.useState(null),x=async()=>{s("running"),d(null),o(null);try{const t=await h.runScript();d(b(t.data)),s("done"),m.success("Skrypt zakończony pomyślnie")}catch(t){o(t),s("error"),m.error(t?.message??"Błąd uruchamiania skryptu")}},n=r==="running";return e.jsxs("section",{className:"imp-section",children:[e.jsxs("div",{className:"imp-section-head",children:[e.jsx("span",{className:"imp-section-icon",children:"⊕"}),e.jsxs("div",{children:[e.jsx("h2",{className:"imp-section-title",children:"Uruchom skrypt Python"}),e.jsx("p",{className:"imp-section-sub",children:"Ręczne wywołanie skryptu importującego — normalnie uruchamia się automatycznie przez cron."})]})]}),e.jsxs("div",{className:"imp-section-body",children:[e.jsx("button",{className:"imp-run-btn",onClick:x,disabled:n,children:n?e.jsxs(e.Fragment,{children:[e.jsx("span",{className:"imp-spin"}),"Uruchamianie…"]}):"▶ Uruchom skrypt"}),r==="running"&&e.jsxs("div",{className:"imp-running-info",children:[e.jsx("span",{className:"imp-spin imp-spin--sm"}),e.jsx("span",{children:"Skrypt działa — może to potrwać kilka minut…"})]}),e.jsx(v,{log:a,error:r==="error"?u:null})]})]})}function N(){const[r,s]=c.useState(null),[a,d]=c.useState(!1),[u,o]=c.useState(null),[x,n]=c.useState(null),t=c.useRef(),f=i=>{const p=i.target.files?.[0];if(p){if(!p.name.endsWith(".json")){m.warn("Wybierz plik z rozszerzeniem .json");return}s(p),o(null),n(null)}},g=i=>{i.preventDefault();const p=i.dataTransfer.files?.[0];if(p){if(!p.name.endsWith(".json")){m.warn("Tylko pliki .json");return}s(p),o(null),n(null)}},l=async()=>{if(r){d(!0),o(null),n(null);try{const i=await r.text(),p=JSON.parse(i),y=await h.importFromJson(p);o(b(y.data)),m.success("Import z JSON zakończony")}catch(i){if(i instanceof SyntaxError){const p={message:"Nieprawidłowy JSON — sprawdź format pliku"};n(p),m.error(p.message)}else n(i),m.error(i?.message??"Błąd importu")}finally{d(!1)}}},j=()=>{s(null),o(null),n(null),t.current&&(t.current.value="")};return e.jsxs("section",{className:"imp-section",children:[e.jsxs("div",{className:"imp-section-head",children:[e.jsx("span",{className:"imp-section-icon",children:"◈"}),e.jsxs("div",{children:[e.jsx("h2",{className:"imp-section-title",children:"Import z pliku JSON"}),e.jsx("p",{className:"imp-section-sub",children:"Wgraj plik .json z listą ofert — zostanie przetworzone przez backend."})]})]}),e.jsxs("div",{className:"imp-section-body",children:[e.jsxs("div",{className:`imp-drop${r?" imp-drop--has-file":""}`,onClick:()=>t.current?.click(),onDrop:g,onDragOver:i=>i.preventDefault(),role:"button",tabIndex:0,onKeyDown:i=>i.key==="Enter"&&t.current?.click(),"aria-label":"Wybierz plik JSON",children:[e.jsx("input",{ref:t,type:"file",accept:".json",style:{display:"none"},onChange:f}),r?e.jsxs("div",{className:"imp-drop-file",children:[e.jsx("span",{className:"imp-drop-file-icon",children:"◉"}),e.jsxs("div",{children:[e.jsx("p",{className:"imp-drop-file-name",children:r.name}),e.jsxs("p",{className:"imp-drop-file-size",children:[(r.size/1024).toFixed(1)," KB"]})]}),e.jsx("button",{className:"imp-drop-clear",onClick:i=>{i.stopPropagation(),j()},"aria-label":"Usuń plik",children:"✕"})]}):e.jsxs("div",{className:"imp-drop-placeholder",children:[e.jsx("span",{className:"imp-drop-ico",children:"⊕"}),e.jsxs("p",{children:["Kliknij lub przeciągnij plik ",e.jsx("strong",{children:".json"})]})]})]}),e.jsx("button",{className:"imp-action-btn",onClick:l,disabled:!r||a,children:a?e.jsxs(e.Fragment,{children:[e.jsx("span",{className:"imp-spin"}),"Importowanie…"]}):"↑ Importuj plik"}),e.jsx(v,{log:u,error:x})]})]})}function S(){const[r,s]=c.useState(""),[a,d]=c.useState(!1),[u,o]=c.useState(null),[x,n]=c.useState(null),t=l=>{try{return["http:","https:"].includes(new URL(l).protocol)}catch{return!1}},f=async()=>{if(!t(r)){m.warn("Wpisz poprawny adres URL (http:// lub https://)");return}d(!0),o(null),n(null);try{const l=await h.importFromUrl(r.trim());o(b(l.data)),m.success("Import z URL zakończony")}catch(l){n(l),m.error(l?.message??"Błąd importu z URL")}finally{d(!1)}},g=l=>{l.key==="Enter"&&!a&&f()};return e.jsxs("section",{className:"imp-section",children:[e.jsxs("div",{className:"imp-section-head",children:[e.jsx("span",{className:"imp-section-icon",children:"◎"}),e.jsxs("div",{children:[e.jsx("h2",{className:"imp-section-title",children:"Import z URL"}),e.jsx("p",{className:"imp-section-sub",children:"Podaj adres URL do pliku JSON z ofertami — backend pobierze i przetworzy dane."})]})]}),e.jsxs("div",{className:"imp-section-body",children:[e.jsxs("div",{className:"imp-url-row",children:[e.jsxs("div",{className:"imp-url-wrap",children:[e.jsx("span",{className:"imp-url-prefix",children:"https://"}),e.jsx("input",{className:"imp-url-input",type:"url",placeholder:"example.com/offers.json",value:r,onChange:l=>{s(l.target.value),o(null),n(null)},onKeyDown:g,disabled:a,spellCheck:!1}),r&&e.jsx("button",{className:"imp-url-clear",onClick:()=>{s(""),o(null),n(null)},"aria-label":"Wyczyść URL",children:"✕"})]}),e.jsx("button",{className:"imp-action-btn imp-action-btn--inline",onClick:f,disabled:!r.trim()||a,children:a?e.jsxs(e.Fragment,{children:[e.jsx("span",{className:"imp-spin"}),"Pobieranie…"]}):"↓ Importuj"})]}),r&&!t(r)&&e.jsx("p",{className:"imp-url-hint",children:"⚠ Wpisz pełny adres zaczynający się od http:// lub https://"}),e.jsx(v,{log:u,error:x})]})]})}function C(){return k("Import ofert"),e.jsxs("div",{className:"import-page animate-fade-in",children:[e.jsx(z,{title:"Import ofert",subtitle:"Zarządzaj źródłami danych"}),e.jsxs("div",{className:"imp-grid",children:[e.jsx(w,{}),e.jsx(N,{}),e.jsx(S,{})]}),e.jsx(L,{})]})}function L(){return e.jsx("style",{children:`
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

      .imp-running-info {
        display: flex; align-items: center; gap: 8px;
        font-family: var(--font-mono); font-size: 0.76rem; color: var(--text-2);
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
