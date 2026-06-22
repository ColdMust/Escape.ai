const terminal = document.getElementById('terminal');
const actionPanel = document.getElementById('action-panel');
const actionPrompt = document.getElementById('action-prompt');
const actionButtons = document.getElementById('action-buttons');
const actionInputForm = document.getElementById('action-input-form');
const actionInput = document.getElementById('action-input');
const hud = document.getElementById('hud');
const btnScrollBottom = document.getElementById('btn-scroll-bottom');
const appShell = document.getElementById('app') || document.querySelector('.app');
const panelHp = document.getElementById('panel-hp');
const hpOperadorFill = document.getElementById('hp-operador-fill');
const hpOperadorText = document.getElementById('hp-operador-text');
const hpBarOperador = document.getElementById('hp-bar-operador');
const hpEnemies = document.getElementById('hp-enemies');
const hpEnemiesSection = document.getElementById('hp-enemies-section');
const hackOverlay = document.getElementById('hack-overlay');
const hackTimer = document.getElementById('hack-timer');
const hackHacker = document.getElementById('hack-hacker');
const hackTitle = document.getElementById('hack-title');
const hackLore = document.getElementById('hack-lore');
const hackCode = document.getElementById('hack-code');
const hackQuestion = document.getElementById('hack-question');
const hackOptions = document.getElementById('hack-options');
const hackActionPrompt = document.getElementById('hack-action-prompt');
const hackActionButtons = document.getElementById('hack-action-buttons');
const hackActionForm = document.getElementById('hack-action-form');
const hackActionInput = document.getElementById('hack-action-input');

let battleEntered = false;
let hpAnimated = false;
let resolvePending = null;
let hackCountdownInterval = null;
let activeHackDeadline = null;

function stopHackCountdown() {
  if (hackCountdownInterval) {
    clearInterval(hackCountdownInterval);
    hackCountdownInterval = null;
  }
  activeHackDeadline = null;
}

function setHackTimerDisplay(secondsLeft) {
  if (!hackTimer) return;
  hackTimer.textContent = String(secondsLeft);
  hackTimer.classList.toggle('urgent', secondsLeft <= 10);
}

function startHackCountdown(deadlineMs) {
  if (activeHackDeadline === deadlineMs && hackCountdownInterval) return;
  stopHackCountdown();
  activeHackDeadline = deadlineMs;

  const tick = () => {
    const left = Math.max(0, Math.ceil((deadlineMs - Date.now()) / 1000));
    setHackTimerDisplay(left);
    if (left <= 0) stopHackCountdown();
  };

  tick();
  hackCountdownInterval = setInterval(tick, 200);
}

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

