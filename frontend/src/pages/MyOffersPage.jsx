// ╔══════════════════════════════════════════════╗
// ║  ŚCIEŻKA:  src/pages/MyOffersPage.jsx        ║
// ║  AKCJA:    NADPISZ istniejący plik (stub)    ║
// ╚══════════════════════════════════════════════╝

import { useState, useCallback, useMemo } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { userOffersApi } from '@/api/services'
import { useApi, useTitle, usePortal } from '@/hooks'
import { useAuthStore, toast } from '@/store'
import { Badge, EmptyState, PageHeader } from '@/components/ui'
import PortalTabs from '@/components/ui/PortalTabs'
import { formatDate, normalizeLevel, truncate, formatSalary, portalById } from '@/utils'

// ─────────────────────────────────────────────────────────────────
// Stałe
// ─────────────────────────────────────────────────────────────────
const TABS = [
  { id: 'not-applied', label: 'Do aplikacji',   icon: '◎' },
  { id: 'applied',     label: 'Zaaplikowane',   icon: '✓' },
]

// Klucze dat w kolejności priorytetu
const DATE_KEYS = ['createdAt', 'fetchedAt', 'updatedAt']
function getDate(offer) {
  for (const k of DATE_KEYS) if (offer[k]) return offer[k]
  return null
}

// ─────────────────────────────────────────────────────────────────
// Komponent główny
// ─────────────────────────────────────────────────────────────────
// Poziomy w stałej kolejności; pozostałe (np. Manager) po nich, nieokreślony na końcu
const ALL_LEVELS = 'Wszystkie'
const UNKNOWN_LEVEL = 'Nieokreślony'
const LEVEL_ORDER = ['Trainee', 'Junior', 'Mid', 'Senior', 'Expert']

function levelOf(offer) {
  const l = normalizeLevel(offer.level)
  return !l || l === '—' || l.toLowerCase().startsWith('nieokre') ? UNKNOWN_LEVEL : l
}

function levelRank(l) {
  if (l === UNKNOWN_LEVEL) return 1000
  const i = LEVEL_ORDER.indexOf(l)
  return i >= 0 ? i : LEVEL_ORDER.length
}

// key={portal} → osobne listy, liczniki i wskaźnik aplikacji dla każdego portalu;
// wybrany poziom trzymany w ?level=… (zostaje przy zmianie portalu i odświeżeniu)
export default function MyOffersPage() {
  const [portal, setPortal] = usePortal()
  const [params, setParams] = useSearchParams()
  const level = params.get('level') || ALL_LEVELS
  const setLevel = useCallback((l) => {
    setParams(prev => {
      const next = new URLSearchParams(prev)
      if (l === ALL_LEVELS) next.delete('level'); else next.set('level', l)
      return next
    }, { replace: true })
  }, [setParams])
  return (
    <MyOffersView
      key={portal}
      source={portal}
      level={level}
      onLevelChange={setLevel}
      tabs={<PortalTabs value={portal} onChange={setPortal} />}
    />
  )
}

