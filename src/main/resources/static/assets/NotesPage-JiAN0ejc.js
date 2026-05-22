import{a as z,r as b,j as a,t as j}from"./index-B4E70c8-.js";import{u as C,b as w,n as v,c as S}from"./index-BBvXT-jJ.js";import{P as k,E as h}from"./index-CsYv5Omc.js";import{a as A,b as D,t as F}from"./index-Cvq4Ykd3.js";const T=[{id:"notes",label:"Notatki",icon:"◷"},{id:"companies",label:"Firmy i daty",icon:"◉"}];function $({userId:n,onClose:t,onCreated:m}){const[x,u]=b.useState({companyName:"",offerUrl:""}),[l,c]=b.useState({}),[s,o]=b.useState(!1),f=()=>{const r={};return x.companyName.trim()||(r.companyName="Pole wymagane"),r},d=r=>{const{name:p,value:g}=r.target;u(y=>({...y,[p]:g})),l[p]&&c(y=>({...y,[p]:void 0}))},i=async r=>{r.preventDefault();const p=f();if(Object.keys(p).length){c(p);return}o(!0);try{await v.createExternal(n,x.companyName.trim(),x.offerUrl.trim()||void 0),j.success(`Notatka dla "${x.companyName.trim()}" dodana`),m(),t()}catch(g){j.error(g?.message??"Błąd tworzenia notatki")}finally{o(!1)}},e=r=>{r.key==="Escape"&&t()};return a.jsx("div",{className:"n-modal-backdrop",onClick:t,onKeyDown:e,children:a.jsxs("div",{className:"n-modal animate-fade-in",onClick:r=>r.stopPropagation(),role:"dialog","aria-modal":"true","aria-label":"Nowa notatka zewnętrzna",children:[a.jsxs("div",{className:"n-modal-head",children:[a.jsxs("div",{children:[a.jsx("h2",{className:"n-modal-title",children:"Nowa notatka zewnętrzna"}),a.jsx("p",{className:"n-modal-sub",children:"Aplikacja spoza systemu (własne źródło)"})]}),a.jsx("button",{className:"n-modal-close",onClick:t,"aria-label":"Zamknij",children:"✕"})]}),a.jsxs("form",{onSubmit:i,className:"n-modal-body",noValidate:!0,children:[a.jsxs("div",{className:"n-field",children:[a.jsxs("label",{className:"n-label",htmlFor:"nm-company",children:["Nazwa firmy ",a.jsx("span",{className:"n-required",children:"*"})]}),a.jsx("input",{id:"nm-company",name:"companyName",className:`n-input${l.companyName?" n-input--err":""}`,placeholder:"np. Acme Corp",value:x.companyName,onChange:d,autoFocus:!0,disabled:s}),l.companyName&&a.jsx("span",{className:"n-err",children:l.companyName})]}),a.jsxs("div",{className:"n-field",children:[a.jsxs("label",{className:"n-label",htmlFor:"nm-url",children:["URL oferty ",a.jsx("span",{className:"n-optional",children:"(opcjonalnie)"})]}),a.jsx("input",{id:"nm-url",name:"offerUrl",type:"url",className:"n-input",placeholder:"https://example.com/job/123",value:x.offerUrl,onChange:d,disabled:s})]}),a.jsxs("div",{className:"n-modal-foot",children:[a.jsx("button",{type:"button",className:"btn-ghost",onClick:t,disabled:s,children:"Anuluj"}),a.jsx("button",{type:"submit",className:"btn-primary",disabled:s,children:s?a.jsxs(a.Fragment,{children:[a.jsx("span",{className:"n-spin"}),"Zapisywanie…"]}):"✓ Dodaj notatkę"})]})]})]})})}function L({note:n,style:t}){return a.jsxs("article",{className:"n-card animate-fade-in",style:t,children:[a.jsxs("div",{className:"n-card-head",children:[a.jsxs("span",{className:"n-card-company",children:[a.jsx("span",{className:"n-card-dot",children:"◉"}),n.companyName??"—"]}),a.jsx("time",{className:"n-card-date",children:D(n.appliedAt)})]}),n.offerUrl&&a.jsxs("a",{href:n.offerUrl,target:"_blank",rel:"noopener noreferrer",className:"n-card-link",onClick:m=>m.stopPropagation(),children:["↗ ",F(n.offerUrl,60)]}),n.offerId&&a.jsxs("p",{className:"n-card-offerid",children:["ID oferty: ",n.offerId]})]})}function M({notes:n,loading:t,error:m,reload:x}){const[u,l]=b.useState(""),[c,s]=b.useState("Wszystkie"),o=S(u,250),f=b.useMemo(()=>{if(!n)return[];const e=new Map;return n.forEach(r=>{if(r.companyName){const p=r.companyName.toLowerCase();e.has(p)||e.set(p,r.companyName)}}),["Wszystkie",...Array.from(e.values()).sort((r,p)=>r.localeCompare(p,"pl"))]},[n]),d=b.useMemo(()=>{let e=n??[];if(c!=="Wszystkie"&&(e=e.filter(r=>r.companyName?.toLowerCase()===c.toLowerCase())),o.trim()){const r=o.toLowerCase();e=e.filter(p=>p.companyName?.toLowerCase().includes(r)||p.offerUrl?.toLowerCase().includes(r))}return e},[n,c,o]),i=o.trim()!==""||c!=="Wszystkie";return t?a.jsx("div",{className:"n-grid",children:Array.from({length:6}).map((e,r)=>a.jsx("div",{className:"n-skel",style:{animationDelay:`${r*40}ms`}},r))}):m?a.jsx(h,{icon:"✕",title:"Błąd ładowania notatek",description:m.message??"Nie udało się pobrać notatek.",action:a.jsx("button",{className:"btn-primary btn-sm",onClick:x,children:"Spróbuj ponownie"})}):a.jsxs(a.Fragment,{children:[a.jsxs("div",{className:"n-toolbar",children:[a.jsxs("div",{className:"n-search-wrap",children:[a.jsx("span",{className:"n-search-ico",children:"⊘"}),a.jsx("input",{className:"n-search",type:"search",placeholder:"Szukaj po firmie, tytule, treści…",value:u,onChange:e=>l(e.target.value)}),u&&a.jsx("button",{className:"n-search-clear",onClick:()=>l(""),children:"✕"})]}),a.jsx("select",{className:"n-select",value:c,onChange:e=>s(e.target.value),children:f.map(e=>a.jsx("option",{value:e,children:e},e))})]}),n&&a.jsxs("p",{className:"n-count",children:[a.jsx("strong",{children:d.length}),i?` z ${n.length}`:""," notatek"]}),d.length===0&&a.jsx(h,{icon:"◷",title:"Brak notatek",description:i?"Żadna notatka nie pasuje do kryteriów.":"Nie masz jeszcze żadnych notatek aplikacyjnych.",action:i?a.jsx("button",{className:"btn-ghost btn-sm",onClick:()=>{l(""),s("Wszystkie")},children:"Wyczyść filtry"}):null}),d.length>0&&a.jsx("div",{className:"n-grid",children:d.map((e,r)=>a.jsx(L,{note:e,style:{animationDelay:`${Math.min(r*30,350)}ms`}},e.id??r))})]})}function U({userId:n}){const{data:t,loading:m,error:x,execute:u}=w(b.useCallback(()=>v.getCompaniesWithDates(n),[n]),{immediate:!!n});if(m)return a.jsx("div",{className:"n-companies-grid",children:Array.from({length:8}).map((s,o)=>a.jsx("div",{className:"n-skel n-skel--company",style:{animationDelay:`${o*35}ms`}},o))});if(x)return a.jsx(h,{icon:"✕",title:"Błąd ładowania",description:x.message??"Nie udało się pobrać listy firm.",action:a.jsx("button",{className:"btn-primary btn-sm",onClick:u,children:"Spróbuj ponownie"})});const l=t&&typeof t=="object"&&!Array.isArray(t)?Object.entries(t).map(([s,o])=>({companyName:s,dates:o})):[];if(!l.length)return a.jsx(h,{icon:"◉",title:"Brak firm",description:"Gdy zaczniesz aplikować, firmy pojawią się tutaj wraz z datami."});const c=[...l].sort((s,o)=>{const f=s.dates?.at(-1)?new Date(s.dates.at(-1)):0;return(o.dates?.at(-1)?new Date(o.dates.at(-1)):0)-f});return a.jsxs(a.Fragment,{children:[a.jsxs("p",{className:"n-count",children:[a.jsx("strong",{children:c.length})," firm"]}),a.jsx("div",{className:"n-companies-grid",children:c.map(({companyName:s,dates:o},f)=>a.jsxs("div",{className:"n-company-card animate-fade-in",style:{animationDelay:`${Math.min(f*30,350)}ms`},children:[a.jsxs("div",{className:"n-company-head",children:[a.jsx("span",{className:"n-company-dot",children:"◉"}),a.jsx("span",{className:"n-company-name",children:s})]}),a.jsx("div",{className:"n-company-dates",children:o?.length>0?o.map((d,i)=>a.jsx("span",{className:"n-company-date-chip",children:A(d)},i)):a.jsx("span",{className:"n-company-date-chip n-company-date-chip--empty",children:"brak daty"})}),a.jsxs("p",{className:"n-company-count",children:[o?.length??0," ",o?.length===1?"aplikacja":"aplikacji"]})]},s))})]})}function q(){C("Notatki aplikacyjne");const t=z(i=>i.user)?.id,[m,x]=b.useState("notes"),[u,l]=b.useState(!1),{data:c,loading:s,error:o,execute:f}=w(b.useCallback(()=>v.getAll(t),[t]),{immediate:!!t}),d=c?.length??0;return t?a.jsxs("div",{className:"notes-page animate-fade-in",children:[a.jsx(k,{title:"Notatki aplikacyjne",subtitle:d>0?`${d} notatek łącznie`:"Twoje aplikacje",actions:a.jsxs("div",{style:{display:"flex",gap:8},children:[a.jsx("button",{className:"btn-ghost btn-sm",onClick:f,disabled:s,children:"↻ Odśwież"}),a.jsx("button",{className:"btn-primary btn-sm",onClick:()=>l(!0),children:"⊕ Nowa notatka"})]})}),a.jsx("div",{className:"n-tabs",role:"tablist",children:T.map(i=>{const e=i.id==="notes"?d:null;return a.jsxs("button",{role:"tab","aria-selected":m===i.id,className:`n-tab${m===i.id?" n-tab--active":""}`,onClick:()=>x(i.id),children:[a.jsx("span",{children:i.icon}),i.label,e!==null&&e>0&&a.jsx("span",{className:`n-tab-count${m===i.id?" n-tab-count--active":""}`,children:e})]},i.id)})}),m==="notes"?a.jsx(M,{notes:c,loading:s,error:o,reload:f}):a.jsx(U,{userId:t}),u&&a.jsx($,{userId:t,onClose:()=>l(!1),onCreated:f}),a.jsx(N,{})]}):a.jsxs("div",{className:"notes-page animate-fade-in",children:[a.jsx(k,{title:"Notatki aplikacyjne",subtitle:"Twoje aplikacje"}),a.jsx(h,{icon:"⊙",title:"Brak danych sesji",description:"Wyloguj się i zaloguj ponownie."}),a.jsx(N,{})]})}function N(){return a.jsx("style",{children:`
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
      .n-card-offerid {
        font-family: var(--font-mono); font-size: 0.68rem; color: var(--text-3); margin: 0;
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
