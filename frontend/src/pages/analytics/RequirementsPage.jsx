import { useState, useEffect } from 'react'
import { analyticsApi } from '@/api/services'
import { useTitle } from '@/hooks'
import { PageHeader, EmptyState } from '@/components/ui'

const ALL_TAB = '__all__'

// ─────────────────────────────────────────────────────────────────
function SkillList({ data, loading }) {
  if (loading) return <SkillListSkeleton />
  if (!data?.length) return (
    <EmptyState icon="◌" title="Brak danych" description="Brak wymagań w bazie dla wybranego filtra." />
  )

  const max = data[0]?.count ?? 1

  return (
    <div className="rq-list">
      {data.map((item, i) => {
        const pct = Math.round((item.count / max) * 100)
        return (
          <div key={item.skill} className="rq-row">
            <span className="rq-rank">{i + 1}</span>
            <span className="rq-skill">{item.skill}</span>
            <div className="rq-bar-wrap">
              <div className="rq-bar-fill" style={{ width: `${pct}%` }} />
            </div>
            <span className="rq-count">{item.count}</span>
          </div>
        )
      })}
    </div>
  )
}

function SkillListSkeleton() {
  return (
    <div className="rq-list">
      {Array.from({ length: 12 }, (_, i) => (
        <div key={i} className="rq-row rq-row--skel">
          <div className="rq-skel rq-skel--rank" />
          <div className="rq-skel rq-skel--skill" style={{ width: `${40 + (i % 5) * 10}%` }} />
          <div className="rq-skel rq-skel--bar" />
          <div className="rq-skel rq-skel--count" />
        </div>
      ))}
    </div>
  )
}

// ─────────────────────────────────────────────────────────────────
export default function RequirementsPage() {
  useTitle('Analityka wymagań')

  const [levels,         setLevels]         = useState([])
  const [sources,        setSources]        = useState([])
  const [selectedSources,setSelectedSources]= useState([])
  const [activeTab,      setActiveTab]      = useState(ALL_TAB)
  const [cache,          setCache]          = useState({})
  const [loading,        setLoading]        = useState(false)

  // Pobierz dostępne poziomy i źródła
  useEffect(() => {
    analyticsApi.getAvailableLevels()
      .then(res => setLevels(Array.isArray(res.data) ? res.data : []))
      .catch(() => {})
    analyticsApi.getAvailableSources()
      .then(res => {
        const list = Array.isArray(res.data) ? res.data : []
        setSources(list)
        setSelectedSources(list) // domyślnie wszystkie zaznaczone
      })
      .catch(() => {})
  }, [])

  // Klucz cache uwzględnia zakładkę i wybrane źródła
  const cacheKey = `${activeTab}::${[...selectedSources].sort().join(',')}`

  useEffect(() => {
    if (cache[cacheKey] !== undefined) return
    setLoading(true)
    const req = activeTab === ALL_TAB
      ? analyticsApi.getTopSkills(selectedSources)
      : analyticsApi.getTopSkillsByLevel(activeTab, selectedSources)
    req
      .then(res => setCache(c => ({ ...c, [cacheKey]: res.data ?? [] })))
      .catch(() => setCache(c => ({ ...c, [cacheKey]: [] })))
      .finally(() => setLoading(false))
  }, [activeTab, cacheKey])

  const toggleSource = (src) => {
    setSelectedSources(prev =>
      prev.includes(src) ? prev.filter(s => s !== src) : [...prev, src]
    )
  }

  const tabs = [{ key: ALL_TAB, label: 'Wszystkie' }, ...levels.map(l => ({ key: l, label: l }))]
  const data = cache[cacheKey]

  return (
    <div className="rq-page animate-fade-in">
      <PageHeader
        title="Analityka wymagań"
        subtitle="Wymagania rynkowe posortowane po liczbie wystąpień"
      />

      {/* ── Filtry źródeł ── */}
      {sources.length > 0 && (
        <div className="rq-sources">
          <span className="rq-sources-label">Źródło:</span>
          {sources.map(src => (
            <label key={src} className="rq-source-check">
              <input
                type="checkbox"
                checked={selectedSources.includes(src)}
                onChange={() => toggleSource(src)}
              />
              <span className="rq-source-name">{src}</span>
            </label>
          ))}
        </div>
      )}

      {/* ── Przyciski poziomów ── */}
      <div className="rq-tabs">
        {tabs.map(({ key, label }) => (
          <button
            key={key}
            className={`rq-tab${activeTab === key ? ' rq-tab--active' : ''}`}
            onClick={() => setActiveTab(key)}
          >
            {label}
            {cache[`${key}::${[...selectedSources].sort().join(',')}`] !== undefined && (
              <span className="rq-tab-count">
                {cache[`${key}::${[...selectedSources].sort().join(',')}`].length}
              </span>
            )}
          </button>
        ))}
      </div>

      {/* ── Lista ── */}
      <div className="rq-content">
        <SkillList data={data} loading={loading} />
      </div>

      <ReqStyles />
    </div>
  )
}

