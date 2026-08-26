import './style.css'

type Action = { icon: string; title: string; detail: string; time: string; undo?: boolean }

const history: Action[] = [
  { icon: '✓', title: 'Battery saver turned on', detail: 'System setting changed', time: 'Just now', undo: true },
  { icon: '◷', title: 'Reminder created', detail: 'Call Mom · Today at 7:00 PM', time: '12 min ago', undo: true },
  { icon: '⌁', title: 'Storage scan completed', detail: '1.8 GB available to review', time: 'Yesterday' },
]

document.querySelector<HTMLDivElement>('#app')!.innerHTML = `
  <main class="shell">
    <nav class="topbar">
      <div class="brand"><span class="brand-mark">F</span><span>FRIDAY</span><span class="beta">VOICE OS / 01</span></div>
      <div class="system-status"><span class="status-dot"></span> SYSTEM ONLINE <button class="icon-button" aria-label="Open settings">•••</button></div>
    </nav>

    <section class="hero">
      <div class="hud-readout readout-left"><span>CPU TEMP</span><b>36.4 C</b></div>
      <div class="hud-readout readout-right"><span>SECURITY</span><b>VERIFIED</b></div>
      <div class="eyebrow"><span class="status-dot"></span> MIC ACTIVE / LISTENING FOR “HEY FRIDAY”</div>
      <div class="core"><span class="core-ring ring-one"></span><span class="core-ring ring-two"></span><span class="core-light"></span><span class="core-scan"></span></div>
      <h1>At your<br><em>service.</em></h1>
      <p class="hero-copy">Voice control for the things your phone can do. Say the word and I’ll handle the rest.</p>
      <p class="transcript" id="transcript">Say a command. Friday is listening.</p>
    </section>

    <section class="command-panel">
      <div class="section-heading"><div><span class="kicker">QUICK COMMANDS</span><h2>What can I do?</h2></div><span class="shortcut">WAKE WORD <b>⌘</b></span></div>
      <div class="command-grid">
        <button class="command" data-command="Find junk files"><span class="command-icon coral">⌁</span><span><strong>Find junk files</strong><small>Scan your storage</small></span><span class="arrow">↗</span></button>
        <button class="command" data-command="Remind me to call Mom"><span class="command-icon yellow">◷</span><span><strong>Remind me to call Mom</strong><small>Create a reminder</small></span><span class="arrow">↗</span></button>
        <button class="command" data-command="Turn on battery saver"><span class="command-icon mint">ϟ</span><span><strong>Turn on battery saver</strong><small>Change a setting</small></span><span class="arrow">↗</span></button>
      </div>
    </section>

    <section class="activity"><div class="section-heading"><div><span class="kicker">AUDIT TRAIL</span><h2>Recent activity</h2></div><button class="text-button">View all <span>→</span></button></div><div id="history-list"></div></section>
    <section class="access-panel"><div class="section-heading"><div><span class="kicker">ACCESS CONTROL</span><h2>Phone permissions</h2></div><span class="access-note">USER CONTROLLED</span></div><p class="access-copy">Friday requests only what a command needs. Every access can be revoked from Android settings.</p><div class="permission-list" id="permission-list"></div></section>
    <footer><span>FRIDAY INTELLIGENCE SYSTEM</span><span class="footer-line"></span><span>LOCAL VOICE PROCESSING</span><span class="footer-spacer"></span><span>LATENCY 12 MS</span></footer>
  </main>
  <div class="toast" id="toast" role="status"></div>
`

const historyList = document.querySelector<HTMLDivElement>('#history-list')!
const transcript = document.querySelector<HTMLParagraphElement>('#transcript')!
const toast = document.querySelector<HTMLDivElement>('#toast')!
const permissionList = document.querySelector<HTMLDivElement>('#permission-list')!

const permissions = [
  { name: 'Microphone', purpose: 'Hear “Hey Friday” and commands', icon: '◉', enabled: true },
  { name: 'Files & media', purpose: 'Find user-approved removable files', icon: '▣', enabled: false },
  { name: 'Contacts', purpose: 'Identify people in your commands', icon: '◎', enabled: false },
  { name: 'Notifications', purpose: 'Create and deliver reminders', icon: '◇', enabled: false },
  { name: 'System settings', purpose: 'Change approved device settings', icon: 'ϟ', enabled: false },
]

