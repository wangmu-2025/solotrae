package com.example.examquestionbank;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

/**
 * 用户协议页面
 */
public class UserAgreementActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);

        // 初始化视图
        initViews();

        // 设置返回按钮点击事件
        toolbar.setNavigationOnClickListener(v -> finish());

        // 设置内容
        setContent();
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvContent = findViewById(R.id.tvContent);
    }

    /**
     * 设置用户协议内容
     */
    private void setContent() {
        String content = "用户协议\n\n" +
                "欢迎使用考试题库应用！\n\n" +
                "1. 服务条款\n" +
                "1.1 本协议是您与考试题库应用之间关于使用本应用的法律协议。\n" +
                "1.2 您在使用本应用前，应当仔细阅读并遵守本协议的全部内容。\n" +
                "1.3 您一旦使用本应用，即视为您已完全同意并接受本协议的全部条款。\n\n" +
                "2. 用户权利与义务\n" +
                "2.1 用户有权使用本应用提供的各项服务。\n" +
                "2.2 用户应当遵守法律法规，不得利用本应用从事任何违法活动。\n" +
                "2.3 用户应当妥善保管自己的账号和密码，对账号下的所有活动负责。\n\n" +
                "3. 知识产权\n" +
                "3.1 本应用的所有内容（包括但不限于文字、图片、音频、视频等）均受知识产权法律保护。\n" +
                "3.2 用户不得未经授权复制、修改、传播本应用的任何内容。\n\n" +
                "4. 服务变更与终止\n" +
                "4.1 本应用有权随时变更或终止部分或全部服务，无需提前通知用户。\n" +
                "4.2 用户违反本协议的，本应用有权终止其使用权限。\n\n" +
                "5. 免责声明\n" +
                "5.1 本应用不对因网络故障、系统故障等不可抗力导致的服务中断或数据丢失承担责任。\n" +
                "5.2 本应用不对用户因使用本应用而产生的任何直接或间接损失承担责任。\n\n" +
                "6. 协议变更\n" +
                "6.1 本应用有权随时修改本协议，修改后的协议将在应用内公告。\n" +
                "6.2 用户继续使用本应用，视为接受修改后的协议。\n\n" +
                "7. 法律适用与争议解决\n" +
                "7.1 本协议受中华人民共和国法律管辖。\n" +
                "7.2 因本协议产生的争议，双方应友好协商解决；协商不成的，任何一方均可向有管辖权的人民法院提起诉讼。\n\n" +
                "8. 其他条款\n" +
                "8.1 本协议的最终解释权归考试题库应用所有。\n" +
                "8.2 本协议自2026年1月1日起生效。";

        tvContent.setText(content);
    }
}