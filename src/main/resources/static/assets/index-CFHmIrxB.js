import{j as e,r as p}from"./index-DlloBimL.js";const l={trainee:{bg:"rgba(100,116,139,0.18)",border:"rgba(100,116,139,0.35)",color:"#94a3b8"},junior:{bg:"rgba(16,185,129,0.13)",border:"rgba(16,185,129,0.35)",color:"#10b981"},mid:{bg:"rgba(56,189,248,0.12)",border:"rgba(56,189,248,0.35)",color:"#38bdf8"},senior:{bg:"rgba(99,102,241,0.14)",border:"rgba(99,102,241,0.40)",color:"#818cf8"},expert:{bg:"rgba(245,158,11,0.13)",border:"rgba(245,158,11,0.38)",color:"#f59e0b"},default:{bg:"rgba(148,163,184,0.08)",border:"rgba(148,163,184,0.15)",color:"#64748b"}};function x({level:a}){const o=(a||"").toLowerCase(),r=l[o]||l.default;return e.jsx("span",{className:"badge",style:{background:r.bg,borderColor:r.border,color:r.color},children:a||"—"})}function b({title:a,subtitle:o,actions:r}){return e.jsxs(e.Fragment,{children:[e.jsxs("header",{className:"page-header animate-fade-in",children:[e.jsxs("div",{children:[e.jsx("h1",{className:"page-title",children:a}),o&&e.jsx("p",{className:"page-subtitle",children:o})]}),r&&e.jsx("div",{className:"page-header-actions",children:r})]}),e.jsx("style",{children:`
        .page-header {
          display: flex; align-items: flex-start;
          justify-content: space-between; gap: 16px;
          margin-bottom: 24px; flex-wrap: wrap;
        }
        .page-title {
          font-family: var(--font-display);
          font-size: 1.75rem; font-weight: 800; color: var(--text-0);
        }
        .page-subtitle { font-size: 0.82rem; color: var(--text-2); margin-top: 3px; }
        .page-header-actions {
          display: flex; gap: 8px; align-items: center; flex-shrink: 0;
        }
        .badge {
          display: inline-flex; align-items: center;
          font-family: var(--font-mono); font-size: 0.68rem; font-weight: 700;
          letter-spacing: 0.07em; text-transform: uppercase;
          padding: 3px 8px; border-radius: 100px; border: 1px solid;
          white-space: nowrap;
        }
      `})]})}function g({icon:a="◌",title:o,description:r,action:n}){return e.jsxs(e.Fragment,{children:[e.jsxs("div",{className:"empty-state",children:[e.jsx("span",{className:"empty-icon",children:a}),e.jsx("h3",{className:"empty-title",children:o}),r&&e.jsx("p",{className:"empty-desc",children:r}),n&&e.jsx("div",{className:"empty-action",children:n})]}),e.jsx("style",{children:`
        .empty-state {
          display: flex; flex-direction: column;
          align-items: center; text-align: center;
          padding: 64px 24px; gap: 8px;
        }
        .empty-icon  { font-size: 2.5rem; color: var(--text-3); margin-bottom: 8px; }
        .empty-title { font-family: var(--font-display); font-size: 1rem; font-weight: 700; color: var(--text-1); }
        .empty-desc  { font-size: 0.82rem; color: var(--text-2); max-width: 380px; line-height: 1.6; }
        .empty-action{ margin-top: 12px; }
      `})]})}function m({title:a,description:o,onConfirm:r,onClose:n}){const[i,d]=p.useState(""),s=i==="POTWIERDŹ",c=()=>{s&&(r(),n())};return e.jsxs(e.Fragment,{children:[e.jsx("div",{className:"conf-backdrop",onClick:n,onKeyDown:t=>{t.key==="Escape"&&n()},children:e.jsxs("div",{className:"conf-modal animate-fade-in",onClick:t=>t.stopPropagation(),role:"dialog","aria-modal":"true",children:[e.jsx("div",{className:"conf-icon",children:"⚠"}),e.jsx("h3",{className:"conf-title",children:a}),o&&e.jsx("p",{className:"conf-desc",children:o}),e.jsxs("p",{className:"conf-hint",children:["Wpisz ",e.jsx("code",{children:"POTWIERDŹ"})," aby kontynuować:"]}),e.jsx("input",{className:"conf-input",autoFocus:!0,placeholder:"POTWIERDŹ",value:i,onChange:t=>d(t.target.value),onKeyDown:t=>t.key==="Enter"&&c()}),e.jsxs("div",{className:"conf-foot",children:[e.jsx("button",{className:"conf-btn conf-btn--cancel",onClick:n,children:"Anuluj"}),e.jsx("button",{className:"conf-btn conf-btn--delete",disabled:!s,onClick:c,children:"Usuń"})]})]})}),e.jsx("style",{children:`
        .conf-backdrop {
          position: fixed; inset: 0; z-index: 300;
          background: rgba(0,0,0,.72); backdrop-filter: blur(4px);
          display: flex; align-items: center; justify-content: center; padding: 16px;
        }
        .conf-modal {
          background: var(--bg-1); border: 1px solid rgba(239,68,68,.4);
          border-radius: var(--radius-xl); width: 100%; max-width: 400px;
          padding: 28px 24px; display: flex; flex-direction: column; gap: 12px;
          box-shadow: 0 0 40px rgba(239,68,68,.15);
        }
        .conf-icon { font-size: 2rem; text-align: center; }
        .conf-title {
          font-family: var(--font-display); font-size: 1rem; font-weight: 800;
          color: var(--text-0); text-align: center;
        }
        .conf-desc { font-size: 0.79rem; color: var(--text-2); text-align: center; line-height: 1.6; }
        .conf-hint { font-size: 0.75rem; color: var(--text-2); }
        .conf-hint code { color: var(--red); font-family: var(--font-mono); }
        .conf-input {
          width: 100%; padding: 9px 12px; box-sizing: border-box;
          background: var(--bg-2); border: 1px solid var(--border-1);
          border-radius: var(--radius-md); font-family: var(--font-mono);
          font-size: 0.85rem; color: var(--text-0); outline: none;
          transition: border-color .15s;
        }
        .conf-input:focus { border-color: var(--red); box-shadow: 0 0 0 2px rgba(239,68,68,.15); }
        .conf-foot { display: flex; justify-content: flex-end; gap: 8px; margin-top: 4px; }
        .conf-btn {
          padding: 8px 18px; border-radius: var(--radius-md); border: 1px solid;
          font-family: var(--font-mono); font-size: 0.8rem; font-weight: 600;
          cursor: pointer; transition: background .15s, filter .15s;
        }
        .conf-btn--cancel {
          background: transparent; color: var(--text-1); border-color: var(--border-1);
        }
        .conf-btn--cancel:hover { background: var(--bg-2); }
        .conf-btn--delete {
          background: var(--red); color: #fff; border-color: var(--red);
        }
        .conf-btn--delete:disabled { opacity: .35; cursor: not-allowed; }
        .conf-btn--delete:not(:disabled):hover { filter: brightness(1.12); }
      `})]})}export{x as B,m as C,g as E,b as P};
