import { useCallback, useEffect, useMemo, useRef, useState } from 'react'
import { Link, useLocation } from 'react-router-dom'
import { scraperUrlsApi } from '@/api/services'
import { usePortal } from '@/hooks'
import { toast } from '@/store'
import { EmptyState } from '@/components/ui'
import PortalTabs from '@/components/ui/PortalTabs'
import { PORTALS, portalById, urlMatchesPortal, formatDateTime } from '@/utils'

// ─────────────────────────────────────────────────────────────────
// Panel admina → adresy wyszukiwania scraperów — osobna lista dla każdego portalu.
// Lista jest w bazie; scraper czyta ją przy każdym przebiegu, więc zmiany
// działają od następnego pobrania, bez restartu serwera.
// ─────────────────────────────────────────────────────────────────

const EXAMPLES = {
  NoFluff:     'https://nofluffjobs.com/pl/warszawa/Java?criteria=seniority%3Djunior',
  TheProtocol: 'https://theprotocol.it/filtry/java;t/junior,mid;p/lodz;wp',
  JustJoin:    'https://justjoin.it/job-offers/lodz/java?experience-levels=junior,mid',
}

function validate(url, portalId) {
  const v = url.trim()
  if (!v) return 'Podaj adres wyszukiwania'
  if (/\s/.test(v)) return 'Adres nie może zawierać spacji'
  let parsed
  try { parsed = new URL(v) } catch { return 'To nie jest poprawny adres URL' }
  if (!['https:', 'http:'].includes(parsed.protocol)) return 'Adres musi zaczynać się od https://'
  if (!urlMatchesPortal(v, portalId)) return `Adres musi prowadzić do ${portalById(portalId).domain}`
  return null
}

// Ścieżka i parametry wyszukiwania osobno — łatwiej zobaczyć, czym różnią się adresy
function UrlParts({ url }) {
  let parsed = null
  try { parsed = new URL(url) } catch { /* pokaż surowy tekst */ }
  if (!parsed) return <span className="su-url-raw">{url}</span>
  const params = [...parsed.searchParams.entries()]
  const pathFilters = parsed.pathname.split('/').filter(s => s.includes(';'))
  return (
    <div className="su-url">
      <span className="su-url-text">
        <span className="su-url-host">{parsed.host}</span>
        <span className="su-url-path">{decodeURIComponent(parsed.pathname)}</span>
        {parsed.search && <span className="su-url-query">{decodeURIComponent(parsed.search)}</span>}
      </span>
      {(params.length > 0 || pathFilters.length > 0) && (
        <span className="su-chips">
          {pathFilters.map(f => <span key={f} className="su-chip su-chip--path">{decodeURIComponent(f)}</span>)}
          {params.map(([k, v]) => (
            <span key={k + v} className="su-chip"><b>{k}</b>={v}</span>
          ))}
        </span>
      )}
    </div>
  )
}

function UrlForm({ portalId, initial, submitLabel, onSubmit, onCancel, busy, autoFocus }) {
  const [url, setUrl] = useState(initial?.url ?? '')
  const [label, setLabel] = useState(initial?.label ?? '')
  const [touched, setTouched] = useState(false)
  const error = validate(url, portalId)
  const showError = touched && url.length > 0 && error

  const submit = async (e) => {
    e.preventDefault()
    setTouched(true)
    if (error) return
    const ok = await onSubmit({ url: url.trim(), label: label.trim() || null })
    if (ok && !initial) { setUrl(''); setLabel(''); setTouched(false) }
  }

  return (
    <form className="su-form" onSubmit={submit} noValidate>
      <div className="su-form-row">
        <label className="su-field su-field--url">
          <span className="su-field-label">Adres wyszukiwania (z parametrami)</span>
          <input
            className={`su-input su-input--mono${showError ? ' su-input--err' : ''}`}
            value={url}
            autoFocus={autoFocus}
            onChange={e => setUrl(e.target.value)}
            onBlur={() => setTouched(true)}
            placeholder={EXAMPLES[portalId]}
            spellCheck={false}
          />
        </label>
        <label className="su-field su-field--label">
          <span className="su-field-label">Opis <span className="su-opt">(opcjonalnie)</span></span>
          <input
            className="su-input"
            value={label}
            maxLength={120}
            onChange={e => setLabel(e.target.value)}
            placeholder="np. Java – Łódź, junior/mid"
          />
        </label>
      </div>
      {showError && <p className="su-err">{error}</p>}
      <div className="su-form-actions">
        {onCancel && <button type="button" className="su-btn su-btn--ghost" onClick={onCancel}>Anuluj</button>}
        {!error && (
          <a className="su-btn su-btn--ghost" href={url.trim()} target="_blank" rel="noopener noreferrer">
            ↗ Sprawdź w serwisie
          </a>
        )}
        <button type="submit" className="su-btn su-btn--primary" disabled={busy || (touched && !!error)}>
          {busy ? <><span className="su-spin" />Zapisywanie…</> : submitLabel}
        </button>
      </div>
    </form>
  )
}

