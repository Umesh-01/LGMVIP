package com.umeshsingh.facedetection.preference;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.umeshsingh.facedetection.R;

public class StillImagePreferenceFragment extends PreferenceFragment {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.preference_still_image);
    FaceDetectionUtils.setUpFaceDetectionPreferences(this, /* isStreamMode = */false);
  }
}
