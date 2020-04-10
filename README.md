# FpShadowLayout
## 自定义viewGroup，给view加上阴影效果。支持单边阴影，多边阴影，圆角矩形阴影。
## ![](https://github.com/maxiaoyi/FpShadowLayout/raw/master/demo.png)
## 用法：implementation 'com.mxy.fpshadowlayout:fpshadowlayout:0.0.1'
矩形用法：


        <com.mxy.fpshadowlayout.FpShadowLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:fp_shadowColor="#FF00FF"
            app:fp_shadowRadius="10dp"
            app:fp_shadowShape="fp_rectangle"
            app:fp_shadowSide="fp_all"
            >
            <RelativeLayout
                android:layout_width="100dp"
                android:layout_height="100dp"
                android:padding="50dp"
                android:background="@color/colorAccent"
                ></RelativeLayout>

        </com.mxy.fpshadowlayout.FpShadowLayout>
 
 圆角矩形用法：

         <com.mxy.fpshadowlayout.FpShadowLayout
            android:layout_width="match_parent"
            android:layout_height="100dp"
            app:fp_shadowColor="#800080"
            app:fp_shadowRadius="10dp"
            app:fp_shadowShape="fp_round_rectangle"
            app:fp_shadowSide="fp_top"
            app:fp_round_corner="fp_corner_leftTop"
            app:fp_shadowRoundRadius="15dp"
            >
            <RelativeLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:background="@drawable/shape_lefttop_corner"
                ></RelativeLayout>

        </com.mxy.fpshadowlayout.FpShadowLayout>
 需要指出的是设置圆角矩形时，只需要写阴影所在的边的圆角情况，如：阴影在上边和左边，只需要说明左上角、右上角和左下角，如果左上角和右上角是圆角，左下角是直角时这样写，app:fp_round_corner="fp_corner_leftTop|fp_corner_rightTop"
 实现原理请移步：https://blog.csdn.net/u012155141/article/details/90477443
