package com.zxl.easyapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.zxl.easyapp.R;
import com.zxl.easyapp.common.BaseConstant;

import org.xutils.common.util.LogUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 张晓莉 on 2016/8/12.
 * 图片操作工具类
 */
public class BitmapUtil {
    public static File protraitFile;
    public static String protraitPath;
    private static Uri cropUri;
    public static Uri origUri;

    /**
     * 添加高斯模糊效果
     *
     * @param sentBitmap 需要模糊的图片
     * @param radius     模糊半径
     * @return
     */
    public static Bitmap fastBlur(Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        if (radius < 1) {
            return (null);
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[w * h];
        LogUtil.e(w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;
        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];
        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }
        yw = yi = 0;
        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;
        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;
            for (x = 0; x < w; x++) {
                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;
                sir = stack[i + radius];
                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];
                rbs = r1 - Math.abs(i);
                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];
                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi += w;
            }
        }
        LogUtil.e(w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }

    /**
     * 保存图片
     *
     * @param bm
     * @param picName
     * @return
     */
    public static boolean saveBitmap(Bitmap bm, String picName) {
        Boolean isDown = false;
        try {
            if (!isFileExist("")) {
                File fileDir = createSDDir("");
            }
            File file = new File(BaseConstant.SD_PATH, picName);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            isDown = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isDown;
    }

    /**
     * 创建SD文件夹
     *
     * @param dirName
     * @return
     * @throws IOException
     */
    public static File createSDDir(String dirName) throws IOException {
        File dir = new File(BaseConstant.SD_PATH + dirName);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
        }
        return dir;
    }

    /**
     * 创建文件
     *
     * @param fileName
     * @return
     */
    public static boolean isFileExist(String fileName) {
        File file = new File(BaseConstant.SD_PATH + fileName);
        file.isFile();
        return file.exists();
    }

    /**
     * 选择图片裁剪
     */
    public static void startImagePick(Context context) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            ((Activity) context).startActivityForResult(Intent.createChooser(intent, "选择图片"), BaseConstant.REQUEST_CODE_GETIMAGE_BYCROP);
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            ((Activity) context).startActivityForResult(Intent.createChooser(intent, "选择图片"), BaseConstant.REQUEST_CODE_GETIMAGE_BYCROP);
        }
    }

    /**
     * 相机拍照
     */
    public static void startActionCamera(Context context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getCameraTempFile(context));
        ((Activity) context).startActivityForResult(intent, BaseConstant.REQUEST_CODE_GETIMAGE_BYCAMERA);
    }

    // 拍照保存的绝对路径
    private static Uri getCameraTempFile(Context context) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(BaseConstant.SD_PATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
            Toast.makeText(context, "无法保存上传的头像，请检查SD卡是否挂载", Toast.LENGTH_SHORT).show();
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        // 照片命名
        String cropFileName = "osc_camera_" + timeStamp + ".jpg";
        // 裁剪头像的绝对路径
        protraitPath = BaseConstant.SD_PATH + cropFileName;
        protraitFile = new File(protraitPath);
        cropUri = Uri.fromFile(protraitFile);
        origUri = cropUri;
        return cropUri;
    }

    /**
     * 拍照后裁剪
     *
     * @param data 原始图片
     */
    public static void startActionCrop(Context context, Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("output", getUploadTempFile(context, data));
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", BaseConstant.CROP);// 输出图片大小
        intent.putExtra("outputY", BaseConstant.CROP);
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        ((Activity) context).startActivityForResult(intent, BaseConstant.REQUEST_CODE_GETIMAGE_BYSDCARD);
    }

    // 裁剪头像的绝对路径
    private static Uri getUploadTempFile(Context context, Uri uri) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(BaseConstant.SD_PATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
            Toast.makeText(context, "无法保存上传的头像，请检查SD卡是否挂载", Toast.LENGTH_SHORT).show();
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String thePath = BitmapUtil.getAbsolutePathFromNoStandardUri(uri);
        // 如果是标准Uri
        if (VerificationUtil.isEmpty(thePath)) {
            thePath = BitmapUtil.getAbsoluteImagePath((Activity) context, uri);
        }
        String ext = FileUtil.getFileFormat(thePath);
        ext = VerificationUtil.isEmpty(ext) ? "jpg" : ext;
        // 照片命名
        String cropFileName = "osc_crop_" + timeStamp + "." + ext;
        // 裁剪头像的绝对路径
        protraitPath = BaseConstant.SD_PATH + cropFileName;
        protraitFile = new File(protraitPath);
        cropUri = Uri.fromFile(protraitFile);
        return cropUri;
    }

    /**
     * 判断当前Url是否标准的content://样式，如果不是，则返回绝对路径
     *
     * @return
     */
    public static String getAbsolutePathFromNoStandardUri(Uri mUri) {
        String filePath = null;
        String mUriString = mUri.toString();
        mUriString = Uri.decode(mUriString);
        String pre1 = "file://" + BaseConstant.SDCARD + File.separator;
        String pre2 = "file://" + BaseConstant.SDCARD_MNT + File.separator;
        if (mUriString.startsWith(pre1)) {
            filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring(pre1.length());
        } else if (mUriString.startsWith(pre2)) {
            filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring(pre2.length());
        }
        return filePath;
    }

    /**
     * 通过uri获取文件的绝对路径
     *
     * @param uri
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getAbsoluteImagePath(Activity context, Uri uri) {
        String imagePath = "";
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.managedQuery(uri, proj, // Which columns to
                // return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                imagePath = cursor.getString(column_index);
            }
        }
        return imagePath;
    }

    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 480, 480);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 写图片文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
     *
     * @throws IOException
     */
    public static void saveImage(Context context, String fileName, Bitmap bitmap) throws IOException {
        saveImage(context, fileName, bitmap, 100);
    }

    public static void saveImage(Context context, String fileName,
                                 Bitmap bitmap, int quality) throws IOException {
        if (bitmap == null || fileName == null || context == null)
            return;
        FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        byte[] bytes = stream.toByteArray();
        fos.write(bytes);
        fos.close();
    }

    /**
     * 创建缩略图
     *
     * @param context
     * @param largeImagePath 原始大图路径
     * @param thumbfilePath  输出缩略图路径
     * @param square_size    输出图片宽度
     * @param quality        输出图片质量
     * @throws IOException
     */
    public static boolean createImageThumbnail(Context context, String largeImagePath, String thumbfilePath, int square_size, int quality) throws IOException {
        boolean go_on = true;
        // 原始图片bitmap
        Bitmap cur_bitmap = null;
        try {
            cur_bitmap = Bimp.getSmallBitmap(largeImagePath);
        } catch (OutOfMemoryError e) {
            cur_bitmap = null;
            return go_on;
        }
        if (cur_bitmap == null) {
            return go_on;
        }
        // 原始图片的高宽
        int[] cur_img_size = new int[]{cur_bitmap.getWidth(), cur_bitmap.getHeight()};
        // 计算原始图片缩放后的宽高
        int[] new_img_size = scaleImageSize(cur_img_size, square_size);
        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.watermark);
        int waterMarkWidth = 0;
        if (cur_bitmap.getWidth() > cur_bitmap.getHeight()) {
            waterMarkWidth = cur_bitmap.getHeight() / 10;
        } else {
            waterMarkWidth = cur_bitmap.getWidth() / 10;
        }
        Bitmap waterMarkBitmap = null;
        try {
            waterMarkBitmap = zoomBitmap(thumb, waterMarkWidth, waterMarkWidth);
        } catch (OutOfMemoryError e) {
            waterMarkBitmap = null;
            return go_on;
        }
        Bitmap thb_bitmap = null;
        try {
            thb_bitmap = Bimp.createWatermarkBitmap(cur_bitmap, waterMarkBitmap);
        } catch (OutOfMemoryError e) {
            thb_bitmap = null;
            return go_on;
        }
        Bitmap thb_thumb_1 = null;
        try {
            thb_thumb_1 = zoomBitmap(thb_bitmap, new_img_size[0], new_img_size[1]);
        } catch (OutOfMemoryError e) {
            thb_thumb_1 = null;
            return go_on;
        }
        saveImageToSD(null, thumbfilePath, thb_thumb_1, quality);
        go_on = false;
        return go_on;
    }

    /**
     * 计算缩放图片的宽高
     *
     * @param img_size
     * @param square_size
     * @return
     */
    public static int[] scaleImageSize(int[] img_size, int square_size) {
        if (img_size[0] <= square_size && img_size[1] <= square_size)
            return img_size;
        double ratio = square_size
                / (double) Math.max(img_size[0], img_size[1]);
        return new int[]{(int) (img_size[0] * ratio),
                (int) (img_size[1] * ratio)};
    }

    /**
     * 放大缩小图片
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        Bitmap newbmp = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            float scaleWidht = ((float) w / width);
            float scaleHeight = ((float) h / height);
            matrix.postScale(scaleWidht, scaleHeight);
            try {
                newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return newbmp;
    }

    /**
     * 写图片文件到SD卡
     *
     * @throws IOException
     */
    public static void saveImageToSD(Context ctx, String filePath, Bitmap bitmap, int quality) throws IOException {
        if (bitmap != null) {
            File file = new File(filePath.substring(0,
                    filePath.lastIndexOf(File.separator)));
            if (!file.exists()) {
                file.mkdirs();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bitmap.recycle();
            bos.flush();
            bos.close();
            if (ctx != null) {
                scanPhoto(ctx, filePath);
            }
        }
    }

    /**
     * 让Gallery上能马上看到该图片
     */
    private static void scanPhoto(Context ctx, String imgFileName) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imgFileName);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        ctx.sendBroadcast(mediaScanIntent);
    }
}