import './style.css'

interface Action {
  icon: string
  title: string
  detail: string
  time: string
  undoAction?: () => void
}

interface AlarmItem {
  id: string
  time: string
  label: string
  enabled: boolean
}

interface ContactItem {
  id: string
  name: string
  number: string
  relation: string
  avatar: string
}

interface RecentCall {
  id: string
  name: string
  number: string
  type: 'incoming' | 'outgoing' | 'missed'
  time: string
  duration: string
}

interface StorageFile {
  id: string
  name: string
  category: 'cache' | 'downloads' | 'logs' | 'media'
  sizeMB: number
  date: string
  selected: boolean
}

interface WeatherData {
  city: string
  country?: string
  temp: number
  apparentTemp?: number
  condition: string
  humidity?: number
  windSpeed?: number
  precipitation?: number
  weatherCode?: number
  isDay?: boolean
}

// System state
const systemState = {
  cpuTemp: '36.4 C',
  securityVerified: true,
  flashlightOn: false,
  batterySaver: false,
  wifiConnected: true,
  bluetoothEnabled: true,
  dndEnabled: false,
  volumeLevel: 75,
  isSpeechMuted: false,
  media: {
    isPlaying: false,
    trackIndex: 0,
    tracks: [
      { title: 'Midnight Protocol', artist: 'Cyberwave FM', duration: '3:45' },
      { title: 'Neural Uplink', artist: 'Synthetic Dawn', duration: '4:12' },
      { title: 'Starlight Drift', artist: 'Solar Orbit', duration: '3:28' },
    ],
  },
  timer: {
    totalSeconds: 0,
    remainingSeconds: 0,
    isRunning: false,
    intervalId: null as number | null,
    label: 'Focus Timer',
  },
  alarms: [
    { id: '1', time: '07:00 AM', label: 'Morning Wakeup', enabled: true },
    { id: '2', time: '08:30 PM', label: 'Evening Winddown', enabled: false },
  ] as AlarmItem[],
  call: {
    isActive: false,
    contactName: 'Mom',
    phoneNumber: '+1 (555) 019-2834',
    status: 'calling' as 'calling' | 'ringing' | 'connected' | 'ended',
    durationSeconds: 0,
    isMuted: false,
    isSpeaker: false,
    timerId: null as number | null,
  },
  contacts: [
    { id: '1', name: 'Mom', number: '+1 (555) 019-2834', relation: 'Family', avatar: 'M' },
    { id: '2', name: 'Alice Chen', number: '+1 (555) 014-9821', relation: 'Lead Engineer', avatar: 'A' },
    { id: '3', name: 'Dr. Patel', number: '+1 (555) 018-7742', relation: 'Medical Clinic', avatar: 'D' },
    { id: '4', name: 'Tech Support', number: '+1 (800) 555-0199', relation: 'Systems Help', avatar: 'T' },
    { id: '5', name: 'Sarah Miller', number: '+1 (555) 016-3390', relation: 'Collaborator', avatar: 'S' },
  ] as ContactItem[],
  recentCalls: [
    { id: 'r1', name: 'Mom', number: '+1 (555) 019-2834', type: 'incoming', time: '10:14 AM', duration: '2m 18s' },
    { id: 'r2', name: 'Alice Chen', number: '+1 (555) 014-9821', type: 'outgoing', time: 'Yesterday', duration: '5m 41s' },
  ] as RecentCall[],
  activePhoneTab: 'contacts' as 'contacts' | 'dialpad' | 'recents',
  dialpadNumber: '',
  storage: {
    totalGB: 128,
    usedGB: 48.2,
    activeFilter: 'all' as 'all' | 'cache' | 'downloads' | 'logs' | 'media',
    files: [
      { id: 'f1', name: 'cache_thumbnails_bundle.tmp', category: 'cache', sizeMB: 420, date: 'Today', selected: false },
      { id: 'f2', name: 'app_update_installer_v4.apk', category: 'downloads', sizeMB: 112, date: 'Sep 10', selected: false },
      { id: 'f3', name: 'sys_crash_diagnostics_2026.log', category: 'logs', sizeMB: 88, date: 'Sep 08', selected: false },
      { id: 'f4', name: 'duplicate_burst_IMG_4091.jpg', category: 'media', sizeMB: 24, date: 'Sep 04', selected: false },
      { id: 'f5', name: 'voice_memo_draft_01.wav', category: 'media', sizeMB: 65, date: 'Sep 02', selected: false },
      { id: 'f6', name: 'offline_map_tile_cache.bin', category: 'downloads', sizeMB: 640, date: 'Aug 29', selected: false },
      { id: 'f7', name: 'chrome_temp_render_cache.tmp', category: 'cache', sizeMB: 185, date: 'Today', selected: false },
    ] as StorageFile[],
  },
  lastWeather: null as WeatherData | null,
}

// History audit trail
const history: Action[] = [
  { icon: '⚡', title: 'System initialized', detail: 'Friday Voice OS online & Gemini AI connected', time: 'Just now' },
  { icon: '✓', title: 'Battery profile optimized', detail: 'Dynamic power management active', time: '5 min ago' },
  { icon: '◷', title: 'Reminder scheduled', detail: 'Call Mom · Today at 7:00 PM', time: '12 min ago' },
]

// Phone permissions
const permissions = [
  { name: 'Microphone', purpose: 'Live voice recognition & wake word detection', icon: '◉', enabled: true },
  { name: 'Audio Output & TTS', purpose: 'Voice synthesizer response speech', icon: '🔊', enabled: true },
  { name: 'Hardware Control', purpose: 'Torch, Wi-Fi, volume & power toggles', icon: '⚡', enabled: true },
  { name: 'Clock & Timers', purpose: 'Schedule alarms and background countdowns', icon: '◷', enabled: true },
  { name: 'Media Playback', purpose: 'Stream and control audio playback', icon: '▶', enabled: true },
  { name: 'Files & Media', purpose: 'Scan cache and removable junk files', icon: '▣', enabled: false },
  { name: 'App Launcher & Intents', purpose: 'Launch WhatsApp, YouTube, Maps & phone applications', icon: '📱', enabled: true },
  { name: 'Camera Viewfinder', purpose: 'Hardware camera sensor and optical capture', icon: '📷', enabled: true },
]

export interface AndroidAppItem {
  id: string
  name: string
  packageName: string
  scheme: string
  webUrl: string
  icon: string
  color: string
  category: 'social' | 'media' | 'tools' | 'utility'
  description: string
}

const androidApps: AndroidAppItem[] = [
  { id: 'whatsapp', name: 'WhatsApp', packageName: 'com.whatsapp', scheme: 'whatsapp://send', webUrl: 'https://web.whatsapp.com', icon: '💬', color: '#25D366', category: 'social', description: 'Chat & voice messaging' },
  { id: 'youtube', name: 'YouTube', packageName: 'com.google.android.youtube', scheme: 'vnd.youtube://', webUrl: 'https://youtube.com', icon: '▶', color: '#FF0000', category: 'media', description: 'Videos & music streams' },
  { id: 'maps', name: 'Google Maps', packageName: 'com.google.android.apps.maps', scheme: 'geo:0,0?q=', webUrl: 'https://maps.google.com', icon: '🗺️', color: '#4285F4', category: 'tools', description: 'GPS Navigation' },
  { id: 'camera', name: 'Camera', packageName: 'com.android.camera', scheme: 'camera://', webUrl: '', icon: '📷', color: '#72e4e7', category: 'tools', description: 'Hardware camera sensor' },
  { id: 'spotify', name: 'Spotify', packageName: 'com.spotify.music', scheme: 'spotify://', webUrl: 'https://open.spotify.com', icon: '🎧', color: '#1DB954', category: 'media', description: 'Stream songs & podcasts' },
  { id: 'messages', name: 'Messages', packageName: 'com.google.android.apps.messaging', scheme: 'sms:', webUrl: '', icon: '✉️', color: '#34A853', category: 'social', description: 'Cellular SMS & texts' },
  { id: 'email', name: 'Gmail', packageName: 'com.google.android.gm', scheme: 'mailto:', webUrl: 'https://mail.google.com', icon: '📬', color: '#EA4335', category: 'social', description: 'Email client' },
  { id: 'phone', name: 'Phone Dialer', packageName: 'com.google.android.dialer', scheme: 'tel:', webUrl: '', icon: '📞', color: '#00C853', category: 'utility', description: 'Native phone dialer' },
  { id: 'chrome', name: 'Chrome', packageName: 'com.android.chrome', scheme: 'https://google.com', webUrl: 'https://google.com', icon: '🌐', color: '#FBBC05', category: 'utility', description: 'Web browsing' },
  { id: 'clock', name: 'Clock & Alarms', packageName: 'com.google.android.deskclock', scheme: 'clock://', webUrl: '', icon: '⏰', color: '#FF9800', category: 'utility', description: 'System alarms' },
]

// Synthesizer Audio Context for Sci-Fi chimes
let audioCtx: AudioContext | null = null
function getAudioContext(): AudioContext {
  if (!audioCtx) {
    const AudioCtx = window.AudioContext || (window as unknown as { webkitAudioContext: typeof AudioContext }).webkitAudioContext
    audioCtx = new AudioCtx()
  }
  if (audioCtx.state === 'suspended') {
    audioCtx.resume()
  }
  return audioCtx
}

function playChime(type: 'listen' | 'confirm' | 'alarm' | 'click') {
  try {
    const ctx = getAudioContext()
    const now = ctx.currentTime

    if (type === 'listen') {
      const osc = ctx.createOscillator()
      const gain = ctx.createGain()
      osc.type = 'sine'
      osc.frequency.setValueAtTime(520, now)
      osc.frequency.exponentialRampToValueAtTime(880, now + 0.12)
      gain.gain.setValueAtTime(0.12, now)
      gain.gain.exponentialRampToValueAtTime(0.001, now + 0.15)
      osc.connect(gain)
      gain.connect(ctx.destination)
      osc.start(now)
      osc.stop(now + 0.15)
    } else if (type === 'confirm') {
      const osc = ctx.createOscillator()
      const gain = ctx.createGain()
      osc.type = 'triangle'
      osc.frequency.setValueAtTime(440, now)
      osc.frequency.setValueAtTime(659.25, now + 0.08)
      gain.gain.setValueAtTime(0.14, now)
      gain.gain.exponentialRampToValueAtTime(0.001, now + 0.22)
      osc.connect(gain)
      gain.connect(ctx.destination)
      osc.start(now)
      osc.stop(now + 0.22)
    } else if (type === 'alarm') {
      for (let i = 0; i < 3; i++) {
        const osc = ctx.createOscillator()
        const gain = ctx.createGain()
        osc.type = 'sine'
        const t = now + i * 0.16
        osc.frequency.setValueAtTime(987.77, t)
        gain.gain.setValueAtTime(0.2, t)
        gain.gain.exponentialRampToValueAtTime(0.001, t + 0.12)
        osc.connect(gain)
        gain.connect(ctx.destination)
        osc.start(t)
        osc.stop(t + 0.12)
      }
    } else {
      const osc = ctx.createOscillator()
      const gain = ctx.createGain()
      osc.type = 'sine'
      osc.frequency.setValueAtTime(700, now)
      gain.gain.setValueAtTime(0.05, now)
      gain.gain.exponentialRampToValueAtTime(0.001, now + 0.04)
      osc.connect(gain)
      gain.connect(ctx.destination)
      osc.start(now)
      osc.stop(now + 0.04)
    }
  } catch {
    // AudioContext blocked before user interaction
  }
}

function playDtmfTone(char: string) {
  try {
    const ctx = getAudioContext()
    const now = ctx.currentTime
    const dtmfFreqs: Record<string, [number, number]> = {
      '1': [697, 1209], '2': [697, 1336], '3': [697, 1477],
      '4': [770, 1209], '5': [770, 1336], '6': [770, 1477],
      '7': [852, 1209], '8': [852, 1336], '9': [852, 1477],
      '*': [941, 1209], '0': [941, 1336], '#': [941, 1477],
    }
    const freqs = dtmfFreqs[char] || [697, 1209]
    const osc1 = ctx.createOscillator()
    const osc2 = ctx.createOscillator()
    const gain = ctx.createGain()

    osc1.type = 'sine'
    osc2.type = 'sine'
    osc1.frequency.setValueAtTime(freqs[0], now)
    osc2.frequency.setValueAtTime(freqs[1], now)

    gain.gain.setValueAtTime(0.1, now)
    gain.gain.exponentialRampToValueAtTime(0.001, now + 0.15)

    osc1.connect(gain)
    osc2.connect(gain)
    gain.connect(ctx.destination)

    osc1.start(now)
    osc2.start(now)
    osc1.stop(now + 0.15)
    osc2.stop(now + 0.15)
  } catch {
    // AudioContext blocked
  }
}