function UrlRow({ item, index, portalId, isLast, onSave, onDelete }) {
  const [editing, setEditing] = useState(false)
  const [confirming, setConfirming] = useState(false)
  const [busy, setBusy] = useState(false)

  const save = async (data) => {
    setBusy(true)
    const ok = await onSave(item.id, data)
    setBusy(false)
    if (ok) setEditing(false)
    return ok
  }

  const remove = async () => {
    setBusy(true)
    await onDelete(item.id)
    setBusy(false)
    setConfirming(false)
  }

  return (
    <li className={`su-row${editing ? ' su-row--editing' : ''} animate-fade-in`}>
      <span className="su-idx">{index + 1}</span>
      <div className="su-row-main">
        {editing ? (
          <UrlForm portalId={portalId} initial={item} submitLabel="Zapisz zmiany" busy={busy}
                   onSubmit={save} onCancel={() => setEditing(false)} autoFocus />
        ) : (
          <>
            <div className="su-row-head">
              <span className={`su-label${item.label ? '' : ' su-label--empty'}`}>{item.label || 'Bez opisu'}</span>
              {item.isDefault && <span className="su-badge">domyślny</span>}
            </div>
            <UrlParts url={item.url} />
            <p className="su-meta">
              {item.createdBy ? `dodał ${item.createdBy}` : 'lista startowa'} · {formatDateTime(item.createdAt)}
              {item.updatedAt && <> · zmieniono {formatDateTime(item.updatedAt)}</>}
            </p>
          </>
        )}
      </div>
      {!editing && (
        <div className="su-row-actions">
          {confirming ? (
            <div className="su-confirm">
              <span>Usunąć?</span>
              <button className="su-btn su-btn--danger su-btn--sm" onClick={remove} disabled={busy}>
                {busy ? <span className="su-spin" /> : 'Tak, usuń'}
              </button>
              <button className="su-btn su-btn--ghost su-btn--sm" onClick={() => setConfirming(false)}>Nie</button>
            </div>
          ) : (
            <>
              <a className="su-icon-btn" href={item.url} target="_blank" rel="noopener noreferrer"
                 title="Otwórz wyszukiwanie w serwisie">↗</a>
              <button className="su-icon-btn" title="Edytuj adres lub opis" onClick={() => setEditing(true)}>✎</button>
              <button className="su-icon-btn su-icon-btn--danger"
                      title={isLast ? 'To ostatni adres portalu — najpierw dodaj nowy' : 'Usuń adres'}
                      disabled={isLast} onClick={() => setConfirming(true)}>✕</button>
            </>
          )}
        </div>
      )}
    </li>
  )
}

