/**
 * Cliente da API Java — o frontend só renderiza; toda a lógica roda no servidor.
 */
const GameApi = {
  sessionId: null,
  pollInterval: 200,

  async createSession() {
    const res = await fetch('/api/session', { method: 'POST' });
    if (!res.ok) throw new Error('Falha ao criar sessão');
    const data = await res.json();
    this.sessionId = data.sessionId;
    return this.sessionId;
  },

  async poll() {
    const res = await fetch(`/api/session/${this.sessionId}`);
    if (!res.ok) throw new Error('Sessão não encontrada');
    return res.json();
  },

  async sendInput(value) {
    const res = await fetch(`/api/session/${this.sessionId}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ value: String(value) }),
    });
    if (!res.ok) throw new Error('Falha ao enviar entrada');
  },
};
