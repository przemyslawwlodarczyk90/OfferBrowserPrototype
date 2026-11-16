import{c as Q,r as b,j as a,u as X,a as aa,g as $,t as W}from"./index-DlloBimL.js";import{a as oa,u as ea,b as R,p as Z,n as U,f as ta,t as _,d as ra}from"./index-DqRoP1O5.js";import{P as I,E as P,B as sa}from"./index-CFHmIrxB.js";import{P as na}from"./PortalTabs-TUmy03Fd.js";const ia=[{id:"not-applied",label:"Do aplikacji",icon:"◎"},{id:"applied",label:"Zaaplikowane",icon:"✓"}],la=["createdAt","fetchedAt","updatedAt"];function ca(o){for(const t of la)if(o[t])return o[t];return null}const d="Wszystkie",Y="Nieokreślony",K=["Trainee","Junior","Mid","Senior","Expert"];function E(o){const t=U(o.level);return!t||t==="—"||t.toLowerCase().startsWith("nieokre")?Y:t}function V(o){if(o===Y)return 1e3;const t=K.indexOf(o);return t>=0?t:K.length}function ga(){const[o,t]=oa(),[r,l]=Q(),g=r.get("level")||d,v=b.useCallback(s=>{l(p=>{const x=new URLSearchParams(p);return s===d?x.delete("level"):x.set("level",s),x},{replace:!0})},[l]);return a.jsx(da,{source:o,level:g,onLevelChange:v,tabs:a.jsx(na,{value:o,onChange:t})},o)}function da({source:o,tabs:t,level:r,onLevelChange:l}){ea("Moje oferty");const g=X(),s=aa(e=>e.user)?.id,[p,x]=b.useState("not-applied"),{data:m,loading:j,error:z,execute:i}=R(b.useCallback(()=>$.getNotApplied(s,o),[s,o]),{immediate:!!s}),{data:u,loading:L,error:H,execute:A}=R(b.useCallback(()=>$.getApplied(s,o),[s,o]),{immediate:!!s}),J=b.useCallback(()=>{i(),A()},[i,A]),n=p==="not-applied",q=b.useMemo(()=>{const e=[...m??[],...u??[]],c=n?m??[]:u??[],D=new Set(e.map(E));return r!==d&&D.add(r),[...D].sort((h,N)=>V(h)-V(N)||h.localeCompare(N,"pl")).map(h=>({name:h,count:c.filter(N=>E(N)===h).length}))},[m,u,n,r]),O=b.useCallback(e=>r===d?e??[]:(e??[]).filter(c=>E(c)===r),[r]),M=O(m),B=O(u),f=B.length,k=M.length,G=(n?m:u)?.length??0,C=j||L,S=n?M:B,y=n?j:L,w=n?z:H,T=n?i:A;return s?a.jsxs("div",{className:"my-offers-page animate-fade-in",children:[a.jsx(I,{title:"Moje oferty",subtitle:`Zarządzaj swoimi aplikacjami — ${Z(o).label}`,actions:a.jsx("button",{className:"btn btn--secondary btn--sm",onClick:T,disabled:y,children:"↻ Odśwież"})}),t,a.jsxs("div",{className:"mo-levels",role:"group","aria-label":"Poziom doświadczenia",children:[a.jsxs("button",{className:`mo-pill${r===d?" mo-pill--on":""}`,onClick:()=>l(d),children:["Wszystkie ",!C&&a.jsx("span",{className:"mo-pill-count",children:G})]}),q.map(e=>a.jsxs("button",{className:`mo-pill${r===e.name?" mo-pill--on":""}${!C&&e.count===0?" mo-pill--empty":""}`,onClick:()=>l(e.name),children:[e.name," ",!C&&a.jsx("span",{className:"mo-pill-count",children:e.count})]},e.name))]}),a.jsxs("div",{className:"mo-stats",children:[a.jsxs("div",{className:"mo-stat-box",children:[a.jsx("span",{className:"mo-stat-value",children:f}),a.jsx("span",{className:"mo-stat-label",children:"Zaaplikowano"})]}),a.jsxs("div",{className:"mo-stat-box",children:[a.jsx("span",{className:"mo-stat-value",children:k}),a.jsx("span",{className:"mo-stat-label",children:"Oczekuje"})]}),a.jsxs("div",{className:"mo-stat-box",children:[a.jsx("span",{className:"mo-stat-value",children:f+k>0?`${Math.round(f/(f+k)*100)}%`:"—"}),a.jsx("span",{className:"mo-stat-label",children:"Wskaźnik aplikacji"})]})]}),a.jsx("div",{className:"mo-tabs",role:"tablist",children:ia.map(e=>{const c=e.id==="not-applied"?k:f;return a.jsxs("button",{role:"tab","aria-selected":p===e.id,className:`mo-tab${p===e.id?" mo-tab--active":""}`,onClick:()=>x(e.id),children:[a.jsx("span",{className:"mo-tab-icon",children:e.icon}),e.label,a.jsx("span",{className:`mo-tab-count${p===e.id?" mo-tab-count--active":""}`,children:c})]},e.id)})}),y&&a.jsx("div",{className:"mo-grid",children:Array.from({length:8}).map((e,c)=>a.jsx("div",{className:"mo-skel",style:{animationDelay:`${c*40}ms`}},c))}),!y&&w&&a.jsx(P,{icon:"✕",title:"Błąd ładowania",description:w.message??"Nie udało się pobrać listy ofert.",action:a.jsx("button",{className:"btn btn--primary btn--sm",onClick:T,children:"Spróbuj ponownie"})}),!y&&!w&&S.length===0&&a.jsx(P,{icon:n?"◎":"✓",title:r!==d?`Brak ofert na poziomie ${r}`:n?"Brak ofert do aplikacji":"Brak zaaplikowanych ofert",description:r!==d?`Na tej liście nie ma ofert ${Z(o).label} na poziomie ${r}. Wybierz inny poziom.`:n?"Wszystkie oferty zostały już przetworzone lub baza jest pusta.":'Nie zaaplikowałeś jeszcze na żadną ofertę. Przejdź do listy ofert i kliknij "Aplikuj".',action:r!==d?a.jsx("button",{className:"btn btn--secondary btn--sm",onClick:()=>l(d),children:"Pokaż wszystkie poziomy"}):n?null:a.jsx("button",{className:"btn btn--primary btn--sm",onClick:()=>g("/offers"),children:"Przeglądaj oferty →"})}),!y&&!w&&S.length>0&&a.jsx("div",{className:"mo-grid",children:S.map((e,c)=>a.jsx(pa,{offer:e,isApplied:!n,userId:s,onDetail:()=>g(`/offers/${e.id}`),onApplied:J,style:{animationDelay:`${Math.min(c*30,400)}ms`}},e.id))}),a.jsx(F,{})]}):a.jsxs("div",{className:"my-offers-page animate-fade-in",children:[a.jsx(I,{title:"Moje oferty",subtitle:"Zarządzaj aplikacjami"}),a.jsx(P,{icon:"⊙",title:"Brak danych sesji",description:"Nie można pobrać ID użytkownika z sesji. Wyloguj się i zaloguj ponownie."}),a.jsx(F,{})]})}function pa({offer:o,isApplied:t,userId:r,onDetail:l,onApplied:g,style:v}){const[s,p]=b.useState(!1),x=U(o.level),m=ta(o.salaryRange??o.salary),j=ca(o),z=async i=>{i.stopPropagation(),p(!0);try{await $.applyToOffer(r,o.id),W.success(`Aplikacja na "${_(o.title,40)}" zapisana!`),g()}catch(u){W.error(u?.message??"Błąd podczas aplikowania")}finally{p(!1)}};return a.jsxs("article",{className:"mo-card animate-fade-in",style:v,onClick:l,tabIndex:0,role:"button",onKeyDown:i=>i.key==="Enter"&&l(),children:[a.jsxs("div",{className:"mo-card-top",children:[a.jsx(sa,{level:x}),t?a.jsx("span",{className:"mo-status mo-status--applied",children:"✓ Zaaplikowano"}):a.jsx("span",{className:"mo-status mo-status--pending",children:"◎ Oczekuje"})]}),a.jsx("h3",{className:"mo-card-title",children:_(o.title,70)}),a.jsxs("p",{className:"mo-card-company",children:[a.jsx("span",{"aria-hidden":"true",style:{color:"var(--text-3)",fontSize:".72rem"},children:"◉"}),o.companyName??o.company??"—"]}),a.jsxs("div",{className:"mo-chips",children:[o.city&&a.jsxs("span",{className:"mo-chip",children:["◎ ",o.city]}),m&&a.jsxs("span",{className:"mo-chip mo-chip--salary",children:["₿ ",m]})]}),a.jsxs("div",{className:"mo-card-foot",children:[a.jsx("time",{className:"mo-card-date",children:ra(j)}),a.jsxs("div",{className:"mo-card-actions",onClick:i=>i.stopPropagation(),children:[!t&&a.jsx("button",{className:"mo-apply-btn",onClick:z,disabled:s,"aria-label":"Aplikuj na tę ofertę",children:s?a.jsx("span",{className:"mo-apply-spin","aria-hidden":"true"}):"✓ Aplikuj"}),a.jsx("button",{className:"mo-detail-btn",onClick:i=>{i.stopPropagation(),l()},"aria-label":"Szczegóły oferty",children:"→"})]})]})]})}function F(){return a.jsx("style",{children:`
      .my-offers-page { max-width: 1080px; }

      /* ── Przełącznik poziomów ── */
      .mo-levels { display: flex; gap: 6px; flex-wrap: wrap; margin-bottom: 16px; }
      .mo-pill {
        display: inline-flex; align-items: center; gap: 7px;
        padding: 5px 13px; border-radius: 100px;
        border: 1px solid var(--border-1); background: var(--bg-2);
        color: var(--text-1); font-family: var(--font-mono); font-size: 0.72rem; font-weight: 600;
        cursor: pointer; white-space: nowrap; transition: border-color .15s, color .15s, background .15s;
      }
      .mo-pill:hover { border-color: var(--accent); color: var(--accent); }
      .mo-pill--on { background: var(--accent); color: #fff; border-color: var(--accent); }
      .mo-pill--on:hover { color: #fff; }
      .mo-pill--empty:not(.mo-pill--on) { opacity: .55; }
      .mo-pill-count {
        font-size: 0.62rem; font-weight: 700; padding: 0 6px; border-radius: 100px;
        background: var(--bg-4); color: var(--text-1);
      }
      .mo-pill--on .mo-pill-count { background: rgba(255,255,255,.22); color: #fff; }

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
    `})}export{ga as default};