export default function ScraperUrlsSection() {
  const [portal, setPortal] = usePortal()
  const location = useLocation()
  const anchor = useRef(null)
  const [lists, setLists] = useState({})
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [adding, setAdding] = useState(false)
  const [restoring, setRestoring] = useState(false)
  const info = portalById(portal)

  // wszystkie portale naraz — liczniki na zakładkach
  const loadAll = useCallback(async () => {
    setLoading(true)
    try {
      const results = await Promise.all(PORTALS.map(p => scraperUrlsApi.list(p.id)))
      setLists(Object.fromEntries(PORTALS.map((p, i) => [p.id, results[i].data])))
      setError(null)
    } catch (err) {
      setError(err)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => { loadAll() }, [loadAll])

  // link "Adresy wyszukiwania …" z zakładki Import prowadzi prosto do tej sekcji
  useEffect(() => {
    if (!loading && location.hash === '#scraper-urls') {
      anchor.current?.scrollIntoView({ behavior: 'smooth', block: 'start' })
    }
  }, [loading, location.hash])

  const setList = (id, items) => setLists(prev => ({ ...prev, [id]: items }))
  const items = useMemo(() => lists[portal] ?? [], [lists, portal])
  const counts = Object.fromEntries(PORTALS.map(p => [p.id, lists[p.id]?.length]))

  const add = async (data) => {
    setAdding(true)
    try {
      const res = await scraperUrlsApi.add(portal, data)
      setList(portal, [...items, res.data])
      toast.success(`Dodano adres — ${info.label} użyje go przy następnym pobraniu`)
      return true
    } catch (err) {
      toast.error(err.message ?? 'Nie udało się dodać adresu')
      return false
    } finally {
      setAdding(false)
    }
  }

  const save = async (id, data) => {
    try {
      const res = await scraperUrlsApi.update(id, data)
      setList(portal, items.map(i => i.id === id ? res.data : i))
      toast.success('Zapisano zmiany adresu')
      return true
    } catch (err) {
      toast.error(err.message ?? 'Nie udało się zapisać')
      return false
    }
  }

  const remove = async (id) => {
    try {
      await scraperUrlsApi.remove(id)
      setList(portal, items.filter(i => i.id !== id))
      toast.success('Usunięto adres')
    } catch (err) {
      toast.error(err.message ?? 'Nie udało się usunąć')
    }
  }

  const restore = async () => {
    setRestoring(true)
    try {
      const res = await scraperUrlsApi.restoreDefaults(portal)
      setList(portal, res.data.urls)
      if (res.data.added > 0) toast.success(`Przywrócono ${res.data.added} domyślnych adresów`)
      else toast.info('Wszystkie domyślne adresy są już na liście')
    } catch (err) {
      toast.error(err.message ?? 'Nie udało się przywrócić')
    } finally {
      setRestoring(false)
    }
  }

  return (
    <section className="su-page" id="scraper-urls" ref={anchor}>
      <div className="su-head">
        <h2 className="adm-section-title--standalone su-title">Adresy wyszukiwania scraperów</h2>
        <p className="su-sub">Listy wyszukiwania, po których chodzą scrapery — osobno dla każdego portalu</p>
      </div>

      <PortalTabs value={portal} onChange={setPortal} counts={counts} />

      <div className="su-info">
        <span className="su-info-icon">ⓘ</span>
        <p>
          Scraper <strong>{info.label}</strong> przechodzi po kolei przez poniższe adresy (strona po stronie) i pobiera
          nowe oferty. Zmiany zapisują się w bazie i działają <strong>od następnego pobrania</strong> — bez restartu.
          Żeby zmienić parametry wyszukiwania, ustaw filtry w serwisie, skopiuj adres z paska przeglądarki
          i wklej go tutaj (albo edytuj istniejący ✎).{' '}
          <Link to={`/import?portal=${info.slug}`} className="su-link">Uruchom scraper →</Link>
        </p>
      </div>

      <section className="su-card">
        <header className="su-card-head">
          <h2 className="su-card-title">Dodaj adres — {info.label}</h2>
        </header>
        <div className="su-card-body">
          <UrlForm key={portal} portalId={portal} submitLabel="+ Dodaj adres" busy={adding} onSubmit={add} />
        </div>
      </section>

      <section className="su-card">
        <header className="su-card-head">
          <h2 className="su-card-title">
            Lista {info.label}
            {!loading && <span className="su-count">{items.length}</span>}
          </h2>
          <button className="su-btn su-btn--ghost su-btn--sm" onClick={restore} disabled={restoring || loading}
                  title="Dodaje brakujące adresy z listy startowej; Twoje adresy zostają">
            {restoring ? <><span className="su-spin" />Przywracanie…</> : '↺ Przywróć domyślne'}
          </button>
        </header>

        {loading && (
          <div className="su-skel-list">
            {[0, 1, 2].map(i => <div key={i} className="su-skel" />)}
          </div>
        )}
        {!loading && error && (
          <EmptyState icon="✕" title="Nie udało się wczytać adresów" description={error.message}
                      action={<button className="su-btn su-btn--primary" onClick={loadAll}>Spróbuj ponownie</button>} />
        )}
        {!loading && !error && items.length === 0 && (
          <EmptyState icon="⛓" title="Brak adresów"
                      description={`Scraper ${info.label} nie ma czego przeszukiwać — dodaj adres albo przywróć domyślne.`} />
        )}
        {!loading && !error && items.length > 0 && (
          <ol className="su-list">
            {items.map((item, i) => (
              <UrlRow key={item.id} item={item} index={i} portalId={portal} isLast={items.length === 1}
                      onSave={save} onDelete={remove} />
            ))}
          </ol>
        )}
      </section>

      <ScraperUrlsStyles />
    </section>
  )
}

function ScraperUrlsStyles() {
  return (
    <style>{`
      .su-page { display: flex; flex-direction: column; gap: 16px; margin-bottom: 24px; scroll-margin-top: 16px; }
      .su-head { display: flex; flex-direction: column; gap: 2px; }
      .su-title { margin-bottom: 0 !important; }
      .su-sub { font-size: 0.78rem; color: var(--text-2); }
      .su-page .portal-tabs { margin-bottom: 0; align-self: flex-start; }

      .su-info {
        display: flex; gap: 12px; align-items: flex-start;
        padding: 14px 16px; border-radius: var(--radius-lg);
        background: var(--accent-glow); border: 1px solid var(--border-0);
        font-size: 0.8rem; color: var(--text-1); line-height: 1.65;
      }
      .su-info strong { color: var(--text-0); }
      .su-info-icon { color: var(--accent); font-size: 1.05rem; line-height: 1.4; }
      .su-link { color: var(--accent); font-weight: 600; white-space: nowrap; }
      .su-link:hover { text-decoration: underline; }

      .su-card { background: var(--bg-1); border: 1px solid var(--border-1); border-radius: var(--radius-lg); overflow: hidden; }
      .su-card-head {
        display: flex; align-items: center; justify-content: space-between; gap: 12px;
        padding: 14px 18px; border-bottom: 1px solid var(--border-1);
      }
      .su-card-title {
        display: flex; align-items: center; gap: 8px;
        font-size: 0.86rem; font-weight: 700; color: var(--text-1);
        text-transform: uppercase; letter-spacing: .06em;
      }
      .su-count {
        font-family: var(--font-mono); font-size: 0.66rem; padding: 1px 7px; border-radius: 100px;
        background: var(--bg-3); color: var(--text-1); border: 1px solid var(--border-1);
      }
      .su-card-body { padding: 16px 18px; }

      /* ── Formularz ── */
      .su-form { display: flex; flex-direction: column; gap: 10px; }
      .su-form-row { display: grid; grid-template-columns: minmax(0, 2.2fr) minmax(0, 1fr); gap: 10px; }
      @media (max-width: 760px) { .su-form-row { grid-template-columns: 1fr; } }
      .su-field { display: flex; flex-direction: column; gap: 5px; min-width: 0; }
      .su-field-label {
        font-family: var(--font-mono); font-size: 0.66rem; font-weight: 700;
        color: var(--text-2); text-transform: uppercase; letter-spacing: .07em;
      }
      .su-opt { text-transform: none; letter-spacing: 0; font-weight: 400; color: var(--text-3); }
      .su-input {
        width: 100%; padding: 9px 12px; border-radius: var(--radius-md);
        background: var(--bg-2); border: 1px solid var(--border-1); color: var(--text-0);
        font-size: 0.84rem; outline: none; transition: border-color .15s, box-shadow .15s;
      }
      .su-input--mono { font-family: var(--font-mono); font-size: 0.78rem; }
      .su-input::placeholder { color: var(--text-3); }
      .su-input:focus { border-color: var(--accent); box-shadow: 0 0 0 3px var(--accent-glow); }
      .su-input--err { border-color: var(--red); }
      .su-err { font-family: var(--font-mono); font-size: 0.72rem; color: var(--red); }
      .su-form-actions { display: flex; justify-content: flex-end; gap: 8px; flex-wrap: wrap; }

      /* ── Przyciski ── */
      .su-btn {
        display: inline-flex; align-items: center; gap: 7px; white-space: nowrap;
        padding: 8px 16px; border-radius: var(--radius-md); border: 1px solid transparent;
        font-family: var(--font-mono); font-size: 0.76rem; font-weight: 700; cursor: pointer;
        transition: background .15s, border-color .15s, box-shadow .15s, opacity .15s;
      }
      .su-btn:disabled { opacity: .5; cursor: not-allowed; }
      .su-btn--sm { padding: 5px 11px; font-size: 0.7rem; }
      .su-btn--primary { background: var(--accent); color: #fff; border-color: var(--accent); }
      .su-btn--primary:hover:not(:disabled) { background: var(--accent-dim); box-shadow: var(--shadow-accent); }
      .su-btn--ghost { background: var(--bg-2); color: var(--text-1); border-color: var(--border-1); }
      .su-btn--ghost:hover:not(:disabled) { color: var(--text-0); border-color: var(--border-0); }
      .su-btn--danger { background: rgba(244,63,94,.12); color: var(--red); border-color: rgba(244,63,94,.4); }
      .su-btn--danger:hover:not(:disabled) { background: rgba(244,63,94,.2); }
      .su-spin {
        width: 11px; height: 11px; border: 2px solid transparent; border-top-color: currentColor;
        border-radius: 50%; animation: spin .6s linear infinite;
      }

      /* ── Lista ── */
      .su-list { list-style: none; }
      .su-row {
        display: flex; gap: 14px; align-items: flex-start;
        padding: 14px 18px; border-bottom: 1px solid var(--border-2);
        transition: background .15s;
      }
      .su-row:last-child { border-bottom: none; }
      .su-row:hover { background: var(--bg-2); }
      .su-row--editing { background: var(--bg-2); }
      .su-idx {
        flex-shrink: 0; width: 26px; height: 26px; border-radius: 50%;
        display: flex; align-items: center; justify-content: center;
        font-family: var(--font-mono); font-size: 0.7rem; font-weight: 700;
        background: var(--accent-glow); color: var(--accent); border: 1px solid var(--border-0);
      }
      .su-row-main { flex: 1; min-width: 0; display: flex; flex-direction: column; gap: 6px; }
      .su-row-head { display: flex; align-items: center; gap: 8px; flex-wrap: wrap; }
      .su-label { font-size: 0.88rem; font-weight: 700; color: var(--text-0); }
      .su-label--empty { color: var(--text-3); font-weight: 400; font-style: italic; }
      .su-badge {
        font-family: var(--font-mono); font-size: 0.6rem; font-weight: 700; text-transform: uppercase;
        letter-spacing: .07em; padding: 2px 7px; border-radius: 100px;
        background: rgba(16,185,129,.1); color: var(--green); border: 1px solid rgba(16,185,129,.3);
      }
      .su-url { display: flex; flex-direction: column; gap: 6px; min-width: 0; }
      .su-url-text, .su-url-raw {
        font-family: var(--font-mono); font-size: 0.74rem; line-height: 1.5;
        overflow-wrap: anywhere; word-break: break-all;
      }
      .su-url-host { color: var(--text-2); }
      .su-url-path { color: var(--text-0); }
      .su-url-query { color: var(--cyan); }
      .su-chips { display: flex; flex-wrap: wrap; gap: 5px; }
      .su-chip {
        font-family: var(--font-mono); font-size: 0.66rem; color: var(--text-1);
        padding: 2px 8px; border-radius: var(--radius-sm);
        background: var(--cyan-glow); border: 1px solid rgba(56,189,248,.25);
      }
      .su-chip b { color: var(--cyan); font-weight: 700; }
      .su-chip--path { background: var(--accent-glow); border-color: var(--border-0); }
      .su-meta { font-family: var(--font-mono); font-size: 0.66rem; color: var(--text-3); }

      .su-row-actions { display: flex; gap: 6px; flex-shrink: 0; align-items: center; }
      .su-icon-btn {
        width: 30px; height: 30px; border-radius: var(--radius-md);
        display: inline-flex; align-items: center; justify-content: center;
        background: var(--bg-2); border: 1px solid var(--border-1); color: var(--text-1);
        font-size: 0.85rem; cursor: pointer; transition: color .15s, border-color .15s, background .15s;
      }
      .su-icon-btn:hover:not(:disabled) { color: var(--text-0); border-color: var(--border-0); background: var(--bg-3); }
      .su-icon-btn--danger:hover:not(:disabled) { color: var(--red); border-color: rgba(244,63,94,.45); }
      .su-icon-btn:disabled { opacity: .35; cursor: not-allowed; }
      .su-confirm {
        display: flex; align-items: center; gap: 6px;
        font-family: var(--font-mono); font-size: 0.72rem; color: var(--red);
      }
      @media (max-width: 640px) {
        .su-row { flex-wrap: wrap; }
        .su-row-actions { width: 100%; justify-content: flex-end; }
      }

      .su-skel-list { padding: 14px 18px; display: flex; flex-direction: column; gap: 10px; }
      .su-skel {
        height: 64px; border-radius: var(--radius-md);
        background: linear-gradient(90deg, var(--bg-2) 25%, var(--bg-3) 50%, var(--bg-2) 75%);
        background-size: 200% 100%; animation: shimmer 1.5s ease infinite;
      }
    `}</style>
  )
}
