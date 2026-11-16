// ╔══════════════════════════════════════════════════════════╗
// ║  ŚCIEŻKA:  src/hooks/index.js                          ║
// ║  AKCJA:    NADPISZ — POPRAWKA useApi                   ║
// ║  FIX:      immediate+deps teraz poprawnie re-triggeruje ║
// ╚══════════════════════════════════════════════════════════╝

import { useState, useEffect, useCallback, useRef } from 'react'
import { useSearchParams } from 'react-router-dom'
import { PORTALS } from '@/utils'

// ── useApi ────────────────────────────────────────────────────────
//
// POPRAWKA:
//   Stary kod: useEffect(() => execute(), [immediate])
//   Problem:   `immediate` to boolean — nigdy się nie zmienia,
//              więc execute() nie re-triggerowało się gdy zmieniły
//              się deps (np. id w OfferDetailPage, userId w MyOffersPage)
//
//   Nowy kod:  useEffect(() => execute(), [execute])
//              execute jest memoizowany przez `deps` (useCallback),
//              więc zmiana deps → nowe execute → re-fetch ✓
//
export function useApi(apiFn, { immediate = false, deps = [] } = {}) {
  const [data,    setData]    = useState(null)
  const [loading, setLoading] = useState(immediate)
  const [error,   setError]   = useState(null)
  const mountedRef = useRef(true)

  useEffect(() => {
    mountedRef.current = true
    return () => { mountedRef.current = false }
  }, [])

  const execute = useCallback(async (...args) => {
    setLoading(true)
    setError(null)
    try {
      const res = await apiFn(...args)
      if (mountedRef.current) setData(res.data)
      return res.data
    } catch (err) {
      if (mountedRef.current) setError(err)
      throw err
    } finally {
      if (mountedRef.current) setLoading(false)
    }
  }, deps) // eslint-disable-line react-hooks/exhaustive-deps

  // ← POPRAWKA: reaguj na zmianę execute (= zmianę deps), nie tylko immediate
  useEffect(() => {
    if (immediate) execute()
  }, [execute]) // eslint-disable-line react-hooks/exhaustive-deps

  const reset = useCallback(() => {
    setData(null); setError(null); setLoading(false)
  }, [])

  return { data, loading, error, execute, reset }
}

// ── usePortal ─────────────────────────────────────────────────────
// Wybrany portal (No Fluff Jobs / theprotocol.it / Just Join IT) trzymany w ?portal=… — przeżywa
// odświeżenie i jest wspólny dla stron; ostatni wybór pamiętany lokalnie.
// extra — dodatkowe zakładki tylko tej strony (np. "Wszystkie strony"); nie są zapamiętywane dla innych stron.
const PORTAL_KEY = 'ob-portal'
const NO_EXTRA = []

export function usePortal(extra = NO_EXTRA) {
  const [params, setParams] = useSearchParams()
  const fromUrl = [...extra, ...PORTALS].find(p => p.slug === params.get('portal'))
  let stored = null
  try { stored = PORTALS.find(p => p.id === localStorage.getItem(PORTAL_KEY)) } catch { /* brak dostępu */ }
  const portal = (fromUrl ?? stored ?? PORTALS[0]).id

  const setPortal = useCallback((id) => {
    const p = [...extra, ...PORTALS].find(x => x.id === id) ?? PORTALS[0]
    if (PORTALS.includes(p)) {
      try { localStorage.setItem(PORTAL_KEY, p.id) } catch { /* brak dostępu */ }
    }
    setParams(prev => {
      const next = new URLSearchParams(prev)
      next.set('portal', p.slug)
      return next
    }, { replace: true })
  }, [setParams, extra])

  return [portal, setPortal]
}

// ── useDebounce ───────────────────────────────────────────────────
export function useDebounce(value, delay = 300) {
  const [debounced, setDebounced] = useState(value)
  useEffect(() => {
    const t = setTimeout(() => setDebounced(value), delay)
    return () => clearTimeout(t)
  }, [value, delay])
  return debounced
}

// ── useTitle ──────────────────────────────────────────────────────
export function useTitle(title) {
  useEffect(() => {
    const prev = document.title
    document.title = title ? `${title} — OfferBrowser` : 'OfferBrowser'
    return () => { document.title = prev }
  }, [title])
}