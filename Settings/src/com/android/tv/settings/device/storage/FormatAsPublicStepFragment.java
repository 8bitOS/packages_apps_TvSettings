/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.android.tv.settings.device.storage;

import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;

import com.android.tv.settings.R;
import com.android.tv.settings.device.StorageResetActivity;

import java.util.List;

public class FormatAsPublicStepFragment extends GuidedStepFragment {
    private static final int ACTION_ID_CANCEL = 0;
    private static final int ACTION_ID_BACKUP = 1;
    private static final int ACTION_ID_FORMAT = 2;

    private StorageManager mStorageManager;

    public static FormatAsPublicStepFragment newInstance(VolumeInfo volumeInfo) {
        final FormatAsPublicStepFragment fragment = new FormatAsPublicStepFragment();
        final Bundle b = new Bundle(1);
        b.putString(VolumeInfo.EXTRA_VOLUME_ID, volumeInfo.getId());
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStorageManager = getActivity().getSystemService(StorageManager.class);
    }

    @Override
    public @NonNull GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        return new GuidanceStylist.Guidance(
                getString(R.string.storage_wizard_format_as_public_title),
                getString(R.string.storage_wizard_format_as_public_description), "",
                getActivity().getDrawable(R.drawable.ic_settings_storage));
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
        actions.add(new GuidedAction.Builder()
                .id(ACTION_ID_CANCEL)
                .title(getString(android.R.string.cancel))
                .build());
        actions.add(new GuidedAction.Builder()
                .id(ACTION_ID_BACKUP)
                .title(getString(R.string.storage_wizard_backup_apps_action))
                .build());
        actions.add(new GuidedAction.Builder()
                .id(ACTION_ID_FORMAT)
                .title(getString(R.string.storage_wizard_format_action))
                .build());
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        final long id = action.getId();

        if (id == ACTION_ID_CANCEL) {
            getFragmentManager().popBackStack();
        } else if (id == ACTION_ID_BACKUP) {
            // TODO
        } else if (id == ACTION_ID_FORMAT) {
            final VolumeInfo volumeInfo = mStorageManager.findVolumeById(
                    getArguments().getString(VolumeInfo.EXTRA_VOLUME_ID));
            new StorageResetActivity.FormatAsPublicTask(getActivity(), volumeInfo).execute();
            getFragmentManager().popBackStack();
        }
    }
}