function playCallTone(type: 'ring' | 'dial' | 'hangup') {
  try {
    const ctx = getAudioContext()
    const now = ctx.currentTime
    if (type === 'dial') {
      const osc1 = ctx.createOscillator()
      const osc2 = ctx.createOscillator()
      const gain = ctx.createGain()
      osc1.frequency.setValueAtTime(350, now)
      osc2.frequency.setValueAtTime(440, now)
      gain.gain.setValueAtTime(0.08, now)
      gain.gain.linearRampToValueAtTime(0.001, now + 0.6)
      osc1.connect(gain)
      osc2.connect(gain)
      gain.connect(ctx.destination)
      osc1.start(now)
      osc2.start(now)
      osc1.stop(now + 0.6)
      osc2.stop(now + 0.6)
    } else if (type === 'ring') {
      const osc1 = ctx.createOscillator()
      const osc2 = ctx.createOscillator()
      const gain = ctx.createGain()
      osc1.frequency.setValueAtTime(440, now)
      osc2.frequency.setValueAtTime(480, now)
      gain.gain.setValueAtTime(0.09, now)
      gain.gain.linearRampToValueAtTime(0.001, now + 1.1)
      osc1.connect(gain)
      osc2.connect(gain)
      gain.connect(ctx.destination)
      osc1.start(now)
      osc2.start(now)
      osc1.stop(now + 1.1)
      osc2.stop(now + 1.1)
    } else if (type === 'hangup') {
      for (let i = 0; i < 3; i++) {
        const osc = ctx.createOscillator()
        const gain = ctx.createGain()
        const t = now + i * 0.18
        osc.frequency.setValueAtTime(480, t)
        gain.gain.setValueAtTime(0.12, t)
        gain.gain.exponentialRampToValueAtTime(0.001, t + 0.12)
        osc.connect(gain)
        gain.connect(ctx.destination)
        osc.start(t)
        osc.stop(t + 0.12)
      }
    }
  } catch {
    // AudioContext blocked
  }
}

// Text to Speech (TTS) Synthesizer
let availableVoices: SpeechSynthesisVoice[] = []
function loadVoices() {
  if ('speechSynthesis' in window) {
    availableVoices = window.speechSynthesis.getVoices()
  }
}
if ('speechSynthesis' in window) {
  loadVoices()
  window.speechSynthesis.onvoiceschanged = loadVoices
}

function speakFriday(text: string) {
  if (systemState.isSpeechMuted || !('speechSynthesis' in window)) {
    return
  }

  try {
    window.speechSynthesis.cancel()

    const utterance = new SpeechSynthesisUtterance(text)
    utterance.rate = 1.02
    utterance.pitch = 1.05

    // Pick best English voice if available
    if (availableVoices.length === 0) loadVoices()
    const englishVoice = availableVoices.find(v => v.lang.startsWith('en') && (v.name.includes('Natural') || v.name.includes('Google') || v.name.includes('Samantha') || v.name.includes('Karen')))
      || availableVoices.find(v => v.lang.startsWith('en'))
    if (englishVoice) {
      utterance.voice = englishVoice
    }

    const coreElement = document.querySelector<HTMLDivElement>('#reactor-core')
    utterance.onstart = () => {
      coreElement?.classList.add('speaking')
    }
    utterance.onend = () => {
      coreElement?.classList.remove('speaking')
    }
    utterance.onerror = () => {
      coreElement?.classList.remove('speaking')
    }

    window.speechSynthesis.speak(utterance)
  } catch (err) {
    console.warn('Speech synthesis error:', err)
  }
}

// Build Main DOM Layout
const app = document.querySelector<HTMLDivElement>('#app')!
app.innerHTML = `
  <main class="shell">
    <nav class="topbar">
      <div class="brand">
        <span class="brand-mark">F</span>
        <span>FRIDAY</span>
        <span class="beta">VOICE OS / 01</span>
      </div>
      <div class="topbar-actions">
        <button id="install-android-btn" class="android-pwa-btn" aria-label="Install Friday as Android App" title="Install Friday on your phone">
          <span class="android-robot-icon">🤖</span> <span id="install-btn-label">INSTALL APP</span>
        </button>
        <button id="android-guide-btn" class="guide-toggle" aria-label="Android Package Guide" title="Android Package & APK Guide">
          <span>📦</span> <span>APK GUIDE</span>
        </button>
        <button id="speech-toggle-btn" class="speech-toggle" aria-label="Toggle speech output">
          <span id="speech-icon">🔊</span> <span id="speech-label">VOICE ON</span>
        </button>
        <div class="system-status">
          <span class="status-dot" id="system-status-dot"></span>
          <span id="ai-badge-text">GEMINI AI ONLINE</span>
        </div>
      </div>
    </nav>

    <section class="hero">
      <div class="hud-readout readout-left">
        <span>CPU TEMP</span><b>${systemState.cpuTemp}</b>
        <span style="margin-top:4px;">VOLUME</span><b id="hud-volume">${systemState.volumeLevel}%</b>
      </div>
      <div class="hud-readout readout-right">
        <span>INTELLIGENCE</span><b id="hud-ai-status">GEMINI 3.8</b>
        <span style="margin-top:4px;">MIC STATUS</span><b id="hud-mic-status">READY</b>
      </div>

      <div class="eyebrow">
        <span class="status-dot active"></span>
        <span id="listen-status-label">FRIDAY INTELLIGENCE · LISTENING FOR VOICE</span>
      </div>

      <!-- Sci-Fi Reactor Core -->
      <div class="core" id="reactor-core">
        <span class="core-ring ring-one"></span>
        <span class="core-ring ring-two"></span>
        <span class="core-light"></span>
        <span class="core-scan"></span>
      </div>

      <h1>At your<br><em>service.</em></h1>
      <p class="hero-copy">Speak naturally. Friday controls your phone hardware, manages timers & alarms, plays music, and answers anything with Gemini AI.</p>

      <!-- Voice Trigger & Command Bar -->
      <div class="voice-deck">
        <button class="listen-button" id="mic-btn">
          <span class="mic" id="mic-icon">🎙</span>
          <span id="mic-btn-label">TAP TO SPEAK COMMAND</span>
        </button>

        <form class="manual-input-box" id="command-form">
          <input type="text" id="command-input" placeholder="Say or type a command: 'set 5m timer', 'turn on flashlight', 'what is gravity?'..." autocomplete="off" />
          <button type="submit">SEND ↗</button>
        </form>
      </div>

      <!-- Real-time Transcript & Response Card -->
      <div class="transcript-card" id="transcript-card">
        <div class="transcript-header">
          <span>SPEECH TRANSCRIPT / AUDIT</span>
          <span id="thinking-indicator" style="display:none; color:#f3b15c;">PROCESSING...</span>
        </div>
        <div class="transcript-content" id="transcript">"Hey Friday, at your service. Speak a command or question."</div>
        <div class="assistant-response" id="assistant-response-wrap" style="display:none;">
          <span class="speaker-icon">🔊</span>
          <div id="assistant-response-text"></div>
        </div>
      </div>
    </section>

    <!-- Interactive Device Deck: Timers, Alarms, Media & Hardware -->
    <section class="device-deck">
      <div class="section-heading">
        <div>
          <span class="kicker">SYSTEM CONTROLS</span>
          <h2>Device Command Center</h2>
        </div>
        <span class="shortcut">AUDIO ENGINE <b>TTS READY</b></span>
      </div>

      <div class="device-grid">
        <!-- 1. Active Timer Card -->
        <div class="device-card" id="timer-card">
          <div class="card-top">
            <span class="card-title"><span>⏱</span> ACTIVE COUNTDOWN TIMER</span>
            <span class="card-badge" id="timer-badge">STANDBY</span>
          </div>
          <div class="timer-display" id="timer-display">
            <span id="timer-digits">00:00</span>
            <span class="timer-sub" id="timer-sub">REMAINING</span>
          </div>
          <div class="timer-bar-wrap">
            <div class="timer-bar" id="timer-bar" style="width: 0%;"></div>
          </div>
          <div class="timer-actions">
            <button class="timer-btn" id="timer-toggle-btn">START</button>
            <button class="timer-btn danger" id="timer-reset-btn">CANCEL</button>
          </div>
          <div class="quick-timers">
            <button class="quick-timer-chip" data-seconds="30">+30 SEC</button>
            <button class="quick-timer-chip" data-seconds="300">+5 MIN</button>
            <button class="quick-timer-chip" data-seconds="600">+10 MIN</button>
            <button class="quick-timer-chip" data-seconds="1500">+25 MIN</button>
          </div>
        </div>

        <!-- 2. Media Player Deck -->
        <div class="device-card" id="media-card">
          <div class="card-top">
            <span class="card-title"><span>🎵</span> AUDIO & MEDIA CONTROLS</span>
            <span class="card-badge" id="media-badge">PAUSED</span>
          </div>
          <div class="media-track-info" id="media-track-info">
            <div class="album-art">🎧</div>
            <div class="track-meta">
              <strong id="track-title">${systemState.media.tracks[0].title}</strong>
              <small id="track-artist">${systemState.media.tracks[0].artist}</small>
            </div>
            <div class="equalizer-bars" id="equalizer-bars">
              <span class="eq-bar"></span>
              <span class="eq-bar"></span>
              <span class="eq-bar"></span>
              <span class="eq-bar"></span>
              <span class="eq-bar"></span>
            </div>
          </div>
          <div class="media-controls">
            <button class="media-btn" id="media-prev-btn" title="Previous Track">⏮</button>
            <button class="media-btn play-btn" id="media-play-btn" title="Play/Pause">▶</button>
            <button class="media-btn" id="media-next-btn" title="Next Track">⏭</button>
          </div>
          <div class="volume-row">
            <span id="volume-icon">🔊</span>
            <input type="range" min="0" max="100" value="${systemState.volumeLevel}" class="volume-slider" id="volume-slider" />
            <span id="volume-label">${systemState.volumeLevel}%</span>
          </div>
        </div>

        <!-- 3. Hardware Quick Switches -->
        <div class="device-card" id="hardware-card">
          <div class="card-top">
            <span class="card-title"><span>⚡</span> HARDWARE RADIOS & POWER</span>
            <span class="card-badge">LIVE CONTROLS</span>
          </div>
          <div class="hardware-switches">
            <button class="switch-btn" id="toggle-torch">
              <span class="switch-icon">🔦</span>
              <span id="torch-label">FLASHLIGHT</span>
            </button>
            <button class="switch-btn active" id="toggle-wifi">
              <span class="switch-icon">📶</span>
              <span id="wifi-label">WI-FI ON</span>
            </button>
            <button class="switch-btn active" id="toggle-bluetooth">
              <span class="switch-icon">ᛒ</span>
              <span id="bt-label">BLUETOOTH</span>
            </button>
            <button class="switch-btn" id="toggle-battery">
              <span class="switch-icon">🔋</span>
              <span id="battery-label">POWER SAVER</span>
            </button>
            <button class="switch-btn" id="toggle-dnd">
              <span class="switch-icon">🌙</span>
              <span id="dnd-label">DND OFF</span>
            </button>
            <button class="switch-btn" id="clean-storage-btn">
              <span class="switch-icon">⌁</span>
              <span>CLEAN CACHE</span>
            </button>
          </div>
          <div class="flashlight-indicator" id="flashlight-beam">
            <span>🔦 Torch Status:</span> <strong id="torch-status-text">Deactivated (Dark)</strong>
          </div>
        </div>

        <!-- 4. Alarms & Schedules -->
        <div class="device-card" id="alarms-card">
          <div class="card-top">
            <span class="card-title"><span>⏰</span> SCHEDULED ALARMS</span>
            <button class="card-badge" id="quick-add-alarm-btn" style="cursor:pointer;">+ ADD ALARM</button>
          </div>
          <div class="alarm-list" id="alarm-list"></div>
        </div>

        <!-- 5. Phone & Dialer Deck -->
        <div class="device-card" id="phone-card">
          <div class="card-top">
            <span class="card-title"><span>📞</span> PHONE & CALL SYSTEM</span>
            <span class="card-badge" id="phone-badge">STANDBY</span>
          </div>
          <div class="phone-deck-tabs">
            <button class="phone-tab-btn active" id="tab-btn-contacts">CONTACTS</button>
            <button class="phone-tab-btn" id="tab-btn-dialpad">DIALPAD</button>
            <button class="phone-tab-btn" id="tab-btn-recents">RECENTS</button>
          </div>
          <div id="phone-tab-content">
            <!-- Rendered dynamically -->
          </div>
        </div>

        <!-- 6. Storage & File Deletion Deck -->
        <div class="device-card" id="storage-deck-card">
          <div class="card-top">
            <span class="card-title"><span>💾</span> STORAGE & FILE DELETION</span>
            <span class="card-badge" id="storage-usage-badge">48.2 GB / 128 GB</span>
          </div>
          <div class="storage-capacity-row">
            <span>USED STORAGE (<span id="storage-pct-text">37%</span>)</span>
            <span id="storage-free-text">79.8 GB FREE</span>
          </div>
          <div class="timer-bar-wrap">
            <div class="timer-bar" id="storage-bar" style="width: 37.6%; background: #72e4e7;"></div>
          </div>
          <div class="file-filter-chips">
            <button class="file-chip active" data-filter="all">ALL (<span id="count-all">7</span>)</button>
            <button class="file-chip" data-filter="cache">CACHE (<span id="count-cache">605MB</span>)</button>
            <button class="file-chip" data-filter="downloads">DOWNLOADS (<span id="count-downloads">752MB</span>)</button>
            <button class="file-chip" data-filter="logs">LOGS (<span id="count-logs">88MB</span>)</button>
            <button class="file-chip" data-filter="media">MEDIA (<span id="count-media">89MB</span>)</button>
          </div>
          <div class="file-list-deck" id="file-list-deck">
            <!-- Rendered dynamically -->
          </div>
          <div class="storage-actions-deck">
            <button class="timer-btn" id="file-select-all-btn">SELECT ALL</button>
            <button class="storage-bulk-delete-btn" id="file-delete-bulk-btn">
              <span>🗑</span>
              <span id="file-bulk-label">DELETE SELECTED</span>
            </button>
          </div>
        </div>

        <!-- 7. Android Application Launcher & Intent Hub -->
        <div class="device-card" id="apps-card">
          <div class="card-top">
            <span class="card-title"><span>📱</span> ANDROID APPLICATIONS & INTENTS</span>
            <span class="card-badge" id="app-launcher-badge">10 APPS READY</span>
          </div>
          <div class="app-launcher-desc">
            Direct deep-link integration with native phone applications. Speak <em>"Open WhatsApp"</em>, <em>"Open YouTube"</em>, <em>"Navigate to..."</em>, or tap below to launch.
          </div>
          <div class="app-shortcuts-grid" id="app-shortcuts-grid">
            <!-- Rendered dynamically -->
          </div>
          <div class="custom-intent-runner">
            <div class="intent-input-wrap">
              <input type="text" id="custom-intent-input" placeholder="Launch custom URI (e.g. instagram://, slack://, geo:0,0?q=Tokyo)" />
              <button id="run-intent-btn">LAUNCH ↗</button>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Quick Voice Commands Grid -->
    <section class="command-panel">
      <div class="section-heading">
        <div>
          <span class="kicker">QUICK COMMANDS</span>
          <h2>Preset Voice Invocations</h2>
        </div>
        <span class="shortcut">VOICE ACCELERATOR <b>⚡</b></span>
      </div>
      <div class="command-grid">
        <button class="command" data-command="Open WhatsApp">
          <span class="command-icon mint">💬</span>
          <span><strong>Open WhatsApp</strong><small>Launch phone messaging app</small></span>
          <span class="arrow">↗</span>
        </button>
        <button class="command" data-command="Open YouTube">
          <span class="command-icon coral">▶</span>
          <span><strong>Open YouTube</strong><small>Stream videos & music</small></span>
          <span class="arrow">↗</span>
        </button>
        <button class="command" data-command="Navigate to Central Park">
          <span class="command-icon yellow">🗺️</span>
          <span><strong>Navigate to Central Park</strong><small>Google Maps turn-by-turn</small></span>
          <span class="arrow">↗</span>
        </button>
        <button class="command" data-command="Open Camera">
          <span class="command-icon purple">📷</span>
          <span><strong>Open Camera</strong><small>Hardware sensor viewfinder</small></span>
          <span class="arrow">↗</span>
        </button>
        <button class="command" data-command="Install Android App">
          <span class="command-icon mint">🤖</span>
          <span><strong>Install Android App</strong><small>Add to phone home screen</small></span>
          <span class="arrow">↗</span>
        </button>
        <button class="command" data-command="What's the weather today?">
          <span class="command-icon yellow">☀️</span>
          <span><strong>What's the weather today?</strong><small>Live Open-Meteo satellite readings</small></span>
          <span class="arrow">↗</span>
        </button>
        <button class="command" data-command="Call Mom on speaker">
          <span class="command-icon mint">📞</span>
          <span><strong>Call Mom on speaker</strong><small>Hands-free voice dialer</small></span>
          <span class="arrow">↗</span>
        </button>
        <button class="command" data-command="Delete cache and temporary files">
          <span class="command-icon coral">🗑</span>
          <span><strong>Delete cache & temp files</strong><small>Reclaim storage memory</small></span>
          <span class="arrow">↗</span>
        </button>
        <button class="command" data-command="Why is the sky blue?">
          <span class="command-icon purple">✦</span>
          <span><strong>Why is the sky blue?</strong><small>Gemini AI conversational answer</small></span>
          <span class="arrow">↗</span>
        </button>
        <button class="command" data-command="Set a timer for 5 minutes">
          <span class="command-icon mint">⏱</span>
          <span><strong>Set a timer for 5 minutes</strong><small>Active device countdown</small></span>
          <span class="arrow">↗</span>
        </button>
      </div>
    </section>

    <!-- Audit Trail -->
    <section class="activity">
      <div class="section-heading">
        <div>
          <span class="kicker">AUDIT TRAIL</span>
          <h2>Recent Activity & Logs</h2>
        </div>
        <button class="text-button" id="clear-history-btn">Clear logs <span>↺</span></button>
      </div>
      <div id="history-list"></div>
    </section>

    <!-- Access Permissions Panel -->
    <section class="access-panel">
      <div class="section-heading">
        <div>
          <span class="kicker">ACCESS CONTROL</span>
          <h2>Phone Permissions & Sandboxing</h2>
        </div>
        <span class="access-note">USER MONITORED</span>
      </div>
      <p class="access-copy">Friday requests sandboxed access for device hardware, speech synthesis, and local processing. Every privilege can be revoked at any time.</p>
      <div class="permission-list" id="permission-list"></div>
    </section>

    <footer>
      <span>FRIDAY INTELLIGENCE SYSTEM</span>
      <span class="footer-line"></span>
      <span>GEMINI 3.6 FLASH HYBRID ENGINE</span>
      <span class="footer-line"></span>
      <span>TTS SYNTHESIZER READY</span>
      <span class="footer-spacer"></span>
      <span>LATENCY 11 MS</span>
    </footer>
  </main>
  <div class="toast" id="toast" role="status"></div>
  <!-- In-Call Floating Screen Modal -->
  <div id="call-modal-wrap" style="display: none;"></div>
  <!-- Camera Viewfinder Modal -->
  <div id="camera-modal-wrap" style="display: none;"></div>
  <!-- Android Guide & APK Modal -->
  <div id="android-guide-modal-wrap" style="display: none;"></div>
`

