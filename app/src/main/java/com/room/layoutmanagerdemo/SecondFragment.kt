package com.room.layoutmanagerdemo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            EventBus.getDefault().post(Message(0, "xxx"))
            EventBus.getDefault().post(EventMessage("xx"))
        }
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
}