import api from './axios'

// ── Auth ──────────────────────────────────────────────────────────
export const authApi = {
  register: (data)  => api.post('/auth/register', data),
  login:    (data)  => api.post('/auth/login', data),
  updateProfile:    (data)  => api.put('/auth/update', data),
  changePassword:   (data)  => api.post('/auth/change-password', data),
  confirmRegistration: (token) =>
    api.get('/v1/registration/confirm', { params: { token } }),
}

// ── Offers ────────────────────────────────────────────────────────
export const offersApi = {
  getAll:   ()               => api.get('/offers'),
  getById:  (id)             => api.get(`/offers/${id}`),
  create:   (data)           => api.post('/offers', data),
  update:   (id, data)       => api.put(`/offers/${id}`, data),
  delete:   (id)             => api.delete(`/offers/${id}`),
  markDuplicateById: (userId, id) =>
    api.post(`/offers/${id}/mark-duplicate`, null, { headers: { userId } }),
}

// ── User Offers ───────────────────────────────────────────────────
export const userOffersApi = {
  getNotApplied: (userId) =>
    api.get('/user-offers/not-applied', { headers: { userId } }),
  getApplied: (userId) =>
    api.get('/user-offers/applied', { headers: { userId } }),
  getUseless: (userId) =>
    api.get('/user-offers/useless', { headers: { userId } }),
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
  getTotalOffers:      () => api.get('/statistics/total-offers'),
  getLevelDistribution:() => api.get('/statistics/level-distribution'),
  getCityDistribution: () => api.get('/statistics/city-distribution'),
}

// ── Requirements Analytics ────────────────────────────────────────
export const analyticsApi = {
  getTopSkills:       (limit = 20)               => api.get('/analytics/requirements/top',          { params: { limit } }),
  getTopSkillsByLevel:(level, limit = 15)        => api.get('/analytics/requirements/top-by-level', { params: { level, limit } }),
  getAvailableLevels: ()                         => api.get('/analytics/requirements/levels'),
}

// ── Import ────────────────────────────────────────────────────────
export const importApi = {
  runScript:       ()     => api.get('/python-script/run', { timeout: 0 }),
  importFromJson:  (data) => api.post('/python-script/import', data),
  importFromUrl:   (url)  => api.post('/python-script/import-from-url', null, { params: { offerUrl: url } }),
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