package qianniao.com.myapplication.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import qianniao.com.myapplication.helpdeskdemo.ui.MainActivity;
import qianniao.com.myapplication.R;
import qianniao.com.myapplication.base.BaseView;
import qianniao.com.myapplication.presenter.TestPresenter;

/**
 * Created by Administrator on 2017/1/17.
 */

public class TestFragment extends BaseFragment implements BaseView {
    @BindView(R.id.textView_erWeiMa)
    TextView textViewErWeiMa;
    @BindView(R.id.button_saoMa)
    Button buttonSaoMa;
    @BindView(R.id.et_qr_string)
    EditText etQrString;
    @BindView(R.id.btn_add_qrcode)
    Button btnAddQrcode;
    @BindView(R.id.logo)
    CheckBox logo;
    @BindView(R.id.iv_qr_image)
    ImageView ivQrImage;
    @BindView(R.id.service)
    Button service;

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_test, null);
        ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            //result就是二维码扫描的结果。
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            textViewErWeiMa.setText(scanResult);
        }
    }


    public TestFragment() {
        presenter = new TestPresenter(this);
    }

    @Override
    public void showSuccessView() {
        initListener();

    }


    public void initListener() {
        //环信
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        //二维码相关
        buttonSaoMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        btnAddQrcode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String contentString = etQrString.getText().toString();
                if (!contentString.equals("")) {
                    //根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
                    Bitmap qrCodeBitmap = EncodingUtils.createQRCode(contentString, 350, 350,
                            logo.isChecked() ?
                                    BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher) :
                                    null);
                    ivQrImage.setImageBitmap(qrCodeBitmap);
                } else {
                    Toast.makeText(getActivity(), "Text can not be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void updataView() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