// ─────────────────────────────────────────────────────────────────
function ReqStyles() {
  return (
    <style>{`
      .rq-page { max-width: 860px; }

      /* ── Filtry źródeł ── */
      .rq-sources {
        display: flex; align-items: center; flex-wrap: wrap; gap: 12px;
        margin-bottom: 16px;
        padding: 10px 14px;
        background: var(--bg-1); border: 1px solid var(--border-1);
        border-radius: var(--radius-md);
      }
      .rq-sources-label {
        font-family: var(--font-mono); font-size: 0.68rem; font-weight: 700;
        color: var(--text-3); text-transform: uppercase; letter-spacing: .07em;
        flex-shrink: 0;
      }
      .rq-source-check {
        display: inline-flex; align-items: center; gap: 6px;
        cursor: pointer; user-select: none;
      }
      .rq-source-check input[type="checkbox"] {
        accent-color: var(--accent);
        width: 14px; height: 14px; cursor: pointer; flex-shrink: 0;
      }
      .rq-source-name {
        font-family: var(--font-mono); font-size: 0.78rem; font-weight: 600;
        color: var(--text-1);
      }
      .rq-source-check:hover .rq-source-name { color: var(--accent); }

      /* ── Przyciski poziomów ── */
      .rq-tabs {
        display: flex; flex-wrap: wrap; gap: 8px; margin-bottom: 16px;
      }
      .rq-tab {
        display: inline-flex; align-items: center; gap: 7px;
        font-family: var(--font-mono); font-size: 0.78rem; font-weight: 600;
        padding: 7px 16px; border-radius: 100px;
        border: 1px solid var(--border-1);
        background: var(--bg-1); color: var(--text-2);
        cursor: pointer; transition: border-color .15s, color .15s, background .15s;
        white-space: nowrap;
      }
      .rq-tab:hover { border-color: var(--border-0); color: var(--text-0); }
      .rq-tab--active {
        border-color: var(--accent); color: var(--accent);
        background: var(--accent-glow);
      }
      .rq-tab-count {
        font-size: 0.66rem; font-weight: 700;
        background: var(--bg-3); color: var(--text-3);
        border-radius: 100px; padding: 1px 6px;
      }
      .rq-tab--active .rq-tab-count {
        background: rgba(245,166,35,.18); color: var(--accent);
      }

      /* ── Lista ── */
      .rq-content {
        background: var(--bg-1); border: 1px solid var(--border-1);
        border-radius: var(--radius-lg); overflow: hidden;
      }
      .rq-list { display: flex; flex-direction: column; }

      .rq-row {
        display: grid;
        grid-template-columns: 36px 1fr 140px 52px;
        align-items: center; gap: 12px;
        padding: 9px 16px;
        border-bottom: 1px solid var(--border-2);
        transition: background .1s;
      }
      .rq-row:last-child { border-bottom: none; }
      .rq-row:hover { background: var(--bg-2); }

      .rq-rank {
        font-family: var(--font-mono); font-size: 0.68rem;
        color: var(--text-3); text-align: right; flex-shrink: 0;
      }
      .rq-skill {
        font-family: var(--font-mono); font-size: 0.82rem;
        color: var(--text-0); font-weight: 600;
        overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
      }
      .rq-bar-wrap {
        height: 6px; background: var(--bg-3);
        border-radius: 3px; overflow: hidden;
      }
      .rq-bar-fill {
        height: 100%; background: var(--accent);
        border-radius: 3px; transition: width .4s ease;
      }
      .rq-count {
        font-family: var(--font-mono); font-size: 0.78rem;
        font-weight: 700; color: var(--accent); text-align: right;
      }

      /* ── Skeleton ── */
      .rq-row--skel { pointer-events: none; }
      .rq-skel {
        border-radius: var(--radius-sm);
        background: linear-gradient(90deg, var(--bg-2) 25%, var(--bg-3) 50%, var(--bg-2) 75%);
        background-size: 200% 100%;
        animation: shimmer 1.5s ease infinite;
      }
      .rq-skel--rank  { width: 20px; height: 10px; }
      .rq-skel--skill { height: 10px; }
      .rq-skel--bar   { height: 6px; }
      .rq-skel--count { width: 28px; height: 10px; margin-left: auto; }
    `}</style>
  )
}
