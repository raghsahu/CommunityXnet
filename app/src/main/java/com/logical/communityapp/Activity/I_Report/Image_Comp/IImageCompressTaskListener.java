package com.logical.communityapp.Activity.I_Report.Image_Comp;
import java.io.File;
import java.util.List;
/**
 * Created by Raghvendra Sahu on 02/12/2019.
 */
public interface IImageCompressTaskListener {


    public void onComplete(List<File> compressed);
    public void onError(Throwable error);
}
