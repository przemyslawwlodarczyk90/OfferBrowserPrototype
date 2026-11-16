import { useState, useEffect, useCallback, useRef } from 'react'
import { Link } from 'react-router-dom'
import { scrapersApi } from '@/api/services'
import { useAuthStore, toast } from '@/store'
import { formatDateTime, portalById } from '@/utils'

const POLL_MS = 4000

const STATUS_META = {
  QUEUED:  { label: 'W kolejce',        cls: 'run' },
  RUNNING: { label: 'W trakcie',        cls: 'run' },
  SUCCESS: { label: 'Sukces',           cls: 'ok' },
  PARTIAL: { label: 'Częściowy sukces', cls: 'warn' },
  FAILED:  { label: 'Błąd',             cls: 'err' },
}

// Sekcja scrapera jednego portalu: uruchomienie, trwający proces, wynik ostatniego przebiegu
export default function ScraperSection({ source }) {
  const isAdmin = useAuthStore(s => s.user?.role === 'ADMIN')
  const portal = portalById(source)
  const [status, setStatus]   = useState(null)
  const [error, setError]     = useState(null)
  const [starting, setStarting] = useState(false)
  const timer = useRef(null)

  const load = useCallback(async () => {
    try {
      const res = await scrapersApi.get(source)
      setStatus(res.data)
      setError(null)
      return res.data
    } catch (err) {
      setError(err)
      return null
    }
  }, [source])

  // odpytywanie tylko gdy coś pracuje
  useEffect(() => {
    let cancelled = false
    const tick = async () => {
      const s = await load()
      if (!cancelled && s?.running) timer.current = setTimeout(tick, POLL_MS)
    }
    tick()
    return () => { cancelled = true; clearTimeout(timer.current) }
  }, [load])

  const start = async () => {
    setStarting(true)
    try {
      await scrapersApi.run(source)
      toast.info(`Scraper ${portal.label} uruchomiony w tle`)
      clearTimeout(timer.current)
      const poll = async () => {
        const s = await load()
        if (s?.running) timer.current = setTimeout(poll, POLL_MS)
        else if (s?.lastRun) {
          const st = s.lastRun.status
          if (st === 'SUCCESS') toast.success(s.lastRun.message)
          else if (st === 'PARTIAL') toast.warn(s.lastRun.message)
          else toast.error(s.lastRun.message ?? 'Błąd przebiegu')
        }
      }
      poll()
    } catch (err) {
      if (err.status === 409) toast.warn(`Scraper ${portal.label} już pracuje`)
      else toast.error(err?.message ?? 'Nie udało się uruchomić')
      load()
    } finally {
      setStarting(false)
    }
  }

  const run = status?.lastRun
  const meta = run ? STATUS_META[run.status] ?? STATUS_META.FAILED : null
  const busy = status?.running || starting

  return (
    <section className="imp-section">
      <div className="imp-section-head">
        <span className="imp-section-icon">⚙</span>
        <div>
          <h2 className="imp-section-title">Scraper {portal.label}</h2>
          <p className="imp-section-sub">
            Pobiera aktualne oferty Java (praktykant / junior / mid) z {portal.label} i importuje je do bazy.
            Działa niezależnie od pozostałych portali.
          </p>
        </div>
      </div>

      <div className="imp-section-body">
        {error && !status && (
          <div className="imp-log imp-log--error">
            <span className="imp-log-icon">✕</span>
            <div className="imp-log-body">
              <p className="imp-log-title">Nie udało się pobrać statusu</p>
              <p className="imp-log-msg">{error.message}</p>
            </div>
          </div>
        )}

        {status && !status.enabled && (
          <p className="sc-note sc-note--warn">⚠ Scraper jest wyłączony w konfiguracji serwera.</p>
        )}

        <div className="imp-btn-row">
          {isAdmin ? (
            <button className="imp-action-btn imp-action-btn--accent" onClick={start}
                    disabled={busy || !status?.enabled}>
              {status?.running ? <><span className="imp-spin" />Scraper pracuje…</> : '▶ Uruchom scraper'}
            </button>
          ) : (
            <p className="sc-note">Uruchamianie scrapera jest dostępne dla administratora.</p>
          )}
          {isAdmin && (
            <Link to={`/admin?portal=${portal.slug}#scraper-urls`} className="imp-action-btn sc-urls-link">
              ⛓ Adresy wyszukiwania {portal.label}
            </Link>
          )}
        </div>

        {status?.running && (
          <div className="imp-log imp-log--run">
            <span className="imp-spin" />
            <div className="imp-log-body">
              <p className="imp-log-title">{run?.status === 'QUEUED' ? 'Czeka na zakończenie innego scrapera' : 'Trwa pobieranie ofert…'}</p>
              <p className="imp-log-msg">Start: {formatDateTime(run?.startedAt)} · możesz swobodnie korzystać z aplikacji.</p>
            </div>
          </div>
        )}

        {run && !status?.running && (
          <div className={`imp-log imp-log--${meta.cls === 'ok' ? 'success' : meta.cls === 'warn' ? 'warn' : 'error'}`}>
            <span className="imp-log-icon">{meta.cls === 'ok' ? '✓' : meta.cls === 'warn' ? '!' : '✕'}</span>
            <div className="imp-log-body">
              <p className="imp-log-title">
                Ostatni przebieg: {meta.label}
                <span className="sc-sub"> · {run.trigger === 'schedule' ? 'harmonogram' : 'ręcznie'}
                  {run.mode === 'IMPORT_ONLY' ? ' · tylko import' : ''} · {formatDateTime(run.finishedAt ?? run.startedAt)}</span>
              </p>
              {run.message && <p className="imp-log-msg">{run.message}</p>}
              <div className="imp-log-stats">
                {run.scraper && [
                  ['Na listach', run.scraper.found],
                  ['Unikalne', run.scraper.unique],
                  ['Już w bazie', run.scraper.alreadyInDatabase],
                  ['Pobrane', run.scraper.detailsOk],
                  ['Błędy', run.scraper.errors],
                  ['Czas [s]', run.scraper.durationSec],
                ].filter(([, v]) => v != null).map(([l, v]) => <Stat key={l} label={l} value={v} />)}
                {run.import && [
                  ['Nowe', run.import.imported, 'var(--green)'],
                  ['Zaktual.', run.import.updated],
                  ['Pominięte', run.import.skipped, 'var(--yellow)'],
                  ['Odrzucone', run.import.rejected, 'var(--red)'],
                ].filter(([, v]) => v != null).map(([l, v, c]) => <Stat key={l} label={l} value={v} color={c} />)}
              </div>
            </div>
          </div>
        )}

        {status && (
          <p className="sc-note">
            {status.todaysRun
              ? <>⏱ Dzisiejsze automatyczne {status.enabled ? 'pobranie' : 'import pliku'}: <strong>{status.todaysRun.runAt.slice(0, 5)}</strong>
                  {status.todaysRun.started ? ' — już uruchomione.' : '.'}</>
              : '⏱ Na dziś nie zaplanowano automatycznego pobrania.'}
            {' '}Harmonogram losowany codziennie o 00:01: jedna z 20 godzin przesunięta o 3/7/11/15/17 min w przód lub wstecz, inna niż wczoraj, min. 2 h odstępu od pozostałych portali.
            {!run && !status.running && ' Brak przebiegów od uruchomienia serwera.'}
          </p>
        )}
      </div>

      <style>{`
        .imp-action-btn--accent { background: var(--accent); color: #000; border-color: var(--accent); }
        .imp-action-btn--accent:hover:not(:disabled) { background: var(--accent-dim); border-color: var(--accent-dim); }
        .imp-log--run  { background: var(--accent-glow); border-color: var(--border-0); align-items: center; }
        .imp-log--warn { background: rgba(234,179,8,.06); border-color: rgba(234,179,8,.3); }
        .imp-log--warn .imp-log-icon { color: var(--yellow); }
        .sc-note { font-family: var(--font-mono); font-size: 0.74rem; color: var(--text-2); line-height: 1.6; }
        .sc-note strong { color: var(--accent); }
        .sc-note--warn { color: var(--yellow); }
        .sc-sub { font-family: var(--font-mono); font-weight: 400; font-size: 0.7rem; color: var(--text-3); }
        .sc-urls-link { text-decoration: none; }
      `}</style>
    </section>
  )
}

function Stat({ label, value, color }) {
  return (
    <div className="imp-log-stat">
      <span className="imp-log-stat-val" style={{ color: color ?? 'var(--text-0)' }}>{value}</span>
      <span className="imp-log-stat-label">{label}</span>
    </div>
  )
}
