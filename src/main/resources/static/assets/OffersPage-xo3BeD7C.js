import{j as o,u as Q,a as ee,r as n,e as T,o as R,t as K}from"./index-DlloBimL.js";import{a as oe,u as ae,p as re,b as D,c as te,n as F,s as se,f as ne,t as le,d as ie}from"./index-DqRoP1O5.js";import{P as ce,E as Y,C as de,B as pe}from"./index-CFHmIrxB.js";import{P as fe}from"./PortalTabs-TUmy03Fd.js";const me=[{value:"newest",label:"↓ Najnowsze"},{value:"oldest",label:"↑ Najstarsze"}],ue=["Wszystkie","Trainee","Junior","Mid","Senior","Expert"],be=["Wszystkie","Nieprzydatne/Duplikat","Normalne"],_=["createdAt","fetchedAt","updatedAt"];function xe(r){for(const c of _)if(r[c])return r[c];return null}function ze(){const[r,c]=oe();return o.jsx(ge,{source:r,tabs:o.jsx(fe,{value:r,onChange:c})},r)}function ge({source:r,tabs:c}){ae("Oferty pracy");const b=Q(),f=re(r).label,s=ee(e=>e.user)?.role==="ADMIN",{data:i,loading:d,error:p,execute:v}=D(()=>R.getAll(r),{immediate:!0,deps:[r]}),{data:j}=D(T.getUselessOfferIds,{immediate:s}),{data:y}=D(T.getDuplicateOfferIds,{immediate:s}),{data:m}=D(T.getFlagDominant,{immediate:s}),u=n.useMemo(()=>new Set(j??[]),[j]),E=n.useMemo(()=>new Set(y??[]),[y]),k=n.useMemo(()=>new Set([...u,...E]),[u,E]),z=n.useMemo(()=>(i??[]).filter(e=>k.has(e.id)).length,[i,k]),q=n.useMemo(()=>{if(!m)return{};const e={};for(const[t,a]of Object.entries(m))e[Number(t)]=a;return e},[m]),[A,O]=n.useState(""),[L,Z]=n.useState("newest"),[w,I]=n.useState("Wszystkie"),[x,M]=n.useState("Wszystkie"),[g,N]=n.useState(new Set),[S,W]=n.useState(null),[B,U]=n.useState(!1),C=te(A,280),h=n.useMemo(()=>{let e=i??[];if(w!=="Wszystkie"&&(e=e.filter(a=>F(a.level)===w)),s&&x==="Nieprzydatne/Duplikat"&&(e=e.filter(a=>k.has(a.id))),s&&x==="Normalne"&&(e=e.filter(a=>!k.has(a.id))),C.trim()){const a=C.toLowerCase();e=e.filter(l=>l.title?.toLowerCase().includes(a)||(l.companyName??l.company)?.toLowerCase().includes(a)||l.city?.toLowerCase().includes(a))}const t=_.find(a=>e.some(l=>l[a]))??"createdAt";return se(e,t,L==="newest"?"desc":"asc")},[i,w,C,L,s,x,k]),H=n.useCallback(e=>b(`/offers/${e}`),[b]),J=n.useCallback((e,t)=>{t.stopPropagation(),N(a=>{const l=new Set(a);return l.has(e)?l.delete(e):l.add(e),l})},[]),V=()=>N(new Set(h.map(e=>e.id))),X=()=>N(new Set),G=()=>{const e=g.size;W({title:`Usuń ${e} ofert${e===1?"ę":e<5?"y":""}?`,description:"Operacja nieodwracalna — oferty znikną z bazy danych na stałe.",onConfirm:async()=>{U(!0);let t=0,a=0;for(const l of g)try{await R.delete(l),t++}catch{a++}U(!1),N(new Set),v(),a===0?K.success(`Usunięto ${t} ofert`):K.warn(`Usunięto ${t}, błąd przy ${a}`)}})},P=C.trim()!==""||w!=="Wszystkie"||s&&x!=="Wszystkie";return o.jsxs("div",{className:"offers-page",children:[o.jsx(ce,{title:"Oferty pracy",subtitle:i?`${i.length} ofert z ${f}${s&&z>0?` · ${z} nieprzydatnych/duplikatów`:""}`:"",actions:o.jsx("button",{className:"btn btn--secondary btn--sm",onClick:v,disabled:d,children:"↻ Odśwież"})}),c,o.jsxs("div",{className:"of-toolbar",children:[o.jsxs("div",{className:"of-search-wrap",children:[o.jsx("span",{className:"of-search-ico","aria-hidden":"true",children:"⊘"}),o.jsx("input",{className:"of-search",type:"search",placeholder:"Szukaj po tytule, firmie, mieście…",value:A,onChange:e=>O(e.target.value)}),A&&o.jsx("button",{className:"of-clear",onClick:()=>O(""),"aria-label":"Wyczyść",children:"✕"})]}),o.jsx("select",{className:"of-select",value:L,onChange:e=>Z(e.target.value),children:me.map(e=>o.jsx("option",{value:e.value,children:e.label},e.value))})]}),o.jsx("div",{className:"of-levels",children:ue.map(e=>o.jsx("button",{className:`of-pill${w===e?" of-pill--on":""}`,onClick:()=>I(e),children:e},e))}),s&&o.jsx("div",{className:"of-levels of-status-row",children:be.map(e=>{const t=e==="Nieprzydatne/Duplikat";return o.jsxs("button",{className:`of-pill${x===e?" of-pill--on":""}${t&&x!==e?" of-pill--dup-idle":""}`,onClick:()=>M(e),children:[t?`⚑ ${e}`:e,t&&z>0&&o.jsx("span",{className:"of-pill-badge",children:z})]},e)})}),!d&&!p&&i&&o.jsxs("div",{className:"of-count-row",children:[o.jsxs("p",{className:"of-count",children:[o.jsx("strong",{children:h.length}),P?` z ${i.length}`:""," ofert"]}),s&&o.jsx("div",{className:"of-bulk-bar",children:g.size>0?o.jsxs(o.Fragment,{children:[o.jsxs("span",{className:"of-sel-label",children:[g.size," zaznaczonych"]}),o.jsx("button",{className:"of-bulk-btn",onClick:X,children:"Odznacz"}),o.jsx("button",{className:"of-bulk-btn of-bulk-btn--danger",onClick:G,disabled:B,children:B?"…":`⊗ Usuń (${g.size})`})]}):o.jsx("button",{className:"of-bulk-btn",onClick:V,disabled:h.length===0,children:"Zaznacz wszystkie"})})]}),d&&o.jsx("div",{className:"of-grid",children:Array.from({length:12}).map((e,t)=>o.jsx("div",{className:"of-skel",style:{animationDelay:`${t*35}ms`}},t))}),!d&&p&&o.jsx(Y,{icon:"✕",title:"Błąd ładowania ofert",description:p.message??"Nie udało się połączyć z serwerem.",action:o.jsx("button",{className:"btn btn--primary btn--sm",onClick:v,children:"Spróbuj ponownie"})}),!d&&!p&&h.length===0&&o.jsx(Y,{icon:"⊘",title:"Brak ofert",description:P?"Żadna oferta nie pasuje do wybranych kryteriów.":`Brak ofert z ${f} — uruchom scraper lub zaimportuj plik w module Import.`,action:P?o.jsx("button",{className:"btn btn--ghost btn--sm",onClick:()=>{O(""),I("Wszystkie"),M("Wszystkie")},children:"Wyczyść filtry"}):null}),!d&&!p&&h.length>0&&o.jsx("div",{className:"of-grid",children:h.map((e,t)=>o.jsx(he,{offer:e,isAdmin:s,flagType:s?q[e.id]:null,selected:g.has(e.id),onSelect:a=>J(e.id,a),onClick:()=>H(e.id),style:{animationDelay:`${Math.min(t*28,400)}ms`}},e.id))}),S&&o.jsx(de,{title:S.title,description:S.description,onConfirm:S.onConfirm,onClose:()=>W(null)}),o.jsx(ve,{})]})}function he({offer:r,isAdmin:c,flagType:b,selected:f,onSelect:$,onClick:s,style:i}){const d=F(r.level),p=ne(r.salaryRange??r.salary),v=xe(r),j=r.companyName??r.company??"—",y=!!b;let m="of-card animate-fade-in";return y&&(m+=" of-card--admin-dup"),f&&(m+=" of-card--selected"),o.jsxs("article",{className:m,onClick:s,style:i,tabIndex:0,role:"button",onKeyDown:u=>u.key==="Enter"&&s(),children:[c&&o.jsx("span",{className:`of-check${f?" of-check--on":""}`,onClick:$,role:"checkbox","aria-checked":f,tabIndex:0,onKeyDown:u=>u.key===" "&&$(u),children:f?"▣":"□"}),y&&o.jsx("span",{className:`of-flag-stamp of-flag-stamp--${b.toLowerCase()}`,children:b==="DUPLICATE"?"DUPLIKAT":"NIEPRZYDATNA"}),o.jsx("div",{className:"of-card-top",children:o.jsx(pe,{level:d})}),o.jsx("h3",{className:"of-card-title",children:le(r.title,72)}),o.jsxs("p",{className:"of-card-company",children:[o.jsx("span",{"aria-hidden":"true",style:{color:"var(--text-3)",fontSize:".72rem"},children:"◉"}),j]}),o.jsxs("div",{className:"of-chips",children:[r.city&&o.jsxs("span",{className:"of-chip",children:["◎ ",r.city]}),p&&o.jsxs("span",{className:"of-chip of-chip--salary",children:["₿ ",p]})]}),o.jsxs("div",{className:"of-card-foot",children:[o.jsx("time",{className:"of-card-date",children:ie(v)}),o.jsx("span",{className:"of-card-arrow","aria-hidden":"true",children:"→"})]})]})}function ve(){return o.jsx("style",{children:`
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
      .of-levels { display: flex; gap: 5px; flex-wrap: wrap; margin-bottom: 10px; }
      .of-status-row { margin-bottom: 16px; }
      .of-pill {
        display: inline-flex; align-items: center; gap: 5px;
        padding: 4px 13px; border-radius: 100px;
        border: 1px solid var(--border-1); background: var(--bg-2);
        color: var(--text-2); font-family: var(--font-mono); font-size: 0.7rem;
        cursor: pointer; transition: border-color .15s, color .15s, background .15s;
        white-space: nowrap;
      }
      .of-pill:hover { border-color: var(--accent); color: var(--accent); }
      .of-pill--on   { background: var(--accent); color: #000; border-color: var(--accent); font-weight: 700; }
      .of-pill--dup-idle { color: var(--red); border-color: rgba(239,68,68,.3); }
      .of-pill--dup-idle:hover { border-color: var(--red); background: rgba(239,68,68,.07); }
      .of-pill-badge {
        font-size: 0.6rem; font-weight: 700;
        background: rgba(239,68,68,.2); color: var(--red);
        border-radius: 100px; padding: 1px 5px;
        min-width: 16px; text-align: center;
      }
      .of-pill--on .of-pill-badge { background: rgba(0,0,0,.2); color: #000; }

      /* Licznik + bulk bar */
      .of-count-row {
        display: flex; align-items: center; justify-content: space-between;
        gap: 8px; margin-bottom: 14px; flex-wrap: wrap;
      }
      .of-count { font-size: 0.73rem; color: var(--text-2); }
      .of-count strong { color: var(--text-0); }
      .of-bulk-bar { display: flex; align-items: center; gap: 8px; }
      .of-sel-label {
        font-family: var(--font-mono); font-size: 0.72rem; color: var(--text-2);
      }
      .of-bulk-btn {
        padding: 5px 12px; border-radius: var(--radius-md); border: 1px solid var(--border-1);
        background: var(--bg-2); color: var(--text-1);
        font-family: var(--font-mono); font-size: 0.72rem; cursor: pointer;
        transition: background .15s, border-color .15s, color .15s;
      }
      .of-bulk-btn:hover:not(:disabled) { background: var(--bg-3); color: var(--text-0); }
      .of-bulk-btn:disabled { opacity: .45; cursor: not-allowed; }
      .of-bulk-btn--danger {
        color: var(--red); border-color: rgba(239,68,68,.35);
        background: rgba(239,68,68,.05);
      }
      .of-bulk-btn--danger:hover:not(:disabled) {
        background: rgba(239,68,68,.12); border-color: var(--red);
      }

      /* Siatka */
      .of-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(288px, 1fr));
        gap: 12px;
      }

      /* Karta */
      .of-card {
        position: relative;
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
      .of-card--admin-dup {
        border-color: rgba(239,68,68,.5);
        background: rgba(239,68,68,.04);
        box-shadow: 0 0 0 1px rgba(239,68,68,.15);
      }
      .of-card--admin-dup:hover,
      .of-card--admin-dup:focus-visible {
        border-color: var(--red);
        box-shadow: 0 4px 16px rgba(239,68,68,.25);
        transform: translateY(-2px);
      }

      /* Stempel flagi */
      .of-flag-stamp {
        position: absolute; top: 8px; right: 36px;
        font-family: var(--font-mono); font-size: 0.55rem; font-weight: 800;
        letter-spacing: .1em; padding: 2px 7px;
        border-radius: var(--radius-sm); border: 1px solid;
        pointer-events: none;
      }
      .of-flag-stamp--duplicate {
        color: var(--red); border-color: rgba(239,68,68,.5);
        background: rgba(239,68,68,.1);
      }
      .of-flag-stamp--useless {
        color: var(--text-2); border-color: var(--border-1);
        background: var(--bg-3);
      }
      .of-card--selected {
        border-color: var(--accent) !important;
        box-shadow: 0 0 0 2px var(--accent-glow) !important;
      }

      /* Checkbox */
      .of-check {
        position: absolute; top: 10px; right: 10px;
        font-size: 0.9rem; color: var(--text-3);
        cursor: pointer; z-index: 2; line-height: 1;
        transition: color .15s;
        user-select: none;
      }
      .of-check:hover { color: var(--accent); }
      .of-check--on   { color: var(--accent); }

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
    `})}export{ze as default};