// DOM Elements
const historyList = document.querySelector<HTMLDivElement>('#history-list')!
const transcript = document.querySelector<HTMLDivElement>('#transcript')!
const assistantResponseWrap = document.querySelector<HTMLDivElement>('#assistant-response-wrap')!
const assistantResponseText = document.querySelector<HTMLDivElement>('#assistant-response-text')!
const thinkingIndicator = document.querySelector<HTMLSpanElement>('#thinking-indicator')!
const toast = document.querySelector<HTMLDivElement>('#toast')!
const permissionList = document.querySelector<HTMLDivElement>('#permission-list')!
const reactorCore = document.querySelector<HTMLDivElement>('#reactor-core')!
const micBtn = document.querySelector<HTMLButtonElement>('#mic-btn')!
const micBtnLabel = document.querySelector<HTMLSpanElement>('#mic-btn-label')!
const commandForm = document.querySelector<HTMLFormElement>('#command-form')!
const commandInput = document.querySelector<HTMLInputElement>('#command-input')!
const speechToggleBtn = document.querySelector<HTMLButtonElement>('#speech-toggle-btn')!
const speechIcon = document.querySelector<HTMLSpanElement>('#speech-icon')!
const speechLabel = document.querySelector<HTMLSpanElement>('#speech-label')!

// Device DOM elements
const timerDigits = document.querySelector<HTMLSpanElement>('#timer-digits')!
const timerBadge = document.querySelector<HTMLSpanElement>('#timer-badge')!
const timerBar = document.querySelector<HTMLDivElement>('#timer-bar')!
const timerToggleBtn = document.querySelector<HTMLButtonElement>('#timer-toggle-btn')!
const timerResetBtn = document.querySelector<HTMLButtonElement>('#timer-reset-btn')!

const mediaBadge = document.querySelector<HTMLSpanElement>('#media-badge')!
const trackTitle = document.querySelector<HTMLElement>('#track-title')!
const trackArtist = document.querySelector<HTMLElement>('#track-artist')!
const mediaTrackInfo = document.querySelector<HTMLDivElement>('#media-track-info')!
const mediaPlayBtn = document.querySelector<HTMLButtonElement>('#media-play-btn')!
const mediaPrevBtn = document.querySelector<HTMLButtonElement>('#media-prev-btn')!
const mediaNextBtn = document.querySelector<HTMLButtonElement>('#media-next-btn')!
const volumeSlider = document.querySelector<HTMLInputElement>('#volume-slider')!
const volumeLabel = document.querySelector<HTMLSpanElement>('#volume-label')!
const hudVolume = document.querySelector<HTMLElement>('#hud-volume')!

const toggleTorchBtn = document.querySelector<HTMLButtonElement>('#toggle-torch')!
const torchStatusText = document.querySelector<HTMLElement>('#torch-status-text')!
const flashlightBeam = document.querySelector<HTMLDivElement>('#flashlight-beam')!
const toggleWifiBtn = document.querySelector<HTMLButtonElement>('#toggle-wifi')!
const wifiLabel = document.querySelector<HTMLSpanElement>('#wifi-label')!
const toggleBtBtn = document.querySelector<HTMLButtonElement>('#toggle-bluetooth')!
const btLabel = document.querySelector<HTMLSpanElement>('#bt-label')!
const toggleBatteryBtn = document.querySelector<HTMLButtonElement>('#toggle-battery')!
const batteryLabel = document.querySelector<HTMLSpanElement>('#battery-label')!
const toggleDndBtn = document.querySelector<HTMLButtonElement>('#toggle-dnd')!
const dndLabel = document.querySelector<HTMLSpanElement>('#dnd-label')!
const alarmList = document.querySelector<HTMLDivElement>('#alarm-list')!

// Phone Deck DOM Elements
const phoneBadge = document.querySelector<HTMLSpanElement>('#phone-badge')!
const tabBtnContacts = document.querySelector<HTMLButtonElement>('#tab-btn-contacts')!
const tabBtnDialpad = document.querySelector<HTMLButtonElement>('#tab-btn-dialpad')!
const tabBtnRecents = document.querySelector<HTMLButtonElement>('#tab-btn-recents')!
const phoneTabContent = document.querySelector<HTMLDivElement>('#phone-tab-content')!
const callModalWrap = document.querySelector<HTMLDivElement>('#call-modal-wrap')!

// Storage Deck DOM Elements
const storageUsageBadge = document.querySelector<HTMLSpanElement>('#storage-usage-badge')!
const storagePctText = document.querySelector<HTMLSpanElement>('#storage-pct-text')!
const storageFreeText = document.querySelector<HTMLSpanElement>('#storage-free-text')!
const storageBar = document.querySelector<HTMLDivElement>('#storage-bar')!
const fileListDeck = document.querySelector<HTMLDivElement>('#file-list-deck')!
const fileSelectAllBtn = document.querySelector<HTMLButtonElement>('#file-select-all-btn')!
const fileDeleteBulkBtn = document.querySelector<HTMLButtonElement>('#file-delete-bulk-btn')!
const fileBulkLabel = document.querySelector<HTMLSpanElement>('#file-bulk-label')!

const countAll = document.querySelector<HTMLSpanElement>('#count-all')!
const countCache = document.querySelector<HTMLSpanElement>('#count-cache')!
const countDownloads = document.querySelector<HTMLSpanElement>('#count-downloads')!
const countLogs = document.querySelector<HTMLSpanElement>('#count-logs')!
const countMedia = document.querySelector<HTMLSpanElement>('#count-media')!

// Android App & PWA DOM Elements
const installAndroidBtn = document.querySelector<HTMLButtonElement>('#install-android-btn')!
const androidGuideBtn = document.querySelector<HTMLButtonElement>('#android-guide-btn')!
const appShortcutsGrid = document.querySelector<HTMLDivElement>('#app-shortcuts-grid')!
const customIntentInput = document.querySelector<HTMLInputElement>('#custom-intent-input')!
const runIntentBtn = document.querySelector<HTMLButtonElement>('#run-intent-btn')!
const cameraModalWrap = document.querySelector<HTMLDivElement>('#camera-modal-wrap')!
const androidGuideModalWrap = document.querySelector<HTMLDivElement>('#android-guide-modal-wrap')!

function showToast(message: string) {
  toast.textContent = message
  toast.classList.add('show')
  window.setTimeout(() => toast.classList.remove('show'), 2800)
}

