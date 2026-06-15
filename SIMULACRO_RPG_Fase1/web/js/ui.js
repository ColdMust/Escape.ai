const terminal = document.getElementById('terminal');
const actionPanel = document.getElementById('action-panel');
const actionPrompt = document.getElementById('action-prompt');
const actionButtons = document.getElementById('action-buttons');
const actionInputForm = document.getElementById('action-input-form');
const actionInput = document.getElementById('action-input');
const hud = document.getElementById('hud');
const btnScrollBottom = document.getElementById('btn-scroll-bottom');

let resolvePending = null;

terminal.addEventListener('wheel', (e) => {
  e.stopPropagation();
}, { passive: true });

if (btnScrollBottom) {
  btnScrollBottom.addEventListener('click', () => {
    terminal.scrollTop = terminal.scrollHeight;
  });
}

function scrollToBottom() {
  terminal.scrollTop = terminal.scrollHeight;
}

function clearActions() {
  actionButtons.innerHTML = '';
  actionPrompt.textContent = '';
  actionInputForm.classList.add('hidden');
  actionInput.value = '';
}

function setHud(text) {
  hud.textContent = text;
}

function log(text = '', className = '') {
  const lines = String(text).split('\n');
  for (const line of lines) {
    const el = document.createElement('div');
    el.className = 'line' + (className ? ` ${className}` : '');
    el.textContent = line;
    terminal.appendChild(el);
  }
  scrollToBottom();
}

function logAscii(text, className = 'ascii') {
  const el = document.createElement('div');
  el.className = 'line ' + className;
  el.textContent = text.trim();
  terminal.appendChild(el);
  scrollToBottom();
}

function logBlock(text, className = '') {
  log(text, className);
}

function waitForUser(setupFn) {
  return new Promise((resolve) => {
    clearActions();
    resolvePending = resolve;
    setupFn();
    scrollToBottom();
  });
}

function finish(value) {
  if (!resolvePending) return;
  const resolve = resolvePending;
  resolvePending = null;
  clearActions();
  resolve(value);
}

const UI = {
  log,
  logAscii,
  logBlock,
  clear: () => { terminal.innerHTML = ''; },
  setHud,
  scrollToBottom,

  async pause(message = '') {
    if (message) log(message, 'dim');
    return waitForUser(() => {
      actionPrompt.textContent = 'Pressione para continuar';
      const btn = document.createElement('button');
      btn.textContent = 'Continuar ▶';
      btn.onclick = () => finish(true);
      actionButtons.appendChild(btn);
    });
  },

  async choose(prompt, options) {
    return waitForUser(() => {
      actionPrompt.textContent = prompt;
      for (const opt of options) {
        const btn = document.createElement('button');
        btn.textContent = opt.label;
        btn.onclick = () => finish(opt.value);
        actionButtons.appendChild(btn);
      }
    });
  },

  async input(prompt, placeholder = '') {
    return waitForUser(() => {
      actionPrompt.textContent = prompt;
      actionInputForm.classList.remove('hidden');
      actionInput.placeholder = placeholder;
      actionInput.focus();

      actionInputForm.onsubmit = (e) => {
        e.preventDefault();
        finish(actionInput.value);
      };
    });
  },

  async confirm(prompt) {
    return this.choose(prompt, [
      { label: 'Sim', value: true },
      { label: 'Não', value: false },
    ]);
  },
};
