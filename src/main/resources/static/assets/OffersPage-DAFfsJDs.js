import{u as z,r as x,j as e}from"./index-B4E70c8-.js";import{u as N,b as S,c as C,o as D}from"./index-BBvXT-jJ.js";import{P as L,E as y,B as A}from"./index-CsYv5Omc.js";import{n as w,s as E,f as O,t as T,a as W}from"./index-Cvq4Ykd3.js";const $=[{value:"newest",label:"↓ Najnowsze"},{value:"oldest",label:"↑ Najstarsze"}],B=["Wszystkie","Trainee","Junior","Mid","Senior","Expert"],j=["createdAt","fetchedAt","updatedAt"];function K(r){for(const o of j)if(r[o])return r[o];return null}function q(){N("Oferty pracy");const r=z(),{data:o,loading:t,error:s,execute:d}=S(D.getAll,{immediate:!0}),[p,i]=x.useState(""),[f,v]=x.useState("newest"),[m,h]=x.useState("Wszystkie"),u=C(p,280),b=x.useMemo(()=>{let a=o??[];if(m!=="Wszystkie"&&(a=a.filter(n=>w(n.level)===m)),u.trim()){const n=u.toLowerCase();a=a.filter(c=>c.title?.toLowerCase().includes(n)||(c.companyName??c.company)?.toLowerCase().includes(n)||c.city?.toLowerCase().includes(n))}const l=j.find(n=>a.some(c=>c[n]))??"createdAt";return E(a,l,f==="newest"?"desc":"asc")},[o,m,u,f]),k=x.useCallback(a=>r(`/offers/${a}`),[r]),g=u.trim()!==""||m!=="Wszystkie";return e.jsxs("div",{className:"offers-page",children:[e.jsx(L,{title:"Oferty pracy",subtitle:o?`${o.length} ofert w bazie`:"",actions:e.jsx("button",{className:"btn btn--secondary btn--sm",onClick:d,disabled:t,children:"↻ Odśwież"})}),e.jsxs("div",{className:"of-toolbar",children:[e.jsxs("div",{className:"of-search-wrap",children:[e.jsx("span",{className:"of-search-ico","aria-hidden":"true",children:"⊘"}),e.jsx("input",{className:"of-search",type:"search",placeholder:"Szukaj po tytule, firmie, mieście…",value:p,onChange:a=>i(a.target.value)}),p&&e.jsx("button",{className:"of-clear",onClick:()=>i(""),"aria-label":"Wyczyść",children:"✕"})]}),e.jsx("select",{className:"of-select",value:f,onChange:a=>v(a.target.value),children:$.map(a=>e.jsx("option",{value:a.value,children:a.label},a.value))})]}),e.jsx("div",{className:"of-levels",children:B.map(a=>e.jsx("button",{className:`of-pill${m===a?" of-pill--on":""}`,onClick:()=>h(a),children:a},a))}),!t&&!s&&o&&e.jsxs("p",{className:"of-count",children:[e.jsx("strong",{children:b.length}),g?` z ${o.length}`:""," ofert"]}),t&&e.jsx("div",{className:"of-grid",children:Array.from({length:12}).map((a,l)=>e.jsx("div",{className:"of-skel",style:{animationDelay:`${l*35}ms`}},l))}),!t&&s&&e.jsx(y,{icon:"✕",title:"Błąd ładowania ofert",description:s.message??"Nie udało się połączyć z serwerem.",action:e.jsx("button",{className:"btn btn--primary btn--sm",onClick:d,children:"Spróbuj ponownie"})}),!t&&!s&&b.length===0&&e.jsx(y,{icon:"⊘",title:"Brak ofert",description:g?"Żadna oferta nie pasuje do wybranych kryteriów.":"Baza jest pusta — zaimportuj dane przez moduł Import.",action:g?e.jsx("button",{className:"btn btn--ghost btn--sm",onClick:()=>{i(""),h("Wszystkie")},children:"Wyczyść filtry"}):null}),!t&&!s&&b.length>0&&e.jsx("div",{className:"of-grid",children:b.map((a,l)=>e.jsx(P,{offer:a,onClick:()=>k(a.id),style:{animationDelay:`${Math.min(l*28,400)}ms`}},a.id))}),e.jsx(Y,{})]})}function P({offer:r,onClick:o,style:t}){const s=w(r.level),d=O(r.salaryRange??r.salary),p=K(r),i=r.isDuplicate??r.duplicate??!1,f=r.companyName??r.company??"—";return e.jsxs("article",{className:`of-card animate-fade-in${i?" of-card--dup":""}`,onClick:o,style:t,tabIndex:0,role:"button",onKeyDown:v=>v.key==="Enter"&&o(),children:[e.jsxs("div",{className:"of-card-top",children:[e.jsx(A,{level:s}),i&&e.jsx("span",{className:"of-dup",children:"DUPLIKAT"})]}),e.jsx("h3",{className:"of-card-title",children:T(r.title,72)}),e.jsxs("p",{className:"of-card-company",children:[e.jsx("span",{"aria-hidden":"true",style:{color:"var(--text-3)",fontSize:".72rem"},children:"◉"}),f]}),e.jsxs("div",{className:"of-chips",children:[r.city&&e.jsxs("span",{className:"of-chip",children:["◎ ",r.city]}),d&&e.jsxs("span",{className:"of-chip of-chip--salary",children:["₿ ",d]})]}),e.jsxs("div",{className:"of-card-foot",children:[e.jsx("time",{className:"of-card-date",children:W(p)}),e.jsx("span",{className:"of-card-arrow","aria-hidden":"true",children:"→"})]})]})}function Y(){return e.jsx("style",{children:`
      .offers-page { max-width: 1080px; }

      /* Toolbar */
      .of-toolbar {
        display: flex; gap: 8px; align-items: center;
        margin-bottom: 10px; flex-wrap: wrap;
      }
      .of-search-wrap { position: relative; flex: 1; min-width: 200px; }
      .of-search-ico  {
        position: absolute; left: 11px; top: 50%; transform: translateY(-50%);
        color: var(--text-3); font-size: 0.82rem; pointer-events: none;
      }
      .of-search {
        width: 100%; padding: 8px 34px 8px 32px;
        background: var(--bg-2); border: 1px solid var(--border-1);
        border-radius: var(--radius-md); font-family: var(--font-mono);
        font-size: 0.81rem; color: var(--text-0); outline: none;
        transition: border-color .15s, box-shadow .15s;
      }
      .of-search::placeholder { color: var(--text-3); }
      .of-search::-webkit-search-cancel-button { display: none; }
      .of-search:focus { border-color: var(--accent); box-shadow: 0 0 0 2px var(--accent-glow); }
      .of-clear {
        position: absolute; right: 10px; top: 50%; transform: translateY(-50%);
        background: none; border: none; color: var(--text-3); cursor: pointer;
        font-size: 0.68rem; padding: 2px; transition: color .15s;
      }
      .of-clear:hover { color: var(--text-0); }
      .of-select {
        background: var(--bg-2); border: 1px solid var(--border-1);
        border-radius: var(--radius-md); padding: 8px 12px;
        font-family: var(--font-mono); font-size: 0.78rem; color: var(--text-1);
        outline: none; cursor: pointer; flex-shrink: 0;
        transition: border-color .15s;
      }
      .of-select:focus { border-color: var(--accent); }

      /* Level pills */
      .of-levels { display: flex; gap: 5px; flex-wrap: wrap; margin-bottom: 16px; }
      .of-pill {
        padding: 4px 13px; border-radius: 100px;
        border: 1px solid var(--border-1); background: var(--bg-2);
        color: var(--text-2); font-family: var(--font-mono); font-size: 0.7rem;
        cursor: pointer; transition: border-color .15s, color .15s, background .15s;
        white-space: nowrap;
      }
      .of-pill:hover { border-color: var(--accent); color: var(--accent); }
      .of-pill--on   { background: var(--accent); color: #000; border-color: var(--accent); font-weight: 700; }

      /* Licznik */
      .of-count { font-size: 0.73rem; color: var(--text-2); margin-bottom: 14px; }
      .of-count strong { color: var(--text-0); }

      /* Siatka */
      .of-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(288px, 1fr));
        gap: 12px;
      }

      /* Karta */
      .of-card {
        display: flex; flex-direction: column; gap: 7px;
        background: var(--bg-1); border: 1px solid var(--border-1);
        border-radius: var(--radius-lg); padding: 1.05rem 1.1rem;
        cursor: pointer; outline: none;
        transition: border-color .15s, transform .15s, box-shadow .15s;
      }
      .of-card:hover,
      .of-card:focus-visible {
        border-color: var(--accent);
        transform: translateY(-2px);
        box-shadow: var(--shadow-accent);
      }
      .of-card--dup { opacity: .5; }
      .of-card--dup:hover { opacity: .72; }

      .of-card-top {
        display: flex; align-items: center;
        justify-content: space-between; gap: 6px;
      }
      .of-dup {
        font-family: var(--font-mono); font-size: 0.58rem; font-weight: 700;
        letter-spacing: .09em; color: var(--red);
        border: 1px solid rgba(239,68,68,.35); padding: 2px 6px;
        border-radius: var(--radius-sm);
      }
      .of-card-title {
        font-family: var(--font-display); font-weight: 700;
        font-size: 0.89rem; color: var(--text-0); line-height: 1.35; margin: 0;
        display: -webkit-box; -webkit-line-clamp: 2;
        -webkit-box-orient: vertical; overflow: hidden;
      }
      .of-card-company {
        display: flex; align-items: center; gap: 5px;
        font-size: 0.77rem; color: var(--text-2); margin: 0;
      }
      .of-chips { display: flex; flex-wrap: wrap; gap: 5px; }
      .of-chip {
        display: flex; align-items: center; gap: 4px;
        font-family: var(--font-mono); font-size: 0.68rem; color: var(--text-2);
        background: var(--bg-2); border: 1px solid var(--border-1);
        padding: 3px 8px; border-radius: var(--radius-sm);
      }
      .of-chip--salary {
        color: var(--cyan); border-color: rgba(0,212,212,.2);
        background: rgba(0,212,212,.05);
      }
      .of-card-foot {
        display: flex; justify-content: space-between; align-items: center;
        margin-top: auto; padding-top: 8px; border-top: 1px solid var(--border-1);
      }
      .of-card-date  { font-family: var(--font-mono); font-size: 0.66rem; color: var(--text-3); }
      .of-card-arrow { font-size: 0.78rem; color: var(--text-3); transition: color .15s, transform .15s; }
      .of-card:hover  .of-card-arrow,
      .of-card:focus-visible .of-card-arrow { color: var(--accent); transform: translateX(4px); }

      /* Skeleton */
      .of-skel {
        height: 150px; border-radius: var(--radius-lg);
        background: linear-gradient(90deg, var(--bg-2) 25%, var(--bg-3) 50%, var(--bg-2) 75%);
        background-size: 200% 100%;
        animation: shimmer 1.5s ease infinite;
      }
    `})}export{q as default};
