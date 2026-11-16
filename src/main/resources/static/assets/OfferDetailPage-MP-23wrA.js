import{f as C,u as O,a as I,r as o,e as P,j as e,t as i,g as w,o as f}from"./index-DlloBimL.js";import{b as E,u as L,n as q,f as H,d as R,e as W}from"./index-DqRoP1O5.js";import{B as $,C as F}from"./index-CFHmIrxB.js";function J(){const{id:r}=C(),s=O(),l=I(a=>a.user),c=l?.role==="ADMIN",{data:t,loading:z,error:p}=E(()=>f.getById(r),{immediate:!0,deps:[r]}),[d,x]=o.useState(null);o.useEffect(()=>{c&&P.getOfferMarkers(r).then(a=>x(a.data??a)).catch(()=>x([]))},[c,r]),L(t?.title??"Szczegóły oferty");const[g,u]=o.useState(!1),[b,h]=o.useState(!1),[k,y]=o.useState(!1),[D,v]=o.useState(!1),A=async()=>{if(!l?.id){i.warn("Brak ID użytkownika w sesji");return}u(!0);try{await w.applyToOffer(l.id,r),i.success("Aplikacja zapisana!")}catch(a){i.error(a?.message??"Błąd zapisu aplikacji")}finally{u(!1)}},S=async()=>{if(!l?.id){i.warn("Brak ID użytkownika w sesji");return}h(!0);try{await f.markDuplicateById(l.id,r),i.info("Oferta oznaczona jako duplikat"),s(-1)}catch(a){i.error(a?.message??"Błąd oznaczania duplikatu")}finally{h(!1)}},T=async()=>{try{await f.delete(r),i.info("Oferta usunięta"),s(-1)}catch(a){i.error(a?.message??"Błąd usuwania oferty")}},B=async()=>{if(!l?.id){i.warn("Brak ID użytkownika w sesji");return}y(!0);try{await w.markUseless(l.id,r),i.info("Oferta przeniesiona do śmietnika"),s(-1)}catch(a){i.error(a?.message??"Błąd oznaczania oferty")}finally{y(!1)}};if(z)return e.jsx(K,{});if(p||!t){const a=p?.response?.status===404;return e.jsxs("div",{className:"detail-page animate-fade-in",children:[e.jsx("button",{className:"detail-back",onClick:()=>s(-1),children:"← Wróć"}),e.jsxs("div",{className:"detail-err",children:[e.jsx("span",{style:{fontSize:"2.2rem"},children:a?"◌":"✕"}),a?e.jsxs(e.Fragment,{children:[e.jsx("p",{style:{fontWeight:700,color:"var(--text-0)"},children:"Oferta została usunięta z bazy danych"}),e.jsx("p",{style:{fontSize:"0.8rem"},children:"Twoja notatka aplikacyjna pozostaje zapisana."})]}):e.jsx("p",{children:p?.message??"Nie znaleziono oferty."})]})]})}const j=q(t.level),N=H(t.salaryRange??t.salary),M=t.isDuplicate??t.duplicate??!1,U=t.companyName??t.company??"—";return e.jsxs("div",{className:"detail-page animate-fade-in",children:[e.jsxs("div",{className:"detail-nav",children:[e.jsx("button",{className:"detail-back",onClick:()=>s(-1),children:"← Oferty"}),e.jsxs("div",{className:"detail-actions",children:[e.jsx("button",{className:"btn btn--ghost btn--sm",onClick:B,disabled:k,title:"Przenieś do śmietnika",children:k?"…":"🗑 Nieprzydatne"}),e.jsx("button",{className:"btn btn--danger btn--sm",onClick:S,disabled:b,title:"Oznacz jako duplikat",children:b?"…":"⊗ Duplikat"}),c&&e.jsx("button",{className:"btn btn--danger btn--sm detail-delete-btn",onClick:()=>v(!0),title:"Usuń ofertę z bazy",children:"✕ Usuń ofertę"}),e.jsx("button",{className:"btn btn--primary btn--sm",onClick:A,disabled:g,children:g?"…":"✓ Aplikuj"})]})]}),e.jsxs("div",{className:"detail-hero",children:[e.jsxs("div",{className:"detail-hero-left",children:[e.jsxs("div",{className:"detail-badges",children:[e.jsx($,{level:j}),M&&e.jsx("span",{className:"detail-dup-tag",children:"DUPLIKAT"})]}),e.jsx("h1",{className:"detail-title",children:t.title}),e.jsx("p",{className:"detail-company",children:U})]}),N&&e.jsx("div",{className:"detail-salary",children:N})]}),e.jsxs("div",{className:"detail-meta",children:[e.jsx(m,{icon:"◎",label:"Miasto",value:t.city??t.location??"—"}),e.jsx(m,{icon:"⊙",label:"Poziom",value:j}),e.jsx(m,{icon:"◷",label:"Dodano",value:R(t.fetchedAt)}),t.source&&e.jsx(m,{icon:"⊛",label:"Serwis",value:t.source}),t.offerUrl&&e.jsxs("div",{className:"meta-box meta-box--wide",children:[e.jsx("span",{className:"meta-label",children:"Link do oferty"}),e.jsx("a",{href:t.offerUrl,target:"_blank",rel:"noopener noreferrer",className:"meta-link",children:t.offerUrl})]})]}),t.requirements?.length>0&&e.jsxs("section",{className:"detail-section",children:[e.jsx("h2",{className:"detail-section-h",children:"Wymagania"}),e.jsx("div",{className:"detail-skills",children:t.requirements.map((a,n)=>e.jsx("span",{className:"detail-skill-tag detail-skill-tag--req",children:a},n))})]}),t.niceToHave?.length>0&&e.jsxs("section",{className:"detail-section",children:[e.jsx("h2",{className:"detail-section-h",children:"Mile widziane"}),e.jsx("div",{className:"detail-skills",children:t.niceToHave.map((a,n)=>e.jsx("span",{className:"detail-skill-tag detail-skill-tag--nice",children:a},n))})]}),t.description&&e.jsxs("section",{className:"detail-section",children:[e.jsx("h2",{className:"detail-section-h",children:"Opis oferty"}),e.jsx("div",{className:"detail-desc",children:t.description.split(`
`).map((a,n)=>a.trim()?e.jsx("p",{style:{margin:"0 0 .55rem"},children:a},n):e.jsx("br",{},n))})]}),c&&e.jsxs("section",{className:"detail-section detail-markers-section",children:[e.jsxs("h2",{className:"detail-section-h",children:["⚑ Flagi użytkowników",d?.length>0&&e.jsx("span",{className:"detail-markers-count",children:d.length})]}),!d||d.length===0?e.jsx("p",{className:"detail-markers-empty",children:"Żaden użytkownik nie oflagował tej oferty."}):e.jsx("ul",{className:"detail-markers-list",children:d.map((a,n)=>e.jsxs("li",{className:"detail-marker-row",children:[e.jsxs("span",{className:"detail-marker-user",children:[e.jsx("span",{className:"detail-marker-dot",children:"◉"}),a.username,e.jsxs("span",{className:"detail-marker-email",children:["(",a.email,")"]})]}),e.jsx("span",{className:`detail-flag-badge detail-flag-badge--${a.flagType?.toLowerCase()}`,children:a.flagType==="DUPLICATE"?"DUPLIKAT":"NIEPRZYDATNA"}),e.jsx("time",{className:"detail-marker-date",children:a.flaggedAt?W(a.flaggedAt):"—"})]},n))})]}),D&&e.jsx(F,{title:"Usuń ofertę z bazy danych?",description:`"${t.title}" zostanie trwale usunięta. Tej operacji nie można cofnąć.`,onConfirm:T,onClose:()=>v(!1)}),e.jsx(Y,{})]})}function m({icon:r,label:s,value:l}){return e.jsxs("div",{className:"meta-box",children:[e.jsxs("span",{className:"meta-label",children:[r," ",s]}),e.jsx("span",{className:"meta-value",children:l})]})}function K(){return e.jsxs("div",{className:"detail-page animate-fade-in",children:[[140,100,36,36,36,200].map((r,s)=>e.jsx("div",{className:"d-skel",style:{height:r,marginBottom:12,animationDelay:`${s*60}ms`}},s)),e.jsx("style",{children:`
        .d-skel {
          border-radius: var(--radius-md);
          background: linear-gradient(90deg, var(--bg-2) 25%, var(--bg-3) 50%, var(--bg-2) 75%);
          background-size: 200% 100%;
          animation: shimmer 1.5s ease infinite;
        }
      `})]})}function Y(){return e.jsx("style",{children:`
      .detail-page { max-width: 820px; }

      /* Nawigacja */
      .detail-nav {
        display: flex; align-items: center;
        justify-content: space-between; gap: 12px;
        margin-bottom: 24px; flex-wrap: wrap;
      }
      .detail-back {
        background: none; border: none; color: var(--text-2);
        font-family: var(--font-mono); font-size: 0.78rem; cursor: pointer;
        padding: 0; transition: color .15s;
      }
      .detail-back:hover { color: var(--accent); }
      .detail-actions { display: flex; gap: 8px; }

      /* Hero */
      .detail-hero {
        display: flex; justify-content: space-between;
        align-items: flex-start; gap: 20px; margin-bottom: 20px; flex-wrap: wrap;
      }
      .detail-hero-left { flex: 1; }
      .detail-badges { display: flex; align-items: center; gap: 7px; margin-bottom: 10px; }
      .detail-dup-tag {
        font-family: var(--font-mono); font-size: 0.6rem; font-weight: 700;
        letter-spacing: .1em; color: var(--red);
        border: 1px solid rgba(239,68,68,.4); padding: 2px 7px;
        border-radius: var(--radius-sm);
      }
      .detail-title {
        font-family: var(--font-display); font-weight: 800;
        font-size: 1.6rem; color: var(--text-0);
        line-height: 1.25; margin: 0 0 6px;
      }
      .detail-company { font-size: 0.88rem; color: var(--text-2); margin: 0; }
      .detail-salary {
        font-family: var(--font-mono); font-size: 1rem; font-weight: 700;
        color: var(--cyan); padding: 10px 16px;
        background: rgba(0,212,212,.07);
        border: 1px solid rgba(0,212,212,.25);
        border-radius: var(--radius-lg); white-space: nowrap; align-self: flex-start;
      }

      /* Meta */
      .detail-meta {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(170px, 1fr));
        gap: 8px; margin-bottom: 24px;
      }
      .meta-box {
        background: var(--bg-2); border: 1px solid var(--border-1);
        border-radius: var(--radius-md); padding: 10px 12px;
        display: flex; flex-direction: column; gap: 4px;
      }
      .meta-box--wide { grid-column: 1 / -1; }
      .meta-label {
        font-size: 0.64rem; color: var(--text-3);
        text-transform: uppercase; letter-spacing: .07em; font-weight: 600;
      }
      .meta-value { font-family: var(--font-mono); font-size: 0.8rem; color: var(--text-0); }
      .meta-link  {
        font-family: var(--font-mono); font-size: 0.74rem; color: var(--accent);
        text-decoration: none; word-break: break-all;
      }
      .meta-link:hover { text-decoration: underline; }

      /* Tagi skillów */
      .detail-skills {
        display: flex; flex-wrap: wrap; gap: 7px;
      }
      .detail-skill-tag {
        font-family: var(--font-mono); font-size: 0.74rem; font-weight: 600;
        padding: 4px 10px; border-radius: 100px; border: 1px solid;
        white-space: nowrap;
      }
      .detail-skill-tag--req {
        color: var(--accent); border-color: rgba(245,166,35,.35);
        background: rgba(245,166,35,.08);
      }
      .detail-skill-tag--nice {
        color: var(--cyan); border-color: rgba(0,212,212,.3);
        background: rgba(0,212,212,.07);
      }

      /* Opis */
      .detail-section  { margin-bottom: 20px; }
      .detail-section-h {
        font-family: var(--font-display); font-size: 0.85rem; font-weight: 700;
        color: var(--text-1); text-transform: uppercase; letter-spacing: .08em;
        margin-bottom: 10px; padding-bottom: 8px;
        border-bottom: 1px solid var(--border-1);
      }
      .detail-desc {
        font-size: 0.83rem; color: var(--text-1); line-height: 1.7;
        background: var(--bg-1); border: 1px solid var(--border-1);
        border-radius: var(--radius-md); padding: 14px 16px;
      }

      /* Admin delete btn */
      .detail-delete-btn {
        background: var(--red) !important;
        border-color: var(--red) !important;
        color: #fff !important;
      }
      .detail-delete-btn:hover:not(:disabled) {
        filter: brightness(1.12) !important;
      }

      /* Markers */
      .detail-markers-section { border-top: 1px solid rgba(239,68,68,.2); }
      .detail-markers-section .detail-section-h {
        color: var(--red); border-bottom-color: rgba(239,68,68,.2);
        display: flex; align-items: center; gap: 8px;
      }
      .detail-markers-count {
        font-family: var(--font-mono); font-size: 0.65rem; font-weight: 700;
        background: rgba(239,68,68,.15); color: var(--red);
        border-radius: 100px; padding: 1px 7px;
      }
      .detail-markers-empty {
        font-size: 0.79rem; color: var(--text-3); font-style: italic;
      }
      .detail-markers-list {
        list-style: none; margin: 0; padding: 0;
        display: flex; flex-direction: column; gap: 6px;
      }
      .detail-marker-row {
        display: flex; justify-content: space-between; align-items: center;
        gap: 12px; padding: 8px 12px;
        background: var(--bg-2); border: 1px solid var(--border-1);
        border-left: 2px solid rgba(239,68,68,.4);
        border-radius: var(--radius-md);
      }
      .detail-marker-user {
        display: flex; align-items: center; gap: 6px;
        font-family: var(--font-mono); font-size: 0.79rem; color: var(--text-0);
      }
      .detail-marker-dot { color: var(--red); font-size: 0.65rem; }
      .detail-marker-email { font-size: 0.68rem; color: var(--text-3); }
      .detail-marker-date {
        font-family: var(--font-mono); font-size: 0.67rem; color: var(--text-3);
        white-space: nowrap; flex-shrink: 0;
      }
      .detail-flag-badge {
        font-family: var(--font-mono); font-size: 0.6rem; font-weight: 700;
        letter-spacing: .08em; padding: 2px 7px; border-radius: 100px;
        border: 1px solid; flex-shrink: 0;
      }
      .detail-flag-badge--duplicate {
        color: var(--red); border-color: rgba(239,68,68,.4);
        background: rgba(239,68,68,.08);
      }
      .detail-flag-badge--useless {
        color: var(--text-2); border-color: var(--border-1);
        background: var(--bg-3);
      }

      /* Error */
      .detail-err {
        display: flex; flex-direction: column;
        align-items: center; gap: 12px;
        padding: 48px 24px; color: var(--text-2); text-align: center;
      }
    `})}export{J as default};