function MyOffersView({ source, tabs, level, onLevelChange }) {
  useTitle('Moje oferty')
  const navigate  = useNavigate()
  const user      = useAuthStore((s) => s.user)
  const userId    = user?.id

  const [activeTab, setActiveTab] = useState('not-applied')

  // Oba listy ładowane niezależnie
  const {
    data: notApplied,
    loading: loadingNA,
    error: errorNA,
    execute: reloadNA,
  } = useApi(
    useCallback(() => userOffersApi.getNotApplied(userId, source), [userId, source]),
    { immediate: !!userId }
  )

  const {
    data: applied,
    loading: loadingA,
    error: errorA,
    execute: reloadA,
  } = useApi(
    useCallback(() => userOffersApi.getApplied(userId, source), [userId, source]),
    { immediate: !!userId }
  )

  // Po aplikacji odśwież obie listy
  const handleApplied = useCallback(() => {
    reloadNA()
    reloadA()
  }, [reloadNA, reloadA])

  // Poziomy występujące w ofertach tego portalu (obie listy) + liczniki dla bieżącej zakładki
  const isNA = activeTab === 'not-applied'
  const levels = useMemo(() => {
    const all = [...(notApplied ?? []), ...(applied ?? [])]
    const current = isNA ? (notApplied ?? []) : (applied ?? [])
    const names = new Set(all.map(levelOf))
    if (level !== ALL_LEVELS) names.add(level)
    return [...names]
      .sort((a, b) => levelRank(a) - levelRank(b) || a.localeCompare(b, 'pl'))
      .map(name => ({ name, count: current.filter(o => levelOf(o) === name).length }))
  }, [notApplied, applied, isNA, level])

  const byLevel = useCallback(
    (items) => level === ALL_LEVELS ? (items ?? []) : (items ?? []).filter(o => levelOf(o) === level),
    [level]
  )
  const notAppliedFiltered = byLevel(notApplied)
  const appliedFiltered    = byLevel(applied)

  const appliedCount    = appliedFiltered.length
  const notAppliedCount = notAppliedFiltered.length
  const totalCurrent    = (isNA ? notApplied : applied)?.length ?? 0
  const loadingLevels   = loadingNA || loadingA

  const list    = isNA ? notAppliedFiltered : appliedFiltered
  const loading = isNA ? loadingNA : loadingA
  const error   = isNA ? errorNA   : errorA
  const reload  = isNA ? reloadNA  : reloadA

  // Brak userId w sesji
  if (!userId) {
    return (
      <div className="my-offers-page animate-fade-in">
        <PageHeader title="Moje oferty" subtitle="Zarządzaj aplikacjami" />
        <EmptyState
          icon="⊙"
          title="Brak danych sesji"
          description="Nie można pobrać ID użytkownika z sesji. Wyloguj się i zaloguj ponownie."
        />
        <MyOffersStyles />
      </div>
    )
  }

  return (
    <div className="my-offers-page animate-fade-in">

      {/* ── Nagłówek ── */}
      <PageHeader
        title="Moje oferty"
        subtitle={`Zarządzaj swoimi aplikacjami — ${portalById(source).label}`}
        actions={
          <button
            className="btn btn--secondary btn--sm"
            onClick={reload}
            disabled={loading}
          >
            ↻ Odśwież
          </button>
        }
      />

      {tabs}

      {/* ── Przełącznik poziomów (dla wybranej strony) ── */}
      <div className="mo-levels" role="group" aria-label="Poziom doświadczenia">
        <button
          className={`mo-pill${level === ALL_LEVELS ? ' mo-pill--on' : ''}`}
          onClick={() => onLevelChange(ALL_LEVELS)}
        >
          Wszystkie {!loadingLevels && <span className="mo-pill-count">{totalCurrent}</span>}
        </button>
        {levels.map(l => (
          <button
            key={l.name}
            className={`mo-pill${level === l.name ? ' mo-pill--on' : ''}${!loadingLevels && l.count === 0 ? ' mo-pill--empty' : ''}`}
            onClick={() => onLevelChange(l.name)}
          >
            {l.name} {!loadingLevels && <span className="mo-pill-count">{l.count}</span>}
          </button>
        ))}
      </div>

      {/* ── Licznik aplikacji ── */}
      <div className="mo-stats">
        <div className="mo-stat-box">
          <span className="mo-stat-value">{appliedCount}</span>
          <span className="mo-stat-label">Zaaplikowano</span>
        </div>
        <div className="mo-stat-box">
          <span className="mo-stat-value">{notAppliedCount}</span>
          <span className="mo-stat-label">Oczekuje</span>
        </div>
        <div className="mo-stat-box">
          <span className="mo-stat-value">
            {appliedCount + notAppliedCount > 0
              ? `${Math.round((appliedCount / (appliedCount + notAppliedCount)) * 100)}%`
              : '—'}
          </span>
          <span className="mo-stat-label">Wskaźnik aplikacji</span>
        </div>
      </div>

      {/* ── Zakładki ── */}
      <div className="mo-tabs" role="tablist">
        {TABS.map((tab) => {
          const count = tab.id === 'not-applied' ? notAppliedCount : appliedCount
          return (
            <button
              key={tab.id}
              role="tab"
              aria-selected={activeTab === tab.id}
              className={`mo-tab${activeTab === tab.id ? ' mo-tab--active' : ''}`}
              onClick={() => setActiveTab(tab.id)}
            >
              <span className="mo-tab-icon">{tab.icon}</span>
              {tab.label}
              <span className={`mo-tab-count${activeTab === tab.id ? ' mo-tab-count--active' : ''}`}>
                {count}
              </span>
            </button>
          )
        })}
      </div>

      {/* ── Skeleton ── */}
      {loading && (
        <div className="mo-grid">
          {Array.from({ length: 8 }).map((_, i) => (
            <div key={i} className="mo-skel" style={{ animationDelay: `${i * 40}ms` }} />
          ))}
        </div>
      )}

      {/* ── Błąd ── */}
      {!loading && error && (
        <EmptyState
          icon="✕"
          title="Błąd ładowania"
          description={error.message ?? 'Nie udało się pobrać listy ofert.'}
          action={
            <button className="btn btn--primary btn--sm" onClick={reload}>
              Spróbuj ponownie
            </button>
          }
        />
      )}

      {/* ── Pusta lista ── */}
      {!loading && !error && list.length === 0 && (
        <EmptyState
          icon={isNA ? '◎' : '✓'}
          title={level !== ALL_LEVELS
            ? `Brak ofert na poziomie ${level}`
            : isNA ? 'Brak ofert do aplikacji' : 'Brak zaaplikowanych ofert'}
          description={
            level !== ALL_LEVELS
              ? `Na tej liście nie ma ofert ${portalById(source).label} na poziomie ${level}. Wybierz inny poziom.`
              : isNA
              ? 'Wszystkie oferty zostały już przetworzone lub baza jest pusta.'
              : 'Nie zaaplikowałeś jeszcze na żadną ofertę. Przejdź do listy ofert i kliknij "Aplikuj".'
          }
          action={
            level !== ALL_LEVELS ? (
              <button className="btn btn--secondary btn--sm" onClick={() => onLevelChange(ALL_LEVELS)}>
                Pokaż wszystkie poziomy
              </button>
            ) : isNA ? null : (
              <button
                className="btn btn--primary btn--sm"
                onClick={() => navigate('/offers')}
              >
                Przeglądaj oferty →
              </button>
            )
          }
        />
      )}

      {/* ── Lista kart ── */}
      {!loading && !error && list.length > 0 && (
        <div className="mo-grid">
          {list.map((offer, i) => (
            <MyOfferCard
              key={offer.id}
              offer={offer}
              isApplied={!isNA}
              userId={userId}
              onDetail={() => navigate(`/offers/${offer.id}`)}
              onApplied={handleApplied}
              style={{ animationDelay: `${Math.min(i * 30, 400)}ms` }}
            />
          ))}
        </div>
      )}

      <MyOffersStyles />
    </div>
  )
}