function renderHistory() {
  historyList.innerHTML = history.map((item) => `<article class="history-item"><span class="history-icon">${item.icon}</span><div><strong>${item.title}</strong><small>${item.detail}</small></div><time>${item.time}</time>${item.undo ? '<button class="undo">Undo</button>' : ''}</article>`).join('')
  historyList.querySelectorAll<HTMLButtonElement>('.undo').forEach((button) => button.addEventListener('click', () => showToast('Undo request queued')))
}

function showToast(message: string) {
  toast.textContent = message
  toast.classList.add('show')
  window.setTimeout(() => toast.classList.remove('show'), 2600)
}

function renderPermissions() {
  permissionList.innerHTML = permissions.map((permission, index) => `<div class="permission-row"><span class="permission-icon">${permission.icon}</span><span class="permission-info"><strong>${permission.name}</strong><small>${permission.purpose}</small></span><span class="permission-state ${permission.enabled ? 'granted' : ''}">${permission.enabled ? 'GRANTED' : 'NOT GRANTED'}</span><button class="permission-action" data-permission="${index}">${permission.enabled ? 'REVOKE' : 'GRANT'}</button></div>`).join('')
  permissionList.querySelectorAll<HTMLButtonElement>('.permission-action').forEach((button) => button.addEventListener('click', () => {
    const permission = permissions[Number(button.dataset.permission)]
    permission.enabled = !permission.enabled
    renderPermissions()
    showToast(permission.enabled ? `${permission.name} access granted for this preview` : `${permission.name} access revoked`)
  }))
}

function handleCommand(command: string) {
  transcript.textContent = `“${command}”`
  const normalized = command.toLowerCase()
  if (normalized.includes('junk') || normalized.includes('delete')) {
    showToast('I found 1.8 GB of removable cache. Review before deleting.')
    transcript.textContent = 'I found 1.8 GB of removable cache. Delete it?'
    return
  }
  if (normalized.includes('remind') || normalized.includes('mom')) {
    history.unshift({ icon: '◷', title: 'Reminder created', detail: 'Call Mom · Today at 7:00 PM', time: 'Just now', undo: true })
    renderHistory(); showToast('Reminder created for today at 7:00 PM')
    return
  }
  if (normalized.includes('battery')) {
    history.unshift({ icon: '✓', title: 'Battery saver turned on', detail: 'System setting changed', time: 'Just now', undo: true })
    renderHistory(); showToast('Battery saver is now on')
    return
  }
  transcript.textContent = 'I’m not sure what you mean. Could you be more specific?'
  showToast('I need a little more detail before I act.')
}

renderHistory()
renderPermissions()
document.querySelectorAll<HTMLButtonElement>('.command').forEach((button) => button.addEventListener('click', () => handleCommand(button.dataset.command!)))

type Recognition = {
  continuous: boolean
  interimResults: boolean
  lang: string
  start: () => void
  onresult: ((event: { results: ArrayLike<ArrayLike<{ transcript: string }>> }) => void) | null
  onend: (() => void) | null
  onerror: ((event: { error: string }) => void) | null
}

const recognitionWindow = window as Window & { SpeechRecognition?: new () => Recognition; webkitSpeechRecognition?: new () => Recognition }
const SpeechRecognition = recognitionWindow.SpeechRecognition ?? recognitionWindow.webkitSpeechRecognition

function beginVoiceControl() {
  if (!SpeechRecognition) {
    transcript.textContent = 'Live voice control is unavailable in this browser.'
    showToast('Use Chrome or the Android app for microphone control.')
    return
  }
  const recognition = new SpeechRecognition()
  let permissionDenied = false
  recognition.continuous = true
  recognition.interimResults = false
  recognition.lang = 'en-US'
  recognition.onresult = (event) => {
    const latest = event.results[event.results.length - 1][0].transcript.trim()
    if (latest) handleCommand(latest)
  }
  recognition.onerror = (event) => {
    if (event.error === 'not-allowed' || event.error === 'service-not-allowed') {
      permissionDenied = true
      transcript.textContent = 'Microphone permission is needed for hands-free control.'
    }
  }
  recognition.onend = () => { if (!permissionDenied) window.setTimeout(() => beginVoiceControl(), 350) }
  try { recognition.start() } catch { transcript.textContent = 'Waiting for microphone access…' }
}

beginVoiceControl()
