package br.com.resetlife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.resetlife.presentation.ResetLifeApp
import br.com.resetlife.presentation.theme.ResetLifeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ResetLifeTheme {
                ResetLifeApp(application as ResetLifeApplication)
            }
        }
    }
}
