import api from './axios'

// ── Auth ──────────────────────────────────────────────────────────
export const authApi = {
  register: (data)  => api.post('/auth/register', data),
  me:       ()      => api.get('/auth/me'),
  login:    (data)  => api.post('/auth/login', data),
  updateProfile:    (data)  => api.put('/auth/update', data),
  changePassword:   (data)  => api.post('/auth/change-password', data),
  confirmRegistration: (token) =>
    api.get('/v1/registration/confirm', { params: { token } }),
}

// ── Offers ────────────────────────────────────────────────────────
export const offersApi = {
  getAll:   (source)         => api.get('/offers', source ? { params: { source } } : {}),
  getById:  (id)             => api.get(`/offers/${id}`),
  create:   (data)           => api.post('/offers', data),
  update:   (id, data)       => api.put(`/offers/${id}`, data),
  delete:   (id)             => api.delete(`/offers/${id}`),
  markDuplicateById: (userId, id) =>
    api.post(`/offers/${id}/mark-duplicate`, null, { headers: { userId } }),
}

// ── User Offers ───────────────────────────────────────────────────
export const userOffersApi = {
  getNotApplied: (userId, source) =>
    api.get('/user-offers/not-applied', { headers: { userId }, params: source ? { source } : {} }),
  getApplied: (userId, source) =>
    api.get('/user-offers/applied', { headers: { userId }, params: source ? { source } : {} }),
  getUseless: (userId, source) =>
    api.get('/user-offers/useless', { headers: { userId }, params: source ? { source } : {} }),
  applyToOffer: (userId, offerId) =>
    api.post(`/user-offers/${offerId}/apply`, null, { headers: { userId } }),
  markUseless: (userId, offerId) =>
    api.post(`/user-offers/${offerId}/useless`, null, { headers: { userId } }),
}

// ── Application Notes ─────────────────────────────────────────────
export const notesApi = {
  getAll: (userId) =>
    api.get('/application-notes', { params: { userId } }),
  getCompaniesWithDates: (userId) =>
    api.get('/application-notes/companies-with-dates', { params: { userId } }),
  create: (userId, offerId, companyName, offerUrl) =>
    api.post('/application-notes', null, { params: { userId, offerId, companyName, ...(offerUrl && { offerUrl }) } }),
  createExternal: (userId, companyName, offerUrl) =>
    api.post('/application-notes/external', null, { params: { userId, companyName, ...(offerUrl && { offerUrl }) } }),
}

// ── Statistics ────────────────────────────────────────────────────
export const statsApi = {
  getTotalOffers:      (source) => api.get('/statistics/total-offers',       source ? { params: { source } } : {}),
  getLevelDistribution:(source) => api.get('/statistics/level-distribution', source ? { params: { source } } : {}),
  getCityDistribution: (source) => api.get('/statistics/city-distribution',  source ? { params: { source } } : {}),
  // Wszystkie strony: poziomy sumowane ze wszystkich portali, z podziałem na portal
  getLevelBySource:    ()       => api.get('/statistics/level-distribution-by-source'),
}

// ── CV użytkownika ────────────────────────────────────────────────
export const cvApi = {
  info:     ()     => api.get('/users/me/cv'),
  download: ()     => api.get('/users/me/cv/file', { responseType: 'blob' }),
  upload:   (file, onProgress) => {
    const form = new FormData()
    form.append('file', file)
    return api.post('/users/me/cv', form, {
      headers: { 'Content-Type': 'multipart/form-data' },
      timeout: 120000,
      onUploadProgress: (e) => onProgress?.(e.total ? Math.round((e.loaded / e.total) * 100) : null),
    })
  },
  remove:   ()     => api.delete('/users/me/cv'),
}

// ── Kopia zapasowa bazy (admin) ───────────────────────────────────
export const backupApi = {
  list:          ()         => api.get('/admin/backups'),
  create:        ()         => api.post('/admin/backups', null, { timeout: 0 }),
  download:      (fileName) => api.get(`/admin/backups/${encodeURIComponent(fileName)}`, { responseType: 'blob', timeout: 0 }),
  remove:        (fileName) => api.delete(`/admin/backups/${encodeURIComponent(fileName)}`),
  restore:       (fileName) => api.post(`/admin/backups/${encodeURIComponent(fileName)}/restore`, null, { timeout: 0 }),
  restoreUpload: (file)     => {
    const form = new FormData()
    form.append('file', file)
    return api.post('/admin/backups/restore-upload', form, { headers: { 'Content-Type': 'multipart/form-data' }, timeout: 0 })
  },
}

// ── Adresy wyszukiwania scraperów (admin, osobna lista dla każdego portalu) ──
export const scraperUrlsApi = {
  list:            (source)        => api.get('/admin/scraper-urls', { params: { source } }),
  defaults:        (source)        => api.get('/admin/scraper-urls/defaults', { params: { source } }),
  add:             (source, data)  => api.post('/admin/scraper-urls', data, { params: { source } }),
  update:          (id, data)      => api.put(`/admin/scraper-urls/${id}`, data),
  remove:          (id)            => api.delete(`/admin/scraper-urls/${id}`),
  restoreDefaults: (source)        => api.post('/admin/scraper-urls/restore-defaults', null, { params: { source } }),
}

// ── Requirements Analytics ────────────────────────────────────────
export const analyticsApi = {
  getTopSkills:        (sources)        => api.get('/analytics/requirements/top',          sources?.length ? { params: { sources: sources.join(',') } } : {}),
  getTopSkillsByLevel: (level, sources) => api.get('/analytics/requirements/top-by-level', { params: { level, ...(sources?.length ? { sources: sources.join(',') } : {}) } }),
  getAvailableLevels:  (sources)        => api.get('/analytics/requirements/levels',       sources?.length ? { params: { sources: sources.join(',') } } : {}),
  getAvailableSources: ()               => api.get('/analytics/requirements/sources'),
}

// ── Import ────────────────────────────────────────────────────────
export const importApi = {
  runScript:       ()     => api.get('/python-script/run', { timeout: 0 }),
  importFromJson:  (data, source) => api.post('/python-script/import', data, { params: { source } }),
  importFromUrl:   (url)  => api.post('/python-script/import-from-url', null, { params: { offerUrl: url }, timeout: 150000 }),
}

// ── Scrapery (per portal) ─────────────────────────────────────────
export const scrapersApi = {
  getAll:     ()       => api.get('/scrapers'),
  get:        (source) => api.get(`/scrapers/${source}`),
  run:        (source) => api.post(`/scrapers/${source}/run`),
}

// ── Admin ─────────────────────────────────────────────────────────
export const adminApi = {
  getUsers:            ()        => api.get('/admin/users'),
  getOfferMarkers:     (offerId) => api.get(`/admin/offers/${offerId}/markers`),
  getUselessOfferIds:  ()        => api.get('/admin/offers/useless-ids'),
  getDuplicateOfferIds:()        => api.get('/admin/offers/duplicate-ids'),
  getFlagDominant:     ()        => api.get('/admin/offers/flag-dominant'),
}

// ── Notifications ─────────────────────────────────────────────────
export const notificationsApi = {
  sendDailyEmails: () => api.get('/notifications/daily-unapplied-offers'),
}