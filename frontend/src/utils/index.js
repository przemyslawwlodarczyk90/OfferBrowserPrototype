import { format, parseISO, formatDistanceToNow } from 'date-fns'
import { pl } from 'date-fns/locale'

// ── Daty ──────────────────────────────────────────────────────────
export function formatDate(dateStr) {
  if (!dateStr) return '—'
  try { return format(parseISO(dateStr), 'dd.MM.yyyy', { locale: pl }) }
  catch { return dateStr }
}

export function formatDateTime(dateStr) {
  if (!dateStr) return '—'
  try { return format(parseISO(dateStr), 'dd.MM.yyyy HH:mm', { locale: pl }) }
  catch { return dateStr }
}

export function timeAgo(dateStr) {
  if (!dateStr) return '—'
  try { return formatDistanceToNow(parseISO(dateStr), { addSuffix: true, locale: pl }) }
  catch { return dateStr }
}

// ── Tekst ─────────────────────────────────────────────────────────
export function truncate(str, len = 120) {
  if (!str) return ''
  return str.length <= len ? str : str.slice(0, len) + '…'
}

// ── Poziom ───────────────────────────────────────────────────────
export function normalizeLevel(level) {
  const map = {
    junior: 'Junior', mid: 'Mid', senior: 'Senior',
    expert: 'Expert', trainee: 'Trainee',
  }
  return map[(level || '').toLowerCase()] ?? level ?? '—'
}

// ── Wynagrodzenie ────────────────────────────────────────────────
export function formatSalary(salary) {
  if (!salary || salary === 'Brak wynagrodzenia') return null
  return salary
}

// ── Portale ──────────────────────────────────────────────────────
// id = wartość pola `source` w backendzie; slug = parametr ?portal= w URL
export const PORTALS = [
  { id: 'NoFluff',     slug: 'nofluff',     label: 'No Fluff Jobs',  domain: 'nofluffjobs.com' },
  { id: 'TheProtocol', slug: 'theprotocol', label: 'theprotocol.it', domain: 'theprotocol.it' },
  { id: 'JustJoin',    slug: 'justjoin',    label: 'Just Join IT',   domain: 'justjoin.it' },
]

// Zakładka "Wszystkie strony" (tylko statystyki) — dane sumowane ze wszystkich portali
export const ALL_PORTALS = { id: 'ALL', slug: 'all', label: 'Wszystkie strony' }

// Kolor portalu na wykresach (stały, żeby ten sam portal miał ten sam kolor wszędzie)
export const PORTAL_COLORS = {
  NoFluff:     '#6366f1',
  TheProtocol: '#38bdf8',
  JustJoin:    '#f59e0b',
}

// Rozmiar pliku: 1536 → "1,5 KB"
export function formatBytes(bytes) {
  if (bytes == null) return '—'
  if (bytes < 1024) return `${bytes} B`
  const units = ['KB', 'MB', 'GB']
  let v = bytes / 1024, i = 0
  while (v >= 1024 && i < units.length - 1) { v /= 1024; i++ }
  return `${v.toLocaleString('pl-PL', { maximumFractionDigits: 1 })} ${units[i]}`
}
export const DEFAULT_PORTAL = PORTALS[0].id

export function portalById(id) {
  return PORTALS.find(p => p.id === id) ?? PORTALS[0]
}

// Czy URL należy do domeny portalu (bez podszywania się: theprotocol.it.evil.com ≠ theprotocol.it)
export function urlMatchesPortal(url, portalId) {
  try {
    const host = new URL(url).hostname.toLowerCase()
    const d = portalById(portalId).domain
    return host === d || host.endsWith('.' + d)
  } catch {
    return false
  }
}

// ── Sortowanie ───────────────────────────────────────────────────
// key  — nazwa pola daty, np. 'createdAt'
// dir  — 'desc' (najnowsze) | 'asc' (najstarsze)
export function sortByDate(list, key = 'createdAt', dir = 'desc') {
  return [...list].sort((a, b) => {
    const da = a[key] ? new Date(a[key]) : 0
    const db = b[key] ? new Date(b[key]) : 0
    return dir === 'desc' ? db - da : da - db
  })
}






