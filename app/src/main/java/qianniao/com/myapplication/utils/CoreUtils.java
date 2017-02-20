package qianniao.com.myapplication.utils;

/**
 * Created by Administrator on 2017/1/17.
 */

import java.util.HashMap;
import java.util.Map;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import android.graphics.Bitmap;
import android.graphics.Color;
/**
 *条码码操作类
 */
public class CoreUtils {
    private static final int BLACK = 0xff000000;
    private static final int WHITE = 0xffffffff;
    private static final int TRANSPARENT = 0;
    /**
     * 生成二维码
     * @param content 二维码文字内容
     * @param width 图片像素宽
     * @param height 图片像素高
     * @param charaterset 内容文字的编码方式，如gbk，utf-8
     * @param format 二维码图片的编码方式
     * @return Bitmap对象
     */
    public static Bitmap creat2DCore(String content,int width,int height,String charaterset,BarcodeFormat format){
        if (content == null || "".equals(content)) {
            return null;
        }
//配置参数
        HashMap hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET,charaterset);
//容错级别
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//设置空白边距的宽度
//hints.put(EncodeHintType.MARGIN, 2);
// 图像数据转换，使用了矩阵转换
//BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, format, width, height, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        int[] pixels = new int[width * height];
//事实上这里应该封装的。谷歌没有做。类似javase中的BufferImage
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (bitMatrix.get(x, y)) {
                    pixels[y * width + x] = Color.BLACK;
                } else {
                    pixels[y * width + x] = Color.WHITE;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
    /**
     * 二维码的解析
     * @param bitmap 二维码图片对象
     * @param charaterset 编码。以此文字编码解析二维码文字内容
     * @return Result 对象。谷歌封装了解析的相关结果
     */
    public static Result parseCode(Bitmap bitmap,String charaterset) {
        MultiFormatReader formatReader = new MultiFormatReader();

        int bw = bitmap.getWidth();
        int bh = bitmap.getHeight();
/*
* 似乎之前的版本中，是封装bitmap对象到RgbLuminanceSource构造方法里的，现在变麻烦了
* 我只能说，很糟糕的改变
*
*/ int[] pixels = new int[bw*bh];
        bitmap.getPixels(pixels, 0, bw, 0, 0, bw, bh);
        LuminanceSource source = new RGBLuminanceSource(bw, bh,pixels);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

        Map hints = new HashMap();
        hints.put(DecodeHintType.CHARACTER_SET, charaterset);
// Map hints = new HashMap();
// hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//其他版本的写法
        Result result = null;
        try {
            result = formatReader.decode(binaryBitmap, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
