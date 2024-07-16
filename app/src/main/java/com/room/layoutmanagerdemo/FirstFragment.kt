package com.room.layoutmanagerdemo

import android.graphics.Bitmap
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.room.layoutmanagerdemo.databinding.FragmentFirstBinding
import com.room.layoutmanagerdemo.ui.CustomItemAnimator
import com.room.roomwordsample.adapters.HexagonLayoutManagerAdapter
import com.room.roomwordsample.layout.manager.HexagonLayoutManager


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var x: Int = 0
    private var y: Int = 0
    private var z: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = HexagonLayoutManagerAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = HexagonLayoutManager(9)
        binding.recyclerView.post {
            Log.e("jaesonzhang", "onViewCreated: ${binding.recyclerView.height},${binding.recyclerView.width}")
        }
        binding.recyclerView.itemAnimator = CustomItemAnimator()
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        var i = 0
        val list: MutableList<String> = mutableListOf()
        while (i < 100) {
            i++
            list.add("$i")
        }
        adapter.add(list)
        adapter.notifyDataSetChanged()

        binding.buttonAdd.setOnClickListener {
            i++
            adapter.add("$i")
        }
        binding.buttonMove.setOnClickListener {
            adapter.move(0, 6)
        }
        binding.buttonRemove.setOnClickListener {
            adapter.remove(30)
        }

        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                x = progress
                processBar()
            }


            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit


            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })

        binding.seekBar2.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                y = progress
                processBar()
            }


            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit


            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })

        binding.seekBar3.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                z = progress
                processBar()
            }


            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit


            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })
        binding.seekBar4.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.navigationView.setAngle(progress)
            }


            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit


            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun processBar() {
        val originalBitmap = binding.imageView.drawToBitmap()
        // 创建一个新的Bitmap来存储旋转后的图像
        val rotatedBitmap = rotateBitmap(originalBitmap, x.toFloat(), y.toFloat(), z.toFloat())
        binding.imageView2.setImageBitmap(rotatedBitmap)

    }

    fun rotateBitmap(bitmap: Bitmap, rotateX: Float, rotateY: Float, rotateZ: Float): Bitmap {
        val rotatedBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)

        // 创建一个Canvas对象，并将新的Bitmap关联起来
        val canvas = Canvas(rotatedBitmap)

        // 中心点坐标
        val centerX = bitmap.width / 2f
        val centerY = bitmap.height / 2f

        // 创建Camera对象
        val camera = Camera()

        // 创建Matrix对象
        val matrix = Matrix()

        // 保存Camera状态
        camera.save()
        // 旋转Camera的三个轴
        camera.rotateX(rotateX)
        camera.rotateY(rotateY)
        camera.rotateZ(rotateZ)
        // 将Camera的变换应用到Matrix上
        camera.getMatrix(matrix)
        camera.restore()

        // 调整Matrix的中心点
        matrix.preTranslate(-centerX, -centerY)
        matrix.postTranslate(centerX, centerY)

        // 绘制旋转后的Bitmap
        canvas.drawBitmap(bitmap, matrix, Paint())

        return rotatedBitmap
    }
}