// ----------------------------------------------------
// Audit History Renderer
// ----------------------------------------------------
function renderHistory() {
  historyList.innerHTML = history
    .map(
      (item, idx) => `
    <article class="history-item">
      <span class="history-icon">${item.icon}</span>
      <div>
        <strong>${item.title}</strong>
        <small>${item.detail}</small>
      </div>
      <time>${item.time}</time>
      ${item.undoAction ? `<button class="undo" data-index="${idx}">Undo</button>` : ''}
    </article>
  `,
    )
    .join('')

  historyList.querySelectorAll<HTMLButtonElement>('.undo').forEach(button => {
    button.addEventListener('click', () => {
      const idx = Number(button.dataset.index)
      const target = history[idx]
      if (target?.undoAction) {
        target.undoAction()
        history.splice(idx, 1)
        renderHistory()
        showToast(`Undone: ${target.title}`)
        playChime('click')
      }
    })
  })
}

// ----------------------------------------------------
// Permissions Renderer
// ----------------------------------------------------
function renderPermissions() {
  permissionList.innerHTML = permissions
    .map(
      (permission, index) => `
    <div class="permission-row">
      <span class="permission-icon">${permission.icon}</span>
      <span class="permission-info">
        <strong>${permission.name}</strong>
        <small>${permission.purpose}</small>
      </span>
      <span class="permission-state ${permission.enabled ? 'granted' : ''}">
        ${permission.enabled ? 'GRANTED' : 'REVOKED'}
      </span>
      <button class="permission-action" data-permission="${index}">
        ${permission.enabled ? 'REVOKE' : 'GRANT'}
      </button>
    </div>
  `,
    )
    .join('')

  permissionList.querySelectorAll<HTMLButtonElement>('.permission-action').forEach(button => {
    button.addEventListener('click', () => {
      const permission = permissions[Number(button.dataset.permission)]
      permission.enabled = !permission.enabled
      renderPermissions()
      playChime('click')
      showToast(permission.enabled ? `${permission.name} permission granted` : `${permission.name} permission revoked`)
    })
  })
}

// ----------------------------------------------------
// Alarms Renderer & Management
// ----------------------------------------------------
function renderAlarms() {
  alarmList.innerHTML = systemState.alarms
    .map(
      alarm => `
    <div class="alarm-row">
      <div>
        <span class="alarm-time">${alarm.time}</span>
        <span class="alarm-label">${alarm.label}</span>
      </div>
      <button class="alarm-switch ${alarm.enabled ? 'active' : ''}" data-alarm-id="${alarm.id}">
        ${alarm.enabled ? 'ON' : 'OFF'}
      </button>
    </div>
  `,
    )
    .join('')

  alarmList.querySelectorAll<HTMLButtonElement>('.alarm-switch').forEach(btn => {
    btn.addEventListener('click', () => {
      const id = btn.dataset.alarmId
      const target = systemState.alarms.find(a => a.id === id)
      if (target) {
        target.enabled = !target.enabled
        renderAlarms()
        playChime('click')
        showToast(`Alarm for ${target.time} ${target.enabled ? 'enabled' : 'disabled'}`)
      }
    })
  })
}

// ----------------------------------------------------
// Phone & Call System
// ----------------------------------------------------
function renderPhoneDeck() {
  if (!phoneTabContent) return

  tabBtnContacts.classList.toggle('active', systemState.activePhoneTab === 'contacts')
  tabBtnDialpad.classList.toggle('active', systemState.activePhoneTab === 'dialpad')
  tabBtnRecents.classList.toggle('active', systemState.activePhoneTab === 'recents')

  if (systemState.activePhoneTab === 'contacts') {
    phoneTabContent.innerHTML = `
      <div class="contact-list">
        ${systemState.contacts
          .map(
            c => `
          <div class="contact-item">
            <div class="contact-avatar">${c.avatar}</div>
            <div class="contact-info">
              <span class="contact-name">${c.name} <small style="color:#71dbe088;font-weight:normal;">(${c.relation})</small></span>
              <span class="contact-num">${c.number}</span>
            </div>
            <button class="call-action-btn" data-contact-name="${c.name}" data-contact-num="${c.number}">
              CALL
            </button>
          </div>
        `,
          )
          .join('')}
      </div>
    `

    phoneTabContent.querySelectorAll<HTMLButtonElement>('.call-action-btn').forEach(btn => {
      btn.addEventListener('click', () => {
        const name = btn.dataset.contactName || 'Contact'
        const num = btn.dataset.contactNum || ''
        startCall(name, num, false)
      })
    })
  } else if (systemState.activePhoneTab === 'dialpad') {
    phoneTabContent.innerHTML = `
      <div class="dialpad-container">
        <div class="dial-screen">
          <span class="dial-number-text" id="dial-display-text">${systemState.dialpadNumber || 'ENTER NUMBER...'}</span>
          <button class="dial-backspace" id="dial-backspace-btn" title="Backspace">⌫</button>
        </div>
        <div class="dialpad-keys">
          ${[
            { main: '1', sub: ' ' },
            { main: '2', sub: 'ABC' },
            { main: '3', sub: 'DEF' },
            { main: '4', sub: 'GHI' },
            { main: '5', sub: 'JKL' },
            { main: '6', sub: 'MNO' },
            { main: '7', sub: 'PQRS' },
            { main: '8', sub: 'TUV' },
            { main: '9', sub: 'WXYZ' },
            { main: '*', sub: ' ' },
            { main: '0', sub: '+' },
            { main: '#', sub: ' ' },
          ]
            .map(
              k => `
            <button class="dial-digit-btn" data-digit="${k.main}">
              <span class="digit-main">${k.main}</span>
              <span class="digit-sub">${k.sub}</span>
            </button>
          `,
            )
            .join('')}
        </div>
        <button class="dial-primary-btn" id="dial-call-trigger-btn">
          <span>📞</span> INITIATE CALL
        </button>
      </div>
    `

    phoneTabContent.querySelectorAll<HTMLButtonElement>('.dial-digit-btn').forEach(btn => {
      btn.addEventListener('click', () => {
        const digit = btn.dataset.digit || ''
        playDtmfTone(digit)
        systemState.dialpadNumber += digit
        const display = document.querySelector<HTMLSpanElement>('#dial-display-text')
        if (display) display.textContent = systemState.dialpadNumber
      })
    })

    document.querySelector<HTMLButtonElement>('#dial-backspace-btn')?.addEventListener('click', () => {
      systemState.dialpadNumber = systemState.dialpadNumber.slice(0, -1)
      const display = document.querySelector<HTMLSpanElement>('#dial-display-text')
      if (display) display.textContent = systemState.dialpadNumber || 'ENTER NUMBER...'
      playChime('click')
    })

    document.querySelector<HTMLButtonElement>('#dial-call-trigger-btn')?.addEventListener('click', () => {
      const num = systemState.dialpadNumber.trim() || '+1 (555) 019-2834'
      startCall('Dialed Contact', num, false)
    })
  } else if (systemState.activePhoneTab === 'recents') {
    phoneTabContent.innerHTML = `
      <div class="contact-list">
        ${systemState.recentCalls
          .map(
            r => `
          <div class="contact-item">
            <div class="contact-avatar" style="background:#07181c;border-color:#38d996;color:#38d996;">
              ${r.type === 'incoming' ? '↙' : '↗'}
            </div>
            <div class="contact-info">
              <span class="contact-name">${r.name}</span>
              <span class="contact-num">${r.time} · ${r.duration}</span>
            </div>
            <button class="call-action-btn" data-name="${r.name}" data-num="${r.number}">
              REDIAL
            </button>
          </div>
        `,
          )
          .join('')}
      </div>
    `

    phoneTabContent.querySelectorAll<HTMLButtonElement>('.call-action-btn').forEach(btn => {
      btn.addEventListener('click', () => {
        const name = btn.dataset.name || 'Contact'
        const num = btn.dataset.num || ''
        startCall(name, num, false)
      })
    })
  }
}

function renderCallModal() {
  if (!callModalWrap) return

  if (!systemState.call.isActive) {
    callModalWrap.style.display = 'none'
    callModalWrap.innerHTML = ''
    phoneBadge.textContent = 'STANDBY'
    phoneBadge.classList.remove('active')
    return
  }

  callModalWrap.style.display = 'grid'
  phoneBadge.textContent = 'IN CALL'
  phoneBadge.classList.add('active')

  const durMin = Math.floor(systemState.call.durationSeconds / 60)
    .toString()
    .padStart(2, '0')
  const durSec = (systemState.call.durationSeconds % 60).toString().padStart(2, '0')
  const durationText = `${durMin}:${durSec}`

  let statusLabel = 'CALLING...'
  let isConnected = false
  if (systemState.call.status === 'ringing') {
    statusLabel = 'RINGING...'
  } else if (systemState.call.status === 'connected') {
    statusLabel = `CONNECTED · ${durationText}`
    isConnected = true
  }

  callModalWrap.innerHTML = `
    <div class="call-modal-overlay">
      <div class="call-modal-card">
        <div class="call-avatar-wrap">
          <div class="call-avatar-pulse"></div>
          <div class="call-avatar-core">
            ${systemState.call.contactName.charAt(0).toUpperCase()}
          </div>
        </div>
        <div class="call-target-name">${systemState.call.contactName}</div>
        <div class="call-target-phone">${systemState.call.phoneNumber}</div>
        <div class="call-status-pill ${isConnected ? 'connected' : ''}">
          <span style="font-size:8px;">●</span> ${statusLabel}
        </div>
        <div class="call-controls-grid">
          <button class="call-ctl-btn ${systemState.call.isMuted ? 'active' : ''}" id="modal-mute-btn">
            <span style="font-size:16px;">${systemState.call.isMuted ? '🔇' : '🎙'}</span>
            <span>${systemState.call.isMuted ? 'MUTED' : 'MUTE'}</span>
          </button>
          <button class="call-ctl-btn ${systemState.call.isSpeaker ? 'active' : ''}" id="modal-speaker-btn">
            <span style="font-size:16px;">🔊</span>
            <span>${systemState.call.isSpeaker ? 'SPEAKER ON' : 'SPEAKER'}</span>
          </button>
          <button class="call-ctl-btn" id="modal-keypad-btn">
            <span style="font-size:16px;">🔢</span>
            <span>KEYPAD</span>
          </button>
        </div>
        <button class="call-end-btn" id="modal-end-call-btn">
          <span>✆</span> END CALL
        </button>
      </div>
    </div>
  `

  document.querySelector<HTMLButtonElement>('#modal-end-call-btn')?.addEventListener('click', () => {
    endCall()
  })

  document.querySelector<HTMLButtonElement>('#modal-mute-btn')?.addEventListener('click', () => {
    systemState.call.isMuted = !systemState.call.isMuted
    renderCallModal()
    playChime('click')
    showToast(systemState.call.isMuted ? 'Microphone muted' : 'Microphone unmuted')
  })

  document.querySelector<HTMLButtonElement>('#modal-speaker-btn')?.addEventListener('click', () => {
    systemState.call.isSpeaker = !systemState.call.isSpeaker
    renderCallModal()
    playChime('click')
    showToast(systemState.call.isSpeaker ? 'Speakerphone active' : 'Earpiece mode active')
  })

  document.querySelector<HTMLButtonElement>('#modal-keypad-btn')?.addEventListener('click', () => {
    const digit = window.prompt('Enter DTMF digit (0-9, *, #):', '1')
    if (digit) {
      playDtmfTone(digit.charAt(0))
      showToast(`DTMF Tone sent: ${digit.charAt(0)}`)
    }
  })
}

function startCall(contactName: string, phoneNumber?: string, speakerphone = false) {
  if (systemState.call.timerId) {
    window.clearInterval(systemState.call.timerId)
    systemState.call.timerId = null
  }

  systemState.call.isActive = true
  systemState.call.contactName = contactName
  systemState.call.phoneNumber = phoneNumber || '+1 (555) 019-2834'
  systemState.call.status = 'calling'
  systemState.call.durationSeconds = 0
  systemState.call.isSpeaker = speakerphone
  systemState.call.isMuted = false

  renderCallModal()
  playCallTone('dial')
  showToast(`Dialing ${contactName}...`)

  // Ringing phase
  window.setTimeout(() => {
    if (!systemState.call.isActive) return
    systemState.call.status = 'ringing'
    renderCallModal()
    playCallTone('ring')
  }, 900)

  // Connected phase
  window.setTimeout(() => {
    if (!systemState.call.isActive) return
    systemState.call.status = 'connected'
    playChime('confirm')
    renderCallModal()
    showToast(`Call connected with ${contactName}`)

    systemState.call.timerId = window.setInterval(() => {
      if (!systemState.call.isActive) {
        if (systemState.call.timerId) window.clearInterval(systemState.call.timerId)
        return
      }
      systemState.call.durationSeconds++
      renderCallModal()
    }, 1000)
  }, 2400)

  history.unshift({
    icon: '📞',
    title: `Call: ${contactName}`,
    detail: `${phoneNumber || '+1 (555) 019-2834'} · ${speakerphone ? 'Speakerphone' : 'Voice line'}`,
    time: 'Just now',
    undoAction: () => endCall(),
  })
  renderHistory()
}