// ─────────────────────────────────────────────────────────────────
// Karta oferty użytkownika
// ─────────────────────────────────────────────────────────────────
function MyOfferCard({ offer, isApplied, userId, onDetail, onApplied, style }) {
  const [applying, setApplying] = useState(false)
  const level  = normalizeLevel(offer.level)
  const salary = formatSalary(offer.salaryRange ?? offer.salary)
  const date   = getDate(offer)

  const handleApply = async (e) => {
    e.stopPropagation()
    setApplying(true)
    try {
      await userOffersApi.applyToOffer(userId, offer.id)
      toast.success(`Aplikacja na "${truncate(offer.title, 40)}" zapisana!`)
      onApplied()
    } catch (err) {
      toast.error(err?.message ?? 'Błąd podczas aplikowania')
    } finally {
      setApplying(false)
    }
  }

  return (
    <article
      className="mo-card animate-fade-in"
      style={style}
      onClick={onDetail}
      tabIndex={0}
      role="button"
      onKeyDown={(e) => e.key === 'Enter' && onDetail()}
    >
      {/* Górny pasek: badge + status */}
      <div className="mo-card-top">
        <Badge level={level} />
        {isApplied
          ? <span className="mo-status mo-status--applied">✓ Zaaplikowano</span>
          : <span className="mo-status mo-status--pending">◎ Oczekuje</span>
        }
      </div>

      {/* Tytuł */}
      <h3 className="mo-card-title">{truncate(offer.title, 70)}</h3>

      {/* Firma */}
      <p className="mo-card-company">
        <span aria-hidden="true" style={{ color: 'var(--text-3)', fontSize: '.72rem' }}>◉</span>
        {offer.companyName ?? offer.company ?? '—'}
      </p>

      {/* Chipy: miasto + wynagrodzenie */}
      <div className="mo-chips">
        {offer.city && (
          <span className="mo-chip">◎ {offer.city}</span>
        )}
        {salary && (
          <span className="mo-chip mo-chip--salary">₿ {salary}</span>
        )}
      </div>

      {/* Stopka: data + przycisk */}
      <div className="mo-card-foot">
        <time className="mo-card-date">{formatDate(date)}</time>
        <div className="mo-card-actions" onClick={(e) => e.stopPropagation()}>
          {!isApplied && (
            <button
              className="mo-apply-btn"
              onClick={handleApply}
              disabled={applying}
              aria-label="Aplikuj na tę ofertę"
            >
              {applying
                ? <span className="mo-apply-spin" aria-hidden="true" />
                : '✓ Aplikuj'
              }
            </button>
          )}
          <button
            className="mo-detail-btn"
            onClick={(e) => { e.stopPropagation(); onDetail() }}
            aria-label="Szczegóły oferty"
          >
            →
          </button>
        </div>
      </div>
    </article>
  )
}

