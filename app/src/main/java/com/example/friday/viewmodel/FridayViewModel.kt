package com.example.friday.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.AlarmClock
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.friday.model.ActionItem
import com.example.friday.model.ChatMessage
import com.example.friday.model.ContactInfo
import com.example.friday.model.PermissionItem
import com.example.friday.model.Speaker
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale

data class FridayUiState(
    val isListening: Boolean = false,
    val isSpeaking: Boolean = false,
    val transcript: String = "“Hey Friday, status report”",
    val isProcessing: Boolean = false,
    val reactorCorePower: Int = 100,
    val armorStatus: String = "MARK LXXXV - NOMINAL",
    val cpuTemp: String = "36.8°C",
    val securityStatus: String = "STARK ENCRYPTED",
    val latencyMs: Int = 8,
    val batteryLevel: Int = 88,
    val isCharging: Boolean = false,
    val isTorchOn: Boolean = false,
    val freeStorageGb: Double = 34.2,
    val totalStorageGb: Double = 128.0,
    val contactsList: List<ContactInfo> = emptyList(),
    val chatMessages: List<ChatMessage> = emptyList(),
    val history: List<ActionItem> = emptyList(),
    val permissions: List<PermissionItem> = emptyList(),
    val allPermissionsGranted: Boolean = false,
    val showPermissionModal: Boolean = false,
    val toastMessage: String? = null,
    val showSettings: Boolean = false,
    val showClearHistoryDialog: Boolean = false,
    val audioLevel: Float = 0f,
    val continuousListening: Boolean = true,
    val speechRate: Float = 1.05f,
    val speechPitch: Float = 1.15f,
    val hapticsEnabled: Boolean = true,
    val voiceMuted: Boolean = false
)

class FridayViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val context: Context get() = getApplication<Application>().applicationContext
    private val _uiState = MutableStateFlow(FridayUiState())
    val uiState: StateFlow<FridayUiState> = _uiState.asStateFlow()

    private var speechRecognizer: SpeechRecognizer? = null
    private var textToSpeech: TextToSpeech? = null
    private var isTtsReady = false
    private var audioSimJob: Job? = null
    private var cameraManager: CameraManager? = null
    private var cameraIdWithFlash: String? = null

    init {
        initializePermissionsList()
        initializeDefaultHistory()
        initCameraFlashlight()
        readBatteryAndStorage()
        initTextToSpeech()
        startTelemetryLoop()

        // Welcome greeting from Friday
        viewModelScope.launch {
            delay(800)
            checkInitialPermissions()
            val welcomeText = "Good day, Boss. F.R.I.D.A.Y. online. All Mark eighty-five telemetry linked. What are your orders?"
            addMessage(Speaker.FRIDAY, welcomeText)
            speak(welcomeText)
        }
    }

    private fun initializePermissionsList() {
        val list = listOf(
            PermissionItem(
                id = "audio",
                protocolCode = "STARK-AUD-01",
                name = "Audio Array & Speech Capture",
                purpose = "Continuous acoustic monitoring and autonomous voice comprehension",
                icon = "◉",
                permissions = listOf(android.Manifest.permission.RECORD_AUDIO),
                isGranted = ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
            ),
            PermissionItem(
                id = "contacts",
                protocolCode = "STARK-NET-02",
                name = "Neural Contact Directory",
                purpose = "Access phone numbers, identities, and autonomous call sequencing",
                icon = "◎",
                permissions = listOf(android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.CALL_PHONE),
                isGranted = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
            ),
            PermissionItem(
                id = "storage",
                protocolCode = "STARK-MEM-03",
                name = "Storage & Memory Matrix",
                purpose = "Local file telemetry, cache diagnostics, and media access",
                icon = "▣",
                permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    listOf(
                        android.Manifest.permission.READ_MEDIA_IMAGES,
                        android.Manifest.permission.READ_MEDIA_AUDIO,
                        android.Manifest.permission.READ_MEDIA_VIDEO
                    )
                } else {
                    listOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                },
                isGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                } else {
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                }
            ),
            PermissionItem(
                id = "camera",
                protocolCode = "STARK-OPT-04",
                name = "Optical Sensors & Flashlight",
                purpose = "Camera diagnostic feeds and arc torch illuminator toggle",
                icon = "⌖",
                permissions = listOf(android.Manifest.permission.CAMERA),
                isGranted = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            ),
            PermissionItem(
                id = "location",
                protocolCode = "STARK-GEO-05",
                name = "Orbital Geo-Telemetry",
                purpose = "Global positioning, atmospheric navigation, and locale context",
                icon = "⌖",
                permissions = listOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                isGranted = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            ),
            PermissionItem(
                id = "notifications",
                protocolCode = "STARK-HUD-06",
                name = "HUD Notifications & Alarms",
                purpose = "Priority mission reminders and background telemetry alerts",
                icon = "◇",
                permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    listOf(android.Manifest.permission.POST_NOTIFICATIONS)
                } else emptyList(),
                isGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                } else true
            )
        )

        val allGranted = list.all { it.isGranted }
        _uiState.update { it.copy(permissions = list, allPermissionsGranted = allGranted) }
    }

    private fun initializeDefaultHistory() {
        val initialHistory = listOf(
            ActionItem(
                icon = "⚡",
                title = "Mark LXXXV Telemetry Linked",
                detail = "Stark security handshake established",
                time = "Just now",
                canUndo = false
            ),
            ActionItem(
                icon = "⌁",
                title = "Arc Core Diagnostics Verified",
                detail = "Subsystem power levels 100% nominal",
                time = "2m ago",
                canUndo = false
            )
        )
        _uiState.update { it.copy(history = initialHistory) }
    }

    private fun checkInitialPermissions() {
        initializePermissionsList()
        val missing = _uiState.value.permissions.filter { !it.isGranted }
        if (missing.isNotEmpty()) {
            val msg = "Boss, I require authorization to link with your device access points — contacts, files, audio array, and hardware sensors."
            addMessage(Speaker.FRIDAY, msg, "PERMISSION_PROMPT")
        }
    }

    private fun initTextToSpeech() {
        try {
            textToSpeech = TextToSpeech(context, this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech?.let { tts ->
                val result = tts.setLanguage(Locale.US)
                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    isTtsReady = true
                    tts.setPitch(_uiState.value.speechPitch)
                    tts.setSpeechRate(_uiState.value.speechRate)

                    tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {
                            _uiState.update { it.copy(isSpeaking = true) }
                            startAudioWaveformSimulation()
                        }

                        override fun onDone(utteranceId: String?) {
                            _uiState.update { it.copy(isSpeaking = false) }
                            stopAudioWaveformSimulation()
                            if (_uiState.value.continuousListening) {
                                viewModelScope.launch {
                                    delay(400)
                                    startListening()
                                }
                            }
                        }

                        @Deprecated("Deprecated in Java")
                        override fun onError(utteranceId: String?) {
                            _uiState.update { it.copy(isSpeaking = false) }
                            stopAudioWaveformSimulation()
                        }
                    })
                }
            }
        }
    }

    fun speak(text: String) {
        if (_uiState.value.voiceMuted) return
        if (isTtsReady && textToSpeech != null) {
            val bundle = Bundle().apply {
                putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
            }
            textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, bundle, "FRIDAY_UTTERANCE_${System.currentTimeMillis()}")
        }
    }

    private fun initCameraFlashlight() {
        try {
            cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
            cameraManager?.let { manager ->
                for (id in manager.cameraIdList) {
                    val chars = manager.getCameraCharacteristics(id)
                    val hasFlash = chars.get(android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
                    if (hasFlash) {
                        cameraIdWithFlash = id
                        break
                    }
                }
            }
        } catch (_: Exception) {}
    }

    fun toggleFlashlight() {
        val current = _uiState.value.isTorchOn
        val target = !current
        setFlashlightState(target)
    }

    private fun setFlashlightState(enable: Boolean) {
        try {
            cameraIdWithFlash?.let { id ->
                cameraManager?.setTorchMode(id, enable)
                _uiState.update { it.copy(isTorchOn = enable) }
                vibrateFeedback(40)
                val statusText = if (enable) "Arc Torch activated, Boss." else "Arc Torch disengaged."
                addMessage(Speaker.FRIDAY, statusText)
                speak(statusText)
                logAction("⚡", if (enable) "Torch Activated" else "Torch Deactivated", "Optical flash node toggled", true)
            } ?: run {
                val fallbackMsg = if (enable) "Flashlight simulation active (Torch hardware not detected)." else "Flashlight turned off."
                _uiState.update { it.copy(isTorchOn = enable) }
                addMessage(Speaker.FRIDAY, fallbackMsg)
                speak(fallbackMsg)
            }
        } catch (e: CameraAccessException) {
            val errorMsg = "Unable to access optical torch node, Boss."
            addMessage(Speaker.FRIDAY, errorMsg)
            speak(errorMsg)
        }
    }

    fun queryContacts(): List<ContactInfo> {
        val contacts = mutableListOf<ContactInfo>()
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            try {
                val cr = context.contentResolver
                val cursor = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    arrayOf(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
                    ),
                    null,
                    null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
                )

                cursor?.use {
                    val nameIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    val numIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val keyIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY)

                    while (it.moveToNext() && contacts.size < 20) {
                        val name = if (nameIdx != -1) it.getString(nameIdx) else "Unknown"
                        val number = if (numIdx != -1) it.getString(numIdx) else ""
                        val key = if (keyIdx != -1) it.getString(keyIdx) else null
                        if (name.isNotBlank()) {
                            contacts.add(ContactInfo(name = name, number = number, lookupKey = key))
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (contacts.isEmpty()) {
            contacts.addAll(
                listOf(
                    ContactInfo("Pepper Potts", "+1 (555) 019-2834"),
                    ContactInfo("James Rhodes (War Machine)", "+1 (555) 014-9821"),
                    ContactInfo("Happy Hogan", "+1 (555) 012-7634"),
                    ContactInfo("Peter Parker", "+1 (555) 017-4390"),
                    ContactInfo("Bruce Banner", "+1 (555) 018-3829")
                )
            )
        }

        _uiState.update { it.copy(contactsList = contacts) }
        return contacts
    }

    private fun readBatteryAndStorage() {
        try {
            val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus = context.registerReceiver(null, ifilter)
            val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL

            val batteryPct = if (level >= 0 && scale > 0) ((level / scale.toFloat()) * 100).toInt() else 88

            val stat = StatFs(Environment.getDataDirectory().path)
            val bytesAvailable = stat.availableBlocksLong * stat.blockSizeLong
            val totalBytes = stat.blockCountLong * stat.blockSizeLong
            val freeGb = (bytesAvailable / (1024.0 * 1024.0 * 1024.0))
            val totalGb = (totalBytes / (1024.0 * 1024.0 * 1024.0))

            _uiState.update {
                it.copy(
                    batteryLevel = batteryPct,
                    isCharging = isCharging,
                    freeStorageGb = String.format(Locale.US, "%.1f", freeGb).toDoubleOrNull() ?: 34.2,
                    totalStorageGb = String.format(Locale.US, "%.1f", totalGb).toDoubleOrNull() ?: 128.0
                )
            }
        } catch (_: Exception) {}
    }

    private fun startTelemetryLoop() {
        viewModelScope.launch {
            while (isActive) {
                delay(3500)
                val temp = 36.2f + (Math.random().toFloat() * 1.5f)
                val latency = 6 + (Math.random() * 8).toInt()
                _uiState.update {
                    it.copy(
                        cpuTemp = String.format(Locale.US, "%.1f°C", temp),
                        latencyMs = latency
                    )
                }
            }
        }
    }

    fun startListening() {
        if (_uiState.value.isSpeaking) {
            textToSpeech?.stop()
            _uiState.update { it.copy(isSpeaking = false) }
        }

        _uiState.update {
            it.copy(
                isListening = true,
                transcript = "Friday is listening... Speak now, Boss."
            )
        }

        vibrateFeedback(30)
        startAudioWaveformSimulation()

        try {
            if (SpeechRecognizer.isRecognitionAvailable(context)) {
                if (speechRecognizer == null) {
                    speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
                }
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                    putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                    putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                }

                speechRecognizer?.setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {}
                    override fun onBeginningOfSpeech() {
                        _uiState.update { it.copy(transcript = "Detecting voice input...") }
                    }
                    override fun onRmsChanged(rmsdB: Float) {
                        val level = (rmsdB / 10f).coerceIn(0.15f, 1.0f)
                        _uiState.update { it.copy(audioLevel = level) }
                    }
                    override fun onBufferReceived(buffer: ByteArray?) {}
                    override fun onEndOfSpeech() {
                        _uiState.update { it.copy(isListening = false, isProcessing = true) }
                    }
                    override fun onError(error: Int) {
                        _uiState.update {
                            it.copy(
                                isListening = false,
                                isProcessing = false,
                                transcript = "Audio standby. Tap mic or core reactor to speak."
                            )
                        }
                        stopAudioWaveformSimulation()
                    }
                    override fun onResults(results: Bundle?) {
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val text = matches?.firstOrNull()
                        if (!text.isNullOrBlank()) {
                            executeUserSpeech(text)
                        } else {
                            _uiState.update { it.copy(isListening = false, isProcessing = false) }
                        }
                        stopAudioWaveformSimulation()
                    }
                    override fun onPartialResults(partialResults: Bundle?) {
                        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val text = matches?.firstOrNull()
                        if (!text.isNullOrBlank()) {
                            _uiState.update { it.copy(transcript = "“$text”") }
                        }
                    }
                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })

                speechRecognizer?.startListening(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
        } catch (_: Exception) {}
        stopAudioWaveformSimulation()
        _uiState.update { it.copy(isListening = false, isProcessing = false) }
    }

    fun toggleListening() {
        if (_uiState.value.isListening) {
            stopListening()
        } else {
            startListening()
        }
    }

    private fun startAudioWaveformSimulation() {
        audioSimJob?.cancel()
        audioSimJob = viewModelScope.launch {
            while (isActive) {
                delay(70)
                val randomLevel = (0.25f + Math.random().toFloat() * 0.75f)
                _uiState.update { it.copy(audioLevel = randomLevel) }
            }
        }
    }

    private fun stopAudioWaveformSimulation() {
        audioSimJob?.cancel()
        audioSimJob = null
        _uiState.update { it.copy(audioLevel = 0.05f) }
    }

    fun executeUserSpeech(speech: String) {
        val command = speech.trim()
        if (command.isBlank()) return

        stopListening()
        addMessage(Speaker.USER, command)
        _uiState.update { it.copy(transcript = "“$command”", isProcessing = true) }

        viewModelScope.launch {
            delay(250) // Cybernetic HUD processing pulse
            _uiState.update { it.copy(isProcessing = false) }
            processAutonomousCommand(command)
        }
    }

    private fun processAutonomousCommand(raw: String) {
        val cmd = raw.lowercase(Locale.ROOT)
        vibrateFeedback(45)

        when {
            // Permission / System Handshake
            cmd.contains("permission") || cmd.contains("access") || cmd.contains("handshake") || cmd.contains("authorize") || cmd.contains("grant") -> {
                val reply = "Initiating Stark Protocol 01. Linking full phone privileges right away, Boss."
                addMessage(Speaker.FRIDAY, reply, "PERMISSION_REQUEST")
                speak(reply)
                _uiState.update { it.copy(showPermissionModal = true) }
                logAction("🛡", "System Permissions Protocol", "Authorization handshake initiated", false)
            }

            // Flashlight / Torch
            cmd.contains("flashlight") || cmd.contains("torch") || cmd.contains("light") -> {
                val turnOn = !cmd.contains("off") && !cmd.contains("disable")
                setFlashlightState(turnOn)
            }

            // Contacts / Call
            cmd.contains("contact") || cmd.contains("call") || cmd.contains("phone number") || cmd.contains("who is") -> {
                val contacts = queryContacts()
                val targetName = extractTargetName(cmd)

                if (targetName != null) {
                    val match = contacts.firstOrNull { it.name.lowercase().contains(targetName.lowercase()) }
                    if (match != null) {
                        val reply = "Found ${match.name}: ${match.number}. Initiating comms protocol."
                        addMessage(Speaker.FRIDAY, reply)
                        speak(reply)
                        launchDialer(match.number)
                        logAction("◎", "Comms Link: ${match.name}", "Dialer protocol triggered for ${match.number}", true)
                    } else {
                        val reply = "I couldn't locate $targetName in the directory, Boss. Showing all active contacts."
                        addMessage(Speaker.FRIDAY, reply)
                        speak(reply)
                    }
                } else {
                    val count = contacts.size
                    val top3 = contacts.take(3).joinToString(", ") { it.name }
                    val reply = "Directory active with $count entries linked including $top3. Who shall I call, Boss?"
                    addMessage(Speaker.FRIDAY, reply)
                    speak(reply)
                    logAction("◎", "Contacts Directory Queried", "$count records verified", false)
                }
            }

            // Battery / Power
            cmd.contains("battery") || cmd.contains("power") || cmd.contains("charge") || cmd.contains("energy") -> {
                readBatteryAndStorage()
                val lvl = _uiState.value.batteryLevel
                val charging = if (_uiState.value.isCharging) "charging rapidly" else "running on internal arc power"
                val reply = "Battery is at $lvl percent, $charging. Mark LXXXV auxiliary reserves nominal."
                addMessage(Speaker.FRIDAY, reply)
                speak(reply)
                logAction("⚡", "Battery Telemetry Check", "Charge level: $lvl%", false)
            }

            // Files / Storage / Clean Cache
            cmd.contains("file") || cmd.contains("storage") || cmd.contains("memory") || cmd.contains("junk") || cmd.contains("clean") -> {
                readBatteryAndStorage()
                val free = _uiState.value.freeStorageGb
                val total = _uiState.value.totalStorageGb
                val reply = "Storage telemetry: $free GB free of $total GB total. Local memory matrix is fully optimized, Boss."
                addMessage(Speaker.FRIDAY, reply)
                speak(reply)
                logAction("▣", "Storage Matrix Scan", "$free GB available", true)
            }

            // Launch Camera / Optical
            cmd.contains("camera") || cmd.contains("photo") || cmd.contains("picture") -> {
                val reply = "Opening optical imaging sensors now, Boss."
                addMessage(Speaker.FRIDAY, reply)
                speak(reply)
                launchCameraApp()
                logAction("⌖", "Camera Sensor Activated", "Optical subsystem launched", false)
            }

            // Reminder / Alarm
            cmd.contains("remind") || cmd.contains("alarm") || cmd.contains("timer") -> {
                val reply = "Setting protocol reminder: $raw."
                addMessage(Speaker.FRIDAY, reply)
                speak(reply)
                launchAlarmApp(raw)
                logAction("◷", "Alarm Protocol Queued", raw, true)
            }

            // Status / Diagnostics / Mark LXXXV
            cmd.contains("status") || cmd.contains("report") || cmd.contains("diagnostic") || cmd.contains("health") -> {
                val reply = "Armor Status: Mark LXXXV Nominal. Arc Reactor at 100%. CPU temperature ${_uiState.value.cpuTemp}. Security protocol: Stark Encrypted."
                addMessage(Speaker.FRIDAY, reply)
                speak(reply)
                logAction("◈", "Full Diagnostic Report", "All subsystem parameters nominal", false)
            }

            // Identity / Easter eggs
            cmd.contains("who are you") || cmd.contains("your name") || cmd.contains("friday") -> {
                val reply = "I am F.R.I.D.A.Y. — Female Replacement Intelligent Digital Assistant Youth, created by Stark Industries to serve you, Boss."
                addMessage(Speaker.FRIDAY, reply)
                speak(reply)
            }

            cmd.contains("joke") || cmd.contains("funny") -> {
                val jokes = listOf(
                    "I asked Jarvis why he didn't upgrade his armor. He said he couldn't find a good patch.",
                    "Why did Tony Stark love Kotlin? Because it prevents null pointer crashes mid-flight.",
                    "Ultron thought he had all the power, until Boss showed him what an Arc Reactor actually does."
                )
                val chosen = jokes.random()
                addMessage(Speaker.FRIDAY, chosen)
                speak(chosen)
            }

            cmd.contains("open settings") || cmd.contains("setting") -> {
                val reply = "Opening system configuration deck, Boss."
                addMessage(Speaker.FRIDAY, reply)
                speak(reply)
                _uiState.update { it.copy(showSettings = true) }
            }

            // Generic smart handler
            else -> {
                val reply = "Command acknowledged, Boss: “$raw”. Subsystems dispatched and logged."
                addMessage(Speaker.FRIDAY, reply)
                speak(reply)
                logAction("⚡", "Autonomous Execution", raw, true)
            }
        }
    }

    private fun extractTargetName(cmd: String): String? {
        val keywords = listOf("call ", "contact ", "reach ", "dial ", "phone ")
        for (kw in keywords) {
            val idx = cmd.indexOf(kw)
            if (idx != -1) {
                val candidate = cmd.substring(idx + kw.length).trim()
                if (candidate.isNotBlank()) return candidate
            }
        }
        return null
    }

    private fun launchDialer(phone: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${phone.replace(" ", "")}")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (_: Exception) {}
    }

    private fun launchCameraApp() {
        try {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (_: Exception) {}
    }

    private fun launchAlarmApp(note: String) {
        try {
            val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, note)
                putExtra(AlarmClock.EXTRA_HOUR, 7)
                putExtra(AlarmClock.EXTRA_MINUTES, 0)
                putExtra(AlarmClock.EXTRA_SKIP_UI, false)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            // Fallback to clock or settings
        }
    }

    fun addMessage(speaker: Speaker, text: String, actionType: String? = null) {
        val msg = ChatMessage(
            speaker = speaker,
            text = text,
            systemActionType = actionType
        )
        _uiState.update {
            it.copy(chatMessages = it.chatMessages + msg)
        }
    }

    fun logAction(icon: String, title: String, detail: String, canUndo: Boolean) {
        val item = ActionItem(
            icon = icon,
            title = title,
            detail = detail,
            time = "Just now",
            canUndo = canUndo
        )
        _uiState.update {
            it.copy(history = listOf(item) + it.history)
        }
    }

    fun undoAction(item: ActionItem) {
        _uiState.update { state ->
            val updated = state.history.map {
                if (it.id == item.id) {
                    it.copy(title = "${it.title} (Reverted)", canUndo = false)
                } else it
            }
            state.copy(
                history = updated,
                toastMessage = "Stark Protocol: Action “${item.title}” reversed."
            )
        }
        val reply = "Action reversed, Boss."
        speak(reply)
    }

    fun onPermissionsResult(permissionsMap: Map<String, Boolean>) {
        initializePermissionsList()
        val allGranted = _uiState.value.permissions.all { it.isGranted }
        val grantedCount = _uiState.value.permissions.count { it.isGranted }
        val totalCount = _uiState.value.permissions.size

        val feedback = if (allGranted) {
            "All device access nodes established! Mark LXXXV is at full autonomous capability, Boss."
        } else {
            "Access updated: $grantedCount of $totalCount protocols linked. Full autonomy standing by."
        }

        addMessage(Speaker.FRIDAY, feedback)
        speak(feedback)
        _uiState.update {
            it.copy(
                allPermissionsGranted = allGranted,
                showPermissionModal = !allGranted
            )
        }
        logAction("🛡", "Device Link Handshake", "$grantedCount/$totalCount nodes operational", false)
    }

    fun refreshAllAccess() {
        initializePermissionsList()
        queryContacts()
        readBatteryAndStorage()
    }

    fun setShowPermissionModal(show: Boolean) {
        _uiState.update { it.copy(showPermissionModal = show) }
    }

    fun setShowSettings(show: Boolean) {
        _uiState.update { it.copy(showSettings = show) }
    }

    fun setShowClearHistoryDialog(show: Boolean) {
        _uiState.update { it.copy(showClearHistoryDialog = show) }
    }

    fun clearAllHistory() {
        _uiState.update {
            it.copy(
                history = emptyList(),
                showClearHistoryDialog = false,
                toastMessage = "Audit trail wiped from local core."
            )
        }
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun updateContinuousListening(enabled: Boolean) {
        _uiState.update { it.copy(continuousListening = enabled) }
    }

    fun updateSpeechRate(rate: Float) {
        _uiState.update { it.copy(speechRate = rate) }
        textToSpeech?.setSpeechRate(rate)
    }

    fun updateSpeechPitch(pitch: Float) {
        _uiState.update { it.copy(speechPitch = pitch) }
        textToSpeech?.setPitch(pitch)
    }

    fun updateHaptics(enabled: Boolean) {
        _uiState.update { it.copy(hapticsEnabled = enabled) }
    }

    fun toggleVoiceMuted() {
        val newMute = !_uiState.value.voiceMuted
        _uiState.update { it.copy(voiceMuted = newMute) }
        if (newMute) {
            textToSpeech?.stop()
            _uiState.update { it.copy(isSpeaking = false) }
        } else {
            speak("Audio speech synthesis unmuted, Boss.")
        }
    }

    private fun vibrateFeedback(durationMs: Long) {
        if (!_uiState.value.hapticsEnabled) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(durationMs)
                }
            }
        } catch (_: Exception) {}
    }

    override fun onCleared() {
        super.onCleared()
        speechRecognizer?.destroy()
        speechRecognizer = null
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
        audioSimJob?.cancel()
    }
}
