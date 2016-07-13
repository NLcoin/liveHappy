package com.wxgh.livehappy.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wxgh.livehappy.R;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class ViewDialog extends Dialog {
    /**
     * 构造方法
     *
     * @param context
     *            Context
     * @param theme
     *            主题id
     */
    public ViewDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 构造方法
     *
     * @param context
     *            Context
     */
    public ViewDialog(Context context) {
        super(context);
    }

    /**
     * 自定义对话框的构建类
     *
     * @author MuWei
     * @version 1.00 2015/02/06 新建
     */
    public static class Builder {

        /** Context */
        private Context context;

        /** 标题文本 */
        private String title;
        /** 显示信息文本 */
        private String message;
        /** Positive Button文本 */
        private String positiveText;
        /** Negative Button文本 */
        private String negativeText;
        /** Neutral Button文本 */
        private String neutralText;

        /** 是否可按返回键退出 */
        private boolean cancelable = true;

        /** 自定义显示的View */
        private View contentView;

        /** 标题文本框 */
        private TextView txtTitle;
        /** 内容布局 */
        private LinearLayout llytContent;
        /** 消息文本框 */
        private TextView txtMessage;
        /** Positive Button */
        private Button btnPositive;
        /** Negative Button */
        private Button btnNegative;
        /** Neutral Button */
        private Button btnNeutral;

        /** Positive Button点击监听 */
        private DialogInterface.OnClickListener positiveOnClickListener;
        /** Negative Button点击监听 */
        private DialogInterface.OnClickListener negativeOnClickListener;
        /** Neutral Button点击监听 */
        private DialogInterface.OnClickListener neutralOnClickListener;

        /** 关闭监听 */
        private DialogInterface.OnCancelListener onCancelListener;

        /** 按键监听 */
        private DialogInterface.OnKeyListener onKeyListener;

        /**
         * 构造方法
         *
         * @param context
         *            Context
         */
        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 获取Context
         *
         * @return Context
         */
        public Context getContext() {
            return context;
        }

        /**
         * 设置信息
         *
         * @param message
         *            信息id
         * @return 当前Builder
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * 设置信息
         *
         * @param message
         *            信息文本
         * @return 当前Builder
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置标题
         *
         * @param title
         *            标题id
         * @return 当前Builder
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * 设置标题
         *
         * @param title
         *            标题文本
         * @return 当前Builder
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置自定义View
         *
         * @param view
         *            自定义View
         * @return 当前Builder
         */
        public Builder setContentView(View view) {
            this.contentView = view;
            return this;
        }

        /**
         * 设置Positive Button点击监听
         *
         * @param positiveText
         *            Positive Button显示文本Id
         * @param listener
         *            点击监听
         * @return 当前Builder
         */
        public Builder setPositiveButton(int positiveText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveText = (String) context.getText(positiveText);
            this.positiveOnClickListener = listener;
            return this;
        }

        /**
         * 设置Positive Button点击监听
         *
         * @param positiveText
         *            Positive Button显示文本
         * @param listener
         *            点击监听
         * @return 当前Builder
         */
        public Builder setPositiveButton(String positiveText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveText = positiveText;
            this.positiveOnClickListener = listener;
            return this;
        }

        /**
         * 设置Negative Button点击监听
         *
         * @param negativeText
         *            Negative Button显示文本Id
         * @param listener
         *            点击监听
         * @return 当前Builder
         */
        public Builder setNegativeButton(int negativeText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeText = (String) context.getText(negativeText);
            this.negativeOnClickListener = listener;
            return this;
        }

        /**
         * 设置Negative Button点击监听
         *
         * @param negativeText
         *            Negative Button显示文本
         * @param listener
         *            点击监听
         * @return 当前Builder
         */
        public Builder setNegativeButton(String negativeText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeText = negativeText;
            this.negativeOnClickListener = listener;
            return this;
        }

        /**
         * 设置Neutral Button点击监听
         *
         * @param neutralText
         *            Neutral Button显示文本Id
         * @param listener
         *            点击监听
         * @return 当前Builder
         */
        public Builder setNeutralButton(int neutralText,
                                        DialogInterface.OnClickListener listener) {
            this.neutralText = (String) context.getText(neutralText);
            this.neutralOnClickListener = listener;
            return this;
        }

        /**
         * 设置Neutral Button点击监听
         *
         * @param neutralText
         *            Neutral Button显示文本
         * @param listener
         *            点击监听
         * @return 当前Builder
         */
        public Builder setNeutralButton(String neutralText,
                                        DialogInterface.OnClickListener listener) {
            this.neutralText = neutralText;
            this.neutralOnClickListener = listener;
            return this;
        }

        /**
         * 设置是否可按返回键退出
         *
         * @param cancelable
         *            是否可按返回键退出
         * @return 当前Builder
         */
        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        /**
         * 设置关闭监听
         *
         * @param listener
         *            关闭监听
         * @return 当前Builder
         */
        public Builder setOnCancelListener(
                DialogInterface.OnCancelListener listener) {
            this.onCancelListener = listener;
            return this;
        }

        /**
         * 设置按键监听
         *
         * @param listener
         *            按键监听
         * @return 当前Builder
         */
        public Builder setOnKeyListener(DialogInterface.OnKeyListener listener) {
            this.onKeyListener = listener;
            return this;
        }

        /**
         * 创建对话框
         *
         * @return 对话框实例
         */
        public ViewDialog create() {
            // 获取LayoutInflater
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 使用样式初始化对话框
            final ViewDialog dialog = new ViewDialog(context,
                    R.style.dialog_style);

            // 获取对话框布局
            View view = inflater.inflate(R.layout.layout_view_dialog, null);
            // 加载布局
            dialog.addContentView(view, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            // 初始化控件
            initView(view);

            // 设置Positive Button
            if (!TextUtils.isEmpty(positiveText)) {
                // Positive Button文本存在，显示按钮
                btnPositive.setText(positiveText);
                // 监听存在
                if (positiveOnClickListener != null) {
                    // 添加按钮监听
                    btnPositive.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            positiveOnClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            } else {
                // Positive Button文本不存在，隐藏按钮
                btnPositive.setVisibility(View.GONE);
            }

            // 设置Negative Button
            if (!TextUtils.isEmpty(negativeText)) {
                // Negative Button文本存在，显示按钮
                btnNegative.setText(negativeText);
                // 监听存在
                if (negativeOnClickListener != null) {
                    // 添加按钮监听
                    btnNegative.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            negativeOnClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            } else {
                // Negative Button文本不存在，隐藏按钮
                btnNegative.setVisibility(View.GONE);
            }

            // 标题存在
            if (!TextUtils.isEmpty(title)) {
                // 显示标题文本框
                txtTitle.setText(title);
                txtTitle.setVisibility(View.VISIBLE);
            } else {
                // 隐藏标题文本框
                txtTitle.setVisibility(View.GONE);
            }

            // 信息存在
            if (!TextUtils.isEmpty(message)) {
                // 显示信息文本框
                txtMessage.setText(message);
                txtMessage.setVisibility(View.VISIBLE);
            } else if (contentView != null) {
                // 自定义View，移除原有的View
                llytContent.removeAllViews();
                // 添加新View
                llytContent.addView(contentView, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }

            // 设置是否可按手机返回键返回
            dialog.setCancelable(cancelable);
            if (cancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            // 设置对话框关闭监听
            dialog.setOnCancelListener(onCancelListener);
            // 设置对话框按键监听
            if (onKeyListener != null) {
                dialog.setOnKeyListener(onKeyListener);
            }

            // 加载View
            dialog.setContentView(view);
            // 返回
            return dialog;
        }

        /**
         * 初始化控件
         *
         * @param view
         *            View
         */
        private void initView(View view) {
            txtTitle = (TextView) view
                    .findViewById(R.id.txtTitle);
            llytContent = (LinearLayout) view
                    .findViewById(R.id.llytContent);
            txtMessage = (TextView) view
                    .findViewById(R.id.txtMessage);
            btnPositive = (Button) view
                    .findViewById(R.id.btnPositive);
            btnNegative = (Button) view
                    .findViewById(R.id.btnNegative);
        }
    }
}
