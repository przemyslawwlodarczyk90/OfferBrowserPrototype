import{j as r}from"./index-DlloBimL.js";import{P as i}from"./index-DqRoP1O5.js";function b({value:e,onChange:n,counts:l,extra:d=[]}){return r.jsxs(r.Fragment,{children:[r.jsx("div",{className:"portal-tabs",role:"tablist","aria-label":"Portal z ofertami",children:[...d,...i].map(a=>{const o=a.id===e,t=l?.[a.id];return r.jsxs("button",{role:"tab","aria-selected":o,className:`portal-tab${o?" portal-tab--on":""}`,onClick:()=>!o&&n(a.id),children:[r.jsx("span",{className:"portal-tab-dot","aria-hidden":"true"}),a.label,t!=null&&r.jsx("span",{className:"portal-tab-count",children:t})]},a.id)})}),r.jsx("style",{children:`
        .portal-tabs {
          display: inline-flex; gap: 4px; padding: 4px;
          background: var(--bg-1); border: 1px solid var(--border-1);
          border-radius: var(--radius-lg); margin-bottom: 18px;
          max-width: 100%; overflow-x: auto;
        }
        .portal-tab {
          display: inline-flex; align-items: center; gap: 8px;
          padding: 7px 16px; border: 1px solid transparent; border-radius: var(--radius-md);
          background: none; color: var(--text-2);
          font-family: var(--font-mono); font-size: 0.78rem; font-weight: 600;
          cursor: pointer; white-space: nowrap;
          transition: background .15s, color .15s, border-color .15s;
        }
        .portal-tab:hover { color: var(--text-0); background: var(--bg-2); }
        .portal-tab--on {
          background: var(--accent-glow); color: var(--accent);
          border-color: var(--accent); cursor: default;
        }
        .portal-tab-dot {
          width: 7px; height: 7px; border-radius: 50%;
          background: var(--text-3); transition: background .15s;
        }
        .portal-tab--on .portal-tab-dot { background: var(--accent); }
        .portal-tab-count {
          font-size: 0.64rem; font-weight: 700; padding: 1px 6px; border-radius: 100px;
          background: var(--bg-3); color: var(--text-2); border: 1px solid var(--border-1);
        }
        .portal-tab--on .portal-tab-count { background: var(--accent); color: #000; border-color: var(--accent); }
      `})]})}export{b as P};