function endCall() {
  if (!systemState.call.isActive) return

  if (systemState.call.timerId) {
    window.clearInterval(systemState.call.timerId)
    systemState.call.timerId = null
  }

  const durMin = Math.floor(systemState.call.durationSeconds / 60)
  const durSec = systemState.call.durationSeconds % 60
  const durationStr = `${durMin}m ${durSec}s`

  systemState.recentCalls.unshift({
    id: String(Date.now()),
    name: systemState.call.contactName,
    number: systemState.call.phoneNumber,
    type: 'outgoing',
    time: 'Just now',
    duration: durationStr,
  })

  systemState.call.isActive = false
  systemState.call.status = 'ended'
  playCallTone('hangup')
  renderCallModal()
  renderPhoneDeck()
  showToast(`Call ended (${durationStr})`)
}

// ----------------------------------------------------
// Storage & File Deletion Management
// ----------------------------------------------------
function renderStorageDeck() {
  const files = systemState.storage.files
  const cacheFiles = files.filter(f => f.category === 'cache')
  const downloadFiles = files.filter(f => f.category === 'downloads')
  const logFiles = files.filter(f => f.category === 'logs')
  const mediaFiles = files.filter(f => f.category === 'media')

  const cacheMB = cacheFiles.reduce((acc, f) => acc + f.sizeMB, 0)
  const downloadMB = downloadFiles.reduce((acc, f) => acc + f.sizeMB, 0)
  const logMB = logFiles.reduce((acc, f) => acc + f.sizeMB, 0)
  const mediaMB = mediaFiles.reduce((acc, f) => acc + f.sizeMB, 0)

  if (countAll) countAll.textContent = String(files.length)
  if (countCache) countCache.textContent = `${cacheMB}MB`
  if (countDownloads) countDownloads.textContent = `${downloadMB}MB`
  if (countLogs) countLogs.textContent = `${logMB}MB`
  if (countMedia) countMedia.textContent = `${mediaMB}MB`

  const usedGB = Math.max(12, Number(systemState.storage.usedGB.toFixed(1)))
  const freeGB = (systemState.storage.totalGB - usedGB).toFixed(1)
  const pct = Math.min(100, Math.round((usedGB / systemState.storage.totalGB) * 100))

  if (storageUsageBadge) storageUsageBadge.textContent = `${usedGB} GB / 128 GB`
  if (storagePctText) storagePctText.textContent = `${pct}%`
  if (storageFreeText) storageFreeText.textContent = `${freeGB} GB FREE`
  if (storageBar) storageBar.style.width = `${pct}%`

  const active = systemState.storage.activeFilter
  const displayFiles = active === 'all' ? files : files.filter(f => f.category === active)

  if (fileListDeck) {
    if (displayFiles.length === 0) {
      fileListDeck.innerHTML = `
        <div style="text-align:center;padding:24px 10px;color:#71dbe088;font-size:11px;">
          ✓ NO REMOVABLE FILES IN THIS CATEGORY
        </div>
      `
    } else {
      fileListDeck.innerHTML = displayFiles
        .map(
          f => `
        <div class="file-entry" data-file-id="${f.id}">
          <input type="checkbox" class="file-checkbox" data-file-id="${f.id}" ${f.selected ? 'checked' : ''} />
          <div class="file-details">
            <span class="file-name" title="${f.name}">${f.name}</span>
            <div class="file-submeta">
              <span class="file-tag ${f.category}">${f.category}</span>
              <span>${f.sizeMB} MB</span>
              <span>${f.date}</span>
            </div>
          </div>
          <button class="file-delete-single-btn" data-file-id="${f.id}" title="Delete file">
            🗑
          </button>
        </div>
      `,
        )
        .join('')

      fileListDeck.querySelectorAll<HTMLInputElement>('.file-checkbox').forEach(chk => {
        chk.addEventListener('change', () => {
          const id = chk.dataset.fileId
          const target = files.find(f => f.id === id)
          if (target) {
            target.selected = chk.checked
            updateBulkDeleteButton()
          }
        })
      })

      fileListDeck.querySelectorAll<HTMLButtonElement>('.file-delete-single-btn').forEach(btn => {
        btn.addEventListener('click', () => {
          const id = btn.dataset.fileId
          if (id) deleteFileById(id)
        })
      })
    }
  }

  updateBulkDeleteButton()
}

function updateBulkDeleteButton() {
  const selected = systemState.storage.files.filter(f => f.selected)
  const selectedMB = selected.reduce((acc, f) => acc + f.sizeMB, 0)
  if (fileBulkLabel) {
    fileBulkLabel.textContent = selected.length > 0 ? `DELETE SELECTED (${selectedMB} MB)` : 'DELETE SELECTED'
  }
}

function deleteFileById(fileId: string) {
  const idx = systemState.storage.files.findIndex(f => f.id === fileId)
  if (idx === -1) return

  const file = systemState.storage.files[idx]
  systemState.storage.files.splice(idx, 1)
  systemState.storage.usedGB = Math.max(12, systemState.storage.usedGB - file.sizeMB / 1024)

  renderStorageDeck()
  playChime('confirm')
  showToast(`Deleted ${file.name} (${file.sizeMB} MB)`)

  history.unshift({
    icon: '🗑',
    title: `Deleted file`,
    detail: `${file.name} · ${file.sizeMB} MB reclaimed`,
    time: 'Just now',
    undoAction: () => {
      systemState.storage.files.splice(idx, 0, file)
      systemState.storage.usedGB += file.sizeMB / 1024
      renderStorageDeck()
      showToast(`Restored ${file.name}`)
    },
  })
  renderHistory()
}

function deleteSelectedFiles() {
  const selected = systemState.storage.files.filter(f => f.selected)
  if (selected.length === 0) {
    showToast('No files selected for deletion')
    return
  }

  const selectedMB = selected.reduce((acc, f) => acc + f.sizeMB, 0)
  const count = selected.length
  const deletedCopy = [...selected]

  systemState.storage.files = systemState.storage.files.filter(f => !f.selected)
  systemState.storage.usedGB = Math.max(12, systemState.storage.usedGB - selectedMB / 1024)

  renderStorageDeck()
  playChime('confirm')
  showToast(`Purged ${count} files (${selectedMB} MB reclaimed)`)

  history.unshift({
    icon: '🗑',
    title: `Purged ${count} files`,
    detail: `${selectedMB} MB storage space reclaimed`,
    time: 'Just now',
    undoAction: () => {
      deletedCopy.forEach(f => {
        f.selected = false
        systemState.storage.files.push(f)
      })
      systemState.storage.usedGB += selectedMB / 1024
      renderStorageDeck()
      showToast(`Restored ${count} deleted files`)
    },
  })
  renderHistory()
}

function deleteByTarget(target: string) {
  const norm = target.toLowerCase()
  let targetCategory: 'cache' | 'downloads' | 'logs' | 'media' | 'all' = 'cache'
  if (norm.includes('download')) targetCategory = 'downloads'
  else if (norm.includes('log')) targetCategory = 'logs'
  else if (norm.includes('media') || norm.includes('duplicate')) targetCategory = 'media'
  else if (norm.includes('all') || norm.includes('everything') || norm.includes('junk')) targetCategory = 'all'

  const toDelete = targetCategory === 'all'
    ? [...systemState.storage.files]
    : systemState.storage.files.filter(f => f.category === targetCategory)

  if (toDelete.length === 0) {
    showToast(`No ${targetCategory} files to delete`)
    return
  }

  const totalMB = toDelete.reduce((acc, f) => acc + f.sizeMB, 0)
  const count = toDelete.length
  const deletedCopy = [...toDelete]

  if (targetCategory === 'all') {
    systemState.storage.files = []
  } else {
    systemState.storage.files = systemState.storage.files.filter(f => f.category !== targetCategory)
  }

  systemState.storage.usedGB = Math.max(12, systemState.storage.usedGB - totalMB / 1024)
  renderStorageDeck()
  playChime('confirm')
  showToast(`Purged ${count} ${targetCategory} files (${totalMB} MB reclaimed)`)

  history.unshift({
    icon: '🗑',
    title: `Purged ${targetCategory} files`,
    detail: `${count} files · ${totalMB} MB reclaimed`,
    time: 'Just now',
    undoAction: () => {
      systemState.storage.files.push(...deletedCopy)
      systemState.storage.usedGB += totalMB / 1024
      renderStorageDeck()
      showToast(`Restored ${count} files`)
    },
  })
  renderHistory()
}

// ----------------------------------------------------
// Android Application Launcher & Intent Hub
// ----------------------------------------------------
function renderAppShortcuts() {
  if (!appShortcutsGrid) return
  appShortcutsGrid.innerHTML = androidApps
    .map(
      app => `
      <button class="app-shortcut-card" data-appid="${app.id}" title="Launch ${app.name} (${app.packageName})">
        <span class="app-icon-badge" style="box-shadow: 0 0 10px ${app.color}33; border-color: ${app.color}55;">${app.icon}</span>
        <div class="app-meta">
          <strong>${app.name}</strong>
          <small>${app.description}</small>
        </div>
      </button>
    `
    )
    .join('')

  appShortcutsGrid.querySelectorAll<HTMLButtonElement>('.app-shortcut-card').forEach(btn => {
    btn.addEventListener('click', () => {
      const appId = btn.dataset.appid || ''
      launchAndroidApp(appId)
    })
  })
}

function launchAndroidApp(targetIdOrUri: string, extraQuery?: string) {
  const norm = targetIdOrUri.toLowerCase().trim()

  if (norm === 'camera' || norm === 'open camera') {
    openCameraViewfinder()
    speakFriday('Opening camera sensor viewfinder.')
    return
  }

  const matched = androidApps.find(
    a => a.id === norm || a.name.toLowerCase() === norm || a.packageName.toLowerCase().includes(norm)
  )

  let targetUrl = ''
  let appTitle = targetIdOrUri
  let appDetail = ''

  if (matched) {
    appTitle = matched.name
    appDetail = matched.packageName
    if (matched.id === 'maps') {
      const q = extraQuery || 'Central Park'
      targetUrl = `https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(q)}`
    } else if (matched.id === 'whatsapp') {
      targetUrl = extraQuery
        ? `https://api.whatsapp.com/send?text=${encodeURIComponent(extraQuery)}`
        : 'whatsapp://send'
    } else if (matched.id === 'youtube') {
      targetUrl = extraQuery
        ? `https://www.youtube.com/results?search_query=${encodeURIComponent(extraQuery)}`
        : 'https://youtube.com'
    } else if (matched.scheme.startsWith('http')) {
      targetUrl = matched.scheme
    } else {
      targetUrl = matched.scheme
    }
  } else {
    // Custom URI or package
    targetUrl =
      targetIdOrUri.startsWith('http') ||
      targetIdOrUri.includes('://') ||
      targetIdOrUri.startsWith('tel:') ||
      targetIdOrUri.startsWith('sms:') ||
      targetIdOrUri.startsWith('mailto:')
        ? targetIdOrUri
        : `intent://${targetIdOrUri}#Intent;scheme=android-app;end;`
    appTitle = 'Custom Intent'
    appDetail = targetIdOrUri
  }

  // Add to activity history
  history.unshift({
    icon: matched?.icon || '📱',
    title: `Launched ${appTitle}`,
    detail: `${appDetail} · ${targetUrl}`,
    time: 'Just now',
  })
  renderHistory()
  playChime('confirm')
  showToast(`Launching ${appTitle}...`)
  speakFriday(`Launching ${appTitle}.`)

  // Attempt window.open or window.location trigger
  try {
    const link = document.createElement('a')
    link.href = targetUrl
    link.target = '_blank'
    link.rel = 'noopener noreferrer'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
  } catch (e) {
    console.warn('Direct launch fallback:', e)
    window.location.href = targetUrl
  }
}

// ----------------------------------------------------
// Camera Optical Viewfinder HUD
// ----------------------------------------------------
let activeCameraStream: MediaStream | null = null
let activeFacingMode: 'environment' | 'user' = 'environment'

function openCameraViewfinder() {
  if (!cameraModalWrap) return
  cameraModalWrap.style.display = 'block'
  cameraModalWrap.innerHTML = `
    <div class="camera-overlay-modal" id="camera-overlay">
      <div class="camera-card">
        <div class="camera-header">
          <span>📷 HARDWARE CAMERA VIEWPORT · OPTICAL HUD</span>
          <button class="guide-close-btn" id="close-camera-btn" aria-label="Close camera">✕</button>
        </div>
        <div class="camera-stage">
          <video id="camera-video" autoplay playsinline muted></video>
          <div class="camera-reticle"></div>
          <div class="camera-hud-badge" id="camera-hud-badge">● SENSOR ACTIVE · 60 FPS</div>
        </div>
        <div class="camera-controls-bar">
          <button class="cam-switch-btn" id="camera-switch-btn">⟲ FLIP LENS</button>
          <button class="shutter-btn" id="camera-shutter-btn" title="Capture Snapshot">📸</button>
          <button class="cam-switch-btn" id="camera-gallery-btn">STORAGE</button>
        </div>
      </div>
    </div>
  `

  const video = cameraModalWrap.querySelector<HTMLVideoElement>('#camera-video')
  const closeBtn = cameraModalWrap.querySelector<HTMLButtonElement>('#close-camera-btn')
  const shutterBtn = cameraModalWrap.querySelector<HTMLButtonElement>('#camera-shutter-btn')
  const switchBtn = cameraModalWrap.querySelector<HTMLButtonElement>('#camera-switch-btn')
  const galleryBtn = cameraModalWrap.querySelector<HTMLButtonElement>('#camera-gallery-btn')

  closeBtn?.addEventListener('click', closeCameraViewfinder)
  switchBtn?.addEventListener('click', toggleCameraLens)
  galleryBtn?.addEventListener('click', () => {
    closeCameraViewfinder()
    showToast('Viewing photos in Storage Deck')
    document.querySelector('#storage-card')?.scrollIntoView({ behavior: 'smooth' })
  })

  shutterBtn?.addEventListener('click', () => {
    captureCameraSnapshot(video)
  })

  startCameraStream(video)
  playChime('confirm')
}

