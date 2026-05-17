import{e as N,u as z,a as w,r as b,j as a,o as h,t as s,f as D}from"./index-BXWfP1i_.js";import{a as A,u as B}from"./index-BwTqQwrb.js";import{B as O}from"./index-huvFFqiG.js";import{n as S,f as M,a as C,b as I}from"./index-Cvq4Ykd3.js";function H(){const{id:t}=N(),r=z(),l=w(i=>i.user),{data:e,loading:v,error:d}=A(()=>h.getById(t),{immediate:!0,deps:[t]});B(e?.title??"Szczegóły oferty");const[c,m]=b.useState(!1),[p,f]=b.useState(!1),y=async()=>{if(!l?.id){s.warn("Brak ID użytkownika w sesji");return}m(!0);try{await D.applyToOffer(l.id,t),s.success("Aplikacja zapisana!")}catch(i){s.error(i?.message??"Błąd zapisu aplikacji")}finally{m(!1)}},j=async()=>{f(!0);try{await h.markDuplicateById(t),s.info("Oferta oznaczona jako duplikat"),r(-1)}catch(i){s.error(i?.message??"Błąd oznaczania duplikatu")}finally{f(!1)}};if(v)return a.jsx(P,{});if(d||!e)return a.jsxs("div",{className:"detail-page animate-fade-in",children:[a.jsx("button",{className:"detail-back",onClick:()=>r(-1),children:"← Wróć"}),a.jsxs("div",{className:"detail-err",children:[a.jsx("span",{style:{fontSize:"2.2rem"},children:"✕"}),a.jsx("p",{children:d?.message??"Nie znaleziono oferty."})]})]});const x=S(e.level),g=M(e.salaryRange??e.salary),o=e.isDuplicate??e.duplicate??!1,k=e.companyName??e.company??"—";return a.jsxs("div",{className:"detail-page animate-fade-in",children:[a.jsxs("div",{className:"detail-nav",children:[a.jsx("button",{className:"detail-back",onClick:()=>r(-1),children:"← Oferty"}),a.jsxs("div",{className:"detail-actions",children:[a.jsx("button",{className:"btn btn--danger btn--sm",onClick:j,disabled:p||o,title:o?"Już oznaczono":"Oznacz jako duplikat",children:p?"…":"⊗ Duplikat"}),a.jsx("button",{className:"btn btn--primary btn--sm",onClick:y,disabled:c,children:c?"…":"✓ Aplikuj"})]})]}),a.jsxs("div",{className:"detail-hero",children:[a.jsxs("div",{className:"detail-hero-left",children:[a.jsxs("div",{className:"detail-badges",children:[a.jsx(O,{level:x}),o&&a.jsx("span",{className:"detail-dup-tag",children:"DUPLIKAT"})]}),a.jsx("h1",{className:"detail-title",children:e.title}),a.jsx("p",{className:"detail-company",children:k})]}),g&&a.jsx("div",{className:"detail-salary",children:g})]}),a.jsxs("div",{className:"detail-meta",children:[a.jsx(n,{icon:"◎",label:"Miasto",value:e.city??"—"}),a.jsx(n,{icon:"⊙",label:"Poziom",value:x}),a.jsx(n,{icon:"◷",label:"Dodano",value:C(e.createdAt??e.fetchedAt)}),a.jsx(n,{icon:"◈",label:"Aktualizacja",value:I(e.updatedAt)}),e.offerUrl&&a.jsxs("div",{className:"meta-box meta-box--wide",children:[a.jsx("span",{className:"meta-label",children:"Źródło"}),a.jsx("a",{href:e.offerUrl,target:"_blank",rel:"noopener noreferrer",className:"meta-link",children:e.offerUrl})]})]}),e.description&&a.jsxs("section",{className:"detail-section",children:[a.jsx("h2",{className:"detail-section-h",children:"Opis oferty"}),a.jsx("div",{className:"detail-desc",children:e.description.split(`
`).map((i,u)=>i.trim()?a.jsx("p",{style:{margin:"0 0 .55rem"},children:i},u):a.jsx("br",{},u))})]}),a.jsx(T,{})]})}function n({icon:t,label:r,value:l}){return a.jsxs("div",{className:"meta-box",children:[a.jsxs("span",{className:"meta-label",children:[t," ",r]}),a.jsx("span",{className:"meta-value",children:l})]})}function P(){return a.jsxs("div",{className:"detail-page animate-fade-in",children:[[140,100,36,36,36,200].map((t,r)=>a.jsx("div",{className:"d-skel",style:{height:t,marginBottom:12,animationDelay:`${r*60}ms`}},r)),a.jsx("style",{children:`
        .d-skel {
          border-radius: var(--radius-md);
          background: linear-gradient(90deg, var(--bg-2) 25%, var(--bg-3) 50%, var(--bg-2) 75%);
          background-size: 200% 100%;
          animation: shimmer 1.5s ease infinite;
        }
      `})]})}function T(){return a.jsx("style",{children:`
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

      /* Error */
      .detail-err {
        display: flex; flex-direction: column;
        align-items: center; gap: 12px;
        padding: 48px 24px; color: var(--text-2); text-align: center;
      }
    `})}export{H as default};