function clearHackActions() {
  if (hackActionButtons) hackActionButtons.innerHTML = '';
  if (hackActionPrompt) hackActionPrompt.textContent = '';
  if (hackActionForm) hackActionForm.classList.add('hidden');
  if (hackActionInput) hackActionInput.value = '';
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

function waitForHackUser(setupFn) {
  return new Promise((resolve) => {
    clearHackActions();
    resolvePending = resolve;
    setupFn();
    hackActionInput?.focus();
  });
}

function finish(value) {
  if (!resolvePending) return;
  const resolve = resolvePending;
  resolvePending = null;
  clearActions();
  clearHackActions();
  resolve(value);
}

function shake(type = 'deal') {
  if (!appShell) return;
  const cls = type === 'hit' ? 'shake-hit' : 'shake-deal';
  appShell.classList.remove('shake-hit', 'shake-deal');
  void appShell.offsetWidth;
  appShell.classList.add(cls);
  appShell.addEventListener('animationend', () => {
    appShell.classList.remove(cls);
  }, { once: true });
}

function pct(hp, max) {
  if (!max) return 0;
  return Math.max(0, Math.min(100, (hp / max) * 100));
}

function setBarFill(el, hp, max, animateFromZero) {
  const target = pct(hp, max);
  if (animateFromZero) {
    el.style.width = '0%';
    requestAnimationFrame(() => {
      requestAnimationFrame(() => { el.style.width = target + '%'; });
    });
  } else {
    el.style.width = target + '%';
  }
}

function updateEnemyBars(enemies, animate) {
  if (!hpEnemies) return;

  const list = enemies || [];
  if (hpEnemiesSection) {
    hpEnemiesSection.classList.toggle('hidden', list.length === 0);
  }

  const existing = new Map(
    [...hpEnemies.querySelectorAll('[data-enemy-key]')].map(el => [el.dataset.enemyKey, el])
  );
  const activeKeys = new Set();

  list.forEach((e, index) => {
    const key = `${e.name}-${index}`;
    activeKeys.add(key);

    let wrap = existing.get(key);
    if (!wrap) {
      wrap = document.createElement('div');
      wrap.className = 'hp-bar-group' + (animate ? ' animate-in' : '');
      wrap.dataset.enemyKey = key;
      wrap.innerHTML = `
        <div class="hp-label">
          <span class="hp-enemy-name">${e.name}</span>
          <span class="hp-values hp-enemy-values">—/—</span>
        </div>
        <div class="hp-track"><div class="hp-fill hp-fill-enemy"></div></div>
      `;
      hpEnemies.appendChild(wrap);
    }

    const textEl = wrap.querySelector('.hp-enemy-values');
    const fillEl = wrap.querySelector('.hp-fill-enemy');
    if (textEl) textEl.textContent = `${e.hp}/${e.maxHp}`;
    if (fillEl) setBarFill(fillEl, e.hp, e.maxHp, animate && !existing.has(key));
  });

  for (const [key, el] of existing) {
    if (!activeKeys.has(key)) el.remove();
  }
}

function updateBattleUi(ui) {
  if (!ui) return;

  if (ui.battleActive || ui.mode === 'battle' || ui.mode === 'hack') {
    if (!battleEntered) {
      battleEntered = true;
      appShell?.classList.add('battle-mode');
      panelHp?.classList.remove('hidden');
      panelHp?.classList.add('visible');
      hpAnimated = false;
    }

    const animate = !hpAnimated;
    if (animate) {
      hpAnimated = true;
      hpBarOperador?.classList.remove('animate-in');
      void hpBarOperador?.offsetWidth;
      hpBarOperador?.classList.add('animate-in');
    }

    if (hpOperadorText) hpOperadorText.textContent = `${ui.playerHp}/${ui.playerMax}`;
    setBarFill(hpOperadorFill, ui.playerHp, ui.playerMax, animate);
    updateEnemyBars(ui.enemies, animate);
  } else if (battleEntered) {
    battleEntered = false;
    hpAnimated = false;
    appShell?.classList.remove('battle-mode');
    panelHp?.classList.remove('visible');
    panelHp?.classList.add('hidden');
    if (hpEnemies) hpEnemies.innerHTML = '';
    if (hpEnemiesSection) hpEnemiesSection.classList.add('hidden');
  }
}

function updateHackUi(ui) {
  if (!ui || !ui.hackActive) {
    stopHackCountdown();
    document.body.classList.remove('hack-mode');
    hackOverlay?.classList.add('hidden');
    clearHackActions();
    return;
  }

  document.body.classList.add('hack-mode');
  hackOverlay?.classList.remove('hidden');

  if (ui.hackDeadlineMs) {
    startHackCountdown(ui.hackDeadlineMs);
  } else if (ui.hackTimeLeft != null) {
    startHackCountdown(Date.now() + ui.hackTimeLeft * 1000);
  }

  if (hackHacker) hackHacker.textContent = '💀 ' + ui.hackHackerName + ' invadiu sua consciência';
  if (hackTitle) hackTitle.textContent = ui.hackTitle;
  if (hackLore) hackLore.textContent = ui.hackLore;
  if (hackCode) hackCode.textContent = ui.hackBody;
  if (hackQuestion) hackQuestion.textContent = ui.hackPergunta;
  if (hackOptions) hackOptions.textContent = ui.hackOpcoes;
}

const UI = {
  log,
  logAscii,
  logBlock,
  clear: () => { terminal.innerHTML = ''; },
  setHud,
  scrollToBottom,
  shake,
  updateBattleUi,
  updateHackUi,

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

  async hackChoose(prompt, options) {
    return waitForHackUser(() => {
      if (hackActionPrompt) hackActionPrompt.textContent = prompt;
      for (const opt of options) {
        const btn = document.createElement('button');
        btn.type = 'button';
        btn.textContent = opt.label;
        btn.onclick = () => finish(opt.value);
        hackActionButtons?.appendChild(btn);
      }
    });
  },

  async hackInput(prompt, placeholder = '') {
    return waitForHackUser(() => {
      if (hackActionPrompt) hackActionPrompt.textContent = prompt;
      if (hackActionForm) hackActionForm.classList.remove('hidden');
      if (hackActionInput) {
        hackActionInput.placeholder = placeholder;
        hackActionInput.focus();
        hackActionForm.onsubmit = (e) => {
          e.preventDefault();
          finish(hackActionInput.value);
        };
      }
    });
  },

  async confirm(prompt) {
    return this.choose(prompt, [
      { label: 'Sim', value: true },
      { label: 'Não', value: false },
    ]);
  },
};
