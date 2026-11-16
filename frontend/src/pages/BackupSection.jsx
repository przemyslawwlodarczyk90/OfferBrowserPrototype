import { useCallback, useEffect, useRef, useState } from 'react'
import { backupApi } from '@/api/services'
import { toast } from '@/store'
import { formatBytes, formatDateTime } from '@/utils'

// Panel admina → kopia całej bazy do JSON (katalog backup.dir) i przywracanie z JSON
const KIND_LABELS = { manual: 'ręczna', 'pre-restore': 'przed przywróceniem' }
const kindOf = (b) => b.fileName.startsWith('uploaded_') ? 'wgrana' : (KIND_LABELS[b.kind] ?? b.kind ?? '—')
const CONFIRM_WORD = 'PRZYWRÓĆ'

function saveBlob(blob, fileName) {
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = fileName
  document.body.appendChild(a)
  a.click()
  a.remove()
  setTimeout(() => URL.revokeObjectURL(url), 1000)
}

// Przywrócenie nadpisuje dane — wymaga wpisania słowa potwierdzenia
function RestoreModal({ target, onConfirm, onClose, busy }) {
  const [text, setText] = useState('')
  const ok = text.trim().toUpperCase() === CONFIRM_WORD
  return (
    <div className="bk-backdrop" onClick={() => !busy && onClose()}>
      <div className="bk-modal animate-fade-in" role="dialog" aria-modal="true" onClick={e => e.stopPropagation()}>
        <div className="bk-modal-icon">⚠</div>
        <h3 className="bk-modal-title">Przywrócić bazę danych?</h3>
        <p className="bk-modal-desc">
          Dane wszystkich tabel z kopii <code>{target.label}</code> zastąpią obecne
          (użytkownicy, oferty, statusy, notatki, flagi, adresy scraperów).
          Przed przywróceniem automatycznie zapisze się kopia obecnego stanu — w razie pomyłki możesz do niej wrócić.
        </p>
        <p className="bk-modal-hint">Wpisz <code>{CONFIRM_WORD}</code>, aby kontynuować:</p>
        <input className="bk-input" autoFocus value={text} placeholder={CONFIRM_WORD}
               onChange={e => setText(e.target.value)}
               onKeyDown={e => e.key === 'Enter' && ok && !busy && onConfirm()} />
        <div className="bk-modal-foot">
          <button className="bk-btn bk-btn--ghost" onClick={onClose} disabled={busy}>Anuluj</button>
          <button className="bk-btn bk-btn--danger" onClick={onConfirm} disabled={!ok || busy}>
            {busy ? <><span className="bk-spin" />Przywracanie…</> : '↺ Przywróć bazę'}
          </button>
        </div>
      </div>
    </div>
  )
}

