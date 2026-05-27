import { useState, useEffect, useCallback } from 'react'
import {
  BarChart, Bar, XAxis, YAxis, Tooltip,
  ResponsiveContainer, Cell,
} from 'recharts'
import { analyticsApi } from '@/api/services'
import { useApi, useTitle } from '@/hooks'
import { PageHeader, EmptyState } from '@/components/ui'

// ─────────────────────────────────────────────────────────────────
const LEVEL_COLORS = {
  Trainee: '#64748b',
  Junior:  '#22c55e',
  Mid:     '#f5a623',
  Senior:  '#00d4d4',
  Expert:  '#a855f7',
}
const SKILL_COLOR   = '#f5a623'
const SKILL_COLORS  = [
  '#f5a623', '#00d4d4', '#a855f7', '#22c55e',
  '#ef4444', '#eab308', '#3b82f6', '#ec4899',
  '#14b8a6', '#f97316', '#8b5cf6', '#06b6d4',
]

// ─────────────────────────────────────────────────────────────────
function ChartSkeleton({ height = 340 }) {
  return <div className="req-skel" style={{ height }} aria-hidden="true" />
}

function CustomTooltip({ active, payload, label }) {
  if (!active || !payload?.length) return null
  return (
    <div className="req-tooltip">
      <p className="req-tooltip-label">{label ?? payload[0]?.name}</p>
      <p className="req-tooltip-val">{payload[0]?.value} ofert</p>
    </div>
  )
}

// ─────────────────────────────────────────────────────────────────
// Wykres poziomy — top skille
// ─────────────────────────────────────────────────────────────────
function SkillBarChart({ data, loading, error, color }) {
  if (loading) return <ChartSkeleton height={Math.max(220, (data?.length ?? 20) * 22)} />
  if (error)   return <EmptyState icon="✕" title="Błąd" description={error.message ?? 'Nie udało się załadować.'} />
  if (!data?.length) return <EmptyState icon="◌" title="Brak danych" description="Brak wymagań w bazie." />

  const chartData = [...data].reverse()

  return (
    <ResponsiveContainer width="100%" height={Math.max(220, chartData.length * 28)}>
      <BarChart
        data={chartData}
        layout="vertical"
        margin={{ top: 4, right: 40, left: 8, bottom: 4 }}
      >
        <XAxis
          type="number"
          tick={{ fill: '#70708a', fontFamily: 'JetBrains Mono', fontSize: 10 }}
          axisLine={false}
          tickLine={false}
          allowDecimals={false}
        />
        <YAxis
          type="category"
          dataKey="skill"
          width={140}
          tick={{ fill: '#b0b0c8', fontFamily: 'JetBrains Mono', fontSize: 11 }}
          axisLine={false}
          tickLine={false}
        />
        <Tooltip content={<CustomTooltip />} cursor={{ fill: 'rgba(245,166,35,0.06)' }} />
        <Bar dataKey="count" radius={[0, 4, 4, 0]} maxBarSize={18}>
          {chartData.map((entry, i) => (
            <Cell
              key={entry.skill}
              fill={color ?? SKILL_COLORS[i % SKILL_COLORS.length]}
            />
          ))}
        </Bar>
      </BarChart>
    </ResponsiveContainer>
  )
}

// ─────────────────────────────────────────────────────────────────
// Karta wykresu
// ─────────────────────────────────────────────────────────────────
function ChartCard({ title, sub, children }) {
  return (
    <div className="req-chart-card">
      <div className="req-chart-head">
        <h2 className="req-chart-title">{title}</h2>
        {sub && <p className="req-chart-sub">{sub}</p>}
      </div>
      {children}
    </div>
  )
}

// ─────────────────────────────────────────────────────────────────
// Karta statystyczna
// ─────────────────────────────────────────────────────────────────
function StatCard({ icon, label, value, loading }) {
  return (
    <div className="req-stat-card">
      <span className="req-stat-icon">{icon}</span>
      <div className="req-stat-body">
        {loading
          ? <div className="req-skel req-skel--val" />
          : <span className="req-stat-val">{value ?? '—'}</span>
        }
        <span className="req-stat-label">{label}</span>
      </div>
    </div>
  )
}

// ─────────────────────────────────────────────────────────────────
// Sekcja per-level
// ─────────────────────────────────────────────────────────────────
function LevelSection({ level, limit }) {
  const {
    data, loading, error,
  } = useApi(
    useCallback(() => analyticsApi.getTopSkillsByLevel(level, limit), [level, limit]),
    { immediate: true },
  )

  const color = LEVEL_COLORS[level] ?? SKILL_COLOR

  return (
    <ChartCard
      title={`Top ${limit} — ${level}`}
      sub={`Najczęstsze wymagania dla poziomu ${level}`}
    >
      <SkillBarChart data={data} loading={loading} error={error} color={color} />
    </ChartCard>
  )
}

