package com.room.layoutmanagerdemo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.room.layoutmanagerdemo.databinding.FragmentFirstBinding
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
            adapter.move(0, 9)
        }
        binding.buttonRemove.setOnClickListener {
            adapter.remove(30)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}