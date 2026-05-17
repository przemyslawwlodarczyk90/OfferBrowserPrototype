import{u as O,a as T,r as b,f as z,j as a,t as S}from"./index-BXWfP1i_.js";import{u as D,a as C}from"./index-BwTqQwrb.js";import{P,E as N,B as E}from"./index-huvFFqiG.js";import{n as $,f as Z,t as B,a as I}from"./index-Cvq4Ykd3.js";const K=[{id:"not-applied",label:"Do aplikacji",icon:"◎"},{id:"applied",label:"Zaaplikowane",icon:"✓"}],W=["createdAt","fetchedAt","updatedAt"];function Y(e){for(const n of W)if(e[n])return e[n];return null}function q(){D("Moje oferty");const e=O(),t=T(o=>o.user)?.id,[i,h]=b.useState("not-applied"),{data:x,loading:u,error:g,execute:c}=C(b.useCallback(()=>z.getNotApplied(t),[t]),{immediate:!!t}),{data:d,loading:v,error:j,execute:r}=C(b.useCallback(()=>z.getApplied(t),[t]),{immediate:!!t}),k=b.useCallback(()=>{c(),r()},[c,r]),p=d?.length??0,f=x?.length??0,s=i==="not-applied",w=s?x??[]:d??[],m=s?u:v,y=s?g:j,A=s?c:r;return t?a.jsxs("div",{className:"my-offers-page animate-fade-in",children:[a.jsx(P,{title:"Moje oferty",subtitle:"Zarządzaj swoimi aplikacjami",actions:a.jsx("button",{className:"btn btn--secondary btn--sm",onClick:A,disabled:m,children:"↻ Odśwież"})}),a.jsxs("div",{className:"mo-stats",children:[a.jsxs("div",{className:"mo-stat-box",children:[a.jsx("span",{className:"mo-stat-value",children:p}),a.jsx("span",{className:"mo-stat-label",children:"Zaaplikowano"})]}),a.jsxs("div",{className:"mo-stat-box",children:[a.jsx("span",{className:"mo-stat-value",children:f}),a.jsx("span",{className:"mo-stat-label",children:"Oczekuje"})]}),a.jsxs("div",{className:"mo-stat-box",children:[a.jsx("span",{className:"mo-stat-value",children:p+f>0?`${Math.round(p/(p+f)*100)}%`:"—"}),a.jsx("span",{className:"mo-stat-label",children:"Wskaźnik aplikacji"})]})]}),a.jsx("div",{className:"mo-tabs",role:"tablist",children:K.map(o=>{const l=o.id==="not-applied"?f:p;return a.jsxs("button",{role:"tab","aria-selected":i===o.id,className:`mo-tab${i===o.id?" mo-tab--active":""}`,onClick:()=>h(o.id),children:[a.jsx("span",{className:"mo-tab-icon",children:o.icon}),o.label,a.jsx("span",{className:`mo-tab-count${i===o.id?" mo-tab-count--active":""}`,children:l})]},o.id)})}),m&&a.jsx("div",{className:"mo-grid",children:Array.from({length:8}).map((o,l)=>a.jsx("div",{className:"mo-skel",style:{animationDelay:`${l*40}ms`}},l))}),!m&&y&&a.jsx(N,{icon:"✕",title:"Błąd ładowania",description:y.message??"Nie udało się pobrać listy ofert.",action:a.jsx("button",{className:"btn btn--primary btn--sm",onClick:A,children:"Spróbuj ponownie"})}),!m&&!y&&w.length===0&&a.jsx(N,{icon:s?"◎":"✓",title:s?"Brak ofert do aplikacji":"Brak zaaplikowanych ofert",description:s?"Wszystkie oferty zostały już przetworzone lub baza jest pusta.":'Nie zaaplikowałeś jeszcze na żadną ofertę. Przejdź do listy ofert i kliknij "Aplikuj".',action:s?null:a.jsx("button",{className:"btn btn--primary btn--sm",onClick:()=>e("/offers"),children:"Przeglądaj oferty →"})}),!m&&!y&&w.length>0&&a.jsx("div",{className:"mo-grid",children:w.map((o,l)=>a.jsx(_,{offer:o,isApplied:!s,userId:t,onDetail:()=>e(`/offers/${o.id}`),onApplied:k,style:{animationDelay:`${Math.min(l*30,400)}ms`}},o.id))}),a.jsx(M,{})]}):a.jsxs("div",{className:"my-offers-page animate-fade-in",children:[a.jsx(P,{title:"Moje oferty",subtitle:"Zarządzaj aplikacjami"}),a.jsx(N,{icon:"⊙",title:"Brak danych sesji",description:"Nie można pobrać ID użytkownika z sesji. Wyloguj się i zaloguj ponownie."}),a.jsx(M,{})]})}function _({offer:e,isApplied:n,userId:t,onDetail:i,onApplied:h,style:x}){const[u,g]=b.useState(!1),c=$(e.level),d=Z(e.salary),v=Y(e),j=async r=>{r.stopPropagation(),g(!0);try{await z.applyToOffer(t,e.id),S.success(`Aplikacja na "${B(e.title,40)}" zapisana!`),h()}catch(k){S.error(k?.message??"Błąd podczas aplikowania")}finally{g(!1)}};return a.jsxs("article",{className:"mo-card animate-fade-in",style:x,onClick:i,tabIndex:0,role:"button",onKeyDown:r=>r.key==="Enter"&&i(),children:[a.jsxs("div",{className:"mo-card-top",children:[a.jsx(E,{level:c}),n?a.jsx("span",{className:"mo-status mo-status--applied",children:"✓ Zaaplikowano"}):a.jsx("span",{className:"mo-status mo-status--pending",children:"◎ Oczekuje"})]}),a.jsx("h3",{className:"mo-card-title",children:B(e.title,70)}),a.jsxs("p",{className:"mo-card-company",children:[a.jsx("span",{"aria-hidden":"true",style:{color:"var(--text-3)",fontSize:".72rem"},children:"◉"}),e.companyName??"—"]}),a.jsxs("div",{className:"mo-chips",children:[e.city&&a.jsxs("span",{className:"mo-chip",children:["◎ ",e.city]}),d&&a.jsxs("span",{className:"mo-chip mo-chip--salary",children:["₿ ",d]})]}),a.jsxs("div",{className:"mo-card-foot",children:[a.jsx("time",{className:"mo-card-date",children:I(v)}),a.jsxs("div",{className:"mo-card-actions",onClick:r=>r.stopPropagation(),children:[!n&&a.jsx("button",{className:"mo-apply-btn",onClick:j,disabled:u,"aria-label":"Aplikuj na tę ofertę",children:u?a.jsx("span",{className:"mo-apply-spin","aria-hidden":"true"}):"✓ Aplikuj"}),a.jsx("button",{className:"mo-detail-btn",onClick:r=>{r.stopPropagation(),i()},"aria-label":"Szczegóły oferty",children:"→"})]})]})]})}function M(){return a.jsx("style",{children:`
      .my-offers-page { max-width: 1080px; }

      /* ── Statystyki ── */
      .mo-stats {
        display: flex; gap: 10px; margin-bottom: 20px; flex-wrap: wrap;
      }
      .mo-stat-box {
        display: flex; flex-direction: column; gap: 3px;
        background: var(--bg-1); border: 1px solid var(--border-1);
        border-radius: var(--radius-lg); padding: 14px 20px;
        min-width: 120px; flex: 1;
      }
      .mo-stat-value {
        font-family: var(--font-display); font-size: 1.7rem;
        font-weight: 800; color: var(--accent); line-height: 1;
      }
      .mo-stat-label {
        font-family: var(--font-mono); font-size: 0.65rem;
        color: var(--text-2); text-transform: uppercase; letter-spacing: .08em;
      }

      /* ── Zakładki ── */
      .mo-tabs {
        display: flex; gap: 4px; margin-bottom: 20px;
        border-bottom: 1px solid var(--border-1); padding-bottom: 0;
      }
      .mo-tab {
        display: flex; align-items: center; gap: 7px;
        padding: 9px 16px; background: none; border: none;
        border-bottom: 2px solid transparent; margin-bottom: -1px;
        font-family: var(--font-mono); font-size: 0.78rem; color: var(--text-2);
        cursor: pointer; transition: color .15s, border-color .15s;
        white-space: nowrap;
      }
      .mo-tab:hover { color: var(--text-0); }
      .mo-tab--active { color: var(--accent); border-bottom-color: var(--accent); }
      .mo-tab-icon { font-size: 0.85rem; }
      .mo-tab-count {
        font-size: 0.65rem; font-weight: 700;
        background: var(--bg-3); color: var(--text-2);
        border: 1px solid var(--border-1);
        padding: 1px 6px; border-radius: 100px;
        transition: background .15s, color .15s, border-color .15s;
      }
      .mo-tab-count--active {
        background: var(--accent-glow); color: var(--accent);
        border-color: var(--border-0);
      }

      /* ── Siatka ── */
      .mo-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
        gap: 12px;
      }

      /* ── Karta ── */
      .mo-card {
        display: flex; flex-direction: column; gap: 7px;
        background: var(--bg-1); border: 1px solid var(--border-1);
        border-radius: var(--radius-lg); padding: 1.05rem 1.1rem;
        cursor: pointer; outline: none;
        transition: border-color .15s, transform .15s, box-shadow .15s;
      }
      .mo-card:hover,
      .mo-card:focus-visible {
        border-color: var(--accent);
        transform: translateY(-2px);
        box-shadow: var(--shadow-accent);
      }

      /* Top: badge + status */
      .mo-card-top {
        display: flex; align-items: center;
        justify-content: space-between; gap: 6px;
      }
      .mo-status {
        font-family: var(--font-mono); font-size: 0.62rem; font-weight: 700;
        letter-spacing: .07em; padding: 2px 8px;
        border-radius: 100px; border: 1px solid; white-space: nowrap;
      }
      .mo-status--applied {
        color: var(--green); border-color: rgba(34,197,94,.35);
        background: rgba(34,197,94,.08);
      }
      .mo-status--pending {
        color: var(--yellow); border-color: rgba(234,179,8,.35);
        background: rgba(234,179,8,.08);
      }

      /* Tytuł */
      .mo-card-title {
        font-family: var(--font-display); font-weight: 700;
        font-size: 0.89rem; color: var(--text-0); line-height: 1.35; margin: 0;
        display: -webkit-box; -webkit-line-clamp: 2;
        -webkit-box-orient: vertical; overflow: hidden;
      }

      /* Firma */
      .mo-card-company {
        display: flex; align-items: center; gap: 5px;
        font-size: 0.77rem; color: var(--text-2); margin: 0;
      }

      /* Chipy */
      .mo-chips { display: flex; flex-wrap: wrap; gap: 5px; }
      .mo-chip {
        display: flex; align-items: center; gap: 4px;
        font-family: var(--font-mono); font-size: 0.68rem; color: var(--text-2);
        background: var(--bg-2); border: 1px solid var(--border-1);
        padding: 3px 8px; border-radius: var(--radius-sm);
      }
      .mo-chip--salary {
        color: var(--cyan); border-color: rgba(0,212,212,.2);
        background: rgba(0,212,212,.05);
      }

      /* Stopka */
      .mo-card-foot {
        display: flex; justify-content: space-between; align-items: center;
        margin-top: auto; padding-top: 8px; border-top: 1px solid var(--border-1);
        gap: 8px;
      }
      .mo-card-date {
        font-family: var(--font-mono); font-size: 0.66rem; color: var(--text-3);
      }
      .mo-card-actions { display: flex; gap: 5px; align-items: center; }

      /* Przycisk Aplikuj */
      .mo-apply-btn {
        position: relative;
        padding: 5px 13px; font-family: var(--font-mono); font-size: 0.72rem;
        font-weight: 700; color: #000;
        background: var(--accent); border: 1px solid var(--accent);
        border-radius: var(--radius-md); cursor: pointer;
        transition: background .15s, box-shadow .15s, transform .1s;
        white-space: nowrap; min-width: 72px;
        display: flex; align-items: center; justify-content: center;
      }
      .mo-apply-btn:hover:not(:disabled) {
        background: var(--accent-dim); box-shadow: var(--shadow-accent);
      }
      .mo-apply-btn:active:not(:disabled) { transform: scale(.97); }
      .mo-apply-btn:disabled { opacity: .55; cursor: not-allowed; }
      .mo-apply-spin {
        width: 12px; height: 12px;
        border: 2px solid transparent; border-top-color: #000;
        border-radius: 50%; animation: spin .6s linear infinite;
      }

      /* Przycisk Szczegóły */
      .mo-detail-btn {
        width: 28px; height: 28px;
        display: flex; align-items: center; justify-content: center;
        background: var(--bg-2); border: 1px solid var(--border-1);
        border-radius: var(--radius-md); color: var(--text-2);
        font-size: 0.8rem; cursor: pointer;
        transition: border-color .15s, color .15s;
      }
      .mo-detail-btn:hover { border-color: var(--accent); color: var(--accent); }

      /* Skeleton */
      .mo-skel {
        height: 158px; border-radius: var(--radius-lg);
        background: linear-gradient(90deg, var(--bg-2) 25%, var(--bg-3) 50%, var(--bg-2) 75%);
        background-size: 200% 100%;
        animation: shimmer 1.5s ease infinite;
      }
    `})}export{q as default};