// ─────────────────────────────────────────────────────────────────
// Strona główna
// ─────────────────────────────────────────────────────────────────
export default function RequirementsPage() {
  useTitle('Analityka wymagań')

  const [levels, setLevels] = useState([])

  const {
    data: topSkills, loading: loadingTop, error: errorTop,
  } = useApi(useCallback(() => analyticsApi.getTopSkills(20), []), { immediate: true })

  useEffect(() => {
    analyticsApi.getAvailableLevels()
      .then(res => setLevels(Array.isArray(res.data) ? res.data : []))
      .catch(() => setLevels([]))
  }, [])

  const topSkill = topSkills?.[0]

  return (
    <div className="req-page animate-fade-in">
      <PageHeader
        title="Analityka wymagań"
        subtitle="Najpopularniejsze umiejętności na rynku pracy"
      />

      {/* ── Karty liczników ── */}
      <div className="req-stat-row">
        <StatCard icon="⬡" label="Top skill"        value={topSkill?.skill ?? null} loading={loadingTop} />
        <StatCard icon="◉" label="Wystąpień (top)"  value={topSkill?.count ?? null} loading={loadingTop} />
        <StatCard icon="▦" label="Poziomów"         value={levels.length || null}   loading={!levels.length && loadingTop} />
        <StatCard icon="◎" label="Unikalnych skilli" value={topSkills?.length ?? null} loading={loadingTop} />
      </div>

      {/* ── Top 20 overall ── */}
      <div className="req-charts-grid req-charts-grid--full">
        <ChartCard
          title="Top 20 wymagań — wszystkie poziomy"
          sub="Skille posortowane po liczbie wystąpień we wszystkich ofertach"
        >
          <SkillBarChart data={topSkills} loading={loadingTop} error={errorTop} />
        </ChartCard>
      </div>

      {/* ── Per-level ── */}
      {levels.length > 0 && (
        <>
          <div className="req-section-head">
            <h2 className="req-section-title">Wymagania według poziomu</h2>
            <p className="req-section-sub">Top 15 skilli dla każdego poziomu stanowiska</p>
          </div>
          <div className="req-charts-grid">
            {levels.map(level => (
              <LevelSection key={level} level={level} limit={15} />
            ))}
          </div>
        </>
      )}

      <ReqStyles />
    </div>
  )
}

// ─────────────────────────────────────────────────────────────────
function ReqStyles() {
  return (
    <style>{`
      .req-page { max-width: 1200px; }

      /* ── Karty liczników ── */
      .req-stat-row {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
        gap: 10px; margin-bottom: 20px;
      }
      .req-stat-card {
        display: flex; align-items: center; gap: 14px;
        background: var(--bg-1); border: 1px solid var(--border-1);
        border-radius: var(--radius-lg); padding: 16px 18px;
        transition: border-color .15s;
      }
      .req-stat-card:hover { border-color: var(--border-0); }
      .req-stat-icon { font-size: 1.3rem; color: var(--accent); flex-shrink: 0; }
      .req-stat-body { display: flex; flex-direction: column; gap: 2px; min-width: 0; overflow: hidden; }
      .req-stat-val {
        font-family: var(--font-display); font-size: 1.4rem;
        font-weight: 800; color: var(--text-0); line-height: 1;
        overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
      }
      .req-stat-label {
        font-family: var(--font-mono); font-size: 0.63rem;
        color: var(--text-2); text-transform: uppercase; letter-spacing: .08em;
      }
      .req-skel--val { height: 24px; width: 80px; border-radius: var(--radius-sm); }

      /* ── Siatki ── */
      .req-charts-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(420px, 1fr));
        gap: 16px; margin-bottom: 20px;
      }
      .req-charts-grid--full { grid-template-columns: 1fr; }

      /* ── Karta wykresu ── */
      .req-chart-card {
        background: var(--bg-1); border: 1px solid var(--border-1);
        border-radius: var(--radius-lg); padding: 20px;
        display: flex; flex-direction: column; gap: 14px;
      }
      .req-chart-head { display: flex; flex-direction: column; gap: 3px; }
      .req-chart-title {
        font-family: var(--font-display); font-weight: 700;
        font-size: 0.95rem; color: var(--text-0);
      }
      .req-chart-sub { font-size: 0.74rem; color: var(--text-2); }

      /* ── Sekcja nagłówek ── */
      .req-section-head { margin: 8px 0 14px; }
      .req-section-title {
        font-family: var(--font-display); font-weight: 700;
        font-size: 1rem; color: var(--text-0); margin-bottom: 4px;
      }
      .req-section-sub { font-size: 0.76rem; color: var(--text-2); }

      /* ── Tooltip ── */
      .req-tooltip {
        background: var(--bg-3); border: 1px solid var(--border-1);
        border-radius: var(--radius-md); padding: 8px 12px;
        font-family: var(--font-mono);
      }
      .req-tooltip-label { font-size: 0.72rem; color: var(--text-2); margin-bottom: 3px; }
      .req-tooltip-val   { font-size: 0.84rem; font-weight: 700; color: var(--accent); }

      /* ── Skeleton ── */
      .req-skel {
        border-radius: var(--radius-md);
        background: linear-gradient(90deg, var(--bg-2) 25%, var(--bg-3) 50%, var(--bg-2) 75%);
        background-size: 200% 100%;
        animation: shimmer 1.5s ease infinite;
      }
    `}</style>
  )
}
