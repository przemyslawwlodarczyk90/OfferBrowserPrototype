import{u as L,a as S,j as a}from"./index-B4E70c8-.js";import{u as E,b,s as j,o as M,d as k}from"./index-BBvXT-jJ.js";import{n as R,a as $}from"./index-Cvq4Ykd3.js";function f({icon:e,label:d,value:o,sub:n,accent:t,loading:i}){return a.jsxs("div",{className:"db-stat",style:t?{borderColor:t}:{},children:[a.jsx("span",{className:"db-stat-icon",style:t?{color:t}:{},children:e}),a.jsxs("div",{className:"db-stat-body",children:[i?a.jsx("div",{className:"db-skel db-skel--val"}):a.jsx("span",{className:"db-stat-val",children:o??"—"}),a.jsx("span",{className:"db-stat-label",children:d}),n&&a.jsx("span",{className:"db-stat-sub",children:n})]})]})}function x({icon:e,label:d,desc:o,to:n,accent:t,navigate:i}){return a.jsxs("button",{className:"db-action",onClick:()=>i(n),children:[a.jsx("span",{className:"db-action-icon",style:{color:t},children:e}),a.jsxs("div",{className:"db-action-body",children:[a.jsx("span",{className:"db-action-label",children:d}),a.jsx("span",{className:"db-action-desc",children:o})]}),a.jsx("span",{className:"db-action-arrow",children:"→"})]})}function P({offer:e,onClick:d}){const o=R(e.level),t={Trainee:"#64748b",Junior:"#22c55e",Mid:"#f5a623",Senior:"#00d4d4",Expert:"#a855f7"}[o]??"var(--text-3)";return a.jsxs("button",{className:"db-offer",onClick:d,children:[a.jsx("span",{className:"db-offer-dot",style:{background:t}}),a.jsxs("div",{className:"db-offer-body",children:[a.jsx("p",{className:"db-offer-title",children:e.title}),a.jsxs("p",{className:"db-offer-meta",children:[e.company??e.companyName??"—",e.city?` · ${e.city}`:""]})]}),a.jsx("span",{className:"db-offer-date",children:$(e.fetchedAt)})]})}function Z(){E("Dashboard");const e=L(),d=S(s=>s.user),o=d?.id,{data:n,loading:t}=b(()=>j.getTotalOffers(),{immediate:!0}),{data:i,loading:w}=b(()=>j.getLevelDistribution(),{immediate:!0}),{data:h,loading:g}=b(()=>M.getAll(),{immediate:!0}),{data:N,loading:z}=b(()=>k.getApplied(o),{immediate:!!o,deps:[o]}),{data:A,loading:O}=b(()=>k.getNotApplied(o),{immediate:!!o,deps:[o]}),C=n?.totalOffers??n??0,c=N?.length??0,l=A?.length??0,p=c+l>0?Math.round(c/(c+l)*100):0;let v="—";if(i){const s=Array.isArray(i)?i.map(r=>({name:r.level??r.name??r.key,count:r.count??r.value??0})):Object.entries(i).map(([r,m])=>({name:r,count:m}));s.length&&(v=s.reduce((r,m)=>r.count>m.count?r:m).name)}const u=h?[...h].sort((s,r)=>new Date(r.fetchedAt)-new Date(s.fetchedAt)).slice(0,5):[],y=new Date().getHours(),D=y<12?"Dzień dobry":y<18?"Cześć":"Dobry wieczór";return a.jsxs("div",{className:"dashboard animate-fade-in",children:[a.jsxs("div",{className:"db-hero",children:[a.jsxs("div",{children:[a.jsxs("h1",{className:"db-hero-title",children:[D,", ",a.jsx("span",{className:"db-hero-name",children:d?.username??"użytkowniku"})," 👋"]}),a.jsxs("p",{className:"db-hero-sub",children:["Masz ",a.jsx("strong",{children:l})," ofert czekających na aplikację."]})]}),a.jsx("button",{className:"db-hero-btn",onClick:()=>e("/offers"),children:"Przeglądaj oferty →"})]}),a.jsxs("div",{className:"db-stats",children:[a.jsx(f,{icon:"◉",label:"Wszystkich ofert",value:C,loading:t,accent:"var(--accent)"}),a.jsx(f,{icon:"✓",label:"Zaaplikowano",value:c,loading:z,accent:"var(--green)",sub:p>0?`${p}% wskaźnik`:null}),a.jsx(f,{icon:"◎",label:"Oczekuje",value:l,loading:O,accent:"var(--yellow)"}),a.jsx(f,{icon:"⊕",label:"Dominujący poziom",value:v,loading:w,accent:"var(--cyan)"})]}),a.jsxs("div",{className:"db-grid",children:[a.jsxs("section",{className:"db-card",children:[a.jsxs("div",{className:"db-card-head",children:[a.jsx("h2",{className:"db-card-title",children:"◷ Ostatnio dodane"}),a.jsx("button",{className:"db-card-link",onClick:()=>e("/offers"),children:"Zobacz wszystkie →"})]}),a.jsxs("div",{className:"db-card-body",children:[g&&Array.from({length:5}).map((s,r)=>a.jsx("div",{className:"db-skel db-skel--row",style:{animationDelay:`${r*40}ms`}},r)),!g&&u.length===0&&a.jsx("p",{className:"db-empty",children:"Brak ofert — uruchom import w sekcji Import."}),!g&&u.map(s=>a.jsx(P,{offer:s,onClick:()=>e(`/offers/${s.id}`)},s.id))]})]}),a.jsxs("section",{className:"db-card",children:[a.jsx("div",{className:"db-card-head",children:a.jsx("h2",{className:"db-card-title",children:"⊕ Szybkie akcje"})}),a.jsxs("div",{className:"db-card-body db-card-body--actions",children:[a.jsx(x,{icon:"◉",label:"Moje oferty",desc:l>0?`${l} ofert do aplikacji`:"Zarządzaj aplikacjami",to:"/my-offers",accent:"var(--accent)",navigate:e}),a.jsx(x,{icon:"◈",label:"Import",desc:"Pobierz nowe oferty",to:"/import",accent:"var(--cyan)",navigate:e}),a.jsx(x,{icon:"◷",label:"Notatki",desc:"Historia aplikacji",to:"/notes",accent:"var(--green)",navigate:e}),a.jsx(x,{icon:"▦",label:"Statystyki",desc:"Wykresy i rozkłady",to:"/stats",accent:"var(--yellow)",navigate:e})]}),a.jsxs("div",{className:"db-progress-wrap",children:[a.jsxs("div",{className:"db-progress-head",children:[a.jsx("span",{className:"db-progress-label",children:"Wskaźnik aplikacji"}),a.jsxs("span",{className:"db-progress-pct",children:[p,"%"]})]}),a.jsx("div",{className:"db-progress-track",children:a.jsx("div",{className:"db-progress-fill",style:{width:`${p}%`}})}),a.jsxs("p",{className:"db-progress-sub",children:[c," zaaplikowane z ",c+l," dostępnych"]})]})]})]}),a.jsx(T,{})]})}function T(){return a.jsx("style",{children:`
      .dashboard { max-width: 1080px; }

      /* ── Hero ── */
      .db-hero {
        display: flex; align-items: center;
        justify-content: space-between; flex-wrap: wrap; gap: 16px;
        background: var(--bg-1); border: 1px solid var(--border-0);
        border-radius: var(--radius-lg); padding: 24px 28px;
        margin-bottom: 20px;
        background-image: radial-gradient(ellipse at top right,
          rgba(245,166,35,0.08) 0%, transparent 60%);
      }
      .db-hero-title {
        font-family: var(--font-display); font-size: 1.5rem;
        font-weight: 800; color: var(--text-0); margin-bottom: 6px;
      }
      .db-hero-name { color: var(--accent); }
      .db-hero-sub  {
        font-size: 0.82rem; color: var(--text-2);
      }
      .db-hero-sub strong { color: var(--text-0); }
      .db-hero-btn {
        padding: 10px 20px; background: var(--accent); color: #000;
        border: none; border-radius: var(--radius-md);
        font-family: var(--font-mono); font-size: 0.8rem; font-weight: 700;
        cursor: pointer; white-space: nowrap;
        transition: background .15s, box-shadow .15s;
      }
      .db-hero-btn:hover { background: var(--accent-dim); box-shadow: var(--shadow-accent); }

      /* ── Statystyki ── */
      .db-stats {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
        gap: 10px; margin-bottom: 20px;
      }
      .db-stat {
        display: flex; align-items: center; gap: 14px;
        background: var(--bg-1); border: 1px solid var(--border-1);
        border-left: 3px solid var(--border-1);
        border-radius: var(--radius-lg); padding: 16px 18px;
        transition: border-color .15s, transform .15s;
      }
      .db-stat:hover { transform: translateY(-2px); }
      .db-stat-icon { font-size: 1.3rem; flex-shrink: 0; }
      .db-stat-body { display: flex; flex-direction: column; gap: 2px; }
      .db-stat-val  {
        font-family: var(--font-display); font-size: 1.6rem;
        font-weight: 800; color: var(--text-0); line-height: 1;
      }
      .db-stat-label {
        font-family: var(--font-mono); font-size: 0.64rem;
        color: var(--text-2); text-transform: uppercase; letter-spacing: .07em;
      }
      .db-stat-sub {
        font-family: var(--font-mono); font-size: 0.62rem; color: var(--text-3);
      }

      /* ── Siatka główna ── */
      .db-grid {
        display: grid;
        grid-template-columns: 1fr 340px;
        gap: 16px;
      }
      @media (max-width: 780px) {
        .db-grid { grid-template-columns: 1fr; }
      }

      /* ── Karta ── */
      .db-card {
        background: var(--bg-1); border: 1px solid var(--border-1);
        border-radius: var(--radius-lg); overflow: hidden;
        display: flex; flex-direction: column;
      }
      .db-card-head {
        display: flex; justify-content: space-between; align-items: center;
        padding: 14px 18px; border-bottom: 1px solid var(--border-1);
        background: var(--bg-2);
      }
      .db-card-title {
        font-family: var(--font-display); font-weight: 700;
        font-size: 0.88rem; color: var(--text-0);
      }
      .db-card-link {
        background: none; border: none; color: var(--accent);
        font-family: var(--font-mono); font-size: 0.7rem;
        cursor: pointer; padding: 0; transition: opacity .15s;
      }
      .db-card-link:hover { opacity: .75; }
      .db-card-body { padding: 8px 0; flex: 1; }
      .db-card-body--actions { padding: 8px; display: flex; flex-direction: column; gap: 4px; }

      /* ── Mini oferta ── */
      .db-offer {
        display: flex; align-items: center; gap: 12px;
        width: 100%; padding: 10px 18px;
        background: none; border: none; text-align: left;
        cursor: pointer; transition: background .12s;
      }
      .db-offer:hover { background: var(--bg-2); }
      .db-offer-dot {
        width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0;
      }
      .db-offer-body { flex: 1; min-width: 0; }
      .db-offer-title {
        font-size: 0.82rem; color: var(--text-0); font-weight: 500;
        white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
      }
      .db-offer-meta {
        font-family: var(--font-mono); font-size: 0.67rem;
        color: var(--text-3); margin-top: 1px;
      }
      .db-offer-date {
        font-family: var(--font-mono); font-size: 0.64rem;
        color: var(--text-3); white-space: nowrap; flex-shrink: 0;
      }

      /* ── Szybka akcja ── */
      .db-action {
        display: flex; align-items: center; gap: 12px;
        background: var(--bg-2); border: 1px solid var(--border-1);
        border-radius: var(--radius-md); padding: 12px 14px;
        cursor: pointer; text-align: left; width: 100%;
        transition: border-color .15s, background .15s;
      }
      .db-action:hover { border-color: var(--border-0); background: var(--bg-3); }
      .db-action-icon { font-size: 1.1rem; flex-shrink: 0; }
      .db-action-body { flex: 1; }
      .db-action-label {
        font-family: var(--font-display); font-weight: 700;
        font-size: 0.82rem; color: var(--text-0); display: block;
      }
      .db-action-desc {
        font-size: 0.7rem; color: var(--text-3);
      }
      .db-action-arrow { color: var(--text-3); font-size: 0.8rem; }

      /* ── Pasek postępu ── */
      .db-progress-wrap {
        margin: 8px 14px 14px; padding: 14px;
        background: var(--bg-2); border: 1px solid var(--border-1);
        border-radius: var(--radius-md);
      }
      .db-progress-head {
        display: flex; justify-content: space-between;
        margin-bottom: 8px;
      }
      .db-progress-label {
        font-family: var(--font-mono); font-size: 0.7rem; color: var(--text-2);
      }
      .db-progress-pct {
        font-family: var(--font-mono); font-size: 0.7rem;
        font-weight: 700; color: var(--accent);
      }
      .db-progress-track {
        height: 6px; background: var(--bg-3);
        border-radius: 3px; overflow: hidden;
      }
      .db-progress-fill {
        height: 100%; border-radius: 3px;
        background: linear-gradient(90deg, var(--accent-dim), var(--accent));
        transition: width .6s ease;
      }
      .db-progress-sub {
        font-family: var(--font-mono); font-size: 0.65rem;
        color: var(--text-3); margin-top: 6px;
      }

      /* ── Empty / Skeleton ── */
      .db-empty {
        font-size: 0.78rem; color: var(--text-3); padding: 24px 18px;
        text-align: center;
      }
      .db-skel {
        background: linear-gradient(90deg, var(--bg-2) 25%, var(--bg-3) 50%, var(--bg-2) 75%);
        background-size: 200% 100%; border-radius: var(--radius-sm);
        animation: shimmer 1.5s ease infinite;
      }
      .db-skel--val { height: 28px; width: 56px; }
      .db-skel--row { height: 44px; margin: 4px 18px; }
    `})}export{Z as default};