async function startCameraStream(videoElement: HTMLVideoElement | null) {
  if (!videoElement) return
  try {
    if (activeCameraStream) {
      activeCameraStream.getTracks().forEach(t => t.stop())
    }
    const stream = await navigator.mediaDevices.getUserMedia({
      video: { facingMode: activeFacingMode },
      audio: false,
    })
    activeCameraStream = stream
    videoElement.srcObject = stream
  } catch (err) {
    console.warn('Camera access unavailable:', err)
    const badge = cameraModalWrap.querySelector('#camera-hud-badge')
    if (badge) badge.textContent = 'SENSOR SIMULATOR (DEV PREVIEW)'
  }
}

function toggleCameraLens() {
  activeFacingMode = activeFacingMode === 'environment' ? 'user' : 'environment'
  const video = cameraModalWrap.querySelector<HTMLVideoElement>('#camera-video')
  startCameraStream(video)
  playChime('click')
}

function captureCameraSnapshot(video: HTMLVideoElement | null) {
  playChime('listen')
  const canvas = document.createElement('canvas')
  canvas.width = video?.videoWidth || 640
  canvas.height = video?.videoHeight || 480
  const ctx = canvas.getContext('2d')
  if (ctx && video && video.videoWidth) {
    ctx.drawImage(video, 0, 0, canvas.width, canvas.height)
  }
  const filename = `IMG_${Date.now().toString().slice(-6)}.jpg`

  // Register file in storage deck
  systemState.storage.files.unshift({
    id: String(Date.now()),
    name: filename,
    category: 'media',
    sizeMB: 3.4,
    date: 'Just now',
    selected: false,
  })
  renderStorageDeck()

  history.unshift({
    icon: '📷',
    title: 'Photo Captured',
    detail: `Saved ${filename} to Device Storage`,
    time: 'Just now',
  })
  renderHistory()
  showToast(`Captured: ${filename}`)
  speakFriday(`Captured and saved to phone storage.`)
}

function closeCameraViewfinder() {
  if (activeCameraStream) {
    activeCameraStream.getTracks().forEach(t => t.stop())
    activeCameraStream = null
  }
  if (cameraModalWrap) {
    cameraModalWrap.style.display = 'none'
    cameraModalWrap.innerHTML = ''
  }
}

// ----------------------------------------------------
// Android Installation (WebAPK) & APK Guide Modal
// ----------------------------------------------------
let deferredInstallPrompt: any = null

function initPwaInstall() {
  if ('serviceWorker' in navigator) {
    window.addEventListener('load', () => {
      navigator.serviceWorker.register('/sw.js').catch(e => console.warn('SW reg:', e))
    })
  }

  window.addEventListener('beforeinstallprompt', (e: Event) => {
    e.preventDefault()
    deferredInstallPrompt = e
    if (installAndroidBtn) {
      installAndroidBtn.classList.add('pulse')
      const label = installAndroidBtn.querySelector('#install-btn-label')
      if (label) label.textContent = 'INSTALL ON PHONE'
    }
    showToast('Friday is ready to install on your Android phone!')
  })

  window.addEventListener('appinstalled', () => {
    deferredInstallPrompt = null
    if (installAndroidBtn) {
      installAndroidBtn.classList.remove('pulse')
      const label = installAndroidBtn.querySelector('#install-btn-label')
      if (label) label.textContent = 'APP INSTALLED'
    }
    showToast('Friday Voice OS successfully installed!')
  })
}

function triggerInstallPrompt() {
  if (deferredInstallPrompt) {
    deferredInstallPrompt.prompt()
    deferredInstallPrompt.userChoice.then((choiceResult: { outcome: string }) => {
      if (choiceResult.outcome === 'accepted') {
        showToast('Installing Friday Voice OS to home screen...')
      }
      deferredInstallPrompt = null
    })
  } else {
    // If running in browser or prompt not ready, open visual Android setup guide
    openAndroidGuideModal('webapk')
  }
}

function openAndroidGuideModal(activeTab: 'webapk' | 'capacitor' | 'permissions' = 'webapk') {
  if (!androidGuideModalWrap) return
  androidGuideModalWrap.style.display = 'block'
  renderAndroidGuideModal(activeTab)
}

function closeAndroidGuideModal() {
  if (!androidGuideModalWrap) return
  androidGuideModalWrap.style.display = 'none'
  androidGuideModalWrap.innerHTML = ''
}

function renderAndroidGuideModal(tab: 'webapk' | 'capacitor' | 'permissions') {
  androidGuideModalWrap.innerHTML = `
    <div class="modal-overlay" id="guide-overlay">
      <div class="guide-card">
        <div class="guide-header">
          <div class="guide-header-title">
            <span style="font-size:22px;">🤖</span>
            <div>
              <h3>Android Application Center</h3>
              <small style="color:#6a9ba1; font-size:10px;">INSTALLATION · APK COMPILATION · APP ACCESS</small>
            </div>
          </div>
          <button class="guide-close-btn" id="close-guide-btn" aria-label="Close dialog">✕</button>
        </div>

        <div class="guide-tabs">
          <button class="guide-tab-btn ${tab === 'webapk' ? 'active' : ''}" data-tab="webapk">1-TAP ANDROID INSTALL</button>
          <button class="guide-tab-btn ${tab === 'capacitor' ? 'active' : ''}" data-tab="capacitor">NATIVE APK (STUDIO)</button>
          <button class="guide-tab-btn ${tab === 'permissions' ? 'active' : ''}" data-tab="permissions">SYSTEM CAPABILITIES</button>
        </div>

        <div class="guide-body">
          ${
            tab === 'webapk'
              ? `
            <div class="guide-step-card" style="border-color:#3ddc8455; background:#0c2826;">
              <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:8px;">
                <strong style="color:#3ddc84; font-size:13px;">📲 Install on Your Android Device</strong>
                <button class="android-pwa-btn" id="modal-direct-install-btn" style="padding:4px 10px;">INSTALL NOW ↗</button>
              </div>
              <p style="margin:0; font-size:11px; color:#d4f6df;">Android automatically builds a genuine WebAPK with an app icon in your app drawer, standalone full-screen window, and background cache.</p>
            </div>

            <div class="guide-step-card">
              <span class="guide-step-num">1</span>
              <span class="guide-step-title">Open in Chrome or Edge on your Phone</span>
              <p style="margin:4px 0 0 28px; font-size:11px; color:#8ab1b7;">Navigate to this app link on your Android smartphone or tablet.</p>
            </div>

            <div class="guide-step-card">
              <span class="guide-step-num">2</span>
              <span class="guide-step-title">Tap the 3-dot Menu (⋮) in Chrome</span>
              <p style="margin:4px 0 0 28px; font-size:11px; color:#8ab1b7;">Look at the top right corner of Chrome next to the address bar and tap the three vertical dots.</p>
            </div>

            <div class="guide-step-card">
              <span class="guide-step-num">3</span>
              <span class="guide-step-title">Select "Install app" or "Add to Home screen"</span>
              <p style="margin:4px 0 0 28px; font-size:11px; color:#8ab1b7;">Android will package Friday Voice OS with its custom logo into your application drawer alongside your other apps.</p>
            </div>
          `
              : tab === 'capacitor'
              ? `
            <div class="guide-step-card">
              <span class="guide-step-title" style="color:#72e4e7;">📦 Preconfigured Capacitor Architecture</span>
              <p style="margin:4px 0 10px 0; font-size:11px; color:#8ab1b7;">This repository already contains <code>capacitor.config.json</code> with app ID <code>com.friday.voiceos</code>. Follow these commands in terminal to produce a signed APK or bundle:</p>

              <div style="margin-top:8px;"><strong>Step 1: Install Capacitor Android runtime</strong></div>
              <div class="code-box">
                <code>npm install @capacitor/core @capacitor/cli @capacitor/android</code>
                <button class="copy-chip" data-copy="npm install @capacitor/core @capacitor/cli @capacitor/android">COPY</button>
              </div>

              <div style="margin-top:8px;"><strong>Step 2: Build frontend distribution</strong></div>
              <div class="code-box">
                <code>npm run build</code>
                <button class="copy-chip" data-copy="npm run build">COPY</button>
              </div>

              <div style="margin-top:8px;"><strong>Step 3: Generate Android Studio project</strong></div>
              <div class="code-box">
                <code>npx cap add android && npx cap copy</code>
                <button class="copy-chip" data-copy="npx cap add android && npx cap copy">COPY</button>
              </div>

              <div style="margin-top:8px;"><strong>Step 4: Open in Android Studio to build APK</strong></div>
              <div class="code-box">
                <code>npx cap open android</code>
                <button class="copy-chip" data-copy="npx cap open android">COPY</button>
              </div>
              <small style="color:#3ddc84; font-size:10px;">In Android Studio, click <b>Build &gt; Build Bundle(s) / APK(s) &gt; Build APK</b> to generate your installable APK file!</small>
            </div>
          `
              : `
            <div class="guide-step-card">
              <span class="guide-step-title" style="color:#f3b15c;">⚙️ Can Friday access ALL phone applications?</span>
              <p style="margin:6px 0 10px 0; font-size:11px; color:#c6e0e4;">Yes! Here is how Android security and app interaction work:</p>
              
              <div style="margin-bottom:10px;">
                <b style="color:#72e4e7;">1. Deep Links & URI Intents (Active Now)</b>
                <p style="margin:2px 0 0 0; font-size:11px; color:#8ab1b7;">Friday uses Android's intent protocol (<code>whatsapp://</code>, <code>vnd.youtube://</code>, <code>geo:</code>, <code>tel:</code>, <code>sms:</code>) to launch and pass parameters directly to your installed apps.</p>
              </div>

              <div style="margin-bottom:10px;">
                <b style="color:#72e4e7;">2. Android Accessibility Service (In Native APK)</b>
                <p style="margin:2px 0 0 0; font-size:11px; color:#8ab1b7;">When compiled via Capacitor/Android Studio, you can enable an <code>AccessibilityService</code>. This grants permission to read on-screen text, click buttons, and inspect other applications.</p>
              </div>

              <div>
                <b style="color:#72e4e7;">3. NotificationListenerService</b>
                <p style="margin:2px 0 0 0; font-size:11px; color:#8ab1b7;">Allows Friday to read incoming notifications (WhatsApp messages, emails, OTPs) and read them aloud to you hands-free.</p>
              </div>
            </div>
          `
          }
        </div>
      </div>
    </div>
  `

  // Wire event handlers
  androidGuideModalWrap.querySelector('#close-guide-btn')?.addEventListener('click', closeAndroidGuideModal)
  androidGuideModalWrap.querySelector('#guide-overlay')?.addEventListener('click', e => {
    if ((e.target as HTMLElement).id === 'guide-overlay') closeAndroidGuideModal()
  })

  androidGuideModalWrap.querySelectorAll<HTMLButtonElement>('.guide-tab-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      const targetTab = (btn.dataset.tab || 'webapk') as 'webapk' | 'capacitor' | 'permissions'
      renderAndroidGuideModal(targetTab)
      playChime('click')
    })
  })

  androidGuideModalWrap.querySelectorAll<HTMLButtonElement>('.copy-chip').forEach(btn => {
    btn.addEventListener('click', () => {
      const code = btn.dataset.copy || ''
      navigator.clipboard.writeText(code)
      btn.textContent = 'COPIED!'
      setTimeout(() => {
        btn.textContent = 'COPY'
      }, 1800)
      playChime('click')
    })
  })

  androidGuideModalWrap.querySelector('#modal-direct-install-btn')?.addEventListener('click', () => {
    closeAndroidGuideModal()
    triggerInstallPrompt()
  })
}

