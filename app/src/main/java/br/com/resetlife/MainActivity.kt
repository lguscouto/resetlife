package br.com.resetlife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.resetlife.ResetLifeApplication
import br.com.resetlife.presentation.ResetLifeApp
import br.com.resetlife.presentation.theme.ResetLifeTheme
import br.com.resetlife.presentation.theme.ThemeMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val app = application as ResetLifeApplication
            val themeMode by app.themeManager.themeMode.collectAsStateWithLifecycle(ThemeMode.SYSTEM)
            ResetLifeTheme(themeMode = themeMode) {
                ResetLifeApp(app)
            }
        }
    }
}
