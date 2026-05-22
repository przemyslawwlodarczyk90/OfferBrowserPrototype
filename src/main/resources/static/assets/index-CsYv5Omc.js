import{j as e}from"./index-B4E70c8-.js";const o={trainee:{bg:"rgba(100,116,139,0.2)",border:"rgba(100,116,139,0.4)",color:"#94a3b8"},junior:{bg:"rgba(34,197,94,0.12)",border:"rgba(34,197,94,0.35)",color:"#22c55e"},mid:{bg:"rgba(245,166,35,0.12)",border:"rgba(245,166,35,0.35)",color:"#f5a623"},senior:{bg:"rgba(0,212,212,0.10)",border:"rgba(0,212,212,0.35)",color:"#00d4d4"},expert:{bg:"rgba(168,85,247,0.12)",border:"rgba(168,85,247,0.35)",color:"#a855f7"},default:{bg:"rgba(255,255,255,0.06)",border:"rgba(255,255,255,0.12)",color:"#70708a"}};function i({level:r}){const t=(r||"").toLowerCase(),a=o[t]||o.default;return e.jsx("span",{className:"badge",style:{background:a.bg,borderColor:a.border,color:a.color},children:r||"—"})}function l({title:r,subtitle:t,actions:a}){return e.jsxs(e.Fragment,{children:[e.jsxs("header",{className:"page-header animate-fade-in",children:[e.jsxs("div",{children:[e.jsx("h1",{className:"page-title",children:r}),t&&e.jsx("p",{className:"page-subtitle",children:t})]}),a&&e.jsx("div",{className:"page-header-actions",children:a})]}),e.jsx("style",{children:`
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
      `})]})}function c({icon:r="◌",title:t,description:a,action:s}){return e.jsxs(e.Fragment,{children:[e.jsxs("div",{className:"empty-state",children:[e.jsx("span",{className:"empty-icon",children:r}),e.jsx("h3",{className:"empty-title",children:t}),a&&e.jsx("p",{className:"empty-desc",children:a}),s&&e.jsx("div",{className:"empty-action",children:s})]}),e.jsx("style",{children:`
        .empty-state {
          display: flex; flex-direction: column;
          align-items: center; text-align: center;
          padding: 64px 24px; gap: 8px;
        }
        .empty-icon  { font-size: 2.5rem; color: var(--text-3); margin-bottom: 8px; }
        .empty-title { font-family: var(--font-display); font-size: 1rem; font-weight: 700; color: var(--text-1); }
        .empty-desc  { font-size: 0.82rem; color: var(--text-2); max-width: 380px; line-height: 1.6; }
        .empty-action{ margin-top: 12px; }
      `})]})}export{i as B,c as E,l as P};