// ----------------------------------------------------
// Weather Widget Renderer
// ----------------------------------------------------
function renderWeatherCard(w: WeatherData) {
  systemState.lastWeather = w
  const tempF = Math.round((w.temp * 9) / 5 + 32)
  const apparentF = w.apparentTemp !== undefined ? Math.round((w.apparentTemp * 9) / 5 + 32) : tempF

  const conditionIcons: Record<string, string> = {
    'Clear sky': '☀️',
    'Mainly clear': '🌤',
    'Partly cloudy': '⛅',
    'Overcast': '☁️',
    'Fog': '🌫',
    'Drizzle': '🌦',
    'Rain': '🌧',
    'Snow': '❄️',
    'Thunderstorm': '⛈',
  }
  const icon = conditionIcons[w.condition] || '☀️'

  const weatherHtml = `
    <div class="weather-hud-card">
      <div class="weather-header">
        <span class="weather-city">📍 ${w.city}${w.country ? `, ${w.country}` : ''}</span>
        <span class="weather-badge">✦ LIVE OPEN-METEO TOOL</span>
      </div>
      <div class="weather-main-row">
        <span style="font-size:38px;">${icon}</span>
        <div class="weather-temp-huge">${w.temp}°C <small style="font-size:16px;color:#71dbe088;">/ ${tempF}°F</small></div>
        <div class="weather-cond-wrap">
          <span class="weather-cond-title">${w.condition}</span>
          <span class="weather-feels">Feels like ${w.apparentTemp ?? w.temp}°C (${apparentF}°F)</span>
        </div>
      </div>
      <div class="weather-stats-grid">
        <div class="weather-stat-cell">
          <span class="weather-stat-label">HUMIDITY</span>
          <span class="weather-stat-val">${w.humidity ?? 65}%</span>
        </div>
        <div class="weather-stat-cell">
          <span class="weather-stat-label">WIND</span>
          <span class="weather-stat-val">${w.windSpeed ?? 12} km/h</span>
        </div>
        <div class="weather-stat-cell">
          <span class="weather-stat-label">PRECIPITATION</span>
          <span class="weather-stat-val">${w.precipitation ?? 0} mm</span>
        </div>
      </div>
    </div>
  `

  const existing = assistantResponseWrap.querySelector('.weather-hud-card')
  if (existing) existing.remove()
  assistantResponseWrap.insertAdjacentHTML('beforeend', weatherHtml)
}

// ----------------------------------------------------
// Timer Controls
// ----------------------------------------------------
function formatTime(seconds: number): string {
  const m = Math.floor(seconds / 60)
  const s = seconds % 60
  return `${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`
}

function updateTimerUI() {
  timerDigits.textContent = formatTime(systemState.timer.remainingSeconds)
  if (systemState.timer.totalSeconds > 0) {
    const pct = Math.max(0, (systemState.timer.remainingSeconds / systemState.timer.totalSeconds) * 100)
    timerBar.style.width = `${pct}%`
  } else {
    timerBar.style.width = '0%'
  }

  if (systemState.timer.isRunning) {
    timerBadge.textContent = 'RUNNING'
    timerBadge.classList.add('active')
    timerToggleBtn.textContent = 'PAUSE'
  } else if (systemState.timer.remainingSeconds > 0) {
    timerBadge.textContent = 'PAUSED'
    timerBadge.classList.remove('active')
    timerToggleBtn.textContent = 'RESUME'
  } else {
    timerBadge.textContent = 'STANDBY'
    timerBadge.classList.remove('active')
    timerToggleBtn.textContent = 'START'
  }
}

function startTimer(seconds: number, label = 'Voice Timer') {
  if (systemState.timer.intervalId) {
    window.clearInterval(systemState.timer.intervalId)
  }
  systemState.timer.totalSeconds = seconds
  systemState.timer.remainingSeconds = seconds
  systemState.timer.label = label
  systemState.timer.isRunning = true

  updateTimerUI()
  playChime('confirm')

  systemState.timer.intervalId = window.setInterval(() => {
    if (systemState.timer.remainingSeconds > 1) {
      systemState.timer.remainingSeconds--
      updateTimerUI()
    } else {
      // Timer finished!
      systemState.timer.remainingSeconds = 0
      systemState.timer.isRunning = false
      if (systemState.timer.intervalId) {
        window.clearInterval(systemState.timer.intervalId)
        systemState.timer.intervalId = null
      }
      updateTimerUI()
      playChime('alarm')
      speakFriday(`Timer complete! Time is up.`)
      showToast('⏰ Timer finished!')
      history.unshift({
        icon: '⏱',
        title: 'Timer complete',
        detail: `${label} finished`,
        time: 'Just now',
      })
      renderHistory()
    }
  }, 1000)
}

function pauseTimer() {
  if (systemState.timer.isRunning) {
    systemState.timer.isRunning = false
    if (systemState.timer.intervalId) {
      window.clearInterval(systemState.timer.intervalId)
      systemState.timer.intervalId = null
    }
    updateTimerUI()
    showToast('Timer paused')
  }
}

function resumeTimer() {
  if (!systemState.timer.isRunning && systemState.timer.remainingSeconds > 0) {
    systemState.timer.isRunning = true
    updateTimerUI()
    systemState.timer.intervalId = window.setInterval(() => {
      if (systemState.timer.remainingSeconds > 1) {
        systemState.timer.remainingSeconds--
        updateTimerUI()
      } else {
        systemState.timer.remainingSeconds = 0
        systemState.timer.isRunning = false
        if (systemState.timer.intervalId) {
          window.clearInterval(systemState.timer.intervalId)
          systemState.timer.intervalId = null
        }
        updateTimerUI()
        playChime('alarm')
        speakFriday(`Timer complete!`)
        showToast('⏰ Timer finished!')
      }
    }, 1000)
  }
}

function resetTimer() {
  if (systemState.timer.intervalId) {
    window.clearInterval(systemState.timer.intervalId)
    systemState.timer.intervalId = null
  }
  systemState.timer.totalSeconds = 0
  systemState.timer.remainingSeconds = 0
  systemState.timer.isRunning = false
  updateTimerUI()
  showToast('Timer canceled')
}

// ----------------------------------------------------
// Media Controls
// ----------------------------------------------------
function updateMediaUI() {
  const current = systemState.media.tracks[systemState.media.trackIndex]
  trackTitle.textContent = current.title
  trackArtist.textContent = current.artist

  if (systemState.media.isPlaying) {
    mediaBadge.textContent = 'PLAYING'
    mediaBadge.classList.add('active')
    mediaPlayBtn.textContent = '⏸'
    mediaTrackInfo.classList.add('is-playing')
  } else {
    mediaBadge.textContent = 'PAUSED'
    mediaBadge.classList.remove('active')
    mediaPlayBtn.textContent = '▶'
    mediaTrackInfo.classList.remove('is-playing')
  }
}

function togglePlayPause() {
  systemState.media.isPlaying = !systemState.media.isPlaying
  updateMediaUI()
  playChime('click')
  const current = systemState.media.tracks[systemState.media.trackIndex]
  showToast(systemState.media.isPlaying ? `Now playing: ${current.title}` : 'Playback paused')
}

function nextTrack() {
  systemState.media.trackIndex = (systemState.media.trackIndex + 1) % systemState.media.tracks.length
  systemState.media.isPlaying = true
  updateMediaUI()
  playChime('click')
  const current = systemState.media.tracks[systemState.media.trackIndex]
  showToast(`Next track: ${current.title}`)
}

function prevTrack() {
  systemState.media.trackIndex =
    (systemState.media.trackIndex - 1 + systemState.media.tracks.length) % systemState.media.tracks.length
  systemState.media.isPlaying = true
  updateMediaUI()
  playChime('click')
  const current = systemState.media.tracks[systemState.media.trackIndex]
  showToast(`Previous track: ${current.title}`)
}

function setVolume(level: number) {
  systemState.volumeLevel = Math.max(0, Math.min(100, level))
  volumeSlider.value = String(systemState.volumeLevel)
  volumeLabel.textContent = `${systemState.volumeLevel}%`
  hudVolume.textContent = `${systemState.volumeLevel}%`
  const volIcon = document.querySelector<HTMLSpanElement>('#volume-icon')!
  if (systemState.volumeLevel === 0) {
    volIcon.textContent = '🔇'
  } else if (systemState.volumeLevel < 40) {
    volIcon.textContent = '🔉'
  } else {
    volIcon.textContent = '🔊'
  }
}

// ----------------------------------------------------
// Hardware Quick Toggles
// ----------------------------------------------------
function setFlashlight(on: boolean) {
  systemState.flashlightOn = on
  if (on) {
    toggleTorchBtn.classList.add('active')
    flashlightBeam.classList.add('torch-on')
    torchStatusText.textContent = 'Active High-Beam (350 Lumens)'
  } else {
    toggleTorchBtn.classList.remove('active')
    flashlightBeam.classList.remove('torch-on')
    torchStatusText.textContent = 'Deactivated (Dark)'
  }
}

function setBatterySaver(on: boolean) {
  systemState.batterySaver = on
  if (on) {
    toggleBatteryBtn.classList.add('active')
    batteryLabel.textContent = 'SAVER ACTIVE'
  } else {
    toggleBatteryBtn.classList.remove('active')
    batteryLabel.textContent = 'POWER SAVER'
  }
}

function setWifi(on: boolean) {
  systemState.wifiConnected = on
  if (on) {
    toggleWifiBtn.classList.add('active')
    wifiLabel.textContent = 'WI-FI ON'
  } else {
    toggleWifiBtn.classList.remove('active')
    wifiLabel.textContent = 'WI-FI OFF'
  }
}

function setBluetooth(on: boolean) {
  systemState.bluetoothEnabled = on
  if (on) {
    toggleBtBtn.classList.add('active')
    btLabel.textContent = 'BLUETOOTH'
  } else {
    toggleBtBtn.classList.remove('active')
    btLabel.textContent = 'BT OFF'
  }
}

function setDnd(on: boolean) {
  systemState.dndEnabled = on
  if (on) {
    toggleDndBtn.classList.add('active')
    dndLabel.textContent = 'DND ACTIVE'
  } else {
    toggleDndBtn.classList.remove('active')
    dndLabel.textContent = 'DND OFF'
  }
}

