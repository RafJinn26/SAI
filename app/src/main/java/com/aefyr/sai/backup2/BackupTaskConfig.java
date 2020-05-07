package com.aefyr.sai.backup2;

import android.os.Parcel;
import android.os.Parcelable;

import com.aefyr.sai.model.common.PackageMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BackupTaskConfig implements Parcelable {

    public static final Parcelable.Creator<BackupTaskConfig> CREATOR = new Parcelable.Creator<BackupTaskConfig>() {
        @Override
        public BackupTaskConfig createFromParcel(Parcel in) {
            return new BackupTaskConfig(in);
        }

        @Override
        public BackupTaskConfig[] newArray(int size) {
            return new BackupTaskConfig[size];
        }
    };
    private PackageMeta mPackageMeta;
    private ArrayList<File> mApksToBackup = new ArrayList<>();
    private boolean mPackApksIntoAnArchive = true;

    private BackupTaskConfig(PackageMeta packageMeta) {
        this.mPackageMeta = packageMeta;
    }

    BackupTaskConfig(Parcel in) {
        mPackageMeta = in.readParcelable(PackageMeta.class.getClassLoader());

        ArrayList<String> apkFilePaths = new ArrayList<>();
        in.readStringList(apkFilePaths);
        for (String apkFilePath : apkFilePaths)
            mApksToBackup.add(new File(apkFilePath));

        mPackApksIntoAnArchive = in.readInt() == 1;
    }

    public PackageMeta packageMeta() {
        return mPackageMeta;
    }

    public List<File> apksToBackup() {
        return mApksToBackup;
    }

    public boolean packApksIntoAnArchive() {
        return mPackApksIntoAnArchive;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mPackageMeta, flags);

        ArrayList<String> apkFilePaths = new ArrayList<>();
        for (File apkFile : mApksToBackup)
            apkFilePaths.add(apkFile.getAbsolutePath());
        dest.writeStringList(apkFilePaths);

        dest.writeInt(mPackApksIntoAnArchive ? 1 : 0);
    }

    public static class Builder {
        private BackupTaskConfig mConfig;

        public Builder(PackageMeta packageMeta) {
            mConfig = new BackupTaskConfig(packageMeta);
        }

        public BackupTaskConfig.Builder addApk(File apkFile) {
            mConfig.mApksToBackup.add(apkFile);
            return this;
        }

        public BackupTaskConfig.Builder addAllApks(Collection<File> apkFiles) {
            mConfig.mApksToBackup.addAll(apkFiles);
            return this;
        }

        public BackupTaskConfig.Builder setPackApksIntoAnArchive(boolean pack) {
            mConfig.mPackApksIntoAnArchive = pack;
            return this;
        }

        public BackupTaskConfig build() {
            return mConfig;
        }
    }


}
