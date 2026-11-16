import { useCallback, useEffect, useRef, useState } from 'react'
import { cvApi } from '@/api/services'
import { toast } from '@/store'
import { formatBytes, formatDateTime } from '@/utils'

// Sekcja profilu: CV użytkownika (jeden plik; w bazie tylko ścieżka, plik na serwerze)
const ALLOWED = ['pdf', 'doc', 'docx', 'odt', 'rtf']
const DEFAULT_MAX = 10 * 1024 * 1024
const ext = (name) => (name.split('.').pop() || '').toLowerCase()

function validateFile(file, maxBytes) {
  if (!file) return 'Nie wybrano pliku'
  if (!ALLOWED.includes(ext(file.name))) return 'Dozwolone formaty: PDF, DOC, DOCX, ODT, RTF'
  if (file.size === 0) return 'Plik jest pusty'
  if (file.size > maxBytes) return `Plik jest za duży — maksymalnie ${formatBytes(maxBytes)}`
  return null
}

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

export default function CvSection() {
  const [cv, setCv] = useState(null)
  const [maxBytes, setMaxBytes] = useState(DEFAULT_MAX)
  const [loading, setLoading] = useState(true)
  const [progress, setProgress] = useState(null)   // null = nie wysyłamy
  const [dragging, setDragging] = useState(false)
  const [error, setError] = useState(null)
  const [confirming, setConfirming] = useState(false)
  const [busy, setBusy] = useState(false)
  const input = useRef(null)

  const load = useCallback(async () => {
    setLoading(true)
    try {
      const res = await cvApi.info()
      setCv(res.data)
      if (res.data.maxBytes) setMaxBytes(res.data.maxBytes)
    } catch (err) {
      if (err.status === 404) {
        setCv(null)
        if (err.response?.data?.maxBytes) setMaxBytes(err.response.data.maxBytes)
      } else {
        setError(err.message ?? 'Nie udało się pobrać informacji o CV')
      }
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => { load() }, [load])

  const upload = async (file) => {
    const problem = validateFile(file, maxBytes)
    setError(problem)
    if (problem) return
    setProgress(0)
    try {
      const res = await cvApi.upload(file, setProgress)
      setCv(res.data)
      setConfirming(false)
      toast.success(cv ? 'CV zostało zastąpione nowym plikiem' : 'CV zostało zapisane')
    } catch (err) {
      setError(err.message ?? 'Nie udało się wysłać pliku')
    } finally {
      setProgress(null)
      if (input.current) input.current.value = ''
    }
  }

  const onDrop = (e) => {
    e.preventDefault()
    setDragging(false)
    if (progress != null) return
    upload(e.dataTransfer.files?.[0])
  }

  const download = async (preview) => {
    setBusy(true)
    try {
      const res = await cvApi.download()
      const blob = new Blob([res.data], { type: cv?.contentType || res.data.type })
      if (preview && cv?.contentType === 'application/pdf') {
        const url = URL.createObjectURL(blob)
        window.open(url, '_blank', 'noopener')
        setTimeout(() => URL.revokeObjectURL(url), 60_000)
      } else {
        saveBlob(blob, cv?.fileName ?? 'CV')
      }
    } catch (err) {
      toast.error(err.status === 404 ? 'Plik CV nie istnieje na serwerze' : 'Nie udało się pobrać CV')
      if (err.status === 404) load()
    } finally {
      setBusy(false)
    }
  }

  const remove = async () => {
    setBusy(true)
    try {
      await cvApi.remove()
      setCv(null)
      setConfirming(false)
      toast.success('CV zostało usunięte')
    } catch (err) {
      toast.error(err.message ?? 'Nie udało się usunąć CV')
    } finally {
      setBusy(false)
    }
  }

  const uploading = progress != null

  return (
    <section className="pf-section">
      <div className="pf-section-head">
        <span className="pf-section-icon">⎙</span>
        <div>
          <h2 className="pf-section-title">Moje CV</h2>
          <p className="pf-section-sub">Jeden plik PDF, DOC, DOCX, ODT lub RTF (do {formatBytes(maxBytes)}) — nowy plik zastępuje poprzedni</p>
        </div>
      </div>

      <div className="pf-section-body">
        {loading ? (
          <div className="cv-skel" />
        ) : cv && !uploading ? (
          <div className="cv-file animate-fade-in">
            <span className={`cv-file-icon cv-file-icon--${ext(cv.fileName)}`}>{ext(cv.fileName).toUpperCase()}</span>
            <div className="cv-file-info">
              <p className="cv-file-name" title={cv.fileName}>{cv.fileName}</p>
              <p className="cv-file-meta">{formatBytes(cv.size)} · wgrano {formatDateTime(cv.uploadedAt)}</p>
            </div>
            {confirming ? (
              <div className="cv-confirm">
                <span>Usunąć CV?</span>
                <button className="pf-btn pf-btn--danger" onClick={remove} disabled={busy}>Tak, usuń</button>
                <button className="pf-btn pf-btn--ghost" onClick={() => setConfirming(false)}>Nie</button>
              </div>
            ) : (
              <div className="cv-file-actions">
                {cv.contentType === 'application/pdf' && (
                  <button className="pf-btn pf-btn--ghost" onClick={() => download(true)} disabled={busy}>◉ Podgląd</button>
                )}
                <button className="pf-btn pf-btn--ghost" onClick={() => download(false)} disabled={busy}>↓ Pobierz</button>
                <button className="pf-btn pf-btn--ghost" onClick={() => input.current?.click()} disabled={busy}>⇄ Zamień</button>
                <button className="pf-btn pf-btn--danger" onClick={() => setConfirming(true)} disabled={busy}>✕ Usuń</button>
              </div>
            )}
          </div>
        ) : (
          <div
            className={`cv-drop${dragging ? ' cv-drop--over' : ''}${uploading ? ' cv-drop--busy' : ''}`}
            role="button"
            tabIndex={0}
            onClick={() => !uploading && input.current?.click()}
            onKeyDown={e => (e.key === 'Enter' || e.key === ' ') && !uploading && input.current?.click()}
            onDragOver={e => { e.preventDefault(); setDragging(true) }}
            onDragLeave={() => setDragging(false)}
            onDrop={onDrop}
          >
            {uploading ? (
              <>
                <span className="cv-drop-icon">⇡</span>
                <p className="cv-drop-title">Wysyłanie… {progress != null ? `${progress}%` : ''}</p>
                <div className="cv-progress"><div className="cv-progress-fill" style={{ width: `${progress ?? 0}%` }} /></div>
              </>
            ) : (
              <>
                <span className="cv-drop-icon">⎙</span>
                <p className="cv-drop-title">Przeciągnij tutaj plik CV</p>
                <p className="cv-drop-sub">albo <span className="cv-drop-link">wybierz z dysku</span> · PDF, DOC, DOCX, ODT, RTF</p>
              </>
            )}
          </div>
        )}

        <input
          ref={input}
          type="file"
          hidden
          accept=".pdf,.doc,.docx,.odt,.rtf,application/pdf"
          onChange={e => upload(e.target.files?.[0])}
        />

        {error && <p className="pf-err cv-err">✕ {error}</p>}
      </div>

      <style>{`
        .cv-skel {
          height: 76px; border-radius: var(--radius-md);
          background: linear-gradient(90deg, var(--bg-2) 25%, var(--bg-3) 50%, var(--bg-2) 75%);
          background-size: 200% 100%; animation: shimmer 1.5s ease infinite;
        }
        .cv-drop {
          display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 6px;
          padding: 28px 16px; border-radius: var(--radius-lg); cursor: pointer; text-align: center;
          border: 2px dashed var(--border-0); background: var(--bg-2);
          transition: border-color .15s, background .15s, transform .15s;
        }
        .cv-drop:hover, .cv-drop:focus-visible { border-color: var(--accent); background: var(--accent-glow); outline: none; }
        .cv-drop--over { border-color: var(--accent); background: var(--accent-glow-strong); transform: scale(1.01); }
        .cv-drop--busy { cursor: progress; }
        .cv-drop-icon { font-size: 1.8rem; color: var(--accent); line-height: 1; }
        .cv-drop-title { font-weight: 700; color: var(--text-0); font-size: 0.9rem; }
        .cv-drop-sub { font-family: var(--font-mono); font-size: 0.72rem; color: var(--text-2); }
        .cv-drop-link { color: var(--accent); text-decoration: underline; }
        .cv-progress { width: min(320px, 100%); height: 6px; border-radius: 3px; background: var(--bg-4); overflow: hidden; }
        .cv-progress-fill { height: 100%; background: var(--accent); transition: width .2s ease; }

        .cv-file {
          display: flex; align-items: center; gap: 14px; flex-wrap: wrap;
          padding: 14px 16px; border-radius: var(--radius-lg);
          background: var(--bg-2); border: 1px solid var(--border-1);
        }
        .cv-file-icon {
          width: 46px; height: 54px; flex-shrink: 0; border-radius: 6px;
          display: flex; align-items: flex-end; justify-content: center; padding-bottom: 7px;
          font-family: var(--font-mono); font-size: 0.6rem; font-weight: 800; letter-spacing: .04em;
          color: #fff; background: var(--accent);
          clip-path: polygon(0 0, 72% 0, 100% 22%, 100% 100%, 0 100%);
        }
        .cv-file-icon--pdf { background: #e5484d; }
        .cv-file-icon--doc, .cv-file-icon--docx { background: #3b82f6; }
        .cv-file-icon--odt { background: #0ea5e9; }
        .cv-file-info { flex: 1; min-width: 160px; }
        .cv-file-name {
          font-weight: 700; color: var(--text-0); font-size: 0.9rem;
          overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
        }
        .cv-file-meta { font-family: var(--font-mono); font-size: 0.7rem; color: var(--text-2); margin-top: 3px; }
        .cv-file-actions, .cv-confirm { display: flex; gap: 8px; flex-wrap: wrap; align-items: center; }
        .cv-confirm span { font-family: var(--font-mono); font-size: 0.74rem; color: var(--red); }
        .cv-err { margin-top: 10px; display: block; }
      `}</style>
    </section>
  )
}
