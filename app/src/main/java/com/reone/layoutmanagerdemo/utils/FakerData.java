package com.reone.layoutmanagerdemo.utils;


import com.github.javafaker.Cat;
import com.github.javafaker.Faker;
import com.reone.layoutmanagerdemo.R;
import com.reone.layoutmanagerdemo.bean.ItemBean;

/**
 * Created by wangxingsheng on 2020/6/9.
 * desc:
 */
public class FakerData {
    public static final int[] headers = {
            R.mipmap.header_icon_1
            , R.mipmap.header_icon_2
            , R.mipmap.header_icon_3
            , R.mipmap.header_icon_4
            , R.mipmap.header_icon_5
            , R.mipmap.header_icon_6
            , R.mipmap.header_icon_7
            , R.mipmap.header_icon_8
            , R.mipmap.header_icon_9
            , R.mipmap.header_icon_10
            , R.mipmap.header_icon_11
            , R.mipmap.header_icon_12
            , R.mipmap.header_icon_13};

    public static final int[] norutos = {
            R.mipmap.naruto_1
            , R.mipmap.naruto_2
            , R.mipmap.naruto_3
            , R.mipmap.naruto_4
            , R.mipmap.naruto_5
            , R.mipmap.naruto_6
            , R.mipmap.naruto_7
            , R.mipmap.naruto_8
            , R.mipmap.naruto_9
            , R.mipmap.naruto_10
            , R.mipmap.naruto_12
            , R.mipmap.naruto_13
            , R.mipmap.naruto_14
            , R.mipmap.naruto_15
            , R.mipmap.naruto_16
            , R.mipmap.naruto_17
            , R.mipmap.naruto_18
            , R.mipmap.naruto_19
            , R.mipmap.naruto_20
    };

    public static final int[] movies = {
            R.mipmap.movie_1
            , R.mipmap.movie_2
            , R.mipmap.movie_3
            , R.mipmap.movie_4
            , R.mipmap.movie_5
            , R.mipmap.movie_6
            , R.mipmap.movie_7
            , R.mipmap.movie_8
            , R.mipmap.movie_9
            , R.mipmap.movie_10
            , R.mipmap.movie_12
            , R.mipmap.movie_13
            , R.mipmap.movie_14
            , R.mipmap.movie_15
            , R.mipmap.movie_16
            , R.mipmap.movie_17
            , R.mipmap.movie_18
            , R.mipmap.movie_19
            , R.mipmap.movie_20
    };

    private static int tempIndex = 0;

    public static ItemBean createItemBean() {
        Cat cat = Faker.instance().cat();
        ItemBean itemBean = new ItemBean();
        itemBean.setName(cat.name());
        itemBean.setBreed(cat.breed());
        itemBean.setRegistry(cat.registry());
        itemBean.setHeaderIcon(headers[tempIndex % headers.length]);
        itemBean.setMovie(movies[tempIndex % movies.length]);
        itemBean.setNoruto(norutos[tempIndex % norutos.length]);
        tempIndex++;
        return itemBean;
    }
}
