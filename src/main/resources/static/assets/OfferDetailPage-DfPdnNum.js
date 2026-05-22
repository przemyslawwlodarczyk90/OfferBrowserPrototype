import{c as B,u as A,a as O,r as o,j as e,t as s}from"./index-B4E70c8-.js";import{b as S,u as U,o as v,d as j}from"./index-BBvXT-jJ.js";import{B as M}from"./index-CsYv5Omc.js";import{n as C,f as I,a as P}from"./index-Cvq4Ykd3.js";function K(){const{id:r}=B(),i=A(),n=O(t=>t.user),{data:a,loading:k,error:c}=S(()=>v.getById(r),{immediate:!0,deps:[r]});U(a?.title??"Szczegóły oferty");const[m,p]=o.useState(!1),[f,x]=o.useState(!1),[g,u]=o.useState(!1),N=async()=>{if(!n?.id){s.warn("Brak ID użytkownika w sesji");return}p(!0);try{await j.applyToOffer(n.id,r),s.success("Aplikacja zapisana!")}catch(t){s.error(t?.message??"Błąd zapisu aplikacji")}finally{p(!1)}},z=async()=>{x(!0);try{await v.markDuplicateById(r),s.info("Oferta oznaczona jako duplikat"),i(-1)}catch(t){s.error(t?.message??"Błąd oznaczania duplikatu")}finally{x(!1)}},w=async()=>{if(!n?.id){s.warn("Brak ID użytkownika w sesji");return}u(!0);try{await j.markUseless(n.id,r),s.info("Oferta przeniesiona do śmietnika"),i(-1)}catch(t){s.error(t?.message??"Błąd oznaczania oferty")}finally{u(!1)}};if(k)return e.jsx(E,{});if(c||!a)return e.jsxs("div",{className:"detail-page animate-fade-in",children:[e.jsx("button",{className:"detail-back",onClick:()=>i(-1),children:"← Wróć"}),e.jsxs("div",{className:"detail-err",children:[e.jsx("span",{style:{fontSize:"2.2rem"},children:"✕"}),e.jsx("p",{children:c?.message??"Nie znaleziono oferty."})]})]});const b=C(a.level),h=I(a.salaryRange??a.salary),l=a.isDuplicate??a.duplicate??!1,D=a.companyName??a.company??"—";return e.jsxs("div",{className:"detail-page animate-fade-in",children:[e.jsxs("div",{className:"detail-nav",children:[e.jsx("button",{className:"detail-back",onClick:()=>i(-1),children:"← Oferty"}),e.jsxs("div",{className:"detail-actions",children:[e.jsx("button",{className:"btn btn--ghost btn--sm",onClick:w,disabled:g,title:"Przenieś do śmietnika",children:g?"…":"🗑 Nieprzydatne"}),e.jsx("button",{className:"btn btn--danger btn--sm",onClick:z,disabled:f||l,title:l?"Już oznaczono":"Oznacz jako duplikat",children:f?"…":"⊗ Duplikat"}),e.jsx("button",{className:"btn btn--primary btn--sm",onClick:N,disabled:m,children:m?"…":"✓ Aplikuj"})]})]}),e.jsxs("div",{className:"detail-hero",children:[e.jsxs("div",{className:"detail-hero-left",children:[e.jsxs("div",{className:"detail-badges",children:[e.jsx(M,{level:b}),l&&e.jsx("span",{className:"detail-dup-tag",children:"DUPLIKAT"})]}),e.jsx("h1",{className:"detail-title",children:a.title}),e.jsx("p",{className:"detail-company",children:D})]}),h&&e.jsx("div",{className:"detail-salary",children:h})]}),e.jsxs("div",{className:"detail-meta",children:[e.jsx(d,{icon:"◎",label:"Miasto",value:a.city??a.location??"—"}),e.jsx(d,{icon:"⊙",label:"Poziom",value:b}),e.jsx(d,{icon:"◷",label:"Dodano",value:P(a.fetchedAt)}),a.offerUrl&&e.jsxs("div",{className:"meta-box meta-box--wide",children:[e.jsx("span",{className:"meta-label",children:"Źródło"}),e.jsx("a",{href:a.offerUrl,target:"_blank",rel:"noopener noreferrer",className:"meta-link",children:a.offerUrl})]})]}),a.description&&e.jsxs("section",{className:"detail-section",children:[e.jsx("h2",{className:"detail-section-h",children:"Opis oferty"}),e.jsx("div",{className:"detail-desc",children:a.description.split(`
`).map((t,y)=>t.trim()?e.jsx("p",{style:{margin:"0 0 .55rem"},children:t},y):e.jsx("br",{},y))})]}),e.jsx(T,{})]})}function d({icon:r,label:i,value:n}){return e.jsxs("div",{className:"meta-box",children:[e.jsxs("span",{className:"meta-label",children:[r," ",i]}),e.jsx("span",{className:"meta-value",children:n})]})}function E(){return e.jsxs("div",{className:"detail-page animate-fade-in",children:[[140,100,36,36,36,200].map((r,i)=>e.jsx("div",{className:"d-skel",style:{height:r,marginBottom:12,animationDelay:`${i*60}ms`}},i)),e.jsx("style",{children:`
        .d-skel {
          border-radius: var(--radius-md);
          background: linear-gradient(90deg, var(--bg-2) 25%, var(--bg-3) 50%, var(--bg-2) 75%);
          background-size: 200% 100%;
          animation: shimmer 1.5s ease infinite;
        }
      `})]})}function T(){return e.jsx("style",{children:`
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
    `})}export{K as default};
