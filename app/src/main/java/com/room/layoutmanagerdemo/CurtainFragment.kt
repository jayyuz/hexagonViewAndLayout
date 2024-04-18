package com.room.layoutmanagerdemo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.room.layoutmanagerdemo.ui.curtain_layout.CurtainLayout
import com.room.layoutmanagerdemo.ui.curtain_layout.adapter.CurtainAdapter

class CurtainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_curtain, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = listOf<String>(
            "https://p0.ssl.img.360kuai.com/t0185821e4d2b4a11ba.jpg?size=546x524",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t01e37e628e97befdd6.webp",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t016304001eb8b977f0.webp",
            "https://p1.ssl.qhimgs1.com/sdr/400__/t01ccca1cf240701f17.webp",
            "https://p0.ssl.qhimgs4.com/t016713273d110ef879.jpg",
            "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2070426602,409573032&fm=175&app=25&f=JPEG?w=640&h=933&s=DA44A908A62B26B575A909820300A086",
            "https://p5.ssl.qhimgs1.com/sdr/400__/t01856aec8b4ca19e1f.webp",
            "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1327775093,1603230616&fm=175&app=25&f=JPG?w=480&h=640&s=7294558CFC33649C2FE548CA030010B6",
            "https://pics4.baidu.com/feed/adaf2edda3cc7cd9feaa7bdccffbb73ab90e9134.jpeg?token=a0fb9842cea35c3fef9e82be0e005a6d&s=F9A58F503EB3469CC02038870300F0A1",
            "https://pics5.baidu.com/feed/bba1cd11728b4710ebff76d80a6b2df9fd032378.jpeg?token=9ed81590a3e25e8a06876d83074059fc&s=5EA6A544801347D44B2C388303003080",
            "https://p0.ssl.img.360kuai.com/t01ab1f68a877024809.jpg?size=640x479",
            "https://p5.ssl.qhimgs1.com/sdr/400__/t01856aec8b4ca19e1f.webp",
            "https://hbimg.huabanimg.com/cad0bf017e9b6e92bb63a5ccd884bbb13df6c3d1b18a-HCbrMJ_fw658",
            "https://p2.ssl.qhimgs1.com/sdr/400__/t019c9569641c081d22.jpg",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t018ce861697da641d6.png",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg",
            "https://p1.ssl.qhimgs1.com/sdr/400__/t014c5cf5fcb4a02091.jpg",
            "https://p0.ssl.img.360kuai.com/t0185821e4d2b4a11ba.jpg?size=546x524",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t01e37e628e97befdd6.webp",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t016304001eb8b977f0.webp",
            "https://p1.ssl.qhimgs1.com/sdr/400__/t01ccca1cf240701f17.webp",
            "https://p0.ssl.qhimgs4.com/t016713273d110ef879.jpg",
            "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2070426602,409573032&fm=175&app=25&f=JPEG?w=640&h=933&s=DA44A908A62B26B575A909820300A086",
            "https://p5.ssl.qhimgs1.com/sdr/400__/t01856aec8b4ca19e1f.webp",
            "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1327775093,1603230616&fm=175&app=25&f=JPG?w=480&h=640&s=7294558CFC33649C2FE548CA030010B6",
            "https://pics4.baidu.com/feed/adaf2edda3cc7cd9feaa7bdccffbb73ab90e9134.jpeg?token=a0fb9842cea35c3fef9e82be0e005a6d&s=F9A58F503EB3469CC02038870300F0A1",
            "https://pics5.baidu.com/feed/bba1cd11728b4710ebff76d80a6b2df9fd032378.jpeg?token=9ed81590a3e25e8a06876d83074059fc&s=5EA6A544801347D44B2C388303003080",
            "https://p0.ssl.img.360kuai.com/t01ab1f68a877024809.jpg?size=640x479",
            "https://p5.ssl.qhimgs1.com/sdr/400__/t01856aec8b4ca19e1f.webp",
            "https://hbimg.huabanimg.com/cad0bf017e9b6e92bb63a5ccd884bbb13df6c3d1b18a-HCbrMJ_fw658",
            "https://p2.ssl.qhimgs1.com/sdr/400__/t019c9569641c081d22.jpg",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t018ce861697da641d6.png",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg",
            "https://p1.ssl.qhimgs1.com/sdr/400__/t014c5cf5fcb4a02091.jpg",
            "https://p0.ssl.img.360kuai.com/t0185821e4d2b4a11ba.jpg?size=546x524",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t01e37e628e97befdd6.webp",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t016304001eb8b977f0.webp",
            "https://p1.ssl.qhimgs1.com/sdr/400__/t01ccca1cf240701f17.webp",
            "https://p0.ssl.qhimgs4.com/t016713273d110ef879.jpg",
            "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2070426602,409573032&fm=175&app=25&f=JPEG?w=640&h=933&s=DA44A908A62B26B575A909820300A086",
            "https://p5.ssl.qhimgs1.com/sdr/400__/t01856aec8b4ca19e1f.webp",
            "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1327775093,1603230616&fm=175&app=25&f=JPG?w=480&h=640&s=7294558CFC33649C2FE548CA030010B6",
            "https://pics4.baidu.com/feed/adaf2edda3cc7cd9feaa7bdccffbb73ab90e9134.jpeg?token=a0fb9842cea35c3fef9e82be0e005a6d&s=F9A58F503EB3469CC02038870300F0A1",
            "https://pics5.baidu.com/feed/bba1cd11728b4710ebff76d80a6b2df9fd032378.jpeg?token=9ed81590a3e25e8a06876d83074059fc&s=5EA6A544801347D44B2C388303003080",
            "https://p0.ssl.img.360kuai.com/t01ab1f68a877024809.jpg?size=640x479",
            "https://p5.ssl.qhimgs1.com/sdr/400__/t01856aec8b4ca19e1f.webp",
            "https://hbimg.huabanimg.com/cad0bf017e9b6e92bb63a5ccd884bbb13df6c3d1b18a-HCbrMJ_fw658",
            "https://p2.ssl.qhimgs1.com/sdr/400__/t019c9569641c081d22.jpg",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t018ce861697da641d6.png",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg",
            "https://p1.ssl.qhimgs1.com/sdr/400__/t014c5cf5fcb4a02091.jpg",
            "https://p0.ssl.img.360kuai.com/t0185821e4d2b4a11ba.jpg?size=546x524",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t01e37e628e97befdd6.webp",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t016304001eb8b977f0.webp",
            "https://p1.ssl.qhimgs1.com/sdr/400__/t01ccca1cf240701f17.webp",
            "https://p0.ssl.qhimgs4.com/t016713273d110ef879.jpg",
            "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2070426602,409573032&fm=175&app=25&f=JPEG?w=640&h=933&s=DA44A908A62B26B575A909820300A086",
            "https://p5.ssl.qhimgs1.com/sdr/400__/t01856aec8b4ca19e1f.webp",
            "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1327775093,1603230616&fm=175&app=25&f=JPG?w=480&h=640&s=7294558CFC33649C2FE548CA030010B6",
            "https://pics4.baidu.com/feed/adaf2edda3cc7cd9feaa7bdccffbb73ab90e9134.jpeg?token=a0fb9842cea35c3fef9e82be0e005a6d&s=F9A58F503EB3469CC02038870300F0A1",
            "https://pics5.baidu.com/feed/bba1cd11728b4710ebff76d80a6b2df9fd032378.jpeg?token=9ed81590a3e25e8a06876d83074059fc&s=5EA6A544801347D44B2C388303003080",
            "https://p0.ssl.img.360kuai.com/t01ab1f68a877024809.jpg?size=640x479",
            "https://p5.ssl.qhimgs1.com/sdr/400__/t01856aec8b4ca19e1f.webp",
            "https://hbimg.huabanimg.com/cad0bf017e9b6e92bb63a5ccd884bbb13df6c3d1b18a-HCbrMJ_fw658",
            "https://p2.ssl.qhimgs1.com/sdr/400__/t019c9569641c081d22.jpg",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t018ce861697da641d6.png",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg",
            "https://p1.ssl.qhimgs1.com/sdr/400__/t014c5cf5fcb4a02091.jpg",
            "https://p0.ssl.img.360kuai.com/t0185821e4d2b4a11ba.jpg?size=546x524",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t01e37e628e97befdd6.webp",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t016304001eb8b977f0.webp",
            "https://p1.ssl.qhimgs1.com/sdr/400__/t01ccca1cf240701f17.webp",
            "https://p0.ssl.qhimgs4.com/t016713273d110ef879.jpg",
            "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2070426602,409573032&fm=175&app=25&f=JPEG?w=640&h=933&s=DA44A908A62B26B575A909820300A086",
            "https://p5.ssl.qhimgs1.com/sdr/400__/t01856aec8b4ca19e1f.webp",
            "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1327775093,1603230616&fm=175&app=25&f=JPG?w=480&h=640&s=7294558CFC33649C2FE548CA030010B6",
            "https://pics4.baidu.com/feed/adaf2edda3cc7cd9feaa7bdccffbb73ab90e9134.jpeg?token=a0fb9842cea35c3fef9e82be0e005a6d&s=F9A58F503EB3469CC02038870300F0A1",
            "https://pics5.baidu.com/feed/bba1cd11728b4710ebff76d80a6b2df9fd032378.jpeg?token=9ed81590a3e25e8a06876d83074059fc&s=5EA6A544801347D44B2C388303003080",
            "https://p0.ssl.img.360kuai.com/t01ab1f68a877024809.jpg?size=640x479",
            "https://p5.ssl.qhimgs1.com/sdr/400__/t01856aec8b4ca19e1f.webp",
            "https://hbimg.huabanimg.com/cad0bf017e9b6e92bb63a5ccd884bbb13df6c3d1b18a-HCbrMJ_fw658",
            "https://p2.ssl.qhimgs1.com/sdr/400__/t019c9569641c081d22.jpg",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t018ce861697da641d6.png",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg",
            "https://p1.ssl.qhimgs1.com/sdr/400__/t014c5cf5fcb4a02091.jpg",
            "https://p0.ssl.img.360kuai.com/t0185821e4d2b4a11ba.jpg?size=546x524",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t01e37e628e97befdd6.webp",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t016304001eb8b977f0.webp",
            "https://p1.ssl.qhimgs1.com/sdr/400__/t01ccca1cf240701f17.webp",
            "https://p0.ssl.qhimgs4.com/t016713273d110ef879.jpg",
            "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2070426602,409573032&fm=175&app=25&f=JPEG?w=640&h=933&s=DA44A908A62B26B575A909820300A086",
            "https://p5.ssl.qhimgs1.com/sdr/400__/t01856aec8b4ca19e1f.webp",
            "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1327775093,1603230616&fm=175&app=25&f=JPG?w=480&h=640&s=7294558CFC33649C2FE548CA030010B6",
            "https://pics4.baidu.com/feed/adaf2edda3cc7cd9feaa7bdccffbb73ab90e9134.jpeg?token=a0fb9842cea35c3fef9e82be0e005a6d&s=F9A58F503EB3469CC02038870300F0A1",
            "https://pics5.baidu.com/feed/bba1cd11728b4710ebff76d80a6b2df9fd032378.jpeg?token=9ed81590a3e25e8a06876d83074059fc&s=5EA6A544801347D44B2C388303003080",
            "https://p0.ssl.img.360kuai.com/t01ab1f68a877024809.jpg?size=640x479",
            "https://p5.ssl.qhimgs1.com/sdr/400__/t01856aec8b4ca19e1f.webp",
            "https://hbimg.huabanimg.com/cad0bf017e9b6e92bb63a5ccd884bbb13df6c3d1b18a-HCbrMJ_fw658",
            "https://p2.ssl.qhimgs1.com/sdr/400__/t019c9569641c081d22.jpg",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t018ce861697da641d6.png",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg",
            "https://p1.ssl.qhimgs1.com/sdr/400__/t014c5cf5fcb4a02091.jpg",
            "https://p0.ssl.img.360kuai.com/t0185821e4d2b4a11ba.jpg?size=546x524",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t01e37e628e97befdd6.webp",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t016304001eb8b977f0.webp",
            "https://p1.ssl.qhimgs1.com/sdr/400__/t01ccca1cf240701f17.webp",
            "https://p0.ssl.qhimgs4.com/t016713273d110ef879.jpg",
            "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2070426602,409573032&fm=175&app=25&f=JPEG?w=640&h=933&s=DA44A908A62B26B575A909820300A086",
            "https://p5.ssl.qhimgs1.com/sdr/400__/t01856aec8b4ca19e1f.webp",
            "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1327775093,1603230616&fm=175&app=25&f=JPG?w=480&h=640&s=7294558CFC33649C2FE548CA030010B6",
            "https://pics4.baidu.com/feed/adaf2edda3cc7cd9feaa7bdccffbb73ab90e9134.jpeg?token=a0fb9842cea35c3fef9e82be0e005a6d&s=F9A58F503EB3469CC02038870300F0A1",
            "https://pics5.baidu.com/feed/bba1cd11728b4710ebff76d80a6b2df9fd032378.jpeg?token=9ed81590a3e25e8a06876d83074059fc&s=5EA6A544801347D44B2C388303003080",
            "https://p0.ssl.img.360kuai.com/t01ab1f68a877024809.jpg?size=640x479",
            "https://p5.ssl.qhimgs1.com/sdr/400__/t01856aec8b4ca19e1f.webp",
            "https://hbimg.huabanimg.com/cad0bf017e9b6e92bb63a5ccd884bbb13df6c3d1b18a-HCbrMJ_fw658",
            "https://p2.ssl.qhimgs1.com/sdr/400__/t019c9569641c081d22.jpg",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t018ce861697da641d6.png",
            "https://p0.ssl.qhimgs1.com/sdr/400__/t01be107caac4f9b78a.jpg",
            "https://p1.ssl.qhimgs1.com/sdr/400__/t014c5cf5fcb4a02091.jpg",
        )

        val adapter = TestImgAdapter(list)
        val curtainView = view.findViewById<CurtainLayout>(R.id.curtain_view)

        curtainView.adapter = adapter
    }
}

/**
 * 测试的数据适配器
 */
class TestImgAdapter(private val data: List<String>) : CurtainAdapter() {

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return if (position == 1 || position == 8) 5 else 6
    }

    override fun onCreateItemView(context: Context, parent: ViewGroup, itemType: Int): View {
        return if (itemType == 5) {
            LayoutInflater.from(context).inflate(R.layout.item_img_2, parent, false)
        } else {
            LayoutInflater.from(context).inflate(R.layout.item_img, parent, false)
        }
    }

    override fun onBindItemView(itemView: View, itemType: Int, position: Int) {

        val imageView = itemView.findViewById<ImageView>(R.id.iv_img)
        val options = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .fitCenter()
//                .placeholder(R.drawable.chengxiao)
//                .error(R.drawable.rocket)
        Glide.with(itemView.context).load(data[position]).apply(options).into(imageView)

    }


}