// ─────────────────────────────────────────────────────────────────
// Style
// ─────────────────────────────────────────────────────────────────
function MyOffersStyles() {
  return (
    <style>{`
      .my-offers-page { max-width: 1080px; }

      /* ── Przełącznik poziomów ── */
      .mo-levels { display: flex; gap: 6px; flex-wrap: wrap; margin-bottom: 16px; }
      .mo-pill {
        display: inline-flex; align-items: center; gap: 7px;
        padding: 5px 13px; border-radius: 100px;
        border: 1px solid var(--border-1); background: var(--bg-2);
        color: var(--text-1); font-family: var(--font-mono); font-size: 0.72rem; font-weight: 600;
        cursor: pointer; white-space: nowrap; transition: border-color .15s, color .15s, background .15s;
      }
      .mo-pill:hover { border-color: var(--accent); color: var(--accent); }
      .mo-pill--on { background: var(--accent); color: #fff; border-color: var(--accent); }
      .mo-pill--on:hover { color: #fff; }
      .mo-pill--empty:not(.mo-pill--on) { opacity: .55; }
      .mo-pill-count {
        font-size: 0.62rem; font-weight: 700; padding: 0 6px; border-radius: 100px;
        background: var(--bg-4); color: var(--text-1);
      }
      .mo-pill--on .mo-pill-count { background: rgba(255,255,255,.22); color: #fff; }

      /* ── Statystyki ── */
      .mo-stats {
        display: flex; gap: 10px; margin-bottom: 20px; flex-wrap: wrap;
      }
      .mo-stat-box {
        display: flex; flex-direction: column; gap: 3px;
        background: var(--bg-1); border: 1px solid var(--border-1);
        border-radius: var(--radius-lg); padding: 14px 20px;
        min-width: 120px; flex: 1;
      }
      .mo-stat-value {
        font-family: var(--font-display); font-size: 1.7rem;
        font-weight: 800; color: var(--accent); line-height: 1;
      }
      .mo-stat-label {
        font-family: var(--font-mono); font-size: 0.65rem;
        color: var(--text-2); text-transform: uppercase; letter-spacing: .08em;
      }

      /* ── Zakładki ── */
      .mo-tabs {
        display: flex; gap: 4px; margin-bottom: 20px;
        border-bottom: 1px solid var(--border-1); padding-bottom: 0;
      }
      .mo-tab {
        display: flex; align-items: center; gap: 7px;
        padding: 9px 16px; background: none; border: none;
        border-bottom: 2px solid transparent; margin-bottom: -1px;
        font-family: var(--font-mono); font-size: 0.78rem; color: var(--text-2);
        cursor: pointer; transition: color .15s, border-color .15s;
        white-space: nowrap;
      }
      .mo-tab:hover { color: var(--text-0); }
      .mo-tab--active { color: var(--accent); border-bottom-color: var(--accent); }
      .mo-tab-icon { font-size: 0.85rem; }
      .mo-tab-count {
        font-size: 0.65rem; font-weight: 700;
        background: var(--bg-3); color: var(--text-2);
        border: 1px solid var(--border-1);
        padding: 1px 6px; border-radius: 100px;
        transition: background .15s, color .15s, border-color .15s;
      }
      .mo-tab-count--active {
        background: var(--accent-glow); color: var(--accent);
        border-color: var(--border-0);
      }

      /* ── Siatka ── */
      .mo-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
        gap: 12px;
      }

      /* ── Karta ── */
      .mo-card {
        display: flex; flex-direction: column; gap: 7px;
        background: var(--bg-1); border: 1px solid var(--border-1);
        border-radius: var(--radius-lg); padding: 1.05rem 1.1rem;
        cursor: pointer; outline: none;
        transition: border-color .15s, transform .15s, box-shadow .15s;
      }
      .mo-card:hover,
      .mo-card:focus-visible {
        border-color: var(--accent);
        transform: translateY(-2px);
        box-shadow: var(--shadow-accent);
      }

      /* Top: badge + status */
      .mo-card-top {
        display: flex; align-items: center;
        justify-content: space-between; gap: 6px;
      }
      .mo-status {
        font-family: var(--font-mono); font-size: 0.62rem; font-weight: 700;
        letter-spacing: .07em; padding: 2px 8px;
        border-radius: 100px; border: 1px solid; white-space: nowrap;
      }
      .mo-status--applied {
        color: var(--green); border-color: rgba(34,197,94,.35);
        background: rgba(34,197,94,.08);
      }
      .mo-status--pending {
        color: var(--yellow); border-color: rgba(234,179,8,.35);
        background: rgba(234,179,8,.08);
      }

      /* Tytuł */
      .mo-card-title {
        font-family: var(--font-display); font-weight: 700;
        font-size: 0.89rem; color: var(--text-0); line-height: 1.35; margin: 0;
        display: -webkit-box; -webkit-line-clamp: 2;
        -webkit-box-orient: vertical; overflow: hidden;
      }

      /* Firma */
      .mo-card-company {
        display: flex; align-items: center; gap: 5px;
        font-size: 0.77rem; color: var(--text-2); margin: 0;
      }

      /* Chipy */
      .mo-chips { display: flex; flex-wrap: wrap; gap: 5px; }
      .mo-chip {
        display: flex; align-items: center; gap: 4px;
        font-family: var(--font-mono); font-size: 0.68rem; color: var(--text-2);
        background: var(--bg-2); border: 1px solid var(--border-1);
        padding: 3px 8px; border-radius: var(--radius-sm);
      }
      .mo-chip--salary {
        color: var(--cyan); border-color: rgba(0,212,212,.2);
        background: rgba(0,212,212,.05);
      }

      /* Stopka */
      .mo-card-foot {
        display: flex; justify-content: space-between; align-items: center;
        margin-top: auto; padding-top: 8px; border-top: 1px solid var(--border-1);
        gap: 8px;
      }
      .mo-card-date {
        font-family: var(--font-mono); font-size: 0.66rem; color: var(--text-3);
      }
      .mo-card-actions { display: flex; gap: 5px; align-items: center; }

      /* Przycisk Aplikuj */
      .mo-apply-btn {
        position: relative;
        padding: 5px 13px; font-family: var(--font-mono); font-size: 0.72rem;
        font-weight: 700; color: #000;
        background: var(--accent); border: 1px solid var(--accent);
        border-radius: var(--radius-md); cursor: pointer;
        transition: background .15s, box-shadow .15s, transform .1s;
        white-space: nowrap; min-width: 72px;
        display: flex; align-items: center; justify-content: center;
      }
      .mo-apply-btn:hover:not(:disabled) {
        background: var(--accent-dim); box-shadow: var(--shadow-accent);
      }
      .mo-apply-btn:active:not(:disabled) { transform: scale(.97); }
      .mo-apply-btn:disabled { opacity: .55; cursor: not-allowed; }
      .mo-apply-spin {
        width: 12px; height: 12px;
        border: 2px solid transparent; border-top-color: #000;
        border-radius: 50%; animation: spin .6s linear infinite;
      }

      /* Przycisk Szczegóły */
      .mo-detail-btn {
        width: 28px; height: 28px;
        display: flex; align-items: center; justify-content: center;
        background: var(--bg-2); border: 1px solid var(--border-1);
        border-radius: var(--radius-md); color: var(--text-2);
        font-size: 0.8rem; cursor: pointer;
        transition: border-color .15s, color .15s;
      }
      .mo-detail-btn:hover { border-color: var(--accent); color: var(--accent); }

      /* Skeleton */
      .mo-skel {
        height: 158px; border-radius: var(--radius-lg);
        background: linear-gradient(90deg, var(--bg-2) 25%, var(--bg-3) 50%, var(--bg-2) 75%);
        background-size: 200% 100%;
        animation: shimmer 1.5s ease infinite;
      }
    `}</style>
  )
}