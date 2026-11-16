import{j as r,r as o,p as m}from"./index-DlloBimL.js";import{P as k}from"./PortalTabs-TUmy03Fd.js";import{a as w,u as y,p as j}from"./index-DqRoP1O5.js";import{P as N,E as z}from"./index-CFHmIrxB.js";const u="__all__";function S({data:e,loading:t}){if(t)return r.jsx(A,{});if(!e?.length)return r.jsx(z,{icon:"◌",title:"Brak danych",description:"Brak wymagań w bazie dla wybranego filtra."});const p=e[0]?.count??1;return r.jsx("div",{className:"rq-list",children:e.map((n,s)=>{const x=Math.round(n.count/p*100);return r.jsxs("div",{className:"rq-row",children:[r.jsx("span",{className:"rq-rank",children:s+1}),r.jsx("span",{className:"rq-skill",children:n.skill}),r.jsx("div",{className:"rq-bar-wrap",children:r.jsx("div",{className:"rq-bar-fill",style:{width:`${x}%`}})}),r.jsx("span",{className:"rq-count",children:n.count})]},n.skill)})})}function A(){return r.jsx("div",{className:"rq-list",children:Array.from({length:12},(e,t)=>r.jsxs("div",{className:"rq-row rq-row--skel",children:[r.jsx("div",{className:"rq-skel rq-skel--rank"}),r.jsx("div",{className:"rq-skel rq-skel--skill",style:{width:`${40+t%5*10}%`}}),r.jsx("div",{className:"rq-skel rq-skel--bar"}),r.jsx("div",{className:"rq-skel rq-skel--count"})]},t))})}function R(){const[e,t]=w();return r.jsx(L,{source:e,tabs:r.jsx(k,{value:e,onChange:t})},e)}function L({source:e,tabs:t}){y("Analityka wymagań");const[p,n]=o.useState([]),[s,x]=o.useState(u),[d,g]=o.useState({}),[f,b]=o.useState(!1),i=o.useMemo(()=>[e],[e]);o.useEffect(()=>{m.getAvailableLevels(i).then(a=>n(Array.isArray(a.data)?a.data:[])).catch(()=>{})},[i]);const l=s;o.useEffect(()=>{if(d[l]!==void 0)return;b(!0),(s===u?m.getTopSkills(i):m.getTopSkillsByLevel(s,i)).then(c=>g(q=>({...q,[l]:c.data??[]}))).catch(()=>g(c=>({...c,[l]:[]}))).finally(()=>b(!1))},[s,l,i]);const h=[{key:u,label:"Wszystkie"},...p.map(a=>({key:a,label:a}))],v=d[l];return r.jsxs("div",{className:"rq-page animate-fade-in",children:[r.jsx(N,{title:"Analityka wymagań",subtitle:`Wymagania rynkowe posortowane po liczbie wystąpień — ${j(e).label}`}),t,r.jsx("div",{className:"rq-tabs",children:h.map(({key:a,label:c})=>r.jsxs("button",{className:`rq-tab${s===a?" rq-tab--active":""}`,onClick:()=>x(a),children:[c,d[a]!==void 0&&r.jsx("span",{className:"rq-tab-count",children:d[a].length})]},a))}),r.jsx("div",{className:"rq-content",children:r.jsx(S,{data:v,loading:f})}),r.jsx(P,{})]})}function P(){return r.jsx("style",{children:`
      .rq-page { max-width: 860px; }

      /* ── Filtry źródeł ── */
      .rq-sources {
        display: flex; align-items: center; flex-wrap: wrap; gap: 12px;
        margin-bottom: 16px;
        padding: 10px 14px;
        background: var(--bg-1); border: 1px solid var(--border-1);
        border-radius: var(--radius-md);
      }
      .rq-sources-label {
        font-family: var(--font-mono); font-size: 0.68rem; font-weight: 700;
        color: var(--text-3); text-transform: uppercase; letter-spacing: .07em;
        flex-shrink: 0;
      }
      .rq-source-check {
        display: inline-flex; align-items: center; gap: 6px;
        cursor: pointer; user-select: none;
      }
      .rq-source-check input[type="checkbox"] {
        accent-color: var(--accent);
        width: 14px; height: 14px; cursor: pointer; flex-shrink: 0;
      }
      .rq-source-name {
        font-family: var(--font-mono); font-size: 0.78rem; font-weight: 600;
        color: var(--text-1);
      }
      .rq-source-check:hover .rq-source-name { color: var(--accent); }

      /* ── Przyciski poziomów ── */
      .rq-tabs {
        display: flex; flex-wrap: wrap; gap: 8px; margin-bottom: 16px;
      }
      .rq-tab {
        display: inline-flex; align-items: center; gap: 7px;
        font-family: var(--font-mono); font-size: 0.78rem; font-weight: 600;
        padding: 7px 16px; border-radius: 100px;
        border: 1px solid var(--border-1);
        background: var(--bg-1); color: var(--text-2);
        cursor: pointer; transition: border-color .15s, color .15s, background .15s;
        white-space: nowrap;
      }
      .rq-tab:hover { border-color: var(--border-0); color: var(--text-0); }
      .rq-tab--active {
        border-color: var(--accent); color: var(--accent);
        background: var(--accent-glow);
      }
      .rq-tab-count {
        font-size: 0.66rem; font-weight: 700;
        background: var(--bg-3); color: var(--text-3);
        border-radius: 100px; padding: 1px 6px;
      }
      .rq-tab--active .rq-tab-count {
        background: rgba(245,166,35,.18); color: var(--accent);
      }

      /* ── Lista ── */
      .rq-content {
        background: var(--bg-1); border: 1px solid var(--border-1);
        border-radius: var(--radius-lg); overflow: hidden;
      }
      .rq-list { display: flex; flex-direction: column; }

      .rq-row {
        display: grid;
        grid-template-columns: 36px 1fr 140px 52px;
        align-items: center; gap: 12px;
        padding: 9px 16px;
        border-bottom: 1px solid var(--border-2);
        transition: background .1s;
      }
      .rq-row:last-child { border-bottom: none; }
      .rq-row:hover { background: var(--bg-2); }

      .rq-rank {
        font-family: var(--font-mono); font-size: 0.68rem;
        color: var(--text-3); text-align: right; flex-shrink: 0;
      }
      .rq-skill {
        font-family: var(--font-mono); font-size: 0.82rem;
        color: var(--text-0); font-weight: 600;
        overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
      }
      .rq-bar-wrap {
        height: 6px; background: var(--bg-3);
        border-radius: 3px; overflow: hidden;
      }
      .rq-bar-fill {
        height: 100%; background: var(--accent);
        border-radius: 3px; transition: width .4s ease;
      }
      .rq-count {
        font-family: var(--font-mono); font-size: 0.78rem;
        font-weight: 700; color: var(--accent); text-align: right;
      }

      /* ── Skeleton ── */
      .rq-row--skel { pointer-events: none; }
      .rq-skel {
        border-radius: var(--radius-sm);
        background: linear-gradient(90deg, var(--bg-2) 25%, var(--bg-3) 50%, var(--bg-2) 75%);
        background-size: 200% 100%;
        animation: shimmer 1.5s ease infinite;
      }
      .rq-skel--rank  { width: 20px; height: 10px; }
      .rq-skel--skill { height: 10px; }
      .rq-skel--bar   { height: 6px; }
      .rq-skel--count { width: 28px; height: 10px; margin-left: auto; }
    `})}export{R as default};
