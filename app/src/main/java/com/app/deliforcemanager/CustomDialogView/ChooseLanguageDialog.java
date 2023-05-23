package com.app.deliforcemanager.CustomDialogView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.app.deliforcemanager.R;

import timber.log.Timber;

public class ChooseLanguageDialog extends Dialog {

    private RadioGroup selectLangGrp;
    private SelectAppLangChangeListener selectAppLangChangeListener;
    private int lang_type;

    public ChooseLanguageDialog(@NonNull Context context, int themeResId, int lang_type, SelectAppLangChangeListener appLangChangeListener) {
        super(context);
        this.selectAppLangChangeListener = appLangChangeListener;
        this.lang_type = lang_type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.alert_language_selection);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        selectLangGrp = findViewById(R.id.radioLanguageSelectionGroup);
        RadioButton englsih_radio = findViewById(R.id.radio_lang_english);
        RadioButton spanish_radio = findViewById(R.id.radio_lang_spanish);
        RadioButton french_radio = findViewById(R.id.radio_lang_french);
        RadioButton portuguese_radio = findViewById(R.id.radio_lang_portuguese);
        RadioButton malay_radio = findViewById(R.id.radio_lang_malay);
        RadioButton arabic_radio = findViewById(R.id.radio_lang_arabic);


        Timber.e("lang_type%s", String.valueOf(lang_type));
        if (lang_type == 1) {
            englsih_radio.setChecked(true);
        } else if (lang_type == 2) {
            spanish_radio.setChecked(true);
        } else if (lang_type == 3) {
            french_radio.setChecked(true);
        } else if (lang_type == 4) {
            portuguese_radio.setChecked(true);
        } else if (lang_type == 5) {
            malay_radio.setChecked(true);
        } else if (lang_type == 6) {
            arabic_radio.setChecked(true);
        }
        onClickEvent();
    }

    private void onClickEvent() {

        selectLangGrp.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_lang_english:
                    selectAppLangChangeListener.onEnglishSelectClick();
                    break;
                case R.id.radio_lang_spanish:
                    selectAppLangChangeListener.onSpanishSelectClick();
                    break;
                case R.id.radio_lang_french:
                    selectAppLangChangeListener.onFrenchSelectClick();
                    break;
                case R.id.radio_lang_portuguese:
                    selectAppLangChangeListener.onPortugueseSelectClick();
                    break;
                case R.id.radio_lang_malay:
                    selectAppLangChangeListener.onMalaySelectClick();
                    break;
                case R.id.radio_lang_arabic:
                    selectAppLangChangeListener.onArabicSelectClick();
                    break;
            }
        });

    }

    public interface SelectAppLangChangeListener {

        void onEnglishSelectClick();

        void onSpanishSelectClick();

        void onFrenchSelectClick();

        void onPortugueseSelectClick();

        void onMalaySelectClick();

        void onArabicSelectClick();
    }
}
