package com.room.layoutmanagerdemo

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ReplacementSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.room.layoutmanagerdemo.databinding.FragmentSecondBinding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val hello by lazy {
        Hello()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            EventBus.getDefault().post(Message(0, "xxx"))
            EventBus.getDefault().post(EventMessage("xx"))
        }
        val name = "@livehouse12312"
        val str = "不能正常的处理这段文字的名称的断行, 不能,不能12, 她说: $name 不能正常的处理这段文字的名称的断行"
        val nameIndex = str.indexOf(name)
        val sb = SpannableStringBuilder(str)
        val sb2 = SpannableString(str)
        sb2.setSpan(NameSpan(binding.textView), nameIndex, nameIndex + name.length, 0)
        binding.textView.text = sb2
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        hello.start()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
        hello.end()
    }

    @Subscribe
    fun onGetMessage(message: Message) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onGetHello(eventBus: EventMessage) {
        Log.e("xxx", "onGetHello: from SecondFragment")
    }

    data class Message(val id: Int, val msg: String)

    class NameSpan(val textView: TextView) : ReplacementSpan() {

        private lateinit var name: String

        override fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
            name = text.toString().substring(start, end)
            return paint.measureText(name).toInt()
        }

        override fun draw(
            canvas: Canvas,
            text: CharSequence?,
            start: Int,
            end: Int,
            x: Float,
            top: Int,
            y: Int,
            bottom: Int,
            paint: Paint
        ) {
            paint.color = Color.RED
            paint.setShader(
                LinearGradient(
                    x,
                    top.toFloat(),
                    paint.measureText(name) + x,
                    bottom.toFloat(),
                    arrayOf(Color.RED, Color.GREEN).toIntArray(),
                    arrayOf(0F, 1F).toFloatArray(),
                    Shader.TileMode.REPEAT
                )
            )
            canvas.drawText(name, x, y.toFloat(), paint)
        }
    }
}