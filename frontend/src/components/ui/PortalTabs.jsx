import { PORTALS } from '@/utils'

// Przełącznik portali: No Fluff Jobs | theprotocol.it | Just Join IT — wspólny dla wszystkich stron.
// extra — dodatkowe zakładki przed portalami (np. "Wszystkie strony" w statystykach)
export default function PortalTabs({ value, onChange, counts, extra = [] }) {
  return (
    <>
      <div className="portal-tabs" role="tablist" aria-label="Portal z ofertami">
        {[...extra, ...PORTALS].map(p => {
          const active = p.id === value
          const count = counts?.[p.id]
          return (
            <button
              key={p.id}
              role="tab"
              aria-selected={active}
              className={`portal-tab${active ? ' portal-tab--on' : ''}`}
              onClick={() => !active && onChange(p.id)}
            >
              <span className="portal-tab-dot" aria-hidden="true" />
              {p.label}
              {count != null && <span className="portal-tab-count">{count}</span>}
            </button>
          )
        })}
      </div>
      <style>{`
        .portal-tabs {
          display: inline-flex; gap: 4px; padding: 4px;
          background: var(--bg-1); border: 1px solid var(--border-1);
          border-radius: var(--radius-lg); margin-bottom: 18px;
          max-width: 100%; overflow-x: auto;
        }
        .portal-tab {
          display: inline-flex; align-items: center; gap: 8px;
          padding: 7px 16px; border: 1px solid transparent; border-radius: var(--radius-md);
          background: none; color: var(--text-2);
          font-family: var(--font-mono); font-size: 0.78rem; font-weight: 600;
          cursor: pointer; white-space: nowrap;
          transition: background .15s, color .15s, border-color .15s;
        }
        .portal-tab:hover { color: var(--text-0); background: var(--bg-2); }
        .portal-tab--on {
          background: var(--accent-glow); color: var(--accent);
          border-color: var(--accent); cursor: default;
        }
        .portal-tab-dot {
          width: 7px; height: 7px; border-radius: 50%;
          background: var(--text-3); transition: background .15s;
        }
        .portal-tab--on .portal-tab-dot { background: var(--accent); }
        .portal-tab-count {
          font-size: 0.64rem; font-weight: 700; padding: 1px 6px; border-radius: 100px;
          background: var(--bg-3); color: var(--text-2); border: 1px solid var(--border-1);
        }
        .portal-tab--on .portal-tab-count { background: var(--accent); color: #000; border-color: var(--accent); }
      `}</style>
    </>
  )
}
