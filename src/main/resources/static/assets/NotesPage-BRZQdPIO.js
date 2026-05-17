import{a as D,r as m,n as y,j as a,t as j}from"./index-BXWfP1i_.js";import{u as S,a as C,b as A}from"./index-BwTqQwrb.js";import{P as k,E as h}from"./index-huvFFqiG.js";import{a as N,b as T,t as w}from"./index-Cvq4Ykd3.js";const $=[{id:"notes",label:"Notatki",icon:"◷"},{id:"companies",label:"Firmy i daty",icon:"◉"}];function F({userId:e,onClose:s,onCreated:d}){const[c,x]=m.useState({companyName:"",offerUrl:""}),[i,n]=m.useState({}),[t,p]=m.useState(!1),f=()=>{const o={};return c.companyName.trim()||(o.companyName="Pole wymagane"),o},b=o=>{const{name:u,value:g}=o.target;x(v=>({...v,[u]:g})),i[u]&&n(v=>({...v,[u]:void 0}))},l=async o=>{o.preventDefault();const u=f();if(Object.keys(u).length){n(u);return}p(!0);try{await y.createExternal(e,c.companyName.trim(),c.offerUrl.trim()||void 0),j.success(`Notatka dla "${c.companyName.trim()}" dodana`),d(),s()}catch(g){j.error(g?.message??"Błąd tworzenia notatki")}finally{p(!1)}},r=o=>{o.key==="Escape"&&s()};return a.jsx("div",{className:"n-modal-backdrop",onClick:s,onKeyDown:r,children:a.jsxs("div",{className:"n-modal animate-fade-in",onClick:o=>o.stopPropagation(),role:"dialog","aria-modal":"true","aria-label":"Nowa notatka zewnętrzna",children:[a.jsxs("div",{className:"n-modal-head",children:[a.jsxs("div",{children:[a.jsx("h2",{className:"n-modal-title",children:"Nowa notatka zewnętrzna"}),a.jsx("p",{className:"n-modal-sub",children:"Aplikacja spoza systemu (własne źródło)"})]}),a.jsx("button",{className:"n-modal-close",onClick:s,"aria-label":"Zamknij",children:"✕"})]}),a.jsxs("form",{onSubmit:l,className:"n-modal-body",noValidate:!0,children:[a.jsxs("div",{className:"n-field",children:[a.jsxs("label",{className:"n-label",htmlFor:"nm-company",children:["Nazwa firmy ",a.jsx("span",{className:"n-required",children:"*"})]}),a.jsx("input",{id:"nm-company",name:"companyName",className:`n-input${i.companyName?" n-input--err":""}`,placeholder:"np. Acme Corp",value:c.companyName,onChange:b,autoFocus:!0,disabled:t}),i.companyName&&a.jsx("span",{className:"n-err",children:i.companyName})]}),a.jsxs("div",{className:"n-field",children:[a.jsxs("label",{className:"n-label",htmlFor:"nm-url",children:["URL oferty ",a.jsx("span",{className:"n-optional",children:"(opcjonalnie)"})]}),a.jsx("input",{id:"nm-url",name:"offerUrl",type:"url",className:"n-input",placeholder:"https://example.com/job/123",value:c.offerUrl,onChange:b,disabled:t})]}),a.jsxs("div",{className:"n-modal-foot",children:[a.jsx("button",{type:"button",className:"btn-ghost",onClick:s,disabled:t,children:"Anuluj"}),a.jsx("button",{type:"submit",className:"btn-primary",disabled:t,children:t?a.jsxs(a.Fragment,{children:[a.jsx("span",{className:"n-spin"}),"Zapisywanie…"]}):"✓ Dodaj notatkę"})]})]})]})})}function B({note:e,style:s}){const[d,c]=m.useState(!1),x=e.content?.trim();return a.jsxs("article",{className:"n-card animate-fade-in",style:s,children:[a.jsxs("div",{className:"n-card-head",children:[a.jsxs("div",{className:"n-card-meta",children:[a.jsxs("span",{className:"n-card-company",children:[a.jsx("span",{className:"n-card-dot",children:"◉"}),e.companyName??"—"]}),e.external&&a.jsx("span",{className:"n-badge n-badge--ext",children:"zewnętrzna"})]}),a.jsx("time",{className:"n-card-date",children:T(e.applicationDate??e.createdAt)})]}),e.offerTitle&&a.jsx("p",{className:"n-card-offer",children:w(e.offerTitle,80)}),e.offerUrl&&a.jsxs("a",{href:e.offerUrl,target:"_blank",rel:"noopener noreferrer",className:"n-card-link",onClick:i=>i.stopPropagation(),children:["↗ ",w(e.offerUrl,60)]}),x&&a.jsxs("div",{className:"n-card-content-wrap",children:[a.jsx("p",{className:`n-card-content${d?"":" n-card-content--clamped"}`,children:e.content}),e.content.length>120&&a.jsx("button",{className:"n-card-expand",onClick:()=>c(i=>!i),children:d?"▲ Zwiń":"▼ Rozwiń"})]})]})}function E({notes:e,loading:s,error:d,reload:c}){const[x,i]=m.useState(""),[n,t]=m.useState("Wszystkie"),p=A(x,250),f=m.useMemo(()=>{if(!e)return[];const r=new Set(e.map(o=>o.companyName).filter(Boolean));return["Wszystkie",...Array.from(r).sort()]},[e]),b=m.useMemo(()=>{let r=e??[];if(n!=="Wszystkie"&&(r=r.filter(o=>o.companyName===n)),p.trim()){const o=p.toLowerCase();r=r.filter(u=>u.companyName?.toLowerCase().includes(o)||u.offerTitle?.toLowerCase().includes(o)||u.content?.toLowerCase().includes(o))}return r},[e,n,p]),l=p.trim()!==""||n!=="Wszystkie";return s?a.jsx("div",{className:"n-grid",children:Array.from({length:6}).map((r,o)=>a.jsx("div",{className:"n-skel",style:{animationDelay:`${o*40}ms`}},o))}):d?a.jsx(h,{icon:"✕",title:"Błąd ładowania notatek",description:d.message??"Nie udało się pobrać notatek.",action:a.jsx("button",{className:"btn-primary btn-sm",onClick:c,children:"Spróbuj ponownie"})}):a.jsxs(a.Fragment,{children:[a.jsxs("div",{className:"n-toolbar",children:[a.jsxs("div",{className:"n-search-wrap",children:[a.jsx("span",{className:"n-search-ico",children:"⊘"}),a.jsx("input",{className:"n-search",type:"search",placeholder:"Szukaj po firmie, tytule, treści…",value:x,onChange:r=>i(r.target.value)}),x&&a.jsx("button",{className:"n-search-clear",onClick:()=>i(""),children:"✕"})]}),a.jsx("select",{className:"n-select",value:n,onChange:r=>t(r.target.value),children:f.map(r=>a.jsx("option",{value:r,children:r},r))})]}),e&&a.jsxs("p",{className:"n-count",children:[a.jsx("strong",{children:b.length}),l?` z ${e.length}`:""," notatek"]}),b.length===0&&a.jsx(h,{icon:"◷",title:"Brak notatek",description:l?"Żadna notatka nie pasuje do kryteriów.":"Nie masz jeszcze żadnych notatek aplikacyjnych.",action:l?a.jsx("button",{className:"btn-ghost btn-sm",onClick:()=>{i(""),t("Wszystkie")},children:"Wyczyść filtry"}):null}),b.length>0&&a.jsx("div",{className:"n-grid",children:b.map((r,o)=>a.jsx(B,{note:r,style:{animationDelay:`${Math.min(o*30,350)}ms`}},r.id??o))})]})}function M({userId:e}){const{data:s,loading:d,error:c,execute:x}=C(m.useCallback(()=>y.getCompaniesWithDates(e),[e]),{immediate:!!e});if(d)return a.jsx("div",{className:"n-companies-grid",children:Array.from({length:8}).map((n,t)=>a.jsx("div",{className:"n-skel n-skel--company",style:{animationDelay:`${t*35}ms`}},t))});if(c)return a.jsx(h,{icon:"✕",title:"Błąd ładowania",description:c.message??"Nie udało się pobrać listy firm.",action:a.jsx("button",{className:"btn-primary btn-sm",onClick:x,children:"Spróbuj ponownie"})});if(!s?.length)return a.jsx(h,{icon:"◉",title:"Brak firm",description:"Gdy zaczniesz aplikować, firmy pojawią się tutaj wraz z datami."});const i=[...s].sort((n,t)=>{const p=new Date(n.lastApplicationDate??n.applicationDate??0);return new Date(t.lastApplicationDate??t.applicationDate??0)-p});return a.jsxs(a.Fragment,{children:[a.jsxs("p",{className:"n-count",children:[a.jsx("strong",{children:i.length})," firm"]}),a.jsx("div",{className:"n-companies-grid",children:i.map((n,t)=>a.jsxs("div",{className:"n-company-card animate-fade-in",style:{animationDelay:`${Math.min(t*30,350)}ms`},children:[a.jsxs("div",{className:"n-company-head",children:[a.jsx("span",{className:"n-company-dot",children:"◉"}),a.jsx("span",{className:"n-company-name",children:n.companyName??"—"})]}),a.jsx("div",{className:"n-company-dates",children:n.applicationDates?.length>0?n.applicationDates.map((p,f)=>a.jsx("span",{className:"n-company-date-chip",children:N(p)},f)):n.applicationDate||n.lastApplicationDate?a.jsx("span",{className:"n-company-date-chip",children:N(n.applicationDate??n.lastApplicationDate)}):a.jsx("span",{className:"n-company-date-chip n-company-date-chip--empty",children:"brak daty"})}),n.count!=null&&a.jsxs("p",{className:"n-company-count",children:[n.count," ",n.count===1?"aplikacja":"aplikacji"]})]},n.companyName??t))})]})}function q(){S("Notatki aplikacyjne");const s=D(l=>l.user)?.id,[d,c]=m.useState("notes"),[x,i]=m.useState(!1),{data:n,loading:t,error:p,execute:f}=C(m.useCallback(()=>y.getAll(s),[s]),{immediate:!!s}),b=n?.length??0;return s?a.jsxs("div",{className:"notes-page animate-fade-in",children:[a.jsx(k,{title:"Notatki aplikacyjne",subtitle:b>0?`${b} notatek łącznie`:"Twoje aplikacje",actions:a.jsxs("div",{style:{display:"flex",gap:8},children:[a.jsx("button",{className:"btn-ghost btn-sm",onClick:f,disabled:t,children:"↻ Odśwież"}),a.jsx("button",{className:"btn-primary btn-sm",onClick:()=>i(!0),children:"⊕ Nowa notatka"})]})}),a.jsx("div",{className:"n-tabs",role:"tablist",children:$.map(l=>{const r=l.id==="notes"?b:null;return a.jsxs("button",{role:"tab","aria-selected":d===l.id,className:`n-tab${d===l.id?" n-tab--active":""}`,onClick:()=>c(l.id),children:[a.jsx("span",{children:l.icon}),l.label,r!==null&&r>0&&a.jsx("span",{className:`n-tab-count${d===l.id?" n-tab-count--active":""}`,children:r})]},l.id)})}),d==="notes"?a.jsx(E,{notes:n,loading:t,error:p,reload:f}):a.jsx(M,{userId:s}),x&&a.jsx(F,{userId:s,onClose:()=>i(!1),onCreated:f}),a.jsx(z,{})]}):a.jsxs("div",{className:"notes-page animate-fade-in",children:[a.jsx(k,{title:"Notatki aplikacyjne",subtitle:"Twoje aplikacje"}),a.jsx(h,{icon:"⊙",title:"Brak danych sesji",description:"Wyloguj się i zaloguj ponownie."}),a.jsx(z,{})]})}function z(){return a.jsx("style",{children:`
      .notes-page { max-width: 1080px; }

      /* ── Zakładki ── */
      .n-tabs {
        display: flex; gap: 4px; margin-bottom: 20px;
        border-bottom: 1px solid var(--border-1);
      }
      .n-tab {
        display: flex; align-items: center; gap: 7px;
        padding: 9px 16px; background: none; border: none;
        border-bottom: 2px solid transparent; margin-bottom: -1px;
        font-family: var(--font-mono); font-size: 0.78rem; color: var(--text-2);
        cursor: pointer; transition: color .15s, border-color .15s; white-space: nowrap;
      }
      .n-tab:hover { color: var(--text-0); }
      .n-tab--active { color: var(--accent); border-bottom-color: var(--accent); }
      .n-tab-count {
        font-size: 0.65rem; font-weight: 700;
        background: var(--bg-3); color: var(--text-2);
        border: 1px solid var(--border-1);
        padding: 1px 6px; border-radius: 100px;
        transition: background .15s, color .15s;
      }
      .n-tab-count--active {
        background: var(--accent-glow); color: var(--accent); border-color: var(--border-0);
      }

      /* ── Toolbar ── */
      .n-toolbar {
        display: flex; gap: 8px; margin-bottom: 10px; flex-wrap: wrap;
      }
      .n-search-wrap { position: relative; flex: 1; min-width: 200px; }
      .n-search-ico {
        position: absolute; left: 11px; top: 50%; transform: translateY(-50%);
        color: var(--text-3); font-size: 0.82rem; pointer-events: none;
      }
      .n-search {
        width: 100%; padding: 8px 32px 8px 32px;
        background: var(--bg-2); border: 1px solid var(--border-1);
        border-radius: var(--radius-md); font-family: var(--font-mono);
        font-size: 0.81rem; color: var(--text-0); outline: none;
        transition: border-color .15s, box-shadow .15s;
      }
      .n-search::placeholder { color: var(--text-3); }
      .n-search::-webkit-search-cancel-button { display: none; }
      .n-search:focus { border-color: var(--accent); box-shadow: 0 0 0 2px var(--accent-glow); }
      .n-search-clear {
        position: absolute; right: 10px; top: 50%; transform: translateY(-50%);
        background: none; border: none; color: var(--text-3); cursor: pointer;
        font-size: 0.68rem; padding: 2px; transition: color .15s;
      }
      .n-search-clear:hover { color: var(--text-0); }
      .n-select {
        background: var(--bg-2); border: 1px solid var(--border-1);
        border-radius: var(--radius-md); padding: 8px 12px;
        font-family: var(--font-mono); font-size: 0.78rem; color: var(--text-1);
        outline: none; cursor: pointer; transition: border-color .15s;
      }
      .n-select:focus { border-color: var(--accent); }

      /* Licznik */
      .n-count { font-size: 0.73rem; color: var(--text-2); margin-bottom: 14px; }
      .n-count strong { color: var(--text-0); }

      /* ── Siatka notatek ── */
      .n-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
        gap: 12px;
      }

      /* ── Karta notatki ── */
      .n-card {
        display: flex; flex-direction: column; gap: 8px;
        background: var(--bg-1); border: 1px solid var(--border-1);
        border-radius: var(--radius-lg); padding: 14px 16px;
        transition: border-color .15s;
      }
      .n-card:hover { border-color: var(--border-0); }

      .n-card-head {
        display: flex; justify-content: space-between;
        align-items: flex-start; gap: 8px;
      }
      .n-card-meta { display: flex; align-items: center; gap: 7px; flex-wrap: wrap; }
      .n-card-company {
        display: flex; align-items: center; gap: 5px;
        font-family: var(--font-display); font-weight: 700;
        font-size: 0.84rem; color: var(--text-0);
      }
      .n-card-dot { color: var(--accent); font-size: 0.7rem; }
      .n-badge {
        font-family: var(--font-mono); font-size: 0.6rem; font-weight: 700;
        letter-spacing: .07em; padding: 2px 7px;
        border-radius: 100px; border: 1px solid; white-space: nowrap;
      }
      .n-badge--ext {
        color: var(--cyan); border-color: rgba(0,212,212,.3);
        background: rgba(0,212,212,.07);
      }
      .n-card-date {
        font-family: var(--font-mono); font-size: 0.65rem;
        color: var(--text-3); white-space: nowrap; flex-shrink: 0;
      }
      .n-card-offer {
        font-size: 0.78rem; color: var(--text-1);
        padding: 6px 8px; background: var(--bg-2);
        border-radius: var(--radius-sm); border-left: 2px solid var(--border-0);
      }
      .n-card-link {
        font-family: var(--font-mono); font-size: 0.7rem; color: var(--accent);
        text-decoration: none; word-break: break-all;
        transition: opacity .15s;
      }
      .n-card-link:hover { opacity: .75; }
      .n-card-content-wrap { display: flex; flex-direction: column; gap: 4px; }
      .n-card-content {
        font-size: 0.79rem; color: var(--text-1); line-height: 1.6; margin: 0;
      }
      .n-card-content--clamped {
        display: -webkit-box; -webkit-line-clamp: 3;
        -webkit-box-orient: vertical; overflow: hidden;
      }
      .n-card-expand {
        background: none; border: none; color: var(--text-3);
        font-family: var(--font-mono); font-size: 0.68rem;
        cursor: pointer; padding: 0; align-self: flex-start;
        transition: color .15s;
      }
      .n-card-expand:hover { color: var(--accent); }

      /* ── Skeleton ── */
      .n-skel {
        height: 120px; border-radius: var(--radius-lg);
        background: linear-gradient(90deg, var(--bg-2) 25%, var(--bg-3) 50%, var(--bg-2) 75%);
        background-size: 200% 100%;
        animation: shimmer 1.5s ease infinite;
      }
      .n-skel--company { height: 80px; }

      /* ── Firmy ── */
      .n-companies-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
        gap: 10px;
      }
      .n-company-card {
        background: var(--bg-1); border: 1px solid var(--border-1);
        border-radius: var(--radius-lg); padding: 14px 16px;
        display: flex; flex-direction: column; gap: 8px;
        transition: border-color .15s;
      }
      .n-company-card:hover { border-color: var(--border-0); }
      .n-company-head { display: flex; align-items: center; gap: 7px; }
      .n-company-dot { color: var(--accent); font-size: 0.8rem; flex-shrink: 0; }
      .n-company-name {
        font-family: var(--font-display); font-weight: 700;
        font-size: 0.85rem; color: var(--text-0);
      }
      .n-company-dates { display: flex; flex-wrap: wrap; gap: 5px; }
      .n-company-date-chip {
        font-family: var(--font-mono); font-size: 0.67rem; color: var(--text-2);
        background: var(--bg-2); border: 1px solid var(--border-1);
        padding: 2px 8px; border-radius: var(--radius-sm);
      }
      .n-company-date-chip--empty { color: var(--text-3); font-style: italic; }
      .n-company-count {
        font-family: var(--font-mono); font-size: 0.68rem; color: var(--text-3);
      }

      /* ── Modal ── */
      .n-modal-backdrop {
        position: fixed; inset: 0; z-index: 200;
        background: rgba(0,0,0,0.65); backdrop-filter: blur(4px);
        display: flex; align-items: center; justify-content: center;
        padding: 16px;
      }
      .n-modal {
        background: var(--bg-1); border: 1px solid var(--border-1);
        border-radius: var(--radius-xl); width: 100%; max-width: 440px;
        box-shadow: var(--shadow-lg);
      }
      .n-modal-head {
        display: flex; justify-content: space-between; align-items: flex-start;
        padding: 20px 20px 16px; border-bottom: 1px solid var(--border-1);
      }
      .n-modal-title {
        font-family: var(--font-display); font-weight: 800;
        font-size: 1rem; color: var(--text-0); margin-bottom: 3px;
      }
      .n-modal-sub { font-size: 0.76rem; color: var(--text-2); }
      .n-modal-close {
        background: none; border: none; color: var(--text-3);
        cursor: pointer; font-size: 0.8rem; padding: 4px;
        transition: color .15s; flex-shrink: 0;
      }
      .n-modal-close:hover { color: var(--text-0); }

      .n-modal-body { padding: 20px; display: flex; flex-direction: column; gap: 16px; }
      .n-field { display: flex; flex-direction: column; gap: 6px; }
      .n-label {
        font-family: var(--font-mono); font-size: 0.73rem; color: var(--text-2);
      }
      .n-required { color: var(--accent); }
      .n-optional { color: var(--text-3); font-size: 0.68rem; }
      .n-input {
        padding: 9px 12px;
        background: var(--bg-2); border: 1px solid var(--border-1);
        border-radius: var(--radius-md);
        font-family: var(--font-mono); font-size: 0.82rem; color: var(--text-0);
        outline: none; transition: border-color .15s, box-shadow .15s;
      }
      .n-input::placeholder { color: var(--text-3); }
      .n-input:focus { border-color: var(--accent); box-shadow: 0 0 0 2px var(--accent-glow); }
      .n-input--err { border-color: var(--red); }
      .n-err { font-family: var(--font-mono); font-size: 0.7rem; color: var(--red); }

      .n-modal-foot {
        display: flex; justify-content: flex-end; gap: 8px;
        padding-top: 4px;
      }

      /* Przyciski lokalne */
      .btn-primary, .btn-ghost {
        display: inline-flex; align-items: center; gap: 6px;
        font-family: var(--font-mono); font-weight: 600;
        border-radius: var(--radius-md); border: 1px solid;
        cursor: pointer; transition: background .15s, border-color .15s, box-shadow .15s;
        white-space: nowrap;
      }
      .btn-primary {
        padding: 8px 16px; font-size: 0.8rem;
        background: var(--accent); color: #000; border-color: var(--accent);
      }
      .btn-primary:hover:not(:disabled) { background: var(--accent-dim); box-shadow: var(--shadow-accent); }
      .btn-primary:disabled { opacity: .5; cursor: not-allowed; }
      .btn-ghost {
        padding: 8px 14px; font-size: 0.8rem;
        background: transparent; color: var(--text-1); border-color: var(--border-1);
      }
      .btn-ghost:hover:not(:disabled) { background: var(--bg-2); color: var(--text-0); }
      .btn-ghost:disabled { opacity: .5; cursor: not-allowed; }
      .btn-sm { padding: 6px 12px !important; font-size: 0.74rem !important; }

      /* Spinner */
      .n-spin {
        display: inline-block; width: 12px; height: 12px;
        border: 2px solid transparent; border-top-color: currentColor;
        border-radius: 50%; animation: spin .6s linear infinite;
      }
    `})}export{q as default};