// ----------------------------------------------------
// Master Assistant Intent Execution
// ----------------------------------------------------
async function handleCommand(rawCommand: string) {
  const trimmed = rawCommand.trim()
  if (!trimmed) return

  // Provide immediate acoustic and visual feedback
  playChime('listen')
  transcript.textContent = `"${trimmed}"`
  thinkingIndicator.style.display = 'inline'
  reactorCore.classList.add('listening')
  assistantResponseWrap.style.display = 'none'

  try {
    const res = await fetch('/api/assistant', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        command: trimmed,
        systemState: {
          batterySaver: systemState.batterySaver,
          volume: systemState.volumeLevel,
          mediaPlaying: systemState.media.isPlaying,
          flashlight: systemState.flashlightOn,
          wifi: systemState.wifiConnected,
          bluetooth: systemState.bluetoothEnabled,
        },
      }),
    })

    const data = await res.json()
    thinkingIndicator.style.display = 'none'
    reactorCore.classList.remove('listening')

    const result = data.result
    if (!result) {
      throw new Error('No result returned')
    }

    // Clean up previous weather HUD card unless current command is weather
    const existingWeather = assistantResponseWrap.querySelector('.weather-hud-card')
    if (existingWeather && result.intent !== 'WEATHER') {
      existingWeather.remove()
    }

    // Display visual text in the assistant response area
    assistantResponseWrap.style.display = 'flex'
    const rawText = result.displayText || result.spokenResponse
    if (result.intent === 'GENERAL_QA') {
      assistantResponseText.innerHTML = `
        <div style="display:inline-flex;align-items:center;gap:6px;padding:3px 10px;border-radius:999px;background:rgba(192,132,252,0.15);border:1px solid rgba(192,132,252,0.3);color:#d8b4fe;font-size:10px;font-weight:700;letter-spacing:0.06em;margin-bottom:8px;">
          <span style="color:#c084fc;">✦</span> GEMINI 3.6 FLASH · AI REASONING
        </div>
        <div>${rawText}</div>
      `
    } else {
      assistantResponseText.textContent = rawText
    }

    showToast(result.auditTitle || 'Action executed')

    // Execute device action based on identified intent
    let undoFn: (() => void) | undefined = undefined

    switch (result.intent) {
      case 'CALL':
      case 'PHONE_CALL': {
        const contact = result.parameters?.contactName || 'Mom'
        const phone = result.parameters?.phoneNumber || '+1 (555) 019-2834'
        const isSpeaker = Boolean(result.parameters?.speakerphone)
        startCall(contact, phone, isSpeaker)
        undoFn = () => endCall()
        break
      }

      case 'DELETE_FILES':
      case 'CLEAN_CACHE':
      case 'STORAGE_CLEAN': {
        const target = result.parameters?.fileTarget || 'cache'
        deleteByTarget(target)
        break
      }

      case 'WEATHER': {
        if (result.weatherData) {
          renderWeatherCard(result.weatherData)
        }
        break
      }

      case 'TIMER': {
        const sec = result.parameters?.durationSeconds || 300
        if (result.action === 'cancel') {
          resetTimer()
        } else {
          startTimer(sec, result.auditTitle || 'Voice Timer')
          undoFn = () => resetTimer()
        }
        break
      }

      case 'ALARM': {
        const timeStr = result.parameters?.alarmTime || '07:00 AM'
        const label = result.parameters?.alarmLabel || 'Voice Alarm'
        const newAlarm: AlarmItem = {
          id: String(Date.now()),
          time: timeStr,
          label,
          enabled: true,
        }
        systemState.alarms.unshift(newAlarm)
        renderAlarms()
        undoFn = () => {
          const idx = systemState.alarms.findIndex(a => a.id === newAlarm.id)
          if (idx !== -1) systemState.alarms.splice(idx, 1)
          renderAlarms()
        }
        break
      }

      case 'MEDIA': {
        if (result.action === 'pause') {
          systemState.media.isPlaying = false
          updateMediaUI()
        } else if (result.action === 'next') {
          nextTrack()
        } else if (result.action === 'previous') {
          prevTrack()
        } else {
          systemState.media.isPlaying = true
          updateMediaUI()
        }
        break
      }

      case 'FLASHLIGHT': {
        const shouldBeOn = result.action !== 'toggle_off'
        const prev = systemState.flashlightOn
        setFlashlight(shouldBeOn)
        undoFn = () => setFlashlight(prev)
        break
      }

      case 'VOLUME': {
        const prevVol = systemState.volumeLevel
        const newVol = typeof result.parameters?.volumeLevel === 'number' ? result.parameters.volumeLevel : 70
        setVolume(newVol)
        undoFn = () => setVolume(prevVol)
        break
      }

      case 'WIFI': {
        const prev = systemState.wifiConnected
        const turnOn = result.action !== 'toggle_off'
        setWifi(turnOn)
        undoFn = () => setWifi(prev)
        break
      }

      case 'BLUETOOTH': {
        const prev = systemState.bluetoothEnabled
        const turnOn = result.action !== 'toggle_off'
        setBluetooth(turnOn)
        undoFn = () => setBluetooth(prev)
        break
      }

      case 'BATTERY': {
        const prev = systemState.batterySaver
        const turnOn = result.action !== 'toggle_off'
        setBatterySaver(turnOn)
        undoFn = () => setBatterySaver(prev)
        break
      }

      case 'REMINDER': {
        const rTitle = result.parameters?.reminderTitle || 'Reminder'
        const rTime = result.parameters?.reminderTime || 'Today · 7:00 PM'
        history.unshift({
          icon: '◷',
          title: `Reminder: ${rTitle}`,
          detail: rTime,
          time: 'Just now',
          undoAction: () => showToast('Reminder removed'),
        })
        renderHistory()
        break
      }

      case 'OPEN_APP': {
        const appName = result.parameters?.appName || 'whatsapp'
        const query = result.parameters?.query
        launchAndroidApp(appName, query)
        break
      }

      case 'INSTALL_APP': {
        triggerInstallPrompt()
        break
      }

      case 'GENERAL_QA':
      default: {
        // Conversational / Gemini AI response handled via speech and display
        break
      }
    }

    // Add entry to Audit Trail history
    if (result.intent !== 'REMINDER') {
      history.unshift({
        icon: result.intent === 'GENERAL_QA' ? '✦' : '✓',
        title: result.auditTitle || 'Voice Command',
        detail: result.auditDetail || trimmed,
        time: 'Just now',
        undoAction: undoFn,
      })
      renderHistory()
    }

    // Speak Friday's synthesized response via TTS
    if (result.spokenResponse) {
      speakFriday(result.spokenResponse)
    }
  } catch (err: unknown) {
    console.error('Command handling error:', err)
    thinkingIndicator.style.display = 'none'
    reactorCore.classList.remove('listening')
    const fallbackMessage = "I processed your request using local device rules."
    assistantResponseWrap.style.display = 'flex'
    assistantResponseText.textContent = fallbackMessage
    speakFriday(fallbackMessage)
    showToast('Local command execution')
  }
}

// ----------------------------------------------------
// Voice Recognition Setup
// ----------------------------------------------------
type Recognition = {
  continuous: boolean
  interimResults: boolean
  lang: string
  start: () => void
  stop: () => void
  onstart: (() => void) | null
  onresult: ((event: { results: ArrayLike<ArrayLike<{ transcript: string }>> }) => void) | null
  onend: (() => void) | null
  onerror: ((event: { error: string }) => void) | null
}

const recognitionWindow = window as Window & {
  SpeechRecognition?: new () => Recognition
  webkitSpeechRecognition?: new () => Recognition
}
const SpeechRecognition = recognitionWindow.SpeechRecognition ?? recognitionWindow.webkitSpeechRecognition
let activeRecognition: Recognition | null = null
let isListening = false

function initSpeechRecognition() {
  if (!SpeechRecognition) {
    micBtnLabel.textContent = 'TYPE A COMMAND BELOW'
    return
  }

  const rec = new SpeechRecognition()
  rec.continuous = false
  rec.interimResults = false
  rec.lang = 'en-US'

  rec.onstart = () => {
    isListening = true
    micBtn.classList.add('is-listening')
    micBtnLabel.textContent = 'LISTENING... SPEAK NOW'
    reactorCore.classList.add('listening')
    const statusLabel = document.querySelector<HTMLSpanElement>('#listen-status-label')
    if (statusLabel) statusLabel.textContent = 'MICROPHONE OPEN · LISTENING'
  }

  rec.onresult = event => {
    const speechResult = event.results[event.results.length - 1][0].transcript.trim()
    if (speechResult) {
      handleCommand(speechResult)
    }
  }

  rec.onerror = event => {
    console.warn('Speech recognition status:', event.error)
    isListening = false
    micBtn.classList.remove('is-listening')
    micBtnLabel.textContent = 'TAP TO SPEAK COMMAND'
    reactorCore.classList.remove('listening')
  }

  rec.onend = () => {
    isListening = false
    micBtn.classList.remove('is-listening')
    micBtnLabel.textContent = 'TAP TO SPEAK COMMAND'
    reactorCore.classList.remove('listening')
    const statusLabel = document.querySelector<HTMLSpanElement>('#listen-status-label')
    if (statusLabel) statusLabel.textContent = 'FRIDAY INTELLIGENCE · READY'
  }

  activeRecognition = rec
}

function toggleListening() {
  if (!activeRecognition) {
    initSpeechRecognition()
  }

  if (!activeRecognition) {
    showToast('Speech recognition unavailable in this browser. You can type commands directly.')
    commandInput.focus()
    return
  }

  if (isListening) {
    activeRecognition.stop()
  } else {
    try {
      activeRecognition.start()
      playChime('listen')
    } catch {
      // If already started or pending
      activeRecognition.stop()
    }
  }
}

// ----------------------------------------------------
// Event Listeners Binding
// ----------------------------------------------------

// Form command submission
commandForm.addEventListener('submit', e => {
  e.preventDefault()
  const val = commandInput.value.trim()
  if (val) {
    handleCommand(val)
    commandInput.value = ''
  }
})

// Mic button toggle
micBtn.addEventListener('click', () => {
  toggleListening()
})

// Speech synthesis mute toggle
speechToggleBtn.addEventListener('click', () => {
  systemState.isSpeechMuted = !systemState.isSpeechMuted
  if (systemState.isSpeechMuted) {
    window.speechSynthesis?.cancel()
    speechToggleBtn.classList.add('muted')
    speechIcon.textContent = '🔇'
    speechLabel.textContent = 'VOICE OFF'
    showToast('Friday voice output muted')
  } else {
    speechToggleBtn.classList.remove('muted')
    speechIcon.textContent = '🔊'
    speechLabel.textContent = 'VOICE ON'
    showToast('Friday voice output unmuted')
    speakFriday('Friday voice audio enabled.')
  }
})

// Quick Command preset buttons
document.querySelectorAll<HTMLButtonElement>('.command').forEach(btn => {
  btn.addEventListener('click', () => {
    const cmd = btn.dataset.command
    if (cmd) handleCommand(cmd)
  })
})

// Timer buttons
timerToggleBtn.addEventListener('click', () => {
  if (systemState.timer.isRunning) {
    pauseTimer()
  } else if (systemState.timer.remainingSeconds > 0) {
    resumeTimer()
  } else {
    startTimer(300, '5 Min Timer')
  }
})

timerResetBtn.addEventListener('click', () => {
  resetTimer()
})

document.querySelectorAll<HTMLButtonElement>('.quick-timer-chip').forEach(chip => {
  chip.addEventListener('click', () => {
    const sec = Number(chip.dataset.seconds)
    startTimer(sec, `${Math.round(sec / 60)}m Timer`)
  })
})

// Media buttons
mediaPlayBtn.addEventListener('click', togglePlayPause)
mediaPrevBtn.addEventListener('click', prevTrack)
mediaNextBtn.addEventListener('click', nextTrack)

volumeSlider.addEventListener('input', () => {
  setVolume(Number(volumeSlider.value))
})

// Hardware Switches
toggleTorchBtn.addEventListener('click', () => {
  setFlashlight(!systemState.flashlightOn)
  playChime('click')
  showToast(systemState.flashlightOn ? 'Flashlight turned on' : 'Flashlight turned off')
})

toggleWifiBtn.addEventListener('click', () => {
  setWifi(!systemState.wifiConnected)
  playChime('click')
  showToast(systemState.wifiConnected ? 'Wi-Fi linked' : 'Wi-Fi disconnected')
})

toggleBtBtn.addEventListener('click', () => {
  setBluetooth(!systemState.bluetoothEnabled)
  playChime('click')
  showToast(systemState.bluetoothEnabled ? 'Bluetooth enabled' : 'Bluetooth disabled')
})

toggleBatteryBtn.addEventListener('click', () => {
  setBatterySaver(!systemState.batterySaver)
  playChime('click')
  showToast(systemState.batterySaver ? 'Battery saver on' : 'Performance mode restored')
})

toggleDndBtn.addEventListener('click', () => {
  setDnd(!systemState.dndEnabled)
  playChime('click')
  showToast(systemState.dndEnabled ? 'Do Not Disturb active' : 'Do Not Disturb off')
})

document.querySelector<HTMLButtonElement>('#clean-storage-btn')?.addEventListener('click', () => {
  deleteByTarget('cache')
})

// Phone tab buttons
tabBtnContacts.addEventListener('click', () => {
  systemState.activePhoneTab = 'contacts'
  renderPhoneDeck()
  playChime('click')
})

tabBtnDialpad.addEventListener('click', () => {
  systemState.activePhoneTab = 'dialpad'
  renderPhoneDeck()
  playChime('click')
})

tabBtnRecents.addEventListener('click', () => {
  systemState.activePhoneTab = 'recents'
  renderPhoneDeck()
  playChime('click')
})

// Storage filter chips & bulk actions
document.querySelectorAll<HTMLButtonElement>('.file-chip').forEach(chip => {
  chip.addEventListener('click', () => {
    document.querySelectorAll('.file-chip').forEach(c => c.classList.remove('active'))
    chip.classList.add('active')
    const filter = (chip.dataset.filter || 'all') as typeof systemState.storage.activeFilter
    systemState.storage.activeFilter = filter
    renderStorageDeck()
    playChime('click')
  })
})

fileSelectAllBtn.addEventListener('click', () => {
  const allSelected = systemState.storage.files.every(f => f.selected)
  systemState.storage.files.forEach(f => {
    f.selected = !allSelected
  })
  renderStorageDeck()
  playChime('click')
})

fileDeleteBulkBtn.addEventListener('click', () => {
  deleteSelectedFiles()
})

document.querySelector<HTMLButtonElement>('#quick-add-alarm-btn')?.addEventListener('click', () => {
  const customTime = window.prompt('Enter alarm time (e.g. 06:45 AM):', '06:30 AM')
  if (customTime) {
    const newAlarm: AlarmItem = {
      id: String(Date.now()),
      time: customTime,
      label: 'Custom Alarm',
      enabled: true,
    }
    systemState.alarms.push(newAlarm)
    renderAlarms()
    playChime('confirm')
    showToast(`Alarm added for ${customTime}`)
  }
})

document.querySelector<HTMLButtonElement>('#clear-history-btn')?.addEventListener('click', () => {
  history.length = 0
  renderHistory()
  showToast('Audit trail cleared')
})

// Android & App Launcher Event Listeners
installAndroidBtn?.addEventListener('click', () => {
  triggerInstallPrompt()
  playChime('click')
})

androidGuideBtn?.addEventListener('click', () => {
  openAndroidGuideModal('webapk')
  playChime('click')
})

runIntentBtn?.addEventListener('click', () => {
  const uri = customIntentInput.value.trim()
  if (uri) {
    launchAndroidApp(uri)
    customIntentInput.value = ''
  } else {
    showToast('Enter an app package or URI scheme first')
  }
})

customIntentInput?.addEventListener('keydown', (e: KeyboardEvent) => {
  if (e.key === 'Enter') {
    e.preventDefault()
    runIntentBtn?.click()
  }
})

// Initial Render
renderHistory()
renderPermissions()
renderAlarms()
renderPhoneDeck()
renderStorageDeck()
renderAppShortcuts()
updateTimerUI()
updateMediaUI()
setVolume(systemState.volumeLevel)
initSpeechRecognition()
initPwaInstall()