export default function BackupSection() {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [creating, setCreating] = useState(false)
  const [restoreTarget, setRestoreTarget] = useState(null)   // { fileName } | { file }
  const [restoring, setRestoring] = useState(false)
  const [result, setResult] = useState(null)
  const [deleting, setDeleting] = useState(null)
  const input = useRef(null)

  const load = useCallback(async () => {
    try {
      const res = await backupApi.list()
      setData(res.data)
    } catch (err) {
      toast.error(err.message ?? 'Nie udało się wczytać listy kopii')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => { load() }, [load])

  const create = async () => {
    setCreating(true)
    try {
      const res = await backupApi.create()
      toast.success(`Kopia zapisana: ${res.data.fileName} (${res.data.totalRows} wierszy)`)
      load()
    } catch (err) {
      toast.error(err.message ?? 'Nie udało się utworzyć kopii')
    } finally {
      setCreating(false)
    }
  }

  const download = async (fileName) => {
    try {
      const res = await backupApi.download(fileName)
      saveBlob(res.data, fileName)
    } catch {
      toast.error('Nie udało się pobrać kopii')
    }
  }

  const remove = async (fileName) => {
    try {
      await backupApi.remove(fileName)
      setDeleting(null)
      toast.success('Usunięto kopię')
      load()
    } catch (err) {
      toast.error(err.message ?? 'Nie udało się usunąć kopii')
    }
  }

  const pickFile = (file) => {
    if (input.current) input.current.value = ''
    if (!file) return
    if (!file.name.toLowerCase().endsWith('.json')) {
      toast.error('Wybierz plik .json z kopią bazy')
      return
    }
    setRestoreTarget({ file, label: file.name })
  }

  const restore = async () => {
    setRestoring(true)
    try {
      const res = restoreTarget.file
        ? await backupApi.restoreUpload(restoreTarget.file)
        : await backupApi.restore(restoreTarget.fileName)
      setResult(res.data)
      setRestoreTarget(null)
      toast.success(`Przywrócono bazę — ${res.data.totalRows} wierszy`)
      load()
    } catch (err) {
      toast.error(err.message ?? 'Przywracanie nie powiodło się — baza bez zmian')
    } finally {
      setRestoring(false)
    }
  }

  const backups = data?.backups ?? []

  return (
    <section className="bk-wrap">
      <div className="bk-head">
        <div>
          <h2 className="adm-section-title--standalone bk-title">Kopia zapasowa bazy danych</h2>
          <p className="bk-sub">
            Cała baza (wszystkie tabele) w jednym pliku JSON. Katalog: <code>{data?.directory ?? '…'}</code>
            {' '}(<code>backup.dir</code> w properties)
          </p>
        </div>
        <div className="bk-actions">
          <button className="bk-btn bk-btn--ghost" onClick={() => input.current?.click()} disabled={restoring}>
            ⇪ Przywróć z pliku JSON…
          </button>
          <button className="bk-btn bk-btn--primary" onClick={create} disabled={creating}>
            {creating ? <><span className="bk-spin" />Tworzenie…</> : '⤓ Utwórz kopię teraz'}
          </button>
          <input ref={input} type="file" accept=".json,application/json" hidden onChange={e => pickFile(e.target.files?.[0])} />
        </div>
      </div>

      {result && (
        <div className="bk-result animate-fade-in">
          <span className="bk-result-icon">✓</span>
          <div className="bk-result-body">
            <p className="bk-result-title">Baza przywrócona z <code>{result.fileName}</code> — {result.totalRows} wierszy</p>
            <p className="bk-result-meta">
              {Object.entries(result.restoredRows).map(([t, n]) => `${t}: ${n}`).join(' · ')}
            </p>
            <p className="bk-result-meta">
              Kopia stanu sprzed przywrócenia: <code>{result.safetyBackup}</code>
              {result.untouchedTables?.length > 0 && <> · tabele spoza kopii (bez zmian): {result.untouchedTables.join(', ')}</>}
            </p>
          </div>
          <button className="bk-icon-btn" title="Zamknij" onClick={() => setResult(null)}>✕</button>
        </div>
      )}

      <div className="bk-card">
        {loading ? (
          <div className="bk-skel" />
        ) : backups.length === 0 ? (
          <p className="bk-empty">Brak kopii — kliknij „Utwórz kopię teraz”.</p>
        ) : (
          <div className="bk-table-wrap">
            <table className="bk-table">
              <thead>
                <tr>
                  <th>Plik</th>
                  <th>Utworzono</th>
                  <th>Rodzaj</th>
                  <th className="bk-num">Wiersze</th>
                  <th className="bk-num">Rozmiar</th>
                  <th className="bk-right">Akcje</th>
                </tr>
              </thead>
              <tbody>
                {backups.map(b => (
                  <tr key={b.fileName}>
                    <td className="bk-file" title={Object.entries(b.rowCounts).map(([t, n]) => `${t}: ${n}`).join('\n')}>{b.fileName}</td>
                    <td>{formatDateTime(b.createdAt)}{b.createdBy && <span className="bk-by"> · {b.createdBy}</span>}</td>
                    <td><span className={`bk-kind bk-kind--${b.kind === 'pre-restore' ? 'safety' : 'normal'}`}>{kindOf(b)}</span></td>
                    <td className="bk-num">{b.totalRows.toLocaleString('pl-PL')}</td>
                    <td className="bk-num">{formatBytes(b.size)}</td>
                    <td className="bk-right">
                      {deleting === b.fileName ? (
                        <span className="bk-confirm">
                          Usunąć?
                          <button className="bk-btn bk-btn--danger bk-btn--sm" onClick={() => remove(b.fileName)}>Tak</button>
                          <button className="bk-btn bk-btn--ghost bk-btn--sm" onClick={() => setDeleting(null)}>Nie</button>
                        </span>
                      ) : (
                        <span className="bk-row-actions">
                          <button className="bk-btn bk-btn--ghost bk-btn--sm" onClick={() => setRestoreTarget({ fileName: b.fileName, label: b.fileName })}>↺ Przywróć</button>
                          <button className="bk-icon-btn" title="Pobierz plik" onClick={() => download(b.fileName)}>↓</button>
                          <button className="bk-icon-btn bk-icon-btn--danger" title="Usuń kopię" onClick={() => setDeleting(b.fileName)}>✕</button>
                        </span>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {restoreTarget && (
        <RestoreModal target={restoreTarget} busy={restoring} onConfirm={restore} onClose={() => setRestoreTarget(null)} />
      )}

      <style>{`
        .bk-wrap { display: flex; flex-direction: column; gap: 12px; margin-bottom: 24px; }
        .bk-head { display: flex; justify-content: space-between; align-items: flex-end; gap: 12px; flex-wrap: wrap; }
        .bk-title { margin-bottom: 2px !important; }
        .bk-sub { font-size: 0.78rem; color: var(--text-2); }
        .bk-sub code, .bk-result code, .bk-modal code {
          font-family: var(--font-mono); font-size: 0.72rem; color: var(--text-1);
          background: var(--bg-3); padding: 1px 5px; border-radius: var(--radius-sm); overflow-wrap: anywhere;
        }
        .bk-actions { display: flex; gap: 8px; flex-wrap: wrap; }

        .bk-btn {
          display: inline-flex; align-items: center; gap: 7px; white-space: nowrap;
          padding: 9px 16px; border-radius: var(--radius-md); border: 1px solid transparent;
          font-family: var(--font-mono); font-size: 0.76rem; font-weight: 700; cursor: pointer;
          transition: background .15s, border-color .15s, box-shadow .15s, opacity .15s;
        }
        .bk-btn:disabled { opacity: .5; cursor: not-allowed; }
        .bk-btn--sm { padding: 5px 10px; font-size: 0.7rem; }
        .bk-btn--primary { background: var(--accent); color: #fff; border-color: var(--accent); }
        .bk-btn--primary:hover:not(:disabled) { background: var(--accent-dim); box-shadow: var(--shadow-accent); }
        .bk-btn--ghost { background: var(--bg-2); color: var(--text-1); border-color: var(--border-1); }
        .bk-btn--ghost:hover:not(:disabled) { color: var(--text-0); border-color: var(--border-0); }
        .bk-btn--danger { background: rgba(244,63,94,.12); color: var(--red); border-color: rgba(244,63,94,.4); }
        .bk-btn--danger:hover:not(:disabled) { background: rgba(244,63,94,.2); }
        .bk-spin { width: 11px; height: 11px; border: 2px solid transparent; border-top-color: currentColor; border-radius: 50%; animation: spin .6s linear infinite; }
        .bk-icon-btn {
          width: 28px; height: 28px; border-radius: var(--radius-md); cursor: pointer;
          display: inline-flex; align-items: center; justify-content: center;
          background: var(--bg-2); border: 1px solid var(--border-1); color: var(--text-1); font-size: 0.8rem;
        }
        .bk-icon-btn:hover { color: var(--text-0); border-color: var(--border-0); }
        .bk-icon-btn--danger:hover { color: var(--red); border-color: rgba(244,63,94,.45); }

        .bk-card { background: var(--bg-1); border: 1px solid var(--border-1); border-radius: var(--radius-lg); overflow: hidden; }
        .bk-empty { padding: 28px; text-align: center; color: var(--text-2); font-size: 0.82rem; }
        .bk-skel {
          height: 90px; margin: 14px; border-radius: var(--radius-md);
          background: linear-gradient(90deg, var(--bg-2) 25%, var(--bg-3) 50%, var(--bg-2) 75%);
          background-size: 200% 100%; animation: shimmer 1.5s ease infinite;
        }
        .bk-table-wrap { overflow-x: auto; }
        .bk-table { width: 100%; border-collapse: collapse; font-size: 0.78rem; color: var(--text-1); }
        .bk-table th {
          text-align: left; padding: 10px 14px; background: var(--bg-2); color: var(--text-2);
          font-family: var(--font-mono); font-size: 0.64rem; text-transform: uppercase; letter-spacing: .07em;
          border-bottom: 1px solid var(--border-1); white-space: nowrap;
        }
        .bk-table td { padding: 10px 14px; border-bottom: 1px solid var(--border-2); white-space: nowrap; vertical-align: middle; }
        .bk-table tr:last-child td { border-bottom: none; }
        .bk-table tbody tr:hover { background: var(--bg-2); }
        .bk-file { font-family: var(--font-mono); font-size: 0.72rem; color: var(--text-0); }
        .bk-by { color: var(--text-3); }
        .bk-num { text-align: right; font-family: var(--font-mono); }
        .bk-right { text-align: right; }
        .bk-row-actions, .bk-confirm { display: inline-flex; gap: 6px; align-items: center; }
        .bk-confirm { font-family: var(--font-mono); font-size: 0.72rem; color: var(--red); }
        .bk-kind {
          font-family: var(--font-mono); font-size: 0.62rem; font-weight: 700; text-transform: uppercase;
          letter-spacing: .06em; padding: 2px 8px; border-radius: 100px;
          background: var(--accent-glow); color: var(--accent); border: 1px solid var(--border-0);
        }
        .bk-kind--safety { background: rgba(245,158,11,.1); color: var(--yellow); border-color: rgba(245,158,11,.35); }

        .bk-result {
          display: flex; gap: 12px; align-items: flex-start; padding: 12px 14px; border-radius: var(--radius-lg);
          background: rgba(16,185,129,.07); border: 1px solid rgba(16,185,129,.3);
        }
        .bk-result-icon { color: var(--green); font-weight: 800; }
        .bk-result-body { flex: 1; min-width: 0; display: flex; flex-direction: column; gap: 4px; }
        .bk-result-title { font-size: 0.84rem; font-weight: 700; color: var(--text-0); }
        .bk-result-meta { font-family: var(--font-mono); font-size: 0.68rem; color: var(--text-2); overflow-wrap: anywhere; }

        .bk-backdrop {
          position: fixed; inset: 0; z-index: 300; background: rgba(0,0,0,.65);
          display: flex; align-items: center; justify-content: center; padding: 16px;
        }
        .bk-modal {
          width: min(480px, 100%); background: var(--bg-1); border: 1px solid rgba(244,63,94,.35);
          border-radius: var(--radius-lg); padding: 24px; box-shadow: var(--shadow-lg);
          display: flex; flex-direction: column; gap: 12px;
        }
        .bk-modal-icon { font-size: 1.6rem; color: var(--red); }
        .bk-modal-title { font-size: 1.05rem; font-weight: 800; color: var(--text-0); }
        .bk-modal-desc, .bk-modal-hint { font-size: 0.8rem; color: var(--text-1); line-height: 1.6; }
        .bk-input {
          padding: 9px 12px; border-radius: var(--radius-md); background: var(--bg-2);
          border: 1px solid var(--border-1); color: var(--text-0); font-family: var(--font-mono); outline: none;
        }
        .bk-input:focus { border-color: var(--red); box-shadow: 0 0 0 3px rgba(244,63,94,.15); }
        .bk-modal-foot { display: flex; justify-content: flex-end; gap: 8px; margin-top: 4px; }
      `}</style>
    </section>
  )
}
