package com.alvincezy.tinypic2;

import com.intellij.openapi.vfs.VirtualFile;
import com.tinify.Result;
import com.tinify.Source;
import com.tinify.Tinify;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * Created by alvince on 17-7-14.
 *
 * @author alvince.zy@gmail.com
 */
public class TinifyFlowable {

    private final VirtualFile file;
    private Source source;
    private Result result;

    public TinifyFlowable(@NotNull VirtualFile file) {
        this.file = file;
    }

    public void load() throws IOException {
        source = Tinify.fromFile(file.getPath());
    }

    public boolean performTinify() {
        if (source != null) {
            try {
                result = source.result();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @NotNull
    public VirtualFile file() {
        return file;
    }

    @Nullable
    public Source source() {
        return source;
    }

    @Nullable
    public Result result() {
        return result;
    }
}
